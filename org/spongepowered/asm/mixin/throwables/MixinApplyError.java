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
/*    */ public class MixinApplyError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MixinApplyError(String paramString) {
/* 35 */     super(paramString);
/*    */   }
/*    */   
/*    */   public MixinApplyError(Throwable paramThrowable) {
/* 39 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public MixinApplyError(String paramString, Throwable paramThrowable) {
/* 43 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\throwables\MixinApplyError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */