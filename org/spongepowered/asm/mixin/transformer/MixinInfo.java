/*      */ package org.spongepowered.asm.mixin.transformer;
/*      */ 
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Functions;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.spongepowered.asm.lib.ClassReader;
/*      */ import org.spongepowered.asm.lib.ClassVisitor;
/*      */ import org.spongepowered.asm.lib.MethodVisitor;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldNode;
/*      */ import org.spongepowered.asm.lib.tree.InnerClassNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.mixin.Implements;
/*      */ import org.spongepowered.asm.mixin.Mixin;
/*      */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*      */ import org.spongepowered.asm.mixin.Overwrite;
/*      */ import org.spongepowered.asm.mixin.Pseudo;
/*      */ import org.spongepowered.asm.mixin.Shadow;
/*      */ import org.spongepowered.asm.mixin.Unique;
/*      */ import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
/*      */ import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
/*      */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*      */ import org.spongepowered.asm.mixin.injection.Surrogate;
/*      */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
/*      */ import org.spongepowered.asm.mixin.transformer.throwables.MixinTargetAlreadyLoadedException;
/*      */ import org.spongepowered.asm.service.IMixinService;
/*      */ import org.spongepowered.asm.service.MixinService;
/*      */ import org.spongepowered.asm.util.Annotations;
/*      */ import org.spongepowered.asm.util.Bytecode;
/*      */ import org.spongepowered.asm.util.perf.Profiler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MixinInfo
/*      */   implements Comparable<MixinInfo>, IMixinInfo
/*      */ {
/*      */   class MixinMethodNode
/*      */     extends MethodNode
/*      */   {
/*      */     private final String originalName;
/*      */     
/*      */     public MixinMethodNode(int param1Int, String param1String1, String param1String2, String param1String3, String[] param1ArrayOfString) {
/*   90 */       super(327680, param1Int, param1String1, param1String2, param1String3, param1ArrayOfString);
/*   91 */       this.originalName = param1String1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*   96 */       return String.format("%s%s", new Object[] { this.originalName, this.desc });
/*      */     }
/*      */     
/*      */     public String getOriginalName() {
/*  100 */       return this.originalName;
/*      */     }
/*      */     
/*      */     public boolean isInjector() {
/*  104 */       return (getInjectorAnnotation() != null || isSurrogate());
/*      */     }
/*      */     
/*      */     public boolean isSurrogate() {
/*  108 */       return (getVisibleAnnotation((Class)Surrogate.class) != null);
/*      */     }
/*      */     
/*      */     public boolean isSynthetic() {
/*  112 */       return Bytecode.hasFlag(this, 4096);
/*      */     }
/*      */     
/*      */     public AnnotationNode getVisibleAnnotation(Class<? extends Annotation> param1Class) {
/*  116 */       return Annotations.getVisible(this, param1Class);
/*      */     }
/*      */     
/*      */     public AnnotationNode getInjectorAnnotation() {
/*  120 */       return InjectionInfo.getInjectorAnnotation(MixinInfo.this, this);
/*      */     }
/*      */     
/*      */     public IMixinInfo getOwner() {
/*  124 */       return MixinInfo.this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class MixinClassNode
/*      */     extends ClassNode
/*      */   {
/*      */     public final List<MixinInfo.MixinMethodNode> mixinMethods;
/*      */ 
/*      */     
/*      */     public MixinClassNode(MixinInfo param1MixinInfo1) {
/*  137 */       this(327680);
/*      */     }
/*      */ 
/*      */     
/*      */     public MixinClassNode(int param1Int) {
/*  142 */       super(param1Int);
/*  143 */       this.mixinMethods = this.methods;
/*      */     }
/*      */     
/*      */     public MixinInfo getMixin() {
/*  147 */       return MixinInfo.this;
/*      */     }
/*      */ 
/*      */     
/*      */     public MethodVisitor visitMethod(int param1Int, String param1String1, String param1String2, String param1String3, String[] param1ArrayOfString) {
/*  152 */       MixinInfo.MixinMethodNode mixinMethodNode = new MixinInfo.MixinMethodNode(param1Int, param1String1, param1String2, param1String3, param1ArrayOfString);
/*  153 */       this.methods.add(mixinMethodNode);
/*  154 */       return (MethodVisitor)mixinMethodNode;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class State
/*      */   {
/*      */     private byte[] mixinBytes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClassInfo classInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean detachedSuper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean unique;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  189 */     protected final Set<String> interfaces = new HashSet<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  194 */     protected final List<InterfaceInfo> softImplements = new ArrayList<InterfaceInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  199 */     protected final Set<String> syntheticInnerClasses = new HashSet<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  204 */     protected final Set<String> innerClasses = new HashSet<String>();
/*      */ 
/*      */ 
/*      */     
/*      */     protected MixinInfo.MixinClassNode classNode;
/*      */ 
/*      */ 
/*      */     
/*      */     State(byte[] param1ArrayOfbyte) {
/*  213 */       this(param1ArrayOfbyte, null);
/*      */     }
/*      */     
/*      */     State(byte[] param1ArrayOfbyte, ClassInfo param1ClassInfo) {
/*  217 */       this.mixinBytes = param1ArrayOfbyte;
/*  218 */       connect();
/*  219 */       this.classInfo = (param1ClassInfo != null) ? param1ClassInfo : ClassInfo.fromClassNode(getClassNode());
/*      */     }
/*      */     
/*      */     private void connect() {
/*  223 */       this.classNode = createClassNode(0);
/*      */     }
/*      */     
/*      */     private void complete() {
/*  227 */       this.classNode = null;
/*      */     }
/*      */     
/*      */     ClassInfo getClassInfo() {
/*  231 */       return this.classInfo;
/*      */     }
/*      */     
/*      */     byte[] getClassBytes() {
/*  235 */       return this.mixinBytes;
/*      */     }
/*      */     
/*      */     MixinInfo.MixinClassNode getClassNode() {
/*  239 */       return this.classNode;
/*      */     }
/*      */     
/*      */     boolean isDetachedSuper() {
/*  243 */       return this.detachedSuper;
/*      */     }
/*      */     
/*      */     boolean isUnique() {
/*  247 */       return this.unique;
/*      */     }
/*      */     
/*      */     List<? extends InterfaceInfo> getSoftImplements() {
/*  251 */       return this.softImplements;
/*      */     }
/*      */     
/*      */     Set<String> getSyntheticInnerClasses() {
/*  255 */       return this.syntheticInnerClasses;
/*      */     }
/*      */     
/*      */     Set<String> getInnerClasses() {
/*  259 */       return this.innerClasses;
/*      */     }
/*      */     
/*      */     Set<String> getInterfaces() {
/*  263 */       return this.interfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MixinInfo.MixinClassNode createClassNode(int param1Int) {
/*  273 */       MixinInfo.MixinClassNode mixinClassNode = new MixinInfo.MixinClassNode(MixinInfo.this);
/*  274 */       ClassReader classReader = new ClassReader(this.mixinBytes);
/*  275 */       classReader.accept((ClassVisitor)mixinClassNode, param1Int);
/*  276 */       return mixinClassNode;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void validate(MixinInfo.SubType param1SubType, List<ClassInfo> param1List) {
/*  286 */       MixinPreProcessorStandard mixinPreProcessorStandard = param1SubType.createPreProcessor(getClassNode()).prepare();
/*  287 */       for (ClassInfo classInfo : param1List) {
/*  288 */         mixinPreProcessorStandard.conform(classInfo);
/*      */       }
/*      */       
/*  291 */       param1SubType.validate(this, param1List);
/*      */       
/*  293 */       this.detachedSuper = param1SubType.isDetachedSuper();
/*  294 */       this.unique = (Annotations.getVisible(getClassNode(), Unique.class) != null);
/*      */ 
/*      */       
/*  297 */       validateInner();
/*  298 */       validateClassVersion();
/*  299 */       validateRemappables(param1List);
/*      */ 
/*      */       
/*  302 */       readImplementations(param1SubType);
/*  303 */       readInnerClasses();
/*      */ 
/*      */       
/*  306 */       validateChanges(param1SubType, param1List);
/*      */ 
/*      */       
/*  309 */       complete();
/*      */     }
/*      */ 
/*      */     
/*      */     private void validateInner() {
/*  314 */       if (!this.classInfo.isProbablyStatic()) {
/*  315 */         throw new InvalidMixinException(MixinInfo.this, "Inner class mixin must be declared static");
/*      */       }
/*      */     }
/*      */     
/*      */     private void validateClassVersion() {
/*  320 */       if (this.classNode.version > MixinEnvironment.getCompatibilityLevel().classVersion()) {
/*  321 */         String str = ".";
/*  322 */         for (MixinEnvironment.CompatibilityLevel compatibilityLevel : MixinEnvironment.CompatibilityLevel.values()) {
/*  323 */           if (compatibilityLevel.classVersion() >= this.classNode.version) {
/*  324 */             str = String.format(". Mixin requires compatibility level %s or above.", new Object[] { compatibilityLevel.name() });
/*      */           }
/*      */         } 
/*      */         
/*  328 */         throw new InvalidMixinException(MixinInfo.this, "Unsupported mixin class version " + this.classNode.version + str);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void validateRemappables(List<ClassInfo> param1List) {
/*  334 */       if (param1List.size() > 1) {
/*  335 */         for (FieldNode fieldNode : this.classNode.fields) {
/*  336 */           validateRemappable(Shadow.class, fieldNode.name, Annotations.getVisible(fieldNode, Shadow.class));
/*      */         }
/*      */         
/*  339 */         for (MethodNode methodNode : this.classNode.methods) {
/*  340 */           validateRemappable(Shadow.class, methodNode.name, Annotations.getVisible(methodNode, Shadow.class));
/*  341 */           AnnotationNode annotationNode = Annotations.getVisible(methodNode, Overwrite.class);
/*  342 */           if (annotationNode != null && ((methodNode.access & 0x8) == 0 || (methodNode.access & 0x1) == 0)) {
/*  343 */             throw new InvalidMixinException(MixinInfo.this, "Found @Overwrite annotation on " + methodNode.name + " in " + MixinInfo.this);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void validateRemappable(Class<Shadow> param1Class, String param1String, AnnotationNode param1AnnotationNode) {
/*  350 */       if (param1AnnotationNode != null && ((Boolean)Annotations.getValue(param1AnnotationNode, "remap", Boolean.TRUE)).booleanValue()) {
/*  351 */         throw new InvalidMixinException(MixinInfo.this, "Found a remappable @" + param1Class.getSimpleName() + " annotation on " + param1String + " in " + this);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void readImplementations(MixinInfo.SubType param1SubType) {
/*  360 */       this.interfaces.addAll(this.classNode.interfaces);
/*  361 */       this.interfaces.addAll(param1SubType.getInterfaces());
/*      */       
/*  363 */       AnnotationNode annotationNode = Annotations.getInvisible(this.classNode, Implements.class);
/*  364 */       if (annotationNode == null) {
/*      */         return;
/*      */       }
/*      */       
/*  368 */       List list = (List)Annotations.getValue(annotationNode);
/*  369 */       if (list == null) {
/*      */         return;
/*      */       }
/*      */       
/*  373 */       for (AnnotationNode annotationNode1 : list) {
/*  374 */         InterfaceInfo interfaceInfo = InterfaceInfo.fromAnnotation(MixinInfo.this, annotationNode1);
/*  375 */         this.softImplements.add(interfaceInfo);
/*  376 */         this.interfaces.add(interfaceInfo.getInternalName());
/*      */         
/*  378 */         if (!(this instanceof MixinInfo.Reloaded)) {
/*  379 */           this.classInfo.addInterface(interfaceInfo.getInternalName());
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void readInnerClasses() {
/*  390 */       for (InnerClassNode innerClassNode : this.classNode.innerClasses) {
/*  391 */         ClassInfo classInfo = ClassInfo.forName(innerClassNode.name);
/*  392 */         if ((innerClassNode.outerName != null && innerClassNode.outerName.equals(this.classInfo.getName())) || innerClassNode.name
/*  393 */           .startsWith(this.classNode.name + "$")) {
/*  394 */           if (classInfo.isProbablyStatic() && classInfo.isSynthetic()) {
/*  395 */             this.syntheticInnerClasses.add(innerClassNode.name); continue;
/*      */           } 
/*  397 */           this.innerClasses.add(innerClassNode.name);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void validateChanges(MixinInfo.SubType param1SubType, List<ClassInfo> param1List) {
/*  404 */       param1SubType.createPreProcessor(this.classNode).prepare();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class Reloaded
/*      */     extends State
/*      */   {
/*      */     private final MixinInfo.State previous;
/*      */ 
/*      */ 
/*      */     
/*      */     Reloaded(MixinInfo.State param1State, byte[] param1ArrayOfbyte) {
/*  419 */       super(param1ArrayOfbyte, param1State.getClassInfo());
/*  420 */       this.previous = param1State;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void validateChanges(MixinInfo.SubType param1SubType, List<ClassInfo> param1List) {
/*  429 */       if (!this.syntheticInnerClasses.equals(this.previous.syntheticInnerClasses)) {
/*  430 */         throw new MixinReloadException(MixinInfo.this, "Cannot change inner classes");
/*      */       }
/*  432 */       if (!this.interfaces.equals(this.previous.interfaces)) {
/*  433 */         throw new MixinReloadException(MixinInfo.this, "Cannot change interfaces");
/*      */       }
/*  435 */       if (!(new HashSet(this.softImplements)).equals(new HashSet<InterfaceInfo>(this.previous.softImplements))) {
/*  436 */         throw new MixinReloadException(MixinInfo.this, "Cannot change soft interfaces");
/*      */       }
/*  438 */       List<ClassInfo> list = MixinInfo.this.readTargetClasses(this.classNode, true);
/*  439 */       if (!(new HashSet(list)).equals(new HashSet<ClassInfo>(param1List))) {
/*  440 */         throw new MixinReloadException(MixinInfo.this, "Cannot change target classes");
/*      */       }
/*  442 */       int i = MixinInfo.this.readPriority(this.classNode);
/*  443 */       if (i != MixinInfo.this.getPriority()) {
/*  444 */         throw new MixinReloadException(MixinInfo.this, "Cannot change mixin priority");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class SubType
/*      */   {
/*      */     protected final MixinInfo mixin;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final String annotationType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean targetMustBeInterface;
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean detached;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SubType(MixinInfo param1MixinInfo, String param1String, boolean param1Boolean) {
/*  475 */       this.mixin = param1MixinInfo;
/*  476 */       this.annotationType = param1String;
/*  477 */       this.targetMustBeInterface = param1Boolean;
/*      */     }
/*      */     
/*      */     Collection<String> getInterfaces() {
/*  481 */       return Collections.emptyList();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isDetachedSuper() {
/*  491 */       return this.detached;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isLoadable() {
/*  501 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void validateTarget(String param1String, ClassInfo param1ClassInfo) {
/*  511 */       boolean bool = param1ClassInfo.isInterface();
/*  512 */       if (bool != this.targetMustBeInterface) {
/*  513 */         String str = bool ? "" : "not ";
/*  514 */         throw new InvalidMixinException(this.mixin, this.annotationType + " target type mismatch: " + param1String + " is " + str + "an interface in " + this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     abstract void validate(MixinInfo.State param1State, List<ClassInfo> param1List);
/*      */ 
/*      */     
/*      */     abstract MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode param1MixinClassNode);
/*      */ 
/*      */     
/*      */     static class Standard
/*      */       extends SubType
/*      */     {
/*      */       Standard(MixinInfo param2MixinInfo) {
/*  529 */         super(param2MixinInfo, "@Mixin", false);
/*      */       }
/*      */ 
/*      */       
/*      */       void validate(MixinInfo.State param2State, List<ClassInfo> param2List) {
/*  534 */         MixinInfo.MixinClassNode mixinClassNode = param2State.getClassNode();
/*      */         
/*  536 */         for (ClassInfo classInfo : param2List) {
/*  537 */           if (mixinClassNode.superName.equals(classInfo.getSuperName())) {
/*      */             continue;
/*      */           }
/*      */           
/*  541 */           if (!classInfo.hasSuperClass(mixinClassNode.superName, ClassInfo.Traversal.SUPER)) {
/*  542 */             ClassInfo classInfo1 = ClassInfo.forName(mixinClassNode.superName);
/*  543 */             if (classInfo1.isMixin())
/*      */             {
/*  545 */               for (ClassInfo classInfo2 : classInfo1.getTargets()) {
/*  546 */                 if (param2List.contains(classInfo2)) {
/*  547 */                   throw new InvalidMixinException(this.mixin, "Illegal hierarchy detected. Derived mixin " + this + " targets the same class " + classInfo2
/*  548 */                       .getClassName() + " as its superclass " + classInfo1
/*  549 */                       .getClassName());
/*      */                 }
/*      */               } 
/*      */             }
/*      */             
/*  554 */             throw new InvalidMixinException(this.mixin, "Super class '" + mixinClassNode.superName.replace('/', '.') + "' of " + this.mixin
/*  555 */                 .getName() + " was not found in the hierarchy of target class '" + classInfo + "'");
/*      */           } 
/*      */           
/*  558 */           this.detached = true;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode param2MixinClassNode) {
/*  564 */         return new MixinPreProcessorStandard(this.mixin, param2MixinClassNode);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     static class Interface
/*      */       extends SubType
/*      */     {
/*      */       Interface(MixinInfo param2MixinInfo) {
/*  574 */         super(param2MixinInfo, "@Mixin", true);
/*      */       }
/*      */ 
/*      */       
/*      */       void validate(MixinInfo.State param2State, List<ClassInfo> param2List) {
/*  579 */         if (!MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
/*  580 */           throw new InvalidMixinException(this.mixin, "Interface mixin not supported in current enviromnment");
/*      */         }
/*      */         
/*  583 */         MixinInfo.MixinClassNode mixinClassNode = param2State.getClassNode();
/*      */         
/*  585 */         if (!"java/lang/Object".equals(mixinClassNode.superName)) {
/*  586 */           throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + mixinClassNode.superName
/*  587 */               .replace('/', '.'));
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode param2MixinClassNode) {
/*  593 */         return new MixinPreProcessorInterface(this.mixin, param2MixinClassNode);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static class Accessor
/*      */       extends SubType
/*      */     {
/*  603 */       private final Collection<String> interfaces = new ArrayList<String>();
/*      */       
/*      */       Accessor(MixinInfo param2MixinInfo) {
/*  606 */         super(param2MixinInfo, "@Mixin", false);
/*  607 */         this.interfaces.add(param2MixinInfo.getClassRef());
/*      */       }
/*      */ 
/*      */       
/*      */       boolean isLoadable() {
/*  612 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       Collection<String> getInterfaces() {
/*  617 */         return this.interfaces;
/*      */       }
/*      */ 
/*      */       
/*      */       void validateTarget(String param2String, ClassInfo param2ClassInfo) {
/*  622 */         boolean bool = param2ClassInfo.isInterface();
/*  623 */         if (bool && !MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
/*  624 */           throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       void validate(MixinInfo.State param2State, List<ClassInfo> param2List) {
/*  630 */         MixinInfo.MixinClassNode mixinClassNode = param2State.getClassNode();
/*      */         
/*  632 */         if (!"java/lang/Object".equals(mixinClassNode.superName)) {
/*  633 */           throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + mixinClassNode.superName
/*  634 */               .replace('/', '.'));
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode param2MixinClassNode) {
/*  640 */         return new MixinPreProcessorAccessor(this.mixin, param2MixinClassNode);
/*      */       }
/*      */     }
/*      */     
/*      */     static SubType getTypeFor(MixinInfo param1MixinInfo) {
/*  645 */       if (!param1MixinInfo.getClassInfo().isInterface()) {
/*  646 */         return new Standard(param1MixinInfo);
/*      */       }
/*      */       
/*  649 */       int i = 0;
/*  650 */       for (ClassInfo.Method method : param1MixinInfo.getClassInfo().getMethods()) {
/*  651 */         i |= !method.isAccessor() ? 1 : 0;
/*      */       }
/*      */       
/*  654 */       if (i != 0)
/*      */       {
/*  656 */         return new Interface(param1MixinInfo);
/*      */       }
/*      */ 
/*      */       
/*  660 */       return new Accessor(param1MixinInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  668 */   private static final IMixinService classLoaderUtil = MixinService.getService();
/*      */   static class Accessor extends SubType {
/*      */     private final Collection<String> interfaces = new ArrayList<String>();
/*      */     Accessor(MixinInfo param1MixinInfo) { super(param1MixinInfo, "@Mixin", false); this.interfaces.add(param1MixinInfo.getClassRef()); }
/*      */     boolean isLoadable() { return true; }
/*      */     Collection<String> getInterfaces() { return this.interfaces; }
/*  674 */     void validateTarget(String param1String, ClassInfo param1ClassInfo) { boolean bool = param1ClassInfo.isInterface(); if (bool && !MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");  } void validate(MixinInfo.State param1State, List<ClassInfo> param1List) { MixinInfo.MixinClassNode mixinClassNode = param1State.getClassNode(); if (!"java/lang/Object".equals(mixinClassNode.superName)) throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + mixinClassNode.superName.replace('/', '.'));  } MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode param1MixinClassNode) { return new MixinPreProcessorAccessor(this.mixin, param1MixinClassNode); } } static int mixinOrder = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  679 */   private final transient Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  684 */   private final transient Profiler profiler = MixinEnvironment.getProfiler();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient MixinConfig parent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String name;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String className;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int priority;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean virtual;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final List<ClassInfo> targetClasses;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final List<String> targetClassNames;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  724 */   private final transient int order = mixinOrder++;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient IMixinService service;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient IMixinConfigPlugin plugin;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient MixinEnvironment.Phase phase;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient ClassInfo info;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient SubType type;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final transient boolean strict;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient State pendingState;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient State state;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinInfo(IMixinService paramIMixinService, MixinConfig paramMixinConfig, String paramString, boolean paramBoolean1, IMixinConfigPlugin paramIMixinConfigPlugin, boolean paramBoolean2) {
/*  778 */     this.service = paramIMixinService;
/*  779 */     this.parent = paramMixinConfig;
/*  780 */     this.name = paramString;
/*  781 */     this.className = paramMixinConfig.getMixinPackage() + paramString;
/*  782 */     this.plugin = paramIMixinConfigPlugin;
/*  783 */     this.phase = paramMixinConfig.getEnvironment().getPhase();
/*  784 */     this.strict = paramMixinConfig.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_TARGETS);
/*      */ 
/*      */     
/*      */     try {
/*  788 */       byte[] arrayOfByte = loadMixinClass(this.className, paramBoolean1);
/*  789 */       this.pendingState = new State(arrayOfByte);
/*  790 */       this.info = this.pendingState.getClassInfo();
/*  791 */       this.type = SubType.getTypeFor(this);
/*  792 */     } catch (InvalidMixinException invalidMixinException) {
/*  793 */       throw invalidMixinException;
/*  794 */     } catch (Exception exception) {
/*  795 */       throw new InvalidMixinException(this, exception);
/*      */     } 
/*      */     
/*  798 */     if (!this.type.isLoadable())
/*      */     {
/*      */ 
/*      */       
/*  802 */       classLoaderUtil.registerInvalidClass(this.className);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  807 */       this.priority = readPriority(this.pendingState.getClassNode());
/*  808 */       this.virtual = readPseudo(this.pendingState.getClassNode());
/*  809 */       this.targetClasses = readTargetClasses(this.pendingState.getClassNode(), paramBoolean2);
/*  810 */       this.targetClassNames = Collections.unmodifiableList(Lists.transform(this.targetClasses, Functions.toStringFunction()));
/*  811 */     } catch (InvalidMixinException invalidMixinException) {
/*  812 */       throw invalidMixinException;
/*  813 */     } catch (Exception exception) {
/*  814 */       throw new InvalidMixinException(this, exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void validate() {
/*  822 */     if (this.pendingState == null) {
/*  823 */       throw new IllegalStateException("No pending validation state for " + this);
/*      */     }
/*      */     
/*      */     try {
/*  827 */       this.pendingState.validate(this.type, this.targetClasses);
/*  828 */       this.state = this.pendingState;
/*      */     } finally {
/*  830 */       this.pendingState = null;
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
/*      */   protected List<ClassInfo> readTargetClasses(MixinClassNode paramMixinClassNode, boolean paramBoolean) {
/*  842 */     if (paramMixinClassNode == null) {
/*  843 */       return Collections.emptyList();
/*      */     }
/*      */     
/*  846 */     AnnotationNode annotationNode = Annotations.getInvisible(paramMixinClassNode, Mixin.class);
/*  847 */     if (annotationNode == null) {
/*  848 */       throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", new Object[] { this.className }));
/*      */     }
/*      */     
/*  851 */     ArrayList<ClassInfo> arrayList = new ArrayList();
/*  852 */     List list1 = (List)Annotations.getValue(annotationNode, "value");
/*  853 */     List list2 = (List)Annotations.getValue(annotationNode, "targets");
/*      */     
/*  855 */     if (list1 != null) {
/*  856 */       readTargets(arrayList, Lists.transform(list1, new Function<Type, String>()
/*      */             {
/*      */               public String apply(Type param1Type) {
/*  859 */                 return param1Type.getClassName();
/*      */               }
/*      */             }), paramBoolean, false);
/*      */     }
/*      */     
/*  864 */     if (list2 != null) {
/*  865 */       readTargets(arrayList, Lists.transform(list2, new Function<String, String>()
/*      */             {
/*      */               public String apply(String param1String) {
/*  868 */                 return MixinInfo.this.getParent().remapClassName(MixinInfo.this.getClassRef(), param1String);
/*      */               }
/*      */             }), paramBoolean, true);
/*      */     }
/*      */     
/*  873 */     return arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readTargets(Collection<ClassInfo> paramCollection, Collection<String> paramCollection1, boolean paramBoolean1, boolean paramBoolean2) {
/*  880 */     for (String str1 : paramCollection1) {
/*  881 */       String str2 = str1.replace('/', '.');
/*  882 */       if (classLoaderUtil.isClassLoaded(str2) && !isReloading()) {
/*  883 */         String str = String.format("Critical problem: %s target %s was already transformed.", new Object[] { this, str2 });
/*  884 */         if (this.parent.isRequired()) {
/*  885 */           throw new MixinTargetAlreadyLoadedException(this, str, str2);
/*      */         }
/*  887 */         this.logger.error(str);
/*      */       } 
/*      */       
/*  890 */       if (shouldApplyMixin(paramBoolean1, str2)) {
/*  891 */         ClassInfo classInfo = getTarget(str2, paramBoolean2);
/*  892 */         if (classInfo != null && !paramCollection.contains(classInfo)) {
/*  893 */           paramCollection.add(classInfo);
/*  894 */           classInfo.addMixin(this);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldApplyMixin(boolean paramBoolean, String paramString) {
/*  901 */     Profiler.Section section = this.profiler.begin("plugin");
/*  902 */     boolean bool = (this.plugin == null || paramBoolean || this.plugin.shouldApplyMixin(paramString, this.className)) ? true : false;
/*  903 */     section.end();
/*  904 */     return bool;
/*      */   }
/*      */   
/*      */   private ClassInfo getTarget(String paramString, boolean paramBoolean) throws InvalidMixinException {
/*  908 */     ClassInfo classInfo = ClassInfo.forName(paramString);
/*  909 */     if (classInfo == null) {
/*  910 */       if (isVirtual()) {
/*  911 */         this.logger.debug("Skipping virtual target {} for {}", new Object[] { paramString, this });
/*      */       } else {
/*  913 */         handleTargetError(String.format("@Mixin target %s was not found %s", new Object[] { paramString, this }));
/*      */       } 
/*  915 */       return null;
/*      */     } 
/*  917 */     this.type.validateTarget(paramString, classInfo);
/*  918 */     if (paramBoolean && classInfo.isPublic() && !isVirtual()) {
/*  919 */       handleTargetError(String.format("@Mixin target %s is public in %s and should be specified in value", new Object[] { paramString, this }));
/*      */     }
/*  921 */     return classInfo;
/*      */   }
/*      */   
/*      */   private void handleTargetError(String paramString) {
/*  925 */     if (this.strict) {
/*  926 */       this.logger.error(paramString);
/*  927 */       throw new InvalidMixinException(this, paramString);
/*      */     } 
/*  929 */     this.logger.warn(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int readPriority(ClassNode paramClassNode) {
/*  939 */     if (paramClassNode == null) {
/*  940 */       return this.parent.getDefaultMixinPriority();
/*      */     }
/*      */     
/*  943 */     AnnotationNode annotationNode = Annotations.getInvisible(paramClassNode, Mixin.class);
/*  944 */     if (annotationNode == null) {
/*  945 */       throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", new Object[] { this.className }));
/*      */     }
/*      */     
/*  948 */     Integer integer = (Integer)Annotations.getValue(annotationNode, "priority");
/*  949 */     return (integer == null) ? this.parent.getDefaultMixinPriority() : integer.intValue();
/*      */   }
/*      */   
/*      */   protected boolean readPseudo(ClassNode paramClassNode) {
/*  953 */     return (Annotations.getInvisible(paramClassNode, Pseudo.class) != null);
/*      */   }
/*      */   
/*      */   private boolean isReloading() {
/*  957 */     return this.pendingState instanceof Reloaded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private State getState() {
/*  965 */     return (this.state != null) ? this.state : this.pendingState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ClassInfo getClassInfo() {
/*  972 */     return this.info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IMixinConfig getConfig() {
/*  980 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinConfig getParent() {
/*  987 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPriority() {
/*  995 */     return this.priority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1003 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/* 1011 */     return this.className;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassRef() {
/* 1019 */     return getClassInfo().getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getClassBytes() {
/* 1027 */     return getState().getClassBytes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDetachedSuper() {
/* 1036 */     return getState().isDetachedSuper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUnique() {
/* 1043 */     return getState().isUnique();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVirtual() {
/* 1050 */     return this.virtual;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAccessor() {
/* 1057 */     return this.type instanceof SubType.Accessor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLoadable() {
/* 1064 */     return this.type.isLoadable();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Level getLoggingLevel() {
/* 1071 */     return this.parent.getLoggingLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment.Phase getPhase() {
/* 1079 */     return this.phase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinClassNode getClassNode(int paramInt) {
/* 1087 */     return getState().createClassNode(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTargetClasses() {
/* 1095 */     return this.targetClassNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<InterfaceInfo> getSoftImplements() {
/* 1102 */     return Collections.unmodifiableList(getState().getSoftImplements());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Set<String> getSyntheticInnerClasses() {
/* 1109 */     return Collections.unmodifiableSet(getState().getSyntheticInnerClasses());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Set<String> getInnerClasses() {
/* 1116 */     return Collections.unmodifiableSet(getState().getInnerClasses());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<ClassInfo> getTargets() {
/* 1123 */     return Collections.unmodifiableList(this.targetClasses);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Set<String> getInterfaces() {
/* 1132 */     return getState().getInterfaces();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MixinTargetContext createContextFor(TargetClassContext paramTargetClassContext) {
/* 1142 */     MixinClassNode mixinClassNode = getClassNode(8);
/* 1143 */     Profiler.Section section = this.profiler.begin("pre");
/* 1144 */     MixinTargetContext mixinTargetContext = this.type.createPreProcessor(mixinClassNode).prepare().createContextFor(paramTargetClassContext);
/* 1145 */     section.end();
/* 1146 */     return mixinTargetContext;
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
/*      */   private byte[] loadMixinClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 1158 */     byte[] arrayOfByte = null;
/*      */     
/*      */     try {
/* 1161 */       if (paramBoolean) {
/* 1162 */         String str = this.service.getClassRestrictions(paramString);
/* 1163 */         if (str.length() > 0) {
/* 1164 */           this.logger.error("Classloader restrictions [{}] encountered loading {}, name: {}", new Object[] { str, this, paramString });
/*      */         }
/*      */       } 
/* 1167 */       arrayOfByte = this.service.getBytecodeProvider().getClassBytes(paramString, paramBoolean);
/*      */     }
/* 1169 */     catch (ClassNotFoundException classNotFoundException) {
/* 1170 */       throw new ClassNotFoundException(String.format("The specified mixin '%s' was not found", new Object[] { paramString }));
/* 1171 */     } catch (IOException iOException) {
/* 1172 */       this.logger.warn("Failed to load mixin {}, the specified mixin will not be applied", new Object[] { paramString });
/* 1173 */       throw new InvalidMixinException(this, "An error was encountered whilst loading the mixin class", iOException);
/*      */     } 
/*      */     
/* 1176 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void reloadMixin(byte[] paramArrayOfbyte) {
/* 1185 */     if (this.pendingState != null) {
/* 1186 */       throw new IllegalStateException("Cannot reload mixin while it is initialising");
/*      */     }
/* 1188 */     this.pendingState = new Reloaded(this.state, paramArrayOfbyte);
/* 1189 */     validate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(MixinInfo paramMixinInfo) {
/* 1197 */     if (paramMixinInfo == null) {
/* 1198 */       return 0;
/*      */     }
/* 1200 */     if (paramMixinInfo.priority == this.priority) {
/* 1201 */       return this.order - paramMixinInfo.order;
/*      */     }
/* 1203 */     return this.priority - paramMixinInfo.priority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void preApply(String paramString, ClassNode paramClassNode) {
/* 1210 */     if (this.plugin != null) {
/* 1211 */       Profiler.Section section = this.profiler.begin("plugin");
/* 1212 */       this.plugin.preApply(paramString, paramClassNode, this.className, this);
/* 1213 */       section.end();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void postApply(String paramString, ClassNode paramClassNode) {
/* 1221 */     if (this.plugin != null) {
/* 1222 */       Profiler.Section section = this.profiler.begin("plugin");
/* 1223 */       this.plugin.postApply(paramString, paramClassNode, this.className, this);
/* 1224 */       section.end();
/*      */     } 
/*      */     
/* 1227 */     this.parent.postApply(paramString, paramClassNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1235 */     return String.format("%s:%s", new Object[] { this.parent.getName(), this.name });
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */