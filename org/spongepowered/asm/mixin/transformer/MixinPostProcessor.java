/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.ClassReader;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.FieldVisitor;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.gen.Accessor;
/*     */ import org.spongepowered.asm.mixin.gen.Invoker;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
/*     */ import org.spongepowered.asm.transformers.MixinClassWriter;
/*     */ import org.spongepowered.asm.transformers.TreeTransformer;
/*     */ import org.spongepowered.asm.util.Bytecode;
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
/*     */ class MixinPostProcessor
/*     */   extends TreeTransformer
/*     */   implements MixinConfig.IListener
/*     */ {
/*  66 */   private final Set<String> syntheticInnerClasses = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private final Map<String, MixinInfo> accessorMixins = new HashMap<String, MixinInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private final Set<String> loadable = new HashSet<String>();
/*     */ 
/*     */   
/*     */   public void onInit(MixinInfo paramMixinInfo) {
/*  80 */     for (String str : paramMixinInfo.getSyntheticInnerClasses()) {
/*  81 */       registerSyntheticInner(str.replace('/', '.'));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPrepare(MixinInfo paramMixinInfo) {
/*  87 */     String str = paramMixinInfo.getClassName();
/*     */     
/*  89 */     if (paramMixinInfo.isLoadable()) {
/*  90 */       registerLoadable(str);
/*     */     }
/*     */     
/*  93 */     if (paramMixinInfo.isAccessor()) {
/*  94 */       registerAccessor(paramMixinInfo);
/*     */     }
/*     */   }
/*     */   
/*     */   void registerSyntheticInner(String paramString) {
/*  99 */     this.syntheticInnerClasses.add(paramString);
/*     */   }
/*     */   
/*     */   void registerLoadable(String paramString) {
/* 103 */     this.loadable.add(paramString);
/*     */   }
/*     */   
/*     */   void registerAccessor(MixinInfo paramMixinInfo) {
/* 107 */     registerLoadable(paramMixinInfo.getClassName());
/* 108 */     this.accessorMixins.put(paramMixinInfo.getClassName(), paramMixinInfo);
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
/*     */   boolean canTransform(String paramString) {
/* 120 */     return (this.syntheticInnerClasses.contains(paramString) || this.loadable.contains(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 128 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDelegationExcluded() {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] transformClassBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
/* 146 */     if (this.syntheticInnerClasses.contains(paramString2)) {
/* 147 */       return processSyntheticInner(paramArrayOfbyte);
/*     */     }
/*     */     
/* 150 */     if (this.accessorMixins.containsKey(paramString2)) {
/* 151 */       MixinInfo mixinInfo = this.accessorMixins.get(paramString2);
/* 152 */       return processAccessor(paramArrayOfbyte, mixinInfo);
/*     */     } 
/*     */     
/* 155 */     return paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] processSyntheticInner(byte[] paramArrayOfbyte) {
/* 164 */     ClassReader classReader = new ClassReader(paramArrayOfbyte);
/* 165 */     MixinClassWriter mixinClassWriter = new MixinClassWriter(classReader, 0);
/*     */     
/* 167 */     ClassVisitor classVisitor = new ClassVisitor(327680, (ClassVisitor)mixinClassWriter)
/*     */       {
/*     */         public void visit(int param1Int1, int param1Int2, String param1String1, String param1String2, String param1String3, String[] param1ArrayOfString)
/*     */         {
/* 171 */           super.visit(param1Int1, param1Int2 | 0x1, param1String1, param1String2, param1String3, param1ArrayOfString);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public FieldVisitor visitField(int param1Int, String param1String1, String param1String2, String param1String3, Object param1Object) {
/* 177 */           if ((param1Int & 0x6) == 0) {
/* 178 */             param1Int |= 0x1;
/*     */           }
/* 180 */           return super.visitField(param1Int, param1String1, param1String2, param1String3, param1Object);
/*     */         }
/*     */ 
/*     */         
/*     */         public MethodVisitor visitMethod(int param1Int, String param1String1, String param1String2, String param1String3, String[] param1ArrayOfString) {
/* 185 */           if ((param1Int & 0x6) == 0) {
/* 186 */             param1Int |= 0x1;
/*     */           }
/* 188 */           return super.visitMethod(param1Int, param1String1, param1String2, param1String3, param1ArrayOfString);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 193 */     classReader.accept(classVisitor, 8);
/* 194 */     return mixinClassWriter.toByteArray();
/*     */   }
/*     */   
/*     */   private byte[] processAccessor(byte[] paramArrayOfbyte, MixinInfo paramMixinInfo) {
/* 198 */     if (!MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8)) {
/* 199 */       return paramArrayOfbyte;
/*     */     }
/*     */     
/* 202 */     boolean bool = false;
/* 203 */     MixinInfo.MixinClassNode mixinClassNode = paramMixinInfo.getClassNode(0);
/* 204 */     ClassInfo classInfo = paramMixinInfo.getTargets().get(0);
/*     */     
/* 206 */     for (MixinInfo.MixinMethodNode mixinMethodNode : mixinClassNode.mixinMethods) {
/*     */       
/* 208 */       if (!Bytecode.hasFlag(mixinMethodNode, 8)) {
/*     */         continue;
/*     */       }
/*     */       
/* 212 */       AnnotationNode annotationNode1 = mixinMethodNode.getVisibleAnnotation((Class)Accessor.class);
/* 213 */       AnnotationNode annotationNode2 = mixinMethodNode.getVisibleAnnotation((Class)Invoker.class);
/* 214 */       if (annotationNode1 != null || annotationNode2 != null) {
/* 215 */         ClassInfo.Method method = getAccessorMethod(paramMixinInfo, mixinMethodNode, classInfo);
/* 216 */         createProxy(mixinMethodNode, classInfo, method);
/* 217 */         bool = true;
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     if (bool) {
/* 222 */       return writeClass(mixinClassNode);
/*     */     }
/*     */     
/* 225 */     return paramArrayOfbyte;
/*     */   }
/*     */   
/*     */   private static ClassInfo.Method getAccessorMethod(MixinInfo paramMixinInfo, MethodNode paramMethodNode, ClassInfo paramClassInfo) throws MixinTransformerError {
/* 229 */     ClassInfo.Method method = paramMixinInfo.getClassInfo().findMethod(paramMethodNode, 10);
/*     */ 
/*     */ 
/*     */     
/* 233 */     if (!method.isRenamed()) {
/* 234 */       throw new MixinTransformerError("Unexpected state: " + paramMixinInfo + " loaded before " + paramClassInfo + " was conformed");
/*     */     }
/*     */     
/* 237 */     return method;
/*     */   }
/*     */   
/*     */   private static void createProxy(MethodNode paramMethodNode, ClassInfo paramClassInfo, ClassInfo.Method paramMethod) {
/* 241 */     paramMethodNode.instructions.clear();
/* 242 */     Type[] arrayOfType = Type.getArgumentTypes(paramMethodNode.desc);
/* 243 */     Type type = Type.getReturnType(paramMethodNode.desc);
/* 244 */     Bytecode.loadArgs(arrayOfType, paramMethodNode.instructions, 0);
/* 245 */     paramMethodNode.instructions.add((AbstractInsnNode)new MethodInsnNode(184, paramClassInfo.getName(), paramMethod.getName(), paramMethodNode.desc, false));
/* 246 */     paramMethodNode.instructions.add((AbstractInsnNode)new InsnNode(type.getOpcode(172)));
/* 247 */     paramMethodNode.maxStack = Bytecode.getFirstNonArgLocalIndex(arrayOfType, false);
/* 248 */     paramMethodNode.maxLocals = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */