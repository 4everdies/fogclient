/*     */ package org.spongepowered.asm.lib.tree.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.IincInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MultiANewArrayInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Frame<V extends Value>
/*     */ {
/*     */   private V returnValue;
/*     */   private V[] values;
/*     */   private int locals;
/*     */   private int top;
/*     */   
/*     */   public Frame(int paramInt1, int paramInt2) {
/*  88 */     this.values = (V[])new Value[paramInt1 + paramInt2];
/*  89 */     this.locals = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Frame(Frame<? extends V> paramFrame) {
/*  99 */     this(paramFrame.locals, paramFrame.values.length - paramFrame.locals);
/* 100 */     init(paramFrame);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Frame<V> init(Frame<? extends V> paramFrame) {
/* 111 */     this.returnValue = paramFrame.returnValue;
/* 112 */     System.arraycopy(paramFrame.values, 0, this.values, 0, this.values.length);
/* 113 */     this.top = paramFrame.top;
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturn(V paramV) {
/* 125 */     this.returnValue = paramV;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLocals() {
/* 134 */     return this.locals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/* 143 */     return this.values.length - this.locals;
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
/*     */   public V getLocal(int paramInt) throws IndexOutOfBoundsException {
/* 156 */     if (paramInt >= this.locals) {
/* 157 */       throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
/*     */     }
/*     */     
/* 160 */     return this.values[paramInt];
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
/*     */   public void setLocal(int paramInt, V paramV) throws IndexOutOfBoundsException {
/* 175 */     if (paramInt >= this.locals) {
/* 176 */       throw new IndexOutOfBoundsException("Trying to access an inexistant local variable " + paramInt);
/*     */     }
/*     */     
/* 179 */     this.values[paramInt] = paramV;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStackSize() {
/* 189 */     return this.top;
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
/*     */   public V getStack(int paramInt) throws IndexOutOfBoundsException {
/* 202 */     return this.values[paramInt + this.locals];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearStack() {
/* 209 */     this.top = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V pop() throws IndexOutOfBoundsException {
/* 220 */     if (this.top == 0) {
/* 221 */       throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
/*     */     }
/*     */     
/* 224 */     return this.values[--this.top + this.locals];
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
/*     */   public void push(V paramV) throws IndexOutOfBoundsException {
/* 236 */     if (this.top + this.locals >= this.values.length) {
/* 237 */       throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
/*     */     }
/*     */     
/* 240 */     this.values[this.top++ + this.locals] = paramV; } public void execute(AbstractInsnNode paramAbstractInsnNode, Interpreter<V> paramInterpreter) throws AnalyzerException {
/*     */     V v1;
/*     */     V v2;
/*     */     int i;
/*     */     String str;
/*     */     int j;
/*     */     V v3;
/*     */     ArrayList<V> arrayList;
/*     */     int k;
/* 249 */     switch (paramAbstractInsnNode.getOpcode()) {
/*     */       case 0:
/*     */         return;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/* 270 */         push(paramInterpreter.newOperation(paramAbstractInsnNode));
/*     */       
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/* 277 */         push(paramInterpreter.copyOperation(paramAbstractInsnNode, 
/* 278 */               getLocal(((VarInsnNode)paramAbstractInsnNode).var)));
/*     */       
/*     */       case 46:
/*     */       case 47:
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 52:
/*     */       case 53:
/* 288 */         v1 = pop();
/* 289 */         v2 = pop();
/* 290 */         push(paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1));
/*     */       
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/* 297 */         v2 = paramInterpreter.copyOperation(paramAbstractInsnNode, pop());
/* 298 */         i = ((VarInsnNode)paramAbstractInsnNode).var;
/* 299 */         setLocal(i, v2);
/* 300 */         if (v2.getSize() == 2) {
/* 301 */           setLocal(i + 1, paramInterpreter.newValue(null));
/*     */         }
/* 303 */         if (i > 0) {
/* 304 */           V v = getLocal(i - 1);
/* 305 */           if (v != null && v.getSize() == 2) {
/* 306 */             setLocal(i - 1, paramInterpreter.newValue(null));
/*     */           }
/*     */         } 
/*     */       
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 83:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/* 318 */         v3 = pop();
/* 319 */         v1 = pop();
/* 320 */         v2 = pop();
/* 321 */         paramInterpreter.ternaryOperation(paramAbstractInsnNode, v2, v1, v3);
/*     */       
/*     */       case 87:
/* 324 */         if (pop().getSize() == 2) {
/* 325 */           throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of POP");
/*     */         }
/*     */       
/*     */       case 88:
/* 329 */         if (pop().getSize() == 1 && 
/* 330 */           pop().getSize() != 1) {
/* 331 */           throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of POP2");
/*     */         }
/*     */ 
/*     */       
/*     */       case 89:
/* 336 */         v2 = pop();
/* 337 */         if (v2.getSize() != 1) {
/* 338 */           throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP");
/*     */         }
/* 340 */         push(v2);
/* 341 */         push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/*     */       
/*     */       case 90:
/* 344 */         v2 = pop();
/* 345 */         v1 = pop();
/* 346 */         if (v2.getSize() != 1 || v1.getSize() != 1) {
/* 347 */           throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP_X1");
/*     */         }
/* 349 */         push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/* 350 */         push(v1);
/* 351 */         push(v2);
/*     */       
/*     */       case 91:
/* 354 */         v2 = pop();
/* 355 */         if (v2.getSize() == 1)
/* 356 */         { v1 = pop();
/* 357 */           if (v1.getSize() == 1)
/* 358 */           { v3 = pop();
/* 359 */             if (v3.getSize() == 1)
/* 360 */             { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/* 361 */               push(v3);
/* 362 */               push(v1);
/* 363 */               push(v2);
/*     */ 
/*     */               
/*     */                }
/*     */             
/*     */             else
/*     */             
/*     */             { 
/*     */ 
/*     */               
/* 373 */               throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP_X2"); }  } else { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); push(v1); push(v2); }  } else { throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP_X2"); } 
/*     */       case 92:
/* 375 */         v2 = pop();
/* 376 */         if (v2.getSize() == 1)
/* 377 */         { v1 = pop();
/* 378 */           if (v1.getSize() == 1)
/* 379 */           { push(v1);
/* 380 */             push(v2);
/* 381 */             push(paramInterpreter.copyOperation(paramAbstractInsnNode, v1));
/* 382 */             push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/*     */             
/*     */              }
/*     */           
/*     */           else
/*     */           
/*     */           { 
/*     */             
/* 390 */             throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2"); }  } else { push(v2); push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); } 
/*     */       case 93:
/* 392 */         v2 = pop();
/* 393 */         if (v2.getSize() == 1)
/* 394 */         { v1 = pop();
/* 395 */           if (v1.getSize() == 1)
/* 396 */           { v3 = pop();
/* 397 */             if (v3.getSize() == 1)
/* 398 */             { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v1));
/* 399 */               push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/* 400 */               push(v3);
/* 401 */               push(v1);
/* 402 */               push(v2);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */                }
/*     */             
/*     */             else
/*     */             
/*     */             { 
/*     */ 
/*     */ 
/*     */               
/* 415 */               throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X1"); }  } else { throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X1"); }  } else { v1 = pop(); if (v1.getSize() == 1) { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); push(v1); push(v2); } else { throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X1"); }  } 
/*     */       case 94:
/* 417 */         v2 = pop();
/* 418 */         if (v2.getSize() == 1)
/* 419 */         { v1 = pop();
/* 420 */           if (v1.getSize() == 1)
/* 421 */           { v3 = pop();
/* 422 */             if (v3.getSize() == 1)
/* 423 */             { V v = pop();
/* 424 */               if (v.getSize() == 1)
/* 425 */               { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v1));
/* 426 */                 push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/* 427 */                 push(v);
/* 428 */                 push(v3);
/* 429 */                 push(v1);
/* 430 */                 push(v2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                  }
/*     */               
/*     */               else
/*     */               
/*     */               { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 460 */                 throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X2"); }  } else { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v1)); push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); push(v3); push(v1); push(v2); }  } else { throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X2"); }  } else { v1 = pop(); if (v1.getSize() == 1) { v3 = pop(); if (v3.getSize() == 1) { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); push(v3); push(v1); push(v2); } else { throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of DUP2_X2"); }  } else { push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2)); push(v1); push(v2); }  } 
/*     */       case 95:
/* 462 */         v1 = pop();
/* 463 */         v2 = pop();
/* 464 */         if (v2.getSize() != 1 || v1.getSize() != 1) {
/* 465 */           throw new AnalyzerException(paramAbstractInsnNode, "Illegal use of SWAP");
/*     */         }
/* 467 */         push(paramInterpreter.copyOperation(paramAbstractInsnNode, v1));
/* 468 */         push(paramInterpreter.copyOperation(paramAbstractInsnNode, v2));
/*     */       
/*     */       case 96:
/*     */       case 97:
/*     */       case 98:
/*     */       case 99:
/*     */       case 100:
/*     */       case 101:
/*     */       case 102:
/*     */       case 103:
/*     */       case 104:
/*     */       case 105:
/*     */       case 106:
/*     */       case 107:
/*     */       case 108:
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 115:
/* 490 */         v1 = pop();
/* 491 */         v2 = pop();
/* 492 */         push(paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1));
/*     */       
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/* 498 */         push(paramInterpreter.unaryOperation(paramAbstractInsnNode, pop()));
/*     */       
/*     */       case 120:
/*     */       case 121:
/*     */       case 122:
/*     */       case 123:
/*     */       case 124:
/*     */       case 125:
/*     */       case 126:
/*     */       case 127:
/*     */       case 128:
/*     */       case 129:
/*     */       case 130:
/*     */       case 131:
/* 512 */         v1 = pop();
/* 513 */         v2 = pop();
/* 514 */         push(paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1));
/*     */       
/*     */       case 132:
/* 517 */         i = ((IincInsnNode)paramAbstractInsnNode).var;
/* 518 */         setLocal(i, paramInterpreter.unaryOperation(paramAbstractInsnNode, getLocal(i)));
/*     */       
/*     */       case 133:
/*     */       case 134:
/*     */       case 135:
/*     */       case 136:
/*     */       case 137:
/*     */       case 138:
/*     */       case 139:
/*     */       case 140:
/*     */       case 141:
/*     */       case 142:
/*     */       case 143:
/*     */       case 144:
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/* 535 */         push(paramInterpreter.unaryOperation(paramAbstractInsnNode, pop()));
/*     */       
/*     */       case 148:
/*     */       case 149:
/*     */       case 150:
/*     */       case 151:
/*     */       case 152:
/* 542 */         v1 = pop();
/* 543 */         v2 = pop();
/* 544 */         push(paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1));
/*     */       
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/* 552 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */       
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/* 562 */         v1 = pop();
/* 563 */         v2 = pop();
/* 564 */         paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1);
/*     */       
/*     */       case 167:
/*     */         return;
/*     */       case 168:
/* 569 */         push(paramInterpreter.newOperation(paramAbstractInsnNode));
/*     */       
/*     */       case 169:
/*     */         return;
/*     */       case 170:
/*     */       case 171:
/* 575 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */       
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/* 582 */         v2 = pop();
/* 583 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, v2);
/* 584 */         paramInterpreter.returnOperation(paramAbstractInsnNode, v2, this.returnValue);
/*     */       
/*     */       case 177:
/* 587 */         if (this.returnValue != null) {
/* 588 */           throw new AnalyzerException(paramAbstractInsnNode, "Incompatible return type");
/*     */         }
/*     */       
/*     */       case 178:
/* 592 */         push(paramInterpreter.newOperation(paramAbstractInsnNode));
/*     */       
/*     */       case 179:
/* 595 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */       
/*     */       case 180:
/* 598 */         push(paramInterpreter.unaryOperation(paramAbstractInsnNode, pop()));
/*     */       
/*     */       case 181:
/* 601 */         v1 = pop();
/* 602 */         v2 = pop();
/* 603 */         paramInterpreter.binaryOperation(paramAbstractInsnNode, v2, v1);
/*     */       
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 185:
/* 609 */         arrayList = new ArrayList();
/* 610 */         str = ((MethodInsnNode)paramAbstractInsnNode).desc;
/* 611 */         for (k = (Type.getArgumentTypes(str)).length; k > 0; k--) {
/* 612 */           arrayList.add(0, pop());
/*     */         }
/* 614 */         if (paramAbstractInsnNode.getOpcode() != 184) {
/* 615 */           arrayList.add(0, pop());
/*     */         }
/* 617 */         if (Type.getReturnType(str) == Type.VOID_TYPE) {
/* 618 */           paramInterpreter.naryOperation(paramAbstractInsnNode, arrayList);
/*     */         } else {
/* 620 */           push(paramInterpreter.naryOperation(paramAbstractInsnNode, arrayList));
/*     */         } 
/*     */ 
/*     */       
/*     */       case 186:
/* 625 */         arrayList = new ArrayList<V>();
/* 626 */         str = ((InvokeDynamicInsnNode)paramAbstractInsnNode).desc;
/* 627 */         for (k = (Type.getArgumentTypes(str)).length; k > 0; k--) {
/* 628 */           arrayList.add(0, pop());
/*     */         }
/* 630 */         if (Type.getReturnType(str) == Type.VOID_TYPE) {
/* 631 */           paramInterpreter.naryOperation(paramAbstractInsnNode, arrayList);
/*     */         } else {
/* 633 */           push(paramInterpreter.naryOperation(paramAbstractInsnNode, arrayList));
/*     */         } 
/*     */ 
/*     */       
/*     */       case 187:
/* 638 */         push(paramInterpreter.newOperation(paramAbstractInsnNode));
/*     */       
/*     */       case 188:
/*     */       case 189:
/*     */       case 190:
/* 643 */         push(paramInterpreter.unaryOperation(paramAbstractInsnNode, pop()));
/*     */       
/*     */       case 191:
/* 646 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */       
/*     */       case 192:
/*     */       case 193:
/* 650 */         push(paramInterpreter.unaryOperation(paramAbstractInsnNode, pop()));
/*     */       
/*     */       case 194:
/*     */       case 195:
/* 654 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */       
/*     */       case 197:
/* 657 */         arrayList = new ArrayList<V>();
/* 658 */         for (j = ((MultiANewArrayInsnNode)paramAbstractInsnNode).dims; j > 0; j--) {
/* 659 */           arrayList.add(0, pop());
/*     */         }
/* 661 */         push(paramInterpreter.naryOperation(paramAbstractInsnNode, arrayList));
/*     */       
/*     */       case 198:
/*     */       case 199:
/* 665 */         paramInterpreter.unaryOperation(paramAbstractInsnNode, pop());
/*     */     } 
/*     */     
/* 668 */     throw new RuntimeException("Illegal opcode " + paramAbstractInsnNode.getOpcode());
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
/*     */   public boolean merge(Frame<? extends V> paramFrame, Interpreter<V> paramInterpreter) throws AnalyzerException {
/* 686 */     if (this.top != paramFrame.top) {
/* 687 */       throw new AnalyzerException(null, "Incompatible stack heights");
/*     */     }
/* 689 */     boolean bool = false;
/* 690 */     for (byte b = 0; b < this.locals + this.top; b++) {
/* 691 */       V v = paramInterpreter.merge(this.values[b], paramFrame.values[b]);
/* 692 */       if (!v.equals(this.values[b])) {
/* 693 */         this.values[b] = v;
/* 694 */         bool = true;
/*     */       } 
/*     */     } 
/* 697 */     return bool;
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
/*     */   public boolean merge(Frame<? extends V> paramFrame, boolean[] paramArrayOfboolean) {
/* 712 */     boolean bool = false;
/* 713 */     for (byte b = 0; b < this.locals; b++) {
/* 714 */       if (!paramArrayOfboolean[b] && !this.values[b].equals(paramFrame.values[b])) {
/* 715 */         this.values[b] = paramFrame.values[b];
/* 716 */         bool = true;
/*     */       } 
/*     */     } 
/* 719 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 729 */     StringBuilder stringBuilder = new StringBuilder(); byte b;
/* 730 */     for (b = 0; b < getLocals(); b++) {
/* 731 */       stringBuilder.append(getLocal(b));
/*     */     }
/* 733 */     stringBuilder.append(' ');
/* 734 */     for (b = 0; b < getStackSize(); b++) {
/* 735 */       stringBuilder.append(getStack(b).toString());
/*     */     }
/* 737 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\Frame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */