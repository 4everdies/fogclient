/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
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
/*     */ 
/*     */ 
/*     */ @AtCode("NEW")
/*     */ public class BeforeNew
/*     */   extends InjectionPoint
/*     */ {
/*     */   private final String target;
/*     */   private final String desc;
/*     */   private final int ordinal;
/*     */   
/*     */   public BeforeNew(InjectionPointData paramInjectionPointData) {
/* 107 */     super(paramInjectionPointData);
/*     */     
/* 109 */     this.ordinal = paramInjectionPointData.getOrdinal();
/* 110 */     String str = Strings.emptyToNull(paramInjectionPointData.get("class", paramInjectionPointData.get("target", "")).replace('.', '/'));
/* 111 */     MemberInfo memberInfo = MemberInfo.parseAndValidate(str, paramInjectionPointData.getContext());
/* 112 */     this.target = memberInfo.toCtorType();
/* 113 */     this.desc = memberInfo.toCtorDesc();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDescriptor() {
/* 120 */     return (this.desc != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 126 */     boolean bool = false;
/* 127 */     byte b = 0;
/*     */     
/* 129 */     ArrayList arrayList = new ArrayList();
/* 130 */     Collection<AbstractInsnNode> collection = (this.desc != null) ? arrayList : paramCollection;
/* 131 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator();
/* 132 */     while (listIterator.hasNext()) {
/* 133 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*     */       
/* 135 */       if (abstractInsnNode instanceof TypeInsnNode && abstractInsnNode.getOpcode() == 187 && matchesOwner((TypeInsnNode)abstractInsnNode)) {
/* 136 */         if (this.ordinal == -1 || this.ordinal == b) {
/* 137 */           collection.add(abstractInsnNode);
/* 138 */           bool = (this.desc == null) ? true : false;
/*     */         } 
/*     */         
/* 141 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     if (this.desc != null) {
/* 146 */       for (TypeInsnNode typeInsnNode : arrayList) {
/* 147 */         if (findCtor(paramInsnList, typeInsnNode)) {
/* 148 */           paramCollection.add(typeInsnNode);
/* 149 */           bool = true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 154 */     return bool;
/*     */   }
/*     */   
/*     */   protected boolean findCtor(InsnList paramInsnList, TypeInsnNode paramTypeInsnNode) {
/* 158 */     int i = paramInsnList.indexOf((AbstractInsnNode)paramTypeInsnNode);
/* 159 */     for (ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator(i); listIterator.hasNext(); ) {
/* 160 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/* 161 */       if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() == 183) {
/* 162 */         MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
/* 163 */         if ("<init>".equals(methodInsnNode.name) && methodInsnNode.owner.equals(paramTypeInsnNode.desc) && methodInsnNode.desc.equals(this.desc)) {
/* 164 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matchesOwner(TypeInsnNode paramTypeInsnNode) {
/* 172 */     return (this.target == null || this.target.equals(paramTypeInsnNode.desc));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeNew.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */