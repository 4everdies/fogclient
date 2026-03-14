/*     */ package org.spongepowered.asm.mixin.injection.invoke;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.invoke.util.InsnFinder;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.Locals;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
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
/*     */ public class ModifyConstantInjector
/*     */   extends RedirectInjector
/*     */ {
/*     */   private static final int OPCODE_OFFSET = 6;
/*     */   
/*     */   public ModifyConstantInjector(InjectionInfo paramInjectionInfo) {
/*  63 */     super(paramInjectionInfo, "@ModifyConstant");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/*  68 */     if (!preInject(paramInjectionNode)) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     if (paramInjectionNode.isReplaced()) {
/*  73 */       throw new UnsupportedOperationException("Target failure for " + this.info);
/*     */     }
/*     */     
/*  76 */     AbstractInsnNode abstractInsnNode = paramInjectionNode.getCurrentTarget();
/*  77 */     if (abstractInsnNode instanceof JumpInsnNode) {
/*  78 */       checkTargetModifiers(paramTarget, false);
/*  79 */       injectExpandedConstantModifier(paramTarget, (JumpInsnNode)abstractInsnNode);
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     if (Bytecode.isConstant(abstractInsnNode)) {
/*  84 */       checkTargetModifiers(paramTarget, false);
/*  85 */       injectConstantModifier(paramTarget, abstractInsnNode);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     throw new InvalidInjectionException(this.info, this.annotationType + " annotation is targetting an invalid insn in " + paramTarget + " in " + this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectExpandedConstantModifier(Target paramTarget, JumpInsnNode paramJumpInsnNode) {
/* 100 */     int i = paramJumpInsnNode.getOpcode();
/* 101 */     if (i < 155 || i > 158) {
/* 102 */       throw new InvalidInjectionException(this.info, this.annotationType + " annotation selected an invalid opcode " + 
/* 103 */           Bytecode.getOpcodeName(i) + " in " + paramTarget + " in " + this);
/*     */     }
/*     */     
/* 106 */     InsnList insnList = new InsnList();
/* 107 */     insnList.add((AbstractInsnNode)new InsnNode(3));
/* 108 */     AbstractInsnNode abstractInsnNode = invokeConstantHandler(Type.getType("I"), paramTarget, insnList, insnList);
/* 109 */     insnList.add((AbstractInsnNode)new JumpInsnNode(i + 6, paramJumpInsnNode.label));
/* 110 */     paramTarget.replaceNode((AbstractInsnNode)paramJumpInsnNode, abstractInsnNode, insnList);
/* 111 */     paramTarget.addToStack(1);
/*     */   }
/*     */   
/*     */   private void injectConstantModifier(Target paramTarget, AbstractInsnNode paramAbstractInsnNode) {
/* 115 */     Type type = Bytecode.getConstantType(paramAbstractInsnNode);
/*     */     
/* 117 */     if (type.getSort() <= 5 && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/* 118 */       checkNarrowing(paramTarget, paramAbstractInsnNode, type);
/*     */     }
/*     */     
/* 121 */     InsnList insnList1 = new InsnList();
/* 122 */     InsnList insnList2 = new InsnList();
/* 123 */     AbstractInsnNode abstractInsnNode = invokeConstantHandler(type, paramTarget, insnList1, insnList2);
/* 124 */     paramTarget.wrapNode(paramAbstractInsnNode, abstractInsnNode, insnList1, insnList2);
/*     */   }
/*     */   
/*     */   private AbstractInsnNode invokeConstantHandler(Type paramType, Target paramTarget, InsnList paramInsnList1, InsnList paramInsnList2) {
/* 128 */     String str = Bytecode.generateDescriptor(paramType, new Object[] { paramType });
/* 129 */     boolean bool = checkDescriptor(str, paramTarget, "getter");
/*     */     
/* 131 */     if (!this.isStatic) {
/* 132 */       paramInsnList1.insert((AbstractInsnNode)new VarInsnNode(25, 0));
/* 133 */       paramTarget.addToStack(1);
/*     */     } 
/*     */     
/* 136 */     if (bool) {
/* 137 */       pushArgs(paramTarget.arguments, paramInsnList2, paramTarget.getArgIndices(), 0, paramTarget.arguments.length);
/* 138 */       paramTarget.addToStack(Bytecode.getArgsSize(paramTarget.arguments));
/*     */     } 
/*     */     
/* 141 */     return invokeHandler(paramInsnList2);
/*     */   }
/*     */   
/*     */   private void checkNarrowing(Target paramTarget, AbstractInsnNode paramAbstractInsnNode, Type paramType) {
/* 145 */     AbstractInsnNode abstractInsnNode = (new InsnFinder()).findPopInsn(paramTarget, paramAbstractInsnNode);
/*     */     
/* 147 */     if (abstractInsnNode == null)
/*     */       return; 
/* 149 */     if (abstractInsnNode instanceof FieldInsnNode) {
/* 150 */       FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
/* 151 */       Type type = Type.getType(fieldInsnNode.desc);
/* 152 */       checkNarrowing(paramTarget, paramAbstractInsnNode, paramType, type, paramTarget.indexOf(abstractInsnNode), String.format("%s %s %s.%s", new Object[] {
/* 153 */               Bytecode.getOpcodeName(abstractInsnNode), SignaturePrinter.getTypeName(type, false), fieldInsnNode.owner.replace('/', '.'), fieldInsnNode.name }));
/* 154 */     } else if (abstractInsnNode.getOpcode() == 172) {
/* 155 */       checkNarrowing(paramTarget, paramAbstractInsnNode, paramType, paramTarget.returnType, paramTarget.indexOf(abstractInsnNode), "RETURN " + 
/* 156 */           SignaturePrinter.getTypeName(paramTarget.returnType, false));
/* 157 */     } else if (abstractInsnNode.getOpcode() == 54) {
/* 158 */       int i = ((VarInsnNode)abstractInsnNode).var;
/* 159 */       LocalVariableNode localVariableNode = Locals.getLocalVariableAt(paramTarget.classNode, paramTarget.method, abstractInsnNode, i);
/*     */ 
/*     */ 
/*     */       
/* 163 */       if (localVariableNode != null && localVariableNode.desc != null) {
/* 164 */         String str = (localVariableNode.name != null) ? localVariableNode.name : "unnamed";
/* 165 */         Type type = Type.getType(localVariableNode.desc);
/* 166 */         checkNarrowing(paramTarget, paramAbstractInsnNode, paramType, type, paramTarget.indexOf(abstractInsnNode), String.format("ISTORE[var=%d] %s %s", new Object[] { Integer.valueOf(i), 
/* 167 */                 SignaturePrinter.getTypeName(type, false), str }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkNarrowing(Target paramTarget, AbstractInsnNode paramAbstractInsnNode, Type paramType1, Type paramType2, int paramInt, String paramString) {
/* 173 */     int i = paramType1.getSort();
/* 174 */     int j = paramType2.getSort();
/* 175 */     if (j < i) {
/* 176 */       String str1 = SignaturePrinter.getTypeName(paramType1, false);
/* 177 */       String str2 = SignaturePrinter.getTypeName(paramType2, false);
/* 178 */       String str3 = (j == 1) ? ". Implicit conversion to <boolean> can cause nondeterministic (JVM-specific) behaviour!" : "";
/* 179 */       Level level = (j == 1) ? Level.ERROR : Level.WARN;
/* 180 */       Injector.logger.log(level, "Narrowing conversion of <{}> to <{}> in {} target {} at opcode {} ({}){}", new Object[] { str1, str2, this.info, paramTarget, 
/* 181 */             Integer.valueOf(paramInt), paramString, str3 });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\ModifyConstantInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */