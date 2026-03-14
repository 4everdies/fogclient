/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.Implements;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.gen.Accessor;
/*     */ import org.spongepowered.asm.mixin.gen.Invoker;
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
/*     */ @SupportedAnnotationTypes({"org.spongepowered.asm.mixin.Mixin", "org.spongepowered.asm.mixin.Shadow", "org.spongepowered.asm.mixin.Overwrite", "org.spongepowered.asm.mixin.gen.Accessor", "org.spongepowered.asm.mixin.Implements"})
/*     */ public class MixinObfuscationProcessorTargets
/*     */   extends MixinObfuscationProcessor
/*     */ {
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*  66 */     if (paramRoundEnvironment.processingOver()) {
/*  67 */       postProcess(paramRoundEnvironment);
/*  68 */       return true;
/*     */     } 
/*     */     
/*  71 */     processMixins(paramRoundEnvironment);
/*  72 */     processShadows(paramRoundEnvironment);
/*  73 */     processOverwrites(paramRoundEnvironment);
/*  74 */     processAccessors(paramRoundEnvironment);
/*  75 */     processInvokers(paramRoundEnvironment);
/*  76 */     processImplements(paramRoundEnvironment);
/*  77 */     postProcess(paramRoundEnvironment);
/*     */     
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void postProcess(RoundEnvironment paramRoundEnvironment) {
/*  84 */     super.postProcess(paramRoundEnvironment);
/*     */     
/*     */     try {
/*  87 */       this.mixins.writeReferences();
/*  88 */       this.mixins.writeMappings();
/*  89 */     } catch (Exception exception) {
/*  90 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processShadows(RoundEnvironment paramRoundEnvironment) {
/*  99 */     for (Element element1 : paramRoundEnvironment.getElementsAnnotatedWith((Class)Shadow.class)) {
/* 100 */       Element element2 = element1.getEnclosingElement();
/* 101 */       if (!(element2 instanceof TypeElement)) {
/* 102 */         this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(element2), element1);
/*     */         
/*     */         continue;
/*     */       } 
/* 106 */       AnnotationHandle annotationHandle = AnnotationHandle.of(element1, Shadow.class);
/*     */       
/* 108 */       if (element1.getKind() == ElementKind.FIELD) {
/* 109 */         this.mixins.registerShadow((TypeElement)element2, (VariableElement)element1, annotationHandle); continue;
/* 110 */       }  if (element1.getKind() == ElementKind.METHOD) {
/* 111 */         this.mixins.registerShadow((TypeElement)element2, (ExecutableElement)element1, annotationHandle); continue;
/*     */       } 
/* 113 */       this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method or field", element1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processOverwrites(RoundEnvironment paramRoundEnvironment) {
/* 123 */     for (Element element1 : paramRoundEnvironment.getElementsAnnotatedWith((Class)Overwrite.class)) {
/* 124 */       Element element2 = element1.getEnclosingElement();
/* 125 */       if (!(element2 instanceof TypeElement)) {
/* 126 */         this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(element2), element1);
/*     */         
/*     */         continue;
/*     */       } 
/* 130 */       if (element1.getKind() == ElementKind.METHOD) {
/* 131 */         this.mixins.registerOverwrite((TypeElement)element2, (ExecutableElement)element1); continue;
/*     */       } 
/* 133 */       this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", element1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processAccessors(RoundEnvironment paramRoundEnvironment) {
/* 143 */     for (Element element1 : paramRoundEnvironment.getElementsAnnotatedWith((Class)Accessor.class)) {
/* 144 */       Element element2 = element1.getEnclosingElement();
/* 145 */       if (!(element2 instanceof TypeElement)) {
/* 146 */         this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(element2), element1);
/*     */         
/*     */         continue;
/*     */       } 
/* 150 */       if (element1.getKind() == ElementKind.METHOD) {
/* 151 */         this.mixins.registerAccessor((TypeElement)element2, (ExecutableElement)element1); continue;
/*     */       } 
/* 153 */       this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", element1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInvokers(RoundEnvironment paramRoundEnvironment) {
/* 163 */     for (Element element1 : paramRoundEnvironment.getElementsAnnotatedWith((Class)Invoker.class)) {
/* 164 */       Element element2 = element1.getEnclosingElement();
/* 165 */       if (!(element2 instanceof TypeElement)) {
/* 166 */         this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(element2), element1);
/*     */         
/*     */         continue;
/*     */       } 
/* 170 */       if (element1.getKind() == ElementKind.METHOD) {
/* 171 */         this.mixins.registerInvoker((TypeElement)element2, (ExecutableElement)element1); continue;
/*     */       } 
/* 173 */       this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", element1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processImplements(RoundEnvironment paramRoundEnvironment) {
/* 183 */     for (Element element : paramRoundEnvironment.getElementsAnnotatedWith((Class)Implements.class)) {
/* 184 */       if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE) {
/* 185 */         AnnotationHandle annotationHandle = AnnotationHandle.of(element, Implements.class);
/* 186 */         this.mixins.registerSoftImplements((TypeElement)element, annotationHandle); continue;
/*     */       } 
/* 188 */       this.mixins.printMessage(Diagnostic.Kind.ERROR, "Found an @Implements annotation on an element which is not a class or interface", element);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\MixinObfuscationProcessorTargets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */