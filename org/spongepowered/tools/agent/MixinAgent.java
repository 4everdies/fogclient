/*     */ package org.spongepowered.tools.agent;
/*     */ 
/*     */ import java.lang.instrument.ClassDefinition;
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.lang.instrument.Instrumentation;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTransformer;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
/*     */ import org.spongepowered.asm.service.IMixinService;
/*     */ import org.spongepowered.asm.service.MixinService;
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
/*     */ public class MixinAgent
/*     */   implements IHotSwap
/*     */ {
/*     */   class Transformer
/*     */     implements ClassFileTransformer
/*     */   {
/*     */     public byte[] transform(ClassLoader param1ClassLoader, String param1String, Class<?> param1Class, ProtectionDomain param1ProtectionDomain, byte[] param1ArrayOfbyte) throws IllegalClassFormatException {
/*  58 */       if (param1Class == null) {
/*  59 */         return null;
/*     */       }
/*     */       
/*  62 */       byte[] arrayOfByte = MixinAgent.classLoader.getFakeMixinBytecode(param1Class);
/*  63 */       if (arrayOfByte != null) {
/*  64 */         List<String> list = reloadMixin(param1String, param1ArrayOfbyte);
/*  65 */         if (list == null || !reApplyMixins(list)) {
/*  66 */           return MixinAgent.ERROR_BYTECODE;
/*     */         }
/*     */         
/*  69 */         return arrayOfByte;
/*     */       } 
/*     */       
/*     */       try {
/*  73 */         MixinAgent.logger.info("Redefining class " + param1String);
/*  74 */         return MixinAgent.this.classTransformer.transformClassBytes(null, param1String, param1ArrayOfbyte);
/*  75 */       } catch (Throwable throwable) {
/*  76 */         MixinAgent.logger.error("Error while re-transforming class " + param1String, throwable);
/*  77 */         return MixinAgent.ERROR_BYTECODE;
/*     */       } 
/*     */     }
/*     */     
/*     */     private List<String> reloadMixin(String param1String, byte[] param1ArrayOfbyte) {
/*  82 */       MixinAgent.logger.info("Redefining mixin {}", new Object[] { param1String });
/*     */       try {
/*  84 */         return MixinAgent.this.classTransformer.reload(param1String.replace('/', '.'), param1ArrayOfbyte);
/*  85 */       } catch (MixinReloadException mixinReloadException) {
/*  86 */         MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[] { mixinReloadException.getMixinInfo(), mixinReloadException.getMessage() });
/*  87 */       } catch (Throwable throwable) {
/*     */         
/*  89 */         MixinAgent.logger.error("Error while finding targets for mixin " + param1String, throwable);
/*     */       } 
/*  91 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean reApplyMixins(List<String> param1List) {
/* 102 */       IMixinService iMixinService = MixinService.getService();
/*     */       
/* 104 */       for (String str1 : param1List) {
/* 105 */         String str2 = str1.replace('/', '.');
/* 106 */         MixinAgent.logger.debug("Re-transforming target class {}", new Object[] { str1 });
/*     */         try {
/* 108 */           Class<?> clazz = iMixinService.getClassProvider().findClass(str2);
/* 109 */           byte[] arrayOfByte = MixinAgent.classLoader.getOriginalTargetBytecode(str2);
/* 110 */           if (arrayOfByte == null) {
/* 111 */             MixinAgent.logger.error("Target class {} bytecode is not registered", new Object[] { str2 });
/* 112 */             return false;
/*     */           } 
/* 114 */           arrayOfByte = MixinAgent.this.classTransformer.transformClassBytes(null, str2, arrayOfByte);
/* 115 */           MixinAgent.instrumentation.redefineClasses(new ClassDefinition[] { new ClassDefinition(clazz, arrayOfByte) });
/* 116 */         } catch (Throwable throwable) {
/* 117 */           MixinAgent.logger.error("Error while re-transforming target class " + str1, throwable);
/* 118 */           return false;
/*     */         } 
/*     */       } 
/* 121 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static final byte[] ERROR_BYTECODE = new byte[] { 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   static final MixinAgentClassLoader classLoader = new MixinAgentClassLoader();
/*     */   
/* 137 */   static final Logger logger = LogManager.getLogger("mixin.agent");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   static Instrumentation instrumentation = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private static List<MixinAgent> agents = new ArrayList<MixinAgent>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final MixinTransformer classTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MixinAgent(MixinTransformer paramMixinTransformer) {
/* 162 */     this.classTransformer = paramMixinTransformer;
/* 163 */     agents.add(this);
/* 164 */     if (instrumentation != null) {
/* 165 */       initTransformer();
/*     */     }
/*     */   }
/*     */   
/*     */   private void initTransformer() {
/* 170 */     instrumentation.addTransformer(new Transformer(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerMixinClass(String paramString) {
/* 175 */     classLoader.addMixinClass(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTargetClass(String paramString, byte[] paramArrayOfbyte) {
/* 180 */     classLoader.addTargetClass(paramString, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void init(Instrumentation paramInstrumentation) {
/* 190 */     instrumentation = paramInstrumentation;
/* 191 */     if (!instrumentation.isRedefineClassesSupported()) {
/* 192 */       logger.error("The instrumentation doesn't support re-definition of classes");
/*     */     }
/* 194 */     for (MixinAgent mixinAgent : agents) {
/* 195 */       mixinAgent.initTransformer();
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
/*     */ 
/*     */   
/*     */   public static void premain(String paramString, Instrumentation paramInstrumentation) {
/* 209 */     System.setProperty("mixin.hotSwap", "true");
/* 210 */     init(paramInstrumentation);
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
/*     */   
/*     */   public static void agentmain(String paramString, Instrumentation paramInstrumentation) {
/* 223 */     init(paramInstrumentation);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\agent\MixinAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */