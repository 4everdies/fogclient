/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.Interface;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
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
/*     */ public class AnnotatedMixinElementHandlerSoftImplements
/*     */   extends AnnotatedMixinElementHandler
/*     */ {
/*     */   AnnotatedMixinElementHandlerSoftImplements(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/*  48 */     super(paramIMixinAnnotationProcessor, paramAnnotatedMixin);
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
/*     */   public void process(AnnotationHandle paramAnnotationHandle) {
/*  62 */     if (!this.mixin.remap()) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     List list = paramAnnotationHandle.getAnnotationList("value");
/*     */ 
/*     */     
/*  69 */     if (list.size() < 1) {
/*  70 */       this.ap.printMessage(Diagnostic.Kind.WARNING, "Empty @Implements annotation", this.mixin.getMixin(), paramAnnotationHandle.asMirror());
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     for (AnnotationHandle annotationHandle : list) {
/*  75 */       Interface.Remap remap = (Interface.Remap)annotationHandle.getValue("remap", Interface.Remap.ALL);
/*  76 */       if (remap == Interface.Remap.NONE) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/*  81 */         TypeHandle typeHandle = new TypeHandle((DeclaredType)annotationHandle.getValue("iface"));
/*  82 */         String str = (String)annotationHandle.getValue("prefix");
/*  83 */         processSoftImplements(remap, typeHandle, str);
/*  84 */       } catch (Exception exception) {
/*  85 */         this.ap.printMessage(Diagnostic.Kind.ERROR, "Unexpected error: " + exception.getClass().getName() + ": " + exception.getMessage(), this.mixin.getMixin(), annotationHandle
/*  86 */             .asMirror());
/*     */       } 
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
/*     */   private void processSoftImplements(Interface.Remap paramRemap, TypeHandle paramTypeHandle, String paramString) {
/* 100 */     for (ExecutableElement executableElement : paramTypeHandle.getEnclosedElements(new ElementKind[] { ElementKind.METHOD })) {
/* 101 */       processMethod(paramRemap, paramTypeHandle, paramString, executableElement);
/*     */     }
/*     */     
/* 104 */     for (TypeHandle typeHandle : paramTypeHandle.getInterfaces()) {
/* 105 */       processSoftImplements(paramRemap, typeHandle, paramString);
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
/*     */   private void processMethod(Interface.Remap paramRemap, TypeHandle paramTypeHandle, String paramString, ExecutableElement paramExecutableElement) {
/* 120 */     String str1 = paramExecutableElement.getSimpleName().toString();
/* 121 */     String str2 = TypeUtils.getJavaSignature(paramExecutableElement);
/* 122 */     String str3 = TypeUtils.getDescriptor(paramExecutableElement);
/*     */     
/* 124 */     if (paramRemap != Interface.Remap.ONLY_PREFIXED) {
/* 125 */       MethodHandle methodHandle = this.mixin.getHandle().findMethod(str1, str2);
/* 126 */       if (methodHandle != null) {
/* 127 */         addInterfaceMethodMapping(paramRemap, paramTypeHandle, (String)null, methodHandle, str1, str3);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (paramString != null) {
/* 132 */       MethodHandle methodHandle = this.mixin.getHandle().findMethod(paramString + str1, str2);
/* 133 */       if (methodHandle != null) {
/* 134 */         addInterfaceMethodMapping(paramRemap, paramTypeHandle, paramString, methodHandle, str1, str3);
/*     */       }
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
/*     */   private void addInterfaceMethodMapping(Interface.Remap paramRemap, TypeHandle paramTypeHandle, String paramString1, MethodHandle paramMethodHandle, String paramString2, String paramString3) {
/* 152 */     MappingMethod mappingMethod = new MappingMethod(paramTypeHandle.getName(), paramString2, paramString3);
/* 153 */     ObfuscationData<MappingMethod> obfuscationData = this.obf.getDataProvider().getObfMethod(mappingMethod);
/* 154 */     if (obfuscationData.isEmpty()) {
/* 155 */       if (paramRemap.forceRemap()) {
/* 156 */         this.ap.printMessage(Diagnostic.Kind.ERROR, "No obfuscation mapping for soft-implementing method", paramMethodHandle.getElement());
/*     */       }
/*     */       return;
/*     */     } 
/* 160 */     addMethodMappings(paramMethodHandle.getName(), paramString3, applyPrefix(obfuscationData, paramString1));
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
/*     */   private ObfuscationData<MappingMethod> applyPrefix(ObfuscationData<MappingMethod> paramObfuscationData, String paramString) {
/* 172 */     if (paramString == null) {
/* 173 */       return paramObfuscationData;
/*     */     }
/*     */     
/* 176 */     ObfuscationData<MappingMethod> obfuscationData = new ObfuscationData();
/* 177 */     for (ObfuscationType obfuscationType : paramObfuscationData) {
/* 178 */       MappingMethod mappingMethod = paramObfuscationData.get(obfuscationType);
/* 179 */       obfuscationData.put(obfuscationType, mappingMethod.addPrefix(paramString));
/*     */     } 
/* 181 */     return obfuscationData;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandlerSoftImplements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */