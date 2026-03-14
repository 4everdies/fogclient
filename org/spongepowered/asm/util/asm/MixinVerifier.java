/*     */ package org.spongepowered.asm.util.asm;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.analysis.SimpleVerifier;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MixinVerifier
/*     */   extends SimpleVerifier
/*     */ {
/*     */   private Type currentClass;
/*     */   private Type currentSuperClass;
/*     */   private List<Type> currentClassInterfaces;
/*     */   private boolean isInterface;
/*     */   
/*     */   public MixinVerifier(Type paramType1, Type paramType2, List<Type> paramList, boolean paramBoolean) {
/*  44 */     super(paramType1, paramType2, paramList, paramBoolean);
/*  45 */     this.currentClass = paramType1;
/*  46 */     this.currentSuperClass = paramType2;
/*  47 */     this.currentClassInterfaces = paramList;
/*  48 */     this.isInterface = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInterface(Type paramType) {
/*  53 */     if (this.currentClass != null && paramType.equals(this.currentClass)) {
/*  54 */       return this.isInterface;
/*     */     }
/*  56 */     return ClassInfo.forType(paramType).isInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Type getSuperClass(Type paramType) {
/*  61 */     if (this.currentClass != null && paramType.equals(this.currentClass)) {
/*  62 */       return this.currentSuperClass;
/*     */     }
/*  64 */     ClassInfo classInfo = ClassInfo.forType(paramType).getSuperClass();
/*  65 */     return (classInfo == null) ? null : Type.getType("L" + classInfo.getName() + ";");
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isAssignableFrom(Type paramType1, Type paramType2) {
/*  70 */     if (paramType1.equals(paramType2)) {
/*  71 */       return true;
/*     */     }
/*  73 */     if (this.currentClass != null && paramType1.equals(this.currentClass)) {
/*  74 */       if (getSuperClass(paramType2) == null) {
/*  75 */         return false;
/*     */       }
/*  77 */       if (this.isInterface) {
/*  78 */         return (paramType2.getSort() == 10 || paramType2.getSort() == 9);
/*     */       }
/*  80 */       return isAssignableFrom(paramType1, getSuperClass(paramType2));
/*     */     } 
/*  82 */     if (this.currentClass != null && paramType2.equals(this.currentClass)) {
/*  83 */       if (isAssignableFrom(paramType1, this.currentSuperClass)) {
/*  84 */         return true;
/*     */       }
/*  86 */       if (this.currentClassInterfaces != null) {
/*  87 */         for (byte b = 0; b < this.currentClassInterfaces.size(); b++) {
/*  88 */           Type type = this.currentClassInterfaces.get(b);
/*  89 */           if (isAssignableFrom(paramType1, type)) {
/*  90 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*  94 */       return false;
/*     */     } 
/*  96 */     ClassInfo classInfo = ClassInfo.forType(paramType1);
/*  97 */     if (classInfo == null) {
/*  98 */       return false;
/*     */     }
/* 100 */     if (classInfo.isInterface()) {
/* 101 */       classInfo = ClassInfo.forName("java/lang/Object");
/*     */     }
/* 103 */     return ClassInfo.forType(paramType2).hasSuperClass(classInfo);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\asm\MixinVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */