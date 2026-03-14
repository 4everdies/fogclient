/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class ActionSetting extends Setting {
/*    */   private Runnable action;
/*    */   
/*    */   public ActionSetting(String paramString, Module paramModule, Runnable paramRunnable) {
/*  9 */     super(paramString, paramModule);
/* 10 */     this.action = paramRunnable;
/*    */   }
/*    */   
/*    */   public void perform() {
/* 14 */     if (this.action != null)
/* 15 */       this.action.run(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\ActionSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */