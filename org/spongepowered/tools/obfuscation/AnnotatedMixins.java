/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.processing.Filer;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.gen.Accessor;
/*     */ import org.spongepowered.asm.mixin.gen.Invoker;
/*     */ import org.spongepowered.asm.util.ITokenProvider;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IJavadocProvider;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandleSimulated;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeReference;
/*     */ import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
/*     */ import org.spongepowered.tools.obfuscation.validation.ParentValidator;
/*     */ import org.spongepowered.tools.obfuscation.validation.TargetValidator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AnnotatedMixins
/*     */   implements ITokenProvider, IJavadocProvider, IMixinAnnotationProcessor, ITypeHandleProvider
/*     */ {
/*     */   private static final String MAPID_SYSTEM_PROPERTY = "mixin.target.mapid";
/*  89 */   private static Map<ProcessingEnvironment, AnnotatedMixins> instances = new HashMap<ProcessingEnvironment, AnnotatedMixins>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMixinAnnotationProcessor.CompilerEnvironment env;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ProcessingEnvironment processingEnv;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   private final Map<String, AnnotatedMixin> mixins = new HashMap<String, AnnotatedMixin>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private final List<AnnotatedMixin> mixinsForPass = new ArrayList<AnnotatedMixin>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IObfuscationManager obf;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<IMixinValidator> validators;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final Map<String, Integer> tokenCache = new HashMap<String, Integer>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final TargetMap targets;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AnnotatedMixins(ProcessingEnvironment paramProcessingEnvironment) {
/* 141 */     this.env = detectEnvironment(paramProcessingEnvironment);
/* 142 */     this.processingEnv = paramProcessingEnvironment;
/*     */     
/* 144 */     printMessage(Diagnostic.Kind.NOTE, "SpongePowered MIXIN Annotation Processor Version=0.7.11");
/*     */     
/* 146 */     this.targets = initTargetMap();
/* 147 */     this.obf = new ObfuscationManager(this);
/* 148 */     this.obf.init();
/*     */     
/* 150 */     this.validators = (List<IMixinValidator>)ImmutableList.of(new ParentValidator(this), new TargetValidator(this));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     initTokenCache(getOption("tokens"));
/*     */   }
/*     */   
/*     */   protected TargetMap initTargetMap() {
/* 159 */     TargetMap targetMap = TargetMap.create(System.getProperty("mixin.target.mapid"));
/* 160 */     System.setProperty("mixin.target.mapid", targetMap.getSessionId());
/* 161 */     String str = getOption("dependencyTargetsFile");
/* 162 */     if (str != null) {
/*     */       try {
/* 164 */         targetMap.readImports(new File(str));
/* 165 */       } catch (IOException iOException) {
/* 166 */         printMessage(Diagnostic.Kind.WARNING, "Could not read from specified imports file: " + str);
/*     */       } 
/*     */     }
/* 169 */     return targetMap;
/*     */   }
/*     */   
/*     */   private void initTokenCache(String paramString) {
/* 173 */     if (paramString != null) {
/* 174 */       Pattern pattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)=([0-9]+)$");
/*     */       
/* 176 */       String[] arrayOfString = paramString.replaceAll("\\s", "").toUpperCase().split("[;,]");
/* 177 */       for (String str : arrayOfString) {
/* 178 */         Matcher matcher = pattern.matcher(str);
/* 179 */         if (matcher.matches()) {
/* 180 */           this.tokenCache.put(matcher.group(1), Integer.valueOf(Integer.parseInt(matcher.group(2))));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ITypeHandleProvider getTypeProvider() {
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ITokenProvider getTokenProvider() {
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IObfuscationManager getObfuscationManager() {
/* 198 */     return this.obf;
/*     */   }
/*     */ 
/*     */   
/*     */   public IJavadocProvider getJavadocProvider() {
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProcessingEnvironment getProcessingEnvironment() {
/* 208 */     return this.processingEnv;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMixinAnnotationProcessor.CompilerEnvironment getCompilerEnvironment() {
/* 213 */     return this.env;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getToken(String paramString) {
/* 218 */     if (this.tokenCache.containsKey(paramString)) {
/* 219 */       return this.tokenCache.get(paramString);
/*     */     }
/*     */     
/* 222 */     String str = getOption(paramString);
/* 223 */     Integer integer = null;
/*     */     try {
/* 225 */       integer = Integer.valueOf(Integer.parseInt(str));
/* 226 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 230 */     this.tokenCache.put(paramString, integer);
/* 231 */     return integer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOption(String paramString) {
/* 236 */     if (paramString == null) {
/* 237 */       return null;
/*     */     }
/*     */     
/* 240 */     String str = this.processingEnv.getOptions().get(paramString);
/* 241 */     if (str != null) {
/* 242 */       return str;
/*     */     }
/*     */     
/* 245 */     return getProperties().getProperty(paramString);
/*     */   }
/*     */   
/*     */   public Properties getProperties() {
/* 249 */     if (this.properties == null) {
/* 250 */       this.properties = new Properties();
/*     */       
/*     */       try {
/* 253 */         Filer filer = this.processingEnv.getFiler();
/* 254 */         FileObject fileObject = filer.getResource(StandardLocation.SOURCE_PATH, "", "mixin.properties");
/* 255 */         if (fileObject != null) {
/* 256 */           InputStream inputStream = fileObject.openInputStream();
/* 257 */           this.properties.load(inputStream);
/* 258 */           inputStream.close();
/*     */         } 
/* 260 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 265 */     return this.properties;
/*     */   }
/*     */   
/*     */   private IMixinAnnotationProcessor.CompilerEnvironment detectEnvironment(ProcessingEnvironment paramProcessingEnvironment) {
/* 269 */     if (paramProcessingEnvironment.getClass().getName().contains("jdt")) {
/* 270 */       return IMixinAnnotationProcessor.CompilerEnvironment.JDT;
/*     */     }
/*     */     
/* 273 */     return IMixinAnnotationProcessor.CompilerEnvironment.JAVAC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeMappings() {
/* 280 */     this.obf.writeMappings();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeReferences() {
/* 287 */     this.obf.writeReferences();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 294 */     this.mixins.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMixin(TypeElement paramTypeElement) {
/* 301 */     String str = paramTypeElement.getQualifiedName().toString();
/*     */     
/* 303 */     if (!this.mixins.containsKey(str)) {
/* 304 */       AnnotatedMixin annotatedMixin = new AnnotatedMixin(this, paramTypeElement);
/* 305 */       this.targets.registerTargets(annotatedMixin);
/* 306 */       annotatedMixin.runValidators(IMixinValidator.ValidationPass.EARLY, this.validators);
/* 307 */       this.mixins.put(str, annotatedMixin);
/* 308 */       this.mixinsForPass.add(annotatedMixin);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMixin getMixin(TypeElement paramTypeElement) {
/* 316 */     return getMixin(paramTypeElement.getQualifiedName().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMixin getMixin(String paramString) {
/* 323 */     return this.mixins.get(paramString);
/*     */   }
/*     */   
/*     */   public Collection<TypeMirror> getMixinsTargeting(TypeMirror paramTypeMirror) {
/* 327 */     return getMixinsTargeting((TypeElement)((DeclaredType)paramTypeMirror).asElement());
/*     */   }
/*     */   
/*     */   public Collection<TypeMirror> getMixinsTargeting(TypeElement paramTypeElement) {
/* 331 */     ArrayList<TypeMirror> arrayList = new ArrayList();
/*     */     
/* 333 */     for (TypeReference typeReference : this.targets.getMixinsTargeting(paramTypeElement)) {
/* 334 */       TypeHandle typeHandle = typeReference.getHandle(this.processingEnv);
/* 335 */       if (typeHandle != null) {
/* 336 */         arrayList.add(typeHandle.getType());
/*     */       }
/*     */     } 
/*     */     
/* 340 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAccessor(TypeElement paramTypeElement, ExecutableElement paramExecutableElement) {
/* 350 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 351 */     if (annotatedMixin == null) {
/* 352 */       printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", paramExecutableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 356 */     AnnotationHandle annotationHandle = AnnotationHandle.of(paramExecutableElement, Accessor.class);
/* 357 */     annotatedMixin.registerAccessor(paramExecutableElement, annotationHandle, shouldRemap(annotatedMixin, annotationHandle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerInvoker(TypeElement paramTypeElement, ExecutableElement paramExecutableElement) {
/* 367 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 368 */     if (annotatedMixin == null) {
/* 369 */       printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", paramExecutableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 373 */     AnnotationHandle annotationHandle = AnnotationHandle.of(paramExecutableElement, Invoker.class);
/* 374 */     annotatedMixin.registerInvoker(paramExecutableElement, annotationHandle, shouldRemap(annotatedMixin, annotationHandle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerOverwrite(TypeElement paramTypeElement, ExecutableElement paramExecutableElement) {
/* 384 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 385 */     if (annotatedMixin == null) {
/* 386 */       printMessage(Diagnostic.Kind.ERROR, "Found @Overwrite annotation on a non-mixin method", paramExecutableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 390 */     AnnotationHandle annotationHandle = AnnotationHandle.of(paramExecutableElement, Overwrite.class);
/* 391 */     annotatedMixin.registerOverwrite(paramExecutableElement, annotationHandle, shouldRemap(annotatedMixin, annotationHandle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerShadow(TypeElement paramTypeElement, VariableElement paramVariableElement, AnnotationHandle paramAnnotationHandle) {
/* 402 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 403 */     if (annotatedMixin == null) {
/* 404 */       printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin field", paramVariableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 408 */     annotatedMixin.registerShadow(paramVariableElement, paramAnnotationHandle, shouldRemap(annotatedMixin, paramAnnotationHandle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerShadow(TypeElement paramTypeElement, ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle) {
/* 419 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 420 */     if (annotatedMixin == null) {
/* 421 */       printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin method", paramExecutableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 425 */     annotatedMixin.registerShadow(paramExecutableElement, paramAnnotationHandle, shouldRemap(annotatedMixin, paramAnnotationHandle));
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
/*     */   public void registerInjector(TypeElement paramTypeElement, ExecutableElement paramExecutableElement, AnnotationHandle paramAnnotationHandle) {
/* 437 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 438 */     if (annotatedMixin == null) {
/* 439 */       printMessage(Diagnostic.Kind.ERROR, "Found " + paramAnnotationHandle + " annotation on a non-mixin method", paramExecutableElement);
/*     */       
/*     */       return;
/*     */     } 
/* 443 */     InjectorRemap injectorRemap = new InjectorRemap(shouldRemap(annotatedMixin, paramAnnotationHandle));
/* 444 */     annotatedMixin.registerInjector(paramExecutableElement, paramAnnotationHandle, injectorRemap);
/* 445 */     injectorRemap.dispatchPendingMessages((Messager)this);
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
/*     */   public void registerSoftImplements(TypeElement paramTypeElement, AnnotationHandle paramAnnotationHandle) {
/* 457 */     AnnotatedMixin annotatedMixin = getMixin(paramTypeElement);
/* 458 */     if (annotatedMixin == null) {
/* 459 */       printMessage(Diagnostic.Kind.ERROR, "Found @Implements annotation on a non-mixin class");
/*     */       
/*     */       return;
/*     */     } 
/* 463 */     annotatedMixin.registerSoftImplements(paramAnnotationHandle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPassStarted() {
/* 471 */     this.mixinsForPass.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPassCompleted(RoundEnvironment paramRoundEnvironment) {
/* 478 */     if (!"true".equalsIgnoreCase(getOption("disableTargetExport"))) {
/* 479 */       this.targets.write(true);
/*     */     }
/*     */     
/* 482 */     for (AnnotatedMixin annotatedMixin : paramRoundEnvironment.processingOver() ? (Object<?>)this.mixins.values() : (Object<?>)this.mixinsForPass) {
/* 483 */       annotatedMixin.runValidators(paramRoundEnvironment.processingOver() ? IMixinValidator.ValidationPass.FINAL : IMixinValidator.ValidationPass.LATE, this.validators);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldRemap(AnnotatedMixin paramAnnotatedMixin, AnnotationHandle paramAnnotationHandle) {
/* 488 */     return paramAnnotationHandle.getBoolean("remap", paramAnnotatedMixin.remap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence) {
/* 496 */     if (this.env == IMixinAnnotationProcessor.CompilerEnvironment.JAVAC || paramKind != Diagnostic.Kind.NOTE) {
/* 497 */       this.processingEnv.getMessager().printMessage(paramKind, paramCharSequence);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement) {
/* 506 */     this.processingEnv.getMessager().printMessage(paramKind, paramCharSequence, paramElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror) {
/* 514 */     this.processingEnv.getMessager().printMessage(paramKind, paramCharSequence, paramElement, paramAnnotationMirror);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/* 522 */     this.processingEnv.getMessager().printMessage(paramKind, paramCharSequence, paramElement, paramAnnotationMirror, paramAnnotationValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getTypeHandle(String paramString) {
/* 531 */     paramString = paramString.replace('/', '.');
/*     */     
/* 533 */     Elements elements = this.processingEnv.getElementUtils();
/* 534 */     TypeElement typeElement = elements.getTypeElement(paramString);
/* 535 */     if (typeElement != null) {
/*     */       try {
/* 537 */         return new TypeHandle(typeElement);
/* 538 */       } catch (NullPointerException nullPointerException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 543 */     int i = paramString.lastIndexOf('.');
/* 544 */     if (i > -1) {
/* 545 */       String str = paramString.substring(0, i);
/* 546 */       PackageElement packageElement = elements.getPackageElement(str);
/* 547 */       if (packageElement != null) {
/* 548 */         return new TypeHandle(packageElement, paramString);
/*     */       }
/*     */     } 
/*     */     
/* 552 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeHandle getSimulatedHandle(String paramString, TypeMirror paramTypeMirror) {
/* 560 */     paramString = paramString.replace('/', '.');
/* 561 */     int i = paramString.lastIndexOf('.');
/* 562 */     if (i > -1) {
/* 563 */       String str = paramString.substring(0, i);
/* 564 */       PackageElement packageElement = this.processingEnv.getElementUtils().getPackageElement(str);
/* 565 */       if (packageElement != null) {
/* 566 */         return (TypeHandle)new TypeHandleSimulated(packageElement, paramString, paramTypeMirror);
/*     */       }
/*     */     } 
/*     */     
/* 570 */     return (TypeHandle)new TypeHandleSimulated(paramString, paramTypeMirror);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavadoc(Element paramElement) {
/* 579 */     Elements elements = this.processingEnv.getElementUtils();
/* 580 */     return elements.getDocComment(paramElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotatedMixins getMixinsForEnvironment(ProcessingEnvironment paramProcessingEnvironment) {
/* 587 */     AnnotatedMixins annotatedMixins = instances.get(paramProcessingEnvironment);
/* 588 */     if (annotatedMixins == null) {
/* 589 */       annotatedMixins = new AnnotatedMixins(paramProcessingEnvironment);
/* 590 */       instances.put(paramProcessingEnvironment, annotatedMixins);
/*     */     } 
/* 592 */     return annotatedMixins;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\AnnotatedMixins.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */