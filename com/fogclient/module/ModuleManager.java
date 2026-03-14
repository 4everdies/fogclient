/*     */ package com.fogclient.module;
/*     */ import com.fogclient.module.combat.DefaultSoupModule;
/*     */ import com.fogclient.module.combat.GhostReachModule;
/*     */ import com.fogclient.module.player.HelperMLGModule;
/*     */ import com.fogclient.module.render.InventoryScaleLiteModule;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ModuleManager {
/*     */   private static ModuleManager instance;
/*     */   
/*     */   public ModuleManager() {
/*  12 */     this.modules = new ArrayList<>();
/*  13 */     registerModules();
/*     */   }
/*     */   private List<Module> modules;
/*     */   public static ModuleManager getInstance() {
/*  17 */     if (instance == null) {
/*  18 */       instance = new ModuleManager();
/*     */     }
/*  20 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerModules() {
/*  25 */     this.modules.add(new SuperKBModule());
/*  26 */     this.modules.add(new BowAimbotModule());
/*  27 */     this.modules.add(new GhostReachModule());
/*  28 */     this.modules.add(new AutoRecraftModule());
/*  29 */     this.modules.add(new SecondClickModule());
/*  30 */     this.modules.add(new RecraftAuxModule());
/*  31 */     this.modules.add(new DefaultSoupModule());
/*  32 */     this.modules.add(new NoDefenderModule());
/*  33 */     this.modules.add(new SemDelayHitsModule());
/*  34 */     this.modules.add(new BlockHitProModule());
/*  35 */     this.modules.add(new PerfectAimModule());
/*  36 */     this.modules.add(new SoupaModule());
/*     */ 
/*     */     
/*  39 */     this.modules.add(new DestakBauModule());
/*  40 */     this.modules.add(new DestakBigornaModule());
/*  41 */     this.modules.add(new DestakCamaModule());
/*  42 */     this.modules.add(new AchaFerroModule());
/*  43 */     this.modules.add(new InventoryScaleLiteModule());
/*  44 */     this.modules.add(new EscolheNickModule());
/*  45 */     this.modules.add(new UltraF5Module());
/*     */ 
/*     */     
/*  48 */     this.modules.add(new JumpResetModule());
/*  49 */     this.modules.add(new LegitBridgeModule());
/*     */ 
/*     */     
/*  52 */     this.modules.add(new RoubaLavaModule());
/*  53 */     this.modules.add(new JuntaPotesModule());
/*  54 */     this.modules.add(new LavaNoMeuPeModule());
/*  55 */     this.modules.add(new HelperMLGModule());
/*     */ 
/*     */     
/*  58 */     this.modules.add(new DiamondCollectorModule());
/*  59 */     this.modules.add(new TankoSimModule());
/*  60 */     this.modules.add(new TreinoBotModule());
/*  61 */     this.modules.add(new FullOptimizerModule());
/*  62 */     this.modules.add(new LembreteModule());
/*     */ 
/*     */     
/*  65 */     this.modules.add(new ClickGUIModule());
/*  66 */     this.modules.add(new TrollModule());
/*     */ 
/*     */     
/*  69 */     this.modules.add(new ManualModule());
/*     */   }
/*     */   
/*     */   public List<Module> getModules() {
/*  73 */     return this.modules;
/*     */   }
/*     */   
/*     */   public List<Module> getModulesByCategory(Category paramCategory) {
/*  77 */     return (List<Module>)this.modules.stream()
/*  78 */       .filter(paramModule -> (paramModule.getCategory() == paramCategory))
/*  79 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public Module getModule(Class<? extends Module> paramClass) {
/*  83 */     for (Module module : this.modules) {
/*  84 */       if (module.getClass() == paramClass) {
/*  85 */         return module;
/*     */       }
/*     */     } 
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   public Module getModuleByName(String paramString) {
/*  92 */     for (Module module : this.modules) {
/*  93 */       if (module.getName().equalsIgnoreCase(paramString)) {
/*  94 */         return module;
/*     */       }
/*     */     } 
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public void onKeyPressed(int paramInt) {
/* 101 */     if (paramInt == 0)
/*     */       return; 
/* 103 */     for (Module module : this.modules) {
/*     */       
/* 105 */       if (module.getKeybind() == paramInt) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 112 */         boolean bool = (module.isAction() && module.getActionKeybind() == paramInt) ? true : false;
/*     */         
/* 114 */         if (!bool) {
/* 115 */           module.toggle();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 120 */       if (module.isAction() && module.isEnabled() && module.getActionKeybind() == paramInt)
/* 121 */         module.onAction(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */