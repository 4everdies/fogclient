/*     */ package com.fogclient.util;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Ballistics
/*     */ {
/*     */   public static float[] calculateAngles(EntityLivingBase paramEntityLivingBase, double paramDouble) {
/*  15 */     Minecraft minecraft = Minecraft.func_71410_x();
/*  16 */     EntityPlayerSP entityPlayerSP = minecraft.field_71439_g;
/*     */     
/*  18 */     float f = getArrowVelocity(minecraft.field_71439_g.func_71057_bx());
/*  19 */     if (f < 0.1D) return null;
/*     */ 
/*     */     
/*  22 */     double d1 = entityPlayerSP.func_70032_d((Entity)paramEntityLivingBase);
/*  23 */     double d2 = d1 / f;
/*     */ 
/*     */     
/*  26 */     double d3 = paramEntityLivingBase.field_70165_t - paramEntityLivingBase.field_70142_S;
/*  27 */     double d4 = paramEntityLivingBase.field_70163_u - paramEntityLivingBase.field_70137_T;
/*  28 */     double d5 = paramEntityLivingBase.field_70161_v - paramEntityLivingBase.field_70136_U;
/*     */     
/*  30 */     double d6 = d3 * d2 * paramDouble;
/*  31 */     double d7 = d4 * d2 * paramDouble;
/*  32 */     double d8 = d5 * d2 * paramDouble;
/*     */     
/*  34 */     double d9 = paramEntityLivingBase.field_70165_t + d6;
/*  35 */     double d10 = paramEntityLivingBase.field_70163_u + paramEntityLivingBase.func_70047_e() / 2.0D + d7;
/*  36 */     double d11 = paramEntityLivingBase.field_70161_v + d8;
/*     */     
/*  38 */     return solveTrajectory(((Entity)entityPlayerSP).field_70165_t, ((Entity)entityPlayerSP).field_70163_u + entityPlayerSP.func_70047_e(), ((Entity)entityPlayerSP).field_70161_v, d9, d10, d11, f);
/*     */   }
/*     */   
/*     */   private static float getArrowVelocity(int paramInt) {
/*  42 */     float f = paramInt / 20.0F;
/*  43 */     f = (f * f + f * 2.0F) / 3.0F;
/*  44 */     if (f > 1.0F) {
/*  45 */       f = 1.0F;
/*     */     }
/*  47 */     return f * 3.0F;
/*     */   }
/*     */   
/*     */   private static float[] solveTrajectory(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, float paramFloat) {
/*  51 */     double d1 = paramDouble4 - paramDouble1;
/*  52 */     double d2 = paramDouble5 - paramDouble2;
/*  53 */     double d3 = paramDouble6 - paramDouble3;
/*  54 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/*     */     
/*  56 */     double d5 = Math.atan2(d3, d1) * 180.0D / Math.PI - 90.0D;
/*     */ 
/*     */     
/*  59 */     double d6 = Math.atan2(d2, d4);
/*  60 */     double d7 = d6 + Math.toRadians(5.0D);
/*     */     
/*  62 */     double d8 = getSimulationError(paramDouble1, paramDouble2, paramDouble3, d5, d6, paramFloat, d4, paramDouble5);
/*  63 */     if (Double.isNaN(d8)) return null;
/*     */     
/*  65 */     for (byte b = 0; b < 20; b++) {
/*  66 */       double d = getSimulationError(paramDouble1, paramDouble2, paramDouble3, d5, d7, paramFloat, d4, paramDouble5);
/*  67 */       if (Double.isNaN(d)) {
/*  68 */         d7 = (d6 + d7) / 2.0D;
/*     */       }
/*     */       else {
/*     */         
/*  72 */         if (Math.abs(d) < 0.05D) {
/*  73 */           return new float[] { (float)d5, (float)-(d7 * 180.0D / Math.PI) };
/*     */         }
/*     */         
/*  76 */         double d9 = d7 - d6;
/*  77 */         double d10 = d - d8;
/*     */         
/*  79 */         if (Math.abs(d10) < 1.0E-5D)
/*     */           break; 
/*  81 */         double d11 = d7 - d * d9 / d10;
/*     */         
/*  83 */         if (d11 < -1.5707963267948966D) d11 = -1.5707963267948966D; 
/*  84 */         if (d11 > 1.5707963267948966D) d11 = 1.5707963267948966D;
/*     */         
/*  86 */         d6 = d7;
/*  87 */         d8 = d;
/*  88 */         d7 = d11;
/*     */       } 
/*     */     } 
/*  91 */     return new float[] { (float)d5, (float)-(d7 * 180.0D / Math.PI) };
/*     */   }
/*     */   
/*     */   private static double getSimulationError(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, float paramFloat, double paramDouble6, double paramDouble7) {
/*  95 */     Vec3 vec3 = simulateArrow(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramFloat, paramDouble6);
/*  96 */     if (vec3 == null) return Double.NaN; 
/*  97 */     return vec3.field_72448_b - paramDouble7;
/*     */   }
/*     */   
/*     */   private static Vec3 simulateArrow(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, float paramFloat, double paramDouble6) {
/* 101 */     double d1 = (paramDouble4 + 90.0D) * Math.PI / 180.0D;
/*     */     
/* 103 */     double d2 = paramFloat * Math.cos(d1) * Math.cos(paramDouble5);
/* 104 */     double d3 = paramFloat * Math.sin(d1) * Math.cos(paramDouble5);
/* 105 */     double d4 = paramFloat * Math.sin(paramDouble5);
/*     */     
/* 107 */     double d5 = paramDouble1;
/* 108 */     double d6 = paramDouble2;
/* 109 */     double d7 = paramDouble3;
/*     */ 
/*     */     
/* 112 */     double d8 = 0.05000000074505806D;
/* 113 */     double d9 = 0.99D;
/*     */     
/* 115 */     for (byte b = 0; b < 'Ĭ'; b++) {
/* 116 */       d5 += d2;
/* 117 */       d6 += d4;
/* 118 */       d7 += d3;
/*     */       
/* 120 */       double d = (d5 - paramDouble1) * (d5 - paramDouble1) + (d7 - paramDouble3) * (d7 - paramDouble3);
/* 121 */       if (d >= paramDouble6 * paramDouble6) {
/* 122 */         return new Vec3(d5, d6, d7);
/*     */       }
/*     */       
/* 125 */       d2 *= d9;
/* 126 */       d4 *= d9;
/* 127 */       d3 *= d9;
/* 128 */       d4 -= d8;
/*     */     } 
/* 130 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\util\Ballistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */