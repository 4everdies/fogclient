/*    */ package org.spongepowered.asm.lib.commons;
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
/*    */ 
/*    */ public class AnnotationRemapper
/*    */   extends AnnotationVisitor
/*    */ {
/*    */   protected final Remapper remapper;
/*    */   
/*    */   public AnnotationRemapper(AnnotationVisitor paramAnnotationVisitor, Remapper paramRemapper) {
/* 47 */     this(327680, paramAnnotationVisitor, paramRemapper);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AnnotationRemapper(int paramInt, AnnotationVisitor paramAnnotationVisitor, Remapper paramRemapper) {
/* 52 */     super(paramInt, paramAnnotationVisitor);
/* 53 */     this.remapper = paramRemapper;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(String paramString, Object paramObject) {
/* 58 */     this.av.visit(paramString, this.remapper.mapValue(paramObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/* 63 */     this.av.visitEnum(paramString1, this.remapper.mapDesc(paramString2), paramString3);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
/* 68 */     AnnotationVisitor annotationVisitor = this.av.visitAnnotation(paramString1, this.remapper.mapDesc(paramString2));
/* 69 */     return (annotationVisitor == null) ? null : ((annotationVisitor == this.av) ? this : new AnnotationRemapper(annotationVisitor, this.remapper));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitArray(String paramString) {
/* 75 */     AnnotationVisitor annotationVisitor = this.av.visitArray(paramString);
/* 76 */     return (annotationVisitor == null) ? null : ((annotationVisitor == this.av) ? this : new AnnotationRemapper(annotationVisitor, this.remapper));
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\AnnotationRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */