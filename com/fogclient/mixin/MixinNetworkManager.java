/*    */ package com.fogclient.mixin;
/*    */ 
/*    */ import com.fogclient.module.combat.ghostreach.GhostNettyHandler;
/*    */ import com.fogclient.module.combat.ghostreach.GhostReachCore;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({NetworkManager.class})
/*    */ public class MixinNetworkManager {
/*    */   @Inject(method = {"channelActive"}, at = {@At("RETURN")}, remap = false)
/*    */   private void onChannelActive(ChannelHandlerContext paramChannelHandlerContext, CallbackInfo paramCallbackInfo) {
/* 18 */     ChannelPipeline channelPipeline = paramChannelHandlerContext.pipeline();
/* 19 */     if (channelPipeline.get("ghost_handler") == null)
/* 20 */       channelPipeline.addBefore("packet_handler", "ghost_handler", (ChannelHandler)new GhostNettyHandler(GhostReachCore.INSTANCE)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\mixin\MixinNetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */