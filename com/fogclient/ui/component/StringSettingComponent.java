/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.Setting;
/*    */ import com.fogclient.setting.StringSetting;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ 
/*    */ public class StringSettingComponent
/*    */   extends SettingComponent
/*    */ {
/*    */   private StringSetting stringSetting;
/*    */   private boolean isFocused;
/*    */   private int cursorTimer;
/*    */   
/*    */   public StringSettingComponent(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 15 */     super(paramSetting, paramModuleButton, paramInt);
/* 16 */     this.stringSetting = (StringSetting)paramSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 21 */     return 30;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/* 26 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*    */     
/* 28 */     mc.field_71466_p.func_175063_a(this.stringSetting.getName(), (this.x + 2), (this.y + 2), -16711936);
/*    */     
/* 30 */     int i = this.x + 2;
/* 31 */     int j = this.y + 14;
/* 32 */     int k = this.width - 4;
/* 33 */     byte b = 12;
/*    */     
/* 35 */     Gui.func_73734_a(i, j, i + k, j + b, -16777216);
/*    */     
/* 37 */     String str1 = this.stringSetting.getText();
/*    */     
/* 39 */     boolean bool = (this.isFocused && System.currentTimeMillis() / 500L % 2L == 0L) ? true : false;
/*    */     
/* 41 */     String str2 = str1 + (bool ? "_" : "");
/*    */ 
/*    */     
/* 44 */     if (mc.field_71466_p.func_78256_a(str2) > k - 4) {
/*    */ 
/*    */       
/* 47 */       String str = str2;
/* 48 */       while (mc.field_71466_p.func_78256_a(str) > k - 4 && str.length() > 0) {
/* 49 */         str = str.substring(1);
/*    */       }
/* 51 */       str2 = str;
/*    */     } 
/*    */     
/* 54 */     mc.field_71466_p.func_175063_a(str2, (i + 2), (j + 2), -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/* 59 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, this.height)) {
/* 60 */       this.isFocused = true;
/*    */     } else {
/* 62 */       this.isFocused = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*    */ 
/*    */   
/*    */   public void keyTyped(char paramChar, int paramInt) {
/* 72 */     if (this.isFocused) {
/* 73 */       if (paramInt == 14) {
/* 74 */         String str = this.stringSetting.getText();
/* 75 */         if (str.length() > 0) {
/* 76 */           this.stringSetting.setText(str.substring(0, str.length() - 1));
/* 77 */           this.stringSetting.setValue(this.stringSetting.getText());
/*    */         } 
/* 79 */       } else if (paramInt == 28) {
/* 80 */         this.isFocused = false;
/* 81 */       } else if (isAllowedCharacter(paramChar)) {
/* 82 */         String str = this.stringSetting.getText();
/* 83 */         this.stringSetting.setText(str + paramChar);
/* 84 */         this.stringSetting.setValue(this.stringSetting.getText());
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean isAllowedCharacter(char paramChar) {
/* 90 */     return (paramChar >= ' ' && paramChar != '');
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\StringSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */