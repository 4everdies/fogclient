/*    */ package org.spongepowered.asm.transformers;
/*    */ 
/*    */ import org.spongepowered.asm.lib.ClassReader;
/*    */ import org.spongepowered.asm.lib.ClassVisitor;
/*    */ import org.spongepowered.asm.lib.tree.ClassNode;
/*    */ import org.spongepowered.asm.service.ILegacyClassTransformer;
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
/*    */ public abstract class TreeTransformer
/*    */   implements ILegacyClassTransformer
/*    */ {
/*    */   private ClassReader classReader;
/*    */   private ClassNode classNode;
/*    */   
/*    */   protected final ClassNode readClass(byte[] paramArrayOfbyte) {
/* 45 */     return readClass(paramArrayOfbyte, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final ClassNode readClass(byte[] paramArrayOfbyte, boolean paramBoolean) {
/* 55 */     ClassReader classReader = new ClassReader(paramArrayOfbyte);
/* 56 */     if (paramBoolean) {
/* 57 */       this.classReader = classReader;
/*    */     }
/*    */     
/* 60 */     ClassNode classNode = new ClassNode();
/* 61 */     classReader.accept((ClassVisitor)classNode, 8);
/* 62 */     return classNode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final byte[] writeClass(ClassNode paramClassNode) {
/* 71 */     if (this.classReader != null && this.classNode == paramClassNode) {
/* 72 */       this.classNode = null;
/* 73 */       MixinClassWriter mixinClassWriter1 = new MixinClassWriter(this.classReader, 3);
/* 74 */       this.classReader = null;
/* 75 */       paramClassNode.accept((ClassVisitor)mixinClassWriter1);
/* 76 */       return mixinClassWriter1.toByteArray();
/*    */     } 
/*    */     
/* 79 */     this.classNode = null;
/*    */     
/* 81 */     MixinClassWriter mixinClassWriter = new MixinClassWriter(3);
/* 82 */     paramClassNode.accept((ClassVisitor)mixinClassWriter);
/* 83 */     return mixinClassWriter.toByteArray();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\transformers\TreeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */