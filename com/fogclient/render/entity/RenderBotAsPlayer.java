/*    */ package com.fogclient.render.entity;
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import net.minecraft.client.model.ModelPlayer;
/*    */ import net.minecraft.client.renderer.entity.RenderLiving;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ public class RenderBotAsPlayer extends RenderLiving<EntityPvPBot> {
/*    */   public RenderBotAsPlayer(RenderManager paramRenderManager) {
/* 16 */     super(paramRenderManager, (ModelBase)new ModelPlayer(0.0F, false), 0.5F);
/* 17 */     this.model = (ModelPlayer)this.field_77045_g;
/*    */   }
/*    */   
/*    */   private final ModelPlayer model;
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityPvPBot paramEntityPvPBot) {
/* 23 */     return DefaultPlayerSkin.func_177335_a();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\render\entity\RenderBotAsPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */