/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class BooleanSetting extends Setting {
/*    */   private boolean enabled;
/*    */   
/*    */   public BooleanSetting(String paramString, Module paramModule, boolean paramBoolean) {
/*  9 */     super(paramString, paramModule);
/* 10 */     this.enabled = paramBoolean;
/*    */   }
/*    */   
/*    */   public BooleanSetting(String paramString, boolean paramBoolean) {
/* 14 */     super(paramString);
/* 15 */     this.enabled = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 19 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public boolean getValue() {
/* 23 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setEnabled(boolean paramBoolean) {
/* 27 */     this.enabled = paramBoolean;
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 31 */     this.enabled = !this.enabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\BooleanSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */