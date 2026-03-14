/*     */ package org.spongepowered.tools.obfuscation.mapping.mcp;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.common.io.LineProcessor;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.annotation.processing.Filer;
/*     */ import javax.annotation.processing.Messager;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.asm.obfuscation.mapping.mcp.MappingFieldSrg;
/*     */ import org.spongepowered.tools.obfuscation.mapping.common.MappingProvider;
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
/*     */ public class MappingProviderSrg
/*     */   extends MappingProvider
/*     */ {
/*     */   public MappingProviderSrg(Messager paramMessager, Filer paramFiler) {
/*  53 */     super(paramMessager, paramFiler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(final File input) throws IOException {
/*  59 */     final BiMap packageMap = this.packageMap;
/*  60 */     final BiMap classMap = this.classMap;
/*  61 */     final BiMap fieldMap = this.fieldMap;
/*  62 */     final BiMap methodMap = this.methodMap;
/*     */     
/*  64 */     Files.readLines(input, Charset.defaultCharset(), new LineProcessor<String>()
/*     */         {
/*     */           public String getResult() {
/*  67 */             return null;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean processLine(String param1String) throws IOException {
/*  72 */             if (Strings.isNullOrEmpty(param1String) || param1String.startsWith("#")) {
/*  73 */               return true;
/*     */             }
/*     */             
/*  76 */             String str = param1String.substring(0, 2);
/*  77 */             String[] arrayOfString = param1String.substring(4).split(" ");
/*     */             
/*  79 */             if (str.equals("PK")) {
/*  80 */               packageMap.forcePut(arrayOfString[0], arrayOfString[1]);
/*  81 */             } else if (str.equals("CL")) {
/*  82 */               classMap.forcePut(arrayOfString[0], arrayOfString[1]);
/*  83 */             } else if (str.equals("FD")) {
/*  84 */               fieldMap.forcePut((new MappingFieldSrg(arrayOfString[0])).copy(), (new MappingFieldSrg(arrayOfString[1])).copy());
/*  85 */             } else if (str.equals("MD")) {
/*  86 */               methodMap.forcePut(new MappingMethod(arrayOfString[0], arrayOfString[1]), new MappingMethod(arrayOfString[2], arrayOfString[3]));
/*     */             } else {
/*  88 */               throw new MixinException("Invalid SRG file: " + input);
/*     */             } 
/*     */             
/*  91 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField getFieldMapping(MappingField paramMappingField) {
/*     */     MappingFieldSrg mappingFieldSrg;
/*  99 */     if (paramMappingField.getDesc() != null) {
/* 100 */       mappingFieldSrg = new MappingFieldSrg(paramMappingField);
/*     */     }
/* 102 */     return (MappingField)this.fieldMap.get(mappingFieldSrg);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mapping\mcp\MappingProviderSrg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */