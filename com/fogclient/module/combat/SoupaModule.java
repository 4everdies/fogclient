/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class SoupaModule
/*     */   extends Module {
/*  14 */   private final Random random = new Random();
/*     */ 
/*     */   
/*  17 */   private int stage = 0;
/*  18 */   private int previousSlot = -1;
/*  19 */   private int soupSlot = -1;
/*     */   
/*     */   public SoupaModule() {
/*  22 */     super("SopaFacil", "Automatically eats soup when health is low.", Category.COMBAT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  28 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  34 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  39 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onKeyPressed() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAction() {
/*  49 */     if (isEnabled() && 
/*  50 */       this.stage == 0) {
/*  51 */       tryStartSoup((EntityPlayer)mc.field_71439_g);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTickStart() {
/*  58 */     if (!isEnabled())
/*     */       return; 
/*  60 */     if (mc.field_71439_g == null || mc.field_71441_e == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  65 */     if (this.stage == 1) {
/*  66 */       finishSoup((EntityPlayer)mc.field_71439_g);
/*     */       
/*  68 */       this.stage = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryStartSoup(EntityPlayer paramEntityPlayer) {
/*  73 */     if (paramEntityPlayer == null)
/*     */       return; 
/*  75 */     float f1 = paramEntityPlayer.func_110138_aP();
/*  76 */     float f2 = paramEntityPlayer.func_110143_aJ();
/*     */ 
/*     */ 
/*     */     
/*  80 */     if (f2 > f1 - 7.0F) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  85 */     ArrayList<Integer> arrayList = new ArrayList();
/*  86 */     for (byte b = 0; b < 9; b++) {
/*  87 */       ItemStack itemStack1 = paramEntityPlayer.field_71071_by.field_70462_a[b];
/*  88 */       if (itemStack1 != null && itemStack1.func_77973_b() == Items.field_151009_A) {
/*  89 */         arrayList.add(Integer.valueOf(b));
/*     */       }
/*     */     } 
/*     */     
/*  93 */     if (arrayList.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  98 */     this.soupSlot = ((Integer)arrayList.get(this.random.nextInt(arrayList.size()))).intValue();
/*  99 */     this.previousSlot = paramEntityPlayer.field_71071_by.field_70461_c;
/*     */ 
/*     */     
/* 102 */     paramEntityPlayer.field_71071_by.field_70461_c = this.soupSlot;
/*     */ 
/*     */     
/* 105 */     ItemStack itemStack = paramEntityPlayer.field_71071_by.func_70448_g();
/* 106 */     if (itemStack != null && itemStack.func_77973_b() == Items.field_151009_A) {
/* 107 */       mc.field_71442_b.func_78769_a(paramEntityPlayer, (World)mc.field_71441_e, itemStack);
/*     */     }
/*     */     
/* 110 */     this.stage = 1;
/*     */   }
/*     */   
/*     */   private void finishSoup(EntityPlayer paramEntityPlayer) {
/* 114 */     if (paramEntityPlayer.field_71071_by.field_70461_c == this.soupSlot) {
/* 115 */       mc.field_71439_g.func_71040_bB(false);
/*     */     }
/*     */     
/* 118 */     if (this.previousSlot != -1) {
/* 119 */       paramEntityPlayer.field_71071_by.field_70461_c = this.previousSlot;
/*     */     }
/*     */     
/* 122 */     reset();
/*     */   }
/*     */   
/*     */   private void reset() {
/* 126 */     this.stage = 0;
/* 127 */     this.previousSlot = -1;
/* 128 */     this.soupSlot = -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\SoupaModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */