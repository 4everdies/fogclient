/*    */ package com.fogclient.module.render;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import net.minecraftforge.client.event.GuiOpenEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InventoryScaleLiteModule
/*    */   extends Module
/*    */ {
/* 14 */   private final NumberSetting normalScale = new NumberSetting("Normal Scale", this, 2.0D, 0.0D, 3.0D, 1.0D);
/* 15 */   private final NumberSetting inventoryScale = new NumberSetting("Inv Scale", this, 2.0D, 0.0D, 3.0D, 1.0D);
/*    */   
/*    */   public InventoryScaleLiteModule() {
/* 18 */     super("EscalaCustomizada", "Altera a escala da GUI no inventÃ¡rio", Category.RENDER);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 30 */     mc.field_71474_y.field_74335_Z = (int)this.normalScale.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEvent(Event paramEvent) {
/* 35 */     if (paramEvent instanceof GuiOpenEvent) {
/* 36 */       onGuiOpen((GuiOpenEvent)paramEvent);
/*    */     }
/*    */   }
/*    */   
/*    */   private void onGuiOpen(GuiOpenEvent paramGuiOpenEvent) {
/* 41 */     if (!isEnabled())
/*    */       return; 
/* 43 */     if (paramGuiOpenEvent.gui instanceof net.minecraft.client.gui.inventory.GuiContainer) {
/* 44 */       mc.field_71474_y.field_74335_Z = (int)this.inventoryScale.getValue();
/* 45 */     } else if (paramGuiOpenEvent.gui == null || paramGuiOpenEvent.gui instanceof com.fogclient.ui.ClickGUI) {
/* 46 */       mc.field_71474_y.field_74335_Z = (int)this.normalScale.getValue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\InventoryScaleLiteModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */