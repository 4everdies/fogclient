/*    */ package com.fogclient.module;
/*    */ 
/*    */ public enum Category {
/*  4 */   COMBAT("Guerra"),
/*  5 */   PLAYER("Player"),
/*  6 */   MOVEMENT("Mobilidade"),
/*  7 */   RENDER("Visual"),
/*  8 */   MISC("Utilidades"),
/*  9 */   CLIENT("Client"),
/* 10 */   MANUAL("Manual");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   Category(String paramString1) {
/* 15 */     this.name = paramString1;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */