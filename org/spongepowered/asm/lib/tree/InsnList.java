/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InsnList
/*     */ {
/*     */   private int size;
/*     */   private AbstractInsnNode first;
/*     */   private AbstractInsnNode last;
/*     */   AbstractInsnNode[] cache;
/*     */   
/*     */   public int size() {
/*  70 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode getFirst() {
/*  80 */     return this.first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode getLast() {
/*  90 */     return this.last;
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
/*     */   public AbstractInsnNode get(int paramInt) {
/* 106 */     if (paramInt < 0 || paramInt >= this.size) {
/* 107 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 109 */     if (this.cache == null) {
/* 110 */       this.cache = toArray();
/*     */     }
/* 112 */     return this.cache[paramInt];
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
/*     */   public boolean contains(AbstractInsnNode paramAbstractInsnNode) {
/* 125 */     AbstractInsnNode abstractInsnNode = this.first;
/* 126 */     while (abstractInsnNode != null && abstractInsnNode != paramAbstractInsnNode) {
/* 127 */       abstractInsnNode = abstractInsnNode.next;
/*     */     }
/* 129 */     return (abstractInsnNode != null);
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
/*     */   public int indexOf(AbstractInsnNode paramAbstractInsnNode) {
/* 147 */     if (this.cache == null) {
/* 148 */       this.cache = toArray();
/*     */     }
/* 150 */     return paramAbstractInsnNode.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 160 */     AbstractInsnNode abstractInsnNode = this.first;
/* 161 */     while (abstractInsnNode != null) {
/* 162 */       abstractInsnNode.accept(paramMethodVisitor);
/* 163 */       abstractInsnNode = abstractInsnNode.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<AbstractInsnNode> iterator() {
/* 173 */     return iterator(0);
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
/*     */   public ListIterator<AbstractInsnNode> iterator(int paramInt) {
/* 186 */     return new InsnListIterator(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode[] toArray() {
/* 195 */     byte b = 0;
/* 196 */     AbstractInsnNode abstractInsnNode = this.first;
/* 197 */     AbstractInsnNode[] arrayOfAbstractInsnNode = new AbstractInsnNode[this.size];
/* 198 */     while (abstractInsnNode != null) {
/* 199 */       arrayOfAbstractInsnNode[b] = abstractInsnNode;
/* 200 */       abstractInsnNode.index = b++;
/* 201 */       abstractInsnNode = abstractInsnNode.next;
/*     */     } 
/* 203 */     return arrayOfAbstractInsnNode;
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
/*     */   public void set(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 216 */     AbstractInsnNode abstractInsnNode1 = paramAbstractInsnNode1.next;
/* 217 */     paramAbstractInsnNode2.next = abstractInsnNode1;
/* 218 */     if (abstractInsnNode1 != null) {
/* 219 */       abstractInsnNode1.prev = paramAbstractInsnNode2;
/*     */     } else {
/* 221 */       this.last = paramAbstractInsnNode2;
/*     */     } 
/* 223 */     AbstractInsnNode abstractInsnNode2 = paramAbstractInsnNode1.prev;
/* 224 */     paramAbstractInsnNode2.prev = abstractInsnNode2;
/* 225 */     if (abstractInsnNode2 != null) {
/* 226 */       abstractInsnNode2.next = paramAbstractInsnNode2;
/*     */     } else {
/* 228 */       this.first = paramAbstractInsnNode2;
/*     */     } 
/* 230 */     if (this.cache != null) {
/* 231 */       int i = paramAbstractInsnNode1.index;
/* 232 */       this.cache[i] = paramAbstractInsnNode2;
/* 233 */       paramAbstractInsnNode2.index = i;
/*     */     } else {
/* 235 */       paramAbstractInsnNode2.index = 0;
/*     */     } 
/* 237 */     paramAbstractInsnNode1.index = -1;
/* 238 */     paramAbstractInsnNode1.prev = null;
/* 239 */     paramAbstractInsnNode1.next = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(AbstractInsnNode paramAbstractInsnNode) {
/* 250 */     this.size++;
/* 251 */     if (this.last == null) {
/* 252 */       this.first = paramAbstractInsnNode;
/* 253 */       this.last = paramAbstractInsnNode;
/*     */     } else {
/* 255 */       this.last.next = paramAbstractInsnNode;
/* 256 */       paramAbstractInsnNode.prev = this.last;
/*     */     } 
/* 258 */     this.last = paramAbstractInsnNode;
/* 259 */     this.cache = null;
/* 260 */     paramAbstractInsnNode.index = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(InsnList paramInsnList) {
/* 271 */     if (paramInsnList.size == 0) {
/*     */       return;
/*     */     }
/* 274 */     this.size += paramInsnList.size;
/* 275 */     if (this.last == null) {
/* 276 */       this.first = paramInsnList.first;
/* 277 */       this.last = paramInsnList.last;
/*     */     } else {
/* 279 */       AbstractInsnNode abstractInsnNode = paramInsnList.first;
/* 280 */       this.last.next = abstractInsnNode;
/* 281 */       abstractInsnNode.prev = this.last;
/* 282 */       this.last = paramInsnList.last;
/*     */     } 
/* 284 */     this.cache = null;
/* 285 */     paramInsnList.removeAll(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insert(AbstractInsnNode paramAbstractInsnNode) {
/* 296 */     this.size++;
/* 297 */     if (this.first == null) {
/* 298 */       this.first = paramAbstractInsnNode;
/* 299 */       this.last = paramAbstractInsnNode;
/*     */     } else {
/* 301 */       this.first.prev = paramAbstractInsnNode;
/* 302 */       paramAbstractInsnNode.next = this.first;
/*     */     } 
/* 304 */     this.first = paramAbstractInsnNode;
/* 305 */     this.cache = null;
/* 306 */     paramAbstractInsnNode.index = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insert(InsnList paramInsnList) {
/* 317 */     if (paramInsnList.size == 0) {
/*     */       return;
/*     */     }
/* 320 */     this.size += paramInsnList.size;
/* 321 */     if (this.first == null) {
/* 322 */       this.first = paramInsnList.first;
/* 323 */       this.last = paramInsnList.last;
/*     */     } else {
/* 325 */       AbstractInsnNode abstractInsnNode = paramInsnList.last;
/* 326 */       this.first.prev = abstractInsnNode;
/* 327 */       abstractInsnNode.next = this.first;
/* 328 */       this.first = paramInsnList.first;
/*     */     } 
/* 330 */     this.cache = null;
/* 331 */     paramInsnList.removeAll(false);
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
/*     */   public void insert(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 346 */     this.size++;
/* 347 */     AbstractInsnNode abstractInsnNode = paramAbstractInsnNode1.next;
/* 348 */     if (abstractInsnNode == null) {
/* 349 */       this.last = paramAbstractInsnNode2;
/*     */     } else {
/* 351 */       abstractInsnNode.prev = paramAbstractInsnNode2;
/*     */     } 
/* 353 */     paramAbstractInsnNode1.next = paramAbstractInsnNode2;
/* 354 */     paramAbstractInsnNode2.next = abstractInsnNode;
/* 355 */     paramAbstractInsnNode2.prev = paramAbstractInsnNode1;
/* 356 */     this.cache = null;
/* 357 */     paramAbstractInsnNode2.index = 0;
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
/*     */   public void insert(AbstractInsnNode paramAbstractInsnNode, InsnList paramInsnList) {
/* 371 */     if (paramInsnList.size == 0) {
/*     */       return;
/*     */     }
/* 374 */     this.size += paramInsnList.size;
/* 375 */     AbstractInsnNode abstractInsnNode1 = paramInsnList.first;
/* 376 */     AbstractInsnNode abstractInsnNode2 = paramInsnList.last;
/* 377 */     AbstractInsnNode abstractInsnNode3 = paramAbstractInsnNode.next;
/* 378 */     if (abstractInsnNode3 == null) {
/* 379 */       this.last = abstractInsnNode2;
/*     */     } else {
/* 381 */       abstractInsnNode3.prev = abstractInsnNode2;
/*     */     } 
/* 383 */     paramAbstractInsnNode.next = abstractInsnNode1;
/* 384 */     abstractInsnNode2.next = abstractInsnNode3;
/* 385 */     abstractInsnNode1.prev = paramAbstractInsnNode;
/* 386 */     this.cache = null;
/* 387 */     paramInsnList.removeAll(false);
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
/*     */   public void insertBefore(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 402 */     this.size++;
/* 403 */     AbstractInsnNode abstractInsnNode = paramAbstractInsnNode1.prev;
/* 404 */     if (abstractInsnNode == null) {
/* 405 */       this.first = paramAbstractInsnNode2;
/*     */     } else {
/* 407 */       abstractInsnNode.next = paramAbstractInsnNode2;
/*     */     } 
/* 409 */     paramAbstractInsnNode1.prev = paramAbstractInsnNode2;
/* 410 */     paramAbstractInsnNode2.next = paramAbstractInsnNode1;
/* 411 */     paramAbstractInsnNode2.prev = abstractInsnNode;
/* 412 */     this.cache = null;
/* 413 */     paramAbstractInsnNode2.index = 0;
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
/*     */   public void insertBefore(AbstractInsnNode paramAbstractInsnNode, InsnList paramInsnList) {
/* 428 */     if (paramInsnList.size == 0) {
/*     */       return;
/*     */     }
/* 431 */     this.size += paramInsnList.size;
/* 432 */     AbstractInsnNode abstractInsnNode1 = paramInsnList.first;
/* 433 */     AbstractInsnNode abstractInsnNode2 = paramInsnList.last;
/* 434 */     AbstractInsnNode abstractInsnNode3 = paramAbstractInsnNode.prev;
/* 435 */     if (abstractInsnNode3 == null) {
/* 436 */       this.first = abstractInsnNode1;
/*     */     } else {
/* 438 */       abstractInsnNode3.next = abstractInsnNode1;
/*     */     } 
/* 440 */     paramAbstractInsnNode.prev = abstractInsnNode2;
/* 441 */     abstractInsnNode2.next = paramAbstractInsnNode;
/* 442 */     abstractInsnNode1.prev = abstractInsnNode3;
/* 443 */     this.cache = null;
/* 444 */     paramInsnList.removeAll(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(AbstractInsnNode paramAbstractInsnNode) {
/* 454 */     this.size--;
/* 455 */     AbstractInsnNode abstractInsnNode1 = paramAbstractInsnNode.next;
/* 456 */     AbstractInsnNode abstractInsnNode2 = paramAbstractInsnNode.prev;
/* 457 */     if (abstractInsnNode1 == null) {
/* 458 */       if (abstractInsnNode2 == null) {
/* 459 */         this.first = null;
/* 460 */         this.last = null;
/*     */       } else {
/* 462 */         abstractInsnNode2.next = null;
/* 463 */         this.last = abstractInsnNode2;
/*     */       }
/*     */     
/* 466 */     } else if (abstractInsnNode2 == null) {
/* 467 */       this.first = abstractInsnNode1;
/* 468 */       abstractInsnNode1.prev = null;
/*     */     } else {
/* 470 */       abstractInsnNode2.next = abstractInsnNode1;
/* 471 */       abstractInsnNode1.prev = abstractInsnNode2;
/*     */     } 
/*     */     
/* 474 */     this.cache = null;
/* 475 */     paramAbstractInsnNode.index = -1;
/* 476 */     paramAbstractInsnNode.prev = null;
/* 477 */     paramAbstractInsnNode.next = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeAll(boolean paramBoolean) {
/* 488 */     if (paramBoolean) {
/* 489 */       AbstractInsnNode abstractInsnNode = this.first;
/* 490 */       while (abstractInsnNode != null) {
/* 491 */         AbstractInsnNode abstractInsnNode1 = abstractInsnNode.next;
/* 492 */         abstractInsnNode.index = -1;
/* 493 */         abstractInsnNode.prev = null;
/* 494 */         abstractInsnNode.next = null;
/* 495 */         abstractInsnNode = abstractInsnNode1;
/*     */       } 
/*     */     } 
/* 498 */     this.size = 0;
/* 499 */     this.first = null;
/* 500 */     this.last = null;
/* 501 */     this.cache = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 508 */     removeAll(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetLabels() {
/* 517 */     AbstractInsnNode abstractInsnNode = this.first;
/* 518 */     while (abstractInsnNode != null) {
/* 519 */       if (abstractInsnNode instanceof LabelNode) {
/* 520 */         ((LabelNode)abstractInsnNode).resetLabel();
/*     */       }
/* 522 */       abstractInsnNode = abstractInsnNode.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final class InsnListIterator
/*     */     implements ListIterator
/*     */   {
/*     */     AbstractInsnNode next;
/*     */     
/*     */     AbstractInsnNode prev;
/*     */     
/*     */     AbstractInsnNode remove;
/*     */     
/*     */     InsnListIterator(int param1Int) {
/* 537 */       if (param1Int == InsnList.this.size()) {
/* 538 */         this.next = null;
/* 539 */         this.prev = InsnList.this.getLast();
/*     */       } else {
/* 541 */         this.next = InsnList.this.get(param1Int);
/* 542 */         this.prev = this.next.prev;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 547 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public Object next() {
/* 551 */       if (this.next == null) {
/* 552 */         throw new NoSuchElementException();
/*     */       }
/* 554 */       AbstractInsnNode abstractInsnNode = this.next;
/* 555 */       this.prev = abstractInsnNode;
/* 556 */       this.next = abstractInsnNode.next;
/* 557 */       this.remove = abstractInsnNode;
/* 558 */       return abstractInsnNode;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 562 */       if (this.remove != null) {
/* 563 */         if (this.remove == this.next) {
/* 564 */           this.next = this.next.next;
/*     */         } else {
/* 566 */           this.prev = this.prev.prev;
/*     */         } 
/* 568 */         InsnList.this.remove(this.remove);
/* 569 */         this.remove = null;
/*     */       } else {
/* 571 */         throw new IllegalStateException();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 576 */       return (this.prev != null);
/*     */     }
/*     */     
/*     */     public Object previous() {
/* 580 */       AbstractInsnNode abstractInsnNode = this.prev;
/* 581 */       this.next = abstractInsnNode;
/* 582 */       this.prev = abstractInsnNode.prev;
/* 583 */       this.remove = abstractInsnNode;
/* 584 */       return abstractInsnNode;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 588 */       if (this.next == null) {
/* 589 */         return InsnList.this.size();
/*     */       }
/* 591 */       if (InsnList.this.cache == null) {
/* 592 */         InsnList.this.cache = InsnList.this.toArray();
/*     */       }
/* 594 */       return this.next.index;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 598 */       if (this.prev == null) {
/* 599 */         return -1;
/*     */       }
/* 601 */       if (InsnList.this.cache == null) {
/* 602 */         InsnList.this.cache = InsnList.this.toArray();
/*     */       }
/* 604 */       return this.prev.index;
/*     */     }
/*     */     
/*     */     public void add(Object param1Object) {
/* 608 */       if (this.next != null) {
/* 609 */         InsnList.this.insertBefore(this.next, (AbstractInsnNode)param1Object);
/* 610 */       } else if (this.prev != null) {
/* 611 */         InsnList.this.insert(this.prev, (AbstractInsnNode)param1Object);
/*     */       } else {
/* 613 */         InsnList.this.add((AbstractInsnNode)param1Object);
/*     */       } 
/* 615 */       this.prev = (AbstractInsnNode)param1Object;
/* 616 */       this.remove = null;
/*     */     }
/*     */     
/*     */     public void set(Object param1Object) {
/* 620 */       if (this.remove != null) {
/* 621 */         InsnList.this.set(this.remove, (AbstractInsnNode)param1Object);
/* 622 */         if (this.remove == this.prev) {
/* 623 */           this.prev = (AbstractInsnNode)param1Object;
/*     */         } else {
/* 625 */           this.next = (AbstractInsnNode)param1Object;
/*     */         } 
/*     */       } else {
/* 628 */         throw new IllegalStateException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\InsnList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */