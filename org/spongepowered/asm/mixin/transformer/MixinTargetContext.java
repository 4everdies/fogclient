/*      */ package org.spongepowered.asm.mixin.transformer;
/*      */ 
/*      */ import com.google.common.collect.BiMap;
/*      */ import com.google.common.collect.HashBiMap;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.spongepowered.asm.lib.Handle;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldNode;
/*      */ import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.LdcInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*      */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*      */ import org.spongepowered.asm.mixin.SoftOverride;
/*      */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*      */ import org.spongepowered.asm.mixin.gen.AccessorInfo;
/*      */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*      */ import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
/*      */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*      */ import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
/*      */ import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
/*      */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*      */ import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
/*      */ import org.spongepowered.asm.mixin.struct.MemberRef;
/*      */ import org.spongepowered.asm.mixin.struct.SourceMap;
/*      */ import org.spongepowered.asm.mixin.transformer.ext.Extensions;
/*      */ import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
/*      */ import org.spongepowered.asm.obfuscation.RemapperChain;
/*      */ import org.spongepowered.asm.util.Annotations;
/*      */ import org.spongepowered.asm.util.Bytecode;
/*      */ import org.spongepowered.asm.util.ClassSignature;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MixinTargetContext
/*      */   extends ClassContext
/*      */   implements IMixinContext
/*      */ {
/*   93 */   private static final Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final MixinInfo mixin;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ClassNode classNode;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final TargetClassContext targetClass;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String sessionId;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ClassInfo targetClassInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  123 */   private final BiMap<String, String> innerClasses = (BiMap<String, String>)HashBiMap.create();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  128 */   private final List<MethodNode> shadowMethods = new ArrayList<MethodNode>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  133 */   private final Map<FieldNode, ClassInfo.Field> shadowFields = new LinkedHashMap<FieldNode, ClassInfo.Field>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  138 */   private final List<MethodNode> mergedMethods = new ArrayList<MethodNode>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   private final InjectorGroupInfo.Map injectorGroups = new InjectorGroupInfo.Map();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  148 */   private final List<InjectionInfo> injectors = new ArrayList<InjectionInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  153 */   private final List<AccessorInfo> accessors = new ArrayList<AccessorInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean inheritsFromMixin;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean detachedSuper;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final SourceMap.File stratum;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  174 */   private int minRequiredClassVersion = MixinEnvironment.CompatibilityLevel.JAVA_6.classVersion();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinTargetContext(MixinInfo paramMixinInfo, ClassNode paramClassNode, TargetClassContext paramTargetClassContext) {
/*  184 */     this.mixin = paramMixinInfo;
/*  185 */     this.classNode = paramClassNode;
/*  186 */     this.targetClass = paramTargetClassContext;
/*  187 */     this.targetClassInfo = ClassInfo.forName(getTarget().getClassRef());
/*  188 */     this.stratum = paramTargetClassContext.getSourceMap().addFile(this.classNode);
/*  189 */     this.inheritsFromMixin = (paramMixinInfo.getClassInfo().hasMixinInHierarchy() || this.targetClassInfo.hasMixinTargetInHierarchy());
/*  190 */     this.detachedSuper = !this.classNode.superName.equals((getTarget().getClassNode()).superName);
/*  191 */     this.sessionId = paramTargetClassContext.getSessionId();
/*  192 */     requireVersion(paramClassNode.version);
/*      */     
/*  194 */     InnerClassGenerator innerClassGenerator = (InnerClassGenerator)paramTargetClassContext.getExtensions().getGenerator(InnerClassGenerator.class);
/*  195 */     for (String str : this.mixin.getInnerClasses()) {
/*  196 */       this.innerClasses.put(str, innerClassGenerator.registerInnerClass(this.mixin, str, this));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addShadowMethod(MethodNode paramMethodNode) {
/*  206 */     this.shadowMethods.add(paramMethodNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addShadowField(FieldNode paramFieldNode, ClassInfo.Field paramField) {
/*  216 */     this.shadowFields.put(paramFieldNode, paramField);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addAccessorMethod(MethodNode paramMethodNode, Class<? extends Annotation> paramClass) {
/*  226 */     this.accessors.add(AccessorInfo.of(this, paramMethodNode, paramClass));
/*      */   }
/*      */   
/*      */   void addMixinMethod(MethodNode paramMethodNode) {
/*  230 */     Annotations.setVisible(paramMethodNode, MixinMerged.class, new Object[] { "mixin", getClassName() });
/*  231 */     getTarget().addMixinMethod(paramMethodNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void methodMerged(MethodNode paramMethodNode) {
/*  240 */     this.mergedMethods.add(paramMethodNode);
/*  241 */     this.targetClassInfo.addMethod(paramMethodNode);
/*  242 */     getTarget().methodMerged(paramMethodNode);
/*      */     
/*  244 */     Annotations.setVisible(paramMethodNode, MixinMerged.class, new Object[] { "mixin", 
/*  245 */           getClassName(), "priority", 
/*  246 */           Integer.valueOf(getPriority()), "sessionId", this.sessionId });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  255 */     return this.mixin.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment getEnvironment() {
/*  264 */     return this.mixin.getParent().getEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOption(MixinEnvironment.Option paramOption) {
/*  273 */     return getEnvironment().getOption(paramOption);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassNode getClassNode() {
/*  283 */     return this.classNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  293 */     return this.mixin.getClassName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassRef() {
/*  302 */     return this.mixin.getClassRef();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TargetClassContext getTarget() {
/*  311 */     return this.targetClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTargetClassRef() {
/*  322 */     return getTarget().getClassRef();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassNode getTargetClassNode() {
/*  331 */     return getTarget().getClassNode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo getTargetClassInfo() {
/*  340 */     return this.targetClassInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassInfo getClassInfo() {
/*  350 */     return this.mixin.getClassInfo();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassSignature getSignature() {
/*  359 */     return getClassInfo().getSignature();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SourceMap.File getStratum() {
/*  368 */     return this.stratum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMinRequiredClassVersion() {
/*  375 */     return this.minRequiredClassVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDefaultRequiredInjections() {
/*  385 */     return this.mixin.getParent().getDefaultRequiredInjections();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultInjectorGroup() {
/*  394 */     return this.mixin.getParent().getDefaultInjectorGroup();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxShiftByValue() {
/*  403 */     return this.mixin.getParent().getMaxShiftByValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InjectorGroupInfo.Map getInjectorGroups() {
/*  412 */     return this.injectorGroups;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean requireOverwriteAnnotations() {
/*  421 */     return this.mixin.getParent().requireOverwriteAnnotations();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo findRealType(ClassInfo paramClassInfo) {
/*  432 */     if (paramClassInfo == getClassInfo()) {
/*  433 */       return this.targetClassInfo;
/*      */     }
/*      */     
/*  436 */     ClassInfo classInfo = this.targetClassInfo.findCorrespondingType(paramClassInfo);
/*  437 */     if (classInfo == null) {
/*  438 */       throw new InvalidMixinException(this, "Resolution error: unable to find corresponding type for " + paramClassInfo + " in hierarchy of " + this.targetClassInfo);
/*      */     }
/*      */ 
/*      */     
/*  442 */     return classInfo;
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
/*      */   public void transformMethod(MethodNode paramMethodNode) {
/*  454 */     validateMethod(paramMethodNode);
/*  455 */     transformDescriptor(paramMethodNode);
/*  456 */     transformLVT(paramMethodNode);
/*      */ 
/*      */     
/*  459 */     this.stratum.applyOffset(paramMethodNode);
/*      */     
/*  461 */     AbstractInsnNode abstractInsnNode = null;
/*  462 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  463 */       AbstractInsnNode abstractInsnNode1 = listIterator.next();
/*      */       
/*  465 */       if (abstractInsnNode1 instanceof MethodInsnNode) {
/*  466 */         transformMethodRef(paramMethodNode, listIterator, (MemberRef)new MemberRef.Method((MethodInsnNode)abstractInsnNode1));
/*  467 */       } else if (abstractInsnNode1 instanceof FieldInsnNode) {
/*  468 */         transformFieldRef(paramMethodNode, listIterator, (MemberRef)new MemberRef.Field((FieldInsnNode)abstractInsnNode1));
/*  469 */         checkFinal(paramMethodNode, listIterator, (FieldInsnNode)abstractInsnNode1);
/*  470 */       } else if (abstractInsnNode1 instanceof TypeInsnNode) {
/*  471 */         transformTypeNode(paramMethodNode, listIterator, (TypeInsnNode)abstractInsnNode1, abstractInsnNode);
/*  472 */       } else if (abstractInsnNode1 instanceof LdcInsnNode) {
/*  473 */         transformConstantNode(paramMethodNode, listIterator, (LdcInsnNode)abstractInsnNode1);
/*  474 */       } else if (abstractInsnNode1 instanceof InvokeDynamicInsnNode) {
/*  475 */         transformInvokeDynamicNode(paramMethodNode, listIterator, (InvokeDynamicInsnNode)abstractInsnNode1);
/*      */       } 
/*      */       
/*  478 */       abstractInsnNode = abstractInsnNode1;
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
/*      */   private void validateMethod(MethodNode paramMethodNode) {
/*  490 */     if (Annotations.getInvisible(paramMethodNode, SoftOverride.class) != null) {
/*  491 */       ClassInfo.Method method = this.targetClassInfo.findMethodInHierarchy(paramMethodNode.name, paramMethodNode.desc, ClassInfo.SearchType.SUPER_CLASSES_ONLY, ClassInfo.Traversal.SUPER);
/*      */       
/*  493 */       if (method == null || !method.isInjected()) {
/*  494 */         throw new InvalidMixinException(this, "Mixin method " + paramMethodNode.name + paramMethodNode.desc + " is tagged with @SoftOverride but no valid method was found in superclasses of " + 
/*  495 */             getTarget().getClassName());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void transformLVT(MethodNode paramMethodNode) {
/*  506 */     if (paramMethodNode.localVariables == null) {
/*      */       return;
/*      */     }
/*      */     
/*  510 */     for (LocalVariableNode localVariableNode : paramMethodNode.localVariables) {
/*  511 */       if (localVariableNode == null || localVariableNode.desc == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  515 */       localVariableNode.desc = transformSingleDescriptor(Type.getType(localVariableNode.desc));
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
/*      */   private void transformMethodRef(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, MemberRef paramMemberRef) {
/*  528 */     transformDescriptor(paramMemberRef);
/*      */     
/*  530 */     if (paramMemberRef.getOwner().equals(getClassRef())) {
/*  531 */       paramMemberRef.setOwner(getTarget().getClassRef());
/*  532 */       ClassInfo.Method method = getClassInfo().findMethod(paramMemberRef.getName(), paramMemberRef.getDesc(), 10);
/*  533 */       if (method != null && method.isRenamed() && method.getOriginalName().equals(paramMemberRef.getName()) && method.isSynthetic()) {
/*  534 */         paramMemberRef.setName(method.getName());
/*      */       }
/*  536 */       upgradeMethodRef(paramMethodNode, paramMemberRef, method);
/*  537 */     } else if (this.innerClasses.containsKey(paramMemberRef.getOwner())) {
/*  538 */       paramMemberRef.setOwner((String)this.innerClasses.get(paramMemberRef.getOwner()));
/*  539 */       paramMemberRef.setDesc(transformMethodDescriptor(paramMemberRef.getDesc()));
/*  540 */     } else if (this.detachedSuper || this.inheritsFromMixin) {
/*  541 */       if (paramMemberRef.getOpcode() == 183) {
/*  542 */         updateStaticBinding(paramMethodNode, paramMemberRef);
/*  543 */       } else if (paramMemberRef.getOpcode() == 182 && ClassInfo.forName(paramMemberRef.getOwner()).isMixin()) {
/*  544 */         updateDynamicBinding(paramMethodNode, paramMemberRef);
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
/*      */   private void transformFieldRef(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, MemberRef paramMemberRef) {
/*  561 */     if ("super$".equals(paramMemberRef.getName())) {
/*  562 */       if (paramMemberRef instanceof MemberRef.Field) {
/*  563 */         processImaginarySuper(paramMethodNode, ((MemberRef.Field)paramMemberRef).insn);
/*  564 */         paramIterator.remove();
/*      */       } else {
/*  566 */         throw new InvalidMixinException(this.mixin, "Cannot call imaginary super from method handle.");
/*      */       } 
/*      */     }
/*      */     
/*  570 */     transformDescriptor(paramMemberRef);
/*      */     
/*  572 */     if (paramMemberRef.getOwner().equals(getClassRef())) {
/*  573 */       paramMemberRef.setOwner(getTarget().getClassRef());
/*      */       
/*  575 */       ClassInfo.Field field = getClassInfo().findField(paramMemberRef.getName(), paramMemberRef.getDesc(), 10);
/*      */       
/*  577 */       if (field != null && field.isRenamed() && field.getOriginalName().equals(paramMemberRef.getName()) && field.isStatic()) {
/*  578 */         paramMemberRef.setName(field.getName());
/*      */       }
/*      */     } else {
/*  581 */       ClassInfo classInfo = ClassInfo.forName(paramMemberRef.getOwner());
/*  582 */       if (classInfo.isMixin()) {
/*  583 */         ClassInfo classInfo1 = this.targetClassInfo.findCorrespondingType(classInfo);
/*  584 */         paramMemberRef.setOwner((classInfo1 != null) ? classInfo1.getName() : getTarget().getClassRef());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkFinal(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, FieldInsnNode paramFieldInsnNode) {
/*  590 */     if (!paramFieldInsnNode.owner.equals(getTarget().getClassRef())) {
/*      */       return;
/*      */     }
/*      */     
/*  594 */     int i = paramFieldInsnNode.getOpcode();
/*  595 */     if (i == 180 || i == 178) {
/*      */       return;
/*      */     }
/*      */     
/*  599 */     for (Map.Entry<FieldNode, ClassInfo.Field> entry : this.shadowFields.entrySet()) {
/*  600 */       FieldNode fieldNode = (FieldNode)entry.getKey();
/*  601 */       if (!fieldNode.desc.equals(paramFieldInsnNode.desc) || !fieldNode.name.equals(paramFieldInsnNode.name)) {
/*      */         continue;
/*      */       }
/*  604 */       ClassInfo.Field field = (ClassInfo.Field)entry.getValue();
/*  605 */       if (field.isDecoratedFinal()) {
/*  606 */         if (field.isDecoratedMutable()) {
/*  607 */           if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/*  608 */             logger.warn("Write access to @Mutable @Final field {} in {}::{}", new Object[] { field, this.mixin, paramMethodNode.name });
/*      */           }
/*      */         }
/*  611 */         else if ("<init>".equals(paramMethodNode.name) || "<clinit>".equals(paramMethodNode.name)) {
/*  612 */           logger.warn("@Final field {} in {} should be final", new Object[] { field, this.mixin });
/*      */         } else {
/*  614 */           logger.error("Write access detected to @Final field {} in {}::{}", new Object[] { field, this.mixin, paramMethodNode.name });
/*  615 */           if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
/*  616 */             throw new InvalidMixinException(this.mixin, "Write access detected to @Final field " + field + " in " + this.mixin + "::" + paramMethodNode.name);
/*      */           }
/*      */         } 
/*      */       }
/*      */       return;
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
/*      */   private void transformTypeNode(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, TypeInsnNode paramTypeInsnNode, AbstractInsnNode paramAbstractInsnNode) {
/*  637 */     if (paramTypeInsnNode.getOpcode() == 192 && paramTypeInsnNode.desc
/*  638 */       .equals(getTarget().getClassRef()) && paramAbstractInsnNode
/*  639 */       .getOpcode() == 25 && ((VarInsnNode)paramAbstractInsnNode).var == 0) {
/*      */       
/*  641 */       paramIterator.remove();
/*      */       
/*      */       return;
/*      */     } 
/*  645 */     if (paramTypeInsnNode.desc.equals(getClassRef())) {
/*  646 */       paramTypeInsnNode.desc = getTarget().getClassRef();
/*      */     } else {
/*  648 */       String str = (String)this.innerClasses.get(paramTypeInsnNode.desc);
/*  649 */       if (str != null) {
/*  650 */         paramTypeInsnNode.desc = str;
/*      */       }
/*      */     } 
/*      */     
/*  654 */     transformDescriptor(paramTypeInsnNode);
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
/*      */   private void transformConstantNode(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, LdcInsnNode paramLdcInsnNode) {
/*  666 */     paramLdcInsnNode.cst = transformConstant(paramMethodNode, paramIterator, paramLdcInsnNode.cst);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void transformInvokeDynamicNode(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, InvokeDynamicInsnNode paramInvokeDynamicInsnNode) {
/*  677 */     requireVersion(51);
/*  678 */     paramInvokeDynamicInsnNode.desc = transformMethodDescriptor(paramInvokeDynamicInsnNode.desc);
/*  679 */     paramInvokeDynamicInsnNode.bsm = transformHandle(paramMethodNode, paramIterator, paramInvokeDynamicInsnNode.bsm);
/*  680 */     for (byte b = 0; b < paramInvokeDynamicInsnNode.bsmArgs.length; b++) {
/*  681 */       paramInvokeDynamicInsnNode.bsmArgs[b] = transformConstant(paramMethodNode, paramIterator, paramInvokeDynamicInsnNode.bsmArgs[b]);
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
/*      */   private Object transformConstant(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, Object paramObject) {
/*  694 */     if (paramObject instanceof Type) {
/*  695 */       Type type = (Type)paramObject;
/*  696 */       String str = transformDescriptor(type);
/*  697 */       if (!type.toString().equals(str)) {
/*  698 */         return Type.getType(str);
/*      */       }
/*  700 */       return paramObject;
/*  701 */     }  if (paramObject instanceof Handle) {
/*  702 */       return transformHandle(paramMethodNode, paramIterator, (Handle)paramObject);
/*      */     }
/*  704 */     return paramObject;
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
/*      */   private Handle transformHandle(MethodNode paramMethodNode, Iterator<AbstractInsnNode> paramIterator, Handle paramHandle) {
/*  716 */     MemberRef.Handle handle = new MemberRef.Handle(paramHandle);
/*  717 */     if (handle.isField()) {
/*  718 */       transformFieldRef(paramMethodNode, paramIterator, (MemberRef)handle);
/*      */     } else {
/*  720 */       transformMethodRef(paramMethodNode, paramIterator, (MemberRef)handle);
/*      */     } 
/*  722 */     return handle.getMethodHandle();
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
/*      */   private void processImaginarySuper(MethodNode paramMethodNode, FieldInsnNode paramFieldInsnNode) {
/*  738 */     if (paramFieldInsnNode.getOpcode() != 180) {
/*  739 */       if ("<init>".equals(paramMethodNode.name)) {
/*  740 */         throw new InvalidMixinException(this, "Illegal imaginary super declaration: field " + paramFieldInsnNode.name + " must not specify an initialiser");
/*      */       }
/*      */ 
/*      */       
/*  744 */       throw new InvalidMixinException(this, "Illegal imaginary super access: found " + Bytecode.getOpcodeName(paramFieldInsnNode.getOpcode()) + " opcode in " + paramMethodNode.name + paramMethodNode.desc);
/*      */     } 
/*      */ 
/*      */     
/*  748 */     if ((paramMethodNode.access & 0x2) != 0 || (paramMethodNode.access & 0x8) != 0) {
/*  749 */       throw new InvalidMixinException(this, "Illegal imaginary super access: method " + paramMethodNode.name + paramMethodNode.desc + " is private or static");
/*      */     }
/*      */ 
/*      */     
/*  753 */     if (Annotations.getInvisible(paramMethodNode, SoftOverride.class) == null) {
/*  754 */       throw new InvalidMixinException(this, "Illegal imaginary super access: method " + paramMethodNode.name + paramMethodNode.desc + " is not decorated with @SoftOverride");
/*      */     }
/*      */ 
/*      */     
/*  758 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(paramMethodNode.instructions.indexOf((AbstractInsnNode)paramFieldInsnNode)); listIterator.hasNext(); ) {
/*  759 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  760 */       if (abstractInsnNode instanceof MethodInsnNode) {
/*  761 */         MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
/*  762 */         if (methodInsnNode.owner.equals(getClassRef()) && methodInsnNode.name.equals(paramMethodNode.name) && methodInsnNode.desc.equals(paramMethodNode.desc)) {
/*  763 */           methodInsnNode.setOpcode(183);
/*  764 */           updateStaticBinding(paramMethodNode, (MemberRef)new MemberRef.Method(methodInsnNode));
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*  770 */     throw new InvalidMixinException(this, "Illegal imaginary super access: could not find INVOKE for " + paramMethodNode.name + paramMethodNode.desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateStaticBinding(MethodNode paramMethodNode, MemberRef paramMemberRef) {
/*  781 */     updateBinding(paramMethodNode, paramMemberRef, ClassInfo.Traversal.SUPER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateDynamicBinding(MethodNode paramMethodNode, MemberRef paramMemberRef) {
/*  792 */     updateBinding(paramMethodNode, paramMemberRef, ClassInfo.Traversal.ALL);
/*      */   }
/*      */   
/*      */   private void updateBinding(MethodNode paramMethodNode, MemberRef paramMemberRef, ClassInfo.Traversal paramTraversal) {
/*  796 */     if ("<init>".equals(paramMethodNode.name) || paramMemberRef
/*  797 */       .getOwner().equals(getTarget().getClassRef()) || 
/*  798 */       getTarget().getClassRef().startsWith("<")) {
/*      */       return;
/*      */     }
/*      */     
/*  802 */     ClassInfo.Method method = this.targetClassInfo.findMethodInHierarchy(paramMemberRef.getName(), paramMemberRef.getDesc(), paramTraversal
/*  803 */         .getSearchType(), paramTraversal);
/*  804 */     if (method != null) {
/*  805 */       if (method.getOwner().isMixin()) {
/*  806 */         throw new InvalidMixinException(this, "Invalid " + paramMemberRef + " in " + this + " resolved " + method.getOwner() + " but is mixin.");
/*      */       }
/*      */       
/*  809 */       paramMemberRef.setOwner(method.getImplementor().getName());
/*  810 */     } else if (ClassInfo.forName(paramMemberRef.getOwner()).isMixin()) {
/*  811 */       throw new MixinTransformerError("Error resolving " + paramMemberRef + " in " + this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformDescriptor(FieldNode paramFieldNode) {
/*  821 */     if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
/*      */       return;
/*      */     }
/*  824 */     paramFieldNode.desc = transformSingleDescriptor(paramFieldNode.desc, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformDescriptor(MethodNode paramMethodNode) {
/*  833 */     if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
/*      */       return;
/*      */     }
/*  836 */     paramMethodNode.desc = transformMethodDescriptor(paramMethodNode.desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformDescriptor(MemberRef paramMemberRef) {
/*  846 */     if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
/*      */       return;
/*      */     }
/*  849 */     if (paramMemberRef.isField()) {
/*  850 */       paramMemberRef.setDesc(transformSingleDescriptor(paramMemberRef.getDesc(), false));
/*      */     } else {
/*  852 */       paramMemberRef.setDesc(transformMethodDescriptor(paramMemberRef.getDesc()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformDescriptor(TypeInsnNode paramTypeInsnNode) {
/*  862 */     if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
/*      */       return;
/*      */     }
/*  865 */     paramTypeInsnNode.desc = transformSingleDescriptor(paramTypeInsnNode.desc, true);
/*      */   }
/*      */   
/*      */   private String transformDescriptor(Type paramType) {
/*  869 */     if (paramType.getSort() == 11) {
/*  870 */       return transformMethodDescriptor(paramType.getDescriptor());
/*      */     }
/*  872 */     return transformSingleDescriptor(paramType);
/*      */   }
/*      */   
/*      */   private String transformSingleDescriptor(Type paramType) {
/*  876 */     if (paramType.getSort() < 9) {
/*  877 */       return paramType.toString();
/*      */     }
/*      */     
/*  880 */     return transformSingleDescriptor(paramType.toString(), false);
/*      */   }
/*      */   
/*      */   private String transformSingleDescriptor(String paramString, boolean paramBoolean) {
/*  884 */     String str1 = paramString;
/*  885 */     while (str1.startsWith("[") || str1.startsWith("L")) {
/*  886 */       if (str1.startsWith("[")) {
/*  887 */         str1 = str1.substring(1);
/*      */         continue;
/*      */       } 
/*  890 */       str1 = str1.substring(1, str1.indexOf(";"));
/*  891 */       paramBoolean = true;
/*      */     } 
/*      */     
/*  894 */     if (!paramBoolean) {
/*  895 */       return paramString;
/*      */     }
/*      */     
/*  898 */     String str2 = (String)this.innerClasses.get(str1);
/*  899 */     if (str2 != null) {
/*  900 */       return paramString.replace(str1, str2);
/*      */     }
/*      */     
/*  903 */     if (this.innerClasses.inverse().containsKey(str1)) {
/*  904 */       return paramString;
/*      */     }
/*      */     
/*  907 */     ClassInfo classInfo = ClassInfo.forName(str1);
/*      */     
/*  909 */     if (!classInfo.isMixin()) {
/*  910 */       return paramString;
/*      */     }
/*      */     
/*  913 */     return paramString.replace(str1, findRealType(classInfo).toString());
/*      */   }
/*      */   
/*      */   private String transformMethodDescriptor(String paramString) {
/*  917 */     StringBuilder stringBuilder = new StringBuilder();
/*  918 */     stringBuilder.append('(');
/*  919 */     for (Type type : Type.getArgumentTypes(paramString)) {
/*  920 */       stringBuilder.append(transformSingleDescriptor(type));
/*      */     }
/*  922 */     return stringBuilder.append(')').append(transformSingleDescriptor(Type.getReturnType(paramString))).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Target getTargetMethod(MethodNode paramMethodNode) {
/*  933 */     return getTarget().getTargetMethod(paramMethodNode);
/*      */   }
/*      */   
/*      */   MethodNode findMethod(MethodNode paramMethodNode, AnnotationNode paramAnnotationNode) {
/*  937 */     LinkedList<String> linkedList = new LinkedList();
/*  938 */     linkedList.add(paramMethodNode.name);
/*  939 */     if (paramAnnotationNode != null) {
/*  940 */       List<? extends String> list = (List)Annotations.getValue(paramAnnotationNode, "aliases");
/*  941 */       if (list != null) {
/*  942 */         linkedList.addAll(list);
/*      */       }
/*      */     } 
/*      */     
/*  946 */     return getTarget().findMethod(linkedList, paramMethodNode.desc);
/*      */   }
/*      */   
/*      */   MethodNode findRemappedMethod(MethodNode paramMethodNode) {
/*  950 */     RemapperChain remapperChain = getEnvironment().getRemappers();
/*  951 */     String str = remapperChain.mapMethodName(getTarget().getClassRef(), paramMethodNode.name, paramMethodNode.desc);
/*  952 */     if (str.equals(paramMethodNode.name)) {
/*  953 */       return null;
/*      */     }
/*      */     
/*  956 */     LinkedList<String> linkedList = new LinkedList();
/*  957 */     linkedList.add(str);
/*      */     
/*  959 */     return getTarget().findAliasedMethod(linkedList, paramMethodNode.desc);
/*      */   }
/*      */   
/*      */   FieldNode findField(FieldNode paramFieldNode, AnnotationNode paramAnnotationNode) {
/*  963 */     LinkedList<String> linkedList = new LinkedList();
/*  964 */     linkedList.add(paramFieldNode.name);
/*  965 */     if (paramAnnotationNode != null) {
/*  966 */       List<? extends String> list = (List)Annotations.getValue(paramAnnotationNode, "aliases");
/*  967 */       if (list != null) {
/*  968 */         linkedList.addAll(list);
/*      */       }
/*      */     } 
/*      */     
/*  972 */     return getTarget().findAliasedField(linkedList, paramFieldNode.desc);
/*      */   }
/*      */   
/*      */   FieldNode findRemappedField(FieldNode paramFieldNode) {
/*  976 */     RemapperChain remapperChain = getEnvironment().getRemappers();
/*  977 */     String str = remapperChain.mapFieldName(getTarget().getClassRef(), paramFieldNode.name, paramFieldNode.desc);
/*  978 */     if (str.equals(paramFieldNode.name)) {
/*  979 */       return null;
/*      */     }
/*      */     
/*  982 */     LinkedList<String> linkedList = new LinkedList();
/*  983 */     linkedList.add(str);
/*  984 */     return getTarget().findAliasedField(linkedList, paramFieldNode.desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void requireVersion(int paramInt) {
/*  994 */     this.minRequiredClassVersion = Math.max(this.minRequiredClassVersion, paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  999 */     if (paramInt > MixinEnvironment.getCompatibilityLevel().classVersion()) {
/* 1000 */       throw new InvalidMixinException(this, "Unsupported mixin class version " + paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Extensions getExtensions() {
/* 1009 */     return this.targetClass.getExtensions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IMixinInfo getMixin() {
/* 1017 */     return this.mixin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinInfo getInfo() {
/* 1024 */     return this.mixin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPriority() {
/* 1034 */     return this.mixin.getPriority();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getInterfaces() {
/* 1043 */     return this.mixin.getInterfaces();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<MethodNode> getShadowMethods() {
/* 1052 */     return this.shadowMethods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<MethodNode> getMethods() {
/* 1061 */     return this.classNode.methods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<FieldNode, ClassInfo.Field>> getShadowFields() {
/* 1070 */     return this.shadowFields.entrySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<FieldNode> getFields() {
/* 1079 */     return this.classNode.fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Level getLoggingLevel() {
/* 1088 */     return this.mixin.getLoggingLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldSetSourceFile() {
/* 1098 */     return this.mixin.getParent().shouldSetSourceFile();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSourceFile() {
/* 1107 */     return this.classNode.sourceFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IReferenceMapper getReferenceMapper() {
/* 1116 */     return this.mixin.getParent().getReferenceMapper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void preApply(String paramString, ClassNode paramClassNode) {
/* 1126 */     this.mixin.preApply(paramString, paramClassNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void postApply(String paramString, ClassNode paramClassNode) {
/*      */     try {
/* 1137 */       this.injectorGroups.validateAll();
/* 1138 */     } catch (InjectionValidationException injectionValidationException) {
/* 1139 */       InjectorGroupInfo injectorGroupInfo = injectionValidationException.getGroup();
/* 1140 */       throw new InjectionError(
/* 1141 */           String.format("Critical injection failure: Callback group %s in %s failed injection check: %s", new Object[] {
/* 1142 */               injectorGroupInfo, this.mixin, injectionValidationException.getMessage()
/*      */             }));
/*      */     } 
/* 1145 */     this.mixin.postApply(paramString, paramClassNode);
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
/*      */   public String getUniqueName(MethodNode paramMethodNode, boolean paramBoolean) {
/* 1158 */     return getTarget().getUniqueName(paramMethodNode, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUniqueName(FieldNode paramFieldNode) {
/* 1169 */     return getTarget().getUniqueName(paramFieldNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prepareInjections() {
/* 1177 */     this.injectors.clear();
/*      */     
/* 1179 */     for (MethodNode methodNode : this.mergedMethods) {
/* 1180 */       InjectionInfo injectionInfo = InjectionInfo.parse(this, methodNode);
/* 1181 */       if (injectionInfo == null) {
/*      */         continue;
/*      */       }
/*      */       
/* 1185 */       if (injectionInfo.isValid()) {
/* 1186 */         injectionInfo.prepare();
/* 1187 */         this.injectors.add(injectionInfo);
/*      */       } 
/*      */       
/* 1190 */       methodNode.visibleAnnotations.remove(injectionInfo.getAnnotation());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyInjections() {
/* 1198 */     for (InjectionInfo injectionInfo : this.injectors) {
/* 1199 */       injectionInfo.inject();
/*      */     }
/*      */     
/* 1202 */     for (InjectionInfo injectionInfo : this.injectors) {
/* 1203 */       injectionInfo.postInject();
/*      */     }
/*      */     
/* 1206 */     this.injectors.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<MethodNode> generateAccessors() {
/* 1214 */     for (AccessorInfo accessorInfo : this.accessors) {
/* 1215 */       accessorInfo.locate();
/*      */     }
/*      */     
/* 1218 */     ArrayList<MethodNode> arrayList = new ArrayList();
/*      */     
/* 1220 */     for (AccessorInfo accessorInfo : this.accessors) {
/* 1221 */       MethodNode methodNode = accessorInfo.generate();
/* 1222 */       getTarget().addMixinMethod(methodNode);
/* 1223 */       arrayList.add(methodNode);
/*      */     } 
/*      */     
/* 1226 */     return arrayList;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinTargetContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */