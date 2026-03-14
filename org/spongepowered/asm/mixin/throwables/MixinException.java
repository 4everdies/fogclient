/*    */ package org.spongepowered.asm.mixin.throwables;
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
/*    */ public class MixinException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MixinException() {}
/*    */   
/*    */   public MixinException(String paramString) {
/* 38 */     super(paramString);
/*    */   }
/*    */   
/*    */   public MixinException(Throwable paramThrowable) {
/* 42 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public MixinException(String paramString, Throwable paramThrowable) {
/* 46 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\throwables\MixinException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */