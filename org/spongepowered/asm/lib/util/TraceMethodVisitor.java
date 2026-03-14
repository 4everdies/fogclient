/*     */ package org.spongepowered.asm.lib.util;
/*     */ 
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
/*     */ import org.spongepowered.asm.lib.Handle;
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
/*     */ public final class TraceMethodVisitor
/*     */   extends MethodVisitor
/*     */ {
/*     */   public final Printer p;
/*     */   
/*     */   public TraceMethodVisitor(Printer paramPrinter) {
/*  51 */     this((MethodVisitor)null, paramPrinter);
/*     */   }
/*     */   
/*     */   public TraceMethodVisitor(MethodVisitor paramMethodVisitor, Printer paramPrinter) {
/*  55 */     super(327680, paramMethodVisitor);
/*  56 */     this.p = paramPrinter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitParameter(String paramString, int paramInt) {
/*  61 */     this.p.visitParameter(paramString, paramInt);
/*  62 */     super.visitParameter(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  68 */     Printer printer = this.p.visitMethodAnnotation(paramString, paramBoolean);
/*  69 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitAnnotation(paramString, paramBoolean);
/*     */     
/*  71 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  77 */     Printer printer = this.p.visitMethodTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/*  79 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/*  81 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/*  86 */     this.p.visitMethodAttribute(paramAttribute);
/*  87 */     super.visitAttribute(paramAttribute);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/*  92 */     Printer printer = this.p.visitAnnotationDefault();
/*  93 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitAnnotationDefault();
/*  94 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/* 100 */     Printer printer = this.p.visitParameterAnnotation(paramInt, paramString, paramBoolean);
/* 101 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitParameterAnnotation(paramInt, paramString, paramBoolean);
/*     */     
/* 103 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCode() {
/* 108 */     this.p.visitCode();
/* 109 */     super.visitCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/* 115 */     this.p.visitFrame(paramInt1, paramInt2, paramArrayOfObject1, paramInt3, paramArrayOfObject2);
/* 116 */     super.visitFrame(paramInt1, paramInt2, paramArrayOfObject1, paramInt3, paramArrayOfObject2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInsn(int paramInt) {
/* 121 */     this.p.visitInsn(paramInt);
/* 122 */     super.visitInsn(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntInsn(int paramInt1, int paramInt2) {
/* 127 */     this.p.visitIntInsn(paramInt1, paramInt2);
/* 128 */     super.visitIntInsn(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitVarInsn(int paramInt1, int paramInt2) {
/* 133 */     this.p.visitVarInsn(paramInt1, paramInt2);
/* 134 */     super.visitVarInsn(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeInsn(int paramInt, String paramString) {
/* 139 */     this.p.visitTypeInsn(paramInt, paramString);
/* 140 */     super.visitTypeInsn(paramInt, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 146 */     this.p.visitFieldInsn(paramInt, paramString1, paramString2, paramString3);
/* 147 */     super.visitFieldInsn(paramInt, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 154 */     if (this.api >= 327680) {
/* 155 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*     */       return;
/*     */     } 
/* 158 */     this.p.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/* 159 */     if (this.mv != null) {
/* 160 */       this.mv.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 167 */     if (this.api < 327680) {
/* 168 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*     */       return;
/*     */     } 
/* 171 */     this.p.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/* 172 */     if (this.mv != null) {
/* 173 */       this.mv.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/* 180 */     this.p.visitInvokeDynamicInsn(paramString1, paramString2, paramHandle, paramVarArgs);
/* 181 */     super.visitInvokeDynamicInsn(paramString1, paramString2, paramHandle, paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitJumpInsn(int paramInt, Label paramLabel) {
/* 186 */     this.p.visitJumpInsn(paramInt, paramLabel);
/* 187 */     super.visitJumpInsn(paramInt, paramLabel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLabel(Label paramLabel) {
/* 192 */     this.p.visitLabel(paramLabel);
/* 193 */     super.visitLabel(paramLabel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLdcInsn(Object paramObject) {
/* 198 */     this.p.visitLdcInsn(paramObject);
/* 199 */     super.visitLdcInsn(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIincInsn(int paramInt1, int paramInt2) {
/* 204 */     this.p.visitIincInsn(paramInt1, paramInt2);
/* 205 */     super.visitIincInsn(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label... paramVarArgs) {
/* 211 */     this.p.visitTableSwitchInsn(paramInt1, paramInt2, paramLabel, paramVarArgs);
/* 212 */     super.visitTableSwitchInsn(paramInt1, paramInt2, paramLabel, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel) {
/* 218 */     this.p.visitLookupSwitchInsn(paramLabel, paramArrayOfint, paramArrayOfLabel);
/* 219 */     super.visitLookupSwitchInsn(paramLabel, paramArrayOfint, paramArrayOfLabel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/* 224 */     this.p.visitMultiANewArrayInsn(paramString, paramInt);
/* 225 */     super.visitMultiANewArrayInsn(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 232 */     Printer printer = this.p.visitInsnAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/* 233 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitInsnAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/* 235 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/* 241 */     this.p.visitTryCatchBlock(paramLabel1, paramLabel2, paramLabel3, paramString);
/* 242 */     super.visitTryCatchBlock(paramLabel1, paramLabel2, paramLabel3, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 248 */     Printer printer = this.p.visitTryCatchAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/* 250 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitTryCatchAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/* 252 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/* 259 */     this.p.visitLocalVariable(paramString1, paramString2, paramString3, paramLabel1, paramLabel2, paramInt);
/* 260 */     super.visitLocalVariable(paramString1, paramString2, paramString3, paramLabel1, paramLabel2, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/* 267 */     Printer printer = this.p.visitLocalVariableAnnotation(paramInt, paramTypePath, paramArrayOfLabel1, paramArrayOfLabel2, paramArrayOfint, paramString, paramBoolean);
/*     */ 
/*     */     
/* 270 */     AnnotationVisitor annotationVisitor = (this.mv == null) ? null : this.mv.visitLocalVariableAnnotation(paramInt, paramTypePath, paramArrayOfLabel1, paramArrayOfLabel2, paramArrayOfint, paramString, paramBoolean);
/*     */     
/* 272 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLineNumber(int paramInt, Label paramLabel) {
/* 277 */     this.p.visitLineNumber(paramInt, paramLabel);
/* 278 */     super.visitLineNumber(paramInt, paramLabel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMaxs(int paramInt1, int paramInt2) {
/* 283 */     this.p.visitMaxs(paramInt1, paramInt2);
/* 284 */     super.visitMaxs(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 289 */     this.p.visitMethodEnd();
/* 290 */     super.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\TraceMethodVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */