/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractInsnNode
/*     */ {
/*     */   public static final int INSN = 0;
/*     */   public static final int INT_INSN = 1;
/*     */   public static final int VAR_INSN = 2;
/*     */   public static final int TYPE_INSN = 3;
/*     */   public static final int FIELD_INSN = 4;
/*     */   public static final int METHOD_INSN = 5;
/*     */   public static final int INVOKE_DYNAMIC_INSN = 6;
/*     */   public static final int JUMP_INSN = 7;
/*     */   public static final int LABEL = 8;
/*     */   public static final int LDC_INSN = 9;
/*     */   public static final int IINC_INSN = 10;
/*     */   public static final int TABLESWITCH_INSN = 11;
/*     */   public static final int LOOKUPSWITCH_INSN = 12;
/*     */   public static final int MULTIANEWARRAY_INSN = 13;
/*     */   public static final int FRAME = 14;
/*     */   public static final int LINE = 15;
/*     */   protected int opcode;
/*     */   public List<TypeAnnotationNode> visibleTypeAnnotations;
/*     */   public List<TypeAnnotationNode> invisibleTypeAnnotations;
/*     */   AbstractInsnNode prev;
/*     */   AbstractInsnNode next;
/*     */   int index;
/*     */   
/*     */   protected AbstractInsnNode(int paramInt) {
/* 178 */     this.opcode = paramInt;
/* 179 */     this.index = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpcode() {
/* 188 */     return this.opcode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode getPrevious() {
/* 207 */     return this.prev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode getNext() {
/* 218 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void accept(MethodVisitor paramMethodVisitor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void acceptAnnotations(MethodVisitor paramMethodVisitor) {
/* 237 */     byte b1 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(); byte b2;
/* 238 */     for (b2 = 0; b2 < b1; b2++) {
/* 239 */       TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(b2);
/* 240 */       typeAnnotationNode.accept(paramMethodVisitor.visitInsnAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
/*     */     } 
/*     */ 
/*     */     
/* 244 */     b1 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size();
/* 245 */     for (b2 = 0; b2 < b1; b2++) {
/* 246 */       TypeAnnotationNode typeAnnotationNode = this.invisibleTypeAnnotations.get(b2);
/* 247 */       typeAnnotationNode.accept(paramMethodVisitor.visitInsnAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, false));
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
/*     */ 
/*     */   
/*     */   public abstract AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static LabelNode clone(LabelNode paramLabelNode, Map<LabelNode, LabelNode> paramMap) {
/* 274 */     return paramMap.get(paramLabelNode);
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
/*     */   static LabelNode[] clone(List<LabelNode> paramList, Map<LabelNode, LabelNode> paramMap) {
/* 288 */     LabelNode[] arrayOfLabelNode = new LabelNode[paramList.size()];
/* 289 */     for (byte b = 0; b < arrayOfLabelNode.length; b++) {
/* 290 */       arrayOfLabelNode[b] = paramMap.get(paramList.get(b));
/*     */     }
/* 292 */     return arrayOfLabelNode;
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
/*     */   protected final AbstractInsnNode cloneAnnotations(AbstractInsnNode paramAbstractInsnNode) {
/* 304 */     if (paramAbstractInsnNode.visibleTypeAnnotations != null) {
/* 305 */       this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
/* 306 */       for (byte b = 0; b < paramAbstractInsnNode.visibleTypeAnnotations.size(); b++) {
/* 307 */         TypeAnnotationNode typeAnnotationNode1 = paramAbstractInsnNode.visibleTypeAnnotations.get(b);
/* 308 */         TypeAnnotationNode typeAnnotationNode2 = new TypeAnnotationNode(typeAnnotationNode1.typeRef, typeAnnotationNode1.typePath, typeAnnotationNode1.desc);
/*     */         
/* 310 */         typeAnnotationNode1.accept(typeAnnotationNode2);
/* 311 */         this.visibleTypeAnnotations.add(typeAnnotationNode2);
/*     */       } 
/*     */     } 
/* 314 */     if (paramAbstractInsnNode.invisibleTypeAnnotations != null) {
/* 315 */       this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
/* 316 */       for (byte b = 0; b < paramAbstractInsnNode.invisibleTypeAnnotations.size(); b++) {
/* 317 */         TypeAnnotationNode typeAnnotationNode1 = paramAbstractInsnNode.invisibleTypeAnnotations.get(b);
/* 318 */         TypeAnnotationNode typeAnnotationNode2 = new TypeAnnotationNode(typeAnnotationNode1.typeRef, typeAnnotationNode1.typePath, typeAnnotationNode1.desc);
/*     */         
/* 320 */         typeAnnotationNode1.accept(typeAnnotationNode2);
/* 321 */         this.invisibleTypeAnnotations.add(typeAnnotationNode2);
/*     */       } 
/*     */     } 
/* 324 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\AbstractInsnNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */