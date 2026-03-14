/*     */ package com.fogclient.ui.component;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.module.ModuleManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ 
/*     */ public class Panel
/*     */   extends Component
/*     */ {
/*     */   private String title;
/*     */   private int x;
/*     */   private int y;
/*     */   private int width;
/*  17 */   private List<ModuleButton> components = new ArrayList<>(); private int height; private boolean dragging; private int dragX; private int dragY; private boolean open;
/*     */   
/*     */   public Panel(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Category paramCategory) {
/*  20 */     this.title = paramString;
/*  21 */     this.x = paramInt1;
/*  22 */     this.y = paramInt2;
/*  23 */     this.width = paramInt3;
/*  24 */     this.height = paramInt4;
/*  25 */     this.open = true;
/*  26 */     this.dragging = false;
/*     */     
/*  28 */     int i = paramInt4;
/*  29 */     for (Module module : ModuleManager.getInstance().getModulesByCategory(paramCategory)) {
/*  30 */       ModuleButton moduleButton = new ModuleButton(module, this, i);
/*  31 */       this.components.add(moduleButton);
/*  32 */       i += 16;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/*  38 */     if (this.dragging) {
/*  39 */       this.x = paramInt1 - this.dragX;
/*  40 */       this.y = paramInt2 - this.dragY;
/*     */     } 
/*     */     
/*  43 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -14540254);
/*  44 */     mc.field_71466_p.func_175063_a(this.title, (this.x + (this.width - mc.field_71466_p.func_78256_a(this.title)) / 2), (this.y + this.height / 2 - 4), -16711936);
/*     */     
/*  46 */     if (this.open) {
/*  47 */       int i = this.height;
/*  48 */       for (ModuleButton moduleButton : this.components) {
/*  49 */         moduleButton.setOffset(i);
/*  50 */         moduleButton.render(paramInt1, paramInt2, paramFloat);
/*  51 */         i += moduleButton.getHeight();
/*     */       } 
/*  53 */       Gui.func_73734_a(this.x, this.y + i, this.x + this.width, this.y + i + 2, -15658735);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/*  59 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, this.height)) {
/*  60 */       if (paramInt3 == 0) {
/*  61 */         this.dragging = true;
/*  62 */         this.dragX = paramInt1 - this.x;
/*  63 */         this.dragY = paramInt2 - this.y;
/*  64 */       } else if (paramInt3 == 1) {
/*  65 */         this.open = !this.open;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     if (this.open) {
/*  71 */       int i = this.height;
/*  72 */       for (ModuleButton moduleButton : this.components) {
/*  73 */         moduleButton.setOffset(i);
/*  74 */         moduleButton.mouseClicked(paramInt1, paramInt2, paramInt3);
/*  75 */         i += moduleButton.getHeight();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {
/*  82 */     this.dragging = false;
/*  83 */     if (this.open) {
/*  84 */       for (ModuleButton moduleButton : this.components) {
/*  85 */         moduleButton.mouseReleased(paramInt1, paramInt2, paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(char paramChar, int paramInt) {
/*  92 */     if (this.open)
/*  93 */       for (ModuleButton moduleButton : this.components) {
/*  94 */         moduleButton.keyTyped(paramChar, paramInt);
/*     */       } 
/*     */   }
/*     */   
/*     */   public int getX() {
/*  99 */     return this.x; }
/* 100 */   public int getY() { return this.y; }
/* 101 */   public void setX(int paramInt) { this.x = paramInt; }
/* 102 */   public void setY(int paramInt) { this.y = paramInt; }
/* 103 */   public int getWidth() { return this.width; }
/* 104 */   public boolean isOpen() { return this.open; }
/* 105 */   public void setOpen(boolean paramBoolean) { this.open = paramBoolean; } public String getTitle() {
/* 106 */     return this.title;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */