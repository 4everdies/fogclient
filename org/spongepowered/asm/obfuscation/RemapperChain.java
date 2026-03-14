/*     */ package org.spongepowered.asm.obfuscation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.mixin.extensibility.IRemapper;
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
/*     */ public class RemapperChain
/*     */   implements IRemapper
/*     */ {
/*  38 */   private final List<IRemapper> remappers = new ArrayList<IRemapper>();
/*     */ 
/*     */   
/*     */   public String toString() {
/*  42 */     return String.format("RemapperChain[%d]", new Object[] { Integer.valueOf(this.remappers.size()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemapperChain add(IRemapper paramIRemapper) {
/*  52 */     this.remappers.add(paramIRemapper);
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String mapMethodName(String paramString1, String paramString2, String paramString3) {
/*  58 */     for (IRemapper iRemapper : this.remappers) {
/*  59 */       String str = iRemapper.mapMethodName(paramString1, paramString2, paramString3);
/*  60 */       if (str != null && !str.equals(paramString2)) {
/*  61 */         paramString2 = str;
/*     */       }
/*     */     } 
/*  64 */     return paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String mapFieldName(String paramString1, String paramString2, String paramString3) {
/*  69 */     for (IRemapper iRemapper : this.remappers) {
/*  70 */       String str = iRemapper.mapFieldName(paramString1, paramString2, paramString3);
/*  71 */       if (str != null && !str.equals(paramString2)) {
/*  72 */         paramString2 = str;
/*     */       }
/*     */     } 
/*  75 */     return paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String map(String paramString) {
/*  80 */     for (IRemapper iRemapper : this.remappers) {
/*  81 */       String str = iRemapper.map(paramString);
/*  82 */       if (str != null && !str.equals(paramString)) {
/*  83 */         paramString = str;
/*     */       }
/*     */     } 
/*  86 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String unmap(String paramString) {
/*  91 */     for (IRemapper iRemapper : this.remappers) {
/*  92 */       String str = iRemapper.unmap(paramString);
/*  93 */       if (str != null && !str.equals(paramString)) {
/*  94 */         paramString = str;
/*     */       }
/*     */     } 
/*  97 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String mapDesc(String paramString) {
/* 102 */     for (IRemapper iRemapper : this.remappers) {
/* 103 */       String str = iRemapper.mapDesc(paramString);
/* 104 */       if (str != null && !str.equals(paramString)) {
/* 105 */         paramString = str;
/*     */       }
/*     */     } 
/* 108 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String unmapDesc(String paramString) {
/* 113 */     for (IRemapper iRemapper : this.remappers) {
/* 114 */       String str = iRemapper.unmapDesc(paramString);
/* 115 */       if (str != null && !str.equals(paramString)) {
/* 116 */         paramString = str;
/*     */       }
/*     */     } 
/* 119 */     return paramString;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\obfuscation\RemapperChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */