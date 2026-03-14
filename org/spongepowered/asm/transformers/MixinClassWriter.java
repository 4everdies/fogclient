/*    */ package org.spongepowered.asm.transformers;
/*    */ 
/*    */ import org.spongepowered.asm.lib.ClassReader;
/*    */ import org.spongepowered.asm.lib.ClassWriter;
/*    */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
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
/*    */ public class MixinClassWriter
/*    */   extends ClassWriter
/*    */ {
/*    */   public MixinClassWriter(int paramInt) {
/* 38 */     super(paramInt);
/*    */   }
/*    */   
/*    */   public MixinClassWriter(ClassReader paramClassReader, int paramInt) {
/* 42 */     super(paramClassReader, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getCommonSuperClass(String paramString1, String paramString2) {
/* 51 */     return ClassInfo.getCommonSuperClass(paramString1, paramString2).getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\transformers\MixinClassWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */