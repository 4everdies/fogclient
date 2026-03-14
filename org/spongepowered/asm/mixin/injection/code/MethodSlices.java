/*     */ package org.spongepowered.asm.mixin.injection.code;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
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
/*     */ public final class MethodSlices
/*     */ {
/*     */   private final InjectionInfo info;
/*  50 */   private final Map<String, MethodSlice> slices = new HashMap<String, MethodSlice>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodSlices(InjectionInfo paramInjectionInfo) {
/*  58 */     this.info = paramInjectionInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void add(MethodSlice paramMethodSlice) {
/*  67 */     String str = this.info.getSliceId(paramMethodSlice.getId());
/*  68 */     if (this.slices.containsKey(str)) {
/*  69 */       throw new InvalidSliceException(this.info, paramMethodSlice + " has a duplicate id, '" + str + "' was already defined");
/*     */     }
/*  71 */     this.slices.put(str, paramMethodSlice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodSlice get(String paramString) {
/*  82 */     return this.slices.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return String.format("MethodSlices%s", new Object[] { this.slices.keySet() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodSlices parse(InjectionInfo paramInjectionInfo) {
/* 100 */     MethodSlices methodSlices = new MethodSlices(paramInjectionInfo);
/*     */     
/* 102 */     AnnotationNode annotationNode = paramInjectionInfo.getAnnotation();
/* 103 */     if (annotationNode != null) {
/* 104 */       for (AnnotationNode annotationNode1 : Annotations.getValue(annotationNode, "slice", true)) {
/* 105 */         MethodSlice methodSlice = MethodSlice.parse((ISliceContext)paramInjectionInfo, annotationNode1);
/* 106 */         methodSlices.add(methodSlice);
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return methodSlices;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\code\MethodSlices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */