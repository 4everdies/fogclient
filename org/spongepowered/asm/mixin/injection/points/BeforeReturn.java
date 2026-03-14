/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
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
/*     */ @AtCode("RETURN")
/*     */ public class BeforeReturn
/*     */   extends InjectionPoint
/*     */ {
/*     */   private final int ordinal;
/*     */   
/*     */   public BeforeReturn(InjectionPointData paramInjectionPointData) {
/*  77 */     super(paramInjectionPointData);
/*     */     
/*  79 */     this.ordinal = paramInjectionPointData.getOrdinal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkPriority(int paramInt1, int paramInt2) {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/*  89 */     boolean bool = false;
/*     */ 
/*     */     
/*  92 */     int i = Type.getReturnType(paramString).getOpcode(172);
/*  93 */     byte b = 0;
/*     */     
/*  95 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator();
/*  96 */     while (listIterator.hasNext()) {
/*  97 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*     */       
/*  99 */       if (abstractInsnNode instanceof org.spongepowered.asm.lib.tree.InsnNode && abstractInsnNode.getOpcode() == i) {
/* 100 */         if (this.ordinal == -1 || this.ordinal == b) {
/* 101 */           paramCollection.add(abstractInsnNode);
/* 102 */           bool = true;
/*     */         } 
/*     */         
/* 105 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     return bool;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */