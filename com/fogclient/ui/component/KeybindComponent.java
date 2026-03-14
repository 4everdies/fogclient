/*     */ package com.fogclient.ui.component;
/*     */ 
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.Setting;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class KeybindComponent extends SettingComponent {
/*     */   private boolean binding;
/*     */   private Module module;
/*     */   private boolean isActionBind;
/*     */   
/*     */   public KeybindComponent(Module paramModule, ModuleButton paramModuleButton, int paramInt, boolean paramBoolean) {
/*  14 */     super((Setting)null, paramModuleButton, paramInt);
/*  15 */     this.module = paramModule;
/*  16 */     this.binding = false;
/*  17 */     this.isActionBind = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/*  23 */     Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
/*     */ 
/*     */     
/*  26 */     boolean bool = isHovered(paramInt1, paramInt2, this.x + 2, this.y + 2, this.width - 4, this.height - 4);
/*  27 */     int i = bool ? -11184811 : -13421773;
/*  28 */     Gui.func_73734_a(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, i);
/*     */     
/*  30 */     int j = this.isActionBind ? this.module.getActionKeybind() : this.module.getKeybind();
/*  31 */     String str1 = getKeyName(j);
/*     */     
/*  33 */     String str2 = this.isActionBind ? "Action: " : "Bind: ";
/*  34 */     String str3 = this.binding ? "Press Key/Mouse..." : (str2 + str1);
/*     */ 
/*     */     
/*  37 */     int k = mc.field_71466_p.func_78256_a(str3);
/*  38 */     mc.field_71466_p.func_175063_a(str3, (this.x + (this.width - k) / 2), (this.y + (this.height - 8) / 2), this.binding ? -43691 : -16711936);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/*  43 */     if (isHovered(paramInt1, paramInt2, this.x + 2, this.y + 2, this.width - 4, this.height - 4)) {
/*  44 */       if (paramInt3 == 0) {
/*     */         
/*  46 */         this.binding = !this.binding;
/*  47 */       } else if (this.binding) {
/*     */         
/*  49 */         int i = -100 + paramInt3;
/*  50 */         if (this.isActionBind) {
/*  51 */           this.module.setActionKeybind(i);
/*     */         } else {
/*  53 */           this.module.setKeybind(i);
/*     */         } 
/*  55 */         this.binding = false;
/*     */       } 
/*  57 */     } else if (this.binding) {
/*     */       
/*  59 */       int i = -100 + paramInt3;
/*  60 */       if (this.isActionBind) {
/*  61 */         this.module.setActionKeybind(i);
/*     */       } else {
/*  63 */         this.module.setKeybind(i);
/*     */       } 
/*  65 */       this.binding = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(char paramChar, int paramInt) {
/*  71 */     if (this.binding) {
/*  72 */       boolean bool = (paramInt == 1 || paramInt == 211) ? false : paramInt;
/*     */       
/*  74 */       if (this.isActionBind) {
/*  75 */         this.module.setActionKeybind(bool);
/*     */       } else {
/*  77 */         this.module.setKeybind(bool);
/*     */       } 
/*  79 */       this.binding = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getKeyName(int paramInt) {
/*  92 */     if (paramInt == 0) {
/*  93 */       return "NONE";
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (paramInt < 0) {
/*  98 */       int i = paramInt + 100;
/*  99 */       switch (i) { case 0:
/* 100 */           return "Mouse Left";
/* 101 */         case 1: return "Mouse Right";
/* 102 */         case 2: return "Mouse Middle";
/* 103 */         case 3: return "Mouse 4";
/* 104 */         case 4: return "Mouse 5"; }
/* 105 */        return "Mouse " + i;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 110 */     String str = Keyboard.getKeyName(paramInt);
/* 111 */     return (str != null) ? str : ("KEY_" + paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\KeybindComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */