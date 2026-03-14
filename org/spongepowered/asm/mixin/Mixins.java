/*     */ package org.spongepowered.asm.mixin;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.launch.GlobalProperties;
/*     */ import org.spongepowered.asm.mixin.transformer.Config;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mixins
/*     */ {
/*  46 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CONFIGS_KEY = "mixin.configs.queue";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final Set<String> errorHandlers = new LinkedHashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addConfigurations(String... paramVarArgs) {
/*  66 */     MixinEnvironment mixinEnvironment = MixinEnvironment.getDefaultEnvironment();
/*  67 */     for (String str : paramVarArgs) {
/*  68 */       createConfiguration(str, mixinEnvironment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addConfiguration(String paramString) {
/*  78 */     createConfiguration(paramString, MixinEnvironment.getDefaultEnvironment());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   static void addConfiguration(String paramString, MixinEnvironment paramMixinEnvironment) {
/*  83 */     createConfiguration(paramString, paramMixinEnvironment);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void createConfiguration(String paramString, MixinEnvironment paramMixinEnvironment) {
/*  88 */     Config config = null;
/*     */     
/*     */     try {
/*  91 */       config = Config.create(paramString, paramMixinEnvironment);
/*  92 */     } catch (Exception exception) {
/*  93 */       logger.error("Error encountered reading mixin config " + paramString + ": " + exception.getClass().getName() + " " + exception.getMessage(), exception);
/*     */     } 
/*     */     
/*  96 */     registerConfiguration(config);
/*     */   }
/*     */   
/*     */   private static void registerConfiguration(Config paramConfig) {
/* 100 */     if (paramConfig == null) {
/*     */       return;
/*     */     }
/*     */     
/* 104 */     MixinEnvironment mixinEnvironment = paramConfig.getEnvironment();
/* 105 */     if (mixinEnvironment != null) {
/* 106 */       mixinEnvironment.registerConfig(paramConfig.getName());
/*     */     }
/* 108 */     getConfigs().add(paramConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getUnvisitedCount() {
/* 127 */     byte b = 0;
/* 128 */     for (Config config : getConfigs()) {
/* 129 */       if (!config.isVisited()) {
/* 130 */         b++;
/*     */       }
/*     */     } 
/* 133 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<Config> getConfigs() {
/* 141 */     Set<Config> set = (Set)GlobalProperties.get("mixin.configs.queue");
/* 142 */     if (set == null) {
/* 143 */       set = new LinkedHashSet();
/* 144 */       GlobalProperties.put("mixin.configs.queue", set);
/*     */     } 
/* 146 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerErrorHandlerClass(String paramString) {
/* 155 */     if (paramString != null) {
/* 156 */       errorHandlers.add(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getErrorHandlerClasses() {
/* 164 */     return Collections.unmodifiableSet(errorHandlers);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\Mixins.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */