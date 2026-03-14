/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class StringSetting extends Setting {
/*    */   private String value;
/*    */   private String text;
/*    */   
/*    */   public StringSetting(String paramString1, Module paramModule, String paramString2) {
/* 10 */     super(paramString1, paramModule);
/* 11 */     this.value = paramString2;
/* 12 */     this.text = paramString2;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 16 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String paramString) {
/* 20 */     this.value = paramString;
/* 21 */     if (this.text == null) {
/* 22 */       this.text = paramString;
/*    */     }
/*    */   }
/*    */   
/*    */   public String getText() {
/* 27 */     return this.text;
/*    */   }
/*    */   
/*    */   public void setText(String paramString) {
/* 31 */     this.text = paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\StringSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */