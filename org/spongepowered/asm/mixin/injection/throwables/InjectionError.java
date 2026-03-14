/*    */ package org.spongepowered.asm.mixin.injection.throwables;
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
/*    */ public class InjectionError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public InjectionError() {}
/*    */   
/*    */   public InjectionError(String paramString) {
/* 38 */     super(paramString);
/*    */   }
/*    */   
/*    */   public InjectionError(Throwable paramThrowable) {
/* 42 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public InjectionError(String paramString, Throwable paramThrowable) {
/* 46 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\throwables\InjectionError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */