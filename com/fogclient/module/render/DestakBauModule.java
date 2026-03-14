/*     */ package com.fogclient.module.render;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DestakBauModule
/*     */   extends Module
/*     */ {
/*     */   public DestakBauModule() {
/*  25 */     super("EncontraBau", "Mostra baÃºs e ender chests atravÃ©s das paredes.", Category.RENDER);
/*  26 */     this.keybind = 48;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/*  41 */     if (paramEvent instanceof RenderWorldLastEvent) {
/*  42 */       onRenderWorldLast((RenderWorldLastEvent)paramEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onRenderWorldLast(RenderWorldLastEvent paramRenderWorldLastEvent) {
/*  47 */     if (!isEnabled())
/*     */       return; 
/*  49 */     Minecraft minecraft = Minecraft.func_71410_x();
/*  50 */     if (minecraft.field_71441_e == null || minecraft.field_71439_g == null) {
/*     */       return;
/*     */     }
/*  53 */     GlStateManager.func_179094_E();
/*     */ 
/*     */     
/*  56 */     GlStateManager.func_179090_x();
/*  57 */     GlStateManager.func_179147_l();
/*  58 */     GlStateManager.func_179140_f();
/*  59 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */ 
/*     */     
/*  62 */     GlStateManager.func_179097_i();
/*     */ 
/*     */     
/*  65 */     GlStateManager.func_179129_p();
/*     */     
/*  67 */     for (TileEntity tileEntity : minecraft.field_71441_e.field_147482_g) {
/*  68 */       if (tileEntity instanceof net.minecraft.tileentity.TileEntityChest || tileEntity instanceof net.minecraft.tileentity.TileEntityEnderChest) {
/*  69 */         renderChestESP(tileEntity, paramRenderWorldLastEvent.partialTicks);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  74 */     GlStateManager.func_179089_o();
/*  75 */     GlStateManager.func_179126_j();
/*  76 */     GlStateManager.func_179084_k();
/*  77 */     GlStateManager.func_179145_e();
/*  78 */     GlStateManager.func_179098_w();
/*  79 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderChestESP(TileEntity paramTileEntity, float paramFloat) {
/*  83 */     Minecraft minecraft = Minecraft.func_71410_x();
/*     */ 
/*     */     
/*  86 */     double d1 = (minecraft.func_175598_ae()).field_78730_l;
/*  87 */     double d2 = (minecraft.func_175598_ae()).field_78731_m;
/*  88 */     double d3 = (minecraft.func_175598_ae()).field_78728_n;
/*     */     
/*  90 */     BlockPos blockPos = paramTileEntity.func_174877_v();
/*  91 */     double d4 = blockPos.func_177958_n() - d1;
/*  92 */     double d5 = blockPos.func_177956_o() - d2;
/*  93 */     double d6 = blockPos.func_177952_p() - d3;
/*     */ 
/*     */     
/*  96 */     float f1 = 1.0F;
/*  97 */     float f2 = 0.8F;
/*  98 */     float f3 = 0.0F;
/*  99 */     float f4 = 0.5F;
/*     */ 
/*     */ 
/*     */     
/* 103 */     GL11.glLineWidth(2.0F);
/* 104 */     GlStateManager.func_179131_c(f1, f2, f3, 1.0F);
/*     */     
/* 106 */     AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d5, d6, d4 + 1.0D, d5 + 1.0D, d6 + 1.0D);
/* 107 */     RenderGlobal.func_181561_a(axisAlignedBB);
/*     */ 
/*     */     
/* 110 */     GlStateManager.func_179131_c(f1, f2, f3, 0.2F);
/* 111 */     drawFilledBox(axisAlignedBB);
/*     */ 
/*     */     
/* 114 */     renderBeam(d4 + 0.5D, d5 + 0.5D, d6 + 0.5D, f1, f2, f3);
/*     */ 
/*     */     
/* 117 */     GL11.glLineWidth(1.0F);
/*     */   }
/*     */   
/*     */   private void drawFilledBox(AxisAlignedBB paramAxisAlignedBB) {
/* 121 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 122 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 124 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 127 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 128 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 129 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 130 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */ 
/*     */     
/* 133 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 134 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 135 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 136 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */ 
/*     */ 
/*     */     
/* 140 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 141 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 142 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 143 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */ 
/*     */     
/* 146 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 147 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 148 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 149 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */ 
/*     */     
/* 152 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 153 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 154 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 155 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72340_a, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/*     */ 
/*     */     
/* 158 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 159 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72339_c).func_181675_d();
/* 160 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72337_e, paramAxisAlignedBB.field_72334_f).func_181675_d();
/* 161 */     worldRenderer.func_181662_b(paramAxisAlignedBB.field_72336_d, paramAxisAlignedBB.field_72338_b, paramAxisAlignedBB.field_72334_f).func_181675_d();
/*     */     
/* 163 */     tessellator.func_78381_a();
/*     */   }
/*     */   
/*     */   private void renderBeam(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 167 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 168 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 170 */     GlStateManager.func_179131_c(paramFloat1, paramFloat2, paramFloat3, 0.4F);
/*     */     
/* 172 */     double d1 = 0.2D;
/* 173 */     double d2 = 256.0D;
/*     */     
/* 175 */     worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 178 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 179 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 180 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 181 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */ 
/*     */     
/* 184 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 185 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 186 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 187 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 190 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 191 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 + d1).func_181675_d();
/* 192 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/* 193 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 + d1).func_181675_d();
/*     */ 
/*     */     
/* 196 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 197 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2, paramDouble3 - d1).func_181675_d();
/* 198 */     worldRenderer.func_181662_b(paramDouble1 + d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/* 199 */     worldRenderer.func_181662_b(paramDouble1 - d1, paramDouble2 + d2, paramDouble3 - d1).func_181675_d();
/*     */     
/* 201 */     tessellator.func_78381_a();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\DestakBauModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */