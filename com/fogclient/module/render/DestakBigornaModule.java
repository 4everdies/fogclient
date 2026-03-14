/*     */ package com.fogclient.module.render;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DestakBigornaModule
/*     */   extends Module
/*     */ {
/*  32 */   private final Map<ChunkCoordIntPair, Set<BlockPos>> chunkAnvils = new HashMap<>();
/*  33 */   private final Set<ChunkCoordIntPair> scannedChunks = new HashSet<>();
/*     */   
/*     */   public DestakBigornaModule() {
/*  36 */     super("EncontraBigorna", "Highlights anvils (Unlimited Range).", Category.RENDER);
/*  37 */     this.keybind = 36;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  42 */     this.chunkAnvils.clear();
/*  43 */     this.scannedChunks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  48 */     this.chunkAnvils.clear();
/*  49 */     this.scannedChunks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  54 */     if (!isEnabled())
/*     */       return; 
/*  56 */     Minecraft minecraft = Minecraft.func_71410_x();
/*  57 */     if (minecraft.field_71439_g == null || minecraft.field_71441_e == null)
/*     */       return; 
/*  59 */     IChunkProvider iChunkProvider = minecraft.field_71441_e.func_72863_F();
/*     */     
/*  61 */     HashSet<ChunkCoordIntPair> hashSet = new HashSet();
/*  62 */     byte b1 = 0;
/*  63 */     byte b2 = 20;
/*     */ 
/*     */     
/*  66 */     byte b3 = 10;
/*  67 */     int i = minecraft.field_71439_g.field_70176_ah;
/*  68 */     int j = minecraft.field_71439_g.field_70164_aj;
/*     */     byte b;
/*  70 */     for (b = -b3; b <= b3; b++) {
/*  71 */       for (byte b4 = -b3; b4 <= b3; b4++) {
/*  72 */         if (iChunkProvider.func_73149_a(i + b, j + b4)) {
/*  73 */           Chunk chunk = iChunkProvider.func_73154_d(i + b, j + b4);
/*  74 */           ChunkCoordIntPair chunkCoordIntPair = chunk.func_76632_l();
/*  75 */           hashSet.add(chunkCoordIntPair);
/*     */           
/*  77 */           if (!this.scannedChunks.contains(chunkCoordIntPair) && 
/*  78 */             b1 < b2) {
/*  79 */             scanChunk(chunk);
/*  80 */             this.scannedChunks.add(chunkCoordIntPair);
/*  81 */             b1++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.scannedChunks.removeIf(paramChunkCoordIntPair -> !paramSet.contains(paramChunkCoordIntPair));
/*  90 */     this.chunkAnvils.keySet().removeIf(paramChunkCoordIntPair -> !paramSet.contains(paramChunkCoordIntPair));
/*     */ 
/*     */     
/*  93 */     if (minecraft.field_71439_g.field_70173_aa % 20 == 0) {
/*  94 */       for (b = -2; b <= 2; b++) {
/*  95 */         for (byte b4 = -2; b4 <= 2; b4++) {
/*  96 */           if (iChunkProvider.func_73149_a(i + b, j + b4)) {
/*  97 */             Chunk chunk = iChunkProvider.func_73154_d(i + b, j + b4);
/*  98 */             scanChunk(chunk);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void scanChunk(Chunk paramChunk) {
/* 106 */     HashSet<BlockPos> hashSet = new HashSet();
/* 107 */     ExtendedBlockStorage[] arrayOfExtendedBlockStorage = paramChunk.func_76587_i();
/*     */     
/* 109 */     int i = paramChunk.field_76635_g * 16;
/* 110 */     int j = paramChunk.field_76647_h * 16;
/*     */     
/* 112 */     for (byte b = 0; b < arrayOfExtendedBlockStorage.length; b++) {
/* 113 */       ExtendedBlockStorage extendedBlockStorage = arrayOfExtendedBlockStorage[b];
/* 114 */       if (extendedBlockStorage != null && !extendedBlockStorage.func_76663_a()) {
/* 115 */         for (byte b1 = 0; b1 < 16; b1++) {
/* 116 */           for (byte b2 = 0; b2 < 16; b2++) {
/* 117 */             for (byte b3 = 0; b3 < 16; b3++) {
/* 118 */               if (extendedBlockStorage.func_150819_a(b1, b2, b3) == Blocks.field_150467_bQ) {
/* 119 */                 hashSet.add(new BlockPos(i + b1, b * 16 + b2, j + b3));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 127 */     if (!hashSet.isEmpty()) {
/* 128 */       this.chunkAnvils.put(paramChunk.func_76632_l(), hashSet);
/*     */     } else {
/* 130 */       this.chunkAnvils.remove(paramChunk.func_76632_l());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/* 136 */     if (paramEvent instanceof RenderWorldLastEvent) {
/* 137 */       onRenderWorldLast((RenderWorldLastEvent)paramEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onRenderWorldLast(RenderWorldLastEvent paramRenderWorldLastEvent) {
/* 142 */     if (!isEnabled())
/*     */       return; 
/* 144 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 145 */     if (minecraft.field_71441_e == null || minecraft.field_71439_g == null)
/*     */       return; 
/* 147 */     GlStateManager.func_179094_E();
/*     */     
/* 149 */     GlStateManager.func_179090_x();
/* 150 */     GlStateManager.func_179147_l();
/* 151 */     GlStateManager.func_179140_f();
/* 152 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 154 */     GlStateManager.func_179097_i();
/* 155 */     GlStateManager.func_179129_p();
/*     */     
/* 157 */     for (Set<BlockPos> set : this.chunkAnvils.values()) {
/* 158 */       for (BlockPos blockPos : set) {
/* 159 */         renderAnvilESP(blockPos, paramRenderWorldLastEvent.partialTicks);
/*     */       }
/*     */     } 
/*     */     
/* 163 */     GlStateManager.func_179089_o();
/* 164 */     GlStateManager.func_179126_j();
/* 165 */     GlStateManager.func_179084_k();
/* 166 */     GlStateManager.func_179145_e();
/* 167 */     GlStateManager.func_179098_w();
/* 168 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderAnvilESP(BlockPos paramBlockPos, float paramFloat) {
/* 172 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 173 */     double d1 = (minecraft.func_175598_ae()).field_78730_l;
/* 174 */     double d2 = (minecraft.func_175598_ae()).field_78731_m;
/* 175 */     double d3 = (minecraft.func_175598_ae()).field_78728_n;
/*     */     
/* 177 */     double d4 = paramBlockPos.func_177958_n() - d1;
/* 178 */     double d5 = paramBlockPos.func_177956_o() - d2;
/* 179 */     double d6 = paramBlockPos.func_177952_p() - d3;
/*     */ 
/*     */     
/* 182 */     float f1 = 0.3F;
/* 183 */     float f2 = 0.3F;
/* 184 */     float f3 = 0.3F;
/*     */     
/* 186 */     GL11.glLineWidth(2.0F);
/* 187 */     GlStateManager.func_179131_c(f1, f2, f3, 1.0F);
/*     */     
/* 189 */     AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d5, d6, d4 + 1.0D, d5 + 1.0D, d6 + 1.0D);
/* 190 */     RenderGlobal.func_181561_a(axisAlignedBB);
/*     */     
/* 192 */     GlStateManager.func_179131_c(f1, f2, f3, 0.2F);
/* 193 */     drawFilledBox(axisAlignedBB);
/*     */ 
/*     */     
/* 196 */     renderBeam(d4 + 0.5D, d5 + 0.5D, d6 + 0.5D, f1, f2, f3);
/*     */     
/* 198 */     GL11.glLineWidth(1.0F);
/*     */   }
/*     */   
/*     */   private void renderBeam(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 202 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 203 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 205 */     GlStateManager.func_179131_c(paramFloat1, paramFloat2, paramFloat3, 0.4F);
/*     */     
/* 207 */     double d1 = 0.2D;
/* 208 */     double d2 = 256.0D;
/*     */     
/* 210 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 213 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 214 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 215 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 216 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */ 
/*     */     
/* 219 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 220 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 221 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 222 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 225 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 226 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 227 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 228 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 231 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 232 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 233 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 234 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */     
/* 236 */     tessellator.func_78381_a();
/*     */   }
/*     */   
/*     */   private void drawFilledBox(AxisAlignedBB paramAxisAlignedBB) {
/* 240 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 241 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 243 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */     
/* 245 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 246 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 247 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 248 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 250 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 251 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 252 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 253 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 255 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 256 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 257 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 258 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 260 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 261 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 262 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 263 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 265 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 266 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 267 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 268 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 270 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 271 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 272 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 273 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 275 */     tessellator.func_78381_a();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\DestakBigornaModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */