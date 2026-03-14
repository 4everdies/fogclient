/*    */ package com.fogclient.setting;
/*    */ 
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class NumberSetting extends Setting {
/*    */   private double value;
/*    */   private double min;
/*    */   private double max;
/*    */   private double increment;
/*    */   
/*    */   public NumberSetting(String paramString, Module paramModule, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 12 */     super(paramString, paramModule);
/* 13 */     this.value = paramDouble1;
/* 14 */     this.min = paramDouble2;
/* 15 */     this.max = paramDouble3;
/* 16 */     this.increment = paramDouble4;
/*    */   }
/*    */   
/*    */   public NumberSetting(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 20 */     super(paramString);
/* 21 */     this.value = paramDouble1;
/* 22 */     this.min = paramDouble2;
/* 23 */     this.max = paramDouble3;
/* 24 */     this.increment = paramDouble4;
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 28 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(double paramDouble) {
/* 32 */     double d = 1.0D / this.increment;
/* 33 */     this.value = Math.round(Math.max(this.min, Math.min(this.max, paramDouble)) * d) / d;
/*    */   }
/*    */   
/*    */   public double getMin() {
/* 37 */     return this.min;
/*    */   }
/*    */   
/*    */   public double getMax() {
/* 41 */     return this.max;
/*    */   }
/*    */   
/*    */   public double getIncrement() {
/* 45 */     return this.increment;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\setting\NumberSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */