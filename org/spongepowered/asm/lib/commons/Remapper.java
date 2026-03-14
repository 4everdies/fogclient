/*     */ package org.spongepowered.asm.lib.commons;
/*     */ 
/*     */ import org.spongepowered.asm.lib.Handle;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.signature.SignatureReader;
/*     */ import org.spongepowered.asm.lib.signature.SignatureVisitor;
/*     */ import org.spongepowered.asm.lib.signature.SignatureWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Remapper
/*     */ {
/*     */   public String mapDesc(String paramString) {
/*     */     String str1;
/*     */     byte b;
/*     */     String str2;
/*  54 */     Type type = Type.getType(paramString);
/*  55 */     switch (type.getSort()) {
/*     */       case 9:
/*  57 */         str1 = mapDesc(type.getElementType().getDescriptor());
/*  58 */         for (b = 0; b < type.getDimensions(); b++) {
/*  59 */           str1 = '[' + str1;
/*     */         }
/*  61 */         return str1;
/*     */       case 10:
/*  63 */         str2 = map(type.getInternalName());
/*  64 */         if (str2 != null)
/*  65 */           return 'L' + str2 + ';'; 
/*     */         break;
/*     */     } 
/*  68 */     return paramString;
/*     */   } private Type mapType(Type paramType) {
/*     */     String str;
/*     */     byte b;
/*  72 */     switch (paramType.getSort()) {
/*     */       case 9:
/*  74 */         str = mapDesc(paramType.getElementType().getDescriptor());
/*  75 */         for (b = 0; b < paramType.getDimensions(); b++) {
/*  76 */           str = '[' + str;
/*     */         }
/*  78 */         return Type.getType(str);
/*     */       case 10:
/*  80 */         str = map(paramType.getInternalName());
/*  81 */         return (str != null) ? Type.getObjectType(str) : paramType;
/*     */       case 11:
/*  83 */         return Type.getMethodType(mapMethodDesc(paramType.getDescriptor()));
/*     */     } 
/*  85 */     return paramType;
/*     */   }
/*     */   
/*     */   public String mapType(String paramString) {
/*  89 */     if (paramString == null) {
/*  90 */       return null;
/*     */     }
/*  92 */     return mapType(Type.getObjectType(paramString)).getInternalName();
/*     */   }
/*     */   
/*     */   public String[] mapTypes(String[] paramArrayOfString) {
/*  96 */     String[] arrayOfString = null;
/*  97 */     boolean bool = false;
/*  98 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  99 */       String str1 = paramArrayOfString[b];
/* 100 */       String str2 = map(str1);
/* 101 */       if (str2 != null && arrayOfString == null) {
/* 102 */         arrayOfString = new String[paramArrayOfString.length];
/* 103 */         if (b > 0) {
/* 104 */           System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, b);
/*     */         }
/* 106 */         bool = true;
/*     */       } 
/* 108 */       if (bool) {
/* 109 */         arrayOfString[b] = (str2 == null) ? str1 : str2;
/*     */       }
/*     */     } 
/* 112 */     return bool ? arrayOfString : paramArrayOfString;
/*     */   }
/*     */   
/*     */   public String mapMethodDesc(String paramString) {
/* 116 */     if ("()V".equals(paramString)) {
/* 117 */       return paramString;
/*     */     }
/*     */     
/* 120 */     Type[] arrayOfType = Type.getArgumentTypes(paramString);
/* 121 */     StringBuilder stringBuilder = new StringBuilder("(");
/* 122 */     for (byte b = 0; b < arrayOfType.length; b++) {
/* 123 */       stringBuilder.append(mapDesc(arrayOfType[b].getDescriptor()));
/*     */     }
/* 125 */     Type type = Type.getReturnType(paramString);
/* 126 */     if (type == Type.VOID_TYPE) {
/* 127 */       stringBuilder.append(")V");
/* 128 */       return stringBuilder.toString();
/*     */     } 
/* 130 */     stringBuilder.append(')').append(mapDesc(type.getDescriptor()));
/* 131 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public Object mapValue(Object paramObject) {
/* 135 */     if (paramObject instanceof Type) {
/* 136 */       return mapType((Type)paramObject);
/*     */     }
/* 138 */     if (paramObject instanceof Handle) {
/* 139 */       Handle handle = (Handle)paramObject;
/* 140 */       return new Handle(handle.getTag(), mapType(handle.getOwner()), mapMethodName(handle
/* 141 */             .getOwner(), handle.getName(), handle.getDesc()), 
/* 142 */           mapMethodDesc(handle.getDesc()), handle.isInterface());
/*     */     } 
/* 144 */     return paramObject;
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
/*     */   public String mapSignature(String paramString, boolean paramBoolean) {
/* 157 */     if (paramString == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     SignatureReader signatureReader = new SignatureReader(paramString);
/* 161 */     SignatureWriter signatureWriter = new SignatureWriter();
/* 162 */     SignatureVisitor signatureVisitor = createSignatureRemapper((SignatureVisitor)signatureWriter);
/* 163 */     if (paramBoolean) {
/* 164 */       signatureReader.acceptType(signatureVisitor);
/*     */     } else {
/* 166 */       signatureReader.accept(signatureVisitor);
/*     */     } 
/* 168 */     return signatureWriter.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SignatureVisitor createRemappingSignatureAdapter(SignatureVisitor paramSignatureVisitor) {
/* 177 */     return new SignatureRemapper(paramSignatureVisitor, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SignatureVisitor createSignatureRemapper(SignatureVisitor paramSignatureVisitor) {
/* 182 */     return createRemappingSignatureAdapter(paramSignatureVisitor);
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
/*     */   public String mapMethodName(String paramString1, String paramString2, String paramString3) {
/* 197 */     return paramString2;
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
/*     */   public String mapInvokeDynamicMethodName(String paramString1, String paramString2) {
/* 210 */     return paramString1;
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
/*     */   public String mapFieldName(String paramString1, String paramString2, String paramString3) {
/* 225 */     return paramString2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String map(String paramString) {
/* 236 */     return paramString;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\commons\Remapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */