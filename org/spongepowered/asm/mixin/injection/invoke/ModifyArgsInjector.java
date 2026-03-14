/*     */ package org.spongepowered.asm.mixin.injection.invoke;
/*     */ 
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.util.Bytecode;
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
/*     */ public class ModifyArgsInjector
/*     */   extends InvokeInjector
/*     */ {
/*     */   private final ArgsClassGenerator argsClassGenerator;
/*     */   
/*     */   public ModifyArgsInjector(InjectionInfo paramInjectionInfo) {
/*  52 */     super(paramInjectionInfo, "@ModifyArgs");
/*     */     
/*  54 */     this.argsClassGenerator = (ArgsClassGenerator)paramInjectionInfo.getContext().getExtensions().getGenerator(ArgsClassGenerator.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTarget(Target paramTarget) {
/*  63 */     checkTargetModifiers(paramTarget, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/*  68 */     checkTargetForNode(paramTarget, paramInjectionNode);
/*  69 */     super.inject(paramTarget, paramInjectionNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void injectAtInvoke(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/*  77 */     MethodInsnNode methodInsnNode = (MethodInsnNode)paramInjectionNode.getCurrentTarget();
/*     */     
/*  79 */     Type[] arrayOfType = Type.getArgumentTypes(methodInsnNode.desc);
/*  80 */     if (arrayOfType.length == 0) {
/*  81 */       throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " targets a method invocation " + methodInsnNode.name + methodInsnNode.desc + " with no arguments!");
/*     */     }
/*     */ 
/*     */     
/*  85 */     String str = this.argsClassGenerator.getClassRef(methodInsnNode.desc);
/*  86 */     boolean bool = verifyTarget(paramTarget);
/*     */     
/*  88 */     InsnList insnList = new InsnList();
/*  89 */     paramTarget.addToStack(1);
/*     */     
/*  91 */     packArgs(insnList, str, methodInsnNode);
/*     */     
/*  93 */     if (bool) {
/*  94 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*  95 */       Bytecode.loadArgs(paramTarget.arguments, insnList, paramTarget.isStatic ? 0 : 1);
/*     */     } 
/*     */     
/*  98 */     invokeHandler(insnList);
/*  99 */     unpackArgs(insnList, str, arrayOfType);
/*     */     
/* 101 */     paramTarget.insns.insertBefore((AbstractInsnNode)methodInsnNode, insnList);
/*     */   }
/*     */   
/*     */   private boolean verifyTarget(Target paramTarget) {
/* 105 */     String str = String.format("(L%s;)V", new Object[] { ArgsClassGenerator.ARGS_REF });
/* 106 */     if (!this.methodNode.desc.equals(str)) {
/* 107 */       String str1 = Bytecode.changeDescriptorReturnType(paramTarget.method.desc, "V");
/* 108 */       String str2 = String.format("(L%s;%s", new Object[] { ArgsClassGenerator.ARGS_REF, str1.substring(1) });
/*     */       
/* 110 */       if (this.methodNode.desc.equals(str2)) {
/* 111 */         return true;
/*     */       }
/*     */       
/* 114 */       throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " has an invalid signature " + this.methodNode.desc + ", expected " + str + " or " + str2);
/*     */     } 
/*     */     
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   private void packArgs(InsnList paramInsnList, String paramString, MethodInsnNode paramMethodInsnNode) {
/* 121 */     String str = Bytecode.changeDescriptorReturnType(paramMethodInsnNode.desc, "L" + paramString + ";");
/* 122 */     paramInsnList.add((AbstractInsnNode)new MethodInsnNode(184, paramString, "of", str, false));
/* 123 */     paramInsnList.add((AbstractInsnNode)new InsnNode(89));
/*     */     
/* 125 */     if (!this.isStatic) {
/* 126 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 127 */       paramInsnList.add((AbstractInsnNode)new InsnNode(95));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void unpackArgs(InsnList paramInsnList, String paramString, Type[] paramArrayOfType) {
/* 132 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 133 */       if (b < paramArrayOfType.length - 1) {
/* 134 */         paramInsnList.add((AbstractInsnNode)new InsnNode(89));
/*     */       }
/* 136 */       paramInsnList.add((AbstractInsnNode)new MethodInsnNode(182, paramString, "$" + b, "()" + paramArrayOfType[b].getDescriptor(), false));
/* 137 */       if (b < paramArrayOfType.length - 1)
/* 138 */         if (paramArrayOfType[b].getSize() == 1) {
/* 139 */           paramInsnList.add((AbstractInsnNode)new InsnNode(95));
/*     */         } else {
/* 141 */           paramInsnList.add((AbstractInsnNode)new InsnNode(93));
/* 142 */           paramInsnList.add((AbstractInsnNode)new InsnNode(88));
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\ModifyArgsInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */