/*    */ package com.fogclient;
/*    */ 
/*    */ import com.fogclient.a.e;
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import com.fogclient.render.entity.RenderBotAsPlayer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.client.registry.RenderingRegistry;
/*    */ 
/*    */ public class ClientProxy extends CommonProxy {
/*    */   public void registerRenderers() {
/* 13 */     RenderingRegistry.registerEntityRenderingHandler(EntityPvPBot.class, (Render)new RenderBotAsPlayer(Minecraft.func_71410_x().func_175598_ae()));
/*    */ 
/*    */     
/* 16 */     MinecraftForge.EVENT_BUS.register(new e());
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\ClientProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */