/*     */ package org.spongepowered.asm.service.mojang;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.launchwrapper.IClassNameTransformer;
/*     */ import net.minecraft.launchwrapper.IClassTransformer;
/*     */ import net.minecraft.launchwrapper.ITweaker;
/*     */ import net.minecraft.launchwrapper.Launch;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.launch.GlobalProperties;
/*     */ import org.spongepowered.asm.lib.ClassReader;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*     */ import org.spongepowered.asm.service.IClassBytecodeProvider;
/*     */ import org.spongepowered.asm.service.IClassProvider;
/*     */ import org.spongepowered.asm.service.ILegacyClassTransformer;
/*     */ import org.spongepowered.asm.service.IMixinService;
/*     */ import org.spongepowered.asm.service.ITransformer;
/*     */ import org.spongepowered.asm.util.ReEntranceLock;
/*     */ import org.spongepowered.asm.util.perf.Profiler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MixinServiceLaunchWrapper
/*     */   implements IClassBytecodeProvider, IClassProvider, IMixinService
/*     */ {
/*     */   public static final String BLACKBOARD_KEY_TWEAKCLASSES = "TweakClasses";
/*     */   public static final String BLACKBOARD_KEY_TWEAKS = "Tweaks";
/*     */   private static final String LAUNCH_PACKAGE = "org.spongepowered.asm.launch.";
/*     */   private static final String MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
/*     */   private static final String STATE_TWEAKER = "org.spongepowered.asm.mixin.EnvironmentStateTweaker";
/*     */   private static final String TRANSFORMER_PROXY_CLASS = "org.spongepowered.asm.mixin.transformer.Proxy";
/*  79 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private final LaunchClassLoaderUtil classLoaderUtil = new LaunchClassLoaderUtil(Launch.classLoader);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private final ReEntranceLock lock = new ReEntranceLock(1);
/*     */ 
/*     */ 
/*     */   
/*     */   private IClassNameTransformer nameTransformer;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  99 */     return "LaunchWrapper";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/*     */     try {
/* 109 */       Launch.classLoader.hashCode();
/* 110 */     } catch (Throwable throwable) {
/* 111 */       return false;
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 122 */     Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.launch.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MixinEnvironment.Phase getInitialPhase() {
/* 130 */     if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") > 132) {
/* 131 */       return MixinEnvironment.Phase.DEFAULT;
/*     */     }
/* 133 */     return MixinEnvironment.Phase.PREINIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 141 */     if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") < 4) {
/* 142 */       logger.error("MixinBootstrap.doInit() called during a tweak constructor!");
/*     */     }
/*     */     
/* 145 */     List<String> list = (List)GlobalProperties.get("TweakClasses");
/* 146 */     if (list != null) {
/* 147 */       list.add("org.spongepowered.asm.mixin.EnvironmentStateTweaker");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReEntranceLock getReEntranceLock() {
/* 156 */     return this.lock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getPlatformAgents() {
/* 164 */     return (Collection<String>)ImmutableList.of("org.spongepowered.asm.launch.platform.MixinPlatformAgentFML");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IClassProvider getClassProvider() {
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IClassBytecodeProvider getBytecodeProvider() {
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> findClass(String paramString) throws ClassNotFoundException {
/* 191 */     return Launch.classLoader.findClass(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> findClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 200 */     return Class.forName(paramString, paramBoolean, (ClassLoader)Launch.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> findAgentClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 209 */     return Class.forName(paramString, paramBoolean, Launch.class.getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginPhase() {
/* 217 */     Launch.classLoader.registerTransformer("org.spongepowered.asm.mixin.transformer.Proxy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkEnv(Object paramObject) {
/* 226 */     if (paramObject.getClass().getClassLoader() != Launch.class.getClassLoader()) {
/* 227 */       throw new MixinException("Attempted to init the mixin environment in the wrong classloader");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getResourceAsStream(String paramString) {
/* 237 */     return Launch.classLoader.getResourceAsStream(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerInvalidClass(String paramString) {
/* 246 */     this.classLoaderUtil.registerInvalidClass(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassLoaded(String paramString) {
/* 255 */     return this.classLoaderUtil.isClassLoaded(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassRestrictions(String paramString) {
/* 264 */     String str = "";
/* 265 */     if (this.classLoaderUtil.isClassClassLoaderExcluded(paramString, null)) {
/* 266 */       str = "PACKAGE_CLASSLOADER_EXCLUSION";
/*     */     }
/* 268 */     if (this.classLoaderUtil.isClassTransformerExcluded(paramString, null)) {
/* 269 */       str = ((str.length() > 0) ? (str + ",") : "") + "PACKAGE_TRANSFORMER_EXCLUSION";
/*     */     }
/* 271 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL[] getClassPath() {
/* 279 */     return (URL[])Launch.classLoader.getSources().toArray((Object[])new URL[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ITransformer> getTransformers() {
/* 287 */     List list = Launch.classLoader.getTransformers();
/* 288 */     ArrayList<ITransformer> arrayList = new ArrayList(list.size());
/* 289 */     for (IClassTransformer iClassTransformer : list) {
/* 290 */       if (iClassTransformer instanceof ITransformer) {
/* 291 */         arrayList.add((ITransformer)iClassTransformer);
/*     */       } else {
/* 293 */         arrayList.add(new LegacyTransformerHandle(iClassTransformer));
/*     */       } 
/*     */       
/* 296 */       if (iClassTransformer instanceof IClassNameTransformer) {
/* 297 */         logger.debug("Found name transformer: {}", new Object[] { iClassTransformer.getClass().getName() });
/* 298 */         this.nameTransformer = (IClassNameTransformer)iClassTransformer;
/*     */       } 
/*     */     } 
/*     */     
/* 302 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getClassBytes(String paramString1, String paramString2) throws IOException {
/* 311 */     byte[] arrayOfByte = Launch.classLoader.getClassBytes(paramString1);
/* 312 */     if (arrayOfByte != null) {
/* 313 */       return arrayOfByte;
/*     */     }
/*     */     
/* 316 */     URLClassLoader uRLClassLoader = (URLClassLoader)Launch.class.getClassLoader();
/*     */     
/* 318 */     InputStream inputStream = null;
/*     */     try {
/* 320 */       String str = paramString2.replace('.', '/').concat(".class");
/* 321 */       inputStream = uRLClassLoader.getResourceAsStream(str);
/* 322 */       return IOUtils.toByteArray(inputStream);
/* 323 */     } catch (Exception exception) {
/* 324 */       return null;
/*     */     } finally {
/* 326 */       IOUtils.closeQuietly(inputStream);
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
/*     */   public byte[] getClassBytes(String paramString, boolean paramBoolean) throws ClassNotFoundException, IOException {
/* 342 */     String str1 = paramString.replace('/', '.');
/* 343 */     String str2 = unmapClassName(str1);
/*     */     
/* 345 */     Profiler profiler = MixinEnvironment.getProfiler();
/* 346 */     Profiler.Section section = profiler.begin(1, "class.load");
/* 347 */     byte[] arrayOfByte = getClassBytes(str2, str1);
/* 348 */     section.end();
/*     */     
/* 350 */     if (paramBoolean) {
/* 351 */       Profiler.Section section1 = profiler.begin(1, "class.transform");
/* 352 */       arrayOfByte = applyTransformers(str2, str1, arrayOfByte, profiler);
/* 353 */       section1.end();
/*     */     } 
/*     */     
/* 356 */     if (arrayOfByte == null) {
/* 357 */       throw new ClassNotFoundException(String.format("The specified class '%s' was not found", new Object[] { str1 }));
/*     */     }
/*     */     
/* 360 */     return arrayOfByte;
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
/*     */   private byte[] applyTransformers(String paramString1, String paramString2, byte[] paramArrayOfbyte, Profiler paramProfiler) {
/* 374 */     if (this.classLoaderUtil.isClassExcluded(paramString1, paramString2)) {
/* 375 */       return paramArrayOfbyte;
/*     */     }
/*     */     
/* 378 */     MixinEnvironment mixinEnvironment = MixinEnvironment.getCurrentEnvironment();
/*     */     
/* 380 */     for (ILegacyClassTransformer iLegacyClassTransformer : mixinEnvironment.getTransformers()) {
/*     */       
/* 382 */       this.lock.clear();
/*     */       
/* 384 */       int i = iLegacyClassTransformer.getName().lastIndexOf('.');
/* 385 */       String str = iLegacyClassTransformer.getName().substring(i + 1);
/* 386 */       Profiler.Section section = paramProfiler.begin(2, str.toLowerCase());
/* 387 */       section.setInfo(iLegacyClassTransformer.getName());
/* 388 */       paramArrayOfbyte = iLegacyClassTransformer.transformClassBytes(paramString1, paramString2, paramArrayOfbyte);
/* 389 */       section.end();
/*     */       
/* 391 */       if (this.lock.isSet()) {
/*     */         
/* 393 */         mixinEnvironment.addTransformerExclusion(iLegacyClassTransformer.getName());
/*     */         
/* 395 */         this.lock.clear();
/* 396 */         logger.info("A re-entrant transformer '{}' was detected and will no longer process meta class data", new Object[] { iLegacyClassTransformer
/* 397 */               .getName() });
/*     */       } 
/*     */     } 
/*     */     
/* 401 */     return paramArrayOfbyte;
/*     */   }
/*     */   
/*     */   private String unmapClassName(String paramString) {
/* 405 */     if (this.nameTransformer == null) {
/* 406 */       findNameTransformer();
/*     */     }
/*     */     
/* 409 */     if (this.nameTransformer != null) {
/* 410 */       return this.nameTransformer.unmapClassName(paramString);
/*     */     }
/*     */     
/* 413 */     return paramString;
/*     */   }
/*     */   
/*     */   private void findNameTransformer() {
/* 417 */     List list = Launch.classLoader.getTransformers();
/* 418 */     for (IClassTransformer iClassTransformer : list) {
/* 419 */       if (iClassTransformer instanceof IClassNameTransformer) {
/* 420 */         logger.debug("Found name transformer: {}", new Object[] { iClassTransformer.getClass().getName() });
/* 421 */         this.nameTransformer = (IClassNameTransformer)iClassTransformer;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassNode getClassNode(String paramString) throws ClassNotFoundException, IOException {
/* 432 */     return getClassNode(getClassBytes(paramString, true), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassNode getClassNode(byte[] paramArrayOfbyte, int paramInt) {
/* 443 */     ClassNode classNode = new ClassNode();
/* 444 */     ClassReader classReader = new ClassReader(paramArrayOfbyte);
/* 445 */     classReader.accept((ClassVisitor)classNode, paramInt);
/* 446 */     return classNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getSideName() {
/* 456 */     for (ITweaker iTweaker : GlobalProperties.get("Tweaks")) {
/* 457 */       if (iTweaker.getClass().getName().endsWith(".common.launcher.FMLServerTweaker"))
/* 458 */         return "SERVER"; 
/* 459 */       if (iTweaker.getClass().getName().endsWith(".common.launcher.FMLTweaker")) {
/* 460 */         return "CLIENT";
/*     */       }
/*     */     } 
/*     */     
/* 464 */     String str = getSideName("net.minecraftforge.fml.relauncher.FMLLaunchHandler", "side");
/* 465 */     if (str != null) {
/* 466 */       return str;
/*     */     }
/*     */     
/* 469 */     str = getSideName("cpw.mods.fml.relauncher.FMLLaunchHandler", "side");
/* 470 */     if (str != null) {
/* 471 */       return str;
/*     */     }
/*     */     
/* 474 */     str = getSideName("com.mumfrey.liteloader.launch.LiteLoaderTweaker", "getEnvironmentType");
/* 475 */     if (str != null) {
/* 476 */       return str;
/*     */     }
/*     */     
/* 479 */     return "UNKNOWN";
/*     */   }
/*     */   
/*     */   private String getSideName(String paramString1, String paramString2) {
/*     */     try {
/* 484 */       Class<?> clazz = Class.forName(paramString1, false, (ClassLoader)Launch.classLoader);
/* 485 */       Method method = clazz.getDeclaredMethod(paramString2, new Class[0]);
/* 486 */       return ((Enum)method.invoke(null, new Object[0])).name();
/* 487 */     } catch (Exception exception) {
/* 488 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int findInStackTrace(String paramString1, String paramString2) {
/* 493 */     Thread thread = Thread.currentThread();
/*     */     
/* 495 */     if (!"main".equals(thread.getName())) {
/* 496 */       return 0;
/*     */     }
/*     */     
/* 499 */     StackTraceElement[] arrayOfStackTraceElement = thread.getStackTrace();
/* 500 */     for (StackTraceElement stackTraceElement : arrayOfStackTraceElement) {
/* 501 */       if (paramString1.equals(stackTraceElement.getClassName()) && paramString2.equals(stackTraceElement.getMethodName())) {
/* 502 */         return stackTraceElement.getLineNumber();
/*     */       }
/*     */     } 
/*     */     
/* 506 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\service\mojang\MixinServiceLaunchWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */