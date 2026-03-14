/*    */ package com.fogclient.module.combat;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*    */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ public class DefaultSoupModule
/*    */   extends Module
/*    */ {
/*    */   public DefaultSoupModule() {
/* 18 */     super("SopaPadrao", "Consome sopa automaticamente.", Category.COMBAT);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEvent(Event paramEvent) {
/* 33 */     if (paramEvent instanceof PlayerInteractEvent) {
/* 34 */       onInteract((PlayerInteractEvent)paramEvent);
/*    */     }
/*    */   }
/*    */   
/*    */   private void onInteract(PlayerInteractEvent paramPlayerInteractEvent) {
/* 39 */     if (!isEnabled())
/*    */       return; 
/* 41 */     EntityPlayer entityPlayer = paramPlayerInteractEvent.entityPlayer;
/* 42 */     if (entityPlayer == null)
/*    */       return; 
/* 44 */     if (paramPlayerInteractEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR && paramPlayerInteractEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
/*    */       return; 
/* 46 */     ItemStack itemStack = entityPlayer.func_70694_bm();
/* 47 */     if (itemStack == null || itemStack.func_77973_b() != Items.field_151009_A)
/*    */       return; 
/* 49 */     if (entityPlayer.func_110143_aJ() >= entityPlayer.func_110138_aP())
/*    */       return; 
/* 51 */     paramPlayerInteractEvent.setCanceled(true);
/*    */     
/* 53 */     if (entityPlayer.field_70170_p.field_72995_K) {
/*    */       
/* 55 */       Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(itemStack));
/*    */       
/* 57 */       float f = Math.min(7.0F, entityPlayer.func_110138_aP() - entityPlayer.func_110143_aJ());
/* 58 */       if (f > 0.0F) {
/* 59 */         entityPlayer.func_70606_j(entityPlayer.func_110143_aJ() + f);
/*    */       }
/*    */       
/* 62 */       if (!entityPlayer.field_71075_bZ.field_75098_d) {
/* 63 */         entityPlayer.field_71071_by.func_70299_a(entityPlayer.field_71071_by.field_70461_c, new ItemStack(Items.field_151054_z));
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 68 */       float f = Math.min(7.0F, entityPlayer.func_110138_aP() - entityPlayer.func_110143_aJ());
/* 69 */       if (f > 0.0F) {
/* 70 */         entityPlayer.func_70606_j(entityPlayer.func_110143_aJ() + f);
/*    */       }
/*    */       
/* 73 */       if (!entityPlayer.field_71075_bZ.field_75098_d) {
/* 74 */         entityPlayer.field_71071_by.func_70299_a(entityPlayer.field_71071_by.field_70461_c, new ItemStack(Items.field_151054_z));
/* 75 */         entityPlayer.field_71069_bz.func_75142_b();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\DefaultSoupModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */