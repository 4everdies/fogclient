/*     */ package org.spongepowered.asm.launch.platform;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launchwrapper.ITweaker;
/*     */ import net.minecraft.launchwrapper.Launch;
/*     */ import net.minecraft.launchwrapper.LaunchClassLoader;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.spongepowered.asm.launch.GlobalProperties;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.extensibility.IRemapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MixinPlatformAgentFML
/*     */   extends MixinPlatformAgentAbstract
/*     */ {
/*     */   private static final String LOAD_CORE_MOD_METHOD = "loadCoreMod";
/*     */   private static final String GET_REPARSEABLE_COREMODS_METHOD = "getReparseableCoremods";
/*     */   private static final String CORE_MOD_MANAGER_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
/*     */   private static final String CORE_MOD_MANAGER_CLASS_LEGACY = "cpw.mods.fml.relauncher.CoreModManager";
/*     */   private static final String GET_IGNORED_MODS_METHOD = "getIgnoredMods";
/*     */   private static final String GET_IGNORED_MODS_METHOD_LEGACY = "getLoadedCoremods";
/*     */   private static final String FML_REMAPPER_ADAPTER_CLASS = "org.spongepowered.asm.bridge.RemapperAdapterFML";
/*     */   private static final String FML_CMDLINE_COREMODS = "fml.coreMods.load";
/*     */   private static final String FML_PLUGIN_WRAPPER_CLASS = "FMLPluginWrapper";
/*     */   private static final String FML_CORE_MOD_INSTANCE_FIELD = "coreModInstance";
/*     */   private static final String MFATT_FORCELOADASMOD = "ForceLoadAsMod";
/*     */   private static final String MFATT_FMLCOREPLUGIN = "FMLCorePlugin";
/*     */   private static final String MFATT_COREMODCONTAINSMOD = "FMLCorePluginContainsFMLMod";
/*     */   private static final String FML_TWEAKER_DEOBF = "FMLDeobfTweaker";
/*     */   private static final String FML_TWEAKER_INJECTION = "FMLInjectionAndSortingTweaker";
/*     */   private static final String FML_TWEAKER_TERMINAL = "TerminalTweaker";
/*  87 */   private static final Set<String> loadedCoreMods = new HashSet<String>();
/*     */   private final ITweaker coreModWrapper;
/*     */   private final String fileName;
/*     */   private Class<?> clCoreModManager;
/*     */   private boolean initInjectionState;
/*     */   
/*     */   static {
/*  94 */     for (String str : System.getProperty("fml.coreMods.load", "").split(",")) {
/*  95 */       if (!str.isEmpty()) {
/*  96 */         MixinPlatformAgentAbstract.logger.debug("FML platform agent will ignore coremod {} specified on the command line", new Object[] { str });
/*  97 */         loadedCoreMods.add(str);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MixinPlatformAgentFML(MixinPlatformManager paramMixinPlatformManager, URI paramURI) {
/* 132 */     super(paramMixinPlatformManager, paramURI);
/* 133 */     this.fileName = this.container.getName();
/* 134 */     this.coreModWrapper = initFMLCoreMod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ITweaker initFMLCoreMod() {
/*     */     try {
/*     */       try {
/* 143 */         this.clCoreModManager = getCoreModManagerClass();
/* 144 */       } catch (ClassNotFoundException classNotFoundException) {
/* 145 */         MixinPlatformAgentAbstract.logger.info("FML platform manager could not load class {}. Proceeding without FML support.", new Object[] { classNotFoundException
/* 146 */               .getMessage() });
/* 147 */         return null;
/*     */       } 
/*     */       
/* 150 */       if ("true".equalsIgnoreCase(this.attributes.get("ForceLoadAsMod"))) {
/* 151 */         MixinPlatformAgentAbstract.logger.debug("ForceLoadAsMod was specified for {}, attempting force-load", new Object[] { this.fileName });
/* 152 */         loadAsMod();
/*     */       } 
/*     */       
/* 155 */       return injectCorePlugin();
/* 156 */     } catch (Exception exception) {
/* 157 */       MixinPlatformAgentAbstract.logger.catching(exception);
/* 158 */       return null;
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
/*     */   private void loadAsMod() {
/*     */     try {
/* 175 */       getIgnoredMods(this.clCoreModManager).remove(this.fileName);
/* 176 */     } catch (Exception exception) {
/* 177 */       MixinPlatformAgentAbstract.logger.catching(exception);
/*     */     } 
/*     */     
/* 180 */     if (this.attributes.get("FMLCorePluginContainsFMLMod") != null) {
/* 181 */       if (isIgnoredReparseable()) {
/* 182 */         MixinPlatformAgentAbstract.logger.debug("Ignoring request to add {} to reparseable coremod collection - it is a deobfuscated dependency", new Object[] { this.fileName });
/*     */         
/*     */         return;
/*     */       } 
/* 186 */       addReparseableJar();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isIgnoredReparseable() {
/* 191 */     return this.container.toString().contains("deobfedDeps");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addReparseableJar() {
/*     */     try {
/* 200 */       Method method = this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.reparseablecoremodsmethod", "getReparseableCoremods"), new Class[0]);
/*     */ 
/*     */       
/* 203 */       List<String> list = (List)method.invoke(null, new Object[0]);
/* 204 */       if (!list.contains(this.fileName)) {
/* 205 */         MixinPlatformAgentAbstract.logger.debug("Adding {} to reparseable coremod collection", new Object[] { this.fileName });
/* 206 */         list.add(this.fileName);
/*     */       } 
/* 208 */     } catch (Exception exception) {
/* 209 */       MixinPlatformAgentAbstract.logger.catching(exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ITweaker injectCorePlugin() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 214 */     String str = this.attributes.get("FMLCorePlugin");
/* 215 */     if (str == null) {
/* 216 */       return null;
/*     */     }
/*     */     
/* 219 */     if (isAlreadyInjected(str)) {
/* 220 */       MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Skipping because it was already injected.", new Object[] { this.fileName, str });
/* 221 */       return null;
/*     */     } 
/*     */     
/* 224 */     MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Injecting it into FML for co-initialisation:", new Object[] { this.fileName, str });
/* 225 */     Method method = this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.loadcoremodmethod", "loadCoreMod"), new Class[] { LaunchClassLoader.class, String.class, File.class });
/*     */     
/* 227 */     method.setAccessible(true);
/* 228 */     ITweaker iTweaker = (ITweaker)method.invoke(null, new Object[] { Launch.classLoader, str, this.container });
/* 229 */     if (iTweaker == null) {
/* 230 */       MixinPlatformAgentAbstract.logger.debug("Core plugin {} could not be loaded.", new Object[] { str });
/* 231 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 236 */     this.initInjectionState = isTweakerQueued("FMLInjectionAndSortingTweaker");
/*     */     
/* 238 */     loadedCoreMods.add(str);
/* 239 */     return iTweaker;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAlreadyInjected(String paramString) {
/* 244 */     if (loadedCoreMods.contains(paramString)) {
/* 245 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 250 */       List list = (List)GlobalProperties.get("Tweaks");
/* 251 */       if (list == null) {
/* 252 */         return false;
/*     */       }
/*     */       
/* 255 */       for (ITweaker iTweaker : list) {
/* 256 */         Class<?> clazz = iTweaker.getClass();
/* 257 */         if ("FMLPluginWrapper".equals(clazz.getSimpleName())) {
/* 258 */           Field field = clazz.getField("coreModInstance");
/* 259 */           field.setAccessible(true);
/* 260 */           Object object = field.get(iTweaker);
/* 261 */           if (paramString.equals(object.getClass().getName())) {
/* 262 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 266 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPhaseProvider() {
/* 275 */     return MixinPlatformAgentFML.class.getName() + "$PhaseProvider";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 283 */     this.initInjectionState |= isTweakerQueued("FMLInjectionAndSortingTweaker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initPrimaryContainer() {
/* 292 */     if (this.clCoreModManager != null)
/*     */     {
/* 294 */       injectRemapper();
/*     */     }
/*     */   }
/*     */   
/*     */   private void injectRemapper() {
/*     */     try {
/* 300 */       MixinPlatformAgentAbstract.logger.debug("Creating FML remapper adapter: {}", new Object[] { "org.spongepowered.asm.bridge.RemapperAdapterFML" });
/* 301 */       Class<?> clazz = Class.forName("org.spongepowered.asm.bridge.RemapperAdapterFML", true, (ClassLoader)Launch.classLoader);
/* 302 */       Method method = clazz.getDeclaredMethod("create", new Class[0]);
/* 303 */       IRemapper iRemapper = (IRemapper)method.invoke(null, new Object[0]);
/* 304 */       MixinEnvironment.getDefaultEnvironment().getRemappers().add(iRemapper);
/* 305 */     } catch (Exception exception) {
/* 306 */       MixinPlatformAgentAbstract.logger.debug("Failed instancing FML remapper adapter, things will probably go horribly for notch-obf'd mods!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inject() {
/* 315 */     if (this.coreModWrapper != null && checkForCoInitialisation()) {
/* 316 */       MixinPlatformAgentAbstract.logger.debug("FML agent is co-initiralising coremod instance {} for {}", new Object[] { this.coreModWrapper, this.uri });
/* 317 */       this.coreModWrapper.injectIntoClassLoader(Launch.classLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLaunchTarget() {
/* 326 */     return null;
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
/*     */   protected final boolean checkForCoInitialisation() {
/* 342 */     boolean bool1 = isTweakerQueued("FMLInjectionAndSortingTweaker");
/* 343 */     boolean bool2 = isTweakerQueued("TerminalTweaker");
/* 344 */     if ((this.initInjectionState && bool2) || bool1) {
/* 345 */       MixinPlatformAgentAbstract.logger.debug("FML agent is skipping co-init for {} because FML will inject it normally", new Object[] { this.coreModWrapper });
/* 346 */       return false;
/*     */     } 
/*     */     
/* 349 */     return !isTweakerQueued("FMLDeobfTweaker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isTweakerQueued(String paramString) {
/* 360 */     for (String str : GlobalProperties.get("TweakClasses")) {
/* 361 */       if (str.endsWith(paramString)) {
/* 362 */         return true;
/*     */       }
/*     */     } 
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> getCoreModManagerClass() throws ClassNotFoundException {
/*     */     try {
/* 374 */       return Class.forName(GlobalProperties.getString("mixin.launch.fml.coremodmanagerclass", "net.minecraftforge.fml.relauncher.CoreModManager"));
/*     */     }
/* 376 */     catch (ClassNotFoundException classNotFoundException) {
/* 377 */       return Class.forName("cpw.mods.fml.relauncher.CoreModManager");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<String> getIgnoredMods(Class<?> paramClass) throws IllegalAccessException, InvocationTargetException {
/* 383 */     Method method = null;
/*     */     
/*     */     try {
/* 386 */       method = paramClass.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.ignoredmodsmethod", "getIgnoredMods"), new Class[0]);
/*     */     }
/* 388 */     catch (NoSuchMethodException noSuchMethodException) {
/*     */       
/*     */       try {
/* 391 */         method = paramClass.getDeclaredMethod("getLoadedCoremods", new Class[0]);
/* 392 */       } catch (NoSuchMethodException noSuchMethodException1) {
/* 393 */         MixinPlatformAgentAbstract.logger.catching(Level.DEBUG, noSuchMethodException1);
/* 394 */         return Collections.emptyList();
/*     */       } 
/*     */     } 
/*     */     
/* 398 */     return (List<String>)method.invoke(null, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\platform\MixinPlatformAgentFML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */