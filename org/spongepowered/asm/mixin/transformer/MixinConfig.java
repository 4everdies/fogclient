/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.launch.MixinInitialisationError;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.refmap.RemappingReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.service.IMixinService;
/*     */ import org.spongepowered.asm.service.MixinService;
/*     */ import org.spongepowered.asm.util.VersionNumber;
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
/*     */ final class MixinConfig
/*     */   implements Comparable<MixinConfig>, IMixinConfig
/*     */ {
/*     */   static class InjectorOptions
/*     */   {
/*     */     @SerializedName("defaultRequire")
/*  76 */     int defaultRequireValue = 0;
/*     */     
/*     */     @SerializedName("defaultGroup")
/*  79 */     String defaultGroup = "default";
/*     */     
/*     */     @SerializedName("injectionPoints")
/*     */     List<String> injectionPoints;
/*     */     
/*     */     @SerializedName("maxShiftBy")
/*  85 */     int maxShiftBy = 0;
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
/*     */ 
/*     */   
/*     */   static class OverwriteOptions
/*     */   {
/*     */     @SerializedName("conformVisibility")
/*     */     boolean conformAccessModifiers;
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
/*     */     @SerializedName("requireAnnotations")
/*     */     boolean requireOverwriteAnnotations;
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
/*     */   
/* 128 */   private static int configOrder = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   private static final Set<String> globalMixinList = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private final transient Map<String, List<MixinInfo>> mixinMapping = new HashMap<String, List<MixinInfo>>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   private final transient Set<String> unhandledTargets = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   private final transient List<MixinInfo> mixins = new ArrayList<MixinInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Config handle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("target")
/*     */   private String selector;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("minVersion")
/*     */   private String version;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("compatibilityLevel")
/*     */   private String compatibility;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("required")
/*     */   private boolean required;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("priority")
/* 190 */   private int priority = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("mixinPriority")
/* 199 */   private int mixinPriority = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("package")
/*     */   private String mixinPackage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("mixins")
/*     */   private List<String> mixinClasses;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("client")
/*     */   private List<String> mixinClassesClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("server")
/*     */   private List<String> mixinClassesServer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("setSourceFile")
/*     */   private boolean setSourceFile = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("refmap")
/*     */   private String refMapperConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("verbose")
/*     */   private boolean verboseLogging;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   private final transient int order = configOrder++;
/*     */   
/* 252 */   private final transient List<IListener> listeners = new ArrayList<IListener>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient IMixinService service;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient MixinEnvironment env;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient String name;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("plugin")
/*     */   private String pluginClassName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("injectors")
/* 280 */   private InjectorOptions injectorOptions = new InjectorOptions();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("overwrites")
/* 286 */   private OverwriteOptions overwriteOptions = new OverwriteOptions();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient IMixinConfigPlugin plugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient IReferenceMapper refMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean prepared = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean visited = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean onLoad(IMixinService paramIMixinService, String paramString, MixinEnvironment paramMixinEnvironment) {
/* 325 */     this.service = paramIMixinService;
/* 326 */     this.name = paramString;
/* 327 */     this.env = parseSelector(this.selector, paramMixinEnvironment);
/* 328 */     this.required &= !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED) ? 1 : 0;
/* 329 */     initCompatibilityLevel();
/* 330 */     initInjectionPoints();
/* 331 */     return checkVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initCompatibilityLevel() {
/* 336 */     if (this.compatibility == null) {
/*     */       return;
/*     */     }
/*     */     
/* 340 */     MixinEnvironment.CompatibilityLevel compatibilityLevel1 = MixinEnvironment.CompatibilityLevel.valueOf(this.compatibility.trim().toUpperCase());
/* 341 */     MixinEnvironment.CompatibilityLevel compatibilityLevel2 = MixinEnvironment.getCompatibilityLevel();
/*     */     
/* 343 */     if (compatibilityLevel1 == compatibilityLevel2) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 348 */     if (compatibilityLevel2.isAtLeast(compatibilityLevel1) && 
/* 349 */       !compatibilityLevel2.canSupport(compatibilityLevel1)) {
/* 350 */       throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + compatibilityLevel1 + " which is too old");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 355 */     if (!compatibilityLevel2.canElevateTo(compatibilityLevel1)) {
/* 356 */       throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + compatibilityLevel1 + " which is prohibited by " + compatibilityLevel2);
/*     */     }
/*     */ 
/*     */     
/* 360 */     MixinEnvironment.setCompatibilityLevel(compatibilityLevel1);
/*     */   }
/*     */ 
/*     */   
/*     */   private MixinEnvironment parseSelector(String paramString, MixinEnvironment paramMixinEnvironment) {
/* 365 */     if (paramString != null) {
/* 366 */       String[] arrayOfString = paramString.split("[&\\| ]");
/* 367 */       for (String str : arrayOfString) {
/* 368 */         str = str.trim();
/* 369 */         Pattern pattern = Pattern.compile("^@env(?:ironment)?\\(([A-Z]+)\\)$");
/* 370 */         Matcher matcher = pattern.matcher(str);
/* 371 */         if (matcher.matches())
/*     */         {
/* 373 */           return MixinEnvironment.getEnvironment(MixinEnvironment.Phase.forName(matcher.group(1)));
/*     */         }
/*     */       } 
/*     */       
/* 377 */       MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(paramString);
/* 378 */       if (phase != null) {
/* 379 */         return MixinEnvironment.getEnvironment(phase);
/*     */       }
/*     */     } 
/* 382 */     return paramMixinEnvironment;
/*     */   }
/*     */ 
/*     */   
/*     */   private void initInjectionPoints() {
/* 387 */     if (this.injectorOptions.injectionPoints == null) {
/*     */       return;
/*     */     }
/*     */     
/* 391 */     for (String str : this.injectorOptions.injectionPoints) {
/*     */       try {
/* 393 */         Class<?> clazz = this.service.getClassProvider().findClass(str, true);
/* 394 */         if (InjectionPoint.class.isAssignableFrom(clazz)) {
/* 395 */           InjectionPoint.register(clazz); continue;
/*     */         } 
/* 397 */         this.logger.error("Unable to register injection point {} for {}, class must extend InjectionPoint", new Object[] { clazz, this });
/*     */       }
/* 399 */       catch (Throwable throwable) {
/* 400 */         this.logger.catching(throwable);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkVersion() throws MixinInitialisationError {
/* 406 */     if (this.version == null) {
/* 407 */       this.logger.error("Mixin config {} does not specify \"minVersion\" property", new Object[] { this.name });
/*     */     }
/*     */     
/* 410 */     VersionNumber versionNumber1 = VersionNumber.parse(this.version);
/* 411 */     VersionNumber versionNumber2 = VersionNumber.parse(this.env.getVersion());
/* 412 */     if (versionNumber1.compareTo(versionNumber2) > 0) {
/* 413 */       this.logger.warn("Mixin config {} requires mixin subsystem version {} but {} was found. The mixin config will not be applied.", new Object[] { this.name, versionNumber1, versionNumber2 });
/*     */ 
/*     */       
/* 416 */       if (this.required) {
/* 417 */         throw new MixinInitialisationError("Required mixin config " + this.name + " requires mixin subsystem version " + versionNumber1);
/*     */       }
/*     */       
/* 420 */       return false;
/*     */     } 
/*     */     
/* 423 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addListener(IListener paramIListener) {
/* 432 */     this.listeners.add(paramIListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void onSelect() {
/* 439 */     if (this.pluginClassName != null) {
/*     */       try {
/* 441 */         Class<IMixinConfigPlugin> clazz = this.service.getClassProvider().findClass(this.pluginClassName, true);
/* 442 */         this.plugin = clazz.newInstance();
/*     */         
/* 444 */         if (this.plugin != null) {
/* 445 */           this.plugin.onLoad(this.mixinPackage);
/*     */         }
/* 447 */       } catch (Throwable throwable) {
/* 448 */         throwable.printStackTrace();
/* 449 */         this.plugin = null;
/*     */       } 
/*     */     }
/*     */     
/* 453 */     if (!this.mixinPackage.endsWith(".")) {
/* 454 */       this.mixinPackage += ".";
/*     */     }
/*     */     
/* 457 */     boolean bool = false;
/*     */     
/* 459 */     if (this.refMapperConfig == null) {
/* 460 */       if (this.plugin != null) {
/* 461 */         this.refMapperConfig = this.plugin.getRefMapperConfig();
/*     */       }
/*     */       
/* 464 */       if (this.refMapperConfig == null) {
/* 465 */         bool = true;
/* 466 */         this.refMapperConfig = "mixin.refmap.json";
/*     */       } 
/*     */     } 
/*     */     
/* 470 */     this.refMapper = (IReferenceMapper)ReferenceMapper.read(this.refMapperConfig);
/* 471 */     this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
/*     */     
/* 473 */     if (!bool && this.refMapper.isDefault() && !this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
/* 474 */       this.logger.warn("Reference map '{}' for {} could not be read. If this is a development environment you can ignore this message", new Object[] { this.refMapperConfig, this });
/*     */     }
/*     */ 
/*     */     
/* 478 */     if (this.env.getOption(MixinEnvironment.Option.REFMAP_REMAP)) {
/* 479 */       this.refMapper = RemappingReferenceMapper.of(this.env, this.refMapper);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void prepare() {
/* 497 */     if (this.prepared) {
/*     */       return;
/*     */     }
/* 500 */     this.prepared = true;
/*     */     
/* 502 */     prepareMixins(this.mixinClasses, false);
/*     */     
/* 504 */     switch (this.env.getSide()) {
/*     */       case CLIENT:
/* 506 */         prepareMixins(this.mixinClassesClient, false);
/*     */         return;
/*     */       case SERVER:
/* 509 */         prepareMixins(this.mixinClassesServer, false);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 514 */     this.logger.warn("Mixin environment was unable to detect the current side, sided mixins will not be applied");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void postInitialise() {
/* 520 */     if (this.plugin != null) {
/* 521 */       List<String> list = this.plugin.getMixins();
/* 522 */       prepareMixins(list, true);
/*     */     } 
/*     */     
/* 525 */     for (Iterator<MixinInfo> iterator = this.mixins.iterator(); iterator.hasNext(); ) {
/* 526 */       MixinInfo mixinInfo = iterator.next();
/*     */       try {
/* 528 */         mixinInfo.validate();
/* 529 */         for (IListener iListener : this.listeners) {
/* 530 */           iListener.onInit(mixinInfo);
/*     */         }
/* 532 */       } catch (InvalidMixinException invalidMixinException) {
/* 533 */         this.logger.error(invalidMixinException.getMixin() + ": " + invalidMixinException.getMessage(), (Throwable)invalidMixinException);
/* 534 */         removeMixin(mixinInfo);
/* 535 */         iterator.remove();
/* 536 */       } catch (Exception exception) {
/* 537 */         this.logger.error(exception.getMessage(), exception);
/* 538 */         removeMixin(mixinInfo);
/* 539 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   } static interface IListener {
/*     */     void onPrepare(MixinInfo param1MixinInfo); void onInit(MixinInfo param1MixinInfo); }
/*     */   private void removeMixin(MixinInfo paramMixinInfo) {
/* 545 */     for (List<MixinInfo> list : this.mixinMapping.values()) {
/* 546 */       for (Iterator<MixinInfo> iterator = list.iterator(); iterator.hasNext();) {
/* 547 */         if (paramMixinInfo == iterator.next()) {
/* 548 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepareMixins(List<String> paramList, boolean paramBoolean) {
/* 555 */     if (paramList == null) {
/*     */       return;
/*     */     }
/*     */     
/* 559 */     for (String str1 : paramList) {
/* 560 */       String str2 = this.mixinPackage + str1;
/*     */       
/* 562 */       if (str1 == null || globalMixinList.contains(str2)) {
/*     */         continue;
/*     */       }
/*     */       
/* 566 */       MixinInfo mixinInfo = null;
/*     */       
/*     */       try {
/* 569 */         mixinInfo = new MixinInfo(this.service, this, str1, true, this.plugin, paramBoolean);
/* 570 */         if (mixinInfo.getTargetClasses().size() > 0) {
/* 571 */           globalMixinList.add(str2);
/* 572 */           for (String str3 : mixinInfo.getTargetClasses()) {
/* 573 */             String str4 = str3.replace('/', '.');
/* 574 */             mixinsFor(str4).add(mixinInfo);
/* 575 */             this.unhandledTargets.add(str4);
/*     */           } 
/* 577 */           for (IListener iListener : this.listeners) {
/* 578 */             iListener.onPrepare(mixinInfo);
/*     */           }
/* 580 */           this.mixins.add(mixinInfo);
/*     */         } 
/* 582 */       } catch (InvalidMixinException invalidMixinException) {
/* 583 */         if (this.required) {
/* 584 */           throw invalidMixinException;
/*     */         }
/* 586 */         this.logger.error(invalidMixinException.getMessage(), (Throwable)invalidMixinException);
/* 587 */       } catch (Exception exception) {
/* 588 */         if (this.required) {
/* 589 */           throw new InvalidMixinException(mixinInfo, "Error initialising mixin " + mixinInfo + " - " + exception.getClass() + ": " + exception.getMessage(), exception);
/*     */         }
/* 591 */         this.logger.error(exception.getMessage(), exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void postApply(String paramString, ClassNode paramClassNode) {
/* 597 */     this.unhandledTargets.remove(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Config getHandle() {
/* 604 */     if (this.handle == null) {
/* 605 */       this.handle = new Config(this);
/*     */     }
/* 607 */     return this.handle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 615 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MixinEnvironment getEnvironment() {
/* 624 */     return this.env;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 632 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMixinPackage() {
/* 640 */     return this.mixinPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPriority() {
/* 648 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMixinPriority() {
/* 656 */     return this.mixinPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultRequiredInjections() {
/* 666 */     return this.injectorOptions.defaultRequireValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultInjectorGroup() {
/* 675 */     String str = this.injectorOptions.defaultGroup;
/* 676 */     return (str != null && !str.isEmpty()) ? str : "default";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean conformOverwriteVisibility() {
/* 686 */     return this.overwriteOptions.conformAccessModifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requireOverwriteAnnotations() {
/* 696 */     return this.overwriteOptions.requireOverwriteAnnotations;
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
/*     */   public int getMaxShiftByValue() {
/* 708 */     return Math.min(Math.max(this.injectorOptions.maxShiftBy, 0), 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean select(MixinEnvironment paramMixinEnvironment) {
/* 713 */     this.visited = true;
/* 714 */     return (this.env == paramMixinEnvironment);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isVisited() {
/* 719 */     return this.visited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getDeclaredMixinCount() {
/* 728 */     return getCollectionSize((Collection<?>[])new Collection[] { this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getMixinCount() {
/* 737 */     return this.mixins.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getClasses() {
/* 744 */     return Collections.unmodifiableList(this.mixinClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldSetSourceFile() {
/* 752 */     return this.setSourceFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IReferenceMapper getReferenceMapper() {
/* 759 */     if (this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
/* 760 */       return (IReferenceMapper)ReferenceMapper.DEFAULT_MAPPER;
/*     */     }
/* 762 */     this.refMapper.setContext(this.env.getRefmapObfuscationContext());
/* 763 */     return this.refMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String remapClassName(String paramString1, String paramString2) {
/* 771 */     return getReferenceMapper().remap(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMixinConfigPlugin getPlugin() {
/* 779 */     return this.plugin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getTargets() {
/* 787 */     return Collections.unmodifiableSet(this.mixinMapping.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getUnhandledTargets() {
/* 794 */     return Collections.unmodifiableSet(this.unhandledTargets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLoggingLevel() {
/* 801 */     return this.verboseLogging ? Level.INFO : Level.DEBUG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean packageMatch(String paramString) {
/* 812 */     return paramString.startsWith(this.mixinPackage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMixinsFor(String paramString) {
/* 823 */     return this.mixinMapping.containsKey(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MixinInfo> getMixinsFor(String paramString) {
/* 833 */     return mixinsFor(paramString);
/*     */   }
/*     */   
/*     */   private List<MixinInfo> mixinsFor(String paramString) {
/* 837 */     List<MixinInfo> list = this.mixinMapping.get(paramString);
/* 838 */     if (list == null) {
/* 839 */       list = new ArrayList();
/* 840 */       this.mixinMapping.put(paramString, list);
/*     */     } 
/* 842 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> reloadMixin(String paramString, byte[] paramArrayOfbyte) {
/* 853 */     for (MixinInfo mixinInfo : this.mixins) {
/*     */       
/* 855 */       if (mixinInfo.getClassName().equals(paramString)) {
/* 856 */         mixinInfo.reloadMixin(paramArrayOfbyte);
/* 857 */         return mixinInfo.getTargetClasses();
/*     */       } 
/*     */     } 
/* 860 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 865 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MixinConfig paramMixinConfig) {
/* 873 */     if (paramMixinConfig == null) {
/* 874 */       return 0;
/*     */     }
/* 876 */     if (paramMixinConfig.priority == this.priority) {
/* 877 */       return this.order - paramMixinConfig.order;
/*     */     }
/* 879 */     return this.priority - paramMixinConfig.priority;
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
/*     */   static Config create(String paramString, MixinEnvironment paramMixinEnvironment) {
/*     */     try {
/* 892 */       IMixinService iMixinService = MixinService.getService();
/* 893 */       MixinConfig mixinConfig = (MixinConfig)(new Gson()).fromJson(new InputStreamReader(iMixinService.getResourceAsStream(paramString)), MixinConfig.class);
/* 894 */       if (mixinConfig.onLoad(iMixinService, paramString, paramMixinEnvironment)) {
/* 895 */         return mixinConfig.getHandle();
/*     */       }
/* 897 */       return null;
/* 898 */     } catch (Exception exception) {
/* 899 */       exception.printStackTrace();
/* 900 */       throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", new Object[] { paramString }), exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getCollectionSize(Collection<?>... paramVarArgs) {
/* 905 */     int i = 0;
/* 906 */     for (Collection<?> collection : paramVarArgs) {
/* 907 */       if (collection != null) {
/* 908 */         i += collection.size();
/*     */       }
/*     */     } 
/* 911 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */