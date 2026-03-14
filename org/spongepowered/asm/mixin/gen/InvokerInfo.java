/*    */ package org.spongepowered.asm.mixin.gen;
/*    */ 
/*    */ import org.spongepowered.asm.lib.Type;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
/*    */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*    */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
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
/*    */ public class InvokerInfo
/*    */   extends AccessorInfo
/*    */ {
/*    */   public InvokerInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/* 38 */     super(paramMixinTargetContext, paramMethodNode, (Class)Invoker.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AccessorInfo.AccessorType initType() {
/* 43 */     return AccessorInfo.AccessorType.METHOD_PROXY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Type initTargetFieldType() {
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemberInfo initTarget() {
/* 53 */     return new MemberInfo(getTargetName(), null, this.method.desc);
/*    */   }
/*    */ 
/*    */   
/*    */   public void locate() {
/* 58 */     this.targetMethod = findTargetMethod();
/*    */   }
/*    */   
/*    */   private MethodNode findTargetMethod() {
/* 62 */     return findTarget(this.classNode.methods);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\InvokerInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */