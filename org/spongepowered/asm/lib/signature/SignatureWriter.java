/*     */ package org.spongepowered.asm.lib.signature;
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
/*     */ public class SignatureWriter
/*     */   extends SignatureVisitor
/*     */ {
/*  45 */   private final StringBuilder buf = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasFormals;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasParameters;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int argumentStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SignatureWriter() {
/*  69 */     super(327680);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFormalTypeParameter(String paramString) {
/*  78 */     if (!this.hasFormals) {
/*  79 */       this.hasFormals = true;
/*  80 */       this.buf.append('<');
/*     */     } 
/*  82 */     this.buf.append(paramString);
/*  83 */     this.buf.append(':');
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitClassBound() {
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitInterfaceBound() {
/*  93 */     this.buf.append(':');
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitSuperclass() {
/*  99 */     endFormals();
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitInterface() {
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitParameterType() {
/* 110 */     endFormals();
/* 111 */     if (!this.hasParameters) {
/* 112 */       this.hasParameters = true;
/* 113 */       this.buf.append('(');
/*     */     } 
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitReturnType() {
/* 120 */     endFormals();
/* 121 */     if (!this.hasParameters) {
/* 122 */       this.buf.append('(');
/*     */     }
/* 124 */     this.buf.append(')');
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitExceptionType() {
/* 130 */     this.buf.append('^');
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitBaseType(char paramChar) {
/* 136 */     this.buf.append(paramChar);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeVariable(String paramString) {
/* 141 */     this.buf.append('T');
/* 142 */     this.buf.append(paramString);
/* 143 */     this.buf.append(';');
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitArrayType() {
/* 148 */     this.buf.append('[');
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitClassType(String paramString) {
/* 154 */     this.buf.append('L');
/* 155 */     this.buf.append(paramString);
/* 156 */     this.argumentStack *= 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClassType(String paramString) {
/* 161 */     endArguments();
/* 162 */     this.buf.append('.');
/* 163 */     this.buf.append(paramString);
/* 164 */     this.argumentStack *= 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeArgument() {
/* 169 */     if (this.argumentStack % 2 == 0) {
/* 170 */       this.argumentStack++;
/* 171 */       this.buf.append('<');
/*     */     } 
/* 173 */     this.buf.append('*');
/*     */   }
/*     */ 
/*     */   
/*     */   public SignatureVisitor visitTypeArgument(char paramChar) {
/* 178 */     if (this.argumentStack % 2 == 0) {
/* 179 */       this.argumentStack++;
/* 180 */       this.buf.append('<');
/*     */     } 
/* 182 */     if (paramChar != '=') {
/* 183 */       this.buf.append(paramChar);
/*     */     }
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 190 */     endArguments();
/* 191 */     this.buf.append(';');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     return this.buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void endFormals() {
/* 212 */     if (this.hasFormals) {
/* 213 */       this.hasFormals = false;
/* 214 */       this.buf.append('>');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void endArguments() {
/* 222 */     if (this.argumentStack % 2 != 0) {
/* 223 */       this.buf.append('>');
/*     */     }
/* 225 */     this.argumentStack /= 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\signature\SignatureWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */