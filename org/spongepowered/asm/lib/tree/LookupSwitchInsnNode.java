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
/*     */ public class LookupSwitchInsnNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public LabelNode dflt;
/*     */   public List<Integer> keys;
/*     */   public List<LabelNode> labels;
/*     */   
/*     */   public LookupSwitchInsnNode(LabelNode paramLabelNode, int[] paramArrayOfint, LabelNode[] paramArrayOfLabelNode) {
/*  77 */     super(171);
/*  78 */     this.dflt = paramLabelNode;
/*  79 */     this.keys = new ArrayList<Integer>((paramArrayOfint == null) ? 0 : paramArrayOfint.length);
/*  80 */     this.labels = new ArrayList<LabelNode>((paramArrayOfLabelNode == null) ? 0 : paramArrayOfLabelNode.length);
/*     */     
/*  82 */     if (paramArrayOfint != null) {
/*  83 */       for (byte b = 0; b < paramArrayOfint.length; b++) {
/*  84 */         this.keys.add(Integer.valueOf(paramArrayOfint[b]));
/*     */       }
/*     */     }
/*  87 */     if (paramArrayOfLabelNode != null) {
/*  88 */       this.labels.addAll(Arrays.asList(paramArrayOfLabelNode));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/*  94 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/*  99 */     int[] arrayOfInt = new int[this.keys.size()];
/* 100 */     for (byte b1 = 0; b1 < arrayOfInt.length; b1++) {
/* 101 */       arrayOfInt[b1] = ((Integer)this.keys.get(b1)).intValue();
/*     */     }
/* 103 */     Label[] arrayOfLabel = new Label[this.labels.size()];
/* 104 */     for (byte b2 = 0; b2 < arrayOfLabel.length; b2++) {
/* 105 */       arrayOfLabel[b2] = ((LabelNode)this.labels.get(b2)).getLabel();
/*     */     }
/* 107 */     paramMethodVisitor.visitLookupSwitchInsn(this.dflt.getLabel(), arrayOfInt, arrayOfLabel);
/* 108 */     acceptAnnotations(paramMethodVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 114 */     LookupSwitchInsnNode lookupSwitchInsnNode = new LookupSwitchInsnNode(clone(this.dflt, paramMap), null, clone(this.labels, paramMap));
/* 115 */     lookupSwitchInsnNode.keys.addAll(this.keys);
/* 116 */     return lookupSwitchInsnNode.cloneAnnotations(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\LookupSwitchInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */