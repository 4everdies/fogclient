/*    */ package com.fogclient.util;
/*    */ 
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class MathUtils
/*    */ {
/*    */   public static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
/*  8 */     return Math.max(paramFloat2, Math.min(paramFloat3, paramFloat1));
/*    */   }
/*    */   
/*    */   public static Vec3 getBezierPoint(Vec3 paramVec31, Vec3 paramVec32, Vec3 paramVec33, float paramFloat) {
/* 12 */     double d1 = Math.pow((1.0F - paramFloat), 2.0D) * paramVec31.field_72450_a + (2.0F * (1.0F - paramFloat) * paramFloat) * paramVec32.field_72450_a + Math.pow(paramFloat, 2.0D) * paramVec33.field_72450_a;
/* 13 */     double d2 = Math.pow((1.0F - paramFloat), 2.0D) * paramVec31.field_72448_b + (2.0F * (1.0F - paramFloat) * paramFloat) * paramVec32.field_72448_b + Math.pow(paramFloat, 2.0D) * paramVec33.field_72448_b;
/* 14 */     double d3 = Math.pow((1.0F - paramFloat), 2.0D) * paramVec31.field_72449_c + (2.0F * (1.0F - paramFloat) * paramFloat) * paramVec32.field_72449_c + Math.pow(paramFloat, 2.0D) * paramVec33.field_72449_c;
/* 15 */     return new Vec3(d1, d2, d3);
/*    */   }
/*    */   
/*    */   public static float[] getRotations(Vec3 paramVec31, Vec3 paramVec32) {
/* 19 */     double d1 = paramVec32.field_72450_a - paramVec31.field_72450_a;
/* 20 */     double d2 = paramVec32.field_72448_b - paramVec31.field_72448_b;
/* 21 */     double d3 = paramVec32.field_72449_c - paramVec31.field_72449_c;
/*    */     
/* 23 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/*    */     
/* 25 */     float f1 = (float)(Math.atan2(d3, d1) * 180.0D / Math.PI) - 90.0F;
/* 26 */     float f2 = (float)-(Math.atan2(d2, d4) * 180.0D / Math.PI);
/*    */     
/* 28 */     return new float[] { f1, f2 };
/*    */   }
/*    */   
/*    */   public static float wrapAngleTo180(float paramFloat) {
/* 32 */     paramFloat %= 360.0F;
/* 33 */     if (paramFloat >= 180.0F) {
/* 34 */       paramFloat -= 360.0F;
/*    */     }
/* 36 */     if (paramFloat < -180.0F) {
/* 37 */       paramFloat += 360.0F;
/*    */     }
/* 39 */     return paramFloat;
/*    */   }
/*    */   
/*    */   public static float getAngleDifference(float paramFloat1, float paramFloat2) {
/* 43 */     return wrapAngleTo180(paramFloat1 - paramFloat2);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\util\MathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */