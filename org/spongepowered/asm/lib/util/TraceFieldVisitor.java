/*    */ package org.spongepowered.asm.lib.util;
/*    */ 
/*    */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*    */ import org.spongepowered.asm.lib.Attribute;
/*    */ import org.spongepowered.asm.lib.FieldVisitor;
/*    */ import org.spongepowered.asm.lib.TypePath;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TraceFieldVisitor
/*    */   extends FieldVisitor
/*    */ {
/*    */   public final Printer p;
/*    */   
/*    */   public TraceFieldVisitor(Printer paramPrinter) {
/* 49 */     this(null, paramPrinter);
/*    */   }
/*    */   
/*    */   public TraceFieldVisitor(FieldVisitor paramFieldVisitor, Printer paramPrinter) {
/* 53 */     super(327680, paramFieldVisitor);
/* 54 */     this.p = paramPrinter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 60 */     Printer printer = this.p.visitFieldAnnotation(paramString, paramBoolean);
/* 61 */     AnnotationVisitor annotationVisitor = (this.fv == null) ? null : this.fv.visitAnnotation(paramString, paramBoolean);
/*    */     
/* 63 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 69 */     Printer printer = this.p.visitFieldTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*    */     
/* 71 */     AnnotationVisitor annotationVisitor = (this.fv == null) ? null : this.fv.visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*    */     
/* 73 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitAttribute(Attribute paramAttribute) {
/* 78 */     this.p.visitFieldAttribute(paramAttribute);
/* 79 */     super.visitAttribute(paramAttribute);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 84 */     this.p.visitFieldEnd();
/* 85 */     super.visitEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\TraceFieldVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */