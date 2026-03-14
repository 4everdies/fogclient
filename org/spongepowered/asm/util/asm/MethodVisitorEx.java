/*    */ package org.spongepowered.asm.util.asm;
/*    */ 
/*    */ import org.spongepowered.asm.lib.MethodVisitor;
/*    */ import org.spongepowered.asm.util.Bytecode;
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
/*    */ public class MethodVisitorEx
/*    */   extends MethodVisitor
/*    */ {
/*    */   public MethodVisitorEx(MethodVisitor paramMethodVisitor) {
/* 37 */     super(327680, paramMethodVisitor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visitConstant(byte paramByte) {
/* 47 */     if (paramByte > -2 && paramByte < 6) {
/* 48 */       visitInsn(Bytecode.CONSTANTS_INT[paramByte + 1]);
/*    */       return;
/*    */     } 
/* 51 */     visitIntInsn(16, paramByte);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\asm\MethodVisitorEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */