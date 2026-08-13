# Estrutura do Projeto SlideInk

```
slideink/
├── README.md                    # Documentação geral do projeto
├── docs/                        # Documentação consolidada
│   └── SLIDEINK_DOCUMENTO_CONSOLIDADO.md
│
├── pocs/                        # Proofs of Concept
│   ├── overlay-poc/             # POC 1: Overlay (Windows)
│   ├── android-windows-poc/     # POC 2: Comunicação Android → Windows
│   ├── sharing-poc/             # POC 3: Compartilhamento (Zoom/Meet/Teams)
│   └── bluetooth-poc/           # POC 5: Bluetooth
│
├── windows/                     # Aplicação Windows
│   ├── overlay-poc/             # POC 1 - Overlay (atual)
│   │   ├── package.json
│   │   ├── README.md
│   │   └── src/
│   │       ├── main.js          # Electron main process
│   │       └── index.html       # UI com PDF viewer + overlay
│   │
│   └── app/                     # Aplicação principal (futuro)
│       ├── src/
│       │   ├── main/            # Electron main process
│       │   ├── renderer/        # UI renderer
│       │   ├── transport/       # Camada de transporte
│       │   └── pdf/             # Renderização de PDF
│       └── package.json
│
└── android/                     # Aplicação Android (futuro)
    ├── app/
    │   └── src/
    │       ├── main/
    │       │   ├── java/        # Código Kotlin
    │       │   └── res/         # Recursos
    │       └── test/            # Testes
    └── build.gradle
```

## Status Atual

### ✅ Concluído
- [x] Documento consolidado do produto
- [x] Estrutura de diretórios inicial
- [x] POC 1 — Overlay (Windows): Código base criado

### 🚧 Em Desenvolvimento
- [ ] POC 1 — Overlay: Testes de compartilhamento no Zoom/Meet/Teams
- [ ] Protocolo de comunicação entre Android e Windows

### 📋 Pendente
- [ ] POC 2 — Comunicação Android → Windows
- [ ] POC 3 — Testes de compartilhamento nas plataformas
- [ ] POC 4 — Performance com PDFs grandes
- [ ] POC 5 — Bluetooth
- [ ] Aplicação Android MVP
- [ ] Aplicação Windows MVP
- [ ] Transport Layer (USB, Wi-Fi)
- [ ] Failover entre transportes

## Próximos Passos Imediatos

1. **Testar POC 1 do Overlay**
   - Instalar dependências (`npm install`)
   - Executar (`npm start`)
   - Testar compartilhamento no Zoom
   - Testar compartilhamento no Google Meet
   - Testar compartilhamento no Teams
   - Documentar resultados

2. **Se POC 1 for bem-sucedida:**
   - Implementar WebSocket server no Windows
   - Criar interface básica Android
   - Implementar protocolo de comunicação
   - Testar envio de comandos (próximo/anterior)
   - Testar envio de strokes

3. **Se POC 1 falhar:**
   - Investigar causa raiz
   - Avaliar alternativas (ex: WPF, WinUI3)
   - Decidir se continua ou pivoteia

## Princípios de Desenvolvimento

Seguindo o documento consolidado:

1. **KISS**: Começar simples, apenas o necessário para validar hipóteses
2. **Fail Fast**: Identificar problemas cedo, especialmente no overlay
3. **Separação de Concerns**: Interface não conhece detalhes de transporte
4. **Não construir complexidade desnecessária**: Sem login, nuvem, IA, etc.

## Tecnologias

### Windows (POC Atual)
- Electron
- TypeScript/JavaScript
- PDF.js
- Canvas API

### Android (Futuro)
- Kotlin
- Jetpack Compose
- Canvas
- WebSocket client

### Comunicação
- WebSocket (Wi-Fi)
- USB serial
- Bluetooth (BLE + RFCOMM)

---

**Última atualização:** 13 de agosto de 2026  
**Versão:** 0.1.0
