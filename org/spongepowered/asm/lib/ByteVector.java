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
/*     */ public class ByteVector
/*     */ {
/*     */   byte[] data;
/*     */   int length;
/*     */   
/*     */   public ByteVector() {
/*  55 */     this.data = new byte[64];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteVector(int paramInt) {
/*  66 */     this.data = new byte[paramInt];
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
/*     */   public ByteVector putByte(int paramInt) {
/*  78 */     int i = this.length;
/*  79 */     if (i + 1 > this.data.length) {
/*  80 */       enlarge(1);
/*     */     }
/*  82 */     this.data[i++] = (byte)paramInt;
/*  83 */     this.length = i;
/*  84 */     return this;
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
/*     */   ByteVector put11(int paramInt1, int paramInt2) {
/*  98 */     int i = this.length;
/*  99 */     if (i + 2 > this.data.length) {
/* 100 */       enlarge(2);
/*     */     }
/* 102 */     byte[] arrayOfByte = this.data;
/* 103 */     arrayOfByte[i++] = (byte)paramInt1;
/* 104 */     arrayOfByte[i++] = (byte)paramInt2;
/* 105 */     this.length = i;
/* 106 */     return this;
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
/*     */   public ByteVector putShort(int paramInt) {
/* 118 */     int i = this.length;
/* 119 */     if (i + 2 > this.data.length) {
/* 120 */       enlarge(2);
/*     */     }
/* 122 */     byte[] arrayOfByte = this.data;
/* 123 */     arrayOfByte[i++] = (byte)(paramInt >>> 8);
/* 124 */     arrayOfByte[i++] = (byte)paramInt;
/* 125 */     this.length = i;
/* 126 */     return this;
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
/*     */   ByteVector put12(int paramInt1, int paramInt2) {
/* 140 */     int i = this.length;
/* 141 */     if (i + 3 > this.data.length) {
/* 142 */       enlarge(3);
/*     */     }
/* 144 */     byte[] arrayOfByte = this.data;
/* 145 */     arrayOfByte[i++] = (byte)paramInt1;
/* 146 */     arrayOfByte[i++] = (byte)(paramInt2 >>> 8);
/* 147 */     arrayOfByte[i++] = (byte)paramInt2;
/* 148 */     this.length = i;
/* 149 */     return this;
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
/*     */   public ByteVector putInt(int paramInt) {
/* 161 */     int i = this.length;
/* 162 */     if (i + 4 > this.data.length) {
/* 163 */       enlarge(4);
/*     */     }
/* 165 */     byte[] arrayOfByte = this.data;
/* 166 */     arrayOfByte[i++] = (byte)(paramInt >>> 24);
/* 167 */     arrayOfByte[i++] = (byte)(paramInt >>> 16);
/* 168 */     arrayOfByte[i++] = (byte)(paramInt >>> 8);
/* 169 */     arrayOfByte[i++] = (byte)paramInt;
/* 170 */     this.length = i;
/* 171 */     return this;
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
/*     */   public ByteVector putLong(long paramLong) {
/* 183 */     int i = this.length;
/* 184 */     if (i + 8 > this.data.length) {
/* 185 */       enlarge(8);
/*     */     }
/* 187 */     byte[] arrayOfByte = this.data;
/* 188 */     int j = (int)(paramLong >>> 32L);
/* 189 */     arrayOfByte[i++] = (byte)(j >>> 24);
/* 190 */     arrayOfByte[i++] = (byte)(j >>> 16);
/* 191 */     arrayOfByte[i++] = (byte)(j >>> 8);
/* 192 */     arrayOfByte[i++] = (byte)j;
/* 193 */     j = (int)paramLong;
/* 194 */     arrayOfByte[i++] = (byte)(j >>> 24);
/* 195 */     arrayOfByte[i++] = (byte)(j >>> 16);
/* 196 */     arrayOfByte[i++] = (byte)(j >>> 8);
/* 197 */     arrayOfByte[i++] = (byte)j;
/* 198 */     this.length = i;
/* 199 */     return this;
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
/*     */   public ByteVector putUTF8(String paramString) {
/* 211 */     int i = paramString.length();
/* 212 */     if (i > 65535) {
/* 213 */       throw new IllegalArgumentException();
/*     */     }
/* 215 */     int j = this.length;
/* 216 */     if (j + 2 + i > this.data.length) {
/* 217 */       enlarge(2 + i);
/*     */     }
/* 219 */     byte[] arrayOfByte = this.data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     arrayOfByte[j++] = (byte)(i >>> 8);
/* 227 */     arrayOfByte[j++] = (byte)i;
/* 228 */     for (byte b = 0; b < i; b++) {
/* 229 */       char c = paramString.charAt(b);
/* 230 */       if (c >= '\001' && c <= '') {
/* 231 */         arrayOfByte[j++] = (byte)c;
/*     */       } else {
/* 233 */         this.length = j;
/* 234 */         return encodeUTF8(paramString, b, 65535);
/*     */       } 
/*     */     } 
/* 237 */     this.length = j;
/* 238 */     return this;
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
/*     */ 
/*     */   
/*     */   ByteVector encodeUTF8(String paramString, int paramInt1, int paramInt2) {
/* 259 */     int i = paramString.length();
/* 260 */     int j = paramInt1;
/*     */     int k;
/* 262 */     for (k = paramInt1; k < i; k++) {
/* 263 */       char c = paramString.charAt(k);
/* 264 */       if (c >= '\001' && c <= '') {
/* 265 */         j++;
/* 266 */       } else if (c > '߿') {
/* 267 */         j += 3;
/*     */       } else {
/* 269 */         j += 2;
/*     */       } 
/*     */     } 
/* 272 */     if (j > paramInt2) {
/* 273 */       throw new IllegalArgumentException();
/*     */     }
/* 275 */     k = this.length - paramInt1 - 2;
/* 276 */     if (k >= 0) {
/* 277 */       this.data[k] = (byte)(j >>> 8);
/* 278 */       this.data[k + 1] = (byte)j;
/*     */     } 
/* 280 */     if (this.length + j - paramInt1 > this.data.length) {
/* 281 */       enlarge(j - paramInt1);
/*     */     }
/* 283 */     int m = this.length;
/* 284 */     for (int n = paramInt1; n < i; n++) {
/* 285 */       char c = paramString.charAt(n);
/* 286 */       if (c >= '\001' && c <= '') {
/* 287 */         this.data[m++] = (byte)c;
/* 288 */       } else if (c > '߿') {
/* 289 */         this.data[m++] = (byte)(0xE0 | c >> 12 & 0xF);
/* 290 */         this.data[m++] = (byte)(0x80 | c >> 6 & 0x3F);
/* 291 */         this.data[m++] = (byte)(0x80 | c & 0x3F);
/*     */       } else {
/* 293 */         this.data[m++] = (byte)(0xC0 | c >> 6 & 0x1F);
/* 294 */         this.data[m++] = (byte)(0x80 | c & 0x3F);
/*     */       } 
/*     */     } 
/* 297 */     this.length = m;
/* 298 */     return this;
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
/*     */   public ByteVector putByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 315 */     if (this.length + paramInt2 > this.data.length) {
/* 316 */       enlarge(paramInt2);
/*     */     }
/* 318 */     if (paramArrayOfbyte != null) {
/* 319 */       System.arraycopy(paramArrayOfbyte, paramInt1, this.data, this.length, paramInt2);
/*     */     }
/* 321 */     this.length += paramInt2;
/* 322 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void enlarge(int paramInt) {
/* 333 */     int i = 2 * this.data.length;
/* 334 */     int j = this.length + paramInt;
/* 335 */     byte[] arrayOfByte = new byte[(i > j) ? i : j];
/* 336 */     System.arraycopy(this.data, 0, arrayOfByte, 0, this.length);
/* 337 */     this.data = arrayOfByte;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\ByteVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */