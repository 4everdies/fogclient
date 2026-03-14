/*     */ package org.spongepowered.asm.mixin.injection.invoke;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
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
/*     */ public abstract class InvokeInjector
/*     */   extends Injector
/*     */ {
/*     */   protected final String annotationType;
/*     */   
/*     */   public InvokeInjector(InjectionInfo paramInjectionInfo, String paramString) {
/*  54 */     super(paramInjectionInfo);
/*  55 */     this.annotationType = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sanityCheck(Target paramTarget, List<InjectionPoint> paramList) {
/*  65 */     super.sanityCheck(paramTarget, paramList);
/*  66 */     checkTarget(paramTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTarget(Target paramTarget) {
/*  75 */     checkTargetModifiers(paramTarget, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkTargetModifiers(Target paramTarget, boolean paramBoolean) {
/*  87 */     if (paramBoolean && paramTarget.isStatic != this.isStatic)
/*  88 */       throw new InvalidInjectionException(this.info, "'static' modifier of handler method does not match target in " + this); 
/*  89 */     if (!paramBoolean && !this.isStatic && paramTarget.isStatic) {
/*  90 */       throw new InvalidInjectionException(this.info, "non-static callback method " + this + " targets a static method which is not supported");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTargetForNode(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 106 */     if (paramTarget.isCtor) {
/* 107 */       MethodInsnNode methodInsnNode = paramTarget.findSuperInitNode();
/* 108 */       int i = paramTarget.indexOf((AbstractInsnNode)methodInsnNode);
/* 109 */       int j = paramTarget.indexOf(paramInjectionNode.getCurrentTarget());
/* 110 */       if (j <= i) {
/* 111 */         if (!this.isStatic) {
/* 112 */           throw new InvalidInjectionException(this.info, "Pre-super " + this.annotationType + " invocation must be static in " + this);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/* 117 */     checkTargetModifiers(paramTarget, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {
/* 127 */     if (!(paramInjectionNode.getCurrentTarget() instanceof MethodInsnNode)) {
/* 128 */       throw new InvalidInjectionException(this.info, this.annotationType + " annotation on is targetting a non-method insn in " + paramTarget + " in " + this);
/*     */     }
/*     */ 
/*     */     
/* 132 */     injectAtInvoke(paramTarget, paramInjectionNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void injectAtInvoke(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractInsnNode invokeHandlerWithArgs(Type[] paramArrayOfType, InsnList paramInsnList, int[] paramArrayOfint) {
/* 150 */     return invokeHandlerWithArgs(paramArrayOfType, paramInsnList, paramArrayOfint, 0, paramArrayOfType.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractInsnNode invokeHandlerWithArgs(Type[] paramArrayOfType, InsnList paramInsnList, int[] paramArrayOfint, int paramInt1, int paramInt2) {
/* 162 */     if (!this.isStatic) {
/* 163 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
/*     */     }
/* 165 */     pushArgs(paramArrayOfType, paramInsnList, paramArrayOfint, paramInt1, paramInt2);
/* 166 */     return invokeHandler(paramInsnList);
/*     */   }
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
/*     */   protected int[] storeArgs(Target paramTarget, Type[] paramArrayOfType, InsnList paramInsnList, int paramInt) {
/* 180 */     int[] arrayOfInt = paramTarget.generateArgMap(paramArrayOfType, paramInt);
/* 181 */     storeArgs(paramArrayOfType, paramInsnList, arrayOfInt, paramInt, paramArrayOfType.length);
/* 182 */     return arrayOfInt;
/*     */   }
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
/*     */   protected void storeArgs(Type[] paramArrayOfType, InsnList paramInsnList, int[] paramArrayOfint, int paramInt1, int paramInt2) {
/* 195 */     for (int i = paramInt2 - 1; i >= paramInt1; i--) {
/* 196 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(paramArrayOfType[i].getOpcode(54), paramArrayOfint[i]));
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
/*     */   
/*     */   protected void pushArgs(Type[] paramArrayOfType, InsnList paramInsnList, int[] paramArrayOfint, int paramInt1, int paramInt2) {
/* 209 */     for (int i = paramInt1; i < paramInt2; i++)
/* 210 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(paramArrayOfType[i].getOpcode(21), paramArrayOfint[i])); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\InvokeInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */