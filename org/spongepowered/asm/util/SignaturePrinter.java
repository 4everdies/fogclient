/*     */ package org.spongepowered.asm.util;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignaturePrinter
/*     */ {
/*     */   private final String name;
/*     */   private final Type returnType;
/*     */   private final Type[] argTypes;
/*     */   private final String[] argNames;
/*  64 */   private String modifiers = "private void";
/*     */ 
/*     */   
/*     */   private boolean fullyQualified;
/*     */ 
/*     */ 
/*     */   
/*     */   public SignaturePrinter(MethodNode paramMethodNode) {
/*  72 */     this(paramMethodNode.name, Type.VOID_TYPE, Type.getArgumentTypes(paramMethodNode.desc));
/*  73 */     setModifiers(paramMethodNode);
/*     */   }
/*     */   
/*     */   public SignaturePrinter(MethodNode paramMethodNode, String[] paramArrayOfString) {
/*  77 */     this(paramMethodNode.name, Type.VOID_TYPE, Type.getArgumentTypes(paramMethodNode.desc), paramArrayOfString);
/*  78 */     setModifiers(paramMethodNode);
/*     */   }
/*     */   
/*     */   public SignaturePrinter(MemberInfo paramMemberInfo) {
/*  82 */     this(paramMemberInfo.name, paramMemberInfo.desc);
/*     */   }
/*     */   
/*     */   public SignaturePrinter(String paramString1, String paramString2) {
/*  86 */     this(paramString1, Type.getReturnType(paramString2), Type.getArgumentTypes(paramString2));
/*     */   }
/*     */   
/*     */   public SignaturePrinter(String paramString, Type paramType, Type[] paramArrayOfType) {
/*  90 */     this.name = paramString;
/*  91 */     this.returnType = paramType;
/*  92 */     this.argTypes = new Type[paramArrayOfType.length];
/*  93 */     this.argNames = new String[paramArrayOfType.length];
/*  94 */     for (byte b1 = 0, b2 = 0; b1 < paramArrayOfType.length; b1++) {
/*  95 */       if (paramArrayOfType[b1] != null) {
/*  96 */         this.argTypes[b1] = paramArrayOfType[b1];
/*  97 */         this.argNames[b1] = "var" + b2++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public SignaturePrinter(String paramString, Type paramType, LocalVariableNode[] paramArrayOfLocalVariableNode) {
/* 103 */     this.name = paramString;
/* 104 */     this.returnType = paramType;
/* 105 */     this.argTypes = new Type[paramArrayOfLocalVariableNode.length];
/* 106 */     this.argNames = new String[paramArrayOfLocalVariableNode.length];
/* 107 */     for (byte b = 0; b < paramArrayOfLocalVariableNode.length; b++) {
/* 108 */       if (paramArrayOfLocalVariableNode[b] != null) {
/* 109 */         this.argTypes[b] = Type.getType((paramArrayOfLocalVariableNode[b]).desc);
/* 110 */         this.argNames[b] = (paramArrayOfLocalVariableNode[b]).name;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public SignaturePrinter(String paramString, Type paramType, Type[] paramArrayOfType, String[] paramArrayOfString) {
/* 116 */     this.name = paramString;
/* 117 */     this.returnType = paramType;
/* 118 */     this.argTypes = paramArrayOfType;
/* 119 */     this.argNames = paramArrayOfString;
/* 120 */     if (this.argTypes.length > this.argNames.length) {
/* 121 */       throw new IllegalArgumentException(String.format("Types array length must not exceed names array length! (names=%d, types=%d)", new Object[] {
/* 122 */               Integer.valueOf(this.argNames.length), Integer.valueOf(this.argTypes.length)
/*     */             }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedArgs() {
/* 130 */     return appendArgs(new StringBuilder(), true, true).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReturnType() {
/* 137 */     return getTypeName(this.returnType, false, this.fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModifiers(MethodNode paramMethodNode) {
/* 146 */     String str = getTypeName(Type.getReturnType(paramMethodNode.desc), false, this.fullyQualified);
/* 147 */     if ((paramMethodNode.access & 0x1) != 0) {
/* 148 */       setModifiers("public " + str);
/* 149 */     } else if ((paramMethodNode.access & 0x4) != 0) {
/* 150 */       setModifiers("protected " + str);
/* 151 */     } else if ((paramMethodNode.access & 0x2) != 0) {
/* 152 */       setModifiers("private " + str);
/*     */     } else {
/* 154 */       setModifiers(str);
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
/*     */   public SignaturePrinter setModifiers(String paramString) {
/* 167 */     this.modifiers = paramString.replace("${returnType}", getReturnType());
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SignaturePrinter setFullyQualified(boolean paramBoolean) {
/* 179 */     this.fullyQualified = paramBoolean;
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullyQualified() {
/* 188 */     return this.fullyQualified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     return appendArgs((new StringBuilder()).append(this.modifiers).append(" ").append(this.name), false, true).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toDescriptor() {
/* 203 */     StringBuilder stringBuilder = appendArgs(new StringBuilder(), true, false);
/* 204 */     return stringBuilder.append(getTypeName(this.returnType, false, this.fullyQualified)).toString();
/*     */   }
/*     */   
/*     */   private StringBuilder appendArgs(StringBuilder paramStringBuilder, boolean paramBoolean1, boolean paramBoolean2) {
/* 208 */     paramStringBuilder.append('(');
/* 209 */     for (byte b = 0; b < this.argTypes.length; b++) {
/* 210 */       if (this.argTypes[b] != null) {
/*     */         
/* 212 */         if (b > 0) {
/* 213 */           paramStringBuilder.append(',');
/* 214 */           if (paramBoolean2) {
/* 215 */             paramStringBuilder.append(' ');
/*     */           }
/*     */         } 
/*     */         try {
/* 219 */           String str = paramBoolean1 ? null : (Strings.isNullOrEmpty(this.argNames[b]) ? ("unnamed" + b) : this.argNames[b]);
/* 220 */           appendType(paramStringBuilder, this.argTypes[b], str);
/* 221 */         } catch (Exception exception) {
/*     */           
/* 223 */           throw new RuntimeException(exception);
/*     */         } 
/*     */       } 
/* 226 */     }  return paramStringBuilder.append(")");
/*     */   }
/*     */   
/*     */   private StringBuilder appendType(StringBuilder paramStringBuilder, Type paramType, String paramString) {
/* 230 */     switch (paramType.getSort()) {
/*     */       case 9:
/* 232 */         return appendArraySuffix(appendType(paramStringBuilder, paramType.getElementType(), paramString), paramType);
/*     */       case 10:
/* 234 */         return appendType(paramStringBuilder, paramType.getClassName(), paramString);
/*     */     } 
/* 236 */     paramStringBuilder.append(getTypeName(paramType, false, this.fullyQualified));
/* 237 */     if (paramString != null) {
/* 238 */       paramStringBuilder.append(' ').append(paramString);
/*     */     }
/* 240 */     return paramStringBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder appendType(StringBuilder paramStringBuilder, String paramString1, String paramString2) {
/* 245 */     if (!this.fullyQualified) {
/* 246 */       paramString1 = paramString1.substring(paramString1.lastIndexOf('.') + 1);
/*     */     }
/* 248 */     paramStringBuilder.append(paramString1);
/* 249 */     if (paramString1.endsWith("CallbackInfoReturnable")) {
/* 250 */       paramStringBuilder.append('<').append(getTypeName(this.returnType, true, this.fullyQualified)).append('>');
/*     */     }
/* 252 */     if (paramString2 != null) {
/* 253 */       paramStringBuilder.append(' ').append(paramString2);
/*     */     }
/* 255 */     return paramStringBuilder;
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
/*     */   public static String getTypeName(Type paramType, boolean paramBoolean) {
/* 267 */     return getTypeName(paramType, paramBoolean, false);
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
/*     */   public static String getTypeName(Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
/*     */     String str;
/* 280 */     switch (paramType.getSort()) { case 0:
/* 281 */         return paramBoolean1 ? "Void" : "void";
/* 282 */       case 1: return paramBoolean1 ? "Boolean" : "boolean";
/* 283 */       case 2: return paramBoolean1 ? "Character" : "char";
/* 284 */       case 3: return paramBoolean1 ? "Byte" : "byte";
/* 285 */       case 4: return paramBoolean1 ? "Short" : "short";
/* 286 */       case 5: return paramBoolean1 ? "Integer" : "int";
/* 287 */       case 6: return paramBoolean1 ? "Float" : "float";
/* 288 */       case 7: return paramBoolean1 ? "Long" : "long";
/* 289 */       case 8: return paramBoolean1 ? "Double" : "double";
/* 290 */       case 9: return getTypeName(paramType.getElementType(), paramBoolean1, paramBoolean2) + arraySuffix(paramType);
/*     */       case 10:
/* 292 */         str = paramType.getClassName();
/* 293 */         if (!paramBoolean2) {
/* 294 */           str = str.substring(str.lastIndexOf('.') + 1);
/*     */         }
/* 296 */         return str; }
/*     */     
/* 298 */     return "Object";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String arraySuffix(Type paramType) {
/* 303 */     return Strings.repeat("[]", paramType.getDimensions());
/*     */   }
/*     */ 
/*     */   
/*     */   private static StringBuilder appendArraySuffix(StringBuilder paramStringBuilder, Type paramType) {
/* 308 */     for (byte b = 0; b < paramType.getDimensions(); b++) {
/* 309 */       paramStringBuilder.append("[]");
/*     */     }
/* 311 */     return paramStringBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\SignaturePrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */