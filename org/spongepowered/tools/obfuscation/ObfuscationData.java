/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObfuscationData<T>
/*     */   implements Iterable<ObfuscationType>
/*     */ {
/*  53 */   private final Map<ObfuscationType, T> data = new HashMap<ObfuscationType, T>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final T defaultValue;
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData() {
/*  62 */     this(null);
/*     */   }
/*     */   
/*     */   public ObfuscationData(T paramT) {
/*  66 */     this.defaultValue = paramT;
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
/*     */   @Deprecated
/*     */   public void add(ObfuscationType paramObfuscationType, T paramT) {
/*  81 */     put(paramObfuscationType, paramT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(ObfuscationType paramObfuscationType, T paramT) {
/*  91 */     this.data.put(paramObfuscationType, paramT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  98 */     return this.data.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(ObfuscationType paramObfuscationType) {
/* 109 */     T t = this.data.get(paramObfuscationType);
/* 110 */     return (t != null) ? t : this.defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<ObfuscationType> iterator() {
/* 118 */     return this.data.keySet().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return String.format("ObfuscationData[%s,DEFAULT=%s]", new Object[] { listValues(), this.defaultValue });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String values() {
/* 135 */     return "[" + listValues() + "]";
/*     */   }
/*     */   
/*     */   private String listValues() {
/* 139 */     StringBuilder stringBuilder = new StringBuilder();
/* 140 */     boolean bool = false;
/* 141 */     for (ObfuscationType obfuscationType : this.data.keySet()) {
/* 142 */       if (bool) {
/* 143 */         stringBuilder.append(',');
/*     */       }
/* 145 */       stringBuilder.append(obfuscationType.getKey()).append('=').append(this.data.get(obfuscationType));
/* 146 */       bool = true;
/*     */     } 
/* 148 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\ObfuscationData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */