/*     */ package org.spongepowered.asm.mixin.transformer.ext.extensions;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IExtension;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
/*     */ import org.spongepowered.asm.util.Constants;
/*     */ import org.spongepowered.asm.util.perf.Profiler;
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
/*     */ public class ExtensionClassExporter
/*     */   implements IExtension
/*     */ {
/*     */   private static final String DECOMPILER_CLASS = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler";
/*     */   private static final String EXPORT_CLASS_DIR = "class";
/*     */   private static final String EXPORT_JAVA_DIR = "java";
/*  56 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private final File classExportDir = new File(Constants.DEBUG_OUTPUT_DIR, "class");
/*     */ 
/*     */   
/*     */   private final IDecompiler decompiler;
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionClassExporter(MixinEnvironment paramMixinEnvironment) {
/*  69 */     this.decompiler = initDecompiler(paramMixinEnvironment, new File(Constants.DEBUG_OUTPUT_DIR, "java"));
/*     */     
/*     */     try {
/*  72 */       FileUtils.deleteDirectory(this.classExportDir);
/*  73 */     } catch (IOException iOException) {
/*  74 */       logger.warn("Error cleaning class output directory: {}", new Object[] { iOException.getMessage() });
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDecompilerActive() {
/*  79 */     return (this.decompiler != null);
/*     */   }
/*     */   
/*     */   private IDecompiler initDecompiler(MixinEnvironment paramMixinEnvironment, File paramFile) {
/*  83 */     if (!paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE)) {
/*  84 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  88 */       boolean bool = paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_THREADED);
/*  89 */       logger.info("Attempting to load Fernflower decompiler{}", new Object[] { bool ? " (Threaded mode)" : "" });
/*  90 */       String str = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler" + (bool ? "Async" : "");
/*     */       
/*  92 */       Class<?> clazz = Class.forName(str);
/*  93 */       Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { File.class });
/*  94 */       IDecompiler iDecompiler = (IDecompiler)constructor.newInstance(new Object[] { paramFile });
/*  95 */       logger.info("Fernflower decompiler was successfully initialised, exported classes will be decompiled{}", new Object[] { bool ? " in a separate thread" : "" });
/*     */       
/*  97 */       return iDecompiler;
/*  98 */     } catch (Throwable throwable) {
/*  99 */       logger.info("Fernflower could not be loaded, exported classes will not be decompiled. {}: {}", new Object[] { throwable
/* 100 */             .getClass().getSimpleName(), throwable.getMessage() });
/*     */       
/* 102 */       return null;
/*     */     } 
/*     */   }
/*     */   private String prepareFilter(String paramString) {
/* 106 */     paramString = "^\\Q" + paramString.replace("**", "").replace("*", "").replace("?", "") + "\\E$";
/* 107 */     return paramString.replace("", "\\E.*\\Q").replace("", "\\E[^\\.]+\\Q").replace("", "\\E.\\Q").replace("\\Q\\E", "");
/*     */   }
/*     */   
/*     */   private boolean applyFilter(String paramString1, String paramString2) {
/* 111 */     return Pattern.compile(prepareFilter(paramString1), 2).matcher(paramString2).matches();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkActive(MixinEnvironment paramMixinEnvironment) {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void preApply(ITargetClassContext paramITargetClassContext) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void postApply(ITargetClassContext paramITargetClassContext) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void export(MixinEnvironment paramMixinEnvironment, String paramString, boolean paramBoolean, byte[] paramArrayOfbyte) {
/* 130 */     if (paramBoolean || paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
/* 131 */       String str = paramMixinEnvironment.getOptionValue(MixinEnvironment.Option.DEBUG_EXPORT_FILTER);
/* 132 */       if (paramBoolean || str == null || applyFilter(str, paramString)) {
/* 133 */         Profiler.Section section = MixinEnvironment.getProfiler().begin("debug.export");
/* 134 */         File file = dumpClass(paramString.replace('.', '/'), paramArrayOfbyte);
/* 135 */         if (this.decompiler != null) {
/* 136 */           this.decompiler.decompile(file);
/*     */         }
/* 138 */         section.end();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File dumpClass(String paramString, byte[] paramArrayOfbyte) {
/* 151 */     File file = new File(this.classExportDir, paramString + ".class");
/*     */     try {
/* 153 */       FileUtils.writeByteArrayToFile(file, paramArrayOfbyte);
/* 154 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 157 */     return file;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\ext\extensions\ExtensionClassExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */