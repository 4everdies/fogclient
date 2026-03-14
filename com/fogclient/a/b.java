/*    */ package com.fogclient.a;
/*    */ 
/*    */ public class b {
/*    */   public static class a {
/*    */     public final boolean authorized = true;
/*  6 */     public final String plan = "Premium";
/*  7 */     public final String expiration = "never";
/*  8 */     public final String sessionToken = "local-session";
/*    */     public final boolean networkError = false;
/*    */     
/* 11 */     public static a denied() { return new a(); } public static a networkFailure() {
/* 12 */       return new a();
/*    */     } }
/*    */   
/*    */   public static a verifyAndGetInfo(String email, String hwid) {
/* 16 */     return new a();
/*    */   }
/*    */   
/*    */   public static a verifyAndGetInfo(String email, String hwid, String sessionToken) {
/* 20 */     return new a();
/*    */   }
/*    */   
/*    */   public static a verifyAndGetInfo(String email, String hwid, String sessionToken, String nick, String server) {
/* 24 */     return new a();
/*    */   }
/*    */   
/* 27 */   public static boolean hasSavedEmail() { return true; } public static String getSavedEmail() {
/* 28 */     return "local@localhost";
/*    */   }
/*    */   
/*    */   public static void saveEmail(String email) {}
/*    */   
/*    */   public static void clearSavedEmail() {}
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\a\b.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */