/*    */ package com.fogclient.module.misc;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.BooleanSetting;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class TankoSimModule
/*    */   extends Module
/*    */ {
/* 15 */   private final BooleanSetting woodShovel = new BooleanSetting("PÃ¡ de Madeira", this, true);
/* 16 */   private final BooleanSetting stoneShovel = new BooleanSetting("PÃ¡ de Pedra", this, false);
/* 17 */   private final BooleanSetting shears = new BooleanSetting("Tesouras", this, false);
/* 18 */   private final BooleanSetting armor = new BooleanSetting("Armaduras", this, false);
/*    */   
/*    */   public TankoSimModule() {
/* 21 */     super("TankoSIM", "Drops wooden shovels automatically from inventory", Category.MISC);
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
/*    */   public void onTick() {
/* 36 */     if (!isEnabled())
/*    */       return; 
/* 38 */     if (mc.field_71439_g == null || mc.field_71439_g.field_71069_bz == null)
/*    */       return; 
/* 40 */     for (byte b = 0; b < mc.field_71439_g.field_71069_bz.field_75151_b.size(); b++) {
/* 41 */       Slot slot = mc.field_71439_g.field_71069_bz.field_75151_b.get(b);
/* 42 */       if (!slot.func_75216_d())
/*    */         continue; 
/* 44 */       ItemStack itemStack = slot.func_75211_c();
/* 45 */       Item item = itemStack.func_77973_b();
/*    */       
/* 47 */       boolean bool = false;
/*    */       
/* 49 */       if (this.woodShovel.getValue() && item == Items.field_151038_n) {
/* 50 */         bool = true;
/* 51 */       } else if (this.stoneShovel.getValue() && item == Items.field_151051_r) {
/* 52 */         bool = true;
/* 53 */       } else if (this.shears.getValue() && item == Items.field_151097_aZ) {
/* 54 */         bool = true;
/* 55 */       } else if (this.armor.getValue() && item instanceof net.minecraft.item.ItemArmor) {
/*    */ 
/*    */         
/* 58 */         if (b >= 5 && b <= 8) {
/*    */           continue;
/*    */         }
/*    */ 
/*    */         
/* 63 */         bool = true;
/*    */       } 
/*    */       
/* 66 */       if (bool)
/*    */       {
/* 68 */         mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71069_bz.field_75152_c, b, 1, 4, (EntityPlayer)mc.field_71439_g);
/*    */       }
/*    */       continue;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\TankoSimModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */