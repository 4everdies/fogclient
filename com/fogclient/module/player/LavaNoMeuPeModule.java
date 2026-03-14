/*     */ package com.fogclient.module.player;
/*     */ import com.fogclient.module.Module;
/*     */ import java.lang.reflect.Field;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.ItemRenderer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ 
/*     */ public class LavaNoMeuPeModule extends Module {
/*  15 */   private int state = 0;
/*  16 */   private int stateTicks = 0;
/*  17 */   private int oldSlot = -1;
/*  18 */   private float oldPitch = 0.0F;
/*  19 */   private float oldYaw = 0.0F;
/*     */   
/*     */   private BlockPos lavaPos;
/*     */   private static Field itemRendererField;
/*     */   
/*     */   public LavaNoMeuPeModule() {
/*  25 */     super("LavaNoMeuPe", "Places lava under your feet", Category.PLAYER);
/*  26 */     this.keybind = 0;
/*     */     try {
/*  28 */       itemRendererField = ReflectionHelper.findField(EntityRenderer.class, new String[] { "itemRenderer", "field_78516_c" });
/*  29 */       itemRendererField.setAccessible(true);
/*  30 */     } catch (Exception exception) {
/*  31 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAction() {
/*  42 */     if (isEnabled() && this.state == 0) {
/*  43 */       this.state = 1;
/*  44 */       this.stateTicks = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  50 */     this.state = 0;
/*  51 */     this.stateTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  56 */     this.state = 0;
/*  57 */     this.stateTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  62 */     if (mc.field_71439_g == null)
/*     */       return; 
/*  64 */     if (this.state > 0) {
/*  65 */       byte b; byte b1; switch (this.state) {
/*     */         case 1:
/*  67 */           b = -1;
/*  68 */           for (b1 = 0; b1 < 9; b1++) {
/*  69 */             ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70301_a(b1);
/*  70 */             if (itemStack != null && itemStack.func_77973_b() == Items.field_151129_at) {
/*  71 */               b = b1;
/*     */               break;
/*     */             } 
/*     */           } 
/*  75 */           if (b != -1) {
/*  76 */             this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  77 */             this.oldPitch = mc.field_71439_g.field_70125_A;
/*  78 */             this.oldYaw = mc.field_71439_g.field_70177_z;
/*  79 */             mc.field_71439_g.field_71071_by.field_70461_c = b;
/*  80 */             mc.field_71439_g.field_70125_A = 90.0F;
/*  81 */             double d1 = mc.field_71439_g.field_70165_t;
/*  82 */             double d2 = mc.field_71439_g.field_70163_u;
/*  83 */             double d3 = mc.field_71439_g.field_70161_v;
/*  84 */             this.lavaPos = new BlockPos(Math.floor(d1), Math.floor(d2) - 1.0D, Math.floor(d3));
/*  85 */             this.state = 2;
/*  86 */             this.stateTicks = 0; break;
/*     */           } 
/*  88 */           this.state = 0;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2:
/*  93 */           if (this.stateTicks == 0) {
/*  94 */             if (mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm())) {
/*  95 */               resetEquippedProgress();
/*     */             }
/*  97 */           } else if (this.stateTicks >= 3 && this.stateTicks <= 5 && this.lavaPos != null) {
/*     */             
/*  99 */             BlockPos blockPos = this.lavaPos;
/* 100 */             double d1 = mc.field_71439_g.func_70092_e(blockPos.func_177958_n() + 0.5D, blockPos.func_177956_o() + 0.5D, blockPos.func_177952_p() + 0.5D);
/* 101 */             for (byte b2 = -1; b2 <= 1; b2++) {
/* 102 */               for (byte b3 = -1; b3 <= 1; b3++) {
/* 103 */                 for (byte b4 = -1; b4 <= 1; b4++) {
/* 104 */                   BlockPos blockPos1 = blockPos.func_177982_a(b2, b3, b4);
/* 105 */                   if (mc.field_71441_e.func_180495_p(blockPos1).func_177230_c() == Blocks.field_150353_l || mc.field_71441_e.func_180495_p(blockPos1).func_177230_c() == Blocks.field_150356_k) {
/* 106 */                     double d = mc.field_71439_g.func_70092_e(blockPos1.func_177958_n() + 0.5D, blockPos1.func_177956_o() + 0.5D, blockPos1.func_177952_p() + 0.5D);
/* 107 */                     if (d < d1) {
/* 108 */                       d1 = d;
/* 109 */                       blockPos = blockPos1;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 115 */             double d2 = blockPos.func_177958_n() + 0.5D;
/* 116 */             double d3 = blockPos.func_177956_o() + 0.5D;
/* 117 */             double d4 = blockPos.func_177952_p() + 0.5D;
/* 118 */             double d5 = d2 - mc.field_71439_g.field_70165_t;
/* 119 */             double d6 = d4 - mc.field_71439_g.field_70161_v;
/* 120 */             double d7 = d3 - mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e();
/* 121 */             double d8 = Math.sqrt(d5 * d5 + d6 * d6);
/* 122 */             float f1 = (float)(Math.atan2(d6, d5) * 180.0D / Math.PI) - 90.0F;
/* 123 */             float f2 = (float)-(Math.atan2(d7, d8) * 180.0D / Math.PI);
/* 124 */             mc.field_71439_g.field_70177_z = f1;
/* 125 */             mc.field_71439_g.field_70125_A = f2;
/* 126 */             if (mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm())) {
/* 127 */               resetEquippedProgress();
/*     */             }
/*     */           } 
/* 130 */           this.stateTicks++;
/* 131 */           if (this.stateTicks > 6) {
/* 132 */             if (this.oldSlot != -1) mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot; 
/* 133 */             mc.field_71439_g.field_70177_z = this.oldYaw;
/* 134 */             mc.field_71439_g.field_70125_A = this.oldPitch;
/* 135 */             this.state = 0;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetEquippedProgress() {
/*     */     try {
/* 145 */       if (itemRendererField != null) {
/* 146 */         ItemRenderer itemRenderer = (ItemRenderer)itemRendererField.get(mc.field_71460_t);
/* 147 */         if (itemRenderer != null) {
/* 148 */           itemRenderer.func_78444_b();
/*     */         }
/*     */       } 
/* 151 */     } catch (Exception exception) {
/* 152 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\player\LavaNoMeuPeModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */