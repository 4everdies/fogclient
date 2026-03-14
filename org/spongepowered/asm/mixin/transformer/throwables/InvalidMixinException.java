/*    */ package org.spongepowered.asm.mixin.transformer.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*    */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*    */ import org.spongepowered.asm.mixin.throwables.MixinException;
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
/*    */ public class InvalidMixinException
/*    */   extends MixinException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   private final IMixinInfo mixin;
/*    */   
/*    */   public InvalidMixinException(IMixinInfo paramIMixinInfo, String paramString) {
/* 41 */     super(paramString);
/* 42 */     this.mixin = paramIMixinInfo;
/*    */   }
/*    */   
/*    */   public InvalidMixinException(IMixinContext paramIMixinContext, String paramString) {
/* 46 */     this(paramIMixinContext.getMixin(), paramString);
/*    */   }
/*    */   
/*    */   public InvalidMixinException(IMixinInfo paramIMixinInfo, Throwable paramThrowable) {
/* 50 */     super(paramThrowable);
/* 51 */     this.mixin = paramIMixinInfo;
/*    */   }
/*    */   
/*    */   public InvalidMixinException(IMixinContext paramIMixinContext, Throwable paramThrowable) {
/* 55 */     this(paramIMixinContext.getMixin(), paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidMixinException(IMixinInfo paramIMixinInfo, String paramString, Throwable paramThrowable) {
/* 59 */     super(paramString, paramThrowable);
/* 60 */     this.mixin = paramIMixinInfo;
/*    */   }
/*    */   
/*    */   public InvalidMixinException(IMixinContext paramIMixinContext, String paramString, Throwable paramThrowable) {
/* 64 */     super(paramString, paramThrowable);
/* 65 */     this.mixin = paramIMixinContext.getMixin();
/*    */   }
/*    */   
/*    */   public IMixinInfo getMixin() {
/* 69 */     return this.mixin;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\throwables\InvalidMixinException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */