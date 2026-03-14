/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.processing.Filer;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.asm.util.ObfuscationUtil;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationEnvironment;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ObfuscationEnvironment
/*     */   implements IObfuscationEnvironment
/*     */ {
/*     */   protected final ObfuscationType type;
/*     */   protected final IMappingProvider mappingProvider;
/*     */   protected final IMappingWriter mappingWriter;
/*     */   
/*     */   final class RemapperProxy
/*     */     implements ObfuscationUtil.IClassRemapper
/*     */   {
/*     */     public String map(String param1String) {
/*  69 */       if (ObfuscationEnvironment.this.mappingProvider == null) {
/*  70 */         return null;
/*     */       }
/*  72 */       return ObfuscationEnvironment.this.mappingProvider.getClassMapping(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public String unmap(String param1String) {
/*  77 */       if (ObfuscationEnvironment.this.mappingProvider == null) {
/*  78 */         return null;
/*     */       }
/*  80 */       return ObfuscationEnvironment.this.mappingProvider.getClassMapping(param1String);
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
/*  97 */   protected final RemapperProxy remapper = new RemapperProxy();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final IMixinAnnotationProcessor ap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String outFileName;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<String> inFileNames;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initDone;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObfuscationEnvironment(ObfuscationType paramObfuscationType) {
/* 121 */     this.type = paramObfuscationType;
/* 122 */     this.ap = paramObfuscationType.getAnnotationProcessor();
/*     */     
/* 124 */     this.inFileNames = paramObfuscationType.getInputFileNames();
/* 125 */     this.outFileName = paramObfuscationType.getOutputFileName();
/*     */     
/* 127 */     this.mappingProvider = getMappingProvider((Messager)this.ap, this.ap.getProcessingEnvironment().getFiler());
/* 128 */     this.mappingWriter = getMappingWriter((Messager)this.ap, this.ap.getProcessingEnvironment().getFiler());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return this.type.toString();
/*     */   }
/*     */   
/*     */   protected abstract IMappingProvider getMappingProvider(Messager paramMessager, Filer paramFiler);
/*     */   
/*     */   protected abstract IMappingWriter getMappingWriter(Messager paramMessager, Filer paramFiler);
/*     */   
/*     */   private boolean initMappings() {
/* 141 */     if (!this.initDone) {
/* 142 */       this.initDone = true;
/*     */       
/* 144 */       if (this.inFileNames == null) {
/* 145 */         this.ap.printMessage(Diagnostic.Kind.ERROR, "The " + this.type.getConfig().getInputFileOption() + " argument was not supplied, obfuscation processing will not occur");
/*     */         
/* 147 */         return false;
/*     */       } 
/*     */       
/* 150 */       byte b = 0;
/*     */       
/* 152 */       for (String str : this.inFileNames) {
/* 153 */         File file = new File(str);
/*     */         try {
/* 155 */           if (file.isFile()) {
/* 156 */             this.ap.printMessage(Diagnostic.Kind.NOTE, "Loading " + this.type + " mappings from " + file.getAbsolutePath());
/* 157 */             this.mappingProvider.read(file);
/* 158 */             b++;
/*     */           } 
/* 160 */         } catch (Exception exception) {
/* 161 */           exception.printStackTrace();
/*     */         } 
/*     */       } 
/*     */       
/* 165 */       if (b < 1) {
/* 166 */         this.ap.printMessage(Diagnostic.Kind.ERROR, "No valid input files for " + this.type + " could be read, processing may not be sucessful.");
/* 167 */         this.mappingProvider.clear();
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return !this.mappingProvider.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationType getType() {
/* 178 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod getObfMethod(MemberInfo paramMemberInfo) {
/* 186 */     MappingMethod mappingMethod = getObfMethod(paramMemberInfo.asMethodMapping());
/* 187 */     if (mappingMethod != null || !paramMemberInfo.isFullyQualified()) {
/* 188 */       return mappingMethod;
/*     */     }
/*     */ 
/*     */     
/* 192 */     TypeHandle typeHandle = this.ap.getTypeProvider().getTypeHandle(paramMemberInfo.owner);
/* 193 */     if (typeHandle == null || typeHandle.isImaginary()) {
/* 194 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 198 */     TypeMirror typeMirror = typeHandle.getElement().getSuperclass();
/* 199 */     if (typeMirror.getKind() != TypeKind.DECLARED) {
/* 200 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 204 */     String str = ((TypeElement)((DeclaredType)typeMirror).asElement()).getQualifiedName().toString();
/* 205 */     return getObfMethod(new MemberInfo(paramMemberInfo.name, str.replace('.', '/'), paramMemberInfo.desc, paramMemberInfo.matchAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod getObfMethod(MappingMethod paramMappingMethod) {
/* 213 */     return getObfMethod(paramMappingMethod, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod getObfMethod(MappingMethod paramMappingMethod, boolean paramBoolean) {
/* 221 */     if (initMappings()) {
/* 222 */       boolean bool = true;
/* 223 */       MappingMethod mappingMethod1 = null;
/* 224 */       for (MappingMethod mappingMethod2 = paramMappingMethod; mappingMethod2 != null && mappingMethod1 == null; mappingMethod2 = mappingMethod2.getSuper()) {
/* 225 */         mappingMethod1 = this.mappingProvider.getMethodMapping(mappingMethod2);
/*     */       }
/*     */ 
/*     */       
/* 229 */       if (mappingMethod1 == null) {
/* 230 */         if (paramBoolean) {
/* 231 */           return null;
/*     */         }
/* 233 */         mappingMethod1 = paramMappingMethod.copy();
/* 234 */         bool = false;
/*     */       } 
/* 236 */       String str1 = getObfClass(mappingMethod1.getOwner());
/* 237 */       if (str1 == null || str1.equals(paramMappingMethod.getOwner()) || str1.equals(mappingMethod1.getOwner())) {
/* 238 */         return bool ? mappingMethod1 : null;
/*     */       }
/* 240 */       if (bool) {
/* 241 */         return mappingMethod1.move(str1);
/*     */       }
/* 243 */       String str2 = ObfuscationUtil.mapDescriptor(mappingMethod1.getDesc(), this.remapper);
/* 244 */       return new MappingMethod(str1, mappingMethod1.getSimpleName(), str2);
/*     */     } 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo remapDescriptor(MemberInfo paramMemberInfo) {
/* 257 */     boolean bool = false;
/*     */     
/* 259 */     String str1 = paramMemberInfo.owner;
/* 260 */     if (str1 != null) {
/* 261 */       String str = this.remapper.map(str1);
/* 262 */       if (str != null) {
/* 263 */         str1 = str;
/* 264 */         bool = true;
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     String str2 = paramMemberInfo.desc;
/* 269 */     if (str2 != null) {
/* 270 */       String str = ObfuscationUtil.mapDescriptor(paramMemberInfo.desc, this.remapper);
/* 271 */       if (!str.equals(paramMemberInfo.desc)) {
/* 272 */         str2 = str;
/* 273 */         bool = true;
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     return bool ? new MemberInfo(paramMemberInfo.name, str1, str2, paramMemberInfo.matchAll) : null;
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
/*     */   public String remapDescriptor(String paramString) {
/* 289 */     return ObfuscationUtil.mapDescriptor(paramString, this.remapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingField getObfField(MemberInfo paramMemberInfo) {
/* 297 */     return getObfField(paramMemberInfo.asFieldMapping(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingField getObfField(MappingField paramMappingField) {
/* 305 */     return getObfField(paramMappingField, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingField getObfField(MappingField paramMappingField, boolean paramBoolean) {
/* 313 */     if (!initMappings()) {
/* 314 */       return null;
/*     */     }
/*     */     
/* 317 */     MappingField mappingField = this.mappingProvider.getFieldMapping(paramMappingField);
/*     */     
/* 319 */     if (mappingField == null) {
/* 320 */       if (paramBoolean) {
/* 321 */         return null;
/*     */       }
/* 323 */       mappingField = paramMappingField;
/*     */     } 
/* 325 */     String str = getObfClass(mappingField.getOwner());
/* 326 */     if (str == null || str.equals(paramMappingField.getOwner()) || str.equals(mappingField.getOwner())) {
/* 327 */       return (mappingField != paramMappingField) ? mappingField : null;
/*     */     }
/* 329 */     return mappingField.move(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObfClass(String paramString) {
/* 337 */     if (!initMappings()) {
/* 338 */       return null;
/*     */     }
/* 340 */     return this.mappingProvider.getClassMapping(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeMappings(Collection<IMappingConsumer> paramCollection) {
/* 348 */     IMappingConsumer.MappingSet mappingSet1 = new IMappingConsumer.MappingSet();
/* 349 */     IMappingConsumer.MappingSet mappingSet2 = new IMappingConsumer.MappingSet();
/*     */     
/* 351 */     for (IMappingConsumer iMappingConsumer : paramCollection) {
/* 352 */       mappingSet1.addAll((Collection)iMappingConsumer.getFieldMappings(this.type));
/* 353 */       mappingSet2.addAll((Collection)iMappingConsumer.getMethodMappings(this.type));
/*     */     } 
/*     */     
/* 356 */     this.mappingWriter.write(this.outFileName, this.type, mappingSet1, mappingSet2);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\ObfuscationEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */