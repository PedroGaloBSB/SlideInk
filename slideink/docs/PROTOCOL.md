# Protocolo de Comunicação SlideInk

## Visão Geral

Este documento define o protocolo de comunicação entre o aplicativo Android e o aplicativo Windows do SlideInk.

## Princípios

1. **Independência de Transporte**: O protocolo funciona sobre USB, Wi-Fi ou Bluetooth
2. **Simplicidade**: Mensagens em texto claro, fáceis de debugar
3. **Stateless**: Cada mensagem contém informação suficiente para ser processada
4. **Extensibilidade**: Novos comandos podem ser adicionados sem quebrar compatibilidade

## Formato das Mensagens

### Estrutura Básica

```
<TIPO>:<PAYLOAD>
```

Exemplos:
```
NEXT_SLIDE
PREVIOUS_SLIDE
GOTO_SLIDE:15
STROKE_START:1:100:200
STROKE_POINT:1:105:205
STROKE_END:1:110:210
CLEAR_ANNOTATIONS
```

## Comandos de Controle de Apresentação

| Comando | Payload | Descrição |
|---------|---------|-----------|
| `NEXT_SLIDE` | nenhum | Avança para o próximo slide |
| `PREVIOUS_SLIDE` | nenhum | Volta para o slide anterior |
| `GOTO_SLIDE` | número da página | Vai para uma página específica |
| `START_PRESENTATION` | nenhum | Inicia a apresentação |
| `END_PRESENTATION` | nenhum | Encerra a apresentação |
| `LASER_ON` | nenhum | Ativa o apontador laser |
| `LASER_OFF` | nenhum | Desativa o apontador laser |
| `LASER_MOVE` | x:y | Move o laser para a posição (x, y) |

## Comandos de Anotação

### Sistema de Coordenadas

As coordenadas são normalizadas (0-1000) para independência de resolução:
- (0, 0) = canto superior esquerdo
- (1000, 1000) = canto inferior direito

### Tipos de Caneta

| ID | Tipo | Descrição |
|----|------|-----------|
| 1 | CANETA | Caneta padrão |
| 2 | MARCADOR | Marcador transparente |
| 3 | BORRACHA | Borracha |

### Comandos de Desenho

| Comando | Payload | Descrição |
|---------|---------|-----------|
| `SET_PEN` | tipo:cor:tamanho | Configura a caneta ativa |
| `STROKE_START` | id:x:y | Inicia um novo traço |
| `STROKE_POINT` | id:x:y | Adiciona ponto ao traço |
| `STROKE_END` | id:x:y | Finaliza o traço |
| `CLEAR_ANNOTATIONS` | nenhum | Limpa todas as anotações |
| `UNDO_STROKE` | nenhum | Desfaz último traço |

### Exemplo de Sequência de Desenho

```
// Configurar caneta azul, tamanho 5
SET_PEN:1:#0000FF:5

// Iniciar traço na posição (100, 150)
STROKE_START:1:100:150

// Adicionar pontos intermediários
STROKE_POINT:1:105:155
STROKE_POINT:1:110:160
STROKE_POINT:1:115:165

// Finalizar traço na posição (120, 170)
STROKE_END:1:120:170
```

## Comandos de Estado

### Android → Windows

| Comando | Payload | Descrição |
|---------|---------|-----------|
| `CONNECT_REQUEST` | device_name | Solicita conexão |
| `DISCONNECT` | nenhum | Desconecta |
| `PING` | timestamp | Teste de latência |
| `BATTERY_LEVEL` | porcentagem | Nível da bateria |

### Windows → Android

| Comando | Payload | Descrição |
|---------|---------|-----------|
| `CONNECT_ACCEPTED` | session_id | Conexão aceita |
| `CONNECT_REJECTED` | reason | Conexão rejeitada |
| `PONG` | timestamp | Resposta ao ping |
| `CURRENT_PAGE` | número | Página atual |
| `TOTAL_PAGES` | número | Total de páginas |
| `CONNECTION_STATUS` | status | Status da conexão |

## Códigos de Erro

| Código | Descrição |
|--------|-----------|
| `ERR_UNKNOWN_COMMAND` | Comando não reconhecido |
| `ERR_INVALID_PAYLOAD` | Payload inválido |
| `ERR_PAGE_OUT_OF_RANGE` | Página fora do intervalo |
| `ERR_CONNECTION_FAILED` | Falha na conexão |
| `ERR_TIMEOUT` | Timeout na operação |

## Implementação de Referência

### Envio (Android)

```kotlin
fun sendCommand(command: String) {
    transport.send(command)
}

fun nextSlide() {
    sendCommand("NEXT_SLIDE")
}

fun drawStroke(points: List<Point>) {
    val strokeId = System.currentTimeMillis()
    sendCommand("STROKE_START:$strokeId:${points[0].x}:${points[0].y}")
    
    for (i in 1 until points.size - 1) {
        sendCommand("STROKE_POINT:$strokeId:${points[i].x}:${points[i].y}")
    }
    
    val last = points.last()
    sendCommand("STROKE_END:$strokeId:${last.x}:${last.y}")
}
```

### Recebimento (Windows)

```javascript
function handleMessage(message) {
    const [type, ...payloadParts] = message.split(':');
    const payload = payloadParts.join(':');
    
    switch (type) {
        case 'NEXT_SLIDE':
            goToPage(currentPage + 1);
            break;
        case 'PREVIOUS_SLIDE':
            goToPage(currentPage - 1);
            break;
        case 'GOTO_SLIDE':
            goToPage(parseInt(payload));
            break;
        case 'STROKE_START':
            const [id, x, y] = payload.split(':');
            startStroke(id, x, y);
            break;
        // ... outros casos
    }
}
```

## Versionamento

O protocolo inclui opcionalmente versão no handshake inicial:

```
PROTOCOL_VERSION:1.0
```

Versões futuras devem manter compatibilidade com comandos existentes.

## Segurança (Futuro)

Para versões futuras, considerar:
- Token de autenticação por sessão
- Criptografia TLS para Wi-Fi
- Pairing seguro para Bluetooth

---

**Versão do Protocolo:** 1.0  
**Data:** 13 de agosto de 2026  
**Status:** Em desenvolvimento
