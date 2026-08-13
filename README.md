# SlideInk

**Aplicação de apresentação e anotação em tempo real para apresentações remotas**

SlideInk é uma ferramenta que permite apresentar slides do seu computador enquanto faz anotações em tempo real usando um dispositivo Android como controle remoto. As anotações aparecem sobre os slides e são visíveis para todos os participantes em videoconferências (Zoom, Google Meet, Microsoft Teams).

## 🎯 Problema que Resolve

Durante apresentações remotas:
- É difícil fazer anotações sobre slides enquanto se apresenta
- Alternar entre janelas quebra o fluxo da apresentação
- Participantes não veem suas anotações quando você compartilha a tela

## ✨ Solução

SlideInk cria:
1. **Overlay transparente** no Windows que mostra seus slides + anotações
2. **Controle remoto Android** que permite navegar slides e desenhar
3. **Sincronização em tempo real** via Wi-Fi, USB ou Bluetooth

## 📦 Estrutura do Projeto

```
/workspace/
├── README.md                          # Este arquivo
├── RESUMO_FINAL.md                    # Resumo da implementação inicial
├── SLIDEINK_DOCUMENTO_CONSOLIDADO.md  # Documento mestre completo (34 seções)
└── slideink/
    ├── README.md                      # Estrutura detalhada do projeto
    ├── NEXT_STEPS.md                  # Próximos passos
    ├── docs/                          # Documentação técnica
    │   ├── SLIDEINK_DOCUMENTO_CONSOLIDADO.md
    │   ├── PROTOCOL.md                # Protocolo Android ↔ Windows
    │   └── ROADMAP.md                 # Roadmap até o MVP
    ├── windows/                       # Aplicação Windows
    │   └── overlay-poc/               # POC 1 - Overlay (pronta para teste)
    │       ├── src/
    │       │   ├── main.js            # Electron app
    │       │   └── index.html         # Interface + PDF + Overlay
    │       ├── package.json
    │       ├── README.md
    │       └── TESTING_GUIDE.md
    └── android/                       # Aplicação Android (futuro)
```

## 🚀 Começando Rápido

### Pré-requisitos

- **Windows 10/11** (para testar a POC atual)
- **Node.js 16+** instalado
- **PDF de exemplo** para testes

### Executar a POC do Overlay

```bash
cd slideink/windows/overlay-poc
npm install
npm start
```

**O que acontece:**
1. Duas janelas abrem (viewer de PDF + overlay transparente)
2. Um PDF de exemplo é carregado
3. Você pode navegar entre páginas
4. Botões de teste desenham formas no overlay
5. Você pode desenhar livremente com o mouse

## 🧪 Teste Crítico

Após rodar a POC, valide o **risco principal do projeto**:

1. Abra Zoom, Google Meet ou Teams
2. Inicie uma reunião (pode ser sozinho)
3. Compartilhe a tela inteira
4. **Verifique:** As anotações aparecem na prévia da plataforma?

**Se funcionar em 2+ plataformas:** ✅ POC aprovada → Continue desenvolvimento  
**Se não funcionar:** ❌ POC reprovada → Investigue alternativas

Guia completo: `slideink/windows/overlay-poc/TESTING_GUIDE.md`

## 📊 Status Atual

| Fase | Nome | Status | Progresso |
|------|------|--------|-----------|
| 0 | Descoberta | ✅ Concluída | 100% |
| 1 | Overlay | 🟡 Código pronto, testes pendentes | 80% |
| 2 | Compartilhamento | ⏳ Pendente | 0% |
| 3 | Comunicação | ⏳ Pendente | 0% |
| 4 | Anotação | ⏳ Pendente | 0% |
| 5 | MVP | ⏳ Pendente | 0% |

**Progresso total até MVP:** ~15%

## 🛠️ Tecnologias

### Windows (POC Atual)
- Electron
- JavaScript/TypeScript
- PDF.js
- Canvas API

### Android (Futuro)
- Kotlin
- Jetpack Compose
- WebSocket client

### Comunicação
- WebSocket (Wi-Fi)
- USB serial
- Bluetooth (BLE + RFCOMM)

## 📋 Princípios de Desenvolvimento

1. **KISS** - Começar simples, apenas o necessário para validar hipóteses
2. **Fail Fast** - Identificar problemas cedo, especialmente no overlay
3. **Separação de Concerns** - Interface não conhece detalhes de transporte
4. **Sem complexidade desnecessária** - Sem login, nuvem, IA, etc.

## 📚 Documentação

| Arquivo | Descrição |
|---------|-----------|
| `RESUMO_FINAL.md` | Resumo executivo da implementação inicial |
| `SLIDEINK_DOCUMENTO_CONSOLIDADO.md` | Documento mestre completo com problema, solução, arquitetura |
| `slideink/README.md` | Estrutura detalhada do projeto e status |
| `slideink/NEXT_STEPS.md` | Próximos passos imediatos |
| `slideink/docs/PROTOCOL.md` | Protocolo de comunicação Android ↔ Windows |
| `slideink/docs/ROADMAP.md` | Roadmap em 8 fases até o MVP |
| `slideink/windows/overlay-poc/README.md` | Como executar a POC do overlay |
| `slideink/windows/overlay-poc/TESTING_GUIDE.md` | Guia passo-a-passo para testes |

## 🎯 Próxima Ação

**Tempo estimado:** 2-3 horas

1. Execute a POC no Windows (30 min)
2. Teste funcionalidades locais (15 min)
3. Teste no Zoom (30 min)
4. Teste no Google Meet (30 min)
5. Teste no Microsoft Teams (30 min)
6. Documente resultados (15 min)

Leia primeiro: `slideink/NEXT_STEPS.md`

## ⚠️ Riscos Principais

1. **Overlay não capturado** - Plataformas de videoconferência podem não capturar o overlay transparente
2. **Latência** - Atraso entre comando Android e ação no Windows
3. **Performance** - PDFs grandes podem causar lentidão

## 🤝 Contribuindo

Este projeto segue uma abordagem lean startup:
- Validar hipóteses críticas primeiro
- Construir apenas o necessário para o MVP
- Iterar rapidamente baseado em feedback

## 📄 Licença

[Informações de licença]

---

**Última atualização:** 13 de agosto de 2026  
**Versão:** 0.1.0  
**Status:** Aguardando validação da POC 1 (Overlay)
