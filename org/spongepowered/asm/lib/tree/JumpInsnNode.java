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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JumpInsnNode
/*    */   extends AbstractInsnNode
/*    */ {
/*    */   public LabelNode label;
/*    */   
/*    */   public JumpInsnNode(int paramInt, LabelNode paramLabelNode) {
/* 64 */     super(paramInt);
/* 65 */     this.label = paramLabelNode;
/*    */   }
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
/*    */   public void setOpcode(int paramInt) {
/* 78 */     this.opcode = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getType() {
/* 83 */     return 7;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(MethodVisitor paramMethodVisitor) {
/* 88 */     paramMethodVisitor.visitJumpInsn(this.opcode, this.label.getLabel());
/* 89 */     acceptAnnotations(paramMethodVisitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 94 */     return (new JumpInsnNode(this.opcode, clone(this.label, paramMap)))
/* 95 */       .cloneAnnotations(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\JumpInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */