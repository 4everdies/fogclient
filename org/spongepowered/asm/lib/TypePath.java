/*     */ package org.spongepowered.asm.lib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypePath
/*     */ {
/*     */   public static final int ARRAY_ELEMENT = 0;
/*     */   public static final int INNER_TYPE = 1;
/*     */   public static final int WILDCARD_BOUND = 2;
/*     */   public static final int TYPE_ARGUMENT = 3;
/*     */   byte[] b;
/*     */   int offset;
/*     */   
/*     */   TypePath(byte[] paramArrayOfbyte, int paramInt) {
/*  85 */     this.b = paramArrayOfbyte;
/*  86 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  95 */     return this.b[this.offset];
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
/*     */   public int getStep(int paramInt) {
/* 108 */     return this.b[this.offset + 2 * paramInt + 1];
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
/*     */   public int getStepArgument(int paramInt) {
/* 122 */     return this.b[this.offset + 2 * paramInt + 2];
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
/*     */   public static TypePath fromString(String paramString) {
/* 135 */     if (paramString == null || paramString.length() == 0) {
/* 136 */       return null;
/*     */     }
/* 138 */     int i = paramString.length();
/* 139 */     ByteVector byteVector = new ByteVector(i);
/* 140 */     byteVector.putByte(0);
/* 141 */     for (byte b = 0; b < i; ) {
/* 142 */       char c = paramString.charAt(b++);
/* 143 */       if (c == '[') {
/* 144 */         byteVector.put11(0, 0); continue;
/* 145 */       }  if (c == '.') {
/* 146 */         byteVector.put11(1, 0); continue;
/* 147 */       }  if (c == '*') {
/* 148 */         byteVector.put11(2, 0); continue;
/* 149 */       }  if (c >= '0' && c <= '9') {
/* 150 */         int j = c - 48;
/* 151 */         while (b < i && (c = paramString.charAt(b)) >= '0' && c <= '9') {
/* 152 */           j = j * 10 + c - 48;
/* 153 */           b++;
/*     */         } 
/* 155 */         if (b < i && paramString.charAt(b) == ';') {
/* 156 */           b++;
/*     */         }
/* 158 */         byteVector.put11(3, j);
/*     */       } 
/*     */     } 
/* 161 */     byteVector.data[0] = (byte)(byteVector.length / 2);
/* 162 */     return new TypePath(byteVector.data, 0);
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
/*     */   public String toString() {
/* 174 */     int i = getLength();
/* 175 */     StringBuilder stringBuilder = new StringBuilder(i * 2);
/* 176 */     for (byte b = 0; b < i; b++) {
/* 177 */       switch (getStep(b)) {
/*     */         case 0:
/* 179 */           stringBuilder.append('[');
/*     */           break;
/*     */         case 1:
/* 182 */           stringBuilder.append('.');
/*     */           break;
/*     */         case 2:
/* 185 */           stringBuilder.append('*');
/*     */           break;
/*     */         case 3:
/* 188 */           stringBuilder.append(getStepArgument(b)).append(';');
/*     */           break;
/*     */         default:
/* 191 */           stringBuilder.append('_'); break;
/*     */       } 
/*     */     } 
/* 194 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\TypePath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */