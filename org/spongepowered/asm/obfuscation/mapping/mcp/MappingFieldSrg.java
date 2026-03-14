/*    */ package org.spongepowered.asm.obfuscation.mapping.mcp;
/*    */ 
/*    */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
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
/*    */ public class MappingFieldSrg
/*    */   extends MappingField
/*    */ {
/*    */   private final String srg;
/*    */   
/*    */   public MappingFieldSrg(String paramString) {
/* 37 */     super(getOwnerFromSrg(paramString), getNameFromSrg(paramString), null);
/* 38 */     this.srg = paramString;
/*    */   }
/*    */   
/*    */   public MappingFieldSrg(MappingField paramMappingField) {
/* 42 */     super(paramMappingField.getOwner(), paramMappingField.getName(), null);
/* 43 */     this.srg = paramMappingField.getOwner() + "/" + paramMappingField.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String serialise() {
/* 48 */     return this.srg;
/*    */   }
/*    */   
/*    */   private static String getNameFromSrg(String paramString) {
/* 52 */     if (paramString == null) {
/* 53 */       return null;
/*    */     }
/* 55 */     int i = paramString.lastIndexOf('/');
/* 56 */     return (i > -1) ? paramString.substring(i + 1) : paramString;
/*    */   }
/*    */   
/*    */   private static String getOwnerFromSrg(String paramString) {
/* 60 */     if (paramString == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     int i = paramString.lastIndexOf('/');
/* 64 */     return (i > -1) ? paramString.substring(0, i) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\obfuscation\mapping\mcp\MappingFieldSrg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */