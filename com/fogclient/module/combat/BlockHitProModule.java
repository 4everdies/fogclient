/*    */ package com.fogclient.module.combat;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ public class BlockHitProModule
/*    */   extends Module {
/* 11 */   private int blockTicks = 0;
/*    */   
/*    */   public BlockHitProModule() {
/* 14 */     super("BlockHitPro", "Automatically blocks with sword after attacking", Category.COMBAT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 19 */     this.blockTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 24 */     this.blockTicks = 0;
/* 25 */     if (mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 26 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), false);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 32 */     if (mc.field_71439_g == null)
/*    */       return; 
/* 34 */     if (this.blockTicks > 0 && mc.field_71439_g.func_70694_bm() != null && mc.field_71439_g.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemSword) {
/* 35 */       if (this.blockTicks == 2) {
/* 36 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), true);
/* 37 */         KeyBinding.func_74507_a(mc.field_71474_y.field_74313_G.func_151463_i());
/*    */       } 
/* 39 */       this.blockTicks--;
/* 40 */       if (this.blockTicks == 0) {
/* 41 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), false);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEvent(Event paramEvent) {
/* 48 */     if (paramEvent instanceof AttackEntityEvent) {
/* 49 */       AttackEntityEvent attackEntityEvent = (AttackEntityEvent)paramEvent;
/* 50 */       if (attackEntityEvent.entityPlayer == mc.field_71439_g && 
/* 51 */         attackEntityEvent.entityPlayer.func_70694_bm() != null && attackEntityEvent.entityPlayer.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemSword)
/* 52 */         this.blockTicks = 2; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\BlockHitProModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */