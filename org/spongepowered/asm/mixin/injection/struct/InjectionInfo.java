/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.Dynamic;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyArgs;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyConstant;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.code.ISliceContext;
/*     */ import org.spongepowered.asm.mixin.injection.code.Injector;
/*     */ import org.spongepowered.asm.mixin.injection.code.InjectorTarget;
/*     */ import org.spongepowered.asm.mixin.injection.code.MethodSlice;
/*     */ import org.spongepowered.asm.mixin.injection.code.MethodSlices;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
/*     */ import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class InjectionInfo
/*     */   extends SpecialMethodInfo
/*     */   implements ISliceContext
/*     */ {
/*     */   protected final boolean isStatic;
/*  85 */   protected final Deque<MethodNode> targets = new ArrayDeque<MethodNode>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MethodSlices slices;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String atKey;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected final List<InjectionPoint> injectionPoints = new ArrayList<InjectionPoint>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected final Map<Target, List<InjectionNodes.InjectionNode>> targetNodes = new LinkedHashMap<Target, List<InjectionNodes.InjectionNode>>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Injector injector;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectorGroupInfo group;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private final List<MethodNode> injectedMethods = new ArrayList<MethodNode>(0);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private int expectedCallbackCount = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private int requiredCallbackCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private int maxCallbackCount = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private int injectedCallbackCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode) {
/* 151 */     this(paramMixinTargetContext, paramMethodNode, paramAnnotationNode, "at");
/*     */   }
/*     */   
/*     */   protected InjectionInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, String paramString) {
/* 155 */     super(paramMixinTargetContext, paramMethodNode, paramAnnotationNode);
/* 156 */     this.isStatic = Bytecode.methodIsStatic(paramMethodNode);
/* 157 */     this.slices = MethodSlices.parse(this);
/* 158 */     this.atKey = paramString;
/* 159 */     readAnnotation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAnnotation() {
/* 166 */     if (this.annotation == null) {
/*     */       return;
/*     */     }
/*     */     
/* 170 */     String str = "@" + Bytecode.getSimpleName(this.annotation);
/* 171 */     List<AnnotationNode> list = readInjectionPoints(str);
/* 172 */     findMethods(parseTargets(str), str);
/* 173 */     parseInjectionPoints(list);
/* 174 */     parseRequirements();
/* 175 */     this.injector = parseInjector(this.annotation);
/*     */   }
/*     */   
/*     */   protected Set<MemberInfo> parseTargets(String paramString) {
/* 179 */     List list = Annotations.getValue(this.annotation, "method", false);
/* 180 */     if (list == null) {
/* 181 */       throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing method name", new Object[] { paramString, this.method.name }));
/*     */     }
/*     */     
/* 184 */     LinkedHashSet<MemberInfo> linkedHashSet = new LinkedHashSet();
/* 185 */     for (String str : list) {
/*     */       try {
/* 187 */         MemberInfo memberInfo = MemberInfo.parseAndValidate(str, (IMixinContext)this.mixin);
/* 188 */         if (memberInfo.owner != null && !memberInfo.owner.equals(this.mixin.getTargetClassRef())) {
/* 189 */           throw new InvalidInjectionException(this, 
/* 190 */               String.format("%s annotation on %s specifies a target class '%s', which is not supported", new Object[] { paramString, this.method.name, memberInfo.owner }));
/*     */         }
/*     */         
/* 193 */         linkedHashSet.add(memberInfo);
/* 194 */       } catch (InvalidMemberDescriptorException invalidMemberDescriptorException) {
/* 195 */         throw new InvalidInjectionException(this, String.format("%s annotation on %s, has invalid target descriptor: \"%s\". %s", new Object[] { paramString, this.method.name, str, this.mixin
/* 196 */                 .getReferenceMapper().getStatus() }));
/*     */       } 
/*     */     } 
/* 199 */     return linkedHashSet;
/*     */   }
/*     */   
/*     */   protected List<AnnotationNode> readInjectionPoints(String paramString) {
/* 203 */     List<AnnotationNode> list = Annotations.getValue(this.annotation, this.atKey, false);
/* 204 */     if (list == null) {
/* 205 */       throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing '%s' value(s)", new Object[] { paramString, this.method.name, this.atKey }));
/*     */     }
/*     */     
/* 208 */     return list;
/*     */   }
/*     */   
/*     */   protected void parseInjectionPoints(List<AnnotationNode> paramList) {
/* 212 */     this.injectionPoints.addAll(InjectionPoint.parse((IMixinContext)this.mixin, this.method, this.annotation, paramList));
/*     */   }
/*     */   
/*     */   protected void parseRequirements() {
/* 216 */     this.group = this.mixin.getInjectorGroups().parseGroup(this.method, this.mixin.getDefaultInjectorGroup()).add(this);
/*     */     
/* 218 */     Integer integer1 = (Integer)Annotations.getValue(this.annotation, "expect");
/* 219 */     if (integer1 != null) {
/* 220 */       this.expectedCallbackCount = integer1.intValue();
/*     */     }
/*     */     
/* 223 */     Integer integer2 = (Integer)Annotations.getValue(this.annotation, "require");
/* 224 */     if (integer2 != null && integer2.intValue() > -1) {
/* 225 */       this.requiredCallbackCount = integer2.intValue();
/* 226 */     } else if (this.group.isDefault()) {
/* 227 */       this.requiredCallbackCount = this.mixin.getDefaultRequiredInjections();
/*     */     } 
/*     */     
/* 230 */     Integer integer3 = (Integer)Annotations.getValue(this.annotation, "allow");
/* 231 */     if (integer3 != null) {
/* 232 */       this.maxCallbackCount = Math.max(Math.max(this.requiredCallbackCount, 1), integer3.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Injector parseInjector(AnnotationNode paramAnnotationNode);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 246 */     return (this.targets.size() > 0 && this.injectionPoints.size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 253 */     this.targetNodes.clear();
/* 254 */     for (MethodNode methodNode : this.targets) {
/* 255 */       Target target = this.mixin.getTargetMethod(methodNode);
/* 256 */       InjectorTarget injectorTarget = new InjectorTarget(this, target);
/* 257 */       this.targetNodes.put(target, this.injector.find(injectorTarget, this.injectionPoints));
/* 258 */       injectorTarget.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inject() {
/* 266 */     for (Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : this.targetNodes.entrySet()) {
/* 267 */       this.injector.inject((Target)entry.getKey(), (List)entry.getValue());
/*     */     }
/* 269 */     this.targets.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postInject() {
/* 276 */     for (MethodNode methodNode : this.injectedMethods) {
/* 277 */       this.classNode.methods.add(methodNode);
/*     */     }
/*     */     
/* 280 */     String str1 = getDescription();
/* 281 */     String str2 = this.mixin.getReferenceMapper().getStatus();
/* 282 */     String str3 = getDynamicInfo();
/* 283 */     if (this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_INJECTORS) && this.injectedCallbackCount < this.expectedCallbackCount)
/* 284 */       throw new InvalidInjectionException(this, 
/* 285 */           String.format("Injection validation failed: %s %s%s in %s expected %d invocation(s) but %d succeeded. %s%s", new Object[] {
/* 286 */               str1, this.method.name, this.method.desc, this.mixin, Integer.valueOf(this.expectedCallbackCount), Integer.valueOf(this.injectedCallbackCount), str2, str3
/*     */             })); 
/* 288 */     if (this.injectedCallbackCount < this.requiredCallbackCount)
/* 289 */       throw new InjectionError(
/* 290 */           String.format("Critical injection failure: %s %s%s in %s failed injection check, (%d/%d) succeeded. %s%s", new Object[] {
/* 291 */               str1, this.method.name, this.method.desc, this.mixin, Integer.valueOf(this.injectedCallbackCount), Integer.valueOf(this.requiredCallbackCount), str2, str3
/*     */             })); 
/* 293 */     if (this.injectedCallbackCount > this.maxCallbackCount) {
/* 294 */       throw new InjectionError(
/* 295 */           String.format("Critical injection failure: %s %s%s in %s failed injection check, %d succeeded of %d allowed.%s", new Object[] {
/* 296 */               str1, this.method.name, this.method.desc, this.mixin, Integer.valueOf(this.injectedCallbackCount), Integer.valueOf(this.maxCallbackCount), str3
/*     */             }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyInjected(Target paramTarget) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDescription() {
/* 312 */     return "Callback method";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 317 */     return describeInjector((IMixinContext)this.mixin, this.annotation, this.method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<MethodNode> getTargets() {
/* 326 */     return this.targets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodSlice getSlice(String paramString) {
/* 334 */     return this.slices.get(getSliceId(paramString));
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
/*     */   public String getSliceId(String paramString) {
/* 346 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInjectedCallbackCount() {
/* 355 */     return this.injectedCallbackCount;
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
/*     */   public MethodNode addMethod(int paramInt, String paramString1, String paramString2) {
/* 368 */     MethodNode methodNode = new MethodNode(327680, paramInt | 0x1000, paramString1, paramString2, null, null);
/* 369 */     this.injectedMethods.add(methodNode);
/* 370 */     return methodNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallbackInvocation(MethodNode paramMethodNode) {
/* 379 */     this.injectedCallbackCount++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findMethods(Set<MemberInfo> paramSet, String paramString) {
/* 389 */     this.targets.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     byte b = this.mixin.getEnvironment().getOption(MixinEnvironment.Option.REFMAP_REMAP) ? 2 : 1;
/*     */     
/* 397 */     for (MemberInfo memberInfo : paramSet) {
/* 398 */       for (byte b1 = 0, b2 = 0; b2 < b && b1 < 1; b2++) {
/* 399 */         byte b3 = 0;
/* 400 */         for (MethodNode methodNode : this.classNode.methods) {
/* 401 */           if (memberInfo.matches(methodNode.name, methodNode.desc, b3)) {
/* 402 */             boolean bool = (Annotations.getVisible(methodNode, MixinMerged.class) != null) ? true : false;
/* 403 */             if (memberInfo.matchAll && (Bytecode.methodIsStatic(methodNode) != this.isStatic || methodNode == this.method || bool)) {
/*     */               continue;
/*     */             }
/*     */             
/* 407 */             checkTarget(methodNode);
/* 408 */             this.targets.add(methodNode);
/* 409 */             b3++;
/* 410 */             b1++;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 415 */         memberInfo = memberInfo.transform(null);
/*     */       } 
/*     */     } 
/*     */     
/* 419 */     if (this.targets.size() == 0)
/* 420 */       throw new InvalidInjectionException(this, 
/* 421 */           String.format("%s annotation on %s could not find any targets matching %s in the target class %s. %s%s", new Object[] {
/* 422 */               paramString, this.method.name, namesOf(paramSet), this.mixin.getTarget(), this.mixin
/* 423 */               .getReferenceMapper().getStatus(), getDynamicInfo()
/*     */             })); 
/*     */   }
/*     */   
/*     */   private void checkTarget(MethodNode paramMethodNode) {
/* 428 */     AnnotationNode annotationNode = Annotations.getVisible(paramMethodNode, MixinMerged.class);
/* 429 */     if (annotationNode == null) {
/*     */       return;
/*     */     }
/*     */     
/* 433 */     if (Annotations.getVisible(paramMethodNode, Final.class) != null) {
/* 434 */       throw new InvalidInjectionException(this, String.format("%s cannot inject into @Final method %s::%s%s merged by %s", new Object[] { this, this.classNode.name, paramMethodNode.name, paramMethodNode.desc, 
/* 435 */               Annotations.getValue(annotationNode, "mixin") }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDynamicInfo() {
/* 446 */     AnnotationNode annotationNode = Annotations.getInvisible(this.method, Dynamic.class);
/* 447 */     String str = Strings.nullToEmpty((String)Annotations.getValue(annotationNode));
/* 448 */     Type type = (Type)Annotations.getValue(annotationNode, "mixin");
/* 449 */     if (type != null) {
/* 450 */       str = String.format("{%s} %s", new Object[] { type.getClassName(), str }).trim();
/*     */     }
/* 452 */     return (str.length() > 0) ? String.format(" Method is @Dynamic(%s)", new Object[] { str }) : "";
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
/*     */   public static InjectionInfo parse(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/* 465 */     AnnotationNode annotationNode = getInjectorAnnotation(paramMixinTargetContext.getMixin(), paramMethodNode);
/*     */     
/* 467 */     if (annotationNode == null) {
/* 468 */       return null;
/*     */     }
/*     */     
/* 471 */     if (annotationNode.desc.endsWith(Inject.class.getSimpleName() + ";"))
/* 472 */       return new CallbackInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode); 
/* 473 */     if (annotationNode.desc.endsWith(ModifyArg.class.getSimpleName() + ";"))
/* 474 */       return new ModifyArgInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode); 
/* 475 */     if (annotationNode.desc.endsWith(ModifyArgs.class.getSimpleName() + ";"))
/* 476 */       return new ModifyArgsInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode); 
/* 477 */     if (annotationNode.desc.endsWith(Redirect.class.getSimpleName() + ";"))
/* 478 */       return new RedirectInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode); 
/* 479 */     if (annotationNode.desc.endsWith(ModifyVariable.class.getSimpleName() + ";"))
/* 480 */       return new ModifyVariableInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode); 
/* 481 */     if (annotationNode.desc.endsWith(ModifyConstant.class.getSimpleName() + ";")) {
/* 482 */       return new ModifyConstantInjectionInfo(paramMixinTargetContext, paramMethodNode, annotationNode);
/*     */     }
/*     */     
/* 485 */     return null;
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
/*     */   public static AnnotationNode getInjectorAnnotation(IMixinInfo paramIMixinInfo, MethodNode paramMethodNode) {
/* 499 */     AnnotationNode annotationNode = null;
/*     */     try {
/* 501 */       annotationNode = Annotations.getSingleVisible(paramMethodNode, new Class[] { Inject.class, ModifyArg.class, ModifyArgs.class, Redirect.class, ModifyVariable.class, ModifyConstant.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 509 */     catch (IllegalArgumentException illegalArgumentException) {
/* 510 */       throw new InvalidMixinException(paramIMixinInfo, String.format("Error parsing annotations on %s in %s: %s", new Object[] { paramMethodNode.name, paramIMixinInfo.getClassName(), illegalArgumentException
/* 511 */               .getMessage() }));
/*     */     } 
/* 513 */     return annotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInjectorPrefix(AnnotationNode paramAnnotationNode) {
/* 523 */     if (paramAnnotationNode != null) {
/* 524 */       if (paramAnnotationNode.desc.endsWith(ModifyArg.class.getSimpleName() + ";"))
/* 525 */         return "modify"; 
/* 526 */       if (paramAnnotationNode.desc.endsWith(ModifyArgs.class.getSimpleName() + ";"))
/* 527 */         return "args"; 
/* 528 */       if (paramAnnotationNode.desc.endsWith(Redirect.class.getSimpleName() + ";"))
/* 529 */         return "redirect"; 
/* 530 */       if (paramAnnotationNode.desc.endsWith(ModifyVariable.class.getSimpleName() + ";"))
/* 531 */         return "localvar"; 
/* 532 */       if (paramAnnotationNode.desc.endsWith(ModifyConstant.class.getSimpleName() + ";")) {
/* 533 */         return "constant";
/*     */       }
/*     */     } 
/* 536 */     return "handler";
/*     */   }
/*     */   
/*     */   static String describeInjector(IMixinContext paramIMixinContext, AnnotationNode paramAnnotationNode, MethodNode paramMethodNode) {
/* 540 */     return String.format("%s->@%s::%s%s", new Object[] { paramIMixinContext.toString(), Bytecode.getSimpleName(paramAnnotationNode), paramMethodNode.name, paramMethodNode.desc });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String namesOf(Collection<MemberInfo> paramCollection) {
/* 550 */     byte b = 0; int i = paramCollection.size();
/* 551 */     StringBuilder stringBuilder = new StringBuilder();
/* 552 */     for (MemberInfo memberInfo : paramCollection) {
/* 553 */       if (b) {
/* 554 */         if (b == i - 1) {
/* 555 */           stringBuilder.append(" or ");
/*     */         } else {
/* 557 */           stringBuilder.append(", ");
/*     */         } 
/*     */       }
/* 560 */       stringBuilder.append('\'').append(memberInfo.name).append('\'');
/* 561 */       b++;
/*     */     } 
/* 563 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\InjectionInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */