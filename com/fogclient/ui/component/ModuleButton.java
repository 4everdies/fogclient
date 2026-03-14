/*     */ package com.fogclient.ui.component;
/*     */ 
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.KeySetting;
/*     */ import com.fogclient.setting.Setting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModuleButton
/*     */   extends Component
/*     */ {
/*     */   private Module module;
/*     */   private Panel parent;
/*     */   private int offset;
/*     */   private boolean extended;
/*  22 */   private List<SettingComponent> settings = new ArrayList<>(); private int x;
/*     */   private int y;
/*     */   
/*     */   public ModuleButton(Module paramModule, Panel paramPanel, int paramInt) {
/*  26 */     this.module = paramModule;
/*  27 */     this.parent = paramPanel;
/*  28 */     this.offset = paramInt;
/*  29 */     this.extended = false;
/*  30 */     this.height = 16;
/*     */ 
/*     */     
/*  33 */     this.settings.add(new KeybindComponent(paramModule, this, 0, false));
/*     */ 
/*     */     
/*  36 */     if (paramModule.isAction()) {
/*  37 */       this.settings.add(new KeybindComponent(paramModule, this, 0, true));
/*     */     }
/*     */     
/*  40 */     for (Setting setting : paramModule.getSettings()) {
/*  41 */       if (setting instanceof com.fogclient.setting.BooleanSetting) {
/*  42 */         this.settings.add(new CheckBox(setting, this, 0)); continue;
/*  43 */       }  if (setting instanceof com.fogclient.setting.NumberSetting) {
/*  44 */         this.settings.add(new Slider(setting, this, 0)); continue;
/*  45 */       }  if (setting instanceof com.fogclient.setting.ModeSetting) {
/*  46 */         this.settings.add(new ModeBox(setting, this, 0)); continue;
/*  47 */       }  if (setting instanceof KeySetting) {
/*  48 */         this.settings.add(new KeySettingComponent((KeySetting)setting, this, 0)); continue;
/*  49 */       }  if (setting instanceof com.fogclient.setting.StringSetting) {
/*  50 */         this.settings.add(new StringSettingComponent(setting, this, 0)); continue;
/*  51 */       }  if (setting instanceof com.fogclient.setting.ActionSetting)
/*  52 */         this.settings.add(new ActionSettingComponent(setting, this, 0)); 
/*     */     } 
/*     */   }
/*     */   private int width; private int height;
/*     */   
/*     */   public void setOffset(int paramInt) {
/*  58 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int paramInt1, int paramInt2, float paramFloat) {
/*  63 */     this.x = this.parent.getX();
/*  64 */     this.y = this.parent.getY() + this.offset;
/*  65 */     this.width = this.parent.getWidth();
/*     */     
/*  67 */     int i = this.module.isEnabled() ? -16711936 : -5592406;
/*  68 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, 16)) {
/*  69 */       Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + 16, -12303292);
/*     */     } else {
/*  71 */       Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + 16, -14540254);
/*     */     } 
/*     */     
/*  74 */     mc.field_71466_p.func_175063_a(this.module.getName(), (this.x + 4), (this.y + 4), i);
/*     */     
/*  76 */     if (this.extended) {
/*  77 */       int j = 16;
/*  78 */       for (SettingComponent settingComponent : this.settings) {
/*  79 */         settingComponent.setOffset(j);
/*  80 */         settingComponent.updatePosition(this.x, this.y, this.width);
/*  81 */         settingComponent.render(paramInt1, paramInt2, paramFloat);
/*  82 */         j += settingComponent.getHeight();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int paramInt1, int paramInt2, int paramInt3) {
/*  89 */     if (isHovered(paramInt1, paramInt2, this.x, this.y, this.width, 16)) {
/*  90 */       if (paramInt3 == 0) {
/*  91 */         this.module.toggle();
/*  92 */       } else if (paramInt3 == 1) {
/*  93 */         this.extended = !this.extended;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (this.extended) {
/*  99 */       for (SettingComponent settingComponent : this.settings) {
/* 100 */         settingComponent.mouseClicked(paramInt1, paramInt2, paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int paramInt1, int paramInt2, int paramInt3) {
/* 107 */     if (this.extended) {
/* 108 */       for (SettingComponent settingComponent : this.settings) {
/* 109 */         settingComponent.mouseReleased(paramInt1, paramInt2, paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(char paramChar, int paramInt) {
/* 116 */     if (this.extended) {
/* 117 */       for (SettingComponent settingComponent : this.settings) {
/* 118 */         settingComponent.keyTyped(paramChar, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 124 */     if (this.extended) {
/* 125 */       int i = 16;
/* 126 */       for (SettingComponent settingComponent : this.settings) {
/* 127 */         i += settingComponent.getHeight();
/*     */       }
/* 129 */       return i;
/*     */     } 
/* 131 */     return 16;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */