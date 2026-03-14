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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MethodWriter
/*      */   extends MethodVisitor
/*      */ {
/*      */   static final int ACC_CONSTRUCTOR = 524288;
/*      */   static final int SAME_FRAME = 0;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
/*      */   static final int RESERVED = 128;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
/*      */   static final int CHOP_FRAME = 248;
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */   static final int APPEND_FRAME = 252;
/*      */   static final int FULL_FRAME = 255;
/*      */   static final int FRAMES = 0;
/*      */   static final int INSERTED_FRAMES = 1;
/*      */   static final int MAXS = 2;
/*      */   static final int NOTHING = 3;
/*      */   final ClassWriter cw;
/*      */   private int access;
/*      */   private final int name;
/*      */   private final int desc;
/*      */   private final String descriptor;
/*      */   String signature;
/*      */   int classReaderOffset;
/*      */   int classReaderLength;
/*      */   int exceptionCount;
/*      */   int[] exceptions;
/*      */   private ByteVector annd;
/*      */   private AnnotationWriter anns;
/*      */   private AnnotationWriter ianns;
/*      */   private AnnotationWriter tanns;
/*      */   private AnnotationWriter itanns;
/*      */   private AnnotationWriter[] panns;
/*      */   private AnnotationWriter[] ipanns;
/*      */   private int synthetics;
/*      */   private Attribute attrs;
/*  243 */   private ByteVector code = new ByteVector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int currentLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int frameCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector stackMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int previousFrameOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] previousFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] frame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handlerCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler firstHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler lastHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int methodParametersCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector methodParameters;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVarCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVar;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVarTypeCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVarType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lineNumberCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector lineNumber;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lastCodeOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter ctanns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter ictanns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute cattrs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int subroutines;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int compute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label labels;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label previousBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label currentBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int stackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxStackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MethodWriter(ClassWriter paramClassWriter, int paramInt1, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, int paramInt2) {
/*  459 */     super(327680);
/*  460 */     if (paramClassWriter.firstMethod == null) {
/*  461 */       paramClassWriter.firstMethod = this;
/*      */     } else {
/*  463 */       paramClassWriter.lastMethod.mv = this;
/*      */     } 
/*  465 */     paramClassWriter.lastMethod = this;
/*  466 */     this.cw = paramClassWriter;
/*  467 */     this.access = paramInt1;
/*  468 */     if ("<init>".equals(paramString1)) {
/*  469 */       this.access |= 0x80000;
/*      */     }
/*  471 */     this.name = paramClassWriter.newUTF8(paramString1);
/*  472 */     this.desc = paramClassWriter.newUTF8(paramString2);
/*  473 */     this.descriptor = paramString2;
/*      */     
/*  475 */     this.signature = paramString3;
/*      */     
/*  477 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/*  478 */       this.exceptionCount = paramArrayOfString.length;
/*  479 */       this.exceptions = new int[this.exceptionCount];
/*  480 */       for (byte b = 0; b < this.exceptionCount; b++) {
/*  481 */         this.exceptions[b] = paramClassWriter.newClass(paramArrayOfString[b]);
/*      */       }
/*      */     } 
/*  484 */     this.compute = paramInt2;
/*  485 */     if (paramInt2 != 3) {
/*      */       
/*  487 */       int i = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
/*  488 */       if ((paramInt1 & 0x8) != 0) {
/*  489 */         i--;
/*      */       }
/*  491 */       this.maxLocals = i;
/*  492 */       this.currentLocals = i;
/*      */       
/*  494 */       this.labels = new Label();
/*  495 */       this.labels.status |= 0x8;
/*  496 */       visitLabel(this.labels);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitParameter(String paramString, int paramInt) {
/*  506 */     if (this.methodParameters == null) {
/*  507 */       this.methodParameters = new ByteVector();
/*      */     }
/*  509 */     this.methodParametersCount++;
/*  510 */     this.methodParameters.putShort((paramString == null) ? 0 : this.cw.newUTF8(paramString))
/*  511 */       .putShort(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotationDefault() {
/*  519 */     this.annd = new ByteVector();
/*  520 */     return new AnnotationWriter(this.cw, false, this.annd, null, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  529 */     ByteVector byteVector = new ByteVector();
/*      */     
/*  531 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/*  532 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
/*  533 */     if (paramBoolean) {
/*  534 */       annotationWriter.next = this.anns;
/*  535 */       this.anns = annotationWriter;
/*      */     } else {
/*  537 */       annotationWriter.next = this.ianns;
/*  538 */       this.ianns = annotationWriter;
/*      */     } 
/*  540 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  549 */     ByteVector byteVector = new ByteVector();
/*      */     
/*  551 */     AnnotationWriter.putTarget(paramInt, paramTypePath, byteVector);
/*      */     
/*  553 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/*  554 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
/*      */     
/*  556 */     if (paramBoolean) {
/*  557 */       annotationWriter.next = this.tanns;
/*  558 */       this.tanns = annotationWriter;
/*      */     } else {
/*  560 */       annotationWriter.next = this.itanns;
/*  561 */       this.itanns = annotationWriter;
/*      */     } 
/*  563 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/*  572 */     ByteVector byteVector = new ByteVector();
/*  573 */     if ("Ljava/lang/Synthetic;".equals(paramString)) {
/*      */ 
/*      */       
/*  576 */       this.synthetics = Math.max(this.synthetics, paramInt + 1);
/*  577 */       return new AnnotationWriter(this.cw, false, byteVector, null, 0);
/*      */     } 
/*      */     
/*  580 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/*  581 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
/*  582 */     if (paramBoolean) {
/*  583 */       if (this.panns == null) {
/*  584 */         this.panns = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */       }
/*  586 */       annotationWriter.next = this.panns[paramInt];
/*  587 */       this.panns[paramInt] = annotationWriter;
/*      */     } else {
/*  589 */       if (this.ipanns == null) {
/*  590 */         this.ipanns = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */       }
/*  592 */       annotationWriter.next = this.ipanns[paramInt];
/*  593 */       this.ipanns[paramInt] = annotationWriter;
/*      */     } 
/*  595 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitAttribute(Attribute paramAttribute) {
/*  600 */     if (paramAttribute.isCodeAttribute()) {
/*  601 */       paramAttribute.next = this.cattrs;
/*  602 */       this.cattrs = paramAttribute;
/*      */     } else {
/*  604 */       paramAttribute.next = this.attrs;
/*  605 */       this.attrs = paramAttribute;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitCode() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/*  616 */     if (this.compute == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  620 */     if (this.compute == 1) {
/*  621 */       if (this.currentBlock.frame == null) {
/*      */ 
/*      */ 
/*      */         
/*  625 */         this.currentBlock.frame = new CurrentFrame();
/*  626 */         this.currentBlock.frame.owner = this.currentBlock;
/*  627 */         this.currentBlock.frame.initInputFrame(this.cw, this.access, 
/*  628 */             Type.getArgumentTypes(this.descriptor), paramInt2);
/*  629 */         visitImplicitFirstFrame();
/*      */       } else {
/*  631 */         if (paramInt1 == -1) {
/*  632 */           this.currentBlock.frame.set(this.cw, paramInt2, paramArrayOfObject1, paramInt3, paramArrayOfObject2);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  640 */         visitFrame(this.currentBlock.frame);
/*      */       } 
/*  642 */     } else if (paramInt1 == -1) {
/*  643 */       if (this.previousFrame == null) {
/*  644 */         visitImplicitFirstFrame();
/*      */       }
/*  646 */       this.currentLocals = paramInt2;
/*  647 */       int i = startFrame(this.code.length, paramInt2, paramInt3); byte b;
/*  648 */       for (b = 0; b < paramInt2; b++) {
/*  649 */         if (paramArrayOfObject1[b] instanceof String) {
/*  650 */           this.frame[i++] = 0x1700000 | this.cw
/*  651 */             .addType((String)paramArrayOfObject1[b]);
/*  652 */         } else if (paramArrayOfObject1[b] instanceof Integer) {
/*  653 */           this.frame[i++] = ((Integer)paramArrayOfObject1[b]).intValue();
/*      */         } else {
/*  655 */           this.frame[i++] = 0x1800000 | this.cw
/*  656 */             .addUninitializedType("", ((Label)paramArrayOfObject1[b]).position);
/*      */         } 
/*      */       } 
/*      */       
/*  660 */       for (b = 0; b < paramInt3; b++) {
/*  661 */         if (paramArrayOfObject2[b] instanceof String) {
/*  662 */           this.frame[i++] = 0x1700000 | this.cw
/*  663 */             .addType((String)paramArrayOfObject2[b]);
/*  664 */         } else if (paramArrayOfObject2[b] instanceof Integer) {
/*  665 */           this.frame[i++] = ((Integer)paramArrayOfObject2[b]).intValue();
/*      */         } else {
/*  667 */           this.frame[i++] = 0x1800000 | this.cw
/*  668 */             .addUninitializedType("", ((Label)paramArrayOfObject2[b]).position);
/*      */         } 
/*      */       } 
/*      */       
/*  672 */       endFrame();
/*      */     } else {
/*      */       int i; byte b;
/*  675 */       if (this.stackMap == null) {
/*  676 */         this.stackMap = new ByteVector();
/*  677 */         i = this.code.length;
/*      */       } else {
/*  679 */         i = this.code.length - this.previousFrameOffset - 1;
/*  680 */         if (i < 0) {
/*  681 */           if (paramInt1 == 3) {
/*      */             return;
/*      */           }
/*  684 */           throw new IllegalStateException();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  689 */       switch (paramInt1) {
/*      */         case 0:
/*  691 */           this.currentLocals = paramInt2;
/*  692 */           this.stackMap.putByte(255).putShort(i).putShort(paramInt2);
/*  693 */           for (b = 0; b < paramInt2; b++) {
/*  694 */             writeFrameType(paramArrayOfObject1[b]);
/*      */           }
/*  696 */           this.stackMap.putShort(paramInt3);
/*  697 */           for (b = 0; b < paramInt3; b++) {
/*  698 */             writeFrameType(paramArrayOfObject2[b]);
/*      */           }
/*      */           break;
/*      */         case 1:
/*  702 */           this.currentLocals += paramInt2;
/*  703 */           this.stackMap.putByte(251 + paramInt2).putShort(i);
/*  704 */           for (b = 0; b < paramInt2; b++) {
/*  705 */             writeFrameType(paramArrayOfObject1[b]);
/*      */           }
/*      */           break;
/*      */         case 2:
/*  709 */           this.currentLocals -= paramInt2;
/*  710 */           this.stackMap.putByte(251 - paramInt2).putShort(i);
/*      */           break;
/*      */         case 3:
/*  713 */           if (i < 64) {
/*  714 */             this.stackMap.putByte(i); break;
/*      */           } 
/*  716 */           this.stackMap.putByte(251).putShort(i);
/*      */           break;
/*      */         
/*      */         case 4:
/*  720 */           if (i < 64) {
/*  721 */             this.stackMap.putByte(64 + i);
/*      */           } else {
/*  723 */             this.stackMap.putByte(247)
/*  724 */               .putShort(i);
/*      */           } 
/*  726 */           writeFrameType(paramArrayOfObject2[0]);
/*      */           break;
/*      */       } 
/*      */       
/*  730 */       this.previousFrameOffset = this.code.length;
/*  731 */       this.frameCount++;
/*      */     } 
/*      */     
/*  734 */     this.maxStack = Math.max(this.maxStack, paramInt3);
/*  735 */     this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitInsn(int paramInt) {
/*  740 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  742 */     this.code.putByte(paramInt);
/*      */ 
/*      */     
/*  745 */     if (this.currentBlock != null) {
/*  746 */       if (this.compute == 0 || this.compute == 1) {
/*  747 */         this.currentBlock.frame.execute(paramInt, 0, null, null);
/*      */       } else {
/*      */         
/*  750 */         int i = this.stackSize + Frame.SIZE[paramInt];
/*  751 */         if (i > this.maxStackSize) {
/*  752 */           this.maxStackSize = i;
/*      */         }
/*  754 */         this.stackSize = i;
/*      */       } 
/*      */       
/*  757 */       if ((paramInt >= 172 && paramInt <= 177) || paramInt == 191)
/*      */       {
/*  759 */         noSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIntInsn(int paramInt1, int paramInt2) {
/*  766 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  768 */     if (this.currentBlock != null) {
/*  769 */       if (this.compute == 0 || this.compute == 1) {
/*  770 */         this.currentBlock.frame.execute(paramInt1, paramInt2, null, null);
/*  771 */       } else if (paramInt1 != 188) {
/*      */ 
/*      */         
/*  774 */         int i = this.stackSize + 1;
/*  775 */         if (i > this.maxStackSize) {
/*  776 */           this.maxStackSize = i;
/*      */         }
/*  778 */         this.stackSize = i;
/*      */       } 
/*      */     }
/*      */     
/*  782 */     if (paramInt1 == 17) {
/*  783 */       this.code.put12(paramInt1, paramInt2);
/*      */     } else {
/*  785 */       this.code.put11(paramInt1, paramInt2);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitVarInsn(int paramInt1, int paramInt2) {
/*  791 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  793 */     if (this.currentBlock != null) {
/*  794 */       if (this.compute == 0 || this.compute == 1) {
/*  795 */         this.currentBlock.frame.execute(paramInt1, paramInt2, null, null);
/*      */       
/*      */       }
/*  798 */       else if (paramInt1 == 169) {
/*      */         
/*  800 */         this.currentBlock.status |= 0x100;
/*      */ 
/*      */         
/*  803 */         this.currentBlock.inputStackTop = this.stackSize;
/*  804 */         noSuccessor();
/*      */       } else {
/*  806 */         int i = this.stackSize + Frame.SIZE[paramInt1];
/*  807 */         if (i > this.maxStackSize) {
/*  808 */           this.maxStackSize = i;
/*      */         }
/*  810 */         this.stackSize = i;
/*      */       } 
/*      */     }
/*      */     
/*  814 */     if (this.compute != 3) {
/*      */       int i;
/*      */       
/*  817 */       if (paramInt1 == 22 || paramInt1 == 24 || paramInt1 == 55 || paramInt1 == 57) {
/*      */         
/*  819 */         i = paramInt2 + 2;
/*      */       } else {
/*  821 */         i = paramInt2 + 1;
/*      */       } 
/*  823 */       if (i > this.maxLocals) {
/*  824 */         this.maxLocals = i;
/*      */       }
/*      */     } 
/*      */     
/*  828 */     if (paramInt2 < 4 && paramInt1 != 169) {
/*      */       int i;
/*  830 */       if (paramInt1 < 54) {
/*      */         
/*  832 */         i = 26 + (paramInt1 - 21 << 2) + paramInt2;
/*      */       } else {
/*      */         
/*  835 */         i = 59 + (paramInt1 - 54 << 2) + paramInt2;
/*      */       } 
/*  837 */       this.code.putByte(i);
/*  838 */     } else if (paramInt2 >= 256) {
/*  839 */       this.code.putByte(196).put12(paramInt1, paramInt2);
/*      */     } else {
/*  841 */       this.code.put11(paramInt1, paramInt2);
/*      */     } 
/*  843 */     if (paramInt1 >= 54 && this.compute == 0 && this.handlerCount > 0) {
/*  844 */       visitLabel(new Label());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeInsn(int paramInt, String paramString) {
/*  850 */     this.lastCodeOffset = this.code.length;
/*  851 */     Item item = this.cw.newClassItem(paramString);
/*      */     
/*  853 */     if (this.currentBlock != null) {
/*  854 */       if (this.compute == 0 || this.compute == 1) {
/*  855 */         this.currentBlock.frame.execute(paramInt, this.code.length, this.cw, item);
/*  856 */       } else if (paramInt == 187) {
/*      */ 
/*      */         
/*  859 */         int i = this.stackSize + 1;
/*  860 */         if (i > this.maxStackSize) {
/*  861 */           this.maxStackSize = i;
/*      */         }
/*  863 */         this.stackSize = i;
/*      */       } 
/*      */     }
/*      */     
/*  867 */     this.code.put12(paramInt, item.index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  873 */     this.lastCodeOffset = this.code.length;
/*  874 */     Item item = this.cw.newFieldItem(paramString1, paramString2, paramString3);
/*      */     
/*  876 */     if (this.currentBlock != null) {
/*  877 */       if (this.compute == 0 || this.compute == 1) {
/*  878 */         this.currentBlock.frame.execute(paramInt, 0, this.cw, item);
/*      */       } else {
/*      */         int i;
/*      */         
/*  882 */         char c = paramString3.charAt(0);
/*  883 */         switch (paramInt) {
/*      */           case 178:
/*  885 */             i = this.stackSize + ((c == 'D' || c == 'J') ? 2 : 1);
/*      */             break;
/*      */           case 179:
/*  888 */             i = this.stackSize + ((c == 'D' || c == 'J') ? -2 : -1);
/*      */             break;
/*      */           case 180:
/*  891 */             i = this.stackSize + ((c == 'D' || c == 'J') ? 1 : 0);
/*      */             break;
/*      */           
/*      */           default:
/*  895 */             i = this.stackSize + ((c == 'D' || c == 'J') ? -3 : -2);
/*      */             break;
/*      */         } 
/*      */         
/*  899 */         if (i > this.maxStackSize) {
/*  900 */           this.maxStackSize = i;
/*      */         }
/*  902 */         this.stackSize = i;
/*      */       } 
/*      */     }
/*      */     
/*  906 */     this.code.put12(paramInt, item.index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/*  912 */     this.lastCodeOffset = this.code.length;
/*  913 */     Item item = this.cw.newMethodItem(paramString1, paramString2, paramString3, paramBoolean);
/*  914 */     int i = item.intVal;
/*      */     
/*  916 */     if (this.currentBlock != null) {
/*  917 */       if (this.compute == 0 || this.compute == 1) {
/*  918 */         this.currentBlock.frame.execute(paramInt, 0, this.cw, item);
/*      */       } else {
/*      */         int j;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  928 */         if (i == 0) {
/*      */ 
/*      */           
/*  931 */           i = Type.getArgumentsAndReturnSizes(paramString3);
/*      */ 
/*      */           
/*  934 */           item.intVal = i;
/*      */         } 
/*      */         
/*  937 */         if (paramInt == 184) {
/*  938 */           j = this.stackSize - (i >> 2) + (i & 0x3) + 1;
/*      */         } else {
/*  940 */           j = this.stackSize - (i >> 2) + (i & 0x3);
/*      */         } 
/*      */         
/*  943 */         if (j > this.maxStackSize) {
/*  944 */           this.maxStackSize = j;
/*      */         }
/*  946 */         this.stackSize = j;
/*      */       } 
/*      */     }
/*      */     
/*  950 */     if (paramInt == 185) {
/*  951 */       if (i == 0) {
/*  952 */         i = Type.getArgumentsAndReturnSizes(paramString3);
/*  953 */         item.intVal = i;
/*      */       } 
/*  955 */       this.code.put12(185, item.index).put11(i >> 2, 0);
/*      */     } else {
/*  957 */       this.code.put12(paramInt, item.index);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/*  964 */     this.lastCodeOffset = this.code.length;
/*  965 */     Item item = this.cw.newInvokeDynamicItem(paramString1, paramString2, paramHandle, paramVarArgs);
/*  966 */     int i = item.intVal;
/*      */     
/*  968 */     if (this.currentBlock != null) {
/*  969 */       if (this.compute == 0 || this.compute == 1) {
/*  970 */         this.currentBlock.frame.execute(186, 0, this.cw, item);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  980 */         if (i == 0) {
/*      */ 
/*      */           
/*  983 */           i = Type.getArgumentsAndReturnSizes(paramString2);
/*      */ 
/*      */           
/*  986 */           item.intVal = i;
/*      */         } 
/*  988 */         int j = this.stackSize - (i >> 2) + (i & 0x3) + 1;
/*      */ 
/*      */         
/*  991 */         if (j > this.maxStackSize) {
/*  992 */           this.maxStackSize = j;
/*      */         }
/*  994 */         this.stackSize = j;
/*      */       } 
/*      */     }
/*      */     
/*  998 */     this.code.put12(186, item.index);
/*  999 */     this.code.putShort(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int paramInt, Label paramLabel) {
/* 1004 */     boolean bool = (paramInt >= 200) ? true : false;
/* 1005 */     paramInt = bool ? (paramInt - 33) : paramInt;
/* 1006 */     this.lastCodeOffset = this.code.length;
/* 1007 */     Label label = null;
/*      */     
/* 1009 */     if (this.currentBlock != null) {
/* 1010 */       if (this.compute == 0) {
/* 1011 */         this.currentBlock.frame.execute(paramInt, 0, null, null);
/*      */         
/* 1013 */         (paramLabel.getFirst()).status |= 0x10;
/*      */         
/* 1015 */         addSuccessor(0, paramLabel);
/* 1016 */         if (paramInt != 167)
/*      */         {
/* 1018 */           label = new Label();
/*      */         }
/* 1020 */       } else if (this.compute == 1) {
/* 1021 */         this.currentBlock.frame.execute(paramInt, 0, null, null);
/*      */       }
/* 1023 */       else if (paramInt == 168) {
/* 1024 */         if ((paramLabel.status & 0x200) == 0) {
/* 1025 */           paramLabel.status |= 0x200;
/* 1026 */           this.subroutines++;
/*      */         } 
/* 1028 */         this.currentBlock.status |= 0x80;
/* 1029 */         addSuccessor(this.stackSize + 1, paramLabel);
/*      */         
/* 1031 */         label = new Label();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1042 */         this.stackSize += Frame.SIZE[paramInt];
/* 1043 */         addSuccessor(this.stackSize, paramLabel);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1048 */     if ((paramLabel.status & 0x2) != 0 && paramLabel.position - this.code.length < -32768) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1057 */       if (paramInt == 167) {
/* 1058 */         this.code.putByte(200);
/* 1059 */       } else if (paramInt == 168) {
/* 1060 */         this.code.putByte(201);
/*      */       }
/*      */       else {
/*      */         
/* 1064 */         if (label != null) {
/* 1065 */           label.status |= 0x10;
/*      */         }
/* 1067 */         this.code.putByte((paramInt <= 166) ? ((paramInt + 1 ^ 0x1) - 1) : (paramInt ^ 0x1));
/*      */         
/* 1069 */         this.code.putShort(8);
/* 1070 */         this.code.putByte(200);
/*      */       } 
/* 1072 */       paramLabel.put(this, this.code, this.code.length - 1, true);
/* 1073 */     } else if (bool) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1079 */       this.code.putByte(paramInt + 33);
/* 1080 */       paramLabel.put(this, this.code, this.code.length - 1, true);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 1088 */       this.code.putByte(paramInt);
/* 1089 */       paramLabel.put(this, this.code, this.code.length - 1, false);
/*      */     } 
/* 1091 */     if (this.currentBlock != null) {
/* 1092 */       if (label != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1097 */         visitLabel(label);
/*      */       }
/* 1099 */       if (paramInt == 167) {
/* 1100 */         noSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLabel(Label paramLabel) {
/* 1108 */     this.cw.hasAsmInsns |= paramLabel.resolve(this, this.code.length, this.code.data);
/*      */     
/* 1110 */     if ((paramLabel.status & 0x1) != 0) {
/*      */       return;
/*      */     }
/* 1113 */     if (this.compute == 0) {
/* 1114 */       if (this.currentBlock != null) {
/* 1115 */         if (paramLabel.position == this.currentBlock.position) {
/*      */           
/* 1117 */           this.currentBlock.status |= paramLabel.status & 0x10;
/* 1118 */           paramLabel.frame = this.currentBlock.frame;
/*      */           
/*      */           return;
/*      */         } 
/* 1122 */         addSuccessor(0, paramLabel);
/*      */       } 
/*      */       
/* 1125 */       this.currentBlock = paramLabel;
/* 1126 */       if (paramLabel.frame == null) {
/* 1127 */         paramLabel.frame = new Frame();
/* 1128 */         paramLabel.frame.owner = paramLabel;
/*      */       } 
/*      */       
/* 1131 */       if (this.previousBlock != null) {
/* 1132 */         if (paramLabel.position == this.previousBlock.position) {
/* 1133 */           this.previousBlock.status |= paramLabel.status & 0x10;
/* 1134 */           paramLabel.frame = this.previousBlock.frame;
/* 1135 */           this.currentBlock = this.previousBlock;
/*      */           return;
/*      */         } 
/* 1138 */         this.previousBlock.successor = paramLabel;
/*      */       } 
/* 1140 */       this.previousBlock = paramLabel;
/* 1141 */     } else if (this.compute == 1) {
/* 1142 */       if (this.currentBlock == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1147 */         this.currentBlock = paramLabel;
/*      */       }
/*      */       else {
/*      */         
/* 1151 */         this.currentBlock.frame.owner = paramLabel;
/*      */       } 
/* 1153 */     } else if (this.compute == 2) {
/* 1154 */       if (this.currentBlock != null) {
/*      */         
/* 1156 */         this.currentBlock.outputStackMax = this.maxStackSize;
/* 1157 */         addSuccessor(this.stackSize, paramLabel);
/*      */       } 
/*      */       
/* 1160 */       this.currentBlock = paramLabel;
/*      */       
/* 1162 */       this.stackSize = 0;
/* 1163 */       this.maxStackSize = 0;
/*      */       
/* 1165 */       if (this.previousBlock != null) {
/* 1166 */         this.previousBlock.successor = paramLabel;
/*      */       }
/* 1168 */       this.previousBlock = paramLabel;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLdcInsn(Object paramObject) {
/* 1174 */     this.lastCodeOffset = this.code.length;
/* 1175 */     Item item = this.cw.newConstItem(paramObject);
/*      */     
/* 1177 */     if (this.currentBlock != null) {
/* 1178 */       if (this.compute == 0 || this.compute == 1) {
/* 1179 */         this.currentBlock.frame.execute(18, 0, this.cw, item);
/*      */       } else {
/*      */         int j;
/*      */         
/* 1183 */         if (item.type == 5 || item.type == 6) {
/* 1184 */           j = this.stackSize + 2;
/*      */         } else {
/* 1186 */           j = this.stackSize + 1;
/*      */         } 
/*      */         
/* 1189 */         if (j > this.maxStackSize) {
/* 1190 */           this.maxStackSize = j;
/*      */         }
/* 1192 */         this.stackSize = j;
/*      */       } 
/*      */     }
/*      */     
/* 1196 */     int i = item.index;
/* 1197 */     if (item.type == 5 || item.type == 6) {
/* 1198 */       this.code.put12(20, i);
/* 1199 */     } else if (i >= 256) {
/* 1200 */       this.code.put12(19, i);
/*      */     } else {
/* 1202 */       this.code.put11(18, i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIincInsn(int paramInt1, int paramInt2) {
/* 1208 */     this.lastCodeOffset = this.code.length;
/* 1209 */     if (this.currentBlock != null && (
/* 1210 */       this.compute == 0 || this.compute == 1)) {
/* 1211 */       this.currentBlock.frame.execute(132, paramInt1, null, null);
/*      */     }
/*      */     
/* 1214 */     if (this.compute != 3) {
/*      */       
/* 1216 */       int i = paramInt1 + 1;
/* 1217 */       if (i > this.maxLocals) {
/* 1218 */         this.maxLocals = i;
/*      */       }
/*      */     } 
/*      */     
/* 1222 */     if (paramInt1 > 255 || paramInt2 > 127 || paramInt2 < -128) {
/* 1223 */       this.code.putByte(196).put12(132, paramInt1)
/* 1224 */         .putShort(paramInt2);
/*      */     } else {
/* 1226 */       this.code.putByte(132).put11(paramInt1, paramInt2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label... paramVarArgs) {
/* 1233 */     this.lastCodeOffset = this.code.length;
/*      */     
/* 1235 */     int i = this.code.length;
/* 1236 */     this.code.putByte(170);
/* 1237 */     this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1238 */     paramLabel.put(this, this.code, i, true);
/* 1239 */     this.code.putInt(paramInt1).putInt(paramInt2);
/* 1240 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 1241 */       paramVarArgs[b].put(this, this.code, i, true);
/*      */     }
/*      */     
/* 1244 */     visitSwitchInsn(paramLabel, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel) {
/* 1250 */     this.lastCodeOffset = this.code.length;
/*      */     
/* 1252 */     int i = this.code.length;
/* 1253 */     this.code.putByte(171);
/* 1254 */     this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1255 */     paramLabel.put(this, this.code, i, true);
/* 1256 */     this.code.putInt(paramArrayOfLabel.length);
/* 1257 */     for (byte b = 0; b < paramArrayOfLabel.length; b++) {
/* 1258 */       this.code.putInt(paramArrayOfint[b]);
/* 1259 */       paramArrayOfLabel[b].put(this, this.code, i, true);
/*      */     } 
/*      */     
/* 1262 */     visitSwitchInsn(paramLabel, paramArrayOfLabel);
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitSwitchInsn(Label paramLabel, Label[] paramArrayOfLabel) {
/* 1267 */     if (this.currentBlock != null) {
/* 1268 */       if (this.compute == 0) {
/* 1269 */         this.currentBlock.frame.execute(171, 0, null, null);
/*      */         
/* 1271 */         addSuccessor(0, paramLabel);
/* 1272 */         (paramLabel.getFirst()).status |= 0x10;
/* 1273 */         for (byte b = 0; b < paramArrayOfLabel.length; b++) {
/* 1274 */           addSuccessor(0, paramArrayOfLabel[b]);
/* 1275 */           (paramArrayOfLabel[b].getFirst()).status |= 0x10;
/*      */         } 
/*      */       } else {
/*      */         
/* 1279 */         this.stackSize--;
/*      */         
/* 1281 */         addSuccessor(this.stackSize, paramLabel);
/* 1282 */         for (byte b = 0; b < paramArrayOfLabel.length; b++) {
/* 1283 */           addSuccessor(this.stackSize, paramArrayOfLabel[b]);
/*      */         }
/*      */       } 
/*      */       
/* 1287 */       noSuccessor();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/* 1293 */     this.lastCodeOffset = this.code.length;
/* 1294 */     Item item = this.cw.newClassItem(paramString);
/*      */     
/* 1296 */     if (this.currentBlock != null) {
/* 1297 */       if (this.compute == 0 || this.compute == 1) {
/* 1298 */         this.currentBlock.frame.execute(197, paramInt, this.cw, item);
/*      */       }
/*      */       else {
/*      */         
/* 1302 */         this.stackSize += 1 - paramInt;
/*      */       } 
/*      */     }
/*      */     
/* 1306 */     this.code.put12(197, item.index).putByte(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 1315 */     ByteVector byteVector = new ByteVector();
/*      */     
/* 1317 */     paramInt = paramInt & 0xFF0000FF | this.lastCodeOffset << 8;
/* 1318 */     AnnotationWriter.putTarget(paramInt, paramTypePath, byteVector);
/*      */     
/* 1320 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/* 1321 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
/*      */     
/* 1323 */     if (paramBoolean) {
/* 1324 */       annotationWriter.next = this.ctanns;
/* 1325 */       this.ctanns = annotationWriter;
/*      */     } else {
/* 1327 */       annotationWriter.next = this.ictanns;
/* 1328 */       this.ictanns = annotationWriter;
/*      */     } 
/* 1330 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/* 1336 */     this.handlerCount++;
/* 1337 */     Handler handler = new Handler();
/* 1338 */     handler.start = paramLabel1;
/* 1339 */     handler.end = paramLabel2;
/* 1340 */     handler.handler = paramLabel3;
/* 1341 */     handler.desc = paramString;
/* 1342 */     handler.type = (paramString != null) ? this.cw.newClass(paramString) : 0;
/* 1343 */     if (this.lastHandler == null) {
/* 1344 */       this.firstHandler = handler;
/*      */     } else {
/* 1346 */       this.lastHandler.next = handler;
/*      */     } 
/* 1348 */     this.lastHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 1357 */     ByteVector byteVector = new ByteVector();
/*      */     
/* 1359 */     AnnotationWriter.putTarget(paramInt, paramTypePath, byteVector);
/*      */     
/* 1361 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/* 1362 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
/*      */     
/* 1364 */     if (paramBoolean) {
/* 1365 */       annotationWriter.next = this.ctanns;
/* 1366 */       this.ctanns = annotationWriter;
/*      */     } else {
/* 1368 */       annotationWriter.next = this.ictanns;
/* 1369 */       this.ictanns = annotationWriter;
/*      */     } 
/* 1371 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/* 1378 */     if (paramString3 != null) {
/* 1379 */       if (this.localVarType == null) {
/* 1380 */         this.localVarType = new ByteVector();
/*      */       }
/* 1382 */       this.localVarTypeCount++;
/* 1383 */       this.localVarType.putShort(paramLabel1.position)
/* 1384 */         .putShort(paramLabel2.position - paramLabel1.position)
/* 1385 */         .putShort(this.cw.newUTF8(paramString1)).putShort(this.cw.newUTF8(paramString3))
/* 1386 */         .putShort(paramInt);
/*      */     } 
/* 1388 */     if (this.localVar == null) {
/* 1389 */       this.localVar = new ByteVector();
/*      */     }
/* 1391 */     this.localVarCount++;
/* 1392 */     this.localVar.putShort(paramLabel1.position)
/* 1393 */       .putShort(paramLabel2.position - paramLabel1.position)
/* 1394 */       .putShort(this.cw.newUTF8(paramString1)).putShort(this.cw.newUTF8(paramString2))
/* 1395 */       .putShort(paramInt);
/* 1396 */     if (this.compute != 3) {
/*      */       
/* 1398 */       char c = paramString2.charAt(0);
/* 1399 */       int i = paramInt + ((c == 'J' || c == 'D') ? 2 : 1);
/* 1400 */       if (i > this.maxLocals) {
/* 1401 */         this.maxLocals = i;
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
/*      */   public AnnotationVisitor visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/* 1413 */     ByteVector byteVector = new ByteVector();
/*      */     
/* 1415 */     byteVector.putByte(paramInt >>> 24).putShort(paramArrayOfLabel1.length); int i;
/* 1416 */     for (i = 0; i < paramArrayOfLabel1.length; i++) {
/* 1417 */       byteVector.putShort((paramArrayOfLabel1[i]).position)
/* 1418 */         .putShort((paramArrayOfLabel2[i]).position - (paramArrayOfLabel1[i]).position)
/* 1419 */         .putShort(paramArrayOfint[i]);
/*      */     }
/* 1421 */     if (paramTypePath == null) {
/* 1422 */       byteVector.putByte(0);
/*      */     } else {
/* 1424 */       i = paramTypePath.b[paramTypePath.offset] * 2 + 1;
/* 1425 */       byteVector.putByteArray(paramTypePath.b, paramTypePath.offset, i);
/*      */     } 
/*      */     
/* 1428 */     byteVector.putShort(this.cw.newUTF8(paramString)).putShort(0);
/* 1429 */     AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
/*      */     
/* 1431 */     if (paramBoolean) {
/* 1432 */       annotationWriter.next = this.ctanns;
/* 1433 */       this.ctanns = annotationWriter;
/*      */     } else {
/* 1435 */       annotationWriter.next = this.ictanns;
/* 1436 */       this.ictanns = annotationWriter;
/*      */     } 
/* 1438 */     return annotationWriter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int paramInt, Label paramLabel) {
/* 1443 */     if (this.lineNumber == null) {
/* 1444 */       this.lineNumber = new ByteVector();
/*      */     }
/* 1446 */     this.lineNumberCount++;
/* 1447 */     this.lineNumber.putShort(paramLabel.position);
/* 1448 */     this.lineNumber.putShort(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMaxs(int paramInt1, int paramInt2) {
/* 1453 */     if (this.compute == 0) {
/*      */       
/* 1455 */       Handler handler = this.firstHandler;
/* 1456 */       while (handler != null) {
/* 1457 */         Label label3 = handler.start.getFirst();
/* 1458 */         Label label4 = handler.handler.getFirst();
/* 1459 */         Label label5 = handler.end.getFirst();
/*      */         
/* 1461 */         String str = (handler.desc == null) ? "java/lang/Throwable" : handler.desc;
/*      */         
/* 1463 */         int j = 0x1700000 | this.cw.addType(str);
/*      */         
/* 1465 */         label4.status |= 0x10;
/*      */         
/* 1467 */         while (label3 != label5) {
/*      */           
/* 1469 */           Edge edge = new Edge();
/* 1470 */           edge.info = j;
/* 1471 */           edge.successor = label4;
/*      */           
/* 1473 */           edge.next = label3.successors;
/* 1474 */           label3.successors = edge;
/*      */           
/* 1476 */           label3 = label3.successor;
/*      */         } 
/* 1478 */         handler = handler.next;
/*      */       } 
/*      */ 
/*      */       
/* 1482 */       Frame frame = this.labels.frame;
/* 1483 */       frame.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), this.maxLocals);
/*      */       
/* 1485 */       visitFrame(frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1493 */       int i = 0;
/* 1494 */       Label label1 = this.labels;
/* 1495 */       while (label1 != null) {
/*      */         
/* 1497 */         Label label = label1;
/* 1498 */         label1 = label1.next;
/* 1499 */         label.next = null;
/* 1500 */         frame = label.frame;
/*      */         
/* 1502 */         if ((label.status & 0x10) != 0) {
/* 1503 */           label.status |= 0x20;
/*      */         }
/*      */         
/* 1506 */         label.status |= 0x40;
/*      */         
/* 1508 */         int j = frame.inputStack.length + label.outputStackMax;
/* 1509 */         if (j > i) {
/* 1510 */           i = j;
/*      */         }
/*      */         
/* 1513 */         Edge edge = label.successors;
/* 1514 */         while (edge != null) {
/* 1515 */           Label label3 = edge.successor.getFirst();
/* 1516 */           boolean bool = frame.merge(this.cw, label3.frame, edge.info);
/* 1517 */           if (bool && label3.next == null) {
/*      */ 
/*      */             
/* 1520 */             label3.next = label1;
/* 1521 */             label1 = label3;
/*      */           } 
/* 1523 */           edge = edge.next;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1528 */       Label label2 = this.labels;
/* 1529 */       while (label2 != null) {
/* 1530 */         frame = label2.frame;
/* 1531 */         if ((label2.status & 0x20) != 0) {
/* 1532 */           visitFrame(frame);
/*      */         }
/* 1534 */         if ((label2.status & 0x40) == 0) {
/*      */           
/* 1536 */           Label label = label2.successor;
/* 1537 */           int j = label2.position;
/* 1538 */           int k = ((label == null) ? this.code.length : label.position) - 1;
/*      */           
/* 1540 */           if (k >= j) {
/* 1541 */             i = Math.max(i, 1);
/*      */             int m;
/* 1543 */             for (m = j; m < k; m++) {
/* 1544 */               this.code.data[m] = 0;
/*      */             }
/* 1546 */             this.code.data[k] = -65;
/*      */             
/* 1548 */             m = startFrame(j, 0, 1);
/* 1549 */             this.frame[m] = 0x1700000 | this.cw
/* 1550 */               .addType("java/lang/Throwable");
/* 1551 */             endFrame();
/*      */ 
/*      */             
/* 1554 */             this.firstHandler = Handler.remove(this.firstHandler, label2, label);
/*      */           } 
/*      */         } 
/* 1557 */         label2 = label2.successor;
/*      */       } 
/*      */       
/* 1560 */       handler = this.firstHandler;
/* 1561 */       this.handlerCount = 0;
/* 1562 */       while (handler != null) {
/* 1563 */         this.handlerCount++;
/* 1564 */         handler = handler.next;
/*      */       } 
/*      */       
/* 1567 */       this.maxStack = i;
/* 1568 */     } else if (this.compute == 2) {
/*      */       
/* 1570 */       Handler handler = this.firstHandler;
/* 1571 */       while (handler != null) {
/* 1572 */         Label label1 = handler.start;
/* 1573 */         Label label2 = handler.handler;
/* 1574 */         Label label3 = handler.end;
/*      */         
/* 1576 */         while (label1 != label3) {
/*      */           
/* 1578 */           Edge edge = new Edge();
/* 1579 */           edge.info = Integer.MAX_VALUE;
/* 1580 */           edge.successor = label2;
/*      */           
/* 1582 */           if ((label1.status & 0x80) == 0) {
/* 1583 */             edge.next = label1.successors;
/* 1584 */             label1.successors = edge;
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1589 */             edge.next = label1.successors.next.next;
/* 1590 */             label1.successors.next.next = edge;
/*      */           } 
/*      */           
/* 1593 */           label1 = label1.successor;
/*      */         } 
/* 1595 */         handler = handler.next;
/*      */       } 
/*      */       
/* 1598 */       if (this.subroutines > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1605 */         byte b = 0;
/* 1606 */         this.labels.visitSubroutine(null, 1L, this.subroutines);
/*      */         
/* 1608 */         Label label1 = this.labels;
/* 1609 */         while (label1 != null) {
/* 1610 */           if ((label1.status & 0x80) != 0) {
/*      */             
/* 1612 */             Label label2 = label1.successors.next.successor;
/*      */             
/* 1614 */             if ((label2.status & 0x400) == 0) {
/*      */               
/* 1616 */               b++;
/* 1617 */               label2.visitSubroutine(null, b / 32L << 32L | 1L << b % 32, this.subroutines);
/*      */             } 
/*      */           } 
/*      */           
/* 1621 */           label1 = label1.successor;
/*      */         } 
/*      */         
/* 1624 */         label1 = this.labels;
/* 1625 */         while (label1 != null) {
/* 1626 */           if ((label1.status & 0x80) != 0) {
/* 1627 */             Label label2 = this.labels;
/* 1628 */             while (label2 != null) {
/* 1629 */               label2.status &= 0xFFFFF7FF;
/* 1630 */               label2 = label2.successor;
/*      */             } 
/*      */             
/* 1633 */             Label label3 = label1.successors.next.successor;
/* 1634 */             label3.visitSubroutine(label1, 0L, this.subroutines);
/*      */           } 
/* 1636 */           label1 = label1.successor;
/*      */         } 
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
/* 1650 */       int i = 0;
/* 1651 */       Label label = this.labels;
/* 1652 */       while (label != null) {
/*      */         
/* 1654 */         Label label1 = label;
/* 1655 */         label = label.next;
/*      */         
/* 1657 */         int j = label1.inputStackTop;
/* 1658 */         int k = j + label1.outputStackMax;
/*      */         
/* 1660 */         if (k > i) {
/* 1661 */           i = k;
/*      */         }
/*      */         
/* 1664 */         Edge edge = label1.successors;
/* 1665 */         if ((label1.status & 0x80) != 0)
/*      */         {
/* 1667 */           edge = edge.next;
/*      */         }
/* 1669 */         while (edge != null) {
/* 1670 */           label1 = edge.successor;
/*      */           
/* 1672 */           if ((label1.status & 0x8) == 0) {
/*      */             
/* 1674 */             label1.inputStackTop = (edge.info == Integer.MAX_VALUE) ? 1 : (j + edge.info);
/*      */ 
/*      */             
/* 1677 */             label1.status |= 0x8;
/* 1678 */             label1.next = label;
/* 1679 */             label = label1;
/*      */           } 
/* 1681 */           edge = edge.next;
/*      */         } 
/*      */       } 
/* 1684 */       this.maxStack = Math.max(paramInt1, i);
/*      */     } else {
/* 1686 */       this.maxStack = paramInt1;
/* 1687 */       this.maxLocals = paramInt2;
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
/*      */   public void visitEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addSuccessor(int paramInt, Label paramLabel) {
/* 1709 */     Edge edge = new Edge();
/* 1710 */     edge.info = paramInt;
/* 1711 */     edge.successor = paramLabel;
/*      */     
/* 1713 */     edge.next = this.currentBlock.successors;
/* 1714 */     this.currentBlock.successors = edge;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void noSuccessor() {
/* 1722 */     if (this.compute == 0) {
/* 1723 */       Label label = new Label();
/* 1724 */       label.frame = new Frame();
/* 1725 */       label.frame.owner = label;
/* 1726 */       label.resolve(this, this.code.length, this.code.data);
/* 1727 */       this.previousBlock.successor = label;
/* 1728 */       this.previousBlock = label;
/*      */     } else {
/* 1730 */       this.currentBlock.outputStackMax = this.maxStackSize;
/*      */     } 
/* 1732 */     if (this.compute != 1) {
/* 1733 */       this.currentBlock = null;
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
/*      */   private void visitFrame(Frame paramFrame) {
/* 1749 */     byte b1 = 0;
/* 1750 */     int i = 0;
/* 1751 */     byte b2 = 0;
/* 1752 */     int[] arrayOfInt1 = paramFrame.inputLocals;
/* 1753 */     int[] arrayOfInt2 = paramFrame.inputStack;
/*      */     
/*      */     byte b3;
/* 1756 */     for (b3 = 0; b3 < arrayOfInt1.length; b3++) {
/* 1757 */       int k = arrayOfInt1[b3];
/* 1758 */       if (k == 16777216) {
/* 1759 */         b1++;
/*      */       } else {
/* 1761 */         i += b1 + 1;
/* 1762 */         b1 = 0;
/*      */       } 
/* 1764 */       if (k == 16777220 || k == 16777219) {
/* 1765 */         b3++;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1770 */     for (b3 = 0; b3 < arrayOfInt2.length; b3++) {
/* 1771 */       int k = arrayOfInt2[b3];
/* 1772 */       b2++;
/* 1773 */       if (k == 16777220 || k == 16777219) {
/* 1774 */         b3++;
/*      */       }
/*      */     } 
/*      */     
/* 1778 */     int j = startFrame(paramFrame.owner.position, i, b2);
/* 1779 */     for (b3 = 0; i > 0; b3++, i--) {
/* 1780 */       int k = arrayOfInt1[b3];
/* 1781 */       this.frame[j++] = k;
/* 1782 */       if (k == 16777220 || k == 16777219) {
/* 1783 */         b3++;
/*      */       }
/*      */     } 
/* 1786 */     for (b3 = 0; b3 < arrayOfInt2.length; b3++) {
/* 1787 */       int k = arrayOfInt2[b3];
/* 1788 */       this.frame[j++] = k;
/* 1789 */       if (k == 16777220 || k == 16777219) {
/* 1790 */         b3++;
/*      */       }
/*      */     } 
/* 1793 */     endFrame();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitImplicitFirstFrame() {
/* 1801 */     int i = startFrame(0, this.descriptor.length() + 1, 0);
/* 1802 */     if ((this.access & 0x8) == 0) {
/* 1803 */       if ((this.access & 0x80000) == 0) {
/* 1804 */         this.frame[i++] = 0x1700000 | this.cw.addType(this.cw.thisName);
/*      */       } else {
/* 1806 */         this.frame[i++] = 6;
/*      */       } 
/*      */     }
/* 1809 */     byte b = 1;
/*      */     while (true) {
/* 1811 */       byte b1 = b;
/* 1812 */       switch (this.descriptor.charAt(b++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 1818 */           this.frame[i++] = 1;
/*      */           continue;
/*      */         case 'F':
/* 1821 */           this.frame[i++] = 2;
/*      */           continue;
/*      */         case 'J':
/* 1824 */           this.frame[i++] = 4;
/*      */           continue;
/*      */         case 'D':
/* 1827 */           this.frame[i++] = 3;
/*      */           continue;
/*      */         case '[':
/* 1830 */           while (this.descriptor.charAt(b) == '[') {
/* 1831 */             b++;
/*      */           }
/* 1833 */           if (this.descriptor.charAt(b) == 'L') {
/* 1834 */             b++;
/* 1835 */             while (this.descriptor.charAt(b) != ';') {
/* 1836 */               b++;
/*      */             }
/*      */           } 
/* 1839 */           this.frame[i++] = 0x1700000 | this.cw
/* 1840 */             .addType(this.descriptor.substring(b1, ++b));
/*      */           continue;
/*      */         case 'L':
/* 1843 */           while (this.descriptor.charAt(b) != ';') {
/* 1844 */             b++;
/*      */           }
/* 1846 */           this.frame[i++] = 0x1700000 | this.cw
/* 1847 */             .addType(this.descriptor.substring(b1 + 1, b++));
/*      */           continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1853 */     this.frame[1] = i - 3;
/* 1854 */     endFrame();
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
/*      */   private int startFrame(int paramInt1, int paramInt2, int paramInt3) {
/* 1869 */     int i = 3 + paramInt2 + paramInt3;
/* 1870 */     if (this.frame == null || this.frame.length < i) {
/* 1871 */       this.frame = new int[i];
/*      */     }
/* 1873 */     this.frame[0] = paramInt1;
/* 1874 */     this.frame[1] = paramInt2;
/* 1875 */     this.frame[2] = paramInt3;
/* 1876 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endFrame() {
/* 1884 */     if (this.previousFrame != null) {
/* 1885 */       if (this.stackMap == null) {
/* 1886 */         this.stackMap = new ByteVector();
/*      */       }
/* 1888 */       writeFrame();
/* 1889 */       this.frameCount++;
/*      */     } 
/* 1891 */     this.previousFrame = this.frame;
/* 1892 */     this.frame = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeFrame() {
/* 1900 */     int n, i = this.frame[1];
/* 1901 */     int j = this.frame[2];
/* 1902 */     if ((this.cw.version & 0xFFFF) < 50) {
/* 1903 */       this.stackMap.putShort(this.frame[0]).putShort(i);
/* 1904 */       writeFrameTypes(3, 3 + i);
/* 1905 */       this.stackMap.putShort(j);
/* 1906 */       writeFrameTypes(3 + i, 3 + i + j);
/*      */       return;
/*      */     } 
/* 1909 */     int k = this.previousFrame[1];
/* 1910 */     char c = 'ÿ';
/* 1911 */     int m = 0;
/*      */     
/* 1913 */     if (this.frameCount == 0) {
/* 1914 */       n = this.frame[0];
/*      */     } else {
/* 1916 */       n = this.frame[0] - this.previousFrame[0] - 1;
/*      */     } 
/* 1918 */     if (j == 0) {
/* 1919 */       m = i - k;
/* 1920 */       switch (m) {
/*      */         case -3:
/*      */         case -2:
/*      */         case -1:
/* 1924 */           c = 'ø';
/* 1925 */           k = i;
/*      */           break;
/*      */         case 0:
/* 1928 */           c = (n < 64) ? Character.MIN_VALUE : 'û';
/*      */           break;
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/* 1933 */           c = 'ü';
/*      */           break;
/*      */       } 
/* 1936 */     } else if (i == k && j == 1) {
/* 1937 */       c = (n < 63) ? '@' : '÷';
/*      */     } 
/*      */     
/* 1940 */     if (c != 'ÿ') {
/*      */       
/* 1942 */       byte b1 = 3;
/* 1943 */       for (byte b2 = 0; b2 < k; b2++) {
/* 1944 */         if (this.frame[b1] != this.previousFrame[b1]) {
/* 1945 */           c = 'ÿ';
/*      */           break;
/*      */         } 
/* 1948 */         b1++;
/*      */       } 
/*      */     } 
/* 1951 */     switch (c) {
/*      */       case '\000':
/* 1953 */         this.stackMap.putByte(n);
/*      */         return;
/*      */       case '@':
/* 1956 */         this.stackMap.putByte(64 + n);
/* 1957 */         writeFrameTypes(3 + i, 4 + i);
/*      */         return;
/*      */       case '÷':
/* 1960 */         this.stackMap.putByte(247).putShort(n);
/*      */         
/* 1962 */         writeFrameTypes(3 + i, 4 + i);
/*      */         return;
/*      */       case 'û':
/* 1965 */         this.stackMap.putByte(251).putShort(n);
/*      */         return;
/*      */       case 'ø':
/* 1968 */         this.stackMap.putByte(251 + m).putShort(n);
/*      */         return;
/*      */       case 'ü':
/* 1971 */         this.stackMap.putByte(251 + m).putShort(n);
/* 1972 */         writeFrameTypes(3 + k, 3 + i);
/*      */         return;
/*      */     } 
/*      */     
/* 1976 */     this.stackMap.putByte(255).putShort(n).putShort(i);
/* 1977 */     writeFrameTypes(3, 3 + i);
/* 1978 */     this.stackMap.putShort(j);
/* 1979 */     writeFrameTypes(3 + i, 3 + i + j);
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
/*      */   private void writeFrameTypes(int paramInt1, int paramInt2) {
/* 1995 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 1996 */       int j = this.frame[i];
/* 1997 */       int k = j & 0xF0000000;
/* 1998 */       if (k == 0) {
/* 1999 */         int m = j & 0xFFFFF;
/* 2000 */         switch (j & 0xFF00000) {
/*      */           case 24117248:
/* 2002 */             this.stackMap.putByte(7).putShort(this.cw
/* 2003 */                 .newClass((this.cw.typeTable[m]).strVal1));
/*      */             break;
/*      */           case 25165824:
/* 2006 */             this.stackMap.putByte(8).putShort((this.cw.typeTable[m]).intVal);
/*      */             break;
/*      */           default:
/* 2009 */             this.stackMap.putByte(m); break;
/*      */         } 
/*      */       } else {
/* 2012 */         StringBuilder stringBuilder = new StringBuilder();
/* 2013 */         k >>= 28;
/* 2014 */         while (k-- > 0) {
/* 2015 */           stringBuilder.append('[');
/*      */         }
/* 2017 */         if ((j & 0xFF00000) == 24117248) {
/* 2018 */           stringBuilder.append('L');
/* 2019 */           stringBuilder.append((this.cw.typeTable[j & 0xFFFFF]).strVal1);
/* 2020 */           stringBuilder.append(';');
/*      */         } else {
/* 2022 */           switch (j & 0xF) {
/*      */             case 1:
/* 2024 */               stringBuilder.append('I');
/*      */               break;
/*      */             case 2:
/* 2027 */               stringBuilder.append('F');
/*      */               break;
/*      */             case 3:
/* 2030 */               stringBuilder.append('D');
/*      */               break;
/*      */             case 9:
/* 2033 */               stringBuilder.append('Z');
/*      */               break;
/*      */             case 10:
/* 2036 */               stringBuilder.append('B');
/*      */               break;
/*      */             case 11:
/* 2039 */               stringBuilder.append('C');
/*      */               break;
/*      */             case 12:
/* 2042 */               stringBuilder.append('S');
/*      */               break;
/*      */             default:
/* 2045 */               stringBuilder.append('J'); break;
/*      */           } 
/*      */         } 
/* 2048 */         this.stackMap.putByte(7).putShort(this.cw.newClass(stringBuilder.toString()));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeFrameType(Object paramObject) {
/* 2054 */     if (paramObject instanceof String) {
/* 2055 */       this.stackMap.putByte(7).putShort(this.cw.newClass((String)paramObject));
/* 2056 */     } else if (paramObject instanceof Integer) {
/* 2057 */       this.stackMap.putByte(((Integer)paramObject).intValue());
/*      */     } else {
/* 2059 */       this.stackMap.putByte(8).putShort(((Label)paramObject).position);
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
/*      */   final int getSize() {
/* 2073 */     if (this.classReaderOffset != 0) {
/* 2074 */       return 6 + this.classReaderLength;
/*      */     }
/* 2076 */     int i = 8;
/* 2077 */     if (this.code.length > 0) {
/* 2078 */       if (this.code.length > 65535) {
/* 2079 */         throw new RuntimeException("Method code too large!");
/*      */       }
/* 2081 */       this.cw.newUTF8("Code");
/* 2082 */       i += 18 + this.code.length + 8 * this.handlerCount;
/* 2083 */       if (this.localVar != null) {
/* 2084 */         this.cw.newUTF8("LocalVariableTable");
/* 2085 */         i += 8 + this.localVar.length;
/*      */       } 
/* 2087 */       if (this.localVarType != null) {
/* 2088 */         this.cw.newUTF8("LocalVariableTypeTable");
/* 2089 */         i += 8 + this.localVarType.length;
/*      */       } 
/* 2091 */       if (this.lineNumber != null) {
/* 2092 */         this.cw.newUTF8("LineNumberTable");
/* 2093 */         i += 8 + this.lineNumber.length;
/*      */       } 
/* 2095 */       if (this.stackMap != null) {
/* 2096 */         boolean bool = ((this.cw.version & 0xFFFF) >= 50) ? true : false;
/* 2097 */         this.cw.newUTF8(bool ? "StackMapTable" : "StackMap");
/* 2098 */         i += 8 + this.stackMap.length;
/*      */       } 
/* 2100 */       if (this.ctanns != null) {
/* 2101 */         this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
/* 2102 */         i += 8 + this.ctanns.getSize();
/*      */       } 
/* 2104 */       if (this.ictanns != null) {
/* 2105 */         this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
/* 2106 */         i += 8 + this.ictanns.getSize();
/*      */       } 
/* 2108 */       if (this.cattrs != null) {
/* 2109 */         i += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */     } 
/*      */     
/* 2113 */     if (this.exceptionCount > 0) {
/* 2114 */       this.cw.newUTF8("Exceptions");
/* 2115 */       i += 8 + 2 * this.exceptionCount;
/*      */     } 
/* 2117 */     if ((this.access & 0x1000) != 0 && ((
/* 2118 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
/*      */       
/* 2120 */       this.cw.newUTF8("Synthetic");
/* 2121 */       i += 6;
/*      */     } 
/*      */     
/* 2124 */     if ((this.access & 0x20000) != 0) {
/* 2125 */       this.cw.newUTF8("Deprecated");
/* 2126 */       i += 6;
/*      */     } 
/* 2128 */     if (this.signature != null) {
/* 2129 */       this.cw.newUTF8("Signature");
/* 2130 */       this.cw.newUTF8(this.signature);
/* 2131 */       i += 8;
/*      */     } 
/* 2133 */     if (this.methodParameters != null) {
/* 2134 */       this.cw.newUTF8("MethodParameters");
/* 2135 */       i += 7 + this.methodParameters.length;
/*      */     } 
/* 2137 */     if (this.annd != null) {
/* 2138 */       this.cw.newUTF8("AnnotationDefault");
/* 2139 */       i += 6 + this.annd.length;
/*      */     } 
/* 2141 */     if (this.anns != null) {
/* 2142 */       this.cw.newUTF8("RuntimeVisibleAnnotations");
/* 2143 */       i += 8 + this.anns.getSize();
/*      */     } 
/* 2145 */     if (this.ianns != null) {
/* 2146 */       this.cw.newUTF8("RuntimeInvisibleAnnotations");
/* 2147 */       i += 8 + this.ianns.getSize();
/*      */     } 
/* 2149 */     if (this.tanns != null) {
/* 2150 */       this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
/* 2151 */       i += 8 + this.tanns.getSize();
/*      */     } 
/* 2153 */     if (this.itanns != null) {
/* 2154 */       this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
/* 2155 */       i += 8 + this.itanns.getSize();
/*      */     } 
/* 2157 */     if (this.panns != null) {
/* 2158 */       this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
/* 2159 */       i += 7 + 2 * (this.panns.length - this.synthetics);
/* 2160 */       for (int j = this.panns.length - 1; j >= this.synthetics; j--) {
/* 2161 */         i += (this.panns[j] == null) ? 0 : this.panns[j].getSize();
/*      */       }
/*      */     } 
/* 2164 */     if (this.ipanns != null) {
/* 2165 */       this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
/* 2166 */       i += 7 + 2 * (this.ipanns.length - this.synthetics);
/* 2167 */       for (int j = this.ipanns.length - 1; j >= this.synthetics; j--) {
/* 2168 */         i += (this.ipanns[j] == null) ? 0 : this.ipanns[j].getSize();
/*      */       }
/*      */     } 
/* 2171 */     if (this.attrs != null) {
/* 2172 */       i += this.attrs.getSize(this.cw, null, 0, -1, -1);
/*      */     }
/* 2174 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void put(ByteVector paramByteVector) {
/* 2185 */     byte b = 64;
/* 2186 */     int i = 0xE0000 | (this.access & 0x40000) / 64;
/*      */ 
/*      */     
/* 2189 */     paramByteVector.putShort(this.access & (i ^ 0xFFFFFFFF)).putShort(this.name).putShort(this.desc);
/* 2190 */     if (this.classReaderOffset != 0) {
/* 2191 */       paramByteVector.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
/*      */       return;
/*      */     } 
/* 2194 */     int j = 0;
/* 2195 */     if (this.code.length > 0) {
/* 2196 */       j++;
/*      */     }
/* 2198 */     if (this.exceptionCount > 0) {
/* 2199 */       j++;
/*      */     }
/* 2201 */     if ((this.access & 0x1000) != 0 && ((
/* 2202 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*      */     {
/* 2204 */       j++;
/*      */     }
/*      */     
/* 2207 */     if ((this.access & 0x20000) != 0) {
/* 2208 */       j++;
/*      */     }
/* 2210 */     if (this.signature != null) {
/* 2211 */       j++;
/*      */     }
/* 2213 */     if (this.methodParameters != null) {
/* 2214 */       j++;
/*      */     }
/* 2216 */     if (this.annd != null) {
/* 2217 */       j++;
/*      */     }
/* 2219 */     if (this.anns != null) {
/* 2220 */       j++;
/*      */     }
/* 2222 */     if (this.ianns != null) {
/* 2223 */       j++;
/*      */     }
/* 2225 */     if (this.tanns != null) {
/* 2226 */       j++;
/*      */     }
/* 2228 */     if (this.itanns != null) {
/* 2229 */       j++;
/*      */     }
/* 2231 */     if (this.panns != null) {
/* 2232 */       j++;
/*      */     }
/* 2234 */     if (this.ipanns != null) {
/* 2235 */       j++;
/*      */     }
/* 2237 */     if (this.attrs != null) {
/* 2238 */       j += this.attrs.getCount();
/*      */     }
/* 2240 */     paramByteVector.putShort(j);
/* 2241 */     if (this.code.length > 0) {
/* 2242 */       int k = 12 + this.code.length + 8 * this.handlerCount;
/* 2243 */       if (this.localVar != null) {
/* 2244 */         k += 8 + this.localVar.length;
/*      */       }
/* 2246 */       if (this.localVarType != null) {
/* 2247 */         k += 8 + this.localVarType.length;
/*      */       }
/* 2249 */       if (this.lineNumber != null) {
/* 2250 */         k += 8 + this.lineNumber.length;
/*      */       }
/* 2252 */       if (this.stackMap != null) {
/* 2253 */         k += 8 + this.stackMap.length;
/*      */       }
/* 2255 */       if (this.ctanns != null) {
/* 2256 */         k += 8 + this.ctanns.getSize();
/*      */       }
/* 2258 */       if (this.ictanns != null) {
/* 2259 */         k += 8 + this.ictanns.getSize();
/*      */       }
/* 2261 */       if (this.cattrs != null) {
/* 2262 */         k += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */       
/* 2265 */       paramByteVector.putShort(this.cw.newUTF8("Code")).putInt(k);
/* 2266 */       paramByteVector.putShort(this.maxStack).putShort(this.maxLocals);
/* 2267 */       paramByteVector.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
/* 2268 */       paramByteVector.putShort(this.handlerCount);
/* 2269 */       if (this.handlerCount > 0) {
/* 2270 */         Handler handler = this.firstHandler;
/* 2271 */         while (handler != null) {
/* 2272 */           paramByteVector.putShort(handler.start.position).putShort(handler.end.position)
/* 2273 */             .putShort(handler.handler.position).putShort(handler.type);
/* 2274 */           handler = handler.next;
/*      */         } 
/*      */       } 
/* 2277 */       j = 0;
/* 2278 */       if (this.localVar != null) {
/* 2279 */         j++;
/*      */       }
/* 2281 */       if (this.localVarType != null) {
/* 2282 */         j++;
/*      */       }
/* 2284 */       if (this.lineNumber != null) {
/* 2285 */         j++;
/*      */       }
/* 2287 */       if (this.stackMap != null) {
/* 2288 */         j++;
/*      */       }
/* 2290 */       if (this.ctanns != null) {
/* 2291 */         j++;
/*      */       }
/* 2293 */       if (this.ictanns != null) {
/* 2294 */         j++;
/*      */       }
/* 2296 */       if (this.cattrs != null) {
/* 2297 */         j += this.cattrs.getCount();
/*      */       }
/* 2299 */       paramByteVector.putShort(j);
/* 2300 */       if (this.localVar != null) {
/* 2301 */         paramByteVector.putShort(this.cw.newUTF8("LocalVariableTable"));
/* 2302 */         paramByteVector.putInt(this.localVar.length + 2).putShort(this.localVarCount);
/* 2303 */         paramByteVector.putByteArray(this.localVar.data, 0, this.localVar.length);
/*      */       } 
/* 2305 */       if (this.localVarType != null) {
/* 2306 */         paramByteVector.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
/* 2307 */         paramByteVector.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
/* 2308 */         paramByteVector.putByteArray(this.localVarType.data, 0, this.localVarType.length);
/*      */       } 
/* 2310 */       if (this.lineNumber != null) {
/* 2311 */         paramByteVector.putShort(this.cw.newUTF8("LineNumberTable"));
/* 2312 */         paramByteVector.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
/* 2313 */         paramByteVector.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
/*      */       } 
/* 2315 */       if (this.stackMap != null) {
/* 2316 */         boolean bool = ((this.cw.version & 0xFFFF) >= 50) ? true : false;
/* 2317 */         paramByteVector.putShort(this.cw.newUTF8(bool ? "StackMapTable" : "StackMap"));
/* 2318 */         paramByteVector.putInt(this.stackMap.length + 2).putShort(this.frameCount);
/* 2319 */         paramByteVector.putByteArray(this.stackMap.data, 0, this.stackMap.length);
/*      */       } 
/* 2321 */       if (this.ctanns != null) {
/* 2322 */         paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
/* 2323 */         this.ctanns.put(paramByteVector);
/*      */       } 
/* 2325 */       if (this.ictanns != null) {
/* 2326 */         paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 2327 */         this.ictanns.put(paramByteVector);
/*      */       } 
/* 2329 */       if (this.cattrs != null) {
/* 2330 */         this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, paramByteVector);
/*      */       }
/*      */     } 
/* 2333 */     if (this.exceptionCount > 0) {
/* 2334 */       paramByteVector.putShort(this.cw.newUTF8("Exceptions")).putInt(2 * this.exceptionCount + 2);
/*      */       
/* 2336 */       paramByteVector.putShort(this.exceptionCount);
/* 2337 */       for (byte b1 = 0; b1 < this.exceptionCount; b1++) {
/* 2338 */         paramByteVector.putShort(this.exceptions[b1]);
/*      */       }
/*      */     } 
/* 2341 */     if ((this.access & 0x1000) != 0 && ((
/* 2342 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*      */     {
/* 2344 */       paramByteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
/*      */     }
/*      */     
/* 2347 */     if ((this.access & 0x20000) != 0) {
/* 2348 */       paramByteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
/*      */     }
/* 2350 */     if (this.signature != null) {
/* 2351 */       paramByteVector.putShort(this.cw.newUTF8("Signature")).putInt(2)
/* 2352 */         .putShort(this.cw.newUTF8(this.signature));
/*      */     }
/* 2354 */     if (this.methodParameters != null) {
/* 2355 */       paramByteVector.putShort(this.cw.newUTF8("MethodParameters"));
/* 2356 */       paramByteVector.putInt(this.methodParameters.length + 1).putByte(this.methodParametersCount);
/*      */       
/* 2358 */       paramByteVector.putByteArray(this.methodParameters.data, 0, this.methodParameters.length);
/*      */     } 
/* 2360 */     if (this.annd != null) {
/* 2361 */       paramByteVector.putShort(this.cw.newUTF8("AnnotationDefault"));
/* 2362 */       paramByteVector.putInt(this.annd.length);
/* 2363 */       paramByteVector.putByteArray(this.annd.data, 0, this.annd.length);
/*      */     } 
/* 2365 */     if (this.anns != null) {
/* 2366 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
/* 2367 */       this.anns.put(paramByteVector);
/*      */     } 
/* 2369 */     if (this.ianns != null) {
/* 2370 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
/* 2371 */       this.ianns.put(paramByteVector);
/*      */     } 
/* 2373 */     if (this.tanns != null) {
/* 2374 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
/* 2375 */       this.tanns.put(paramByteVector);
/*      */     } 
/* 2377 */     if (this.itanns != null) {
/* 2378 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 2379 */       this.itanns.put(paramByteVector);
/*      */     } 
/* 2381 */     if (this.panns != null) {
/* 2382 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
/* 2383 */       AnnotationWriter.put(this.panns, this.synthetics, paramByteVector);
/*      */     } 
/* 2385 */     if (this.ipanns != null) {
/* 2386 */       paramByteVector.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
/* 2387 */       AnnotationWriter.put(this.ipanns, this.synthetics, paramByteVector);
/*      */     } 
/* 2389 */     if (this.attrs != null)
/* 2390 */       this.attrs.put(this.cw, null, 0, -1, -1, paramByteVector); 
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\MethodWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */