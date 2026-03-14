/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import com.fogclient.util.MathUtils;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfectAimModule
/*     */   extends Module
/*     */ {
/*  21 */   private final BooleanSetting enablePrediction = new BooleanSetting("Prediction", this, true);
/*  22 */   private final NumberSetting aimbotSpeed = new NumberSetting("Speed", this, 3.0D, 0.1D, 10.0D, 0.1D);
/*  23 */   private final NumberSetting hitboxPixels = new NumberSetting("Hitbox Pixels", this, 5.0D, 0.0D, 50.0D, 1.0D);
/*  24 */   private final BooleanSetting enableHitboxCorrection = new BooleanSetting("Hitbox Correction", this, true);
/*  25 */   private final BooleanSetting enableSensitivityAdjustment = new BooleanSetting("Sensitivity Adjust", this, false);
/*  26 */   private final NumberSetting sensitivityReduction = new NumberSetting("Sensitivity Reduction", this, 20.0D, 0.0D, 100.0D, 5.0D);
/*     */   
/*     */   private EntityLivingBase currentTarget;
/*     */   private Vec3 predictedPosition;
/*  30 */   private final Random random = new Random();
/*     */ 
/*     */   
/*  33 */   private float storedSensitivity = -1.0F;
/*     */   private boolean isSensitivityModified = false;
/*     */   
/*     */   public PerfectAimModule() {
/*  37 */     super("MiraCerteira", "Aims at the closest player.", Category.COMBAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  42 */     this.storedSensitivity = -1.0F;
/*  43 */     this.isSensitivityModified = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  48 */     if (this.isSensitivityModified && this.storedSensitivity != -1.0F) {
/*  49 */       mc.field_71474_y.field_74341_c = this.storedSensitivity;
/*  50 */       this.isSensitivityModified = false;
/*  51 */       this.storedSensitivity = -1.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTickStart() {
/*  57 */     if (!isEnabled())
/*  58 */       return;  if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/*  60 */     updateTargetAndPrediction();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRenderTick() {
/*  65 */     if (!isEnabled())
/*  66 */       return;  if (mc.field_71439_g == null)
/*     */       return; 
/*  68 */     if (this.currentTarget != null && isAimingConditionMet()) {
/*  69 */       applyAimbotRotation();
/*     */     }
/*     */     
/*  72 */     adjustSensitivity();
/*     */   }
/*     */   
/*     */   private void updateTargetAndPrediction() {
/*  76 */     Entity entity = (mc.field_71476_x != null) ? mc.field_71476_x.field_72308_g : null;
/*  77 */     if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
/*  78 */       this.currentTarget = (EntityLivingBase)entity;
/*     */     } else {
/*  80 */       this.currentTarget = findClosestTarget();
/*     */     } 
/*     */     
/*  83 */     if (this.currentTarget != null) {
/*  84 */       if (this.enablePrediction.isEnabled()) {
/*  85 */         predictPosition(this.currentTarget);
/*     */       } else {
/*  87 */         this.predictedPosition = this.currentTarget.func_174791_d();
/*     */       } 
/*     */     } else {
/*  90 */       this.predictedPosition = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private EntityLivingBase findClosestTarget() {
/*  95 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/*  96 */     double d = 100.0D;
/*  97 */     EntityLivingBase entityLivingBase = null;
/*     */     
/*  99 */     List list = mc.field_71441_e.func_72910_y();
/* 100 */     for (Entity entity : list) {
/* 101 */       if (entity instanceof net.minecraft.entity.player.EntityPlayer && entity != entityPlayerSP && !entity.func_82150_aj()) {
/* 102 */         double d1 = entity.func_70032_d((Entity)entityPlayerSP);
/* 103 */         if (d1 < 6.0D) {
/* 104 */           float[] arrayOfFloat = MathUtils.getRotations(entityPlayerSP.func_174824_e(1.0F), entity.func_174791_d().func_72441_c(0.0D, entity.field_70131_O / 2.0D, 0.0D));
/* 105 */           float f1 = Math.abs(MathUtils.wrapAngleTo180(arrayOfFloat[0] - entityPlayerSP.field_70177_z));
/* 106 */           float f2 = Math.abs(arrayOfFloat[1] - entityPlayerSP.field_70125_A);
/* 107 */           double d2 = (f1 + f2);
/*     */           
/* 109 */           if (d2 < d) {
/* 110 */             d = d2;
/* 111 */             entityLivingBase = (EntityLivingBase)entity;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 116 */     return entityLivingBase;
/*     */   }
/*     */   
/*     */   private void predictPosition(EntityLivingBase paramEntityLivingBase) {
/* 120 */     int i = 0;
/* 121 */     if (paramEntityLivingBase instanceof net.minecraft.entity.player.EntityPlayer) {
/*     */       try {
/* 123 */         i = mc.func_147114_u().func_175102_a(paramEntityLivingBase.func_110124_au()).func_178853_c();
/* 124 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 127 */     int j = Math.round(i / 50.0F) + 2;
/*     */     
/* 129 */     double d1 = paramEntityLivingBase.field_70165_t;
/* 130 */     double d2 = paramEntityLivingBase.field_70163_u;
/* 131 */     double d3 = paramEntityLivingBase.field_70161_v;
/* 132 */     double d4 = paramEntityLivingBase.field_70165_t - paramEntityLivingBase.field_70142_S;
/* 133 */     double d5 = paramEntityLivingBase.field_70163_u - paramEntityLivingBase.field_70137_T;
/* 134 */     double d6 = paramEntityLivingBase.field_70161_v - paramEntityLivingBase.field_70136_U;
/*     */     
/* 136 */     for (byte b = 0; b < j; ) {
/* 137 */       d5 -= 0.08D;
/* 138 */       d4 *= 0.91D;
/* 139 */       d5 *= 0.98D;
/* 140 */       d6 *= 0.91D;
/*     */       
/* 142 */       if (mc.field_71441_e.func_72945_a((Entity)paramEntityLivingBase, paramEntityLivingBase.func_174813_aQ().func_72317_d(d4, d5, d6)).isEmpty()) {
/* 143 */         d1 += d4;
/* 144 */         d2 += d5;
/* 145 */         d3 += d6;
/*     */         
/*     */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     this.predictedPosition = new Vec3(d1, d2, d3);
/*     */   }
/*     */   
/*     */   private boolean isAimingConditionMet() {
/* 155 */     boolean bool = (Math.abs(mc.field_71417_B.field_74377_a) > 0 || Math.abs(mc.field_71417_B.field_74375_b) > 0) ? true : false;
/* 156 */     boolean bool1 = mc.field_71474_y.field_74312_F.func_151470_d();
/* 157 */     return (bool && bool1);
/*     */   }
/*     */   
/*     */   private void applyAimbotRotation() {
/* 161 */     if (this.predictedPosition == null)
/*     */       return; 
/* 163 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/*     */     
/* 165 */     float f1 = this.currentTarget.field_70130_N;
/* 166 */     float f2 = this.currentTarget.field_70131_O;
/*     */ 
/*     */     
/* 169 */     Vec3 vec3 = new Vec3(this.predictedPosition.field_72450_a, this.predictedPosition.field_72448_b + (f2 / 2.0F), this.predictedPosition.field_72449_c);
/* 170 */     float[] arrayOfFloat = MathUtils.getRotations(entityPlayerSP.func_174824_e(1.0F), vec3);
/*     */     
/* 172 */     double d1 = entityPlayerSP.func_70011_f(this.predictedPosition.field_72450_a, this.predictedPosition.field_72448_b, this.predictedPosition.field_72449_c);
/* 173 */     double d2 = Math.toDegrees(Math.atan(f1 / d1));
/* 174 */     double d3 = Math.toDegrees(Math.atan(f2 / d1));
/*     */     
/* 176 */     float f3 = MathUtils.wrapAngleTo180(entityPlayerSP.field_70177_z);
/* 177 */     float f4 = entityPlayerSP.field_70125_A;
/*     */     
/* 179 */     float f5 = MathUtils.wrapAngleTo180(arrayOfFloat[0]);
/* 180 */     float f6 = arrayOfFloat[1];
/*     */     
/* 182 */     float f7 = MathUtils.wrapAngleTo180(f5 - f3);
/* 183 */     float f8 = f6 - f4;
/*     */     
/* 185 */     float f9 = (float)(d2 / 2.0D);
/* 186 */     float f10 = (float)(d3 / 2.0D);
/*     */     
/* 188 */     float f11 = (float)(this.hitboxPixels.getValue() * 0.18000000715255737D);
/*     */     
/* 190 */     boolean bool1 = (Math.abs(f7) < f9 + f11 && Math.abs(f8) < f10 + f11) ? true : false;
/* 191 */     boolean bool2 = (Math.abs(f7) < f9 && Math.abs(f8) < f10) ? true : false;
/*     */     
/* 193 */     if (mc.field_71476_x != null && mc.field_71476_x.field_72308_g == this.currentTarget) {
/* 194 */       bool2 = true;
/*     */     }
/*     */     
/* 197 */     if (this.enableHitboxCorrection.isEnabled() && bool1 && !bool2) {
/*     */       float f12, f13;
/*     */       
/* 200 */       if (bool2) {
/* 201 */         f12 = f5;
/* 202 */         f13 = f6;
/*     */       } else {
/* 204 */         float f21 = (f7 > 0.0F) ? f9 : -f9;
/* 205 */         float f22 = (f8 > 0.0F) ? f10 : -f10;
/* 206 */         f12 = f5 - f21;
/* 207 */         f13 = f6 - f22;
/*     */       } 
/*     */       
/* 210 */       float f14 = (float)Math.sqrt((mc.field_71417_B.field_74377_a * mc.field_71417_B.field_74377_a + mc.field_71417_B.field_74375_b * mc.field_71417_B.field_74375_b));
/*     */       
/* 212 */       float f15 = (float)(this.aimbotSpeed.getValue() / 100.0D);
/* 213 */       float f16 = f15 / 12.0F;
/* 214 */       float f17 = f15 * 4.0F;
/*     */       
/* 216 */       float f18 = f15 + f14 * f16;
/* 217 */       f18 = MathHelper.func_76131_a(f18, f15 * 0.35F, f17);
/*     */       
/* 219 */       float f19 = MathUtils.wrapAngleTo180(f12 - f3) * f18;
/* 220 */       float f20 = (f13 - f4) * f18;
/*     */       
/* 222 */       if (this.enableHitboxCorrection.isEnabled()) {
/* 223 */         float f = (this.random.nextFloat() - 0.5F) * 0.05F;
/* 224 */         f19 += f;
/* 225 */         f20 += f;
/*     */       } 
/*     */       
/* 228 */       entityPlayerSP.field_70177_z += f19;
/* 229 */       entityPlayerSP.field_70125_A += f20;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void adjustSensitivity() {
/* 234 */     if (!this.enableSensitivityAdjustment.isEnabled()) {
/* 235 */       if (this.isSensitivityModified && this.storedSensitivity != -1.0F) {
/* 236 */         mc.field_71474_y.field_74341_c = this.storedSensitivity;
/* 237 */         this.isSensitivityModified = false;
/* 238 */         this.storedSensitivity = -1.0F;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 243 */     boolean bool = false;
/* 244 */     if (mc.field_71476_x != null && mc.field_71476_x.field_72308_g instanceof net.minecraft.entity.player.EntityPlayer) {
/* 245 */       bool = true;
/*     */     }
/*     */     
/* 248 */     if (bool) {
/* 249 */       if (!this.isSensitivityModified) {
/* 250 */         this.storedSensitivity = mc.field_71474_y.field_74341_c;
/* 251 */         this.isSensitivityModified = true;
/*     */       } 
/*     */       
/* 254 */       float f1 = (float)(this.sensitivityReduction.getValue() / 100.0D);
/* 255 */       float f2 = this.storedSensitivity * (1.0F - f1);
/*     */       
/* 257 */       mc.field_71474_y.field_74341_c = lerp(mc.field_71474_y.field_74341_c, f2, 0.25F);
/*     */     
/*     */     }
/* 260 */     else if (this.isSensitivityModified) {
/* 261 */       mc.field_71474_y.field_74341_c = lerp(mc.field_71474_y.field_74341_c, this.storedSensitivity, 0.25F);
/*     */       
/* 263 */       if (Math.abs(mc.field_71474_y.field_74341_c - this.storedSensitivity) < 0.001D) {
/* 264 */         mc.field_71474_y.field_74341_c = this.storedSensitivity;
/* 265 */         this.isSensitivityModified = false;
/* 266 */         this.storedSensitivity = -1.0F;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 273 */     return paramFloat1 + paramFloat3 * (paramFloat2 - paramFloat1);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\PerfectAimModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */