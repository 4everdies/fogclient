/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Label;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.TypePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVariableAnnotationNode
/*     */   extends TypeAnnotationNode
/*     */ {
/*     */   public List<LabelNode> start;
/*     */   public List<LabelNode> end;
/*     */   public List<Integer> index;
/*     */   
/*     */   public LocalVariableAnnotationNode(int paramInt, TypePath paramTypePath, LabelNode[] paramArrayOfLabelNode1, LabelNode[] paramArrayOfLabelNode2, int[] paramArrayOfint, String paramString) {
/*  96 */     this(327680, paramInt, paramTypePath, paramArrayOfLabelNode1, paramArrayOfLabelNode2, paramArrayOfint, paramString);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalVariableAnnotationNode(int paramInt1, int paramInt2, TypePath paramTypePath, LabelNode[] paramArrayOfLabelNode1, LabelNode[] paramArrayOfLabelNode2, int[] paramArrayOfint, String paramString) {
/* 126 */     super(paramInt1, paramInt2, paramTypePath, paramString);
/* 127 */     this.start = new ArrayList<LabelNode>(paramArrayOfLabelNode1.length);
/* 128 */     this.start.addAll(Arrays.asList(paramArrayOfLabelNode1));
/* 129 */     this.end = new ArrayList<LabelNode>(paramArrayOfLabelNode2.length);
/* 130 */     this.end.addAll(Arrays.asList(paramArrayOfLabelNode2));
/* 131 */     this.index = new ArrayList<Integer>(paramArrayOfint.length);
/* 132 */     for (int i : paramArrayOfint) {
/* 133 */       this.index.add(Integer.valueOf(i));
/*     */     }
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
/*     */   public void accept(MethodVisitor paramMethodVisitor, boolean paramBoolean) {
/* 146 */     Label[] arrayOfLabel1 = new Label[this.start.size()];
/* 147 */     Label[] arrayOfLabel2 = new Label[this.end.size()];
/* 148 */     int[] arrayOfInt = new int[this.index.size()];
/* 149 */     for (byte b = 0; b < arrayOfLabel1.length; b++) {
/* 150 */       arrayOfLabel1[b] = ((LabelNode)this.start.get(b)).getLabel();
/* 151 */       arrayOfLabel2[b] = ((LabelNode)this.end.get(b)).getLabel();
/* 152 */       arrayOfInt[b] = ((Integer)this.index.get(b)).intValue();
/*     */     } 
/* 154 */     accept(paramMethodVisitor.visitLocalVariableAnnotation(this.typeRef, this.typePath, arrayOfLabel1, arrayOfLabel2, arrayOfInt, this.desc, true));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\LocalVariableAnnotationNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */