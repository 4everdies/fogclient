/*     */ package org.spongepowered.asm.mixin.injection.invoke;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LabelNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.Coerce;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeNew;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.util.Annotations;
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
/*     */ public class RedirectInjector
/*     */   extends InvokeInjector
/*     */ {
/*     */   private static final String KEY_NOMINATORS = "nominators";
/*     */   private static final String KEY_FUZZ = "fuzz";
/*     */   private static final String KEY_OPCODE = "opcode";
/*     */   protected Meta meta;
/*     */   
/*     */   class Meta
/*     */   {
/*     */     public static final String KEY = "redirector";
/*     */     final int priority;
/*     */     final boolean isFinal;
/*     */     final String name;
/*     */     final String desc;
/*     */     
/*     */     public Meta(int param1Int, boolean param1Boolean, String param1String1, String param1String2) {
/* 110 */       this.priority = param1Int;
/* 111 */       this.isFinal = param1Boolean;
/* 112 */       this.name = param1String1;
/* 113 */       this.desc = param1String2;
/*     */     }
/*     */     
/*     */     RedirectInjector getOwner() {
/* 117 */       return RedirectInjector.this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConstructorRedirectData
/*     */   {
/*     */     public static final String KEY = "ctor";
/*     */ 
/*     */     
/*     */     public boolean wildcard = false;
/*     */ 
/*     */     
/* 131 */     public int injected = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   static class RedirectedInvoke
/*     */   {
/*     */     final Target target;
/*     */     
/*     */     final MethodInsnNode node;
/*     */     
/*     */     final Type returnType;
/*     */     
/*     */     final Type[] args;
/*     */     
/*     */     final Type[] locals;
/*     */     boolean captureTargetArgs = false;
/*     */     
/*     */     RedirectedInvoke(Target param1Target, MethodInsnNode param1MethodInsnNode) {
/* 149 */       this.target = param1Target;
/* 150 */       this.node = param1MethodInsnNode;
/* 151 */       this.returnType = Type.getReturnType(param1MethodInsnNode.desc);
/* 152 */       this.args = Type.getArgumentTypes(param1MethodInsnNode.desc);
/* 153 */       this
/*     */         
/* 155 */         .locals = (param1MethodInsnNode.getOpcode() == 184) ? this.args : (Type[])ObjectArrays.concat(Type.getType("L" + param1MethodInsnNode.owner + ";"), (Object[])this.args);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   private Map<BeforeNew, ConstructorRedirectData> ctorRedirectors = new HashMap<BeforeNew, ConstructorRedirectData>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectInjector(InjectionInfo paramInjectionInfo) {
/* 167 */     this(paramInjectionInfo, "@Redirect");
/*     */   }
/*     */   
/*     */   protected RedirectInjector(InjectionInfo paramInjectionInfo, String paramString) {
/* 171 */     super(paramInjectionInfo, paramString);
/*     */     
/* 173 */     int i = paramInjectionInfo.getContext().getPriority();
/* 174 */     boolean bool = (Annotations.getVisible(this.methodNode, Final.class) != null) ? true : false;
/* 175 */     this.meta = new Meta(i, bool, this.info.toString(), this.methodNode.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTarget(Target paramTarget) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTargetNode(Target paramTarget, List<InjectionNodes.InjectionNode> paramList, AbstractInsnNode paramAbstractInsnNode, Set<InjectionPoint> paramSet) {
/* 189 */     InjectionNodes.InjectionNode injectionNode1 = paramTarget.getInjectionNode(paramAbstractInsnNode);
/* 190 */     ConstructorRedirectData constructorRedirectData = null;
/* 191 */     int i = 8;
/* 192 */     int j = 0;
/*     */     
/* 194 */     if (injectionNode1 != null) {
/* 195 */       Meta meta = (Meta)injectionNode1.getDecoration("redirector");
/*     */       
/* 197 */       if (meta != null && meta.getOwner() != this) {
/* 198 */         if (meta.priority >= this.meta.priority) {
/* 199 */           Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, 
/* 200 */                 Integer.valueOf(this.meta.priority), meta.name, Integer.valueOf(meta.priority) }); return;
/*     */         } 
/* 202 */         if (meta.isFinal) {
/* 203 */           throw new InvalidInjectionException(this.info, String.format("%s conflict: %s failed because target was already remapped by %s", new Object[] { this.annotationType, this, meta.name }));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 209 */     for (InjectionPoint injectionPoint : paramSet) {
/* 210 */       if (injectionPoint instanceof BeforeNew) {
/* 211 */         constructorRedirectData = getCtorRedirect((BeforeNew)injectionPoint);
/* 212 */         constructorRedirectData.wildcard = !((BeforeNew)injectionPoint).hasDescriptor(); continue;
/* 213 */       }  if (injectionPoint instanceof BeforeFieldAccess) {
/* 214 */         BeforeFieldAccess beforeFieldAccess = (BeforeFieldAccess)injectionPoint;
/* 215 */         i = beforeFieldAccess.getFuzzFactor();
/* 216 */         j = beforeFieldAccess.getArrayOpcode();
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     InjectionNodes.InjectionNode injectionNode2 = paramTarget.addInjectionNode(paramAbstractInsnNode);
/* 221 */     injectionNode2.decorate("redirector", this.meta);
/* 222 */     injectionNode2.decorate("nominators", paramSet);
/* 223 */     if (paramAbstractInsnNode instanceof TypeInsnNode && paramAbstractInsnNode.getOpcode() == 187) {
/* 224 */       injectionNode2.decorate("ctor", constructorRedirectData);
/*     */     } else {
/* 226 */       injectionNode2.decorate("fuzz", Integer.valueOf(i));
/* 227 */       injectionNode2.decorate("opcode", Integer.valueOf(j));
/*     */     } 
/* 229 */     paramList.add(injectionNode2);
/*     */   }
/*     */   
/*     */   private ConstructorRedirectData getCtorRedirect(BeforeNew paramBeforeNew) {
/* 233 */     ConstructorRedirectData constructorRedirectData = this.ctorRedirectors.get(paramBeforeNew);
/* 234 */     if (constructorRedirectData == null) {
/* 235 */       constructorRedirectData = new ConstructorRedirectData();
/* 236 */       this.ctorRedirectors.put(paramBeforeNew, constructorRedirectData);
/*     */     } 
/* 238 */     return constructorRedirectData;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 243 */     if (!preInject(paramInjectionNode)) {
/*     */       return;
/*     */     }
/*     */     
/* 247 */     if (paramInjectionNode.isReplaced()) {
/* 248 */       throw new UnsupportedOperationException("Redirector target failure for " + this.info);
/*     */     }
/*     */     
/* 251 */     if (paramInjectionNode.getCurrentTarget() instanceof MethodInsnNode) {
/* 252 */       checkTargetForNode(paramTarget, paramInjectionNode);
/* 253 */       injectAtInvoke(paramTarget, paramInjectionNode);
/*     */       
/*     */       return;
/*     */     } 
/* 257 */     if (paramInjectionNode.getCurrentTarget() instanceof FieldInsnNode) {
/* 258 */       checkTargetForNode(paramTarget, paramInjectionNode);
/* 259 */       injectAtFieldAccess(paramTarget, paramInjectionNode);
/*     */       
/*     */       return;
/*     */     } 
/* 263 */     if (paramInjectionNode.getCurrentTarget() instanceof TypeInsnNode && paramInjectionNode.getCurrentTarget().getOpcode() == 187) {
/* 264 */       if (!this.isStatic && paramTarget.isStatic) {
/* 265 */         throw new InvalidInjectionException(this.info, String.format("non-static callback method %s has a static target which is not supported", new Object[] { this }));
/*     */       }
/*     */       
/* 268 */       injectAtConstructor(paramTarget, paramInjectionNode);
/*     */       
/*     */       return;
/*     */     } 
/* 272 */     throw new InvalidInjectionException(this.info, String.format("%s annotation on is targetting an invalid insn in %s in %s", new Object[] { this.annotationType, paramTarget, this }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean preInject(InjectionNodes.InjectionNode paramInjectionNode) {
/* 277 */     Meta meta = (Meta)paramInjectionNode.getDecoration("redirector");
/* 278 */     if (meta.getOwner() != this) {
/* 279 */       Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, 
/* 280 */             Integer.valueOf(this.meta.priority), meta.name, Integer.valueOf(meta.priority) });
/* 281 */       return false;
/*     */     } 
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void postInject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 288 */     super.postInject(paramTarget, paramInjectionNode);
/* 289 */     if (paramInjectionNode.getOriginalTarget() instanceof TypeInsnNode && paramInjectionNode.getOriginalTarget().getOpcode() == 187) {
/* 290 */       ConstructorRedirectData constructorRedirectData = (ConstructorRedirectData)paramInjectionNode.getDecoration("ctor");
/* 291 */       if (constructorRedirectData.wildcard && constructorRedirectData.injected == 0) {
/* 292 */         throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", new Object[] { this.annotationType, paramTarget }));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void injectAtInvoke(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 302 */     RedirectedInvoke redirectedInvoke = new RedirectedInvoke(paramTarget, (MethodInsnNode)paramInjectionNode.getCurrentTarget());
/*     */     
/* 304 */     validateParams(redirectedInvoke);
/*     */     
/* 306 */     InsnList insnList = new InsnList();
/* 307 */     int i = Bytecode.getArgsSize(redirectedInvoke.locals) + 1;
/* 308 */     int j = 1;
/* 309 */     int[] arrayOfInt = storeArgs(paramTarget, redirectedInvoke.locals, insnList, 0);
/* 310 */     if (redirectedInvoke.captureTargetArgs) {
/* 311 */       int k = Bytecode.getArgsSize(paramTarget.arguments);
/* 312 */       i += k;
/* 313 */       j += k;
/* 314 */       arrayOfInt = Ints.concat(new int[][] { arrayOfInt, paramTarget.getArgIndices() });
/*     */     } 
/* 316 */     AbstractInsnNode abstractInsnNode = invokeHandlerWithArgs(this.methodArgs, insnList, arrayOfInt);
/* 317 */     paramTarget.replaceNode((AbstractInsnNode)redirectedInvoke.node, abstractInsnNode, insnList);
/* 318 */     paramTarget.addToLocals(i);
/* 319 */     paramTarget.addToStack(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateParams(RedirectedInvoke paramRedirectedInvoke) {
/* 330 */     int i = this.methodArgs.length;
/*     */     
/* 332 */     String str = String.format("%s handler method %s", new Object[] { this.annotationType, this });
/* 333 */     if (!paramRedirectedInvoke.returnType.equals(this.returnType)) {
/* 334 */       throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Expected return type %s found %s", new Object[] { str, this.returnType, paramRedirectedInvoke.returnType }));
/*     */     }
/*     */ 
/*     */     
/* 338 */     for (byte b = 0; b < i; b++) {
/* 339 */       Type type1 = null;
/* 340 */       if (b >= this.methodArgs.length) {
/* 341 */         throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Not enough arguments found for capture of target method args, expected %d but found %d", new Object[] { str, 
/*     */                 
/* 343 */                 Integer.valueOf(i), Integer.valueOf(this.methodArgs.length) }));
/*     */       }
/*     */       
/* 346 */       Type type2 = this.methodArgs[b];
/*     */       
/* 348 */       if (b < paramRedirectedInvoke.locals.length) {
/* 349 */         type1 = paramRedirectedInvoke.locals[b];
/*     */       } else {
/* 351 */         paramRedirectedInvoke.captureTargetArgs = true;
/* 352 */         i = Math.max(i, paramRedirectedInvoke.locals.length + paramRedirectedInvoke.target.arguments.length);
/* 353 */         int j = b - paramRedirectedInvoke.locals.length;
/* 354 */         if (j >= paramRedirectedInvoke.target.arguments.length) {
/* 355 */           throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected additional target argument with type %s at index %d", new Object[] { str, type2, 
/*     */                   
/* 357 */                   Integer.valueOf(b) }));
/*     */         }
/* 359 */         type1 = paramRedirectedInvoke.target.arguments[j];
/*     */       } 
/*     */       
/* 362 */       AnnotationNode annotationNode = Annotations.getInvisibleParameter(this.methodNode, Coerce.class, b);
/*     */       
/* 364 */       if (type2.equals(type1)) {
/* 365 */         if (annotationNode != null && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/* 366 */           Injector.logger.warn("Redundant @Coerce on {} argument {}, {} is identical to {}", new Object[] { str, Integer.valueOf(b), type1, type2 });
/*     */         
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 372 */         boolean bool = Injector.canCoerce(type2, type1);
/* 373 */         if (annotationNode == null) {
/* 374 */           throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected argument type %s at index %d, expected %s", new Object[] { str, type2, 
/*     */                   
/* 376 */                   Integer.valueOf(b), type1 }));
/*     */         }
/*     */         
/* 379 */         if (!bool) {
/* 380 */           throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Cannot @Coerce argument type %s at index %d to %s", new Object[] { str, type1, 
/*     */                   
/* 382 */                   Integer.valueOf(b), type2 }));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectAtFieldAccess(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 391 */     FieldInsnNode fieldInsnNode = (FieldInsnNode)paramInjectionNode.getCurrentTarget();
/* 392 */     int i = fieldInsnNode.getOpcode();
/* 393 */     Type type1 = Type.getType("L" + fieldInsnNode.owner + ";");
/* 394 */     Type type2 = Type.getType(fieldInsnNode.desc);
/*     */     
/* 396 */     byte b1 = (type2.getSort() == 9) ? type2.getDimensions() : 0;
/* 397 */     byte b2 = (this.returnType.getSort() == 9) ? this.returnType.getDimensions() : 0;
/*     */     
/* 399 */     if (b2 > b1)
/* 400 */       throw new InvalidInjectionException(this.info, "Dimensionality of handler method is greater than target array on " + this); 
/* 401 */     if (b2 == 0 && b1 > 0) {
/* 402 */       int j = ((Integer)paramInjectionNode.getDecoration("fuzz")).intValue();
/* 403 */       int k = ((Integer)paramInjectionNode.getDecoration("opcode")).intValue();
/* 404 */       injectAtArrayField(paramTarget, fieldInsnNode, i, type1, type2, j, k);
/*     */     } else {
/* 406 */       injectAtScalarField(paramTarget, fieldInsnNode, i, type1, type2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectAtArrayField(Target paramTarget, FieldInsnNode paramFieldInsnNode, int paramInt1, Type paramType1, Type paramType2, int paramInt2, int paramInt3) {
/* 414 */     Type type = paramType2.getElementType();
/* 415 */     if (paramInt1 != 178 && paramInt1 != 180)
/* 416 */       throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for array access %s", new Object[] {
/* 417 */               Bytecode.getOpcodeName(paramInt1), this.info })); 
/* 418 */     if (this.returnType.getSort() != 0) {
/* 419 */       if (paramInt3 != 190) {
/* 420 */         paramInt3 = type.getOpcode(46);
/*     */       }
/* 422 */       AbstractInsnNode abstractInsnNode = BeforeFieldAccess.findArrayNode(paramTarget.insns, paramFieldInsnNode, paramInt3, paramInt2);
/* 423 */       injectAtGetArray(paramTarget, paramFieldInsnNode, abstractInsnNode, paramType1, paramType2);
/*     */     } else {
/* 425 */       AbstractInsnNode abstractInsnNode = BeforeFieldAccess.findArrayNode(paramTarget.insns, paramFieldInsnNode, type.getOpcode(79), paramInt2);
/* 426 */       injectAtSetArray(paramTarget, paramFieldInsnNode, abstractInsnNode, paramType1, paramType2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectAtGetArray(Target paramTarget, FieldInsnNode paramFieldInsnNode, AbstractInsnNode paramAbstractInsnNode, Type paramType1, Type paramType2) {
/* 434 */     String str = getGetArrayHandlerDescriptor(paramAbstractInsnNode, this.returnType, paramType2);
/* 435 */     boolean bool = checkDescriptor(str, paramTarget, "array getter");
/* 436 */     injectArrayRedirect(paramTarget, paramFieldInsnNode, paramAbstractInsnNode, bool, "array getter");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectAtSetArray(Target paramTarget, FieldInsnNode paramFieldInsnNode, AbstractInsnNode paramAbstractInsnNode, Type paramType1, Type paramType2) {
/* 443 */     String str = Bytecode.generateDescriptor(null, (Object[])getArrayArgs(paramType2, 1, new Type[] { paramType2.getElementType() }));
/* 444 */     boolean bool = checkDescriptor(str, paramTarget, "array setter");
/* 445 */     injectArrayRedirect(paramTarget, paramFieldInsnNode, paramAbstractInsnNode, bool, "array setter");
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
/*     */   public void injectArrayRedirect(Target paramTarget, FieldInsnNode paramFieldInsnNode, AbstractInsnNode paramAbstractInsnNode, boolean paramBoolean, String paramString) {
/* 462 */     if (paramAbstractInsnNode == null) {
/* 463 */       String str = "";
/* 464 */       throw new InvalidInjectionException(this.info, String.format("Array element %s on %s could not locate a matching %s instruction in %s. %s", new Object[] { this.annotationType, this, paramString, paramTarget, str }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 469 */     if (!this.isStatic) {
/* 470 */       paramTarget.insns.insertBefore((AbstractInsnNode)paramFieldInsnNode, (AbstractInsnNode)new VarInsnNode(25, 0));
/* 471 */       paramTarget.addToStack(1);
/*     */     } 
/*     */     
/* 474 */     InsnList insnList = new InsnList();
/* 475 */     if (paramBoolean) {
/* 476 */       pushArgs(paramTarget.arguments, insnList, paramTarget.getArgIndices(), 0, paramTarget.arguments.length);
/* 477 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*     */     } 
/* 479 */     paramTarget.replaceNode(paramAbstractInsnNode, invokeHandler(insnList), insnList);
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
/*     */   public void injectAtScalarField(Target paramTarget, FieldInsnNode paramFieldInsnNode, int paramInt, Type paramType1, Type paramType2) {
/* 492 */     AbstractInsnNode abstractInsnNode = null;
/* 493 */     InsnList insnList = new InsnList();
/* 494 */     if (paramInt == 178 || paramInt == 180) {
/* 495 */       abstractInsnNode = injectAtGetField(insnList, paramTarget, paramFieldInsnNode, (paramInt == 178), paramType1, paramType2);
/* 496 */     } else if (paramInt == 179 || paramInt == 181) {
/* 497 */       abstractInsnNode = injectAtPutField(insnList, paramTarget, paramFieldInsnNode, (paramInt == 179), paramType1, paramType2);
/*     */     } else {
/* 499 */       throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for %s", new Object[] { Bytecode.getOpcodeName(paramInt), this.info }));
/*     */     } 
/*     */     
/* 502 */     paramTarget.replaceNode((AbstractInsnNode)paramFieldInsnNode, abstractInsnNode, insnList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractInsnNode injectAtGetField(InsnList paramInsnList, Target paramTarget, FieldInsnNode paramFieldInsnNode, boolean paramBoolean, Type paramType1, Type paramType2) {
/* 512 */     String str = paramBoolean ? Bytecode.generateDescriptor(paramType2, new Object[0]) : Bytecode.generateDescriptor(paramType2, new Object[] { paramType1 });
/* 513 */     boolean bool = checkDescriptor(str, paramTarget, "getter");
/*     */     
/* 515 */     if (!this.isStatic) {
/* 516 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 517 */       if (!paramBoolean) {
/* 518 */         paramInsnList.add((AbstractInsnNode)new InsnNode(95));
/*     */       }
/*     */     } 
/*     */     
/* 522 */     if (bool) {
/* 523 */       pushArgs(paramTarget.arguments, paramInsnList, paramTarget.getArgIndices(), 0, paramTarget.arguments.length);
/* 524 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*     */     } 
/*     */     
/* 527 */     paramTarget.addToStack(this.isStatic ? 0 : 1);
/* 528 */     return invokeHandler(paramInsnList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractInsnNode injectAtPutField(InsnList paramInsnList, Target paramTarget, FieldInsnNode paramFieldInsnNode, boolean paramBoolean, Type paramType1, Type paramType2) {
/* 538 */     String str = paramBoolean ? Bytecode.generateDescriptor(null, new Object[] { paramType2 }) : Bytecode.generateDescriptor(null, new Object[] { paramType1, paramType2 });
/* 539 */     boolean bool = checkDescriptor(str, paramTarget, "setter");
/*     */     
/* 541 */     if (!this.isStatic) {
/* 542 */       if (paramBoolean) {
/* 543 */         paramInsnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 544 */         paramInsnList.add((AbstractInsnNode)new InsnNode(95));
/*     */       } else {
/* 546 */         int i = paramTarget.allocateLocals(paramType2.getSize());
/* 547 */         paramInsnList.add((AbstractInsnNode)new VarInsnNode(paramType2.getOpcode(54), i));
/* 548 */         paramInsnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 549 */         paramInsnList.add((AbstractInsnNode)new InsnNode(95));
/* 550 */         paramInsnList.add((AbstractInsnNode)new VarInsnNode(paramType2.getOpcode(21), i));
/*     */       } 
/*     */     }
/*     */     
/* 554 */     if (bool) {
/* 555 */       pushArgs(paramTarget.arguments, paramInsnList, paramTarget.getArgIndices(), 0, paramTarget.arguments.length);
/* 556 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*     */     } 
/*     */     
/* 559 */     paramTarget.addToStack((!this.isStatic && !paramBoolean) ? 1 : 0);
/* 560 */     return invokeHandler(paramInsnList);
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
/*     */   protected boolean checkDescriptor(String paramString1, Target paramTarget, String paramString2) {
/* 574 */     if (this.methodNode.desc.equals(paramString1)) {
/* 575 */       return false;
/*     */     }
/*     */     
/* 578 */     int i = paramString1.indexOf(')');
/* 579 */     String str = String.format("%s%s%s", new Object[] { paramString1.substring(0, i), Joiner.on("").join((Object[])paramTarget.arguments), paramString1.substring(i) });
/* 580 */     if (this.methodNode.desc.equals(str)) {
/* 581 */       return true;
/*     */     }
/*     */     
/* 584 */     throw new InvalidInjectionException(this.info, String.format("%s method %s %s has an invalid signature. Expected %s but found %s", new Object[] { this.annotationType, paramString2, this, paramString1, this.methodNode.desc }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void injectAtConstructor(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 589 */     ConstructorRedirectData constructorRedirectData = (ConstructorRedirectData)paramInjectionNode.getDecoration("ctor");
/*     */     
/* 591 */     if (constructorRedirectData == null)
/*     */     {
/* 593 */       throw new InvalidInjectionException(this.info, String.format("%s ctor redirector has no metadata, the injector failed a preprocessing phase", new Object[] { this.annotationType }));
/*     */     }
/*     */ 
/*     */     
/* 597 */     TypeInsnNode typeInsnNode = (TypeInsnNode)paramInjectionNode.getCurrentTarget();
/* 598 */     AbstractInsnNode abstractInsnNode = paramTarget.get(paramTarget.indexOf((AbstractInsnNode)typeInsnNode) + 1);
/* 599 */     MethodInsnNode methodInsnNode = paramTarget.findInitNodeFor(typeInsnNode);
/*     */     
/* 601 */     if (methodInsnNode == null) {
/* 602 */       if (!constructorRedirectData.wildcard) {
/* 603 */         throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", new Object[] { this.annotationType, paramTarget }));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 609 */     boolean bool = (abstractInsnNode.getOpcode() == 89) ? true : false;
/* 610 */     String str = methodInsnNode.desc.replace(")V", ")L" + typeInsnNode.desc + ";");
/* 611 */     boolean bool1 = false;
/*     */     try {
/* 613 */       bool1 = checkDescriptor(str, paramTarget, "constructor");
/* 614 */     } catch (InvalidInjectionException invalidInjectionException) {
/* 615 */       if (!constructorRedirectData.wildcard) {
/* 616 */         throw invalidInjectionException;
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 621 */     if (bool) {
/* 622 */       paramTarget.removeNode(abstractInsnNode);
/*     */     }
/*     */     
/* 625 */     if (this.isStatic) {
/* 626 */       paramTarget.removeNode((AbstractInsnNode)typeInsnNode);
/*     */     } else {
/* 628 */       paramTarget.replaceNode((AbstractInsnNode)typeInsnNode, (AbstractInsnNode)new VarInsnNode(25, 0));
/*     */     } 
/*     */     
/* 631 */     InsnList insnList = new InsnList();
/* 632 */     if (bool1) {
/* 633 */       pushArgs(paramTarget.arguments, insnList, paramTarget.getArgIndices(), 0, paramTarget.arguments.length);
/* 634 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*     */     } 
/*     */     
/* 637 */     invokeHandler(insnList);
/*     */     
/* 639 */     if (bool) {
/*     */ 
/*     */ 
/*     */       
/* 643 */       LabelNode labelNode = new LabelNode();
/* 644 */       insnList.add((AbstractInsnNode)new InsnNode(89));
/* 645 */       insnList.add((AbstractInsnNode)new JumpInsnNode(199, labelNode));
/* 646 */       throwException(insnList, "java/lang/NullPointerException", String.format("%s constructor handler %s returned null for %s", new Object[] { this.annotationType, this, typeInsnNode.desc
/* 647 */               .replace('/', '.') }));
/* 648 */       insnList.add((AbstractInsnNode)labelNode);
/* 649 */       paramTarget.addToStack(1);
/*     */     } else {
/*     */       
/* 652 */       insnList.add((AbstractInsnNode)new InsnNode(87));
/*     */     } 
/*     */     
/* 655 */     paramTarget.replaceNode((AbstractInsnNode)methodInsnNode, insnList);
/* 656 */     constructorRedirectData.injected++;
/*     */   }
/*     */   
/*     */   private static String getGetArrayHandlerDescriptor(AbstractInsnNode paramAbstractInsnNode, Type paramType1, Type paramType2) {
/* 660 */     if (paramAbstractInsnNode != null && paramAbstractInsnNode.getOpcode() == 190) {
/* 661 */       return Bytecode.generateDescriptor(Type.INT_TYPE, (Object[])getArrayArgs(paramType2, 0, new Type[0]));
/*     */     }
/* 663 */     return Bytecode.generateDescriptor(paramType1, (Object[])getArrayArgs(paramType2, 1, new Type[0]));
/*     */   }
/*     */   
/*     */   private static Type[] getArrayArgs(Type paramType, int paramInt, Type... paramVarArgs) {
/* 667 */     int i = paramType.getDimensions() + paramInt;
/* 668 */     Type[] arrayOfType = new Type[i + paramVarArgs.length];
/* 669 */     for (byte b = 0; b < arrayOfType.length; b++) {
/* 670 */       arrayOfType[b] = (b == 0) ? paramType : ((b < i) ? Type.INT_TYPE : paramVarArgs[i - b]);
/*     */     }
/* 672 */     return arrayOfType;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\RedirectInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */