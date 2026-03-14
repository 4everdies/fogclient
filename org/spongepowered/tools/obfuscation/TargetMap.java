/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import com.google.common.io.Files;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TargetMap
/*     */   extends HashMap<TypeReference, Set<TypeReference>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String sessionId;
/*     */   
/*     */   private TargetMap() {
/*  65 */     this(String.valueOf(System.currentTimeMillis()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TargetMap(String paramString) {
/*  74 */     this.sessionId = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  81 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerTargets(AnnotatedMixin paramAnnotatedMixin) {
/*  90 */     registerTargets(paramAnnotatedMixin.getTargets(), paramAnnotatedMixin.getHandle());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerTargets(List<TypeHandle> paramList, TypeHandle paramTypeHandle) {
/* 100 */     for (TypeHandle typeHandle : paramList) {
/* 101 */       addMixin(typeHandle, paramTypeHandle);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMixin(TypeHandle paramTypeHandle1, TypeHandle paramTypeHandle2) {
/* 112 */     addMixin(paramTypeHandle1.getReference(), paramTypeHandle2.getReference());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMixin(String paramString1, String paramString2) {
/* 122 */     addMixin(new TypeReference(paramString1), new TypeReference(paramString2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMixin(TypeReference paramTypeReference1, TypeReference paramTypeReference2) {
/* 132 */     Set<TypeReference> set = getMixinsFor(paramTypeReference1);
/* 133 */     set.add(paramTypeReference2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<TypeReference> getMixinsTargeting(TypeElement paramTypeElement) {
/* 143 */     return getMixinsTargeting(new TypeHandle(paramTypeElement));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<TypeReference> getMixinsTargeting(TypeHandle paramTypeHandle) {
/* 153 */     return getMixinsTargeting(paramTypeHandle.getReference());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<TypeReference> getMixinsTargeting(TypeReference paramTypeReference) {
/* 163 */     return Collections.unmodifiableCollection(getMixinsFor(paramTypeReference));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<TypeReference> getMixinsFor(TypeReference paramTypeReference) {
/* 173 */     Set<TypeReference> set = get(paramTypeReference);
/* 174 */     if (set == null) {
/* 175 */       set = new HashSet<TypeReference>();
/* 176 */       put(paramTypeReference, set);
/*     */     } 
/* 178 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readImports(File paramFile) throws IOException {
/* 188 */     if (!paramFile.isFile()) {
/*     */       return;
/*     */     }
/*     */     
/* 192 */     for (String str : Files.readLines(paramFile, Charset.defaultCharset())) {
/* 193 */       String[] arrayOfString = str.split("\t");
/* 194 */       if (arrayOfString.length == 2) {
/* 195 */         addMixin(arrayOfString[1], arrayOfString[0]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(boolean paramBoolean) {
/* 206 */     ObjectOutputStream objectOutputStream = null;
/* 207 */     FileOutputStream fileOutputStream = null;
/*     */     try {
/* 209 */       File file = getSessionFile(this.sessionId);
/* 210 */       if (paramBoolean) {
/* 211 */         file.deleteOnExit();
/*     */       }
/* 213 */       fileOutputStream = new FileOutputStream(file, true);
/* 214 */       objectOutputStream = new ObjectOutputStream(fileOutputStream);
/* 215 */       objectOutputStream.writeObject(this);
/* 216 */     } catch (Exception exception) {
/* 217 */       exception.printStackTrace();
/*     */     } finally {
/* 219 */       if (objectOutputStream != null) {
/*     */         try {
/* 221 */           objectOutputStream.close();
/* 222 */         } catch (IOException iOException) {
/* 223 */           iOException.printStackTrace();
/*     */         } 
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
/*     */   private static TargetMap read(File paramFile) {
/* 236 */     ObjectInputStream objectInputStream = null;
/* 237 */     FileInputStream fileInputStream = null;
/*     */     try {
/* 239 */       fileInputStream = new FileInputStream(paramFile);
/* 240 */       objectInputStream = new ObjectInputStream(fileInputStream);
/* 241 */       return (TargetMap)objectInputStream.readObject();
/* 242 */     } catch (Exception exception) {
/* 243 */       exception.printStackTrace();
/*     */     } finally {
/* 245 */       if (objectInputStream != null) {
/*     */         try {
/* 247 */           objectInputStream.close();
/* 248 */         } catch (IOException iOException) {
/* 249 */           iOException.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/* 253 */     return null;
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
/*     */   public static TargetMap create(String paramString) {
/* 266 */     if (paramString != null) {
/* 267 */       File file = getSessionFile(paramString);
/* 268 */       if (file.exists()) {
/* 269 */         TargetMap targetMap = read(file);
/* 270 */         if (targetMap != null) {
/* 271 */           return targetMap;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     return new TargetMap();
/*     */   }
/*     */   
/*     */   private static File getSessionFile(String paramString) {
/* 280 */     File file = new File(System.getProperty("java.io.tmpdir"));
/* 281 */     return new File(file, String.format("mixin-targetdb-%s.tmp", new Object[] { paramString }));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\TargetMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */