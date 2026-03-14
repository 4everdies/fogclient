/*    */ package org.spongepowered.asm.lib.tree;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.spongepowered.asm.lib.MethodVisitor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntInsnNode
/*    */   extends AbstractInsnNode
/*    */ {
/*    */   public int operand;
/*    */   
/*    */   public IntInsnNode(int paramInt1, int paramInt2) {
/* 58 */     super(paramInt1);
/* 59 */     this.operand = paramInt2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOpcode(int paramInt) {
/* 70 */     this.opcode = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getType() {
/* 75 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(MethodVisitor paramMethodVisitor) {
/* 80 */     paramMethodVisitor.visitIntInsn(this.opcode, this.operand);
/* 81 */     acceptAnnotations(paramMethodVisitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 86 */     return (new IntInsnNode(this.opcode, this.operand)).cloneAnnotations(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\IntInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */