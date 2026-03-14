/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeUtils
/*     */ {
/*     */   private static final int MAX_GENERIC_RECURSION_DEPTH = 5;
/*     */   private static final String OBJECT_SIG = "java.lang.Object";
/*     */   private static final String OBJECT_REF = "java/lang/Object";
/*     */   
/*     */   public static PackageElement getPackage(TypeMirror paramTypeMirror) {
/*  67 */     if (!(paramTypeMirror instanceof DeclaredType)) {
/*  68 */       return null;
/*     */     }
/*  70 */     return getPackage((TypeElement)((DeclaredType)paramTypeMirror).asElement());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PackageElement getPackage(TypeElement paramTypeElement) {
/*  79 */     Element element = paramTypeElement.getEnclosingElement();
/*  80 */     while (element != null && !(element instanceof PackageElement)) {
/*  81 */       element = element.getEnclosingElement();
/*     */     }
/*  83 */     return (PackageElement)element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getElementType(Element paramElement) {
/*  94 */     if (paramElement instanceof TypeElement)
/*  95 */       return "TypeElement"; 
/*  96 */     if (paramElement instanceof ExecutableElement)
/*  97 */       return "ExecutableElement"; 
/*  98 */     if (paramElement instanceof VariableElement)
/*  99 */       return "VariableElement"; 
/* 100 */     if (paramElement instanceof PackageElement)
/* 101 */       return "PackageElement"; 
/* 102 */     if (paramElement instanceof javax.lang.model.element.TypeParameterElement) {
/* 103 */       return "TypeParameterElement";
/*     */     }
/*     */     
/* 106 */     return paramElement.getClass().getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stripGenerics(String paramString) {
/* 116 */     StringBuilder stringBuilder = new StringBuilder();
/* 117 */     for (byte b1 = 0, b2 = 0; b1 < paramString.length(); b1++) {
/* 118 */       char c = paramString.charAt(b1);
/* 119 */       if (c == '<') {
/* 120 */         b2++;
/*     */       }
/* 122 */       if (b2 == 0) {
/* 123 */         stringBuilder.append(c);
/* 124 */       } else if (c == '>') {
/* 125 */         b2--;
/*     */       } 
/*     */     } 
/* 128 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(VariableElement paramVariableElement) {
/* 138 */     return (paramVariableElement != null) ? paramVariableElement.getSimpleName().toString() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(ExecutableElement paramExecutableElement) {
/* 148 */     return (paramExecutableElement != null) ? paramExecutableElement.getSimpleName().toString() : null;
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
/*     */   public static String getJavaSignature(Element paramElement) {
/* 161 */     if (paramElement instanceof ExecutableElement) {
/* 162 */       ExecutableElement executableElement = (ExecutableElement)paramElement;
/* 163 */       StringBuilder stringBuilder = (new StringBuilder()).append("(");
/* 164 */       boolean bool = false;
/* 165 */       for (VariableElement variableElement : executableElement.getParameters()) {
/* 166 */         if (bool) {
/* 167 */           stringBuilder.append(',');
/*     */         }
/* 169 */         stringBuilder.append(getTypeName(variableElement.asType()));
/* 170 */         bool = true;
/*     */       } 
/* 172 */       stringBuilder.append(')').append(getTypeName(executableElement.getReturnType()));
/* 173 */       return stringBuilder.toString();
/*     */     } 
/* 175 */     return getTypeName(paramElement.asType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaSignature(String paramString) {
/* 185 */     return (new SignaturePrinter("", paramString)).setFullyQualified(true).toDescriptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTypeName(TypeMirror paramTypeMirror) {
/* 195 */     switch (paramTypeMirror.getKind()) { case PUBLIC:
/* 196 */         return getTypeName(((ArrayType)paramTypeMirror).getComponentType()) + "[]";
/* 197 */       case PROTECTED: return getTypeName((DeclaredType)paramTypeMirror);
/* 198 */       case PRIVATE: return getTypeName(getUpperBound(paramTypeMirror));
/* 199 */       case null: return "java.lang.Object"; }
/* 200 */      return paramTypeMirror.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTypeName(DeclaredType paramDeclaredType) {
/* 211 */     if (paramDeclaredType == null) {
/* 212 */       return "java.lang.Object";
/*     */     }
/* 214 */     return getInternalName((TypeElement)paramDeclaredType.asElement()).replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescriptor(Element paramElement) {
/* 224 */     if (paramElement instanceof ExecutableElement)
/* 225 */       return getDescriptor((ExecutableElement)paramElement); 
/* 226 */     if (paramElement instanceof VariableElement) {
/* 227 */       return getInternalName((VariableElement)paramElement);
/*     */     }
/*     */     
/* 230 */     return getInternalName(paramElement.asType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescriptor(ExecutableElement paramExecutableElement) {
/* 240 */     if (paramExecutableElement == null) {
/* 241 */       return null;
/*     */     }
/*     */     
/* 244 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/* 246 */     for (VariableElement variableElement : paramExecutableElement.getParameters()) {
/* 247 */       stringBuilder.append(getInternalName(variableElement));
/*     */     }
/*     */     
/* 250 */     String str = getInternalName(paramExecutableElement.getReturnType());
/* 251 */     return String.format("(%s)%s", new Object[] { stringBuilder, str });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInternalName(VariableElement paramVariableElement) {
/* 261 */     if (paramVariableElement == null) {
/* 262 */       return null;
/*     */     }
/* 264 */     return getInternalName(paramVariableElement.asType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInternalName(TypeMirror paramTypeMirror) {
/* 274 */     switch (paramTypeMirror.getKind()) { case PUBLIC:
/* 275 */         return "[" + getInternalName(((ArrayType)paramTypeMirror).getComponentType());
/* 276 */       case PROTECTED: return "L" + getInternalName((DeclaredType)paramTypeMirror) + ";";
/* 277 */       case PRIVATE: return "L" + getInternalName(getUpperBound(paramTypeMirror)) + ";";
/* 278 */       case null: return "Z";
/* 279 */       case null: return "B";
/* 280 */       case null: return "C";
/* 281 */       case null: return "D";
/* 282 */       case null: return "F";
/* 283 */       case null: return "I";
/* 284 */       case null: return "J";
/* 285 */       case null: return "S";
/* 286 */       case null: return "V";
/*     */       case null:
/* 288 */         return "Ljava/lang/Object;"; }
/*     */ 
/*     */ 
/*     */     
/* 292 */     throw new IllegalArgumentException("Unable to parse type symbol " + paramTypeMirror + " with " + paramTypeMirror.getKind() + " to equivalent bytecode type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInternalName(DeclaredType paramDeclaredType) {
/* 302 */     if (paramDeclaredType == null) {
/* 303 */       return "java/lang/Object";
/*     */     }
/* 305 */     return getInternalName((TypeElement)paramDeclaredType.asElement());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInternalName(TypeElement paramTypeElement) {
/* 315 */     if (paramTypeElement == null) {
/* 316 */       return null;
/*     */     }
/* 318 */     StringBuilder stringBuilder = new StringBuilder();
/* 319 */     stringBuilder.append(paramTypeElement.getSimpleName());
/* 320 */     Element element = paramTypeElement.getEnclosingElement();
/* 321 */     while (element != null) {
/* 322 */       if (element instanceof TypeElement) {
/* 323 */         stringBuilder.insert(0, "$").insert(0, element.getSimpleName());
/* 324 */       } else if (element instanceof PackageElement) {
/* 325 */         stringBuilder.insert(0, "/").insert(0, ((PackageElement)element).getQualifiedName().toString().replace('.', '/'));
/*     */       } 
/* 327 */       element = element.getEnclosingElement();
/*     */     } 
/* 329 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private static DeclaredType getUpperBound(TypeMirror paramTypeMirror) {
/*     */     try {
/* 334 */       return getUpperBound0(paramTypeMirror, 5);
/* 335 */     } catch (IllegalStateException illegalStateException) {
/* 336 */       throw new IllegalArgumentException("Type symbol \"" + paramTypeMirror + "\" is too complex", illegalStateException);
/* 337 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 338 */       throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + paramTypeMirror, illegalArgumentException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DeclaredType getUpperBound0(TypeMirror paramTypeMirror, int paramInt) {
/* 343 */     if (paramInt == 0) {
/* 344 */       throw new IllegalStateException("Generic symbol \"" + paramTypeMirror + "\" is too complex, exceeded " + '\005' + " iterations attempting to determine upper bound");
/*     */     }
/*     */     
/* 347 */     if (paramTypeMirror instanceof DeclaredType) {
/* 348 */       return (DeclaredType)paramTypeMirror;
/*     */     }
/* 350 */     if (paramTypeMirror instanceof TypeVariable) {
/*     */       try {
/* 352 */         TypeMirror typeMirror = ((TypeVariable)paramTypeMirror).getUpperBound();
/* 353 */         return getUpperBound0(typeMirror, --paramInt);
/* 354 */       } catch (IllegalStateException illegalStateException) {
/* 355 */         throw illegalStateException;
/* 356 */       } catch (IllegalArgumentException illegalArgumentException) {
/* 357 */         throw illegalArgumentException;
/* 358 */       } catch (Exception exception) {
/* 359 */         throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + paramTypeMirror);
/*     */       } 
/*     */     }
/* 362 */     return null;
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
/*     */   public static boolean isAssignable(ProcessingEnvironment paramProcessingEnvironment, TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 374 */     boolean bool = paramProcessingEnvironment.getTypeUtils().isAssignable(paramTypeMirror1, paramTypeMirror2);
/* 375 */     if (!bool && paramTypeMirror1 instanceof DeclaredType && paramTypeMirror2 instanceof DeclaredType) {
/* 376 */       TypeMirror typeMirror1 = toRawType(paramProcessingEnvironment, (DeclaredType)paramTypeMirror1);
/* 377 */       TypeMirror typeMirror2 = toRawType(paramProcessingEnvironment, (DeclaredType)paramTypeMirror2);
/* 378 */       return paramProcessingEnvironment.getTypeUtils().isAssignable(typeMirror1, typeMirror2);
/*     */     } 
/*     */     
/* 381 */     return bool;
/*     */   }
/*     */   
/*     */   private static TypeMirror toRawType(ProcessingEnvironment paramProcessingEnvironment, DeclaredType paramDeclaredType) {
/* 385 */     return paramProcessingEnvironment.getElementUtils().getTypeElement(((TypeElement)paramDeclaredType.asElement()).getQualifiedName()).asType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Visibility getVisibility(Element paramElement) {
/* 395 */     if (paramElement == null) {
/* 396 */       return null;
/*     */     }
/*     */     
/* 399 */     for (Modifier modifier : paramElement.getModifiers()) {
/* 400 */       switch (modifier) { case PUBLIC:
/* 401 */           return Visibility.PUBLIC;
/* 402 */         case PROTECTED: return Visibility.PROTECTED;
/* 403 */         case PRIVATE: return Visibility.PRIVATE; }
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 408 */     return Visibility.PACKAGE;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\TypeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */