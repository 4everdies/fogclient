/*     */ package com.fogclient.module.misc;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import net.minecraftforge.client.event.EntityViewRenderEvent;
/*     */ import net.minecraftforge.client.event.RenderBlockOverlayEvent;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FullOptimizerModule
/*     */   extends Module
/*     */ {
/*     */   public static FullOptimizerModule INSTANCE;
/*  30 */   public final BooleanSetting optimizeFog = new BooleanSetting("Optimize Fog", this, true);
/*  31 */   public final BooleanSetting optimizeBlockAnimations = new BooleanSetting("Optimize Block Anim", this, true);
/*  32 */   public final BooleanSetting optimizeLighting = new BooleanSetting("Optimize Lighting", this, true);
/*  33 */   public final BooleanSetting forceMinRenderDistance = new BooleanSetting("Force Min Render", this, true);
/*  34 */   public final BooleanSetting disableParticles = new BooleanSetting("Disable Particles", this, true);
/*  35 */   public final BooleanSetting disableShaders = new BooleanSetting("Disable Shaders", this, true);
/*  36 */   public final BooleanSetting solidRenderOnly = new BooleanSetting("Solid Render Only", this, false);
/*  37 */   public final BooleanSetting optimizeTileEntities = new BooleanSetting("Optimize TileEntities", this, true);
/*  38 */   public final BooleanSetting optimizeRedstone = new BooleanSetting("Optimize Redstone", this, true);
/*  39 */   public final BooleanSetting disableWeather = new BooleanSetting("Disable Weather", this, true);
/*  40 */   public final BooleanSetting cancelBackgroundThreads = new BooleanSetting("Cancel BG Threads", this, true);
/*  41 */   public final BooleanSetting ignorePartialTicks = new BooleanSetting("Ignore Partial Ticks", this, true);
/*  42 */   public final BooleanSetting directInput = new BooleanSetting("Direct Input", this, true);
/*  43 */   public final BooleanSetting disableMouseAccel = new BooleanSetting("Disable Mouse Accel", this, true);
/*  44 */   public final BooleanSetting reduceGLCalls = new BooleanSetting("Reduce GL Calls", this, true);
/*     */   
/*  46 */   private int originalRenderDistance = -1;
/*  47 */   private int originalAmbientOcclusion = -1;
/*  48 */   private int originalParticleSetting = -1;
/*     */   private boolean originalFog = true;
/*  50 */   private int originalFrameRate = -1;
/*     */   
/*     */   public FullOptimizerModule() {
/*  53 */     super("FPSTurbo", "Optimizes game performance", Category.MISC);
/*  54 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  59 */     if (this.forceMinRenderDistance.getValue()) {
/*  60 */       this.originalRenderDistance = mc.field_71474_y.field_151451_c;
/*  61 */       mc.field_71474_y.field_151451_c = 2;
/*     */     } 
/*     */     
/*  64 */     if (this.optimizeLighting.getValue()) {
/*  65 */       this.originalAmbientOcclusion = mc.field_71474_y.field_74348_k;
/*  66 */       mc.field_71474_y.field_74348_k = 0;
/*  67 */       mc.field_71474_y.field_74333_Y = 100.0F;
/*     */     } 
/*     */     
/*  70 */     if (this.disableParticles.getValue()) {
/*  71 */       this.originalParticleSetting = mc.field_71474_y.field_74362_aa;
/*  72 */       mc.field_71474_y.field_74362_aa = 2;
/*     */     } 
/*     */     
/*  75 */     if (this.optimizeFog.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     if (this.directInput.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     if (this.disableMouseAccel.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (this.reduceGLCalls.getValue()) {
/*     */       
/*  93 */       this.originalFrameRate = mc.field_71474_y.field_74350_i;
/*  94 */       mc.field_71474_y.field_74350_i = 260;
/*  95 */       mc.field_71474_y.field_74352_v = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 101 */     if (this.originalRenderDistance != -1) {
/* 102 */       mc.field_71474_y.field_151451_c = this.originalRenderDistance;
/* 103 */       this.originalRenderDistance = -1;
/*     */     } 
/*     */     
/* 106 */     if (this.originalAmbientOcclusion != -1) {
/* 107 */       mc.field_71474_y.field_74348_k = this.originalAmbientOcclusion;
/* 108 */       this.originalAmbientOcclusion = -1;
/* 109 */       mc.field_71474_y.field_74333_Y = 1.0F;
/*     */     } 
/*     */     
/* 112 */     if (this.originalParticleSetting != -1) {
/* 113 */       mc.field_71474_y.field_74362_aa = this.originalParticleSetting;
/* 114 */       this.originalParticleSetting = -1;
/*     */     } 
/*     */     
/* 117 */     if (this.originalFrameRate != -1) {
/* 118 */       mc.field_71474_y.field_74350_i = this.originalFrameRate;
/* 119 */       this.originalFrameRate = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/* 125 */     if (paramEvent instanceof TickEvent.WorldTickEvent) {
/* 126 */       onWorldTick((TickEvent.WorldTickEvent)paramEvent);
/* 127 */     } else if (paramEvent instanceof BlockEvent.NeighborNotifyEvent) {
/* 128 */       onNeighborNotify((BlockEvent.NeighborNotifyEvent)paramEvent);
/* 129 */     } else if (paramEvent instanceof EntityViewRenderEvent.FogDensity) {
/* 130 */       if (this.optimizeFog.getValue() && isEnabled()) {
/* 131 */         EntityViewRenderEvent.FogDensity fogDensity = (EntityViewRenderEvent.FogDensity)paramEvent;
/* 132 */         fogDensity.density = 0.0F;
/* 133 */         fogDensity.setCanceled(true);
/*     */       } 
/* 135 */     } else if (!(paramEvent instanceof EntityViewRenderEvent.FogColors) || 
/* 136 */       !this.optimizeFog.getValue() || isEnabled()) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderBlockOverlay(RenderBlockOverlayEvent paramRenderBlockOverlayEvent) {
/* 144 */     if (isEnabled() && this.optimizeBlockAnimations.getValue())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       if (paramRenderBlockOverlayEvent.overlayType != RenderBlockOverlayEvent.OverlayType.BLOCK);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void onWorldTick(TickEvent.WorldTickEvent paramWorldTickEvent) {
/* 157 */     if (!isEnabled())
/* 158 */       return;  World world = paramWorldTickEvent.world;
/* 159 */     if (world == null)
/*     */       return; 
/* 161 */     if (this.disableWeather.getValue()) {
/* 162 */       WorldInfo worldInfo = world.func_72912_H();
/* 163 */       if (worldInfo != null) {
/* 164 */         worldInfo.func_176142_i(999999);
/* 165 */         worldInfo.func_76080_g(0);
/* 166 */         worldInfo.func_76090_f(0);
/* 167 */         worldInfo.func_76084_b(false);
/* 168 */         worldInfo.func_76069_a(false);
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     if (this.optimizeTileEntities.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 181 */     if (!isEnabled())
/*     */       return; 
/* 183 */     if (this.forceMinRenderDistance.getValue() && mc.field_71474_y.field_151451_c != 2) {
/* 184 */       mc.field_71474_y.field_151451_c = 2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (this.solidRenderOnly.getValue()) {
/* 190 */       mc.field_71474_y.field_74347_j = false;
/* 191 */       mc.field_71474_y.field_74345_l = 0;
/*     */     } 
/*     */ 
/*     */     
/* 195 */     if (this.disableMouseAccel.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onNeighborNotify(BlockEvent.NeighborNotifyEvent paramNeighborNotifyEvent) {
/* 202 */     if (!isEnabled()) {
/*     */       return;
/*     */     }
/* 205 */     if (this.optimizeLighting.getValue() || this.optimizeRedstone.getValue()) {
/* 206 */       World world = paramNeighborNotifyEvent.world;
/* 207 */       BlockPos blockPos = paramNeighborNotifyEvent.pos;
/* 208 */       if (world.field_72995_K) {
/*     */         
/* 210 */         world.func_175653_a(EnumSkyBlock.BLOCK, blockPos, 15);
/* 211 */         world.func_175653_a(EnumSkyBlock.SKY, blockPos, 15);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\FullOptimizerModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */