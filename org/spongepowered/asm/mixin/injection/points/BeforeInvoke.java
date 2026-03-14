/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AtCode("INVOKE")
/*     */ public class BeforeInvoke
/*     */   extends InjectionPoint
/*     */ {
/*     */   protected final MemberInfo target;
/*     */   protected final boolean allowPermissive;
/*     */   protected final int ordinal;
/*     */   protected final String className;
/*     */   protected final IMixinContext context;
/*     */   
/*     */   public enum SearchType
/*     */   {
/*  78 */     STRICT,
/*  79 */     PERMISSIVE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */   
/*     */   private boolean log = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public BeforeInvoke(InjectionPointData paramInjectionPointData) {
/* 120 */     super(paramInjectionPointData);
/*     */     
/* 122 */     this.target = paramInjectionPointData.getTarget();
/* 123 */     this.ordinal = paramInjectionPointData.getOrdinal();
/* 124 */     this.log = paramInjectionPointData.get("log", false);
/* 125 */     this.className = getClassName();
/* 126 */     this.context = paramInjectionPointData.getContext();
/* 127 */     this
/* 128 */       .allowPermissive = (this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP) && this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP_ALLOW_PERMISSIVE) && !this.context.getReferenceMapper().isDefault());
/*     */   }
/*     */   
/*     */   private String getClassName() {
/* 132 */     InjectionPoint.AtCode atCode = getClass().<InjectionPoint.AtCode>getAnnotation(InjectionPoint.AtCode.class);
/* 133 */     return String.format("@At(%s)", new Object[] { (atCode != null) ? atCode.value() : getClass().getSimpleName().toUpperCase() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeforeInvoke setLogging(boolean paramBoolean) {
/* 143 */     this.log = paramBoolean;
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 154 */     log("{} is searching for an injection point in method with descriptor {}", new Object[] { this.className, paramString });
/*     */     
/* 156 */     if (!find(paramString, paramInsnList, paramCollection, this.target, SearchType.STRICT) && this.target.desc != null && this.allowPermissive) {
/* 157 */       this.logger.warn("STRICT match for {} using \"{}\" in {} returned 0 results, attempting permissive search. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, this.target, this.context });
/*     */       
/* 159 */       return find(paramString, paramInsnList, paramCollection, this.target, SearchType.PERMISSIVE);
/*     */     } 
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection, MemberInfo paramMemberInfo, SearchType paramSearchType) {
/* 165 */     if (paramMemberInfo == null) {
/* 166 */       return false;
/*     */     }
/*     */     
/* 169 */     MemberInfo memberInfo = (paramSearchType == SearchType.PERMISSIVE) ? paramMemberInfo.transform(null) : paramMemberInfo;
/*     */     
/* 171 */     byte b1 = 0;
/* 172 */     byte b2 = 0;
/*     */     
/* 174 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator();
/* 175 */     while (listIterator.hasNext()) {
/* 176 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*     */       
/* 178 */       if (matchesInsn(abstractInsnNode)) {
/* 179 */         MemberInfo memberInfo1 = new MemberInfo(abstractInsnNode);
/* 180 */         log("{} is considering insn {}", new Object[] { this.className, memberInfo1 });
/*     */         
/* 182 */         if (memberInfo.matches(memberInfo1.owner, memberInfo1.name, memberInfo1.desc)) {
/* 183 */           log("{} > found a matching insn, checking preconditions...", new Object[] { this.className });
/*     */           
/* 185 */           if (matchesInsn(memberInfo1, b1)) {
/* 186 */             log("{} > > > found a matching insn at ordinal {}", new Object[] { this.className, Integer.valueOf(b1) });
/*     */             
/* 188 */             if (addInsn(paramInsnList, paramCollection, abstractInsnNode)) {
/* 189 */               b2++;
/*     */             }
/*     */             
/* 192 */             if (this.ordinal == b1) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */           
/* 197 */           b1++;
/*     */         } 
/*     */       } 
/*     */       
/* 201 */       inspectInsn(paramString, paramInsnList, abstractInsnNode);
/*     */     } 
/*     */     
/* 204 */     if (paramSearchType == SearchType.PERMISSIVE && b2 > 1) {
/* 205 */       this.logger.warn("A permissive match for {} using \"{}\" in {} matched {} instructions, this may cause unexpected behaviour. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, paramMemberInfo, this.context, 
/* 206 */             Integer.valueOf(b2) });
/*     */     }
/*     */     
/* 209 */     return (b2 > 0);
/*     */   }
/*     */   
/*     */   protected boolean addInsn(InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection, AbstractInsnNode paramAbstractInsnNode) {
/* 213 */     paramCollection.add(paramAbstractInsnNode);
/* 214 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean matchesInsn(AbstractInsnNode paramAbstractInsnNode) {
/* 218 */     return paramAbstractInsnNode instanceof org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inspectInsn(String paramString, InsnList paramInsnList, AbstractInsnNode paramAbstractInsnNode) {}
/*     */ 
/*     */   
/*     */   protected boolean matchesInsn(MemberInfo paramMemberInfo, int paramInt) {
/* 226 */     log("{} > > comparing target ordinal {} with current ordinal {}", new Object[] { this.className, Integer.valueOf(this.ordinal), Integer.valueOf(paramInt) });
/* 227 */     return (this.ordinal == -1 || this.ordinal == paramInt);
/*     */   }
/*     */   
/*     */   protected void log(String paramString, Object... paramVarArgs) {
/* 231 */     if (this.log)
/* 232 */       this.logger.info(paramString, paramVarArgs); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeInvoke.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */