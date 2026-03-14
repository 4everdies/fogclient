/*     */ package org.spongepowered.tools.obfuscation.mirror.mapping;
/*     */ 
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ResolvableMappingMethod
/*     */   extends MappingMethod
/*     */ {
/*     */   private final TypeHandle ownerHandle;
/*     */   
/*     */   public ResolvableMappingMethod(TypeHandle paramTypeHandle, String paramString1, String paramString2) {
/*  41 */     super(paramTypeHandle.getName(), paramString1, paramString2);
/*  42 */     this.ownerHandle = paramTypeHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod getSuper() {
/*  51 */     if (this.ownerHandle == null) {
/*  52 */       return super.getSuper();
/*     */     }
/*     */     
/*  55 */     String str1 = getSimpleName();
/*  56 */     String str2 = getDesc();
/*  57 */     String str3 = TypeUtils.getJavaSignature(str2);
/*     */     
/*  59 */     TypeHandle typeHandle = this.ownerHandle.getSuperclass();
/*  60 */     if (typeHandle != null)
/*     */     {
/*  62 */       if (typeHandle.findMethod(str1, str3) != null) {
/*  63 */         return typeHandle.getMappingMethod(str1, str2);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  68 */     for (TypeHandle typeHandle1 : this.ownerHandle.getInterfaces()) {
/*  69 */       if (typeHandle1.findMethod(str1, str3) != null) {
/*  70 */         return typeHandle1.getMappingMethod(str1, str2);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  75 */     if (typeHandle != null) {
/*  76 */       return typeHandle.getMappingMethod(str1, str2).getSuper();
/*     */     }
/*     */     
/*  79 */     return super.getSuper();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod move(TypeHandle paramTypeHandle) {
/*  90 */     return new ResolvableMappingMethod(paramTypeHandle, getSimpleName(), getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod remap(String paramString) {
/*  95 */     return new ResolvableMappingMethod(this.ownerHandle, paramString, getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod transform(String paramString) {
/* 100 */     return new ResolvableMappingMethod(this.ownerHandle, getSimpleName(), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod copy() {
/* 105 */     return new ResolvableMappingMethod(this.ownerHandle, getSimpleName(), getDesc());
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\mapping\ResolvableMappingMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */