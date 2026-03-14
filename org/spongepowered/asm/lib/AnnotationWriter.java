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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AnnotationWriter
/*     */   extends AnnotationVisitor
/*     */ {
/*     */   private final ClassWriter cw;
/*     */   private int size;
/*     */   private final boolean named;
/*     */   private final ByteVector bv;
/*     */   private final ByteVector parent;
/*     */   private final int offset;
/*     */   AnnotationWriter next;
/*     */   AnnotationWriter prev;
/*     */   
/*     */   AnnotationWriter(ClassWriter paramClassWriter, boolean paramBoolean, ByteVector paramByteVector1, ByteVector paramByteVector2, int paramInt) {
/* 107 */     super(327680);
/* 108 */     this.cw = paramClassWriter;
/* 109 */     this.named = paramBoolean;
/* 110 */     this.bv = paramByteVector1;
/* 111 */     this.parent = paramByteVector2;
/* 112 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String paramString, Object paramObject) {
/* 121 */     this.size++;
/* 122 */     if (this.named) {
/* 123 */       this.bv.putShort(this.cw.newUTF8(paramString));
/*     */     }
/* 125 */     if (paramObject instanceof String) {
/* 126 */       this.bv.put12(115, this.cw.newUTF8((String)paramObject));
/* 127 */     } else if (paramObject instanceof Byte) {
/* 128 */       this.bv.put12(66, (this.cw.newInteger(((Byte)paramObject).byteValue())).index);
/* 129 */     } else if (paramObject instanceof Boolean) {
/* 130 */       boolean bool = ((Boolean)paramObject).booleanValue() ? true : false;
/* 131 */       this.bv.put12(90, (this.cw.newInteger(bool)).index);
/* 132 */     } else if (paramObject instanceof Character) {
/* 133 */       this.bv.put12(67, (this.cw.newInteger(((Character)paramObject).charValue())).index);
/* 134 */     } else if (paramObject instanceof Short) {
/* 135 */       this.bv.put12(83, (this.cw.newInteger(((Short)paramObject).shortValue())).index);
/* 136 */     } else if (paramObject instanceof Type) {
/* 137 */       this.bv.put12(99, this.cw.newUTF8(((Type)paramObject).getDescriptor()));
/* 138 */     } else if (paramObject instanceof byte[]) {
/* 139 */       byte[] arrayOfByte = (byte[])paramObject;
/* 140 */       this.bv.put12(91, arrayOfByte.length);
/* 141 */       for (byte b = 0; b < arrayOfByte.length; b++) {
/* 142 */         this.bv.put12(66, (this.cw.newInteger(arrayOfByte[b])).index);
/*     */       }
/* 144 */     } else if (paramObject instanceof boolean[]) {
/* 145 */       boolean[] arrayOfBoolean = (boolean[])paramObject;
/* 146 */       this.bv.put12(91, arrayOfBoolean.length);
/* 147 */       for (byte b = 0; b < arrayOfBoolean.length; b++) {
/* 148 */         this.bv.put12(90, (this.cw.newInteger(arrayOfBoolean[b] ? 1 : 0)).index);
/*     */       }
/* 150 */     } else if (paramObject instanceof short[]) {
/* 151 */       short[] arrayOfShort = (short[])paramObject;
/* 152 */       this.bv.put12(91, arrayOfShort.length);
/* 153 */       for (byte b = 0; b < arrayOfShort.length; b++) {
/* 154 */         this.bv.put12(83, (this.cw.newInteger(arrayOfShort[b])).index);
/*     */       }
/* 156 */     } else if (paramObject instanceof char[]) {
/* 157 */       char[] arrayOfChar = (char[])paramObject;
/* 158 */       this.bv.put12(91, arrayOfChar.length);
/* 159 */       for (byte b = 0; b < arrayOfChar.length; b++) {
/* 160 */         this.bv.put12(67, (this.cw.newInteger(arrayOfChar[b])).index);
/*     */       }
/* 162 */     } else if (paramObject instanceof int[]) {
/* 163 */       int[] arrayOfInt = (int[])paramObject;
/* 164 */       this.bv.put12(91, arrayOfInt.length);
/* 165 */       for (byte b = 0; b < arrayOfInt.length; b++) {
/* 166 */         this.bv.put12(73, (this.cw.newInteger(arrayOfInt[b])).index);
/*     */       }
/* 168 */     } else if (paramObject instanceof long[]) {
/* 169 */       long[] arrayOfLong = (long[])paramObject;
/* 170 */       this.bv.put12(91, arrayOfLong.length);
/* 171 */       for (byte b = 0; b < arrayOfLong.length; b++) {
/* 172 */         this.bv.put12(74, (this.cw.newLong(arrayOfLong[b])).index);
/*     */       }
/* 174 */     } else if (paramObject instanceof float[]) {
/* 175 */       float[] arrayOfFloat = (float[])paramObject;
/* 176 */       this.bv.put12(91, arrayOfFloat.length);
/* 177 */       for (byte b = 0; b < arrayOfFloat.length; b++) {
/* 178 */         this.bv.put12(70, (this.cw.newFloat(arrayOfFloat[b])).index);
/*     */       }
/* 180 */     } else if (paramObject instanceof double[]) {
/* 181 */       double[] arrayOfDouble = (double[])paramObject;
/* 182 */       this.bv.put12(91, arrayOfDouble.length);
/* 183 */       for (byte b = 0; b < arrayOfDouble.length; b++) {
/* 184 */         this.bv.put12(68, (this.cw.newDouble(arrayOfDouble[b])).index);
/*     */       }
/*     */     } else {
/* 187 */       Item item = this.cw.newConstItem(paramObject);
/* 188 */       this.bv.put12(".s.IFJDCS".charAt(item.type), item.index);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/* 195 */     this.size++;
/* 196 */     if (this.named) {
/* 197 */       this.bv.putShort(this.cw.newUTF8(paramString1));
/*     */     }
/* 199 */     this.bv.put12(101, this.cw.newUTF8(paramString2)).putShort(this.cw.newUTF8(paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
/* 205 */     this.size++;
/* 206 */     if (this.named) {
/* 207 */       this.bv.putShort(this.cw.newUTF8(paramString1));
/*     */     }
/*     */     
/* 210 */     this.bv.put12(64, this.cw.newUTF8(paramString2)).putShort(0);
/* 211 */     return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String paramString) {
/* 216 */     this.size++;
/* 217 */     if (this.named) {
/* 218 */       this.bv.putShort(this.cw.newUTF8(paramString));
/*     */     }
/*     */     
/* 221 */     this.bv.put12(91, 0);
/* 222 */     return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 227 */     if (this.parent != null) {
/* 228 */       byte[] arrayOfByte = this.parent.data;
/* 229 */       arrayOfByte[this.offset] = (byte)(this.size >>> 8);
/* 230 */       arrayOfByte[this.offset + 1] = (byte)this.size;
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
/*     */   int getSize() {
/* 244 */     int i = 0;
/* 245 */     AnnotationWriter annotationWriter = this;
/* 246 */     while (annotationWriter != null) {
/* 247 */       i += annotationWriter.bv.length;
/* 248 */       annotationWriter = annotationWriter.next;
/*     */     } 
/* 250 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void put(ByteVector paramByteVector) {
/* 261 */     byte b = 0;
/* 262 */     int i = 2;
/* 263 */     AnnotationWriter annotationWriter1 = this;
/* 264 */     AnnotationWriter annotationWriter2 = null;
/* 265 */     while (annotationWriter1 != null) {
/* 266 */       b++;
/* 267 */       i += annotationWriter1.bv.length;
/* 268 */       annotationWriter1.visitEnd();
/* 269 */       annotationWriter1.prev = annotationWriter2;
/* 270 */       annotationWriter2 = annotationWriter1;
/* 271 */       annotationWriter1 = annotationWriter1.next;
/*     */     } 
/* 273 */     paramByteVector.putInt(i);
/* 274 */     paramByteVector.putShort(b);
/* 275 */     annotationWriter1 = annotationWriter2;
/* 276 */     while (annotationWriter1 != null) {
/* 277 */       paramByteVector.putByteArray(annotationWriter1.bv.data, 0, annotationWriter1.bv.length);
/* 278 */       annotationWriter1 = annotationWriter1.prev;
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
/*     */   static void put(AnnotationWriter[] paramArrayOfAnnotationWriter, int paramInt, ByteVector paramByteVector) {
/* 294 */     int i = 1 + 2 * (paramArrayOfAnnotationWriter.length - paramInt); int j;
/* 295 */     for (j = paramInt; j < paramArrayOfAnnotationWriter.length; j++) {
/* 296 */       i += (paramArrayOfAnnotationWriter[j] == null) ? 0 : paramArrayOfAnnotationWriter[j].getSize();
/*     */     }
/* 298 */     paramByteVector.putInt(i).putByte(paramArrayOfAnnotationWriter.length - paramInt);
/* 299 */     for (j = paramInt; j < paramArrayOfAnnotationWriter.length; j++) {
/* 300 */       AnnotationWriter annotationWriter1 = paramArrayOfAnnotationWriter[j];
/* 301 */       AnnotationWriter annotationWriter2 = null;
/* 302 */       byte b = 0;
/* 303 */       while (annotationWriter1 != null) {
/* 304 */         b++;
/* 305 */         annotationWriter1.visitEnd();
/* 306 */         annotationWriter1.prev = annotationWriter2;
/* 307 */         annotationWriter2 = annotationWriter1;
/* 308 */         annotationWriter1 = annotationWriter1.next;
/*     */       } 
/* 310 */       paramByteVector.putShort(b);
/* 311 */       annotationWriter1 = annotationWriter2;
/* 312 */       while (annotationWriter1 != null) {
/* 313 */         paramByteVector.putByteArray(annotationWriter1.bv.data, 0, annotationWriter1.bv.length);
/* 314 */         annotationWriter1 = annotationWriter1.prev;
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
/*     */   static void putTarget(int paramInt, TypePath paramTypePath, ByteVector paramByteVector) {
/* 333 */     switch (paramInt >>> 24) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 22:
/* 337 */         paramByteVector.putShort(paramInt >>> 16);
/*     */         break;
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/* 342 */         paramByteVector.putByte(paramInt >>> 24);
/*     */         break;
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/* 349 */         paramByteVector.putInt(paramInt);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 361 */         paramByteVector.put12(paramInt >>> 24, (paramInt & 0xFFFF00) >> 8);
/*     */         break;
/*     */     } 
/* 364 */     if (paramTypePath == null) {
/* 365 */       paramByteVector.putByte(0);
/*     */     } else {
/* 367 */       int i = paramTypePath.b[paramTypePath.offset] * 2 + 1;
/* 368 */       paramByteVector.putByteArray(paramTypePath.b, paramTypePath.offset, i);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\AnnotationWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */