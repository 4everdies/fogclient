/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeHandleSimulated
/*     */   extends TypeHandle
/*     */ {
/*     */   private final TypeElement simulatedType;
/*     */   
/*     */   public TypeHandleSimulated(String paramString, TypeMirror paramTypeMirror) {
/*  52 */     this(TypeUtils.getPackage(paramTypeMirror), paramString, paramTypeMirror);
/*     */   }
/*     */   
/*     */   public TypeHandleSimulated(PackageElement paramPackageElement, String paramString, TypeMirror paramTypeMirror) {
/*  56 */     super(paramPackageElement, paramString);
/*  57 */     this.simulatedType = (TypeElement)((DeclaredType)paramTypeMirror).asElement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeElement getTargetElement() {
/*  66 */     return this.simulatedType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImaginary() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimulated() {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationHandle getAnnotation(Class<? extends Annotation> paramClass) {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getSuperclass() {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String findDescriptor(MemberInfo paramMemberInfo) {
/* 122 */     return (paramMemberInfo != null) ? paramMemberInfo.desc : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldHandle findField(String paramString1, String paramString2, boolean paramBoolean) {
/* 131 */     return new FieldHandle(null, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodHandle findMethod(String paramString1, String paramString2, boolean paramBoolean) {
/* 141 */     return new MethodHandle(null, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod getMappingMethod(String paramString1, String paramString2) {
/* 151 */     String str1 = (new SignaturePrinter(paramString1, paramString2)).setFullyQualified(true).toDescriptor();
/* 152 */     String str2 = TypeUtils.stripGenerics(str1);
/*     */ 
/*     */     
/* 155 */     MethodHandle methodHandle = findMethodRecursive(this, paramString1, str1, str2, true);
/*     */ 
/*     */     
/* 158 */     return (methodHandle != null) ? methodHandle.asMapping(true) : super.getMappingMethod(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MethodHandle findMethodRecursive(TypeHandle paramTypeHandle, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 163 */     TypeElement typeElement = paramTypeHandle.getTargetElement();
/* 164 */     if (typeElement == null) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     MethodHandle methodHandle = TypeHandle.findMethod(paramTypeHandle, paramString1, paramString2, paramString3, paramBoolean);
/* 169 */     if (methodHandle != null) {
/* 170 */       return methodHandle;
/*     */     }
/*     */     
/* 173 */     for (TypeMirror typeMirror1 : typeElement.getInterfaces()) {
/* 174 */       methodHandle = findMethodRecursive(typeMirror1, paramString1, paramString2, paramString3, paramBoolean);
/* 175 */       if (methodHandle != null) {
/* 176 */         return methodHandle;
/*     */       }
/*     */     } 
/*     */     
/* 180 */     TypeMirror typeMirror = typeElement.getSuperclass();
/* 181 */     if (typeMirror == null || typeMirror.getKind() == TypeKind.NONE) {
/* 182 */       return null;
/*     */     }
/*     */     
/* 185 */     return findMethodRecursive(typeMirror, paramString1, paramString2, paramString3, paramBoolean);
/*     */   }
/*     */   
/*     */   private static MethodHandle findMethodRecursive(TypeMirror paramTypeMirror, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 189 */     if (!(paramTypeMirror instanceof DeclaredType)) {
/* 190 */       return null;
/*     */     }
/* 192 */     TypeElement typeElement = (TypeElement)((DeclaredType)paramTypeMirror).asElement();
/* 193 */     return findMethodRecursive(new TypeHandle(typeElement), paramString1, paramString2, paramString3, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\TypeHandleSimulated.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */