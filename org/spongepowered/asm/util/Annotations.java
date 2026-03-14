/*     */ package org.spongepowered.asm.util;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Annotations
/*     */ {
/*     */   public static void setVisible(FieldNode paramFieldNode, Class<? extends Annotation> paramClass, Object... paramVarArgs) {
/*  61 */     AnnotationNode annotationNode = createNode(Type.getDescriptor(paramClass), paramVarArgs);
/*  62 */     paramFieldNode.visibleAnnotations = add(paramFieldNode.visibleAnnotations, annotationNode);
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
/*     */   public static void setInvisible(FieldNode paramFieldNode, Class<? extends Annotation> paramClass, Object... paramVarArgs) {
/*  74 */     AnnotationNode annotationNode = createNode(Type.getDescriptor(paramClass), paramVarArgs);
/*  75 */     paramFieldNode.invisibleAnnotations = add(paramFieldNode.invisibleAnnotations, annotationNode);
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
/*     */   public static void setVisible(MethodNode paramMethodNode, Class<? extends Annotation> paramClass, Object... paramVarArgs) {
/*  87 */     AnnotationNode annotationNode = createNode(Type.getDescriptor(paramClass), paramVarArgs);
/*  88 */     paramMethodNode.visibleAnnotations = add(paramMethodNode.visibleAnnotations, annotationNode);
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
/*     */   public static void setInvisible(MethodNode paramMethodNode, Class<? extends Annotation> paramClass, Object... paramVarArgs) {
/* 100 */     AnnotationNode annotationNode = createNode(Type.getDescriptor(paramClass), paramVarArgs);
/* 101 */     paramMethodNode.invisibleAnnotations = add(paramMethodNode.invisibleAnnotations, annotationNode);
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
/*     */   private static AnnotationNode createNode(String paramString, Object... paramVarArgs) {
/* 113 */     AnnotationNode annotationNode = new AnnotationNode(paramString);
/* 114 */     for (byte b = 0; b < paramVarArgs.length - 1; b += 2) {
/* 115 */       if (!(paramVarArgs[b] instanceof String)) {
/* 116 */         throw new IllegalArgumentException("Annotation keys must be strings, found " + paramVarArgs[b].getClass().getSimpleName() + " with " + paramVarArgs[b]
/* 117 */             .toString() + " at index " + b + " creating " + paramString);
/*     */       }
/* 119 */       annotationNode.visit((String)paramVarArgs[b], paramVarArgs[b + 1]);
/*     */     } 
/* 121 */     return annotationNode;
/*     */   }
/*     */   
/*     */   private static List<AnnotationNode> add(List<AnnotationNode> paramList, AnnotationNode paramAnnotationNode) {
/* 125 */     if (paramList == null) {
/* 126 */       paramList = new ArrayList<AnnotationNode>(1);
/*     */     } else {
/* 128 */       paramList.remove(get(paramList, paramAnnotationNode.desc));
/*     */     } 
/* 130 */     paramList.add(paramAnnotationNode);
/* 131 */     return paramList;
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
/*     */   public static AnnotationNode getVisible(FieldNode paramFieldNode, Class<? extends Annotation> paramClass) {
/* 143 */     return get(paramFieldNode.visibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getInvisible(FieldNode paramFieldNode, Class<? extends Annotation> paramClass) {
/* 155 */     return get(paramFieldNode.invisibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getVisible(MethodNode paramMethodNode, Class<? extends Annotation> paramClass) {
/* 167 */     return get(paramMethodNode.visibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getInvisible(MethodNode paramMethodNode, Class<? extends Annotation> paramClass) {
/* 179 */     return get(paramMethodNode.invisibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getSingleVisible(MethodNode paramMethodNode, Class<? extends Annotation>... paramVarArgs) {
/* 191 */     return getSingle(paramMethodNode.visibleAnnotations, paramVarArgs);
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
/*     */   public static AnnotationNode getSingleInvisible(MethodNode paramMethodNode, Class<? extends Annotation>... paramVarArgs) {
/* 203 */     return getSingle(paramMethodNode.invisibleAnnotations, paramVarArgs);
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
/*     */   public static AnnotationNode getVisible(ClassNode paramClassNode, Class<? extends Annotation> paramClass) {
/* 215 */     return get(paramClassNode.visibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getInvisible(ClassNode paramClassNode, Class<? extends Annotation> paramClass) {
/* 227 */     return get(paramClassNode.invisibleAnnotations, Type.getDescriptor(paramClass));
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
/*     */   public static AnnotationNode getVisibleParameter(MethodNode paramMethodNode, Class<? extends Annotation> paramClass, int paramInt) {
/* 240 */     return getParameter((List<AnnotationNode>[])paramMethodNode.visibleParameterAnnotations, Type.getDescriptor(paramClass), paramInt);
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
/*     */   public static AnnotationNode getInvisibleParameter(MethodNode paramMethodNode, Class<? extends Annotation> paramClass, int paramInt) {
/* 253 */     return getParameter((List<AnnotationNode>[])paramMethodNode.invisibleParameterAnnotations, Type.getDescriptor(paramClass), paramInt);
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
/*     */   public static AnnotationNode getParameter(List<AnnotationNode>[] paramArrayOfList, String paramString, int paramInt) {
/* 266 */     if (paramArrayOfList == null || paramInt < 0 || paramInt >= paramArrayOfList.length) {
/* 267 */       return null;
/*     */     }
/*     */     
/* 270 */     return get(paramArrayOfList[paramInt], paramString);
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
/*     */   public static AnnotationNode get(List<AnnotationNode> paramList, String paramString) {
/* 283 */     if (paramList == null) {
/* 284 */       return null;
/*     */     }
/*     */     
/* 287 */     for (AnnotationNode annotationNode : paramList) {
/* 288 */       if (paramString.equals(annotationNode.desc)) {
/* 289 */         return annotationNode;
/*     */       }
/*     */     } 
/*     */     
/* 293 */     return null;
/*     */   }
/*     */   
/*     */   private static AnnotationNode getSingle(List<AnnotationNode> paramList, Class<? extends Annotation>[] paramArrayOfClass) {
/* 297 */     ArrayList<AnnotationNode> arrayList = new ArrayList();
/* 298 */     for (Class<? extends Annotation> clazz : paramArrayOfClass) {
/* 299 */       AnnotationNode annotationNode = get(paramList, Type.getDescriptor(clazz));
/* 300 */       if (annotationNode != null) {
/* 301 */         arrayList.add(annotationNode);
/*     */       }
/*     */     } 
/*     */     
/* 305 */     int i = arrayList.size();
/* 306 */     if (i > 1) {
/* 307 */       throw new IllegalArgumentException("Conflicting annotations found: " + Lists.transform(arrayList, new Function<AnnotationNode, String>() {
/*     */               public String apply(AnnotationNode param1AnnotationNode) {
/* 309 */                 return param1AnnotationNode.desc;
/*     */               }
/*     */             }));
/*     */     }
/*     */     
/* 314 */     return (i == 0) ? null : arrayList.get(0);
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
/*     */   public static <T> T getValue(AnnotationNode paramAnnotationNode) {
/* 326 */     return getValue(paramAnnotationNode, "value");
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
/*     */   public static <T> T getValue(AnnotationNode paramAnnotationNode, String paramString, T paramT) {
/* 342 */     T t = (T)getValue(paramAnnotationNode, paramString);
/* 343 */     return (t != null) ? t : paramT;
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
/*     */   
/*     */   public static <T> T getValue(AnnotationNode paramAnnotationNode, String paramString, Class<?> paramClass) {
/* 360 */     Preconditions.checkNotNull(paramClass, "annotationClass cannot be null");
/* 361 */     Object object = getValue(paramAnnotationNode, paramString);
/* 362 */     if (object == null) {
/*     */       try {
/* 364 */         object = paramClass.getDeclaredMethod(paramString, new Class[0]).getDefaultValue();
/* 365 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */     
/* 369 */     return (T)object;
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
/*     */   public static <T> T getValue(AnnotationNode paramAnnotationNode, String paramString) {
/* 384 */     boolean bool = false;
/*     */     
/* 386 */     if (paramAnnotationNode == null || paramAnnotationNode.values == null) {
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 391 */     for (T t : paramAnnotationNode.values) {
/* 392 */       if (bool) {
/* 393 */         return t;
/*     */       }
/* 395 */       if (t.equals(paramString)) {
/* 396 */         bool = true;
/*     */       }
/*     */     } 
/*     */     
/* 400 */     return null;
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
/*     */   public static <T extends Enum<T>> T getValue(AnnotationNode paramAnnotationNode, String paramString, Class<T> paramClass, T paramT) {
/* 415 */     String[] arrayOfString = getValue(paramAnnotationNode, paramString);
/* 416 */     if (arrayOfString == null) {
/* 417 */       return paramT;
/*     */     }
/* 419 */     return toEnumValue(paramClass, arrayOfString);
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
/*     */   public static <T> List<T> getValue(AnnotationNode paramAnnotationNode, String paramString, boolean paramBoolean) {
/* 433 */     List list = (List)getValue(paramAnnotationNode, paramString);
/* 434 */     if (list instanceof List)
/* 435 */       return list; 
/* 436 */     if (list != null) {
/* 437 */       ArrayList<List> arrayList = new ArrayList();
/* 438 */       arrayList.add(list);
/* 439 */       return (List)arrayList;
/*     */     } 
/* 441 */     return Collections.emptyList();
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
/*     */   public static <T extends Enum<T>> List<T> getValue(AnnotationNode paramAnnotationNode, String paramString, boolean paramBoolean, Class<T> paramClass) {
/* 456 */     List<T> list = (List<T>)getValue(paramAnnotationNode, paramString);
/* 457 */     if (list instanceof List) {
/* 458 */       for (ListIterator<String[]> listIterator = ((List)list).listIterator(); listIterator.hasNext();) {
/* 459 */         listIterator.set(toEnumValue(paramClass, listIterator.next()));
/*     */       }
/* 461 */       return list;
/* 462 */     }  if (list instanceof String[]) {
/* 463 */       ArrayList<T> arrayList = new ArrayList();
/* 464 */       arrayList.add(toEnumValue(paramClass, (String[])list));
/* 465 */       return arrayList;
/*     */     } 
/* 467 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   private static <T extends Enum<T>> T toEnumValue(Class<T> paramClass, String[] paramArrayOfString) {
/* 471 */     if (!paramClass.getName().equals(Type.getType(paramArrayOfString[0]).getClassName())) {
/* 472 */       throw new IllegalArgumentException("The supplied enum class does not match the stored enum value");
/*     */     }
/* 474 */     return Enum.valueOf(paramClass, paramArrayOfString[1]);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\Annotations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */