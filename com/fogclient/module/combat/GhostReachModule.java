/*    */ package com.fogclient.module.combat;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.module.combat.ghostreach.GhostReachCore;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class GhostReachModule
/*    */   extends Module
/*    */ {
/* 12 */   private final NumberSetting minReach = new NumberSetting("Min Reach", this, 3.0D, 3.0D, 6.0D, 0.1D);
/* 13 */   private final NumberSetting maxReach = new NumberSetting("Max Reach", this, 4.0D, 3.0D, 6.0D, 0.1D);
/* 14 */   private final NumberSetting chance = new NumberSetting("Chance %", this, 100.0D, 0.0D, 100.0D, 1.0D);
/*    */   
/*    */   public GhostReachModule() {
/* 17 */     super("ReachDiferenciado", "Extends attack range legitimately.", Category.COMBAT);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 23 */     updateSettings();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 28 */     GhostReachCore.INSTANCE.targetEntityId = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTickStart() {
/* 33 */     if (!isEnabled())
/*    */       return; 
/* 35 */     updateSettings();
/*    */     
/* 37 */     GhostReachCore ghostReachCore = GhostReachCore.INSTANCE;
/*    */     
/* 39 */     Entity entity = ghostReachCore.findTarget(GhostReachCore.maxReach);
/* 40 */     if (entity != null && ghostReachCore.checkConditions(entity)) {
/* 41 */       ghostReachCore.targetEntityId = entity.func_145782_y();
/*    */     } else {
/* 43 */       ghostReachCore.targetEntityId = -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void updateSettings() {
/* 48 */     GhostReachCore.minReach = this.minReach.getValue();
/* 49 */     GhostReachCore.maxReach = this.maxReach.getValue();
/* 50 */     GhostReachCore.chance = (float)this.chance.getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\GhostReachModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */