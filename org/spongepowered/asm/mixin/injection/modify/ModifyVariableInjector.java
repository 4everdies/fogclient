/*     */ package org.spongepowered.asm.mixin.injection.modify;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
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
/*     */ public class ModifyVariableInjector
/*     */   extends Injector
/*     */ {
/*     */   private final LocalVariableDiscriminator discriminator;
/*     */   
/*     */   static class Context
/*     */     extends LocalVariableDiscriminator.Context
/*     */   {
/*  63 */     final InsnList insns = new InsnList();
/*     */     
/*     */     public Context(Type param1Type, boolean param1Boolean, Target param1Target, AbstractInsnNode param1AbstractInsnNode) {
/*  66 */       super(param1Type, param1Boolean, param1Target, param1AbstractInsnNode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class ContextualInjectionPoint
/*     */     extends InjectionPoint
/*     */   {
/*     */     protected final IMixinContext context;
/*     */ 
/*     */     
/*     */     ContextualInjectionPoint(IMixinContext param1IMixinContext) {
/*  79 */       this.context = param1IMixinContext;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean find(String param1String, InsnList param1InsnList, Collection<AbstractInsnNode> param1Collection) {
/*  84 */       throw new InvalidInjectionException(this.context, getAtCode() + " injection point must be used in conjunction with @ModifyVariable");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean find(Target param1Target, Collection<AbstractInsnNode> param1Collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModifyVariableInjector(InjectionInfo paramInjectionInfo, LocalVariableDiscriminator paramLocalVariableDiscriminator) {
/* 101 */     super(paramInjectionInfo);
/* 102 */     this.discriminator = paramLocalVariableDiscriminator;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean findTargetNodes(MethodNode paramMethodNode, InjectionPoint paramInjectionPoint, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 107 */     if (paramInjectionPoint instanceof ContextualInjectionPoint) {
/* 108 */       Target target = this.info.getContext().getTargetMethod(paramMethodNode);
/* 109 */       return ((ContextualInjectionPoint)paramInjectionPoint).find(target, paramCollection);
/*     */     } 
/* 111 */     return paramInjectionPoint.find(paramMethodNode.desc, paramInsnList, paramCollection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sanityCheck(Target paramTarget, List<InjectionPoint> paramList) {
/* 121 */     super.sanityCheck(paramTarget, paramList);
/*     */     
/* 123 */     if (paramTarget.isStatic != this.isStatic) {
/* 124 */       throw new InvalidInjectionException(this.info, "'static' of variable modifier method does not match target in " + this);
/*     */     }
/*     */     
/* 127 */     int i = this.discriminator.getOrdinal();
/* 128 */     if (i < -1) {
/* 129 */       throw new InvalidInjectionException(this.info, "Invalid ordinal " + i + " specified in " + this);
/*     */     }
/*     */     
/* 132 */     if (this.discriminator.getIndex() == 0 && !this.isStatic) {
/* 133 */       throw new InvalidInjectionException(this.info, "Invalid index 0 specified in non-static variable modifier " + this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 142 */     if (paramInjectionNode.isReplaced()) {
/* 143 */       throw new InvalidInjectionException(this.info, "Variable modifier target for " + this + " was removed by another injector");
/*     */     }
/*     */     
/* 146 */     Context context = new Context(this.returnType, this.discriminator.isArgsOnly(), paramTarget, paramInjectionNode.getCurrentTarget());
/*     */     
/* 148 */     if (this.discriminator.printLVT()) {
/* 149 */       printLocals(context);
/*     */     }
/*     */     
/* 152 */     String str = Bytecode.getDescriptor(new Type[] { this.returnType }, this.returnType);
/* 153 */     if (!str.equals(this.methodNode.desc)) {
/* 154 */       throw new InvalidInjectionException(this.info, "Variable modifier " + this + " has an invalid signature, expected " + str + " but found " + this.methodNode.desc);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 159 */       int i = this.discriminator.findLocal(context);
/* 160 */       if (i > -1) {
/* 161 */         inject(context, i);
/*     */       }
/* 163 */     } catch (InvalidImplicitDiscriminatorException invalidImplicitDiscriminatorException) {
/* 164 */       if (this.discriminator.printLVT()) {
/* 165 */         this.info.addCallbackInvocation(this.methodNode);
/*     */         return;
/*     */       } 
/* 168 */       throw new InvalidInjectionException(this.info, "Implicit variable modifier injection failed in " + this, invalidImplicitDiscriminatorException);
/*     */     } 
/*     */     
/* 171 */     paramTarget.insns.insertBefore(context.node, context.insns);
/* 172 */     paramTarget.addToStack(this.isStatic ? 1 : 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printLocals(Context paramContext) {
/* 179 */     SignaturePrinter signaturePrinter = new SignaturePrinter(this.methodNode.name, this.returnType, this.methodArgs, new String[] { "var" });
/* 180 */     signaturePrinter.setModifiers(this.methodNode);
/*     */     
/* 182 */     (new PrettyPrinter())
/* 183 */       .kvWidth(20)
/* 184 */       .kv("Target Class", this.classNode.name.replace('/', '.'))
/* 185 */       .kv("Target Method", paramContext.target.method.name)
/* 186 */       .kv("Callback Name", this.methodNode.name)
/* 187 */       .kv("Capture Type", SignaturePrinter.getTypeName(this.returnType, false))
/* 188 */       .kv("Instruction", "%s %s", new Object[] { paramContext.node.getClass().getSimpleName(), Bytecode.getOpcodeName(paramContext.node.getOpcode()) }).hr()
/* 189 */       .kv("Match mode", this.discriminator.isImplicit(paramContext) ? "IMPLICIT (match single)" : "EXPLICIT (match by criteria)")
/* 190 */       .kv("Match ordinal", (this.discriminator.getOrdinal() < 0) ? "any" : Integer.valueOf(this.discriminator.getOrdinal()))
/* 191 */       .kv("Match index", (this.discriminator.getIndex() < paramContext.baseArgIndex) ? "any" : Integer.valueOf(this.discriminator.getIndex()))
/* 192 */       .kv("Match name(s)", this.discriminator.hasNames() ? this.discriminator.getNames() : "any")
/* 193 */       .kv("Args only", Boolean.valueOf(this.discriminator.isArgsOnly())).hr()
/* 194 */       .add(paramContext)
/* 195 */       .print(System.err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inject(Context paramContext, int paramInt) {
/* 205 */     if (!this.isStatic) {
/* 206 */       paramContext.insns.add((AbstractInsnNode)new VarInsnNode(25, 0));
/*     */     }
/*     */     
/* 209 */     paramContext.insns.add((AbstractInsnNode)new VarInsnNode(this.returnType.getOpcode(21), paramInt));
/* 210 */     invokeHandler(paramContext.insns);
/* 211 */     paramContext.insns.add((AbstractInsnNode)new VarInsnNode(this.returnType.getOpcode(54), paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\modify\ModifyVariableInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */