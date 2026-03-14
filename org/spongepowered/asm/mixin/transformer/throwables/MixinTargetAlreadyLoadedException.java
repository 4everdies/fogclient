/*    */ package org.spongepowered.asm.mixin.transformer.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
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
/*    */ public class MixinTargetAlreadyLoadedException
/*    */   extends InvalidMixinException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String target;
/*    */   
/*    */   public MixinTargetAlreadyLoadedException(IMixinInfo paramIMixinInfo, String paramString1, String paramString2) {
/* 39 */     super(paramIMixinInfo, paramString1);
/* 40 */     this.target = paramString2;
/*    */   }
/*    */   
/*    */   public MixinTargetAlreadyLoadedException(IMixinInfo paramIMixinInfo, String paramString1, String paramString2, Throwable paramThrowable) {
/* 44 */     super(paramIMixinInfo, paramString1, paramThrowable);
/* 45 */     this.target = paramString2;
/*    */   }
/*    */   
/*    */   public String getTarget() {
/* 49 */     return this.target;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\throwables\MixinTargetAlreadyLoadedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */