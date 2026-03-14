/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import com.fogclient.setting.ModeSetting;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import java.util.Queue;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ 
/*     */ public class SuperKBModule
/*     */   extends Module
/*     */ {
/*  25 */   private final BooleanSetting superKbEnabled = new BooleanSetting("SuperKB", this, true);
/*  26 */   private final ModeSetting kbMode = new ModeSetting("KB Mode", this, "Packet", new String[] { "Packet", "WTap+ReSprint", "Legit" });
/*  27 */   private final BooleanSetting resprintJitter = new BooleanSetting("Jitter", this, false);
/*  28 */   private final NumberSetting resprintTicks = new NumberSetting("Resprint Ticks", this, 1.0D, 1.0D, 5.0D, 1.0D);
/*     */   
/*  30 */   private final BooleanSetting pingSpoofEnabled = new BooleanSetting("PingSpoof", this, false);
/*  31 */   private final NumberSetting pingDelay = new NumberSetting("Ping Delay", this, 100.0D, 0.0D, 1000.0D, 50.0D);
/*     */   
/*  33 */   private final Timer pingTimer = new Timer("FogClient-PingSpoof", true);
/*  34 */   private final Queue<EntityLivingBase> pendingAttacks = new ConcurrentLinkedQueue<>();
/*  35 */   private long clientTicks = 0L;
/*  36 */   private long resprintAtTick = -1L;
/*     */   
/*     */   public SuperKBModule() {
/*  39 */     super("EmpurraTudo", "Deals massive knockback to players.", Category.COMBAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  44 */     this.pendingAttacks.clear();
/*  45 */     this.resprintAtTick = -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  50 */     this.pendingAttacks.clear();
/*  51 */     this.resprintAtTick = -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/*  56 */     if (paramEvent instanceof AttackEntityEvent) {
/*  57 */       onAttack((AttackEntityEvent)paramEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onAttack(AttackEntityEvent paramAttackEntityEvent) {
/*  62 */     if (!isEnabled())
/*  63 */       return;  if (mc.field_71439_g == null || mc.field_71441_e == null || paramAttackEntityEvent.target == null)
/*  64 */       return;  if (!(paramAttackEntityEvent.target instanceof EntityLivingBase))
/*     */       return; 
/*  66 */     final EntityLivingBase target = (EntityLivingBase)paramAttackEntityEvent.target;
/*     */     
/*  68 */     if (this.pingSpoofEnabled.isEnabled()) {
/*  69 */       this.pingTimer.schedule(new TimerTask()
/*     */           {
/*     */             public void run() {
/*  72 */               SuperKBModule.this.pendingAttacks.add(target);
/*     */             }
/*  74 */           },  (long)this.pingDelay.getValue());
/*     */     } else {
/*  76 */       applySuperKB(entityLivingBase);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applySuperKB(EntityLivingBase paramEntityLivingBase) {
/*  81 */     if (!this.superKbEnabled.isEnabled())
/*     */       return; 
/*  83 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/*  84 */     if (entityPlayerSP != null && entityPlayerSP.func_70051_ag() && paramEntityLivingBase.field_70737_aN >= 5) {
/*  85 */       String str = this.kbMode.getValue();
/*     */       
/*  87 */       if (str.equals("Packet")) {
/*  88 */         entityPlayerSP.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)entityPlayerSP, C0BPacketEntityAction.Action.STOP_SPRINTING));
/*  89 */         entityPlayerSP.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)entityPlayerSP, C0BPacketEntityAction.Action.START_SPRINTING));
/*  90 */       } else if (str.equals("WTap+ReSprint")) {
/*  91 */         entityPlayerSP.func_70031_b(false);
/*  92 */         int i = 0;
/*  93 */         if (this.resprintJitter.isEnabled()) {
/*  94 */           i = ThreadLocalRandom.current().nextInt(0, 2);
/*     */         }
/*  96 */         this.resprintAtTick = this.clientTicks + Math.max(1L, (long)this.resprintTicks.getValue()) + i;
/*     */       } else {
/*  98 */         entityPlayerSP.func_70031_b(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 105 */     if (!isEnabled())
/* 106 */       return;  if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/* 108 */     this.clientTicks++;
/*     */     
/*     */     EntityLivingBase entityLivingBase;
/* 111 */     while ((entityLivingBase = this.pendingAttacks.poll()) != null) {
/* 112 */       applySuperKB(entityLivingBase);
/*     */     }
/*     */     
/* 115 */     if (this.resprintAtTick != -1L && this.clientTicks >= this.resprintAtTick && mc.field_71439_g != null) {
/* 116 */       EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/* 117 */       boolean bool = (entityPlayerSP.field_71158_b != null && entityPlayerSP.field_71158_b.field_78900_b > 0.0F) ? true : false;
/* 118 */       if (bool && !entityPlayerSP.func_70093_af() && !entityPlayerSP.func_71039_bw()) {
/* 119 */         entityPlayerSP.func_70031_b(true);
/*     */       }
/* 121 */       this.resprintAtTick = -1L;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\SuperKBModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */