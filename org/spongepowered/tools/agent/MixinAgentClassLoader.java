/*     */ package org.spongepowered.tools.agent;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.ClassWriter;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
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
/*     */ class MixinAgentClassLoader
/*     */   extends ClassLoader
/*     */ {
/*  45 */   private static final Logger logger = LogManager.getLogger("mixin.agent");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private Map<Class<?>, byte[]> mixins = (Map)new HashMap<Class<?>, byte>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private Map<String, byte[]> targets = (Map)new HashMap<String, byte>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addMixinClass(String paramString) {
/*  64 */     logger.debug("Mixin class {} added to class loader", new Object[] { paramString });
/*     */     try {
/*  66 */       byte[] arrayOfByte = materialise(paramString);
/*  67 */       Class<?> clazz = defineClass(paramString, arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/*     */       
/*  70 */       clazz.newInstance();
/*  71 */       this.mixins.put(clazz, arrayOfByte);
/*  72 */     } catch (Throwable throwable) {
/*  73 */       logger.catching(throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addTargetClass(String paramString, byte[] paramArrayOfbyte) {
/*  84 */     this.targets.put(paramString, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getFakeMixinBytecode(Class<?> paramClass) {
/*  94 */     return this.mixins.get(paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getOriginalTargetBytecode(String paramString) {
/* 104 */     return this.targets.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] materialise(String paramString) {
/* 114 */     ClassWriter classWriter = new ClassWriter(3);
/* 115 */     classWriter.visit(MixinEnvironment.getCompatibilityLevel().classVersion(), 1, paramString.replace('.', '/'), null, 
/* 116 */         Type.getInternalName(Object.class), null);
/*     */ 
/*     */     
/* 119 */     MethodVisitor methodVisitor = classWriter.visitMethod(1, "<init>", "()V", null, null);
/* 120 */     methodVisitor.visitCode();
/* 121 */     methodVisitor.visitVarInsn(25, 0);
/* 122 */     methodVisitor.visitMethodInsn(183, Type.getInternalName(Object.class), "<init>", "()V", false);
/* 123 */     methodVisitor.visitInsn(177);
/* 124 */     methodVisitor.visitMaxs(1, 1);
/* 125 */     methodVisitor.visitEnd();
/*     */     
/* 127 */     classWriter.visitEnd();
/* 128 */     return classWriter.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\agent\MixinAgentClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */