/*     */ package org.spongepowered.asm.mixin.injection.invoke;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
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
/*     */ public class ModifyArgInjector
/*     */   extends InvokeInjector
/*     */ {
/*     */   private final int index;
/*     */   private final boolean singleArgMode;
/*     */   
/*     */   public ModifyArgInjector(InjectionInfo paramInjectionInfo, int paramInt) {
/*  64 */     super(paramInjectionInfo, "@ModifyArg");
/*  65 */     this.index = paramInt;
/*  66 */     this.singleArgMode = (this.methodArgs.length == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sanityCheck(Target paramTarget, List<InjectionPoint> paramList) {
/*  76 */     super.sanityCheck(paramTarget, paramList);
/*     */     
/*  78 */     if (this.singleArgMode && 
/*  79 */       !this.methodArgs[0].equals(this.returnType)) {
/*  80 */       throw new InvalidInjectionException(this.info, "@ModifyArg return type on " + this + " must match the parameter type. ARG=" + this.methodArgs[0] + " RETURN=" + this.returnType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTarget(Target paramTarget) {
/*  92 */     if (!this.isStatic && paramTarget.isStatic) {
/*  93 */       throw new InvalidInjectionException(this.info, "non-static callback method " + this + " targets a static method which is not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/*  99 */     checkTargetForNode(paramTarget, paramInjectionNode);
/* 100 */     super.inject(paramTarget, paramInjectionNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void injectAtInvoke(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 108 */     MethodInsnNode methodInsnNode = (MethodInsnNode)paramInjectionNode.getCurrentTarget();
/* 109 */     Type[] arrayOfType = Type.getArgumentTypes(methodInsnNode.desc);
/* 110 */     int i = findArgIndex(paramTarget, arrayOfType);
/* 111 */     InsnList insnList = new InsnList();
/* 112 */     int j = 0;
/*     */     
/* 114 */     if (this.singleArgMode) {
/* 115 */       j = injectSingleArgHandler(paramTarget, arrayOfType, i, insnList);
/*     */     } else {
/* 117 */       j = injectMultiArgHandler(paramTarget, arrayOfType, i, insnList);
/*     */     } 
/*     */     
/* 120 */     paramTarget.insns.insertBefore((AbstractInsnNode)methodInsnNode, insnList);
/* 121 */     paramTarget.addToLocals(j);
/* 122 */     paramTarget.addToStack(2 - j - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int injectSingleArgHandler(Target paramTarget, Type[] paramArrayOfType, int paramInt, InsnList paramInsnList) {
/* 129 */     int[] arrayOfInt = storeArgs(paramTarget, paramArrayOfType, paramInsnList, paramInt);
/* 130 */     invokeHandlerWithArgs(paramArrayOfType, paramInsnList, arrayOfInt, paramInt, paramInt + 1);
/* 131 */     pushArgs(paramArrayOfType, paramInsnList, arrayOfInt, paramInt + 1, paramArrayOfType.length);
/* 132 */     return arrayOfInt[arrayOfInt.length - 1] - paramTarget.getMaxLocals() + paramArrayOfType[paramArrayOfType.length - 1].getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int injectMultiArgHandler(Target paramTarget, Type[] paramArrayOfType, int paramInt, InsnList paramInsnList) {
/* 139 */     if (!Arrays.equals((Object[])paramArrayOfType, (Object[])this.methodArgs)) {
/* 140 */       throw new InvalidInjectionException(this.info, "@ModifyArg method " + this + " targets a method with an invalid signature " + 
/* 141 */           Bytecode.getDescriptor(paramArrayOfType) + ", expected " + Bytecode.getDescriptor(this.methodArgs));
/*     */     }
/*     */     
/* 144 */     int[] arrayOfInt = storeArgs(paramTarget, paramArrayOfType, paramInsnList, 0);
/* 145 */     pushArgs(paramArrayOfType, paramInsnList, arrayOfInt, 0, paramInt);
/* 146 */     invokeHandlerWithArgs(paramArrayOfType, paramInsnList, arrayOfInt, 0, paramArrayOfType.length);
/* 147 */     pushArgs(paramArrayOfType, paramInsnList, arrayOfInt, paramInt + 1, paramArrayOfType.length);
/* 148 */     return arrayOfInt[arrayOfInt.length - 1] - paramTarget.getMaxLocals() + paramArrayOfType[paramArrayOfType.length - 1].getSize();
/*     */   }
/*     */   
/*     */   protected int findArgIndex(Target paramTarget, Type[] paramArrayOfType) {
/* 152 */     if (this.index > -1) {
/* 153 */       if (this.index >= paramArrayOfType.length || !paramArrayOfType[this.index].equals(this.returnType)) {
/* 154 */         throw new InvalidInjectionException(this.info, "Specified index " + this.index + " for @ModifyArg is invalid for args " + 
/* 155 */             Bytecode.getDescriptor(paramArrayOfType) + ", expected " + this.returnType + " on " + this);
/*     */       }
/* 157 */       return this.index;
/*     */     } 
/*     */     
/* 160 */     byte b = -1;
/*     */     
/* 162 */     for (byte b1 = 0; b1 < paramArrayOfType.length; b1++) {
/* 163 */       if (paramArrayOfType[b1].equals(this.returnType)) {
/*     */ 
/*     */ 
/*     */         
/* 167 */         if (b != -1) {
/* 168 */           throw new InvalidInjectionException(this.info, "Found duplicate args with index [" + b + ", " + b1 + "] matching type " + this.returnType + " for @ModifyArg target " + paramTarget + " in " + this + ". Please specify index of desired arg.");
/*     */         }
/*     */ 
/*     */         
/* 172 */         b = b1;
/*     */       } 
/*     */     } 
/* 175 */     if (b == -1) {
/* 176 */       throw new InvalidInjectionException(this.info, "Could not find arg matching type " + this.returnType + " for @ModifyArg target " + paramTarget + " in " + this);
/*     */     }
/*     */ 
/*     */     
/* 180 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\ModifyArgInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */