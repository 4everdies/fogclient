/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Pseudo;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
/*     */ import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnnotatedMixin
/*     */ {
/*     */   private final AnnotationHandle annotation;
/*     */   private final Messager messager;
/*     */   private final ITypeHandleProvider typeProvider;
/*     */   private final IObfuscationManager obf;
/*     */   private final IMappingConsumer mappings;
/*     */   private final TypeElement mixin;
/*     */   private final List<ExecutableElement> methods;
/*     */   private final TypeHandle handle;
/* 106 */   private final List<TypeHandle> targets = new ArrayList<TypeHandle>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final TypeHandle primaryTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String classRef;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean remap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean virtual;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotatedMixinElementHandlerOverwrite overwrites;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotatedMixinElementHandlerShadow shadows;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotatedMixinElementHandlerInjector injectors;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotatedMixinElementHandlerAccessor accessors;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotatedMixinElementHandlerSoftImplements softImplements;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean validated = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMixin(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, TypeElement paramTypeElement) {
/* 162 */     this.typeProvider = paramIMixinAnnotationProcessor.getTypeProvider();
/* 163 */     this.obf = paramIMixinAnnotationProcessor.getObfuscationManager();
/* 164 */     this.mappings = this.obf.createMappingConsumer();
/* 165 */     this.messager = (Messager)paramIMixinAnnotationProcessor;
/* 166 */     this.mixin = paramTypeElement;
/* 167 */     this.handle = new TypeHandle(paramTypeElement);
/* 168 */     this.methods = new ArrayList<ExecutableElement>(this.handle.getEnclosedElements(new ElementKind[] { ElementKind.METHOD }));
/* 169 */     this.virtual = this.handle.getAnnotation(Pseudo.class).exists();
/* 170 */     this.annotation = this.handle.getAnnotation(Mixin.class);
/* 171 */     this.classRef = TypeUtils.getInternalName(paramTypeElement);
/* 172 */     this.primaryTarget = initTargets();
/* 173 */     this.remap = (this.annotation.getBoolean("remap", true) && this.targets.size() > 0);
/*     */     
/* 175 */     this.overwrites = new AnnotatedMixinElementHandlerOverwrite(paramIMixinAnnotationProcessor, this);
/* 176 */     this.shadows = new AnnotatedMixinElementHandlerShadow(paramIMixinAnnotationProcessor, this);
/* 177 */     this.injectors = new AnnotatedMixinElementHandlerInjector(paramIMixinAnnotationProcessor, this);
/* 178 */     this.accessors = new AnnotatedMixinElementHandlerAccessor(paramIMixinAnnotationProcessor, this);
/* 179 */     this.softImplements = new AnnotatedMixinElementHandlerSoftImplements(paramIMixinAnnotationProcessor, this);
/*     */   }
/*     */   
/*     */   AnnotatedMixin runValidators(IMixinValidator.ValidationPass paramValidationPass, Collection<IMixinValidator> paramCollection) {
/* 183 */     for (IMixinValidator iMixinValidator : paramCollection) {
/* 184 */       if (!iMixinValidator.validate(paramValidationPass, this.mixin, this.annotation, this.targets)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 189 */     if (paramValidationPass == IMixinValidator.ValidationPass.FINAL && !this.validated) {
/* 190 */       this.validated = true;
/* 191 */       runFinalValidation();
/*     */     } 
/*     */     
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   private TypeHandle initTargets() {
/* 198 */     TypeHandle typeHandle = null;
/*     */ 
/*     */     
/*     */     try {
/* 202 */       for (TypeMirror typeMirror : this.annotation.getList()) {
/* 203 */         TypeHandle typeHandle1 = new TypeHandle((DeclaredType)typeMirror);
/* 204 */         if (this.targets.contains(typeHandle1)) {
/*     */           continue;
/*     */         }
/* 207 */         addTarget(typeHandle1);
/* 208 */         if (typeHandle == null) {
/* 209 */           typeHandle = typeHandle1;
/*     */         }
/*     */       } 
/* 212 */     } catch (Exception exception) {
/* 213 */       printMessage(Diagnostic.Kind.WARNING, "Error processing public targets: " + exception.getClass().getName() + ": " + exception.getMessage(), this);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 218 */       for (String str : this.annotation.getList("targets")) {
/* 219 */         TypeHandle typeHandle1 = this.typeProvider.getTypeHandle(str);
/* 220 */         if (this.targets.contains(typeHandle1)) {
/*     */           continue;
/*     */         }
/* 223 */         if (this.virtual)
/* 224 */         { typeHandle1 = this.typeProvider.getSimulatedHandle(str, this.mixin.asType()); }
/* 225 */         else { if (typeHandle1 == null) {
/* 226 */             printMessage(Diagnostic.Kind.ERROR, "Mixin target " + str + " could not be found", this);
/* 227 */             return null;
/* 228 */           }  if (typeHandle1.isPublic()) {
/* 229 */             printMessage(Diagnostic.Kind.WARNING, "Mixin target " + str + " is public and must be specified in value", this);
/* 230 */             return null;
/*     */           }  }
/* 232 */          addSoftTarget(typeHandle1, str);
/* 233 */         if (typeHandle == null) {
/* 234 */           typeHandle = typeHandle1;
/*     */         }
/*     */       } 
/* 237 */     } catch (Exception exception) {
/* 238 */       printMessage(Diagnostic.Kind.WARNING, "Error processing private targets: " + exception.getClass().getName() + ": " + exception.getMessage(), this);
/*     */     } 
/*     */     
/* 241 */     if (typeHandle == null) {
/* 242 */       printMessage(Diagnostic.Kind.ERROR, "Mixin has no targets", this);
/*     */     }
/*     */     
/* 245 */     return typeHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, AnnotatedMixin paramAnnotatedMixin) {
/* 252 */     this.messager.printMessage(paramKind, paramCharSequence, this.mixin, this.annotation.asMirror());
/*     */   }
/*     */   
/*     */   private void addSoftTarget(TypeHandle paramTypeHandle, String paramString) {
/* 256 */     ObfuscationData obfuscationData = this.obf.getDataProvider().getObfClass(paramTypeHandle);
/* 257 */     if (!obfuscationData.isEmpty()) {
/* 258 */       this.obf.getReferenceManager().addClassMapping(this.classRef, paramString, obfuscationData);
/*     */     }
/*     */     
/* 261 */     addTarget(paramTypeHandle);
/*     */   }
/*     */   
/*     */   private void addTarget(TypeHandle paramTypeHandle) {
/* 265 */     this.targets.add(paramTypeHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 270 */     return this.mixin.getSimpleName().toString();
/*     */   }
/*     */   
/*     */   public AnnotationHandle getAnnotation() {
/* 274 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeElement getMixin() {
/* 281 */     return this.mixin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getHandle() {
/* 288 */     return this.handle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassRef() {
/* 295 */     return this.classRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 302 */     return (this.mixin.getKind() == ElementKind.INTERFACE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TypeHandle getPrimaryTarget() {
/* 310 */     return this.primaryTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TypeHandle> getTargets() {
/* 317 */     return this.targets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultiTarget() {
/* 324 */     return (this.targets.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remap() {
/* 331 */     return this.remap;
/*     */   }
/*     */   
/*     */   public IMappingConsumer getMappings() {
/* 335 */     return this.mappings;
/*     */   }
/*     */   
/*     */   private void runFinalValidation() {
/* 339 */     for (ExecutableElement executableElement : this.methods) {
/* 340 */       this.overwrites.registerMerge(executableElement);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerOverwrite(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, boolean paramBoolean) {
/* 345 */     this.methods.remove(paramExecutableElement);
/* 346 */     this.overwrites.registerOverwrite(new AnnotatedMixinElementHandlerOverwrite.AnnotatedElementOverwrite(paramExecutableElement, paramAnnotationHandle, paramBoolean));
/*     */   }
/*     */   
/*     */   public void registerShadow(VariableElement paramVariableElement, AnnotationHandle paramAnnotationHandle, boolean paramBoolean) {
/* 350 */     this.shadows.getClass(); this.shadows.registerShadow(new AnnotatedMixinElementHandlerShadow.AnnotatedElementShadowField(this.shadows, paramVariableElement, paramAnnotationHandle, paramBoolean));
/*     */   }
/*     */   
/*     */   public void registerShadow(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, boolean paramBoolean) {
/* 354 */     this.methods.remove(paramExecutableElement);
/* 355 */     this.shadows.getClass(); this.shadows.registerShadow(new AnnotatedMixinElementHandlerShadow.AnnotatedElementShadowMethod(this.shadows, paramExecutableElement, paramAnnotationHandle, paramBoolean));
/*     */   }
/*     */   
/*     */   public void registerInjector(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, InjectorRemap paramInjectorRemap) {
/* 359 */     this.methods.remove(paramExecutableElement);
/* 360 */     this.injectors.registerInjector(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector(paramExecutableElement, paramAnnotationHandle, paramInjectorRemap));
/*     */     
/* 362 */     List list1 = paramAnnotationHandle.getAnnotationList("at");
/* 363 */     for (AnnotationHandle annotationHandle : list1) {
/* 364 */       registerInjectionPoint(paramExecutableElement, paramAnnotationHandle, annotationHandle, paramInjectorRemap, "@At(%s)");
/*     */     }
/*     */     
/* 367 */     List list2 = paramAnnotationHandle.getAnnotationList("slice");
/* 368 */     for (AnnotationHandle annotationHandle1 : list2) {
/* 369 */       String str = (String)annotationHandle1.getValue("id", "");
/*     */       
/* 371 */       AnnotationHandle annotationHandle2 = annotationHandle1.getAnnotation("from");
/* 372 */       if (annotationHandle2 != null) {
/* 373 */         registerInjectionPoint(paramExecutableElement, paramAnnotationHandle, annotationHandle2, paramInjectorRemap, "@Slice[" + str + "](from=@At(%s))");
/*     */       }
/*     */       
/* 376 */       AnnotationHandle annotationHandle3 = annotationHandle1.getAnnotation("to");
/* 377 */       if (annotationHandle3 != null) {
/* 378 */         registerInjectionPoint(paramExecutableElement, paramAnnotationHandle, annotationHandle3, paramInjectorRemap, "@Slice[" + str + "](to=@At(%s))");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerInjectionPoint(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle1, AnnotationHandle paramAnnotationHandle2, InjectorRemap paramInjectorRemap, String paramString) {
/* 384 */     this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjectionPoint(paramExecutableElement, paramAnnotationHandle1, paramAnnotationHandle2, paramInjectorRemap), paramString);
/*     */   }
/*     */   
/*     */   public void registerAccessor(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, boolean paramBoolean) {
/* 388 */     this.methods.remove(paramExecutableElement);
/* 389 */     this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementAccessor(paramExecutableElement, paramAnnotationHandle, paramBoolean));
/*     */   }
/*     */   
/*     */   public void registerInvoker(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, boolean paramBoolean) {
/* 393 */     this.methods.remove(paramExecutableElement);
/* 394 */     this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementInvoker(paramExecutableElement, paramAnnotationHandle, paramBoolean));
/*     */   }
/*     */   
/*     */   public void registerSoftImplements(AnnotationHandle paramAnnotationHandle) {
/* 398 */     this.softImplements.process(paramAnnotationHandle);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */