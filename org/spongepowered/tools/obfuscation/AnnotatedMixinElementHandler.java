/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.asm.util.ConstraintParser;
/*     */ import org.spongepowered.asm.util.throwables.ConstraintViolationException;
/*     */ import org.spongepowered.asm.util.throwables.InvalidConstraintException;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
/*     */ import org.spongepowered.tools.obfuscation.mirror.Visibility;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AnnotatedMixinElementHandler
/*     */ {
/*     */   protected final AnnotatedMixin mixin;
/*     */   protected final String classRef;
/*     */   protected final IMixinAnnotationProcessor ap;
/*     */   protected final IObfuscationManager obf;
/*     */   private IMappingConsumer mappings;
/*     */   
/*     */   static abstract class AnnotatedElement<E extends Element>
/*     */   {
/*     */     protected final E element;
/*     */     protected final AnnotationHandle annotation;
/*     */     private final String desc;
/*     */     
/*     */     public AnnotatedElement(E param1E, AnnotationHandle param1AnnotationHandle) {
/*  74 */       this.element = param1E;
/*  75 */       this.annotation = param1AnnotationHandle;
/*  76 */       this.desc = TypeUtils.getDescriptor((Element)param1E);
/*     */     }
/*     */     
/*     */     public E getElement() {
/*  80 */       return this.element;
/*     */     }
/*     */     
/*     */     public AnnotationHandle getAnnotation() {
/*  84 */       return this.annotation;
/*     */     }
/*     */     
/*     */     public String getSimpleName() {
/*  88 */       return getElement().getSimpleName().toString();
/*     */     }
/*     */     
/*     */     public String getDesc() {
/*  92 */       return this.desc;
/*     */     }
/*     */     
/*     */     public final void printMessage(Messager param1Messager, Diagnostic.Kind param1Kind, CharSequence param1CharSequence) {
/*  96 */       param1Messager.printMessage(param1Kind, param1CharSequence, (Element)this.element, this.annotation.asMirror());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class AliasedElementName
/*     */   {
/*     */     protected final String originalName;
/*     */ 
/*     */ 
/*     */     
/*     */     private final List<String> aliases;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean caseSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AliasedElementName(Element param1Element, AnnotationHandle param1AnnotationHandle) {
/* 119 */       this.originalName = param1Element.getSimpleName().toString();
/* 120 */       this.aliases = param1AnnotationHandle.getList("aliases");
/*     */     }
/*     */     
/*     */     public AliasedElementName setCaseSensitive(boolean param1Boolean) {
/* 124 */       this.caseSensitive = param1Boolean;
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isCaseSensitive() {
/* 129 */       return this.caseSensitive;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasAliases() {
/* 136 */       return (this.aliases.size() > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> getAliases() {
/* 143 */       return this.aliases;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String elementName() {
/* 150 */       return this.originalName;
/*     */     }
/*     */     
/*     */     public String baseName() {
/* 154 */       return this.originalName;
/*     */     }
/*     */     
/*     */     public boolean hasPrefix() {
/* 158 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ShadowElementName
/*     */     extends AliasedElementName
/*     */   {
/*     */     private final boolean hasPrefix;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String prefix;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String baseName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String obfuscated;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ShadowElementName(Element param1Element, AnnotationHandle param1AnnotationHandle) {
/* 191 */       super(param1Element, param1AnnotationHandle);
/*     */       
/* 193 */       this.prefix = (String)param1AnnotationHandle.getValue("prefix", "shadow$");
/*     */       
/* 195 */       boolean bool = false;
/* 196 */       String str = this.originalName;
/* 197 */       if (str.startsWith(this.prefix)) {
/* 198 */         bool = true;
/* 199 */         str = str.substring(this.prefix.length());
/*     */       } 
/*     */       
/* 202 */       this.hasPrefix = bool;
/* 203 */       this.obfuscated = this.baseName = str;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 211 */       return this.baseName;
/*     */     }
/*     */ 
/*     */     
/*     */     public String baseName() {
/* 216 */       return this.baseName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ShadowElementName setObfuscatedName(IMapping<?> param1IMapping) {
/* 226 */       this.obfuscated = param1IMapping.getName();
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ShadowElementName setObfuscatedName(String param1String) {
/* 237 */       this.obfuscated = param1String;
/* 238 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrefix() {
/* 243 */       return this.hasPrefix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String prefix() {
/* 250 */       return this.hasPrefix ? this.prefix : "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String name() {
/* 257 */       return prefix(this.baseName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String obfuscated() {
/* 264 */       return prefix(this.obfuscated);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String prefix(String param1String) {
/* 274 */       return this.hasPrefix ? (this.prefix + param1String) : param1String;
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
/*     */   AnnotatedMixinElementHandler(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/* 296 */     this.ap = paramIMixinAnnotationProcessor;
/* 297 */     this.mixin = paramAnnotatedMixin;
/* 298 */     this.classRef = paramAnnotatedMixin.getClassRef();
/* 299 */     this.obf = paramIMixinAnnotationProcessor.getObfuscationManager();
/*     */   }
/*     */   
/*     */   private IMappingConsumer getMappings() {
/* 303 */     if (this.mappings == null) {
/* 304 */       IMappingConsumer iMappingConsumer = this.mixin.getMappings();
/* 305 */       if (iMappingConsumer instanceof Mappings) {
/* 306 */         this.mappings = ((Mappings)iMappingConsumer).asUnique();
/*     */       } else {
/* 308 */         this.mappings = iMappingConsumer;
/*     */       } 
/*     */     } 
/* 311 */     return this.mappings;
/*     */   }
/*     */   
/*     */   protected final void addFieldMappings(String paramString1, String paramString2, ObfuscationData<MappingField> paramObfuscationData) {
/* 315 */     for (ObfuscationType obfuscationType : paramObfuscationData) {
/* 316 */       MappingField mappingField = paramObfuscationData.get(obfuscationType);
/* 317 */       addFieldMapping(obfuscationType, paramString1, mappingField.getSimpleName(), paramString2, mappingField.getDesc());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addFieldMapping(ObfuscationType paramObfuscationType, ShadowElementName paramShadowElementName, String paramString1, String paramString2) {
/* 325 */     addFieldMapping(paramObfuscationType, paramShadowElementName.name(), paramShadowElementName.obfuscated(), paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addFieldMapping(ObfuscationType paramObfuscationType, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 332 */     MappingField mappingField1 = new MappingField(this.classRef, paramString1, paramString3);
/* 333 */     MappingField mappingField2 = new MappingField(this.classRef, paramString2, paramString4);
/* 334 */     getMappings().addFieldMapping(paramObfuscationType, mappingField1, mappingField2);
/*     */   }
/*     */   
/*     */   protected final void addMethodMappings(String paramString1, String paramString2, ObfuscationData<MappingMethod> paramObfuscationData) {
/* 338 */     for (ObfuscationType obfuscationType : paramObfuscationData) {
/* 339 */       MappingMethod mappingMethod = paramObfuscationData.get(obfuscationType);
/* 340 */       addMethodMapping(obfuscationType, paramString1, mappingMethod.getSimpleName(), paramString2, mappingMethod.getDesc());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addMethodMapping(ObfuscationType paramObfuscationType, ShadowElementName paramShadowElementName, String paramString1, String paramString2) {
/* 348 */     addMethodMapping(paramObfuscationType, paramShadowElementName.name(), paramShadowElementName.obfuscated(), paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addMethodMapping(ObfuscationType paramObfuscationType, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 355 */     MappingMethod mappingMethod1 = new MappingMethod(this.classRef, paramString1, paramString3);
/* 356 */     MappingMethod mappingMethod2 = new MappingMethod(this.classRef, paramString2, paramString4);
/* 357 */     getMappings().addMethodMapping(paramObfuscationType, mappingMethod1, mappingMethod2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkConstraints(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle) {
/*     */     try {
/* 369 */       ConstraintParser.Constraint constraint = ConstraintParser.parse((String)paramAnnotationHandle.getValue("constraints"));
/*     */       try {
/* 371 */         constraint.check(this.ap.getTokenProvider());
/* 372 */       } catch (ConstraintViolationException constraintViolationException) {
/* 373 */         this.ap.printMessage(Diagnostic.Kind.ERROR, constraintViolationException.getMessage(), paramExecutableElement, paramAnnotationHandle.asMirror());
/*     */       } 
/* 375 */     } catch (InvalidConstraintException invalidConstraintException) {
/* 376 */       this.ap.printMessage(Diagnostic.Kind.WARNING, invalidConstraintException.getMessage(), paramExecutableElement, paramAnnotationHandle.asMirror());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void validateTarget(Element paramElement, AnnotationHandle paramAnnotationHandle, AliasedElementName paramAliasedElementName, String paramString) {
/* 381 */     if (paramElement instanceof ExecutableElement) {
/* 382 */       validateTargetMethod((ExecutableElement)paramElement, paramAnnotationHandle, paramAliasedElementName, paramString, false, false);
/* 383 */     } else if (paramElement instanceof VariableElement) {
/* 384 */       validateTargetField((VariableElement)paramElement, paramAnnotationHandle, paramAliasedElementName, paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateTargetMethod(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, AliasedElementName paramAliasedElementName, String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 394 */     String str = TypeUtils.getJavaSignature(paramExecutableElement);
/*     */     
/* 396 */     for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 397 */       if (typeHandle.isImaginary()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 402 */       MethodHandle methodHandle = typeHandle.findMethod(paramExecutableElement);
/*     */ 
/*     */       
/* 405 */       if (methodHandle == null && paramAliasedElementName.hasPrefix()) {
/* 406 */         methodHandle = typeHandle.findMethod(paramAliasedElementName.baseName(), str);
/*     */       }
/*     */ 
/*     */       
/* 410 */       if (methodHandle == null && paramAliasedElementName.hasAliases()) {
/* 411 */         String str1; Iterator<String> iterator = paramAliasedElementName.getAliases().iterator(); do { str1 = iterator.next(); } while (iterator.hasNext() && (
/* 412 */           methodHandle = typeHandle.findMethod(str1, str)) == null);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 418 */       if (methodHandle != null) {
/* 419 */         if (paramBoolean1)
/* 420 */           validateMethodVisibility(paramExecutableElement, paramAnnotationHandle, paramString, typeHandle, methodHandle);  continue;
/*     */       } 
/* 422 */       if (!paramBoolean2) {
/* 423 */         printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + paramString + " method in " + typeHandle, paramExecutableElement, paramAnnotationHandle);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateMethodVisibility(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, String paramString, TypeHandle paramTypeHandle, MethodHandle paramMethodHandle) {
/* 430 */     Visibility visibility1 = paramMethodHandle.getVisibility();
/* 431 */     if (visibility1 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 435 */     Visibility visibility2 = TypeUtils.getVisibility(paramExecutableElement);
/* 436 */     String str = "visibility of " + visibility1 + " method in " + paramTypeHandle;
/* 437 */     if (visibility1.ordinal() > visibility2.ordinal()) {
/* 438 */       printMessage(Diagnostic.Kind.WARNING, visibility2 + " " + paramString + " method cannot reduce " + str, paramExecutableElement, paramAnnotationHandle);
/* 439 */     } else if (visibility1 == Visibility.PRIVATE && visibility2.ordinal() > visibility1.ordinal()) {
/* 440 */       printMessage(Diagnostic.Kind.WARNING, visibility2 + " " + paramString + " method will upgrade " + str, paramExecutableElement, paramAnnotationHandle);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateTargetField(VariableElement paramVariableElement, AnnotationHandle paramAnnotationHandle, AliasedElementName paramAliasedElementName, String paramString) {
/* 449 */     String str = paramVariableElement.asType().toString();
/*     */     
/* 451 */     for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 452 */       String str1; if (typeHandle.isImaginary()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 457 */       FieldHandle fieldHandle = typeHandle.findField(paramVariableElement);
/* 458 */       if (fieldHandle != null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 463 */       List<String> list = paramAliasedElementName.getAliases();
/* 464 */       Iterator<String> iterator = list.iterator(); do { str1 = iterator.next(); } while (iterator.hasNext() && (
/* 465 */         fieldHandle = typeHandle.findField(str1, str)) == null);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 470 */       if (fieldHandle == null) {
/* 471 */         this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + paramString + " field in " + typeHandle, paramVariableElement, paramAnnotationHandle.asMirror());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateReferencedTarget(ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle, MemberInfo paramMemberInfo, String paramString) {
/* 481 */     String str = paramMemberInfo.toDescriptor();
/*     */     
/* 483 */     for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 484 */       if (typeHandle.isImaginary()) {
/*     */         continue;
/*     */       }
/*     */       
/* 488 */       MethodHandle methodHandle = typeHandle.findMethod(paramMemberInfo.name, str);
/* 489 */       if (methodHandle == null) {
/* 490 */         this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target method for " + paramString + " in " + typeHandle, paramExecutableElement, paramAnnotationHandle.asMirror());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void printMessage(Diagnostic.Kind paramKind, String paramString, Element paramElement, AnnotationHandle paramAnnotationHandle) {
/* 496 */     if (paramAnnotationHandle == null) {
/* 497 */       this.ap.printMessage(paramKind, paramString, paramElement);
/*     */     } else {
/* 499 */       this.ap.printMessage(paramKind, paramString, paramElement, paramAnnotationHandle.asMirror());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static <T extends IMapping<T>> ObfuscationData<T> stripOwnerData(ObfuscationData<T> paramObfuscationData) {
/* 504 */     ObfuscationData<Object> obfuscationData = new ObfuscationData();
/* 505 */     for (ObfuscationType obfuscationType : paramObfuscationData) {
/* 506 */       IMapping iMapping = (IMapping)paramObfuscationData.get(obfuscationType);
/* 507 */       obfuscationData.put(obfuscationType, iMapping.move(null));
/*     */     } 
/* 509 */     return (ObfuscationData)obfuscationData;
/*     */   }
/*     */   
/*     */   protected static <T extends IMapping<T>> ObfuscationData<T> stripDescriptors(ObfuscationData<T> paramObfuscationData) {
/* 513 */     ObfuscationData<Object> obfuscationData = new ObfuscationData();
/* 514 */     for (ObfuscationType obfuscationType : paramObfuscationData) {
/* 515 */       IMapping iMapping = (IMapping)paramObfuscationData.get(obfuscationType);
/* 516 */       obfuscationData.put(obfuscationType, iMapping.transform(null));
/*     */     } 
/* 518 */     return (ObfuscationData)obfuscationData;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */