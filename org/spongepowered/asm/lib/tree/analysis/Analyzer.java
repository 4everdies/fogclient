/*     */ package org.spongepowered.asm.lib.tree.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Opcodes;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.IincInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LabelNode;
/*     */ import org.spongepowered.asm.lib.tree.LookupSwitchInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.TableSwitchInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.TryCatchBlockNode;
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
/*     */ public class Analyzer<V extends Value>
/*     */   implements Opcodes
/*     */ {
/*     */   private final Interpreter<V> interpreter;
/*     */   private int n;
/*     */   private InsnList insns;
/*     */   private List<TryCatchBlockNode>[] handlers;
/*     */   private Frame<V>[] frames;
/*     */   private Subroutine[] subroutines;
/*     */   private boolean[] queued;
/*     */   private int[] queue;
/*     */   private int top;
/*     */   
/*     */   public Analyzer(Interpreter<V> paramInterpreter) {
/*  87 */     this.interpreter = paramInterpreter;
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
/*     */   public Frame<V>[] analyze(String paramString, MethodNode paramMethodNode) throws AnalyzerException {
/* 108 */     if ((paramMethodNode.access & 0x500) != 0) {
/* 109 */       this.frames = (Frame<V>[])new Frame[0];
/* 110 */       return this.frames;
/*     */     } 
/* 112 */     this.n = paramMethodNode.instructions.size();
/* 113 */     this.insns = paramMethodNode.instructions;
/* 114 */     this.handlers = (List<TryCatchBlockNode>[])new List[this.n];
/* 115 */     this.frames = (Frame<V>[])new Frame[this.n];
/* 116 */     this.subroutines = new Subroutine[this.n];
/* 117 */     this.queued = new boolean[this.n];
/* 118 */     this.queue = new int[this.n];
/* 119 */     this.top = 0;
/*     */ 
/*     */     
/* 122 */     for (byte b1 = 0; b1 < paramMethodNode.tryCatchBlocks.size(); b1++) {
/* 123 */       TryCatchBlockNode tryCatchBlockNode = paramMethodNode.tryCatchBlocks.get(b1);
/* 124 */       int j = this.insns.indexOf((AbstractInsnNode)tryCatchBlockNode.start);
/* 125 */       int k = this.insns.indexOf((AbstractInsnNode)tryCatchBlockNode.end);
/* 126 */       for (int m = j; m < k; m++) {
/* 127 */         List<TryCatchBlockNode> list = this.handlers[m];
/* 128 */         if (list == null) {
/* 129 */           list = new ArrayList<TryCatchBlockNode>();
/* 130 */           this.handlers[m] = list;
/*     */         } 
/* 132 */         list.add(tryCatchBlockNode);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 137 */     Subroutine subroutine = new Subroutine(null, paramMethodNode.maxLocals, null);
/* 138 */     ArrayList<AbstractInsnNode> arrayList = new ArrayList();
/* 139 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 140 */     findSubroutine(0, subroutine, arrayList);
/* 141 */     while (!arrayList.isEmpty()) {
/* 142 */       JumpInsnNode jumpInsnNode = (JumpInsnNode)arrayList.remove(0);
/* 143 */       Subroutine subroutine1 = (Subroutine)hashMap.get(jumpInsnNode.label);
/* 144 */       if (subroutine1 == null) {
/* 145 */         subroutine1 = new Subroutine(jumpInsnNode.label, paramMethodNode.maxLocals, jumpInsnNode);
/* 146 */         hashMap.put(jumpInsnNode.label, subroutine1);
/* 147 */         findSubroutine(this.insns.indexOf((AbstractInsnNode)jumpInsnNode.label), subroutine1, arrayList); continue;
/*     */       } 
/* 149 */       subroutine1.callers.add(jumpInsnNode);
/*     */     } 
/*     */     
/* 152 */     for (byte b2 = 0; b2 < this.n; b2++) {
/* 153 */       if (this.subroutines[b2] != null && (this.subroutines[b2]).start == null) {
/* 154 */         this.subroutines[b2] = null;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 159 */     Frame<V> frame1 = newFrame(paramMethodNode.maxLocals, paramMethodNode.maxStack);
/* 160 */     Frame<V> frame2 = newFrame(paramMethodNode.maxLocals, paramMethodNode.maxStack);
/* 161 */     frame1.setReturn(this.interpreter.newValue(Type.getReturnType(paramMethodNode.desc)));
/* 162 */     Type[] arrayOfType = Type.getArgumentTypes(paramMethodNode.desc);
/* 163 */     byte b3 = 0;
/* 164 */     if ((paramMethodNode.access & 0x8) == 0) {
/* 165 */       Type type = Type.getObjectType(paramString);
/* 166 */       frame1.setLocal(b3++, this.interpreter.newValue(type));
/*     */     }  int i;
/* 168 */     for (i = 0; i < arrayOfType.length; i++) {
/* 169 */       frame1.setLocal(b3++, this.interpreter.newValue(arrayOfType[i]));
/* 170 */       if (arrayOfType[i].getSize() == 2) {
/* 171 */         frame1.setLocal(b3++, this.interpreter.newValue(null));
/*     */       }
/*     */     } 
/* 174 */     while (b3 < paramMethodNode.maxLocals) {
/* 175 */       frame1.setLocal(b3++, this.interpreter.newValue(null));
/*     */     }
/* 177 */     merge(0, frame1, null);
/*     */     
/* 179 */     init(paramString, paramMethodNode);
/*     */ 
/*     */     
/* 182 */     while (this.top > 0) {
/* 183 */       i = this.queue[--this.top];
/* 184 */       Frame<V> frame = this.frames[i];
/* 185 */       Subroutine subroutine1 = this.subroutines[i];
/* 186 */       this.queued[i] = false;
/*     */       
/* 188 */       AbstractInsnNode abstractInsnNode = null;
/*     */       try {
/* 190 */         abstractInsnNode = paramMethodNode.instructions.get(i);
/* 191 */         int j = abstractInsnNode.getOpcode();
/* 192 */         int k = abstractInsnNode.getType();
/*     */         
/* 194 */         if (k == 8 || k == 15 || k == 14) {
/*     */ 
/*     */           
/* 197 */           merge(i + 1, frame, subroutine1);
/* 198 */           newControlFlowEdge(i, i + 1);
/*     */         } else {
/* 200 */           frame1.init(frame).execute(abstractInsnNode, this.interpreter);
/* 201 */           subroutine1 = (subroutine1 == null) ? null : subroutine1.copy();
/*     */           
/* 203 */           if (abstractInsnNode instanceof JumpInsnNode) {
/* 204 */             JumpInsnNode jumpInsnNode = (JumpInsnNode)abstractInsnNode;
/* 205 */             if (j != 167 && j != 168) {
/* 206 */               merge(i + 1, frame1, subroutine1);
/* 207 */               newControlFlowEdge(i, i + 1);
/*     */             } 
/* 209 */             int m = this.insns.indexOf((AbstractInsnNode)jumpInsnNode.label);
/* 210 */             if (j == 168) {
/* 211 */               merge(m, frame1, new Subroutine(jumpInsnNode.label, paramMethodNode.maxLocals, jumpInsnNode));
/*     */             } else {
/*     */               
/* 214 */               merge(m, frame1, subroutine1);
/*     */             } 
/* 216 */             newControlFlowEdge(i, m);
/* 217 */           } else if (abstractInsnNode instanceof LookupSwitchInsnNode) {
/* 218 */             LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode)abstractInsnNode;
/* 219 */             int m = this.insns.indexOf((AbstractInsnNode)lookupSwitchInsnNode.dflt);
/* 220 */             merge(m, frame1, subroutine1);
/* 221 */             newControlFlowEdge(i, m);
/* 222 */             for (byte b = 0; b < lookupSwitchInsnNode.labels.size(); b++) {
/* 223 */               LabelNode labelNode = lookupSwitchInsnNode.labels.get(b);
/* 224 */               m = this.insns.indexOf((AbstractInsnNode)labelNode);
/* 225 */               merge(m, frame1, subroutine1);
/* 226 */               newControlFlowEdge(i, m);
/*     */             } 
/* 228 */           } else if (abstractInsnNode instanceof TableSwitchInsnNode) {
/* 229 */             TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)abstractInsnNode;
/* 230 */             int m = this.insns.indexOf((AbstractInsnNode)tableSwitchInsnNode.dflt);
/* 231 */             merge(m, frame1, subroutine1);
/* 232 */             newControlFlowEdge(i, m);
/* 233 */             for (byte b = 0; b < tableSwitchInsnNode.labels.size(); b++) {
/* 234 */               LabelNode labelNode = tableSwitchInsnNode.labels.get(b);
/* 235 */               m = this.insns.indexOf((AbstractInsnNode)labelNode);
/* 236 */               merge(m, frame1, subroutine1);
/* 237 */               newControlFlowEdge(i, m);
/*     */             } 
/* 239 */           } else if (j == 169) {
/* 240 */             if (subroutine1 == null) {
/* 241 */               throw new AnalyzerException(abstractInsnNode, "RET instruction outside of a sub routine");
/*     */             }
/*     */             
/* 244 */             for (byte b = 0; b < subroutine1.callers.size(); b++) {
/* 245 */               JumpInsnNode jumpInsnNode = subroutine1.callers.get(b);
/* 246 */               int m = this.insns.indexOf((AbstractInsnNode)jumpInsnNode);
/* 247 */               if (this.frames[m] != null) {
/* 248 */                 merge(m + 1, this.frames[m], frame1, this.subroutines[m], subroutine1.access);
/*     */                 
/* 250 */                 newControlFlowEdge(i, m + 1);
/*     */               } 
/*     */             } 
/* 253 */           } else if (j != 191 && (j < 172 || j > 177)) {
/*     */             
/* 255 */             if (subroutine1 != null) {
/* 256 */               if (abstractInsnNode instanceof VarInsnNode) {
/* 257 */                 int m = ((VarInsnNode)abstractInsnNode).var;
/* 258 */                 subroutine1.access[m] = true;
/* 259 */                 if (j == 22 || j == 24 || j == 55 || j == 57)
/*     */                 {
/*     */                   
/* 262 */                   subroutine1.access[m + 1] = true;
/*     */                 }
/* 264 */               } else if (abstractInsnNode instanceof IincInsnNode) {
/* 265 */                 int m = ((IincInsnNode)abstractInsnNode).var;
/* 266 */                 subroutine1.access[m] = true;
/*     */               } 
/*     */             }
/* 269 */             merge(i + 1, frame1, subroutine1);
/* 270 */             newControlFlowEdge(i, i + 1);
/*     */           } 
/*     */         } 
/*     */         
/* 274 */         List<TryCatchBlockNode> list = this.handlers[i];
/* 275 */         if (list != null) {
/* 276 */           for (byte b = 0; b < list.size(); b++) {
/* 277 */             Type type; TryCatchBlockNode tryCatchBlockNode = list.get(b);
/*     */             
/* 279 */             if (tryCatchBlockNode.type == null) {
/* 280 */               type = Type.getObjectType("java/lang/Throwable");
/*     */             } else {
/* 282 */               type = Type.getObjectType(tryCatchBlockNode.type);
/*     */             } 
/* 284 */             int m = this.insns.indexOf((AbstractInsnNode)tryCatchBlockNode.handler);
/* 285 */             if (newControlFlowExceptionEdge(i, tryCatchBlockNode)) {
/* 286 */               frame2.init(frame);
/* 287 */               frame2.clearStack();
/* 288 */               frame2.push(this.interpreter.newValue(type));
/* 289 */               merge(m, frame2, subroutine1);
/*     */             } 
/*     */           } 
/*     */         }
/* 293 */       } catch (AnalyzerException analyzerException) {
/* 294 */         throw new AnalyzerException(analyzerException.node, "Error at instruction " + i + ": " + analyzerException
/* 295 */             .getMessage(), analyzerException);
/* 296 */       } catch (Exception exception) {
/* 297 */         throw new AnalyzerException(abstractInsnNode, "Error at instruction " + i + ": " + exception
/* 298 */             .getMessage(), exception);
/*     */       } 
/*     */     } 
/*     */     
/* 302 */     return this.frames;
/*     */   }
/*     */ 
/*     */   
/*     */   private void findSubroutine(int paramInt, Subroutine paramSubroutine, List<AbstractInsnNode> paramList) throws AnalyzerException {
/*     */     while (true) {
/* 308 */       if (paramInt < 0 || paramInt >= this.n) {
/* 309 */         throw new AnalyzerException(null, "Execution can fall off end of the code");
/*     */       }
/*     */       
/* 312 */       if (this.subroutines[paramInt] != null) {
/*     */         return;
/*     */       }
/* 315 */       this.subroutines[paramInt] = paramSubroutine.copy();
/* 316 */       AbstractInsnNode abstractInsnNode = this.insns.get(paramInt);
/*     */ 
/*     */       
/* 319 */       if (abstractInsnNode instanceof JumpInsnNode) {
/* 320 */         if (abstractInsnNode.getOpcode() == 168) {
/*     */           
/* 322 */           paramList.add(abstractInsnNode);
/*     */         } else {
/* 324 */           JumpInsnNode jumpInsnNode = (JumpInsnNode)abstractInsnNode;
/* 325 */           findSubroutine(this.insns.indexOf((AbstractInsnNode)jumpInsnNode.label), paramSubroutine, paramList);
/*     */         } 
/* 327 */       } else if (abstractInsnNode instanceof TableSwitchInsnNode) {
/* 328 */         TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)abstractInsnNode;
/* 329 */         findSubroutine(this.insns.indexOf((AbstractInsnNode)tableSwitchInsnNode.dflt), paramSubroutine, paramList);
/* 330 */         for (int i = tableSwitchInsnNode.labels.size() - 1; i >= 0; i--) {
/* 331 */           LabelNode labelNode = tableSwitchInsnNode.labels.get(i);
/* 332 */           findSubroutine(this.insns.indexOf((AbstractInsnNode)labelNode), paramSubroutine, paramList);
/*     */         } 
/* 334 */       } else if (abstractInsnNode instanceof LookupSwitchInsnNode) {
/* 335 */         LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode)abstractInsnNode;
/* 336 */         findSubroutine(this.insns.indexOf((AbstractInsnNode)lookupSwitchInsnNode.dflt), paramSubroutine, paramList);
/* 337 */         for (int i = lookupSwitchInsnNode.labels.size() - 1; i >= 0; i--) {
/* 338 */           LabelNode labelNode = lookupSwitchInsnNode.labels.get(i);
/* 339 */           findSubroutine(this.insns.indexOf((AbstractInsnNode)labelNode), paramSubroutine, paramList);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 344 */       List<TryCatchBlockNode> list = this.handlers[paramInt];
/* 345 */       if (list != null) {
/* 346 */         for (byte b = 0; b < list.size(); b++) {
/* 347 */           TryCatchBlockNode tryCatchBlockNode = list.get(b);
/* 348 */           findSubroutine(this.insns.indexOf((AbstractInsnNode)tryCatchBlockNode.handler), paramSubroutine, paramList);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 353 */       switch (abstractInsnNode.getOpcode()) {
/*     */         case 167:
/*     */         case 169:
/*     */         case 170:
/*     */         case 171:
/*     */         case 172:
/*     */         case 173:
/*     */         case 174:
/*     */         case 175:
/*     */         case 176:
/*     */         case 177:
/*     */         case 191:
/*     */           return;
/*     */       } 
/* 367 */       paramInt++;
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
/*     */   public Frame<V>[] getFrames() {
/* 383 */     return this.frames;
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
/*     */   public List<TryCatchBlockNode> getHandlers(int paramInt) {
/* 395 */     return this.handlers[paramInt];
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
/*     */   protected void init(String paramString, MethodNode paramMethodNode) throws AnalyzerException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Frame<V> newFrame(int paramInt1, int paramInt2) {
/* 423 */     return new Frame<V>(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Frame<V> newFrame(Frame<? extends V> paramFrame) {
/* 434 */     return new Frame<V>(paramFrame);
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
/*     */   protected void newControlFlowEdge(int paramInt1, int paramInt2) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean newControlFlowExceptionEdge(int paramInt1, int paramInt2) {
/* 468 */     return true;
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
/*     */   protected boolean newControlFlowExceptionEdge(int paramInt, TryCatchBlockNode paramTryCatchBlockNode) {
/* 492 */     return newControlFlowExceptionEdge(paramInt, this.insns.indexOf((AbstractInsnNode)paramTryCatchBlockNode.handler));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void merge(int paramInt, Frame<V> paramFrame, Subroutine paramSubroutine) throws AnalyzerException {
/*     */     boolean bool;
/* 499 */     Frame<V> frame = this.frames[paramInt];
/* 500 */     Subroutine subroutine = this.subroutines[paramInt];
/*     */ 
/*     */     
/* 503 */     if (frame == null) {
/* 504 */       this.frames[paramInt] = newFrame(paramFrame);
/* 505 */       bool = true;
/*     */     } else {
/* 507 */       bool = frame.merge(paramFrame, this.interpreter);
/*     */     } 
/*     */     
/* 510 */     if (subroutine == null) {
/* 511 */       if (paramSubroutine != null) {
/* 512 */         this.subroutines[paramInt] = paramSubroutine.copy();
/* 513 */         bool = true;
/*     */       }
/*     */     
/* 516 */     } else if (paramSubroutine != null) {
/* 517 */       bool |= subroutine.merge(paramSubroutine);
/*     */     } 
/*     */     
/* 520 */     if (bool && !this.queued[paramInt]) {
/* 521 */       this.queued[paramInt] = true;
/* 522 */       this.queue[this.top++] = paramInt;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void merge(int paramInt, Frame<V> paramFrame1, Frame<V> paramFrame2, Subroutine paramSubroutine, boolean[] paramArrayOfboolean) throws AnalyzerException {
/*     */     boolean bool;
/* 529 */     Frame<V> frame = this.frames[paramInt];
/* 530 */     Subroutine subroutine = this.subroutines[paramInt];
/*     */ 
/*     */     
/* 533 */     paramFrame2.merge(paramFrame1, paramArrayOfboolean);
/*     */     
/* 535 */     if (frame == null) {
/* 536 */       this.frames[paramInt] = newFrame(paramFrame2);
/* 537 */       bool = true;
/*     */     } else {
/* 539 */       bool = frame.merge(paramFrame2, this.interpreter);
/*     */     } 
/*     */     
/* 542 */     if (subroutine != null && paramSubroutine != null) {
/* 543 */       bool |= subroutine.merge(paramSubroutine);
/*     */     }
/* 545 */     if (bool && !this.queued[paramInt]) {
/* 546 */       this.queued[paramInt] = true;
/* 547 */       this.queue[this.top++] = paramInt;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\Analyzer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */