/*    */ package com.fogclient.module.misc;
/*    */ 
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.ActionSetting;
/*    */ import com.fogclient.setting.ModeSetting;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TreinoBotModule
/*    */   extends Module
/*    */ {
/*    */   private NumberSetting damageSetting;
/*    */   private ModeSetting difficultySetting;
/*    */   private ActionSetting spawnButton;
/*    */   private ActionSetting killButton;
/*    */   
/*    */   public TreinoBotModule() {
/* 23 */     super("BotPvP", "Spawns a bot to train PvP.", Category.MISC);
/*    */     
/* 25 */     this.damageSetting = new NumberSetting("Dano (CoraÃ§Ãµes)", this, 4.0D, 0.5D, 20.0D, 0.5D);
/* 26 */     this.difficultySetting = new ModeSetting("Dificuldade", this, "FÃ¡cil", new String[] { "FÃ¡cil", "MÃ©dio", "DifÃ­cil", "Cheater" });
/*    */     
/* 28 */     this.spawnButton = new ActionSetting("Spawnar Bot", this, new Runnable()
/*    */         {
/*    */           public void run() {
/* 31 */             TreinoBotModule.this.spawnBot();
/*    */           }
/*    */         });
/*    */     
/* 35 */     this.killButton = new ActionSetting("Remover Bots", this, new Runnable()
/*    */         {
/*    */           public void run() {
/* 38 */             TreinoBotModule.this.killBots();
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void spawnBot() {
/* 44 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*    */       return; 
/* 46 */     double d = this.damageSetting.getValue();
/* 47 */     String str1 = this.difficultySetting.getValue();
/*    */     
/* 49 */     String str2 = "facil";
/* 50 */     if (str1.equals("MÃ©dio")) { str2 = "intermediario"; }
/* 51 */     else if (str1.equals("DifÃ­cil")) { str2 = "dificil"; }
/* 52 */     else if (str1.equals("Cheater")) { str2 = "cheater"; }
/*    */     
/* 54 */     mc.field_71439_g.func_71165_d("/spawnbot " + d + " " + str2);
/*    */   }
/*    */   
/*    */   private void killBots() {
/* 58 */     if (mc.field_71439_g == null)
/* 59 */       return;  mc.field_71439_g.func_71165_d("/killbot");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEvent(Event paramEvent) {
/* 74 */     if (paramEvent instanceof PlayerEvent.PlayerLoggedInEvent) {
/* 75 */       PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent = (PlayerEvent.PlayerLoggedInEvent)paramEvent;
/* 76 */       if (!playerLoggedInEvent.player.field_70170_p.field_72995_K) {
/* 77 */         byte b = 0;
/* 78 */         for (EntityPvPBot entityPvPBot : playerLoggedInEvent.player.field_70170_p.field_72996_f) {
/* 79 */           if (entityPvPBot instanceof EntityPvPBot) {
/* 80 */             ((EntityPvPBot)entityPvPBot).func_70106_y();
/* 81 */             b++;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\TreinoBotModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */