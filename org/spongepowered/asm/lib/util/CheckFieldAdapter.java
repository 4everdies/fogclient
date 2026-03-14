/*     */ package org.spongepowered.asm.lib.util;
/*     */ 
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
/*     */ import org.spongepowered.asm.lib.FieldVisitor;
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
/*     */ public class CheckFieldAdapter
/*     */   extends FieldVisitor
/*     */ {
/*     */   private boolean end;
/*     */   
/*     */   public CheckFieldAdapter(FieldVisitor paramFieldVisitor) {
/*  57 */     this(327680, paramFieldVisitor);
/*  58 */     if (getClass() != CheckFieldAdapter.class) {
/*  59 */       throw new IllegalStateException();
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
/*     */   protected CheckFieldAdapter(int paramInt, FieldVisitor paramFieldVisitor) {
/*  73 */     super(paramInt, paramFieldVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  79 */     checkEnd();
/*  80 */     CheckMethodAdapter.checkDesc(paramString, false);
/*  81 */     return new CheckAnnotationAdapter(super.visitAnnotation(paramString, paramBoolean));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  87 */     checkEnd();
/*  88 */     int i = paramInt >>> 24;
/*  89 */     if (i != 19) {
/*  90 */       throw new IllegalArgumentException("Invalid type reference sort 0x" + 
/*  91 */           Integer.toHexString(i));
/*     */     }
/*  93 */     CheckClassAdapter.checkTypeRefAndPath(paramInt, paramTypePath);
/*  94 */     CheckMethodAdapter.checkDesc(paramString, false);
/*  95 */     return new CheckAnnotationAdapter(super.visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 101 */     checkEnd();
/* 102 */     if (paramAttribute == null) {
/* 103 */       throw new IllegalArgumentException("Invalid attribute (must not be null)");
/*     */     }
/*     */     
/* 106 */     super.visitAttribute(paramAttribute);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 111 */     checkEnd();
/* 112 */     this.end = true;
/* 113 */     super.visitEnd();
/*     */   }
/*     */   
/*     */   private void checkEnd() {
/* 117 */     if (this.end)
/* 118 */       throw new IllegalStateException("Cannot call a visit method after visitEnd has been called"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\CheckFieldAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */