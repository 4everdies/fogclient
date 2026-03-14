/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ModeSetting
/*    */   extends Setting {
/*    */   private int index;
/*    */   private List<String> modes;
/*    */   
/*    */   public ModeSetting(String paramString1, Module paramModule, String paramString2, String... paramVarArgs) {
/* 13 */     super(paramString1, paramModule);
/* 14 */     this.modes = Arrays.asList(paramVarArgs);
/* 15 */     this.index = this.modes.indexOf(paramString2);
/*    */   }
/*    */   
/*    */   public ModeSetting(String paramString1, String paramString2, String... paramVarArgs) {
/* 19 */     super(paramString1);
/* 20 */     this.modes = Arrays.asList(paramVarArgs);
/* 21 */     this.index = this.modes.indexOf(paramString2);
/*    */   }
/*    */   
/*    */   public String getMode() {
/* 25 */     if (this.index < 0 || this.index >= this.modes.size()) this.index = 0; 
/* 26 */     return this.modes.get(this.index);
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 30 */     if (this.index < 0 || this.index >= this.modes.size()) this.index = 0; 
/* 31 */     return this.modes.get(this.index);
/*    */   }
/*    */   
/*    */   public boolean is(String paramString) {
/* 35 */     return (this.index == this.modes.indexOf(paramString));
/*    */   }
/*    */   
/*    */   public void cycle() {
/* 39 */     if (this.index < this.modes.size() - 1) {
/* 40 */       this.index++;
/*    */     } else {
/* 42 */       this.index = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<String> getModes() {
/* 47 */     return this.modes;
/*    */   }
/*    */   
/*    */   public void setMode(String paramString) {
/* 51 */     int i = this.modes.indexOf(paramString);
/* 52 */     this.index = (i >= 0) ? i : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\ModeSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */