/*     */ package org.spongepowered.asm.mixin.injection.code;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.InsnNode;
/*     */ import org.spongepowered.asm.lib.tree.LdcInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
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
/*     */ public abstract class Injector
/*     */ {
/*     */   public static final class TargetNode
/*     */   {
/*     */     final AbstractInsnNode insn;
/*  70 */     final Set<InjectionPoint> nominators = new HashSet<InjectionPoint>();
/*     */     
/*     */     TargetNode(AbstractInsnNode param1AbstractInsnNode) {
/*  73 */       this.insn = param1AbstractInsnNode;
/*     */     }
/*     */     
/*     */     public AbstractInsnNode getNode() {
/*  77 */       return this.insn;
/*     */     }
/*     */     
/*     */     public Set<InjectionPoint> getNominators() {
/*  81 */       return Collections.unmodifiableSet(this.nominators);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*  86 */       if (param1Object == null || param1Object.getClass() != TargetNode.class) {
/*  87 */         return false;
/*     */       }
/*     */       
/*  90 */       return (((TargetNode)param1Object).insn == this.insn);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  95 */       return this.insn.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionInfo info;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClassNode classNode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MethodNode methodNode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Type[] methodArgs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Type returnType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isStatic;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Injector(InjectionInfo paramInjectionInfo) {
/* 141 */     this(paramInjectionInfo.getClassNode(), paramInjectionInfo.getMethod());
/* 142 */     this.info = paramInjectionInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Injector(ClassNode paramClassNode, MethodNode paramMethodNode) {
/* 152 */     this.classNode = paramClassNode;
/* 153 */     this.methodNode = paramMethodNode;
/* 154 */     this.methodArgs = Type.getArgumentTypes(this.methodNode.desc);
/* 155 */     this.returnType = Type.getReturnType(this.methodNode.desc);
/* 156 */     this.isStatic = Bytecode.methodIsStatic(this.methodNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return String.format("%s::%s", new Object[] { this.classNode.name, this.methodNode.name });
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
/*     */   public final List<InjectionNodes.InjectionNode> find(InjectorTarget paramInjectorTarget, List<InjectionPoint> paramList) {
/* 173 */     sanityCheck(paramInjectorTarget.getTarget(), paramList);
/*     */     
/* 175 */     ArrayList<InjectionNodes.InjectionNode> arrayList = new ArrayList();
/* 176 */     for (TargetNode targetNode : findTargetNodes(paramInjectorTarget, paramList)) {
/* 177 */       addTargetNode(paramInjectorTarget.getTarget(), arrayList, targetNode.insn, targetNode.nominators);
/*     */     }
/* 179 */     return arrayList;
/*     */   }
/*     */   
/*     */   protected void addTargetNode(Target paramTarget, List<InjectionNodes.InjectionNode> paramList, AbstractInsnNode paramAbstractInsnNode, Set<InjectionPoint> paramSet) {
/* 183 */     paramList.add(paramTarget.addInjectionNode(paramAbstractInsnNode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void inject(Target paramTarget, List<InjectionNodes.InjectionNode> paramList) {
/* 193 */     for (InjectionNodes.InjectionNode injectionNode : paramList) {
/* 194 */       if (injectionNode.isRemoved()) {
/* 195 */         if (this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
/* 196 */           logger.warn("Target node for {} was removed by a previous injector in {}", new Object[] { this.info, paramTarget });
/*     */         }
/*     */         continue;
/*     */       } 
/* 200 */       inject(paramTarget, injectionNode);
/*     */     } 
/*     */     
/* 203 */     for (InjectionNodes.InjectionNode injectionNode : paramList) {
/* 204 */       postInject(paramTarget, injectionNode);
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
/*     */   private Collection<TargetNode> findTargetNodes(InjectorTarget paramInjectorTarget, List<InjectionPoint> paramList) {
/* 218 */     IMixinContext iMixinContext = this.info.getContext();
/* 219 */     MethodNode methodNode = paramInjectorTarget.getMethod();
/* 220 */     TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
/* 221 */     ArrayList<AbstractInsnNode> arrayList = new ArrayList(32);
/*     */     
/* 223 */     for (InjectionPoint injectionPoint : paramList) {
/* 224 */       arrayList.clear();
/*     */       
/* 226 */       if (paramInjectorTarget.isMerged() && 
/* 227 */         !iMixinContext.getClassName().equals(paramInjectorTarget.getMergedBy()) && 
/* 228 */         !injectionPoint.checkPriority(paramInjectorTarget.getMergedPriority(), iMixinContext.getPriority())) {
/* 229 */         throw new InvalidInjectionException(this.info, String.format("%s on %s with priority %d cannot inject into %s merged by %s with priority %d", new Object[] { injectionPoint, this, 
/* 230 */                 Integer.valueOf(iMixinContext.getPriority()), paramInjectorTarget, paramInjectorTarget
/* 231 */                 .getMergedBy(), Integer.valueOf(paramInjectorTarget.getMergedPriority()) }));
/*     */       }
/*     */       
/* 234 */       if (findTargetNodes(methodNode, injectionPoint, paramInjectorTarget.getSlice(injectionPoint), arrayList)) {
/* 235 */         for (AbstractInsnNode abstractInsnNode : arrayList) {
/* 236 */           Integer integer = Integer.valueOf(methodNode.instructions.indexOf(abstractInsnNode));
/* 237 */           TargetNode targetNode = (TargetNode)treeMap.get(integer);
/* 238 */           if (targetNode == null) {
/* 239 */             targetNode = new TargetNode(abstractInsnNode);
/* 240 */             treeMap.put(integer, targetNode);
/*     */           } 
/* 242 */           targetNode.nominators.add(injectionPoint);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 247 */     return treeMap.values();
/*     */   }
/*     */   
/*     */   protected boolean findTargetNodes(MethodNode paramMethodNode, InjectionPoint paramInjectionPoint, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 251 */     return paramInjectionPoint.find(paramMethodNode.desc, paramInsnList, paramCollection);
/*     */   }
/*     */   
/*     */   protected void sanityCheck(Target paramTarget, List<InjectionPoint> paramList) {
/* 255 */     if (paramTarget.classNode != this.classNode) {
/* 256 */       throw new InvalidInjectionException(this.info, "Target class does not match injector class in " + this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void inject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postInject(Target paramTarget, InjectionNodes.InjectionNode paramInjectionNode) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractInsnNode invokeHandler(InsnList paramInsnList) {
/* 273 */     return invokeHandler(paramInsnList, this.methodNode);
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
/*     */   protected AbstractInsnNode invokeHandler(InsnList paramInsnList, MethodNode paramMethodNode) {
/* 285 */     boolean bool = ((paramMethodNode.access & 0x2) != 0) ? true : false;
/* 286 */     char c = this.isStatic ? '¸' : (bool ? '·' : '¶');
/* 287 */     MethodInsnNode methodInsnNode = new MethodInsnNode(c, this.classNode.name, paramMethodNode.name, paramMethodNode.desc, false);
/* 288 */     paramInsnList.add((AbstractInsnNode)methodInsnNode);
/* 289 */     this.info.addCallbackInvocation(paramMethodNode);
/* 290 */     return (AbstractInsnNode)methodInsnNode;
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
/*     */   protected void throwException(InsnList paramInsnList, String paramString1, String paramString2) {
/* 302 */     paramInsnList.add((AbstractInsnNode)new TypeInsnNode(187, paramString1));
/* 303 */     paramInsnList.add((AbstractInsnNode)new InsnNode(89));
/* 304 */     paramInsnList.add((AbstractInsnNode)new LdcInsnNode(paramString2));
/* 305 */     paramInsnList.add((AbstractInsnNode)new MethodInsnNode(183, paramString1, "<init>", "(Ljava/lang/String;)V", false));
/* 306 */     paramInsnList.add((AbstractInsnNode)new InsnNode(191));
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
/*     */   public static boolean canCoerce(Type paramType1, Type paramType2) {
/* 318 */     if (paramType1.getSort() == 10 && paramType2.getSort() == 10) {
/* 319 */       return canCoerce(ClassInfo.forType(paramType1), ClassInfo.forType(paramType2));
/*     */     }
/*     */     
/* 322 */     return canCoerce(paramType1.getDescriptor(), paramType2.getDescriptor());
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
/*     */   public static boolean canCoerce(String paramString1, String paramString2) {
/* 334 */     if (paramString1.length() > 1 || paramString2.length() > 1) {
/* 335 */       return false;
/*     */     }
/*     */     
/* 338 */     return canCoerce(paramString1.charAt(0), paramString2.charAt(0));
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
/*     */   public static boolean canCoerce(char paramChar1, char paramChar2) {
/* 350 */     return (paramChar2 == 'I' && "IBSCZ".indexOf(paramChar1) > -1);
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
/*     */   private static boolean canCoerce(ClassInfo paramClassInfo1, ClassInfo paramClassInfo2) {
/* 363 */     return (paramClassInfo1 != null && paramClassInfo2 != null && (paramClassInfo2 == paramClassInfo1 || paramClassInfo2.hasSuperClass(paramClassInfo1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\code\Injector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */