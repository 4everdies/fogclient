/*    */ package org.spongepowered.asm.mixin.injection.points;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.ListIterator;
/*    */ import org.spongepowered.asm.lib.Type;
/*    */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*    */ import org.spongepowered.asm.lib.tree.InsnList;
/*    */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*    */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*    */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*    */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*    */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @AtCode("TAIL")
/*    */ public class BeforeFinalReturn
/*    */   extends InjectionPoint
/*    */ {
/*    */   private final IMixinContext context;
/*    */   
/*    */   public BeforeFinalReturn(InjectionPointData paramInjectionPointData) {
/* 64 */     super(paramInjectionPointData);
/*    */     
/* 66 */     this.context = paramInjectionPointData.getContext();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkPriority(int paramInt1, int paramInt2) {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 76 */     AbstractInsnNode abstractInsnNode = null;
/*    */ 
/*    */     
/* 79 */     int i = Type.getReturnType(paramString).getOpcode(172);
/*    */     
/* 81 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator();
/* 82 */     while (listIterator.hasNext()) {
/* 83 */       AbstractInsnNode abstractInsnNode1 = listIterator.next();
/* 84 */       if (abstractInsnNode1 instanceof org.spongepowered.asm.lib.tree.InsnNode && abstractInsnNode1.getOpcode() == i) {
/* 85 */         abstractInsnNode = abstractInsnNode1;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 90 */     if (abstractInsnNode == null) {
/* 91 */       throw new InvalidInjectionException(this.context, "TAIL could not locate a valid RETURN in the target method!");
/*    */     }
/*    */     
/* 94 */     paramCollection.add(abstractInsnNode);
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeFinalReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */