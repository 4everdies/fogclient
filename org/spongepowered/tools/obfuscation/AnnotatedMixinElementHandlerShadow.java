/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
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
/*     */ class AnnotatedMixinElementHandlerShadow
/*     */   extends AnnotatedMixinElementHandler
/*     */ {
/*     */   static abstract class AnnotatedElementShadow<E extends Element, M extends IMapping<M>>
/*     */     extends AnnotatedMixinElementHandler.AnnotatedElement<E>
/*     */   {
/*     */     private final boolean shouldRemap;
/*     */     private final AnnotatedMixinElementHandler.ShadowElementName name;
/*     */     private final IMapping.Type type;
/*     */     
/*     */     protected AnnotatedElementShadow(E param1E, AnnotationHandle param1AnnotationHandle, boolean param1Boolean, IMapping.Type param1Type) {
/*  62 */       super(param1E, param1AnnotationHandle);
/*  63 */       this.shouldRemap = param1Boolean;
/*  64 */       this.name = new AnnotatedMixinElementHandler.ShadowElementName((Element)param1E, param1AnnotationHandle);
/*  65 */       this.type = param1Type;
/*     */     }
/*     */     
/*     */     public boolean shouldRemap() {
/*  69 */       return this.shouldRemap;
/*     */     }
/*     */     
/*     */     public AnnotatedMixinElementHandler.ShadowElementName getName() {
/*  73 */       return this.name;
/*     */     }
/*     */     
/*     */     public IMapping.Type getElementType() {
/*  77 */       return this.type;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  82 */       return getElementType().name().toLowerCase();
/*     */     }
/*     */     
/*     */     public AnnotatedMixinElementHandler.ShadowElementName setObfuscatedName(IMapping<?> param1IMapping) {
/*  86 */       return setObfuscatedName(param1IMapping.getSimpleName());
/*     */     }
/*     */     
/*     */     public AnnotatedMixinElementHandler.ShadowElementName setObfuscatedName(String param1String) {
/*  90 */       return getName().setObfuscatedName(param1String);
/*     */     }
/*     */     
/*     */     public ObfuscationData<M> getObfuscationData(IObfuscationDataProvider param1IObfuscationDataProvider, TypeHandle param1TypeHandle) {
/*  94 */       return param1IObfuscationDataProvider.getObfEntry((IMapping)getMapping(param1TypeHandle, getName().toString(), getDesc()));
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract M getMapping(TypeHandle param1TypeHandle, String param1String1, String param1String2);
/*     */ 
/*     */     
/*     */     public abstract void addMapping(ObfuscationType param1ObfuscationType, IMapping<?> param1IMapping);
/*     */   }
/*     */ 
/*     */   
/*     */   class AnnotatedElementShadowField
/*     */     extends AnnotatedElementShadow<VariableElement, MappingField>
/*     */   {
/*     */     public AnnotatedElementShadowField(VariableElement param1VariableElement, AnnotationHandle param1AnnotationHandle, boolean param1Boolean) {
/* 109 */       super(param1VariableElement, param1AnnotationHandle, param1Boolean, IMapping.Type.FIELD);
/*     */     }
/*     */ 
/*     */     
/*     */     public MappingField getMapping(TypeHandle param1TypeHandle, String param1String1, String param1String2) {
/* 114 */       return new MappingField(param1TypeHandle.getName(), param1String1, param1String2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addMapping(ObfuscationType param1ObfuscationType, IMapping<?> param1IMapping) {
/* 119 */       AnnotatedMixinElementHandlerShadow.this.addFieldMapping(param1ObfuscationType, setObfuscatedName(param1IMapping), getDesc(), param1IMapping.getDesc());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class AnnotatedElementShadowMethod
/*     */     extends AnnotatedElementShadow<ExecutableElement, MappingMethod>
/*     */   {
/*     */     public AnnotatedElementShadowMethod(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle, boolean param1Boolean) {
/* 130 */       super(param1ExecutableElement, param1AnnotationHandle, param1Boolean, IMapping.Type.METHOD);
/*     */     }
/*     */ 
/*     */     
/*     */     public MappingMethod getMapping(TypeHandle param1TypeHandle, String param1String1, String param1String2) {
/* 135 */       return param1TypeHandle.getMappingMethod(param1String1, param1String2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addMapping(ObfuscationType param1ObfuscationType, IMapping<?> param1IMapping) {
/* 140 */       AnnotatedMixinElementHandlerShadow.this.addMethodMapping(param1ObfuscationType, setObfuscatedName(param1IMapping), getDesc(), param1IMapping.getDesc());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   AnnotatedMixinElementHandlerShadow(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/* 146 */     super(paramIMixinAnnotationProcessor, paramAnnotatedMixin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerShadow(AnnotatedElementShadow<?, ?> paramAnnotatedElementShadow) {
/* 153 */     validateTarget((Element)paramAnnotatedElementShadow.getElement(), paramAnnotatedElementShadow.getAnnotation(), paramAnnotatedElementShadow.getName(), "@Shadow");
/*     */     
/* 155 */     if (!paramAnnotatedElementShadow.shouldRemap()) {
/*     */       return;
/*     */     }
/*     */     
/* 159 */     for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 160 */       registerShadowForTarget(paramAnnotatedElementShadow, typeHandle);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerShadowForTarget(AnnotatedElementShadow<?, ?> paramAnnotatedElementShadow, TypeHandle paramTypeHandle) {
/* 165 */     ObfuscationData<?> obfuscationData = paramAnnotatedElementShadow.getObfuscationData(this.obf.getDataProvider(), paramTypeHandle);
/*     */     
/* 167 */     if (obfuscationData.isEmpty()) {
/* 168 */       String str = this.mixin.isMultiTarget() ? (" in target " + paramTypeHandle) : "";
/* 169 */       if (paramTypeHandle.isSimulated()) {
/* 170 */         paramAnnotatedElementShadow.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + str + " for @Shadow " + paramAnnotatedElementShadow);
/*     */       } else {
/* 172 */         paramAnnotatedElementShadow.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + str + " for @Shadow " + paramAnnotatedElementShadow);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 177 */     for (ObfuscationType obfuscationType : obfuscationData) {
/*     */       try {
/* 179 */         paramAnnotatedElementShadow.addMapping(obfuscationType, (IMapping)obfuscationData.get(obfuscationType));
/* 180 */       } catch (MappingConflictException mappingConflictException) {
/* 181 */         paramAnnotatedElementShadow.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Shadow " + paramAnnotatedElementShadow + ": " + mappingConflictException.getNew().getSimpleName() + " for target " + paramTypeHandle + " conflicts with existing mapping " + mappingConflictException
/* 182 */             .getOld().getSimpleName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandlerShadow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */