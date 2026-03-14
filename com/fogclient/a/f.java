/*    */ package com.fogclient.a;
/*    */ 
/*    */ public class f {
/*    */   private static volatile boolean securityChecked = true;
/*    */   private static volatile boolean emailValidated = true;
/*    */   private static volatile boolean needsEmailInput = false;
/*  7 */   private static volatile String planType = "Premium";
/*  8 */   private static volatile String planExpiration = "never";
/*  9 */   private static volatile String validatedEmail = "local@localhost";
/* 10 */   private static volatile String sessionToken = "local-session";
/* 11 */   private static volatile a.a pendingBlockReason = null;
/*    */   
/*    */   public static boolean initialize() {
/* 14 */     securityChecked = true;
/* 15 */     emailValidated = true;
/* 16 */     needsEmailInput = false;
/* 17 */     return true;
/*    */   }
/*    */   
/*    */   public static void storePlanInfo(String p1, String p2) {
/* 21 */     planType = p1;
/* 22 */     planExpiration = p2;
/*    */   }
/*    */   
/* 25 */   public static boolean needsEmailInput() { return false; }
/* 26 */   public static boolean isSecurityValidated() { return true; }
/* 27 */   public static String getPlanType() { return planType; }
/* 28 */   public static String getPlanExpiration() { return planExpiration; }
/* 29 */   public static String getValidatedEmail() { return validatedEmail; } public static String getSessionToken() {
/* 30 */     return sessionToken;
/*    */   } public static a.a pollBlockReason() {
/* 32 */     return null;
/*    */   }
/*    */   public static void setPendingBlockReason(a.a reason) {}
/* 35 */   public static void setEmailValidated(boolean v) { emailValidated = v; } public static void setSecurityChecked(boolean v) {
/* 36 */     securityChecked = v;
/*    */   }
/* 38 */   public static void setValidatedEmail(String v) { validatedEmail = v; } public static void setNeedsEmailInput(boolean v) {} public static void setSessionToken(String v) {
/* 39 */     sessionToken = v;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\a\f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */