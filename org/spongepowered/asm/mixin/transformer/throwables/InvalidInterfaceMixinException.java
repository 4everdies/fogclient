/*    */ package org.spongepowered.asm.mixin.transformer.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
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
/*    */ public class InvalidInterfaceMixinException
/*    */   extends InvalidMixinException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinInfo paramIMixinInfo, String paramString) {
/* 38 */     super(paramIMixinInfo, paramString);
/*    */   }
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinContext paramIMixinContext, String paramString) {
/* 42 */     super(paramIMixinContext, paramString);
/*    */   }
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinInfo paramIMixinInfo, Throwable paramThrowable) {
/* 46 */     super(paramIMixinInfo, paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinContext paramIMixinContext, Throwable paramThrowable) {
/* 50 */     super(paramIMixinContext, paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinInfo paramIMixinInfo, String paramString, Throwable paramThrowable) {
/* 54 */     super(paramIMixinInfo, paramString, paramThrowable);
/*    */   }
/*    */   
/*    */   public InvalidInterfaceMixinException(IMixinContext paramIMixinContext, String paramString, Throwable paramThrowable) {
/* 58 */     super(paramIMixinContext, paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\throwables\InvalidInterfaceMixinException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */