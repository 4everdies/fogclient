/*    */ package org.spongepowered.asm.lib.tree.analysis;
/*    */ 
/*    */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
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
/*    */ public class AnalyzerException
/*    */   extends Exception
/*    */ {
/*    */   public final AbstractInsnNode node;
/*    */   
/*    */   public AnalyzerException(AbstractInsnNode paramAbstractInsnNode, String paramString) {
/* 46 */     super(paramString);
/* 47 */     this.node = paramAbstractInsnNode;
/*    */   }
/*    */ 
/*    */   
/*    */   public AnalyzerException(AbstractInsnNode paramAbstractInsnNode, String paramString, Throwable paramThrowable) {
/* 52 */     super(paramString, paramThrowable);
/* 53 */     this.node = paramAbstractInsnNode;
/*    */   }
/*    */ 
/*    */   
/*    */   public AnalyzerException(AbstractInsnNode paramAbstractInsnNode, String paramString, Object paramObject, Value paramValue) {
/* 58 */     super(((paramString == null) ? "Expected " : (paramString + ": expected ")) + paramObject + ", but found " + paramValue);
/*    */     
/* 60 */     this.node = paramAbstractInsnNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\AnalyzerException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */