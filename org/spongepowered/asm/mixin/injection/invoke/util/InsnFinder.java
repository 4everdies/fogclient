/*     */ package org.spongepowered.asm.mixin.injection.invoke.util;
/*     */ 
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Analyzer;
/*     */ import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
/*     */ import org.spongepowered.asm.lib.tree.analysis.BasicInterpreter;
/*     */ import org.spongepowered.asm.lib.tree.analysis.BasicValue;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Frame;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Interpreter;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Value;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InsnFinder
/*     */ {
/*     */   static class AnalysisResultException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private AbstractInsnNode result;
/*     */     
/*     */     public AnalysisResultException(AbstractInsnNode param1AbstractInsnNode) {
/*  53 */       this.result = param1AbstractInsnNode;
/*     */     }
/*     */     
/*     */     public AbstractInsnNode getResult() {
/*  57 */       return this.result;
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
/*     */   enum AnalyzerState
/*     */   {
/*  70 */     SEARCH,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     ANALYSE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     COMPLETE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class PopAnalyzer
/*     */     extends Analyzer<BasicValue>
/*     */   {
/*     */     protected final AbstractInsnNode node;
/*     */ 
/*     */ 
/*     */     
/*     */     class PopFrame
/*     */       extends Frame<BasicValue>
/*     */     {
/*     */       private AbstractInsnNode current;
/*     */ 
/*     */       
/*  99 */       private InsnFinder.AnalyzerState state = InsnFinder.AnalyzerState.SEARCH;
/* 100 */       private int depth = 0;
/*     */       
/*     */       public PopFrame(int param2Int1, int param2Int2) {
/* 103 */         super(param2Int1, param2Int2);
/*     */       }
/*     */ 
/*     */       
/*     */       public void execute(AbstractInsnNode param2AbstractInsnNode, Interpreter<BasicValue> param2Interpreter) throws AnalyzerException {
/* 108 */         this.current = param2AbstractInsnNode;
/* 109 */         super.execute(param2AbstractInsnNode, param2Interpreter);
/*     */       }
/*     */ 
/*     */       
/*     */       public void push(BasicValue param2BasicValue) throws IndexOutOfBoundsException {
/* 114 */         if (this.current == InsnFinder.PopAnalyzer.this.node && this.state == InsnFinder.AnalyzerState.SEARCH) {
/* 115 */           this.state = InsnFinder.AnalyzerState.ANALYSE;
/* 116 */           this.depth++;
/* 117 */         } else if (this.state == InsnFinder.AnalyzerState.ANALYSE) {
/* 118 */           this.depth++;
/*     */         } 
/* 120 */         super.push((Value)param2BasicValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public BasicValue pop() throws IndexOutOfBoundsException {
/* 125 */         if (this.state == InsnFinder.AnalyzerState.ANALYSE && 
/* 126 */           --this.depth == 0) {
/* 127 */           this.state = InsnFinder.AnalyzerState.COMPLETE;
/* 128 */           throw new InsnFinder.AnalysisResultException(this.current);
/*     */         } 
/*     */         
/* 131 */         return (BasicValue)super.pop();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PopAnalyzer(AbstractInsnNode param1AbstractInsnNode) {
/* 139 */       super((Interpreter)new BasicInterpreter());
/* 140 */       this.node = param1AbstractInsnNode;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Frame<BasicValue> newFrame(int param1Int1, int param1Int2) {
/* 145 */       return new PopFrame(param1Int1, param1Int2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractInsnNode findPopInsn(Target paramTarget, AbstractInsnNode paramAbstractInsnNode) {
/*     */     try {
/* 164 */       (new PopAnalyzer(paramAbstractInsnNode)).analyze(paramTarget.classNode.name, paramTarget.method);
/* 165 */     } catch (AnalyzerException analyzerException) {
/* 166 */       if (analyzerException.getCause() instanceof AnalysisResultException) {
/* 167 */         return ((AnalysisResultException)analyzerException.getCause()).getResult();
/*     */       }
/* 169 */       logger.catching((Throwable)analyzerException);
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invok\\util\InsnFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */