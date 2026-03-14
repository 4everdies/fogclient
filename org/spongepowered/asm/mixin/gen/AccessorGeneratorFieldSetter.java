/*    */ package org.spongepowered.asm.mixin.gen;
/*    */ 
/*    */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.InsnNode;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
/*    */ import org.spongepowered.asm.lib.tree.VarInsnNode;
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
/*    */ public class AccessorGeneratorFieldSetter
/*    */   extends AccessorGeneratorField
/*    */ {
/*    */   public AccessorGeneratorFieldSetter(AccessorInfo paramAccessorInfo) {
/* 39 */     super(paramAccessorInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodNode generate() {
/* 47 */     byte b = this.isInstanceField ? 1 : 0;
/* 48 */     int i = b + this.targetType.getSize();
/* 49 */     int j = b + this.targetType.getSize();
/* 50 */     MethodNode methodNode = createMethod(i, j);
/* 51 */     if (this.isInstanceField) {
/* 52 */       methodNode.instructions.add((AbstractInsnNode)new VarInsnNode(25, 0));
/*    */     }
/* 54 */     methodNode.instructions.add((AbstractInsnNode)new VarInsnNode(this.targetType.getOpcode(21), b));
/* 55 */     char c = this.isInstanceField ? 'µ' : '³';
/* 56 */     methodNode.instructions.add((AbstractInsnNode)new FieldInsnNode(c, (this.info.getClassNode()).name, this.targetField.name, this.targetField.desc));
/* 57 */     methodNode.instructions.add((AbstractInsnNode)new InsnNode(177));
/* 58 */     return methodNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\AccessorGeneratorFieldSetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */