/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.Debug;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.struct.SourceMap;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.Extensions;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.ClassSignature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TargetClassContext
/*     */   extends ClassContext
/*     */   implements ITargetClassContext
/*     */ {
/*  60 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MixinEnvironment env;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Extensions extensions;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String sessionId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String className;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassNode classNode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassInfo classInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SourceMap sourceMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassSignature signature;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SortedSet<MixinInfo> mixins;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private final Map<String, Target> targetMethods = new HashMap<String, Target>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   private final Set<MethodNode> mixinMethods = new HashSet<MethodNode>();
/*     */ 
/*     */ 
/*     */   
/*     */   private int nextUniqueMethodIndex;
/*     */ 
/*     */ 
/*     */   
/*     */   private int nextUniqueFieldIndex;
/*     */ 
/*     */   
/*     */   private boolean applied;
/*     */ 
/*     */   
/*     */   private boolean forceExport;
/*     */ 
/*     */ 
/*     */   
/*     */   TargetClassContext(MixinEnvironment paramMixinEnvironment, Extensions paramExtensions, String paramString1, String paramString2, ClassNode paramClassNode, SortedSet<MixinInfo> paramSortedSet) {
/* 137 */     this.env = paramMixinEnvironment;
/* 138 */     this.extensions = paramExtensions;
/* 139 */     this.sessionId = paramString1;
/* 140 */     this.className = paramString2;
/* 141 */     this.classNode = paramClassNode;
/* 142 */     this.classInfo = ClassInfo.fromClassNode(paramClassNode);
/* 143 */     this.signature = this.classInfo.getSignature();
/* 144 */     this.mixins = paramSortedSet;
/* 145 */     this.sourceMap = new SourceMap(paramClassNode.sourceFile);
/* 146 */     this.sourceMap.addFile(this.classNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return this.className;
/*     */   }
/*     */   
/*     */   boolean isApplied() {
/* 155 */     return this.applied;
/*     */   }
/*     */   
/*     */   boolean isExportForced() {
/* 159 */     return this.forceExport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Extensions getExtensions() {
/* 166 */     return this.extensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getSessionId() {
/* 173 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getClassRef() {
/* 181 */     return this.classNode.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getClassName() {
/* 188 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassNode getClassNode() {
/* 196 */     return this.classNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<MethodNode> getMethods() {
/* 203 */     return this.classNode.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<FieldNode> getFields() {
/* 210 */     return this.classNode.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassInfo getClassInfo() {
/* 218 */     return this.classInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SortedSet<MixinInfo> getMixins() {
/* 225 */     return this.mixins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SourceMap getSourceMap() {
/* 232 */     return this.sourceMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void mergeSignature(ClassSignature paramClassSignature) {
/* 241 */     this.signature.merge(paramClassSignature);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addMixinMethod(MethodNode paramMethodNode) {
/* 250 */     this.mixinMethods.add(paramMethodNode);
/*     */   }
/*     */   
/*     */   void methodMerged(MethodNode paramMethodNode) {
/* 254 */     if (!this.mixinMethods.remove(paramMethodNode)) {
/* 255 */       logger.debug("Unexpected: Merged unregistered method {}{} in {}", new Object[] { paramMethodNode.name, paramMethodNode.desc, this });
/*     */     }
/*     */   }
/*     */   
/*     */   MethodNode findMethod(Deque<String> paramDeque, String paramString) {
/* 260 */     return findAliasedMethod(paramDeque, paramString, true);
/*     */   }
/*     */   
/*     */   MethodNode findAliasedMethod(Deque<String> paramDeque, String paramString) {
/* 264 */     return findAliasedMethod(paramDeque, paramString, false);
/*     */   }
/*     */   
/*     */   private MethodNode findAliasedMethod(Deque<String> paramDeque, String paramString, boolean paramBoolean) {
/* 268 */     String str = paramDeque.poll();
/* 269 */     if (str == null) {
/* 270 */       return null;
/*     */     }
/*     */     
/* 273 */     for (MethodNode methodNode : this.classNode.methods) {
/* 274 */       if (methodNode.name.equals(str) && methodNode.desc.equals(paramString)) {
/* 275 */         return methodNode;
/*     */       }
/*     */     } 
/*     */     
/* 279 */     if (paramBoolean) {
/* 280 */       for (MethodNode methodNode : this.mixinMethods) {
/* 281 */         if (methodNode.name.equals(str) && methodNode.desc.equals(paramString)) {
/* 282 */           return methodNode;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 287 */     return findAliasedMethod(paramDeque, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FieldNode findAliasedField(Deque<String> paramDeque, String paramString) {
/* 298 */     String str = paramDeque.poll();
/* 299 */     if (str == null) {
/* 300 */       return null;
/*     */     }
/*     */     
/* 303 */     for (FieldNode fieldNode : this.classNode.fields) {
/* 304 */       if (fieldNode.name.equals(str) && fieldNode.desc.equals(paramString)) {
/* 305 */         return fieldNode;
/*     */       }
/*     */     } 
/*     */     
/* 309 */     return findAliasedField(paramDeque, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Target getTargetMethod(MethodNode paramMethodNode) {
/* 319 */     if (!this.classNode.methods.contains(paramMethodNode)) {
/* 320 */       throw new IllegalArgumentException("Invalid target method supplied to getTargetMethod()");
/*     */     }
/*     */     
/* 323 */     String str = paramMethodNode.name + paramMethodNode.desc;
/* 324 */     Target target = this.targetMethods.get(str);
/* 325 */     if (target == null) {
/* 326 */       target = new Target(this.classNode, paramMethodNode);
/* 327 */       this.targetMethods.put(str, target);
/*     */     } 
/* 329 */     return target;
/*     */   }
/*     */   
/*     */   String getUniqueName(MethodNode paramMethodNode, boolean paramBoolean) {
/* 333 */     String str1 = Integer.toHexString(this.nextUniqueMethodIndex++);
/* 334 */     String str2 = paramBoolean ? "%2$s_$md$%1$s$%3$s" : "md%s$%s$%s";
/* 335 */     return String.format(str2, new Object[] { this.sessionId.substring(30), paramMethodNode.name, str1 });
/*     */   }
/*     */   
/*     */   String getUniqueName(FieldNode paramFieldNode) {
/* 339 */     String str = Integer.toHexString(this.nextUniqueFieldIndex++);
/* 340 */     return String.format("fd%s$%s$%s", new Object[] { this.sessionId.substring(30), paramFieldNode.name, str });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void applyMixins() {
/* 347 */     if (this.applied) {
/* 348 */       throw new IllegalStateException("Mixins already applied to target class " + this.className);
/*     */     }
/* 350 */     this.applied = true;
/*     */     
/* 352 */     MixinApplicatorStandard mixinApplicatorStandard = createApplicator();
/* 353 */     mixinApplicatorStandard.apply(this.mixins);
/* 354 */     applySignature();
/* 355 */     upgradeMethods();
/* 356 */     checkMerges();
/*     */   }
/*     */   
/*     */   private MixinApplicatorStandard createApplicator() {
/* 360 */     if (this.classInfo.isInterface()) {
/* 361 */       return new MixinApplicatorInterface(this);
/*     */     }
/* 363 */     return new MixinApplicatorStandard(this);
/*     */   }
/*     */   
/*     */   private void applySignature() {
/* 367 */     (getClassNode()).signature = this.signature.toString();
/*     */   }
/*     */   
/*     */   private void checkMerges() {
/* 371 */     for (MethodNode methodNode : this.mixinMethods) {
/* 372 */       if (!methodNode.name.startsWith("<")) {
/* 373 */         logger.debug("Unexpected: Registered method {}{} in {} was not merged", new Object[] { methodNode.name, methodNode.desc, this });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void processDebugTasks() {
/* 382 */     if (!this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/*     */       return;
/*     */     }
/*     */     
/* 386 */     AnnotationNode annotationNode = Annotations.getVisible(this.classNode, Debug.class);
/* 387 */     if (annotationNode != null) {
/* 388 */       this.forceExport = Boolean.TRUE.equals(Annotations.getValue(annotationNode, "export"));
/* 389 */       if (Boolean.TRUE.equals(Annotations.getValue(annotationNode, "print"))) {
/* 390 */         Bytecode.textify(this.classNode, System.err);
/*     */       }
/*     */     } 
/*     */     
/* 394 */     for (MethodNode methodNode : this.classNode.methods) {
/* 395 */       AnnotationNode annotationNode1 = Annotations.getVisible(methodNode, Debug.class);
/* 396 */       if (annotationNode1 != null && Boolean.TRUE.equals(Annotations.getValue(annotationNode1, "print")))
/* 397 */         Bytecode.textify(methodNode, System.err); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\TargetClassContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */