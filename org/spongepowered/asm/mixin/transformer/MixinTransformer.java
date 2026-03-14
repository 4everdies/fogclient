/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import java.util.UUID;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.Mixins;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*     */ import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;
/*     */ import org.spongepowered.asm.mixin.throwables.ClassAlreadyLoadedException;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinApplyError;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinPrepareError;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.Extensions;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IExtension;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckInterfaces;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
/*     */ import org.spongepowered.asm.service.IMixinService;
/*     */ import org.spongepowered.asm.service.ITransformer;
/*     */ import org.spongepowered.asm.service.MixinService;
/*     */ import org.spongepowered.asm.transformers.TreeTransformer;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
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
/*     */ public class MixinTransformer
/*     */   extends TreeTransformer
/*     */ {
/*     */   private static final String MIXIN_AGENT_CLASS = "org.spongepowered.tools.agent.MixinAgent";
/*     */   private static final String METRONOME_AGENT_CLASS = "org.spongepowered.metronome.Agent";
/*     */   
/*     */   enum ErrorPhase
/*     */   {
/*  84 */     PREPARE
/*     */     {
/*     */       IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler param2IMixinErrorHandler, String param2String, InvalidMixinException param2InvalidMixinException, IMixinInfo param2IMixinInfo, IMixinErrorHandler.ErrorAction param2ErrorAction) {
/*     */         try {
/*  88 */           return param2IMixinErrorHandler.onPrepareError(param2IMixinInfo.getConfig(), (Throwable)param2InvalidMixinException, param2IMixinInfo, param2ErrorAction);
/*  89 */         } catch (AbstractMethodError abstractMethodError) {
/*     */           
/*  91 */           return param2ErrorAction;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       protected String getContext(IMixinInfo param2IMixinInfo, String param2String) {
/*  97 */         return String.format("preparing %s in %s", new Object[] { param2IMixinInfo.getName(), param2String
/*     */ 
/*     */             
/*     */             });
/*     */       }
/*     */     },
/* 103 */     APPLY
/*     */     {
/*     */       IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler param2IMixinErrorHandler, String param2String, InvalidMixinException param2InvalidMixinException, IMixinInfo param2IMixinInfo, IMixinErrorHandler.ErrorAction param2ErrorAction) {
/*     */         try {
/* 107 */           return param2IMixinErrorHandler.onApplyError(param2String, (Throwable)param2InvalidMixinException, param2IMixinInfo, param2ErrorAction);
/* 108 */         } catch (AbstractMethodError abstractMethodError) {
/*     */           
/* 110 */           return param2ErrorAction;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       protected String getContext(IMixinInfo param2IMixinInfo, String param2String) {
/* 116 */         return String.format("%s -> %s", new Object[] { param2IMixinInfo, param2String });
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     private final String text;
/*     */ 
/*     */     
/*     */     ErrorPhase() {
/* 126 */       this.text = name().toLowerCase();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLogMessage(String param1String, InvalidMixinException param1InvalidMixinException, IMixinInfo param1IMixinInfo) {
/* 134 */       return String.format("Mixin %s failed %s: %s %s", new Object[] { this.text, getContext(param1IMixinInfo, param1String), param1InvalidMixinException.getClass().getName(), param1InvalidMixinException.getMessage() });
/*     */     }
/*     */     
/*     */     public String getErrorMessage(IMixinInfo param1IMixinInfo, IMixinConfig param1IMixinConfig, MixinEnvironment.Phase param1Phase) {
/* 138 */       return String.format("Mixin [%s] from phase [%s] in config [%s] FAILED during %s", new Object[] { param1IMixinInfo, param1Phase, param1IMixinConfig, name() });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler param1IMixinErrorHandler, String param1String, InvalidMixinException param1InvalidMixinException, IMixinInfo param1IMixinInfo, IMixinErrorHandler.ErrorAction param1ErrorAction);
/*     */ 
/*     */     
/*     */     protected abstract String getContext(IMixinInfo param1IMixinInfo, String param1String);
/*     */   }
/*     */   
/* 149 */   static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private final IMixinService service = MixinService.getService();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   private final List<MixinConfig> configs = new ArrayList<MixinConfig>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   private final List<MixinConfig> pendingConfigs = new ArrayList<MixinConfig>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReEntranceLock lock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   private final String sessionId = UUID.randomUUID().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Extensions extensions;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IHotSwap hotSwapper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MixinPostProcessor postProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Profiler profiler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MixinEnvironment currentEnvironment;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private Level verboseLoggingLevel = Level.DEBUG;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean errorState = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   private int transformedCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MixinTransformer() {
/* 222 */     MixinEnvironment mixinEnvironment = MixinEnvironment.getCurrentEnvironment();
/*     */     
/* 224 */     Object object = mixinEnvironment.getActiveTransformer();
/* 225 */     if (object instanceof ITransformer) {
/* 226 */       throw new MixinException("Terminating MixinTransformer instance " + this);
/*     */     }
/*     */ 
/*     */     
/* 230 */     mixinEnvironment.setActiveTransformer((ITransformer)this);
/*     */     
/* 232 */     this.lock = this.service.getReEntranceLock();
/*     */     
/* 234 */     this.extensions = new Extensions(this);
/* 235 */     this.hotSwapper = initHotSwapper(mixinEnvironment);
/* 236 */     this.postProcessor = new MixinPostProcessor();
/*     */     
/* 238 */     this.extensions.add((IClassGenerator)new ArgsClassGenerator());
/* 239 */     this.extensions.add(new InnerClassGenerator());
/*     */     
/* 241 */     this.extensions.add((IExtension)new ExtensionClassExporter(mixinEnvironment));
/* 242 */     this.extensions.add((IExtension)new ExtensionCheckClass());
/* 243 */     this.extensions.add((IExtension)new ExtensionCheckInterfaces());
/*     */     
/* 245 */     this.profiler = MixinEnvironment.getProfiler();
/*     */   }
/*     */   
/*     */   private IHotSwap initHotSwapper(MixinEnvironment paramMixinEnvironment) {
/* 249 */     if (!paramMixinEnvironment.getOption(MixinEnvironment.Option.HOT_SWAP)) {
/* 250 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 254 */       logger.info("Attempting to load Hot-Swap agent");
/*     */ 
/*     */       
/* 257 */       Class<?> clazz = Class.forName("org.spongepowered.tools.agent.MixinAgent");
/* 258 */       Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { MixinTransformer.class });
/* 259 */       return (IHotSwap)constructor.newInstance(new Object[] { this });
/* 260 */     } catch (Throwable throwable) {
/* 261 */       logger.info("Hot-swap agent could not be loaded, hot swapping of mixins won't work. {}: {}", new Object[] { throwable
/* 262 */             .getClass().getSimpleName(), throwable.getMessage() });
/*     */ 
/*     */       
/* 265 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void audit(MixinEnvironment paramMixinEnvironment) {
/* 274 */     HashSet<String> hashSet = new HashSet();
/*     */     
/* 276 */     for (MixinConfig mixinConfig : this.configs) {
/* 277 */       hashSet.addAll(mixinConfig.getUnhandledTargets());
/*     */     }
/*     */     
/* 280 */     Logger logger = LogManager.getLogger("mixin/audit");
/*     */     
/* 282 */     for (String str : hashSet) {
/*     */       try {
/* 284 */         logger.info("Force-loading class {}", new Object[] { str });
/* 285 */         this.service.getClassProvider().findClass(str, true);
/* 286 */       } catch (ClassNotFoundException classNotFoundException) {
/* 287 */         logger.error("Could not force-load " + str, classNotFoundException);
/*     */       } 
/*     */     } 
/*     */     
/* 291 */     for (MixinConfig mixinConfig : this.configs) {
/* 292 */       for (String str : mixinConfig.getUnhandledTargets()) {
/* 293 */         ClassAlreadyLoadedException classAlreadyLoadedException = new ClassAlreadyLoadedException(str + " was already classloaded");
/* 294 */         logger.error("Could not force-load " + str, (Throwable)classAlreadyLoadedException);
/*     */       } 
/*     */     } 
/*     */     
/* 298 */     if (paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_PROFILER)) {
/* 299 */       printProfilerSummary();
/*     */     }
/*     */   }
/*     */   
/*     */   private void printProfilerSummary() {
/* 304 */     DecimalFormat decimalFormat1 = new DecimalFormat("(###0.000");
/* 305 */     DecimalFormat decimalFormat2 = new DecimalFormat("(###0.0");
/* 306 */     PrettyPrinter prettyPrinter = this.profiler.printer(false, false);
/*     */     
/* 308 */     long l1 = this.profiler.get("mixin.prepare").getTotalTime();
/* 309 */     long l2 = this.profiler.get("mixin.read").getTotalTime();
/* 310 */     long l3 = this.profiler.get("mixin.apply").getTotalTime();
/* 311 */     long l4 = this.profiler.get("mixin.write").getTotalTime();
/* 312 */     long l5 = this.profiler.get("mixin").getTotalTime();
/*     */     
/* 314 */     long l6 = this.profiler.get("class.load").getTotalTime();
/* 315 */     long l7 = this.profiler.get("class.transform").getTotalTime();
/* 316 */     long l8 = this.profiler.get("mixin.debug.export").getTotalTime();
/* 317 */     long l9 = l5 - l6 - l7 - l8;
/* 318 */     double d1 = l9 / l5 * 100.0D;
/* 319 */     double d2 = l6 / l5 * 100.0D;
/* 320 */     double d3 = l7 / l5 * 100.0D;
/* 321 */     double d4 = l8 / l5 * 100.0D;
/*     */     
/* 323 */     long l10 = 0L;
/* 324 */     Profiler.Section section = null;
/*     */     
/* 326 */     for (Profiler.Section section1 : this.profiler.getSections()) {
/* 327 */       long l = section1.getName().startsWith("class.transform.") ? section1.getTotalTime() : 0L;
/* 328 */       if (l > l10) {
/* 329 */         l10 = l;
/* 330 */         section = section1;
/*     */       } 
/*     */     } 
/*     */     
/* 334 */     prettyPrinter.hr().add("Summary").hr().add();
/*     */     
/* 336 */     String str = "%9d ms %12s seconds)";
/* 337 */     prettyPrinter.kv("Total mixin time", str, new Object[] { Long.valueOf(l5), decimalFormat1.format(l5 * 0.001D) }).add();
/* 338 */     prettyPrinter.kv("Preparing mixins", str, new Object[] { Long.valueOf(l1), decimalFormat1.format(l1 * 0.001D) });
/* 339 */     prettyPrinter.kv("Reading input", str, new Object[] { Long.valueOf(l2), decimalFormat1.format(l2 * 0.001D) });
/* 340 */     prettyPrinter.kv("Applying mixins", str, new Object[] { Long.valueOf(l3), decimalFormat1.format(l3 * 0.001D) });
/* 341 */     prettyPrinter.kv("Writing output", str, new Object[] { Long.valueOf(l4), decimalFormat1.format(l4 * 0.001D) }).add();
/*     */     
/* 343 */     prettyPrinter.kv("of which", "");
/* 344 */     prettyPrinter.kv("Time spent loading from disk", str, new Object[] { Long.valueOf(l6), decimalFormat1.format(l6 * 0.001D) });
/* 345 */     prettyPrinter.kv("Time spent transforming classes", str, new Object[] { Long.valueOf(l7), decimalFormat1.format(l7 * 0.001D) }).add();
/*     */     
/* 347 */     if (section != null) {
/* 348 */       prettyPrinter.kv("Worst transformer", section.getName());
/* 349 */       prettyPrinter.kv("Class", section.getInfo());
/* 350 */       prettyPrinter.kv("Time spent", "%s seconds", new Object[] { Double.valueOf(section.getTotalSeconds()) });
/* 351 */       prettyPrinter.kv("called", "%d times", new Object[] { Integer.valueOf(section.getTotalCount()) }).add();
/*     */     } 
/*     */     
/* 354 */     prettyPrinter.kv("   Time allocation:     Processing mixins", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(l9), decimalFormat2.format(d1) });
/* 355 */     prettyPrinter.kv("Loading classes", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(l6), decimalFormat2.format(d2) });
/* 356 */     prettyPrinter.kv("Running transformers", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(l7), decimalFormat2.format(d3) });
/* 357 */     if (l8 > 0L) {
/* 358 */       prettyPrinter.kv("Exporting classes (debug)", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(l8), decimalFormat2.format(d4) });
/*     */     }
/* 360 */     prettyPrinter.add();
/*     */     
/*     */     try {
/* 363 */       Class clazz = this.service.getClassProvider().findAgentClass("org.spongepowered.metronome.Agent", false);
/* 364 */       Method method = clazz.getDeclaredMethod("getTimes", new Class[0]);
/*     */ 
/*     */       
/* 367 */       Map map = (Map)method.invoke(null, new Object[0]);
/*     */       
/* 369 */       prettyPrinter.hr().add("Transformer Times").hr().add();
/*     */       
/* 371 */       int i = 10;
/* 372 */       for (Map.Entry entry : map.entrySet()) {
/* 373 */         i = Math.max(i, ((String)entry.getKey()).length());
/*     */       }
/*     */       
/* 376 */       for (Map.Entry entry : map.entrySet()) {
/* 377 */         String str1 = (String)entry.getKey();
/* 378 */         long l = 0L;
/* 379 */         for (Profiler.Section section1 : this.profiler.getSections()) {
/* 380 */           if (str1.equals(section1.getInfo())) {
/* 381 */             l = section1.getTotalTime();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 386 */         if (l > 0L) {
/* 387 */           prettyPrinter.add("%-" + i + "s %8s ms %8s ms in mixin)", new Object[] { str1, Long.valueOf(((Long)entry.getValue()).longValue() + l), "(" + l }); continue;
/*     */         } 
/* 389 */         prettyPrinter.add("%-" + i + "s %8s ms", new Object[] { str1, entry.getValue() });
/*     */       } 
/*     */ 
/*     */       
/* 393 */       prettyPrinter.add();
/*     */     }
/* 395 */     catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 400 */     prettyPrinter.print();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 408 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDelegationExcluded() {
/* 417 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized byte[] transformClassBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
/* 426 */     if (paramString2 == null || this.errorState) {
/* 427 */       return paramArrayOfbyte;
/*     */     }
/*     */     
/* 430 */     MixinEnvironment mixinEnvironment = MixinEnvironment.getCurrentEnvironment();
/*     */     
/* 432 */     if (paramArrayOfbyte == null) {
/* 433 */       for (IClassGenerator iClassGenerator : this.extensions.getGenerators()) {
/* 434 */         Profiler.Section section1 = this.profiler.begin(new String[] { "generator", iClassGenerator.getClass().getSimpleName().toLowerCase() });
/* 435 */         paramArrayOfbyte = iClassGenerator.generate(paramString2);
/* 436 */         section1.end();
/* 437 */         if (paramArrayOfbyte != null) {
/* 438 */           this.extensions.export(mixinEnvironment, paramString2.replace('.', '/'), false, paramArrayOfbyte);
/* 439 */           return paramArrayOfbyte;
/*     */         } 
/*     */       } 
/* 442 */       return paramArrayOfbyte;
/*     */     } 
/*     */     
/* 445 */     boolean bool = this.lock.push().check();
/*     */     
/* 447 */     Profiler.Section section = this.profiler.begin("mixin");
/*     */     
/* 449 */     if (!bool) {
/*     */       try {
/* 451 */         checkSelect(mixinEnvironment);
/* 452 */       } catch (Exception exception) {
/* 453 */         this.lock.pop();
/* 454 */         section.end();
/* 455 */         throw new MixinException(exception);
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 460 */       if (this.postProcessor.canTransform(paramString2)) {
/* 461 */         Profiler.Section section1 = this.profiler.begin("postprocessor");
/* 462 */         byte[] arrayOfByte = this.postProcessor.transformClassBytes(paramString1, paramString2, paramArrayOfbyte);
/* 463 */         section1.end();
/* 464 */         this.extensions.export(mixinEnvironment, paramString2, false, arrayOfByte);
/* 465 */         return arrayOfByte;
/*     */       } 
/*     */       
/* 468 */       TreeSet<MixinInfo> treeSet = null;
/* 469 */       boolean bool1 = false;
/*     */       
/* 471 */       for (MixinConfig mixinConfig : this.configs) {
/* 472 */         if (mixinConfig.packageMatch(paramString2)) {
/* 473 */           bool1 = true;
/*     */           
/*     */           continue;
/*     */         } 
/* 477 */         if (mixinConfig.hasMixinsFor(paramString2)) {
/* 478 */           if (treeSet == null) {
/* 479 */             treeSet = new TreeSet();
/*     */           }
/*     */ 
/*     */           
/* 483 */           treeSet.addAll(mixinConfig.getMixinsFor(paramString2));
/*     */         } 
/*     */       } 
/*     */       
/* 487 */       if (bool1) {
/* 488 */         throw new NoClassDefFoundError(String.format("%s is a mixin class and cannot be referenced directly", new Object[] { paramString2 }));
/*     */       }
/*     */       
/* 491 */       if (treeSet != null) {
/*     */         
/* 493 */         if (bool) {
/* 494 */           logger.warn("Re-entrance detected, this will cause serious problems.", (Throwable)new MixinException());
/* 495 */           throw new MixinApplyError("Re-entrance error.");
/*     */         } 
/*     */         
/* 498 */         if (this.hotSwapper != null) {
/* 499 */           this.hotSwapper.registerTargetClass(paramString2, paramArrayOfbyte);
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 504 */           Profiler.Section section1 = this.profiler.begin("read");
/* 505 */           ClassNode classNode = readClass(paramArrayOfbyte, true);
/* 506 */           TargetClassContext targetClassContext = new TargetClassContext(mixinEnvironment, this.extensions, this.sessionId, paramString2, classNode, treeSet);
/*     */           
/* 508 */           section1.end();
/* 509 */           paramArrayOfbyte = applyMixins(mixinEnvironment, targetClassContext);
/* 510 */           this.transformedCount++;
/* 511 */         } catch (InvalidMixinException invalidMixinException) {
/* 512 */           dumpClassOnFailure(paramString2, paramArrayOfbyte, mixinEnvironment);
/* 513 */           handleMixinApplyError(paramString2, invalidMixinException, mixinEnvironment);
/*     */         } 
/*     */       } 
/*     */       
/* 517 */       return paramArrayOfbyte;
/* 518 */     } catch (Throwable throwable) {
/* 519 */       throwable.printStackTrace();
/* 520 */       dumpClassOnFailure(paramString2, paramArrayOfbyte, mixinEnvironment);
/* 521 */       throw new MixinTransformerError("An unexpected critical error was encountered", throwable);
/*     */     } finally {
/* 523 */       this.lock.pop();
/* 524 */       section.end();
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
/*     */   public List<String> reload(String paramString, byte[] paramArrayOfbyte) {
/* 536 */     if (this.lock.getDepth() > 0) {
/* 537 */       throw new MixinApplyError("Cannot reload mixin if re-entrant lock entered");
/*     */     }
/* 539 */     ArrayList<String> arrayList = new ArrayList();
/* 540 */     for (MixinConfig mixinConfig : this.configs) {
/* 541 */       arrayList.addAll(mixinConfig.reloadMixin(paramString, paramArrayOfbyte));
/*     */     }
/* 543 */     return arrayList;
/*     */   }
/*     */   
/*     */   private void checkSelect(MixinEnvironment paramMixinEnvironment) {
/* 547 */     if (this.currentEnvironment != paramMixinEnvironment) {
/* 548 */       select(paramMixinEnvironment);
/*     */       
/*     */       return;
/*     */     } 
/* 552 */     int i = Mixins.getUnvisitedCount();
/* 553 */     if (i > 0 && this.transformedCount == 0) {
/* 554 */       select(paramMixinEnvironment);
/*     */     }
/*     */   }
/*     */   
/*     */   private void select(MixinEnvironment paramMixinEnvironment) {
/* 559 */     this.verboseLoggingLevel = paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE) ? Level.INFO : Level.DEBUG;
/* 560 */     if (this.transformedCount > 0) {
/* 561 */       logger.log(this.verboseLoggingLevel, "Ending {}, applied {} mixins", new Object[] { this.currentEnvironment, Integer.valueOf(this.transformedCount) });
/*     */     }
/* 563 */     String str = (this.currentEnvironment == paramMixinEnvironment) ? "Checking for additional" : "Preparing";
/* 564 */     logger.log(this.verboseLoggingLevel, "{} mixins for {}", new Object[] { str, paramMixinEnvironment });
/*     */     
/* 566 */     this.profiler.setActive(true);
/* 567 */     this.profiler.mark(paramMixinEnvironment.getPhase().toString() + ":prepare");
/* 568 */     Profiler.Section section = this.profiler.begin("prepare");
/*     */     
/* 570 */     selectConfigs(paramMixinEnvironment);
/* 571 */     this.extensions.select(paramMixinEnvironment);
/* 572 */     int i = prepareConfigs(paramMixinEnvironment);
/* 573 */     this.currentEnvironment = paramMixinEnvironment;
/* 574 */     this.transformedCount = 0;
/*     */     
/* 576 */     section.end();
/*     */     
/* 578 */     long l = section.getTime();
/* 579 */     double d = section.getSeconds();
/* 580 */     if (d > 0.25D) {
/* 581 */       long l1 = this.profiler.get("class.load").getTime();
/* 582 */       long l2 = this.profiler.get("class.transform").getTime();
/* 583 */       long l3 = this.profiler.get("mixin.plugin").getTime();
/* 584 */       String str1 = (new DecimalFormat("###0.000")).format(d);
/* 585 */       String str2 = (new DecimalFormat("###0.0")).format(l / i);
/*     */       
/* 587 */       logger.log(this.verboseLoggingLevel, "Prepared {} mixins in {} sec ({}ms avg) ({}ms load, {}ms transform, {}ms plugin)", new Object[] {
/* 588 */             Integer.valueOf(i), str1, str2, Long.valueOf(l1), Long.valueOf(l2), Long.valueOf(l3)
/*     */           });
/*     */     } 
/* 591 */     this.profiler.mark(paramMixinEnvironment.getPhase().toString() + ":apply");
/* 592 */     this.profiler.setActive(paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_PROFILER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void selectConfigs(MixinEnvironment paramMixinEnvironment) {
/* 601 */     for (Iterator<Config> iterator = Mixins.getConfigs().iterator(); iterator.hasNext(); ) {
/* 602 */       Config config = iterator.next();
/*     */       try {
/* 604 */         MixinConfig mixinConfig = config.get();
/* 605 */         if (mixinConfig.select(paramMixinEnvironment)) {
/* 606 */           iterator.remove();
/* 607 */           logger.log(this.verboseLoggingLevel, "Selecting config {}", new Object[] { mixinConfig });
/* 608 */           mixinConfig.onSelect();
/* 609 */           this.pendingConfigs.add(mixinConfig);
/*     */         } 
/* 611 */       } catch (Exception exception) {
/* 612 */         logger.warn(String.format("Failed to select mixin config: %s", new Object[] { config }), exception);
/*     */       } 
/*     */     } 
/*     */     
/* 616 */     Collections.sort(this.pendingConfigs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int prepareConfigs(MixinEnvironment paramMixinEnvironment) {
/* 626 */     int i = 0;
/*     */     
/* 628 */     final IHotSwap hotSwapper = this.hotSwapper;
/* 629 */     for (MixinConfig mixinConfig : this.pendingConfigs) {
/* 630 */       mixinConfig.addListener(this.postProcessor);
/* 631 */       if (iHotSwap != null) {
/* 632 */         mixinConfig.addListener(new MixinConfig.IListener()
/*     */             {
/*     */               public void onPrepare(MixinInfo param1MixinInfo) {
/* 635 */                 hotSwapper.registerMixinClass(param1MixinInfo.getClassName());
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               public void onInit(MixinInfo param1MixinInfo) {}
/*     */             });
/*     */       }
/*     */     } 
/* 644 */     for (MixinConfig mixinConfig : this.pendingConfigs) {
/*     */       try {
/* 646 */         logger.log(this.verboseLoggingLevel, "Preparing {} ({})", new Object[] { mixinConfig, Integer.valueOf(mixinConfig.getDeclaredMixinCount()) });
/* 647 */         mixinConfig.prepare();
/* 648 */         i += mixinConfig.getMixinCount();
/* 649 */       } catch (InvalidMixinException invalidMixinException) {
/* 650 */         handleMixinPrepareError(mixinConfig, invalidMixinException, paramMixinEnvironment);
/* 651 */       } catch (Exception exception) {
/* 652 */         String str = exception.getMessage();
/* 653 */         logger.error("Error encountered whilst initialising mixin config '" + mixinConfig.getName() + "': " + str, exception);
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     for (MixinConfig mixinConfig : this.pendingConfigs) {
/* 658 */       IMixinConfigPlugin iMixinConfigPlugin = mixinConfig.getPlugin();
/* 659 */       if (iMixinConfigPlugin == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 663 */       HashSet<String> hashSet = new HashSet();
/* 664 */       for (MixinConfig mixinConfig1 : this.pendingConfigs) {
/* 665 */         if (!mixinConfig1.equals(mixinConfig)) {
/* 666 */           hashSet.addAll(mixinConfig1.getTargets());
/*     */         }
/*     */       } 
/*     */       
/* 670 */       iMixinConfigPlugin.acceptTargets(mixinConfig.getTargets(), Collections.unmodifiableSet(hashSet));
/*     */     } 
/*     */     
/* 673 */     for (MixinConfig mixinConfig : this.pendingConfigs) {
/*     */       try {
/* 675 */         mixinConfig.postInitialise();
/* 676 */       } catch (InvalidMixinException invalidMixinException) {
/* 677 */         handleMixinPrepareError(mixinConfig, invalidMixinException, paramMixinEnvironment);
/* 678 */       } catch (Exception exception) {
/* 679 */         String str = exception.getMessage();
/* 680 */         logger.error("Error encountered during mixin config postInit step'" + mixinConfig.getName() + "': " + str, exception);
/*     */       } 
/*     */     } 
/*     */     
/* 684 */     this.configs.addAll(this.pendingConfigs);
/* 685 */     Collections.sort(this.configs);
/* 686 */     this.pendingConfigs.clear();
/*     */     
/* 688 */     return i;
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
/*     */   private byte[] applyMixins(MixinEnvironment paramMixinEnvironment, TargetClassContext paramTargetClassContext) {
/* 700 */     Profiler.Section section = this.profiler.begin("preapply");
/* 701 */     this.extensions.preApply(paramTargetClassContext);
/* 702 */     section = section.next("apply");
/* 703 */     apply(paramTargetClassContext);
/* 704 */     section = section.next("postapply");
/*     */     try {
/* 706 */       this.extensions.postApply(paramTargetClassContext);
/* 707 */     } catch (org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass.ValidationFailedException validationFailedException) {
/* 708 */       logger.info(validationFailedException.getMessage());
/*     */       
/* 710 */       if (paramTargetClassContext.isExportForced() || paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
/* 711 */         writeClass(paramTargetClassContext);
/*     */       }
/*     */     } 
/* 714 */     section.end();
/* 715 */     return writeClass(paramTargetClassContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void apply(TargetClassContext paramTargetClassContext) {
/* 724 */     paramTargetClassContext.applyMixins();
/*     */   }
/*     */   
/*     */   private void handleMixinPrepareError(MixinConfig paramMixinConfig, InvalidMixinException paramInvalidMixinException, MixinEnvironment paramMixinEnvironment) throws MixinPrepareError {
/* 728 */     handleMixinError(paramMixinConfig.getName(), paramInvalidMixinException, paramMixinEnvironment, ErrorPhase.PREPARE);
/*     */   }
/*     */   
/*     */   private void handleMixinApplyError(String paramString, InvalidMixinException paramInvalidMixinException, MixinEnvironment paramMixinEnvironment) throws MixinApplyError {
/* 732 */     handleMixinError(paramString, paramInvalidMixinException, paramMixinEnvironment, ErrorPhase.APPLY);
/*     */   }
/*     */   
/*     */   private void handleMixinError(String paramString, InvalidMixinException paramInvalidMixinException, MixinEnvironment paramMixinEnvironment, ErrorPhase paramErrorPhase) throws Error {
/* 736 */     this.errorState = true;
/*     */     
/* 738 */     IMixinInfo iMixinInfo = paramInvalidMixinException.getMixin();
/*     */     
/* 740 */     if (iMixinInfo == null) {
/* 741 */       logger.error("InvalidMixinException has no mixin!", (Throwable)paramInvalidMixinException);
/* 742 */       throw paramInvalidMixinException;
/*     */     } 
/*     */     
/* 745 */     IMixinConfig iMixinConfig = iMixinInfo.getConfig();
/* 746 */     MixinEnvironment.Phase phase = iMixinInfo.getPhase();
/* 747 */     IMixinErrorHandler.ErrorAction errorAction = iMixinConfig.isRequired() ? IMixinErrorHandler.ErrorAction.ERROR : IMixinErrorHandler.ErrorAction.WARN;
/*     */     
/* 749 */     if (paramMixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/* 750 */       (new PrettyPrinter())
/* 751 */         .add("Invalid Mixin").centre()
/* 752 */         .hr('-')
/* 753 */         .kvWidth(10)
/* 754 */         .kv("Action", paramErrorPhase.name())
/* 755 */         .kv("Mixin", iMixinInfo.getClassName())
/* 756 */         .kv("Config", iMixinConfig.getName())
/* 757 */         .kv("Phase", phase)
/* 758 */         .hr('-')
/* 759 */         .add("    %s", new Object[] { paramInvalidMixinException.getClass().getName()
/* 760 */           }).hr('-')
/* 761 */         .addWrapped("    %s", new Object[] { paramInvalidMixinException.getMessage()
/* 762 */           }).hr('-')
/* 763 */         .add((Throwable)paramInvalidMixinException, 8)
/* 764 */         .trace(errorAction.logLevel);
/*     */     }
/*     */     
/* 767 */     for (IMixinErrorHandler iMixinErrorHandler : getErrorHandlers(iMixinInfo.getPhase())) {
/* 768 */       IMixinErrorHandler.ErrorAction errorAction1 = paramErrorPhase.onError(iMixinErrorHandler, paramString, paramInvalidMixinException, iMixinInfo, errorAction);
/* 769 */       if (errorAction1 != null) {
/* 770 */         errorAction = errorAction1;
/*     */       }
/*     */     } 
/*     */     
/* 774 */     logger.log(errorAction.logLevel, paramErrorPhase.getLogMessage(paramString, paramInvalidMixinException, iMixinInfo), (Throwable)paramInvalidMixinException);
/*     */     
/* 776 */     this.errorState = false;
/*     */     
/* 778 */     if (errorAction == IMixinErrorHandler.ErrorAction.ERROR) {
/* 779 */       throw new MixinApplyError(paramErrorPhase.getErrorMessage(iMixinInfo, iMixinConfig, phase), paramInvalidMixinException);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<IMixinErrorHandler> getErrorHandlers(MixinEnvironment.Phase paramPhase) {
/* 784 */     ArrayList<IMixinErrorHandler> arrayList = new ArrayList();
/*     */     
/* 786 */     for (String str : Mixins.getErrorHandlerClasses()) {
/*     */       try {
/* 788 */         logger.info("Instancing error handler class {}", new Object[] { str });
/* 789 */         Class<IMixinErrorHandler> clazz = this.service.getClassProvider().findClass(str, true);
/* 790 */         IMixinErrorHandler iMixinErrorHandler = clazz.newInstance();
/* 791 */         if (iMixinErrorHandler != null) {
/* 792 */           arrayList.add(iMixinErrorHandler);
/*     */         }
/* 794 */       } catch (Throwable throwable) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 799 */     return arrayList;
/*     */   }
/*     */   
/*     */   private byte[] writeClass(TargetClassContext paramTargetClassContext) {
/* 803 */     return writeClass(paramTargetClassContext.getClassName(), paramTargetClassContext.getClassNode(), paramTargetClassContext.isExportForced());
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] writeClass(String paramString, ClassNode paramClassNode, boolean paramBoolean) {
/* 808 */     Profiler.Section section = this.profiler.begin("write");
/* 809 */     byte[] arrayOfByte = writeClass(paramClassNode);
/* 810 */     section.end();
/* 811 */     this.extensions.export(this.currentEnvironment, paramString, paramBoolean, arrayOfByte);
/* 812 */     return arrayOfByte;
/*     */   }
/*     */   
/*     */   private void dumpClassOnFailure(String paramString, byte[] paramArrayOfbyte, MixinEnvironment paramMixinEnvironment) {
/* 816 */     if (paramMixinEnvironment.getOption(MixinEnvironment.Option.DUMP_TARGET_ON_FAILURE)) {
/* 817 */       ExtensionClassExporter extensionClassExporter = (ExtensionClassExporter)this.extensions.getExtension(ExtensionClassExporter.class);
/* 818 */       extensionClassExporter.dumpClass(paramString.replace('.', '/') + ".target", paramArrayOfbyte);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MixinTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */