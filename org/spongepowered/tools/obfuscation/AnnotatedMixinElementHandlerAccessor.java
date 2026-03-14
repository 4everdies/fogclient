/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*     */ import org.spongepowered.asm.mixin.gen.AccessorInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.Extensions;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotatedMixinElementHandlerAccessor
/*     */   extends AnnotatedMixinElementHandler
/*     */   implements IMixinContext
/*     */ {
/*     */   static class AnnotatedElementAccessor
/*     */     extends AnnotatedMixinElementHandler.AnnotatedElement<ExecutableElement>
/*     */   {
/*     */     private final boolean shouldRemap;
/*     */     private final TypeMirror returnType;
/*     */     private String targetName;
/*     */     
/*     */     public AnnotatedElementAccessor(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle, boolean param1Boolean) {
/*  71 */       super(param1ExecutableElement, param1AnnotationHandle);
/*  72 */       this.shouldRemap = param1Boolean;
/*  73 */       this.returnType = getElement().getReturnType();
/*     */     }
/*     */     
/*     */     public boolean shouldRemap() {
/*  77 */       return this.shouldRemap;
/*     */     }
/*     */     
/*     */     public String getAnnotationValue() {
/*  81 */       return (String)getAnnotation().getValue();
/*     */     }
/*     */     
/*     */     public TypeMirror getTargetType() {
/*  85 */       switch (getAccessorType()) {
/*     */         case FIELD_GETTER:
/*  87 */           return this.returnType;
/*     */         case FIELD_SETTER:
/*  89 */           return ((VariableElement)getElement().getParameters().get(0)).asType();
/*     */       } 
/*  91 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTargetTypeName() {
/*  96 */       return TypeUtils.getTypeName(getTargetType());
/*     */     }
/*     */     
/*     */     public String getAccessorDesc() {
/* 100 */       return TypeUtils.getInternalName(getTargetType());
/*     */     }
/*     */     
/*     */     public MemberInfo getContext() {
/* 104 */       return new MemberInfo(getTargetName(), null, getAccessorDesc());
/*     */     }
/*     */     
/*     */     public AccessorInfo.AccessorType getAccessorType() {
/* 108 */       return (this.returnType.getKind() == TypeKind.VOID) ? AccessorInfo.AccessorType.FIELD_SETTER : AccessorInfo.AccessorType.FIELD_GETTER;
/*     */     }
/*     */     
/*     */     public void setTargetName(String param1String) {
/* 112 */       this.targetName = param1String;
/*     */     }
/*     */     
/*     */     public String getTargetName() {
/* 116 */       return this.targetName;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 121 */       return (this.targetName != null) ? this.targetName : "<invalid>";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class AnnotatedElementInvoker
/*     */     extends AnnotatedElementAccessor
/*     */   {
/*     */     public AnnotatedElementInvoker(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle, boolean param1Boolean) {
/* 131 */       super(param1ExecutableElement, param1AnnotationHandle, param1Boolean);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAccessorDesc() {
/* 136 */       return TypeUtils.getDescriptor(getElement());
/*     */     }
/*     */ 
/*     */     
/*     */     public AccessorInfo.AccessorType getAccessorType() {
/* 141 */       return AccessorInfo.AccessorType.METHOD_PROXY;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTargetTypeName() {
/* 146 */       return TypeUtils.getJavaSignature(getElement());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedMixinElementHandlerAccessor(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/* 152 */     super(paramIMixinAnnotationProcessor, paramAnnotatedMixin);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReferenceMapper getReferenceMapper() {
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 162 */     return this.mixin.getClassRef().replace('/', '.');
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassRef() {
/* 167 */     return this.mixin.getClassRef();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTargetClassRef() {
/* 172 */     throw new UnsupportedOperationException("Target class not available at compile time");
/*     */   }
/*     */ 
/*     */   
/*     */   public IMixinInfo getMixin() {
/* 177 */     throw new UnsupportedOperationException("MixinInfo not available at compile time");
/*     */   }
/*     */ 
/*     */   
/*     */   public Extensions getExtensions() {
/* 182 */     throw new UnsupportedOperationException("Mixin Extensions not available at compile time");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOption(MixinEnvironment.Option paramOption) {
/* 187 */     throw new UnsupportedOperationException("Options not available at compile time");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPriority() {
/* 192 */     throw new UnsupportedOperationException("Priority not available at compile time");
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getTargetMethod(MethodNode paramMethodNode) {
/* 197 */     throw new UnsupportedOperationException("Target not available at compile time");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAccessor(AnnotatedElementAccessor paramAnnotatedElementAccessor) {
/* 206 */     if (paramAnnotatedElementAccessor.getAccessorType() == null) {
/* 207 */       paramAnnotatedElementAccessor.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Unsupported accessor type");
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     String str = getAccessorTargetName(paramAnnotatedElementAccessor);
/* 212 */     if (str == null) {
/* 213 */       paramAnnotatedElementAccessor.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Cannot inflect accessor target name");
/*     */       return;
/*     */     } 
/* 216 */     paramAnnotatedElementAccessor.setTargetName(str);
/*     */     
/* 218 */     for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 219 */       if (paramAnnotatedElementAccessor.getAccessorType() == AccessorInfo.AccessorType.METHOD_PROXY) {
/* 220 */         registerInvokerForTarget((AnnotatedElementInvoker)paramAnnotatedElementAccessor, typeHandle); continue;
/*     */       } 
/* 222 */       registerAccessorForTarget(paramAnnotatedElementAccessor, typeHandle);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerAccessorForTarget(AnnotatedElementAccessor paramAnnotatedElementAccessor, TypeHandle paramTypeHandle) {
/* 228 */     FieldHandle fieldHandle = paramTypeHandle.findField(paramAnnotatedElementAccessor.getTargetName(), paramAnnotatedElementAccessor.getTargetTypeName(), false);
/* 229 */     if (fieldHandle == null) {
/* 230 */       if (!paramTypeHandle.isImaginary()) {
/* 231 */         paramAnnotatedElementAccessor.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Could not locate @Accessor target " + paramAnnotatedElementAccessor + " in target " + paramTypeHandle);
/*     */         
/*     */         return;
/*     */       } 
/* 235 */       fieldHandle = new FieldHandle(paramTypeHandle.getName(), paramAnnotatedElementAccessor.getTargetName(), paramAnnotatedElementAccessor.getDesc());
/*     */     } 
/*     */     
/* 238 */     if (!paramAnnotatedElementAccessor.shouldRemap()) {
/*     */       return;
/*     */     }
/*     */     
/* 242 */     ObfuscationData<IMapping> obfuscationData = this.obf.getDataProvider().getObfField(fieldHandle.asMapping(false).move(paramTypeHandle.getName()));
/* 243 */     if (obfuscationData.isEmpty()) {
/* 244 */       String str = this.mixin.isMultiTarget() ? (" in target " + paramTypeHandle) : "";
/* 245 */       paramAnnotatedElementAccessor.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + str + " for @Accessor target " + paramAnnotatedElementAccessor);
/*     */       
/*     */       return;
/*     */     } 
/* 249 */     obfuscationData = AnnotatedMixinElementHandler.stripOwnerData(obfuscationData);
/*     */     
/*     */     try {
/* 252 */       this.obf.getReferenceManager().addFieldMapping(this.mixin.getClassRef(), paramAnnotatedElementAccessor.getTargetName(), paramAnnotatedElementAccessor.getContext(), obfuscationData);
/* 253 */     } catch (ReferenceConflictException referenceConflictException) {
/* 254 */       paramAnnotatedElementAccessor.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Accessor target " + paramAnnotatedElementAccessor + ": " + referenceConflictException.getNew() + " for target " + paramTypeHandle + " conflicts with existing mapping " + referenceConflictException
/* 255 */           .getOld());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void registerInvokerForTarget(AnnotatedElementInvoker paramAnnotatedElementInvoker, TypeHandle paramTypeHandle) {
/* 260 */     MethodHandle methodHandle = paramTypeHandle.findMethod(paramAnnotatedElementInvoker.getTargetName(), paramAnnotatedElementInvoker.getTargetTypeName(), false);
/* 261 */     if (methodHandle == null) {
/* 262 */       if (!paramTypeHandle.isImaginary()) {
/* 263 */         paramAnnotatedElementInvoker.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Could not locate @Invoker target " + paramAnnotatedElementInvoker + " in target " + paramTypeHandle);
/*     */         
/*     */         return;
/*     */       } 
/* 267 */       methodHandle = new MethodHandle(paramTypeHandle, paramAnnotatedElementInvoker.getTargetName(), paramAnnotatedElementInvoker.getDesc());
/*     */     } 
/*     */     
/* 270 */     if (!paramAnnotatedElementInvoker.shouldRemap()) {
/*     */       return;
/*     */     }
/*     */     
/* 274 */     ObfuscationData<IMapping> obfuscationData = this.obf.getDataProvider().getObfMethod(methodHandle.asMapping(false).move(paramTypeHandle.getName()));
/* 275 */     if (obfuscationData.isEmpty()) {
/* 276 */       String str = this.mixin.isMultiTarget() ? (" in target " + paramTypeHandle) : "";
/* 277 */       paramAnnotatedElementInvoker.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + str + " for @Accessor target " + paramAnnotatedElementInvoker);
/*     */       
/*     */       return;
/*     */     } 
/* 281 */     obfuscationData = AnnotatedMixinElementHandler.stripOwnerData(obfuscationData);
/*     */     
/*     */     try {
/* 284 */       this.obf.getReferenceManager().addMethodMapping(this.mixin.getClassRef(), paramAnnotatedElementInvoker.getTargetName(), paramAnnotatedElementInvoker.getContext(), obfuscationData);
/* 285 */     } catch (ReferenceConflictException referenceConflictException) {
/* 286 */       paramAnnotatedElementInvoker.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Invoker target " + paramAnnotatedElementInvoker + ": " + referenceConflictException.getNew() + " for target " + paramTypeHandle + " conflicts with existing mapping " + referenceConflictException
/* 287 */           .getOld());
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getAccessorTargetName(AnnotatedElementAccessor paramAnnotatedElementAccessor) {
/* 292 */     String str = paramAnnotatedElementAccessor.getAnnotationValue();
/* 293 */     if (Strings.isNullOrEmpty(str)) {
/* 294 */       return inflectAccessorTarget(paramAnnotatedElementAccessor);
/*     */     }
/* 296 */     return str;
/*     */   }
/*     */   
/*     */   private String inflectAccessorTarget(AnnotatedElementAccessor paramAnnotatedElementAccessor) {
/* 300 */     return AccessorInfo.inflectTarget(paramAnnotatedElementAccessor.getSimpleName(), paramAnnotatedElementAccessor.getAccessorType(), "", this, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandlerAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */