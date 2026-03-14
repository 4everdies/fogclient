/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldInsnNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public String owner;
/*     */   public String name;
/*     */   public String desc;
/*     */   
/*     */   public FieldInsnNode(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  77 */     super(paramInt);
/*  78 */     this.owner = paramString1;
/*  79 */     this.name = paramString2;
/*  80 */     this.desc = paramString3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpcode(int paramInt) {
/*  91 */     this.opcode = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/*  96 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 101 */     paramMethodVisitor.visitFieldInsn(this.opcode, this.owner, this.name, this.desc);
/* 102 */     acceptAnnotations(paramMethodVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 107 */     return (new FieldInsnNode(this.opcode, this.owner, this.name, this.desc))
/* 108 */       .cloneAnnotations(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\FieldInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */