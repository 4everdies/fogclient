/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.ModeSetting;
/*    */ import com.fogclient.setting.Setting;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ 
/*    */ public class ModeBox extends SettingComponent {
/*    */   private ModeSetting modeSetting;
/*    */   
/*    */   public ModeBox(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 11 */     super(paramSetting, paramModuleButton, paramInt);
/* 12 */     this.modeSetting = (ModeSetting)paramSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/* 17 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*    */     
/* 19 */     mc.field_71466_p.func_175063_a(this.modeSetting.getName() + ": " + this.modeSetting.getMode(), (this.x + 2), (this.y + 4), -16711936);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/* 24 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, this.height) && paramInt3 == 0)
/* 25 */       this.modeSetting.cycle(); 
/*    */   }
/*    */   
/*    */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*    */   
/*    */   public void keyTyped(char paramChar, int paramInt) {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\ModeBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */