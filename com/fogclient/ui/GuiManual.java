/*     */ package com.fogclient.ui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiManual
/*     */   extends GuiScreen
/*     */ {
/*  13 */   private List<String> rawLines = new ArrayList<>();
/*  14 */   private List<String> displayLines = new ArrayList<>();
/*  15 */   private int scroll = 0;
/*  16 */   private int maxScroll = 0;
/*     */ 
/*     */   
/*     */   public GuiManual() {
/*  20 */     this.rawLines.add("Â§aÂ§lFogClient - Manual do UsuÃ¡rio");
/*  21 */     this.rawLines.add("");
/*  22 */     this.rawLines.add("Bem-vindo ao FogClient, um cliente focado em PvP, utilitÃ¡rios e otimizaÃ§Ã£o.");
/*  23 */     this.rawLines.add("Este manual detalha cada mÃ³dulo, suas configuraÃ§Ãµes e funcionamento.");
/*  24 */     this.rawLines.add("");
/*  25 */     this.rawLines.add("Â§2Â§lCOMO NAVEGAR");
/*  26 */     this.rawLines.add("- Â§fMenu Principal (ClickGUI): Pressione Â§aRSHIFTÂ§f (Tecla padrÃ£o, porÃ©m editÃ¡vel).");
/*  27 */     this.rawLines.add("- Â§fBotÃ£o Esquerdo: Liga/Desliga mÃ³dulos.");
/*  28 */     this.rawLines.add("- Â§fBotÃ£o Direito: Abre configuraÃ§Ãµes detalhadas ao clicar em cima de um mÃ³dulo.");
/*  29 */     this.rawLines.add("- Â§fBind: Serve para ligar e desligar o mÃ³dulo, clique na opÃ§Ã£o 'Bind' e aperte uma tecla para criar atalho.");
/*  30 */     this.rawLines.add("- Â§fAction: Serve executar a aÃ§Ã£o do mÃ³dulo quando ele jÃ¡ estiver ligado (diferente de bind), clique na opÃ§Ã£o 'Action' e aperte uma tecla para executar uma aÃ§Ã£o de um mÃ³dulo que tem esse recurso.");
/*  31 */     this.rawLines.add("");
/*  32 */     this.rawLines.add("Â§2Â§lCATEGORIAS E MÃ“DULOS");
/*  33 */     this.rawLines.add("");
/*     */ 
/*     */     
/*  36 */     this.rawLines.add("Â§a[Guerra]");
/*  37 */     this.rawLines.add("Â§fEmpurraTudo:");
/*  38 */     this.rawLines.add("  Aumenta o knockback (empurrÃ£o) causado no inimigo.");
/*  39 */     this.rawLines.add("  SuperKB: ");
/*  40 */     this.rawLines.add("  - WTap + ResetSprint (Da mini resets no sprint durante o PvP e faz o Player ir mais para trÃ¡s).");
/*  41 */     this.rawLines.add("  - Packet (Envia pacotes para o servidor para simular um KnockBack mais forte no oponente)");
/*  42 */     this.rawLines.add("  - Legit (Ã‰ seguro e simula tÃ©cnica de \"W-Tap\" perfeita.)");
/*  43 */     this.rawLines.add("  PingSpoof: Quando vocÃª bate, o cliente segura esse pacote de ataque por alguns milissegundos (configurÃ¡vel no \"Ping Delay\", ex: 100ms) antes de enviar para o servidor.");
/*  44 */     this.rawLines.add("  Isso cria um efeito onde o inimigo recebe o dano e o empurrÃ£o com um leve atraso.");
/*  45 */     this.rawLines.add("");
/*     */     
/*  47 */     this.rawLines.add("Â§fFlechaCerteira:");
/*  48 */     this.rawLines.add("  Ao atirar uma flecha no oponente, a flecha faz a rota certa para onde o oponente estÃ¡ no momento do disparo.");
/*  49 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/*  50 */     this.rawLines.add("  - Silent: A mira nÃ£o mexe na sua tela.");
/*  51 */     this.rawLines.add("  - Predict: Calcula onde o inimigo estarÃ¡ (para alvos em movimento).");
/*  52 */     this.rawLines.add("  - Priority: Escolhe quem focar (Ex: Mais Perto).");
/*  53 */     this.rawLines.add("");
/*     */     
/*  55 */     this.rawLines.add("Â§fReachDiferenciado:");
/*  56 */     this.rawLines.add("  O ReachDiferenciado serve para vocÃª ganhar o PvP nos detalhes, dando aquele \"hit a mais\" que o inimigo nÃ£o alcanÃ§a, mas de forma que pareÃ§a natural (como se fosse combo ou ping). Se vocÃª quer bater de 6 blocos o tempo todo igual hack escancarado, esse mÃ³dulo nÃ£o vai fazer isso porque ele prioriza a seguranÃ§a da sua conta.");
/*  57 */     this.rawLines.add("  ");
/*  58 */     this.rawLines.add("  O mÃ³dulo manipula visualmente a posiÃ§Ã£o do jogador inimigo no seu cliente para validar o hit de longe:");
/*  59 */     this.rawLines.add("  1. Pra Frente (Perto de vocÃª): Quando o ReachDiferenciado ativa, ele puxa o inimigo para mais perto (teleporte invisÃ­vel ou rÃ¡pido) para o seu jogo entender que ele estÃ¡ no alcance do seu hit (3 blocos).");
/*  60 */     this.rawLines.add("  2. Pra TrÃ¡s (PosiÃ§Ã£o Real): Assim que vocÃª dÃ¡ o hit ou o mÃ³dulo desativa (pelas proteÃ§Ãµes de seguranÃ§a), o jogo \"corrige\" a posiÃ§Ã£o do inimigo devolvendo ele para onde o servidor diz que ele realmente estÃ¡.");
/*  61 */     this.rawLines.add(" ");
/*  62 */     this.rawLines.add("  Permite bater de mais longe que o normal.");
/*  63 */     this.rawLines.add("  Diferente de reachs comuns, ele utiliza randomizaÃ§Ã£o e heurÃ­sticas (falhas propositais) para simular um comportamento humano e evitar detecÃ§Ã£o.");
/*  64 */     this.rawLines.add("  Ele Ã© projetado para ser Legit (indetectÃ¡vel) e simular \"sorte\" ou \"lag\" a seu favor.");
/*  65 */     this.rawLines.add("  Ele nÃ£o deixa vocÃª dar hits de longe muito rÃ¡pido seguidos (tem um delay mÃ­nimo de 500ms entre hits de reach).");
/*  66 */     this.rawLines.add("  Se vocÃª der 2 hits seguidos de longe, ele forÃ§a o prÃ³ximo a ser normal para \"disfarÃ§ar\".");
/*  67 */     this.rawLines.add("  Se o servidor estiver lagado (TPS baixo), ele desativa para evitar bugs de movimentaÃ§Ã£o.");
/*  68 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/*  69 */     this.rawLines.add("  - Min/Max Reach: Define um intervalo (Ex: 3.0 a 4.0) para variar os hits.");
/*  70 */     this.rawLines.add("  - Chance %: Probabilidade do hit ter alcance aumentado.");
/*  71 */     this.rawLines.add("");
/*     */     
/*  73 */     this.rawLines.add("Â§fRecraftPerfeito:");
/*  74 */     this.rawLines.add("  Abre o inventÃ¡rio e faz sopas automaticamente, isso tudo acionado por uma tecla.");
/*  75 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/*  76 */     this.rawLines.add("  - Speed: Velocidade do recraft.");
/*  77 */     this.rawLines.add("");
/*     */     
/*  79 */     this.rawLines.add("Â§fDuploClique:");
/*  80 */     this.rawLines.add("  Configure outra tecla para tambÃ©m clicar com os botÃµes esquerdo ou direito.");
/*  81 */     this.rawLines.add("  AlÃ©m de depender apenas do click esquerdo do mouse para atacar, configure uma segunda tecla para fazer a mesma aÃ§Ã£o.");
/*  82 */     this.rawLines.add("  AlÃ©m de depender apenas do click direito do mouse para defender, comer ou colocar blocos, configure uma segunda tecla para fazer a mesma aÃ§Ã£o.");
/*  83 */     this.rawLines.add("");
/*     */     
/*  85 */     this.rawLines.add("Â§fAjudanteDeRecraft:");
/*  86 */     this.rawLines.add("  Move os itens do recraft automaticamente para a craft do inventÃ¡rio");
/*  87 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/*  88 */     this.rawLines.add("  - Speed: Rapidez do movimento.");
/*  89 */     this.rawLines.add("");
/*     */     
/*  91 */     this.rawLines.add("Â§fSopaPadrao:");
/*  92 */     this.rawLines.add("  VocÃª consegue tomar sopas como no HG em um mundo singleplayer.");
/*  93 */     this.rawLines.add("  Pode ser usado para tirar delays de sopas ou usar contra o BotPvP no Singleplayer e treinar.");
/*  94 */     this.rawLines.add("");
/*     */     
/*  96 */     this.rawLines.add("Â§fSemDefesa:");
/*  97 */     this.rawLines.add("  Bloqueia a defesa da espada.");
/*  98 */     this.rawLines.add("");
/*     */     
/* 100 */     this.rawLines.add("Â§fHitInfinito:");
/* 101 */     this.rawLines.add("  Remove o delay de hit da 1.8.");
/* 102 */     this.rawLines.add("  Permite clicar e combar extremamente rÃ¡pido.");
/* 103 */     this.rawLines.add("");
/*     */     
/* 105 */     this.rawLines.add("Â§fBlockHitPro:");
/* 106 */     this.rawLines.add("  Intercala defesa (block) no momento exato de troca de hits para reduzir dano e combar inimigos.");
/* 107 */     this.rawLines.add("");
/*     */     
/* 109 */     this.rawLines.add("Â§fMiraCerteira:");
/* 110 */     this.rawLines.add("  Ajuda a manter a mira no inimigo de forma suave.");
/* 111 */     this.rawLines.add("  Â§71. Hitbox Pixels (NÃ£o aumenta o hitbox do inimigo)");
/* 112 */     this.rawLines.add("  - Como funciona: Se vocÃª mirar um pouco fora do corpo do inimigo, o mÃ³dulo entende que vocÃª \"quase acertou\" e suavemente empurra sua mira para dentro do corpo dele.");
/* 113 */     this.rawLines.add("  - Na prÃ¡tica: Faz com que seja muito mais difÃ­cil \"pinar\" (errar) a mira por poucos pixels. VocÃª pode mirar um pouco torto que ele corrige.");
/* 114 */     this.rawLines.add("  Â§72. Sensitivity Adjust (Muda a sensibilidade do mouse ao mirar)");
/* 115 */     this.rawLines.add("  - O que faz: Altera a sensibilidade do seu mouse automaticamente quando vocÃª passa a mira sobre um inimigo.");
/* 116 */     this.rawLines.add("  - Como funciona:");
/* 117 */     this.rawLines.add("    - Quando sua mira estÃ¡ em cima do inimigo: Ele diminui a sensibilidade (deixa o mouse mais lento/pesado). Isso ajuda vocÃª a nÃ£o passar a mira direto pelo alvo, dando mais precisÃ£o para manter o combo.");
/* 118 */     this.rawLines.add("    - Quando a mira sai do inimigo: A sensibilidade volta ao normal instantaneamente para vocÃª poder virar a cÃ¢mera rÃ¡pido.");
/* 119 */     this.rawLines.add("  - Na prÃ¡tica: Cria uma sensaÃ§Ã£o de \"imÃ£\" ou \"fricÃ§Ã£o\" quando a mira passa pelo oponente, facilitando manter a mira cravada nele durante o PvP..");
/* 120 */     this.rawLines.add("");
/*     */     
/* 122 */     this.rawLines.add("Â§fSopaFacil:");
/* 123 */     this.rawLines.add("  Ao apertar uma tecla configurÃ¡vel vocÃª toma uma sopa, dropa o pote e volta para a slot original.");
/* 124 */     this.rawLines.add("  VocÃª NÃƒO precisa ter o trabalho de ir atÃ© a sopa, tomar, dropar o pote e voltar para a espada, esse modulo faz essa funÃ§Ã£o automaticamente.");
/* 125 */     this.rawLines.add("");
/*     */ 
/*     */     
/* 128 */     this.rawLines.add("Â§a[Player]");
/* 129 */     this.rawLines.add("Â§fRoubaTudo (Anti Traps na Obsidian):");
/* 130 */     this.rawLines.add("  Ao tentarem colocar lava em vocÃª ou ao seu redor (Raio configurÃ¡vel), ele pega automaticamente desde que vocÃª tenha baldes cheios ou vazios na hotbar (nÃ£o precisa estar com eles em mÃ£os).");
/* 131 */     this.rawLines.add("");
/*     */     
/* 133 */     this.rawLines.add("Â§fJuntaPotes:");
/* 134 */     this.rawLines.add("  Muito usado em HG, em casos de economia de potes ou organizaÃ§Ã£o de inventÃ¡rio.");
/* 135 */     this.rawLines.add("  - O que ele faz: Ele agrupa instantaneamente todos os potes (tigelas) espalhados pelo seu inventÃ¡rio em um Ãºnico pack.");
/* 136 */     this.rawLines.add("  - Como usar:");
/* 137 */     this.rawLines.add("  1. Abra seu inventÃ¡rio.");
/* 138 */     this.rawLines.add("  2. Passe o mouse sobre qualquer pote (tigela).");
/* 139 */     this.rawLines.add("  3. Clique com o BotÃ£o Lateral do Mouse (BotÃ£o 5, geralmente o de \"AvanÃ§ar\").");
/* 140 */     this.rawLines.add("");
/*     */     
/* 142 */     this.rawLines.add("Â§fLavaNoMeuPe:");
/* 143 */     this.rawLines.add("  Coloca lava nos seus pÃ©s e a recolhe rapidamente, evitando que o oponente roube a sua lava.");
/* 144 */     this.rawLines.add("  Essa estratÃ©gia Ã© muito usado em minimushs e gladiators, para ganhar VANTAGEM de KnockBack.");
/* 145 */     this.rawLines.add("");
/*     */     
/* 147 */     this.rawLines.add("Â§fHelperMLG:");
/* 148 */     this.rawLines.add("  Serve para ser indetectÃ¡vel e simular MLG legit e nunca morrer de queda.");
/* 149 */     this.rawLines.add("  - Como funciona:");
/* 150 */     this.rawLines.add("  1. VocÃª precisa estar segurando um Balde de Ã�gua na mÃ£o.");
/* 151 */     this.rawLines.add("  2. VocÃª deve estar olhando para o chÃ£o onde vai cair.");
/* 152 */     this.rawLines.add("  3. Quando vocÃª estiver a menos de 2 blocos de bater no chÃ£o, o mÃ³dulo clica extremamente rÃ¡pido (mais rÃ¡pido que reflexo humano) para colocar a Ã¡gua.");
/* 153 */     this.rawLines.add("  - LimitaÃ§Ã£o importante: Ele nÃ£o troca para o balde sozinho (vocÃª precisa jÃ¡ estar com ele na mÃ£o) e nÃ£o mira sozinho para baixo (vocÃª precisa olhar). Ele apenas executa o \"clique perfeito\" no tempo certo (o timing do MLG).");
/* 154 */     this.rawLines.add("");
/*     */ 
/*     */     
/* 157 */     this.rawLines.add("Â§a[Mobilidade]");
/* 158 */     this.rawLines.add("Â§fPuloDoGato0KB:");
/* 159 */     this.rawLines.add("  Ã© um mÃ³dulo de movimentaÃ§Ã£o focado em reduzir o Knockback (empurrÃ£o) que vocÃª recebe ao tomar dano.");
/* 160 */     this.rawLines.add("  - O que ele faz: Ele faz seu personagem pular automaticamente no exato momento em que vocÃª toma um hit.");
/* 161 */     this.rawLines.add("  - Por que isso funciona (FÃ­sica do Minecraft): No Minecraft, quando vocÃª estÃ¡ no ar (pulando), o atrito com o chÃ£o Ã© menor e a fÃ­sica de \"empurrÃ£o\" funciona de forma diferente.");
/* 162 */     this.rawLines.add("  Ao pular no momento do hit, vocÃª:");
/* 163 */     this.rawLines.add("  1. Reduz a distÃ¢ncia que Ã© jogado para trÃ¡s.");
/* 164 */     this.rawLines.add("  2. MantÃ©m melhor o seu \"momentum\" (velocidade) para continuar combando.");
/* 165 */     this.rawLines.add("  3. Tem mais chance de cair atrÃ¡s do oponente ou em uma posiÃ§Ã£o vantajosa.");
/* 166 */     this.rawLines.add("  - Detalhe importante: Ele sÃ³ ativa se vocÃª estiver segurando o botÃ£o de atacar ou estiver em combate, para nÃ£o ficar pulando aleatoriamente quando tomar dano de outras coisas (como queda ou fogo) que nÃ£o sejam PvP.");
/* 167 */     this.rawLines.add("");
/*     */     
/* 169 */     this.rawLines.add("Â§fLegitBridge:");
/* 170 */     this.rawLines.add("  Simula cliques humanos (Drag Click / Butterfly) para construir pontes com seguranÃ§a (GodBridge/Breezily).");
/* 171 */     this.rawLines.add("  - Como funciona:");
/* 172 */     this.rawLines.add("    1. Segure um bloco na mÃ£o.");
/* 173 */     this.rawLines.add("    2. Ative o mÃ³dulo (pode ser configurado para \"Segurar tecla\" ou \"Apertar tecla\" nas configuraÃ§Ãµes).");
/* 174 */     this.rawLines.add("    3. Simplesmente ande para trÃ¡s mirando na borda do bloco. O mÃ³dulo farÃ¡ o \"timing\" perfeito dos cliques para vocÃª.");
/* 175 */     this.rawLines.add("  - Vantagens Competitivas:");
/* 176 */     this.rawLines.add("    - GodBridge/Breezily Sem EsforÃ§o: Permite fazer as pontes mais difÃ­ceis do jogo sem precisar saber fazer Drag Click ou ter 20 CPS.");
/* 177 */     this.rawLines.add("    - IndetectÃ¡vel (Humanizado): Diferente de macros comuns, ele varia os intervalos dos cliques e simula falhas humanas propositais para burlar Anti-Cheats.");
/* 178 */     this.rawLines.add("    - Salva-Vidas (Auto-Clutch): Se vocÃª errar o passo e cair, o \"Modo PÃ¢nico\" ativa instantaneamente, clicando freneticamente para colocar um bloco embaixo de vocÃª e te salvar da queda.");
/* 179 */     this.rawLines.add("");
/*     */ 
/*     */     
/* 182 */     this.rawLines.add("Â§a[Visual]");
/* 183 */     this.rawLines.add("Â§fEncontraBau:");
/* 184 */     this.rawLines.add("  Destaca BaÃºs atravÃ©s das paredes, deixando o bÃ¡u brilhante e com um feixe de luz.");
/* 185 */     this.rawLines.add("  - Vantagem: Essencial em HG/SkyWars para localizar itens rapidamente e nÃ£o perder tempo procurando baÃºs escondidos.");
/* 186 */     this.rawLines.add("");
/*     */     
/* 188 */     this.rawLines.add("Â§fEncontraBigorna:");
/* 189 */     this.rawLines.add("  Destaca Bigornas atravÃ©s dos blocos, deixando a bigorna brilhante e com um feixe de luz.");
/* 190 */     this.rawLines.add("  - Vantagem: Essencial em HG/Minimush/Scrim/FlameLeague para localizar bigornas rapidamente e fazer a progredir um item seu, como uma espada, juntando duas Espadas com Afiada 1 e transformando em Afiada 2.");
/* 191 */     this.rawLines.add("");
/*     */     
/* 193 */     this.rawLines.add("Â§fEncontraCama:");
/* 194 */     this.rawLines.add("  Destaca camas atravÃ©s dos blocos, deixando a bigorna brilhante e com um feixe de luz.");
/* 195 */     this.rawLines.add("  - Vantagem: Fundamental em BedWars para localizar a cama inimiga instantaneamente, mesmo se estiver coberta por blocos (lÃ£, madeira, endstone), permitindo planejar o rush perfeito ou destruir a cama sem precisar procurar.");
/* 196 */     this.rawLines.add("");
/*     */     
/* 198 */     this.rawLines.add("Â§fAchaFerro:");
/* 199 */     this.rawLines.add("  Destaca minÃ©rios de ferro atravÃ©s dos blocos.");
/* 200 */     this.rawLines.add("  SÃ³ de passar andando no mapa e olhar para baixo aparece onde tem ferro.");
/* 201 */     this.rawLines.add("  - Vantagem: Essencial em cenÃ¡rios de HG, Eventos, Survival para encontrar ferro rapidamente no inÃ­cio da partida, garantindo armadura e ferramentas antes dos oponentes sem perder tempo minerando Ã s cegas.");
/* 202 */     this.rawLines.add("");
/*     */     
/* 204 */     this.rawLines.add("Â§fEscalaCustomizada:");
/* 205 */     this.rawLines.add("  Permite mudar a escala apenas do seu inventÃ¡rio, baÃºs, npcs do Bedwars...");
/* 206 */     this.rawLines.add("  - Por que usar?");
/* 207 */     this.rawLines.add("    Muitos jogadores preferem usar a \"GUI Scale: Large\" ou \"Auto\" para ver o inventÃ¡rio maior e clicar mais facilmente, mas isso deixa tudo gigante (hotbar, scoreboard e etc), ocupando a tela toda.");
/* 208 */     this.rawLines.add("    Com este mÃ³dulo, vocÃª pode deixar o jogo em \"Small\" e o inventÃ¡rio em \"Large\", \"Normal\" ou \"Auto\"...");
/* 209 */     this.rawLines.add("  - Vantagem Competitiva (Refill/Recraft):");
/* 210 */     this.rawLines.add("    Ao deixar o inventÃ¡rio maior ou menor, vocÃª consegue ser mais preciso ao manipular itens no inventÃ¡rio ou craftar.");
/* 211 */     this.rawLines.add("");
/*     */     
/* 213 */     this.rawLines.add("Â§fEscolheNick (NameProtect):");
/* 214 */     this.rawLines.add("  Altera nicks visualmente.");
/* 215 */     this.rawLines.add("  Muito Usados para disfarÃ§ar nicks ou atÃ© ir PvP de brincadeira com o seu YouTuber preferido, colocando nick dele em quem vocÃª quiser.");
/* 216 */     this.rawLines.add("  ApÃ³s configurar o nick, Ã© orientado no chato relog para ser efetivada a alteraÃ§Ã£o por completo (No jogador real, Chat, Tab, InventÃ¡rios)");
/* 217 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/* 218 */     this.rawLines.add("  - Nick Real / Novo Nick: Digite para alterar.");
/* 219 */     this.rawLines.add("  - BotÃ£o de Aplicar e BotÃ£o para Resetar");
/* 220 */     this.rawLines.add("");
/*     */     
/* 222 */     this.rawLines.add("Â§fUltraF5:");
/* 223 */     this.rawLines.add("  Libera a cÃ¢mera em terceira pessoa (F5) para ir muito mais longe do que o limite padrÃ£o do Minecraft.");
/* 224 */     this.rawLines.add("  - Vantagem TÃ¡tica (Vision):");
/* 225 */     this.rawLines.add("    Permite que vocÃª afaste a cÃ¢mera para ter uma visÃ£o aÃ©rea do mapa ao seu redor, funcionando como um \"DRONE\".");
/* 226 */     this.rawLines.add("    - HG/SkyWars: Veja se tem inimigos indo atÃ© vocÃª, escondidos atrÃ¡s de paredes, em cima de Ã¡rvores, dentro de buracos sem precisar se expor.");
/* 227 */     this.rawLines.add("    - Factions/HCF: Verifique se hÃ¡ bases ou armadilhas por perto olhando por cima de muros altos.");
/* 228 */     this.rawLines.add("    - Minerar: Veja se tem lava ou cavernas perigosas ao seu redor antes de quebrar blocos.");
/* 229 */     this.rawLines.add("");
/*     */ 
/*     */     
/* 232 */     this.rawLines.add("Â§a[Utilidades]");
/* 233 */     this.rawLines.add("Â§fDiamondCollector:");
/* 234 */     this.rawLines.add("  Coleta itens de diamante automaticamente de baÃºs assim que vocÃª os abre.");
/* 235 */     this.rawLines.add("  Abra o baÃº e saia correndo; se tiver diamante, jÃ¡ estarÃ¡ no seu inventÃ¡rio.");
/* 236 */     this.rawLines.add("  - Vantagem: Em uma partida, vocÃª nÃ£o perde tempo clicando.");
/* 237 */     this.rawLines.add("");
/*     */     
/* 239 */     this.rawLines.add("Â§fTankoSIM:");
/* 240 */     this.rawLines.add("  Simula um jogador da classe \"Tank\" (comum em HCF) gerenciando seu inventÃ¡rio.");
/* 241 */     this.rawLines.add("  - Funcionalidade: Joga fora automaticamente itens inÃºteis para PvP (como pÃ¡s de madeira, tesouras) ou armaduras dentro do inventÃ¡rio (NÃ£o joga as equipadas), mantendo seu inventÃ¡rio limpo para recraft e refil.");
/* 242 */     this.rawLines.add("  - Vantagem: Evita inventÃ¡rio cheio de lixo no meio do combate.");
/* 243 */     this.rawLines.add("  - CenÃ¡rio real: VocÃª pode dropar toda a sua armadura do seu inventÃ¡rio em um buraco fechado contra um inimigo e ocupar o espaÃ§o livre do inventÃ¡rio dele e vocÃª o matar facilmente, ou pode resistir a traps com pÃ¡s e tesouras que te impediriam de manipular o seu inventÃ¡rio por ocupar espaÃ§o demais nele.");
/* 244 */     this.rawLines.add("");
/*     */     
/* 246 */     this.rawLines.add("Â§fBotPvP:");
/* 247 */     this.rawLines.add("  Cria um NPC (Bot) inteligente para treino de PvP offline.");
/* 248 */     this.rawLines.add("  - CaracterÃ­sticas: O Bot possui W-Tap, strafe e mira, simulando um jogador real.");
/* 249 */     this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
/* 250 */     this.rawLines.add("  - Dificuldade: FÃ¡cil, MÃ©dio, DifÃ­cil ou Cheater.");
/* 251 */     this.rawLines.add("  - Dano: Ajuste quantos coraÃ§Ãµes o bot tira por hit.");
/* 252 */     this.rawLines.add("  - Vantagem: Treine sua mira, combos e reduÃ§aÃµ de KB, alÃ©m disso vocÃª pode spawnar diversos bots e treinar contra vÃ¡rios oponentes.");
/* 253 */     this.rawLines.add("      - Dica: Combine com o mod sopa fÃ¡cil e simule um pvp no HG e evolua cada vez mais.");
/* 254 */     this.rawLines.add("");
/*     */     
/* 256 */     this.rawLines.add("Â§fFPSTurbo:");
/* 257 */     this.rawLines.add("  Otimizador extremo, configura tudo para maximizar o FPS.");
/* 258 */     this.rawLines.add("  - Vantagem: Essencial para rodar o Minecraft liso em PCs fracos ou melhorar o que jÃ¡ estÃ¡ bom.");
/* 259 */     this.rawLines.add("");
/*     */     
/* 261 */     this.rawLines.add("Â§fDespertador:");
/* 262 */     this.rawLines.add("  Exibe lembretes visuais na tela em horÃ¡rios programados, juntamente a um som de notificaÃ§Ã£o.");
/* 263 */     this.rawLines.add("  - Uso: \"Ir para o evento Ã s 19:00\", \"Tomar remÃ©dio\", \"Sair do PC\".");
/* 264 */     this.rawLines.add("  Nunca mais perca um evento do servidor por estar distraÃ­do.");
/* 265 */     this.rawLines.add("");
/*     */ 
/*     */     
/* 268 */     this.rawLines.add("Â§a[Client]");
/* 269 */     this.rawLines.add("Â§fMenu Principal:");
/* 270 */     this.rawLines.add("  O menu que vocÃª estÃ¡ usando agora.");
/* 271 */     this.rawLines.add("");
/* 272 */     this.rawLines.add("Â§fTroll:");
/* 273 */     this.rawLines.add("  Teste kkkk (Cuidado!).");
/* 274 */     this.rawLines.add("");
/*     */     
/* 276 */     this.rawLines.add("Â§7Pressione ESC para fechar.");
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/* 281 */     super.func_73866_w_();
/* 282 */     recalculateLines();
/*     */   }
/*     */   
/*     */   private void recalculateLines() {
/* 286 */     this.displayLines.clear();
/* 287 */     char c = 'Ę';
/*     */     
/* 289 */     for (String str : this.rawLines) {
/*     */       
/* 291 */       if (str.startsWith("Â§aÂ§l") || str.startsWith("Â§2Â§l") || str.startsWith("Â§a[")) {
/* 292 */         this.displayLines.add(str); continue;
/*     */       } 
/* 294 */       if (str.isEmpty()) {
/* 295 */         this.displayLines.add("");
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 301 */       List<? extends String> list = this.field_146297_k.field_71466_p.func_78271_c(str, c);
/* 302 */       this.displayLines.addAll(list);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int paramInt1, int paramInt2, float paramFloat) {
/* 309 */     func_146276_q_();
/*     */ 
/*     */     
/* 312 */     Gui.func_73734_a(this.field_146294_l / 2 - 180, 20, this.field_146294_l / 2 + 180, this.field_146295_m - 20, -15658735);
/* 313 */     Gui.func_73734_a(this.field_146294_l / 2 - 178, 22, this.field_146294_l / 2 + 178, this.field_146295_m - 22, -14540254);
/*     */ 
/*     */     
/* 316 */     int i = this.displayLines.size() * 12;
/* 317 */     int j = this.field_146295_m - 44;
/* 318 */     this.maxScroll = Math.max(0, i - j);
/* 319 */     if (this.scroll < 0) this.scroll = 0; 
/* 320 */     if (this.scroll > this.maxScroll) this.scroll = this.maxScroll;
/*     */ 
/*     */     
/* 323 */     int k = 30 - this.scroll;
/* 324 */     int m = this.field_146295_m - 25;
/*     */     
/* 326 */     for (String str : this.displayLines) {
/* 327 */       if (k > 25 && k < m) {
/* 328 */         if (str.startsWith("Â§aÂ§l") || str.startsWith("Â§2Â§l")) {
/*     */           
/* 330 */           this.field_146297_k.field_71466_p.func_175063_a(str, (this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(str) / 2), k, -16711936);
/* 331 */         } else if (str.startsWith("Â§a[")) {
/*     */           
/* 333 */           this.field_146297_k.field_71466_p.func_175063_a(str, (this.field_146294_l / 2 - 170), k, -16711936);
/* 334 */         } else if (str.startsWith("Â§f")) {
/*     */           
/* 336 */           this.field_146297_k.field_71466_p.func_175063_a(str, (this.field_146294_l / 2 - 170), k, -1);
/*     */         } else {
/*     */           
/* 339 */           this.field_146297_k.field_71466_p.func_175063_a(str, (this.field_146294_l / 2 - 160), k, -5592406);
/*     */         } 
/*     */       }
/* 342 */       k += 12;
/*     */     } 
/*     */ 
/*     */     
/* 346 */     if (this.maxScroll > 0) {
/* 347 */       int n = j * j / i;
/* 348 */       if (n < 10) n = 10; 
/* 349 */       int i1 = 22 + this.scroll * (j - n) / this.maxScroll;
/* 350 */       Gui.func_73734_a(this.field_146294_l / 2 + 174, 22, this.field_146294_l / 2 + 178, this.field_146295_m - 22, -15658735);
/* 351 */       Gui.func_73734_a(this.field_146294_l / 2 + 174, i1, this.field_146294_l / 2 + 178, i1 + n, -16711936);
/*     */     } 
/*     */     
/* 354 */     super.func_73863_a(paramInt1, paramInt2, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 359 */     super.func_146274_d();
/* 360 */     int i = Mouse.getEventDWheel();
/* 361 */     if (i != 0) {
/* 362 */       if (i > 0) { this.scroll -= 12; }
/* 363 */       else { this.scroll += 12; }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_73868_f() {
/* 369 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\GuiManual.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */