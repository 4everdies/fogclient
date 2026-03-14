/*     */ package org.spongepowered.asm.launch.platform;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MainAttributes
/*     */ {
/*  42 */   private static final Map<URI, MainAttributes> instances = new HashMap<URI, MainAttributes>();
/*     */ 
/*     */   
/*     */   protected final Attributes attributes;
/*     */ 
/*     */ 
/*     */   
/*     */   private MainAttributes() {
/*  50 */     this.attributes = new Attributes();
/*     */   }
/*     */   
/*     */   private MainAttributes(File paramFile) {
/*  54 */     this.attributes = getAttributes(paramFile);
/*     */   }
/*     */   
/*     */   public final String get(String paramString) {
/*  58 */     if (this.attributes != null) {
/*  59 */       return this.attributes.getValue(paramString);
/*     */     }
/*  61 */     return null;
/*     */   }
/*     */   
/*     */   private static Attributes getAttributes(File paramFile) {
/*  65 */     if (paramFile == null) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     JarFile jarFile = null;
/*     */     
/*  71 */     try { jarFile = new JarFile(paramFile);
/*  72 */       Manifest manifest = jarFile.getManifest();
/*  73 */       if (manifest != null) {
/*  74 */         return manifest.getMainAttributes();
/*     */       } }
/*  76 */     catch (IOException iOException)
/*     */     
/*     */     { 
/*     */       try {
/*  80 */         if (jarFile != null) {
/*  81 */           jarFile.close();
/*     */         }
/*  83 */       } catch (IOException iOException1) {} } finally { try { if (jarFile != null) jarFile.close();  } catch (IOException iOException) {} }
/*     */ 
/*     */ 
/*     */     
/*  87 */     return new Attributes();
/*     */   }
/*     */   
/*     */   public static MainAttributes of(File paramFile) {
/*  91 */     return of(paramFile.toURI());
/*     */   }
/*     */   
/*     */   public static MainAttributes of(URI paramURI) {
/*  95 */     MainAttributes mainAttributes = instances.get(paramURI);
/*  96 */     if (mainAttributes == null) {
/*  97 */       mainAttributes = new MainAttributes(new File(paramURI));
/*  98 */       instances.put(paramURI, mainAttributes);
/*     */     } 
/* 100 */     return mainAttributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\platform\MainAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */