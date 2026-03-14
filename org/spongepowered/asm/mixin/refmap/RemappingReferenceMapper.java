/*     */ package org.spongepowered.asm.mixin.refmap;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.common.io.LineProcessor;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RemappingReferenceMapper
/*     */   implements IReferenceMapper
/*     */ {
/*     */   private static final String DEFAULT_RESOURCE_PATH_PROPERTY = "net.minecraftforge.gradle.GradleStart.srg.srg-mcp";
/*     */   private static final String DEFAULT_MAPPING_ENV = "searge";
/*  87 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private static final Map<String, Map<String, String>> srgs = new HashMap<String, Map<String, String>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IReferenceMapper refMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> mappings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private final Map<String, Map<String, String>> cache = new HashMap<String, Map<String, String>>();
/*     */   
/*     */   private RemappingReferenceMapper(MixinEnvironment paramMixinEnvironment, IReferenceMapper paramIReferenceMapper) {
/* 112 */     this.refMap = paramIReferenceMapper;
/* 113 */     this.refMap.setContext(getMappingEnv(paramMixinEnvironment));
/*     */     
/* 115 */     String str = getResource(paramMixinEnvironment);
/* 116 */     this.mappings = loadSrgs(str);
/*     */     
/* 118 */     logger.info("Remapping refMap {} using {}", new Object[] { paramIReferenceMapper.getResourceName(), str });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefault() {
/* 126 */     return this.refMap.isDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceName() {
/* 135 */     return this.refMap.getResourceName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 143 */     return this.refMap.getStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContext() {
/* 151 */     return this.refMap.getContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContext(String paramString) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remap(String paramString1, String paramString2) {
/* 169 */     Map<String, String> map = getCache(paramString1);
/* 170 */     String str = map.get(paramString2);
/* 171 */     if (str == null) {
/* 172 */       str = this.refMap.remap(paramString1, paramString2);
/* 173 */       for (Map.Entry<String, String> entry : this.mappings.entrySet()) {
/* 174 */         str = str.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
/*     */       }
/* 176 */       map.put(paramString2, str);
/*     */     } 
/* 178 */     return str;
/*     */   }
/*     */   
/*     */   private Map<String, String> getCache(String paramString) {
/* 182 */     Map<Object, Object> map = (Map)this.cache.get(paramString);
/* 183 */     if (map == null) {
/* 184 */       map = new HashMap<Object, Object>();
/* 185 */       this.cache.put(paramString, map);
/*     */     } 
/* 187 */     return (Map)map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remapWithContext(String paramString1, String paramString2, String paramString3) {
/* 197 */     return this.refMap.remapWithContext(paramString1, paramString2, paramString3);
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
/*     */   private static Map<String, String> loadSrgs(String paramString) {
/* 209 */     if (srgs.containsKey(paramString)) {
/* 210 */       return srgs.get(paramString);
/*     */     }
/*     */     
/* 213 */     final HashMap<Object, Object> map = new HashMap<Object, Object>();
/* 214 */     srgs.put(paramString, hashMap);
/*     */     
/* 216 */     File file = new File(paramString);
/* 217 */     if (!file.isFile()) {
/* 218 */       return (Map)hashMap;
/*     */     }
/*     */     
/*     */     try {
/* 222 */       Files.readLines(file, Charsets.UTF_8, new LineProcessor<Object>()
/*     */           {
/*     */             public Object getResult()
/*     */             {
/* 226 */               return null;
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean processLine(String param1String) throws IOException {
/* 231 */               if (Strings.isNullOrEmpty(param1String) || param1String.startsWith("#")) {
/* 232 */                 return true;
/*     */               }
/* 234 */               boolean bool1 = false, bool2 = false;
/* 235 */               if (bool2 = param1String.startsWith("MD: ") ? true : (param1String.startsWith("FD: ") ? true : false)) {
/* 236 */                 String[] arrayOfString = param1String.substring(4).split(" ", 4);
/* 237 */                 map.put(arrayOfString[bool1]
/* 238 */                     .substring(arrayOfString[bool1].lastIndexOf('/') + 1), arrayOfString[bool2]
/* 239 */                     .substring(arrayOfString[bool2].lastIndexOf('/') + 1));
/*     */               } 
/*     */               
/* 242 */               return true;
/*     */             }
/*     */           });
/* 245 */     } catch (IOException iOException) {
/* 246 */       logger.warn("Could not read input SRG file: {}", new Object[] { paramString });
/* 247 */       logger.catching(iOException);
/*     */     } 
/*     */     
/* 250 */     return (Map)hashMap;
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
/*     */   public static IReferenceMapper of(MixinEnvironment paramMixinEnvironment, IReferenceMapper paramIReferenceMapper) {
/* 262 */     if (!paramIReferenceMapper.isDefault() && hasData(paramMixinEnvironment)) {
/* 263 */       return new RemappingReferenceMapper(paramMixinEnvironment, paramIReferenceMapper);
/*     */     }
/* 265 */     return paramIReferenceMapper;
/*     */   }
/*     */   
/*     */   private static boolean hasData(MixinEnvironment paramMixinEnvironment) {
/* 269 */     String str = getResource(paramMixinEnvironment);
/* 270 */     return (str != null && (new File(str)).exists());
/*     */   }
/*     */   
/*     */   private static String getResource(MixinEnvironment paramMixinEnvironment) {
/* 274 */     String str = paramMixinEnvironment.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_RESOURCE);
/* 275 */     return Strings.isNullOrEmpty(str) ? System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") : str;
/*     */   }
/*     */   
/*     */   private static String getMappingEnv(MixinEnvironment paramMixinEnvironment) {
/* 279 */     String str = paramMixinEnvironment.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_SOURCE_ENV);
/* 280 */     return Strings.isNullOrEmpty(str) ? "searge" : str;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\refmap\RemappingReferenceMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */