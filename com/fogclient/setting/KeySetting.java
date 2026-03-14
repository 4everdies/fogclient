/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class KeySetting extends Setting {
/*    */   private int keyCode;
/*    */   
/*    */   public KeySetting(String paramString, Module paramModule, int paramInt) {
/*  9 */     super(paramString, paramModule);
/* 10 */     this.keyCode = paramInt;
/*    */   }
/*    */   
/*    */   public int getKeyCode() {
/* 14 */     return this.keyCode;
/*    */   }
/*    */   
/*    */   public void setKeyCode(int paramInt) {
/* 18 */     this.keyCode = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\KeySetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */