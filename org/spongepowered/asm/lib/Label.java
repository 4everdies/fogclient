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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Label
/*     */ {
/*     */   static final int DEBUG = 1;
/*     */   static final int RESOLVED = 2;
/*     */   static final int RESIZED = 4;
/*     */   static final int PUSHED = 8;
/*     */   static final int TARGET = 16;
/*     */   static final int STORE = 32;
/*     */   static final int REACHABLE = 64;
/*     */   static final int JSR = 128;
/*     */   static final int RET = 256;
/*     */   static final int SUBROUTINE = 512;
/*     */   static final int VISITED = 1024;
/*     */   static final int VISITED2 = 2048;
/*     */   public Object info;
/*     */   int status;
/*     */   int line;
/*     */   int position;
/*     */   private int referenceCount;
/*     */   private int[] srcAndRefPositions;
/*     */   int inputStackTop;
/*     */   int outputStackMax;
/*     */   Frame frame;
/*     */   Label successor;
/*     */   Edge successors;
/*     */   Label next;
/*     */   
/*     */   public int getOffset() {
/* 278 */     if ((this.status & 0x2) == 0) {
/* 279 */       throw new IllegalStateException("Label offset position has not been resolved yet");
/*     */     }
/*     */     
/* 282 */     return this.position;
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
/*     */   void put(MethodWriter paramMethodWriter, ByteVector paramByteVector, int paramInt, boolean paramBoolean) {
/* 306 */     if ((this.status & 0x2) == 0) {
/* 307 */       if (paramBoolean) {
/* 308 */         addReference(-1 - paramInt, paramByteVector.length);
/* 309 */         paramByteVector.putInt(-1);
/*     */       } else {
/* 311 */         addReference(paramInt, paramByteVector.length);
/* 312 */         paramByteVector.putShort(-1);
/*     */       }
/*     */     
/* 315 */     } else if (paramBoolean) {
/* 316 */       paramByteVector.putInt(this.position - paramInt);
/*     */     } else {
/* 318 */       paramByteVector.putShort(this.position - paramInt);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addReference(int paramInt1, int paramInt2) {
/* 338 */     if (this.srcAndRefPositions == null) {
/* 339 */       this.srcAndRefPositions = new int[6];
/*     */     }
/* 341 */     if (this.referenceCount >= this.srcAndRefPositions.length) {
/* 342 */       int[] arrayOfInt = new int[this.srcAndRefPositions.length + 6];
/* 343 */       System.arraycopy(this.srcAndRefPositions, 0, arrayOfInt, 0, this.srcAndRefPositions.length);
/*     */       
/* 345 */       this.srcAndRefPositions = arrayOfInt;
/*     */     } 
/* 347 */     this.srcAndRefPositions[this.referenceCount++] = paramInt1;
/* 348 */     this.srcAndRefPositions[this.referenceCount++] = paramInt2;
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
/*     */   boolean resolve(MethodWriter paramMethodWriter, int paramInt, byte[] paramArrayOfbyte) {
/* 375 */     boolean bool = false;
/* 376 */     this.status |= 0x2;
/* 377 */     this.position = paramInt;
/* 378 */     byte b = 0;
/* 379 */     while (b < this.referenceCount) {
/* 380 */       int i = this.srcAndRefPositions[b++];
/* 381 */       int j = this.srcAndRefPositions[b++];
/*     */       
/* 383 */       if (i >= 0) {
/* 384 */         int m = paramInt - i;
/* 385 */         if (m < -32768 || m > 32767) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 395 */           int n = paramArrayOfbyte[j - 1] & 0xFF;
/* 396 */           if (n <= 168) {
/*     */             
/* 398 */             paramArrayOfbyte[j - 1] = (byte)(n + 49);
/*     */           } else {
/*     */             
/* 401 */             paramArrayOfbyte[j - 1] = (byte)(n + 20);
/*     */           } 
/* 403 */           bool = true;
/*     */         } 
/* 405 */         paramArrayOfbyte[j++] = (byte)(m >>> 8);
/* 406 */         paramArrayOfbyte[j] = (byte)m; continue;
/*     */       } 
/* 408 */       int k = paramInt + i + 1;
/* 409 */       paramArrayOfbyte[j++] = (byte)(k >>> 24);
/* 410 */       paramArrayOfbyte[j++] = (byte)(k >>> 16);
/* 411 */       paramArrayOfbyte[j++] = (byte)(k >>> 8);
/* 412 */       paramArrayOfbyte[j] = (byte)k;
/*     */     } 
/*     */     
/* 415 */     return bool;
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
/*     */   Label getFirst() {
/* 427 */     return (this.frame == null) ? this : this.frame.owner;
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
/*     */   boolean inSubroutine(long paramLong) {
/* 442 */     if ((this.status & 0x400) != 0) {
/* 443 */       return ((this.srcAndRefPositions[(int)(paramLong >>> 32L)] & (int)paramLong) != 0);
/*     */     }
/* 445 */     return false;
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
/*     */   boolean inSameSubroutine(Label paramLabel) {
/* 458 */     if ((this.status & 0x400) == 0 || (paramLabel.status & 0x400) == 0) {
/* 459 */       return false;
/*     */     }
/* 461 */     for (byte b = 0; b < this.srcAndRefPositions.length; b++) {
/* 462 */       if ((this.srcAndRefPositions[b] & paramLabel.srcAndRefPositions[b]) != 0) {
/* 463 */         return true;
/*     */       }
/*     */     } 
/* 466 */     return false;
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
/*     */   void addToSubroutine(long paramLong, int paramInt) {
/* 478 */     if ((this.status & 0x400) == 0) {
/* 479 */       this.status |= 0x400;
/* 480 */       this.srcAndRefPositions = new int[paramInt / 32 + 1];
/*     */     } 
/* 482 */     this.srcAndRefPositions[(int)(paramLong >>> 32L)] = this.srcAndRefPositions[(int)(paramLong >>> 32L)] | (int)paramLong;
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
/*     */   void visitSubroutine(Label paramLabel, long paramLong, int paramInt) {
/* 503 */     Label label = this;
/* 504 */     while (label != null) {
/*     */       
/* 506 */       Label label1 = label;
/* 507 */       label = label1.next;
/* 508 */       label1.next = null;
/*     */       
/* 510 */       if (paramLabel != null) {
/* 511 */         if ((label1.status & 0x800) != 0) {
/*     */           continue;
/*     */         }
/* 514 */         label1.status |= 0x800;
/*     */         
/* 516 */         if ((label1.status & 0x100) != 0 && 
/* 517 */           !label1.inSameSubroutine(paramLabel)) {
/* 518 */           Edge edge1 = new Edge();
/* 519 */           edge1.info = label1.inputStackTop;
/* 520 */           edge1.successor = paramLabel.successors.successor;
/* 521 */           edge1.next = label1.successors;
/* 522 */           label1.successors = edge1;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 527 */         if (label1.inSubroutine(paramLong)) {
/*     */           continue;
/*     */         }
/*     */         
/* 531 */         label1.addToSubroutine(paramLong, paramInt);
/*     */       } 
/*     */       
/* 534 */       Edge edge = label1.successors;
/* 535 */       while (edge != null) {
/*     */ 
/*     */ 
/*     */         
/* 539 */         if ((label1.status & 0x80) == 0 || edge != label1.successors.next)
/*     */         {
/* 541 */           if (edge.successor.next == null) {
/* 542 */             edge.successor.next = label;
/* 543 */             label = edge.successor;
/*     */           } 
/*     */         }
/* 546 */         edge = edge.next;
/*     */       } 
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
/*     */ 
/*     */   
/*     */   public String toString() {
/* 562 */     return "L" + System.identityHashCode(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\Label.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */