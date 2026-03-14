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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FieldWriter
/*     */   extends FieldVisitor
/*     */ {
/*     */   private final ClassWriter cw;
/*     */   private final int access;
/*     */   private final int name;
/*     */   private final int desc;
/*     */   private int signature;
/*     */   private int value;
/*     */   private AnnotationWriter anns;
/*     */   private AnnotationWriter ianns;
/*     */   private AnnotationWriter tanns;
/*     */   private AnnotationWriter itanns;
/*     */   private Attribute attrs;
/*     */   
/*     */   FieldWriter(ClassWriter paramClassWriter, int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 121 */     super(327680);
/* 122 */     if (paramClassWriter.firstField == null) {
/* 123 */       paramClassWriter.firstField = this;
/*     */     } else {
/* 125 */       paramClassWriter.lastField.fv = this;
/*     */     } 
/* 127 */     paramClassWriter.lastField = this;
/* 128 */     this.cw = paramClassWriter;
/* 129 */     this.access = paramInt;
/* 130 */     this.name = paramClassWriter.newUTF8(paramString1);
/* 131 */     this.desc = paramClassWriter.newUTF8(paramString2);
/* 132 */     if (paramString3 != null) {
/* 133 */       this.signature = paramClassWriter.newUTF8(paramString3);
/*     */     }
/* 135 */     if (paramObject != null) {
/* 136 */       this.value = (paramClassWriter.newConstItem(paramObject)).index;
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
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 150 */     ByteVector byteVector = new ByteVector();
/*     */     
/* 152 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/* 153 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
/* 154 */     if (paramBoolean) {
/* 155 */       annotationWriter.next = this.anns;
/* 156 */       this.anns = annotationWriter;
/*     */     } else {
/* 158 */       annotationWriter.next = this.ianns;
/* 159 */       this.ianns = annotationWriter;
/*     */     } 
/* 161 */     return annotationWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 170 */     ByteVector byteVector = new ByteVector();
/*     */     
/* 172 */     AnnotationWriter.putTarget(paramInt, paramTypePath, byteVector);
/*     */     
/* 174 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/* 175 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
/*     */     
/* 177 */     if (paramBoolean) {
/* 178 */       annotationWriter.next = this.tanns;
/* 179 */       this.tanns = annotationWriter;
/*     */     } else {
/* 181 */       annotationWriter.next = this.itanns;
/* 182 */       this.itanns = annotationWriter;
/*     */     } 
/* 184 */     return annotationWriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 189 */     paramAttribute.next = this.attrs;
/* 190 */     this.attrs = paramAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSize() {
/* 207 */     int i = 8;
/* 208 */     if (this.value != 0) {
/* 209 */       this.cw.newUTF8("ConstantValue");
/* 210 */       i += 8;
/*     */     } 
/* 212 */     if ((this.access & 0x1000) != 0 && ((
/* 213 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
/*     */       
/* 215 */       this.cw.newUTF8("Synthetic");
/* 216 */       i += 6;
/*     */     } 
/*     */     
/* 219 */     if ((this.access & 0x20000) != 0) {
/* 220 */       this.cw.newUTF8("Deprecated");
/* 221 */       i += 6;
/*     */     } 
/* 223 */     if (this.signature != 0) {
/* 224 */       this.cw.newUTF8("Signature");
/* 225 */       i += 8;
/*     */     } 
/* 227 */     if (this.anns != null) {
/* 228 */       this.cw.newUTF8("RuntimeVisibleAnnotations");
/* 229 */       i += 8 + this.anns.getSize();
/*     */     } 
/* 231 */     if (this.ianns != null) {
/* 232 */       this.cw.newUTF8("RuntimeInvisibleAnnotations");
/* 233 */       i += 8 + this.ianns.getSize();
/*     */     } 
/* 235 */     if (this.tanns != null) {
/* 236 */       this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
/* 237 */       i += 8 + this.tanns.getSize();
/*     */     } 
/* 239 */     if (this.itanns != null) {
/* 240 */       this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
/* 241 */       i += 8 + this.itanns.getSize();
/*     */     } 
/* 243 */     if (this.attrs != null) {
/* 244 */       i += this.attrs.getSize(this.cw, null, 0, -1, -1);
/*     */     }
/* 246 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void put(ByteVector paramByteVector) {
/* 256 */     byte b = 64;
/* 257 */     int i = 0x60000 | (this.access & 0x40000) / 64;
/*     */     
/* 259 */     paramByteVector.putShort(this.access & (i ^ 0xFFFFFFFF)).putShort(this.name).putShort(this.desc);
/* 260 */     int j = 0;
/* 261 */     if (this.value != 0) {
/* 262 */       j++;
/*     */     }
/* 264 */     if ((this.access & 0x1000) != 0 && ((
/* 265 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*     */     {
/* 267 */       j++;
/*     */     }
/*     */     
/* 270 */     if ((this.access & 0x20000) != 0) {
/* 271 */       j++;
/*     */     }
/* 273 */     if (this.signature != 0) {
/* 274 */       j++;
/*     */     }
/* 276 */     if (this.anns != null) {
/* 277 */       j++;
/*     */     }
/* 279 */     if (this.ianns != null) {
/* 280 */       j++;
/*     */     }
/* 282 */     if (this.tanns != null) {
/* 283 */       j++;
/*     */     }
/* 285 */     if (this.itanns != null) {
/* 286 */       j++;
/*     */     }
/* 288 */     if (this.attrs != null) {
/* 289 */       j += this.attrs.getCount();
/*     */     }
/* 291 */     paramByteVector.putShort(j);
/* 292 */     if (this.value != 0) {
/* 293 */       paramByteVector.putShort(this.cw.newUTF8("ConstantValue"));
/* 294 */       paramByteVector.putInt(2).putShort(this.value);
/*     */     } 
/* 296 */     if ((this.access & 0x1000) != 0 && ((
/* 297 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*     */     {
/* 299 */       paramByteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
/*     */     }
/*     */     
/* 302 */     if ((this.access & 0x20000) != 0) {
/* 303 */       paramByteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
/*     */     }
/* 305 */     if (this.signature != 0) {
/* 306 */       paramByteVector.putShort(this.cw.newUTF8("Signature"));
/* 307 */       paramByteVector.putInt(2).putShort(this.signature);
/*     */     } 
/* 309 */     if (this.anns != null) {
/* 310 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
/* 311 */       this.anns.put(paramByteVector);
/*     */     } 
/* 313 */     if (this.ianns != null) {
/* 314 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
/* 315 */       this.ianns.put(paramByteVector);
/*     */     } 
/* 317 */     if (this.tanns != null) {
/* 318 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
/* 319 */       this.tanns.put(paramByteVector);
/*     */     } 
/* 321 */     if (this.itanns != null) {
/* 322 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 323 */       this.itanns.put(paramByteVector);
/*     */     } 
/* 325 */     if (this.attrs != null)
/* 326 */       this.attrs.put(this.cw, null, 0, -1, -1, paramByteVector); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\FieldWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */