/*    */ package com.fogclient.module.render;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.ui.ClickGUI;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ public class ClickGUIModule
/*    */   extends Module {
/*    */   public ClickGUIModule() {
/* 11 */     super("Menu Principal", "Displays the click GUI.", Category.CLIENT);
/* 12 */     this.keybind = 54;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 22 */     if (mc.field_71462_r == null) {
/* 23 */       mc.func_147108_a((GuiScreen)new ClickGUI());
/*    */     }
/* 25 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\ClickGUIModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */