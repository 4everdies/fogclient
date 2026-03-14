/*     */ package com.fogclient.module.movement;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegitBridgeModule
/*     */   extends Module
/*     */ {
/*  20 */   private long nextClickTime = 0L;
/*  21 */   private long nextReleaseTime = 0L;
/*     */   private boolean isHolding = false;
/*     */   private boolean wasOnEdge = false;
/*     */   private boolean bridgingActive = false;
/*  25 */   private final Random random = new Random();
/*     */   
/*     */   private BooleanSetting toggleMode;
/*     */   
/*     */   public LegitBridgeModule() {
/*  30 */     super("LegitBridge", "Ajuda a fazer pontes (simula clicks).", Category.MOVEMENT);
/*  31 */     this.keybind = 0;
/*  32 */     this.actionKeybind = 0;
/*     */     
/*  34 */     this.toggleMode = new BooleanSetting("Toggle Action", this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  39 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAction() {
/*  44 */     if (isEnabled() && 
/*  45 */       this.toggleMode.isEnabled())
/*     */     {
/*  47 */       this.bridgingActive = !this.bridgingActive;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  54 */     this.isHolding = false;
/*  55 */     this.wasOnEdge = false;
/*  56 */     this.bridgingActive = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  61 */     if (this.isHolding) {
/*  62 */       int i = (Minecraft.func_71410_x()).field_71474_y.field_74313_G.func_151463_i();
/*  63 */       KeyBinding.func_74510_a(i, false);
/*  64 */       this.isHolding = false;
/*     */     } 
/*  66 */     this.bridgingActive = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  71 */     if (!isEnabled())
/*  72 */       return;  Minecraft minecraft = Minecraft.func_71410_x();
/*     */     
/*  74 */     boolean bool = false;
/*     */ 
/*     */     
/*  77 */     if (this.toggleMode.isEnabled()) {
/*     */       
/*  79 */       bool = (this.actionKeybind == 0 || this.bridgingActive);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  84 */     else if (this.actionKeybind == 0) {
/*  85 */       bool = true;
/*     */     } else {
/*  87 */       bool = Keyboard.isKeyDown(this.actionKeybind);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     if (!bool) {
/*     */       
/*  93 */       if (this.isHolding) {
/*  94 */         int i = minecraft.field_71474_y.field_74313_G.func_151463_i();
/*  95 */         KeyBinding.func_74510_a(i, false);
/*  96 */         this.isHolding = false;
/*     */       } 
/*  98 */       this.wasOnEdge = false;
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     if (minecraft.field_71439_g == null || minecraft.field_71441_e == null)
/* 103 */       return;  if (minecraft.field_71462_r != null) {
/*     */       return;
/*     */     }
/* 106 */     if (minecraft.field_71439_g.func_70694_bm() != null && minecraft.field_71439_g.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemBlock) {
/*     */ 
/*     */       
/* 109 */       boolean bool1 = shouldPlaceBlock(minecraft);
/*     */       
/* 111 */       if (bool1) {
/*     */         
/* 113 */         if (!this.wasOnEdge) {
/* 114 */           this.nextClickTime = System.currentTimeMillis() + this.random.nextInt(20);
/* 115 */           this.isHolding = false;
/*     */         } 
/*     */         
/* 118 */         performHumanClick(minecraft);
/*     */       
/*     */       }
/* 121 */       else if (this.isHolding) {
/* 122 */         int i = minecraft.field_71474_y.field_74313_G.func_151463_i();
/* 123 */         KeyBinding.func_74510_a(i, false);
/* 124 */         this.isHolding = false;
/*     */       } 
/*     */ 
/*     */       
/* 128 */       this.wasOnEdge = bool1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void performHumanClick(Minecraft paramMinecraft) {
/* 133 */     long l = System.currentTimeMillis();
/* 134 */     int i = paramMinecraft.field_71474_y.field_74313_G.func_151463_i();
/*     */     
/* 136 */     if (!this.isHolding) {
/*     */       
/* 138 */       if (l >= this.nextClickTime) {
/* 139 */         KeyBinding.func_74510_a(i, true);
/* 140 */         KeyBinding.func_74507_a(i);
/* 141 */         this.isHolding = true;
/*     */         
/* 143 */         updateHoldTime();
/*     */       }
/*     */     
/*     */     }
/* 147 */     else if (l >= this.nextReleaseTime) {
/* 148 */       KeyBinding.func_74510_a(i, false);
/* 149 */       this.isHolding = false;
/*     */       
/* 151 */       updateNextDelay(paramMinecraft);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateHoldTime() {
/* 159 */     long l = 30L + (long)(Math.abs(this.random.nextGaussian()) * 15.0D);
/*     */ 
/*     */     
/* 162 */     if (l < 15L) l = 15L; 
/* 163 */     if (l > 80L) l = 80L;
/*     */     
/* 165 */     this.nextReleaseTime = System.currentTimeMillis() + l;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNextDelay(Minecraft paramMinecraft) {
/* 170 */     if (paramMinecraft.field_71439_g.field_70181_x < -0.05D) {
/* 171 */       long l1 = (10 + this.random.nextInt(20));
/* 172 */       this.nextClickTime = System.currentTimeMillis() + l1;
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 181 */     byte b = 35;
/* 182 */     int i = (int)(this.random.nextGaussian() * 10.0D);
/*     */     
/* 184 */     long l = (b + i);
/*     */ 
/*     */     
/* 187 */     if (this.random.nextInt(50) == 0) {
/* 188 */       l += (50 + this.random.nextInt(100));
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (l < 10L) l = 10L; 
/* 193 */     if (l > 100L) l = 100L;
/*     */     
/* 195 */     this.nextClickTime = System.currentTimeMillis() + l;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldPlaceBlock(Minecraft paramMinecraft) {
/* 201 */     BlockPos blockPos = new BlockPos(paramMinecraft.field_71439_g.field_70165_t, paramMinecraft.field_71439_g.field_70163_u - 1.0D, paramMinecraft.field_71439_g.field_70161_v);
/*     */ 
/*     */     
/* 204 */     if (paramMinecraft.field_71441_e.func_175623_d(blockPos)) {
/* 205 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 209 */     if (paramMinecraft.field_71439_g.field_70181_x < -0.05D) {
/* 210 */       return true;
/*     */     }
/*     */     
/* 213 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\movement\LegitBridgeModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */