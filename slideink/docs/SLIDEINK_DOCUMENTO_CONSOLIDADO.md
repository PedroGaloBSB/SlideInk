# SLIDEINK

## Documento Consolidado de Produto, Escopo e Arquitetura

**Versão:** 1.0 — Consolidação
**Data:** 13 de agosto de 2026
**Status:** Pré-desenvolvimento / Validação técnica
**Responsável:** Fundador e Desenvolvedor Solo

---

# 1. O QUE É O SLIDEINK?

O **SlideInk** é uma ferramenta para professores, palestrantes e outros apresentadores que precisam **controlar uma apresentação e fazer anotações sobre ela durante uma aula ou apresentação online**.

A ideia central é simples:

> **O celular ou tablet Android vira o controle e a superfície de escrita; o computador Windows continua sendo o palco onde a apresentação acontece.**

O professor poderá:

* avançar e voltar slides;
* ir diretamente para uma página;
* controlar a apresentação;
* utilizar recursos como apontador/laser;
* desenhar e escrever sobre o conteúdo;
* apagar ou limpar as anotações;
* fazer tudo isso pelo dispositivo Android;
* enquanto o computador transmite a apresentação pelo Zoom, Google Meet, Microsoft Teams ou plataformas semelhantes.

O objetivo não é criar mais uma plataforma de videoconferência.

Também não é simplesmente criar um leitor de PDF.

O objetivo é criar uma **camada de apresentação e anotação ao vivo** que funcione junto às ferramentas que o professor já utiliza.

A arquitetura atualmente prevista é composta por dois aplicativos: um aplicativo Android e um aplicativo Windows. 

---

# 2. O PROBLEMA QUE ESTAMOS TENTANDO RESOLVER

Durante uma aula online, o professor frequentemente precisa fazer várias coisas ao mesmo tempo:

1. controlar os slides;
2. compartilhar a tela;
3. explicar o conteúdo;
4. destacar informações;
5. escrever ou desenhar;
6. apagar as marcações;
7. retornar à apresentação.

Quando tudo isso precisa ser feito pelo computador, a experiência pode ficar pouco natural.

O problema que o SlideInk pretende resolver é:

> **Como permitir que o professor controle e anote sua apresentação de maneira natural, sem precisar ficar preso ao mouse e ao teclado do computador?**

A solução proposta é separar as funções:

```text
                PROFESSOR
                    │
                    ▼
            📱 Android
        controle + escrita
                    │
             comunicação
                    │
                    ▼
             🖥️ Windows
        apresentação + overlay
                    │
                    ▼
        Zoom / Meet / Teams
                    │
                    ▼
                 ALUNOS
```

---

# 3. PARA QUEM É O PRODUTO?

O primeiro público de interesse é o **professor que trabalha com aulas online**, especialmente professores de cursinhos, preparação para concursos e cursos educacionais.

Esse público é particularmente interessante porque frequentemente trabalha com:

* PDFs;
* slides;
* questões;
* materiais didáticos;
* aulas ao vivo;
* compartilhamento de tela;
* explicações acompanhadas de marcações.

O conceito também pode posteriormente atender:

* palestrantes;
* treinadores;
* professores particulares;
* instrutores corporativos;
* criadores de conteúdo;
* apresentações profissionais.

### Estratégia inicial

Não devemos tentar atender todo mundo imediatamente.

A recomendação é:

> **começar com o problema específico do professor de aula online e expandir somente depois de provar que o produto funciona e é útil.**

---

# 4. A EXPERIÊNCIA QUE QUEREMOS ENTREGAR

O fluxo ideal para o professor deve ser extremamente simples.

### Antes da aula

```text
Instala SlideInk no Android
             ↓
Instala SlideInk no Windows
             ↓
Abre o material
             ↓
Conecta os dispositivos
```

### Durante a aula

```text
             📱 Android
                 │
        ┌────────┴────────┐
        │                 │
   Controle           Anotação
   de slides          com caneta
        │                 │
        └────────┬────────┘
                 │
                 ▼
             🖥️ Windows
                 │
        apresentação + overlay
                 │
                 ▼
          Zoom / Meet / Teams
                 │
                 ▼
              alunos
```

A experiência desejada é que o professor **não precise pensar na tecnologia**.

Idealmente ele não deveria precisar saber se está conectado por:

* USB;
* Wi-Fi;
* Bluetooth.

Ele simplesmente deveria saber:

> **"Estou conectado ao meu computador e posso dar minha aula."**

---

# 5. O CORAÇÃO DO PRODUTO

O SlideInk possui três funções fundamentais.

## 5.1 Controle da apresentação

O Android deverá permitir:

* próximo slide;
* slide anterior;
* ir para determinada página;
* iniciar apresentação;
* encerrar apresentação;
* apontador/laser;
* eventualmente outras funções de apresentação.

## 5.2 Anotação

O professor deverá poder:

* escrever;
* desenhar;
* destacar;
* apagar;
* limpar a tela;
* alterar características básicas da caneta.

A anotação deve ser produzida no Android e aparecer no Windows.

## 5.3 Apresentação compartilhável

O conteúdo final precisa continuar sendo apresentado pelo computador.

O objetivo é que o professor possa utilizar:

* Zoom;
* Google Meet;
* Microsoft Teams;
* OBS;
* outras ferramentas que capturem a tela.

A POC de compartilhamento de tela prevista no projeto justamente testa se o overlay é capturado corretamente pelas plataformas. 

---

# 6. O OVERLAY É A PRINCIPAL HIPÓTESE TECNOLÓGICA

A ideia inicial é utilizar uma **camada transparente sobre a apresentação no Windows**.

Por exemplo:

```text
┌─────────────────────────────────────────┐
│                                         │
│             PDF / SLIDE                 │
│                                         │
│       "A Constituição..."                │
│                                         │
│            ────────                     │
│                ↑                        │
│          anotação do professor          │
│                                         │
└─────────────────────────────────────────┘
```

O professor não precisa alterar o PDF original.

O SlideInk simplesmente coloca a anotação sobre aquilo que está sendo apresentado.

### Porém, existe uma condição crítica:

**Precisamos provar que o Zoom, Meet, Teams e outras ferramentas capturam esse overlay da forma esperada.**

Esse é um dos maiores riscos do projeto.

O documento técnico já prevê testes de:

* tela inteira;
* janela específica;
* compartilhamento de área;
* aceleração de hardware;
* diferentes plataformas de videoconferência. 

---

# 7. O QUE O MVP NÃO SERÁ

Uma das decisões mais importantes é definir aquilo que **não será feito inicialmente**.

O MVP não precisa:

* substituir Zoom;
* substituir Google Meet;
* substituir Teams;
* criar uma plataforma de aulas;
* possuir uma rede social;
* criar um editor completo de apresentações;
* substituir PowerPoint;
* substituir Canva;
* suportar todos os formatos existentes;
* possuir inteligência artificial;
* possuir uma infraestrutura de nuvem complexa.

Também não precisamos começar tentando resolver:

> PDF + PowerPoint + Canva + Google Slides + navegador + OBS + Zoom + Meet + Teams.

Isso aumentaria muito o risco.

---

# 8. ESCOPO DO MVP

O MVP deve responder a uma pergunta muito simples:

> **"Um professor consegue abrir uma apresentação no Windows, controlar essa apresentação pelo Android e escrever sobre ela, fazendo com que os alunos vejam essas anotações durante uma aula online?"**

Para responder isso, o MVP precisa de:

### Android

* conexão com Windows;
* controle de apresentação;
* canvas de anotação;
* envio dos comandos;
* envio dos traços;
* indicação do estado da conexão.

### Windows

* recebimento dos comandos;
* apresentação de PDF;
* overlay;
* recebimento das anotações;
* controle da apresentação;
* integração indireta com Zoom/Meet/Teams por compartilhamento de tela.

### Conectividade inicial

* **Wi-Fi**
* **USB**

Bluetooth entra na arquitetura, mas não precisa estar funcional no primeiro MVP.

---

# 9. PDF COMO PRIMEIRO FORMATO

PDF foi escolhido como ponto de partida porque permite atacar o problema central sem depender inicialmente de integrações com plataformas externas.

A primeira experiência pode ser:

```text
Professor
   ↓
abre PDF
   ↓
SlideInk Windows exibe PDF
   ↓
Android controla página
   ↓
Android desenha
   ↓
Windows mostra anotação
   ↓
Professor compartilha tela
```

Somente depois de provar esse fluxo devemos avançar para:

* PowerPoint;
* apresentações web;
* Google Slides;
* Canva;
* outros conteúdos.

Isso também permite testar a renderização de PDFs de diferentes tamanhos, algo que já foi previsto no material técnico. 

---

# 10. ARQUITETURA GERAL

A arquitetura conceitual fica:

```text
┌─────────────────────────────────────────────────────────────┐
│                         SLIDEINK                            │
│                                                             │
│  📱 ANDROID                         🖥️ WINDOWS              │
│                                                             │
│  Interface                         Interface                │
│  Controle                          Apresentação             │
│  Canvas                            PDF                      │
│  Anotação                          Overlay                  │
│                                    WebSocket Server         │
│                                                             │
│          └────────── TRANSPORT LAYER ──────────┘            │
│                                                             │
│              USB | Wi-Fi | Bluetooth | Hotspot             │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

A principal decisão arquitetural é:

> **A aplicação não deve depender diretamente de uma tecnologia específica de conexão.**

Em vez de a aplicação saber:

> "vou enviar isso pelo Wi-Fi"

ela deve simplesmente dizer:

> "envie esta mensagem".

A camada de transporte decide como.

---

# 11. TRANSPORT LAYER

A camada de transporte será responsável por esconder as diferenças entre os meios de comunicação.

```text
                 APLICAÇÃO
                     │
                     ▼
             TransportInterface
                     │
              TransportManager
              /       |       \
             /        |        \
           USB       Wi-Fi    Bluetooth
```

O `TransportManager` poderá:

* descobrir conexões disponíveis;
* estabelecer conexão;
* acompanhar qualidade;
* selecionar transporte;
* informar o estado para a interface;
* eventualmente realizar failover.

Essa decisão já foi formalizada no material como **ADR-006 — Transport Layer Agnóstica**. 

---

# 12. USB, WI-FI E BLUETOOTH

Aqui chegamos a uma decisão importante que incorporamos agora.

## USB

Será uma opção prioritária para:

* estabilidade;
* baixa latência;
* situações em que o professor quer máxima confiabilidade.

Mas **não vamos afirmar uma latência específica antes de medir**.

O valor real será obtido na POC.

---

## Wi-Fi

Será o principal caminho sem fio do MVP.

Vantagens:

* não exige cabo;
* experiência mais natural;
* permite liberdade de movimento;
* adequado para transmissão contínua de comandos e strokes.

Também será medido na prática.

---

## Bluetooth

Bluetooth será contemplado **desde o início na arquitetura**, mas não será prometido como funcionalidade do MVP.

A POC deverá responder:

* Bluetooth consegue transmitir comandos adequadamente?
* consegue transmitir strokes?
* qual é a latência?
* qual é o jitter?
* existe perda?
* qual é a estabilidade?
* qual é o impacto na bateria?

O documento atual prevê inclusive testar BLE para comandos e Bluetooth clássico/RFCOMM para transmissão contínua, deixando a decisão final para depois dos testes. 

### Portanto:

```text
USB         → MVP
Wi-Fi       → MVP
Bluetooth   → POC
Hotspot     → evolução posterior
```



---

# 13. UMA CORREÇÃO IMPORTANTE: NÃO FIXAR LATÊNCIAS COMO FATOS

Algumas versões anteriores do documento apresentavam valores como:

* USB ≈ 2 ms;
* Wi-Fi ≈ 20–80 ms;
* Bluetooth ≈ 30–100 ms.

Esses valores **não devem ser tratados como especificações do produto neste momento**.

Devem ser considerados **expectativas ou hipóteses de engenharia**.

A redação correta é:

> **A latência dos transportes será medida em condições reais de utilização e os valores obtidos serão utilizados para estabelecer os critérios definitivos de qualidade.**

Isso deixa o documento tecnicamente mais sério.

---

# 14. SELEÇÃO AUTOMÁTICA DE CONEXÃO

O usuário poderá ter dois modos.

## Automático

O SlideInk verifica:

* quais transportes estão disponíveis;
* qualidade;
* estabilidade;
* latência;
* preferência configurada.

E escolhe a melhor alternativa.

## Manual

O professor poderá escolher:

```text
USB
Wi-Fi
Bluetooth
```

quando disponível.

---

# 15. FAILOVER

Existe uma ideia importante:

> Se a conexão principal falhar, o sistema deve tentar continuar funcionando através de outro transporte.

Exemplo:

```text
Wi-Fi
  ↓
instabilidade
  ↓
USB disponível
  ↓
migração
  ↓
aula continua
```

Mas existe uma ressalva importante:

**failover transparente ainda é um requisito a ser validado.**

Não podemos simplesmente trocar de canal durante uma sequência de desenho sem garantir:

* ordem das mensagens;
* ausência de duplicação;
* ausência de perda;
* sincronização do estado;
* continuidade da anotação.

Portanto:

> **Failover é objetivo arquitetural, não funcionalidade considerada comprovada neste momento.**

---

# 16. PROTOCOLO DE COMUNICAÇÃO

Independentemente do transporte, a aplicação deve trabalhar com o mesmo protocolo lógico.

Por exemplo:

```text
NEXT_SLIDE
PREVIOUS_SLIDE
GOTO_SLIDE
LASER_ON
LASER_OFF
CLEAR_ANNOTATIONS
STROKE_START
STROKE_POINT
STROKE_END
```

Assim:

```text
Android
   │
   │  "NEXT_SLIDE"
   ▼
Transport Layer
   │
   ├── USB
   ├── Wi-Fi
   └── Bluetooth
```

A mensagem continua sendo a mesma.

Isso reduz o acoplamento e facilita futuras mudanças.

---

# 17. POR QUE NÃO COMEÇAR PELO BLUETOOTH?

Porque Bluetooth é uma hipótese adicional.

O problema principal do produto não é:

> "conseguir conectar dois dispositivos."

É:

> **"conseguir produzir uma experiência de anotação suficientemente rápida e confiável para uma aula ao vivo."**

Por isso primeiro devemos provar:

1. apresentação;
2. overlay;
3. compartilhamento;
4. Wi-Fi;
5. USB;
6. experiência de escrita.

Depois Bluetooth.

---

# 18. AS POCs MAIS IMPORTANTES

## POC 1 — Overlay

Provar:

> É possível desenhar uma camada transparente sobre a apresentação?

---

## POC 2 — Android → Windows

Provar:

> Um traço feito no Android aparece no Windows com latência aceitável?

---

## POC 3 — Compartilhamento

Essa é a **POC mais crítica**.

Testar:

* Zoom;
* Google Meet;
* Teams;
* tela inteira;
* janela;
* área selecionada;
* aceleração de hardware.

O objetivo é descobrir:

> **O aluno realmente vê a anotação?**

O plano de testes existente já estabelece esses cenários. 

---

## POC 4 — PDF

Testar:

* PDF pequeno;
* PDF grande;
* muitas páginas;
* troca de página;
* renderização;
* consumo de memória.

---

## POC 5 — Bluetooth

Somente depois dos testes principais.

Medir:

* latência;
* p95/p99;
* jitter;
* estabilidade;
* perda;
* bateria.

O papel do Bluetooth será decidido pelos resultados, e não por uma escolha antecipada. 

---

# 19. A REGRA MAIS IMPORTANTE DO PROJETO

Eu colocaria esta regra no documento oficial:

> ## O SlideInk nunca pode ser responsável por impedir o professor de continuar a aula.

Se o aplicativo falhar:

```text
SlideInk falhou
     ↓
PDF continua funcionando
     ↓
professor continua apresentando
     ↓
aula não é perdida
```

Por isso precisamos de:

* modo de recuperação;
* limpeza rápida;
* retorno à apresentação;
* estado de conexão claramente visível;
* possibilidade de abandonar o SlideInk sem destruir a apresentação.

O conceito de "modo pânico" já apareceu no planejamento e deve ser mantido. 

---

# 20. EXPERIÊNCIA DE USUÁRIO

O professor não deve enxergar uma tela cheia de informações técnicas.

Em vez disso:

```text
┌───────────────────────────────────┐
│        SLIDEINK                   │
│                                   │
│  🟢 Conectado ao computador       │
│                                   │
│  Página 18 / 42                   │
│                                   │
│       ←       18       →          │
│                                   │
│  ✎ Caneta                         │
│  ⌫ Borracha                       │
│  ✕ Limpar                         │
│  🔴 Laser                         │
│                                   │
│  Conexão: Automática              │
└───────────────────────────────────┘
```

O professor não precisa saber que existe:

* WebSocket;
* TransportManager;
* BLE;
* RFCOMM;
* Electron;
* mDNS.

Isso é problema do software.

---

# 21. TECNOLOGIAS PREVISTAS

A arquitetura atualmente estudada considera:

### Android

* Kotlin;
* Jetpack Compose;
* Hilt;
* Canvas;
* Room quando houver necessidade de persistência.

### Windows

* Electron;
* TypeScript;
* PDF.js;
* overlay transparente;
* servidor de comunicação.

Essas tecnologias fazem parte da arquitetura atualmente proposta, mas **não devem ser consideradas decisões irreversíveis antes das POCs**. 

A regra é:

> **A tecnologia deve servir ao produto, e não o contrário.**

Se uma tecnologia impedir o funcionamento correto do overlay, ela deve ser reconsiderada.

---

# 22. O MAIOR RISCO DO PROJETO

Não é o Android.

Não é o WebSocket.

Não é o PDF.

Não é nem mesmo o Bluetooth.

O maior risco é:

# **o conteúdo anotado não ser capturado corretamente pela plataforma de videoconferência.**

Porque podemos construir:

```text
Android ✅
Windows ✅
PDF ✅
Anotação ✅
Comunicação ✅
```

e ainda assim o produto falhar se:

```text
Zoom
  ↓
compartilha PDF
  ↓
NÃO captura overlay
```

Por isso a POC de compartilhamento deve ocorrer **antes de investir pesado no restante da aplicação**.

---

# 23. PLANO DE DESENVOLVIMENTO

O desenvolvimento deve acontecer em etapas.

## Fase 0 — Descoberta

* estudar ferramentas existentes;
* conversar com professores;
* entender o fluxo real;
* validar o problema.

## Fase 1 — Overlay

```text
Windows
+
PDF
+
overlay
```

## Fase 2 — Compartilhamento

```text
Overlay
+
Zoom
+
Meet
+
Teams
```

## Fase 3 — Comunicação

```text
Android
      ↓
Windows
```

Primeiro Wi-Fi.

Depois USB.

## Fase 4 — Anotação

```text
Android
   ↓
stroke
   ↓
Windows
   ↓
overlay
```

## Fase 5 — MVP

Unir tudo.

## Fase 6 — Teste real

Colocar nas mãos de aproximadamente cinco professores.

## Fase 7 — Beta

Corrigir problemas reais.

## Fase 8 — Expansão

Somente então avaliar:

* Bluetooth;
* Hotspot;
* PowerPoint;
* Google Slides;
* Canva;
* outros formatos;
* recursos avançados.

---

# 24. O QUE PRECISA SER VALIDADO COM PROFESSORES

Não devemos perguntar apenas:

> "Você usaria o SlideInk?"

Isso gera respostas pouco confiáveis.

Precisamos descobrir:

* como o professor trabalha hoje;
* qual equipamento usa;
* como controla os slides;
* como faz anotações;
* se utiliza PDF;
* se utiliza PowerPoint;
* qual plataforma de videoconferência utiliza;
* se utiliza mesa digitalizadora;
* se utiliza tablet;
* quais dificuldades encontra;
* quanto tempo perde;
* qual solução utiliza atualmente;
* quanto pagaria por uma solução melhor.

O próprio planejamento anterior já prevê validação com professores antes de avançar demais no produto. 

---

# 25. CONCORRÊNCIA E INSPIRAÇÃO

O SlideInk não precisa inventar todos os componentes do zero.

Existem categorias de software que podem servir como referência:

### Leitores/anotadores de PDF

* Drawboard PDF;
* Xodo;
* PDF Annotator;
* OneNote.

### Quadros digitais

* Microsoft Whiteboard;
* OpenBoard;
* Ziteboard;
* Limnu;
* Whiteboard.fi.

### Controle/integração entre dispositivos

Projetos como KDE Connect também são referências importantes para estudar comunicação entre dispositivos.

A ideia não é copiar esses produtos.

É estudar:

> **o que eles fazem bem, o que fazem mal e o que ainda não resolvem juntos.**

---

# 26. O DIFERENCIAL DO SLIDEINK

Precisamos ser cuidadosos aqui.

Não podemos afirmar:

> "ninguém faz isso".

Isso ainda não foi comprovado.

O diferencial que estamos buscando é a **combinação**:

```text
        Android
           +
   controle da apresentação
           +
       anotação
           +
    Windows overlay
           +
   videoconferência
```

Ou seja:

> **Transformar um dispositivo Android que o professor já possui em uma superfície de controle e anotação para apresentações online.**

A verdadeira vantagem competitiva só poderá ser definida depois de estudarmos concorrentes e validarmos a experiência com usuários.

---

# 27. MODELO DE NEGÓCIO

O projeto estudou um modelo de:

* versão gratuita;
* versão paga;
* possível modelo freemium;
* preço acessível para professores.

Mas aqui também devemos manter uma postura conservadora.

Ainda não temos evidência suficiente para afirmar:

* percentual de conversão;
* receita;
* tamanho real do mercado;
* preço ideal;
* quantidade de usuários pagantes;
* break-even.

Portanto, esses números devem ser tratados como **hipóteses financeiras**, e não previsões.

Primeiro:

> **provar que professores querem usar.**

Depois:

> **provar que professores continuam usando.**

Depois:

> **descobrir quanto estão dispostos a pagar.**

---

# 28. O QUE SERÁ CONSIDERADO SUCESSO NO MVP?

O MVP não será considerado bem-sucedido porque:

* o código ficou bonito;
* a arquitetura ficou sofisticada;
* o aplicativo tem muitas funções.

Ele será bem-sucedido se um professor conseguir fazer isto:

```text
1. Abrir o SlideInk
2. Conectar Android + Windows
3. Abrir um PDF
4. Avançar/voltar páginas pelo celular
5. Escrever pelo celular
6. Ver a anotação no computador
7. Compartilhar a tela
8. Fazer o aluno enxergar a anotação
9. Dar uma aula sem precisar lutar contra o software
```

**Esse é o verdadeiro MVP.**

---

# 29. CRITÉRIO DE GO / NO-GO

Antes de construir o produto inteiro:

### 🟢 GO

Se conseguirmos provar:

* overlay;
* compartilhamento;
* comunicação;
* anotação;
* experiência aceitável;
* interesse real dos professores.

Então continuamos.

### 🟡 REVISÃO

Se:

* funcionar tecnicamente, mas houver problemas de UX;
* houver latência excessiva;
* alguma plataforma tiver limitações;
* professores demonstrarem interesse, mas não pelo fluxo imaginado.

Então ajustamos.

### 🔴 NO-GO / PIVÔ

Se descobrirmos que:

* o overlay não pode ser capturado de maneira confiável;
* a experiência de escrita é ruim;
* o problema não é relevante para professores;
* existe uma solução concorrente claramente superior e difícil de superar.

Nesse caso, não devemos insistir simplesmente porque já escrevemos código.

---

# 30. PRINCÍPIOS DE ARQUITETURA

O projeto deve seguir alguns princípios simples:

### KISS

Começar simples.

### SOLID

Cada componente possui responsabilidade clara.

### DRY

Não duplicar lógica.

### Separation of Concerns

Interface não deve conhecer detalhes de comunicação.

### Fail Fast

Problemas de conexão devem ser identificados rapidamente.

Esses princípios já fazem parte da arquitetura proposta. 

Mas existe um princípio ainda mais importante:

> **Não construir complexidade antes de existir uma necessidade comprovada.**

---

# 31. DECISÕES ARQUITETURAIS ATUAIS

| Decisão                                      | Estado                          |
| -------------------------------------------- | ------------------------------- |
| Dois aplicativos: Android + Windows          | **Definido**                    |
| Android como controle/superfície de anotação | **Definido**                    |
| Windows como palco/apresentação              | **Definido**                    |
| PDF como primeiro formato                    | **Definido para MVP**           |
| Overlay como hipótese principal              | **Validar em POC**              |
| Zoom/Meet/Teams                              | **Validar em POC**              |
| Wi-Fi                                        | **MVP**                         |
| USB                                          | **MVP**                         |
| Bluetooth                                    | **Arquitetura + POC**           |
| Hotspot                                      | **Futuro**                      |
| Transport Layer abstrata                     | **Definido**                    |
| TransportManager                             | **Definido conceitualmente**    |
| Failover automático                          | **Objetivo; validar**           |
| Electron                                     | **Hipótese tecnológica atual**  |
| WebSocket                                    | **Hipótese tecnológica atual**  |
| Modelo freemium                              | **Hipótese de negócio**         |
| Mercado e projeções financeiras              | **Ainda precisam de validação** |

A inclusão do Bluetooth na arquitetura, sem torná-lo requisito funcional do MVP, está formalizada no ADR-007 do material. 

---

# 32. O QUE NÃO DEVEMOS FAZER AGORA

Não devemos começar por:

```text
❌ login
❌ pagamentos
❌ nuvem
❌ IA
❌ banco de dados complexo
❌ dashboard
❌ analytics
❌ suporte a todos os formatos
❌ Bluetooth completo
❌ aplicativo perfeito
```

Devemos começar por:

```text
🧪 PDF
   ↓
🖥️ Overlay
   ↓
🎥 Zoom / Meet / Teams
   ↓
📱 Android
   ↓
✍️ Anotação
   ↓
🔌 Wi-Fi / USB
```

---

# 33. A VISÃO FINAL

Se tudo der certo, a experiência que queremos chegar é esta:

```text
                 PROFESSOR
                     │
                     │
              📱 CELULAR/TABLET
                     │
          ┌──────────┼──────────┐
          │          │          │
       próximo    anterior    escrever
          │          │          │
          └──────────┼──────────┘
                     │
             Transport Layer
                     │
        ┌────────────┼────────────┐
        │            │            │
       USB          Wi-Fi      Bluetooth*
        │            │            │
        └────────────┼────────────┘
                     │
                     ▼
               🖥️ WINDOWS
                     │
            ┌────────┴────────┐
            │                 │
        apresentação       overlay
            │                 │
            └────────┬────────┘
                     │
                     ▼
             Zoom / Meet / Teams
                     │
                     ▼
                  👨‍🎓👩‍🎓
                  ALUNOS
```

* Bluetooth depende da POC.

---

# 34. A DECISÃO MAIS IMPORTANTE DE TODAS

Depois de toda essa discussão, eu resumiria o projeto em uma única frase:

> **O SlideInk não é um aplicativo de PDF, nem um controle remoto e nem uma lousa digital. Ele é uma ferramenta de apresentação que usa o Android como controle e superfície de anotação, enquanto o Windows funciona como palco e entrega o resultado para as plataformas de videoconferência.**

E o primeiro grande teste do projeto é:

> **"Consigo fazer isso funcionar de maneira confiável em uma aula real?"**

Se a resposta for sim, temos um produto para desenvolver.

Se não for, descobriremos cedo — antes de gastar meses construindo funcionalidades em torno de uma hipótese que não funciona.

---

## 📌 Estado atual do projeto

**Não estamos mais na fase de "ter uma ideia".**

Já temos:

* problema definido;
* público inicial;
* proposta de valor;
* escopo inicial;
* MVP;
* arquitetura conceitual;
* estratégia de conectividade;
* POCs;
* riscos;
* critérios de validação;
* estratégia de evolução.

O que **ainda não temos** — e não devemos fingir que temos — é a comprovação de que a solução funciona tecnicamente em condições reais e que professores realmente a querem.

Por isso, **o próximo passo não é construir o produto inteiro**.

É construir a menor prova possível do conceito:

> **Windows + PDF + Overlay → Zoom/Meet/Teams**

e, se isso funcionar:

> **Android → Windows → anotação → compartilhamento.**

Esse caminho mantém o projeto pequeno, mensurável e reversível. A própria documentação técnica anterior aponta a POC do overlay como o próximo passo real antes de continuar o desenvolvimento. 

**Essa passa a ser, na minha avaliação, a versão-base do escopo do SlideInk.**
