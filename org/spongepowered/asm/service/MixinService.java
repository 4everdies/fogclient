/*     */ package org.spongepowered.asm.service;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public final class MixinService
/*     */ {
/*  47 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */   
/*     */   private static MixinService instance;
/*     */ 
/*     */   
/*     */   private ServiceLoader<IMixinServiceBootstrap> bootstrapServiceLoader;
/*     */ 
/*     */   
/*  56 */   private final Set<String> bootedServices = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ServiceLoader<IMixinService> serviceLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private IMixinService service = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MixinService() {
/*  72 */     runBootServices();
/*     */   }
/*     */   
/*     */   private void runBootServices() {
/*  76 */     this.bootstrapServiceLoader = ServiceLoader.load(IMixinServiceBootstrap.class, getClass().getClassLoader());
/*  77 */     for (IMixinServiceBootstrap iMixinServiceBootstrap : this.bootstrapServiceLoader) {
/*     */       try {
/*  79 */         iMixinServiceBootstrap.bootstrap();
/*  80 */         this.bootedServices.add(iMixinServiceBootstrap.getServiceClassName());
/*  81 */       } catch (Throwable throwable) {
/*  82 */         logger.catching(throwable);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MixinService getInstance() {
/*  91 */     if (instance == null) {
/*  92 */       instance = new MixinService();
/*     */     }
/*     */     
/*  95 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void boot() {
/* 102 */     getInstance();
/*     */   }
/*     */   
/*     */   public static IMixinService getService() {
/* 106 */     return getInstance().getServiceInstance();
/*     */   }
/*     */   
/*     */   private synchronized IMixinService getServiceInstance() {
/* 110 */     if (this.service == null) {
/* 111 */       this.service = initService();
/* 112 */       if (this.service == null) {
/* 113 */         throw new ServiceNotAvailableError("No mixin host service is available");
/*     */       }
/*     */     } 
/* 116 */     return this.service;
/*     */   }
/*     */   
/*     */   private IMixinService initService() {
/* 120 */     this.serviceLoader = ServiceLoader.load(IMixinService.class, getClass().getClassLoader());
/* 121 */     Iterator<IMixinService> iterator = this.serviceLoader.iterator();
/* 122 */     while (iterator.hasNext()) {
/*     */       try {
/* 124 */         IMixinService iMixinService = iterator.next();
/* 125 */         if (this.bootedServices.contains(iMixinService.getClass().getName())) {
/* 126 */           logger.debug("MixinService [{}] was successfully booted in {}", new Object[] { iMixinService.getName(), getClass().getClassLoader() });
/*     */         }
/* 128 */         if (iMixinService.isValid()) {
/* 129 */           return iMixinService;
/*     */         }
/* 131 */       } catch (ServiceConfigurationError serviceConfigurationError) {
/* 132 */         serviceConfigurationError.printStackTrace();
/* 133 */       } catch (Throwable throwable) {
/* 134 */         throwable.printStackTrace();
/*     */       } 
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\service\MixinService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */