/*     */ package org.spongepowered.asm.lib.commons;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.spongepowered.asm.lib.signature.SignatureVisitor;
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
/*     */ public class SignatureRemapper
/*     */   extends SignatureVisitor
/*     */ {
/*     */   private final SignatureVisitor v;
/*     */   private final Remapper remapper;
/*  49 */   private Stack<String> classNames = new Stack<String>();
/*     */   
/*     */   public SignatureRemapper(SignatureVisitor paramSignatureVisitor, Remapper paramRemapper) {
/*  52 */     this(327680, paramSignatureVisitor, paramRemapper);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SignatureRemapper(int paramInt, SignatureVisitor paramSignatureVisitor, Remapper paramRemapper) {
/*  57 */     super(paramInt);
/*  58 */     this.v = paramSignatureVisitor;
/*  59 */     this.remapper = paramRemapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitClassType(String paramString) {
/*  64 */     this.classNames.push(paramString);
/*  65 */     this.v.visitClassType(this.remapper.mapType(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClassType(String paramString) {
/*  70 */     String str1 = this.classNames.pop();
/*  71 */     String str2 = str1 + '$' + paramString;
/*  72 */     this.classNames.push(str2);
/*  73 */     String str3 = this.remapper.mapType(str1) + '$';
/*  74 */     String str4 = this.remapper.mapType(str2);
/*     */     
/*  76 */     int i = str4.startsWith(str3) ? str3.length() : (str4.lastIndexOf('$') + 1);
/*  77 */     this.v.visitInnerClassType(str4.substring(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFormalTypeParameter(String paramString) {
/*  82 */     this.v.visitFormalTypeParameter(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeVariable(String paramString) {
/*  87 */     this.v.visitTypeVariable(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitArrayType() {
/*  92 */     this.v.visitArrayType();
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitBaseType(char paramChar) {
/*  98 */     this.v.visitBaseType(paramChar);
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitClassBound() {
/* 103 */     this.v.visitClassBound();
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitExceptionType() {
/* 109 */     this.v.visitExceptionType();
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitInterface() {
/* 115 */     this.v.visitInterface();
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitInterfaceBound() {
/* 121 */     this.v.visitInterfaceBound();
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitParameterType() {
/* 127 */     this.v.visitParameterType();
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitReturnType() {
/* 133 */     this.v.visitReturnType();
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitSuperclass() {
/* 139 */     this.v.visitSuperclass();
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeArgument() {
/* 145 */     this.v.visitTypeArgument();
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitTypeArgument(char paramChar) {
/* 150 */     this.v.visitTypeArgument(paramChar);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 156 */     this.v.visitEnd();
/* 157 */     this.classNames.pop();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\SignatureRemapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */