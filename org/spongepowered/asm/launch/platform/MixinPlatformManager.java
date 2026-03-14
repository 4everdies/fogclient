/*     */ package org.spongepowered.asm.launch.platform;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.Mixins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MixinPlatformManager
/*     */ {
/*     */   private static final String DEFAULT_MAIN_CLASS = "net.minecraft.client.main.Main";
/*     */   private static final String MIXIN_TWEAKER_CLASS = "org.spongepowered.asm.launch.MixinTweaker";
/*  62 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private final Map<URI, MixinContainer> containers = new LinkedHashMap<URI, MixinContainer>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MixinContainer primaryContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prepared = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean injected;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  98 */     logger.debug("Initialising Mixin Platform Manager");
/*     */ 
/*     */     
/* 101 */     URI uRI = null;
/*     */     try {
/* 103 */       uRI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
/* 104 */       if (uRI != null) {
/* 105 */         logger.debug("Mixin platform: primary container is {}", new Object[] { uRI });
/* 106 */         this.primaryContainer = addContainer(uRI);
/*     */       } 
/* 108 */     } catch (URISyntaxException uRISyntaxException) {
/* 109 */       uRISyntaxException.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 113 */     scanClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getPhaseProviderClasses() {
/* 120 */     Collection<String> collection = this.primaryContainer.getPhaseProviders();
/* 121 */     if (collection != null) {
/* 122 */       return Collections.unmodifiableCollection(collection);
/*     */     }
/*     */     
/* 125 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MixinContainer addContainer(URI paramURI) {
/* 136 */     MixinContainer mixinContainer1 = this.containers.get(paramURI);
/* 137 */     if (mixinContainer1 != null) {
/* 138 */       return mixinContainer1;
/*     */     }
/*     */     
/* 141 */     logger.debug("Adding mixin platform agents for container {}", new Object[] { paramURI });
/* 142 */     MixinContainer mixinContainer2 = new MixinContainer(this, paramURI);
/* 143 */     this.containers.put(paramURI, mixinContainer2);
/*     */     
/* 145 */     if (this.prepared) {
/* 146 */       mixinContainer2.prepare();
/*     */     }
/* 148 */     return mixinContainer2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void prepare(List<String> paramList) {
/* 157 */     this.prepared = true;
/* 158 */     for (MixinContainer mixinContainer : this.containers.values()) {
/* 159 */       mixinContainer.prepare();
/*     */     }
/* 161 */     if (paramList != null) {
/* 162 */       parseArgs(paramList);
/*     */     } else {
/* 164 */       String str = System.getProperty("sun.java.command");
/* 165 */       if (str != null) {
/* 166 */         parseArgs(Arrays.asList(str.split(" ")));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseArgs(List<String> paramList) {
/* 177 */     boolean bool = false;
/* 178 */     for (String str : paramList) {
/* 179 */       if (bool) {
/* 180 */         addConfig(str);
/*     */       }
/* 182 */       bool = "--mixin".equals(str);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void inject() {
/* 190 */     if (this.injected) {
/*     */       return;
/*     */     }
/* 193 */     this.injected = true;
/*     */     
/* 195 */     if (this.primaryContainer != null) {
/* 196 */       this.primaryContainer.initPrimaryContainer();
/*     */     }
/*     */     
/* 199 */     scanClasspath();
/* 200 */     logger.debug("inject() running with {} agents", new Object[] { Integer.valueOf(this.containers.size()) });
/* 201 */     for (MixinContainer mixinContainer : this.containers.values()) {
/*     */       try {
/* 203 */         mixinContainer.inject();
/* 204 */       } catch (Exception exception) {
/* 205 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void scanClasspath() {
/* 215 */     URL[] arrayOfURL = MixinService.getService().getClassProvider().getClassPath();
/* 216 */     for (URL uRL : arrayOfURL) {
/*     */       try {
/* 218 */         URI uRI = uRL.toURI();
/* 219 */         if (!this.containers.containsKey(uRI)) {
/*     */ 
/*     */           
/* 222 */           logger.debug("Scanning {} for mixin tweaker", new Object[] { uRI });
/* 223 */           if ("file".equals(uRI.getScheme()) && (new File(uRI)).exists())
/*     */           
/*     */           { 
/* 226 */             MainAttributes mainAttributes = MainAttributes.of(uRI);
/* 227 */             String str = mainAttributes.get("TweakClass");
/* 228 */             if ("org.spongepowered.asm.launch.MixinTweaker".equals(str))
/* 229 */             { logger.debug("{} contains a mixin tweaker, adding agents", new Object[] { uRI });
/* 230 */               addContainer(uRI); }  } 
/*     */         } 
/* 232 */       } catch (Exception exception) {
/* 233 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLaunchTarget() {
/* 243 */     for (MixinContainer mixinContainer : this.containers.values()) {
/* 244 */       String str = mixinContainer.getLaunchTarget();
/* 245 */       if (str != null) {
/* 246 */         return str;
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return "net.minecraft.client.main.Main";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setCompatibilityLevel(String paramString) {
/*     */     try {
/* 262 */       MixinEnvironment.CompatibilityLevel compatibilityLevel = MixinEnvironment.CompatibilityLevel.valueOf(paramString.toUpperCase());
/* 263 */       logger.debug("Setting mixin compatibility level: {}", new Object[] { compatibilityLevel });
/* 264 */       MixinEnvironment.setCompatibilityLevel(compatibilityLevel);
/* 265 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 266 */       logger.warn("Invalid compatibility level specified: {}", new Object[] { paramString });
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
/*     */   final void addConfig(String paramString) {
/* 280 */     if (paramString.endsWith(".json")) {
/* 281 */       logger.debug("Registering mixin config: {}", new Object[] { paramString });
/* 282 */       Mixins.addConfiguration(paramString);
/* 283 */     } else if (paramString.contains(".json@")) {
/* 284 */       int i = paramString.indexOf(".json@");
/* 285 */       String str = paramString.substring(i + 6);
/* 286 */       paramString = paramString.substring(0, i + 5);
/* 287 */       MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(str);
/* 288 */       if (phase != null) {
/* 289 */         logger.warn("Setting config phase via manifest is deprecated: {}. Specify target in config instead", new Object[] { paramString });
/* 290 */         logger.debug("Registering mixin config: {}", new Object[] { paramString });
/* 291 */         MixinEnvironment.getEnvironment(phase).addConfiguration(paramString);
/*     */       } 
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
/*     */   final void addTokenProvider(String paramString) {
/* 306 */     if (paramString.contains("@")) {
/* 307 */       String[] arrayOfString = paramString.split("@", 2);
/* 308 */       MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(arrayOfString[1]);
/* 309 */       if (phase != null) {
/* 310 */         logger.debug("Registering token provider class: {}", new Object[] { arrayOfString[0] });
/* 311 */         MixinEnvironment.getEnvironment(phase).registerTokenProviderClass(arrayOfString[0]);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 316 */     MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\platform\MixinPlatformManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */