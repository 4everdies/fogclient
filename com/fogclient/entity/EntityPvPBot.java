/*     */ package com.fogclient.entity;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPvPBot extends EntityZombie {
/*  11 */   private int wTapCooldown = 0; private DificuldadeBot dificuldade;
/*  12 */   private int ticksDesdeUltimoHitRecebido = 100;
/*  13 */   private String nickExibido = "TreinoBOT";
/*     */   
/*     */   public EntityPvPBot(World paramWorld) {
/*  16 */     this(paramWorld, DificuldadeBot.FACIL);
/*     */   }
/*     */   
/*     */   public EntityPvPBot(World paramWorld, DificuldadeBot paramDificuldadeBot) {
/*  20 */     super(paramWorld);
/*  21 */     this.dificuldade = paramDificuldadeBot;
/*  22 */     this.field_70178_ae = true;
/*  23 */     func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10000.0D);
/*  24 */     func_70606_j(10000.0F);
/*  25 */     func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.287D);
/*     */   }
/*     */   
/*     */   public void setNickExibido(String paramString) {
/*  29 */     this.nickExibido = paramString;
/*  30 */     func_96094_a("Â§c[BOT]Â§r " + this.nickExibido);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_70636_d() {
/*  35 */     super.func_70636_d();
/*     */     
/*  37 */     func_70606_j(func_110138_aP());
/*  38 */     this.ticksDesdeUltimoHitRecebido++;
/*     */     
/*  40 */     EntityPlayer entityPlayer = this.field_70170_p.func_72890_a((Entity)this, 64.0D);
/*  41 */     if (entityPlayer != null && !entityPlayer.field_71075_bZ.field_75098_d && !entityPlayer.field_70128_L) {
/*     */       
/*  43 */       double d1 = entityPlayer.field_70165_t - this.field_70165_t;
/*  44 */       double d2 = entityPlayer.field_70161_v - this.field_70161_v;
/*  45 */       this.field_70177_z = (float)(Math.atan2(d2, d1) * 180.0D / Math.PI) - 90.0F;
/*  46 */       this.field_70759_as = this.field_70177_z;
/*  47 */       this.field_70761_aq = this.field_70177_z;
/*     */ 
/*     */ 
/*     */       
/*  51 */       if (this.ticksDesdeUltimoHitRecebido > 10) {
/*  52 */         if (this.wTapCooldown > 0) {
/*  53 */           func_70661_as().func_75499_g();
/*  54 */           this.wTapCooldown--;
/*     */         } else {
/*  56 */           func_70661_as().func_75497_a((Entity)entityPlayer, 1.0D);
/*     */         } 
/*     */       } else {
/*     */         
/*  60 */         func_70661_as().func_75497_a((Entity)entityPlayer, 1.0D);
/*  61 */         this.wTapCooldown = 0;
/*     */       } 
/*     */ 
/*     */       
/*  65 */       double d3 = func_70032_d((Entity)entityPlayer);
/*  66 */       double d4 = getAlcance();
/*     */       
/*  68 */       if (d3 <= d4) {
/*  69 */         func_71038_i();
/*  70 */         if (d3 <= 3.0D) {
/*  71 */           float f = (float)func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
/*  72 */           entityPlayer.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this), f);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getAlcance() {
/*  79 */     switch (this.dificuldade) { case FACIL:
/*  80 */         return 2.0D;
/*  81 */       case INTERMEDIARIO: return 2.5D;
/*  82 */       case DIFICIL: return 3.0D;
/*  83 */       case CHEATER: return 3.5D; }
/*  84 */      return 2.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String func_70639_aQ() {
/*  90 */     return null;
/*     */   } protected String func_70621_aR() {
/*  92 */     return null;
/*     */   } protected String func_70673_aS() {
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public boolean func_70097_a(DamageSource paramDamageSource, float paramFloat) {
/*  98 */     boolean bool = super.func_70097_a(paramDamageSource, paramFloat);
/*  99 */     if (!this.field_70170_p.field_72995_K && bool) {
/* 100 */       this.ticksDesdeUltimoHitRecebido = 0;
/*     */     }
/* 102 */     return bool;
/*     */   }
/*     */   
/*     */   public boolean func_70692_ba() {
/* 106 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\entity\EntityPvPBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */