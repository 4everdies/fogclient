/*     */ package org.spongepowered.asm.mixin.refmap;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.service.IMixinService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReferenceMapper
/*     */   implements Serializable, IReferenceMapper
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   public static final String DEFAULT_RESOURCE = "mixin.refmap.json";
/*  67 */   public static final ReferenceMapper DEFAULT_MAPPER = new ReferenceMapper(true, "invalid");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private final Map<String, Map<String, String>> mappings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private final Map<String, Map<String, Map<String, String>>> data = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final transient boolean readOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   private transient String context = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient String resource;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceMapper() {
/* 102 */     this(false, "mixin.refmap.json");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ReferenceMapper(boolean paramBoolean, String paramString) {
/* 111 */     this.readOnly = paramBoolean;
/* 112 */     this.resource = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefault() {
/* 120 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   private void setResourceName(String paramString) {
/* 124 */     if (!this.readOnly) {
/* 125 */       this.resource = (paramString != null) ? paramString : "<unknown resource>";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceName() {
/* 135 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 143 */     return isDefault() ? "No refMap loaded." : ("Using refmap " + getResourceName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContext() {
/* 151 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContext(String paramString) {
/* 160 */     this.context = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remap(String paramString1, String paramString2) {
/* 169 */     return remapWithContext(this.context, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remapWithContext(String paramString1, String paramString2, String paramString3) {
/* 179 */     Map<String, Map<String, String>> map = this.mappings;
/* 180 */     if (paramString1 != null) {
/* 181 */       map = this.data.get(paramString1);
/* 182 */       if (map == null) {
/* 183 */         map = this.mappings;
/*     */       }
/*     */     } 
/* 186 */     return remap(map, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String remap(Map<String, Map<String, String>> paramMap, String paramString1, String paramString2) {
/* 193 */     if (paramString1 == null) {
/* 194 */       for (Map<String, String> map1 : paramMap.values()) {
/* 195 */         if (map1.containsKey(paramString2)) {
/* 196 */           return (String)map1.get(paramString2);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 201 */     Map map = paramMap.get(paramString1);
/* 202 */     if (map == null) {
/* 203 */       return paramString2;
/*     */     }
/* 205 */     String str = (String)map.get(paramString2);
/* 206 */     return (str != null) ? str : paramString2;
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
/*     */   public String addMapping(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 219 */     if (this.readOnly || paramString3 == null || paramString4 == null || paramString3.equals(paramString4)) {
/* 220 */       return null;
/*     */     }
/* 222 */     Map<String, Map<String, String>> map = this.mappings;
/* 223 */     if (paramString1 != null) {
/* 224 */       map = this.data.get(paramString1);
/* 225 */       if (map == null) {
/* 226 */         map = Maps.newHashMap();
/* 227 */         this.data.put(paramString1, map);
/*     */       } 
/*     */     } 
/* 230 */     Map<Object, Object> map1 = (Map)map.get(paramString2);
/* 231 */     if (map1 == null) {
/* 232 */       map1 = new HashMap<Object, Object>();
/* 233 */       map.put(paramString2, map1);
/*     */     } 
/* 235 */     return (String)map1.put(paramString3, paramString4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(Appendable paramAppendable) {
/* 244 */     (new GsonBuilder()).setPrettyPrinting().create().toJson(this, paramAppendable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReferenceMapper read(String paramString) {
/* 254 */     Logger logger = LogManager.getLogger("mixin");
/* 255 */     InputStreamReader inputStreamReader = null;
/*     */     try {
/* 257 */       IMixinService iMixinService = MixinService.getService();
/* 258 */       InputStream inputStream = iMixinService.getResourceAsStream(paramString);
/* 259 */       if (inputStream != null) {
/* 260 */         inputStreamReader = new InputStreamReader(inputStream);
/* 261 */         ReferenceMapper referenceMapper = readJson(inputStreamReader);
/* 262 */         referenceMapper.setResourceName(paramString);
/* 263 */         return referenceMapper;
/*     */       } 
/* 265 */     } catch (JsonParseException jsonParseException) {
/* 266 */       logger.error("Invalid REFMAP JSON in " + paramString + ": " + jsonParseException.getClass().getName() + " " + jsonParseException.getMessage());
/* 267 */     } catch (Exception exception) {
/* 268 */       logger.error("Failed reading REFMAP JSON from " + paramString + ": " + exception.getClass().getName() + " " + exception.getMessage());
/*     */     } finally {
/* 270 */       IOUtils.closeQuietly(inputStreamReader);
/*     */     } 
/*     */     
/* 273 */     return DEFAULT_MAPPER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReferenceMapper read(Reader paramReader, String paramString) {
/*     */     try {
/* 285 */       ReferenceMapper referenceMapper = readJson(paramReader);
/* 286 */       referenceMapper.setResourceName(paramString);
/* 287 */       return referenceMapper;
/* 288 */     } catch (Exception exception) {
/* 289 */       return DEFAULT_MAPPER;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ReferenceMapper readJson(Reader paramReader) {
/* 294 */     return (ReferenceMapper)(new Gson()).fromJson(paramReader, ReferenceMapper.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\refmap\ReferenceMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */