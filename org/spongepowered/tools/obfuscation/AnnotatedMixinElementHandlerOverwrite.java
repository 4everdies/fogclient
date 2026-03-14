/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnnotatedMixinElementHandlerOverwrite
/*     */   extends AnnotatedMixinElementHandler
/*     */ {
/*     */   static class AnnotatedElementOverwrite
/*     */     extends AnnotatedMixinElementHandler.AnnotatedElement<ExecutableElement>
/*     */   {
/*     */     private final boolean shouldRemap;
/*     */     
/*     */     public AnnotatedElementOverwrite(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle, boolean param1Boolean) {
/*  51 */       super(param1ExecutableElement, param1AnnotationHandle);
/*  52 */       this.shouldRemap = param1Boolean;
/*     */     }
/*     */     
/*     */     public boolean shouldRemap() {
/*  56 */       return this.shouldRemap;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   AnnotatedMixinElementHandlerOverwrite(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/*  62 */     super(paramIMixinAnnotationProcessor, paramAnnotatedMixin);
/*     */   }
/*     */   
/*     */   public void registerMerge(ExecutableElement paramExecutableElement) {
/*  66 */     validateTargetMethod(paramExecutableElement, null, new AnnotatedMixinElementHandler.AliasedElementName(paramExecutableElement, AnnotationHandle.MISSING), "overwrite", true, true);
/*     */   }
/*     */   
/*     */   public void registerOverwrite(AnnotatedElementOverwrite paramAnnotatedElementOverwrite) {
/*  70 */     AnnotatedMixinElementHandler.AliasedElementName aliasedElementName = new AnnotatedMixinElementHandler.AliasedElementName(paramAnnotatedElementOverwrite.getElement(), paramAnnotatedElementOverwrite.getAnnotation());
/*  71 */     validateTargetMethod(paramAnnotatedElementOverwrite.getElement(), paramAnnotatedElementOverwrite.getAnnotation(), aliasedElementName, "@Overwrite", true, false);
/*  72 */     checkConstraints(paramAnnotatedElementOverwrite.getElement(), paramAnnotatedElementOverwrite.getAnnotation());
/*     */     
/*  74 */     if (paramAnnotatedElementOverwrite.shouldRemap()) {
/*  75 */       for (TypeHandle typeHandle : this.mixin.getTargets()) {
/*  76 */         if (!registerOverwriteForTarget(paramAnnotatedElementOverwrite, typeHandle)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  82 */     if (!"true".equalsIgnoreCase(this.ap.getOption("disableOverwriteChecker"))) {
/*  83 */       Diagnostic.Kind kind = "error".equalsIgnoreCase(this.ap.getOption("overwriteErrorLevel")) ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
/*     */ 
/*     */       
/*  86 */       String str = this.ap.getJavadocProvider().getJavadoc(paramAnnotatedElementOverwrite.getElement());
/*  87 */       if (str == null) {
/*  88 */         this.ap.printMessage(kind, "@Overwrite is missing javadoc comment", paramAnnotatedElementOverwrite.getElement());
/*     */         
/*     */         return;
/*     */       } 
/*  92 */       if (!str.toLowerCase().contains("@author")) {
/*  93 */         this.ap.printMessage(kind, "@Overwrite is missing an @author tag", paramAnnotatedElementOverwrite.getElement());
/*     */       }
/*     */       
/*  96 */       if (!str.toLowerCase().contains("@reason")) {
/*  97 */         this.ap.printMessage(kind, "@Overwrite is missing an @reason tag", paramAnnotatedElementOverwrite.getElement());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean registerOverwriteForTarget(AnnotatedElementOverwrite paramAnnotatedElementOverwrite, TypeHandle paramTypeHandle) {
/* 103 */     MappingMethod mappingMethod = paramTypeHandle.getMappingMethod(paramAnnotatedElementOverwrite.getSimpleName(), paramAnnotatedElementOverwrite.getDesc());
/* 104 */     ObfuscationData<MappingMethod> obfuscationData = this.obf.getDataProvider().getObfMethod(mappingMethod);
/*     */     
/* 106 */     if (obfuscationData.isEmpty()) {
/* 107 */       Diagnostic.Kind kind = Diagnostic.Kind.ERROR;
/*     */ 
/*     */       
/*     */       try {
/* 111 */         Method method = paramAnnotatedElementOverwrite.getElement().getClass().getMethod("isStatic", new Class[0]);
/* 112 */         if (((Boolean)method.invoke(paramAnnotatedElementOverwrite.getElement(), new Object[0])).booleanValue()) {
/* 113 */           kind = Diagnostic.Kind.WARNING;
/*     */         }
/* 115 */       } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 119 */       this.ap.printMessage(kind, "No obfuscation mapping for @Overwrite method", paramAnnotatedElementOverwrite.getElement());
/* 120 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 124 */       addMethodMappings(paramAnnotatedElementOverwrite.getSimpleName(), paramAnnotatedElementOverwrite.getDesc(), obfuscationData);
/* 125 */     } catch (MappingConflictException mappingConflictException) {
/* 126 */       paramAnnotatedElementOverwrite.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Overwrite method: " + mappingConflictException.getNew().getSimpleName() + " for target " + paramTypeHandle + " conflicts with existing mapping " + mappingConflictException
/* 127 */           .getOld().getSimpleName());
/* 128 */       return false;
/*     */     } 
/* 130 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandlerOverwrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */