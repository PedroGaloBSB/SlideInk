# POC 1 — Overlay

## Objetivo

Provar que é possível desenhar uma camada transparente sobre a apresentação de PDF no Windows que seja capturada corretamente pelo Zoom, Google Meet e Microsoft Teams durante o compartilhamento de tela.

## Hipótese

> É possível criar um overlay transparente usando Electron que seja capturado pelas plataformas de videoconferência quando o usuário compartilha:
> - A tela inteira
> - Uma janela específica
> - Uma área selecionada

## Estrutura

```
overlay-poc/
├── package.json          # Dependências e scripts
├── src/
│   ├── main.js           # Processo principal do Electron
│   └── index.html        # Interface com PDF viewer + overlay
```

## Como Executar

### Pré-requisitos
- Node.js (versão 18 ou superior)
- npm

### Passos

1. Navegue até o diretório da POC:
```bash
cd windows/overlay-poc
```

2. Instale as dependências:
```bash
npm install
```

3. Execute a aplicação:
```bash
npm start
```

## Testes a Realizar

### Teste 1 — Renderização do Overlay
- [ ] O canvas de overlay aparece sobre o PDF?
- [ ] As anotações são desenhadas corretamente?
- [ ] O overlay é transparente (permite ver o PDF abaixo)?

### Teste 2 — Compartilhamento no Zoom
- [ ] Compartilhar tela inteira → overlay visível?
- [ ] Compartilhar janela → overlay visível?
- [ ] Compartilhar área → overlay visível?

### Teste 3 — Compartilhamento no Google Meet
- [ ] Compartilhar tela inteira → overlay visível?
- [ ] Compartilhar janela → overlay visível?
- [ ] Compartilhar área → overlay visível?

### Teste 4 — Compartilhamento no Microsoft Teams
- [ ] Compartilhar tela inteira → overlay visível?
- [ ] Compartilhar janela → overlay visível?
- [ ] Compartilhar área → overlay visível?

### Teste 5 — Funcionalidades Básicas
- [ ] Navegação entre páginas do PDF funciona?
- [ ] Botão "Limpar Anotações" funciona?
- [ ] Desenho livre funciona?
- [ ] Formas pré-definidas (linha, círculo, texto) funcionam?

## Critérios de Sucesso

A POC será considerada **bem-sucedida** se:

1. ✅ O overlay for visível em pelo menos 2 das 3 plataformas testadas (Zoom, Meet, Teams)
2. ✅ As anotações forem capturadas ao compartilhar a tela inteira
3. ✅ A navegação do PDF não for prejudicada pelo overlay
4. ✅ A latência percebida for aceitável (< 100ms para desenho)

## Próximos Passos (se bem-sucedida)

1. Integrar com WebSocket para receber comandos do Android
2. Implementar protocolo de comunicação para strokes
3. Adicionar suporte a USB como transporte
4. Expandir para POC 2 (Android → Windows)

## Riscos Identificados

1. **Risco Crítico**: O overlay pode não ser capturado devido à aceleração de hardware
   - Mitigação: Testar com aceleração de hardware desativada
   
2. **Risco Moderado**: Transparência pode não funcionar como esperado
   - Mitigação: Usar `transparent: true` e `frame: false` no Electron

3. **Risco Baixo**: Performance em PDFs grandes
   - Mitigação: Implementar renderização sob demanda

## Registro de Resultados

| Data | Plataforma | Tela Inteira | Janela | Área | Observações |
|------|-----------|--------------|--------|------|-------------|
|      | Zoom      |              |        |      |             |
|      | Meet      |              |        |      |             |
|      | Teams     |              |        |      |             |

## Notas Técnicas

- O overlay usa `setIgnoreMouseEvents(true, { forward: true })` para permitir interação com o PDF abaixo
- O canvas do overlay é redimensionado automaticamente quando o PDF é renderizado
- IPC do Electron é usado para comunicação entre processos quando integrado

---

**Status:** Em desenvolvimento  
**Responsável:** Desenvolvedor Solo  
**Data de Criação:** 13 de agosto de 2026
