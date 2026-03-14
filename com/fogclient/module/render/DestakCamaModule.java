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
/*     */ public class DestakCamaModule
/*     */   extends Module
/*     */ {
/*  32 */   private final Map<ChunkCoordIntPair, Set<BlockPos>> chunkBeds = new HashMap<>();
/*  33 */   private final Set<ChunkCoordIntPair> scannedChunks = new HashSet<>();
/*     */   
/*     */   public DestakCamaModule() {
/*  36 */     super("EncontraCama", "Highlights beds (Unlimited Range).", Category.RENDER);
/*  37 */     this.keybind = 35;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  42 */     this.chunkBeds.clear();
/*  43 */     this.scannedChunks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  48 */     this.chunkBeds.clear();
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
/*     */           
/*  78 */           if (!this.scannedChunks.contains(chunkCoordIntPair) && 
/*  79 */             b1 < b2) {
/*  80 */             scanChunk(chunk);
/*  81 */             this.scannedChunks.add(chunkCoordIntPair);
/*  82 */             b1++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  90 */     this.scannedChunks.removeIf(paramChunkCoordIntPair -> !paramSet.contains(paramChunkCoordIntPair));
/*  91 */     this.chunkBeds.keySet().removeIf(paramChunkCoordIntPair -> !paramSet.contains(paramChunkCoordIntPair));
/*     */ 
/*     */ 
/*     */     
/*  95 */     if (minecraft.field_71439_g.field_70173_aa % 20 == 0) {
/*  96 */       for (b = -2; b <= 2; b++) {
/*  97 */         for (byte b4 = -2; b4 <= 2; b4++) {
/*  98 */           if (iChunkProvider.func_73149_a(i + b, j + b4)) {
/*  99 */             Chunk chunk = iChunkProvider.func_73154_d(i + b, j + b4);
/* 100 */             scanChunk(chunk);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void scanChunk(Chunk paramChunk) {
/* 108 */     HashSet<BlockPos> hashSet = new HashSet();
/* 109 */     ExtendedBlockStorage[] arrayOfExtendedBlockStorage = paramChunk.func_76587_i();
/*     */     
/* 111 */     int i = paramChunk.field_76635_g * 16;
/* 112 */     int j = paramChunk.field_76647_h * 16;
/*     */     
/* 114 */     for (byte b = 0; b < arrayOfExtendedBlockStorage.length; b++) {
/* 115 */       ExtendedBlockStorage extendedBlockStorage = arrayOfExtendedBlockStorage[b];
/* 116 */       if (extendedBlockStorage != null && !extendedBlockStorage.func_76663_a()) {
/* 117 */         for (byte b1 = 0; b1 < 16; b1++) {
/* 118 */           for (byte b2 = 0; b2 < 16; b2++) {
/* 119 */             for (byte b3 = 0; b3 < 16; b3++) {
/* 120 */               if (extendedBlockStorage.func_150819_a(b1, b2, b3) == Blocks.field_150324_C) {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 125 */                 int k = extendedBlockStorage.func_76665_b(b1, b2, b3);
/* 126 */                 if ((k & 0x8) != 0) {
/* 127 */                   hashSet.add(new BlockPos(i + b1, b * 16 + b2, j + b3));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 136 */     if (!hashSet.isEmpty()) {
/* 137 */       this.chunkBeds.put(paramChunk.func_76632_l(), hashSet);
/*     */     } else {
/* 139 */       this.chunkBeds.remove(paramChunk.func_76632_l());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/* 145 */     if (paramEvent instanceof RenderWorldLastEvent) {
/* 146 */       onRenderWorldLast((RenderWorldLastEvent)paramEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onRenderWorldLast(RenderWorldLastEvent paramRenderWorldLastEvent) {
/* 151 */     if (!isEnabled())
/*     */       return; 
/* 153 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 154 */     if (minecraft.field_71441_e == null || minecraft.field_71439_g == null)
/*     */       return; 
/* 156 */     GlStateManager.func_179094_E();
/*     */     
/* 158 */     GlStateManager.func_179090_x();
/* 159 */     GlStateManager.func_179147_l();
/* 160 */     GlStateManager.func_179140_f();
/* 161 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 163 */     GlStateManager.func_179097_i();
/* 164 */     GlStateManager.func_179129_p();
/*     */     
/* 166 */     for (Set<BlockPos> set : this.chunkBeds.values()) {
/* 167 */       for (BlockPos blockPos : set) {
/* 168 */         renderBedESP(blockPos, paramRenderWorldLastEvent.partialTicks);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     GlStateManager.func_179089_o();
/* 173 */     GlStateManager.func_179126_j();
/* 174 */     GlStateManager.func_179084_k();
/* 175 */     GlStateManager.func_179145_e();
/* 176 */     GlStateManager.func_179098_w();
/* 177 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderBedESP(BlockPos paramBlockPos, float paramFloat) {
/* 181 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 182 */     double d1 = (minecraft.func_175598_ae()).field_78730_l;
/* 183 */     double d2 = (minecraft.func_175598_ae()).field_78731_m;
/* 184 */     double d3 = (minecraft.func_175598_ae()).field_78728_n;
/*     */     
/* 186 */     double d4 = paramBlockPos.func_177958_n() - d1;
/* 187 */     double d5 = paramBlockPos.func_177956_o() - d2;
/* 188 */     double d6 = paramBlockPos.func_177952_p() - d3;
/*     */ 
/*     */     
/* 191 */     float f1 = 1.0F;
/* 192 */     float f2 = 0.0F;
/* 193 */     float f3 = 0.0F;
/*     */     
/* 195 */     GL11.glLineWidth(2.0F);
/* 196 */     GlStateManager.func_179131_c(f1, f2, f3, 1.0F);
/*     */ 
/*     */     
/* 199 */     AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d5, d6, d4 + 1.0D, d5 + 0.5625D, d6 + 1.0D);
/* 200 */     RenderGlobal.func_181561_a(axisAlignedBB);
/*     */     
/* 202 */     GlStateManager.func_179131_c(f1, f2, f3, 0.3F);
/* 203 */     drawFilledBox(axisAlignedBB);
/*     */ 
/*     */     
/* 206 */     renderBeam(d4 + 0.5D, d5 + 0.5D, d6 + 0.5D, f1, f2, f3);
/*     */     
/* 208 */     GL11.glLineWidth(1.0F);
/*     */   }
/*     */   
/*     */   private void renderBeam(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 212 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 213 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 215 */     GlStateManager.func_179131_c(paramFloat1, paramFloat2, paramFloat3, 0.4F);
/*     */     
/* 217 */     double d1 = 0.2D;
/* 218 */     double d2 = 256.0D;
/*     */     
/* 220 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 223 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 224 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 225 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 226 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */ 
/*     */     
/* 229 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 230 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 231 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 232 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 235 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 236 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 237 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 238 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 241 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 242 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 243 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 244 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */     
/* 246 */     tessellator.func_78381_a();
/*     */   }
/*     */   
/*     */   private void drawFilledBox(AxisAlignedBB paramAxisAlignedBB) {
/* 250 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 251 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 253 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */     
/* 255 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 256 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 257 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 258 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 260 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 261 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 262 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 263 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 265 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 266 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 267 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 268 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 270 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 271 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 272 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 273 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 275 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 276 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 277 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 278 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 280 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 281 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 282 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 283 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 285 */     tessellator.func_78381_a();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\DestakCamaModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */