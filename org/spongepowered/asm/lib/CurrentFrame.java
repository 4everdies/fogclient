/*    */ package org.spongepowered.asm.lib;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CurrentFrame
/*    */   extends Frame
/*    */ {
/*    */   void execute(int paramInt1, int paramInt2, ClassWriter paramClassWriter, Item paramItem) {
/* 50 */     super.execute(paramInt1, paramInt2, paramClassWriter, paramItem);
/* 51 */     Frame frame = new Frame();
/* 52 */     merge(paramClassWriter, frame, 0);
/* 53 */     set(frame);
/* 54 */     this.owner.inputStackTop = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\CurrentFrame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */