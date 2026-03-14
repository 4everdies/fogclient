/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.ClassReader;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.commons.ClassRemapper;
/*     */ import org.spongepowered.asm.lib.commons.Remapper;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.service.MixinService;
/*     */ import org.spongepowered.asm.transformers.MixinClassWriter;
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
/*     */ final class InnerClassGenerator
/*     */   implements IClassGenerator
/*     */ {
/*     */   static class InnerClassInfo
/*     */     extends Remapper
/*     */   {
/*     */     private final String name;
/*     */     private final String originalName;
/*     */     private final MixinInfo owner;
/*     */     private final MixinTargetContext target;
/*     */     private final String ownerName;
/*     */     private final String targetName;
/*     */     
/*     */     InnerClassInfo(String param1String1, String param1String2, MixinInfo param1MixinInfo, MixinTargetContext param1MixinTargetContext) {
/*  90 */       this.name = param1String1;
/*  91 */       this.originalName = param1String2;
/*  92 */       this.owner = param1MixinInfo;
/*  93 */       this.ownerName = param1MixinInfo.getClassRef();
/*  94 */       this.target = param1MixinTargetContext;
/*  95 */       this.targetName = param1MixinTargetContext.getTargetClassRef();
/*     */     }
/*     */     
/*     */     String getName() {
/*  99 */       return this.name;
/*     */     }
/*     */     
/*     */     String getOriginalName() {
/* 103 */       return this.originalName;
/*     */     }
/*     */     
/*     */     MixinInfo getOwner() {
/* 107 */       return this.owner;
/*     */     }
/*     */     
/*     */     MixinTargetContext getTarget() {
/* 111 */       return this.target;
/*     */     }
/*     */     
/*     */     String getOwnerName() {
/* 115 */       return this.ownerName;
/*     */     }
/*     */     
/*     */     String getTargetName() {
/* 119 */       return this.targetName;
/*     */     }
/*     */     
/*     */     byte[] getClassBytes() throws ClassNotFoundException, IOException {
/* 123 */       return MixinService.getService().getBytecodeProvider().getClassBytes(this.originalName, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String mapMethodName(String param1String1, String param1String2, String param1String3) {
/* 132 */       if (this.ownerName.equalsIgnoreCase(param1String1)) {
/* 133 */         ClassInfo.Method method = this.owner.getClassInfo().findMethod(param1String2, param1String3, 10);
/* 134 */         if (method != null) {
/* 135 */           return method.getName();
/*     */         }
/*     */       } 
/* 138 */       return super.mapMethodName(param1String1, param1String2, param1String3);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String map(String param1String) {
/* 146 */       if (this.originalName.equals(param1String))
/* 147 */         return this.name; 
/* 148 */       if (this.ownerName.equals(param1String)) {
/* 149 */         return this.targetName;
/*     */       }
/* 151 */       return param1String;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class InnerClassAdapter
/*     */     extends ClassRemapper
/*     */   {
/*     */     private final InnerClassGenerator.InnerClassInfo info;
/*     */ 
/*     */ 
/*     */     
/*     */     public InnerClassAdapter(ClassVisitor param1ClassVisitor, InnerClassGenerator.InnerClassInfo param1InnerClassInfo) {
/* 173 */       super(327680, param1ClassVisitor, param1InnerClassInfo);
/* 174 */       this.info = param1InnerClassInfo;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitSource(String param1String1, String param1String2) {
/* 183 */       super.visitSource(param1String1, param1String2);
/* 184 */       AnnotationVisitor annotationVisitor = this.cv.visitAnnotation("Lorg/spongepowered/asm/mixin/transformer/meta/MixinInner;", false);
/* 185 */       annotationVisitor.visit("mixin", this.info.getOwner().toString());
/* 186 */       annotationVisitor.visit("name", this.info.getOriginalName().substring(this.info.getOriginalName().lastIndexOf('/') + 1));
/* 187 */       annotationVisitor.visitEnd();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitInnerClass(String param1String1, String param1String2, String param1String3, int param1Int) {
/* 197 */       if (param1String1.startsWith(this.info.getOriginalName() + "$")) {
/* 198 */         throw new InvalidMixinException(this.info.getOwner(), "Found unsupported nested inner class " + param1String1 + " in " + this.info
/* 199 */             .getOriginalName());
/*     */       }
/*     */       
/* 202 */       super.visitInnerClass(param1String1, param1String2, param1String3, param1Int);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   private final Map<String, String> innerClassNames = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   private final Map<String, InnerClassInfo> innerClasses = new HashMap<String, InnerClassInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String registerInnerClass(MixinInfo paramMixinInfo, String paramString, MixinTargetContext paramMixinTargetContext) {
/* 230 */     String str1 = String.format("%s%s", new Object[] { paramString, paramMixinTargetContext });
/* 231 */     String str2 = this.innerClassNames.get(str1);
/* 232 */     if (str2 == null) {
/* 233 */       str2 = getUniqueReference(paramString, paramMixinTargetContext);
/* 234 */       this.innerClassNames.put(str1, str2);
/* 235 */       this.innerClasses.put(str2, new InnerClassInfo(str2, paramString, paramMixinInfo, paramMixinTargetContext));
/* 236 */       logger.debug("Inner class {} in {} on {} gets unique name {}", new Object[] { paramString, paramMixinInfo.getClassRef(), paramMixinTargetContext
/* 237 */             .getTargetClassRef(), str2 });
/*     */     } 
/* 239 */     return str2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String paramString) {
/* 248 */     String str = paramString.replace('.', '/');
/* 249 */     InnerClassInfo innerClassInfo = this.innerClasses.get(str);
/* 250 */     if (innerClassInfo != null) {
/* 251 */       return generate(innerClassInfo);
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] generate(InnerClassInfo paramInnerClassInfo) {
/*     */     try {
/* 265 */       logger.debug("Generating mapped inner class {} (originally {})", new Object[] { paramInnerClassInfo.getName(), paramInnerClassInfo.getOriginalName() });
/* 266 */       ClassReader classReader = new ClassReader(paramInnerClassInfo.getClassBytes());
/* 267 */       MixinClassWriter mixinClassWriter = new MixinClassWriter(classReader, 0);
/* 268 */       classReader.accept((ClassVisitor)new InnerClassAdapter((ClassVisitor)mixinClassWriter, paramInnerClassInfo), 8);
/* 269 */       return mixinClassWriter.toByteArray();
/* 270 */     } catch (InvalidMixinException invalidMixinException) {
/* 271 */       throw invalidMixinException;
/* 272 */     } catch (Exception exception) {
/* 273 */       logger.catching(exception);
/*     */ 
/*     */       
/* 276 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getUniqueReference(String paramString, MixinTargetContext paramMixinTargetContext) {
/* 288 */     String str = paramString.substring(paramString.lastIndexOf('$') + 1);
/* 289 */     if (str.matches("^[0-9]+$")) {
/* 290 */       str = "Anonymous";
/*     */     }
/* 292 */     return String.format("%s$%s$%s", new Object[] { paramMixinTargetContext.getTargetClassRef(), str, UUID.randomUUID().toString().replace("-", "") });
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\InnerClassGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */