/*    */ package com.fogclient.mixin;
/*    */ 
/*    */ import com.fogclient.module.ModuleManager;
/*    */ import com.fogclient.module.combat.GhostReachModule;
/*    */ import com.fogclient.module.combat.ghostreach.GhostReachCore;
/*    */ import net.minecraft.client.renderer.EntityRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({EntityRenderer.class})
/*    */ public abstract class MixinEntityRenderer
/*    */ {
/*    */   @Inject(method = {"func_78473_a", "getMouseOver"}, at = {@At("HEAD")}, remap = false)
/*    */   private void onGetMouseOver(float paramFloat, CallbackInfo paramCallbackInfo) {
/* 20 */     GhostReachModule ghostReachModule = (GhostReachModule)ModuleManager.getInstance().getModule(GhostReachModule.class);
/* 21 */     if (ghostReachModule != null && ghostReachModule.isEnabled()) {
/* 22 */       GhostReachCore ghostReachCore = GhostReachCore.INSTANCE;
/* 23 */       Entity entity = ghostReachCore.findTarget(GhostReachCore.maxReach);
/*    */       
/* 25 */       if (entity != null && ghostReachCore.checkConditions(entity)) {
/* 26 */         ghostReachCore.targetEntityId = entity.func_145782_y();
/*    */       } else {
/* 28 */         ghostReachCore.targetEntityId = -1;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\mixin\MixinEntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */