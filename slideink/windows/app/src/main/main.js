/**
 * SlideInk - Windows Application (Electron)
 * 
 * Implementa conforme Documento Consolidado:
 * - Apresentação de PDF
 * - Overlay transparente para anotações
 * - WebSocket server para comunicação com Android
 * - Integração com Zoom/Meet/Teams via compartilhamento de tela
 */

const { app, BrowserWindow, screen, ipcMain } = require('electron');
const path = require('path');
const WebSocket = require('ws');
const express = require('express');

// Configurações globais
let presentationWindow = null;
let overlayWindow = null;
let wss = null;
let currentPage = 1;
let totalPages = 0;
let strokes = []; // Armazena anotações atuais

// Porta do servidor WebSocket
const WS_PORT = process.env.SLIDEINK_WS_PORT || 8765;

function createPresentationWindow() {
    const { width, height } = screen.getPrimaryDisplay().workAreaSize;
    
    presentationWindow = new BrowserWindow({
        width: width,
        height: height,
        fullscreen: true,
        frame: false,
        transparent: false,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
            preload: path.join(__dirname, 'preload.js')
        },
        alwaysOnTop: false,
        skipTaskbar: false
    });
    
    presentationWindow.loadFile(path.join(__dirname, 'renderer', 'presentation.html'));
    
    presentationWindow.on('closed', () => {
        presentationWindow = null;
    });
}

function createOverlayWindow() {
    const { width, height } = screen.getPrimaryDisplay().workAreaSize;
    
    overlayWindow = new BrowserWindow({
        width: width,
        height: height,
        x: 0,
        y: 0,
        frame: false,
        transparent: true,
        hasShadow: false,
        skipTaskbar: true,
        alwaysOnTop: true,
        fullscreenable: false,
        resizable: false,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    });
    
    // Torna a janela click-through (permite clicar através dela)
    overlayWindow.setIgnoreMouseEvents(true, { forward: true });
    
    overlayWindow.loadFile(path.join(__dirname, 'renderer', 'overlay.html'));
    
    overlayWindow.on('closed', () => {
        overlayWindow = null;
    });
}

function startWebSocketServer() {
    wss = new WebSocket.Server({ port: WS_PORT });
    
    wss.on('connection', (ws) => {
        console.log('[WS] Cliente Android conectado');
        
        // Envia estado atual para o cliente
        sendCurrentState(ws);
        
        ws.on('message', (message) => {
            try {
                const data = JSON.parse(message);
                handleMessage(data, ws);
            } catch (error) {
                console.error('[WS] Erro ao processar mensagem:', error);
            }
        });
        
        ws.on('close', () => {
            console.log('[WS] Cliente Android desconectado');
        });
        
        ws.on('error', (error) => {
            console.error('[WS] Erro:', error);
        });
    });
    
    console.log(`[WS] Servidor WebSocket rodando na porta ${WS_PORT}`);
}

function handleMessage(data, ws) {
    const { type, payload, timestamp } = data;
    
    console.log(`[WS] Recebido: ${type}`, payload);
    
    switch (type) {
        case 'next_slide':
            nextPage();
            break;
            
        case 'previous_slide':
            previousPage();
            break;
            
        case 'goto_slide':
            goToPage(payload.page);
            break;
            
        case 'stroke_start':
        case 'stroke_point':
        case 'stroke_end':
            // Processa anotação
            handleStroke(data);
            break;
            
        case 'clear_annotations':
            clearAllStrokes();
            break;
            
        case 'undo_stroke':
            undoLastStroke();
            break;
            
        case 'laser_on':
        case 'laser_off':
        case 'laser_move':
            // Processa laser
            handleLaser(data);
            break;
            
        case 'heartbeat':
            // Responde ao heartbeat
            ws.send(JSON.stringify({
                type: 'heartbeat',
                timestamp: Date.now(),
                payload: { latency: Date.now() - timestamp }
            }));
            break;
            
        default:
            console.warn(`[WS] Tipo de mensagem desconhecido: ${type}`);
    }
}

function handleStroke(data) {
    // Adiciona stroke ao array
    if (data.type === 'stroke_start') {
        strokes.push({
            id: data.payload.strokeId,
            points: [],
            color: parseInt(data.payload.color),
            size: parseFloat(data.payload.size),
            tool: data.payload.tool
        });
    } else if (data.type === 'stroke_point') {
        const stroke = strokes.find(s => s.id === data.payload.strokeId);
        if (stroke) {
            const points = data.payload.points.split(';');
            points.forEach(pointStr => {
                const [x, y, pressure] = pointStr.split(',').map(Number);
                stroke.points.push({ x, y, pressure });
            });
        }
    }
    
    // Envia para overlay
    if (overlayWindow) {
        overlayWindow.webContents.send('stroke-update', data);
    }
}

function handleLaser(data) {
    if (overlayWindow) {
        overlayWindow.webContents.send('laser-update', data);
    }
}

function sendCurrentState(ws) {
    // Envia informações da página atual
    ws.send(JSON.stringify({
        type: 'page_info',
        payload: {
            currentPage,
            totalPages
        }
    }));
    
    // Envia todas as strokes atuais
    strokes.forEach(stroke => {
        ws.send(JSON.stringify({
            type: 'stroke_sync',
            payload: stroke
        }));
    });
}

function nextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        updatePresentation();
    }
}

function previousPage() {
    if (currentPage > 1) {
        currentPage--;
        updatePresentation();
    }
}

function goToPage(page) {
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        updatePresentation();
    }
}

function updatePresentation() {
    if (presentationWindow) {
        presentationWindow.webContents.send('page-change', {
            currentPage,
            totalPages
        });
    }
    
    // Notifica Android sobre mudança de página
    broadcastMessage({
        type: 'page_changed',
        payload: {
            current: currentPage,
            total: totalPages
        }
    });
}

function clearAllStrokes() {
    strokes = [];
    if (overlayWindow) {
        overlayWindow.webContents.send('clear-all');
    }
}

function undoLastStroke() {
    if (strokes.length > 0) {
        const removed = strokes.pop();
        if (overlayWindow) {
            overlayWindow.webContents.send('undo-stroke', { strokeId: removed.id });
        }
    }
}

function broadcastMessage(message) {
    if (wss && wss.clients) {
        wss.clients.forEach(client => {
            if (client.readyState === WebSocket.OPEN) {
                client.send(JSON.stringify(message));
            }
        });
    }
}

// Inicialização
app.whenReady().then(() => {
    console.log('[SlideInk] Iniciando aplicação...');
    
    startWebSocketServer();
    createPresentationWindow();
    createOverlayWindow();
    
    // Carrega PDF de exemplo
    setTimeout(() => {
        if (presentationWindow) {
            presentationWindow.webContents.send('load-pdf', {
                path: path.join(__dirname, '..', '..', 'sample.pdf'),
                page: 1
            });
        }
    }, 1000);
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        createPresentationWindow();
        createOverlayWindow();
    }
});

// IPC handlers
ipcMain.on('get-current-page', (event) => {
    event.returnValue = { currentPage, totalPages };
});

ipcMain.on('set-total-pages', (event, count) => {
    totalPages = count;
    broadcastMessage({
        type: 'page_info',
        payload: { currentPage, totalPages }
    });
});

console.log('[SlideInk] Aplicação inicializada');
