/*    */ package com.fogclient.module.manual;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.ui.GuiManual;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ public class ManualModule extends Module {
/*    */   public ManualModule() {
/* 10 */     super("Abrir Manual", "Abre o manual do usuÃ¡rio in-game.", Category.MANUAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 15 */     if (mc.field_71439_g != null) {
/* 16 */       mc.func_147108_a((GuiScreen)new GuiManual());
/*    */     }
/* 18 */     toggle();
/*    */   }
/*    */   
/*    */   public void onDisable() {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\manual\ManualModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */