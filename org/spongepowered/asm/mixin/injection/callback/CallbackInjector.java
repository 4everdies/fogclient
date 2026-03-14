/*     */ package org.spongepowered.asm.mixin.injection.callback;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LabelNode;
/*     */ import org.spongepowered.asm.lib.tree.LdcInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.Coerce;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.Surrogate;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.Locals;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallbackInjector
/*     */   extends Injector
/*     */ {
/*     */   private final boolean cancellable;
/*     */   private final LocalCapture localCapture;
/*     */   private final String identifier;
/*     */   
/*     */   private class Callback
/*     */     extends InsnList
/*     */   {
/*     */     private final MethodNode handler;
/*     */     private final AbstractInsnNode head;
/*     */     final Target target;
/*     */     final InjectionNodes.InjectionNode node;
/*     */     final LocalVariableNode[] locals;
/*     */     final Type[] localTypes;
/*     */     final int frameSize;
/*     */     final int extraArgs;
/*     */     final boolean canCaptureLocals;
/*     */     final boolean isAtReturn;
/*     */     final String desc;
/*     */     final String descl;
/*     */     final String[] argNames;
/*     */     int ctor;
/*     */     int invoke;
/* 157 */     private int marshalVar = -1;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean captureArgs = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Callback(MethodNode param1MethodNode, Target param1Target, InjectionNodes.InjectionNode param1InjectionNode, LocalVariableNode[] param1ArrayOfLocalVariableNode, boolean param1Boolean) {
/* 167 */       this.handler = param1MethodNode;
/* 168 */       this.target = param1Target;
/* 169 */       this.head = param1Target.insns.getFirst();
/* 170 */       this.node = param1InjectionNode;
/* 171 */       this.locals = param1ArrayOfLocalVariableNode;
/* 172 */       this.localTypes = (param1ArrayOfLocalVariableNode != null) ? new Type[param1ArrayOfLocalVariableNode.length] : null;
/* 173 */       this.frameSize = Bytecode.getFirstNonArgLocalIndex(param1Target.arguments, !CallbackInjector.this.isStatic());
/* 174 */       ArrayList<String> arrayList = null;
/*     */       
/* 176 */       if (param1ArrayOfLocalVariableNode != null) {
/* 177 */         byte b1 = CallbackInjector.this.isStatic() ? 0 : 1;
/* 178 */         arrayList = new ArrayList();
/* 179 */         for (byte b2 = 0; b2 <= param1ArrayOfLocalVariableNode.length; b2++) {
/* 180 */           if (b2 == this.frameSize) {
/* 181 */             arrayList.add((param1Target.returnType == Type.VOID_TYPE) ? "ci" : "cir");
/*     */           }
/* 183 */           if (b2 < param1ArrayOfLocalVariableNode.length && param1ArrayOfLocalVariableNode[b2] != null) {
/* 184 */             this.localTypes[b2] = Type.getType((param1ArrayOfLocalVariableNode[b2]).desc);
/* 185 */             if (b2 >= b1) {
/* 186 */               arrayList.add(CallbackInjector.meltSnowman(b2, (param1ArrayOfLocalVariableNode[b2]).name));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 193 */       this.extraArgs = Math.max(0, Bytecode.getFirstNonArgLocalIndex(this.handler) - this.frameSize + 1);
/* 194 */       this.argNames = (arrayList != null) ? arrayList.<String>toArray(new String[arrayList.size()]) : null;
/* 195 */       this.canCaptureLocals = (param1Boolean && param1ArrayOfLocalVariableNode != null && param1ArrayOfLocalVariableNode.length > this.frameSize);
/* 196 */       this.isAtReturn = (this.node.getCurrentTarget() instanceof InsnNode && isValueReturnOpcode(this.node.getCurrentTarget().getOpcode()));
/* 197 */       this.desc = param1Target.getCallbackDescriptor(this.localTypes, param1Target.arguments);
/* 198 */       this.descl = param1Target.getCallbackDescriptor(true, this.localTypes, param1Target.arguments, this.frameSize, this.extraArgs);
/*     */ 
/*     */       
/* 201 */       this.invoke = param1Target.arguments.length + (this.canCaptureLocals ? (this.localTypes.length - this.frameSize) : 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isValueReturnOpcode(int param1Int) {
/* 212 */       return (param1Int >= 172 && param1Int < 177);
/*     */     }
/*     */     
/*     */     String getDescriptor() {
/* 216 */       return this.canCaptureLocals ? this.descl : this.desc;
/*     */     }
/*     */     
/*     */     String getDescriptorWithAllLocals() {
/* 220 */       return this.target.getCallbackDescriptor(true, this.localTypes, this.target.arguments, this.frameSize, 32767);
/*     */     }
/*     */     
/*     */     String getCallbackInfoConstructorDescriptor() {
/* 224 */       return this.isAtReturn ? CallbackInfo.getConstructorDescriptor(this.target.returnType) : CallbackInfo.getConstructorDescriptor();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void add(AbstractInsnNode param1AbstractInsnNode, boolean param1Boolean1, boolean param1Boolean2) {
/* 236 */       add(param1AbstractInsnNode, param1Boolean1, param1Boolean2, false);
/*     */     }
/*     */     
/*     */     void add(AbstractInsnNode param1AbstractInsnNode, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3) {
/* 240 */       if (param1Boolean3) {
/* 241 */         this.target.insns.insertBefore(this.head, param1AbstractInsnNode);
/*     */       } else {
/* 243 */         add(param1AbstractInsnNode);
/*     */       } 
/* 245 */       this.ctor += param1Boolean1 ? 1 : 0;
/* 246 */       this.invoke += param1Boolean2 ? 1 : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void inject() {
/* 254 */       this.target.insertBefore(this.node, this);
/* 255 */       this.target.addToStack(Math.max(this.invoke, this.ctor));
/*     */     }
/*     */     
/*     */     boolean checkDescriptor(String param1String) {
/* 259 */       if (getDescriptor().equals(param1String)) {
/* 260 */         return true;
/*     */       }
/*     */       
/* 263 */       if (this.target.getSimpleCallbackDescriptor().equals(param1String) && !this.canCaptureLocals) {
/* 264 */         this.captureArgs = false;
/* 265 */         return true;
/*     */       } 
/*     */       
/* 268 */       Type[] arrayOfType1 = Type.getArgumentTypes(param1String);
/* 269 */       Type[] arrayOfType2 = Type.getArgumentTypes(this.descl);
/*     */       
/* 271 */       if (arrayOfType1.length != arrayOfType2.length) {
/* 272 */         return false;
/*     */       }
/*     */       
/* 275 */       for (byte b = 0; b < arrayOfType2.length; b++) {
/* 276 */         Type type = arrayOfType1[b];
/* 277 */         if (!type.equals(arrayOfType2[b])) {
/*     */ 
/*     */ 
/*     */           
/* 281 */           if (type.getSort() == 9) {
/* 282 */             return false;
/*     */           }
/*     */           
/* 285 */           if (Annotations.getInvisibleParameter(this.handler, Coerce.class, b) == null) {
/* 286 */             return false;
/*     */           }
/*     */           
/* 289 */           if (!Injector.canCoerce(arrayOfType1[b], arrayOfType2[b]))
/*     */           {
/*     */ 
/*     */             
/* 293 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 298 */       return true;
/*     */     }
/*     */     
/*     */     boolean captureArgs() {
/* 302 */       return this.captureArgs;
/*     */     }
/*     */     
/*     */     int marshalVar() {
/* 306 */       if (this.marshalVar < 0) {
/* 307 */         this.marshalVar = this.target.allocateLocal();
/*     */       }
/*     */       
/* 310 */       return this.marshalVar;
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
/* 333 */   private final Map<Integer, String> ids = new HashMap<Integer, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 339 */   private int totalInjections = 0;
/* 340 */   private int callbackInfoVar = -1;
/*     */ 
/*     */   
/*     */   private String lastId;
/*     */ 
/*     */   
/*     */   private String lastDesc;
/*     */   
/*     */   private Target lastTarget;
/*     */   
/*     */   private String callbackInfoClass;
/*     */ 
/*     */   
/*     */   public CallbackInjector(InjectionInfo paramInjectionInfo, boolean paramBoolean, LocalCapture paramLocalCapture, String paramString) {
/* 354 */     super(paramInjectionInfo);
/* 355 */     this.cancellable = paramBoolean;
/* 356 */     this.localCapture = paramLocalCapture;
/* 357 */     this.identifier = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sanityCheck(Target paramTarget, List<InjectionPoint> paramList) {
/* 367 */     super.sanityCheck(paramTarget, paramList);
/*     */     
/* 369 */     if (paramTarget.isStatic != this.isStatic) {
/* 370 */       throw new InvalidInjectionException(this.info, "'static' modifier of callback method does not match target in " + this);
/*     */     }
/*     */     
/* 373 */     if ("<init>".equals(paramTarget.method.name)) {
/* 374 */       for (InjectionPoint injectionPoint : paramList) {
/* 375 */         if (!injectionPoint.getClass().equals(BeforeReturn.class)) {
/* 376 */           throw new InvalidInjectionException(this.info, "Found injection point type " + injectionPoint.getClass().getSimpleName() + " targetting a ctor in " + this + ". Only RETURN allowed for a ctor target");
/*     */         }
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
/*     */   protected void addTargetNode(Target paramTarget, List<InjectionNodes.InjectionNode> paramList, AbstractInsnNode paramAbstractInsnNode, Set<InjectionPoint> paramSet) {
/* 390 */     InjectionNodes.InjectionNode injectionNode = paramTarget.addInjectionNode(paramAbstractInsnNode);
/*     */     
/* 392 */     for (InjectionPoint injectionPoint : paramSet) {
/* 393 */       String str1 = injectionPoint.getId();
/* 394 */       if (Strings.isNullOrEmpty(str1)) {
/*     */         continue;
/*     */       }
/*     */       
/* 398 */       String str2 = this.ids.get(Integer.valueOf(injectionNode.getId()));
/* 399 */       if (str2 != null && !str2.equals(str1)) {
/* 400 */         Injector.logger.warn("Conflicting id for {} insn in {}, found id {} on {}, previously defined as {}", new Object[] { Bytecode.getOpcodeName(paramAbstractInsnNode), paramTarget
/* 401 */               .toString(), str1, this.info, str2 });
/*     */         
/*     */         break;
/*     */       } 
/* 405 */       this.ids.put(Integer.valueOf(injectionNode.getId()), str1);
/*     */     } 
/*     */     
/* 408 */     paramList.add(injectionNode);
/* 409 */     this.totalInjections++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 419 */     LocalVariableNode[] arrayOfLocalVariableNode = null;
/*     */     
/* 421 */     if (this.localCapture.isCaptureLocals() || this.localCapture.isPrintLocals()) {
/* 422 */       arrayOfLocalVariableNode = Locals.getLocalsAt(this.classNode, paramTarget.method, paramInjectionNode.getCurrentTarget());
/*     */     }
/*     */     
/* 425 */     inject(new Callback(this.methodNode, paramTarget, paramInjectionNode, arrayOfLocalVariableNode, this.localCapture.isCaptureLocals()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inject(Callback paramCallback) {
/* 434 */     if (this.localCapture.isPrintLocals()) {
/* 435 */       printLocals(paramCallback);
/* 436 */       this.info.addCallbackInvocation(this.methodNode);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 443 */     MethodNode methodNode = this.methodNode;
/*     */     
/* 445 */     if (!paramCallback.checkDescriptor(this.methodNode.desc)) {
/* 446 */       if (this.info.getTargets().size() > 1) {
/*     */         return;
/*     */       }
/*     */       
/* 450 */       if (paramCallback.canCaptureLocals) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 456 */         MethodNode methodNode1 = Bytecode.findMethod(this.classNode, this.methodNode.name, paramCallback.getDescriptor());
/* 457 */         if (methodNode1 != null && Annotations.getVisible(methodNode1, Surrogate.class) != null) {
/*     */           
/* 459 */           methodNode = methodNode1;
/*     */         } else {
/*     */           
/* 462 */           String str = generateBadLVTMessage(paramCallback);
/*     */           
/* 464 */           switch (this.localCapture) {
/*     */             case CAPTURE_FAILEXCEPTION:
/* 466 */               Injector.logger.error("Injection error: {}", new Object[] { str });
/* 467 */               methodNode = generateErrorMethod(paramCallback, "org/spongepowered/asm/mixin/injection/throwables/InjectionError", str);
/*     */               break;
/*     */             
/*     */             case CAPTURE_FAILSOFT:
/* 471 */               Injector.logger.warn("Injection warning: {}", new Object[] { str });
/*     */               return;
/*     */             default:
/* 474 */               Injector.logger.error("Critical injection failure: {}", new Object[] { str });
/* 475 */               throw new InjectionError(str);
/*     */           } 
/*     */         
/*     */         } 
/*     */       } else {
/* 480 */         String str = this.methodNode.desc.replace("Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;", "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;");
/*     */ 
/*     */ 
/*     */         
/* 484 */         if (paramCallback.checkDescriptor(str))
/*     */         {
/*     */           
/* 487 */           throw new InvalidInjectionException(this.info, "Invalid descriptor on " + this.info + "! CallbackInfoReturnable is required!");
/*     */         }
/*     */         
/* 490 */         MethodNode methodNode1 = Bytecode.findMethod(this.classNode, this.methodNode.name, paramCallback.getDescriptor());
/* 491 */         if (methodNode1 != null && Annotations.getVisible(methodNode1, Surrogate.class) != null) {
/*     */           
/* 493 */           methodNode = methodNode1;
/*     */         } else {
/* 495 */           throw new InvalidInjectionException(this.info, "Invalid descriptor on " + this.info + "! Expected " + paramCallback.getDescriptor() + " but found " + this.methodNode.desc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 501 */     dupReturnValue(paramCallback);
/* 502 */     if (this.cancellable || this.totalInjections > 1) {
/* 503 */       createCallbackInfo(paramCallback, true);
/*     */     }
/* 505 */     invokeCallback(paramCallback, methodNode);
/* 506 */     injectCancellationCode(paramCallback);
/*     */     
/* 508 */     paramCallback.inject();
/* 509 */     this.info.notifyInjected(paramCallback.target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateBadLVTMessage(Callback paramCallback) {
/* 519 */     int i = paramCallback.target.indexOf(paramCallback.node);
/* 520 */     List<String> list1 = summariseLocals(this.methodNode.desc, paramCallback.target.arguments.length + 1);
/* 521 */     List<String> list2 = summariseLocals(paramCallback.getDescriptorWithAllLocals(), paramCallback.frameSize);
/* 522 */     return String.format("LVT in %s has incompatible changes at opcode %d in callback %s.\nExpected: %s\n   Found: %s", new Object[] { paramCallback.target, 
/* 523 */           Integer.valueOf(i), this, list1, list2 });
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
/*     */   private MethodNode generateErrorMethod(Callback paramCallback, String paramString1, String paramString2) {
/* 535 */     MethodNode methodNode = this.info.addMethod(this.methodNode.access, this.methodNode.name + "$missing", paramCallback.getDescriptor());
/* 536 */     methodNode.maxLocals = Bytecode.getFirstNonArgLocalIndex(Type.getArgumentTypes(paramCallback.getDescriptor()), !this.isStatic);
/* 537 */     methodNode.maxStack = 3;
/* 538 */     InsnList insnList = methodNode.instructions;
/* 539 */     insnList.add((AbstractInsnNode)new TypeInsnNode(187, paramString1));
/* 540 */     insnList.add((AbstractInsnNode)new InsnNode(89));
/* 541 */     insnList.add((AbstractInsnNode)new LdcInsnNode(paramString2));
/* 542 */     insnList.add((AbstractInsnNode)new MethodInsnNode(183, paramString1, "<init>", "(Ljava/lang/String;)V", false));
/* 543 */     insnList.add((AbstractInsnNode)new InsnNode(191));
/* 544 */     return methodNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printLocals(Callback paramCallback) {
/* 553 */     Type[] arrayOfType = Type.getArgumentTypes(paramCallback.getDescriptorWithAllLocals());
/* 554 */     SignaturePrinter signaturePrinter1 = new SignaturePrinter(paramCallback.target.method, paramCallback.argNames);
/* 555 */     SignaturePrinter signaturePrinter2 = new SignaturePrinter(this.methodNode.name, paramCallback.target.returnType, arrayOfType, paramCallback.argNames);
/* 556 */     signaturePrinter2.setModifiers(this.methodNode);
/*     */     
/* 558 */     PrettyPrinter prettyPrinter = new PrettyPrinter();
/* 559 */     prettyPrinter.kv("Target Class", this.classNode.name.replace('/', '.'));
/* 560 */     prettyPrinter.kv("Target Method", signaturePrinter1);
/* 561 */     prettyPrinter.kv("Target Max LOCALS", Integer.valueOf(paramCallback.target.getMaxLocals()));
/* 562 */     prettyPrinter.kv("Initial Frame Size", Integer.valueOf(paramCallback.frameSize));
/* 563 */     prettyPrinter.kv("Callback Name", this.methodNode.name);
/* 564 */     prettyPrinter.kv("Instruction", "%s %s", new Object[] { paramCallback.node.getClass().getSimpleName(), 
/* 565 */           Bytecode.getOpcodeName(paramCallback.node.getCurrentTarget().getOpcode()) });
/* 566 */     prettyPrinter.hr();
/* 567 */     if (paramCallback.locals.length > paramCallback.frameSize) {
/* 568 */       prettyPrinter.add("  %s  %20s  %s", new Object[] { "LOCAL", "TYPE", "NAME" });
/* 569 */       for (byte b = 0; b < paramCallback.locals.length; b++) {
/* 570 */         String str = (b == paramCallback.frameSize) ? ">" : " ";
/* 571 */         if (paramCallback.locals[b] != null) {
/* 572 */           prettyPrinter.add("%s [%3d]  %20s  %-50s %s", new Object[] { str, Integer.valueOf(b), SignaturePrinter.getTypeName(paramCallback.localTypes[b], false), 
/* 573 */                 meltSnowman(b, (paramCallback.locals[b]).name), (b >= paramCallback.frameSize) ? "<capture>" : "" });
/*     */         } else {
/* 575 */           boolean bool = (b > 0 && paramCallback.localTypes[b - 1] != null && paramCallback.localTypes[b - 1].getSize() > 1) ? true : false;
/* 576 */           prettyPrinter.add("%s [%3d]  %20s", new Object[] { str, Integer.valueOf(b), bool ? "<top>" : "-" });
/*     */         } 
/*     */       } 
/* 579 */       prettyPrinter.hr();
/*     */     } 
/* 581 */     prettyPrinter.add().add("/**").add(" * Expected callback signature").add(" * /");
/* 582 */     prettyPrinter.add("%s {", new Object[] { signaturePrinter2 });
/* 583 */     prettyPrinter.add("    // Method body").add("}").add().print(System.err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createCallbackInfo(Callback paramCallback, boolean paramBoolean) {
/* 592 */     if (paramCallback.target != this.lastTarget) {
/* 593 */       this.lastId = null;
/* 594 */       this.lastDesc = null;
/*     */     } 
/* 596 */     this.lastTarget = paramCallback.target;
/*     */     
/* 598 */     String str1 = getIdentifier(paramCallback);
/* 599 */     String str2 = paramCallback.getCallbackInfoConstructorDescriptor();
/*     */ 
/*     */     
/* 602 */     if (str1.equals(this.lastId) && str2.equals(this.lastDesc) && !paramCallback.isAtReturn && !this.cancellable) {
/*     */       return;
/*     */     }
/*     */     
/* 606 */     instanceCallbackInfo(paramCallback, str1, str2, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadOrCreateCallbackInfo(Callback paramCallback) {
/* 613 */     if (this.cancellable || this.totalInjections > 1) {
/* 614 */       paramCallback.add((AbstractInsnNode)new VarInsnNode(25, this.callbackInfoVar), false, true);
/*     */     } else {
/* 616 */       createCallbackInfo(paramCallback, false);
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
/*     */   private void dupReturnValue(Callback paramCallback) {
/* 629 */     if (!paramCallback.isAtReturn) {
/*     */       return;
/*     */     }
/*     */     
/* 633 */     paramCallback.add((AbstractInsnNode)new InsnNode(89));
/* 634 */     paramCallback.add((AbstractInsnNode)new VarInsnNode(paramCallback.target.returnType.getOpcode(54), paramCallback.marshalVar()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void instanceCallbackInfo(Callback paramCallback, String paramString1, String paramString2, boolean paramBoolean) {
/* 645 */     this.lastId = paramString1;
/* 646 */     this.lastDesc = paramString2;
/* 647 */     this.callbackInfoVar = paramCallback.marshalVar();
/* 648 */     this.callbackInfoClass = paramCallback.target.getCallbackInfoClass();
/*     */ 
/*     */ 
/*     */     
/* 652 */     boolean bool = (paramBoolean && this.totalInjections > 1 && !paramCallback.isAtReturn && !this.cancellable) ? true : false;
/*     */     
/* 654 */     paramCallback.add((AbstractInsnNode)new TypeInsnNode(187, this.callbackInfoClass), true, !paramBoolean, bool);
/* 655 */     paramCallback.add((AbstractInsnNode)new InsnNode(89), true, true, bool);
/* 656 */     paramCallback.add((AbstractInsnNode)new LdcInsnNode(paramString1), true, !paramBoolean, bool);
/* 657 */     paramCallback.add((AbstractInsnNode)new InsnNode(this.cancellable ? 4 : 3), true, !paramBoolean, bool);
/*     */     
/* 659 */     if (paramCallback.isAtReturn) {
/* 660 */       paramCallback.add((AbstractInsnNode)new VarInsnNode(paramCallback.target.returnType.getOpcode(21), paramCallback.marshalVar()), true, !paramBoolean);
/* 661 */       paramCallback.add((AbstractInsnNode)new MethodInsnNode(183, this.callbackInfoClass, "<init>", paramString2, false));
/*     */     } else {
/*     */       
/* 664 */       paramCallback.add((AbstractInsnNode)new MethodInsnNode(183, this.callbackInfoClass, "<init>", paramString2, false), false, false, bool);
/*     */     } 
/*     */ 
/*     */     
/* 668 */     if (paramBoolean) {
/* 669 */       paramCallback.target.addLocalVariable(this.callbackInfoVar, "callbackInfo" + this.callbackInfoVar, "L" + this.callbackInfoClass + ";");
/* 670 */       paramCallback.add((AbstractInsnNode)new VarInsnNode(58, this.callbackInfoVar), false, false, bool);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeCallback(Callback paramCallback, MethodNode paramMethodNode) {
/* 679 */     if (!this.isStatic) {
/* 680 */       paramCallback.add((AbstractInsnNode)new VarInsnNode(25, 0), false, true);
/*     */     }
/*     */ 
/*     */     
/* 684 */     if (paramCallback.captureArgs()) {
/* 685 */       Bytecode.loadArgs(paramCallback.target.arguments, paramCallback, this.isStatic ? 0 : 1, -1);
/*     */     }
/*     */ 
/*     */     
/* 689 */     loadOrCreateCallbackInfo(paramCallback);
/*     */ 
/*     */     
/* 692 */     if (paramCallback.canCaptureLocals) {
/* 693 */       Locals.loadLocals(paramCallback.localTypes, paramCallback, paramCallback.frameSize, paramCallback.extraArgs);
/*     */     }
/*     */ 
/*     */     
/* 697 */     invokeHandler(paramCallback, paramMethodNode);
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
/*     */   private String getIdentifier(Callback paramCallback) {
/* 709 */     String str1 = Strings.isNullOrEmpty(this.identifier) ? paramCallback.target.method.name : this.identifier;
/* 710 */     String str2 = this.ids.get(Integer.valueOf(paramCallback.node.getId()));
/* 711 */     return str1 + (Strings.isNullOrEmpty(str2) ? "" : (":" + str2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void injectCancellationCode(Callback paramCallback) {
/* 720 */     if (!this.cancellable) {
/*     */       return;
/*     */     }
/*     */     
/* 724 */     paramCallback.add((AbstractInsnNode)new VarInsnNode(25, this.callbackInfoVar));
/* 725 */     paramCallback.add((AbstractInsnNode)new MethodInsnNode(182, this.callbackInfoClass, CallbackInfo.getIsCancelledMethodName(), 
/* 726 */           CallbackInfo.getIsCancelledMethodSig(), false));
/*     */     
/* 728 */     LabelNode labelNode = new LabelNode();
/* 729 */     paramCallback.add((AbstractInsnNode)new JumpInsnNode(153, labelNode));
/*     */ 
/*     */ 
/*     */     
/* 733 */     injectReturnCode(paramCallback);
/*     */     
/* 735 */     paramCallback.add((AbstractInsnNode)labelNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void injectReturnCode(Callback paramCallback) {
/* 744 */     if (paramCallback.target.returnType.equals(Type.VOID_TYPE)) {
/*     */       
/* 746 */       paramCallback.add((AbstractInsnNode)new InsnNode(177));
/*     */     }
/*     */     else {
/*     */       
/* 750 */       paramCallback.add((AbstractInsnNode)new VarInsnNode(25, paramCallback.marshalVar()));
/* 751 */       String str1 = CallbackInfoReturnable.getReturnAccessor(paramCallback.target.returnType);
/* 752 */       String str2 = CallbackInfoReturnable.getReturnDescriptor(paramCallback.target.returnType);
/* 753 */       paramCallback.add((AbstractInsnNode)new MethodInsnNode(182, this.callbackInfoClass, str1, str2, false));
/* 754 */       if (paramCallback.target.returnType.getSort() == 10) {
/* 755 */         paramCallback.add((AbstractInsnNode)new TypeInsnNode(192, paramCallback.target.returnType.getInternalName()));
/*     */       }
/* 757 */       paramCallback.add((AbstractInsnNode)new InsnNode(paramCallback.target.returnType.getOpcode(172)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isStatic() {
/* 767 */     return this.isStatic;
/*     */   }
/*     */   
/*     */   private static List<String> summariseLocals(String paramString, int paramInt) {
/* 771 */     return summariseLocals(Type.getArgumentTypes(paramString), paramInt);
/*     */   }
/*     */   
/*     */   private static List<String> summariseLocals(Type[] paramArrayOfType, int paramInt) {
/* 775 */     ArrayList<String> arrayList = new ArrayList();
/* 776 */     if (paramArrayOfType != null) {
/* 777 */       for (; paramInt < paramArrayOfType.length; paramInt++) {
/* 778 */         if (paramArrayOfType[paramInt] != null) {
/* 779 */           arrayList.add(paramArrayOfType[paramInt].toString());
/*     */         }
/*     */       } 
/*     */     }
/* 783 */     return arrayList;
/*     */   }
/*     */   
/*     */   static String meltSnowman(int paramInt, String paramString) {
/* 787 */     return (paramString != null && '☃' == paramString.charAt(0)) ? ("var" + paramInt) : paramString;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\callback\CallbackInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */