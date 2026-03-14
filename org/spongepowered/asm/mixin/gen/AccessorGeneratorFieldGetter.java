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
/*    */ public class AccessorGeneratorFieldGetter
/*    */   extends AccessorGeneratorField
/*    */ {
/*    */   public AccessorGeneratorFieldGetter(AccessorInfo paramAccessorInfo) {
/* 39 */     super(paramAccessorInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodNode generate() {
/* 47 */     MethodNode methodNode = createMethod(this.targetType.getSize(), this.targetType.getSize());
/* 48 */     if (this.isInstanceField) {
/* 49 */       methodNode.instructions.add((AbstractInsnNode)new VarInsnNode(25, 0));
/*    */     }
/* 51 */     char c = this.isInstanceField ? '´' : '²';
/* 52 */     methodNode.instructions.add((AbstractInsnNode)new FieldInsnNode(c, (this.info.getClassNode()).name, this.targetField.name, this.targetField.desc));
/* 53 */     methodNode.instructions.add((AbstractInsnNode)new InsnNode(this.targetType.getOpcode(172)));
/* 54 */     return methodNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\AccessorGeneratorFieldGetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */