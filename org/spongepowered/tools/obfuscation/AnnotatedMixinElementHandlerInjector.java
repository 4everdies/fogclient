/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.injection.Coerce;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InvalidMemberDescriptorException;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
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
/*     */ class AnnotatedMixinElementHandlerInjector
/*     */   extends AnnotatedMixinElementHandler
/*     */ {
/*     */   static class AnnotatedElementInjector
/*     */     extends AnnotatedMixinElementHandler.AnnotatedElement<ExecutableElement>
/*     */   {
/*     */     private final InjectorRemap state;
/*     */     
/*     */     public AnnotatedElementInjector(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle, InjectorRemap param1InjectorRemap) {
/*  64 */       super(param1ExecutableElement, param1AnnotationHandle);
/*  65 */       this.state = param1InjectorRemap;
/*     */     }
/*     */     
/*     */     public boolean shouldRemap() {
/*  69 */       return this.state.shouldRemap();
/*     */     }
/*     */     
/*     */     public boolean hasCoerceArgument() {
/*  73 */       if (!this.annotation.toString().equals("@Inject")) {
/*  74 */         return false;
/*     */       }
/*     */       
/*  77 */       Iterator<? extends VariableElement> iterator = this.element.getParameters().iterator(); if (iterator.hasNext()) { VariableElement variableElement = iterator.next();
/*  78 */         return AnnotationHandle.of(variableElement, Coerce.class).exists(); }
/*     */ 
/*     */       
/*  81 */       return false;
/*     */     }
/*     */     
/*     */     public void addMessage(Diagnostic.Kind param1Kind, CharSequence param1CharSequence, Element param1Element, AnnotationHandle param1AnnotationHandle) {
/*  85 */       this.state.addMessage(param1Kind, param1CharSequence, param1Element, param1AnnotationHandle);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  90 */       return getAnnotation().toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class AnnotatedElementInjectionPoint
/*     */     extends AnnotatedMixinElementHandler.AnnotatedElement<ExecutableElement>
/*     */   {
/*     */     private final AnnotationHandle at;
/*     */     
/*     */     private Map<String, String> args;
/*     */     
/*     */     private final InjectorRemap state;
/*     */ 
/*     */     
/*     */     public AnnotatedElementInjectionPoint(ExecutableElement param1ExecutableElement, AnnotationHandle param1AnnotationHandle1, AnnotationHandle param1AnnotationHandle2, InjectorRemap param1InjectorRemap) {
/* 107 */       super(param1ExecutableElement, param1AnnotationHandle1);
/* 108 */       this.at = param1AnnotationHandle2;
/* 109 */       this.state = param1InjectorRemap;
/*     */     }
/*     */     
/*     */     public boolean shouldRemap() {
/* 113 */       return this.at.getBoolean("remap", this.state.shouldRemap());
/*     */     }
/*     */     
/*     */     public AnnotationHandle getAt() {
/* 117 */       return this.at;
/*     */     }
/*     */     
/*     */     public String getAtArg(String param1String) {
/* 121 */       if (this.args == null) {
/* 122 */         this.args = new HashMap<String, String>();
/* 123 */         for (String str : this.at.getList("args")) {
/* 124 */           if (str == null) {
/*     */             continue;
/*     */           }
/* 127 */           int i = str.indexOf('=');
/* 128 */           if (i > -1) {
/* 129 */             this.args.put(str.substring(0, i), str.substring(i + 1)); continue;
/*     */           } 
/* 131 */           this.args.put(str, "");
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 136 */       return this.args.get(param1String);
/*     */     }
/*     */     
/*     */     public void notifyRemapped() {
/* 140 */       this.state.notifyRemapped();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   AnnotatedMixinElementHandlerInjector(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, AnnotatedMixin paramAnnotatedMixin) {
/* 146 */     super(paramIMixinAnnotationProcessor, paramAnnotatedMixin);
/*     */   }
/*     */   
/*     */   public void registerInjector(AnnotatedElementInjector paramAnnotatedElementInjector) {
/* 150 */     if (this.mixin.isInterface()) {
/* 151 */       this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", paramAnnotatedElementInjector.getElement());
/*     */     }
/*     */     
/* 154 */     for (String str : paramAnnotatedElementInjector.getAnnotation().getList("method")) {
/* 155 */       MemberInfo memberInfo = MemberInfo.parse(str);
/* 156 */       if (memberInfo.name == null) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 161 */         memberInfo.validate();
/* 162 */       } catch (InvalidMemberDescriptorException invalidMemberDescriptorException) {
/* 163 */         paramAnnotatedElementInjector.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, invalidMemberDescriptorException.getMessage());
/*     */       } 
/*     */       
/* 166 */       if (memberInfo.desc != null) {
/* 167 */         validateReferencedTarget(paramAnnotatedElementInjector.getElement(), paramAnnotatedElementInjector.getAnnotation(), memberInfo, paramAnnotatedElementInjector.toString());
/*     */       }
/*     */       
/* 170 */       if (!paramAnnotatedElementInjector.shouldRemap()) {
/*     */         continue;
/*     */       }
/*     */       
/* 174 */       for (TypeHandle typeHandle : this.mixin.getTargets()) {
/* 175 */         if (!registerInjector(paramAnnotatedElementInjector, str, memberInfo, typeHandle)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean registerInjector(AnnotatedElementInjector paramAnnotatedElementInjector, String paramString, MemberInfo paramMemberInfo, TypeHandle paramTypeHandle) {
/* 183 */     String str1 = paramTypeHandle.findDescriptor(paramMemberInfo);
/* 184 */     if (str1 == null) {
/* 185 */       Diagnostic.Kind kind = this.mixin.isMultiTarget() ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
/* 186 */       if (paramTypeHandle.isSimulated()) {
/* 187 */         paramAnnotatedElementInjector.printMessage((Messager)this.ap, Diagnostic.Kind.NOTE, paramAnnotatedElementInjector + " target '" + paramString + "' in @Pseudo mixin will not be obfuscated");
/* 188 */       } else if (paramTypeHandle.isImaginary()) {
/* 189 */         paramAnnotatedElementInjector.printMessage((Messager)this.ap, kind, paramAnnotatedElementInjector + " target requires method signature because enclosing type information for " + paramTypeHandle + " is unavailable");
/*     */       }
/* 191 */       else if (!paramMemberInfo.isInitialiser()) {
/* 192 */         paramAnnotatedElementInjector.printMessage((Messager)this.ap, kind, "Unable to determine signature for " + paramAnnotatedElementInjector + " target method");
/*     */       } 
/* 194 */       return true;
/*     */     } 
/*     */     
/* 197 */     String str2 = paramAnnotatedElementInjector + " target " + paramMemberInfo.name;
/* 198 */     MappingMethod mappingMethod = paramTypeHandle.getMappingMethod(paramMemberInfo.name, str1);
/* 199 */     ObfuscationData<IMapping> obfuscationData = this.obf.getDataProvider().getObfMethod(mappingMethod);
/* 200 */     if (obfuscationData.isEmpty()) {
/* 201 */       if (paramTypeHandle.isSimulated())
/* 202 */       { obfuscationData = this.obf.getDataProvider().getRemappedMethod(mappingMethod); }
/* 203 */       else { if (paramMemberInfo.isClassInitialiser()) {
/* 204 */           return true;
/*     */         }
/* 206 */         Diagnostic.Kind kind = paramMemberInfo.isConstructor() ? Diagnostic.Kind.WARNING : Diagnostic.Kind.ERROR;
/* 207 */         paramAnnotatedElementInjector.addMessage(kind, "No obfuscation mapping for " + str2, paramAnnotatedElementInjector.getElement(), paramAnnotatedElementInjector.getAnnotation());
/* 208 */         return false; }
/*     */     
/*     */     }
/*     */     
/* 212 */     IReferenceManager iReferenceManager = this.obf.getReferenceManager();
/*     */     
/*     */     try {
/* 215 */       if ((paramMemberInfo.owner == null && this.mixin.isMultiTarget()) || paramTypeHandle.isSimulated()) {
/* 216 */         obfuscationData = AnnotatedMixinElementHandler.stripOwnerData(obfuscationData);
/*     */       }
/* 218 */       iReferenceManager.addMethodMapping(this.classRef, paramString, obfuscationData);
/* 219 */     } catch (ReferenceConflictException referenceConflictException) {
/* 220 */       String str = this.mixin.isMultiTarget() ? "Multi-target" : "Target";
/*     */       
/* 222 */       if (paramAnnotatedElementInjector.hasCoerceArgument() && paramMemberInfo.owner == null && paramMemberInfo.desc == null) {
/* 223 */         MemberInfo memberInfo1 = MemberInfo.parse(referenceConflictException.getOld());
/* 224 */         MemberInfo memberInfo2 = MemberInfo.parse(referenceConflictException.getNew());
/* 225 */         if (memberInfo1.name.equals(memberInfo2.name)) {
/* 226 */           obfuscationData = AnnotatedMixinElementHandler.stripDescriptors(obfuscationData);
/* 227 */           iReferenceManager.setAllowConflicts(true);
/* 228 */           iReferenceManager.addMethodMapping(this.classRef, paramString, obfuscationData);
/* 229 */           iReferenceManager.setAllowConflicts(false);
/*     */ 
/*     */           
/* 232 */           paramAnnotatedElementInjector.printMessage((Messager)this.ap, Diagnostic.Kind.WARNING, "Coerced " + str + " reference has conflicting descriptors for " + str2 + ": Storing bare references " + obfuscationData
/* 233 */               .values() + " in refMap");
/* 234 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 238 */       paramAnnotatedElementInjector.printMessage((Messager)this.ap, Diagnostic.Kind.ERROR, str + " reference conflict for " + str2 + ": " + paramString + " -> " + referenceConflictException
/* 239 */           .getNew() + " previously defined as " + referenceConflictException.getOld());
/*     */     } 
/*     */     
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerInjectionPoint(AnnotatedElementInjectionPoint paramAnnotatedElementInjectionPoint, String paramString) {
/* 250 */     if (this.mixin.isInterface()) {
/* 251 */       this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", paramAnnotatedElementInjectionPoint.getElement());
/*     */     }
/*     */     
/* 254 */     if (!paramAnnotatedElementInjectionPoint.shouldRemap()) {
/*     */       return;
/*     */     }
/*     */     
/* 258 */     String str1 = InjectionPointData.parseType((String)paramAnnotatedElementInjectionPoint.getAt().getValue("value"));
/* 259 */     String str2 = (String)paramAnnotatedElementInjectionPoint.getAt().getValue("target");
/*     */     
/* 261 */     if ("NEW".equals(str1)) {
/* 262 */       remapNewTarget(String.format(paramString, new Object[] { str1 + ".<target>" }), str2, paramAnnotatedElementInjectionPoint);
/* 263 */       remapNewTarget(String.format(paramString, new Object[] { str1 + ".args[class]" }), paramAnnotatedElementInjectionPoint.getAtArg("class"), paramAnnotatedElementInjectionPoint);
/*     */     } else {
/* 265 */       remapReference(String.format(paramString, new Object[] { str1 + ".<target>" }), str2, paramAnnotatedElementInjectionPoint);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void remapNewTarget(String paramString1, String paramString2, AnnotatedElementInjectionPoint paramAnnotatedElementInjectionPoint) {
/* 270 */     if (paramString2 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 274 */     MemberInfo memberInfo = MemberInfo.parse(paramString2);
/* 275 */     String str = memberInfo.toCtorType();
/*     */     
/* 277 */     if (str != null) {
/* 278 */       String str1 = memberInfo.toCtorDesc();
/* 279 */       MappingMethod mappingMethod = new MappingMethod(str, ".", (str1 != null) ? str1 : "()V");
/* 280 */       ObfuscationData<MappingMethod> obfuscationData = this.obf.getDataProvider().getRemappedMethod(mappingMethod);
/* 281 */       if (obfuscationData.isEmpty()) {
/* 282 */         this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find class mapping for " + paramString1 + " '" + str + "'", paramAnnotatedElementInjectionPoint.getElement(), paramAnnotatedElementInjectionPoint
/* 283 */             .getAnnotation().asMirror());
/*     */         
/*     */         return;
/*     */       } 
/* 287 */       ObfuscationData<String> obfuscationData1 = new ObfuscationData();
/* 288 */       for (ObfuscationType obfuscationType : obfuscationData) {
/* 289 */         MappingMethod mappingMethod1 = obfuscationData.get(obfuscationType);
/* 290 */         if (str1 == null) {
/* 291 */           obfuscationData1.put(obfuscationType, mappingMethod1.getOwner()); continue;
/*     */         } 
/* 293 */         obfuscationData1.put(obfuscationType, mappingMethod1.getDesc().replace(")V", ")L" + mappingMethod1.getOwner() + ";"));
/*     */       } 
/*     */ 
/*     */       
/* 297 */       this.obf.getReferenceManager().addClassMapping(this.classRef, paramString2, obfuscationData1);
/*     */     } 
/*     */     
/* 300 */     paramAnnotatedElementInjectionPoint.notifyRemapped();
/*     */   }
/*     */   
/*     */   protected final void remapReference(String paramString1, String paramString2, AnnotatedElementInjectionPoint paramAnnotatedElementInjectionPoint) {
/* 304 */     if (paramString2 == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 309 */     AnnotationMirror annotationMirror = ((this.ap.getCompilerEnvironment() == IMixinAnnotationProcessor.CompilerEnvironment.JDT) ? paramAnnotatedElementInjectionPoint.getAt() : paramAnnotatedElementInjectionPoint.getAnnotation()).asMirror();
/*     */     
/* 311 */     MemberInfo memberInfo = MemberInfo.parse(paramString2);
/* 312 */     if (!memberInfo.isFullyQualified()) {
/* 313 */       String str = (memberInfo.owner == null) ? ((memberInfo.desc == null) ? "owner and signature" : "owner") : "signature";
/* 314 */       this.ap.printMessage(Diagnostic.Kind.ERROR, paramString1 + " is not fully qualified, missing " + str, paramAnnotatedElementInjectionPoint.getElement(), annotationMirror);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 319 */       memberInfo.validate();
/* 320 */     } catch (InvalidMemberDescriptorException invalidMemberDescriptorException) {
/* 321 */       this.ap.printMessage(Diagnostic.Kind.ERROR, invalidMemberDescriptorException.getMessage(), paramAnnotatedElementInjectionPoint.getElement(), annotationMirror);
/*     */     } 
/*     */     
/*     */     try {
/* 325 */       if (memberInfo.isField()) {
/* 326 */         ObfuscationData obfuscationData = this.obf.getDataProvider().getObfFieldRecursive(memberInfo);
/* 327 */         if (obfuscationData.isEmpty()) {
/* 328 */           this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find field mapping for " + paramString1 + " '" + paramString2 + "'", paramAnnotatedElementInjectionPoint.getElement(), annotationMirror);
/*     */           
/*     */           return;
/*     */         } 
/* 332 */         this.obf.getReferenceManager().addFieldMapping(this.classRef, paramString2, memberInfo, obfuscationData);
/*     */       } else {
/* 334 */         ObfuscationData obfuscationData = this.obf.getDataProvider().getObfMethodRecursive(memberInfo);
/* 335 */         if (obfuscationData.isEmpty() && (
/* 336 */           memberInfo.owner == null || !memberInfo.owner.startsWith("java/lang/"))) {
/* 337 */           this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find method mapping for " + paramString1 + " '" + paramString2 + "'", paramAnnotatedElementInjectionPoint.getElement(), annotationMirror);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 342 */         this.obf.getReferenceManager().addMethodMapping(this.classRef, paramString2, memberInfo, obfuscationData);
/*     */       } 
/* 344 */     } catch (ReferenceConflictException referenceConflictException) {
/*     */ 
/*     */       
/* 347 */       this.ap.printMessage(Diagnostic.Kind.ERROR, "Unexpected reference conflict for " + paramString1 + ": " + paramString2 + " -> " + referenceConflictException
/* 348 */           .getNew() + " previously defined as " + referenceConflictException.getOld(), paramAnnotatedElementInjectionPoint.getElement(), annotationMirror);
/*     */       
/*     */       return;
/*     */     } 
/* 352 */     paramAnnotatedElementInjectionPoint.notifyRemapped();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixinElementHandlerInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */