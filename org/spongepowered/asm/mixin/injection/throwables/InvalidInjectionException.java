/*    */ package org.spongepowered.asm.mixin.injection.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*    */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*    */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
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
/*    */ public class InvalidInjectionException
/*    */   extends InvalidMixinException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   private final InjectionInfo info;
/*    */   
/*    */   public InvalidInjectionException(IMixinContext paramIMixinContext, String paramString) {
/* 42 */     super(paramIMixinContext, paramString);
/* 43 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidInjectionException(InjectionInfo paramInjectionInfo, String paramString) {
/* 47 */     super(paramInjectionInfo.getContext(), paramString);
/* 48 */     this.info = paramInjectionInfo;
/*    */   }
/*    */   
/*    */   public InvalidInjectionException(IMixinContext paramIMixinContext, Throwable paramThrowable) {
/* 52 */     super(paramIMixinContext, paramThrowable);
/* 53 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidInjectionException(InjectionInfo paramInjectionInfo, Throwable paramThrowable) {
/* 57 */     super(paramInjectionInfo.getContext(), paramThrowable);
/* 58 */     this.info = paramInjectionInfo;
/*    */   }
/*    */   
/*    */   public InvalidInjectionException(IMixinContext paramIMixinContext, String paramString, Throwable paramThrowable) {
/* 62 */     super(paramIMixinContext, paramString, paramThrowable);
/* 63 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidInjectionException(InjectionInfo paramInjectionInfo, String paramString, Throwable paramThrowable) {
/* 67 */     super(paramInjectionInfo.getContext(), paramString, paramThrowable);
/* 68 */     this.info = paramInjectionInfo;
/*    */   }
/*    */   
/*    */   public InjectionInfo getInjectionInfo() {
/* 72 */     return this.info;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\throwables\InvalidInjectionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */