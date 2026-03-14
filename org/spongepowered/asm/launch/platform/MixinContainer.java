/*     */ package org.spongepowered.asm.launch.platform;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.launch.GlobalProperties;
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
/*     */ public class MixinContainer
/*     */ {
/*  43 */   private static final List<String> agentClasses = new ArrayList<String>();
/*     */   
/*     */   static {
/*  46 */     GlobalProperties.put("mixin.agents", agentClasses);
/*  47 */     for (String str : MixinService.getService().getPlatformAgents()) {
/*  48 */       agentClasses.add(str);
/*     */     }
/*  50 */     agentClasses.add("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
/*     */   }
/*     */   
/*  53 */   private final Logger logger = LogManager.getLogger("mixin");
/*     */   
/*     */   private final URI uri;
/*     */   
/*  57 */   private final List<IMixinPlatformAgent> agents = new ArrayList<IMixinPlatformAgent>();
/*     */   
/*     */   public MixinContainer(MixinPlatformManager paramMixinPlatformManager, URI paramURI) {
/*  60 */     this.uri = paramURI;
/*     */     
/*  62 */     for (String str : agentClasses) {
/*     */       
/*     */       try {
/*  65 */         Class<?> clazz = Class.forName(str);
/*  66 */         Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { MixinPlatformManager.class, URI.class });
/*  67 */         this.logger.debug("Instancing new {} for {}", new Object[] { clazz.getSimpleName(), this.uri });
/*  68 */         IMixinPlatformAgent iMixinPlatformAgent = (IMixinPlatformAgent)constructor.newInstance(new Object[] { paramMixinPlatformManager, paramURI });
/*  69 */         this.agents.add(iMixinPlatformAgent);
/*  70 */       } catch (Exception exception) {
/*  71 */         this.logger.catching(exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  80 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getPhaseProviders() {
/*  87 */     ArrayList<String> arrayList = new ArrayList();
/*  88 */     for (IMixinPlatformAgent iMixinPlatformAgent : this.agents) {
/*  89 */       String str = iMixinPlatformAgent.getPhaseProvider();
/*  90 */       if (str != null) {
/*  91 */         arrayList.add(str);
/*     */       }
/*     */     } 
/*  94 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 101 */     for (IMixinPlatformAgent iMixinPlatformAgent : this.agents) {
/* 102 */       this.logger.debug("Processing prepare() for {}", new Object[] { iMixinPlatformAgent });
/* 103 */       iMixinPlatformAgent.prepare();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initPrimaryContainer() {
/* 112 */     for (IMixinPlatformAgent iMixinPlatformAgent : this.agents) {
/* 113 */       this.logger.debug("Processing launch tasks for {}", new Object[] { iMixinPlatformAgent });
/* 114 */       iMixinPlatformAgent.initPrimaryContainer();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inject() {
/* 122 */     for (IMixinPlatformAgent iMixinPlatformAgent : this.agents) {
/* 123 */       this.logger.debug("Processing inject() for {}", new Object[] { iMixinPlatformAgent });
/* 124 */       iMixinPlatformAgent.inject();
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
/*     */   public String getLaunchTarget() {
/* 136 */     for (IMixinPlatformAgent iMixinPlatformAgent : this.agents) {
/* 137 */       String str = iMixinPlatformAgent.getLaunchTarget();
/* 138 */       if (str != null) {
/* 139 */         return str;
/*     */       }
/*     */     } 
/* 142 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\platform\MixinContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */