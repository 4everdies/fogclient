/*     */ package com.fogclient.module;
/*     */ 
/*     */ import com.fogclient.a.f;
/*     */ import com.fogclient.setting.Setting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ 
/*     */ public abstract class Module
/*     */ {
/*     */   protected String name;
/*     */   protected String description;
/*     */   protected boolean enabled;
/*     */   protected int keybind;
/*     */   protected int actionKeybind;
/*     */   protected Category category;
/*  19 */   protected List<Setting> settings = new ArrayList<>();
/*  20 */   protected static final Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public Module(String paramString1, String paramString2, Category paramCategory) {
/*  23 */     this.name = paramString1;
/*  24 */     this.description = paramString2;
/*  25 */     this.category = paramCategory;
/*  26 */     this.enabled = false;
/*  27 */     this.keybind = 0;
/*  28 */     this.actionKeybind = 0;
/*     */   }
/*     */   
/*     */   public abstract void onEnable();
/*     */   
/*     */   public abstract void onDisable();
/*     */   
/*     */   public void onEvent(Event paramEvent) {}
/*     */   
/*     */   public void onTick() {}
/*     */   
/*     */   public void onTickStart() {}
/*     */   
/*     */   public void onRenderTick() {}
/*     */   
/*     */   public void onLivingUpdate() {}
/*     */   
/*     */   public void onKeyPressed() {}
/*     */   
/*     */   public void onAction() {}
/*     */   
/*     */   public boolean isKeyJustPressed() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  55 */     return false;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  59 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  63 */     return this.description;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  67 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean paramBoolean) {
/*  71 */     this.enabled = paramBoolean;
/*  72 */     if (paramBoolean) {
/*  73 */       onEnable();
/*     */     } else {
/*  75 */       onDisable();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggle() {
/*  81 */     if (!f.isSecurityValidated())
/*  82 */       return;  if (f.getSessionToken() == null)
/*  83 */       return;  setEnabled(!this.enabled);
/*     */   }
/*     */   
/*     */   public int getKeybind() {
/*  87 */     return this.keybind;
/*     */   }
/*     */   
/*     */   public void setKeybind(int paramInt) {
/*  91 */     this.keybind = paramInt;
/*     */   }
/*     */   
/*     */   public int getActionKeybind() {
/*  95 */     return this.actionKeybind;
/*     */   }
/*     */   
/*     */   public void setActionKeybind(int paramInt) {
/*  99 */     this.actionKeybind = paramInt;
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/* 103 */     return this.category;
/*     */   }
/*     */   
/*     */   public void addSetting(Setting paramSetting) {
/* 107 */     this.settings.add(paramSetting);
/*     */   }
/*     */   
/*     */   public List<Setting> getSettings() {
/* 111 */     return this.settings;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */