/*    */ package com.fogclient.module.combat;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoDefenderModule
/*    */   extends Module
/*    */ {
/*    */   public NoDefenderModule() {
/* 14 */     super("SemDefesa", "Impede o bloqueio com espada.", Category.COMBAT);
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
/* 29 */     if (paramEvent instanceof PlayerInteractEvent) {
/* 30 */       PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent)paramEvent;
/* 31 */       if (playerInteractEvent.entityPlayer != null && playerInteractEvent.entityPlayer.func_70694_bm() != null && playerInteractEvent.entityPlayer.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemSword) {
/* 32 */         if (playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
/* 33 */           playerInteractEvent.setCanceled(true);
/*    */         }
/* 35 */         else if (playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
/* 36 */           playerInteractEvent.useItem = Event.Result.DENY;
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLivingUpdate() {
/* 44 */     if (!isEnabled())
/*    */       return; 
/* 46 */     if (mc.field_71439_g != null && mc.field_71439_g.func_70632_aY())
/* 47 */       mc.field_71439_g.func_71041_bz(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\NoDefenderModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */