/*     */ package org.spongepowered.asm.mixin.injection;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.modify.AfterStoreLocal;
/*     */ import org.spongepowered.asm.mixin.injection.modify.BeforeLoadLocal;
/*     */ import org.spongepowered.asm.mixin.injection.points.AfterInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeFinalReturn;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeNew;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeStringInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.JumpInsnPoint;
/*     */ import org.spongepowered.asm.mixin.injection.points.MethodHead;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class InjectionPoint
/*     */ {
/*     */   public static final int DEFAULT_ALLOWED_SHIFT_BY = 0;
/*     */   public static final int MAX_ALLOWED_SHIFT_BY = 5;
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.TYPE})
/*     */   public static @interface AtCode
/*     */   {
/*     */     String value();
/*     */   }
/*     */   
/*     */   public enum Selector
/*     */   {
/* 112 */     FIRST,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     LAST,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     ONE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     public static final Selector DEFAULT = FIRST;
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   enum ShiftByViolationBehaviour
/*     */   {
/* 142 */     IGNORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     WARN,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     ERROR;
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
/* 170 */   private static Map<String, Class<? extends InjectionPoint>> types = new HashMap<String, Class<? extends InjectionPoint>>();
/*     */   private final String slice;
/*     */   
/*     */   static {
/* 174 */     register((Class)BeforeFieldAccess.class);
/* 175 */     register((Class)BeforeInvoke.class);
/* 176 */     register((Class)BeforeNew.class);
/* 177 */     register((Class)BeforeReturn.class);
/* 178 */     register((Class)BeforeStringInvoke.class);
/* 179 */     register((Class)JumpInsnPoint.class);
/* 180 */     register((Class)MethodHead.class);
/* 181 */     register((Class)AfterInvoke.class);
/* 182 */     register((Class)BeforeLoadLocal.class);
/* 183 */     register((Class)AfterStoreLocal.class);
/* 184 */     register((Class)BeforeFinalReturn.class);
/* 185 */     register((Class)BeforeConstant.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Selector selector;
/*     */   private final String id;
/*     */   
/*     */   protected InjectionPoint() {
/* 193 */     this("", Selector.DEFAULT, null);
/*     */   }
/*     */   
/*     */   protected InjectionPoint(InjectionPointData paramInjectionPointData) {
/* 197 */     this(paramInjectionPointData.getSlice(), paramInjectionPointData.getSelector(), paramInjectionPointData.getId());
/*     */   }
/*     */   
/*     */   public InjectionPoint(String paramString1, Selector paramSelector, String paramString2) {
/* 201 */     this.slice = paramString1;
/* 202 */     this.selector = paramSelector;
/* 203 */     this.id = paramString2;
/*     */   }
/*     */   
/*     */   public String getSlice() {
/* 207 */     return this.slice;
/*     */   }
/*     */   
/*     */   public Selector getSelector() {
/* 211 */     return this.selector;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 215 */     return this.id;
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
/*     */   public boolean checkPriority(int paramInt1, int paramInt2) {
/* 231 */     return (paramInt1 < paramInt2);
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
/*     */   public String toString() {
/* 253 */     return String.format("@At(\"%s\")", new Object[] { getAtCode() });
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
/*     */   protected static AbstractInsnNode nextNode(InsnList paramInsnList, AbstractInsnNode paramAbstractInsnNode) {
/* 265 */     int i = paramInsnList.indexOf(paramAbstractInsnNode) + 1;
/* 266 */     if (i > 0 && i < paramInsnList.size()) {
/* 267 */       return paramInsnList.get(i);
/*     */     }
/* 269 */     return paramAbstractInsnNode;
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class CompositeInjectionPoint
/*     */     extends InjectionPoint
/*     */   {
/*     */     protected final InjectionPoint[] components;
/*     */ 
/*     */     
/*     */     protected CompositeInjectionPoint(InjectionPoint... param1VarArgs) {
/* 280 */       if (param1VarArgs == null || param1VarArgs.length < 2) {
/* 281 */         throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
/*     */       }
/*     */       
/* 284 */       this.components = param1VarArgs;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 292 */       return "CompositeInjectionPoint(" + getClass().getSimpleName() + ")[" + Joiner.on(',').join((Object[])this.components) + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Intersection
/*     */     extends CompositeInjectionPoint
/*     */   {
/*     */     public Intersection(InjectionPoint... param1VarArgs) {
/* 303 */       super(param1VarArgs);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean find(String param1String, InsnList param1InsnList, Collection<AbstractInsnNode> param1Collection) {
/* 309 */       boolean bool = false;
/*     */       
/* 311 */       ArrayList[] arrayOfArrayList = (ArrayList[])Array.newInstance(ArrayList.class, this.components.length);
/*     */       
/* 313 */       for (byte b1 = 0; b1 < this.components.length; b1++) {
/* 314 */         arrayOfArrayList[b1] = new ArrayList();
/* 315 */         this.components[b1].find(param1String, param1InsnList, arrayOfArrayList[b1]);
/*     */       } 
/*     */       
/* 318 */       ArrayList<AbstractInsnNode> arrayList = arrayOfArrayList[0];
/* 319 */       for (byte b2 = 0; b2 < arrayList.size(); b2++) {
/* 320 */         AbstractInsnNode abstractInsnNode = arrayList.get(b2);
/* 321 */         boolean bool1 = true;
/*     */         
/* 323 */         for (byte b = 1; b < arrayOfArrayList.length && 
/* 324 */           arrayOfArrayList[b].contains(abstractInsnNode); b++);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 329 */         if (bool1) {
/*     */ 
/*     */ 
/*     */           
/* 333 */           param1Collection.add(abstractInsnNode);
/* 334 */           bool = true;
/*     */         } 
/*     */       } 
/* 337 */       return bool;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Union
/*     */     extends CompositeInjectionPoint
/*     */   {
/*     */     public Union(InjectionPoint... param1VarArgs) {
/* 348 */       super(param1VarArgs);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean find(String param1String, InsnList param1InsnList, Collection<AbstractInsnNode> param1Collection) {
/* 353 */       LinkedHashSet<AbstractInsnNode> linkedHashSet = new LinkedHashSet();
/*     */       
/* 355 */       for (byte b = 0; b < this.components.length; b++) {
/* 356 */         this.components[b].find(param1String, param1InsnList, linkedHashSet);
/*     */       }
/*     */       
/* 359 */       param1Collection.addAll(linkedHashSet);
/*     */       
/* 361 */       return (linkedHashSet.size() > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Shift
/*     */     extends InjectionPoint
/*     */   {
/*     */     private final InjectionPoint input;
/*     */     
/*     */     private final int shift;
/*     */ 
/*     */     
/*     */     public Shift(InjectionPoint param1InjectionPoint, int param1Int) {
/* 375 */       if (param1InjectionPoint == null) {
/* 376 */         throw new IllegalArgumentException("Must supply an input injection point for SHIFT");
/*     */       }
/*     */       
/* 379 */       this.input = param1InjectionPoint;
/* 380 */       this.shift = param1Int;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 388 */       return "InjectionPoint(" + getClass().getSimpleName() + ")[" + this.input + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean find(String param1String, InsnList param1InsnList, Collection<AbstractInsnNode> param1Collection) {
/* 393 */       List<AbstractInsnNode> list = (param1Collection instanceof List) ? (List)param1Collection : new ArrayList<AbstractInsnNode>(param1Collection);
/*     */       
/* 395 */       this.input.find(param1String, param1InsnList, param1Collection);
/*     */       
/* 397 */       for (byte b = 0; b < list.size(); b++) {
/* 398 */         list.set(b, param1InsnList.get(param1InsnList.indexOf(list.get(b)) + this.shift));
/*     */       }
/*     */       
/* 401 */       if (param1Collection != list) {
/* 402 */         param1Collection.clear();
/* 403 */         param1Collection.addAll(list);
/*     */       } 
/*     */       
/* 406 */       return (param1Collection.size() > 0);
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
/*     */   public static InjectionPoint and(InjectionPoint... paramVarArgs) {
/* 418 */     return new Intersection(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint or(InjectionPoint... paramVarArgs) {
/* 429 */     return new Union(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint after(InjectionPoint paramInjectionPoint) {
/* 440 */     return new Shift(paramInjectionPoint, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint before(InjectionPoint paramInjectionPoint) {
/* 451 */     return new Shift(paramInjectionPoint, -1);
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
/*     */   public static InjectionPoint shift(InjectionPoint paramInjectionPoint, int paramInt) {
/* 463 */     return new Shift(paramInjectionPoint, paramInt);
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
/*     */   public static List<InjectionPoint> parse(IInjectionPointContext paramIInjectionPointContext, List<AnnotationNode> paramList) {
/* 477 */     return parse(paramIInjectionPointContext.getContext(), paramIInjectionPointContext.getMethod(), paramIInjectionPointContext.getAnnotation(), paramList);
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
/*     */   public static List<InjectionPoint> parse(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, List<AnnotationNode> paramList) {
/* 493 */     ImmutableList.Builder builder = ImmutableList.builder();
/* 494 */     for (AnnotationNode annotationNode : paramList) {
/* 495 */       InjectionPoint injectionPoint = parse(paramIMixinContext, paramMethodNode, paramAnnotationNode, annotationNode);
/* 496 */       if (injectionPoint != null) {
/* 497 */         builder.add(injectionPoint);
/*     */       }
/*     */     } 
/* 500 */     return (List<InjectionPoint>)builder.build();
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
/*     */   public static InjectionPoint parse(IInjectionPointContext paramIInjectionPointContext, At paramAt) {
/* 513 */     return parse(paramIInjectionPointContext.getContext(), paramIInjectionPointContext.getMethod(), paramIInjectionPointContext.getAnnotation(), paramAt.value(), paramAt.shift(), paramAt.by(), 
/* 514 */         Arrays.asList(paramAt.args()), paramAt.target(), paramAt.slice(), paramAt.ordinal(), paramAt.opcode(), paramAt.id());
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
/*     */   public static InjectionPoint parse(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, At paramAt) {
/* 529 */     return parse(paramIMixinContext, paramMethodNode, paramAnnotationNode, paramAt.value(), paramAt.shift(), paramAt.by(), Arrays.asList(paramAt.args()), paramAt.target(), paramAt.slice(), paramAt
/* 530 */         .ordinal(), paramAt.opcode(), paramAt.id());
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
/*     */   public static InjectionPoint parse(IInjectionPointContext paramIInjectionPointContext, AnnotationNode paramAnnotationNode) {
/* 544 */     return parse(paramIInjectionPointContext.getContext(), paramIInjectionPointContext.getMethod(), paramIInjectionPointContext.getAnnotation(), paramAnnotationNode);
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
/*     */   public static InjectionPoint parse(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode1, AnnotationNode paramAnnotationNode2) {
/*     */     ImmutableList immutableList;
/* 560 */     String str1 = (String)Annotations.getValue(paramAnnotationNode2, "value");
/* 561 */     List list = (List)Annotations.getValue(paramAnnotationNode2, "args");
/* 562 */     String str2 = (String)Annotations.getValue(paramAnnotationNode2, "target", "");
/* 563 */     String str3 = (String)Annotations.getValue(paramAnnotationNode2, "slice", "");
/* 564 */     At.Shift shift = (At.Shift)Annotations.getValue(paramAnnotationNode2, "shift", At.Shift.class, At.Shift.NONE);
/* 565 */     int i = ((Integer)Annotations.getValue(paramAnnotationNode2, "by", Integer.valueOf(0))).intValue();
/* 566 */     int j = ((Integer)Annotations.getValue(paramAnnotationNode2, "ordinal", Integer.valueOf(-1))).intValue();
/* 567 */     int k = ((Integer)Annotations.getValue(paramAnnotationNode2, "opcode", Integer.valueOf(0))).intValue();
/* 568 */     String str4 = (String)Annotations.getValue(paramAnnotationNode2, "id");
/*     */     
/* 570 */     if (list == null) {
/* 571 */       immutableList = ImmutableList.of();
/*     */     }
/*     */     
/* 574 */     return parse(paramIMixinContext, paramMethodNode, paramAnnotationNode1, str1, shift, i, (List<String>)immutableList, str2, str3, j, k, str4);
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
/*     */ 
/*     */   
/*     */   public static InjectionPoint parse(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, String paramString1, At.Shift paramShift, int paramInt1, List<String> paramList, String paramString2, String paramString3, int paramInt2, int paramInt3, String paramString4) {
/* 599 */     InjectionPointData injectionPointData = new InjectionPointData(paramIMixinContext, paramMethodNode, paramAnnotationNode, paramString1, paramList, paramString2, paramString3, paramInt2, paramInt3, paramString4);
/* 600 */     Class<? extends InjectionPoint> clazz = findClass(paramIMixinContext, injectionPointData);
/* 601 */     InjectionPoint injectionPoint = create(paramIMixinContext, injectionPointData, clazz);
/* 602 */     return shift(paramIMixinContext, paramMethodNode, paramAnnotationNode, injectionPoint, paramShift, paramInt1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<? extends InjectionPoint> findClass(IMixinContext paramIMixinContext, InjectionPointData paramInjectionPointData) {
/* 607 */     String str = paramInjectionPointData.getType();
/* 608 */     Class<?> clazz = types.get(str);
/* 609 */     if (clazz == null) {
/* 610 */       if (str.matches("^([A-Za-z_][A-Za-z0-9_]*\\.)+[A-Za-z_][A-Za-z0-9_]*$")) {
/*     */         try {
/* 612 */           clazz = Class.forName(str);
/* 613 */           types.put(str, clazz);
/* 614 */         } catch (Exception exception) {
/* 615 */           throw new InvalidInjectionException(paramIMixinContext, paramInjectionPointData + " could not be loaded or is not a valid InjectionPoint", exception);
/*     */         } 
/*     */       } else {
/* 618 */         throw new InvalidInjectionException(paramIMixinContext, paramInjectionPointData + " is not a valid injection point specifier");
/*     */       } 
/*     */     }
/* 621 */     return (Class)clazz;
/*     */   }
/*     */   
/*     */   private static InjectionPoint create(IMixinContext paramIMixinContext, InjectionPointData paramInjectionPointData, Class<? extends InjectionPoint> paramClass) {
/* 625 */     Constructor<? extends InjectionPoint> constructor = null;
/*     */     try {
/* 627 */       constructor = paramClass.getDeclaredConstructor(new Class[] { InjectionPointData.class });
/* 628 */       constructor.setAccessible(true);
/* 629 */     } catch (NoSuchMethodException noSuchMethodException) {
/* 630 */       throw new InvalidInjectionException(paramIMixinContext, paramClass.getName() + " must contain a constructor which accepts an InjectionPointData", noSuchMethodException);
/*     */     } 
/*     */     
/* 633 */     InjectionPoint injectionPoint = null;
/*     */     try {
/* 635 */       injectionPoint = constructor.newInstance(new Object[] { paramInjectionPointData });
/* 636 */     } catch (Exception exception) {
/* 637 */       throw new InvalidInjectionException(paramIMixinContext, "Error whilst instancing injection point " + paramClass.getName() + " for " + paramInjectionPointData.getAt(), exception);
/*     */     } 
/*     */     
/* 640 */     return injectionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static InjectionPoint shift(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, InjectionPoint paramInjectionPoint, At.Shift paramShift, int paramInt) {
/* 646 */     if (paramInjectionPoint != null) {
/* 647 */       if (paramShift == At.Shift.BEFORE)
/* 648 */         return before(paramInjectionPoint); 
/* 649 */       if (paramShift == At.Shift.AFTER)
/* 650 */         return after(paramInjectionPoint); 
/* 651 */       if (paramShift == At.Shift.BY) {
/* 652 */         validateByValue(paramIMixinContext, paramMethodNode, paramAnnotationNode, paramInjectionPoint, paramInt);
/* 653 */         return shift(paramInjectionPoint, paramInt);
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     return paramInjectionPoint;
/*     */   }
/*     */   
/*     */   private static void validateByValue(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, InjectionPoint paramInjectionPoint, int paramInt) {
/* 661 */     MixinEnvironment mixinEnvironment = paramIMixinContext.getMixin().getConfig().getEnvironment();
/* 662 */     ShiftByViolationBehaviour shiftByViolationBehaviour = (ShiftByViolationBehaviour)mixinEnvironment.getOption(MixinEnvironment.Option.SHIFT_BY_VIOLATION_BEHAVIOUR, ShiftByViolationBehaviour.WARN);
/* 663 */     if (shiftByViolationBehaviour == ShiftByViolationBehaviour.IGNORE) {
/*     */       return;
/*     */     }
/*     */     
/* 667 */     String str1 = "the maximum allowed value: ";
/* 668 */     String str2 = "Increase the value of maxShiftBy to suppress this warning.";
/* 669 */     int i = 0;
/* 670 */     if (paramIMixinContext instanceof MixinTargetContext) {
/* 671 */       i = ((MixinTargetContext)paramIMixinContext).getMaxShiftByValue();
/*     */     }
/*     */     
/* 674 */     if (paramInt <= i) {
/*     */       return;
/*     */     }
/*     */     
/* 678 */     if (paramInt > 5) {
/* 679 */       str1 = "MAX_ALLOWED_SHIFT_BY=";
/* 680 */       str2 = "You must use an alternate query or a custom injection point.";
/* 681 */       i = 5;
/*     */     } 
/*     */     
/* 684 */     String str3 = String.format("@%s(%s) Shift.BY=%d on %s::%s exceeds %s%d. %s", new Object[] { Bytecode.getSimpleName(paramAnnotationNode), paramInjectionPoint, 
/* 685 */           Integer.valueOf(paramInt), paramIMixinContext, paramMethodNode.name, str1, Integer.valueOf(i), str2 });
/*     */     
/* 687 */     if (shiftByViolationBehaviour == ShiftByViolationBehaviour.WARN && i < 5) {
/* 688 */       LogManager.getLogger("mixin").warn(str3);
/*     */       
/*     */       return;
/*     */     } 
/* 692 */     throw new InvalidInjectionException(paramIMixinContext, str3);
/*     */   }
/*     */   
/*     */   protected String getAtCode() {
/* 696 */     AtCode atCode = getClass().<AtCode>getAnnotation(AtCode.class);
/* 697 */     return (atCode == null) ? getClass().getName() : atCode.value();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(Class<? extends InjectionPoint> paramClass) {
/* 707 */     AtCode atCode = paramClass.<AtCode>getAnnotation(AtCode.class);
/* 708 */     if (atCode == null) {
/* 709 */       throw new IllegalArgumentException("Injection point class " + paramClass + " is not annotated with @AtCode");
/*     */     }
/*     */     
/* 712 */     Class clazz = types.get(atCode.value());
/* 713 */     if (clazz != null && !clazz.equals(paramClass)) {
/* 714 */       LogManager.getLogger("mixin").debug("Overriding InjectionPoint {} with {} (previously {})", new Object[] { atCode.value(), paramClass.getName(), clazz
/* 715 */             .getName() });
/*     */     }
/*     */     
/* 718 */     types.put(atCode.value(), paramClass);
/*     */   }
/*     */   
/*     */   public abstract boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection);
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\InjectionPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */