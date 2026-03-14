/*     */ package org.spongepowered.asm.lib.commons;
/*     */ 
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.FieldVisitor;
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
/*     */ public class ClassRemapper
/*     */   extends ClassVisitor
/*     */ {
/*     */   protected final Remapper remapper;
/*     */   protected String className;
/*     */   
/*     */   public ClassRemapper(ClassVisitor paramClassVisitor, Remapper paramRemapper) {
/*  52 */     this(327680, paramClassVisitor, paramRemapper);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassRemapper(int paramInt, ClassVisitor paramClassVisitor, Remapper paramRemapper) {
/*  57 */     super(paramInt, paramClassVisitor);
/*  58 */     this.remapper = paramRemapper;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  64 */     this.className = paramString1;
/*  65 */     super.visit(paramInt1, paramInt2, this.remapper.mapType(paramString1), this.remapper
/*  66 */         .mapSignature(paramString2, false), this.remapper.mapType(paramString3), (paramArrayOfString == null) ? null : this.remapper
/*  67 */         .mapTypes(paramArrayOfString));
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  72 */     AnnotationVisitor annotationVisitor = super.visitAnnotation(this.remapper.mapDesc(paramString), paramBoolean);
/*     */     
/*  74 */     return (annotationVisitor == null) ? null : createAnnotationRemapper(annotationVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  80 */     AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(paramInt, paramTypePath, this.remapper
/*  81 */         .mapDesc(paramString), paramBoolean);
/*  82 */     return (annotationVisitor == null) ? null : createAnnotationRemapper(annotationVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/*  88 */     FieldVisitor fieldVisitor = super.visitField(paramInt, this.remapper
/*  89 */         .mapFieldName(this.className, paramString1, paramString2), this.remapper
/*  90 */         .mapDesc(paramString2), this.remapper.mapSignature(paramString3, true), this.remapper
/*  91 */         .mapValue(paramObject));
/*  92 */     return (fieldVisitor == null) ? null : createFieldRemapper(fieldVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  98 */     String str = this.remapper.mapMethodDesc(paramString2);
/*  99 */     MethodVisitor methodVisitor = super.visitMethod(paramInt, this.remapper.mapMethodName(this.className, paramString1, paramString2), str, this.remapper
/* 100 */         .mapSignature(paramString3, false), (paramArrayOfString == null) ? null : this.remapper
/*     */         
/* 102 */         .mapTypes(paramArrayOfString));
/* 103 */     return (methodVisitor == null) ? null : createMethodRemapper(methodVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 110 */     super.visitInnerClass(this.remapper.mapType(paramString1), (paramString2 == null) ? null : this.remapper
/* 111 */         .mapType(paramString2), paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/* 116 */     super.visitOuterClass(this.remapper.mapType(paramString1), (paramString2 == null) ? null : this.remapper
/* 117 */         .mapMethodName(paramString1, paramString2, paramString3), (paramString3 == null) ? null : this.remapper
/* 118 */         .mapMethodDesc(paramString3));
/*     */   }
/*     */   
/*     */   protected FieldVisitor createFieldRemapper(FieldVisitor paramFieldVisitor) {
/* 122 */     return new FieldRemapper(paramFieldVisitor, this.remapper);
/*     */   }
/*     */   
/*     */   protected MethodVisitor createMethodRemapper(MethodVisitor paramMethodVisitor) {
/* 126 */     return new MethodRemapper(paramMethodVisitor, this.remapper);
/*     */   }
/*     */   
/*     */   protected AnnotationVisitor createAnnotationRemapper(AnnotationVisitor paramAnnotationVisitor) {
/* 130 */     return new AnnotationRemapper(paramAnnotationVisitor, this.remapper);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\ClassRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */