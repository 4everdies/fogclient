/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import com.fogclient.setting.Setting;
/*    */ 
/*    */ public abstract class SettingComponent
/*    */   extends Component {
/*    */   protected Setting setting;
/*    */   protected ModuleButton parent;
/*    */   protected int offset;
/*    */   
/*    */   public SettingComponent(Setting paramSetting, ModuleButton paramModuleButton, int paramInt) {
/* 12 */     this.setting = paramSetting;
/* 13 */     this.parent = paramModuleButton;
/* 14 */     this.offset = paramInt;
/*    */   }
/*    */   protected int x; protected int y; protected int width; protected int height;
/*    */   public void setOffset(int paramInt) {
/* 18 */     this.offset = paramInt;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 22 */     return 16;
/*    */   }
/*    */   
/*    */   public void updatePosition(int paramInt1, int paramInt2, int paramInt3) {
/* 26 */     this.x = paramInt1;
/* 27 */     this.y = paramInt2 + this.offset;
/* 28 */     this.width = paramInt3;
/* 29 */     this.height = getHeight();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\SettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */