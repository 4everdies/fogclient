/*     */ package org.spongepowered.tools.obfuscation.validation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import org.spongepowered.asm.mixin.gen.Accessor;
/*     */ import org.spongepowered.asm.mixin.gen.Invoker;
/*     */ import org.spongepowered.tools.obfuscation.MixinValidator;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TargetValidator
/*     */   extends MixinValidator
/*     */ {
/*     */   public TargetValidator(IMixinAnnotationProcessor paramIMixinAnnotationProcessor) {
/*  56 */     super(paramIMixinAnnotationProcessor, IMixinValidator.ValidationPass.LATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validate(TypeElement paramTypeElement, AnnotationHandle paramAnnotationHandle, Collection<TypeHandle> paramCollection) {
/*  67 */     if ("true".equalsIgnoreCase(this.options.getOption("disableTargetValidator"))) {
/*  68 */       return true;
/*     */     }
/*     */     
/*  71 */     if (paramTypeElement.getKind() == ElementKind.INTERFACE) {
/*  72 */       validateInterfaceMixin(paramTypeElement, paramCollection);
/*     */     } else {
/*  74 */       validateClassMixin(paramTypeElement, paramCollection);
/*     */     } 
/*     */     
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   private void validateInterfaceMixin(TypeElement paramTypeElement, Collection<TypeHandle> paramCollection) {
/*  81 */     int i = 0;
/*  82 */     for (Element element : paramTypeElement.getEnclosedElements()) {
/*  83 */       if (element.getKind() == ElementKind.METHOD) {
/*  84 */         boolean bool1 = AnnotationHandle.of(element, Accessor.class).exists();
/*  85 */         boolean bool2 = AnnotationHandle.of(element, Invoker.class).exists();
/*  86 */         i |= (!bool1 && !bool2) ? 1 : 0;
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     if (i == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  94 */     for (TypeHandle typeHandle : paramCollection) {
/*  95 */       TypeElement typeElement = typeHandle.getElement();
/*  96 */       if (typeElement != null && typeElement.getKind() != ElementKind.INTERFACE) {
/*  97 */         error("Targetted type '" + typeHandle + " of " + paramTypeElement + " is not an interface", paramTypeElement);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateClassMixin(TypeElement paramTypeElement, Collection<TypeHandle> paramCollection) {
/* 103 */     TypeMirror typeMirror = paramTypeElement.getSuperclass();
/*     */     
/* 105 */     for (TypeHandle typeHandle : paramCollection) {
/* 106 */       TypeMirror typeMirror1 = typeHandle.getType();
/* 107 */       if (typeMirror1 != null && !validateSuperClass(typeMirror1, typeMirror)) {
/* 108 */         error("Superclass " + typeMirror + " of " + paramTypeElement + " was not found in the hierarchy of target class " + typeMirror1, paramTypeElement);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean validateSuperClass(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 114 */     if (TypeUtils.isAssignable(this.processingEnv, paramTypeMirror1, paramTypeMirror2)) {
/* 115 */       return true;
/*     */     }
/*     */     
/* 118 */     return validateSuperClassRecursive(paramTypeMirror1, paramTypeMirror2);
/*     */   }
/*     */   
/*     */   private boolean validateSuperClassRecursive(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 122 */     if (!(paramTypeMirror1 instanceof DeclaredType)) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     if (TypeUtils.isAssignable(this.processingEnv, paramTypeMirror1, paramTypeMirror2)) {
/* 127 */       return true;
/*     */     }
/*     */     
/* 130 */     TypeElement typeElement = (TypeElement)((DeclaredType)paramTypeMirror1).asElement();
/* 131 */     TypeMirror typeMirror = typeElement.getSuperclass();
/* 132 */     if (typeMirror.getKind() == TypeKind.NONE) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     if (checkMixinsFor(typeMirror, paramTypeMirror2)) {
/* 137 */       return true;
/*     */     }
/*     */     
/* 140 */     return validateSuperClassRecursive(typeMirror, paramTypeMirror2);
/*     */   }
/*     */   
/*     */   private boolean checkMixinsFor(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 144 */     for (TypeMirror typeMirror : getMixinsTargeting(paramTypeMirror1)) {
/* 145 */       if (TypeUtils.isAssignable(this.processingEnv, typeMirror, paramTypeMirror2)) {
/* 146 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\validation\TargetValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */