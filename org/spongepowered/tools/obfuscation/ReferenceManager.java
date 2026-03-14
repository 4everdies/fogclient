/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReferenceManager
/*     */   implements IReferenceManager
/*     */ {
/*     */   private final IMixinAnnotationProcessor ap;
/*     */   private final String outRefMapFileName;
/*     */   private final List<ObfuscationEnvironment> environments;
/*     */   
/*     */   public static class ReferenceConflictException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final String oldReference;
/*     */     private final String newReference;
/*     */     
/*     */     public ReferenceConflictException(String param1String1, String param1String2) {
/*  58 */       this.oldReference = param1String1;
/*  59 */       this.newReference = param1String2;
/*     */     }
/*     */     
/*     */     public String getOld() {
/*  63 */       return this.oldReference;
/*     */     }
/*     */     
/*     */     public String getNew() {
/*  67 */       return this.newReference;
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
/*  90 */   private final ReferenceMapper refMapper = new ReferenceMapper();
/*     */   
/*     */   private boolean allowConflicts;
/*     */   
/*     */   public ReferenceManager(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, List<ObfuscationEnvironment> paramList) {
/*  95 */     this.ap = paramIMixinAnnotationProcessor;
/*  96 */     this.environments = paramList;
/*  97 */     this.outRefMapFileName = this.ap.getOption("outRefMapFile");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAllowConflicts() {
/* 106 */     return this.allowConflicts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowConflicts(boolean paramBoolean) {
/* 115 */     this.allowConflicts = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write() {
/* 123 */     if (this.outRefMapFileName == null) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     PrintWriter printWriter = null;
/*     */     
/*     */     try {
/* 130 */       printWriter = newWriter(this.outRefMapFileName, "refmap");
/* 131 */       this.refMapper.write(printWriter);
/* 132 */     } catch (IOException iOException) {
/* 133 */       iOException.printStackTrace();
/*     */     } finally {
/* 135 */       if (printWriter != null) {
/*     */         try {
/* 137 */           printWriter.close();
/* 138 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PrintWriter newWriter(String paramString1, String paramString2) throws IOException {
/* 149 */     if (paramString1.matches("^.*[\\\\/:].*$")) {
/* 150 */       File file = new File(paramString1);
/* 151 */       file.getParentFile().mkdirs();
/* 152 */       this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + paramString2 + " to " + file.getAbsolutePath());
/* 153 */       return new PrintWriter(file);
/*     */     } 
/*     */     
/* 156 */     FileObject fileObject = this.ap.getProcessingEnvironment().getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", paramString1, new javax.lang.model.element.Element[0]);
/* 157 */     this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + paramString2 + " to " + (new File(fileObject.toUri())).getAbsolutePath());
/* 158 */     return new PrintWriter(fileObject.openWriter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceMapper getMapper() {
/* 167 */     return this.refMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethodMapping(String paramString1, String paramString2, ObfuscationData<MappingMethod> paramObfuscationData) {
/* 177 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 178 */       MappingMethod mappingMethod = paramObfuscationData.get(obfuscationEnvironment.getType());
/* 179 */       if (mappingMethod != null) {
/* 180 */         MemberInfo memberInfo = new MemberInfo((IMapping)mappingMethod);
/* 181 */         addMapping(obfuscationEnvironment.getType(), paramString1, paramString2, memberInfo.toString());
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
/*     */   public void addMethodMapping(String paramString1, String paramString2, MemberInfo paramMemberInfo, ObfuscationData<MappingMethod> paramObfuscationData) {
/* 194 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 195 */       MappingMethod mappingMethod = paramObfuscationData.get(obfuscationEnvironment.getType());
/* 196 */       if (mappingMethod != null) {
/* 197 */         MemberInfo memberInfo = paramMemberInfo.remapUsing(mappingMethod, true);
/* 198 */         addMapping(obfuscationEnvironment.getType(), paramString1, paramString2, memberInfo.toString());
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
/*     */   public void addFieldMapping(String paramString1, String paramString2, MemberInfo paramMemberInfo, ObfuscationData<MappingField> paramObfuscationData) {
/* 211 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 212 */       MappingField mappingField = paramObfuscationData.get(obfuscationEnvironment.getType());
/* 213 */       if (mappingField != null) {
/* 214 */         MemberInfo memberInfo = MemberInfo.fromMapping((IMapping)mappingField.transform(obfuscationEnvironment.remapDescriptor(paramMemberInfo.desc)));
/* 215 */         addMapping(obfuscationEnvironment.getType(), paramString1, paramString2, memberInfo.toString());
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
/*     */   public void addClassMapping(String paramString1, String paramString2, ObfuscationData<String> paramObfuscationData) {
/* 227 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 228 */       String str = paramObfuscationData.get(obfuscationEnvironment.getType());
/* 229 */       if (str != null) {
/* 230 */         addMapping(obfuscationEnvironment.getType(), paramString1, paramString2, str);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addMapping(ObfuscationType paramObfuscationType, String paramString1, String paramString2, String paramString3) {
/* 236 */     String str = this.refMapper.addMapping(paramObfuscationType.getKey(), paramString1, paramString2, paramString3);
/* 237 */     if (paramObfuscationType.isDefault()) {
/* 238 */       this.refMapper.addMapping(null, paramString1, paramString2, paramString3);
/*     */     }
/*     */     
/* 241 */     if (!this.allowConflicts && str != null && !str.equals(paramString3))
/* 242 */       throw new ReferenceConflictException(str, paramString3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\ReferenceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */