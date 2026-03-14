/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements Serializable, Comparable<TypeReference>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String name;
/*     */   private transient TypeHandle handle;
/*     */   
/*     */   public TypeReference(TypeHandle paramTypeHandle) {
/*  55 */     this.name = paramTypeHandle.getName();
/*  56 */     this.handle = paramTypeHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeReference(String paramString) {
/*  65 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  72 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  79 */     return this.name.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getHandle(ProcessingEnvironment paramProcessingEnvironment) {
/*  90 */     if (this.handle == null) {
/*  91 */       TypeElement typeElement = paramProcessingEnvironment.getElementUtils().getTypeElement(getClassName());
/*     */       try {
/*  93 */         this.handle = new TypeHandle(typeElement);
/*  94 */       } catch (Exception exception) {
/*     */         
/*  96 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return this.handle;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return String.format("TypeReference[%s]", new Object[] { this.name });
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(TypeReference paramTypeReference) {
/* 110 */     return (paramTypeReference == null) ? -1 : this.name.compareTo(paramTypeReference.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 115 */     return (paramObject instanceof TypeReference && compareTo((TypeReference)paramObject) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\TypeReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */