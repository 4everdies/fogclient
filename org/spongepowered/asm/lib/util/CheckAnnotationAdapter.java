/*     */ package org.spongepowered.asm.lib.util;
/*     */ 
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckAnnotationAdapter
/*     */   extends AnnotationVisitor
/*     */ {
/*     */   private final boolean named;
/*     */   private boolean end;
/*     */   
/*     */   public CheckAnnotationAdapter(AnnotationVisitor paramAnnotationVisitor) {
/*  48 */     this(paramAnnotationVisitor, true);
/*     */   }
/*     */   
/*     */   CheckAnnotationAdapter(AnnotationVisitor paramAnnotationVisitor, boolean paramBoolean) {
/*  52 */     super(327680, paramAnnotationVisitor);
/*  53 */     this.named = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(String paramString, Object paramObject) {
/*  58 */     checkEnd();
/*  59 */     checkName(paramString);
/*  60 */     if (!(paramObject instanceof Byte) && !(paramObject instanceof Boolean) && !(paramObject instanceof Character) && !(paramObject instanceof Short) && !(paramObject instanceof Integer) && !(paramObject instanceof Long) && !(paramObject instanceof Float) && !(paramObject instanceof Double) && !(paramObject instanceof String) && !(paramObject instanceof Type) && !(paramObject instanceof byte[]) && !(paramObject instanceof boolean[]) && !(paramObject instanceof char[]) && !(paramObject instanceof short[]) && !(paramObject instanceof int[]) && !(paramObject instanceof long[]) && !(paramObject instanceof float[]) && !(paramObject instanceof double[]))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  69 */       throw new IllegalArgumentException("Invalid annotation value");
/*     */     }
/*  71 */     if (paramObject instanceof Type) {
/*  72 */       int i = ((Type)paramObject).getSort();
/*  73 */       if (i == 11) {
/*  74 */         throw new IllegalArgumentException("Invalid annotation value");
/*     */       }
/*     */     } 
/*  77 */     if (this.av != null) {
/*  78 */       this.av.visit(paramString, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/*  85 */     checkEnd();
/*  86 */     checkName(paramString1);
/*  87 */     CheckMethodAdapter.checkDesc(paramString2, false);
/*  88 */     if (paramString3 == null) {
/*  89 */       throw new IllegalArgumentException("Invalid enum value");
/*     */     }
/*  91 */     if (this.av != null) {
/*  92 */       this.av.visitEnum(paramString1, paramString2, paramString3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
/*  99 */     checkEnd();
/* 100 */     checkName(paramString1);
/* 101 */     CheckMethodAdapter.checkDesc(paramString2, false);
/* 102 */     return new CheckAnnotationAdapter((this.av == null) ? null : this.av
/* 103 */         .visitAnnotation(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String paramString) {
/* 108 */     checkEnd();
/* 109 */     checkName(paramString);
/* 110 */     return new CheckAnnotationAdapter((this.av == null) ? null : this.av
/* 111 */         .visitArray(paramString), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 116 */     checkEnd();
/* 117 */     this.end = true;
/* 118 */     if (this.av != null) {
/* 119 */       this.av.visitEnd();
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkEnd() {
/* 124 */     if (this.end) {
/* 125 */       throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkName(String paramString) {
/* 131 */     if (this.named && paramString == null)
/* 132 */       throw new IllegalArgumentException("Annotation value name must not be null"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\CheckAnnotationAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */