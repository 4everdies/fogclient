/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.Handle;
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
/*     */ public class InvokeDynamicInsnNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public String name;
/*     */   public String desc;
/*     */   public Handle bsm;
/*     */   public Object[] bsmArgs;
/*     */   
/*     */   public InvokeDynamicInsnNode(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/*  79 */     super(186);
/*  80 */     this.name = paramString1;
/*  81 */     this.desc = paramString2;
/*  82 */     this.bsm = paramHandle;
/*  83 */     this.bsmArgs = paramVarArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/*  88 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/*  93 */     paramMethodVisitor.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
/*  94 */     acceptAnnotations(paramMethodVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/*  99 */     return (new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs))
/* 100 */       .cloneAnnotations(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\InvokeDynamicInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */