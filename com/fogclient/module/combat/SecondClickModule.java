/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.KeySetting;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class SecondClickModule
/*     */   extends Module
/*     */ {
/*  13 */   private final KeySetting leftClickKey = new KeySetting("Left Click Key", this, 0);
/*  14 */   private final KeySetting rightClickKey = new KeySetting("Right Click Key", this, 0);
/*     */   
/*     */   private boolean lastLeftDown = false;
/*     */   
/*     */   private boolean lastRightDown = false;
/*  19 */   private int leftDownTicks = 0;
/*  20 */   private int rightDownTicks = 0;
/*     */   
/*     */   public SecondClickModule() {
/*  23 */     super("DuploClique", "Simulates a double click.", Category.COMBAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  28 */     this.lastLeftDown = false;
/*  29 */     this.lastRightDown = false;
/*  30 */     this.leftDownTicks = 0;
/*  31 */     this.rightDownTicks = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  37 */     if (this.leftClickKey.getKeyCode() != 0) {
/*  38 */       resetKey(mc.field_71474_y.field_74312_F);
/*     */     }
/*  40 */     if (this.rightClickKey.getKeyCode() != 0) {
/*  41 */       resetKey(mc.field_71474_y.field_74313_G);
/*     */     }
/*  43 */     this.lastLeftDown = false;
/*  44 */     this.lastRightDown = false;
/*  45 */     this.leftDownTicks = 0;
/*  46 */     this.rightDownTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  51 */     if (!isEnabled())
/*  52 */       return;  if (mc.field_71462_r != null) {
/*     */       return;
/*     */     }
/*  55 */     int i = this.leftClickKey.getKeyCode();
/*  56 */     if (i != 0) {
/*  57 */       boolean bool1 = isPhysicalKeyDown(i);
/*     */       
/*  59 */       if (bool1) {
/*     */         
/*  61 */         if (!this.lastLeftDown) {
/*  62 */           KeyBinding.func_74507_a(mc.field_71474_y.field_74312_F.func_151463_i());
/*  63 */           this.leftDownTicks = 0;
/*     */         } 
/*  65 */         this.leftDownTicks++;
/*     */       } else {
/*  67 */         this.leftDownTicks = 0;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       boolean bool2 = isPhysicalKeyDown(mc.field_71474_y.field_74312_F.func_151463_i());
/*  76 */       boolean bool = (bool2 || (bool1 && this.leftDownTicks > 2)) ? true : false;
/*     */       
/*  78 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), bool);
/*     */       
/*  80 */       this.lastLeftDown = bool1;
/*     */     } 
/*     */ 
/*     */     
/*  84 */     int j = this.rightClickKey.getKeyCode();
/*  85 */     if (j != 0) {
/*  86 */       boolean bool1 = isPhysicalKeyDown(j);
/*     */       
/*  88 */       if (bool1) {
/*     */         
/*  90 */         if (!this.lastRightDown) {
/*  91 */           KeyBinding.func_74507_a(mc.field_71474_y.field_74313_G.func_151463_i());
/*  92 */           this.rightDownTicks = 0;
/*     */         } 
/*  94 */         this.rightDownTicks++;
/*     */       } else {
/*  96 */         this.rightDownTicks = 0;
/*     */       } 
/*     */ 
/*     */       
/* 100 */       boolean bool2 = isPhysicalKeyDown(mc.field_71474_y.field_74313_G.func_151463_i());
/* 101 */       boolean bool = (bool2 || (bool1 && this.rightDownTicks > 2)) ? true : false;
/*     */       
/* 103 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), bool);
/*     */       
/* 105 */       this.lastRightDown = bool1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isPhysicalKeyDown(int paramInt) {
/* 110 */     if (paramInt < 0) {
/* 111 */       return Mouse.isButtonDown(paramInt + 100);
/*     */     }
/* 113 */     return Keyboard.isKeyDown(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetKey(KeyBinding paramKeyBinding) {
/* 118 */     boolean bool = isPhysicalKeyDown(paramKeyBinding.func_151463_i());
/* 119 */     if (!bool)
/* 120 */       KeyBinding.func_74510_a(paramKeyBinding.func_151463_i(), false); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\SecondClickModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */