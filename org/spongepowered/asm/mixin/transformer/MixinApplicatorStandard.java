/*      */ package org.spongepowered.asm.mixin.transformer;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Deque;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.SortedSet;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.spongepowered.asm.lib.Label;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.signature.SignatureReader;
/*      */ import org.spongepowered.asm.lib.signature.SignatureVisitor;
/*      */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldNode;
/*      */ import org.spongepowered.asm.lib.tree.LabelNode;
/*      */ import org.spongepowered.asm.lib.tree.LineNumberNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.mixin.Final;
/*      */ import org.spongepowered.asm.mixin.Intrinsic;
/*      */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*      */ import org.spongepowered.asm.mixin.Overwrite;
/*      */ import org.spongepowered.asm.mixin.injection.Inject;
/*      */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*      */ import org.spongepowered.asm.mixin.injection.ModifyArgs;
/*      */ import org.spongepowered.asm.mixin.injection.ModifyConstant;
/*      */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*      */ import org.spongepowered.asm.mixin.injection.Redirect;
/*      */ import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
/*      */ import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
/*      */ import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*      */ import org.spongepowered.asm.util.Annotations;
/*      */ import org.spongepowered.asm.util.Bytecode;
/*      */ import org.spongepowered.asm.util.ConstraintParser;
/*      */ import org.spongepowered.asm.util.ITokenProvider;
/*      */ import org.spongepowered.asm.util.perf.Profiler;
/*      */ import org.spongepowered.asm.util.throwables.ConstraintViolationException;
/*      */ import org.spongepowered.asm.util.throwables.InvalidConstraintException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MixinApplicatorStandard
/*      */ {
/*   83 */   protected static final List<Class<? extends Annotation>> CONSTRAINED_ANNOTATIONS = (List<Class<? extends Annotation>>)ImmutableList.of(Overwrite.class, Inject.class, ModifyArg.class, ModifyArgs.class, Redirect.class, ModifyVariable.class, ModifyConstant.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum ApplicatorPass
/*      */   {
/*  100 */     MAIN,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  105 */     PREINJECT,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  110 */     INJECT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum InitialiserInjectionMode
/*      */   {
/*  121 */     DEFAULT,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  127 */     SAFE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class Range
/*      */   {
/*      */     final int start;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int end;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int marker;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Range(int param1Int1, int param1Int2, int param1Int3) {
/*  157 */       this.start = param1Int1;
/*  158 */       this.end = param1Int2;
/*  159 */       this.marker = param1Int3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isValid() {
/*  169 */       return (this.start != 0 && this.end != 0 && this.end >= this.start);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean contains(int param1Int) {
/*  179 */       return (param1Int >= this.start && param1Int <= this.end);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean excludes(int param1Int) {
/*  188 */       return (param1Int < this.start || param1Int > this.end);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  196 */       return String.format("Range[%d-%d,%d,valid=%s)", new Object[] { Integer.valueOf(this.start), Integer.valueOf(this.end), Integer.valueOf(this.marker), Boolean.valueOf(isValid()) });
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
/*  209 */   protected static final int[] INITIALISER_OPCODE_BLACKLIST = new int[] { 177, 21, 22, 23, 24, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 79, 80, 81, 82, 83, 84, 85, 86 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  219 */   protected final Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final TargetClassContext context;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final String targetName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ClassNode targetClass;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  239 */   protected final Profiler profiler = MixinEnvironment.getProfiler();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean mergeSignatures;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinApplicatorStandard(TargetClassContext paramTargetClassContext) {
/*  250 */     this.context = paramTargetClassContext;
/*  251 */     this.targetName = paramTargetClassContext.getClassName();
/*  252 */     this.targetClass = paramTargetClassContext.getClassNode();
/*      */     
/*  254 */     ExtensionClassExporter extensionClassExporter = (ExtensionClassExporter)paramTargetClassContext.getExtensions().getExtension(ExtensionClassExporter.class);
/*  255 */     this
/*  256 */       .mergeSignatures = (extensionClassExporter.isDecompilerActive() && MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void apply(SortedSet<MixinInfo> paramSortedSet) {
/*  263 */     ArrayList<MixinTargetContext> arrayList = new ArrayList();
/*      */     
/*  265 */     for (MixinInfo mixinInfo : paramSortedSet) {
/*  266 */       this.logger.log(mixinInfo.getLoggingLevel(), "Mixing {} from {} into {}", new Object[] { mixinInfo.getName(), mixinInfo.getParent(), this.targetName });
/*  267 */       arrayList.add(mixinInfo.createContextFor(this.context));
/*      */     } 
/*      */     
/*  270 */     MixinTargetContext mixinTargetContext = null;
/*      */     
/*      */     try {
/*  273 */       for (MixinTargetContext mixinTargetContext1 : arrayList) {
/*  274 */         (mixinTargetContext = mixinTargetContext1).preApply(this.targetName, this.targetClass);
/*      */       }
/*      */       
/*  277 */       for (ApplicatorPass applicatorPass : ApplicatorPass.values()) {
/*  278 */         Profiler.Section section = this.profiler.begin(new String[] { "pass", applicatorPass.name().toLowerCase() });
/*  279 */         for (MixinTargetContext mixinTargetContext1 : arrayList) {
/*  280 */           applyMixin(mixinTargetContext = mixinTargetContext1, applicatorPass);
/*      */         }
/*  282 */         section.end();
/*      */       } 
/*      */       
/*  285 */       for (MixinTargetContext mixinTargetContext1 : arrayList) {
/*  286 */         (mixinTargetContext = mixinTargetContext1).postApply(this.targetName, this.targetClass);
/*      */       }
/*  288 */     } catch (InvalidMixinException invalidMixinException) {
/*  289 */       throw invalidMixinException;
/*  290 */     } catch (Exception exception) {
/*  291 */       throw new InvalidMixinException(mixinTargetContext, "Unexpecteded " + exception.getClass().getSimpleName() + " whilst applying the mixin class: " + exception
/*  292 */           .getMessage(), exception);
/*      */     } 
/*      */     
/*  295 */     applySourceMap(this.context);
/*  296 */     this.context.processDebugTasks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void applyMixin(MixinTargetContext paramMixinTargetContext, ApplicatorPass paramApplicatorPass) {
/*  305 */     switch (paramApplicatorPass) {
/*      */       case MAIN:
/*  307 */         applySignature(paramMixinTargetContext);
/*  308 */         applyInterfaces(paramMixinTargetContext);
/*  309 */         applyAttributes(paramMixinTargetContext);
/*  310 */         applyAnnotations(paramMixinTargetContext);
/*  311 */         applyFields(paramMixinTargetContext);
/*  312 */         applyMethods(paramMixinTargetContext);
/*  313 */         applyInitialisers(paramMixinTargetContext);
/*      */         return;
/*      */       
/*      */       case PREINJECT:
/*  317 */         prepareInjections(paramMixinTargetContext);
/*      */         return;
/*      */       
/*      */       case INJECT:
/*  321 */         applyAccessors(paramMixinTargetContext);
/*  322 */         applyInjections(paramMixinTargetContext);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  327 */     throw new IllegalStateException("Invalid pass specified " + paramApplicatorPass);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applySignature(MixinTargetContext paramMixinTargetContext) {
/*  332 */     if (this.mergeSignatures) {
/*  333 */       this.context.mergeSignature(paramMixinTargetContext.getSignature());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyInterfaces(MixinTargetContext paramMixinTargetContext) {
/*  343 */     for (String str : paramMixinTargetContext.getInterfaces()) {
/*  344 */       if (!this.targetClass.interfaces.contains(str)) {
/*  345 */         this.targetClass.interfaces.add(str);
/*  346 */         paramMixinTargetContext.getTargetClassInfo().addInterface(str);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyAttributes(MixinTargetContext paramMixinTargetContext) {
/*  357 */     if (paramMixinTargetContext.shouldSetSourceFile()) {
/*  358 */       this.targetClass.sourceFile = paramMixinTargetContext.getSourceFile();
/*      */     }
/*  360 */     this.targetClass.version = Math.max(this.targetClass.version, paramMixinTargetContext.getMinRequiredClassVersion());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyAnnotations(MixinTargetContext paramMixinTargetContext) {
/*  369 */     ClassNode classNode = paramMixinTargetContext.getClassNode();
/*  370 */     Bytecode.mergeAnnotations(classNode, this.targetClass);
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
/*      */   protected void applyFields(MixinTargetContext paramMixinTargetContext) {
/*  382 */     mergeShadowFields(paramMixinTargetContext);
/*  383 */     mergeNewFields(paramMixinTargetContext);
/*      */   }
/*      */   
/*      */   protected void mergeShadowFields(MixinTargetContext paramMixinTargetContext) {
/*  387 */     for (Map.Entry<FieldNode, ClassInfo.Field> entry : paramMixinTargetContext.getShadowFields()) {
/*  388 */       FieldNode fieldNode1 = (FieldNode)entry.getKey();
/*  389 */       FieldNode fieldNode2 = findTargetField(fieldNode1);
/*  390 */       if (fieldNode2 != null) {
/*  391 */         Bytecode.mergeAnnotations(fieldNode1, fieldNode2);
/*      */ 
/*      */         
/*  394 */         if (((ClassInfo.Field)entry.getValue()).isDecoratedMutable() && !Bytecode.hasFlag(fieldNode2, 2)) {
/*  395 */           fieldNode2.access &= 0xFFFFFFEF;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void mergeNewFields(MixinTargetContext paramMixinTargetContext) {
/*  402 */     for (FieldNode fieldNode1 : paramMixinTargetContext.getFields()) {
/*  403 */       FieldNode fieldNode2 = findTargetField(fieldNode1);
/*  404 */       if (fieldNode2 == null) {
/*      */         
/*  406 */         this.targetClass.fields.add(fieldNode1);
/*      */         
/*  408 */         if (fieldNode1.signature != null) {
/*  409 */           if (this.mergeSignatures) {
/*  410 */             SignatureVisitor signatureVisitor = paramMixinTargetContext.getSignature().getRemapper();
/*  411 */             (new SignatureReader(fieldNode1.signature)).accept(signatureVisitor);
/*  412 */             fieldNode1.signature = signatureVisitor.toString(); continue;
/*      */           } 
/*  414 */           fieldNode1.signature = null;
/*      */         } 
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
/*      */   protected void applyMethods(MixinTargetContext paramMixinTargetContext) {
/*  427 */     for (MethodNode methodNode : paramMixinTargetContext.getShadowMethods()) {
/*  428 */       applyShadowMethod(paramMixinTargetContext, methodNode);
/*      */     }
/*      */     
/*  431 */     for (MethodNode methodNode : paramMixinTargetContext.getMethods()) {
/*  432 */       applyNormalMethod(paramMixinTargetContext, methodNode);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void applyShadowMethod(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  437 */     MethodNode methodNode = findTargetMethod(paramMethodNode);
/*  438 */     if (methodNode != null) {
/*  439 */       Bytecode.mergeAnnotations(paramMethodNode, methodNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyNormalMethod(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  445 */     paramMixinTargetContext.transformMethod(paramMethodNode);
/*      */     
/*  447 */     if (!paramMethodNode.name.startsWith("<")) {
/*  448 */       checkMethodVisibility(paramMixinTargetContext, paramMethodNode);
/*  449 */       checkMethodConstraints(paramMixinTargetContext, paramMethodNode);
/*  450 */       mergeMethod(paramMixinTargetContext, paramMethodNode);
/*  451 */     } else if ("<clinit>".equals(paramMethodNode.name)) {
/*      */       
/*  453 */       appendInsns(paramMixinTargetContext, paramMethodNode);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void mergeMethod(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  464 */     boolean bool = (Annotations.getVisible(paramMethodNode, Overwrite.class) != null) ? true : false;
/*  465 */     MethodNode methodNode = findTargetMethod(paramMethodNode);
/*      */     
/*  467 */     if (methodNode != null) {
/*  468 */       if (isAlreadyMerged(paramMixinTargetContext, paramMethodNode, bool, methodNode)) {
/*      */         return;
/*      */       }
/*      */       
/*  472 */       AnnotationNode annotationNode = Annotations.getInvisible(paramMethodNode, Intrinsic.class);
/*  473 */       if (annotationNode != null) {
/*  474 */         if (mergeIntrinsic(paramMixinTargetContext, paramMethodNode, bool, methodNode, annotationNode)) {
/*  475 */           paramMixinTargetContext.getTarget().methodMerged(paramMethodNode);
/*      */           return;
/*      */         } 
/*      */       } else {
/*  479 */         if (paramMixinTargetContext.requireOverwriteAnnotations() && !bool) {
/*  480 */           throw new InvalidMixinException(paramMixinTargetContext, 
/*  481 */               String.format("%s%s in %s cannot overwrite method in %s because @Overwrite is required by the parent configuration", new Object[] {
/*  482 */                   paramMethodNode.name, paramMethodNode.desc, paramMixinTargetContext, paramMixinTargetContext.getTarget().getClassName()
/*      */                 }));
/*      */         }
/*  485 */         this.targetClass.methods.remove(methodNode);
/*      */       } 
/*  487 */     } else if (bool) {
/*  488 */       throw new InvalidMixinException(paramMixinTargetContext, String.format("Overwrite target \"%s\" was not located in target class %s", new Object[] { paramMethodNode.name, paramMixinTargetContext
/*  489 */               .getTargetClassRef() }));
/*      */     } 
/*      */     
/*  492 */     this.targetClass.methods.add(paramMethodNode);
/*  493 */     paramMixinTargetContext.methodMerged(paramMethodNode);
/*      */     
/*  495 */     if (paramMethodNode.signature != null) {
/*  496 */       if (this.mergeSignatures) {
/*  497 */         SignatureVisitor signatureVisitor = paramMixinTargetContext.getSignature().getRemapper();
/*  498 */         (new SignatureReader(paramMethodNode.signature)).accept(signatureVisitor);
/*  499 */         paramMethodNode.signature = signatureVisitor.toString();
/*      */       } else {
/*  501 */         paramMethodNode.signature = null;
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
/*      */   protected boolean isAlreadyMerged(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode1, boolean paramBoolean, MethodNode paramMethodNode2) {
/*  518 */     AnnotationNode annotationNode = Annotations.getVisible(paramMethodNode2, MixinMerged.class);
/*  519 */     if (annotationNode == null) {
/*  520 */       if (Annotations.getVisible(paramMethodNode2, Final.class) != null) {
/*  521 */         this.logger.warn("Overwrite prohibited for @Final method {} in {}. Skipping method.", new Object[] { paramMethodNode1.name, paramMixinTargetContext });
/*  522 */         return true;
/*      */       } 
/*  524 */       return false;
/*      */     } 
/*      */     
/*  527 */     String str1 = (String)Annotations.getValue(annotationNode, "sessionId");
/*      */     
/*  529 */     if (!this.context.getSessionId().equals(str1)) {
/*  530 */       throw new ClassFormatError("Invalid @MixinMerged annotation found in" + paramMixinTargetContext + " at " + paramMethodNode1.name + " in " + this.targetClass.name);
/*      */     }
/*      */     
/*  533 */     if (Bytecode.hasFlag(paramMethodNode2, 4160) && 
/*  534 */       Bytecode.hasFlag(paramMethodNode1, 4160)) {
/*  535 */       if (paramMixinTargetContext.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/*  536 */         this.logger.warn("Synthetic bridge method clash for {} in {}", new Object[] { paramMethodNode1.name, paramMixinTargetContext });
/*      */       }
/*  538 */       return true;
/*      */     } 
/*      */     
/*  541 */     String str2 = (String)Annotations.getValue(annotationNode, "mixin");
/*  542 */     int i = ((Integer)Annotations.getValue(annotationNode, "priority")).intValue();
/*      */     
/*  544 */     if (i >= paramMixinTargetContext.getPriority() && !str2.equals(paramMixinTargetContext.getClassName())) {
/*  545 */       this.logger.warn("Method overwrite conflict for {} in {}, previously written by {}. Skipping method.", new Object[] { paramMethodNode1.name, paramMixinTargetContext, str2 });
/*  546 */       return true;
/*      */     } 
/*      */     
/*  549 */     if (Annotations.getVisible(paramMethodNode2, Final.class) != null) {
/*  550 */       this.logger.warn("Method overwrite conflict for @Final method {} in {} declared by {}. Skipping method.", new Object[] { paramMethodNode1.name, paramMixinTargetContext, str2 });
/*  551 */       return true;
/*      */     } 
/*      */     
/*  554 */     return false;
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
/*      */   protected boolean mergeIntrinsic(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode1, boolean paramBoolean, MethodNode paramMethodNode2, AnnotationNode paramAnnotationNode) {
/*  573 */     if (paramBoolean) {
/*  574 */       throw new InvalidMixinException(paramMixinTargetContext, "@Intrinsic is not compatible with @Overwrite, remove one of these annotations on " + paramMethodNode1.name + " in " + paramMixinTargetContext);
/*      */     }
/*      */ 
/*      */     
/*  578 */     String str = paramMethodNode1.name + paramMethodNode1.desc;
/*  579 */     if (Bytecode.hasFlag(paramMethodNode1, 8)) {
/*  580 */       throw new InvalidMixinException(paramMixinTargetContext, "@Intrinsic method cannot be static, found " + str + " in " + paramMixinTargetContext);
/*      */     }
/*      */     
/*  583 */     if (!Bytecode.hasFlag(paramMethodNode1, 4096)) {
/*  584 */       AnnotationNode annotationNode = Annotations.getVisible(paramMethodNode1, MixinRenamed.class);
/*  585 */       if (annotationNode == null || !((Boolean)Annotations.getValue(annotationNode, "isInterfaceMember", Boolean.FALSE)).booleanValue()) {
/*  586 */         throw new InvalidMixinException(paramMixinTargetContext, "@Intrinsic method must be prefixed interface method, no rename encountered on " + str + " in " + paramMixinTargetContext);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  591 */     if (!((Boolean)Annotations.getValue(paramAnnotationNode, "displace", Boolean.FALSE)).booleanValue()) {
/*  592 */       this.logger.log(paramMixinTargetContext.getLoggingLevel(), "Skipping Intrinsic mixin method {} for {}", new Object[] { str, paramMixinTargetContext.getTargetClassRef() });
/*  593 */       return true;
/*      */     } 
/*      */     
/*  596 */     displaceIntrinsic(paramMixinTargetContext, paramMethodNode1, paramMethodNode2);
/*  597 */     return false;
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
/*      */   protected void displaceIntrinsic(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode1, MethodNode paramMethodNode2) {
/*  610 */     String str = "proxy+" + paramMethodNode2.name;
/*      */     
/*  612 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode1.instructions.iterator(); listIterator.hasNext(); ) {
/*  613 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  614 */       if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() != 184) {
/*  615 */         MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
/*  616 */         if (methodInsnNode.owner.equals(this.targetClass.name) && methodInsnNode.name.equals(paramMethodNode2.name) && methodInsnNode.desc.equals(paramMethodNode2.desc)) {
/*  617 */           methodInsnNode.name = str;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  622 */     paramMethodNode2.name = str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void appendInsns(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  633 */     if (Type.getReturnType(paramMethodNode.desc) != Type.VOID_TYPE) {
/*  634 */       throw new IllegalArgumentException("Attempted to merge insns from a method which does not return void");
/*      */     }
/*      */     
/*  637 */     MethodNode methodNode = findTargetMethod(paramMethodNode);
/*      */     
/*  639 */     if (methodNode != null) {
/*  640 */       AbstractInsnNode abstractInsnNode = Bytecode.findInsn(methodNode, 177);
/*      */       
/*  642 */       if (abstractInsnNode != null) {
/*  643 */         ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator();
/*  644 */         while (listIterator.hasNext()) {
/*  645 */           AbstractInsnNode abstractInsnNode1 = listIterator.next();
/*  646 */           if (!(abstractInsnNode1 instanceof LineNumberNode) && abstractInsnNode1.getOpcode() != 177) {
/*  647 */             methodNode.instructions.insertBefore(abstractInsnNode, abstractInsnNode1);
/*      */           }
/*      */         } 
/*      */         
/*  651 */         methodNode.maxLocals = Math.max(methodNode.maxLocals, paramMethodNode.maxLocals);
/*  652 */         methodNode.maxStack = Math.max(methodNode.maxStack, paramMethodNode.maxStack);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  658 */     this.targetClass.methods.add(paramMethodNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyInitialisers(MixinTargetContext paramMixinTargetContext) {
/*  669 */     MethodNode methodNode = getConstructor(paramMixinTargetContext);
/*  670 */     if (methodNode == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  675 */     Deque<AbstractInsnNode> deque = getInitialiser(paramMixinTargetContext, methodNode);
/*  676 */     if (deque == null || deque.size() == 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  681 */     for (MethodNode methodNode1 : this.targetClass.methods) {
/*  682 */       if ("<init>".equals(methodNode1.name)) {
/*  683 */         methodNode1.maxStack = Math.max(methodNode1.maxStack, methodNode.maxStack);
/*  684 */         injectInitialiser(paramMixinTargetContext, methodNode1, deque);
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
/*      */   protected MethodNode getConstructor(MixinTargetContext paramMixinTargetContext) {
/*  696 */     MethodNode methodNode = null;
/*      */     
/*  698 */     for (MethodNode methodNode1 : paramMixinTargetContext.getMethods()) {
/*  699 */       if ("<init>".equals(methodNode1.name) && Bytecode.methodHasLineNumbers(methodNode1)) {
/*  700 */         if (methodNode == null) {
/*  701 */           methodNode = methodNode1;
/*      */           continue;
/*      */         } 
/*  704 */         this.logger.warn(String.format("Mixin %s has multiple constructors, %s was selected\n", new Object[] { paramMixinTargetContext, methodNode.desc }));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  709 */     return methodNode;
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
/*      */   private Range getConstructorRange(MethodNode paramMethodNode) {
/*  721 */     boolean bool = false;
/*  722 */     AbstractInsnNode abstractInsnNode = null;
/*      */     
/*  724 */     int i = 0, j = 0, k = 0, m = -1;
/*  725 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  726 */       AbstractInsnNode abstractInsnNode1 = listIterator.next();
/*  727 */       if (abstractInsnNode1 instanceof LineNumberNode) {
/*  728 */         i = ((LineNumberNode)abstractInsnNode1).line;
/*  729 */         bool = true; continue;
/*  730 */       }  if (abstractInsnNode1 instanceof MethodInsnNode) {
/*  731 */         if (abstractInsnNode1.getOpcode() == 183 && "<init>".equals(((MethodInsnNode)abstractInsnNode1).name) && m == -1) {
/*  732 */           m = paramMethodNode.instructions.indexOf(abstractInsnNode1);
/*  733 */           j = i;
/*      */         }  continue;
/*  735 */       }  if (abstractInsnNode1.getOpcode() == 181) {
/*  736 */         bool = false; continue;
/*  737 */       }  if (abstractInsnNode1.getOpcode() == 177) {
/*  738 */         if (bool) {
/*  739 */           k = i; continue;
/*      */         } 
/*  741 */         k = j;
/*  742 */         abstractInsnNode = abstractInsnNode1;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  747 */     if (abstractInsnNode != null) {
/*  748 */       LabelNode labelNode = new LabelNode(new Label());
/*  749 */       paramMethodNode.instructions.insertBefore(abstractInsnNode, (AbstractInsnNode)labelNode);
/*  750 */       paramMethodNode.instructions.insertBefore(abstractInsnNode, (AbstractInsnNode)new LineNumberNode(j, labelNode));
/*      */     } 
/*      */     
/*  753 */     return new Range(j, k, m);
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
/*      */   protected final Deque<AbstractInsnNode> getInitialiser(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  771 */     Range range = getConstructorRange(paramMethodNode);
/*  772 */     if (!range.isValid()) {
/*  773 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  777 */     int i = 0;
/*  778 */     ArrayDeque<AbstractInsnNode> arrayDeque = new ArrayDeque();
/*  779 */     boolean bool = false;
/*  780 */     short s = -1;
/*  781 */     LabelNode labelNode = null;
/*  782 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(range.marker); listIterator.hasNext(); ) {
/*  783 */       AbstractInsnNode abstractInsnNode1 = listIterator.next();
/*  784 */       if (abstractInsnNode1 instanceof LineNumberNode) {
/*  785 */         i = ((LineNumberNode)abstractInsnNode1).line;
/*  786 */         AbstractInsnNode abstractInsnNode2 = paramMethodNode.instructions.get(paramMethodNode.instructions.indexOf(abstractInsnNode1) + 1);
/*  787 */         if (i == range.end && abstractInsnNode2.getOpcode() != 177) {
/*  788 */           bool = true;
/*  789 */           s = 177; continue;
/*      */         } 
/*  791 */         bool = range.excludes(i);
/*  792 */         s = -1; continue;
/*      */       } 
/*  794 */       if (bool) {
/*  795 */         if (labelNode != null) {
/*  796 */           arrayDeque.add(labelNode);
/*  797 */           labelNode = null;
/*      */         } 
/*      */         
/*  800 */         if (abstractInsnNode1 instanceof LabelNode) {
/*  801 */           labelNode = (LabelNode)abstractInsnNode1; continue;
/*      */         } 
/*  803 */         int j = abstractInsnNode1.getOpcode();
/*  804 */         if (j == s) {
/*  805 */           s = -1;
/*      */           continue;
/*      */         } 
/*  808 */         for (int k : INITIALISER_OPCODE_BLACKLIST) {
/*  809 */           if (j == k)
/*      */           {
/*      */             
/*  812 */             throw new InvalidMixinException(paramMixinTargetContext, "Cannot handle " + Bytecode.getOpcodeName(j) + " opcode (0x" + 
/*  813 */                 Integer.toHexString(j).toUpperCase() + ") in class initialiser");
/*      */           }
/*      */         } 
/*      */         
/*  817 */         arrayDeque.add(abstractInsnNode1);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  823 */     AbstractInsnNode abstractInsnNode = arrayDeque.peekLast();
/*  824 */     if (abstractInsnNode != null && 
/*  825 */       abstractInsnNode.getOpcode() != 181) {
/*  826 */       throw new InvalidMixinException(paramMixinTargetContext, "Could not parse initialiser, expected 0xB5, found 0x" + 
/*  827 */           Integer.toHexString(abstractInsnNode.getOpcode()) + " in " + paramMixinTargetContext);
/*      */     }
/*      */ 
/*      */     
/*  831 */     return arrayDeque;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void injectInitialiser(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, Deque<AbstractInsnNode> paramDeque) {
/*  842 */     Map map = Bytecode.cloneLabels(paramMethodNode.instructions);
/*      */     
/*  844 */     AbstractInsnNode abstractInsnNode = findInitialiserInjectionPoint(paramMixinTargetContext, paramMethodNode, paramDeque);
/*  845 */     if (abstractInsnNode == null) {
/*  846 */       this.logger.warn("Failed to locate initialiser injection point in <init>{}, initialiser was not mixed in.", new Object[] { paramMethodNode.desc });
/*      */       
/*      */       return;
/*      */     } 
/*  850 */     for (AbstractInsnNode abstractInsnNode1 : paramDeque) {
/*  851 */       if (abstractInsnNode1 instanceof LabelNode) {
/*      */         continue;
/*      */       }
/*  854 */       if (abstractInsnNode1 instanceof org.spongepowered.asm.lib.tree.JumpInsnNode) {
/*  855 */         throw new InvalidMixinException(paramMixinTargetContext, "Unsupported JUMP opcode in initialiser in " + paramMixinTargetContext);
/*      */       }
/*  857 */       AbstractInsnNode abstractInsnNode2 = abstractInsnNode1.clone(map);
/*  858 */       paramMethodNode.instructions.insert(abstractInsnNode, abstractInsnNode2);
/*  859 */       abstractInsnNode = abstractInsnNode2;
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
/*      */   protected AbstractInsnNode findInitialiserInjectionPoint(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, Deque<AbstractInsnNode> paramDeque) {
/*  873 */     HashSet<String> hashSet = new HashSet();
/*  874 */     for (AbstractInsnNode abstractInsnNode1 : paramDeque) {
/*  875 */       if (abstractInsnNode1.getOpcode() == 181) {
/*  876 */         hashSet.add(fieldKey((FieldInsnNode)abstractInsnNode1));
/*      */       }
/*      */     } 
/*      */     
/*  880 */     InitialiserInjectionMode initialiserInjectionMode = getInitialiserInjectionMode(paramMixinTargetContext.getEnvironment());
/*  881 */     String str1 = paramMixinTargetContext.getTargetClassInfo().getName();
/*  882 */     String str2 = paramMixinTargetContext.getTargetClassInfo().getSuperName();
/*  883 */     AbstractInsnNode abstractInsnNode = null;
/*      */     
/*  885 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  886 */       AbstractInsnNode abstractInsnNode1 = listIterator.next();
/*  887 */       if (abstractInsnNode1.getOpcode() == 183 && "<init>".equals(((MethodInsnNode)abstractInsnNode1).name)) {
/*  888 */         String str = ((MethodInsnNode)abstractInsnNode1).owner;
/*  889 */         if (str.equals(str1) || str.equals(str2)) {
/*  890 */           abstractInsnNode = abstractInsnNode1;
/*  891 */           if (initialiserInjectionMode == InitialiserInjectionMode.SAFE)
/*      */             break; 
/*      */         }  continue;
/*      */       } 
/*  895 */       if (abstractInsnNode1.getOpcode() == 181 && initialiserInjectionMode == InitialiserInjectionMode.DEFAULT) {
/*  896 */         String str = fieldKey((FieldInsnNode)abstractInsnNode1);
/*  897 */         if (hashSet.contains(str)) {
/*  898 */           abstractInsnNode = abstractInsnNode1;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  903 */     return abstractInsnNode;
/*      */   }
/*      */   
/*      */   private InitialiserInjectionMode getInitialiserInjectionMode(MixinEnvironment paramMixinEnvironment) {
/*  907 */     String str = paramMixinEnvironment.getOptionValue(MixinEnvironment.Option.INITIALISER_INJECTION_MODE);
/*  908 */     if (str == null) {
/*  909 */       return InitialiserInjectionMode.DEFAULT;
/*      */     }
/*      */     try {
/*  912 */       return InitialiserInjectionMode.valueOf(str.toUpperCase());
/*  913 */     } catch (Exception exception) {
/*  914 */       this.logger.warn("Could not parse unexpected value \"{}\" for mixin.initialiserInjectionMode, reverting to DEFAULT", new Object[] { str });
/*  915 */       return InitialiserInjectionMode.DEFAULT;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String fieldKey(FieldInsnNode paramFieldInsnNode) {
/*  920 */     return String.format("%s:%s", new Object[] { paramFieldInsnNode.desc, paramFieldInsnNode.name });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareInjections(MixinTargetContext paramMixinTargetContext) {
/*  929 */     paramMixinTargetContext.prepareInjections();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyInjections(MixinTargetContext paramMixinTargetContext) {
/*  938 */     paramMixinTargetContext.applyInjections();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyAccessors(MixinTargetContext paramMixinTargetContext) {
/*  947 */     List<MethodNode> list = paramMixinTargetContext.generateAccessors();
/*  948 */     for (MethodNode methodNode : list) {
/*  949 */       if (!methodNode.name.startsWith("<")) {
/*  950 */         mergeMethod(paramMixinTargetContext, methodNode);
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
/*      */   protected void checkMethodVisibility(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  962 */     if (Bytecode.hasFlag(paramMethodNode, 8) && 
/*  963 */       !Bytecode.hasFlag(paramMethodNode, 2) && 
/*  964 */       !Bytecode.hasFlag(paramMethodNode, 4096) && 
/*  965 */       Annotations.getVisible(paramMethodNode, Overwrite.class) == null) {
/*  966 */       throw new InvalidMixinException(paramMixinTargetContext, 
/*  967 */           String.format("Mixin %s contains non-private static method %s", new Object[] { paramMixinTargetContext, paramMethodNode }));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void applySourceMap(TargetClassContext paramTargetClassContext) {
/*  972 */     this.targetClass.sourceDebug = paramTargetClassContext.getSourceMap().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkMethodConstraints(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/*  982 */     for (Class<? extends Annotation> clazz : CONSTRAINED_ANNOTATIONS) {
/*  983 */       AnnotationNode annotationNode = Annotations.getVisible(paramMethodNode, clazz);
/*  984 */       if (annotationNode != null) {
/*  985 */         checkConstraints(paramMixinTargetContext, paramMethodNode, annotationNode);
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
/*      */   protected final void checkConstraints(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode) {
/*      */     try {
/* 1000 */       ConstraintParser.Constraint constraint = ConstraintParser.parse(paramAnnotationNode);
/*      */       try {
/* 1002 */         constraint.check((ITokenProvider)paramMixinTargetContext.getEnvironment());
/* 1003 */       } catch (ConstraintViolationException constraintViolationException) {
/* 1004 */         String str = String.format("Constraint violation: %s on %s in %s", new Object[] { constraintViolationException.getMessage(), paramMethodNode, paramMixinTargetContext });
/* 1005 */         this.logger.warn(str);
/* 1006 */         if (!paramMixinTargetContext.getEnvironment().getOption(MixinEnvironment.Option.IGNORE_CONSTRAINTS)) {
/* 1007 */           throw new InvalidMixinException(paramMixinTargetContext, str, constraintViolationException);
/*      */         }
/*      */       } 
/* 1010 */     } catch (InvalidConstraintException invalidConstraintException) {
/* 1011 */       throw new InvalidMixinException(paramMixinTargetContext, invalidConstraintException.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final MethodNode findTargetMethod(MethodNode paramMethodNode) {
/* 1022 */     for (MethodNode methodNode : this.targetClass.methods) {
/* 1023 */       if (methodNode.name.equals(paramMethodNode.name) && methodNode.desc.equals(paramMethodNode.desc)) {
/* 1024 */         return methodNode;
/*      */       }
/*      */     } 
/*      */     
/* 1028 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final FieldNode findTargetField(FieldNode paramFieldNode) {
/* 1038 */     for (FieldNode fieldNode : this.targetClass.fields) {
/* 1039 */       if (fieldNode.name.equals(paramFieldNode.name)) {
/* 1040 */         return fieldNode;
/*      */       }
/*      */     } 
/*      */     
/* 1044 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinApplicatorStandard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */