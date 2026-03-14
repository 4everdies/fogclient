/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import com.fogclient.setting.ModeSetting;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import com.fogclient.util.Ballistics;
/*     */ import com.fogclient.util.MathUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovementInput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BowAimbotModule
/*     */   extends Module
/*     */ {
/*  26 */   private final BooleanSetting silent = new BooleanSetting("Silent", this, true);
/*  27 */   private final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);
/*  28 */   private final BooleanSetting ignoreInvisible = new BooleanSetting("Ignore Invisible", this, true);
/*  29 */   private final NumberSetting fov = new NumberSetting("FOV", this, 180.0D, 10.0D, 360.0D, 10.0D);
/*  30 */   private final NumberSetting maxDistance = new NumberSetting("Max Distance", this, 60.0D, 10.0D, 100.0D, 5.0D);
/*  31 */   private final NumberSetting minDistance = new NumberSetting("Min Distance", this, 0.0D, 0.0D, 20.0D, 1.0D);
/*  32 */   private final NumberSetting predictIntensity = new NumberSetting("Predict", this, 1.0D, 0.0D, 5.0D, 0.1D);
/*  33 */   private final ModeSetting targetPriority = new ModeSetting("Priority", this, "Closest", new String[] { "Closest", "Health", "Angle" });
/*     */   
/*     */   private EntityLivingBase currentTarget;
/*     */   
/*     */   private float[] aimAngles;
/*     */   
/*     */   private float originalYaw;
/*     */   
/*     */   private float originalPitch;
/*     */   
/*     */   private float originalPrevYaw;
/*     */   
/*     */   private float originalPrevPitch;
/*     */   private float originalHeadYaw;
/*     */   private float originalPrevHeadYaw;
/*     */   private float originalRenderYawOffset;
/*     */   private float originalPrevRenderYawOffset;
/*     */   private float yawDelta;
/*     */   private float pitchDelta;
/*     */   private boolean isAiming;
/*     */   
/*     */   public BowAimbotModule() {
/*  55 */     super("FlechaCerteira", "Automatically aims at players with bow.", Category.COMBAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  63 */     this.isAiming = false;
/*  64 */     this.currentTarget = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTickStart() {
/*  69 */     if (!isEnabled())
/*     */       return; 
/*  71 */     this.isAiming = false;
/*  72 */     this.yawDelta = 0.0F;
/*  73 */     this.pitchDelta = 0.0F;
/*     */     
/*  75 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/*  77 */     ItemStack itemStack = mc.field_71439_g.func_70694_bm();
/*  78 */     if (itemStack == null || !(itemStack.func_77973_b() instanceof net.minecraft.item.ItemBow))
/*     */       return; 
/*  80 */     if (!mc.field_71439_g.func_71039_bw())
/*     */       return; 
/*  82 */     this.currentTarget = getTarget();
/*  83 */     if (this.currentTarget == null)
/*     */       return; 
/*  85 */     this.aimAngles = Ballistics.calculateAngles(this.currentTarget, this.predictIntensity.getValue());
/*  86 */     if (this.aimAngles == null)
/*     */       return; 
/*  88 */     if (this.silent.isEnabled()) {
/*  89 */       this.originalYaw = mc.field_71439_g.field_70177_z;
/*  90 */       this.originalPitch = mc.field_71439_g.field_70125_A;
/*  91 */       this.originalPrevYaw = mc.field_71439_g.field_70126_B;
/*  92 */       this.originalPrevPitch = mc.field_71439_g.field_70127_C;
/*     */       
/*  94 */       this.originalHeadYaw = mc.field_71439_g.field_70759_as;
/*  95 */       this.originalPrevHeadYaw = mc.field_71439_g.field_70758_at;
/*  96 */       this.originalRenderYawOffset = mc.field_71439_g.field_70761_aq;
/*  97 */       this.originalPrevRenderYawOffset = mc.field_71439_g.field_70760_ar;
/*     */       
/*  99 */       mc.field_71439_g.field_70177_z = this.aimAngles[0];
/* 100 */       mc.field_71439_g.field_70125_A = this.aimAngles[1];
/*     */       
/* 102 */       mc.field_71439_g.field_70759_as = this.aimAngles[0];
/* 103 */       mc.field_71439_g.field_70761_aq = this.aimAngles[0];
/*     */       
/* 105 */       this.isAiming = true;
/*     */     } else {
/* 107 */       mc.field_71439_g.field_70177_z = this.aimAngles[0];
/* 108 */       mc.field_71439_g.field_70125_A = this.aimAngles[1];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 114 */     if (!isEnabled())
/*     */       return; 
/* 116 */     if (this.isAiming && this.silent.isEnabled()) {
/* 117 */       mc.field_71439_g.field_70177_z = this.originalYaw + this.yawDelta;
/* 118 */       mc.field_71439_g.field_70125_A = this.originalPitch + this.pitchDelta;
/*     */       
/* 120 */       mc.field_71439_g.field_70126_B = this.originalYaw;
/* 121 */       mc.field_71439_g.field_70127_C = this.originalPitch;
/*     */       
/* 123 */       mc.field_71439_g.field_70759_as = this.originalHeadYaw;
/* 124 */       mc.field_71439_g.field_70758_at = this.originalHeadYaw;
/* 125 */       mc.field_71439_g.field_70761_aq = this.originalRenderYawOffset;
/* 126 */       mc.field_71439_g.field_70760_ar = this.originalRenderYawOffset;
/*     */       
/* 128 */       this.isAiming = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 134 */     if (mc.field_71439_g != null && this.isAiming && this.silent.isEnabled()) {
/* 135 */       float f1 = mc.field_71439_g.field_70177_z;
/* 136 */       float f2 = mc.field_71439_g.field_70125_A;
/*     */       
/* 138 */       float f3 = f1 - this.aimAngles[0];
/* 139 */       float f4 = f2 - this.aimAngles[1];
/*     */       
/* 141 */       for (; f3 <= -180.0F; f3 += 360.0F);
/* 142 */       for (; f3 > 180.0F; f3 -= 360.0F);
/*     */       
/* 144 */       this.yawDelta += f3;
/* 145 */       this.pitchDelta += f4;
/*     */       
/* 147 */       mc.field_71439_g.field_70177_z = this.aimAngles[0];
/* 148 */       mc.field_71439_g.field_70125_A = this.aimAngles[1];
/*     */       
/* 150 */       MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 151 */       float f5 = movementInput.field_78900_b;
/* 152 */       float f6 = movementInput.field_78902_a;
/*     */       
/* 154 */       float f7 = this.originalYaw;
/* 155 */       float f8 = this.aimAngles[0];
/*     */       
/* 157 */       float f9 = f7 - f8;
/* 158 */       float f10 = (float)Math.toRadians(f9);
/*     */       
/* 160 */       float f11 = MathHelper.func_76134_b(f10);
/* 161 */       float f12 = MathHelper.func_76126_a(f10);
/*     */       
/* 163 */       movementInput.field_78902_a = f6 * f11 - f5 * f12;
/* 164 */       movementInput.field_78900_b = f5 * f11 + f6 * f12;
/*     */     } 
/*     */   }
/*     */   
/*     */   private EntityLivingBase getTarget() {
/* 169 */     if (mc.field_71441_e == null) return null;
/*     */     
/* 171 */     EntityLivingBase entityLivingBase = null;
/* 172 */     double d = Double.MAX_VALUE;
/*     */     
/* 174 */     List list = mc.field_71441_e.func_72910_y();
/*     */     
/* 176 */     for (Entity entity : list) {
/* 177 */       if (!(entity instanceof EntityLivingBase))
/* 178 */         continue;  EntityLivingBase entityLivingBase1 = (EntityLivingBase)entity;
/*     */       
/* 180 */       if (entityLivingBase1 == mc.field_71439_g || 
/* 181 */         entityLivingBase1.field_70128_L || entityLivingBase1.func_110143_aJ() <= 0.0F || (
/* 182 */         this.ignoreInvisible.isEnabled() && entityLivingBase1.func_82150_aj()) || (
/* 183 */         this.ignoreWalls.isEnabled() && !mc.field_71439_g.func_70685_l((Entity)entityLivingBase1))) {
/*     */         continue;
/*     */       }
/* 186 */       double d1 = mc.field_71439_g.func_70032_d((Entity)entityLivingBase1);
/* 187 */       if (d1 < this.minDistance.getValue() || d1 > this.maxDistance.getValue())
/*     */         continue; 
/* 189 */       if (!isInFOV((Entity)entityLivingBase1, (float)this.fov.getValue()))
/*     */         continue; 
/* 191 */       double d2 = getScore(entityLivingBase1, d1);
/* 192 */       if (d2 < d) {
/* 193 */         d = d2;
/* 194 */         entityLivingBase = entityLivingBase1;
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     return entityLivingBase;
/*     */   }
/*     */   
/*     */   private boolean isInFOV(Entity paramEntity, float paramFloat) {
/* 202 */     float[] arrayOfFloat = MathUtils.getRotations((Minecraft.func_71410_x()).field_71439_g.func_174791_d(), paramEntity.func_174791_d());
/* 203 */     float f = MathUtils.getAngleDifference((Minecraft.func_71410_x()).field_71439_g.field_70177_z, arrayOfFloat[0]);
/* 204 */     return (Math.abs(f) < paramFloat / 2.0F);
/*     */   }
/*     */   
/*     */   private double getScore(EntityLivingBase paramEntityLivingBase, double paramDouble) {
/* 208 */     String str = this.targetPriority.getValue();
/* 209 */     if (str.equals("Health"))
/* 210 */       return paramEntityLivingBase.func_110143_aJ(); 
/* 211 */     if (str.equals("Angle")) {
/* 212 */       float[] arrayOfFloat = MathUtils.getRotations((Minecraft.func_71410_x()).field_71439_g.func_174791_d(), paramEntityLivingBase.func_174791_d());
/* 213 */       float f = MathUtils.getAngleDifference((Minecraft.func_71410_x()).field_71439_g.field_70177_z, arrayOfFloat[0]);
/* 214 */       return Math.abs(f);
/*     */     } 
/* 216 */     return paramDouble;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\BowAimbotModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */