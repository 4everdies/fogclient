/*     */ package org.spongepowered.asm.lib;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Type
/*     */ {
/*     */   public static final int VOID = 0;
/*     */   public static final int BOOLEAN = 1;
/*     */   public static final int CHAR = 2;
/*     */   public static final int BYTE = 3;
/*     */   public static final int SHORT = 4;
/*     */   public static final int INT = 5;
/*     */   public static final int FLOAT = 6;
/*     */   public static final int LONG = 7;
/*     */   public static final int DOUBLE = 8;
/*     */   public static final int ARRAY = 9;
/*     */   public static final int OBJECT = 10;
/*     */   public static final int METHOD = 11;
/* 107 */   public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final char[] buf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int off;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int len;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type(int paramInt1, char[] paramArrayOfchar, int paramInt2, int paramInt3) {
/* 203 */     this.sort = paramInt1;
/* 204 */     this.buf = paramArrayOfchar;
/* 205 */     this.off = paramInt2;
/* 206 */     this.len = paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(String paramString) {
/* 217 */     return getType(paramString.toCharArray(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getObjectType(String paramString) {
/* 228 */     char[] arrayOfChar = paramString.toCharArray();
/* 229 */     return new Type((arrayOfChar[0] == '[') ? 9 : 10, arrayOfChar, 0, arrayOfChar.length);
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
/*     */   public static Type getMethodType(String paramString) {
/* 241 */     return getType(paramString.toCharArray(), 0);
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
/*     */   public static Type getMethodType(Type paramType, Type... paramVarArgs) {
/* 257 */     return getType(getMethodDescriptor(paramType, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Class<?> paramClass) {
/* 268 */     if (paramClass.isPrimitive()) {
/* 269 */       if (paramClass == int.class)
/* 270 */         return INT_TYPE; 
/* 271 */       if (paramClass == void.class)
/* 272 */         return VOID_TYPE; 
/* 273 */       if (paramClass == boolean.class)
/* 274 */         return BOOLEAN_TYPE; 
/* 275 */       if (paramClass == byte.class)
/* 276 */         return BYTE_TYPE; 
/* 277 */       if (paramClass == char.class)
/* 278 */         return CHAR_TYPE; 
/* 279 */       if (paramClass == short.class)
/* 280 */         return SHORT_TYPE; 
/* 281 */       if (paramClass == double.class)
/* 282 */         return DOUBLE_TYPE; 
/* 283 */       if (paramClass == float.class) {
/* 284 */         return FLOAT_TYPE;
/*     */       }
/* 286 */       return LONG_TYPE;
/*     */     } 
/*     */     
/* 289 */     return getType(getDescriptor(paramClass));
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
/*     */   public static Type getType(Constructor<?> paramConstructor) {
/* 301 */     return getType(getConstructorDescriptor(paramConstructor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Method paramMethod) {
/* 312 */     return getType(getMethodDescriptor(paramMethod));
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
/*     */   public static Type[] getArgumentTypes(String paramString) {
/* 325 */     char[] arrayOfChar = paramString.toCharArray();
/* 326 */     int i = 1;
/* 327 */     byte b = 0;
/*     */     while (true) {
/* 329 */       char c = arrayOfChar[i++];
/* 330 */       if (c == ')')
/*     */         break; 
/* 332 */       if (c == 'L') {
/* 333 */         while (arrayOfChar[i++] != ';');
/*     */         
/* 335 */         b++; continue;
/* 336 */       }  if (c != '[') {
/* 337 */         b++;
/*     */       }
/*     */     } 
/* 340 */     Type[] arrayOfType = new Type[b];
/* 341 */     i = 1;
/* 342 */     b = 0;
/* 343 */     while (arrayOfChar[i] != ')') {
/* 344 */       arrayOfType[b] = getType(arrayOfChar, i);
/* 345 */       i += (arrayOfType[b]).len + (((arrayOfType[b]).sort == 10) ? 2 : 0);
/* 346 */       b++;
/*     */     } 
/* 348 */     return arrayOfType;
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
/*     */   public static Type[] getArgumentTypes(Method paramMethod) {
/* 361 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 362 */     Type[] arrayOfType = new Type[arrayOfClass.length];
/* 363 */     for (int i = arrayOfClass.length - 1; i >= 0; i--) {
/* 364 */       arrayOfType[i] = getType(arrayOfClass[i]);
/*     */     }
/* 366 */     return arrayOfType;
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
/*     */   public static Type getReturnType(String paramString) {
/* 379 */     char[] arrayOfChar = paramString.toCharArray();
/* 380 */     byte b = 1;
/*     */     while (true) {
/* 382 */       char c = arrayOfChar[b++];
/* 383 */       if (c == ')')
/* 384 */         return getType(arrayOfChar, b); 
/* 385 */       if (c == 'L') {
/* 386 */         while (arrayOfChar[b++] != ';');
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
/*     */   public static Type getReturnType(Method paramMethod) {
/* 402 */     return getType(paramMethod.getReturnType());
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
/*     */   public static int getArgumentsAndReturnSizes(String paramString) {
/* 417 */     byte b1 = 1;
/* 418 */     byte b2 = 1;
/*     */     while (true) {
/* 420 */       char c = paramString.charAt(b2++);
/* 421 */       if (c == ')') {
/* 422 */         c = paramString.charAt(b2);
/* 423 */         return b1 << 2 | ((c == 'V') ? 0 : ((c == 'D' || c == 'J') ? 2 : 1));
/*     */       } 
/* 425 */       if (c == 'L') {
/* 426 */         while (paramString.charAt(b2++) != ';');
/*     */         
/* 428 */         b1++; continue;
/* 429 */       }  if (c == '[') {
/* 430 */         while ((c = paramString.charAt(b2)) == '[') {
/* 431 */           b2++;
/*     */         }
/* 433 */         if (c == 'D' || c == 'J')
/* 434 */           b1--;  continue;
/*     */       } 
/* 436 */       if (c == 'D' || c == 'J') {
/* 437 */         b1 += 2; continue;
/*     */       } 
/* 439 */       b1++;
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
/*     */   private static Type getType(char[] paramArrayOfchar, int paramInt) {
/*     */     byte b;
/* 457 */     switch (paramArrayOfchar[paramInt]) {
/*     */       case 'V':
/* 459 */         return VOID_TYPE;
/*     */       case 'Z':
/* 461 */         return BOOLEAN_TYPE;
/*     */       case 'C':
/* 463 */         return CHAR_TYPE;
/*     */       case 'B':
/* 465 */         return BYTE_TYPE;
/*     */       case 'S':
/* 467 */         return SHORT_TYPE;
/*     */       case 'I':
/* 469 */         return INT_TYPE;
/*     */       case 'F':
/* 471 */         return FLOAT_TYPE;
/*     */       case 'J':
/* 473 */         return LONG_TYPE;
/*     */       case 'D':
/* 475 */         return DOUBLE_TYPE;
/*     */       case '[':
/* 477 */         b = 1;
/* 478 */         while (paramArrayOfchar[paramInt + b] == '[') {
/* 479 */           b++;
/*     */         }
/* 481 */         if (paramArrayOfchar[paramInt + b] == 'L') {
/* 482 */           b++;
/* 483 */           while (paramArrayOfchar[paramInt + b] != ';') {
/* 484 */             b++;
/*     */           }
/*     */         } 
/* 487 */         return new Type(9, paramArrayOfchar, paramInt, b + 1);
/*     */       case 'L':
/* 489 */         b = 1;
/* 490 */         while (paramArrayOfchar[paramInt + b] != ';') {
/* 491 */           b++;
/*     */         }
/* 493 */         return new Type(10, paramArrayOfchar, paramInt + 1, b - 1);
/*     */     } 
/*     */     
/* 496 */     return new Type(11, paramArrayOfchar, paramInt, paramArrayOfchar.length - paramInt);
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
/*     */   public int getSort() {
/* 514 */     return this.sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/* 524 */     byte b = 1;
/* 525 */     while (this.buf[this.off + b] == '[') {
/* 526 */       b++;
/*     */     }
/* 528 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getElementType() {
/* 538 */     return getType(this.buf, this.off + getDimensions());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*     */     StringBuilder stringBuilder;
/*     */     int i;
/* 548 */     switch (this.sort) {
/*     */       case 0:
/* 550 */         return "void";
/*     */       case 1:
/* 552 */         return "boolean";
/*     */       case 2:
/* 554 */         return "char";
/*     */       case 3:
/* 556 */         return "byte";
/*     */       case 4:
/* 558 */         return "short";
/*     */       case 5:
/* 560 */         return "int";
/*     */       case 6:
/* 562 */         return "float";
/*     */       case 7:
/* 564 */         return "long";
/*     */       case 8:
/* 566 */         return "double";
/*     */       case 9:
/* 568 */         stringBuilder = new StringBuilder(getElementType().getClassName());
/* 569 */         for (i = getDimensions(); i > 0; i--) {
/* 570 */           stringBuilder.append("[]");
/*     */         }
/* 572 */         return stringBuilder.toString();
/*     */       case 10:
/* 574 */         return (new String(this.buf, this.off, this.len)).replace('/', '.');
/*     */     } 
/* 576 */     return null;
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
/*     */   public String getInternalName() {
/* 589 */     return new String(this.buf, this.off, this.len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] getArgumentTypes() {
/* 599 */     return getArgumentTypes(getDescriptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getReturnType() {
/* 609 */     return getReturnType(getDescriptor());
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
/*     */   public int getArgumentsAndReturnSizes() {
/* 624 */     return getArgumentsAndReturnSizes(getDescriptor());
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
/*     */   public String getDescriptor() {
/* 637 */     StringBuilder stringBuilder = new StringBuilder();
/* 638 */     getDescriptor(stringBuilder);
/* 639 */     return stringBuilder.toString();
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
/*     */   public static String getMethodDescriptor(Type paramType, Type... paramVarArgs) {
/* 655 */     StringBuilder stringBuilder = new StringBuilder();
/* 656 */     stringBuilder.append('(');
/* 657 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 658 */       paramVarArgs[b].getDescriptor(stringBuilder);
/*     */     }
/* 660 */     stringBuilder.append(')');
/* 661 */     paramType.getDescriptor(stringBuilder);
/* 662 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getDescriptor(StringBuilder paramStringBuilder) {
/* 673 */     if (this.buf == null) {
/*     */ 
/*     */       
/* 676 */       paramStringBuilder.append((char)((this.off & 0xFF000000) >>> 24));
/* 677 */     } else if (this.sort == 10) {
/* 678 */       paramStringBuilder.append('L');
/* 679 */       paramStringBuilder.append(this.buf, this.off, this.len);
/* 680 */       paramStringBuilder.append(';');
/*     */     } else {
/* 682 */       paramStringBuilder.append(this.buf, this.off, this.len);
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
/*     */   public static String getInternalName(Class<?> paramClass) {
/* 701 */     return paramClass.getName().replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescriptor(Class<?> paramClass) {
/* 712 */     StringBuilder stringBuilder = new StringBuilder();
/* 713 */     getDescriptor(stringBuilder, paramClass);
/* 714 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getConstructorDescriptor(Constructor<?> paramConstructor) {
/* 725 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/* 726 */     StringBuilder stringBuilder = new StringBuilder();
/* 727 */     stringBuilder.append('(');
/* 728 */     for (byte b = 0; b < arrayOfClass.length; b++) {
/* 729 */       getDescriptor(stringBuilder, arrayOfClass[b]);
/*     */     }
/* 731 */     return stringBuilder.append(")V").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMethodDescriptor(Method paramMethod) {
/* 742 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 743 */     StringBuilder stringBuilder = new StringBuilder();
/* 744 */     stringBuilder.append('(');
/* 745 */     for (byte b = 0; b < arrayOfClass.length; b++) {
/* 746 */       getDescriptor(stringBuilder, arrayOfClass[b]);
/*     */     }
/* 748 */     stringBuilder.append(')');
/* 749 */     getDescriptor(stringBuilder, paramMethod.getReturnType());
/* 750 */     return stringBuilder.toString();
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
/*     */   private static void getDescriptor(StringBuilder paramStringBuilder, Class<?> paramClass) {
/* 762 */     Class<?> clazz = paramClass;
/*     */     while (true) {
/* 764 */       if (clazz.isPrimitive()) {
/*     */         byte b1;
/* 766 */         if (clazz == int.class) {
/* 767 */           b1 = 73;
/* 768 */         } else if (clazz == void.class) {
/* 769 */           b1 = 86;
/* 770 */         } else if (clazz == boolean.class) {
/* 771 */           b1 = 90;
/* 772 */         } else if (clazz == byte.class) {
/* 773 */           b1 = 66;
/* 774 */         } else if (clazz == char.class) {
/* 775 */           b1 = 67;
/* 776 */         } else if (clazz == short.class) {
/* 777 */           b1 = 83;
/* 778 */         } else if (clazz == double.class) {
/* 779 */           b1 = 68;
/* 780 */         } else if (clazz == float.class) {
/* 781 */           b1 = 70;
/*     */         } else {
/* 783 */           b1 = 74;
/*     */         } 
/* 785 */         paramStringBuilder.append(b1); return;
/*     */       } 
/* 787 */       if (clazz.isArray()) {
/* 788 */         paramStringBuilder.append('[');
/* 789 */         clazz = clazz.getComponentType(); continue;
/*     */       }  break;
/* 791 */     }  paramStringBuilder.append('L');
/* 792 */     String str = clazz.getName();
/* 793 */     int i = str.length();
/* 794 */     for (byte b = 0; b < i; b++) {
/* 795 */       char c = str.charAt(b);
/* 796 */       paramStringBuilder.append((c == '.') ? 47 : c);
/*     */     } 
/* 798 */     paramStringBuilder.append(';');
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
/*     */   public int getSize() {
/* 817 */     return (this.buf == null) ? (this.off & 0xFF) : 1;
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
/*     */   public int getOpcode(int paramInt) {
/* 833 */     if (paramInt == 46 || paramInt == 79)
/*     */     {
/*     */       
/* 836 */       return paramInt + ((this.buf == null) ? ((this.off & 0xFF00) >> 8) : 4);
/*     */     }
/*     */ 
/*     */     
/* 840 */     return paramInt + ((this.buf == null) ? ((this.off & 0xFF0000) >> 16) : 4);
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
/*     */   public boolean equals(Object paramObject) {
/* 857 */     if (this == paramObject) {
/* 858 */       return true;
/*     */     }
/* 860 */     if (!(paramObject instanceof Type)) {
/* 861 */       return false;
/*     */     }
/* 863 */     Type type = (Type)paramObject;
/* 864 */     if (this.sort != type.sort) {
/* 865 */       return false;
/*     */     }
/* 867 */     if (this.sort >= 9) {
/* 868 */       if (this.len != type.len) {
/* 869 */         return false;
/*     */       }
/* 871 */       for (int i = this.off, j = type.off, k = i + this.len; i < k; i++, j++) {
/* 872 */         if (this.buf[i] != type.buf[j]) {
/* 873 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 877 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 887 */     int i = 13 * this.sort;
/* 888 */     if (this.sort >= 9) {
/* 889 */       for (int j = this.off, k = j + this.len; j < k; j++) {
/* 890 */         i = 17 * (i + this.buf[j]);
/*     */       }
/*     */     }
/* 893 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 903 */     return getDescriptor();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\Type.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */