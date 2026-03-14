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
/*     */ public class AchaFerroModule
/*     */   extends Module
/*     */ {
/*  32 */   private final Map<ChunkCoordIntPair, Set<BlockPos>> chunkIrons = new HashMap<>();
/*  33 */   private int currentChunkIndex = 0;
/*     */   
/*     */   public AchaFerroModule() {
/*  36 */     super("AchaFerro", "Mostra minÃ©rios de ferro atravÃ©s das paredes (Alcance Ilimitado).", Category.RENDER);
/*  37 */     this.keybind = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  42 */     this.chunkIrons.clear();
/*  43 */     this.currentChunkIndex = 0;
/*  44 */     if (mc.field_71439_g != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  51 */     this.chunkIrons.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/*  56 */     if (paramEvent instanceof RenderWorldLastEvent) {
/*  57 */       onRenderWorldLast((RenderWorldLastEvent)paramEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  63 */     if (!isEnabled())
/*     */       return; 
/*  65 */     Minecraft minecraft = Minecraft.func_71410_x();
/*  66 */     if (minecraft.field_71439_g == null || minecraft.field_71441_e == null) {
/*     */       return;
/*     */     }
/*  69 */     IChunkProvider iChunkProvider = minecraft.field_71441_e.func_72863_F();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     byte b1 = 8;
/*  76 */     int i = minecraft.field_71439_g.field_70176_ah;
/*  77 */     int j = minecraft.field_71439_g.field_70164_aj;
/*     */     
/*  79 */     for (byte b2 = -b1; b2 <= b1; b2++) {
/*  80 */       for (byte b = -b1; b <= b1; b++) {
/*  81 */         if (iChunkProvider.func_73149_a(i + b2, j + b)) {
/*  82 */           Chunk chunk = iChunkProvider.func_73154_d(i + b2, j + b);
/*  83 */           scanChunk(chunk);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanChunk(Chunk paramChunk) {
/*  90 */     HashSet<BlockPos> hashSet = new HashSet();
/*  91 */     ExtendedBlockStorage[] arrayOfExtendedBlockStorage = paramChunk.func_76587_i();
/*     */     
/*  93 */     int i = paramChunk.field_76635_g * 16;
/*  94 */     int j = paramChunk.field_76647_h * 16;
/*     */     
/*  96 */     for (byte b = 0; b < arrayOfExtendedBlockStorage.length; b++) {
/*  97 */       ExtendedBlockStorage extendedBlockStorage = arrayOfExtendedBlockStorage[b];
/*  98 */       if (extendedBlockStorage != null && !extendedBlockStorage.func_76663_a()) {
/*  99 */         for (byte b1 = 0; b1 < 16; b1++) {
/* 100 */           for (byte b2 = 0; b2 < 16; b2++) {
/* 101 */             for (byte b3 = 0; b3 < 16; b3++) {
/* 102 */               if (extendedBlockStorage.func_150819_a(b1, b2, b3) == Blocks.field_150366_p) {
/* 103 */                 hashSet.add(new BlockPos(i + b1, b * 16 + b2, j + b3));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 111 */     if (!hashSet.isEmpty()) {
/* 112 */       this.chunkIrons.put(paramChunk.func_76632_l(), hashSet);
/*     */     } else {
/* 114 */       this.chunkIrons.remove(paramChunk.func_76632_l());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onRenderWorldLast(RenderWorldLastEvent paramRenderWorldLastEvent) {
/* 119 */     if (!isEnabled())
/*     */       return; 
/* 121 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 122 */     if (minecraft.field_71441_e == null || minecraft.field_71439_g == null)
/*     */       return; 
/* 124 */     GlStateManager.func_179094_E();
/*     */     
/* 126 */     GlStateManager.func_179090_x();
/* 127 */     GlStateManager.func_179147_l();
/* 128 */     GlStateManager.func_179140_f();
/* 129 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 131 */     GlStateManager.func_179097_i();
/* 132 */     GlStateManager.func_179129_p();
/*     */ 
/*     */     
/* 135 */     for (Set<BlockPos> set : this.chunkIrons.values()) {
/* 136 */       for (BlockPos blockPos : set) {
/* 137 */         renderIronESP(blockPos, paramRenderWorldLastEvent.partialTicks);
/*     */       }
/*     */     } 
/*     */     
/* 141 */     GlStateManager.func_179089_o();
/* 142 */     GlStateManager.func_179126_j();
/* 143 */     GlStateManager.func_179084_k();
/* 144 */     GlStateManager.func_179145_e();
/* 145 */     GlStateManager.func_179098_w();
/* 146 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderIronESP(BlockPos paramBlockPos, float paramFloat) {
/* 150 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 151 */     double d1 = (minecraft.func_175598_ae()).field_78730_l;
/* 152 */     double d2 = (minecraft.func_175598_ae()).field_78731_m;
/* 153 */     double d3 = (minecraft.func_175598_ae()).field_78728_n;
/*     */     
/* 155 */     double d4 = paramBlockPos.func_177958_n() - d1;
/* 156 */     double d5 = paramBlockPos.func_177956_o() - d2;
/* 157 */     double d6 = paramBlockPos.func_177952_p() - d3;
/*     */ 
/*     */     
/* 160 */     float f1 = 0.9F;
/* 161 */     float f2 = 0.7F;
/* 162 */     float f3 = 0.5F;
/*     */     
/* 164 */     GL11.glLineWidth(1.0F);
/* 165 */     GlStateManager.func_179131_c(f1, f2, f3, 1.0F);
/*     */     
/* 167 */     AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d5, d6, d4 + 1.0D, d5 + 1.0D, d6 + 1.0D);
/* 168 */     RenderGlobal.func_181561_a(axisAlignedBB);
/*     */     
/* 170 */     GlStateManager.func_179131_c(f1, f2, f3, 0.2F);
/* 171 */     drawFilledBox(axisAlignedBB);
/*     */   }
/*     */   
/*     */   private void drawFilledBox(AxisAlignedBB paramAxisAlignedBB) {
/* 175 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 176 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 178 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */     
/* 180 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 181 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 182 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 183 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 185 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 186 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 187 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 188 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 190 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 191 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 192 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 193 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 195 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 196 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 197 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 198 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 200 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 201 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 202 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 203 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */     
/* 205 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 206 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 207 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 208 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 210 */     tessellator.func_78381_a();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\AchaFerroModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */