/*     */ package org.spongepowered.asm.launch;
/*     */ 
/*     */ import java.util.ServiceLoader;
/*     */ import org.spongepowered.asm.service.IGlobalPropertyService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GlobalProperties
/*     */ {
/*     */   private static IGlobalPropertyService service;
/*     */   
/*     */   public static final class Keys
/*     */   {
/*     */     public static final String INIT = "mixin.initialised";
/*     */     public static final String AGENTS = "mixin.agents";
/*     */     public static final String CONFIGS = "mixin.configs";
/*     */     public static final String TRANSFORMER = "mixin.transformer";
/*     */     public static final String PLATFORM_MANAGER = "mixin.platform";
/*     */     public static final String FML_LOAD_CORE_MOD = "mixin.launch.fml.loadcoremodmethod";
/*     */     public static final String FML_GET_REPARSEABLE_COREMODS = "mixin.launch.fml.reparseablecoremodsmethod";
/*     */     public static final String FML_CORE_MOD_MANAGER = "mixin.launch.fml.coremodmanagerclass";
/*     */     public static final String FML_GET_IGNORED_MODS = "mixin.launch.fml.ignoredmodsmethod";
/*     */   }
/*     */   
/*     */   private static IGlobalPropertyService getService() {
/*  62 */     if (service == null) {
/*     */       
/*  64 */       ServiceLoader<IGlobalPropertyService> serviceLoader = ServiceLoader.load(IGlobalPropertyService.class, GlobalProperties.class.getClassLoader());
/*  65 */       service = serviceLoader.iterator().next();
/*     */     } 
/*  67 */     return service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T get(String paramString) {
/*  78 */     return (T)getService().getProperty(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void put(String paramString, Object paramObject) {
/*  88 */     getService().setProperty(paramString, paramObject);
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
/*     */   public static <T> T get(String paramString, T paramT) {
/* 101 */     return (T)getService().getProperty(paramString, paramT);
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
/*     */   public static String getString(String paramString1, String paramString2) {
/* 114 */     return getService().getPropertyString(paramString1, paramString2);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\GlobalProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */