/*    */ package org.spongepowered.asm.mixin.gen.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.gen.AccessorInfo;
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
/*    */ public class InvalidAccessorException
/*    */   extends InvalidMixinException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   private final AccessorInfo info;
/*    */   
/*    */   public InvalidAccessorException(IMixinContext paramIMixinContext, String paramString) {
/* 41 */     super(paramIMixinContext, paramString);
/* 42 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidAccessorException(AccessorInfo paramAccessorInfo, String paramString) {
/* 46 */     super(paramAccessorInfo.getContext(), paramString);
/* 47 */     this.info = paramAccessorInfo;
/*    */   }
/*    */   
/*    */   public InvalidAccessorException(IMixinContext paramIMixinContext, Throwable paramThrowable) {
/* 51 */     super(paramIMixinContext, paramThrowable);
/* 52 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidAccessorException(AccessorInfo paramAccessorInfo, Throwable paramThrowable) {
/* 56 */     super(paramAccessorInfo.getContext(), paramThrowable);
/* 57 */     this.info = paramAccessorInfo;
/*    */   }
/*    */   
/*    */   public InvalidAccessorException(IMixinContext paramIMixinContext, String paramString, Throwable paramThrowable) {
/* 61 */     super(paramIMixinContext, paramString, paramThrowable);
/* 62 */     this.info = null;
/*    */   }
/*    */   
/*    */   public InvalidAccessorException(AccessorInfo paramAccessorInfo, String paramString, Throwable paramThrowable) {
/* 66 */     super(paramAccessorInfo.getContext(), paramString, paramThrowable);
/* 67 */     this.info = paramAccessorInfo;
/*    */   }
/*    */   
/*    */   public AccessorInfo getAccessorInfo() {
/* 71 */     return this.info;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\throwables\InvalidAccessorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */