# Roadmap do SlideInk

## Visão Geral

Este documento apresenta o plano de desenvolvimento do SlideInk, organizado em fases conforme definido no documento consolidado.

---

## 📍 Fase Atual: Fase 1 — Overlay (POC)

**Status:** Em andamento  
**Duração estimada:** 1-2 semanas  
**Objetivo:** Provar que o overlay funciona e é capturado pelas plataformas de videoconferência

### Entregáveis

- [x] Estrutura do projeto criada
- [x] POC 1 do Overlay implementada
- [ ] **TESTES CRÍTICOS**: Validar no Zoom, Meet e Teams
- [ ] Documentação dos resultados dos testes

### Critério de Conclusão

✅ Overlay visível em pelo menos 2 plataformas ao compartilhar tela inteira

---

## 🗺️ Fases Futuras

### Fase 0 — Descoberta (Concluída)

**Status:** ✅ Concluída  
**Entregáveis:**
- [x] Documento consolidado do produto
- [x] Definição do problema e público-alvo
- [x] Arquitetura conceitual
- [x] Definição do MVP

---

### Fase 2 — Compartilhamento (Próxima)

**Status:** ⏳ Pendente (depende da Fase 1)  
**Duração estimada:** 1 semana  
**Objetivo:** Validar formalmente o compartilhamento nas plataformas

**Atividades:**
- [ ] Testar Zoom (tela inteira, janela, área)
- [ ] Testar Google Meet (tela inteira, janela)
- [ ] Testar Microsoft Teams (tela inteira, janela)
- [ ] Testar OBS (para streaming)
- [ ] Documentar limitações de cada plataforma
- [ ] Decidir plataformas suportadas no MVP

**Critério de Conclusão:**
✅ Matriz de compatibilidade documentada com pelo menos 2 plataformas funcionando

---

### Fase 3 — Comunicação

**Status:** ⏳ Pendente  
**Duração estimada:** 2-3 semanas  
**Objetivo:** Estabelecer comunicação Android ↔ Windows

**Subfases:**

#### 3.1 — WebSocket Server (Windows)
- [ ] Implementar servidor WebSocket
- [ ] Gerenciar múltiplas conexões
- [ ] Implementar handshake de conexão
- [ ] Log de conexões ativas

#### 3.2 — Wi-Fi (Android → Windows)
- [ ] Implementar cliente WebSocket (Android)
- [ ] Descoberta de servidores na rede (mDNS)
- [ ] Conexão automática
- [ ] Indicador de status da conexão

#### 3.3 — USB (Opcional para MVP)
- [ ] Implementar comunicação serial USB
- [ ] Detecção de conexão/desconexão
- [ ] Failover Wi-Fi ↔ USB

**Critério de Conclusão:**
✅ Android conecta ao Windows e envia comandos básicos via Wi-Fi

---

### Fase 4 — Anotação

**Status:** ⏳ Pendente  
**Duração estimada:** 2-3 semanas  
**Objetivo:** Implementar fluxo completo de anotação

**Atividades:**

#### Android
- [ ] Canvas de desenho
- [ ] Detecção de toque com pressão (se suportado)
- [ ] Suporte a stylus/pen
- [ ] Ferramentas: caneta, marcador, borracha
- [ ] Seletor de cores
- [ ] Seletor de espessura

#### Windows
- [ ] Receber strokes do Android
- [ ] Renderizar strokes no overlay
- [ ] Sincronização em tempo real
- [ ] Limpar anotações
- [ ] Desfazer último stroke

#### Protocolo
- [ ] Implementar protocolo definido
- [ ] Compressão de strokes (se necessário)
- [ ] Tratamento de latência
- [ ] Recuperação de erros

**Critério de Conclusão:**
✅ Traço feito no Android aparece no Windows em < 100ms

---

### Fase 5 — MVP

**Status:** ⏳ Pendente  
**Duração estimada:** 3-4 semanas  
**Objetivo:** Unir todas as funcionalidades em produto utilizável

**Atividades:**

#### Integração
- [ ] Unir overlay + comunicação + anotação
- [ ] UI polida (Android)
- [ ] UI polida (Windows)
- [ ] Tratamento de erros
- [ ] Logs e debugging

#### Funcionalidades MVP
- [ ] Carregar PDF local
- [ ] Navegar entre páginas (Android)
- [ ] Ir para página específica
- [ ] Caneta básica (cor, espessura)
- [ ] Borracha
- [ ] Limpar tudo
- [ ] Laser (opcional)

#### Qualidade
- [ ] Testes com PDFs pequenos (< 10 MB)
- [ ] Testes com PDFs grandes (> 50 MB)
- [ ] Testes de estabilidade (1 hora+)
- [ ] Tratamento de desconexão

**Critério de Conclusão:**
✅ Professor consegue dar uma aula completa usando o SlideInk

---

### Fase 6 — Teste Real

**Status:** ⏳ Pendente  
**Duração estimada:** 2 semanas  
**Objetivo:** Validar com usuários reais

**Atividades:**
- [ ] Recrutar 5 professores
- [ ] Fornecer acesso ao MVP
- [ ] Coletar feedback estruturado
- [ ] Medir tempo de configuração
- [ ] Identificar problemas de UX
- [ ] Calcular NPS inicial

**Critério de Conclusão:**
✅ 4 dos 5 professores recomendariam o produto

---

### Fase 7 — Beta

**Status:** ⏳ Pendente  
**Duração estimada:** 4-6 semanas  
**Objetivo:** Corrigir problemas e preparar para lançamento

**Atividades:**
- [ ] Corrigir bugs críticos
- [ ] Melhorar performance
- [ ] Refinar UX baseado no feedback
- [ ] Criar documentação do usuário
- [ ] Preparar site/landing page
- [ ] Definir modelo de preços

**Critério de Conclusão:**
✅ Produto estável pronto para lançamento limitado

---

### Fase 8 — Expansão

**Status:** ⏳ Futuro distante  
**Duração estimada:** TBD  
**Objetivo:** Expandir funcionalidades e formatos

**Possíveis Adições:**
- [ ] Suporte a PowerPoint
- [ ] Suporte a Google Slides
- [ ] Suporte a Canva
- [ ] Bluetooth completo
- [ ] Hotspot automático
- [ ] Gravação de aulas
- [ ] Exportação de anotações
- [ ] Modo offline
- [ ] Multi-plataforma (macOS, Linux)
- [ ] Aplicativo iOS

**Decisão:** Somente após validação do mercado com MVP

---

## 📊 Timeline Resumida

| Fase | Nome | Duração | Status |
|------|------|---------|--------|
| 0 | Descoberta | - | ✅ Concluída |
| 1 | Overlay | 1-2 sem | 🚧 Em andamento |
| 2 | Compartilhamento | 1 sem | ⏳ Pendente |
| 3 | Comunicação | 2-3 sem | ⏳ Pendente |
| 4 | Anotação | 2-3 sem | ⏳ Pendente |
| 5 | MVP | 3-4 sem | ⏳ Pendente |
| 6 | Teste Real | 2 sem | ⏳ Pendente |
| 7 | Beta | 4-6 sem | ⏳ Pendente |
| 8 | Expansão | TBD | 🔮 Futuro |

**Total até MVP:** ~12-16 semanas (3-4 meses)  
**Total até Beta:** ~18-24 semanas (4-6 meses)

---

## 🎯 Marcos Principais (Milestones)

### M1 — POC do Overlay Aprovada
- Quando: Final da Fase 1
- Importância: **CRÍTICA** — Se falhar, o projeto precisa pivotar

### M2 — Primeira Anotação Remota
- Quando: Final da Fase 4
- Importância: **ALTA** — Prova que comunicação + anotação funcionam

### M3 — Primeira Aula Completa
- Quando: Final da Fase 5 (MVP)
- Importância: **CRÍTICA** — Valida o produto como um todo

### M4 — Primeiros Usuários Pagantes
- Quando: Pós-Fase 7
- Importância: **ALTA** — Valida modelo de negócio

---

## 🚨 Riscos e Mitigações

### Risco Crítico: Overlay não é capturado
**Probabilidade:** Média  
**Impacto:** Alto  
**Mitigação:** 
- Testar cedo (Fase 1)
- Ter alternativas prontas (WPF, native Windows)
- Considerar abordagem híbrida

### Risco Alto: Latência inaceitável
**Probabilidade:** Média  
**Impacto:** Alto  
**Mitigação:**
- Medir latência em cada fase
- Otimizar protocolo
- Usar Wi-Fi 5GHz ou USB

### Risco Médio: Professores não adotam
**Probabilidade:** Média  
**Impacto:** Alto  
**Mitigação:**
- Validar com usuários na Fase 6
- Iterar rápido baseado no feedback
- Manter produto simples e focado

### Risco Baixo: Concorrência lança produto similar
**Probabilidade:** Baixa  
**Impacto:** Médio  
**Mitigação:**
- Focar em UX superior
- Construir comunidade
- Preço competitivo

---

## 📈 Métricas de Sucesso

### Técnicas
- Latência de anotação: < 100ms (p95)
- Taxa de sucesso de conexão: > 95%
- Tempo de setup inicial: < 2 minutos
- Estabilidade: > 1 hora sem crashes

### Usuário
- NPS: > 50
- Retenção D7: > 60%
- Conversão free→paid: > 5%
- Suporte tickets/usuário: < 0.5/mês

### Negócio (Futuro)
- MRR: Crescimento consistente
- CAC: < 3x LTV
- Churn mensal: < 5%

---

## 🔄 Processo de Revisão

Este roadmap deve ser revisado:
- **Semanalmente:** Durante desenvolvimento ativo
- **Após cada fase:** Para ajustar próxima fase
- **Quando:** Resultados de testes indicarem mudanças necessárias

**Última revisão:** 13 de agosto de 2026  
**Próxima revisão:** Após conclusão dos testes da Fase 1

---

**Responsável:** Desenvolvedor Solo  
**Status:** Vivo — Será atualizado conforme progresso
