/*     */ package org.spongepowered.tools.obfuscation.service;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.tools.obfuscation.ObfuscationType;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
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
/*     */ public final class ObfuscationServices
/*     */ {
/*     */   private static ObfuscationServices instance;
/*     */   private final ServiceLoader<IObfuscationService> serviceLoader;
/*  56 */   private final Set<IObfuscationService> services = new HashSet<IObfuscationService>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObfuscationServices() {
/*  62 */     this.serviceLoader = ServiceLoader.load(IObfuscationService.class, getClass().getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ObfuscationServices getInstance() {
/*  69 */     if (instance == null) {
/*  70 */       instance = new ObfuscationServices();
/*     */     }
/*  72 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initProviders(IMixinAnnotationProcessor paramIMixinAnnotationProcessor) {
/*     */     try {
/*  82 */       for (IObfuscationService iObfuscationService : this.serviceLoader) {
/*  83 */         if (!this.services.contains(iObfuscationService)) {
/*  84 */           this.services.add(iObfuscationService);
/*     */           
/*  86 */           String str = iObfuscationService.getClass().getSimpleName();
/*     */           
/*  88 */           Collection<ObfuscationTypeDescriptor> collection = iObfuscationService.getObfuscationTypes();
/*  89 */           if (collection != null) {
/*  90 */             for (ObfuscationTypeDescriptor obfuscationTypeDescriptor : collection) {
/*     */               try {
/*  92 */                 ObfuscationType obfuscationType = ObfuscationType.create(obfuscationTypeDescriptor, paramIMixinAnnotationProcessor);
/*  93 */                 paramIMixinAnnotationProcessor.printMessage(Diagnostic.Kind.NOTE, str + " supports type: \"" + obfuscationType + "\"");
/*  94 */               } catch (Exception exception) {
/*  95 */                 exception.printStackTrace();
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 101 */     } catch (ServiceConfigurationError serviceConfigurationError) {
/* 102 */       paramIMixinAnnotationProcessor.printMessage(Diagnostic.Kind.ERROR, serviceConfigurationError.getClass().getSimpleName() + ": " + serviceConfigurationError.getMessage());
/* 103 */       serviceConfigurationError.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getSupportedOptions() {
/* 111 */     HashSet<String> hashSet = new HashSet();
/* 112 */     for (IObfuscationService iObfuscationService : this.serviceLoader) {
/* 113 */       Set<String> set = iObfuscationService.getSupportedOptions();
/* 114 */       if (set != null) {
/* 115 */         hashSet.addAll(set);
/*     */       }
/*     */     } 
/* 118 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IObfuscationService getService(Class<? extends IObfuscationService> paramClass) {
/* 128 */     for (IObfuscationService iObfuscationService : this.serviceLoader) {
/* 129 */       if (paramClass.getName().equals(iObfuscationService.getClass().getName())) {
/* 130 */         return iObfuscationService;
/*     */       }
/*     */     } 
/* 133 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\service\ObfuscationServices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */