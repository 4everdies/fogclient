/*     */ package org.spongepowered.asm.lib.commons;
/*     */ 
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
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
/*     */ public class MethodRemapper
/*     */   extends MethodVisitor
/*     */ {
/*     */   protected final Remapper remapper;
/*     */   
/*     */   public MethodRemapper(MethodVisitor paramMethodVisitor, Remapper paramRemapper) {
/*  50 */     this(327680, paramMethodVisitor, paramRemapper);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MethodRemapper(int paramInt, MethodVisitor paramMethodVisitor, Remapper paramRemapper) {
/*  55 */     super(paramInt, paramMethodVisitor);
/*  56 */     this.remapper = paramRemapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/*  61 */     AnnotationVisitor annotationVisitor = super.visitAnnotationDefault();
/*  62 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  67 */     AnnotationVisitor annotationVisitor = super.visitAnnotation(this.remapper.mapDesc(paramString), paramBoolean);
/*     */     
/*  69 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  75 */     AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(paramInt, paramTypePath, this.remapper
/*  76 */         .mapDesc(paramString), paramBoolean);
/*  77 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/*  83 */     AnnotationVisitor annotationVisitor = super.visitParameterAnnotation(paramInt, this.remapper
/*  84 */         .mapDesc(paramString), paramBoolean);
/*  85 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/*  91 */     super.visitFrame(paramInt1, paramInt2, remapEntries(paramInt2, paramArrayOfObject1), paramInt3, 
/*  92 */         remapEntries(paramInt3, paramArrayOfObject2));
/*     */   }
/*     */   
/*     */   private Object[] remapEntries(int paramInt, Object[] paramArrayOfObject) {
/*  96 */     for (byte b = 0; b < paramInt; b++) {
/*  97 */       if (paramArrayOfObject[b] instanceof String) {
/*  98 */         Object[] arrayOfObject = new Object[paramInt];
/*  99 */         if (b > 0) {
/* 100 */           System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, b);
/*     */         }
/*     */         while (true) {
/* 103 */           Object object = paramArrayOfObject[b];
/* 104 */           arrayOfObject[b++] = (object instanceof String) ? this.remapper
/* 105 */             .mapType((String)object) : object;
/* 106 */           if (b >= paramInt)
/* 107 */             return arrayOfObject; 
/*     */         } 
/*     */       } 
/* 110 */     }  return paramArrayOfObject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 116 */     super.visitFieldInsn(paramInt, this.remapper.mapType(paramString1), this.remapper
/* 117 */         .mapFieldName(paramString1, paramString2, paramString3), this.remapper
/* 118 */         .mapDesc(paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 125 */     if (this.api >= 327680) {
/* 126 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*     */       return;
/*     */     } 
/* 129 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, (paramInt == 185));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 136 */     if (this.api < 327680) {
/* 137 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*     */       return;
/*     */     } 
/* 140 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doVisitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 151 */     if (this.mv != null) {
/* 152 */       this.mv.visitMethodInsn(paramInt, this.remapper.mapType(paramString1), this.remapper
/* 153 */           .mapMethodName(paramString1, paramString2, paramString3), this.remapper
/* 154 */           .mapMethodDesc(paramString3), paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/* 161 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 162 */       paramVarArgs[b] = this.remapper.mapValue(paramVarArgs[b]);
/*     */     }
/* 164 */     super.visitInvokeDynamicInsn(this.remapper
/* 165 */         .mapInvokeDynamicMethodName(paramString1, paramString2), this.remapper
/* 166 */         .mapMethodDesc(paramString2), (Handle)this.remapper.mapValue(paramHandle), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTypeInsn(int paramInt, String paramString) {
/* 172 */     super.visitTypeInsn(paramInt, this.remapper.mapType(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLdcInsn(Object paramObject) {
/* 177 */     super.visitLdcInsn(this.remapper.mapValue(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/* 182 */     super.visitMultiANewArrayInsn(this.remapper.mapDesc(paramString), paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 188 */     AnnotationVisitor annotationVisitor = super.visitInsnAnnotation(paramInt, paramTypePath, this.remapper
/* 189 */         .mapDesc(paramString), paramBoolean);
/* 190 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/* 196 */     super.visitTryCatchBlock(paramLabel1, paramLabel2, paramLabel3, (paramString == null) ? null : this.remapper
/* 197 */         .mapType(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 203 */     AnnotationVisitor annotationVisitor = super.visitTryCatchAnnotation(paramInt, paramTypePath, this.remapper
/* 204 */         .mapDesc(paramString), paramBoolean);
/* 205 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/* 211 */     super.visitLocalVariable(paramString1, this.remapper.mapDesc(paramString2), this.remapper
/* 212 */         .mapSignature(paramString3, true), paramLabel1, paramLabel2, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/* 219 */     AnnotationVisitor annotationVisitor = super.visitLocalVariableAnnotation(paramInt, paramTypePath, paramArrayOfLabel1, paramArrayOfLabel2, paramArrayOfint, this.remapper
/* 220 */         .mapDesc(paramString), paramBoolean);
/* 221 */     return (annotationVisitor == null) ? annotationVisitor : new AnnotationRemapper(annotationVisitor, this.remapper);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\MethodRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */