/*    */ package org.spongepowered.asm.lib.commons;
/*    */ 
/*    */ import org.spongepowered.asm.lib.AnnotationVisitor;
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
/*    */ public class FieldRemapper
/*    */   extends FieldVisitor
/*    */ {
/*    */   private final Remapper remapper;
/*    */   
/*    */   public FieldRemapper(FieldVisitor paramFieldVisitor, Remapper paramRemapper) {
/* 48 */     this(327680, paramFieldVisitor, paramRemapper);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FieldRemapper(int paramInt, FieldVisitor paramFieldVisitor, Remapper paramRemapper) {
/* 53 */     super(paramInt, paramFieldVisitor);
/* 54 */     this.remapper = paramRemapper;
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 59 */     AnnotationVisitor annotationVisitor = this.fv.visitAnnotation(this.remapper.mapDesc(paramString), paramBoolean);
/*    */     
/* 61 */     return (annotationVisitor == null) ? null : new AnnotationRemapper(annotationVisitor, this.remapper);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 67 */     AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(paramInt, paramTypePath, this.remapper
/* 68 */         .mapDesc(paramString), paramBoolean);
/* 69 */     return (annotationVisitor == null) ? null : new AnnotationRemapper(annotationVisitor, this.remapper);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\FieldRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */