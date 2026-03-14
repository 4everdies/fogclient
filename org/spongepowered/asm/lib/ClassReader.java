/*      */ package org.spongepowered.asm.lib;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*      */   static final boolean SIGNATURES = true;
/*      */   static final boolean ANNOTATIONS = true;
/*      */   static final boolean FRAMES = true;
/*      */   static final boolean WRITER = true;
/*      */   static final boolean RESIZE = true;
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   static final int EXPAND_ASM_INSNS = 256;
/*      */   public final byte[] b;
/*      */   private final int[] items;
/*      */   private final String[] strings;
/*      */   private final int maxStringLength;
/*      */   public final int header;
/*      */   
/*      */   public ClassReader(byte[] paramArrayOfbyte) {
/*  168 */     this(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassReader(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  182 */     this.b = paramArrayOfbyte;
/*      */     
/*  184 */     if (readShort(paramInt1 + 6) > 52) {
/*  185 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  188 */     this.items = new int[readUnsignedShort(paramInt1 + 8)];
/*  189 */     int i = this.items.length;
/*  190 */     this.strings = new String[i];
/*  191 */     int j = 0;
/*  192 */     int k = paramInt1 + 10;
/*  193 */     for (byte b = 1; b < i; b++) {
/*  194 */       int m; this.items[b] = k + 1;
/*      */       
/*  196 */       switch (paramArrayOfbyte[k]) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 18:
/*  204 */           m = 5;
/*      */           break;
/*      */         case 5:
/*      */         case 6:
/*  208 */           m = 9;
/*  209 */           b++;
/*      */           break;
/*      */         case 1:
/*  212 */           m = 3 + readUnsignedShort(k + 1);
/*  213 */           if (m > j) {
/*  214 */             j = m;
/*      */           }
/*      */           break;
/*      */         case 15:
/*  218 */           m = 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/*  224 */           m = 3;
/*      */           break;
/*      */       } 
/*  227 */       k += m;
/*      */     } 
/*  229 */     this.maxStringLength = j;
/*      */     
/*  231 */     this.header = k;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAccess() {
/*  244 */     return readUnsignedShort(this.header);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  256 */     return readClass(this.header + 2, new char[this.maxStringLength]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSuperName() {
/*  270 */     return readClass(this.header + 4, new char[this.maxStringLength]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getInterfaces() {
/*  283 */     int i = this.header + 6;
/*  284 */     int j = readUnsignedShort(i);
/*  285 */     String[] arrayOfString = new String[j];
/*  286 */     if (j > 0) {
/*  287 */       char[] arrayOfChar = new char[this.maxStringLength];
/*  288 */       for (byte b = 0; b < j; b++) {
/*  289 */         i += 2;
/*  290 */         arrayOfString[b] = readClass(i, arrayOfChar);
/*      */       } 
/*      */     } 
/*  293 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void copyPool(ClassWriter paramClassWriter) {
/*  304 */     char[] arrayOfChar = new char[this.maxStringLength];
/*  305 */     int i = this.items.length;
/*  306 */     Item[] arrayOfItem = new Item[i]; int j;
/*  307 */     for (j = 1; j < i; j++) {
/*  308 */       int m; String str; int k = this.items[j];
/*  309 */       byte b = this.b[k - 1];
/*  310 */       Item item = new Item(j);
/*      */       
/*  312 */       switch (b) {
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*  316 */           m = this.items[readUnsignedShort(k + 2)];
/*  317 */           item.set(b, readClass(k, arrayOfChar), readUTF8(m, arrayOfChar), 
/*  318 */               readUTF8(m + 2, arrayOfChar));
/*      */           break;
/*      */         case 3:
/*  321 */           item.set(readInt(k));
/*      */           break;
/*      */         case 4:
/*  324 */           item.set(Float.intBitsToFloat(readInt(k)));
/*      */           break;
/*      */         case 12:
/*  327 */           item.set(b, readUTF8(k, arrayOfChar), readUTF8(k + 2, arrayOfChar), null);
/*      */           break;
/*      */         
/*      */         case 5:
/*  331 */           item.set(readLong(k));
/*  332 */           j++;
/*      */           break;
/*      */         case 6:
/*  335 */           item.set(Double.longBitsToDouble(readLong(k)));
/*  336 */           j++;
/*      */           break;
/*      */         case 1:
/*  339 */           str = this.strings[j];
/*  340 */           if (str == null) {
/*  341 */             k = this.items[j];
/*  342 */             str = this.strings[j] = readUTF(k + 2, 
/*  343 */                 readUnsignedShort(k), arrayOfChar);
/*      */           } 
/*  345 */           item.set(b, str, null, null);
/*      */           break;
/*      */         
/*      */         case 15:
/*  349 */           n = this.items[readUnsignedShort(k + 1)];
/*  350 */           m = this.items[readUnsignedShort(n + 2)];
/*  351 */           item.set(20 + readByte(k), 
/*  352 */               readClass(n, arrayOfChar), 
/*  353 */               readUTF8(m, arrayOfChar), readUTF8(m + 2, arrayOfChar));
/*      */           break;
/*      */         
/*      */         case 18:
/*  357 */           if (paramClassWriter.bootstrapMethods == null) {
/*  358 */             copyBootstrapMethods(paramClassWriter, arrayOfItem, arrayOfChar);
/*      */           }
/*  360 */           m = this.items[readUnsignedShort(k + 2)];
/*  361 */           item.set(readUTF8(m, arrayOfChar), readUTF8(m + 2, arrayOfChar), 
/*  362 */               readUnsignedShort(k));
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/*  368 */           item.set(b, readUTF8(k, arrayOfChar), null, null);
/*      */           break;
/*      */       } 
/*      */       
/*  372 */       int n = item.hashCode % arrayOfItem.length;
/*  373 */       item.next = arrayOfItem[n];
/*  374 */       arrayOfItem[n] = item;
/*      */     } 
/*      */     
/*  377 */     j = this.items[1] - 1;
/*  378 */     paramClassWriter.pool.putByteArray(this.b, j, this.header - j);
/*  379 */     paramClassWriter.items = arrayOfItem;
/*  380 */     paramClassWriter.threshold = (int)(0.75D * i);
/*  381 */     paramClassWriter.index = i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void copyBootstrapMethods(ClassWriter paramClassWriter, Item[] paramArrayOfItem, char[] paramArrayOfchar) {
/*  394 */     int i = getAttributes();
/*  395 */     boolean bool = false; int j;
/*  396 */     for (j = readUnsignedShort(i); j > 0; j--) {
/*  397 */       String str = readUTF8(i + 2, paramArrayOfchar);
/*  398 */       if ("BootstrapMethods".equals(str)) {
/*  399 */         bool = true;
/*      */         break;
/*      */       } 
/*  402 */       i += 6 + readInt(i + 4);
/*      */     } 
/*  404 */     if (!bool) {
/*      */       return;
/*      */     }
/*      */     
/*  408 */     j = readUnsignedShort(i + 8); int k, m;
/*  409 */     for (k = 0, m = i + 10; k < j; k++) {
/*  410 */       int n = m - i - 10;
/*  411 */       int i1 = readConst(readUnsignedShort(m), paramArrayOfchar).hashCode();
/*  412 */       for (int i2 = readUnsignedShort(m + 2); i2 > 0; i2--) {
/*  413 */         i1 ^= readConst(readUnsignedShort(m + 4), paramArrayOfchar).hashCode();
/*  414 */         m += 2;
/*      */       } 
/*  416 */       m += 4;
/*  417 */       Item item = new Item(k);
/*  418 */       item.set(n, i1 & Integer.MAX_VALUE);
/*  419 */       int i3 = item.hashCode % paramArrayOfItem.length;
/*  420 */       item.next = paramArrayOfItem[i3];
/*  421 */       paramArrayOfItem[i3] = item;
/*      */     } 
/*  423 */     k = readInt(i + 4);
/*  424 */     ByteVector byteVector = new ByteVector(k + 62);
/*  425 */     byteVector.putByteArray(this.b, i + 10, k - 2);
/*  426 */     paramClassWriter.bootstrapMethodsCount = j;
/*  427 */     paramClassWriter.bootstrapMethods = byteVector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassReader(InputStream paramInputStream) throws IOException {
/*  439 */     this(readClass(paramInputStream, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassReader(String paramString) throws IOException {
/*  451 */     this(readClass(
/*  452 */           ClassLoader.getSystemResourceAsStream(paramString.replace('.', '/') + ".class"), true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] readClass(InputStream paramInputStream, boolean paramBoolean) throws IOException {
/*  469 */     if (paramInputStream == null) {
/*  470 */       throw new IOException("Class not found");
/*      */     }
/*      */     try {
/*  473 */       byte[] arrayOfByte = new byte[paramInputStream.available()];
/*  474 */       int i = 0;
/*      */       while (true) {
/*  476 */         int j = paramInputStream.read(arrayOfByte, i, arrayOfByte.length - i);
/*  477 */         if (j == -1) {
/*  478 */           if (i < arrayOfByte.length) {
/*  479 */             byte[] arrayOfByte1 = new byte[i];
/*  480 */             System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, i);
/*  481 */             arrayOfByte = arrayOfByte1;
/*      */           } 
/*  483 */           return arrayOfByte;
/*      */         } 
/*  485 */         i += j;
/*  486 */         if (i == arrayOfByte.length) {
/*  487 */           int k = paramInputStream.read();
/*  488 */           if (k < 0) {
/*  489 */             return arrayOfByte;
/*      */           }
/*  491 */           byte[] arrayOfByte1 = new byte[arrayOfByte.length + 1000];
/*  492 */           System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, i);
/*  493 */           arrayOfByte1[i++] = (byte)k;
/*  494 */           arrayOfByte = arrayOfByte1;
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  498 */       if (paramBoolean) {
/*  499 */         paramInputStream.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void accept(ClassVisitor paramClassVisitor, int paramInt) {
/*  521 */     accept(paramClassVisitor, new Attribute[0], paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void accept(ClassVisitor paramClassVisitor, Attribute[] paramArrayOfAttribute, int paramInt) {
/*  547 */     int i = this.header;
/*  548 */     char[] arrayOfChar = new char[this.maxStringLength];
/*      */     
/*  550 */     Context context = new Context();
/*  551 */     context.attrs = paramArrayOfAttribute;
/*  552 */     context.flags = paramInt;
/*  553 */     context.buffer = arrayOfChar;
/*      */ 
/*      */     
/*  556 */     int j = readUnsignedShort(i);
/*  557 */     String str1 = readClass(i + 2, arrayOfChar);
/*  558 */     String str2 = readClass(i + 4, arrayOfChar);
/*  559 */     String[] arrayOfString = new String[readUnsignedShort(i + 6)];
/*  560 */     i += 8;
/*  561 */     for (byte b = 0; b < arrayOfString.length; b++) {
/*  562 */       arrayOfString[b] = readClass(i, arrayOfChar);
/*  563 */       i += 2;
/*      */     } 
/*      */ 
/*      */     
/*  567 */     String str3 = null;
/*  568 */     String str4 = null;
/*  569 */     String str5 = null;
/*  570 */     String str6 = null;
/*  571 */     String str7 = null;
/*  572 */     String str8 = null;
/*  573 */     int k = 0;
/*  574 */     int m = 0;
/*  575 */     int n = 0;
/*  576 */     int i1 = 0;
/*  577 */     int i2 = 0;
/*  578 */     Attribute attribute = null;
/*      */     
/*  580 */     i = getAttributes(); int i3;
/*  581 */     for (i3 = readUnsignedShort(i); i3 > 0; i3--) {
/*  582 */       String str = readUTF8(i + 2, arrayOfChar);
/*      */ 
/*      */       
/*  585 */       if ("SourceFile".equals(str)) {
/*  586 */         str4 = readUTF8(i + 8, arrayOfChar);
/*  587 */       } else if ("InnerClasses".equals(str)) {
/*  588 */         i2 = i + 8;
/*  589 */       } else if ("EnclosingMethod".equals(str)) {
/*  590 */         str6 = readClass(i + 8, arrayOfChar);
/*  591 */         int i4 = readUnsignedShort(i + 10);
/*  592 */         if (i4 != 0) {
/*  593 */           str7 = readUTF8(this.items[i4], arrayOfChar);
/*  594 */           str8 = readUTF8(this.items[i4] + 2, arrayOfChar);
/*      */         } 
/*  596 */       } else if ("Signature".equals(str)) {
/*  597 */         str3 = readUTF8(i + 8, arrayOfChar);
/*  598 */       } else if ("RuntimeVisibleAnnotations"
/*  599 */         .equals(str)) {
/*  600 */         k = i + 8;
/*  601 */       } else if ("RuntimeVisibleTypeAnnotations"
/*  602 */         .equals(str)) {
/*  603 */         n = i + 8;
/*  604 */       } else if ("Deprecated".equals(str)) {
/*  605 */         j |= 0x20000;
/*  606 */       } else if ("Synthetic".equals(str)) {
/*  607 */         j |= 0x41000;
/*      */       }
/*  609 */       else if ("SourceDebugExtension".equals(str)) {
/*  610 */         int i4 = readInt(i + 4);
/*  611 */         str5 = readUTF(i + 8, i4, new char[i4]);
/*  612 */       } else if ("RuntimeInvisibleAnnotations"
/*  613 */         .equals(str)) {
/*  614 */         m = i + 8;
/*  615 */       } else if ("RuntimeInvisibleTypeAnnotations"
/*  616 */         .equals(str)) {
/*  617 */         i1 = i + 8;
/*  618 */       } else if ("BootstrapMethods".equals(str)) {
/*  619 */         int[] arrayOfInt = new int[readUnsignedShort(i + 8)]; byte b1; int i4;
/*  620 */         for (b1 = 0, i4 = i + 10; b1 < arrayOfInt.length; b1++) {
/*  621 */           arrayOfInt[b1] = i4;
/*  622 */           i4 += 2 + readUnsignedShort(i4 + 2) << 1;
/*      */         } 
/*  624 */         context.bootstrapMethods = arrayOfInt;
/*      */       } else {
/*  626 */         Attribute attribute1 = readAttribute(paramArrayOfAttribute, str, i + 8, 
/*  627 */             readInt(i + 4), arrayOfChar, -1, null);
/*  628 */         if (attribute1 != null) {
/*  629 */           attribute1.next = attribute;
/*  630 */           attribute = attribute1;
/*      */         } 
/*      */       } 
/*  633 */       i += 6 + readInt(i + 4);
/*      */     } 
/*      */ 
/*      */     
/*  637 */     paramClassVisitor.visit(readInt(this.items[1] - 7), j, str1, str3, str2, arrayOfString);
/*      */ 
/*      */ 
/*      */     
/*  641 */     if ((paramInt & 0x2) == 0 && (str4 != null || str5 != null))
/*      */     {
/*  643 */       paramClassVisitor.visitSource(str4, str5);
/*      */     }
/*      */ 
/*      */     
/*  647 */     if (str6 != null) {
/*  648 */       paramClassVisitor.visitOuterClass(str6, str7, str8);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  653 */     if (k != 0) {
/*  654 */       int i4; for (i3 = readUnsignedShort(k), i4 = k + 2; i3 > 0; i3--) {
/*  655 */         i4 = readAnnotationValues(i4 + 2, arrayOfChar, true, paramClassVisitor
/*  656 */             .visitAnnotation(readUTF8(i4, arrayOfChar), true));
/*      */       }
/*      */     } 
/*  659 */     if (m != 0) {
/*  660 */       int i4; for (i3 = readUnsignedShort(m), i4 = m + 2; i3 > 0; i3--) {
/*  661 */         i4 = readAnnotationValues(i4 + 2, arrayOfChar, true, paramClassVisitor
/*  662 */             .visitAnnotation(readUTF8(i4, arrayOfChar), false));
/*      */       }
/*      */     } 
/*  665 */     if (n != 0) {
/*  666 */       int i4; for (i3 = readUnsignedShort(n), i4 = n + 2; i3 > 0; i3--) {
/*  667 */         i4 = readAnnotationTarget(context, i4);
/*  668 */         i4 = readAnnotationValues(i4 + 2, arrayOfChar, true, paramClassVisitor
/*  669 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  670 */               readUTF8(i4, arrayOfChar), true));
/*      */       } 
/*      */     } 
/*  673 */     if (i1 != 0) {
/*  674 */       int i4; for (i3 = readUnsignedShort(i1), i4 = i1 + 2; i3 > 0; i3--) {
/*  675 */         i4 = readAnnotationTarget(context, i4);
/*  676 */         i4 = readAnnotationValues(i4 + 2, arrayOfChar, true, paramClassVisitor
/*  677 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  678 */               readUTF8(i4, arrayOfChar), false));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  683 */     while (attribute != null) {
/*  684 */       Attribute attribute1 = attribute.next;
/*  685 */       attribute.next = null;
/*  686 */       paramClassVisitor.visitAttribute(attribute);
/*  687 */       attribute = attribute1;
/*      */     } 
/*      */ 
/*      */     
/*  691 */     if (i2 != 0) {
/*  692 */       i3 = i2 + 2;
/*  693 */       for (int i4 = readUnsignedShort(i2); i4 > 0; i4--) {
/*  694 */         paramClassVisitor.visitInnerClass(readClass(i3, arrayOfChar), 
/*  695 */             readClass(i3 + 2, arrayOfChar), readUTF8(i3 + 4, arrayOfChar), 
/*  696 */             readUnsignedShort(i3 + 6));
/*  697 */         i3 += 8;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  702 */     i = this.header + 10 + 2 * arrayOfString.length;
/*  703 */     for (i3 = readUnsignedShort(i - 2); i3 > 0; i3--) {
/*  704 */       i = readField(paramClassVisitor, context, i);
/*      */     }
/*  706 */     i += 2;
/*  707 */     for (i3 = readUnsignedShort(i - 2); i3 > 0; i3--) {
/*  708 */       i = readMethod(paramClassVisitor, context, i);
/*      */     }
/*      */ 
/*      */     
/*  712 */     paramClassVisitor.visitEnd();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readField(ClassVisitor paramClassVisitor, Context paramContext, int paramInt) {
/*  729 */     char[] arrayOfChar = paramContext.buffer;
/*  730 */     int i = readUnsignedShort(paramInt);
/*  731 */     String str1 = readUTF8(paramInt + 2, arrayOfChar);
/*  732 */     String str2 = readUTF8(paramInt + 4, arrayOfChar);
/*  733 */     paramInt += 6;
/*      */ 
/*      */     
/*  736 */     String str3 = null;
/*  737 */     int j = 0;
/*  738 */     int k = 0;
/*  739 */     int m = 0;
/*  740 */     int n = 0;
/*  741 */     Object object = null;
/*  742 */     Attribute attribute = null;
/*      */     
/*  744 */     for (int i1 = readUnsignedShort(paramInt); i1 > 0; i1--) {
/*  745 */       String str = readUTF8(paramInt + 2, arrayOfChar);
/*      */ 
/*      */       
/*  748 */       if ("ConstantValue".equals(str)) {
/*  749 */         int i2 = readUnsignedShort(paramInt + 8);
/*  750 */         object = (i2 == 0) ? null : readConst(i2, arrayOfChar);
/*  751 */       } else if ("Signature".equals(str)) {
/*  752 */         str3 = readUTF8(paramInt + 8, arrayOfChar);
/*  753 */       } else if ("Deprecated".equals(str)) {
/*  754 */         i |= 0x20000;
/*  755 */       } else if ("Synthetic".equals(str)) {
/*  756 */         i |= 0x41000;
/*      */       }
/*  758 */       else if ("RuntimeVisibleAnnotations"
/*  759 */         .equals(str)) {
/*  760 */         j = paramInt + 8;
/*  761 */       } else if ("RuntimeVisibleTypeAnnotations"
/*  762 */         .equals(str)) {
/*  763 */         m = paramInt + 8;
/*  764 */       } else if ("RuntimeInvisibleAnnotations"
/*  765 */         .equals(str)) {
/*  766 */         k = paramInt + 8;
/*  767 */       } else if ("RuntimeInvisibleTypeAnnotations"
/*  768 */         .equals(str)) {
/*  769 */         n = paramInt + 8;
/*      */       } else {
/*  771 */         Attribute attribute1 = readAttribute(paramContext.attrs, str, paramInt + 8, 
/*  772 */             readInt(paramInt + 4), arrayOfChar, -1, null);
/*  773 */         if (attribute1 != null) {
/*  774 */           attribute1.next = attribute;
/*  775 */           attribute = attribute1;
/*      */         } 
/*      */       } 
/*  778 */       paramInt += 6 + readInt(paramInt + 4);
/*      */     } 
/*  780 */     paramInt += 2;
/*      */ 
/*      */     
/*  783 */     FieldVisitor fieldVisitor = paramClassVisitor.visitField(i, str1, str2, str3, object);
/*      */     
/*  785 */     if (fieldVisitor == null) {
/*  786 */       return paramInt;
/*      */     }
/*      */ 
/*      */     
/*  790 */     if (j != 0) {
/*  791 */       for (int i2 = readUnsignedShort(j), i3 = j + 2; i2 > 0; i2--) {
/*  792 */         i3 = readAnnotationValues(i3 + 2, arrayOfChar, true, fieldVisitor
/*  793 */             .visitAnnotation(readUTF8(i3, arrayOfChar), true));
/*      */       }
/*      */     }
/*  796 */     if (k != 0) {
/*  797 */       for (int i2 = readUnsignedShort(k), i3 = k + 2; i2 > 0; i2--) {
/*  798 */         i3 = readAnnotationValues(i3 + 2, arrayOfChar, true, fieldVisitor
/*  799 */             .visitAnnotation(readUTF8(i3, arrayOfChar), false));
/*      */       }
/*      */     }
/*  802 */     if (m != 0) {
/*  803 */       for (int i2 = readUnsignedShort(m), i3 = m + 2; i2 > 0; i2--) {
/*  804 */         i3 = readAnnotationTarget(paramContext, i3);
/*  805 */         i3 = readAnnotationValues(i3 + 2, arrayOfChar, true, fieldVisitor
/*  806 */             .visitTypeAnnotation(paramContext.typeRef, paramContext.typePath, 
/*  807 */               readUTF8(i3, arrayOfChar), true));
/*      */       } 
/*      */     }
/*  810 */     if (n != 0) {
/*  811 */       for (int i2 = readUnsignedShort(n), i3 = n + 2; i2 > 0; i2--) {
/*  812 */         i3 = readAnnotationTarget(paramContext, i3);
/*  813 */         i3 = readAnnotationValues(i3 + 2, arrayOfChar, true, fieldVisitor
/*  814 */             .visitTypeAnnotation(paramContext.typeRef, paramContext.typePath, 
/*  815 */               readUTF8(i3, arrayOfChar), false));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  820 */     while (attribute != null) {
/*  821 */       Attribute attribute1 = attribute.next;
/*  822 */       attribute.next = null;
/*  823 */       fieldVisitor.visitAttribute(attribute);
/*  824 */       attribute = attribute1;
/*      */     } 
/*      */ 
/*      */     
/*  828 */     fieldVisitor.visitEnd();
/*      */     
/*  830 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readMethod(ClassVisitor paramClassVisitor, Context paramContext, int paramInt) {
/*  847 */     char[] arrayOfChar = paramContext.buffer;
/*  848 */     paramContext.access = readUnsignedShort(paramInt);
/*  849 */     paramContext.name = readUTF8(paramInt + 2, arrayOfChar);
/*  850 */     paramContext.desc = readUTF8(paramInt + 4, arrayOfChar);
/*  851 */     paramInt += 6;
/*      */ 
/*      */     
/*  854 */     int i = 0;
/*  855 */     int j = 0;
/*  856 */     String[] arrayOfString = null;
/*  857 */     String str = null;
/*  858 */     int k = 0;
/*  859 */     int m = 0;
/*  860 */     int n = 0;
/*  861 */     int i1 = 0;
/*  862 */     int i2 = 0;
/*  863 */     int i3 = 0;
/*  864 */     int i4 = 0;
/*  865 */     int i5 = 0;
/*  866 */     int i6 = paramInt;
/*  867 */     Attribute attribute = null;
/*      */     
/*  869 */     for (int i7 = readUnsignedShort(paramInt); i7 > 0; i7--) {
/*  870 */       String str1 = readUTF8(paramInt + 2, arrayOfChar);
/*      */ 
/*      */       
/*  873 */       if ("Code".equals(str1)) {
/*  874 */         if ((paramContext.flags & 0x1) == 0) {
/*  875 */           i = paramInt + 8;
/*      */         }
/*  877 */       } else if ("Exceptions".equals(str1)) {
/*  878 */         arrayOfString = new String[readUnsignedShort(paramInt + 8)];
/*  879 */         j = paramInt + 10;
/*  880 */         for (byte b = 0; b < arrayOfString.length; b++) {
/*  881 */           arrayOfString[b] = readClass(j, arrayOfChar);
/*  882 */           j += 2;
/*      */         } 
/*  884 */       } else if ("Signature".equals(str1)) {
/*  885 */         str = readUTF8(paramInt + 8, arrayOfChar);
/*  886 */       } else if ("Deprecated".equals(str1)) {
/*  887 */         paramContext.access |= 0x20000;
/*  888 */       } else if ("RuntimeVisibleAnnotations"
/*  889 */         .equals(str1)) {
/*  890 */         m = paramInt + 8;
/*  891 */       } else if ("RuntimeVisibleTypeAnnotations"
/*  892 */         .equals(str1)) {
/*  893 */         i1 = paramInt + 8;
/*  894 */       } else if ("AnnotationDefault".equals(str1)) {
/*  895 */         i3 = paramInt + 8;
/*  896 */       } else if ("Synthetic".equals(str1)) {
/*  897 */         paramContext.access |= 0x41000;
/*      */       }
/*  899 */       else if ("RuntimeInvisibleAnnotations"
/*  900 */         .equals(str1)) {
/*  901 */         n = paramInt + 8;
/*  902 */       } else if ("RuntimeInvisibleTypeAnnotations"
/*  903 */         .equals(str1)) {
/*  904 */         i2 = paramInt + 8;
/*  905 */       } else if ("RuntimeVisibleParameterAnnotations"
/*  906 */         .equals(str1)) {
/*  907 */         i4 = paramInt + 8;
/*  908 */       } else if ("RuntimeInvisibleParameterAnnotations"
/*  909 */         .equals(str1)) {
/*  910 */         i5 = paramInt + 8;
/*  911 */       } else if ("MethodParameters".equals(str1)) {
/*  912 */         k = paramInt + 8;
/*      */       } else {
/*  914 */         Attribute attribute1 = readAttribute(paramContext.attrs, str1, paramInt + 8, 
/*  915 */             readInt(paramInt + 4), arrayOfChar, -1, null);
/*  916 */         if (attribute1 != null) {
/*  917 */           attribute1.next = attribute;
/*  918 */           attribute = attribute1;
/*      */         } 
/*      */       } 
/*  921 */       paramInt += 6 + readInt(paramInt + 4);
/*      */     } 
/*  923 */     paramInt += 2;
/*      */ 
/*      */     
/*  926 */     MethodVisitor methodVisitor = paramClassVisitor.visitMethod(paramContext.access, paramContext.name, paramContext.desc, str, arrayOfString);
/*      */     
/*  928 */     if (methodVisitor == null) {
/*  929 */       return paramInt;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  942 */     if (methodVisitor instanceof MethodWriter) {
/*  943 */       MethodWriter methodWriter = (MethodWriter)methodVisitor;
/*  944 */       if (methodWriter.cw.cr == this && str == methodWriter.signature) {
/*  945 */         boolean bool = false;
/*  946 */         if (arrayOfString == null) {
/*  947 */           bool = (methodWriter.exceptionCount == 0) ? true : false;
/*  948 */         } else if (arrayOfString.length == methodWriter.exceptionCount) {
/*  949 */           bool = true;
/*  950 */           for (int i8 = arrayOfString.length - 1; i8 >= 0; i8--) {
/*  951 */             j -= 2;
/*  952 */             if (methodWriter.exceptions[i8] != readUnsignedShort(j)) {
/*  953 */               bool = false;
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*  958 */         if (bool) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  964 */           methodWriter.classReaderOffset = i6;
/*  965 */           methodWriter.classReaderLength = paramInt - i6;
/*  966 */           return paramInt;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  972 */     if (k != 0) {
/*  973 */       int i9; for (int i8 = this.b[k] & 0xFF; i8 > 0; i8--, i9 += 4) {
/*  974 */         methodVisitor.visitParameter(readUTF8(i9, arrayOfChar), readUnsignedShort(i9 + 2));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  979 */     if (i3 != 0) {
/*  980 */       AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
/*  981 */       readAnnotationValue(i3, arrayOfChar, null, annotationVisitor);
/*  982 */       if (annotationVisitor != null) {
/*  983 */         annotationVisitor.visitEnd();
/*      */       }
/*      */     } 
/*  986 */     if (m != 0) {
/*  987 */       for (int i8 = readUnsignedShort(m), i9 = m + 2; i8 > 0; i8--) {
/*  988 */         i9 = readAnnotationValues(i9 + 2, arrayOfChar, true, methodVisitor
/*  989 */             .visitAnnotation(readUTF8(i9, arrayOfChar), true));
/*      */       }
/*      */     }
/*  992 */     if (n != 0) {
/*  993 */       for (int i8 = readUnsignedShort(n), i9 = n + 2; i8 > 0; i8--) {
/*  994 */         i9 = readAnnotationValues(i9 + 2, arrayOfChar, true, methodVisitor
/*  995 */             .visitAnnotation(readUTF8(i9, arrayOfChar), false));
/*      */       }
/*      */     }
/*  998 */     if (i1 != 0) {
/*  999 */       for (int i8 = readUnsignedShort(i1), i9 = i1 + 2; i8 > 0; i8--) {
/* 1000 */         i9 = readAnnotationTarget(paramContext, i9);
/* 1001 */         i9 = readAnnotationValues(i9 + 2, arrayOfChar, true, methodVisitor
/* 1002 */             .visitTypeAnnotation(paramContext.typeRef, paramContext.typePath, 
/* 1003 */               readUTF8(i9, arrayOfChar), true));
/*      */       } 
/*      */     }
/* 1006 */     if (i2 != 0) {
/* 1007 */       for (int i8 = readUnsignedShort(i2), i9 = i2 + 2; i8 > 0; i8--) {
/* 1008 */         i9 = readAnnotationTarget(paramContext, i9);
/* 1009 */         i9 = readAnnotationValues(i9 + 2, arrayOfChar, true, methodVisitor
/* 1010 */             .visitTypeAnnotation(paramContext.typeRef, paramContext.typePath, 
/* 1011 */               readUTF8(i9, arrayOfChar), false));
/*      */       } 
/*      */     }
/* 1014 */     if (i4 != 0) {
/* 1015 */       readParameterAnnotations(methodVisitor, paramContext, i4, true);
/*      */     }
/* 1017 */     if (i5 != 0) {
/* 1018 */       readParameterAnnotations(methodVisitor, paramContext, i5, false);
/*      */     }
/*      */ 
/*      */     
/* 1022 */     while (attribute != null) {
/* 1023 */       Attribute attribute1 = attribute.next;
/* 1024 */       attribute.next = null;
/* 1025 */       methodVisitor.visitAttribute(attribute);
/* 1026 */       attribute = attribute1;
/*      */     } 
/*      */ 
/*      */     
/* 1030 */     if (i != 0) {
/* 1031 */       methodVisitor.visitCode();
/* 1032 */       readCode(methodVisitor, paramContext, i);
/*      */     } 
/*      */ 
/*      */     
/* 1036 */     methodVisitor.visitEnd();
/*      */     
/* 1038 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readCode(MethodVisitor paramMethodVisitor, Context paramContext, int paramInt) {
/* 1053 */     byte[] arrayOfByte = this.b;
/* 1054 */     char[] arrayOfChar = paramContext.buffer;
/* 1055 */     int i = readUnsignedShort(paramInt);
/* 1056 */     int j = readUnsignedShort(paramInt + 2);
/* 1057 */     int k = readInt(paramInt + 4);
/* 1058 */     paramInt += 8;
/*      */ 
/*      */     
/* 1061 */     int m = paramInt;
/* 1062 */     int n = paramInt + k;
/* 1063 */     Label[] arrayOfLabel = paramContext.labels = new Label[k + 2];
/* 1064 */     readLabel(k + 1, arrayOfLabel);
/* 1065 */     while (paramInt < n) {
/* 1066 */       int i10, i8 = paramInt - m;
/* 1067 */       int i9 = arrayOfByte[paramInt] & 0xFF;
/* 1068 */       switch (ClassWriter.TYPE[i9]) {
/*      */         case 0:
/*      */         case 4:
/* 1071 */           paramInt++;
/*      */           continue;
/*      */         case 9:
/* 1074 */           readLabel(i8 + readShort(paramInt + 1), arrayOfLabel);
/* 1075 */           paramInt += 3;
/*      */           continue;
/*      */         case 18:
/* 1078 */           readLabel(i8 + readUnsignedShort(paramInt + 1), arrayOfLabel);
/* 1079 */           paramInt += 3;
/*      */           continue;
/*      */         case 10:
/* 1082 */           readLabel(i8 + readInt(paramInt + 1), arrayOfLabel);
/* 1083 */           paramInt += 5;
/*      */           continue;
/*      */         case 17:
/* 1086 */           i9 = arrayOfByte[paramInt + 1] & 0xFF;
/* 1087 */           if (i9 == 132) {
/* 1088 */             paramInt += 6; continue;
/*      */           } 
/* 1090 */           paramInt += 4;
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 14:
/* 1095 */           paramInt = paramInt + 4 - (i8 & 0x3);
/*      */           
/* 1097 */           readLabel(i8 + readInt(paramInt), arrayOfLabel);
/* 1098 */           for (i10 = readInt(paramInt + 8) - readInt(paramInt + 4) + 1; i10 > 0; i10--) {
/* 1099 */             readLabel(i8 + readInt(paramInt + 12), arrayOfLabel);
/* 1100 */             paramInt += 4;
/*      */           } 
/* 1102 */           paramInt += 12;
/*      */           continue;
/*      */         
/*      */         case 15:
/* 1106 */           paramInt = paramInt + 4 - (i8 & 0x3);
/*      */           
/* 1108 */           readLabel(i8 + readInt(paramInt), arrayOfLabel);
/* 1109 */           for (i10 = readInt(paramInt + 4); i10 > 0; i10--) {
/* 1110 */             readLabel(i8 + readInt(paramInt + 12), arrayOfLabel);
/* 1111 */             paramInt += 8;
/*      */           } 
/* 1113 */           paramInt += 8;
/*      */           continue;
/*      */         case 1:
/*      */         case 3:
/*      */         case 11:
/* 1118 */           paramInt += 2;
/*      */           continue;
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 12:
/*      */         case 13:
/* 1125 */           paramInt += 3;
/*      */           continue;
/*      */         case 7:
/*      */         case 8:
/* 1129 */           paramInt += 5;
/*      */           continue;
/*      */       } 
/*      */       
/* 1133 */       paramInt += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1139 */     for (int i1 = readUnsignedShort(paramInt); i1 > 0; i1--) {
/* 1140 */       Label label1 = readLabel(readUnsignedShort(paramInt + 2), arrayOfLabel);
/* 1141 */       Label label2 = readLabel(readUnsignedShort(paramInt + 4), arrayOfLabel);
/* 1142 */       Label label3 = readLabel(readUnsignedShort(paramInt + 6), arrayOfLabel);
/* 1143 */       String str = readUTF8(this.items[readUnsignedShort(paramInt + 8)], arrayOfChar);
/* 1144 */       paramMethodVisitor.visitTryCatchBlock(label1, label2, label3, str);
/* 1145 */       paramInt += 8;
/*      */     } 
/* 1147 */     paramInt += 2;
/*      */ 
/*      */     
/* 1150 */     int[] arrayOfInt1 = null;
/* 1151 */     int[] arrayOfInt2 = null;
/* 1152 */     byte b1 = 0;
/* 1153 */     byte b2 = 0;
/* 1154 */     byte b3 = -1;
/* 1155 */     byte b4 = -1;
/* 1156 */     int i2 = 0;
/* 1157 */     int i3 = 0;
/* 1158 */     boolean bool1 = true;
/* 1159 */     boolean bool2 = ((paramContext.flags & 0x8) != 0) ? true : false;
/* 1160 */     int i4 = 0;
/* 1161 */     int i5 = 0;
/* 1162 */     int i6 = 0;
/* 1163 */     Context context = null;
/* 1164 */     Attribute attribute = null;
/*      */     int i7;
/* 1166 */     for (i7 = readUnsignedShort(paramInt); i7 > 0; i7--) {
/* 1167 */       String str = readUTF8(paramInt + 2, arrayOfChar);
/* 1168 */       if ("LocalVariableTable".equals(str)) {
/* 1169 */         if ((paramContext.flags & 0x2) == 0) {
/* 1170 */           i2 = paramInt + 8;
/* 1171 */           for (int i8 = readUnsignedShort(paramInt + 8), i9 = paramInt; i8 > 0; i8--) {
/* 1172 */             int i10 = readUnsignedShort(i9 + 10);
/* 1173 */             if (arrayOfLabel[i10] == null) {
/* 1174 */               (readLabel(i10, arrayOfLabel)).status |= 0x1;
/*      */             }
/* 1176 */             i10 += readUnsignedShort(i9 + 12);
/* 1177 */             if (arrayOfLabel[i10] == null) {
/* 1178 */               (readLabel(i10, arrayOfLabel)).status |= 0x1;
/*      */             }
/* 1180 */             i9 += 10;
/*      */           } 
/*      */         } 
/* 1183 */       } else if ("LocalVariableTypeTable".equals(str)) {
/* 1184 */         i3 = paramInt + 8;
/* 1185 */       } else if ("LineNumberTable".equals(str)) {
/* 1186 */         if ((paramContext.flags & 0x2) == 0) {
/* 1187 */           for (int i8 = readUnsignedShort(paramInt + 8), i9 = paramInt; i8 > 0; i8--) {
/* 1188 */             int i10 = readUnsignedShort(i9 + 10);
/* 1189 */             if (arrayOfLabel[i10] == null) {
/* 1190 */               (readLabel(i10, arrayOfLabel)).status |= 0x1;
/*      */             }
/* 1192 */             Label label = arrayOfLabel[i10];
/* 1193 */             while (label.line > 0) {
/* 1194 */               if (label.next == null) {
/* 1195 */                 label.next = new Label();
/*      */               }
/* 1197 */               label = label.next;
/*      */             } 
/* 1199 */             label.line = readUnsignedShort(i9 + 12);
/* 1200 */             i9 += 4;
/*      */           } 
/*      */         }
/* 1203 */       } else if ("RuntimeVisibleTypeAnnotations"
/* 1204 */         .equals(str)) {
/* 1205 */         arrayOfInt1 = readTypeAnnotations(paramMethodVisitor, paramContext, paramInt + 8, true);
/*      */         
/* 1207 */         b3 = (arrayOfInt1.length == 0 || readByte(arrayOfInt1[0]) < 67) ? -1 : readUnsignedShort(arrayOfInt1[0] + 1);
/* 1208 */       } else if ("RuntimeInvisibleTypeAnnotations"
/* 1209 */         .equals(str)) {
/* 1210 */         arrayOfInt2 = readTypeAnnotations(paramMethodVisitor, paramContext, paramInt + 8, false);
/*      */         
/* 1212 */         b4 = (arrayOfInt2.length == 0 || readByte(arrayOfInt2[0]) < 67) ? -1 : readUnsignedShort(arrayOfInt2[0] + 1);
/* 1213 */       } else if ("StackMapTable".equals(str)) {
/* 1214 */         if ((paramContext.flags & 0x4) == 0) {
/* 1215 */           i4 = paramInt + 10;
/* 1216 */           i5 = readInt(paramInt + 4);
/* 1217 */           i6 = readUnsignedShort(paramInt + 8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1237 */       else if ("StackMap".equals(str)) {
/* 1238 */         if ((paramContext.flags & 0x4) == 0) {
/* 1239 */           bool1 = false;
/* 1240 */           i4 = paramInt + 10;
/* 1241 */           i5 = readInt(paramInt + 4);
/* 1242 */           i6 = readUnsignedShort(paramInt + 8);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1250 */         for (byte b = 0; b < paramContext.attrs.length; b++) {
/* 1251 */           if ((paramContext.attrs[b]).type.equals(str)) {
/* 1252 */             Attribute attribute1 = paramContext.attrs[b].read(this, paramInt + 8, 
/* 1253 */                 readInt(paramInt + 4), arrayOfChar, m - 8, arrayOfLabel);
/* 1254 */             if (attribute1 != null) {
/* 1255 */               attribute1.next = attribute;
/* 1256 */               attribute = attribute1;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1261 */       paramInt += 6 + readInt(paramInt + 4);
/*      */     } 
/* 1263 */     paramInt += 2;
/*      */ 
/*      */     
/* 1266 */     if (i4 != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1272 */       context = paramContext;
/* 1273 */       context.offset = -1;
/* 1274 */       context.mode = 0;
/* 1275 */       context.localCount = 0;
/* 1276 */       context.localDiff = 0;
/* 1277 */       context.stackCount = 0;
/* 1278 */       context.local = new Object[j];
/* 1279 */       context.stack = new Object[i];
/* 1280 */       if (bool2) {
/* 1281 */         getImplicitFrame(paramContext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1294 */       for (i7 = i4; i7 < i4 + i5 - 2; i7++) {
/* 1295 */         if (arrayOfByte[i7] == 8) {
/* 1296 */           int i8 = readUnsignedShort(i7 + 1);
/* 1297 */           if (i8 >= 0 && i8 < k && (
/* 1298 */             arrayOfByte[m + i8] & 0xFF) == 187) {
/* 1299 */             readLabel(i8, arrayOfLabel);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1305 */     if ((paramContext.flags & 0x100) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1317 */       paramMethodVisitor.visitFrame(-1, j, null, 0, null);
/*      */     }
/*      */ 
/*      */     
/* 1321 */     i7 = ((paramContext.flags & 0x100) == 0) ? -33 : 0;
/* 1322 */     paramInt = m;
/* 1323 */     while (paramInt < n) {
/* 1324 */       Label label2; int i10, i11, i12, arrayOfInt[]; String str1; Handle handle; Label[] arrayOfLabel1; String str2; int i13; byte b5; String str3; Object[] arrayOfObject; byte b6; String str4, str5; int i8 = paramInt - m;
/*      */ 
/*      */       
/* 1327 */       Label label1 = arrayOfLabel[i8];
/* 1328 */       if (label1 != null) {
/* 1329 */         Label label = label1.next;
/* 1330 */         label1.next = null;
/* 1331 */         paramMethodVisitor.visitLabel(label1);
/* 1332 */         if ((paramContext.flags & 0x2) == 0 && label1.line > 0) {
/* 1333 */           paramMethodVisitor.visitLineNumber(label1.line, label1);
/* 1334 */           while (label != null) {
/* 1335 */             paramMethodVisitor.visitLineNumber(label.line, label1);
/* 1336 */             label = label.next;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1342 */       while (context != null && (context.offset == i8 || context.offset == -1)) {
/*      */ 
/*      */ 
/*      */         
/* 1346 */         if (context.offset != -1) {
/* 1347 */           if (!bool1 || bool2) {
/* 1348 */             paramMethodVisitor.visitFrame(-1, context.localCount, context.local, context.stackCount, context.stack);
/*      */           } else {
/*      */             
/* 1351 */             paramMethodVisitor.visitFrame(context.mode, context.localDiff, context.local, context.stackCount, context.stack);
/*      */           } 
/*      */         }
/*      */         
/* 1355 */         if (i6 > 0) {
/* 1356 */           i4 = readFrame(i4, bool1, bool2, context);
/* 1357 */           i6--; continue;
/*      */         } 
/* 1359 */         context = null;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1364 */       int i9 = arrayOfByte[paramInt] & 0xFF;
/* 1365 */       switch (ClassWriter.TYPE[i9]) {
/*      */         case 0:
/* 1367 */           paramMethodVisitor.visitInsn(i9);
/* 1368 */           paramInt++;
/*      */           break;
/*      */         case 4:
/* 1371 */           if (i9 > 54) {
/* 1372 */             i9 -= 59;
/* 1373 */             paramMethodVisitor.visitVarInsn(54 + (i9 >> 2), i9 & 0x3);
/*      */           } else {
/*      */             
/* 1376 */             i9 -= 26;
/* 1377 */             paramMethodVisitor.visitVarInsn(21 + (i9 >> 2), i9 & 0x3);
/*      */           } 
/* 1379 */           paramInt++;
/*      */           break;
/*      */         case 9:
/* 1382 */           paramMethodVisitor.visitJumpInsn(i9, arrayOfLabel[i8 + readShort(paramInt + 1)]);
/* 1383 */           paramInt += 3;
/*      */           break;
/*      */         case 10:
/* 1386 */           paramMethodVisitor.visitJumpInsn(i9 + i7, arrayOfLabel[i8 + 
/* 1387 */                 readInt(paramInt + 1)]);
/* 1388 */           paramInt += 5;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 18:
/* 1394 */           i9 = (i9 < 218) ? (i9 - 49) : (i9 - 20);
/* 1395 */           label2 = arrayOfLabel[i8 + readUnsignedShort(paramInt + 1)];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1401 */           if (i9 == 167 || i9 == 168) {
/* 1402 */             paramMethodVisitor.visitJumpInsn(i9 + 33, label2);
/*      */           } else {
/* 1404 */             i9 = (i9 <= 166) ? ((i9 + 1 ^ 0x1) - 1) : (i9 ^ 0x1);
/*      */             
/* 1406 */             Label label = new Label();
/* 1407 */             paramMethodVisitor.visitJumpInsn(i9, label);
/* 1408 */             paramMethodVisitor.visitJumpInsn(200, label2);
/* 1409 */             paramMethodVisitor.visitLabel(label);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1414 */             if (i4 != 0 && (context == null || context.offset != i8 + 3))
/*      */             {
/* 1416 */               paramMethodVisitor.visitFrame(256, 0, null, 0, null);
/*      */             }
/*      */           } 
/* 1419 */           paramInt += 3;
/*      */           break;
/*      */         
/*      */         case 17:
/* 1423 */           i9 = arrayOfByte[paramInt + 1] & 0xFF;
/* 1424 */           if (i9 == 132) {
/* 1425 */             paramMethodVisitor.visitIincInsn(readUnsignedShort(paramInt + 2), readShort(paramInt + 4));
/* 1426 */             paramInt += 6; break;
/*      */           } 
/* 1428 */           paramMethodVisitor.visitVarInsn(i9, readUnsignedShort(paramInt + 2));
/* 1429 */           paramInt += 4;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 14:
/* 1434 */           paramInt = paramInt + 4 - (i8 & 0x3);
/*      */           
/* 1436 */           i10 = i8 + readInt(paramInt);
/* 1437 */           i11 = readInt(paramInt + 4);
/* 1438 */           i12 = readInt(paramInt + 8);
/* 1439 */           arrayOfLabel1 = new Label[i12 - i11 + 1];
/* 1440 */           paramInt += 12;
/* 1441 */           for (b5 = 0; b5 < arrayOfLabel1.length; b5++) {
/* 1442 */             arrayOfLabel1[b5] = arrayOfLabel[i8 + readInt(paramInt)];
/* 1443 */             paramInt += 4;
/*      */           } 
/* 1445 */           paramMethodVisitor.visitTableSwitchInsn(i11, i12, arrayOfLabel[i10], arrayOfLabel1);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 15:
/* 1450 */           paramInt = paramInt + 4 - (i8 & 0x3);
/*      */           
/* 1452 */           i10 = i8 + readInt(paramInt);
/* 1453 */           i11 = readInt(paramInt + 4);
/* 1454 */           arrayOfInt = new int[i11];
/* 1455 */           arrayOfLabel1 = new Label[i11];
/* 1456 */           paramInt += 8;
/* 1457 */           for (b5 = 0; b5 < i11; b5++) {
/* 1458 */             arrayOfInt[b5] = readInt(paramInt);
/* 1459 */             arrayOfLabel1[b5] = arrayOfLabel[i8 + readInt(paramInt + 4)];
/* 1460 */             paramInt += 8;
/*      */           } 
/* 1462 */           paramMethodVisitor.visitLookupSwitchInsn(arrayOfLabel[i10], arrayOfInt, arrayOfLabel1);
/*      */           break;
/*      */         
/*      */         case 3:
/* 1466 */           paramMethodVisitor.visitVarInsn(i9, arrayOfByte[paramInt + 1] & 0xFF);
/* 1467 */           paramInt += 2;
/*      */           break;
/*      */         case 1:
/* 1470 */           paramMethodVisitor.visitIntInsn(i9, arrayOfByte[paramInt + 1]);
/* 1471 */           paramInt += 2;
/*      */           break;
/*      */         case 2:
/* 1474 */           paramMethodVisitor.visitIntInsn(i9, readShort(paramInt + 1));
/* 1475 */           paramInt += 3;
/*      */           break;
/*      */         case 11:
/* 1478 */           paramMethodVisitor.visitLdcInsn(readConst(arrayOfByte[paramInt + 1] & 0xFF, arrayOfChar));
/* 1479 */           paramInt += 2;
/*      */           break;
/*      */         case 12:
/* 1482 */           paramMethodVisitor.visitLdcInsn(readConst(readUnsignedShort(paramInt + 1), arrayOfChar));
/* 1483 */           paramInt += 3;
/*      */           break;
/*      */         case 6:
/*      */         case 7:
/* 1487 */           i10 = this.items[readUnsignedShort(paramInt + 1)];
/* 1488 */           i11 = (arrayOfByte[i10 - 1] == 11) ? 1 : 0;
/* 1489 */           str1 = readClass(i10, arrayOfChar);
/* 1490 */           i10 = this.items[readUnsignedShort(i10 + 2)];
/* 1491 */           str2 = readUTF8(i10, arrayOfChar);
/* 1492 */           str3 = readUTF8(i10 + 2, arrayOfChar);
/* 1493 */           if (i9 < 182) {
/* 1494 */             paramMethodVisitor.visitFieldInsn(i9, str1, str2, str3);
/*      */           } else {
/* 1496 */             paramMethodVisitor.visitMethodInsn(i9, str1, str2, str3, i11);
/*      */           } 
/* 1498 */           if (i9 == 185) {
/* 1499 */             paramInt += 5; break;
/*      */           } 
/* 1501 */           paramInt += 3;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 8:
/* 1506 */           i10 = this.items[readUnsignedShort(paramInt + 1)];
/* 1507 */           i11 = paramContext.bootstrapMethods[readUnsignedShort(i10)];
/* 1508 */           handle = (Handle)readConst(readUnsignedShort(i11), arrayOfChar);
/* 1509 */           i13 = readUnsignedShort(i11 + 2);
/* 1510 */           arrayOfObject = new Object[i13];
/* 1511 */           i11 += 4;
/* 1512 */           for (b6 = 0; b6 < i13; b6++) {
/* 1513 */             arrayOfObject[b6] = readConst(readUnsignedShort(i11), arrayOfChar);
/* 1514 */             i11 += 2;
/*      */           } 
/* 1516 */           i10 = this.items[readUnsignedShort(i10 + 2)];
/* 1517 */           str4 = readUTF8(i10, arrayOfChar);
/* 1518 */           str5 = readUTF8(i10 + 2, arrayOfChar);
/* 1519 */           paramMethodVisitor.visitInvokeDynamicInsn(str4, str5, handle, arrayOfObject);
/* 1520 */           paramInt += 5;
/*      */           break;
/*      */         
/*      */         case 5:
/* 1524 */           paramMethodVisitor.visitTypeInsn(i9, readClass(paramInt + 1, arrayOfChar));
/* 1525 */           paramInt += 3;
/*      */           break;
/*      */         case 13:
/* 1528 */           paramMethodVisitor.visitIincInsn(arrayOfByte[paramInt + 1] & 0xFF, arrayOfByte[paramInt + 2]);
/* 1529 */           paramInt += 3;
/*      */           break;
/*      */         
/*      */         default:
/* 1533 */           paramMethodVisitor.visitMultiANewArrayInsn(readClass(paramInt + 1, arrayOfChar), arrayOfByte[paramInt + 3] & 0xFF);
/* 1534 */           paramInt += 4;
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1539 */       while (arrayOfInt1 != null && b1 < arrayOfInt1.length && b3 <= i8) {
/* 1540 */         if (b3 == i8) {
/* 1541 */           i10 = readAnnotationTarget(paramContext, arrayOfInt1[b1]);
/* 1542 */           readAnnotationValues(i10 + 2, arrayOfChar, true, paramMethodVisitor
/* 1543 */               .visitInsnAnnotation(paramContext.typeRef, paramContext.typePath, 
/* 1544 */                 readUTF8(i10, arrayOfChar), true));
/*      */         } 
/*      */         
/* 1547 */         b3 = (++b1 >= arrayOfInt1.length || readByte(arrayOfInt1[b1]) < 67) ? -1 : readUnsignedShort(arrayOfInt1[b1] + 1);
/*      */       } 
/* 1549 */       while (arrayOfInt2 != null && b2 < arrayOfInt2.length && b4 <= i8) {
/* 1550 */         if (b4 == i8) {
/* 1551 */           i10 = readAnnotationTarget(paramContext, arrayOfInt2[b2]);
/* 1552 */           readAnnotationValues(i10 + 2, arrayOfChar, true, paramMethodVisitor
/* 1553 */               .visitInsnAnnotation(paramContext.typeRef, paramContext.typePath, 
/* 1554 */                 readUTF8(i10, arrayOfChar), false));
/*      */         } 
/*      */ 
/*      */         
/* 1558 */         b4 = (++b2 >= arrayOfInt2.length || readByte(arrayOfInt2[b2]) < 67) ? -1 : readUnsignedShort(arrayOfInt2[b2] + 1);
/*      */       } 
/*      */     } 
/* 1561 */     if (arrayOfLabel[k] != null) {
/* 1562 */       paramMethodVisitor.visitLabel(arrayOfLabel[k]);
/*      */     }
/*      */ 
/*      */     
/* 1566 */     if ((paramContext.flags & 0x2) == 0 && i2 != 0) {
/* 1567 */       int[] arrayOfInt = null;
/* 1568 */       if (i3 != 0) {
/* 1569 */         paramInt = i3 + 2;
/* 1570 */         arrayOfInt = new int[readUnsignedShort(i3) * 3];
/* 1571 */         for (int i9 = arrayOfInt.length; i9 > 0; ) {
/* 1572 */           arrayOfInt[--i9] = paramInt + 6;
/* 1573 */           arrayOfInt[--i9] = readUnsignedShort(paramInt + 8);
/* 1574 */           arrayOfInt[--i9] = readUnsignedShort(paramInt);
/* 1575 */           paramInt += 10;
/*      */         } 
/*      */       } 
/* 1578 */       paramInt = i2 + 2;
/* 1579 */       for (int i8 = readUnsignedShort(i2); i8 > 0; i8--) {
/* 1580 */         int i9 = readUnsignedShort(paramInt);
/* 1581 */         int i10 = readUnsignedShort(paramInt + 2);
/* 1582 */         int i11 = readUnsignedShort(paramInt + 8);
/* 1583 */         String str = null;
/* 1584 */         if (arrayOfInt != null) {
/* 1585 */           for (byte b = 0; b < arrayOfInt.length; b += 3) {
/* 1586 */             if (arrayOfInt[b] == i9 && arrayOfInt[b + 1] == i11) {
/* 1587 */               str = readUTF8(arrayOfInt[b + 2], arrayOfChar);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/* 1592 */         paramMethodVisitor.visitLocalVariable(readUTF8(paramInt + 4, arrayOfChar), readUTF8(paramInt + 6, arrayOfChar), str, arrayOfLabel[i9], arrayOfLabel[i9 + i10], i11);
/*      */ 
/*      */         
/* 1595 */         paramInt += 10;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1600 */     if (arrayOfInt1 != null) {
/* 1601 */       for (byte b = 0; b < arrayOfInt1.length; b++) {
/* 1602 */         if (readByte(arrayOfInt1[b]) >> 1 == 32) {
/* 1603 */           int i8 = readAnnotationTarget(paramContext, arrayOfInt1[b]);
/* 1604 */           i8 = readAnnotationValues(i8 + 2, arrayOfChar, true, paramMethodVisitor
/* 1605 */               .visitLocalVariableAnnotation(paramContext.typeRef, paramContext.typePath, paramContext.start, paramContext.end, paramContext.index, 
/*      */                 
/* 1607 */                 readUTF8(i8, arrayOfChar), true));
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1612 */     if (arrayOfInt2 != null) {
/* 1613 */       for (byte b = 0; b < arrayOfInt2.length; b++) {
/* 1614 */         if (readByte(arrayOfInt2[b]) >> 1 == 32) {
/* 1615 */           int i8 = readAnnotationTarget(paramContext, arrayOfInt2[b]);
/* 1616 */           i8 = readAnnotationValues(i8 + 2, arrayOfChar, true, paramMethodVisitor
/* 1617 */               .visitLocalVariableAnnotation(paramContext.typeRef, paramContext.typePath, paramContext.start, paramContext.end, paramContext.index, 
/*      */                 
/* 1619 */                 readUTF8(i8, arrayOfChar), false));
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1626 */     while (attribute != null) {
/* 1627 */       Attribute attribute1 = attribute.next;
/* 1628 */       attribute.next = null;
/* 1629 */       paramMethodVisitor.visitAttribute(attribute);
/* 1630 */       attribute = attribute1;
/*      */     } 
/*      */ 
/*      */     
/* 1634 */     paramMethodVisitor.visitMaxs(i, j);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] readTypeAnnotations(MethodVisitor paramMethodVisitor, Context paramContext, int paramInt, boolean paramBoolean) {
/* 1655 */     char[] arrayOfChar = paramContext.buffer;
/* 1656 */     int[] arrayOfInt = new int[readUnsignedShort(paramInt)];
/* 1657 */     paramInt += 2;
/* 1658 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 1659 */       arrayOfInt[b] = paramInt;
/* 1660 */       int i = readInt(paramInt);
/* 1661 */       switch (i >>> 24) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 22:
/* 1665 */           paramInt += 2;
/*      */           break;
/*      */         case 19:
/*      */         case 20:
/*      */         case 21:
/* 1670 */           paramInt++;
/*      */           break;
/*      */         case 64:
/*      */         case 65:
/* 1674 */           for (j = readUnsignedShort(paramInt + 1); j > 0; j--) {
/* 1675 */             int k = readUnsignedShort(paramInt + 3);
/* 1676 */             int m = readUnsignedShort(paramInt + 5);
/* 1677 */             readLabel(k, paramContext.labels);
/* 1678 */             readLabel(k + m, paramContext.labels);
/* 1679 */             paramInt += 6;
/*      */           } 
/* 1681 */           paramInt += 3;
/*      */           break;
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/* 1688 */           paramInt += 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 1700 */           paramInt += 3;
/*      */           break;
/*      */       } 
/* 1703 */       int j = readByte(paramInt);
/* 1704 */       if (i >>> 24 == 66) {
/* 1705 */         TypePath typePath = (j == 0) ? null : new TypePath(this.b, paramInt);
/* 1706 */         paramInt += 1 + 2 * j;
/* 1707 */         paramInt = readAnnotationValues(paramInt + 2, arrayOfChar, true, paramMethodVisitor
/* 1708 */             .visitTryCatchAnnotation(i, typePath, 
/* 1709 */               readUTF8(paramInt, arrayOfChar), paramBoolean));
/*      */       } else {
/* 1711 */         paramInt = readAnnotationValues(paramInt + 3 + 2 * j, arrayOfChar, true, null);
/*      */       } 
/*      */     } 
/* 1714 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readAnnotationTarget(Context paramContext, int paramInt) {
/*      */     byte b;
/* 1732 */     int i = readInt(paramInt);
/* 1733 */     switch (i >>> 24) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 22:
/* 1737 */         i &= 0xFFFF0000;
/* 1738 */         paramInt += 2;
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/* 1743 */         i &= 0xFF000000;
/* 1744 */         paramInt++;
/*      */         break;
/*      */       case 64:
/*      */       case 65:
/* 1748 */         i &= 0xFF000000;
/* 1749 */         j = readUnsignedShort(paramInt + 1);
/* 1750 */         paramContext.start = new Label[j];
/* 1751 */         paramContext.end = new Label[j];
/* 1752 */         paramContext.index = new int[j];
/* 1753 */         paramInt += 3;
/* 1754 */         for (b = 0; b < j; b++) {
/* 1755 */           int k = readUnsignedShort(paramInt);
/* 1756 */           int m = readUnsignedShort(paramInt + 2);
/* 1757 */           paramContext.start[b] = readLabel(k, paramContext.labels);
/* 1758 */           paramContext.end[b] = readLabel(k + m, paramContext.labels);
/* 1759 */           paramContext.index[b] = readUnsignedShort(paramInt + 4);
/* 1760 */           paramInt += 6;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/* 1769 */         i &= 0xFF0000FF;
/* 1770 */         paramInt += 4;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/* 1782 */         i &= (i >>> 24 < 67) ? -256 : -16777216;
/* 1783 */         paramInt += 3;
/*      */         break;
/*      */     } 
/* 1786 */     int j = readByte(paramInt);
/* 1787 */     paramContext.typeRef = i;
/* 1788 */     paramContext.typePath = (j == 0) ? null : new TypePath(this.b, paramInt);
/* 1789 */     return paramInt + 1 + 2 * j;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readParameterAnnotations(MethodVisitor paramMethodVisitor, Context paramContext, int paramInt, boolean paramBoolean) {
/* 1808 */     int i = this.b[paramInt++] & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1815 */     int j = (Type.getArgumentTypes(paramContext.desc)).length - i;
/*      */     byte b;
/* 1817 */     for (b = 0; b < j; b++) {
/*      */       
/* 1819 */       AnnotationVisitor annotationVisitor = paramMethodVisitor.visitParameterAnnotation(b, "Ljava/lang/Synthetic;", false);
/* 1820 */       if (annotationVisitor != null) {
/* 1821 */         annotationVisitor.visitEnd();
/*      */       }
/*      */     } 
/* 1824 */     char[] arrayOfChar = paramContext.buffer;
/* 1825 */     for (; b < i + j; b++) {
/* 1826 */       int k = readUnsignedShort(paramInt);
/* 1827 */       paramInt += 2;
/* 1828 */       for (; k > 0; k--) {
/* 1829 */         AnnotationVisitor annotationVisitor = paramMethodVisitor.visitParameterAnnotation(b, readUTF8(paramInt, arrayOfChar), paramBoolean);
/* 1830 */         paramInt = readAnnotationValues(paramInt + 2, arrayOfChar, true, annotationVisitor);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readAnnotationValues(int paramInt, char[] paramArrayOfchar, boolean paramBoolean, AnnotationVisitor paramAnnotationVisitor) {
/* 1854 */     int i = readUnsignedShort(paramInt);
/* 1855 */     paramInt += 2;
/* 1856 */     if (paramBoolean) {
/* 1857 */       for (; i > 0; i--) {
/* 1858 */         paramInt = readAnnotationValue(paramInt + 2, paramArrayOfchar, readUTF8(paramInt, paramArrayOfchar), paramAnnotationVisitor);
/*      */       }
/*      */     } else {
/* 1861 */       for (; i > 0; i--) {
/* 1862 */         paramInt = readAnnotationValue(paramInt, paramArrayOfchar, null, paramAnnotationVisitor);
/*      */       }
/*      */     } 
/* 1865 */     if (paramAnnotationVisitor != null) {
/* 1866 */       paramAnnotationVisitor.visitEnd();
/*      */     }
/* 1868 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readAnnotationValue(int paramInt, char[] paramArrayOfchar, String paramString, AnnotationVisitor paramAnnotationVisitor) {
/*      */     int i;
/*      */     byte[] arrayOfByte;
/*      */     byte b;
/*      */     boolean[] arrayOfBoolean;
/*      */     short[] arrayOfShort;
/*      */     char[] arrayOfChar;
/*      */     int[] arrayOfInt;
/*      */     long[] arrayOfLong;
/*      */     float[] arrayOfFloat;
/*      */     double[] arrayOfDouble;
/* 1890 */     if (paramAnnotationVisitor == null) {
/* 1891 */       switch (this.b[paramInt] & 0xFF) {
/*      */         case 101:
/* 1893 */           return paramInt + 5;
/*      */         case 64:
/* 1895 */           return readAnnotationValues(paramInt + 3, paramArrayOfchar, true, null);
/*      */         case 91:
/* 1897 */           return readAnnotationValues(paramInt + 1, paramArrayOfchar, false, null);
/*      */       } 
/* 1899 */       return paramInt + 3;
/*      */     } 
/*      */     
/* 1902 */     switch (this.b[paramInt++] & 0xFF) {
/*      */       case 68:
/*      */       case 70:
/*      */       case 73:
/*      */       case 74:
/* 1907 */         paramAnnotationVisitor.visit(paramString, readConst(readUnsignedShort(paramInt), paramArrayOfchar));
/* 1908 */         paramInt += 2;
/*      */         break;
/*      */       case 66:
/* 1911 */         paramAnnotationVisitor.visit(paramString, Byte.valueOf((byte)readInt(this.items[readUnsignedShort(paramInt)])));
/* 1912 */         paramInt += 2;
/*      */         break;
/*      */       case 90:
/* 1915 */         paramAnnotationVisitor.visit(paramString, 
/* 1916 */             (readInt(this.items[readUnsignedShort(paramInt)]) == 0) ? Boolean.FALSE : Boolean.TRUE);
/*      */         
/* 1918 */         paramInt += 2;
/*      */         break;
/*      */       case 83:
/* 1921 */         paramAnnotationVisitor.visit(paramString, Short.valueOf((short)readInt(this.items[readUnsignedShort(paramInt)])));
/* 1922 */         paramInt += 2;
/*      */         break;
/*      */       case 67:
/* 1925 */         paramAnnotationVisitor.visit(paramString, Character.valueOf((char)readInt(this.items[readUnsignedShort(paramInt)])));
/* 1926 */         paramInt += 2;
/*      */         break;
/*      */       case 115:
/* 1929 */         paramAnnotationVisitor.visit(paramString, readUTF8(paramInt, paramArrayOfchar));
/* 1930 */         paramInt += 2;
/*      */         break;
/*      */       case 101:
/* 1933 */         paramAnnotationVisitor.visitEnum(paramString, readUTF8(paramInt, paramArrayOfchar), readUTF8(paramInt + 2, paramArrayOfchar));
/* 1934 */         paramInt += 4;
/*      */         break;
/*      */       case 99:
/* 1937 */         paramAnnotationVisitor.visit(paramString, Type.getType(readUTF8(paramInt, paramArrayOfchar)));
/* 1938 */         paramInt += 2;
/*      */         break;
/*      */       case 64:
/* 1941 */         paramInt = readAnnotationValues(paramInt + 2, paramArrayOfchar, true, paramAnnotationVisitor
/* 1942 */             .visitAnnotation(paramString, readUTF8(paramInt, paramArrayOfchar)));
/*      */         break;
/*      */       case 91:
/* 1945 */         i = readUnsignedShort(paramInt);
/* 1946 */         paramInt += 2;
/* 1947 */         if (i == 0) {
/* 1948 */           return readAnnotationValues(paramInt - 2, paramArrayOfchar, false, paramAnnotationVisitor
/* 1949 */               .visitArray(paramString));
/*      */         }
/* 1951 */         switch (this.b[paramInt++] & 0xFF) {
/*      */           case 66:
/* 1953 */             arrayOfByte = new byte[i];
/* 1954 */             for (b = 0; b < i; b++) {
/* 1955 */               arrayOfByte[b] = (byte)readInt(this.items[readUnsignedShort(paramInt)]);
/* 1956 */               paramInt += 3;
/*      */             } 
/* 1958 */             paramAnnotationVisitor.visit(paramString, arrayOfByte);
/* 1959 */             paramInt--;
/*      */             break;
/*      */           case 90:
/* 1962 */             arrayOfBoolean = new boolean[i];
/* 1963 */             for (b = 0; b < i; b++) {
/* 1964 */               arrayOfBoolean[b] = (readInt(this.items[readUnsignedShort(paramInt)]) != 0);
/* 1965 */               paramInt += 3;
/*      */             } 
/* 1967 */             paramAnnotationVisitor.visit(paramString, arrayOfBoolean);
/* 1968 */             paramInt--;
/*      */             break;
/*      */           case 83:
/* 1971 */             arrayOfShort = new short[i];
/* 1972 */             for (b = 0; b < i; b++) {
/* 1973 */               arrayOfShort[b] = (short)readInt(this.items[readUnsignedShort(paramInt)]);
/* 1974 */               paramInt += 3;
/*      */             } 
/* 1976 */             paramAnnotationVisitor.visit(paramString, arrayOfShort);
/* 1977 */             paramInt--;
/*      */             break;
/*      */           case 67:
/* 1980 */             arrayOfChar = new char[i];
/* 1981 */             for (b = 0; b < i; b++) {
/* 1982 */               arrayOfChar[b] = (char)readInt(this.items[readUnsignedShort(paramInt)]);
/* 1983 */               paramInt += 3;
/*      */             } 
/* 1985 */             paramAnnotationVisitor.visit(paramString, arrayOfChar);
/* 1986 */             paramInt--;
/*      */             break;
/*      */           case 73:
/* 1989 */             arrayOfInt = new int[i];
/* 1990 */             for (b = 0; b < i; b++) {
/* 1991 */               arrayOfInt[b] = readInt(this.items[readUnsignedShort(paramInt)]);
/* 1992 */               paramInt += 3;
/*      */             } 
/* 1994 */             paramAnnotationVisitor.visit(paramString, arrayOfInt);
/* 1995 */             paramInt--;
/*      */             break;
/*      */           case 74:
/* 1998 */             arrayOfLong = new long[i];
/* 1999 */             for (b = 0; b < i; b++) {
/* 2000 */               arrayOfLong[b] = readLong(this.items[readUnsignedShort(paramInt)]);
/* 2001 */               paramInt += 3;
/*      */             } 
/* 2003 */             paramAnnotationVisitor.visit(paramString, arrayOfLong);
/* 2004 */             paramInt--;
/*      */             break;
/*      */           case 70:
/* 2007 */             arrayOfFloat = new float[i];
/* 2008 */             for (b = 0; b < i; b++) {
/* 2009 */               arrayOfFloat[b] = 
/* 2010 */                 Float.intBitsToFloat(readInt(this.items[readUnsignedShort(paramInt)]));
/* 2011 */               paramInt += 3;
/*      */             } 
/* 2013 */             paramAnnotationVisitor.visit(paramString, arrayOfFloat);
/* 2014 */             paramInt--;
/*      */             break;
/*      */           case 68:
/* 2017 */             arrayOfDouble = new double[i];
/* 2018 */             for (b = 0; b < i; b++) {
/* 2019 */               arrayOfDouble[b] = 
/* 2020 */                 Double.longBitsToDouble(readLong(this.items[readUnsignedShort(paramInt)]));
/* 2021 */               paramInt += 3;
/*      */             } 
/* 2023 */             paramAnnotationVisitor.visit(paramString, arrayOfDouble);
/* 2024 */             paramInt--;
/*      */             break;
/*      */         } 
/* 2027 */         paramInt = readAnnotationValues(paramInt - 3, paramArrayOfchar, false, paramAnnotationVisitor.visitArray(paramString));
/*      */         break;
/*      */     } 
/* 2030 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getImplicitFrame(Context paramContext) {
/* 2041 */     String str = paramContext.desc;
/* 2042 */     Object[] arrayOfObject = paramContext.local;
/* 2043 */     byte b1 = 0;
/* 2044 */     if ((paramContext.access & 0x8) == 0) {
/* 2045 */       if ("<init>".equals(paramContext.name)) {
/* 2046 */         arrayOfObject[b1++] = Opcodes.UNINITIALIZED_THIS;
/*      */       } else {
/* 2048 */         arrayOfObject[b1++] = readClass(this.header + 2, paramContext.buffer);
/*      */       } 
/*      */     }
/* 2051 */     byte b2 = 1;
/*      */     while (true) {
/* 2053 */       byte b = b2;
/* 2054 */       switch (str.charAt(b2++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 2060 */           arrayOfObject[b1++] = Opcodes.INTEGER;
/*      */           continue;
/*      */         case 'F':
/* 2063 */           arrayOfObject[b1++] = Opcodes.FLOAT;
/*      */           continue;
/*      */         case 'J':
/* 2066 */           arrayOfObject[b1++] = Opcodes.LONG;
/*      */           continue;
/*      */         case 'D':
/* 2069 */           arrayOfObject[b1++] = Opcodes.DOUBLE;
/*      */           continue;
/*      */         case '[':
/* 2072 */           while (str.charAt(b2) == '[') {
/* 2073 */             b2++;
/*      */           }
/* 2075 */           if (str.charAt(b2) == 'L') {
/* 2076 */             b2++;
/* 2077 */             while (str.charAt(b2) != ';') {
/* 2078 */               b2++;
/*      */             }
/*      */           } 
/* 2081 */           arrayOfObject[b1++] = str.substring(b, ++b2);
/*      */           continue;
/*      */         case 'L':
/* 2084 */           while (str.charAt(b2) != ';') {
/* 2085 */             b2++;
/*      */           }
/* 2087 */           arrayOfObject[b1++] = str.substring(b + 1, b2++);
/*      */           continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 2093 */     paramContext.localCount = b1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readFrame(int paramInt, boolean paramBoolean1, boolean paramBoolean2, Context paramContext) {
/*      */     char c;
/*      */     int i;
/* 2112 */     char[] arrayOfChar = paramContext.buffer;
/* 2113 */     Label[] arrayOfLabel = paramContext.labels;
/*      */ 
/*      */     
/* 2116 */     if (paramBoolean1) {
/* 2117 */       c = this.b[paramInt++] & 0xFF;
/*      */     } else {
/* 2119 */       c = 'ÿ';
/* 2120 */       paramContext.offset = -1;
/*      */     } 
/* 2122 */     paramContext.localDiff = 0;
/* 2123 */     if (c < '@') {
/* 2124 */       i = c;
/* 2125 */       paramContext.mode = 3;
/* 2126 */       paramContext.stackCount = 0;
/* 2127 */     } else if (c < '') {
/* 2128 */       i = c - 64;
/* 2129 */       paramInt = readFrameType(paramContext.stack, 0, paramInt, arrayOfChar, arrayOfLabel);
/* 2130 */       paramContext.mode = 4;
/* 2131 */       paramContext.stackCount = 1;
/*      */     } else {
/* 2133 */       i = readUnsignedShort(paramInt);
/* 2134 */       paramInt += 2;
/* 2135 */       if (c == '÷') {
/* 2136 */         paramInt = readFrameType(paramContext.stack, 0, paramInt, arrayOfChar, arrayOfLabel);
/* 2137 */         paramContext.mode = 4;
/* 2138 */         paramContext.stackCount = 1;
/* 2139 */       } else if (c >= 'ø' && c < 'û') {
/*      */         
/* 2141 */         paramContext.mode = 2;
/* 2142 */         paramContext.localDiff = 251 - c;
/* 2143 */         paramContext.localCount -= paramContext.localDiff;
/* 2144 */         paramContext.stackCount = 0;
/* 2145 */       } else if (c == 'û') {
/* 2146 */         paramContext.mode = 3;
/* 2147 */         paramContext.stackCount = 0;
/* 2148 */       } else if (c < 'ÿ') {
/* 2149 */         byte b = paramBoolean2 ? paramContext.localCount : 0;
/* 2150 */         for (int j = c - 251; j > 0; j--) {
/* 2151 */           paramInt = readFrameType(paramContext.local, b++, paramInt, arrayOfChar, arrayOfLabel);
/*      */         }
/*      */         
/* 2154 */         paramContext.mode = 1;
/* 2155 */         paramContext.localDiff = c - 251;
/* 2156 */         paramContext.localCount += paramContext.localDiff;
/* 2157 */         paramContext.stackCount = 0;
/*      */       } else {
/* 2159 */         paramContext.mode = 0;
/* 2160 */         int j = readUnsignedShort(paramInt);
/* 2161 */         paramInt += 2;
/* 2162 */         paramContext.localDiff = j;
/* 2163 */         paramContext.localCount = j; byte b;
/* 2164 */         for (b = 0; j > 0; j--) {
/* 2165 */           paramInt = readFrameType(paramContext.local, b++, paramInt, arrayOfChar, arrayOfLabel);
/*      */         }
/*      */         
/* 2168 */         j = readUnsignedShort(paramInt);
/* 2169 */         paramInt += 2;
/* 2170 */         paramContext.stackCount = j;
/* 2171 */         for (b = 0; j > 0; j--) {
/* 2172 */           paramInt = readFrameType(paramContext.stack, b++, paramInt, arrayOfChar, arrayOfLabel);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2177 */     paramContext.offset += i + 1;
/* 2178 */     readLabel(paramContext.offset, arrayOfLabel);
/* 2179 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readFrameType(Object[] paramArrayOfObject, int paramInt1, int paramInt2, char[] paramArrayOfchar, Label[] paramArrayOfLabel) {
/* 2203 */     int i = this.b[paramInt2++] & 0xFF;
/* 2204 */     switch (i)
/*      */     { case 0:
/* 2206 */         paramArrayOfObject[paramInt1] = Opcodes.TOP;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2234 */         return paramInt2;case 1: paramArrayOfObject[paramInt1] = Opcodes.INTEGER; return paramInt2;case 2: paramArrayOfObject[paramInt1] = Opcodes.FLOAT; return paramInt2;case 3: paramArrayOfObject[paramInt1] = Opcodes.DOUBLE; return paramInt2;case 4: paramArrayOfObject[paramInt1] = Opcodes.LONG; return paramInt2;case 5: paramArrayOfObject[paramInt1] = Opcodes.NULL; return paramInt2;case 6: paramArrayOfObject[paramInt1] = Opcodes.UNINITIALIZED_THIS; return paramInt2;case 7: paramArrayOfObject[paramInt1] = readClass(paramInt2, paramArrayOfchar); paramInt2 += 2; return paramInt2; }  paramArrayOfObject[paramInt1] = readLabel(readUnsignedShort(paramInt2), paramArrayOfLabel); paramInt2 += 2; return paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Label readLabel(int paramInt, Label[] paramArrayOfLabel) {
/* 2251 */     if (paramArrayOfLabel[paramInt] == null) {
/* 2252 */       paramArrayOfLabel[paramInt] = new Label();
/*      */     }
/* 2254 */     return paramArrayOfLabel[paramInt];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getAttributes() {
/* 2264 */     int i = this.header + 8 + readUnsignedShort(this.header + 6) * 2;
/*      */     int j;
/* 2266 */     for (j = readUnsignedShort(i); j > 0; j--) {
/* 2267 */       for (int k = readUnsignedShort(i + 8); k > 0; k--) {
/* 2268 */         i += 6 + readInt(i + 12);
/*      */       }
/* 2270 */       i += 8;
/*      */     } 
/* 2272 */     i += 2;
/* 2273 */     for (j = readUnsignedShort(i); j > 0; j--) {
/* 2274 */       for (int k = readUnsignedShort(i + 8); k > 0; k--) {
/* 2275 */         i += 6 + readInt(i + 12);
/*      */       }
/* 2277 */       i += 8;
/*      */     } 
/*      */     
/* 2280 */     return i + 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute readAttribute(Attribute[] paramArrayOfAttribute, String paramString, int paramInt1, int paramInt2, char[] paramArrayOfchar, int paramInt3, Label[] paramArrayOfLabel) {
/* 2319 */     for (byte b = 0; b < paramArrayOfAttribute.length; b++) {
/* 2320 */       if ((paramArrayOfAttribute[b]).type.equals(paramString)) {
/* 2321 */         return paramArrayOfAttribute[b].read(this, paramInt1, paramInt2, paramArrayOfchar, paramInt3, paramArrayOfLabel);
/*      */       }
/*      */     } 
/* 2324 */     return (new Attribute(paramString)).read(this, paramInt1, paramInt2, null, -1, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getItemCount() {
/* 2337 */     return this.items.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getItem(int paramInt) {
/* 2351 */     return this.items[paramInt];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStringLength() {
/* 2362 */     return this.maxStringLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readByte(int paramInt) {
/* 2375 */     return this.b[paramInt] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readUnsignedShort(int paramInt) {
/* 2388 */     byte[] arrayOfByte = this.b;
/* 2389 */     return (arrayOfByte[paramInt] & 0xFF) << 8 | arrayOfByte[paramInt + 1] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short readShort(int paramInt) {
/* 2402 */     byte[] arrayOfByte = this.b;
/* 2403 */     return (short)((arrayOfByte[paramInt] & 0xFF) << 8 | arrayOfByte[paramInt + 1] & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readInt(int paramInt) {
/* 2416 */     byte[] arrayOfByte = this.b;
/* 2417 */     return (arrayOfByte[paramInt] & 0xFF) << 24 | (arrayOfByte[paramInt + 1] & 0xFF) << 16 | (arrayOfByte[paramInt + 2] & 0xFF) << 8 | arrayOfByte[paramInt + 3] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long readLong(int paramInt) {
/* 2431 */     long l1 = readInt(paramInt);
/* 2432 */     long l2 = readInt(paramInt + 4) & 0xFFFFFFFFL;
/* 2433 */     return l1 << 32L | l2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String readUTF8(int paramInt, char[] paramArrayOfchar) {
/* 2450 */     int i = readUnsignedShort(paramInt);
/* 2451 */     if (paramInt == 0 || i == 0) {
/* 2452 */       return null;
/*      */     }
/* 2454 */     String str = this.strings[i];
/* 2455 */     if (str != null) {
/* 2456 */       return str;
/*      */     }
/* 2458 */     paramInt = this.items[i];
/* 2459 */     this.strings[i] = readUTF(paramInt + 2, readUnsignedShort(paramInt), paramArrayOfchar); return readUTF(paramInt + 2, readUnsignedShort(paramInt), paramArrayOfchar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String readUTF(int paramInt1, int paramInt2, char[] paramArrayOfchar) {
/* 2475 */     int i = paramInt1 + paramInt2;
/* 2476 */     byte[] arrayOfByte = this.b;
/* 2477 */     byte b1 = 0;
/*      */     
/* 2479 */     byte b2 = 0;
/* 2480 */     char c = Character.MIN_VALUE;
/* 2481 */     while (paramInt1 < i) {
/* 2482 */       int j; byte b = arrayOfByte[paramInt1++];
/* 2483 */       switch (b2) {
/*      */         case false:
/* 2485 */           j = b & 0xFF;
/* 2486 */           if (j < 128) {
/* 2487 */             paramArrayOfchar[b1++] = (char)j; continue;
/* 2488 */           }  if (j < 224 && j > 191) {
/* 2489 */             c = (char)(j & 0x1F);
/* 2490 */             b2 = 1; continue;
/*      */           } 
/* 2492 */           c = (char)(j & 0xF);
/* 2493 */           b2 = 2;
/*      */ 
/*      */ 
/*      */         
/*      */         case true:
/* 2498 */           paramArrayOfchar[b1++] = (char)(c << 6 | j & 0x3F);
/* 2499 */           b2 = 0;
/*      */ 
/*      */         
/*      */         case true:
/* 2503 */           c = (char)(c << 6 | j & 0x3F);
/* 2504 */           b2 = 1;
/*      */       } 
/*      */     
/*      */     } 
/* 2508 */     return new String(paramArrayOfchar, 0, b1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String readClass(int paramInt, char[] paramArrayOfchar) {
/* 2528 */     return readUTF8(this.items[readUnsignedShort(paramInt)], paramArrayOfchar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object readConst(int paramInt, char[] paramArrayOfchar) {
/* 2546 */     int i = this.items[paramInt];
/* 2547 */     switch (this.b[i - 1]) {
/*      */       case 3:
/* 2549 */         return Integer.valueOf(readInt(i));
/*      */       case 4:
/* 2551 */         return Float.valueOf(Float.intBitsToFloat(readInt(i)));
/*      */       case 5:
/* 2553 */         return Long.valueOf(readLong(i));
/*      */       case 6:
/* 2555 */         return Double.valueOf(Double.longBitsToDouble(readLong(i)));
/*      */       case 7:
/* 2557 */         return Type.getObjectType(readUTF8(i, paramArrayOfchar));
/*      */       case 8:
/* 2559 */         return readUTF8(i, paramArrayOfchar);
/*      */       case 16:
/* 2561 */         return Type.getMethodType(readUTF8(i, paramArrayOfchar));
/*      */     } 
/* 2563 */     int j = readByte(i);
/* 2564 */     int[] arrayOfInt = this.items;
/* 2565 */     int k = arrayOfInt[readUnsignedShort(i + 1)];
/* 2566 */     boolean bool = (this.b[k - 1] == 11) ? true : false;
/* 2567 */     String str1 = readClass(k, paramArrayOfchar);
/* 2568 */     k = arrayOfInt[readUnsignedShort(k + 2)];
/* 2569 */     String str2 = readUTF8(k, paramArrayOfchar);
/* 2570 */     String str3 = readUTF8(k + 2, paramArrayOfchar);
/* 2571 */     return new Handle(j, str1, str2, str3, bool);
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\ClassReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */