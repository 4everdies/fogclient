/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import com.fogclient.setting.Setting;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ 
/*    */ public class Slider extends SettingComponent {
/*    */   private NumberSetting numSetting;
/*    */   private boolean dragging = false;
/*    */   
/*    */   public Slider(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 12 */     super(paramSetting, paramModuleButton, paramInt);
/* 13 */     this.numSetting = (NumberSetting)paramSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/* 18 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*    */     
/* 20 */     double d1 = this.numSetting.getMin();
/* 21 */     double d2 = this.numSetting.getMax();
/* 22 */     double d3 = this.numSetting.getValue();
/*    */     
/* 24 */     double d4 = (d3 - d1) / (d2 - d1);
/* 25 */     int i = (int)((this.width - 4) * d4);
/*    */ 
/*    */     
/* 28 */     Gui.func_73734_a(this.x + 2, this.y + 12, this.x + this.width - 2, this.y + 14, -11184811);
/*    */     
/* 30 */     Gui.func_73734_a(this.x + 2, this.y + 12, this.x + 2 + i, this.y + 14, -16711936);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     String str = String.format("%.1f", new Object[] { Double.valueOf(d3) });
/* 36 */     if (this.numSetting.getIncrement() == 1.0D) {
/* 37 */       str = String.format("%d", new Object[] { Integer.valueOf((int)d3) });
/*    */     }
/*    */     
/* 40 */     mc.field_71466_p.func_175063_a(this.numSetting.getName() + ": " + str, (this.x + 2), (this.y + 2), -16711936);
/*    */     
/* 42 */     if (this.dragging) {
/* 43 */       double d5 = Math.min(this.width - 4, Math.max(0, paramInt1 - this.x + 2));
/* 44 */       double d6 = d5 / (this.width - 4);
/* 45 */       double d7 = d1 + (d2 - d1) * d6;
/* 46 */       this.numSetting.setValue(d7);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/* 52 */     if (isHovered(paramInt1, paramInt2, this.x + 2, this.y + 10, this.width - 4, 6) && paramInt3 == 0) {
/* 53 */       this.dragging = true;
/* 54 */     } else if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, this.height) && paramInt3 == 0) {
/* 55 */       this.dragging = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {
/* 61 */     this.dragging = false;
/*    */   }
/*    */   
/*    */   public void keyTyped(char paramChar, int paramInt) {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\Slider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */