/*    */ package org.spongepowered.asm.mixin.injection.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.injection.code.ISliceContext;
/*    */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
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
/*    */ public class InvalidSliceException
/*    */   extends InvalidInjectionException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public InvalidSliceException(IMixinContext paramIMixinContext, String paramString) {
/* 39 */     super(paramIMixinContext, paramString);
/*    */   }
/*    */   
/*    */   public InvalidSliceException(ISliceContext paramISliceContext, String paramString) {
/* 43 */     super(paramISliceContext.getContext(), paramString);
/*    */   }
/*    */   
/*    */   public InvalidSliceException(IMixinContext paramIMixinContext, Throwable paramThrowable) {
/* 47 */     super(paramIMixinContext, paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidSliceException(ISliceContext paramISliceContext, Throwable paramThrowable) {
/* 51 */     super(paramISliceContext.getContext(), paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidSliceException(IMixinContext paramIMixinContext, String paramString, Throwable paramThrowable) {
/* 55 */     super(paramIMixinContext, paramString, paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidSliceException(ISliceContext paramISliceContext, String paramString, Throwable paramThrowable) {
/* 59 */     super(paramISliceContext.getContext(), paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\throwables\InvalidSliceException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */