/*     */ package org.spongepowered.asm.mixin.transformer.debug;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.io.Files;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.jetbrains.java.decompiler.main.Fernflower;
/*     */ import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
/*     */ import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
/*     */ import org.jetbrains.java.decompiler.main.extern.IResultSaver;
/*     */ import org.jetbrains.java.decompiler.util.InterpreterUtil;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
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
/*     */ public class RuntimeDecompiler
/*     */   extends IFernflowerLogger
/*     */   implements IResultSaver, IDecompiler
/*     */ {
/*  52 */   private static final Level[] SEVERITY_LEVELS = new Level[] { Level.TRACE, Level.INFO, Level.WARN, Level.ERROR };
/*     */   
/*  54 */   private final Map<String, Object> options = (Map<String, Object>)ImmutableMap.builder()
/*  55 */     .put("din", "0").put("rbr", "0").put("dgs", "1").put("asc", "1")
/*  56 */     .put("den", "1").put("hdc", "1").put("ind", "    ").build();
/*     */   
/*     */   private final File outputPath;
/*     */   
/*  60 */   protected final Logger logger = LogManager.getLogger("fernflower");
/*     */   
/*     */   public RuntimeDecompiler(File paramFile) {
/*  63 */     this.outputPath = paramFile;
/*  64 */     if (this.outputPath.exists()) {
/*     */       try {
/*  66 */         FileUtils.deleteDirectory(this.outputPath);
/*  67 */       } catch (IOException iOException) {
/*  68 */         this.logger.warn("Error cleaning output directory: {}", new Object[] { iOException.getMessage() });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void decompile(File paramFile) {
/*     */     try {
/*  76 */       Fernflower fernflower = new Fernflower(new IBytecodeProvider()
/*     */           {
/*     */             private byte[] byteCode;
/*     */ 
/*     */             
/*     */             public byte[] getBytecode(String param1String1, String param1String2) throws IOException {
/*  82 */               if (this.byteCode == null) {
/*  83 */                 this.byteCode = InterpreterUtil.getBytes(new File(param1String1));
/*     */               }
/*  85 */               return this.byteCode;
/*     */             }
/*     */           },  this, this.options, this);
/*     */ 
/*     */       
/*  90 */       fernflower.getStructContext().addSpace(paramFile, true);
/*  91 */       fernflower.decompileContext();
/*  92 */     } catch (Throwable throwable) {
/*  93 */       this.logger.warn("Decompilation error while processing {}", new Object[] { paramFile.getName() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveFolder(String paramString) {}
/*     */ 
/*     */   
/*     */   public void saveClassFile(String paramString1, String paramString2, String paramString3, String paramString4, int[] paramArrayOfint) {
/* 103 */     File file = new File(this.outputPath, paramString2 + ".java");
/* 104 */     file.getParentFile().mkdirs();
/*     */     try {
/* 106 */       this.logger.info("Writing {}", new Object[] { file.getAbsolutePath() });
/* 107 */       Files.write(paramString4, file, Charsets.UTF_8);
/* 108 */     } catch (IOException iOException) {
/* 109 */       writeMessage("Cannot write source file " + file, iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startReadingClass(String paramString) {
/* 115 */     this.logger.info("Decompiling {}", new Object[] { paramString });
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeMessage(String paramString, IFernflowerLogger.Severity paramSeverity) {
/* 120 */     this.logger.log(SEVERITY_LEVELS[paramSeverity.ordinal()], paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeMessage(String paramString, Throwable paramThrowable) {
/* 125 */     this.logger.warn("{} {}: {}", new Object[] { paramString, paramThrowable.getClass().getSimpleName(), paramThrowable.getMessage() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeMessage(String paramString, IFernflowerLogger.Severity paramSeverity, Throwable paramThrowable) {
/* 130 */     this.logger.log(SEVERITY_LEVELS[paramSeverity.ordinal()], paramString, paramThrowable);
/*     */   }
/*     */   
/*     */   public void copyFile(String paramString1, String paramString2, String paramString3) {}
/*     */   
/*     */   public void createArchive(String paramString1, String paramString2, Manifest paramManifest) {}
/*     */   
/*     */   public void saveDirEntry(String paramString1, String paramString2, String paramString3) {}
/*     */   
/*     */   public void copyEntry(String paramString1, String paramString2, String paramString3, String paramString4) {}
/*     */   
/*     */   public void saveClassEntry(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {}
/*     */   
/*     */   public void closeArchive(String paramString1, String paramString2) {}
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\debug\RuntimeDecompiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */