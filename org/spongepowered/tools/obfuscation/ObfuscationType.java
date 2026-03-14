/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
/*     */ import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObfuscationType
/*     */ {
/*  48 */   private static final Map<String, ObfuscationType> types = new LinkedHashMap<String, ObfuscationType>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String key;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ObfuscationTypeDescriptor descriptor;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMixinAnnotationProcessor ap;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IOptionProvider options;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObfuscationType(ObfuscationTypeDescriptor paramObfuscationTypeDescriptor, IMixinAnnotationProcessor paramIMixinAnnotationProcessor) {
/*  71 */     this.key = paramObfuscationTypeDescriptor.getKey();
/*  72 */     this.descriptor = paramObfuscationTypeDescriptor;
/*  73 */     this.ap = paramIMixinAnnotationProcessor;
/*  74 */     this.options = (IOptionProvider)paramIMixinAnnotationProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObfuscationEnvironment createEnvironment() {
/*     */     try {
/*  82 */       Class clazz = this.descriptor.getEnvironmentType();
/*  83 */       Constructor<ObfuscationEnvironment> constructor = clazz.getDeclaredConstructor(new Class[] { ObfuscationType.class });
/*  84 */       constructor.setAccessible(true);
/*  85 */       return constructor.newInstance(new Object[] { this });
/*  86 */     } catch (Exception exception) {
/*  87 */       throw new RuntimeException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  98 */     return this.key;
/*     */   }
/*     */   
/*     */   public ObfuscationTypeDescriptor getConfig() {
/* 102 */     return this.descriptor;
/*     */   }
/*     */   
/*     */   public IMixinAnnotationProcessor getAnnotationProcessor() {
/* 106 */     return this.ap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefault() {
/* 113 */     String str = this.options.getOption("defaultObfuscationEnv");
/* 114 */     return ((str == null && this.key.equals("searge")) || (str != null && this.key
/* 115 */       .equals(str.toLowerCase())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupported() {
/* 122 */     return (getInputFileNames().size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getInputFileNames() {
/* 129 */     ImmutableList.Builder builder = ImmutableList.builder();
/*     */     
/* 131 */     String str1 = this.options.getOption(this.descriptor.getInputFileOption());
/* 132 */     if (str1 != null) {
/* 133 */       builder.add(str1);
/*     */     }
/*     */     
/* 136 */     String str2 = this.options.getOption(this.descriptor.getExtraInputFilesOption());
/* 137 */     if (str2 != null) {
/* 138 */       for (String str : str2.split(";")) {
/* 139 */         builder.add(str.trim());
/*     */       }
/*     */     }
/*     */     
/* 143 */     return (List<String>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOutputFileName() {
/* 150 */     return this.options.getOption(this.descriptor.getOutputFileOption());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<ObfuscationType> types() {
/* 157 */     return types.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ObfuscationType create(ObfuscationTypeDescriptor paramObfuscationTypeDescriptor, IMixinAnnotationProcessor paramIMixinAnnotationProcessor) {
/* 168 */     String str = paramObfuscationTypeDescriptor.getKey();
/* 169 */     if (types.containsKey(str)) {
/* 170 */       throw new IllegalArgumentException("Obfuscation type with key " + str + " was already registered");
/*     */     }
/* 172 */     ObfuscationType obfuscationType = new ObfuscationType(paramObfuscationTypeDescriptor, paramIMixinAnnotationProcessor);
/* 173 */     types.put(str, obfuscationType);
/* 174 */     return obfuscationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ObfuscationType get(String paramString) {
/* 185 */     ObfuscationType obfuscationType = types.get(paramString);
/* 186 */     if (obfuscationType == null) {
/* 187 */       throw new IllegalArgumentException("Obfuscation type with key " + paramString + " was not registered");
/*     */     }
/* 189 */     return obfuscationType;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\ObfuscationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */