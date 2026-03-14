/*    */ package com.fogclient.module.misc;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ 
/*    */ public class TrollModule
/*    */   extends Module {
/*    */   public TrollModule() {
/*  9 */     super("Troll", "Crasha o jogo instantaneamente.", Category.CLIENT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 14 */     if (mc.field_71439_g != null)
/* 15 */       throw new RuntimeException("Troll Module Activated! Game Crashed."); 
/*    */   }
/*    */   
/*    */   public void onDisable() {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\TrollModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */