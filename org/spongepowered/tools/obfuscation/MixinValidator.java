/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MixinValidator
/*     */   implements IMixinValidator
/*     */ {
/*     */   protected final ProcessingEnvironment processingEnv;
/*     */   protected final Messager messager;
/*     */   protected final IOptionProvider options;
/*     */   protected final IMixinValidator.ValidationPass pass;
/*     */   
/*     */   public MixinValidator(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, IMixinValidator.ValidationPass paramValidationPass) {
/*  75 */     this.processingEnv = paramIMixinAnnotationProcessor.getProcessingEnvironment();
/*  76 */     this.messager = (Messager)paramIMixinAnnotationProcessor;
/*  77 */     this.options = (IOptionProvider)paramIMixinAnnotationProcessor;
/*  78 */     this.pass = paramValidationPass;
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
/*     */   public final boolean validate(IMixinValidator.ValidationPass paramValidationPass, TypeElement paramTypeElement, AnnotationHandle paramAnnotationHandle, Collection<TypeHandle> paramCollection) {
/*  90 */     if (paramValidationPass != this.pass) {
/*  91 */       return true;
/*     */     }
/*     */     
/*  94 */     return validate(paramTypeElement, paramAnnotationHandle, paramCollection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean validate(TypeElement paramTypeElement, AnnotationHandle paramAnnotationHandle, Collection<TypeHandle> paramCollection);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void note(String paramString, Element paramElement) {
/* 106 */     this.messager.printMessage(Diagnostic.Kind.NOTE, paramString, paramElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void warning(String paramString, Element paramElement) {
/* 116 */     this.messager.printMessage(Diagnostic.Kind.WARNING, paramString, paramElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void error(String paramString, Element paramElement) {
/* 126 */     this.messager.printMessage(Diagnostic.Kind.ERROR, paramString, paramElement);
/*     */   }
/*     */   
/*     */   protected final Collection<TypeMirror> getMixinsTargeting(TypeMirror paramTypeMirror) {
/* 130 */     return AnnotatedMixins.getMixinsForEnvironment(this.processingEnv).getMixinsTargeting(paramTypeMirror);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\MixinValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */