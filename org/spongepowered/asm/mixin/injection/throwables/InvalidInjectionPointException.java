/*    */ package org.spongepowered.asm.mixin.injection.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
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
/*    */ public class InvalidInjectionPointException
/*    */   extends InvalidInjectionException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   
/*    */   public InvalidInjectionPointException(IMixinContext paramIMixinContext, String paramString, Object... paramVarArgs) {
/* 38 */     super(paramIMixinContext, String.format(paramString, paramVarArgs));
/*    */   }
/*    */   
/*    */   public InvalidInjectionPointException(InjectionInfo paramInjectionInfo, String paramString, Object... paramVarArgs) {
/* 42 */     super(paramInjectionInfo, String.format(paramString, paramVarArgs));
/*    */   }
/*    */   
/*    */   public InvalidInjectionPointException(IMixinContext paramIMixinContext, Throwable paramThrowable, String paramString, Object... paramVarArgs) {
/* 46 */     super(paramIMixinContext, String.format(paramString, paramVarArgs), paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidInjectionPointException(InjectionInfo paramInjectionInfo, Throwable paramThrowable, String paramString, Object... paramVarArgs) {
/* 50 */     super(paramInjectionInfo, String.format(paramString, paramVarArgs), paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\throwables\InvalidInjectionPointException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */