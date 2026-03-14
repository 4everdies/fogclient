/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyArgs;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyConstant;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"org.spongepowered.asm.mixin.injection.Inject", "org.spongepowered.asm.mixin.injection.ModifyArg", "org.spongepowered.asm.mixin.injection.ModifyArgs", "org.spongepowered.asm.mixin.injection.Redirect", "org.spongepowered.asm.mixin.injection.At"})
/*     */ public class MixinObfuscationProcessorInjection
/*     */   extends MixinObfuscationProcessor
/*     */ {
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*  68 */     if (paramRoundEnvironment.processingOver()) {
/*  69 */       postProcess(paramRoundEnvironment);
/*  70 */       return true;
/*     */     } 
/*     */     
/*  73 */     processMixins(paramRoundEnvironment);
/*  74 */     processInjectors(paramRoundEnvironment, (Class)Inject.class);
/*  75 */     processInjectors(paramRoundEnvironment, (Class)ModifyArg.class);
/*  76 */     processInjectors(paramRoundEnvironment, (Class)ModifyArgs.class);
/*  77 */     processInjectors(paramRoundEnvironment, (Class)Redirect.class);
/*  78 */     processInjectors(paramRoundEnvironment, (Class)ModifyVariable.class);
/*  79 */     processInjectors(paramRoundEnvironment, (Class)ModifyConstant.class);
/*  80 */     postProcess(paramRoundEnvironment);
/*     */     
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void postProcess(RoundEnvironment paramRoundEnvironment) {
/*  87 */     super.postProcess(paramRoundEnvironment);
/*     */     
/*     */     try {
/*  90 */       this.mixins.writeReferences();
/*  91 */     } catch (Exception exception) {
/*  92 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInjectors(RoundEnvironment paramRoundEnvironment, Class<? extends Annotation> paramClass) {
/* 101 */     for (Element element1 : paramRoundEnvironment.getElementsAnnotatedWith(paramClass)) {
/* 102 */       Element element2 = element1.getEnclosingElement();
/* 103 */       if (!(element2 instanceof TypeElement)) {
/* 104 */         throw new IllegalStateException("@" + paramClass.getSimpleName() + " element has unexpected parent with type " + 
/* 105 */             TypeUtils.getElementType(element2));
/*     */       }
/*     */       
/* 108 */       AnnotationHandle annotationHandle = AnnotationHandle.of(element1, paramClass);
/*     */       
/* 110 */       if (element1.getKind() == ElementKind.METHOD) {
/* 111 */         this.mixins.registerInjector((TypeElement)element2, (ExecutableElement)element1, annotationHandle); continue;
/*     */       } 
/* 113 */       this.mixins.printMessage(Diagnostic.Kind.WARNING, "Found an @" + paramClass
/* 114 */           .getSimpleName() + " annotation on an element which is not a method: " + element1.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\MixinObfuscationProcessorInjection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */