/*    */ package org.spongepowered.asm.mixin.injection.callback;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum LocalCapture
/*    */ {
/* 44 */   NO_CAPTURE(false, false),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   PRINT(false, true),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   CAPTURE_FAILSOFT,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   CAPTURE_FAILHARD,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   CAPTURE_FAILEXCEPTION;
/*    */ 
/*    */   
/*    */   private final boolean captureLocals;
/*    */ 
/*    */   
/*    */   private final boolean printLocals;
/*    */ 
/*    */ 
/*    */   
/*    */   LocalCapture(boolean paramBoolean1, boolean paramBoolean2) {
/* 80 */     this.captureLocals = paramBoolean1;
/* 81 */     this.printLocals = paramBoolean2;
/*    */   }
/*    */   
/*    */   boolean isCaptureLocals() {
/* 85 */     return this.captureLocals;
/*    */   }
/*    */   
/*    */   boolean isPrintLocals() {
/* 89 */     return this.printLocals;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\callback\LocalCapture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */