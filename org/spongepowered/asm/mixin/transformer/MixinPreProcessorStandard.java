/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.Dynamic;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.Unique;
/*     */ import org.spongepowered.asm.mixin.gen.Accessor;
/*     */ import org.spongepowered.asm.mixin.gen.Invoker;
/*     */ import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
/*     */ import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.perf.Profiler;
/*     */ import org.spongepowered.asm.util.throwables.SyntheticBridgeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MixinPreProcessorStandard
/*     */ {
/*     */   enum SpecialMethod
/*     */   {
/*  95 */     MERGE(true),
/*  96 */     OVERWRITE(true, Overwrite.class),
/*  97 */     SHADOW(false, Shadow.class),
/*  98 */     ACCESSOR(false, Accessor.class),
/*  99 */     INVOKER(false, Invoker.class);
/*     */     
/*     */     final boolean isOverwrite;
/*     */     
/*     */     final Class<? extends Annotation> annotation;
/*     */     
/*     */     final String description;
/*     */     
/*     */     SpecialMethod(boolean param1Boolean, Class<? extends Annotation> param1Class) {
/* 108 */       this.isOverwrite = param1Boolean;
/* 109 */       this.annotation = param1Class;
/* 110 */       this.description = "@" + Bytecode.getSimpleName(param1Class);
/*     */     }
/*     */     
/*     */     SpecialMethod(boolean param1Boolean) {
/* 114 */       this.isOverwrite = param1Boolean;
/* 115 */       this.annotation = null;
/* 116 */       this.description = "overwrite";
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 121 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MixinInfo mixin;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MixinInfo.MixinClassNode classNode;
/*     */ 
/*     */   
/*     */   protected final MixinEnvironment env;
/*     */ 
/*     */   
/* 143 */   protected final Profiler profiler = MixinEnvironment.getProfiler();
/*     */   private final boolean verboseLogging;
/*     */   private final boolean strictUnique;
/*     */   private boolean prepared;
/*     */   private boolean attached;
/*     */   
/*     */   MixinPreProcessorStandard(MixinInfo paramMixinInfo, MixinInfo.MixinClassNode paramMixinClassNode) {
/* 150 */     this.mixin = paramMixinInfo;
/* 151 */     this.classNode = paramMixinClassNode;
/* 152 */     this.env = paramMixinInfo.getParent().getEnvironment();
/* 153 */     this.verboseLogging = this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
/* 154 */     this.strictUnique = this.env.getOption(MixinEnvironment.Option.DEBUG_UNIQUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final MixinPreProcessorStandard prepare() {
/* 163 */     if (this.prepared) {
/* 164 */       return this;
/*     */     }
/*     */     
/* 167 */     this.prepared = true;
/*     */     
/* 169 */     Profiler.Section section = this.profiler.begin("prepare");
/*     */     
/* 171 */     for (MixinInfo.MixinMethodNode mixinMethodNode : this.classNode.mixinMethods) {
/* 172 */       ClassInfo.Method method = this.mixin.getClassInfo().findMethod(mixinMethodNode);
/* 173 */       prepareMethod(mixinMethodNode, method);
/*     */     } 
/*     */     
/* 176 */     for (FieldNode fieldNode : this.classNode.fields) {
/* 177 */       prepareField(fieldNode);
/*     */     }
/*     */     
/* 180 */     section.end();
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   protected void prepareMethod(MixinInfo.MixinMethodNode paramMixinMethodNode, ClassInfo.Method paramMethod) {
/* 185 */     prepareShadow(paramMixinMethodNode, paramMethod);
/* 186 */     prepareSoftImplements(paramMixinMethodNode, paramMethod);
/*     */   }
/*     */   
/*     */   protected void prepareShadow(MixinInfo.MixinMethodNode paramMixinMethodNode, ClassInfo.Method paramMethod) {
/* 190 */     AnnotationNode annotationNode = Annotations.getVisible(paramMixinMethodNode, Shadow.class);
/* 191 */     if (annotationNode == null) {
/*     */       return;
/*     */     }
/*     */     
/* 195 */     String str = (String)Annotations.getValue(annotationNode, "prefix", Shadow.class);
/* 196 */     if (paramMixinMethodNode.name.startsWith(str)) {
/* 197 */       Annotations.setVisible(paramMixinMethodNode, MixinRenamed.class, new Object[] { "originalName", paramMixinMethodNode.name });
/* 198 */       String str1 = paramMixinMethodNode.name.substring(str.length());
/* 199 */       paramMixinMethodNode.name = paramMethod.renameTo(str1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void prepareSoftImplements(MixinInfo.MixinMethodNode paramMixinMethodNode, ClassInfo.Method paramMethod) {
/* 204 */     for (InterfaceInfo interfaceInfo : this.mixin.getSoftImplements()) {
/* 205 */       if (interfaceInfo.renameMethod(paramMixinMethodNode)) {
/* 206 */         paramMethod.renameTo(paramMixinMethodNode.name);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareField(FieldNode paramFieldNode) {}
/*     */ 
/*     */   
/*     */   final MixinPreProcessorStandard conform(TargetClassContext paramTargetClassContext) {
/* 216 */     return conform(paramTargetClassContext.getClassInfo());
/*     */   }
/*     */   
/*     */   final MixinPreProcessorStandard conform(ClassInfo paramClassInfo) {
/* 220 */     Profiler.Section section = this.profiler.begin("conform");
/*     */     
/* 222 */     for (MixinInfo.MixinMethodNode mixinMethodNode : this.classNode.mixinMethods) {
/* 223 */       if (mixinMethodNode.isInjector()) {
/* 224 */         ClassInfo.Method method = this.mixin.getClassInfo().findMethod(mixinMethodNode, 10);
/* 225 */         conformInjector(paramClassInfo, mixinMethodNode, method);
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     section.end();
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   private void conformInjector(ClassInfo paramClassInfo, MixinInfo.MixinMethodNode paramMixinMethodNode, ClassInfo.Method paramMethod) {
/* 234 */     MethodMapper methodMapper = paramClassInfo.getMethodMapper();
/* 235 */     methodMapper.remapHandlerMethod(this.mixin, paramMixinMethodNode, paramMethod);
/*     */   }
/*     */   
/*     */   MixinTargetContext createContextFor(TargetClassContext paramTargetClassContext) {
/* 239 */     MixinTargetContext mixinTargetContext = new MixinTargetContext(this.mixin, this.classNode, paramTargetClassContext);
/* 240 */     conform(paramTargetClassContext);
/* 241 */     attach(mixinTargetContext);
/* 242 */     return mixinTargetContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final MixinPreProcessorStandard attach(MixinTargetContext paramMixinTargetContext) {
/* 251 */     if (this.attached) {
/* 252 */       throw new IllegalStateException("Preprocessor was already attached");
/*     */     }
/*     */     
/* 255 */     this.attached = true;
/*     */     
/* 257 */     Profiler.Section section1 = this.profiler.begin("attach");
/*     */ 
/*     */     
/* 260 */     Profiler.Section section2 = this.profiler.begin("methods");
/* 261 */     attachMethods(paramMixinTargetContext);
/* 262 */     section2 = section2.next("fields");
/* 263 */     attachFields(paramMixinTargetContext);
/*     */ 
/*     */     
/* 266 */     section2 = section2.next("transform");
/* 267 */     transform(paramMixinTargetContext);
/* 268 */     section2.end();
/*     */     
/* 270 */     section1.end();
/* 271 */     return this;
/*     */   }
/*     */   
/*     */   protected void attachMethods(MixinTargetContext paramMixinTargetContext) {
/* 275 */     for (Iterator<MixinInfo.MixinMethodNode> iterator = this.classNode.mixinMethods.iterator(); iterator.hasNext(); ) {
/* 276 */       MixinInfo.MixinMethodNode mixinMethodNode = iterator.next();
/*     */       
/* 278 */       if (!validateMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 279 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 283 */       if (attachInjectorMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 284 */         paramMixinTargetContext.addMixinMethod(mixinMethodNode);
/*     */         
/*     */         continue;
/*     */       } 
/* 288 */       if (attachAccessorMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 289 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 293 */       if (attachShadowMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 294 */         paramMixinTargetContext.addShadowMethod(mixinMethodNode);
/* 295 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 299 */       if (attachOverwriteMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 300 */         paramMixinTargetContext.addMixinMethod(mixinMethodNode);
/*     */         
/*     */         continue;
/*     */       } 
/* 304 */       if (attachUniqueMethod(paramMixinTargetContext, mixinMethodNode)) {
/* 305 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 309 */       attachMethod(paramMixinTargetContext, mixinMethodNode);
/* 310 */       paramMixinTargetContext.addMixinMethod(mixinMethodNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean validateMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean attachInjectorMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 319 */     return paramMixinMethodNode.isInjector();
/*     */   }
/*     */   
/*     */   protected boolean attachAccessorMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 323 */     return (attachAccessorMethod(paramMixinTargetContext, paramMixinMethodNode, SpecialMethod.ACCESSOR) || 
/* 324 */       attachAccessorMethod(paramMixinTargetContext, paramMixinMethodNode, SpecialMethod.INVOKER));
/*     */   }
/*     */   
/*     */   protected boolean attachAccessorMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode, SpecialMethod paramSpecialMethod) {
/* 328 */     AnnotationNode annotationNode = paramMixinMethodNode.getVisibleAnnotation(paramSpecialMethod.annotation);
/* 329 */     if (annotationNode == null) {
/* 330 */       return false;
/*     */     }
/*     */     
/* 333 */     String str = paramSpecialMethod + " method " + paramMixinMethodNode.name;
/* 334 */     ClassInfo.Method method = getSpecialMethod(paramMixinMethodNode, paramSpecialMethod);
/* 335 */     if (MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8) && method.isStatic()) {
/* 336 */       if (this.mixin.getTargets().size() > 1) {
/* 337 */         throw new InvalidAccessorException(paramMixinTargetContext, str + " in multi-target mixin is invalid. Mixin must have exactly 1 target.");
/*     */       }
/*     */       
/* 340 */       String str1 = paramMixinTargetContext.getUniqueName(paramMixinMethodNode, true);
/* 341 */       logger.log(this.mixin.getLoggingLevel(), "Renaming @Unique method {}{} to {} in {}", new Object[] { paramMixinMethodNode.name, paramMixinMethodNode.desc, str1, this.mixin });
/*     */       
/* 343 */       paramMixinMethodNode.name = method.renameTo(str1);
/*     */     } else {
/*     */       
/* 346 */       if (!method.isAbstract()) {
/* 347 */         throw new InvalidAccessorException(paramMixinTargetContext, str + " is not abstract");
/*     */       }
/*     */       
/* 350 */       if (method.isStatic()) {
/* 351 */         throw new InvalidAccessorException(paramMixinTargetContext, str + " cannot be static");
/*     */       }
/*     */     } 
/*     */     
/* 355 */     paramMixinTargetContext.addAccessorMethod(paramMixinMethodNode, paramSpecialMethod.annotation);
/* 356 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean attachShadowMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 360 */     return attachSpecialMethod(paramMixinTargetContext, paramMixinMethodNode, SpecialMethod.SHADOW);
/*     */   }
/*     */   
/*     */   protected boolean attachOverwriteMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 364 */     return attachSpecialMethod(paramMixinTargetContext, paramMixinMethodNode, SpecialMethod.OVERWRITE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean attachSpecialMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode, SpecialMethod paramSpecialMethod) {
/* 369 */     AnnotationNode annotationNode = paramMixinMethodNode.getVisibleAnnotation(paramSpecialMethod.annotation);
/* 370 */     if (annotationNode == null) {
/* 371 */       return false;
/*     */     }
/*     */     
/* 374 */     if (paramSpecialMethod.isOverwrite) {
/* 375 */       checkMixinNotUnique(paramMixinMethodNode, paramSpecialMethod);
/*     */     }
/*     */     
/* 378 */     ClassInfo.Method method = getSpecialMethod(paramMixinMethodNode, paramSpecialMethod);
/* 379 */     MethodNode methodNode = paramMixinTargetContext.findMethod(paramMixinMethodNode, annotationNode);
/* 380 */     if (methodNode == null) {
/* 381 */       if (paramSpecialMethod.isOverwrite) {
/* 382 */         return false;
/*     */       }
/* 384 */       methodNode = paramMixinTargetContext.findRemappedMethod(paramMixinMethodNode);
/* 385 */       if (methodNode == null)
/* 386 */         throw new InvalidMixinException(this.mixin, 
/* 387 */             String.format("%s method %s in %s was not located in the target class %s. %s%s", new Object[] {
/* 388 */                 paramSpecialMethod, paramMixinMethodNode.name, this.mixin, paramMixinTargetContext.getTarget(), paramMixinTargetContext.getReferenceMapper().getStatus(), 
/* 389 */                 getDynamicInfo(paramMixinMethodNode)
/*     */               })); 
/* 391 */       paramMixinMethodNode.name = method.renameTo(methodNode.name);
/*     */     } 
/*     */     
/* 394 */     if ("<init>".equals(methodNode.name)) {
/* 395 */       throw new InvalidMixinException(this.mixin, String.format("Nice try! %s in %s cannot alias a constructor", new Object[] { paramMixinMethodNode.name, this.mixin }));
/*     */     }
/*     */     
/* 398 */     if (!Bytecode.compareFlags(paramMixinMethodNode, methodNode, 8)) {
/* 399 */       throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of %s method %s in %s does not match the target", new Object[] { paramSpecialMethod, paramMixinMethodNode.name, this.mixin }));
/*     */     }
/*     */ 
/*     */     
/* 403 */     conformVisibility(paramMixinTargetContext, paramMixinMethodNode, paramSpecialMethod, methodNode);
/*     */     
/* 405 */     if (!methodNode.name.equals(paramMixinMethodNode.name)) {
/* 406 */       if (paramSpecialMethod.isOverwrite && (methodNode.access & 0x2) == 0) {
/* 407 */         throw new InvalidMixinException(this.mixin, "Non-private method cannot be aliased. Found " + methodNode.name);
/*     */       }
/*     */       
/* 410 */       paramMixinMethodNode.name = method.renameTo(methodNode.name);
/*     */     } 
/*     */     
/* 413 */     return true;
/*     */   }
/*     */   
/*     */   private void conformVisibility(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode, SpecialMethod paramSpecialMethod, MethodNode paramMethodNode) {
/* 417 */     Bytecode.Visibility visibility1 = Bytecode.getVisibility(paramMethodNode);
/* 418 */     Bytecode.Visibility visibility2 = Bytecode.getVisibility(paramMixinMethodNode);
/* 419 */     if (visibility2.ordinal() >= visibility1.ordinal()) {
/* 420 */       if (visibility1 == Bytecode.Visibility.PRIVATE && visibility2.ordinal() > Bytecode.Visibility.PRIVATE.ordinal()) {
/* 421 */         paramMixinTargetContext.getTarget().addUpgradedMethod(paramMethodNode);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 426 */     String str = String.format("%s %s method %s in %s cannot reduce visibiliy of %s target method", new Object[] { visibility2, paramSpecialMethod, paramMixinMethodNode.name, this.mixin, visibility1 });
/*     */ 
/*     */     
/* 429 */     if (paramSpecialMethod.isOverwrite && !this.mixin.getParent().conformOverwriteVisibility()) {
/* 430 */       throw new InvalidMixinException(this.mixin, str);
/*     */     }
/*     */     
/* 433 */     if (visibility2 == Bytecode.Visibility.PRIVATE) {
/* 434 */       if (paramSpecialMethod.isOverwrite) {
/* 435 */         logger.warn("Static binding violation: {}, visibility will be upgraded.", new Object[] { str });
/*     */       }
/* 437 */       paramMixinTargetContext.addUpgradedMethod(paramMixinMethodNode);
/* 438 */       Bytecode.setVisibility(paramMixinMethodNode, visibility1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ClassInfo.Method getSpecialMethod(MixinInfo.MixinMethodNode paramMixinMethodNode, SpecialMethod paramSpecialMethod) {
/* 443 */     ClassInfo.Method method = this.mixin.getClassInfo().findMethod(paramMixinMethodNode, 10);
/* 444 */     checkMethodNotUnique(method, paramSpecialMethod);
/* 445 */     return method;
/*     */   }
/*     */   
/*     */   protected void checkMethodNotUnique(ClassInfo.Method paramMethod, SpecialMethod paramSpecialMethod) {
/* 449 */     if (paramMethod.isUnique()) {
/* 450 */       throw new InvalidMixinException(this.mixin, String.format("%s method %s in %s cannot be @Unique", new Object[] { paramSpecialMethod, paramMethod.getName(), this.mixin }));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkMixinNotUnique(MixinInfo.MixinMethodNode paramMixinMethodNode, SpecialMethod paramSpecialMethod) {
/* 455 */     if (this.mixin.isUnique()) {
/* 456 */       throw new InvalidMixinException(this.mixin, String.format("%s method %s found in a @Unique mixin %s", new Object[] { paramSpecialMethod, paramMixinMethodNode.name, this.mixin }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean attachUniqueMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 462 */     ClassInfo.Method method = this.mixin.getClassInfo().findMethod(paramMixinMethodNode, 10);
/* 463 */     if (method == null || (!method.isUnique() && !this.mixin.isUnique() && !method.isSynthetic())) {
/* 464 */       return false;
/*     */     }
/*     */     
/* 467 */     if (method.isSynthetic()) {
/* 468 */       paramMixinTargetContext.transformDescriptor(paramMixinMethodNode);
/* 469 */       method.remapTo(paramMixinMethodNode.desc);
/*     */     } 
/*     */     
/* 472 */     MethodNode methodNode = paramMixinTargetContext.findMethod(paramMixinMethodNode, (AnnotationNode)null);
/* 473 */     if (methodNode == null) {
/* 474 */       return false;
/*     */     }
/*     */     
/* 477 */     String str = method.isSynthetic() ? "synthetic" : "@Unique";
/*     */     
/* 479 */     if (Bytecode.getVisibility(paramMixinMethodNode).ordinal() < Bytecode.Visibility.PUBLIC.ordinal()) {
/* 480 */       String str1 = paramMixinTargetContext.getUniqueName(paramMixinMethodNode, false);
/* 481 */       logger.log(this.mixin.getLoggingLevel(), "Renaming {} method {}{} to {} in {}", new Object[] { str, paramMixinMethodNode.name, paramMixinMethodNode.desc, str1, this.mixin });
/*     */       
/* 483 */       paramMixinMethodNode.name = method.renameTo(str1);
/* 484 */       return false;
/*     */     } 
/*     */     
/* 487 */     if (this.strictUnique) {
/* 488 */       throw new InvalidMixinException(this.mixin, String.format("Method conflict, %s method %s in %s cannot overwrite %s%s in %s", new Object[] { str, paramMixinMethodNode.name, this.mixin, methodNode.name, methodNode.desc, paramMixinTargetContext
/* 489 */               .getTarget() }));
/*     */     }
/*     */     
/* 492 */     AnnotationNode annotationNode = Annotations.getVisible(paramMixinMethodNode, Unique.class);
/* 493 */     if (annotationNode == null || !((Boolean)Annotations.getValue(annotationNode, "silent", Boolean.FALSE)).booleanValue()) {
/* 494 */       if (Bytecode.hasFlag(paramMixinMethodNode, 64)) {
/*     */         
/*     */         try {
/* 497 */           Bytecode.compareBridgeMethods(methodNode, paramMixinMethodNode);
/* 498 */           logger.debug("Discarding sythetic bridge method {} in {} because existing method in {} is compatible", new Object[] { str, paramMixinMethodNode.name, this.mixin, paramMixinTargetContext
/* 499 */                 .getTarget() });
/* 500 */           return true;
/* 501 */         } catch (SyntheticBridgeException syntheticBridgeException) {
/* 502 */           if (this.verboseLogging || this.env.getOption(MixinEnvironment.Option.DEBUG_VERIFY))
/*     */           {
/* 504 */             syntheticBridgeException.printAnalysis(paramMixinTargetContext, methodNode, paramMixinMethodNode);
/*     */           }
/* 506 */           throw new InvalidMixinException(this.mixin, syntheticBridgeException.getMessage());
/*     */         } 
/*     */       }
/*     */       
/* 510 */       logger.warn("Discarding {} public method {} in {} because it already exists in {}", new Object[] { str, paramMixinMethodNode.name, this.mixin, paramMixinTargetContext
/* 511 */             .getTarget() });
/* 512 */       return true;
/*     */     } 
/*     */     
/* 515 */     paramMixinTargetContext.addMixinMethod(paramMixinMethodNode);
/* 516 */     return true;
/*     */   }
/*     */   
/*     */   protected void attachMethod(MixinTargetContext paramMixinTargetContext, MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 520 */     ClassInfo.Method method1 = this.mixin.getClassInfo().findMethod(paramMixinMethodNode);
/* 521 */     if (method1 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 525 */     ClassInfo.Method method2 = this.mixin.getClassInfo().findMethodInHierarchy(paramMixinMethodNode, ClassInfo.SearchType.SUPER_CLASSES_ONLY);
/* 526 */     if (method2 != null && method2.isRenamed()) {
/* 527 */       paramMixinMethodNode.name = method1.renameTo(method2.getName());
/*     */     }
/*     */     
/* 530 */     MethodNode methodNode = paramMixinTargetContext.findMethod(paramMixinMethodNode, (AnnotationNode)null);
/* 531 */     if (methodNode != null) {
/* 532 */       conformVisibility(paramMixinTargetContext, paramMixinMethodNode, SpecialMethod.MERGE, methodNode);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void attachFields(MixinTargetContext paramMixinTargetContext) {
/* 537 */     for (Iterator<FieldNode> iterator = this.classNode.fields.iterator(); iterator.hasNext(); ) {
/* 538 */       FieldNode fieldNode1 = iterator.next();
/* 539 */       AnnotationNode annotationNode = Annotations.getVisible(fieldNode1, Shadow.class);
/* 540 */       boolean bool = (annotationNode != null) ? true : false;
/*     */       
/* 542 */       if (!validateField(paramMixinTargetContext, fieldNode1, annotationNode)) {
/* 543 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 547 */       ClassInfo.Field field = this.mixin.getClassInfo().findField(fieldNode1);
/* 548 */       paramMixinTargetContext.transformDescriptor(fieldNode1);
/* 549 */       field.remapTo(fieldNode1.desc);
/*     */       
/* 551 */       if (field.isUnique() && bool) {
/* 552 */         throw new InvalidMixinException(this.mixin, String.format("@Shadow field %s cannot be @Unique", new Object[] { fieldNode1.name }));
/*     */       }
/*     */       
/* 555 */       FieldNode fieldNode2 = paramMixinTargetContext.findField(fieldNode1, annotationNode);
/* 556 */       if (fieldNode2 == null) {
/* 557 */         if (annotationNode == null) {
/*     */           continue;
/*     */         }
/* 560 */         fieldNode2 = paramMixinTargetContext.findRemappedField(fieldNode1);
/* 561 */         if (fieldNode2 == null)
/*     */         {
/* 563 */           throw new InvalidMixinException(this.mixin, String.format("Shadow field %s was not located in the target class %s. %s%s", new Object[] { fieldNode1.name, paramMixinTargetContext
/* 564 */                   .getTarget(), paramMixinTargetContext.getReferenceMapper().getStatus(), 
/* 565 */                   getDynamicInfo(fieldNode1) }));
/*     */         }
/* 567 */         fieldNode1.name = field.renameTo(fieldNode2.name);
/*     */       } 
/*     */       
/* 570 */       if (!Bytecode.compareFlags(fieldNode1, fieldNode2, 8)) {
/* 571 */         throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of @Shadow field %s in %s does not match the target", new Object[] { fieldNode1.name, this.mixin }));
/*     */       }
/*     */ 
/*     */       
/* 575 */       if (field.isUnique()) {
/* 576 */         if ((fieldNode1.access & 0x6) != 0) {
/* 577 */           String str = paramMixinTargetContext.getUniqueName(fieldNode1);
/* 578 */           logger.log(this.mixin.getLoggingLevel(), "Renaming @Unique field {}{} to {} in {}", new Object[] { fieldNode1.name, fieldNode1.desc, str, this.mixin });
/*     */           
/* 580 */           fieldNode1.name = field.renameTo(str);
/*     */           
/*     */           continue;
/*     */         } 
/* 584 */         if (this.strictUnique) {
/* 585 */           throw new InvalidMixinException(this.mixin, String.format("Field conflict, @Unique field %s in %s cannot overwrite %s%s in %s", new Object[] { fieldNode1.name, this.mixin, fieldNode2.name, fieldNode2.desc, paramMixinTargetContext
/* 586 */                   .getTarget() }));
/*     */         }
/*     */         
/* 589 */         logger.warn("Discarding @Unique public field {} in {} because it already exists in {}. Note that declared FIELD INITIALISERS will NOT be removed!", new Object[] { fieldNode1.name, this.mixin, paramMixinTargetContext
/* 590 */               .getTarget() });
/*     */         
/* 592 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 597 */       if (!fieldNode2.desc.equals(fieldNode1.desc)) {
/* 598 */         throw new InvalidMixinException(this.mixin, String.format("The field %s in the target class has a conflicting signature", new Object[] { fieldNode1.name }));
/*     */       }
/*     */ 
/*     */       
/* 602 */       if (!fieldNode2.name.equals(fieldNode1.name)) {
/* 603 */         if ((fieldNode2.access & 0x2) == 0 && (fieldNode2.access & 0x1000) == 0) {
/* 604 */           throw new InvalidMixinException(this.mixin, "Non-private field cannot be aliased. Found " + fieldNode2.name);
/*     */         }
/*     */         
/* 607 */         fieldNode1.name = field.renameTo(fieldNode2.name);
/*     */       } 
/*     */ 
/*     */       
/* 611 */       iterator.remove();
/*     */       
/* 613 */       if (bool) {
/* 614 */         boolean bool1 = field.isDecoratedFinal();
/* 615 */         if (this.verboseLogging && Bytecode.hasFlag(fieldNode2, 16) != bool1) {
/* 616 */           String str = bool1 ? "@Shadow field {}::{} is decorated with @Final but target is not final" : "@Shadow target {}::{} is final but shadow is not decorated with @Final";
/*     */ 
/*     */           
/* 619 */           logger.warn(str, new Object[] { this.mixin, fieldNode1.name });
/*     */         } 
/*     */         
/* 622 */         paramMixinTargetContext.addShadowField(fieldNode1, field);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean validateField(MixinTargetContext paramMixinTargetContext, FieldNode paramFieldNode, AnnotationNode paramAnnotationNode) {
/* 629 */     if (Bytecode.hasFlag(paramFieldNode, 8) && 
/* 630 */       !Bytecode.hasFlag(paramFieldNode, 2) && 
/* 631 */       !Bytecode.hasFlag(paramFieldNode, 4096) && paramAnnotationNode == null)
/*     */     {
/* 633 */       throw new InvalidMixinException(paramMixinTargetContext, String.format("Mixin %s contains non-private static field %s:%s", new Object[] { paramMixinTargetContext, paramFieldNode.name, paramFieldNode.desc }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 638 */     String str = (String)Annotations.getValue(paramAnnotationNode, "prefix", Shadow.class);
/* 639 */     if (paramFieldNode.name.startsWith(str)) {
/* 640 */       throw new InvalidMixinException(paramMixinTargetContext, String.format("@Shadow field %s.%s has a shadow prefix. This is not allowed.", new Object[] { paramMixinTargetContext, paramFieldNode.name }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 645 */     if ("super$".equals(paramFieldNode.name)) {
/* 646 */       if (paramFieldNode.access != 2) {
/* 647 */         throw new InvalidMixinException(this.mixin, String.format("Imaginary super field %s.%s must be private and non-final", new Object[] { paramMixinTargetContext, paramFieldNode.name }));
/*     */       }
/*     */       
/* 650 */       if (!paramFieldNode.desc.equals("L" + this.mixin.getClassRef() + ";"))
/* 651 */         throw new InvalidMixinException(this.mixin, 
/* 652 */             String.format("Imaginary super field %s.%s must have the same type as the parent mixin (%s)", new Object[] {
/* 653 */                 paramMixinTargetContext, paramFieldNode.name, this.mixin.getClassName()
/*     */               })); 
/* 655 */       return false;
/*     */     } 
/*     */     
/* 658 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void transform(MixinTargetContext paramMixinTargetContext) {
/* 666 */     for (MethodNode methodNode : this.classNode.methods) {
/* 667 */       for (ListIterator<AbstractInsnNode> listIterator = methodNode.instructions.iterator(); listIterator.hasNext(); ) {
/* 668 */         AbstractInsnNode abstractInsnNode = listIterator.next();
/* 669 */         if (abstractInsnNode instanceof MethodInsnNode) {
/* 670 */           transformMethod((MethodInsnNode)abstractInsnNode); continue;
/* 671 */         }  if (abstractInsnNode instanceof FieldInsnNode) {
/* 672 */           transformField((FieldInsnNode)abstractInsnNode);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void transformMethod(MethodInsnNode paramMethodInsnNode) {
/* 679 */     Profiler.Section section = this.profiler.begin("meta");
/* 680 */     ClassInfo classInfo = ClassInfo.forName(paramMethodInsnNode.owner);
/* 681 */     if (classInfo == null) {
/* 682 */       throw new RuntimeException(new ClassNotFoundException(paramMethodInsnNode.owner.replace('/', '.')));
/*     */     }
/*     */     
/* 685 */     ClassInfo.Method method = classInfo.findMethodInHierarchy(paramMethodInsnNode, ClassInfo.SearchType.ALL_CLASSES, 2);
/* 686 */     section.end();
/*     */     
/* 688 */     if (method != null && method.isRenamed()) {
/* 689 */       paramMethodInsnNode.name = method.getName();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void transformField(FieldInsnNode paramFieldInsnNode) {
/* 694 */     Profiler.Section section = this.profiler.begin("meta");
/* 695 */     ClassInfo classInfo = ClassInfo.forName(paramFieldInsnNode.owner);
/* 696 */     if (classInfo == null) {
/* 697 */       throw new RuntimeException(new ClassNotFoundException(paramFieldInsnNode.owner.replace('/', '.')));
/*     */     }
/*     */     
/* 700 */     ClassInfo.Field field = classInfo.findField(paramFieldInsnNode, 2);
/* 701 */     section.end();
/*     */     
/* 703 */     if (field != null && field.isRenamed()) {
/* 704 */       paramFieldInsnNode.name = field.getName();
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
/*     */   protected static String getDynamicInfo(MethodNode paramMethodNode) {
/* 718 */     return getDynamicInfo("Method", Annotations.getInvisible(paramMethodNode, Dynamic.class));
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
/*     */   protected static String getDynamicInfo(FieldNode paramFieldNode) {
/* 731 */     return getDynamicInfo("Field", Annotations.getInvisible(paramFieldNode, Dynamic.class));
/*     */   }
/*     */   
/*     */   private static String getDynamicInfo(String paramString, AnnotationNode paramAnnotationNode) {
/* 735 */     String str = Strings.nullToEmpty((String)Annotations.getValue(paramAnnotationNode));
/* 736 */     Type type = (Type)Annotations.getValue(paramAnnotationNode, "mixin");
/* 737 */     if (type != null) {
/* 738 */       str = String.format("{%s} %s", new Object[] { type.getClassName(), str }).trim();
/*     */     }
/* 740 */     return (str.length() > 0) ? String.format(" %s is @Dynamic(%s)", new Object[] { paramString, str }) : "";
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinPreProcessorStandard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */