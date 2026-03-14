/*    */ package org.spongepowered.asm.mixin.injection.struct;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import org.spongepowered.asm.lib.Type;
/*    */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
/*    */ import org.spongepowered.asm.mixin.injection.Constant;
/*    */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*    */ import org.spongepowered.asm.mixin.injection.invoke.ModifyConstantInjector;
/*    */ import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
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
/*    */ 
/*    */ 
/*    */ public class ModifyConstantInjectionInfo
/*    */   extends InjectionInfo
/*    */ {
/* 46 */   private static final String CONSTANT_ANNOTATION_CLASS = Constant.class.getName().replace('.', '/');
/*    */   
/*    */   public ModifyConstantInjectionInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode) {
/* 49 */     super(paramMixinTargetContext, paramMethodNode, paramAnnotationNode, "constant");
/*    */   }
/*    */   
/*    */   protected List<AnnotationNode> readInjectionPoints(String paramString) {
/*    */     ImmutableList immutableList;
/* 54 */     List<AnnotationNode> list = super.readInjectionPoints(paramString);
/* 55 */     if (list.isEmpty()) {
/* 56 */       AnnotationNode annotationNode = new AnnotationNode(CONSTANT_ANNOTATION_CLASS);
/* 57 */       annotationNode.visit("log", Boolean.TRUE);
/* 58 */       immutableList = ImmutableList.of(annotationNode);
/*    */     } 
/* 60 */     return (List<AnnotationNode>)immutableList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void parseInjectionPoints(List<AnnotationNode> paramList) {
/* 65 */     Type type = Type.getReturnType(this.method.desc);
/*    */     
/* 67 */     for (AnnotationNode annotationNode : paramList) {
/* 68 */       this.injectionPoints.add(new BeforeConstant(getContext(), annotationNode, type.getDescriptor()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected Injector parseInjector(AnnotationNode paramAnnotationNode) {
/* 74 */     return (Injector)new ModifyConstantInjector(this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getDescription() {
/* 79 */     return "Constant modifier method";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSliceId(String paramString) {
/* 84 */     return Strings.nullToEmpty(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\ModifyConstantInjectionInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */