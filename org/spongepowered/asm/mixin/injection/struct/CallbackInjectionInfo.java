/*    */ package org.spongepowered.asm.mixin.injection.struct;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInjector;
/*    */ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
/*    */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*    */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
/*    */ import org.spongepowered.asm.util.Annotations;
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
/*    */ public class CallbackInjectionInfo
/*    */   extends InjectionInfo
/*    */ {
/*    */   protected CallbackInjectionInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode) {
/* 44 */     super(paramMixinTargetContext, paramMethodNode, paramAnnotationNode);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Injector parseInjector(AnnotationNode paramAnnotationNode) {
/* 49 */     boolean bool = ((Boolean)Annotations.getValue(paramAnnotationNode, "cancellable", Boolean.FALSE)).booleanValue();
/* 50 */     LocalCapture localCapture = (LocalCapture)Annotations.getValue(paramAnnotationNode, "locals", LocalCapture.class, (Enum)LocalCapture.NO_CAPTURE);
/* 51 */     String str = (String)Annotations.getValue(paramAnnotationNode, "id", "");
/*    */     
/* 53 */     return (Injector)new CallbackInjector(this, bool, localCapture, str);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSliceId(String paramString) {
/* 58 */     return Strings.nullToEmpty(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\CallbackInjectionInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */