/*     */ package org.spongepowered.asm.mixin.injection.code;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.Slice;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MethodSlice
/*     */ {
/*     */   static final class InsnListSlice
/*     */     extends ReadOnlyInsnList
/*     */   {
/*     */     private final int start;
/*     */     private final int end;
/*     */     
/*     */     static class SliceIterator
/*     */       implements ListIterator<AbstractInsnNode>
/*     */     {
/*     */       private final ListIterator<AbstractInsnNode> iter;
/*     */       private int start;
/*     */       private int end;
/*     */       private int index;
/*     */       
/*     */       public SliceIterator(ListIterator<AbstractInsnNode> param2ListIterator, int param2Int1, int param2Int2, int param2Int3) {
/*  89 */         this.iter = param2ListIterator;
/*  90 */         this.start = param2Int1;
/*  91 */         this.end = param2Int2;
/*  92 */         this.index = param2Int3;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 100 */         return (this.index <= this.end && this.iter.hasNext());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public AbstractInsnNode next() {
/* 108 */         if (this.index > this.end) {
/* 109 */           throw new NoSuchElementException();
/*     */         }
/* 111 */         this.index++;
/* 112 */         return this.iter.next();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean hasPrevious() {
/* 120 */         return (this.index > this.start);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public AbstractInsnNode previous() {
/* 128 */         if (this.index <= this.start) {
/* 129 */           throw new NoSuchElementException();
/*     */         }
/* 131 */         this.index--;
/* 132 */         return this.iter.previous();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int nextIndex() {
/* 140 */         return this.index - this.start;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int previousIndex() {
/* 148 */         return this.index - this.start - 1;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void remove() {
/* 156 */         throw new UnsupportedOperationException("Cannot remove insn from slice");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void set(AbstractInsnNode param2AbstractInsnNode) {
/* 164 */         throw new UnsupportedOperationException("Cannot set insn using slice");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void add(AbstractInsnNode param2AbstractInsnNode) {
/* 172 */         throw new UnsupportedOperationException("Cannot add insn using slice");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected InsnListSlice(InsnList param1InsnList, int param1Int1, int param1Int2) {
/* 182 */       super(param1InsnList);
/*     */ 
/*     */       
/* 185 */       this.start = param1Int1;
/* 186 */       this.end = param1Int2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListIterator<AbstractInsnNode> iterator() {
/* 195 */       return iterator(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListIterator<AbstractInsnNode> iterator(int param1Int) {
/* 205 */       return new SliceIterator(super.iterator(this.start + param1Int), this.start, this.end, this.start + param1Int);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode[] toArray() {
/* 214 */       AbstractInsnNode[] arrayOfAbstractInsnNode1 = super.toArray();
/* 215 */       AbstractInsnNode[] arrayOfAbstractInsnNode2 = new AbstractInsnNode[size()];
/* 216 */       System.arraycopy(arrayOfAbstractInsnNode1, this.start, arrayOfAbstractInsnNode2, 0, arrayOfAbstractInsnNode2.length);
/* 217 */       return arrayOfAbstractInsnNode2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 226 */       return this.end - this.start + 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode getFirst() {
/* 235 */       return super.get(this.start);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode getLast() {
/* 244 */       return super.get(this.end);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode get(int param1Int) {
/* 253 */       return super.get(this.start + param1Int);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(AbstractInsnNode param1AbstractInsnNode) {
/* 262 */       for (AbstractInsnNode abstractInsnNode : toArray()) {
/* 263 */         if (abstractInsnNode == param1AbstractInsnNode) {
/* 264 */           return true;
/*     */         }
/*     */       } 
/* 267 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(AbstractInsnNode param1AbstractInsnNode) {
/* 280 */       int i = super.indexOf(param1AbstractInsnNode);
/* 281 */       return (i >= this.start && i <= this.end) ? (i - this.start) : -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int realIndexOf(AbstractInsnNode param1AbstractInsnNode) {
/* 291 */       return super.indexOf(param1AbstractInsnNode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 299 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ISliceContext owner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InjectionPoint from;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InjectionPoint to;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodSlice(ISliceContext paramISliceContext, String paramString, InjectionPoint paramInjectionPoint1, InjectionPoint paramInjectionPoint2) {
/* 338 */     if (paramInjectionPoint1 == null && paramInjectionPoint2 == null) {
/* 339 */       throw new InvalidSliceException(paramISliceContext, String.format("%s is redundant. No 'from' or 'to' value specified", new Object[] { this }));
/*     */     }
/*     */     
/* 342 */     this.owner = paramISliceContext;
/* 343 */     this.id = Strings.nullToEmpty(paramString);
/* 344 */     this.from = paramInjectionPoint1;
/* 345 */     this.to = paramInjectionPoint2;
/* 346 */     this.name = getSliceName(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 353 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyInsnList getSlice(MethodNode paramMethodNode) {
/* 363 */     int i = paramMethodNode.instructions.size() - 1;
/* 364 */     int j = find(paramMethodNode, this.from, 0, 0, this.name + "(from)");
/* 365 */     int k = find(paramMethodNode, this.to, i, j, this.name + "(to)");
/*     */     
/* 367 */     if (j > k) {
/* 368 */       throw new InvalidSliceException(this.owner, String.format("%s is negative size. Range(%d -> %d)", new Object[] { describe(), Integer.valueOf(j), Integer.valueOf(k) }));
/*     */     }
/*     */     
/* 371 */     if (j < 0 || k < 0 || j > i || k > i) {
/* 372 */       throw new InjectionError("Unexpected critical error in " + this + ": out of bounds start=" + j + " end=" + k + " lim=" + i);
/*     */     }
/*     */     
/* 375 */     if (j == 0 && k == i) {
/* 376 */       return new ReadOnlyInsnList(paramMethodNode.instructions);
/*     */     }
/*     */     
/* 379 */     return new InsnListSlice(paramMethodNode.instructions, j, k);
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
/*     */   private int find(MethodNode paramMethodNode, InjectionPoint paramInjectionPoint, int paramInt1, int paramInt2, String paramString) {
/* 396 */     if (paramInjectionPoint == null) {
/* 397 */       return paramInt1;
/*     */     }
/*     */     
/* 400 */     LinkedList<AbstractInsnNode> linkedList = new LinkedList();
/* 401 */     ReadOnlyInsnList readOnlyInsnList = new ReadOnlyInsnList(paramMethodNode.instructions);
/* 402 */     boolean bool = paramInjectionPoint.find(paramMethodNode.desc, readOnlyInsnList, linkedList);
/* 403 */     InjectionPoint.Selector selector = paramInjectionPoint.getSelector();
/* 404 */     if (linkedList.size() != 1 && selector == InjectionPoint.Selector.ONE) {
/* 405 */       throw new InvalidSliceException(this.owner, String.format("%s requires 1 result but found %d", new Object[] { describe(paramString), Integer.valueOf(linkedList.size()) }));
/*     */     }
/*     */     
/* 408 */     if (!bool) {
/* 409 */       if (this.owner.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/* 410 */         logger.warn("{} did not match any instructions", new Object[] { describe(paramString) });
/*     */       }
/* 412 */       return paramInt2;
/*     */     } 
/*     */     
/* 415 */     return paramMethodNode.instructions.indexOf((selector == InjectionPoint.Selector.FIRST) ? linkedList.getFirst() : linkedList.getLast());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 423 */     return describe();
/*     */   }
/*     */   
/*     */   private String describe() {
/* 427 */     return describe(this.name);
/*     */   }
/*     */   
/*     */   private String describe(String paramString) {
/* 431 */     return describeSlice(paramString, this.owner);
/*     */   }
/*     */   
/*     */   private static String describeSlice(String paramString, ISliceContext paramISliceContext) {
/* 435 */     String str = Bytecode.getSimpleName(paramISliceContext.getAnnotation());
/* 436 */     MethodNode methodNode = paramISliceContext.getMethod();
/* 437 */     return String.format("%s->%s(%s)::%s%s", new Object[] { paramISliceContext.getContext(), str, paramString, methodNode.name, methodNode.desc });
/*     */   }
/*     */   
/*     */   private static String getSliceName(String paramString) {
/* 441 */     return String.format("@Slice[%s]", new Object[] { Strings.nullToEmpty(paramString) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodSlice parse(ISliceContext paramISliceContext, Slice paramSlice) {
/* 452 */     String str = paramSlice.id();
/*     */     
/* 454 */     At at1 = paramSlice.from();
/* 455 */     At at2 = paramSlice.to();
/*     */     
/* 457 */     InjectionPoint injectionPoint1 = (at1 != null) ? InjectionPoint.parse(paramISliceContext, at1) : null;
/* 458 */     InjectionPoint injectionPoint2 = (at2 != null) ? InjectionPoint.parse(paramISliceContext, at2) : null;
/*     */     
/* 460 */     return new MethodSlice(paramISliceContext, str, injectionPoint1, injectionPoint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodSlice parse(ISliceContext paramISliceContext, AnnotationNode paramAnnotationNode) {
/* 471 */     String str = (String)Annotations.getValue(paramAnnotationNode, "id");
/*     */     
/* 473 */     AnnotationNode annotationNode1 = (AnnotationNode)Annotations.getValue(paramAnnotationNode, "from");
/* 474 */     AnnotationNode annotationNode2 = (AnnotationNode)Annotations.getValue(paramAnnotationNode, "to");
/*     */     
/* 476 */     InjectionPoint injectionPoint1 = (annotationNode1 != null) ? InjectionPoint.parse(paramISliceContext, annotationNode1) : null;
/* 477 */     InjectionPoint injectionPoint2 = (annotationNode2 != null) ? InjectionPoint.parse(paramISliceContext, annotationNode2) : null;
/*     */     
/* 479 */     return new MethodSlice(paramISliceContext, str, injectionPoint1, injectionPoint2);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\code\MethodSlice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */