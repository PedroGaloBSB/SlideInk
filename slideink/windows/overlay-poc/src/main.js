const { app, BrowserWindow, screen } = require('electron');
const path = require('path');

let mainWindow;
let overlayWindow;

function createMainWindow() {
  const primaryDisplay = screen.getPrimaryDisplay();
  const { width, height } = primaryDisplay.workAreaSize;

  mainWindow = new BrowserWindow({
    width: width,
    height: height,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
    title: 'SlideInk - PDF Viewer',
  });

  mainWindow.loadFile(path.join(__dirname, 'index.html'));
  
  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

function createOverlayWindow() {
  const primaryDisplay = screen.getPrimaryDisplay();
  const { width, height } = primaryDisplay.workAreaSize;

  overlayWindow = new BrowserWindow({
    width: width,
    height: height,
    x: 0,
    y: 0,
    transparent: true,
    frame: false,
    alwaysOnTop: true,
    skipTaskbar: true,
    focusable: false,
    hasShadow: false,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
  });

  overlayWindow.setIgnoreMouseEvents(true, { forward: true });
  overlayWindow.loadFile(path.join(__dirname, 'overlay.html'));
  
  overlayWindow.on('closed', () => {
    overlayWindow = null;
  });
}

app.whenReady().then(() => {
  createMainWindow();
  createOverlayWindow();

  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  overlayWindow.once('ready-to-show', () => {
    overlayWindow.show();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createMainWindow();
    createOverlayWindow();
  }
});

// IPC para receber comandos de anotação
const { ipcMain } = require('electron');

ipcMain.on('clear-annotations', () => {
  if (overlayWindow) {
    overlayWindow.webContents.send('clear-annotations');
  }
});

ipcMain.on('add-stroke', (event, stroke) => {
  if (overlayWindow) {
    overlayWindow.webContents.send('add-stroke', stroke);
  }
});
