/*     */ package org.spongepowered.asm.lib.tree.analysis;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SmallSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   E e1;
/*     */   E e2;
/*     */   
/*     */   static final <T> Set<T> emptySet() {
/*  50 */     return new SmallSet<T>(null, null);
/*     */   }
/*     */   
/*     */   SmallSet(E paramE1, E paramE2) {
/*  54 */     this.e1 = paramE1;
/*  55 */     this.e2 = paramE2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  64 */     return new SmallSet(this.e1, this.e2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  69 */     return (this.e1 == null) ? 0 : ((this.e2 == null) ? 1 : 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  77 */     return (this.e1 != null);
/*     */   }
/*     */   
/*     */   public E next() {
/*  81 */     if (this.e1 == null) {
/*  82 */       throw new NoSuchElementException();
/*     */     }
/*  84 */     E e = this.e1;
/*  85 */     this.e1 = this.e2;
/*  86 */     this.e2 = null;
/*  87 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {}
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> union(SmallSet<E> paramSmallSet) {
/*  98 */     if ((paramSmallSet.e1 == this.e1 && paramSmallSet.e2 == this.e2) || (paramSmallSet.e1 == this.e2 && paramSmallSet.e2 == this.e1)) {
/*  99 */       return this;
/*     */     }
/* 101 */     if (paramSmallSet.e1 == null) {
/* 102 */       return this;
/*     */     }
/* 104 */     if (this.e1 == null) {
/* 105 */       return paramSmallSet;
/*     */     }
/* 107 */     if (paramSmallSet.e2 == null) {
/* 108 */       if (this.e2 == null)
/* 109 */         return new SmallSet(this.e1, paramSmallSet.e1); 
/* 110 */       if (paramSmallSet.e1 == this.e1 || paramSmallSet.e1 == this.e2) {
/* 111 */         return this;
/*     */       }
/*     */     } 
/* 114 */     if (this.e2 == null)
/*     */     {
/*     */ 
/*     */       
/* 118 */       if (this.e1 == paramSmallSet.e1 || this.e1 == paramSmallSet.e2) {
/* 119 */         return paramSmallSet;
/*     */       }
/*     */     }
/*     */     
/* 123 */     HashSet<E> hashSet = new HashSet(4);
/* 124 */     hashSet.add(this.e1);
/* 125 */     if (this.e2 != null) {
/* 126 */       hashSet.add(this.e2);
/*     */     }
/* 128 */     hashSet.add(paramSmallSet.e1);
/* 129 */     if (paramSmallSet.e2 != null) {
/* 130 */       hashSet.add(paramSmallSet.e2);
/*     */     }
/* 132 */     return hashSet;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\SmallSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */