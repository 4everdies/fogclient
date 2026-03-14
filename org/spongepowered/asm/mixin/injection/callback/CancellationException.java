/*    */ package org.spongepowered.asm.mixin.injection.callback;
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
/*    */ public class CancellationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public CancellationException() {}
/*    */   
/*    */   public CancellationException(String paramString) {
/* 39 */     super(paramString);
/*    */   }
/*    */   
/*    */   public CancellationException(Throwable paramThrowable) {
/* 43 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public CancellationException(String paramString, Throwable paramThrowable) {
/* 47 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\callback\CancellationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */