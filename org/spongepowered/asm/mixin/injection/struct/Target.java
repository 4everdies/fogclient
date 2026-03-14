/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Label;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.LabelNode;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Target
/*     */   implements Comparable<Target>, Iterable<AbstractInsnNode>
/*     */ {
/*     */   public final ClassNode classNode;
/*     */   public final MethodNode method;
/*     */   public final InsnList insns;
/*     */   public final boolean isStatic;
/*     */   public final boolean isCtor;
/*     */   public final Type[] arguments;
/*     */   public final Type returnType;
/*     */   private final int maxStack;
/*     */   private final int maxLocals;
/* 102 */   private final InjectionNodes injectionNodes = new InjectionNodes();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String callbackInfoClass;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String callbackDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] argIndices;
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Integer> argMapVars;
/*     */ 
/*     */ 
/*     */   
/*     */   private LabelNode start;
/*     */ 
/*     */ 
/*     */   
/*     */   private LabelNode end;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target(ClassNode paramClassNode, MethodNode paramMethodNode) {
/* 135 */     this.classNode = paramClassNode;
/* 136 */     this.method = paramMethodNode;
/* 137 */     this.insns = paramMethodNode.instructions;
/* 138 */     this.isStatic = Bytecode.methodIsStatic(paramMethodNode);
/* 139 */     this.isCtor = paramMethodNode.name.equals("<init>");
/* 140 */     this.arguments = Type.getArgumentTypes(paramMethodNode.desc);
/*     */     
/* 142 */     this.returnType = Type.getReturnType(paramMethodNode.desc);
/* 143 */     this.maxStack = paramMethodNode.maxStack;
/* 144 */     this.maxLocals = paramMethodNode.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionNodes.InjectionNode addInjectionNode(AbstractInsnNode paramAbstractInsnNode) {
/* 155 */     return this.injectionNodes.add(paramAbstractInsnNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionNodes.InjectionNode getInjectionNode(AbstractInsnNode paramAbstractInsnNode) {
/* 166 */     return this.injectionNodes.get(paramAbstractInsnNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLocals() {
/* 175 */     return this.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStack() {
/* 184 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentMaxLocals() {
/* 193 */     return this.method.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentMaxStack() {
/* 202 */     return this.method.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int allocateLocal() {
/* 211 */     return allocateLocals(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int allocateLocals(int paramInt) {
/* 222 */     int i = this.method.maxLocals;
/* 223 */     this.method.maxLocals += paramInt;
/* 224 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToLocals(int paramInt) {
/* 233 */     setMaxLocals(this.maxLocals + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLocals(int paramInt) {
/* 243 */     if (paramInt > this.method.maxLocals) {
/* 244 */       this.method.maxLocals = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToStack(int paramInt) {
/* 254 */     setMaxStack(this.maxStack + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxStack(int paramInt) {
/* 264 */     if (paramInt > this.method.maxStack) {
/* 265 */       this.method.maxStack = paramInt;
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
/*     */   public int[] generateArgMap(Type[] paramArrayOfType, int paramInt) {
/* 280 */     if (this.argMapVars == null) {
/* 281 */       this.argMapVars = new ArrayList<Integer>();
/*     */     }
/*     */     
/* 284 */     int[] arrayOfInt = new int[paramArrayOfType.length];
/* 285 */     for (int i = paramInt, j = 0; i < paramArrayOfType.length; i++) {
/* 286 */       int k = paramArrayOfType[i].getSize();
/* 287 */       arrayOfInt[i] = allocateArgMapLocal(j, k);
/* 288 */       j += k;
/*     */     } 
/* 290 */     return arrayOfInt;
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
/*     */   private int allocateArgMapLocal(int paramInt1, int paramInt2) {
/* 306 */     if (paramInt1 >= this.argMapVars.size()) {
/* 307 */       int j = allocateLocals(paramInt2);
/* 308 */       for (byte b = 0; b < paramInt2; b++) {
/* 309 */         this.argMapVars.add(Integer.valueOf(j + b));
/*     */       }
/* 311 */       return j;
/*     */     } 
/*     */     
/* 314 */     int i = ((Integer)this.argMapVars.get(paramInt1)).intValue();
/*     */ 
/*     */     
/* 317 */     if (paramInt2 > 1 && paramInt1 + paramInt2 > this.argMapVars.size()) {
/* 318 */       int j = allocateLocals(1);
/* 319 */       if (j == i + 1) {
/*     */         
/* 321 */         this.argMapVars.add(Integer.valueOf(j));
/* 322 */         return i;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 327 */       this.argMapVars.set(paramInt1, Integer.valueOf(j));
/* 328 */       this.argMapVars.add(Integer.valueOf(allocateLocals(1)));
/* 329 */       return j;
/*     */     } 
/*     */     
/* 332 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getArgIndices() {
/* 341 */     if (this.argIndices == null) {
/* 342 */       this.argIndices = calcArgIndices(this.isStatic ? 0 : 1);
/*     */     }
/* 344 */     return this.argIndices;
/*     */   }
/*     */   
/*     */   private int[] calcArgIndices(int paramInt) {
/* 348 */     int[] arrayOfInt = new int[this.arguments.length];
/* 349 */     for (byte b = 0; b < this.arguments.length; b++) {
/* 350 */       arrayOfInt[b] = paramInt;
/* 351 */       paramInt += this.arguments[b].getSize();
/*     */     } 
/* 353 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCallbackInfoClass() {
/* 363 */     if (this.callbackInfoClass == null) {
/* 364 */       this.callbackInfoClass = CallbackInfo.getCallInfoClassName(this.returnType);
/*     */     }
/* 366 */     return this.callbackInfoClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSimpleCallbackDescriptor() {
/* 375 */     return String.format("(L%s;)V", new Object[] { getCallbackInfoClass() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCallbackDescriptor(Type[] paramArrayOfType1, Type[] paramArrayOfType2) {
/* 386 */     return getCallbackDescriptor(false, paramArrayOfType1, paramArrayOfType2, 0, 32767);
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
/*     */   public String getCallbackDescriptor(boolean paramBoolean, Type[] paramArrayOfType1, Type[] paramArrayOfType2, int paramInt1, int paramInt2) {
/* 400 */     if (this.callbackDescriptor == null) {
/* 401 */       this.callbackDescriptor = String.format("(%sL%s;)V", new Object[] { this.method.desc.substring(1, this.method.desc.indexOf(')')), 
/* 402 */             getCallbackInfoClass() });
/*     */     }
/*     */     
/* 405 */     if (!paramBoolean || paramArrayOfType1 == null) {
/* 406 */       return this.callbackDescriptor;
/*     */     }
/*     */     
/* 409 */     StringBuilder stringBuilder = new StringBuilder(this.callbackDescriptor.substring(0, this.callbackDescriptor.indexOf(')')));
/* 410 */     for (int i = paramInt1; i < paramArrayOfType1.length && paramInt2 > 0; i++) {
/* 411 */       if (paramArrayOfType1[i] != null) {
/* 412 */         stringBuilder.append(paramArrayOfType1[i].getDescriptor());
/* 413 */         paramInt2--;
/*     */       } 
/*     */     } 
/*     */     
/* 417 */     return stringBuilder.append(")V").toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 422 */     return String.format("%s::%s%s", new Object[] { this.classNode.name, this.method.name, this.method.desc });
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Target paramTarget) {
/* 427 */     if (paramTarget == null) {
/* 428 */       return Integer.MAX_VALUE;
/*     */     }
/* 430 */     return toString().compareTo(paramTarget.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(InjectionNodes.InjectionNode paramInjectionNode) {
/* 440 */     return this.insns.indexOf(paramInjectionNode.getCurrentTarget());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(AbstractInsnNode paramAbstractInsnNode) {
/* 450 */     return this.insns.indexOf(paramAbstractInsnNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode get(int paramInt) {
/* 460 */     return this.insns.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<AbstractInsnNode> iterator() {
/* 468 */     return this.insns.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInsnNode findInitNodeFor(TypeInsnNode paramTypeInsnNode) {
/* 479 */     int i = indexOf((AbstractInsnNode)paramTypeInsnNode);
/* 480 */     for (ListIterator<AbstractInsnNode> listIterator = this.insns.iterator(i); listIterator.hasNext(); ) {
/* 481 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/* 482 */       if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() == 183) {
/* 483 */         MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
/* 484 */         if ("<init>".equals(methodInsnNode.name) && methodInsnNode.owner.equals(paramTypeInsnNode.desc)) {
/* 485 */           return methodInsnNode;
/*     */         }
/*     */       } 
/*     */     } 
/* 489 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInsnNode findSuperInitNode() {
/* 500 */     if (!this.isCtor) {
/* 501 */       return null;
/*     */     }
/*     */     
/* 504 */     return Bytecode.findSuperInit(this.method, ClassInfo.forName(this.classNode.name).getSuperName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertBefore(InjectionNodes.InjectionNode paramInjectionNode, InsnList paramInsnList) {
/* 514 */     this.insns.insertBefore(paramInjectionNode.getCurrentTarget(), paramInsnList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertBefore(AbstractInsnNode paramAbstractInsnNode, InsnList paramInsnList) {
/* 524 */     this.insns.insertBefore(paramAbstractInsnNode, paramInsnList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceNode(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 535 */     this.insns.insertBefore(paramAbstractInsnNode1, paramAbstractInsnNode2);
/* 536 */     this.insns.remove(paramAbstractInsnNode1);
/* 537 */     this.injectionNodes.replace(paramAbstractInsnNode1, paramAbstractInsnNode2);
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
/*     */   public void replaceNode(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2, InsnList paramInsnList) {
/* 549 */     this.insns.insertBefore(paramAbstractInsnNode1, paramInsnList);
/* 550 */     this.insns.remove(paramAbstractInsnNode1);
/* 551 */     this.injectionNodes.replace(paramAbstractInsnNode1, paramAbstractInsnNode2);
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
/*     */   public void wrapNode(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2, InsnList paramInsnList1, InsnList paramInsnList2) {
/* 564 */     this.insns.insertBefore(paramAbstractInsnNode1, paramInsnList1);
/* 565 */     this.insns.insert(paramAbstractInsnNode1, paramInsnList2);
/* 566 */     this.injectionNodes.replace(paramAbstractInsnNode1, paramAbstractInsnNode2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceNode(AbstractInsnNode paramAbstractInsnNode, InsnList paramInsnList) {
/* 577 */     this.insns.insertBefore(paramAbstractInsnNode, paramInsnList);
/* 578 */     removeNode(paramAbstractInsnNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeNode(AbstractInsnNode paramAbstractInsnNode) {
/* 588 */     this.insns.remove(paramAbstractInsnNode);
/* 589 */     this.injectionNodes.remove(paramAbstractInsnNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLocalVariable(int paramInt, String paramString1, String paramString2) {
/* 600 */     if (this.start == null) {
/* 601 */       this.start = new LabelNode(new Label());
/* 602 */       this.end = new LabelNode(new Label());
/* 603 */       this.insns.insert((AbstractInsnNode)this.start);
/* 604 */       this.insns.add((AbstractInsnNode)this.end);
/*     */     } 
/* 606 */     addLocalVariable(paramInt, paramString1, paramString2, this.start, this.end);
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
/*     */   private void addLocalVariable(int paramInt, String paramString1, String paramString2, LabelNode paramLabelNode1, LabelNode paramLabelNode2) {
/* 619 */     if (this.method.localVariables == null) {
/* 620 */       this.method.localVariables = new ArrayList();
/*     */     }
/* 622 */     this.method.localVariables.add(new LocalVariableNode(paramString1, paramString2, null, paramLabelNode1, paramLabelNode2, paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\Target.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */