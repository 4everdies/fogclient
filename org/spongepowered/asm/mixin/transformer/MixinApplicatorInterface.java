/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.tree.FieldNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
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
/*     */ class MixinApplicatorInterface
/*     */   extends MixinApplicatorStandard
/*     */ {
/*     */   MixinApplicatorInterface(TargetClassContext paramTargetClassContext) {
/*  48 */     super(paramTargetClassContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyInterfaces(MixinTargetContext paramMixinTargetContext) {
/*  58 */     for (String str : paramMixinTargetContext.getInterfaces()) {
/*  59 */       if (!this.targetClass.name.equals(str) && !this.targetClass.interfaces.contains(str)) {
/*  60 */         this.targetClass.interfaces.add(str);
/*  61 */         paramMixinTargetContext.getTargetClassInfo().addInterface(str);
/*     */       } 
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
/*     */   protected void applyFields(MixinTargetContext paramMixinTargetContext) {
/*  74 */     for (Map.Entry<FieldNode, ClassInfo.Field> entry : paramMixinTargetContext.getShadowFields()) {
/*  75 */       FieldNode fieldNode = (FieldNode)entry.getKey();
/*  76 */       this.logger.error("Ignoring redundant @Shadow field {}:{} in {}", new Object[] { fieldNode.name, fieldNode.desc, paramMixinTargetContext });
/*     */     } 
/*     */     
/*  79 */     mergeNewFields(paramMixinTargetContext);
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
/*     */   protected void applyInitialisers(MixinTargetContext paramMixinTargetContext) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareInjections(MixinTargetContext paramMixinTargetContext) {
/* 100 */     for (MethodNode methodNode : this.targetClass.methods) {
/*     */       try {
/* 102 */         InjectionInfo injectionInfo = InjectionInfo.parse(paramMixinTargetContext, methodNode);
/* 103 */         if (injectionInfo != null) {
/* 104 */           throw new InvalidInterfaceMixinException(paramMixinTargetContext, injectionInfo + " is not supported on interface mixin method " + methodNode.name);
/*     */         }
/* 106 */       } catch (InvalidInjectionException invalidInjectionException) {
/* 107 */         String str = (invalidInjectionException.getInjectionInfo() != null) ? invalidInjectionException.getInjectionInfo().toString() : "Injection";
/* 108 */         throw new InvalidInterfaceMixinException(paramMixinTargetContext, str + " is not supported in interface mixin");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void applyInjections(MixinTargetContext paramMixinTargetContext) {}
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinApplicatorInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */