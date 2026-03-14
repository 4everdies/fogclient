/*    */ package com.fogclient.module.misc;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.item.ItemSword;
/*    */ import net.minecraft.item.ItemTool;
/*    */ 
/*    */ public class DiamondCollectorModule extends Module {
/* 16 */   private final NumberSetting delay = new NumberSetting("Delay (ms)", this, 100.0D, 0.0D, 1000.0D, 10.0D);
/* 17 */   private long lastClickTime = 0L;
/*    */   
/*    */   public DiamondCollectorModule() {
/* 20 */     super("DiamondCollector", "Automatically steals diamond items from chests.", Category.MISC);
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
/* 35 */     if (!isEnabled())
/*    */       return; 
/* 37 */     if (mc.field_71439_g == null || mc.field_71442_b == null)
/*    */       return; 
/* 39 */     if (mc.field_71439_g.field_71070_bA != null && mc.field_71439_g.field_71070_bA instanceof ContainerChest) {
/* 40 */       ContainerChest containerChest = (ContainerChest)mc.field_71439_g.field_71070_bA;
/* 41 */       int i = containerChest.func_85151_d().func_70302_i_();
/*    */       
/* 43 */       if ((System.currentTimeMillis() - this.lastClickTime) < this.delay.getValue()) {
/*    */         return;
/*    */       }
/*    */       
/* 47 */       for (byte b = 0; b < i; b++) {
/* 48 */         ItemStack itemStack = containerChest.func_85151_d().func_70301_a(b);
/* 49 */         if (itemStack != null && isDiamond(itemStack.func_77973_b())) {
/*    */           
/* 51 */           mc.field_71442_b.func_78753_a(containerChest.field_75152_c, b, 0, 1, (EntityPlayer)mc.field_71439_g);
/* 52 */           this.lastClickTime = System.currentTimeMillis();
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isDiamond(Item paramItem) {
/* 60 */     if (paramItem == Items.field_151045_i) return true;
/*    */     
/* 62 */     if (paramItem instanceof ItemTool && ((ItemTool)paramItem).func_77861_e().equals("EMERALD")) return true; 
/* 63 */     if (paramItem instanceof ItemSword && ((ItemSword)paramItem).func_150932_j().equals("EMERALD")) return true; 
/* 64 */     if (paramItem instanceof ItemArmor && ((ItemArmor)paramItem).func_82812_d() == ItemArmor.ArmorMaterial.DIAMOND) return true; 
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\DiamondCollectorModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */