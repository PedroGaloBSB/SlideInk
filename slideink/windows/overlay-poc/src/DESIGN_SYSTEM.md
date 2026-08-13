# SlideInk Design System - Documentação

## Visão Geral

O **SlideInk Design System** é um sistema de design minimalista e profissional criado especificamente para ferramentas de apresentação em tempo real. 

### Princípios Fundamentais

1. **Invisibilidade Ativa**: A interface desaparece durante a aula, aparecendo apenas quando necessária
2. **Foco no Conteúdo**: O slide é sempre o protagonista
3. **Profissionalismo**: Transmite confiança, estabilidade e baixa distração
4. **Minimalismo Funcional**: Apenas o necessário, quando necessário

### Inspirações

- **Apple**: Clareza, tipografia limpa, atenção aos detalhes
- **Linear**: Modernidade sem excessos, cores sutis
- **Notion**: Neutralidade, foco no conteúdo

### O Que Evitamos

- ❌ Gradientes excessivos
- ❌ Glassmorphism exagerado
- ❌ Cards desnecessários
- ❌ Sombras pesadas
- ❌ Cores vibrantes em excesso
- ❌ Animações decorativas
- ❌ Aparência de "app gamer" ou infantil

---

## Tokens de Design

### Cores

#### Neutros

| Token | Valor | Uso |
|-------|-------|-----|
| `--color-neutral-0` | `#FFFFFF` | Branco puro - fundos claros |
| `--color-neutral-50` | `#F8F9FA` | Cinza muito claro - superfícies |
| `--color-neutral-100` | `#F1F3F4` | Cinza claro - divisórias sutis |
| `--color-neutral-200` | `#E8EAED` | Cinza médio-claro - bordas |
| `--color-neutral-300` | `#DADCE0` | Cinza médio - elementos discretos |
| `--color-neutral-400` | `#BDC1C6` | Cinza médio-escuro - ícones secundários |
| `--color-neutral-500` | `#9AA0A6` | Cinza - texto secundário |
| `--color-neutral-600` | `#5F6368` | Cinza escuro - texto terciário |
| `--color-neutral-700` | `#3C4043` | Cinza muito escuro - texto primário |
| `--color-neutral-800` | `#202124` | Quase preto - títulos |
| `--color-neutral-900` | `#1A1A1A` | Preto suave - texto forte |
| `--color-neutral-1000` | `#000000` | Preto puro - contrastes |

#### Cor Primária (SlideInk Blue)

| Token | Valor | Uso |
|-------|-------|-----|
| `--color-primary-500` | `#3F9FED` | **Cor principal** - ações principais, estado ativo |

Usada **apenas** para:
- Ações principais
- Estado ativo
- Foco
- Destaque sutil

#### Cores de Estado

| Token | Valor | Uso |
|-------|-------|-----|
| `--color-success` | `#34A853` | Conectado, sucesso |
| `--color-warning` | `#FBBC04` | Atenção, instável |
| `--color-error` | `#EA4335` | Erro, desconectado |
| `--color-info` | `#4285F4` | Informação |

### Tipografia

#### Font Family

```css
--font-family-base: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
--font-family-mono: 'SF Mono', 'Monaco', 'Inconsolata', 'Fira Mono', 'Droid Sans Mono', 'Source Code Pro', monospace;
```

**Por que system fonts?**
- Performance máxima (sem download)
- Natividade (parece parte do SO)
- Acessibilidade (fontes otimizadas para leitura)

#### Escala de Fontes

| Token | Valor | Uso |
|-------|-------|-----|
| `--font-size-xs` | `11px` | Metadados, labels mínimos |
| `--font-size-sm` | `12px` | Texto secundário, status |
| `--font-size-base` | `14px` | Texto corporal padrão |
| `--font-size-md` | `16px` | Texto importante |
| `--font-size-lg` | `18px` | Títulos pequenos |
| `--font-size-xl` | `20px` | Títulos de seção |
| `--font-size-2xl` | `24px` | Títulos principais |

### Espaçamento (Sistema de 4px)

| Token | Valor |
|-------|-------|
| `--spacing-1` | `4px` |
| `--spacing-2` | `8px` |
| `--spacing-3` | `12px` |
| `--spacing-4` | `16px` |
| `--spacing-5` | `20px` |
| `--spacing-6` | `24px` |
| `--spacing-8` | `32px` |
| `--spacing-10` | `40px` |
| `--spacing-12` | `48px` |
| `--spacing-16` | `64px` |

### Border Radius

| Token | Valor | Uso |
|-------|-------|-----|
| `--radius-none` | `0` | Elementos retos |
| `--radius-sm` | `4px` | Botões pequenos, inputs |
| `--radius-md` | `6px` | Botões padrão |
| `--radius-lg` | `8px` | Containers maiores |
| `--radius-xl` | `12px` | Modais, painéis |
| `--radius-full` | `9999px` | Badges, indicadores |

### Sombras (Extremamente Sutile)

```css
--shadow-none: none;
--shadow-xs: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
--shadow-sm: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
--shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
--shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
--shadow-toolbar: 0 1px 2px 0 rgba(0, 0, 0, 0.06);
```

**Nota:** Sombras são intencionalmente sutis para não competir com o conteúdo da apresentação.

### Transições

```css
--transition-fast: 100ms ease;
--transition-base: 150ms ease;
--transition-slow: 200ms ease;
```

**Princípio:** Transições rápidas e imperceptíveis. A interface deve parecer instantânea.

---

## Componentes

### Buttons

#### Uso Básico

```html
<button class="btn btn-primary">Ação Principal</button>
<button class="btn btn-secondary">Ação Secundária</button>
<button class="btn btn-danger">Ação Destrutiva</button>
<button class="btn btn-ghost">Ação Discreta</button>
```

#### Tamanhos

```html
<button class="btn btn-sm">Pequeno</button>
<button class="btn btn-md">Médio (padrão)</button>
<button class="btn btn-lg">Grande</button>
```

#### Com Ícone

```html
<button class="btn btn-icon">
  <svg class="icon icon-md" viewBox="0 0 24 24">...</svg>
</button>
```

### Status Indicator

```html
<div class="status-indicator">
  <span class="status-dot connected"></span>
  <span class="status-text connected">Conectado</span>
</div>
```

**Estados:**
- `.connected` / `.success` - Verde
- `.disconnected` / `.error` - Vermelho
- `.warning` / `.instable` - Amarelo
- `.info` - Azul

### Toolbar

```html
<div class="toolbar">
  <div class="toolbar-title">SlideInk</div>
  <div class="toolbar-divider"></div>
  <button class="btn btn-secondary btn-sm">Anterior</button>
  <button class="btn btn-secondary btn-sm">Próximo</button>
  <div class="toolbar-spacer"></div>
  <div class="page-navigation">
    <span>Página</span>
    <span class="page-current">1</span>
    <span class="page-separator">/</span>
    <span>42</span>
  </div>
</div>
```

### Status Bar

```html
<div class="status-bar">
  <div class="status-bar-item">
    <div class="status-indicator">
      <span class="status-dot connected"></span>
      <span>Conectado ao Android</span>
    </div>
  </div>
  <div class="status-bar-item">
    <span>Overlay:</span>
    <span class="badge badge-success">Ativo</span>
  </div>
</div>
```

### Badge

```html
<span class="badge">Normal</span>
<span class="badge badge-primary">Primário</span>
<span class="badge badge-success">Sucesso</span>
<span class="badge badge-warning">Aviso</span>
<span class="badge badge-error">Erro</span>
```

### Card Flutuante

```html
<div class="floating-panel">
  <div class="panel-title">
    <svg class="icon icon-sm">...</svg>
    Título do Painel
  </div>
  <p class="panel-text">Descrição ou conteúdo do painel.</p>
</div>
```

---

## Padrões de Layout

### Estrutura Principal

```
┌─────────────────────────────────────┐
│           TOOLBAR                   │ ← 56px
├─────────────────────────────────────┤
│                                     │
│                                     │
│          PDF CONTAINER              │ ← Flex 1
│        (com overlay canvas)         │
│                                     │
│                                     │
├─────────────────────────────────────┤
│          STATUS BAR                 │ ← 32px
└─────────────────────────────────────┘
```

### Hierarquia Visual

1. **Conteúdo/Apresentação** (prioridade máxima)
2. **Ação que o professor está executando**
3. **Estado da conexão**
4. **Ferramentas**
5. **Informações secundárias**

---

## Estados da Interface

### Conexão

| Estado | Indicador | Texto |
|--------|-----------|-------|
| Desconectado | 🔴 Vermelho | "Desconectado do Android" |
| Procurando | 🟡 Amarelo (piscando) | "Procurando dispositivo..." |
| Conectando | 🟡 Amarelo | "Conectando..." |
| Conectado | 🟢 Verde | "Conectado ao Android" |
| Instável | 🟡 Amarelo | "Conexão instável" |
| Reconectando | 🟡 Amarelo | "Reconectando..." |

### Overlay

| Estado | Visualização |
|--------|--------------|
| Ocioso | Invisível (opacity: 0) |
| Ativo | Visível (opacity: 1) |
| Desenho | Cursor muda, canvas recebe eventos |

---

## Microinterações

### Feedback de Conexão

Quando conectar:
- Transição suave do ponto vermelho para verde
- Texto atualiza com fade-in
- Duração: 150ms

### Hover em Botões

- Mudança sutil de background
- Duração: 100ms
- Sem elevação ou sombras pesadas

### Troca de Página

- Fade-out rápido do número antigo
- Fade-in do novo número
- Duração total: 150ms

---

## Acessibilidade

### Contraste

Todos os textos seguem WCAG AA:
- Texto normal: mínimo 4.5:1
- Texto grande: mínimo 3:1

### Focus Visible

```css
.btn:focus-visible {
  outline: none;
  box-shadow: 0 0 0 2px #FFFFFF, 0 0 0 4px #3F9FED;
}
```

### Áreas de Toque

- Mínimo: 40x40px para botões
- Espaçamento entre elementos: 8px mínimo

### Estados Não Dependentes Apenas de Cor

Sempre usar:
- Ícone + cor
- Texto + cor
- Forma + cor

Exemplo: Status dot + texto descritivo

---

## Como Usar

### 1. Importar CSS

```html
<head>
  <link rel="stylesheet" href="design-tokens.css">
  <link rel="stylesheet" href="components.css">
</head>
```

### 2. Usar Classes Prontas

```html
<button class="btn btn-primary btn-sm">
  <svg class="icon icon-sm">...</svg>
  Avançar
</button>

<div class="status-indicator">
  <span class="status-dot connected"></span>
  Conectado
</div>
```

### 3. Usar Variáveis CSS Personalizadas

```css
.minha-classe {
  color: var(--color-primary-500);
  background: var(--color-neutral-50);
  padding: var(--spacing-4);
  border-radius: var(--radius-md);
}
```

---

## Arquivos

```
src/
├── design-tokens.css    # Tokens (cores, tipografia, spacing, etc.)
├── components.css       # Componentes (buttons, badges, toolbar, etc.)
└── index.html           # Exemplo de uso
```

---

## Princípios de Evolução

### Adicionar Novo Componente

1. O componente é realmente necessário?
2. Pode ser construído com componentes existentes?
3. Segue os princípios de design?
4. É acessível?
5. Funciona em diferentes tamanhos de tela?

### Alterar Token Existente

1. Qual o impacto em todos os componentes?
2. A alteração melhora acessibilidade?
3. Mantém consistência visual?
4. Testar em todos os cenários antes de commitar

---

## Exemplo Completo

```html
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>SlideInk Example</title>
  <link rel="stylesheet" href="design-tokens.css">
  <link rel="stylesheet" href="components.css">
</head>
<body>
  <div class="app-container">
    <!-- Toolbar -->
    <div class="toolbar">
      <div class="toolbar-title">SlideInk</div>
      <div class="toolbar-divider"></div>
      <button class="btn btn-secondary btn-sm">Anterior</button>
      <button class="btn btn-secondary btn-sm">Próximo</button>
      <div class="toolbar-spacer"></div>
      <button class="btn btn-danger btn-sm">Limpar</button>
    </div>

    <!-- Content -->
    <div class="pdf-wrapper">
      <canvas id="pdf-render"></canvas>
      <canvas id="overlay-canvas" class="overlay-canvas"></canvas>
    </div>

    <!-- Status Bar -->
    <div class="status-bar">
      <div class="status-bar-item">
        <div class="status-indicator">
          <span class="status-dot connected"></span>
          <span>Conectado ao Android</span>
        </div>
      </div>
      <div class="status-bar-item">
        <span class="badge badge-success">Overlay Ativo</span>
      </div>
    </div>
  </div>
</body>
</html>
```

---

## Versão

**v1.0** - Agosto 2026
- Design tokens completos
- Componentes base implementados
- Documentação inicial

---

## Contribuição

Ao contribuir com o Design System:

1. Mantenha a simplicidade
2. Priorize acessibilidade
3. Documente novas adições
4. Teste em múltiplos cenários
5. Nunca quebre funcionalidades existentes
