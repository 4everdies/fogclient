/*    */ package org.spongepowered.asm.mixin.gen;
/*    */ 
/*    */ import org.spongepowered.asm.lib.Type;
/*    */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.InsnNode;
/*    */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
/*    */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*    */ import org.spongepowered.asm.util.Bytecode;
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
/*    */ public class AccessorGeneratorMethodProxy
/*    */   extends AccessorGenerator
/*    */ {
/*    */   private final MethodNode targetMethod;
/*    */   private final Type[] argTypes;
/*    */   private final Type returnType;
/*    */   private final boolean isInstanceMethod;
/*    */   
/*    */   public AccessorGeneratorMethodProxy(AccessorInfo paramAccessorInfo) {
/* 61 */     super(paramAccessorInfo);
/* 62 */     this.targetMethod = paramAccessorInfo.getTargetMethod();
/* 63 */     this.argTypes = paramAccessorInfo.getArgTypes();
/* 64 */     this.returnType = paramAccessorInfo.getReturnType();
/* 65 */     this.isInstanceMethod = !Bytecode.hasFlag(this.targetMethod, 8);
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodNode generate() {
/* 70 */     int i = Bytecode.getArgsSize(this.argTypes) + this.returnType.getSize() + (this.isInstanceMethod ? 1 : 0);
/* 71 */     MethodNode methodNode = createMethod(i, i);
/* 72 */     if (this.isInstanceMethod) {
/* 73 */       methodNode.instructions.add((AbstractInsnNode)new VarInsnNode(25, 0));
/*    */     }
/* 75 */     Bytecode.loadArgs(this.argTypes, methodNode.instructions, this.isInstanceMethod ? 1 : 0);
/* 76 */     boolean bool = Bytecode.hasFlag(this.targetMethod, 2);
/* 77 */     char c = this.isInstanceMethod ? (bool ? '·' : '¶') : '¸';
/* 78 */     methodNode.instructions.add((AbstractInsnNode)new MethodInsnNode(c, (this.info.getClassNode()).name, this.targetMethod.name, this.targetMethod.desc, false));
/* 79 */     methodNode.instructions.add((AbstractInsnNode)new InsnNode(this.returnType.getOpcode(172)));
/* 80 */     return methodNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\AccessorGeneratorMethodProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */