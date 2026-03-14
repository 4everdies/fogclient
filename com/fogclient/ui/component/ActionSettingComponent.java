/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.ActionSetting;
/*    */ import com.fogclient.setting.Setting;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class ActionSettingComponent extends SettingComponent {
/*    */   public ActionSettingComponent(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 12 */     super(paramSetting, paramModuleButton, paramInt);
/* 13 */     this.actionSetting = (ActionSetting)paramSetting;
/* 14 */     this.height = 16;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/* 19 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*    */     
/* 21 */     boolean bool = isHovered(paramInt1, paramInt2, this.x + 2, this.y + 2, this.width - 4, this.height - 4);
/* 22 */     int i = bool ? -12303292 : -14540254;
/*    */     
/* 24 */     Gui.func_73734_a(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, i);
/*    */     
/* 26 */     String str = this.actionSetting.getName();
/* 27 */     int j = mc.field_71466_p.func_78256_a(str);
/* 28 */     mc.field_71466_p.func_175063_a(str, (this.x + this.width / 2 - j / 2), (this.y + 4), -16711936);
/*    */   }
/*    */   private ActionSetting actionSetting;
/*    */   
/*    */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/* 33 */     if (isHovered(paramInt1, paramInt2, this.x + 2, this.y + 2, this.width - 4, this.height - 4) && 
/* 34 */       paramInt3 == 0) {
/* 35 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
/* 36 */       this.actionSetting.perform();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*    */   
/*    */   public void keyTyped(char paramChar, int paramInt) {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\ActionSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */