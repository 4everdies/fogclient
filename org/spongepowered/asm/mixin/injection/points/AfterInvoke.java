/*    */ package org.spongepowered.asm.mixin.injection.points;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.spongepowered.asm.lib.Type;
/*    */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.InsnList;
/*    */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*    */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*    */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*    */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
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
/*    */ 
/*    */ 
/*    */ @AtCode("INVOKE_ASSIGN")
/*    */ public class AfterInvoke
/*    */   extends BeforeInvoke
/*    */ {
/*    */   public AfterInvoke(InjectionPointData paramInjectionPointData) {
/* 73 */     super(paramInjectionPointData);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean addInsn(InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection, AbstractInsnNode paramAbstractInsnNode) {
/* 78 */     MethodInsnNode methodInsnNode = (MethodInsnNode)paramAbstractInsnNode;
/* 79 */     if (Type.getReturnType(methodInsnNode.desc) == Type.VOID_TYPE) {
/* 80 */       return false;
/*    */     }
/*    */     
/* 83 */     paramAbstractInsnNode = InjectionPoint.nextNode(paramInsnList, paramAbstractInsnNode);
/* 84 */     if (paramAbstractInsnNode instanceof org.spongepowered.asm.lib.tree.VarInsnNode && paramAbstractInsnNode.getOpcode() >= 54) {
/* 85 */       paramAbstractInsnNode = InjectionPoint.nextNode(paramInsnList, paramAbstractInsnNode);
/*    */     }
/*    */     
/* 88 */     paramCollection.add(paramAbstractInsnNode);
/* 89 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\AfterInvoke.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */