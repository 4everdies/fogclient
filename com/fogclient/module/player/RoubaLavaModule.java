/*     */ package com.fogclient.module.player;
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import com.fogclient.setting.Setting;
/*     */ import java.lang.reflect.Field;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ 
/*     */ public class RoubaLavaModule extends Module {
/*     */   private BooleanSetting enableEmptyBucketLava;
/*     */   private BooleanSetting enableEmptyBucketWater;
/*     */   private BooleanSetting enableLavaReset;
/*  28 */   private int originalSlot = -1; private BooleanSetting enableWaterReset; private BooleanSetting enableAntiDrop; private NumberSetting detectionRange; private boolean wasActive = false;
/*  29 */   private int actionTimer = 0;
/*     */   private Field rightClickDelayTimerField;
/*     */   
/*     */   public RoubaLavaModule() {
/*  33 */     super("RoubaTudo", "Steals lava/water from nearby blocks", Category.PLAYER);
/*     */     
/*  35 */     this.enableEmptyBucketLava = new BooleanSetting("Lava Pickup", true);
/*  36 */     this.enableEmptyBucketWater = new BooleanSetting("Water Pickup", true);
/*  37 */     this.enableLavaReset = new BooleanSetting("Lava Reset", true);
/*  38 */     this.enableWaterReset = new BooleanSetting("Water Reset", true);
/*  39 */     this.enableAntiDrop = new BooleanSetting("Anti Drop", true);
/*  40 */     this.detectionRange = new NumberSetting("Range", 0.0D, 0.0D, 6.0D, 1.0D);
/*     */     
/*  42 */     addSetting((Setting)this.enableEmptyBucketLava);
/*  43 */     addSetting((Setting)this.enableEmptyBucketWater);
/*  44 */     addSetting((Setting)this.enableLavaReset);
/*  45 */     addSetting((Setting)this.enableWaterReset);
/*  46 */     addSetting((Setting)this.enableAntiDrop);
/*  47 */     addSetting((Setting)this.detectionRange);
/*     */     
/*     */     try {
/*  50 */       this.rightClickDelayTimerField = ReflectionHelper.findField(Minecraft.class, new String[] { "field_71467_ac", "rightClickDelayTimer" });
/*  51 */       this.rightClickDelayTimerField.setAccessible(true);
/*  52 */     } catch (Exception exception) {
/*  53 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  59 */     this.wasActive = false;
/*  60 */     this.originalSlot = -1;
/*  61 */     this.actionTimer = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  66 */     this.wasActive = false;
/*  67 */     if (this.originalSlot != -1) {
/*  68 */       mc.field_71439_g.field_71071_by.field_70461_c = this.originalSlot;
/*  69 */       this.originalSlot = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTickStart() {
/*  75 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/*  77 */     if (this.actionTimer > 0) {
/*  78 */       this.actionTimer--;
/*     */     }
/*     */     
/*  81 */     if (this.enableAntiDrop.isEnabled() && this.actionTimer > 0 && mc.field_71474_y.field_74316_C.func_151468_f()) {
/*  82 */       ItemStack itemStack = mc.field_71439_g.func_70694_bm();
/*  83 */       if (itemStack != null && (itemStack.func_77973_b() == Items.field_151133_ar || itemStack.func_77973_b() == Items.field_151129_at || itemStack.func_77973_b() == Items.field_151131_as)) {
/*  84 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74316_C.func_151463_i(), false);
/*     */       }
/*     */     } 
/*     */     
/*  88 */     boolean bool1 = false;
/*  89 */     boolean bool2 = false;
/*  90 */     int i = 5;
/*     */     
/*  92 */     if (this.detectionRange.getValue() == 0.0D) {
/*  93 */       if (isPlayerInDanger()) {
/*  94 */         bool2 = true;
/*  95 */         i = 5;
/*     */       } 
/*     */     } else {
/*  98 */       bool2 = true;
/*  99 */       i = (int)this.detectionRange.getValue();
/*     */     } 
/*     */     
/* 102 */     if (bool2) {
/* 103 */       float f = mc.field_71442_b.func_78757_d();
/* 104 */       BlockPos blockPos = findNearestLiquid(i, f);
/*     */       
/* 106 */       if (blockPos != null) {
/* 107 */         Material material = mc.field_71441_e.func_180495_p(blockPos).func_177230_c().func_149688_o();
/* 108 */         int j = findBucketSlot(mc.field_71439_g.field_71071_by, material);
/*     */         
/* 110 */         if (j != -1) {
/* 111 */           bool1 = true;
/* 112 */           lookAtBlock(blockPos);
/* 113 */           mc.field_71460_t.func_78473_a(1.0F);
/*     */           
/* 115 */           if (!this.wasActive) {
/* 116 */             this.originalSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */           }
/* 118 */           mc.field_71439_g.field_71071_by.field_70461_c = j;
/* 119 */           this.actionTimer = 10;
/*     */           
/* 121 */           ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70301_a(j);
/* 122 */           if (itemStack != null) {
/* 123 */             boolean bool3 = (material == Material.field_151587_i && itemStack.func_77973_b() == Items.field_151129_at) ? true : false;
/* 124 */             boolean bool4 = (material == Material.field_151586_h && itemStack.func_77973_b() == Items.field_151131_as) ? true : false;
/*     */             
/* 126 */             if (bool3 && !this.enableLavaReset.isEnabled()) {
/* 127 */               bool1 = false;
/* 128 */               mc.field_71439_g.field_71071_by.field_70461_c = (this.originalSlot != -1) ? this.originalSlot : mc.field_71439_g.field_71071_by.field_70461_c;
/*     */               return;
/*     */             } 
/* 131 */             if (bool4 && !this.enableWaterReset.isEnabled()) {
/* 132 */               bool1 = false;
/* 133 */               mc.field_71439_g.field_71071_by.field_70461_c = (this.originalSlot != -1) ? this.originalSlot : mc.field_71439_g.field_71071_by.field_70461_c;
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 138 */           if (this.rightClickDelayTimerField != null) {
/*     */             try {
/* 140 */               this.rightClickDelayTimerField.setInt(mc, 0);
/* 141 */             } catch (Exception exception) {}
/*     */           }
/*     */           
/* 144 */           boolean bool = false;
/*     */           
/* 146 */           if (mc.field_71476_x != null && mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
/* 147 */             BlockPos blockPos1 = mc.field_71476_x.func_178782_a();
/* 148 */             if (blockPos1.equals(blockPos) && 
/* 149 */               mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g(), blockPos1, mc.field_71476_x.field_178784_b, mc.field_71476_x.field_72307_f)) {
/* 150 */               mc.field_71439_g.func_71038_i();
/* 151 */               bool = true;
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 156 */           if (!bool && mc.field_71439_g.func_174818_b(blockPos) < (f * f)) {
/* 157 */             EnumFacing enumFacing = EnumFacing.UP;
/* 158 */             Vec3 vec3 = new Vec3(blockPos.func_177958_n() + 0.5D, blockPos.func_177956_o() + 0.5D, blockPos.func_177952_p() + 0.5D);
/*     */             
/* 160 */             if (mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g(), blockPos, enumFacing, vec3)) {
/* 161 */               mc.field_71439_g.func_71038_i();
/* 162 */               bool = true;
/*     */             } 
/*     */           } 
/*     */           
/* 166 */           if (!bool) {
/* 167 */             KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), true);
/* 168 */             KeyBinding.func_74507_a(mc.field_71474_y.field_74313_G.func_151463_i());
/*     */           } 
/*     */           
/* 171 */           if (this.rightClickDelayTimerField != null) {
/*     */             try {
/* 173 */               this.rightClickDelayTimerField.setInt(mc, 0);
/* 174 */             } catch (Exception exception) {}
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 180 */     if (this.wasActive && !bool1) {
/* 181 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74313_G.func_151463_i(), false);
/* 182 */       if (this.originalSlot != -1) {
/* 183 */         mc.field_71439_g.field_71071_by.field_70461_c = this.originalSlot;
/* 184 */         this.originalSlot = -1;
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     this.wasActive = bool1;
/*     */   }
/*     */   
/*     */   private boolean isPlayerInDanger() {
/* 192 */     if (mc.field_71439_g.func_70055_a(Material.field_151587_i) || mc.field_71439_g.func_70055_a(Material.field_151586_h)) return true;
/*     */     
/* 194 */     AxisAlignedBB axisAlignedBB = mc.field_71439_g.func_174813_aQ().func_72314_b(0.1D, 0.5D, 0.1D);
/*     */     
/* 196 */     int i = MathHelper.func_76128_c(axisAlignedBB.field_72340_a);
/* 197 */     int j = MathHelper.func_76128_c(axisAlignedBB.field_72336_d + 1.0D);
/* 198 */     int k = MathHelper.func_76128_c(axisAlignedBB.field_72338_b);
/* 199 */     int m = MathHelper.func_76128_c(axisAlignedBB.field_72337_e + 1.0D);
/* 200 */     int n = MathHelper.func_76128_c(axisAlignedBB.field_72339_c);
/* 201 */     int i1 = MathHelper.func_76128_c(axisAlignedBB.field_72334_f + 1.0D);
/*     */     
/* 203 */     for (int i2 = i; i2 < j; i2++) {
/* 204 */       for (int i3 = k; i3 < m; i3++) {
/* 205 */         for (int i4 = n; i4 < i1; i4++) {
/* 206 */           BlockPos blockPos = new BlockPos(i2, i3, i4);
/* 207 */           IBlockState iBlockState = mc.field_71441_e.func_180495_p(blockPos);
/* 208 */           Material material = iBlockState.func_177230_c().func_149688_o();
/* 209 */           if (material == Material.field_151587_i || material == Material.field_151586_h) {
/* 210 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 215 */     return false;
/*     */   }
/*     */   
/*     */   private int findBucketSlot(InventoryPlayer paramInventoryPlayer, Material paramMaterial) {
/* 219 */     byte b1 = -1;
/* 220 */     byte b2 = -1;
/*     */ 
/*     */     
/* 223 */     boolean bool1 = ((paramMaterial == Material.field_151587_i && this.enableEmptyBucketLava.isEnabled()) || (paramMaterial == Material.field_151586_h && this.enableEmptyBucketWater.isEnabled())) ? true : false;
/*     */     
/* 225 */     boolean bool2 = ((paramMaterial == Material.field_151587_i && this.enableLavaReset.isEnabled()) || (paramMaterial == Material.field_151586_h && this.enableWaterReset.isEnabled())) ? true : false;
/*     */     
/* 227 */     for (byte b = 0; b < 9; b++) {
/* 228 */       ItemStack itemStack = paramInventoryPlayer.func_70301_a(b);
/* 229 */       if (itemStack != null) {
/* 230 */         if (bool1 && itemStack.func_77973_b() == Items.field_151133_ar) {
/* 231 */           b1 = b; break;
/*     */         } 
/* 233 */         if (bool2) {
/* 234 */           if (paramMaterial == Material.field_151587_i && itemStack.func_77973_b() == Items.field_151129_at)
/* 235 */           { if (b2 == -1) b2 = b;  }
/* 236 */           else if (paramMaterial == Material.field_151586_h && itemStack.func_77973_b() == Items.field_151131_as && 
/* 237 */             b2 == -1) { b2 = b; }
/*     */         
/*     */         }
/*     */       } 
/*     */     } 
/* 242 */     return (b1 != -1) ? b1 : b2;
/*     */   }
/*     */   
/*     */   private BlockPos findNearestLiquid(int paramInt, float paramFloat) {
/* 246 */     BlockPos blockPos1 = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 247 */     BlockPos blockPos2 = null;
/* 248 */     double d1 = Double.MAX_VALUE;
/* 249 */     double d2 = (paramFloat * paramFloat);
/*     */     
/* 251 */     int i = paramInt;
/* 252 */     for (int j = -i; j <= i; j++) {
/* 253 */       for (int k = -i; k <= i; k++) {
/* 254 */         for (int m = -i; m <= i; m++) {
/* 255 */           BlockPos blockPos = blockPos1.func_177982_a(j, k, m);
/*     */           
/* 257 */           double d = mc.field_71439_g.func_174818_b(blockPos);
/* 258 */           if (d <= d2) {
/*     */             
/* 260 */             IBlockState iBlockState = mc.field_71441_e.func_180495_p(blockPos);
/* 261 */             Block block = iBlockState.func_177230_c();
/* 262 */             Material material = block.func_149688_o();
/*     */             
/* 264 */             if (material == Material.field_151587_i || material == Material.field_151586_h) {
/* 265 */               int n = block.func_176201_c(iBlockState);
/* 266 */               if (n == 0 && 
/* 267 */                 d < d1) {
/* 268 */                 d1 = d;
/* 269 */                 blockPos2 = blockPos;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 276 */     return blockPos2;
/*     */   }
/*     */   
/*     */   private void lookAtBlock(BlockPos paramBlockPos) {
/* 280 */     double d1 = paramBlockPos.func_177958_n() + 0.5D - mc.field_71439_g.field_70165_t;
/* 281 */     double d2 = paramBlockPos.func_177956_o() + 0.5D - mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e();
/* 282 */     double d3 = paramBlockPos.func_177952_p() + 0.5D - mc.field_71439_g.field_70161_v;
/*     */     
/* 284 */     double d4 = MathHelper.func_76133_a(d1 * d1 + d3 * d3);
/* 285 */     float f1 = (float)(MathHelper.func_181159_b(d3, d1) * 180.0D / Math.PI) - 90.0F;
/* 286 */     float f2 = (float)-(MathHelper.func_181159_b(d2, d4) * 180.0D / Math.PI);
/*     */     
/* 288 */     mc.field_71439_g.field_70125_A = f2;
/* 289 */     mc.field_71439_g.field_70177_z = f1;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\player\RoubaLavaModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */