/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.Label;
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
/*     */ public class TableSwitchInsnNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public int min;
/*     */   public int max;
/*     */   public LabelNode dflt;
/*     */   public List<LabelNode> labels;
/*     */   
/*     */   public TableSwitchInsnNode(int paramInt1, int paramInt2, LabelNode paramLabelNode, LabelNode... paramVarArgs) {
/*  84 */     super(170);
/*  85 */     this.min = paramInt1;
/*  86 */     this.max = paramInt2;
/*  87 */     this.dflt = paramLabelNode;
/*  88 */     this.labels = new ArrayList<LabelNode>();
/*  89 */     if (paramVarArgs != null) {
/*  90 */       this.labels.addAll(Arrays.asList(paramVarArgs));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/*  96 */     return 11;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 101 */     Label[] arrayOfLabel = new Label[this.labels.size()];
/* 102 */     for (byte b = 0; b < arrayOfLabel.length; b++) {
/* 103 */       arrayOfLabel[b] = ((LabelNode)this.labels.get(b)).getLabel();
/*     */     }
/* 105 */     paramMethodVisitor.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), arrayOfLabel);
/* 106 */     acceptAnnotations(paramMethodVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 111 */     return (new TableSwitchInsnNode(this.min, this.max, clone(this.dflt, paramMap), clone(this.labels, paramMap)))
/* 112 */       .cloneAnnotations(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\TableSwitchInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */