/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeHandle
/*     */ {
/*     */   private final String name;
/*     */   private final PackageElement pkg;
/*     */   private final TypeElement element;
/*     */   private TypeReference reference;
/*     */   
/*     */   public TypeHandle(PackageElement paramPackageElement, String paramString) {
/*  85 */     this.name = paramString.replace('.', '/');
/*  86 */     this.pkg = paramPackageElement;
/*  87 */     this.element = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle(TypeElement paramTypeElement) {
/*  96 */     this.pkg = TypeUtils.getPackage(paramTypeElement);
/*  97 */     this.name = TypeUtils.getInternalName(paramTypeElement);
/*  98 */     this.element = paramTypeElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle(DeclaredType paramDeclaredType) {
/* 107 */     this((TypeElement)paramDeclaredType.asElement());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 115 */     return this.name.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 122 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PackageElement getPackage() {
/* 129 */     return this.pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeElement getElement() {
/* 136 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeElement getTargetElement() {
/* 144 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationHandle getAnnotation(Class<? extends Annotation> paramClass) {
/* 155 */     return AnnotationHandle.of(getTargetElement(), paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<? extends Element> getEnclosedElements() {
/* 162 */     return getEnclosedElements(getTargetElement());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Element> List<T> getEnclosedElements(ElementKind... paramVarArgs) {
/* 172 */     return getEnclosedElements(getTargetElement(), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeMirror getType() {
/* 180 */     return (getTargetElement() != null) ? getTargetElement().asType() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getSuperclass() {
/* 188 */     TypeElement typeElement = getTargetElement();
/* 189 */     if (typeElement == null) {
/* 190 */       return null;
/*     */     }
/*     */     
/* 193 */     TypeMirror typeMirror = typeElement.getSuperclass();
/* 194 */     if (typeMirror == null || typeMirror.getKind() == TypeKind.NONE) {
/* 195 */       return null;
/*     */     }
/*     */     
/* 198 */     return new TypeHandle((DeclaredType)typeMirror);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TypeHandle> getInterfaces() {
/* 205 */     if (getTargetElement() == null) {
/* 206 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 209 */     ImmutableList.Builder builder = ImmutableList.builder();
/* 210 */     for (TypeMirror typeMirror : getTargetElement().getInterfaces()) {
/* 211 */       builder.add(new TypeHandle((DeclaredType)typeMirror));
/*     */     }
/*     */     
/* 214 */     return (List<TypeHandle>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/* 221 */     return (getTargetElement() != null && getTargetElement().getModifiers().contains(Modifier.PUBLIC));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImaginary() {
/* 228 */     return (getTargetElement() == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimulated() {
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeReference getReference() {
/* 242 */     if (this.reference == null) {
/* 243 */       this.reference = new TypeReference(this);
/*     */     }
/* 245 */     return this.reference;
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
/*     */   public MappingMethod getMappingMethod(String paramString1, String paramString2) {
/* 258 */     return (MappingMethod)new ResolvableMappingMethod(this, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String findDescriptor(MemberInfo paramMemberInfo) {
/* 268 */     String str = paramMemberInfo.desc;
/* 269 */     if (str == null) {
/* 270 */       for (ExecutableElement executableElement : getEnclosedElements(new ElementKind[] { ElementKind.METHOD })) {
/* 271 */         if (executableElement.getSimpleName().toString().equals(paramMemberInfo.name)) {
/* 272 */           str = TypeUtils.getDescriptor(executableElement);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 277 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldHandle findField(VariableElement paramVariableElement) {
/* 288 */     return findField(paramVariableElement, true);
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
/*     */   public final FieldHandle findField(VariableElement paramVariableElement, boolean paramBoolean) {
/* 300 */     return findField(paramVariableElement.getSimpleName().toString(), TypeUtils.getTypeName(paramVariableElement.asType()), paramBoolean);
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
/*     */   public final FieldHandle findField(String paramString1, String paramString2) {
/* 312 */     return findField(paramString1, paramString2, true);
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
/*     */   public FieldHandle findField(String paramString1, String paramString2, boolean paramBoolean) {
/* 325 */     String str = TypeUtils.stripGenerics(paramString2);
/*     */     
/* 327 */     for (VariableElement variableElement : getEnclosedElements(new ElementKind[] { ElementKind.FIELD })) {
/* 328 */       if (compareElement(variableElement, paramString1, paramString2, paramBoolean))
/* 329 */         return new FieldHandle(getTargetElement(), variableElement); 
/* 330 */       if (compareElement(variableElement, paramString1, str, paramBoolean)) {
/* 331 */         return new FieldHandle(getTargetElement(), variableElement, true);
/*     */       }
/*     */     } 
/*     */     
/* 335 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MethodHandle findMethod(ExecutableElement paramExecutableElement) {
/* 346 */     return findMethod(paramExecutableElement, true);
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
/*     */   public final MethodHandle findMethod(ExecutableElement paramExecutableElement, boolean paramBoolean) {
/* 358 */     return findMethod(paramExecutableElement.getSimpleName().toString(), TypeUtils.getJavaSignature(paramExecutableElement), paramBoolean);
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
/*     */   public final MethodHandle findMethod(String paramString1, String paramString2) {
/* 370 */     return findMethod(paramString1, paramString2, true);
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
/*     */   public MethodHandle findMethod(String paramString1, String paramString2, boolean paramBoolean) {
/* 383 */     String str = TypeUtils.stripGenerics(paramString2);
/* 384 */     return findMethod(this, paramString1, paramString2, str, paramBoolean);
/*     */   }
/*     */   
/*     */   protected static MethodHandle findMethod(TypeHandle paramTypeHandle, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 388 */     for (ExecutableElement executableElement : getEnclosedElements(paramTypeHandle.getTargetElement(), new ElementKind[] { ElementKind.CONSTRUCTOR, ElementKind.METHOD })) {
/*     */       
/* 390 */       if (compareElement(executableElement, paramString1, paramString2, paramBoolean) || compareElement(executableElement, paramString1, paramString3, paramBoolean)) {
/* 391 */         return new MethodHandle(paramTypeHandle, executableElement);
/*     */       }
/*     */     } 
/* 394 */     return null;
/*     */   }
/*     */   
/*     */   protected static boolean compareElement(Element paramElement, String paramString1, String paramString2, boolean paramBoolean) {
/*     */     try {
/* 399 */       String str1 = paramElement.getSimpleName().toString();
/* 400 */       String str2 = TypeUtils.getJavaSignature(paramElement);
/* 401 */       String str3 = TypeUtils.stripGenerics(str2);
/* 402 */       boolean bool = paramBoolean ? paramString1.equals(str1) : paramString1.equalsIgnoreCase(str1);
/* 403 */       return (bool && (paramString2.length() == 0 || paramString2.equals(str2) || paramString2.equals(str3)));
/* 404 */     } catch (NullPointerException nullPointerException) {
/* 405 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <T extends Element> List<T> getEnclosedElements(TypeElement paramTypeElement, ElementKind... paramVarArgs) {
/* 411 */     if (paramVarArgs == null || paramVarArgs.length < 1) {
/* 412 */       return (List)getEnclosedElements(paramTypeElement);
/*     */     }
/*     */     
/* 415 */     if (paramTypeElement == null) {
/* 416 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 419 */     ImmutableList.Builder builder = ImmutableList.builder();
/* 420 */     for (Element element : paramTypeElement.getEnclosedElements()) {
/* 421 */       for (ElementKind elementKind : paramVarArgs) {
/* 422 */         if (element.getKind() == elementKind) {
/* 423 */           builder.add(element);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 429 */     return (List<T>)builder.build();
/*     */   }
/*     */   
/*     */   protected static List<? extends Element> getEnclosedElements(TypeElement paramTypeElement) {
/* 433 */     return (paramTypeElement != null) ? paramTypeElement.getEnclosedElements() : Collections.<Element>emptyList();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\TypeHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */