/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.BooleanSetting;
/*    */ import com.fogclient.setting.Setting;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ 
/*    */ public class CheckBox extends SettingComponent {
/*    */   private BooleanSetting boolSetting;
/*    */   
/*    */   public CheckBox(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 11 */     super(paramSetting, paramModuleButton, paramInt);
/* 12 */     this.boolSetting = (BooleanSetting)paramSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/* 17 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*    */ 
/*    */     
/* 20 */     Gui.func_73734_a(this.x + this.width - 15, this.y + 1, this.x + this.width - 1, this.y + 15, -11184811);
/*    */ 
/*    */     
/* 23 */     Gui.func_73734_a(this.x + this.width - 14, this.y + 2, this.x + this.width - 2, this.y + 14, -16777216);
/*    */ 
/*    */     
/* 26 */     if (this.boolSetting.isEnabled()) {
/* 27 */       Gui.func_73734_a(this.x + this.width - 12, this.y + 4, this.x + this.width - 4, this.y + 12, -16711936);
/*    */     }
/*    */     
/* 30 */     mc.field_71466_p.func_175063_a(this.boolSetting.getName(), (this.x + 2), (this.y + 4), -16711936);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/* 35 */     if (isHovered(paramInt1, paramInt2, this.x + this.width - 14, this.y + 2, 12, 12) && paramInt3 == 0)
/* 36 */       this.boolSetting.toggle(); 
/*    */   }
/*    */   
/*    */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*    */   
/*    */   public void keyTyped(char paramChar, int paramInt) {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\CheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */