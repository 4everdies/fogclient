/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.Handle;
/*     */ import org.spongepowered.asm.lib.Label;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.TypePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodNode
/*     */   extends MethodVisitor
/*     */ {
/*     */   public int access;
/*     */   public String name;
/*     */   public String desc;
/*     */   public String signature;
/*     */   public List<String> exceptions;
/*     */   public List<ParameterNode> parameters;
/*     */   public List<AnnotationNode> visibleAnnotations;
/*     */   public List<AnnotationNode> invisibleAnnotations;
/*     */   public List<TypeAnnotationNode> visibleTypeAnnotations;
/*     */   public List<TypeAnnotationNode> invisibleTypeAnnotations;
/*     */   public List<Attribute> attrs;
/*     */   public Object annotationDefault;
/*     */   public List<AnnotationNode>[] visibleParameterAnnotations;
/*     */   public List<AnnotationNode>[] invisibleParameterAnnotations;
/*     */   public InsnList instructions;
/*     */   public List<TryCatchBlockNode> tryCatchBlocks;
/*     */   public int maxStack;
/*     */   public int maxLocals;
/*     */   public List<LocalVariableNode> localVariables;
/*     */   public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;
/*     */   public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;
/*     */   private boolean visited;
/*     */   
/*     */   public MethodNode() {
/* 223 */     this(327680);
/* 224 */     if (getClass() != MethodNode.class) {
/* 225 */       throw new IllegalStateException();
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
/*     */   public MethodNode(int paramInt) {
/* 237 */     super(paramInt);
/* 238 */     this.instructions = new InsnList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodNode(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 265 */     this(327680, paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
/* 266 */     if (getClass() != MethodNode.class) {
/* 267 */       throw new IllegalStateException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodNode(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 294 */     super(paramInt1);
/* 295 */     this.access = paramInt2;
/* 296 */     this.name = paramString1;
/* 297 */     this.desc = paramString2;
/* 298 */     this.signature = paramString3;
/* 299 */     this.exceptions = new ArrayList<String>((paramArrayOfString == null) ? 0 : paramArrayOfString.length);
/*     */     
/* 301 */     boolean bool = ((paramInt2 & 0x400) != 0) ? true : false;
/* 302 */     if (!bool) {
/* 303 */       this.localVariables = new ArrayList<LocalVariableNode>(5);
/*     */     }
/* 305 */     this.tryCatchBlocks = new ArrayList<TryCatchBlockNode>();
/* 306 */     if (paramArrayOfString != null) {
/* 307 */       this.exceptions.addAll(Arrays.asList(paramArrayOfString));
/*     */     }
/* 309 */     this.instructions = new InsnList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitParameter(String paramString, int paramInt) {
/* 318 */     if (this.parameters == null) {
/* 319 */       this.parameters = new ArrayList<ParameterNode>(5);
/*     */     }
/* 321 */     this.parameters.add(new ParameterNode(paramString, paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/* 327 */     return new AnnotationNode(new ArrayList(0)
/*     */         {
/*     */           public boolean add(Object param1Object) {
/* 330 */             MethodNode.this.annotationDefault = param1Object;
/* 331 */             return super.add(param1Object);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 339 */     AnnotationNode annotationNode = new AnnotationNode(paramString);
/* 340 */     if (paramBoolean) {
/* 341 */       if (this.visibleAnnotations == null) {
/* 342 */         this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 344 */       this.visibleAnnotations.add(annotationNode);
/*     */     } else {
/* 346 */       if (this.invisibleAnnotations == null) {
/* 347 */         this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 349 */       this.invisibleAnnotations.add(annotationNode);
/*     */     } 
/* 351 */     return annotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 357 */     TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(paramInt, paramTypePath, paramString);
/* 358 */     if (paramBoolean) {
/* 359 */       if (this.visibleTypeAnnotations == null) {
/* 360 */         this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 362 */       this.visibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } else {
/* 364 */       if (this.invisibleTypeAnnotations == null) {
/* 365 */         this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 367 */       this.invisibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } 
/* 369 */     return typeAnnotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/* 376 */     AnnotationNode annotationNode = new AnnotationNode(paramString);
/* 377 */     if (paramBoolean) {
/* 378 */       if (this.visibleParameterAnnotations == null) {
/* 379 */         int i = (Type.getArgumentTypes(this.desc)).length;
/* 380 */         this.visibleParameterAnnotations = (List<AnnotationNode>[])new List[i];
/*     */       } 
/* 382 */       if (this.visibleParameterAnnotations[paramInt] == null) {
/* 383 */         this.visibleParameterAnnotations[paramInt] = new ArrayList<AnnotationNode>(1);
/*     */       }
/*     */       
/* 386 */       this.visibleParameterAnnotations[paramInt].add(annotationNode);
/*     */     } else {
/* 388 */       if (this.invisibleParameterAnnotations == null) {
/* 389 */         int i = (Type.getArgumentTypes(this.desc)).length;
/* 390 */         this.invisibleParameterAnnotations = (List<AnnotationNode>[])new List[i];
/*     */       } 
/* 392 */       if (this.invisibleParameterAnnotations[paramInt] == null) {
/* 393 */         this.invisibleParameterAnnotations[paramInt] = new ArrayList<AnnotationNode>(1);
/*     */       }
/*     */       
/* 396 */       this.invisibleParameterAnnotations[paramInt].add(annotationNode);
/*     */     } 
/* 398 */     return annotationNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 403 */     if (this.attrs == null) {
/* 404 */       this.attrs = new ArrayList<Attribute>(1);
/*     */     }
/* 406 */     this.attrs.add(paramAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitCode() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/* 416 */     this.instructions.add(new FrameNode(paramInt1, paramInt2, (paramArrayOfObject1 == null) ? null : 
/* 417 */           getLabelNodes(paramArrayOfObject1), paramInt3, (paramArrayOfObject2 == null) ? null : 
/* 418 */           getLabelNodes(paramArrayOfObject2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInsn(int paramInt) {
/* 423 */     this.instructions.add(new InsnNode(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntInsn(int paramInt1, int paramInt2) {
/* 428 */     this.instructions.add(new IntInsnNode(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitVarInsn(int paramInt1, int paramInt2) {
/* 433 */     this.instructions.add(new VarInsnNode(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeInsn(int paramInt, String paramString) {
/* 438 */     this.instructions.add(new TypeInsnNode(paramInt, paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 444 */     this.instructions.add(new FieldInsnNode(paramInt, paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 451 */     if (this.api >= 327680) {
/* 452 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*     */       return;
/*     */     } 
/* 455 */     this.instructions.add(new MethodInsnNode(paramInt, paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 461 */     if (this.api < 327680) {
/* 462 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*     */       return;
/*     */     } 
/* 465 */     this.instructions.add(new MethodInsnNode(paramInt, paramString1, paramString2, paramString3, paramBoolean));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/* 471 */     this.instructions.add(new InvokeDynamicInsnNode(paramString1, paramString2, paramHandle, paramVarArgs));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitJumpInsn(int paramInt, Label paramLabel) {
/* 476 */     this.instructions.add(new JumpInsnNode(paramInt, getLabelNode(paramLabel)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLabel(Label paramLabel) {
/* 481 */     this.instructions.add(getLabelNode(paramLabel));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLdcInsn(Object paramObject) {
/* 486 */     this.instructions.add(new LdcInsnNode(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIincInsn(int paramInt1, int paramInt2) {
/* 491 */     this.instructions.add(new IincInsnNode(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label... paramVarArgs) {
/* 497 */     this.instructions.add(new TableSwitchInsnNode(paramInt1, paramInt2, getLabelNode(paramLabel), 
/* 498 */           getLabelNodes(paramVarArgs)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel) {
/* 504 */     this.instructions.add(new LookupSwitchInsnNode(getLabelNode(paramLabel), paramArrayOfint, 
/* 505 */           getLabelNodes(paramArrayOfLabel)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/* 510 */     this.instructions.add(new MultiANewArrayInsnNode(paramString, paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 518 */     AbstractInsnNode abstractInsnNode = this.instructions.getLast();
/* 519 */     while (abstractInsnNode.getOpcode() == -1) {
/* 520 */       abstractInsnNode = abstractInsnNode.getPrevious();
/*     */     }
/*     */     
/* 523 */     TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(paramInt, paramTypePath, paramString);
/* 524 */     if (paramBoolean) {
/* 525 */       if (abstractInsnNode.visibleTypeAnnotations == null) {
/* 526 */         abstractInsnNode.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/*     */       
/* 529 */       abstractInsnNode.visibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } else {
/* 531 */       if (abstractInsnNode.invisibleTypeAnnotations == null) {
/* 532 */         abstractInsnNode.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/*     */       
/* 535 */       abstractInsnNode.invisibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } 
/* 537 */     return typeAnnotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/* 543 */     this.tryCatchBlocks.add(new TryCatchBlockNode(getLabelNode(paramLabel1), 
/* 544 */           getLabelNode(paramLabel2), getLabelNode(paramLabel3), paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 550 */     TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get((paramInt & 0xFFFF00) >> 8);
/* 551 */     TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(paramInt, paramTypePath, paramString);
/* 552 */     if (paramBoolean) {
/* 553 */       if (tryCatchBlockNode.visibleTypeAnnotations == null) {
/* 554 */         tryCatchBlockNode.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/*     */       
/* 557 */       tryCatchBlockNode.visibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } else {
/* 559 */       if (tryCatchBlockNode.invisibleTypeAnnotations == null) {
/* 560 */         tryCatchBlockNode.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/*     */       
/* 563 */       tryCatchBlockNode.invisibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } 
/* 565 */     return typeAnnotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/* 572 */     this.localVariables.add(new LocalVariableNode(paramString1, paramString2, paramString3, 
/* 573 */           getLabelNode(paramLabel1), getLabelNode(paramLabel2), paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/* 581 */     LocalVariableAnnotationNode localVariableAnnotationNode = new LocalVariableAnnotationNode(paramInt, paramTypePath, getLabelNodes(paramArrayOfLabel1), getLabelNodes(paramArrayOfLabel2), paramArrayOfint, paramString);
/*     */     
/* 583 */     if (paramBoolean) {
/* 584 */       if (this.visibleLocalVariableAnnotations == null) {
/* 585 */         this.visibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
/*     */       }
/*     */       
/* 588 */       this.visibleLocalVariableAnnotations.add(localVariableAnnotationNode);
/*     */     } else {
/* 590 */       if (this.invisibleLocalVariableAnnotations == null) {
/* 591 */         this.invisibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
/*     */       }
/*     */       
/* 594 */       this.invisibleLocalVariableAnnotations.add(localVariableAnnotationNode);
/*     */     } 
/* 596 */     return localVariableAnnotationNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLineNumber(int paramInt, Label paramLabel) {
/* 601 */     this.instructions.add(new LineNumberNode(paramInt, getLabelNode(paramLabel)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMaxs(int paramInt1, int paramInt2) {
/* 606 */     this.maxStack = paramInt1;
/* 607 */     this.maxLocals = paramInt2;
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
/*     */   
/*     */   protected LabelNode getLabelNode(Label paramLabel) {
/* 625 */     if (!(paramLabel.info instanceof LabelNode)) {
/* 626 */       paramLabel.info = new LabelNode();
/*     */     }
/* 628 */     return (LabelNode)paramLabel.info;
/*     */   }
/*     */   
/*     */   private LabelNode[] getLabelNodes(Label[] paramArrayOfLabel) {
/* 632 */     LabelNode[] arrayOfLabelNode = new LabelNode[paramArrayOfLabel.length];
/* 633 */     for (byte b = 0; b < paramArrayOfLabel.length; b++) {
/* 634 */       arrayOfLabelNode[b] = getLabelNode(paramArrayOfLabel[b]);
/*     */     }
/* 636 */     return arrayOfLabelNode;
/*     */   }
/*     */   
/*     */   private Object[] getLabelNodes(Object[] paramArrayOfObject) {
/* 640 */     Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 641 */     for (byte b = 0; b < paramArrayOfObject.length; b++) {
/* 642 */       Object object = paramArrayOfObject[b];
/* 643 */       if (object instanceof Label) {
/* 644 */         object = getLabelNode((Label)object);
/*     */       }
/* 646 */       arrayOfObject[b] = object;
/*     */     } 
/* 648 */     return arrayOfObject;
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
/*     */   public void check(int paramInt) {
/* 666 */     if (paramInt == 262144) {
/* 667 */       if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations
/* 668 */         .size() > 0) {
/* 669 */         throw new RuntimeException();
/*     */       }
/* 671 */       if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations
/* 672 */         .size() > 0) {
/* 673 */         throw new RuntimeException();
/*     */       }
/* 675 */       byte b1 = (this.tryCatchBlocks == null) ? 0 : this.tryCatchBlocks.size(); byte b2;
/* 676 */       for (b2 = 0; b2 < b1; b2++) {
/* 677 */         TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get(b2);
/* 678 */         if (tryCatchBlockNode.visibleTypeAnnotations != null && tryCatchBlockNode.visibleTypeAnnotations
/* 679 */           .size() > 0) {
/* 680 */           throw new RuntimeException();
/*     */         }
/* 682 */         if (tryCatchBlockNode.invisibleTypeAnnotations != null && tryCatchBlockNode.invisibleTypeAnnotations
/* 683 */           .size() > 0) {
/* 684 */           throw new RuntimeException();
/*     */         }
/*     */       } 
/* 687 */       for (b2 = 0; b2 < this.instructions.size(); b2++) {
/* 688 */         AbstractInsnNode abstractInsnNode = this.instructions.get(b2);
/* 689 */         if (abstractInsnNode.visibleTypeAnnotations != null && abstractInsnNode.visibleTypeAnnotations
/* 690 */           .size() > 0) {
/* 691 */           throw new RuntimeException();
/*     */         }
/* 693 */         if (abstractInsnNode.invisibleTypeAnnotations != null && abstractInsnNode.invisibleTypeAnnotations
/* 694 */           .size() > 0) {
/* 695 */           throw new RuntimeException();
/*     */         }
/* 697 */         if (abstractInsnNode instanceof MethodInsnNode) {
/* 698 */           boolean bool = ((MethodInsnNode)abstractInsnNode).itf;
/* 699 */           if (bool != ((abstractInsnNode.opcode == 185))) {
/* 700 */             throw new RuntimeException();
/*     */           }
/*     */         } 
/*     */       } 
/* 704 */       if (this.visibleLocalVariableAnnotations != null && this.visibleLocalVariableAnnotations
/* 705 */         .size() > 0) {
/* 706 */         throw new RuntimeException();
/*     */       }
/* 708 */       if (this.invisibleLocalVariableAnnotations != null && this.invisibleLocalVariableAnnotations
/* 709 */         .size() > 0) {
/* 710 */         throw new RuntimeException();
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
/*     */   public void accept(ClassVisitor paramClassVisitor) {
/* 722 */     String[] arrayOfString = new String[this.exceptions.size()];
/* 723 */     this.exceptions.toArray(arrayOfString);
/* 724 */     MethodVisitor methodVisitor = paramClassVisitor.visitMethod(this.access, this.name, this.desc, this.signature, arrayOfString);
/*     */     
/* 726 */     if (methodVisitor != null) {
/* 727 */       accept(methodVisitor);
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
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 740 */     byte b1 = (this.parameters == null) ? 0 : this.parameters.size(); byte b2;
/* 741 */     for (b2 = 0; b2 < b1; b2++) {
/* 742 */       ParameterNode parameterNode = this.parameters.get(b2);
/* 743 */       paramMethodVisitor.visitParameter(parameterNode.name, parameterNode.access);
/*     */     } 
/*     */     
/* 746 */     if (this.annotationDefault != null) {
/* 747 */       AnnotationVisitor annotationVisitor = paramMethodVisitor.visitAnnotationDefault();
/* 748 */       AnnotationNode.accept(annotationVisitor, null, this.annotationDefault);
/* 749 */       if (annotationVisitor != null) {
/* 750 */         annotationVisitor.visitEnd();
/*     */       }
/*     */     } 
/* 753 */     b1 = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size();
/* 754 */     for (b2 = 0; b2 < b1; b2++) {
/* 755 */       AnnotationNode annotationNode = this.visibleAnnotations.get(b2);
/* 756 */       annotationNode.accept(paramMethodVisitor.visitAnnotation(annotationNode.desc, true));
/*     */     } 
/* 758 */     b1 = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size();
/* 759 */     for (b2 = 0; b2 < b1; b2++) {
/* 760 */       AnnotationNode annotationNode = this.invisibleAnnotations.get(b2);
/* 761 */       annotationNode.accept(paramMethodVisitor.visitAnnotation(annotationNode.desc, false));
/*     */     } 
/* 763 */     b1 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size();
/* 764 */     for (b2 = 0; b2 < b1; b2++) {
/* 765 */       TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(b2);
/* 766 */       typeAnnotationNode.accept(paramMethodVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
/*     */     } 
/*     */ 
/*     */     
/* 770 */     b1 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size();
/* 771 */     for (b2 = 0; b2 < b1; b2++) {
/* 772 */       TypeAnnotationNode typeAnnotationNode = this.invisibleTypeAnnotations.get(b2);
/* 773 */       typeAnnotationNode.accept(paramMethodVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, false));
/*     */     } 
/*     */     
/* 776 */     b1 = (this.visibleParameterAnnotations == null) ? 0 : this.visibleParameterAnnotations.length;
/*     */     
/* 778 */     for (b2 = 0; b2 < b1; b2++) {
/* 779 */       List<AnnotationNode> list = this.visibleParameterAnnotations[b2];
/* 780 */       if (list != null)
/*     */       {
/*     */         
/* 783 */         for (byte b = 0; b < list.size(); b++) {
/* 784 */           AnnotationNode annotationNode = list.get(b);
/* 785 */           annotationNode.accept(paramMethodVisitor.visitParameterAnnotation(b2, annotationNode.desc, true));
/*     */         }  } 
/*     */     } 
/* 788 */     b1 = (this.invisibleParameterAnnotations == null) ? 0 : this.invisibleParameterAnnotations.length;
/*     */     
/* 790 */     for (b2 = 0; b2 < b1; b2++) {
/* 791 */       List<AnnotationNode> list = this.invisibleParameterAnnotations[b2];
/* 792 */       if (list != null)
/*     */       {
/*     */         
/* 795 */         for (byte b = 0; b < list.size(); b++) {
/* 796 */           AnnotationNode annotationNode = list.get(b);
/* 797 */           annotationNode.accept(paramMethodVisitor.visitParameterAnnotation(b2, annotationNode.desc, false));
/*     */         }  } 
/*     */     } 
/* 800 */     if (this.visited) {
/* 801 */       this.instructions.resetLabels();
/*     */     }
/* 803 */     b1 = (this.attrs == null) ? 0 : this.attrs.size();
/* 804 */     for (b2 = 0; b2 < b1; b2++) {
/* 805 */       paramMethodVisitor.visitAttribute(this.attrs.get(b2));
/*     */     }
/*     */     
/* 808 */     if (this.instructions.size() > 0) {
/* 809 */       paramMethodVisitor.visitCode();
/*     */       
/* 811 */       b1 = (this.tryCatchBlocks == null) ? 0 : this.tryCatchBlocks.size();
/* 812 */       for (b2 = 0; b2 < b1; b2++) {
/* 813 */         ((TryCatchBlockNode)this.tryCatchBlocks.get(b2)).updateIndex(b2);
/* 814 */         ((TryCatchBlockNode)this.tryCatchBlocks.get(b2)).accept(paramMethodVisitor);
/*     */       } 
/*     */       
/* 817 */       this.instructions.accept(paramMethodVisitor);
/*     */       
/* 819 */       b1 = (this.localVariables == null) ? 0 : this.localVariables.size();
/* 820 */       for (b2 = 0; b2 < b1; b2++) {
/* 821 */         ((LocalVariableNode)this.localVariables.get(b2)).accept(paramMethodVisitor);
/*     */       }
/*     */ 
/*     */       
/* 825 */       b1 = (this.visibleLocalVariableAnnotations == null) ? 0 : this.visibleLocalVariableAnnotations.size();
/* 826 */       for (b2 = 0; b2 < b1; b2++) {
/* 827 */         ((LocalVariableAnnotationNode)this.visibleLocalVariableAnnotations.get(b2)).accept(paramMethodVisitor, true);
/*     */       }
/*     */       
/* 830 */       b1 = (this.invisibleLocalVariableAnnotations == null) ? 0 : this.invisibleLocalVariableAnnotations.size();
/* 831 */       for (b2 = 0; b2 < b1; b2++) {
/* 832 */         ((LocalVariableAnnotationNode)this.invisibleLocalVariableAnnotations.get(b2)).accept(paramMethodVisitor, false);
/*     */       }
/*     */       
/* 835 */       paramMethodVisitor.visitMaxs(this.maxStack, this.maxLocals);
/* 836 */       this.visited = true;
/*     */     } 
/* 838 */     paramMethodVisitor.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\MethodNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */