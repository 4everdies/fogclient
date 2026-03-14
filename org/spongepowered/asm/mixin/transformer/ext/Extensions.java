/*     */ package org.spongepowered.asm.mixin.transformer.ext;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Extensions
/*     */ {
/*     */   private final MixinTransformer transformer;
/*  52 */   private final List<IExtension> extensions = new ArrayList<IExtension>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final Map<Class<? extends IExtension>, IExtension> extensionMap = new HashMap<Class<? extends IExtension>, IExtension>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final List<IClassGenerator> generators = new ArrayList<IClassGenerator>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final List<IClassGenerator> generatorsView = Collections.unmodifiableList(this.generators);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private final Map<Class<? extends IClassGenerator>, IClassGenerator> generatorMap = new HashMap<Class<? extends IClassGenerator>, IClassGenerator>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private List<IExtension> activeExtensions = Collections.emptyList();
/*     */   
/*     */   public Extensions(MixinTransformer paramMixinTransformer) {
/*  84 */     this.transformer = paramMixinTransformer;
/*     */   }
/*     */   
/*     */   public MixinTransformer getTransformer() {
/*  88 */     return this.transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(IExtension paramIExtension) {
/*  97 */     this.extensions.add(paramIExtension);
/*  98 */     this.extensionMap.put(paramIExtension.getClass(), paramIExtension);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IExtension> getExtensions() {
/* 105 */     return Collections.unmodifiableList(this.extensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IExtension> getActiveExtensions() {
/* 112 */     return this.activeExtensions;
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
/*     */   public <T extends IExtension> T getExtension(Class<? extends IExtension> paramClass) {
/* 124 */     return (T)lookup(paramClass, this.extensionMap, this.extensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void select(MixinEnvironment paramMixinEnvironment) {
/* 133 */     ImmutableList.Builder builder = ImmutableList.builder();
/*     */     
/* 135 */     for (IExtension iExtension : this.extensions) {
/* 136 */       if (iExtension.checkActive(paramMixinEnvironment)) {
/* 137 */         builder.add(iExtension);
/*     */       }
/*     */     } 
/*     */     
/* 141 */     this.activeExtensions = (List<IExtension>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preApply(ITargetClassContext paramITargetClassContext) {
/* 150 */     for (IExtension iExtension : this.activeExtensions) {
/* 151 */       iExtension.preApply(paramITargetClassContext);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postApply(ITargetClassContext paramITargetClassContext) {
/* 161 */     for (IExtension iExtension : this.activeExtensions) {
/* 162 */       iExtension.postApply(paramITargetClassContext);
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
/*     */   public void export(MixinEnvironment paramMixinEnvironment, String paramString, boolean paramBoolean, byte[] paramArrayOfbyte) {
/* 176 */     for (IExtension iExtension : this.activeExtensions) {
/* 177 */       iExtension.export(paramMixinEnvironment, paramString, paramBoolean, paramArrayOfbyte);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(IClassGenerator paramIClassGenerator) {
/* 187 */     this.generators.add(paramIClassGenerator);
/* 188 */     this.generatorMap.put(paramIClassGenerator.getClass(), paramIClassGenerator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IClassGenerator> getGenerators() {
/* 195 */     return this.generatorsView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends IClassGenerator> T getGenerator(Class<? extends IClassGenerator> paramClass) {
/* 205 */     return (T)lookup(paramClass, this.generatorMap, this.generators);
/*     */   }
/*     */   
/*     */   private static <T> T lookup(Class<? extends T> paramClass, Map<Class<? extends T>, T> paramMap, List<T> paramList) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: aload_0
/*     */     //   2: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   7: astore_3
/*     */     //   8: aload_3
/*     */     //   9: ifnonnull -> 108
/*     */     //   12: aload_2
/*     */     //   13: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   18: astore #4
/*     */     //   20: aload #4
/*     */     //   22: invokeinterface hasNext : ()Z
/*     */     //   27: ifeq -> 60
/*     */     //   30: aload #4
/*     */     //   32: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   37: astore #5
/*     */     //   39: aload_0
/*     */     //   40: aload #5
/*     */     //   42: invokevirtual getClass : ()Ljava/lang/Class;
/*     */     //   45: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
/*     */     //   48: ifeq -> 57
/*     */     //   51: aload #5
/*     */     //   53: astore_3
/*     */     //   54: goto -> 60
/*     */     //   57: goto -> 20
/*     */     //   60: aload_3
/*     */     //   61: ifnonnull -> 99
/*     */     //   64: new java/lang/IllegalArgumentException
/*     */     //   67: dup
/*     */     //   68: new java/lang/StringBuilder
/*     */     //   71: dup
/*     */     //   72: invokespecial <init> : ()V
/*     */     //   75: ldc 'Extension for <'
/*     */     //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   80: aload_0
/*     */     //   81: invokevirtual getName : ()Ljava/lang/String;
/*     */     //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   87: ldc '> could not be found'
/*     */     //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   92: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   95: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   98: athrow
/*     */     //   99: aload_1
/*     */     //   100: aload_0
/*     */     //   101: aload_3
/*     */     //   102: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   107: pop
/*     */     //   108: aload_3
/*     */     //   109: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #209	-> 0
/*     */     //   #210	-> 8
/*     */     //   #211	-> 12
/*     */     //   #212	-> 39
/*     */     //   #213	-> 51
/*     */     //   #214	-> 54
/*     */     //   #216	-> 57
/*     */     //   #218	-> 60
/*     */     //   #219	-> 64
/*     */     //   #222	-> 99
/*     */     //   #225	-> 108
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\ext\Extensions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */