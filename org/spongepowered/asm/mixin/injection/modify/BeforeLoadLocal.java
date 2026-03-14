/*     */ package org.spongepowered.asm.mixin.injection.modify;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AtCode("LOAD")
/*     */ public class BeforeLoadLocal
/*     */   extends ModifyVariableInjector.ContextualInjectionPoint
/*     */ {
/*     */   private final Type returnType;
/*     */   private final LocalVariableDiscriminator discriminator;
/*     */   private final int opcode;
/*     */   private final int ordinal;
/*     */   private boolean opcodeAfter;
/*     */   
/*     */   static class SearchState
/*     */   {
/*     */     private final boolean print;
/*     */     private final int targetOrdinal;
/*  96 */     private int ordinal = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean pendingCheck = false;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean found = false;
/*     */ 
/*     */ 
/*     */     
/*     */     private VarInsnNode varNode;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SearchState(int param1Int, boolean param1Boolean) {
/* 115 */       this.targetOrdinal = param1Int;
/* 116 */       this.print = param1Boolean;
/*     */     }
/*     */     
/*     */     boolean success() {
/* 120 */       return this.found;
/*     */     }
/*     */     
/*     */     boolean isPendingCheck() {
/* 124 */       return this.pendingCheck;
/*     */     }
/*     */     
/*     */     void setPendingCheck() {
/* 128 */       this.pendingCheck = true;
/*     */     }
/*     */     
/*     */     void register(VarInsnNode param1VarInsnNode) {
/* 132 */       this.varNode = param1VarInsnNode;
/*     */     }
/*     */     
/*     */     void check(Collection<AbstractInsnNode> param1Collection, AbstractInsnNode param1AbstractInsnNode, int param1Int) {
/* 136 */       this.pendingCheck = false;
/* 137 */       if (param1Int != this.varNode.var && (param1Int > -2 || !this.print)) {
/*     */         return;
/*     */       }
/*     */       
/* 141 */       if (this.targetOrdinal == -1 || this.targetOrdinal == this.ordinal) {
/* 142 */         param1Collection.add(param1AbstractInsnNode);
/* 143 */         this.found = true;
/*     */       } 
/*     */       
/* 146 */       this.ordinal++;
/* 147 */       this.varNode = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeforeLoadLocal(InjectionPointData paramInjectionPointData) {
/* 180 */     this(paramInjectionPointData, 21, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeforeLoadLocal(InjectionPointData paramInjectionPointData, int paramInt, boolean paramBoolean) {
/* 185 */     super(paramInjectionPointData.getContext());
/* 186 */     this.returnType = paramInjectionPointData.getMethodReturnType();
/* 187 */     this.discriminator = paramInjectionPointData.getLocalVariableDiscriminator();
/* 188 */     this.opcode = paramInjectionPointData.getOpcode(this.returnType.getOpcode(paramInt));
/* 189 */     this.ordinal = paramInjectionPointData.getOrdinal();
/* 190 */     this.opcodeAfter = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean find(Target paramTarget, Collection<AbstractInsnNode> paramCollection) {
/* 195 */     SearchState searchState = new SearchState(this.ordinal, this.discriminator.printLVT());
/*     */     
/* 197 */     ListIterator<AbstractInsnNode> listIterator = paramTarget.method.instructions.iterator();
/* 198 */     while (listIterator.hasNext()) {
/* 199 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/* 200 */       if (searchState.isPendingCheck()) {
/* 201 */         int i = this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), paramTarget, abstractInsnNode);
/* 202 */         searchState.check(paramCollection, abstractInsnNode, i); continue;
/* 203 */       }  if (abstractInsnNode instanceof VarInsnNode && abstractInsnNode.getOpcode() == this.opcode && (this.ordinal == -1 || !searchState.success())) {
/* 204 */         searchState.register((VarInsnNode)abstractInsnNode);
/* 205 */         if (this.opcodeAfter) {
/* 206 */           searchState.setPendingCheck(); continue;
/*     */         } 
/* 208 */         int i = this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), paramTarget, abstractInsnNode);
/* 209 */         searchState.check(paramCollection, abstractInsnNode, i);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 214 */     return searchState.success();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\modify\BeforeLoadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */