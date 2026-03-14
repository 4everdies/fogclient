/*     */ package com.fogclient.module.combat.ghostreach;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class GhostReachCore
/*     */ {
/*  13 */   public static final GhostReachCore INSTANCE = new GhostReachCore();
/*     */   
/*  15 */   public static double minReach = 3.0D;
/*  16 */   public static double maxReach = 4.0D;
/*  17 */   public static float chance = 100.0F;
/*     */   
/*     */   public static boolean debug = false;
/*     */   
/*  21 */   public int targetEntityId = -1;
/*     */   
/*  23 */   private final Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public boolean isTarget(int paramInt) {
/*  26 */     return (this.targetEntityId == paramInt);
/*     */   }
/*     */ 
/*     */   
/*  30 */   private long lastReachHitTime = 0L;
/*  31 */   private int consecutiveReachHits = 0;
/*  32 */   private final Random random = new Random();
/*     */   
/*     */   public boolean checkConditions(Entity paramEntity) {
/*  35 */     if (paramEntity == null || this.mc.field_71439_g == null) {
/*  36 */       return false;
/*     */     }
/*     */     
/*  39 */     double d = this.mc.field_71439_g.func_70032_d(paramEntity);
/*  40 */     long l = System.currentTimeMillis();
/*     */ 
/*     */     
/*  43 */     if (l - this.lastReachHitTime < 500L && 
/*  44 */       this.random.nextFloat() > 0.3D) {
/*  45 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  50 */     if (this.consecutiveReachHits >= 2 && 
/*  51 */       this.random.nextFloat() > 0.1D) {
/*  52 */       this.consecutiveReachHits = 0;
/*  53 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     boolean bool = (this.mc.field_71439_g.func_70051_ag() && this.mc.field_71474_y.field_74312_F.func_151470_d() && d >= minReach && d <= maxReach && this.random.nextFloat() * 100.0F < chance && isWithinAngle(paramEntity, 45.0F) && getTPS() >= 18.0D) ? true : false;
/*     */     
/*  64 */     if (bool) {
/*  65 */       this.lastReachHitTime = l;
/*  66 */       this.consecutiveReachHits++;
/*     */     }
/*  68 */     else if (d < minReach) {
/*  69 */       this.consecutiveReachHits = 0;
/*     */     } 
/*     */ 
/*     */     
/*  73 */     return bool;
/*     */   }
/*     */   
/*     */   private boolean isWithinAngle(Entity paramEntity, float paramFloat) {
/*  77 */     Vec3 vec31 = this.mc.field_71439_g.func_70676_i(1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     Vec3 vec32 = (new Vec3(paramEntity.field_70165_t - this.mc.field_71439_g.field_70165_t, paramEntity.field_70163_u - this.mc.field_71439_g.field_70163_u, paramEntity.field_70161_v - this.mc.field_71439_g.field_70161_v)).func_72432_b();
/*  83 */     double d = vec31.func_72430_b(vec32);
/*  84 */     if (d > 1.0D) {
/*  85 */       d = 1.0D;
/*  86 */     } else if (d < -1.0D) {
/*  87 */       d = -1.0D;
/*     */     } 
/*  89 */     return (Math.toDegrees(Math.acos(d)) <= paramFloat);
/*     */   }
/*     */   
/*     */   public Entity findTarget(double paramDouble) {
/*  93 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     Entity entity = null;
/*  98 */     Vec3 vec31 = this.mc.field_71439_g.func_174824_e(1.0F);
/*  99 */     Vec3 vec32 = this.mc.field_71439_g.func_70676_i(1.0F);
/*     */ 
/*     */     
/* 102 */     double d1 = 0.02D;
/* 103 */     double d2 = (this.random.nextDouble() - 0.5D) * d1;
/* 104 */     double d3 = (this.random.nextDouble() - 0.5D) * d1;
/* 105 */     double d4 = (this.random.nextDouble() - 0.5D) * d1;
/*     */     
/* 107 */     Vec3 vec33 = vec31.func_72441_c(vec32.field_72450_a * paramDouble + d2, vec32.field_72448_b * paramDouble + d3, vec32.field_72449_c * paramDouble + d4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     List list = this.mc.field_71441_e.func_72839_b((Entity)this.mc.field_71439_g, this.mc.field_71439_g
/*     */         
/* 115 */         .func_174813_aQ()
/* 116 */         .func_72321_a(vec32.field_72450_a * paramDouble, vec32.field_72448_b * paramDouble, vec32.field_72449_c * paramDouble)
/* 117 */         .func_72314_b(1.0D, 1.0D, 1.0D));
/*     */ 
/*     */     
/* 120 */     double d5 = paramDouble;
/* 121 */     for (Entity entity1 : list) {
/* 122 */       if (entity1.func_70067_L()) {
/* 123 */         AxisAlignedBB axisAlignedBB = entity1.func_174813_aQ().func_72314_b(0.1D, 0.1D, 0.1D);
/* 124 */         MovingObjectPosition movingObjectPosition = axisAlignedBB.func_72327_a(vec31, vec33);
/* 125 */         if (axisAlignedBB.func_72318_a(vec31)) {
/* 126 */           if (d5 >= 0.0D) {
/* 127 */             entity = entity1;
/* 128 */             d5 = 0.0D;
/*     */           }  continue;
/* 130 */         }  if (movingObjectPosition != null) {
/* 131 */           double d = vec31.func_72438_d(movingObjectPosition.field_72307_f);
/* 132 */           if (d < d5 || d5 == 0.0D) {
/* 133 */             entity = entity1;
/* 134 */             d5 = d;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 139 */     return entity;
/*     */   }
/*     */   
/* 142 */   private long lastPacketTime = -1L;
/* 143 */   private double tps = 20.0D;
/*     */   
/*     */   public void updateTPS() {
/* 146 */     long l = System.currentTimeMillis();
/* 147 */     if (this.lastPacketTime != -1L) {
/* 148 */       long l1 = l - this.lastPacketTime;
/* 149 */       if (l1 > 0L) {
/* 150 */         double d = 20.0D * 1000.0D / l1;
/* 151 */         if (d > 20.0D) d = 20.0D; 
/* 152 */         if (d < 0.0D) d = 0.0D;
/*     */         
/* 154 */         this.tps = this.tps * 0.7D + d * 0.3D;
/*     */       } 
/*     */     } 
/* 157 */     this.lastPacketTime = l;
/*     */   }
/*     */   
/*     */   private double getTPS() {
/* 161 */     return this.tps;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\ghostreach\GhostReachCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */