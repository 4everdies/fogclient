/*    */ package org.spongepowered.asm.mixin.transformer.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
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
/*    */ 
/*    */ public class MixinReloadException
/*    */   extends MixinException
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   private final IMixinInfo mixinInfo;
/*    */   
/*    */   public MixinReloadException(IMixinInfo paramIMixinInfo, String paramString) {
/* 41 */     super(paramString);
/* 42 */     this.mixinInfo = paramIMixinInfo;
/*    */   }
/*    */   
/*    */   public IMixinInfo getMixinInfo() {
/* 46 */     return this.mixinInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\throwables\MixinReloadException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */