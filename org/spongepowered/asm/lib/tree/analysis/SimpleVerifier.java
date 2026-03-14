/*     */ package org.spongepowered.asm.lib.tree.analysis;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleVerifier
/*     */   extends BasicVerifier
/*     */ {
/*     */   private final Type currentClass;
/*     */   private final Type currentSuperClass;
/*     */   private final List<Type> currentClassInterfaces;
/*     */   private final boolean isInterface;
/*  69 */   private ClassLoader loader = getClass().getClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleVerifier() {
/*  75 */     this(null, null, false);
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
/*     */   public SimpleVerifier(Type paramType1, Type paramType2, boolean paramBoolean) {
/*  91 */     this(paramType1, paramType2, null, paramBoolean);
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
/*     */ 
/*     */   
/*     */   public SimpleVerifier(Type paramType1, Type paramType2, List<Type> paramList, boolean paramBoolean) {
/* 110 */     this(327680, paramType1, paramType2, paramList, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleVerifier(int paramInt, Type paramType1, Type paramType2, List<Type> paramList, boolean paramBoolean) {
/* 117 */     super(paramInt);
/* 118 */     this.currentClass = paramType1;
/* 119 */     this.currentSuperClass = paramType2;
/* 120 */     this.currentClassInterfaces = paramList;
/* 121 */     this.isInterface = paramBoolean;
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
/*     */   public void setClassLoader(ClassLoader paramClassLoader) {
/* 133 */     this.loader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicValue newValue(Type paramType) {
/* 138 */     if (paramType == null) {
/* 139 */       return BasicValue.UNINITIALIZED_VALUE;
/*     */     }
/*     */     
/* 142 */     boolean bool = (paramType.getSort() == 9) ? true : false;
/* 143 */     if (bool) {
/* 144 */       switch (paramType.getElementType().getSort()) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/* 149 */           return new BasicValue(paramType);
/*     */       } 
/*     */     
/*     */     }
/* 153 */     BasicValue basicValue = super.newValue(paramType);
/* 154 */     if (BasicValue.REFERENCE_VALUE.equals(basicValue)) {
/* 155 */       if (bool) {
/* 156 */         basicValue = newValue(paramType.getElementType());
/* 157 */         String str = basicValue.getType().getDescriptor();
/* 158 */         for (byte b = 0; b < paramType.getDimensions(); b++) {
/* 159 */           str = '[' + str;
/*     */         }
/* 161 */         basicValue = new BasicValue(Type.getType(str));
/*     */       } else {
/* 163 */         basicValue = new BasicValue(paramType);
/*     */       } 
/*     */     }
/* 166 */     return basicValue;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isArrayValue(BasicValue paramBasicValue) {
/* 171 */     Type type = paramBasicValue.getType();
/* 172 */     return (type != null && ("Lnull;"
/* 173 */       .equals(type.getDescriptor()) || type.getSort() == 9));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicValue getElementValue(BasicValue paramBasicValue) throws AnalyzerException {
/* 179 */     Type type = paramBasicValue.getType();
/* 180 */     if (type != null) {
/* 181 */       if (type.getSort() == 9)
/* 182 */         return newValue(Type.getType(type.getDescriptor()
/* 183 */               .substring(1))); 
/* 184 */       if ("Lnull;".equals(type.getDescriptor())) {
/* 185 */         return paramBasicValue;
/*     */       }
/*     */     } 
/* 188 */     throw new Error("Internal error");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSubTypeOf(BasicValue paramBasicValue1, BasicValue paramBasicValue2) {
/* 194 */     Type type1 = paramBasicValue2.getType();
/* 195 */     Type type2 = paramBasicValue1.getType();
/* 196 */     switch (type1.getSort()) {
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/* 201 */         return type2.equals(type1);
/*     */       case 9:
/*     */       case 10:
/* 204 */         if ("Lnull;".equals(type2.getDescriptor()))
/* 205 */           return true; 
/* 206 */         if (type2.getSort() == 10 || type2
/* 207 */           .getSort() == 9) {
/* 208 */           return isAssignableFrom(type1, type2);
/*     */         }
/* 210 */         return false;
/*     */     } 
/*     */     
/* 213 */     throw new Error("Internal error");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue merge(BasicValue paramBasicValue1, BasicValue paramBasicValue2) {
/* 219 */     if (!paramBasicValue1.equals(paramBasicValue2)) {
/* 220 */       Type type1 = paramBasicValue1.getType();
/* 221 */       Type type2 = paramBasicValue2.getType();
/* 222 */       if (type1 != null && (type1
/* 223 */         .getSort() == 10 || type1.getSort() == 9) && 
/* 224 */         type2 != null && (type2
/* 225 */         .getSort() == 10 || type2.getSort() == 9)) {
/* 226 */         if ("Lnull;".equals(type1.getDescriptor())) {
/* 227 */           return paramBasicValue2;
/*     */         }
/* 229 */         if ("Lnull;".equals(type2.getDescriptor())) {
/* 230 */           return paramBasicValue1;
/*     */         }
/* 232 */         if (isAssignableFrom(type1, type2)) {
/* 233 */           return paramBasicValue1;
/*     */         }
/* 235 */         if (isAssignableFrom(type2, type1)) {
/* 236 */           return paramBasicValue2;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/* 243 */           if (type1 == null || isInterface(type1)) {
/* 244 */             return BasicValue.REFERENCE_VALUE;
/*     */           }
/* 246 */           type1 = getSuperClass(type1);
/* 247 */           if (isAssignableFrom(type1, type2)) {
/* 248 */             return newValue(type1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 253 */       return BasicValue.UNINITIALIZED_VALUE;
/*     */     } 
/* 255 */     return paramBasicValue1;
/*     */   }
/*     */   
/*     */   protected boolean isInterface(Type paramType) {
/* 259 */     if (this.currentClass != null && paramType.equals(this.currentClass)) {
/* 260 */       return this.isInterface;
/*     */     }
/* 262 */     return getClass(paramType).isInterface();
/*     */   }
/*     */   
/*     */   protected Type getSuperClass(Type paramType) {
/* 266 */     if (this.currentClass != null && paramType.equals(this.currentClass)) {
/* 267 */       return this.currentSuperClass;
/*     */     }
/* 269 */     Class<?> clazz = getClass(paramType).getSuperclass();
/* 270 */     return (clazz == null) ? null : Type.getType(clazz);
/*     */   }
/*     */   
/*     */   protected boolean isAssignableFrom(Type paramType1, Type paramType2) {
/* 274 */     if (paramType1.equals(paramType2)) {
/* 275 */       return true;
/*     */     }
/* 277 */     if (this.currentClass != null && paramType1.equals(this.currentClass)) {
/* 278 */       if (getSuperClass(paramType2) == null) {
/* 279 */         return false;
/*     */       }
/* 281 */       if (this.isInterface) {
/* 282 */         return (paramType2.getSort() == 10 || paramType2
/* 283 */           .getSort() == 9);
/*     */       }
/* 285 */       return isAssignableFrom(paramType1, getSuperClass(paramType2));
/*     */     } 
/*     */     
/* 288 */     if (this.currentClass != null && paramType2.equals(this.currentClass)) {
/* 289 */       if (isAssignableFrom(paramType1, this.currentSuperClass)) {
/* 290 */         return true;
/*     */       }
/* 292 */       if (this.currentClassInterfaces != null) {
/* 293 */         for (byte b = 0; b < this.currentClassInterfaces.size(); b++) {
/* 294 */           Type type = this.currentClassInterfaces.get(b);
/* 295 */           if (isAssignableFrom(paramType1, type)) {
/* 296 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/* 300 */       return false;
/*     */     } 
/* 302 */     Class<?> clazz = getClass(paramType1);
/* 303 */     if (clazz.isInterface()) {
/* 304 */       clazz = Object.class;
/*     */     }
/* 306 */     return clazz.isAssignableFrom(getClass(paramType2));
/*     */   }
/*     */   
/*     */   protected Class<?> getClass(Type paramType) {
/*     */     try {
/* 311 */       if (paramType.getSort() == 9) {
/* 312 */         return Class.forName(paramType.getDescriptor().replace('/', '.'), false, this.loader);
/*     */       }
/*     */       
/* 315 */       return Class.forName(paramType.getClassName(), false, this.loader);
/* 316 */     } catch (ClassNotFoundException classNotFoundException) {
/* 317 */       throw new RuntimeException(classNotFoundException.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\SimpleVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */