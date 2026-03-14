/*     */ package org.spongepowered.asm.service.mojang;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launchwrapper.LaunchClassLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class LaunchClassLoaderUtil
/*     */ {
/*     */   private static final String CACHED_CLASSES_FIELD = "cachedClasses";
/*     */   private static final String INVALID_CLASSES_FIELD = "invalidClasses";
/*     */   private static final String CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
/*     */   private static final String TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
/*     */   private final LaunchClassLoader classLoader;
/*     */   private final Map<String, Class<?>> cachedClasses;
/*     */   private final Set<String> invalidClasses;
/*     */   private final Set<String> classLoaderExceptions;
/*     */   private final Set<String> transformerExceptions;
/*     */   
/*     */   LaunchClassLoaderUtil(LaunchClassLoader paramLaunchClassLoader) {
/*  64 */     this.classLoader = paramLaunchClassLoader;
/*  65 */     this.cachedClasses = getField(paramLaunchClassLoader, "cachedClasses");
/*  66 */     this.invalidClasses = getField(paramLaunchClassLoader, "invalidClasses");
/*  67 */     this.classLoaderExceptions = getField(paramLaunchClassLoader, "classLoaderExceptions");
/*  68 */     this.transformerExceptions = getField(paramLaunchClassLoader, "transformerExceptions");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LaunchClassLoader getClassLoader() {
/*  75 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isClassLoaded(String paramString) {
/*  86 */     return this.cachedClasses.containsKey(paramString);
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
/*     */   boolean isClassExcluded(String paramString1, String paramString2) {
/*  98 */     return (isClassClassLoaderExcluded(paramString1, paramString2) || isClassTransformerExcluded(paramString1, paramString2));
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
/*     */   boolean isClassClassLoaderExcluded(String paramString1, String paramString2) {
/* 111 */     for (String str : getClassLoaderExceptions()) {
/* 112 */       if ((paramString2 != null && paramString2.startsWith(str)) || paramString1.startsWith(str)) {
/* 113 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return false;
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
/*     */   boolean isClassTransformerExcluded(String paramString1, String paramString2) {
/* 130 */     for (String str : getTransformerExceptions()) {
/* 131 */       if ((paramString2 != null && paramString2.startsWith(str)) || paramString1.startsWith(str)) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void registerInvalidClass(String paramString) {
/* 147 */     if (this.invalidClasses != null) {
/* 148 */       this.invalidClasses.add(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<String> getClassLoaderExceptions() {
/* 156 */     if (this.classLoaderExceptions != null) {
/* 157 */       return this.classLoaderExceptions;
/*     */     }
/* 159 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<String> getTransformerExceptions() {
/* 166 */     if (this.transformerExceptions != null) {
/* 167 */       return this.transformerExceptions;
/*     */     }
/* 169 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T getField(LaunchClassLoader paramLaunchClassLoader, String paramString) {
/*     */     try {
/* 175 */       Field field = LaunchClassLoader.class.getDeclaredField(paramString);
/* 176 */       field.setAccessible(true);
/* 177 */       return (T)field.get(paramLaunchClassLoader);
/* 178 */     } catch (Exception exception) {
/* 179 */       exception.printStackTrace();
/*     */       
/* 181 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\service\mojang\LaunchClassLoaderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */