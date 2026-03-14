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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInsnNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public String owner;
/*     */   public String name;
/*     */   public String desc;
/*     */   public boolean itf;
/*     */   
/*     */   @Deprecated
/*     */   public MethodInsnNode(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  85 */     this(paramInt, paramString1, paramString2, paramString3, (paramInt == 185));
/*     */   }
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
/*     */   public MethodInsnNode(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 108 */     super(paramInt);
/* 109 */     this.owner = paramString1;
/* 110 */     this.name = paramString2;
/* 111 */     this.desc = paramString3;
/* 112 */     this.itf = paramBoolean;
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
/* 123 */     this.opcode = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/* 128 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 133 */     paramMethodVisitor.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
/* 134 */     acceptAnnotations(paramMethodVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 139 */     return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\MethodInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */