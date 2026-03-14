/*     */ package com.fogclient.ui;
/*     */ import com.fogclient.config.ConfigManager;
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.ui.component.Panel;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class ClickGUI extends GuiScreen {
/*  17 */   private List<Panel> panels = new ArrayList<>();
/*     */   
/*     */   public ClickGUI() {
/*  20 */     byte b = 10;
/*  21 */     for (Category category : Category.values()) {
/*  22 */       this.panels.add(new Panel(category.getName(), b, 10, 100, 16, category));
/*  23 */       b += 110;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  29 */     Keyboard.enableRepeatEvents(true);
/*  30 */     super.func_73866_w_();
/*     */ 
/*     */     
/*  33 */     Map map = ConfigManager.loadGUIConfig();
/*     */ 
/*     */     
/*  36 */     byte b1 = 10;
/*  37 */     byte b2 = 10;
/*  38 */     int i = b1;
/*  39 */     byte b3 = b2;
/*  40 */     byte b4 = 100;
/*  41 */     byte b5 = 10;
/*     */     
/*  43 */     for (Panel panel : this.panels) {
/*  44 */       String str = panel.getTitle();
/*     */ 
/*     */       
/*  47 */       if (map.containsKey(str)) {
/*  48 */         ConfigManager.PanelConfig panelConfig = (ConfigManager.PanelConfig)map.get(str);
/*  49 */         panel.setX(panelConfig.x);
/*  50 */         panel.setY(panelConfig.y);
/*  51 */         panel.setOpen(panelConfig.open);
/*     */         
/*     */         continue;
/*     */       } 
/*  55 */       boolean bool = (this.panels.size() * (b4 + b5) + b1 > this.field_146294_l) ? true : false;
/*     */       
/*  57 */       if (bool) {
/*     */         
/*  59 */         if (i + b4 > this.field_146294_l) {
/*     */           
/*  61 */           i = b1;
/*  62 */           b3 += 120;
/*     */         } 
/*     */         
/*  65 */         panel.setX(i);
/*  66 */         panel.setY(b3);
/*     */         
/*  68 */         i += b4 + b5;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_146281_b() {
/*  76 */     Keyboard.enableRepeatEvents(false);
/*     */ 
/*     */     
/*  79 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  80 */     for (Panel panel : this.panels) {
/*  81 */       hashMap.put(panel.getTitle(), panel);
/*     */     }
/*  83 */     ConfigManager.saveGUIConfig(hashMap);
/*     */ 
/*     */     
/*  86 */     ConfigManager.saveConfig();
/*     */     
/*  88 */     super.func_146281_b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int paramInt1, int paramInt2, float paramFloat) {
/*  93 */     func_146276_q_();
/*  94 */     for (Panel panel : this.panels) {
/*  95 */       panel.render(paramInt1, paramInt2, paramFloat);
/*     */     }
/*     */ 
/*     */     
/*  99 */     GlStateManager.func_179094_E();
/* 100 */     GlStateManager.func_179147_l();
/* 101 */     GlStateManager.func_179141_d();
/*     */ 
/*     */     
/* 104 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     ResourceLocation resourceLocation = new ResourceLocation("fogclient", "textures/gui/logo.png");
/* 110 */     this.field_146297_k.func_110434_K().func_110577_a(resourceLocation);
/*     */     
/* 112 */     char c1 = '';
/* 113 */     char c2 = '';
/* 114 */     byte b = 4;
/* 115 */     int i = this.field_146295_m - c2 - 4;
/*     */     
/* 117 */     Gui.func_146110_a(b, i, 0.0F, 0.0F, c1, c2, c1, c2);
/*     */     
/* 119 */     GlStateManager.func_179118_c();
/* 120 */     GlStateManager.func_179084_k();
/* 121 */     GlStateManager.func_179121_F();
/*     */     
/* 123 */     super.func_73863_a(paramInt1, paramInt2, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int paramInt1, int paramInt2, int paramInt3) throws IOException {
/* 128 */     for (Panel panel : this.panels) {
/* 129 */       panel.mouseClicked(paramInt1, paramInt2, paramInt3);
/*     */     }
/* 131 */     super.func_73864_a(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int paramInt1, int paramInt2, int paramInt3) {
/* 136 */     for (Panel panel : this.panels) {
/* 137 */       panel.mouseReleased(paramInt1, paramInt2, paramInt3);
/*     */     }
/* 139 */     super.func_146286_b(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char paramChar, int paramInt) throws IOException {
/* 144 */     for (Panel panel : this.panels) {
/* 145 */       panel.keyTyped(paramChar, paramInt);
/*     */     }
/* 147 */     super.func_73869_a(paramChar, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_73868_f() {
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */