/*      */ package org.spongepowered.asm.mixin;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.apache.logging.log4j.core.Appender;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.Logger;
/*      */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*      */ import org.spongepowered.asm.launch.GlobalProperties;
/*      */ import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
/*      */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*      */ import org.spongepowered.asm.mixin.transformer.MixinTransformer;
/*      */ import org.spongepowered.asm.obfuscation.RemapperChain;
/*      */ import org.spongepowered.asm.service.ILegacyClassTransformer;
/*      */ import org.spongepowered.asm.service.IMixinService;
/*      */ import org.spongepowered.asm.service.ITransformer;
/*      */ import org.spongepowered.asm.service.MixinService;
/*      */ import org.spongepowered.asm.util.ITokenProvider;
/*      */ import org.spongepowered.asm.util.JavaVersion;
/*      */ import org.spongepowered.asm.util.PrettyPrinter;
/*      */ import org.spongepowered.asm.util.perf.Profiler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class MixinEnvironment
/*      */   implements ITokenProvider
/*      */ {
/*      */   public static final class Phase
/*      */   {
/*   75 */     static final Phase NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   81 */     public static final Phase PREINIT = new Phase(0, "PREINIT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   86 */     public static final Phase INIT = new Phase(1, "INIT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   91 */     public static final Phase DEFAULT = new Phase(2, "DEFAULT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   96 */     static final List<Phase> phases = (List<Phase>)ImmutableList.of(PREINIT, INIT, DEFAULT);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int ordinal;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private MixinEnvironment environment;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Phase(int param1Int, String param1String) {
/*  118 */       this.ordinal = param1Int;
/*  119 */       this.name = param1String;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  124 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Phase forName(String param1String) {
/*  135 */       for (Phase phase : phases) {
/*  136 */         if (phase.name.equals(param1String)) {
/*  137 */           return phase;
/*      */         }
/*      */       } 
/*  140 */       return null;
/*      */     }
/*      */     
/*      */     MixinEnvironment getEnvironment() {
/*  144 */       if (this.ordinal < 0) {
/*  145 */         throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
/*      */       }
/*      */       
/*  148 */       if (this.environment == null) {
/*  149 */         this.environment = new MixinEnvironment(this);
/*      */       }
/*      */       
/*  152 */       return this.environment;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Side
/*      */   {
/*  164 */     UNKNOWN
/*      */     {
/*      */       protected boolean detect() {
/*  167 */         return false;
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  174 */     CLIENT
/*      */     {
/*      */       protected boolean detect() {
/*  177 */         String str = MixinService.getService().getSideName();
/*  178 */         return "CLIENT".equals(str);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  185 */     SERVER
/*      */     {
/*      */       protected boolean detect() {
/*  188 */         String str = MixinService.getService().getSideName();
/*  189 */         return ("SERVER".equals(str) || "DEDICATEDSERVER".equals(str));
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract boolean detect();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Option
/*      */   {
/*  204 */     DEBUG_ALL("debug"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  211 */     DEBUG_EXPORT((String)DEBUG_ALL, "export"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  226 */     DEBUG_EXPORT_FILTER((String)DEBUG_EXPORT, "filter", false),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  231 */     DEBUG_EXPORT_DECOMPILE((String)DEBUG_EXPORT, Inherit.ALLOW_OVERRIDE, (Option)"decompile"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  239 */     DEBUG_EXPORT_DECOMPILE_THREADED((String)DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, (Option)"async"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  248 */     DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES((String)DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, (Option)"mergeGenericSignatures"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  255 */     DEBUG_VERIFY((String)DEBUG_ALL, "verify"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  261 */     DEBUG_VERBOSE((String)DEBUG_ALL, "verbose"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  267 */     DEBUG_INJECTORS((String)DEBUG_ALL, "countInjections"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  272 */     DEBUG_STRICT((String)DEBUG_ALL, Inherit.INDEPENDENT, (Option)"strict"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  279 */     DEBUG_UNIQUE((String)DEBUG_STRICT, "unique"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  284 */     DEBUG_TARGETS((String)DEBUG_STRICT, "targets"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  290 */     DEBUG_PROFILER((String)DEBUG_ALL, Inherit.ALLOW_OVERRIDE, (Option)"profiler"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  296 */     DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  301 */     CHECK_ALL("checks"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  307 */     CHECK_IMPLEMENTS((String)CHECK_ALL, "interfaces"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  315 */     CHECK_IMPLEMENTS_STRICT((String)CHECK_IMPLEMENTS, Inherit.ALLOW_OVERRIDE, (Option)"strict"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  320 */     IGNORE_CONSTRAINTS("ignoreConstraints"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  325 */     HOT_SWAP("hotSwap"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  330 */     ENVIRONMENT((String)Inherit.ALWAYS_FALSE, "env"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  335 */     OBFUSCATION_TYPE((String)ENVIRONMENT, Inherit.ALWAYS_FALSE, (Option)"obf"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  340 */     DISABLE_REFMAP((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"disableRefMap"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  350 */     REFMAP_REMAP((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"remapRefMap"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  360 */     REFMAP_REMAP_RESOURCE((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"refMapRemappingFile", (Inherit)""),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  367 */     REFMAP_REMAP_SOURCE_ENV((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"refMapRemappingEnv", (Inherit)"searge"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  376 */     REFMAP_REMAP_ALLOW_PERMISSIVE((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"allowPermissiveMatch", true, "true"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  381 */     IGNORE_REQUIRED((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"ignoreRequired"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     DEFAULT_COMPATIBILITY_LEVEL((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"compatLevel"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  406 */     SHIFT_BY_VIOLATION_BEHAVIOUR((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"shiftByViolation", (Inherit)"warn"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  412 */     INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default");
/*      */     private static final String PREFIX = "mixin";
/*      */     final Option parent;
/*      */     final Inherit inheritance;
/*      */     final String property;
/*      */     final String defaultValue;
/*      */     final boolean isFlag;
/*      */     final int depth;
/*      */     
/*      */     private enum Inherit {
/*  422 */       INHERIT,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  429 */       ALLOW_OVERRIDE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  435 */       INDEPENDENT,
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  440 */       ALWAYS_FALSE;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Option(Option param1Option, Inherit param1Inherit, String param1String1, boolean param1Boolean, String param1String2) {
/*  521 */       this.parent = param1Option;
/*  522 */       this.inheritance = param1Inherit;
/*  523 */       this.property = ((param1Option != null) ? param1Option.property : "mixin") + "." + param1String1;
/*  524 */       this.defaultValue = param1String2;
/*  525 */       this.isFlag = param1Boolean;
/*  526 */       byte b = 0;
/*  527 */       for (; param1Option != null; b++) {
/*  528 */         param1Option = param1Option.parent;
/*      */       }
/*  530 */       this.depth = b;
/*      */     }
/*      */     
/*      */     Option getParent() {
/*  534 */       return this.parent;
/*      */     }
/*      */     
/*      */     String getProperty() {
/*  538 */       return this.property;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  543 */       return this.isFlag ? String.valueOf(getBooleanValue()) : getStringValue();
/*      */     }
/*      */     
/*      */     private boolean getLocalBooleanValue(boolean param1Boolean) {
/*  547 */       return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(param1Boolean)));
/*      */     }
/*      */     
/*      */     private boolean getInheritedBooleanValue() {
/*  551 */       return (this.parent != null && this.parent.getBooleanValue());
/*      */     }
/*      */     
/*      */     final boolean getBooleanValue() {
/*  555 */       if (this.inheritance == Inherit.ALWAYS_FALSE) {
/*  556 */         return false;
/*      */       }
/*      */       
/*  559 */       boolean bool = getLocalBooleanValue(false);
/*  560 */       if (this.inheritance == Inherit.INDEPENDENT) {
/*  561 */         return bool;
/*      */       }
/*      */       
/*  564 */       boolean bool1 = (bool || getInheritedBooleanValue()) ? true : false;
/*  565 */       return (this.inheritance == Inherit.INHERIT) ? bool1 : getLocalBooleanValue(bool1);
/*      */     }
/*      */     
/*      */     final String getStringValue() {
/*  569 */       return (this.inheritance == Inherit.INDEPENDENT || this.parent == null || this.parent.getBooleanValue()) ? 
/*  570 */         System.getProperty(this.property, this.defaultValue) : this.defaultValue;
/*      */     }
/*      */ 
/*      */     
/*      */     <E extends Enum<E>> E getEnumValue(E param1E) {
/*  575 */       String str = System.getProperty(this.property, param1E.name());
/*      */       try {
/*  577 */         return Enum.valueOf((Class)param1E.getClass(), str.toUpperCase());
/*  578 */       } catch (IllegalArgumentException illegalArgumentException) {
/*  579 */         return param1E;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum CompatibilityLevel
/*      */   {
/*  592 */     JAVA_6(6, 50, false),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  597 */     JAVA_7(7, 51, false)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  601 */         return (JavaVersion.current() >= 1.7D);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  609 */     JAVA_8(8, 52, true)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  613 */         return (JavaVersion.current() >= 1.8D);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  621 */     JAVA_9(9, 53, true)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  625 */         return false;
/*      */       }
/*      */     };
/*      */ 
/*      */     
/*      */     private static final int CLASS_V1_9 = 53;
/*      */     
/*      */     private final int ver;
/*      */     
/*      */     private final int classVersion;
/*      */     
/*      */     private final boolean supportsMethodsInInterfaces;
/*      */     
/*      */     private CompatibilityLevel maxCompatibleLevel;
/*      */ 
/*      */     
/*      */     CompatibilityLevel(int param1Int1, int param1Int2, boolean param1Boolean) {
/*  642 */       this.ver = param1Int1;
/*  643 */       this.classVersion = param1Int2;
/*  644 */       this.supportsMethodsInInterfaces = param1Boolean;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setMaxCompatibleLevel(CompatibilityLevel param1CompatibilityLevel) {
/*  649 */       this.maxCompatibleLevel = param1CompatibilityLevel;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isSupported() {
/*  657 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int classVersion() {
/*  664 */       return this.classVersion;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean supportsMethodsInInterfaces() {
/*  672 */       return this.supportsMethodsInInterfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAtLeast(CompatibilityLevel param1CompatibilityLevel) {
/*  683 */       return (param1CompatibilityLevel == null || this.ver >= param1CompatibilityLevel.ver);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canElevateTo(CompatibilityLevel param1CompatibilityLevel) {
/*  693 */       if (param1CompatibilityLevel == null || this.maxCompatibleLevel == null) {
/*  694 */         return true;
/*      */       }
/*  696 */       return (param1CompatibilityLevel.ver <= this.maxCompatibleLevel.ver);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canSupport(CompatibilityLevel param1CompatibilityLevel) {
/*  706 */       if (param1CompatibilityLevel == null) {
/*  707 */         return true;
/*      */       }
/*      */       
/*  710 */       return param1CompatibilityLevel.canElevateTo(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TokenProviderWrapper
/*      */     implements Comparable<TokenProviderWrapper>
/*      */   {
/*  720 */     private static int nextOrder = 0;
/*      */     
/*      */     private final int priority;
/*      */     
/*      */     private final int order;
/*      */     private final IEnvironmentTokenProvider provider;
/*      */     private final MixinEnvironment environment;
/*      */     
/*      */     public TokenProviderWrapper(IEnvironmentTokenProvider param1IEnvironmentTokenProvider, MixinEnvironment param1MixinEnvironment) {
/*  729 */       this.provider = param1IEnvironmentTokenProvider;
/*  730 */       this.environment = param1MixinEnvironment;
/*  731 */       this.order = nextOrder++;
/*  732 */       this.priority = param1IEnvironmentTokenProvider.getPriority();
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(TokenProviderWrapper param1TokenProviderWrapper) {
/*  737 */       if (param1TokenProviderWrapper == null) {
/*  738 */         return 0;
/*      */       }
/*  740 */       if (param1TokenProviderWrapper.priority == this.priority) {
/*  741 */         return param1TokenProviderWrapper.order - this.order;
/*      */       }
/*  743 */       return param1TokenProviderWrapper.priority - this.priority;
/*      */     }
/*      */     
/*      */     public IEnvironmentTokenProvider getProvider() {
/*  747 */       return this.provider;
/*      */     }
/*      */     
/*      */     Integer getToken(String param1String) {
/*  751 */       return this.provider.getToken(param1String, this.environment);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MixinLogWatcher
/*      */   {
/*  761 */     static MixinAppender appender = new MixinAppender();
/*      */     static Logger log;
/*  763 */     static Level oldLevel = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static void begin() {
/*  779 */       Logger logger = LogManager.getLogger("FML");
/*  780 */       if (!(logger instanceof Logger)) {
/*      */         return;
/*      */       }
/*      */       
/*  784 */       log = (Logger)logger;
/*  785 */       oldLevel = log.getLevel();
/*      */       
/*  787 */       appender.start();
/*  788 */       log.addAppender((Appender)appender);
/*      */       
/*  790 */       log.setLevel(Level.ALL);
/*      */     }
/*      */     
/*      */     static void end() {
/*  794 */       if (log != null)
/*      */       {
/*  796 */         log.removeAppender((Appender)appender);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     static class MixinAppender
/*      */       extends AbstractAppender
/*      */     {
/*      */       MixinAppender() {
/*  806 */         super("MixinLogWatcherAppender", null, null);
/*      */       }
/*      */ 
/*      */       
/*      */       public void append(LogEvent param2LogEvent) {
/*  811 */         if (param2LogEvent.getLevel() != Level.DEBUG || !"Validating minecraft".equals(param2LogEvent.getMessage().getFormattedMessage())) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  816 */         MixinEnvironment.gotoPhase(MixinEnvironment.Phase.INIT);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  823 */         if (MixinEnvironment.MixinLogWatcher.log.getLevel() == Level.ALL) {
/*  824 */           MixinEnvironment.MixinLogWatcher.log.setLevel(MixinEnvironment.MixinLogWatcher.oldLevel);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  835 */   private static final Set<String> excludeTransformers = Sets.newHashSet((Object[])new String[] { "net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static MixinEnvironment currentEnvironment;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  851 */   private static Phase currentPhase = Phase.NOT_INITIALISED;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  856 */   private static CompatibilityLevel compatibility = Option.DEFAULT_COMPATIBILITY_LEVEL.<CompatibilityLevel>getEnumValue(CompatibilityLevel.JAVA_6);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean showHeader = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  866 */   private static final Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  871 */   private static final Profiler profiler = new Profiler();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final IMixinService service;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Phase phase;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String configsKey;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean[] options;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  896 */   private final Set<String> tokenProviderClasses = new HashSet<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  901 */   private final List<TokenProviderWrapper> tokenProviders = new ArrayList<TokenProviderWrapper>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  906 */   private final Map<String, Integer> internalTokens = new HashMap<String, Integer>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  911 */   private final RemapperChain remappers = new RemapperChain();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Side side;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ILegacyClassTransformer> transformers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  929 */   private String obfuscationContext = null;
/*      */   
/*      */   MixinEnvironment(Phase paramPhase) {
/*  932 */     this.service = MixinService.getService();
/*  933 */     this.phase = paramPhase;
/*  934 */     this.configsKey = "mixin.configs." + this.phase.name.toLowerCase();
/*      */ 
/*      */     
/*  937 */     String str = getVersion();
/*  938 */     if (str == null || !"0.7.11".equals(str)) {
/*  939 */       throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
/*      */     }
/*      */ 
/*      */     
/*  943 */     this.service.checkEnv(this);
/*      */     
/*  945 */     this.options = new boolean[(Option.values()).length];
/*  946 */     for (Option option : Option.values()) {
/*  947 */       this.options[option.ordinal()] = option.getBooleanValue();
/*      */     }
/*      */     
/*  950 */     if (showHeader) {
/*  951 */       showHeader = false;
/*  952 */       printHeader(str);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void printHeader(Object paramObject) {
/*  957 */     String str1 = getCodeSource();
/*  958 */     String str2 = this.service.getName();
/*  959 */     Side side = getSide();
/*  960 */     logger.info("SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", new Object[] { paramObject, str1, str2, side });
/*      */     
/*  962 */     boolean bool = getOption(Option.DEBUG_VERBOSE);
/*  963 */     if (bool || getOption(Option.DEBUG_EXPORT) || getOption(Option.DEBUG_PROFILER)) {
/*  964 */       PrettyPrinter prettyPrinter = new PrettyPrinter(32);
/*  965 */       prettyPrinter.add("SpongePowered MIXIN%s", new Object[] { bool ? " (Verbose debugging enabled)" : "" }).centre().hr();
/*  966 */       prettyPrinter.kv("Code source", str1);
/*  967 */       prettyPrinter.kv("Internal Version", paramObject);
/*  968 */       prettyPrinter.kv("Java 8 Supported", Boolean.valueOf(CompatibilityLevel.JAVA_8.isSupported())).hr();
/*  969 */       prettyPrinter.kv("Service Name", str2);
/*  970 */       prettyPrinter.kv("Service Class", this.service.getClass().getName()).hr();
/*  971 */       for (Option option : Option.values()) {
/*  972 */         StringBuilder stringBuilder = new StringBuilder();
/*  973 */         for (byte b = 0; b < option.depth; b++) {
/*  974 */           stringBuilder.append("- ");
/*      */         }
/*  976 */         prettyPrinter.kv(option.property, "%s<%s>", new Object[] { stringBuilder, option });
/*      */       } 
/*  978 */       prettyPrinter.hr().kv("Detected Side", side);
/*  979 */       prettyPrinter.print(System.err);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getCodeSource() {
/*      */     try {
/*  985 */       return getClass().getProtectionDomain().getCodeSource().getLocation().toString();
/*  986 */     } catch (Throwable throwable) {
/*  987 */       return "Unknown";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Phase getPhase() {
/*  997 */     return this.phase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> getMixinConfigs() {
/* 1008 */     List<String> list = (List)GlobalProperties.get(this.configsKey);
/* 1009 */     if (list == null) {
/* 1010 */       list = new ArrayList();
/* 1011 */       GlobalProperties.put(this.configsKey, list);
/*      */     } 
/* 1013 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public MixinEnvironment addConfiguration(String paramString) {
/* 1025 */     logger.warn("MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!");
/* 1026 */     Mixins.addConfiguration(paramString, this);
/* 1027 */     return this;
/*      */   }
/*      */   
/*      */   void registerConfig(String paramString) {
/* 1031 */     List<String> list = getMixinConfigs();
/* 1032 */     if (!list.contains(paramString)) {
/* 1033 */       list.add(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public MixinEnvironment registerErrorHandlerClass(String paramString) {
/* 1046 */     Mixins.registerErrorHandlerClass(paramString);
/* 1047 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment registerTokenProviderClass(String paramString) {
/* 1057 */     if (!this.tokenProviderClasses.contains(paramString)) {
/*      */       
/*      */       try {
/*      */         
/* 1061 */         Class<IEnvironmentTokenProvider> clazz = this.service.getClassProvider().findClass(paramString, true);
/* 1062 */         IEnvironmentTokenProvider iEnvironmentTokenProvider = clazz.newInstance();
/* 1063 */         registerTokenProvider(iEnvironmentTokenProvider);
/* 1064 */       } catch (Throwable throwable) {
/* 1065 */         logger.error("Error instantiating " + paramString, throwable);
/*      */       } 
/*      */     }
/* 1068 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment registerTokenProvider(IEnvironmentTokenProvider paramIEnvironmentTokenProvider) {
/* 1078 */     if (paramIEnvironmentTokenProvider != null && !this.tokenProviderClasses.contains(paramIEnvironmentTokenProvider.getClass().getName())) {
/* 1079 */       String str = paramIEnvironmentTokenProvider.getClass().getName();
/* 1080 */       TokenProviderWrapper tokenProviderWrapper = new TokenProviderWrapper(paramIEnvironmentTokenProvider, this);
/* 1081 */       logger.info("Adding new token provider {} to {}", new Object[] { str, this });
/* 1082 */       this.tokenProviders.add(tokenProviderWrapper);
/* 1083 */       this.tokenProviderClasses.add(str);
/* 1084 */       Collections.sort(this.tokenProviders);
/*      */     } 
/*      */     
/* 1087 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer getToken(String paramString) {
/* 1099 */     paramString = paramString.toUpperCase();
/*      */     
/* 1101 */     for (TokenProviderWrapper tokenProviderWrapper : this.tokenProviders) {
/* 1102 */       Integer integer = tokenProviderWrapper.getToken(paramString);
/* 1103 */       if (integer != null) {
/* 1104 */         return integer;
/*      */       }
/*      */     } 
/*      */     
/* 1108 */     return this.internalTokens.get(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Set<String> getErrorHandlerClasses() {
/* 1119 */     return Mixins.getErrorHandlerClasses();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getActiveTransformer() {
/* 1128 */     return GlobalProperties.get("mixin.transformer");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActiveTransformer(ITransformer paramITransformer) {
/* 1137 */     if (paramITransformer != null) {
/* 1138 */       GlobalProperties.put("mixin.transformer", paramITransformer);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment setSide(Side paramSide) {
/* 1149 */     if (paramSide != null && getSide() == Side.UNKNOWN && paramSide != Side.UNKNOWN) {
/* 1150 */       this.side = paramSide;
/*      */     }
/* 1152 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Side getSide() {
/* 1161 */     if (this.side == null) {
/* 1162 */       for (Side side : Side.values()) {
/* 1163 */         if (side.detect()) {
/* 1164 */           this.side = side;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1170 */     return (this.side != null) ? this.side : Side.UNKNOWN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getVersion() {
/* 1179 */     return (String)GlobalProperties.get("mixin.initialised");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOption(Option paramOption) {
/* 1189 */     return this.options[paramOption.ordinal()];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOption(Option paramOption, boolean paramBoolean) {
/* 1199 */     this.options[paramOption.ordinal()] = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOptionValue(Option paramOption) {
/* 1209 */     return paramOption.getStringValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getOption(Option paramOption, E paramE) {
/* 1221 */     return paramOption.getEnumValue(paramE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObfuscationContext(String paramString) {
/* 1230 */     this.obfuscationContext = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getObfuscationContext() {
/* 1237 */     return this.obfuscationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRefmapObfuscationContext() {
/* 1244 */     String str = Option.OBFUSCATION_TYPE.getStringValue();
/* 1245 */     if (str != null) {
/* 1246 */       return str;
/*      */     }
/* 1248 */     return this.obfuscationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RemapperChain getRemappers() {
/* 1255 */     return this.remappers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void audit() {
/* 1262 */     Object object = getActiveTransformer();
/* 1263 */     if (object instanceof MixinTransformer) {
/* 1264 */       MixinTransformer mixinTransformer = (MixinTransformer)object;
/* 1265 */       mixinTransformer.audit(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ILegacyClassTransformer> getTransformers() {
/* 1276 */     if (this.transformers == null) {
/* 1277 */       buildTransformerDelegationList();
/*      */     }
/*      */     
/* 1280 */     return Collections.unmodifiableList(this.transformers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTransformerExclusion(String paramString) {
/* 1289 */     excludeTransformers.add(paramString);
/*      */ 
/*      */     
/* 1292 */     this.transformers = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildTransformerDelegationList() {
/* 1302 */     logger.debug("Rebuilding transformer delegation list:");
/* 1303 */     this.transformers = new ArrayList<ILegacyClassTransformer>();
/* 1304 */     for (ITransformer iTransformer : this.service.getTransformers()) {
/* 1305 */       if (!(iTransformer instanceof ILegacyClassTransformer)) {
/*      */         continue;
/*      */       }
/*      */       
/* 1309 */       ILegacyClassTransformer iLegacyClassTransformer = (ILegacyClassTransformer)iTransformer;
/* 1310 */       String str = iLegacyClassTransformer.getName();
/* 1311 */       boolean bool = true;
/* 1312 */       for (String str1 : excludeTransformers) {
/* 1313 */         if (str.contains(str1)) {
/* 1314 */           bool = false;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1318 */       if (bool && !iLegacyClassTransformer.isDelegationExcluded()) {
/* 1319 */         logger.debug("  Adding:    {}", new Object[] { str });
/* 1320 */         this.transformers.add(iLegacyClassTransformer); continue;
/*      */       } 
/* 1322 */       logger.debug("  Excluding: {}", new Object[] { str });
/*      */     } 
/*      */ 
/*      */     
/* 1326 */     logger.debug("Transformer delegation list created with {} entries", new Object[] { Integer.valueOf(this.transformers.size()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1334 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.phase });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Phase getCurrentPhase() {
/* 1341 */     if (currentPhase == Phase.NOT_INITIALISED) {
/* 1342 */       init(Phase.PREINIT);
/*      */     }
/*      */     
/* 1345 */     return currentPhase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void init(Phase paramPhase) {
/* 1354 */     if (currentPhase == Phase.NOT_INITIALISED) {
/* 1355 */       currentPhase = paramPhase;
/* 1356 */       MixinEnvironment mixinEnvironment = getEnvironment(paramPhase);
/* 1357 */       getProfiler().setActive(mixinEnvironment.getOption(Option.DEBUG_PROFILER));
/*      */       
/* 1359 */       MixinLogWatcher.begin();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getEnvironment(Phase paramPhase) {
/* 1370 */     if (paramPhase == null) {
/* 1371 */       return Phase.DEFAULT.getEnvironment();
/*      */     }
/* 1373 */     return paramPhase.getEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getDefaultEnvironment() {
/* 1382 */     return getEnvironment(Phase.DEFAULT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getCurrentEnvironment() {
/* 1391 */     if (currentEnvironment == null) {
/* 1392 */       currentEnvironment = getEnvironment(getCurrentPhase());
/*      */     }
/*      */     
/* 1395 */     return currentEnvironment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CompatibilityLevel getCompatibilityLevel() {
/* 1402 */     return compatibility;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setCompatibilityLevel(CompatibilityLevel paramCompatibilityLevel) throws IllegalArgumentException {
/* 1414 */     StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
/* 1415 */     if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(arrayOfStackTraceElement[2].getClassName())) {
/* 1416 */       logger.warn("MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!");
/*      */     }
/*      */     
/* 1419 */     if (paramCompatibilityLevel != compatibility && paramCompatibilityLevel.isAtLeast(compatibility)) {
/* 1420 */       if (!paramCompatibilityLevel.isSupported()) {
/* 1421 */         throw new IllegalArgumentException("The requested compatibility level " + paramCompatibilityLevel + " could not be set. Level is not supported");
/*      */       }
/*      */       
/* 1424 */       compatibility = paramCompatibilityLevel;
/* 1425 */       logger.info("Compatibility level set to {}", new Object[] { paramCompatibilityLevel });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Profiler getProfiler() {
/* 1435 */     return profiler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void gotoPhase(Phase paramPhase) {
/* 1444 */     if (paramPhase == null || paramPhase.ordinal < 0) {
/* 1445 */       throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
/*      */     }
/*      */     
/* 1448 */     if (paramPhase.ordinal > (getCurrentPhase()).ordinal) {
/* 1449 */       MixinService.getService().beginPhase();
/*      */     }
/*      */     
/* 1452 */     if (paramPhase == Phase.DEFAULT) {
/* 1453 */       MixinLogWatcher.end();
/*      */     }
/*      */     
/* 1456 */     currentPhase = paramPhase;
/* 1457 */     currentEnvironment = getEnvironment(getCurrentPhase());
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\MixinEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */