/*    */ package org.spongepowered.asm.lib.tree.analysis;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ public class SourceValue
/*    */   implements Value
/*    */ {
/*    */   public final int size;
/*    */   public final Set<AbstractInsnNode> insns;
/*    */   
/*    */   public SourceValue(int paramInt) {
/* 67 */     this(paramInt, SmallSet.emptySet());
/*    */   }
/*    */   
/*    */   public SourceValue(int paramInt, AbstractInsnNode paramAbstractInsnNode) {
/* 71 */     this.size = paramInt;
/* 72 */     this.insns = new SmallSet<AbstractInsnNode>(paramAbstractInsnNode, null);
/*    */   }
/*    */   
/*    */   public SourceValue(int paramInt, Set<AbstractInsnNode> paramSet) {
/* 76 */     this.size = paramInt;
/* 77 */     this.insns = paramSet;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 81 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 86 */     if (!(paramObject instanceof SourceValue)) {
/* 87 */       return false;
/*    */     }
/* 89 */     SourceValue sourceValue = (SourceValue)paramObject;
/* 90 */     return (this.size == sourceValue.size && this.insns.equals(sourceValue.insns));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 95 */     return this.insns.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\SourceValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */