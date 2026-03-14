/*     */ package org.spongepowered.asm.lib.util;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TraceClassVisitor
/*     */   extends ClassVisitor
/*     */ {
/*     */   private final PrintWriter pw;
/*     */   public final Printer p;
/*     */   
/*     */   public TraceClassVisitor(PrintWriter paramPrintWriter) {
/* 103 */     this(null, paramPrintWriter);
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
/*     */   public TraceClassVisitor(ClassVisitor paramClassVisitor, PrintWriter paramPrintWriter) {
/* 116 */     this(paramClassVisitor, new Textifier(), paramPrintWriter);
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
/*     */   public TraceClassVisitor(ClassVisitor paramClassVisitor, Printer paramPrinter, PrintWriter paramPrintWriter) {
/* 134 */     super(327680, paramClassVisitor);
/* 135 */     this.pw = paramPrintWriter;
/* 136 */     this.p = paramPrinter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 143 */     this.p.visit(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramArrayOfString);
/* 144 */     super.visit(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitSource(String paramString1, String paramString2) {
/* 149 */     this.p.visitSource(paramString1, paramString2);
/* 150 */     super.visitSource(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/* 156 */     this.p.visitOuterClass(paramString1, paramString2, paramString3);
/* 157 */     super.visitOuterClass(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 163 */     Printer printer = this.p.visitClassAnnotation(paramString, paramBoolean);
/* 164 */     AnnotationVisitor annotationVisitor = (this.cv == null) ? null : this.cv.visitAnnotation(paramString, paramBoolean);
/*     */     
/* 166 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 172 */     Printer printer = this.p.visitClassTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/* 174 */     AnnotationVisitor annotationVisitor = (this.cv == null) ? null : this.cv.visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*     */     
/* 176 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 181 */     this.p.visitClassAttribute(paramAttribute);
/* 182 */     super.visitAttribute(paramAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 188 */     this.p.visitInnerClass(paramString1, paramString2, paramString3, paramInt);
/* 189 */     super.visitInnerClass(paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 195 */     Printer printer = this.p.visitField(paramInt, paramString1, paramString2, paramString3, paramObject);
/* 196 */     FieldVisitor fieldVisitor = (this.cv == null) ? null : this.cv.visitField(paramInt, paramString1, paramString2, paramString3, paramObject);
/*     */     
/* 198 */     return new TraceFieldVisitor(fieldVisitor, printer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 204 */     Printer printer = this.p.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
/*     */     
/* 206 */     MethodVisitor methodVisitor = (this.cv == null) ? null : this.cv.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
/*     */     
/* 208 */     return new TraceMethodVisitor(methodVisitor, printer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 213 */     this.p.visitClassEnd();
/* 214 */     if (this.pw != null) {
/* 215 */       this.p.print(this.pw);
/* 216 */       this.pw.flush();
/*     */     } 
/* 218 */     super.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\TraceClassVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */