/*    */ package org.spongepowered.asm.lib.util;
/*    */ 
/*    */ import org.spongepowered.asm.lib.AnnotationVisitor;
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
/*    */ public final class TraceAnnotationVisitor
/*    */   extends AnnotationVisitor
/*    */ {
/*    */   private final Printer p;
/*    */   
/*    */   public TraceAnnotationVisitor(Printer paramPrinter) {
/* 46 */     this(null, paramPrinter);
/*    */   }
/*    */   
/*    */   public TraceAnnotationVisitor(AnnotationVisitor paramAnnotationVisitor, Printer paramPrinter) {
/* 50 */     super(327680, paramAnnotationVisitor);
/* 51 */     this.p = paramPrinter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(String paramString, Object paramObject) {
/* 56 */     this.p.visit(paramString, paramObject);
/* 57 */     super.visit(paramString, paramObject);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/* 63 */     this.p.visitEnum(paramString1, paramString2, paramString3);
/* 64 */     super.visitEnum(paramString1, paramString2, paramString3);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
/* 70 */     Printer printer = this.p.visitAnnotation(paramString1, paramString2);
/*    */     
/* 72 */     AnnotationVisitor annotationVisitor = (this.av == null) ? null : this.av.visitAnnotation(paramString1, paramString2);
/* 73 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitArray(String paramString) {
/* 78 */     Printer printer = this.p.visitArray(paramString);
/*    */     
/* 80 */     AnnotationVisitor annotationVisitor = (this.av == null) ? null : this.av.visitArray(paramString);
/* 81 */     return new TraceAnnotationVisitor(annotationVisitor, printer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 86 */     this.p.visitAnnotationEnd();
/* 87 */     super.visitEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\TraceAnnotationVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */