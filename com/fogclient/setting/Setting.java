/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public abstract class Setting {
/*    */   private String name;
/*    */   private Module parent;
/*    */   private boolean hidden = false;
/*    */   
/*    */   public Setting(String paramString, Module paramModule) {
/* 11 */     this.name = paramString;
/* 12 */     this.parent = paramModule;
/* 13 */     if (this.parent != null) {
/* 14 */       this.parent.addSetting(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public Setting(String paramString) {
/* 19 */     this.name = paramString;
/* 20 */     this.parent = null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 24 */     return this.name;
/*    */   }
/*    */   
/*    */   public Module getParent() {
/* 28 */     return this.parent;
/*    */   }
/*    */   
/*    */   public boolean isHidden() {
/* 32 */     return this.hidden;
/*    */   }
/*    */   
/*    */   public void setHidden(boolean paramBoolean) {
/* 36 */     this.hidden = paramBoolean;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */