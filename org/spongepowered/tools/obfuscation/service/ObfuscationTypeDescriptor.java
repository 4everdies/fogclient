/*     */ package org.spongepowered.tools.obfuscation.service;
/*     */ 
/*     */ import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObfuscationTypeDescriptor
/*     */ {
/*     */   private final String key;
/*     */   private final String inputFileArgName;
/*     */   private final String extraInputFilesArgName;
/*     */   private final String outFileArgName;
/*     */   private final Class<? extends ObfuscationEnvironment> environmentType;
/*     */   
/*     */   public ObfuscationTypeDescriptor(String paramString1, String paramString2, String paramString3, Class<? extends ObfuscationEnvironment> paramClass) {
/*  63 */     this(paramString1, paramString2, null, paramString3, paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObfuscationTypeDescriptor(String paramString1, String paramString2, String paramString3, String paramString4, Class<? extends ObfuscationEnvironment> paramClass) {
/*  68 */     this.key = paramString1;
/*  69 */     this.inputFileArgName = paramString2;
/*  70 */     this.extraInputFilesArgName = paramString3;
/*  71 */     this.outFileArgName = paramString4;
/*  72 */     this.environmentType = paramClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getKey() {
/*  79 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInputFileOption() {
/*  86 */     return this.inputFileArgName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtraInputFilesOption() {
/*  93 */     return this.extraInputFilesArgName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOutputFileOption() {
/* 100 */     return this.outFileArgName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends ObfuscationEnvironment> getEnvironmentType() {
/* 107 */     return this.environmentType;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\service\ObfuscationTypeDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */