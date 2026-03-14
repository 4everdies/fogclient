/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
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
/*     */ 
/*     */ @AtCode("JUMP")
/*     */ public class JumpInsnPoint
/*     */   extends InjectionPoint
/*     */ {
/*     */   private final int opCode;
/*     */   private final int ordinal;
/*     */   
/*     */   public JumpInsnPoint(InjectionPointData paramInjectionPointData) {
/*  78 */     this.opCode = paramInjectionPointData.getOpcode(-1, new int[] { 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 198, 199, -1 });
/*     */ 
/*     */     
/*  81 */     this.ordinal = paramInjectionPointData.getOrdinal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/*  86 */     boolean bool = false;
/*  87 */     byte b = 0;
/*     */     
/*  89 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator();
/*  90 */     while (listIterator.hasNext()) {
/*  91 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*     */       
/*  93 */       if (abstractInsnNode instanceof org.spongepowered.asm.lib.tree.JumpInsnNode && (this.opCode == -1 || abstractInsnNode.getOpcode() == this.opCode)) {
/*  94 */         if (this.ordinal == -1 || this.ordinal == b) {
/*  95 */           paramCollection.add(abstractInsnNode);
/*  96 */           bool = true;
/*     */         } 
/*     */         
/*  99 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return bool;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\JumpInsnPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */