/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.LdcInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AtCode("INVOKE_STRING")
/*     */ public class BeforeStringInvoke
/*     */   extends BeforeInvoke
/*     */ {
/*     */   private static final String STRING_VOID_SIG = "(Ljava/lang/String;)V";
/*     */   private final String ldcValue;
/*     */   private boolean foundLdc;
/*     */   
/*     */   public BeforeStringInvoke(InjectionPointData paramInjectionPointData) {
/* 100 */     super(paramInjectionPointData);
/* 101 */     this.ldcValue = paramInjectionPointData.get("ldc", null);
/*     */     
/* 103 */     if (this.ldcValue == null) {
/* 104 */       throw new IllegalArgumentException(getClass().getSimpleName() + " requires named argument \"ldc\" to specify the desired target");
/*     */     }
/*     */     
/* 107 */     if (!"(Ljava/lang/String;)V".equals(this.target.desc)) {
/* 108 */       throw new IllegalArgumentException(getClass().getSimpleName() + " requires target method with with signature " + "(Ljava/lang/String;)V");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 114 */     this.foundLdc = false;
/*     */     
/* 116 */     return super.find(paramString, paramInsnList, paramCollection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void inspectInsn(String paramString, InsnList paramInsnList, AbstractInsnNode paramAbstractInsnNode) {
/* 121 */     if (paramAbstractInsnNode instanceof LdcInsnNode) {
/* 122 */       LdcInsnNode ldcInsnNode = (LdcInsnNode)paramAbstractInsnNode;
/* 123 */       if (ldcInsnNode.cst instanceof String && this.ldcValue.equals(ldcInsnNode.cst)) {
/* 124 */         log("{} > found a matching LDC with value {}", new Object[] { this.className, ldcInsnNode.cst });
/* 125 */         this.foundLdc = true;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 130 */     this.foundLdc = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean matchesInsn(MemberInfo paramMemberInfo, int paramInt) {
/* 135 */     log("{} > > found LDC \"{}\" = {}", new Object[] { this.className, this.ldcValue, Boolean.valueOf(this.foundLdc) });
/* 136 */     return (this.foundLdc && super.matchesInsn(paramMemberInfo, paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeStringInvoke.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */