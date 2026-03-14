/*    */ package org.spongepowered.asm.mixin.gen;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.spongepowered.asm.lib.tree.MethodNode;
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
/*    */ public abstract class AccessorGenerator
/*    */ {
/*    */   protected final AccessorInfo info;
/*    */   
/*    */   public AccessorGenerator(AccessorInfo paramAccessorInfo) {
/* 44 */     this.info = paramAccessorInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final MethodNode createMethod(int paramInt1, int paramInt2) {
/* 55 */     MethodNode methodNode1 = this.info.getMethod();
/* 56 */     MethodNode methodNode2 = new MethodNode(327680, methodNode1.access & 0xFFFFFBFF | 0x1000, methodNode1.name, methodNode1.desc, null, null);
/*    */     
/* 58 */     methodNode2.visibleAnnotations = new ArrayList();
/* 59 */     methodNode2.visibleAnnotations.add(this.info.getAnnotation());
/* 60 */     methodNode2.maxLocals = paramInt1;
/* 61 */     methodNode2.maxStack = paramInt2;
/* 62 */     return methodNode2;
/*    */   }
/*    */   
/*    */   public abstract MethodNode generate();
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\AccessorGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */