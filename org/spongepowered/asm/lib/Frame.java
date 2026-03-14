/*      */ package org.spongepowered.asm.lib;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Frame
/*      */ {
/*      */   static final int DIM = -268435456;
/*      */   static final int ARRAY_OF = 268435456;
/*      */   static final int ELEMENT_OF = -268435456;
/*      */   static final int KIND = 251658240;
/*      */   static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
/*      */   static final int VALUE = 8388607;
/*      */   static final int BASE_KIND = 267386880;
/*      */   static final int BASE_VALUE = 1048575;
/*      */   static final int BASE = 16777216;
/*      */   static final int OBJECT = 24117248;
/*      */   static final int UNINITIALIZED = 25165824;
/*      */   private static final int LOCAL = 33554432;
/*      */   private static final int STACK = 50331648;
/*      */   static final int TOP = 16777216;
/*      */   static final int BOOLEAN = 16777225;
/*      */   static final int BYTE = 16777226;
/*      */   static final int CHAR = 16777227;
/*      */   static final int SHORT = 16777228;
/*      */   static final int INTEGER = 16777217;
/*      */   static final int FLOAT = 16777218;
/*      */   static final int DOUBLE = 16777219;
/*      */   static final int LONG = 16777220;
/*      */   static final int NULL = 16777221;
/*      */   static final int UNINITIALIZED_THIS = 16777222;
/*      */   static final int[] SIZE;
/*      */   Label owner;
/*      */   int[] inputLocals;
/*      */   int[] inputStack;
/*      */   private int[] outputLocals;
/*      */   private int[] outputStack;
/*      */   int outputStackTop;
/*      */   private int initializationCount;
/*      */   private int[] initializations;
/*      */   
/*      */   static {
/*  239 */     int[] arrayOfInt = new int[202];
/*  240 */     String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
/*      */ 
/*      */ 
/*      */     
/*  244 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*  245 */       arrayOfInt[b] = str.charAt(b) - 69;
/*      */     }
/*  247 */     SIZE = arrayOfInt;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void set(ClassWriter paramClassWriter, int paramInt1, Object[] paramArrayOfObject1, int paramInt2, Object[] paramArrayOfObject2) {
/*  548 */     int i = convert(paramClassWriter, paramInt1, paramArrayOfObject1, this.inputLocals);
/*  549 */     while (i < paramArrayOfObject1.length) {
/*  550 */       this.inputLocals[i++] = 16777216;
/*      */     }
/*  552 */     byte b1 = 0;
/*  553 */     for (byte b2 = 0; b2 < paramInt2; b2++) {
/*  554 */       if (paramArrayOfObject2[b2] == Opcodes.LONG || paramArrayOfObject2[b2] == Opcodes.DOUBLE) {
/*  555 */         b1++;
/*      */       }
/*      */     } 
/*  558 */     this.inputStack = new int[paramInt2 + b1];
/*  559 */     convert(paramClassWriter, paramInt2, paramArrayOfObject2, this.inputStack);
/*  560 */     this.outputStackTop = 0;
/*  561 */     this.initializationCount = 0;
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
/*      */   private static int convert(ClassWriter paramClassWriter, int paramInt, Object[] paramArrayOfObject, int[] paramArrayOfint) {
/*  589 */     byte b1 = 0;
/*  590 */     for (byte b2 = 0; b2 < paramInt; b2++) {
/*  591 */       if (paramArrayOfObject[b2] instanceof Integer) {
/*  592 */         paramArrayOfint[b1++] = 0x1000000 | ((Integer)paramArrayOfObject[b2]).intValue();
/*  593 */         if (paramArrayOfObject[b2] == Opcodes.LONG || paramArrayOfObject[b2] == Opcodes.DOUBLE) {
/*  594 */           paramArrayOfint[b1++] = 16777216;
/*      */         }
/*  596 */       } else if (paramArrayOfObject[b2] instanceof String) {
/*  597 */         paramArrayOfint[b1++] = type(paramClassWriter, Type.getObjectType((String)paramArrayOfObject[b2])
/*  598 */             .getDescriptor());
/*      */       } else {
/*  600 */         paramArrayOfint[b1++] = 0x1800000 | paramClassWriter
/*  601 */           .addUninitializedType("", ((Label)paramArrayOfObject[b2]).position);
/*      */       } 
/*      */     } 
/*      */     
/*  605 */     return b1;
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
/*      */   final void set(Frame paramFrame) {
/*  618 */     this.inputLocals = paramFrame.inputLocals;
/*  619 */     this.inputStack = paramFrame.inputStack;
/*  620 */     this.outputLocals = paramFrame.outputLocals;
/*  621 */     this.outputStack = paramFrame.outputStack;
/*  622 */     this.outputStackTop = paramFrame.outputStackTop;
/*  623 */     this.initializationCount = paramFrame.initializationCount;
/*  624 */     this.initializations = paramFrame.initializations;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int get(int paramInt) {
/*  635 */     if (this.outputLocals == null || paramInt >= this.outputLocals.length)
/*      */     {
/*      */       
/*  638 */       return 0x2000000 | paramInt;
/*      */     }
/*  640 */     int i = this.outputLocals[paramInt];
/*  641 */     if (i == 0)
/*      */     {
/*      */       
/*  644 */       i = this.outputLocals[paramInt] = 0x2000000 | paramInt;
/*      */     }
/*  646 */     return i;
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
/*      */   private void set(int paramInt1, int paramInt2) {
/*  660 */     if (this.outputLocals == null) {
/*  661 */       this.outputLocals = new int[10];
/*      */     }
/*  663 */     int i = this.outputLocals.length;
/*  664 */     if (paramInt1 >= i) {
/*  665 */       int[] arrayOfInt = new int[Math.max(paramInt1 + 1, 2 * i)];
/*  666 */       System.arraycopy(this.outputLocals, 0, arrayOfInt, 0, i);
/*  667 */       this.outputLocals = arrayOfInt;
/*      */     } 
/*      */     
/*  670 */     this.outputLocals[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void push(int paramInt) {
/*  681 */     if (this.outputStack == null) {
/*  682 */       this.outputStack = new int[10];
/*      */     }
/*  684 */     int i = this.outputStack.length;
/*  685 */     if (this.outputStackTop >= i) {
/*  686 */       int[] arrayOfInt = new int[Math.max(this.outputStackTop + 1, 2 * i)];
/*  687 */       System.arraycopy(this.outputStack, 0, arrayOfInt, 0, i);
/*  688 */       this.outputStack = arrayOfInt;
/*      */     } 
/*      */     
/*  691 */     this.outputStack[this.outputStackTop++] = paramInt;
/*      */     
/*  693 */     int j = this.owner.inputStackTop + this.outputStackTop;
/*  694 */     if (j > this.owner.outputStackMax) {
/*  695 */       this.owner.outputStackMax = j;
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
/*      */   private void push(ClassWriter paramClassWriter, String paramString) {
/*  710 */     int i = type(paramClassWriter, paramString);
/*  711 */     if (i != 0) {
/*  712 */       push(i);
/*  713 */       if (i == 16777220 || i == 16777219) {
/*  714 */         push(16777216);
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
/*      */   private static int type(ClassWriter paramClassWriter, String paramString) {
/*  730 */     byte b = (paramString.charAt(0) == '(') ? (paramString.indexOf(')') + 1) : 0;
/*  731 */     switch (paramString.charAt(b)) {
/*      */       case 'V':
/*  733 */         return 0;
/*      */       case 'B':
/*      */       case 'C':
/*      */       case 'I':
/*      */       case 'S':
/*      */       case 'Z':
/*  739 */         return 16777217;
/*      */       case 'F':
/*  741 */         return 16777218;
/*      */       case 'J':
/*  743 */         return 16777220;
/*      */       case 'D':
/*  745 */         return 16777219;
/*      */       
/*      */       case 'L':
/*  748 */         str = paramString.substring(b + 1, paramString.length() - 1);
/*  749 */         return 0x1700000 | paramClassWriter.addType(str);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  754 */     int i = b + 1;
/*  755 */     while (paramString.charAt(i) == '[') {
/*  756 */       i++;
/*      */     }
/*  758 */     switch (paramString.charAt(i))
/*      */     { case 'Z':
/*  760 */         j = 16777225;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  789 */         return i - b << 28 | j;case 'C': j = 16777227; return i - b << 28 | j;case 'B': j = 16777226; return i - b << 28 | j;case 'S': j = 16777228; return i - b << 28 | j;case 'I': j = 16777217; return i - b << 28 | j;case 'F': j = 16777218; return i - b << 28 | j;case 'J': j = 16777220; return i - b << 28 | j;case 'D': j = 16777219; return i - b << 28 | j; }  String str = paramString.substring(i + 1, paramString.length() - 1); int j = 0x1700000 | paramClassWriter.addType(str); return i - b << 28 | j;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int pop() {
/*  799 */     if (this.outputStackTop > 0) {
/*  800 */       return this.outputStack[--this.outputStackTop];
/*      */     }
/*      */     
/*  803 */     return 0x3000000 | ---this.owner.inputStackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pop(int paramInt) {
/*  814 */     if (this.outputStackTop >= paramInt) {
/*  815 */       this.outputStackTop -= paramInt;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  820 */       this.owner.inputStackTop -= paramInt - this.outputStackTop;
/*  821 */       this.outputStackTop = 0;
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
/*      */   private void pop(String paramString) {
/*  834 */     char c = paramString.charAt(0);
/*  835 */     if (c == '(') {
/*  836 */       pop((Type.getArgumentsAndReturnSizes(paramString) >> 2) - 1);
/*  837 */     } else if (c == 'J' || c == 'D') {
/*  838 */       pop(2);
/*      */     } else {
/*  840 */       pop(1);
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
/*      */   private void init(int paramInt) {
/*  853 */     if (this.initializations == null) {
/*  854 */       this.initializations = new int[2];
/*      */     }
/*  856 */     int i = this.initializations.length;
/*  857 */     if (this.initializationCount >= i) {
/*  858 */       int[] arrayOfInt = new int[Math.max(this.initializationCount + 1, 2 * i)];
/*  859 */       System.arraycopy(this.initializations, 0, arrayOfInt, 0, i);
/*  860 */       this.initializations = arrayOfInt;
/*      */     } 
/*      */     
/*  863 */     this.initializations[this.initializationCount++] = paramInt;
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
/*      */   private int init(ClassWriter paramClassWriter, int paramInt) {
/*      */     int i;
/*  879 */     if (paramInt == 16777222) {
/*  880 */       i = 0x1700000 | paramClassWriter.addType(paramClassWriter.thisName);
/*  881 */     } else if ((paramInt & 0xFFF00000) == 25165824) {
/*  882 */       String str = (paramClassWriter.typeTable[paramInt & 0xFFFFF]).strVal1;
/*  883 */       i = 0x1700000 | paramClassWriter.addType(str);
/*      */     } else {
/*  885 */       return paramInt;
/*      */     } 
/*  887 */     for (byte b = 0; b < this.initializationCount; b++) {
/*  888 */       int j = this.initializations[b];
/*  889 */       int k = j & 0xF0000000;
/*  890 */       int m = j & 0xF000000;
/*  891 */       if (m == 33554432) {
/*  892 */         j = k + this.inputLocals[j & 0x7FFFFF];
/*  893 */       } else if (m == 50331648) {
/*  894 */         j = k + this.inputStack[this.inputStack.length - (j & 0x7FFFFF)];
/*      */       } 
/*  896 */       if (paramInt == j) {
/*  897 */         return i;
/*      */       }
/*      */     } 
/*  900 */     return paramInt;
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
/*      */   final void initInputFrame(ClassWriter paramClassWriter, int paramInt1, Type[] paramArrayOfType, int paramInt2) {
/*  918 */     this.inputLocals = new int[paramInt2];
/*  919 */     this.inputStack = new int[0];
/*  920 */     byte b1 = 0;
/*  921 */     if ((paramInt1 & 0x8) == 0) {
/*  922 */       if ((paramInt1 & 0x80000) == 0) {
/*  923 */         this.inputLocals[b1++] = 0x1700000 | paramClassWriter.addType(paramClassWriter.thisName);
/*      */       } else {
/*  925 */         this.inputLocals[b1++] = 16777222;
/*      */       } 
/*      */     }
/*  928 */     for (byte b2 = 0; b2 < paramArrayOfType.length; b2++) {
/*  929 */       int i = type(paramClassWriter, paramArrayOfType[b2].getDescriptor());
/*  930 */       this.inputLocals[b1++] = i;
/*  931 */       if (i == 16777220 || i == 16777219) {
/*  932 */         this.inputLocals[b1++] = 16777216;
/*      */       }
/*      */     } 
/*  935 */     while (b1 < paramInt2) {
/*  936 */       this.inputLocals[b1++] = 16777216;
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
/*      */   void execute(int paramInt1, int paramInt2, ClassWriter paramClassWriter, Item paramItem) {
/*      */     int i, j, k, m;
/*      */     String str;
/*  955 */     switch (paramInt1) {
/*      */       case 0:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */       case 167:
/*      */       case 177:
/*      */         return;
/*      */       case 1:
/*  968 */         push(16777221);
/*      */       
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 16:
/*      */       case 17:
/*      */       case 21:
/*  980 */         push(16777217);
/*      */       
/*      */       case 9:
/*      */       case 10:
/*      */       case 22:
/*  985 */         push(16777220);
/*  986 */         push(16777216);
/*      */       
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 23:
/*  992 */         push(16777218);
/*      */       
/*      */       case 14:
/*      */       case 15:
/*      */       case 24:
/*  997 */         push(16777219);
/*  998 */         push(16777216);
/*      */       
/*      */       case 18:
/* 1001 */         switch (paramItem.type) {
/*      */           case 3:
/* 1003 */             push(16777217);
/*      */           
/*      */           case 5:
/* 1006 */             push(16777220);
/* 1007 */             push(16777216);
/*      */           
/*      */           case 4:
/* 1010 */             push(16777218);
/*      */           
/*      */           case 6:
/* 1013 */             push(16777219);
/* 1014 */             push(16777216);
/*      */           
/*      */           case 7:
/* 1017 */             push(0x1700000 | paramClassWriter.addType("java/lang/Class"));
/*      */           
/*      */           case 8:
/* 1020 */             push(0x1700000 | paramClassWriter.addType("java/lang/String"));
/*      */           
/*      */           case 16:
/* 1023 */             push(0x1700000 | paramClassWriter.addType("java/lang/invoke/MethodType"));
/*      */         } 
/*      */ 
/*      */         
/* 1027 */         push(0x1700000 | paramClassWriter.addType("java/lang/invoke/MethodHandle"));
/*      */ 
/*      */       
/*      */       case 25:
/* 1031 */         push(get(paramInt2));
/*      */       
/*      */       case 46:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/* 1037 */         pop(2);
/* 1038 */         push(16777217);
/*      */       
/*      */       case 47:
/*      */       case 143:
/* 1042 */         pop(2);
/* 1043 */         push(16777220);
/* 1044 */         push(16777216);
/*      */       
/*      */       case 48:
/* 1047 */         pop(2);
/* 1048 */         push(16777218);
/*      */       
/*      */       case 49:
/*      */       case 138:
/* 1052 */         pop(2);
/* 1053 */         push(16777219);
/* 1054 */         push(16777216);
/*      */       
/*      */       case 50:
/* 1057 */         pop(1);
/* 1058 */         i = pop();
/* 1059 */         push(-268435456 + i);
/*      */       
/*      */       case 54:
/*      */       case 56:
/*      */       case 58:
/* 1064 */         i = pop();
/* 1065 */         set(paramInt2, i);
/* 1066 */         if (paramInt2 > 0) {
/* 1067 */           int n = get(paramInt2 - 1);
/*      */           
/* 1069 */           if (n == 16777220 || n == 16777219) {
/* 1070 */             set(paramInt2 - 1, 16777216);
/* 1071 */           } else if ((n & 0xF000000) != 16777216) {
/* 1072 */             set(paramInt2 - 1, n | 0x800000);
/*      */           } 
/*      */         } 
/*      */       
/*      */       case 55:
/*      */       case 57:
/* 1078 */         pop(1);
/* 1079 */         i = pop();
/* 1080 */         set(paramInt2, i);
/* 1081 */         set(paramInt2 + 1, 16777216);
/* 1082 */         if (paramInt2 > 0) {
/* 1083 */           int n = get(paramInt2 - 1);
/*      */           
/* 1085 */           if (n == 16777220 || n == 16777219) {
/* 1086 */             set(paramInt2 - 1, 16777216);
/* 1087 */           } else if ((n & 0xF000000) != 16777216) {
/* 1088 */             set(paramInt2 - 1, n | 0x800000);
/*      */           } 
/*      */         } 
/*      */       
/*      */       case 79:
/*      */       case 81:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/* 1098 */         pop(3);
/*      */       
/*      */       case 80:
/*      */       case 82:
/* 1102 */         pop(4);
/*      */       
/*      */       case 87:
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 170:
/*      */       case 171:
/*      */       case 172:
/*      */       case 174:
/*      */       case 176:
/*      */       case 191:
/*      */       case 194:
/*      */       case 195:
/*      */       case 198:
/*      */       case 199:
/* 1121 */         pop(1);
/*      */       
/*      */       case 88:
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 163:
/*      */       case 164:
/*      */       case 165:
/*      */       case 166:
/*      */       case 173:
/*      */       case 175:
/* 1134 */         pop(2);
/*      */       
/*      */       case 89:
/* 1137 */         i = pop();
/* 1138 */         push(i);
/* 1139 */         push(i);
/*      */       
/*      */       case 90:
/* 1142 */         i = pop();
/* 1143 */         j = pop();
/* 1144 */         push(i);
/* 1145 */         push(j);
/* 1146 */         push(i);
/*      */       
/*      */       case 91:
/* 1149 */         i = pop();
/* 1150 */         j = pop();
/* 1151 */         k = pop();
/* 1152 */         push(i);
/* 1153 */         push(k);
/* 1154 */         push(j);
/* 1155 */         push(i);
/*      */       
/*      */       case 92:
/* 1158 */         i = pop();
/* 1159 */         j = pop();
/* 1160 */         push(j);
/* 1161 */         push(i);
/* 1162 */         push(j);
/* 1163 */         push(i);
/*      */       
/*      */       case 93:
/* 1166 */         i = pop();
/* 1167 */         j = pop();
/* 1168 */         k = pop();
/* 1169 */         push(j);
/* 1170 */         push(i);
/* 1171 */         push(k);
/* 1172 */         push(j);
/* 1173 */         push(i);
/*      */       
/*      */       case 94:
/* 1176 */         i = pop();
/* 1177 */         j = pop();
/* 1178 */         k = pop();
/* 1179 */         m = pop();
/* 1180 */         push(j);
/* 1181 */         push(i);
/* 1182 */         push(m);
/* 1183 */         push(k);
/* 1184 */         push(j);
/* 1185 */         push(i);
/*      */       
/*      */       case 95:
/* 1188 */         i = pop();
/* 1189 */         j = pop();
/* 1190 */         push(i);
/* 1191 */         push(j);
/*      */       
/*      */       case 96:
/*      */       case 100:
/*      */       case 104:
/*      */       case 108:
/*      */       case 112:
/*      */       case 120:
/*      */       case 122:
/*      */       case 124:
/*      */       case 126:
/*      */       case 128:
/*      */       case 130:
/*      */       case 136:
/*      */       case 142:
/*      */       case 149:
/*      */       case 150:
/* 1208 */         pop(2);
/* 1209 */         push(16777217);
/*      */       
/*      */       case 97:
/*      */       case 101:
/*      */       case 105:
/*      */       case 109:
/*      */       case 113:
/*      */       case 127:
/*      */       case 129:
/*      */       case 131:
/* 1219 */         pop(4);
/* 1220 */         push(16777220);
/* 1221 */         push(16777216);
/*      */       
/*      */       case 98:
/*      */       case 102:
/*      */       case 106:
/*      */       case 110:
/*      */       case 114:
/*      */       case 137:
/*      */       case 144:
/* 1230 */         pop(2);
/* 1231 */         push(16777218);
/*      */       
/*      */       case 99:
/*      */       case 103:
/*      */       case 107:
/*      */       case 111:
/*      */       case 115:
/* 1238 */         pop(4);
/* 1239 */         push(16777219);
/* 1240 */         push(16777216);
/*      */       
/*      */       case 121:
/*      */       case 123:
/*      */       case 125:
/* 1245 */         pop(3);
/* 1246 */         push(16777220);
/* 1247 */         push(16777216);
/*      */       
/*      */       case 132:
/* 1250 */         set(paramInt2, 16777217);
/*      */       
/*      */       case 133:
/*      */       case 140:
/* 1254 */         pop(1);
/* 1255 */         push(16777220);
/* 1256 */         push(16777216);
/*      */       
/*      */       case 134:
/* 1259 */         pop(1);
/* 1260 */         push(16777218);
/*      */       
/*      */       case 135:
/*      */       case 141:
/* 1264 */         pop(1);
/* 1265 */         push(16777219);
/* 1266 */         push(16777216);
/*      */       
/*      */       case 139:
/*      */       case 190:
/*      */       case 193:
/* 1271 */         pop(1);
/* 1272 */         push(16777217);
/*      */       
/*      */       case 148:
/*      */       case 151:
/*      */       case 152:
/* 1277 */         pop(4);
/* 1278 */         push(16777217);
/*      */       
/*      */       case 168:
/*      */       case 169:
/* 1282 */         throw new RuntimeException("JSR/RET are not supported with computeFrames option");
/*      */       
/*      */       case 178:
/* 1285 */         push(paramClassWriter, paramItem.strVal3);
/*      */       
/*      */       case 179:
/* 1288 */         pop(paramItem.strVal3);
/*      */       
/*      */       case 180:
/* 1291 */         pop(1);
/* 1292 */         push(paramClassWriter, paramItem.strVal3);
/*      */       
/*      */       case 181:
/* 1295 */         pop(paramItem.strVal3);
/* 1296 */         pop();
/*      */       
/*      */       case 182:
/*      */       case 183:
/*      */       case 184:
/*      */       case 185:
/* 1302 */         pop(paramItem.strVal3);
/* 1303 */         if (paramInt1 != 184) {
/* 1304 */           i = pop();
/* 1305 */           if (paramInt1 == 183 && paramItem.strVal2
/* 1306 */             .charAt(0) == '<') {
/* 1307 */             init(i);
/*      */           }
/*      */         } 
/* 1310 */         push(paramClassWriter, paramItem.strVal3);
/*      */       
/*      */       case 186:
/* 1313 */         pop(paramItem.strVal2);
/* 1314 */         push(paramClassWriter, paramItem.strVal2);
/*      */       
/*      */       case 187:
/* 1317 */         push(0x1800000 | paramClassWriter.addUninitializedType(paramItem.strVal1, paramInt2));
/*      */       
/*      */       case 188:
/* 1320 */         pop();
/* 1321 */         switch (paramInt2) {
/*      */           case 4:
/* 1323 */             push(285212681);
/*      */           
/*      */           case 5:
/* 1326 */             push(285212683);
/*      */           
/*      */           case 8:
/* 1329 */             push(285212682);
/*      */           
/*      */           case 9:
/* 1332 */             push(285212684);
/*      */           
/*      */           case 10:
/* 1335 */             push(285212673);
/*      */           
/*      */           case 6:
/* 1338 */             push(285212674);
/*      */           
/*      */           case 7:
/* 1341 */             push(285212675);
/*      */         } 
/*      */ 
/*      */         
/* 1345 */         push(285212676);
/*      */ 
/*      */ 
/*      */       
/*      */       case 189:
/* 1350 */         str = paramItem.strVal1;
/* 1351 */         pop();
/* 1352 */         if (str.charAt(0) == '[') {
/* 1353 */           push(paramClassWriter, '[' + str);
/*      */         } else {
/* 1355 */           push(0x11700000 | paramClassWriter.addType(str));
/*      */         } 
/*      */       
/*      */       case 192:
/* 1359 */         str = paramItem.strVal1;
/* 1360 */         pop();
/* 1361 */         if (str.charAt(0) == '[') {
/* 1362 */           push(paramClassWriter, str);
/*      */         } else {
/* 1364 */           push(0x1700000 | paramClassWriter.addType(str));
/*      */         } 
/*      */     } 
/*      */ 
/*      */     
/* 1369 */     pop(paramInt2);
/* 1370 */     push(paramClassWriter, paramItem.strVal1);
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
/*      */   final boolean merge(ClassWriter paramClassWriter, Frame paramFrame, int paramInt) {
/* 1391 */     boolean bool = false;
/*      */ 
/*      */     
/* 1394 */     int i = this.inputLocals.length;
/* 1395 */     int j = this.inputStack.length;
/* 1396 */     if (paramFrame.inputLocals == null) {
/* 1397 */       paramFrame.inputLocals = new int[i];
/* 1398 */       bool = true;
/*      */     } 
/*      */     byte b;
/* 1401 */     for (b = 0; b < i; b++) {
/* 1402 */       int m; if (this.outputLocals != null && b < this.outputLocals.length) {
/* 1403 */         int n = this.outputLocals[b];
/* 1404 */         if (n == 0) {
/* 1405 */           m = this.inputLocals[b];
/*      */         } else {
/* 1407 */           int i1 = n & 0xF0000000;
/* 1408 */           int i2 = n & 0xF000000;
/* 1409 */           if (i2 == 16777216) {
/* 1410 */             m = n;
/*      */           } else {
/* 1412 */             if (i2 == 33554432) {
/* 1413 */               m = i1 + this.inputLocals[n & 0x7FFFFF];
/*      */             } else {
/* 1415 */               m = i1 + this.inputStack[j - (n & 0x7FFFFF)];
/*      */             } 
/* 1417 */             if ((n & 0x800000) != 0 && (m == 16777220 || m == 16777219))
/*      */             {
/* 1419 */               m = 16777216;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1424 */         m = this.inputLocals[b];
/*      */       } 
/* 1426 */       if (this.initializations != null) {
/* 1427 */         m = init(paramClassWriter, m);
/*      */       }
/* 1429 */       bool |= merge(paramClassWriter, m, paramFrame.inputLocals, b);
/*      */     } 
/*      */     
/* 1432 */     if (paramInt > 0) {
/* 1433 */       for (b = 0; b < i; b++) {
/* 1434 */         int m = this.inputLocals[b];
/* 1435 */         bool |= merge(paramClassWriter, m, paramFrame.inputLocals, b);
/*      */       } 
/* 1437 */       if (paramFrame.inputStack == null) {
/* 1438 */         paramFrame.inputStack = new int[1];
/* 1439 */         bool = true;
/*      */       } 
/* 1441 */       bool |= merge(paramClassWriter, paramInt, paramFrame.inputStack, 0);
/* 1442 */       return bool;
/*      */     } 
/*      */     
/* 1445 */     int k = this.inputStack.length + this.owner.inputStackTop;
/* 1446 */     if (paramFrame.inputStack == null) {
/* 1447 */       paramFrame.inputStack = new int[k + this.outputStackTop];
/* 1448 */       bool = true;
/*      */     } 
/*      */     
/* 1451 */     for (b = 0; b < k; b++) {
/* 1452 */       int m = this.inputStack[b];
/* 1453 */       if (this.initializations != null) {
/* 1454 */         m = init(paramClassWriter, m);
/*      */       }
/* 1456 */       bool |= merge(paramClassWriter, m, paramFrame.inputStack, b);
/*      */     } 
/* 1458 */     for (b = 0; b < this.outputStackTop; b++) {
/* 1459 */       int n, m = this.outputStack[b];
/* 1460 */       int i1 = m & 0xF0000000;
/* 1461 */       int i2 = m & 0xF000000;
/* 1462 */       if (i2 == 16777216) {
/* 1463 */         n = m;
/*      */       } else {
/* 1465 */         if (i2 == 33554432) {
/* 1466 */           n = i1 + this.inputLocals[m & 0x7FFFFF];
/*      */         } else {
/* 1468 */           n = i1 + this.inputStack[j - (m & 0x7FFFFF)];
/*      */         } 
/* 1470 */         if ((m & 0x800000) != 0 && (n == 16777220 || n == 16777219))
/*      */         {
/* 1472 */           n = 16777216;
/*      */         }
/*      */       } 
/* 1475 */       if (this.initializations != null) {
/* 1476 */         n = init(paramClassWriter, n);
/*      */       }
/* 1478 */       bool |= merge(paramClassWriter, n, paramFrame.inputStack, k + b);
/*      */     } 
/* 1480 */     return bool;
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
/*      */   private static boolean merge(ClassWriter paramClassWriter, int paramInt1, int[] paramArrayOfint, int paramInt2) {
/* 1501 */     int j, i = paramArrayOfint[paramInt2];
/* 1502 */     if (i == paramInt1)
/*      */     {
/* 1504 */       return false;
/*      */     }
/* 1506 */     if ((paramInt1 & 0xFFFFFFF) == 16777221) {
/* 1507 */       if (i == 16777221) {
/* 1508 */         return false;
/*      */       }
/* 1510 */       paramInt1 = 16777221;
/*      */     } 
/* 1512 */     if (i == 0) {
/*      */       
/* 1514 */       paramArrayOfint[paramInt2] = paramInt1;
/* 1515 */       return true;
/*      */     } 
/*      */     
/* 1518 */     if ((i & 0xFF00000) == 24117248 || (i & 0xF0000000) != 0) {
/*      */       
/* 1520 */       if (paramInt1 == 16777221)
/*      */       {
/* 1522 */         return false; } 
/* 1523 */       if ((paramInt1 & 0xFFF00000) == (i & 0xFFF00000)) {
/*      */         
/* 1525 */         if ((i & 0xFF00000) == 24117248) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1530 */           j = paramInt1 & 0xF0000000 | 0x1700000 | paramClassWriter.getMergedType(paramInt1 & 0xFFFFF, i & 0xFFFFF);
/*      */         }
/*      */         else {
/*      */           
/* 1534 */           int k = -268435456 + (i & 0xF0000000);
/* 1535 */           j = k | 0x1700000 | paramClassWriter.addType("java/lang/Object");
/*      */         } 
/* 1537 */       } else if ((paramInt1 & 0xFF00000) == 24117248 || (paramInt1 & 0xF0000000) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1542 */         int k = (((paramInt1 & 0xF0000000) == 0 || (paramInt1 & 0xFF00000) == 24117248) ? 0 : -268435456) + (paramInt1 & 0xF0000000);
/*      */         
/* 1544 */         int m = (((i & 0xF0000000) == 0 || (i & 0xFF00000) == 24117248) ? 0 : -268435456) + (i & 0xF0000000);
/*      */ 
/*      */         
/* 1547 */         j = Math.min(k, m) | 0x1700000 | paramClassWriter.addType("java/lang/Object");
/*      */       } else {
/*      */         
/* 1550 */         j = 16777216;
/*      */       } 
/* 1552 */     } else if (i == 16777221) {
/*      */ 
/*      */       
/* 1555 */       j = ((paramInt1 & 0xFF00000) == 24117248 || (paramInt1 & 0xF0000000) != 0) ? paramInt1 : 16777216;
/*      */     } else {
/*      */       
/* 1558 */       j = 16777216;
/*      */     } 
/* 1560 */     if (i != j) {
/* 1561 */       paramArrayOfint[paramInt2] = j;
/* 1562 */       return true;
/*      */     } 
/* 1564 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\Frame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */