/*     */ package com.fogclient.ui.component;
/*     */ 
/*     */ import com.fogclient.setting.KeySetting;
/*     */ import com.fogclient.setting.Setting;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class KeySettingComponent extends SettingComponent {
/*     */   private KeySetting setting;
/*     */   private ModuleButton parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   
/*     */   public KeySettingComponent(KeySetting paramKeySetting, ModuleButton paramModuleButton, int paramInt) {
/*  15 */     super((Setting)paramKeySetting, paramModuleButton, paramInt);
/*  16 */     this.setting = paramKeySetting;
/*  17 */     this.parent = paramModuleButton;
/*  18 */     this.offset = paramInt;
/*  19 */     this.height = 16;
/*  20 */     this.binding = false;
/*     */   }
/*     */   private int y; private int width; private int height; private boolean binding;
/*     */   
/*     */   public void setOffset(int paramInt) {
/*  25 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePosition(int paramInt1, int paramInt2, int paramInt3) {
/*  30 */     this.x = paramInt1;
/*  31 */     this.y = paramInt2 + this.offset;
/*  32 */     this.width = paramInt3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/*  37 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -13421773);
/*     */     
/*  39 */     String str1 = getKeyName(this.setting.getKeyCode());
/*     */     
/*  41 */     String str2 = this.binding ? "Press Key/Mouse..." : (this.setting.getName() + ": " + str1);
/*     */     
/*  43 */     mc.field_71466_p.func_175063_a(str2, (this.x + 4), (this.y + 4), this.binding ? -65536 : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/*  48 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, this.height)) {
/*  49 */       if (paramInt3 == 0) {
/*  50 */         this.binding = !this.binding;
/*  51 */       } else if (paramInt3 == 1) {
/*  52 */         this.setting.setKeyCode(0);
/*  53 */         this.binding = false;
/*  54 */       } else if (this.binding) {
/*     */         
/*  56 */         int i = -100 + paramInt3;
/*  57 */         this.setting.setKeyCode(i);
/*  58 */         this.binding = false;
/*     */       } 
/*  60 */     } else if (this.binding) {
/*     */       
/*  62 */       int i = -100 + paramInt3;
/*  63 */       this.setting.setKeyCode(i);
/*  64 */       this.binding = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyTyped(char paramChar, int paramInt) {
/*  75 */     if (this.binding) {
/*  76 */       if (paramInt == 1 || paramInt == 211) {
/*  77 */         this.setting.setKeyCode(0);
/*     */       } else {
/*  79 */         this.setting.setKeyCode(paramInt);
/*     */       } 
/*  81 */       this.binding = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  87 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getKeyName(int paramInt) {
/*  94 */     if (paramInt == 0) {
/*  95 */       return "NONE";
/*     */     }
/*     */ 
/*     */     
/*  99 */     if (paramInt < 0) {
/* 100 */       int i = paramInt + 100;
/* 101 */       switch (i) { case 0:
/* 102 */           return "Mouse Left";
/* 103 */         case 1: return "Mouse Right";
/* 104 */         case 2: return "Mouse Middle";
/* 105 */         case 3: return "Mouse 4";
/* 106 */         case 4: return "Mouse 5"; }
/* 107 */        return "Mouse " + i;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 112 */     String str = Keyboard.getKeyName(paramInt);
/* 113 */     return (str != null) ? str : ("KEY_" + paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\KeySettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */