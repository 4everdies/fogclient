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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignatureReader
/*     */ {
/*     */   private final String signature;
/*     */   
/*     */   public SignatureReader(String paramString) {
/*  54 */     this.signature = paramString;
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
/*     */   public void accept(SignatureVisitor paramSignatureVisitor) {
/*     */     int j;
/*  73 */     String str = this.signature;
/*  74 */     int i = str.length();
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (str.charAt(0) == '<') {
/*  79 */       char c; j = 2;
/*     */       do {
/*  81 */         int k = str.indexOf(':', j);
/*  82 */         paramSignatureVisitor.visitFormalTypeParameter(str.substring(j - 1, k));
/*  83 */         j = k + 1;
/*     */         
/*  85 */         c = str.charAt(j);
/*  86 */         if (c == 'L' || c == '[' || c == 'T') {
/*  87 */           j = parseType(str, j, paramSignatureVisitor.visitClassBound());
/*     */         }
/*     */         
/*  90 */         while ((c = str.charAt(j++)) == ':') {
/*  91 */           j = parseType(str, j, paramSignatureVisitor.visitInterfaceBound());
/*     */         }
/*  93 */       } while (c != '>');
/*     */     } else {
/*  95 */       j = 0;
/*     */     } 
/*     */     
/*  98 */     if (str.charAt(j) == '(') {
/*  99 */       j++;
/* 100 */       while (str.charAt(j) != ')') {
/* 101 */         j = parseType(str, j, paramSignatureVisitor.visitParameterType());
/*     */       }
/* 103 */       j = parseType(str, j + 1, paramSignatureVisitor.visitReturnType());
/* 104 */       while (j < i) {
/* 105 */         j = parseType(str, j + 1, paramSignatureVisitor.visitExceptionType());
/*     */       }
/*     */     } else {
/* 108 */       j = parseType(str, j, paramSignatureVisitor.visitSuperclass());
/* 109 */       while (j < i) {
/* 110 */         j = parseType(str, j, paramSignatureVisitor.visitInterface());
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptType(SignatureVisitor paramSignatureVisitor) {
/* 130 */     parseType(this.signature, 0, paramSignatureVisitor);
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
/*     */   
/*     */   private static int parseType(String paramString, int paramInt, SignatureVisitor paramSignatureVisitor) {
/*     */     int i;
/*     */     char c;
/* 151 */     switch (c = paramString.charAt(paramInt++)) {
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'F':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'S':
/*     */       case 'V':
/*     */       case 'Z':
/* 161 */         paramSignatureVisitor.visitBaseType(c);
/* 162 */         return paramInt;
/*     */       
/*     */       case '[':
/* 165 */         return parseType(paramString, paramInt, paramSignatureVisitor.visitArrayType());
/*     */       
/*     */       case 'T':
/* 168 */         i = paramString.indexOf(';', paramInt);
/* 169 */         paramSignatureVisitor.visitTypeVariable(paramString.substring(paramInt, i));
/* 170 */         return i + 1;
/*     */     } 
/*     */     
/* 173 */     int j = paramInt;
/* 174 */     boolean bool1 = false;
/* 175 */     boolean bool2 = false; label36: while (true) {
/*     */       String str;
/* 177 */       switch (c = paramString.charAt(paramInt++)) {
/*     */         case '.':
/*     */         case ';':
/* 180 */           if (!bool1) {
/* 181 */             String str1 = paramString.substring(j, paramInt - 1);
/* 182 */             if (bool2) {
/* 183 */               paramSignatureVisitor.visitInnerClassType(str1);
/*     */             } else {
/* 185 */               paramSignatureVisitor.visitClassType(str1);
/*     */             } 
/*     */           } 
/* 188 */           if (c == ';') {
/* 189 */             paramSignatureVisitor.visitEnd();
/* 190 */             return paramInt;
/*     */           } 
/* 192 */           j = paramInt;
/* 193 */           bool1 = false;
/* 194 */           bool2 = true;
/*     */ 
/*     */         
/*     */         case '<':
/* 198 */           str = paramString.substring(j, paramInt - 1);
/* 199 */           if (bool2) {
/* 200 */             paramSignatureVisitor.visitInnerClassType(str);
/*     */           } else {
/* 202 */             paramSignatureVisitor.visitClassType(str);
/*     */           } 
/* 204 */           bool1 = true;
/*     */           while (true) {
/* 206 */             switch (c = paramString.charAt(paramInt)) {
/*     */               case '>':
/*     */                 continue label36;
/*     */               case '*':
/* 210 */                 paramInt++;
/* 211 */                 paramSignatureVisitor.visitTypeArgument();
/*     */                 continue;
/*     */               case '+':
/*     */               case '-':
/* 215 */                 paramInt = parseType(paramString, paramInt + 1, paramSignatureVisitor
/* 216 */                     .visitTypeArgument(c));
/*     */                 continue;
/*     */             } 
/* 219 */             paramInt = parseType(paramString, paramInt, paramSignatureVisitor
/* 220 */                 .visitTypeArgument('='));
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\signature\SignatureReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */