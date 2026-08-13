# Guia de Testes da POC 1 — Overlay

## 🎯 Objetivo do Teste

Validar se o overlay criado com Electron é capturado corretamente pelas plataformas de videoconferência (Zoom, Google Meet, Microsoft Teams).

## 📋 Pré-requisitos

- Windows 10 ou superior
- Node.js instalado (v18+)
- Zoom instalado
- Google Chrome ou Edge
- Microsoft Teams instalado
- Conta de teste em cada plataforma (opcional, pode testar sozinho)

## 🚀 Executando a POC

### Passo 1: Instalar dependências

```bash
cd windows/overlay-poc
npm install
```

### Passo 2: Executar a aplicação

```bash
npm start
```

A aplicação abrirá duas janelas:
1. **Janela Principal**: Visualizador de PDF com toolbar
2. **Janela Overlay**: Camada transparente sobreposta

### Passo 3: Verificar funcionamento básico

Antes de testar nas plataformas, valide:

- [ ] O PDF foi carregado corretamente
- [ ] Os botões "Anterior" e "Próximo" funcionam
- [ ] O botão "Desenhar Linha" cria uma linha vermelha
- [ ] O botão "Desenhar Círculo" cria um círculo azul
- [ ] O botão "Escrever Texto" escreve texto verde
- [ ] O botão "Modo Desenho Livre" permite desenhar com o mouse
- [ ] O botão "Limpar Anotações" remove todos os desenhos

## 🧪 Teste nas Plataformas

### Teste no Zoom

1. Abra o Zoom
2. Inicie uma reunião (pode ser sozinho)
3. Clique em "Compartilhar Tela"
4. Teste cada modalidade:

#### Modalidade A: Tela Inteira
- Selecione "Tela 1" ou "Tela Inteira"
- [ ] O overlay aparece para você na prévia?
- [ ] Peça para alguém entrar na reunião e confirmar se vê
- [ ] Anote: Funciona / Não funciona / Parcial

#### Modalidade B: Janela Específica
- Selecione a janela do SlideInk
- [ ] O overlay aparece na prévia?
- [ ] Alguém confirma que vê?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Modalidade C: Área Selecionada
- Selecione "Avançado" → "Porção da tela"
- Selecione a área do PDF + overlay
- [ ] O overlay aparece?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Observações Zoom:
```
_________________________________________________
_________________________________________________
_________________________________________________
```

---

### Teste no Google Meet

1. Abra o Chrome ou Edge
2. Acesse https://meet.google.com
3. Inicie uma reunião (pode ser sozinho)
4. Clique em "Apresentar agora"
5. Teste cada modalidade:

#### Modalidade A: Tela Inteira
- Selecione "Tela inteira"
- [ ] O overlay aparece na prévia?
- [ ] Alguém confirma que vê?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Modalidade B: Janela
- Selecione "Uma janela"
- Escolha a janela do SlideInk
- [ ] O overlay aparece?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Observações Meet:
```
_________________________________________________
_________________________________________________
_________________________________________________
```

---

### Teste no Microsoft Teams

1. Abra o Teams
2. Inicie uma reunião (pode ser sozinho)
3. Clique em "Compartilhar"
4. Teste cada modalidade:

#### Modalidade A: Tela Inteira
- Selecione "Tela"
- [ ] O overlay aparece?
- [ ] Alguém confirma que vê?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Modalidade B: Janela
- Selecione "Janela"
- Escolha a janela do SlideInk
- [ ] O overlay aparece?
- [ ] Anote: Funciona / Não funciona / Parcial

#### Observações Teams:
```
_________________________________________________
_________________________________________________
_________________________________________________
```

---

## 📊 Registro de Resultados

Preencha a tabela abaixo:

| Plataforma | Tela Inteira | Janela | Área | Observações |
|------------|-------------|--------|------|-------------|
| Zoom       |             |        |      |             |
| Meet       |             |        |      |             |
| Teams      |             |        |      |             |

### Legenda:
- ✅ Funciona perfeitamente
- ⚠️ Funciona com limitações
- ❌ Não funciona

---

## 🐛 Problemas Comuns e Soluções

### Problema: Overlay não aparece em nenhuma plataforma

**Possíveis causas:**
1. Aceleração de hardware está interferindo
2. O overlay não está realmente sobre o PDF

**Soluções:**
1. No Electron, tente desativar aceleração de hardware:
   ```javascript
   app.disableHardwareAcceleration();
   ```
2. Verifique se `alwaysOnTop: true` está configurado
3. Confirme que o canvas do overlay tem `pointer-events: none`

### Problema: Overlay aparece mas é transparente demais

**Solução:**
- Aumente a opacidade das cores no canvas
- Use cores mais vibrantes para testes

### Problema: Funciona em uma plataforma mas não em outra

**Ação:**
- Documente qual plataforma funciona
- Isso já é um bom sinal (significa que a abordagem é viável)
- Foque nas plataformas que funcionam para o MVP

---

## ✅ Critérios de Sucesso

A POC será considerada **APROVADA** se:

1. ✅ O overlay for visível em pelo menos 2 plataformas
2. ✅ Funcionar ao compartilhar tela inteira (mais comum para professores)
3. ✅ As anotações forem nítidas e legíveis
4. ✅ A navegação do PDF não for prejudicada

A POC será considerada **REPROVADA** se:

1. ❌ O overlay não for capturado em nenhuma plataforma
2. ❌ Apenas funcionar em condições muito específicas
3. ❌ A performance for inaceitável (lag > 200ms)

---

## 📝 Próximos Passos

### Se APROVADA:
- [ ] Implementar WebSocket server no Windows
- [ ] Criar app Android básico
- [ ] Implementar protocolo de comunicação
- [ ] Testar envio de comandos em tempo real
- [ ] Avançar para POC 2

### Se REPROVADA:
- [ ] Investigar causa raiz do problema
- [ ] Pesquisar alternativas (WPF, WinUI3, native Windows)
- [ ] Consultar documentação do Electron sobre transparência
- [ ] Considerar pivotar a abordagem técnica

---

## 📞 Contato e Suporte

Em caso de dúvidas durante os testes, documente:
1. Sistema operacional e versão
2. Versão do Zoom/Meet/Teams
3. Screenshots do problema
4. Logs do console (F12 → Console)

---

**Data do teste:** ___/___/______  
**Responsável pelo teste:** _______________________  
**Resultado:** APROVADO / REPROVADO / PARCIAL  
**Observações finais:**
```
_________________________________________________
_________________________________________________
_________________________________________________
```
