/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotationHandle
/*     */ {
/*  47 */   public static final AnnotationHandle MISSING = new AnnotationHandle(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotationMirror annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AnnotationHandle(AnnotationMirror paramAnnotationMirror) {
/*  60 */     this.annotation = paramAnnotationMirror;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationMirror asMirror() {
/*  69 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  79 */     return (this.annotation != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     if (this.annotation == null) {
/*  85 */       return "@{UnknownAnnotation}";
/*     */     }
/*  87 */     return "@" + this.annotation.getAnnotationType().asElement().getSimpleName();
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
/*     */   public <T> T getValue(String paramString, T paramT) {
/* 101 */     if (this.annotation == null) {
/* 102 */       return paramT;
/*     */     }
/*     */     
/* 105 */     AnnotationValue annotationValue = getAnnotationValue(paramString);
/* 106 */     if (paramT instanceof Enum && annotationValue != null) {
/* 107 */       VariableElement variableElement = (VariableElement)annotationValue.getValue();
/* 108 */       if (variableElement == null) {
/* 109 */         return paramT;
/*     */       }
/* 111 */       return (T)Enum.valueOf(paramT.getClass(), variableElement.getSimpleName().toString());
/*     */     } 
/*     */     
/* 114 */     return (annotationValue != null) ? (T)annotationValue.getValue() : paramT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue() {
/* 124 */     return getValue("value", null);
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
/*     */   public <T> T getValue(String paramString) {
/* 136 */     return getValue(paramString, null);
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
/*     */   public boolean getBoolean(String paramString, boolean paramBoolean) {
/* 148 */     return ((Boolean)getValue(paramString, Boolean.valueOf(paramBoolean))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationHandle getAnnotation(String paramString) {
/* 158 */     AnnotationMirror annotationMirror = (AnnotationMirror)getValue(paramString);
/* 159 */     if (annotationMirror instanceof AnnotationMirror)
/* 160 */       return of(annotationMirror); 
/* 161 */     if (annotationMirror instanceof AnnotationValue) {
/* 162 */       Object object = ((AnnotationValue)annotationMirror).getValue();
/* 163 */       if (object instanceof AnnotationMirror) {
/* 164 */         return of((AnnotationMirror)object);
/*     */       }
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<T> getList() {
/* 178 */     return getList("value");
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
/*     */   public <T> List<T> getList(String paramString) {
/* 191 */     List<AnnotationValue> list = getValue(paramString, Collections.emptyList());
/* 192 */     return unwrapAnnotationValueList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AnnotationHandle> getAnnotationList(String paramString) {
/* 202 */     AnnotationMirror annotationMirror = (AnnotationMirror)getValue(paramString, null);
/* 203 */     if (annotationMirror == null) {
/* 204 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */     
/* 208 */     if (annotationMirror instanceof AnnotationMirror) {
/* 209 */       return (List<AnnotationHandle>)ImmutableList.of(of(annotationMirror));
/*     */     }
/*     */     
/* 212 */     List list = (List)annotationMirror;
/* 213 */     ArrayList<AnnotationHandle> arrayList = new ArrayList(list.size());
/* 214 */     for (AnnotationValue annotationValue : list) {
/* 215 */       arrayList.add(new AnnotationHandle((AnnotationMirror)annotationValue.getValue()));
/*     */     }
/* 217 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */   
/*     */   protected AnnotationValue getAnnotationValue(String paramString) {
/* 221 */     for (ExecutableElement executableElement : this.annotation.getElementValues().keySet()) {
/* 222 */       if (executableElement.getSimpleName().contentEquals(paramString)) {
/* 223 */         return this.annotation.getElementValues().get(executableElement);
/*     */       }
/*     */     } 
/*     */     
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <T> List<T> unwrapAnnotationValueList(List<AnnotationValue> paramList) {
/* 232 */     if (paramList == null) {
/* 233 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 236 */     ArrayList<Object> arrayList = new ArrayList(paramList.size());
/* 237 */     for (AnnotationValue annotationValue : paramList) {
/* 238 */       arrayList.add(annotationValue.getValue());
/*     */     }
/*     */     
/* 241 */     return arrayList;
/*     */   }
/*     */   
/*     */   protected static AnnotationMirror getAnnotation(Element paramElement, Class<? extends Annotation> paramClass) {
/* 245 */     if (paramElement == null) {
/* 246 */       return null;
/*     */     }
/*     */     
/* 249 */     List<? extends AnnotationMirror> list = paramElement.getAnnotationMirrors();
/*     */     
/* 251 */     if (list == null) {
/* 252 */       return null;
/*     */     }
/*     */     
/* 255 */     for (AnnotationMirror annotationMirror : list) {
/* 256 */       Element element = annotationMirror.getAnnotationType().asElement();
/* 257 */       if (!(element instanceof TypeElement)) {
/*     */         continue;
/*     */       }
/* 260 */       TypeElement typeElement = (TypeElement)element;
/* 261 */       if (typeElement.getQualifiedName().contentEquals(paramClass.getName())) {
/* 262 */         return annotationMirror;
/*     */       }
/*     */     } 
/*     */     
/* 266 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotationHandle of(AnnotationMirror paramAnnotationMirror) {
/* 276 */     return new AnnotationHandle(paramAnnotationMirror);
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
/*     */   public static AnnotationHandle of(Element paramElement, Class<? extends Annotation> paramClass) {
/* 289 */     return new AnnotationHandle(getAnnotation(paramElement, paramClass));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\AnnotationHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */