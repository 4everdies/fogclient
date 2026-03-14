/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TryCatchBlockNode
/*     */ {
/*     */   public LabelNode start;
/*     */   public LabelNode end;
/*     */   public LabelNode handler;
/*     */   public String type;
/*     */   public List<TypeAnnotationNode> visibleTypeAnnotations;
/*     */   public List<TypeAnnotationNode> invisibleTypeAnnotations;
/*     */   
/*     */   public TryCatchBlockNode(LabelNode paramLabelNode1, LabelNode paramLabelNode2, LabelNode paramLabelNode3, String paramString) {
/* 100 */     this.start = paramLabelNode1;
/* 101 */     this.end = paramLabelNode2;
/* 102 */     this.handler = paramLabelNode3;
/* 103 */     this.type = paramString;
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
/*     */   public void updateIndex(int paramInt) {
/* 116 */     int i = 0x42000000 | paramInt << 8;
/* 117 */     if (this.visibleTypeAnnotations != null) {
/* 118 */       for (TypeAnnotationNode typeAnnotationNode : this.visibleTypeAnnotations) {
/* 119 */         typeAnnotationNode.typeRef = i;
/*     */       }
/*     */     }
/* 122 */     if (this.invisibleTypeAnnotations != null) {
/* 123 */       for (TypeAnnotationNode typeAnnotationNode : this.invisibleTypeAnnotations) {
/* 124 */         typeAnnotationNode.typeRef = i;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 136 */     paramMethodVisitor.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), (this.handler == null) ? null : this.handler
/* 137 */         .getLabel(), this.type);
/*     */     
/* 139 */     byte b1 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(); byte b2;
/* 140 */     for (b2 = 0; b2 < b1; b2++) {
/* 141 */       TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(b2);
/* 142 */       typeAnnotationNode.accept(paramMethodVisitor.visitTryCatchAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
/*     */     } 
/*     */ 
/*     */     
/* 146 */     b1 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size();
/* 147 */     for (b2 = 0; b2 < b1; b2++) {
/* 148 */       TypeAnnotationNode typeAnnotationNode = this.invisibleTypeAnnotations.get(b2);
/* 149 */       typeAnnotationNode.accept(paramMethodVisitor.visitTryCatchAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, false));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\TryCatchBlockNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */