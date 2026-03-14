/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.struct.MemberRef;
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
/*     */ abstract class ClassContext
/*     */ {
/*  50 */   private final Set<ClassInfo.Method> upgradedMethods = new HashSet<ClassInfo.Method>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract String getClassRef();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract ClassNode getClassNode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract ClassInfo getClassInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addUpgradedMethod(MethodNode paramMethodNode) {
/*  80 */     ClassInfo.Method method = getClassInfo().findMethod(paramMethodNode);
/*  81 */     if (method == null)
/*     */     {
/*  83 */       throw new IllegalStateException("Meta method for " + paramMethodNode.name + " not located in " + this);
/*     */     }
/*  85 */     this.upgradedMethods.add(method);
/*     */   }
/*     */   
/*     */   protected void upgradeMethods() {
/*  89 */     for (MethodNode methodNode : (getClassNode()).methods) {
/*  90 */       upgradeMethod(methodNode);
/*     */     }
/*     */   }
/*     */   
/*     */   private void upgradeMethod(MethodNode paramMethodNode) {
/*  95 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  96 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  97 */       if (!(abstractInsnNode instanceof MethodInsnNode)) {
/*     */         continue;
/*     */       }
/*     */       
/* 101 */       MemberRef.Method method = new MemberRef.Method((MethodInsnNode)abstractInsnNode);
/* 102 */       if (method.getOwner().equals(getClassRef())) {
/* 103 */         ClassInfo.Method method1 = getClassInfo().findMethod(method.getName(), method.getDesc(), 10);
/* 104 */         upgradeMethodRef(paramMethodNode, (MemberRef)method, method1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void upgradeMethodRef(MethodNode paramMethodNode, MemberRef paramMemberRef, ClassInfo.Method paramMethod) {
/* 110 */     if (paramMemberRef.getOpcode() != 183) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     if (this.upgradedMethods.contains(paramMethod))
/* 115 */       paramMemberRef.setOpcode(182); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\ClassContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */