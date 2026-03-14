/*     */ package org.spongepowered.asm.util;
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
/*     */ public abstract class ObfuscationUtil
/*     */ {
/*     */   public static String mapDescriptor(String paramString, IClassRemapper paramIClassRemapper) {
/*  65 */     return remapDescriptor(paramString, paramIClassRemapper, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unmapDescriptor(String paramString, IClassRemapper paramIClassRemapper) {
/*  76 */     return remapDescriptor(paramString, paramIClassRemapper, true);
/*     */   }
/*     */   
/*     */   private static String remapDescriptor(String paramString, IClassRemapper paramIClassRemapper, boolean paramBoolean) {
/*  80 */     StringBuilder stringBuilder1 = new StringBuilder();
/*  81 */     StringBuilder stringBuilder2 = null;
/*     */     
/*  83 */     for (byte b = 0; b < paramString.length(); b++) {
/*  84 */       char c = paramString.charAt(b);
/*  85 */       if (stringBuilder2 != null) {
/*  86 */         if (c == ';') {
/*  87 */           stringBuilder1.append('L').append(remap(stringBuilder2.toString(), paramIClassRemapper, paramBoolean)).append(';');
/*  88 */           stringBuilder2 = null;
/*     */         } else {
/*  90 */           stringBuilder2.append(c);
/*     */         }
/*     */       
/*     */       }
/*  94 */       else if (c == 'L') {
/*  95 */         stringBuilder2 = new StringBuilder();
/*     */       } else {
/*  97 */         stringBuilder1.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     if (stringBuilder2 != null) {
/* 102 */       throw new IllegalArgumentException("Invalid descriptor '" + paramString + "', missing ';'");
/*     */     }
/*     */     
/* 105 */     return stringBuilder1.toString();
/*     */   }
/*     */   
/*     */   private static Object remap(String paramString, IClassRemapper paramIClassRemapper, boolean paramBoolean) {
/* 109 */     String str = paramBoolean ? paramIClassRemapper.unmap(paramString) : paramIClassRemapper.map(paramString);
/* 110 */     return (str != null) ? str : paramString;
/*     */   }
/*     */   
/*     */   public static interface IClassRemapper {
/*     */     String map(String param1String);
/*     */     
/*     */     String unmap(String param1String);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\ObfuscationUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */