/*    */ package org.spongepowered.asm.mixin.transformer.throwables;
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
/*    */ public class MixinTransformerError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MixinTransformerError(String paramString) {
/* 35 */     super(paramString);
/*    */   }
/*    */   
/*    */   public MixinTransformerError(Throwable paramThrowable) {
/* 39 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public MixinTransformerError(String paramString, Throwable paramThrowable) {
/* 43 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\throwables\MixinTransformerError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */