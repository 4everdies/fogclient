/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationNode
/*     */   extends AnnotationVisitor
/*     */ {
/*     */   public String desc;
/*     */   public List<Object> values;
/*     */   
/*     */   public AnnotationNode(String paramString) {
/*  74 */     this(327680, paramString);
/*  75 */     if (getClass() != AnnotationNode.class) {
/*  76 */       throw new IllegalStateException();
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
/*     */   public AnnotationNode(int paramInt, String paramString) {
/*  90 */     super(paramInt);
/*  91 */     this.desc = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationNode(List<Object> paramList) {
/* 101 */     super(327680);
/* 102 */     this.values = paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String paramString, Object paramObject) {
/* 111 */     if (this.values == null) {
/* 112 */       this.values = new ArrayList((this.desc != null) ? 2 : 1);
/*     */     }
/* 114 */     if (this.desc != null) {
/* 115 */       this.values.add(paramString);
/*     */     }
/* 117 */     if (paramObject instanceof byte[]) {
/* 118 */       byte[] arrayOfByte = (byte[])paramObject;
/* 119 */       ArrayList<Byte> arrayList = new ArrayList(arrayOfByte.length);
/* 120 */       for (byte b : arrayOfByte) {
/* 121 */         arrayList.add(Byte.valueOf(b));
/*     */       }
/* 123 */       this.values.add(arrayList);
/* 124 */     } else if (paramObject instanceof boolean[]) {
/* 125 */       boolean[] arrayOfBoolean = (boolean[])paramObject;
/* 126 */       ArrayList<Boolean> arrayList = new ArrayList(arrayOfBoolean.length);
/* 127 */       for (boolean bool : arrayOfBoolean) {
/* 128 */         arrayList.add(Boolean.valueOf(bool));
/*     */       }
/* 130 */       this.values.add(arrayList);
/* 131 */     } else if (paramObject instanceof short[]) {
/* 132 */       short[] arrayOfShort = (short[])paramObject;
/* 133 */       ArrayList<Short> arrayList = new ArrayList(arrayOfShort.length);
/* 134 */       for (short s : arrayOfShort) {
/* 135 */         arrayList.add(Short.valueOf(s));
/*     */       }
/* 137 */       this.values.add(arrayList);
/* 138 */     } else if (paramObject instanceof char[]) {
/* 139 */       char[] arrayOfChar = (char[])paramObject;
/* 140 */       ArrayList<Character> arrayList = new ArrayList(arrayOfChar.length);
/* 141 */       for (char c : arrayOfChar) {
/* 142 */         arrayList.add(Character.valueOf(c));
/*     */       }
/* 144 */       this.values.add(arrayList);
/* 145 */     } else if (paramObject instanceof int[]) {
/* 146 */       int[] arrayOfInt = (int[])paramObject;
/* 147 */       ArrayList<Integer> arrayList = new ArrayList(arrayOfInt.length);
/* 148 */       for (int i : arrayOfInt) {
/* 149 */         arrayList.add(Integer.valueOf(i));
/*     */       }
/* 151 */       this.values.add(arrayList);
/* 152 */     } else if (paramObject instanceof long[]) {
/* 153 */       long[] arrayOfLong = (long[])paramObject;
/* 154 */       ArrayList<Long> arrayList = new ArrayList(arrayOfLong.length);
/* 155 */       for (long l : arrayOfLong) {
/* 156 */         arrayList.add(Long.valueOf(l));
/*     */       }
/* 158 */       this.values.add(arrayList);
/* 159 */     } else if (paramObject instanceof float[]) {
/* 160 */       float[] arrayOfFloat = (float[])paramObject;
/* 161 */       ArrayList<Float> arrayList = new ArrayList(arrayOfFloat.length);
/* 162 */       for (float f : arrayOfFloat) {
/* 163 */         arrayList.add(Float.valueOf(f));
/*     */       }
/* 165 */       this.values.add(arrayList);
/* 166 */     } else if (paramObject instanceof double[]) {
/* 167 */       double[] arrayOfDouble = (double[])paramObject;
/* 168 */       ArrayList<Double> arrayList = new ArrayList(arrayOfDouble.length);
/* 169 */       for (double d : arrayOfDouble) {
/* 170 */         arrayList.add(Double.valueOf(d));
/*     */       }
/* 172 */       this.values.add(arrayList);
/*     */     } else {
/* 174 */       this.values.add(paramObject);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/* 181 */     if (this.values == null) {
/* 182 */       this.values = new ArrayList((this.desc != null) ? 2 : 1);
/*     */     }
/* 184 */     if (this.desc != null) {
/* 185 */       this.values.add(paramString1);
/*     */     }
/* 187 */     this.values.add(new String[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
/* 193 */     if (this.values == null) {
/* 194 */       this.values = new ArrayList((this.desc != null) ? 2 : 1);
/*     */     }
/* 196 */     if (this.desc != null) {
/* 197 */       this.values.add(paramString1);
/*     */     }
/* 199 */     AnnotationNode annotationNode = new AnnotationNode(paramString2);
/* 200 */     this.values.add(annotationNode);
/* 201 */     return annotationNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String paramString) {
/* 206 */     if (this.values == null) {
/* 207 */       this.values = new ArrayList((this.desc != null) ? 2 : 1);
/*     */     }
/* 209 */     if (this.desc != null) {
/* 210 */       this.values.add(paramString);
/*     */     }
/* 212 */     ArrayList<Object> arrayList = new ArrayList();
/* 213 */     this.values.add(arrayList);
/* 214 */     return new AnnotationNode(arrayList);
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
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(AnnotationVisitor paramAnnotationVisitor) {
/* 246 */     if (paramAnnotationVisitor != null) {
/* 247 */       if (this.values != null) {
/* 248 */         for (byte b = 0; b < this.values.size(); b += 2) {
/* 249 */           String str = (String)this.values.get(b);
/* 250 */           Object object = this.values.get(b + 1);
/* 251 */           accept(paramAnnotationVisitor, str, object);
/*     */         } 
/*     */       }
/* 254 */       paramAnnotationVisitor.visitEnd();
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
/*     */   static void accept(AnnotationVisitor paramAnnotationVisitor, String paramString, Object paramObject) {
/* 270 */     if (paramAnnotationVisitor != null)
/* 271 */       if (paramObject instanceof String[]) {
/* 272 */         String[] arrayOfString = (String[])paramObject;
/* 273 */         paramAnnotationVisitor.visitEnum(paramString, arrayOfString[0], arrayOfString[1]);
/* 274 */       } else if (paramObject instanceof AnnotationNode) {
/* 275 */         AnnotationNode annotationNode = (AnnotationNode)paramObject;
/* 276 */         annotationNode.accept(paramAnnotationVisitor.visitAnnotation(paramString, annotationNode.desc));
/* 277 */       } else if (paramObject instanceof List) {
/* 278 */         AnnotationVisitor annotationVisitor = paramAnnotationVisitor.visitArray(paramString);
/* 279 */         if (annotationVisitor != null) {
/* 280 */           List list = (List)paramObject;
/* 281 */           for (byte b = 0; b < list.size(); b++) {
/* 282 */             accept(annotationVisitor, null, list.get(b));
/*     */           }
/* 284 */           annotationVisitor.visitEnd();
/*     */         } 
/*     */       } else {
/* 287 */         paramAnnotationVisitor.visit(paramString, paramObject);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\AnnotationNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */