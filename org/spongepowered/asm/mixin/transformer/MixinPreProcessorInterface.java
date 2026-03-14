/*    */ package org.spongepowered.asm.mixin.transformer;
/*    */ 
/*    */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*    */ import org.spongepowered.asm.lib.tree.FieldNode;
/*    */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
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
/*    */ class MixinPreProcessorInterface
/*    */   extends MixinPreProcessorStandard
/*    */ {
/*    */   MixinPreProcessorInterface(MixinInfo paramMixinInfo, MixinInfo.MixinClassNode paramMixinClassNode) {
/* 49 */     super(paramMixinInfo, paramMixinClassNode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareMethod(MixinInfo.MixinMethodNode paramMixinMethodNode, ClassInfo.Method paramMethod) {
/* 60 */     if (!Bytecode.hasFlag(paramMixinMethodNode, 1) && !Bytecode.hasFlag(paramMixinMethodNode, 4096)) {
/* 61 */       throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains a non-public method! Found " + paramMethod + " in " + this.mixin);
/*    */     }
/*    */ 
/*    */     
/* 65 */     super.prepareMethod(paramMixinMethodNode, paramMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean validateField(MixinTargetContext paramMixinTargetContext, FieldNode paramFieldNode, AnnotationNode paramAnnotationNode) {
/* 77 */     if (!Bytecode.hasFlag(paramFieldNode, 8)) {
/* 78 */       throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains an instance field! Found " + paramFieldNode.name + " in " + this.mixin);
/*    */     }
/*    */ 
/*    */     
/* 82 */     return super.validateField(paramMixinTargetContext, paramFieldNode, paramAnnotationNode);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinPreProcessorInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */