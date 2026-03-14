/*    */ package org.spongepowered.asm.lib.commons;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleRemapper
/*    */   extends Remapper
/*    */ {
/*    */   private final Map<String, String> mapping;
/*    */   
/*    */   public SimpleRemapper(Map<String, String> paramMap) {
/* 46 */     this.mapping = paramMap;
/*    */   }
/*    */   
/*    */   public SimpleRemapper(String paramString1, String paramString2) {
/* 50 */     this.mapping = Collections.singletonMap(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapMethodName(String paramString1, String paramString2, String paramString3) {
/* 55 */     String str = map(paramString1 + '.' + paramString2 + paramString3);
/* 56 */     return (str == null) ? paramString2 : str;
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapInvokeDynamicMethodName(String paramString1, String paramString2) {
/* 61 */     String str = map('.' + paramString1 + paramString2);
/* 62 */     return (str == null) ? paramString1 : str;
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapFieldName(String paramString1, String paramString2, String paramString3) {
/* 67 */     String str = map(paramString1 + '.' + paramString2);
/* 68 */     return (str == null) ? paramString2 : str;
/*    */   }
/*    */ 
/*    */   
/*    */   public String map(String paramString) {
/* 73 */     return this.mapping.get(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\SimpleRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */