/*     */ package org.spongepowered.asm.lib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeReference
/*     */ {
/*     */   public static final int CLASS_TYPE_PARAMETER = 0;
/*     */   public static final int METHOD_TYPE_PARAMETER = 1;
/*     */   public static final int CLASS_EXTENDS = 16;
/*     */   public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
/*     */   public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
/*     */   public static final int FIELD = 19;
/*     */   public static final int METHOD_RETURN = 20;
/*     */   public static final int METHOD_RECEIVER = 21;
/*     */   public static final int METHOD_FORMAL_PARAMETER = 22;
/*     */   public static final int THROWS = 23;
/*     */   public static final int LOCAL_VARIABLE = 64;
/*     */   public static final int RESOURCE_VARIABLE = 65;
/*     */   public static final int EXCEPTION_PARAMETER = 66;
/*     */   public static final int INSTANCEOF = 67;
/*     */   public static final int NEW = 68;
/*     */   public static final int CONSTRUCTOR_REFERENCE = 69;
/*     */   public static final int METHOD_REFERENCE = 70;
/*     */   public static final int CAST = 71;
/*     */   public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
/*     */   public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
/*     */   public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
/*     */   public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
/*     */   private int value;
/*     */   
/*     */   public TypeReference(int paramInt) {
/* 190 */     this.value = paramInt;
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
/*     */   public static TypeReference newTypeReference(int paramInt) {
/* 207 */     return new TypeReference(paramInt << 24);
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
/*     */   public static TypeReference newTypeParameterReference(int paramInt1, int paramInt2) {
/* 222 */     return new TypeReference(paramInt1 << 24 | paramInt2 << 16);
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
/*     */   public static TypeReference newTypeParameterBoundReference(int paramInt1, int paramInt2, int paramInt3) {
/* 241 */     return new TypeReference(paramInt1 << 24 | paramInt2 << 16 | paramInt3 << 8);
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
/*     */   public static TypeReference newSuperTypeReference(int paramInt) {
/* 255 */     paramInt &= 0xFFFF;
/* 256 */     return new TypeReference(0x10000000 | paramInt << 8);
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
/*     */   public static TypeReference newFormalParameterReference(int paramInt) {
/* 268 */     return new TypeReference(0x16000000 | paramInt << 16);
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
/*     */   public static TypeReference newExceptionReference(int paramInt) {
/* 282 */     return new TypeReference(0x17000000 | paramInt << 8);
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
/*     */   public static TypeReference newTryCatchReference(int paramInt) {
/* 296 */     return new TypeReference(0x42000000 | paramInt << 8);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeReference newTypeArgumentReference(int paramInt1, int paramInt2) {
/* 320 */     return new TypeReference(paramInt1 << 24 | paramInt2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSort() {
/* 350 */     return this.value >>> 24;
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
/*     */   public int getTypeParameterIndex() {
/* 364 */     return (this.value & 0xFF0000) >> 16;
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
/*     */   public int getTypeParameterBoundIndex() {
/* 377 */     return (this.value & 0xFF00) >> 8;
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
/*     */   public int getSuperTypeIndex() {
/* 390 */     return (short)((this.value & 0xFFFF00) >> 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFormalParameterIndex() {
/* 401 */     return (this.value & 0xFF0000) >> 16;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExceptionIndex() {
/* 412 */     return (this.value & 0xFFFF00) >> 8;
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
/*     */   public int getTryCatchBlockIndex() {
/* 424 */     return (this.value & 0xFFFF00) >> 8;
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
/*     */   public int getTypeArgumentIndex() {
/* 440 */     return this.value & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/* 450 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\TypeReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */