/*    */ package org.spongepowered.asm.lib.tree.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.LabelNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Subroutine
/*    */ {
/*    */   LabelNode start;
/*    */   boolean[] access;
/*    */   List<JumpInsnNode> callers;
/*    */   
/*    */   private Subroutine() {}
/*    */   
/*    */   Subroutine(LabelNode paramLabelNode, int paramInt, JumpInsnNode paramJumpInsnNode) {
/* 56 */     this.start = paramLabelNode;
/* 57 */     this.access = new boolean[paramInt];
/* 58 */     this.callers = new ArrayList<JumpInsnNode>();
/* 59 */     this.callers.add(paramJumpInsnNode);
/*    */   }
/*    */   
/*    */   public Subroutine copy() {
/* 63 */     Subroutine subroutine = new Subroutine();
/* 64 */     subroutine.start = this.start;
/* 65 */     subroutine.access = new boolean[this.access.length];
/* 66 */     System.arraycopy(this.access, 0, subroutine.access, 0, this.access.length);
/* 67 */     subroutine.callers = new ArrayList<JumpInsnNode>(this.callers);
/* 68 */     return subroutine;
/*    */   }
/*    */   
/*    */   public boolean merge(Subroutine paramSubroutine) throws AnalyzerException {
/* 72 */     boolean bool = false; byte b;
/* 73 */     for (b = 0; b < this.access.length; b++) {
/* 74 */       if (paramSubroutine.access[b] && !this.access[b]) {
/* 75 */         this.access[b] = true;
/* 76 */         bool = true;
/*    */       } 
/*    */     } 
/* 79 */     if (paramSubroutine.start == this.start) {
/* 80 */       for (b = 0; b < paramSubroutine.callers.size(); b++) {
/* 81 */         JumpInsnNode jumpInsnNode = paramSubroutine.callers.get(b);
/* 82 */         if (!this.callers.contains(jumpInsnNode)) {
/* 83 */           this.callers.add(jumpInsnNode);
/* 84 */           bool = true;
/*    */         } 
/*    */       } 
/*    */     }
/* 88 */     return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\Subroutine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */