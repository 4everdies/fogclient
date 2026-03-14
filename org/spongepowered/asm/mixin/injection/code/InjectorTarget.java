/*     */ package org.spongepowered.asm.mixin.injection.code;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
/*     */ import org.spongepowered.asm.util.Annotations;
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
/*     */ public class InjectorTarget
/*     */ {
/*     */   private final ISliceContext context;
/*  53 */   private final Map<String, ReadOnlyInsnList> cache = new HashMap<String, ReadOnlyInsnList>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Target target;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String mergedBy;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int mergedPriority;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectorTarget(ISliceContext paramISliceContext, Target paramTarget) {
/*  71 */     this.context = paramISliceContext;
/*  72 */     this.target = paramTarget;
/*     */     
/*  74 */     AnnotationNode annotationNode = Annotations.getVisible(paramTarget.method, MixinMerged.class);
/*  75 */     this.mergedBy = (String)Annotations.getValue(annotationNode, "mixin");
/*  76 */     this.mergedPriority = ((Integer)Annotations.getValue(annotationNode, "priority", Integer.valueOf(1000))).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return this.target.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getTarget() {
/*  88 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodNode getMethod() {
/*  95 */     return this.target.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMerged() {
/* 102 */     return (this.mergedBy != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMergedBy() {
/* 110 */     return this.mergedBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMergedPriority() {
/* 118 */     return this.mergedPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InsnList getSlice(String paramString) {
/* 128 */     ReadOnlyInsnList readOnlyInsnList = this.cache.get(paramString);
/* 129 */     if (readOnlyInsnList == null) {
/* 130 */       MethodSlice methodSlice = this.context.getSlice(paramString);
/* 131 */       if (methodSlice != null) {
/* 132 */         readOnlyInsnList = methodSlice.getSlice(this.target.method);
/*     */       } else {
/*     */         
/* 135 */         readOnlyInsnList = new ReadOnlyInsnList(this.target.method.instructions);
/*     */       } 
/* 137 */       this.cache.put(paramString, readOnlyInsnList);
/*     */     } 
/*     */     
/* 140 */     return readOnlyInsnList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InsnList getSlice(InjectionPoint paramInjectionPoint) {
/* 150 */     return getSlice(paramInjectionPoint.getSlice());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 157 */     for (ReadOnlyInsnList readOnlyInsnList : this.cache.values()) {
/* 158 */       readOnlyInsnList.dispose();
/*     */     }
/*     */     
/* 161 */     this.cache.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\code\InjectorTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */