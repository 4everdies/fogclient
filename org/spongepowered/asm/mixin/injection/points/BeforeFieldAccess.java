/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AtCode("FIELD")
/*     */ public class BeforeFieldAccess
/*     */   extends BeforeInvoke
/*     */ {
/*     */   private static final String ARRAY_GET = "get";
/*     */   private static final String ARRAY_SET = "set";
/*     */   private static final String ARRAY_LENGTH = "length";
/*     */   public static final int ARRAY_SEARCH_FUZZ_DEFAULT = 8;
/*     */   private final int opcode;
/*     */   private final int arrOpcode;
/*     */   private final int fuzzFactor;
/*     */   
/*     */   public BeforeFieldAccess(InjectionPointData paramInjectionPointData) {
/* 116 */     super(paramInjectionPointData);
/* 117 */     this.opcode = paramInjectionPointData.getOpcode(-1, new int[] { 180, 181, 178, 179, -1 });
/*     */     
/* 119 */     String str = paramInjectionPointData.get("array", "");
/* 120 */     this
/*     */       
/* 122 */       .arrOpcode = "get".equalsIgnoreCase(str) ? 46 : ("set".equalsIgnoreCase(str) ? 79 : ("length".equalsIgnoreCase(str) ? 190 : 0));
/* 123 */     this.fuzzFactor = Math.min(Math.max(paramInjectionPointData.get("fuzz", 8), 1), 32);
/*     */   }
/*     */   
/*     */   public int getFuzzFactor() {
/* 127 */     return this.fuzzFactor;
/*     */   }
/*     */   
/*     */   public int getArrayOpcode() {
/* 131 */     return this.arrOpcode;
/*     */   }
/*     */   
/*     */   private int getArrayOpcode(String paramString) {
/* 135 */     if (this.arrOpcode != 190) {
/* 136 */       return Type.getType(paramString).getElementType().getOpcode(this.arrOpcode);
/*     */     }
/* 138 */     return this.arrOpcode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean matchesInsn(AbstractInsnNode paramAbstractInsnNode) {
/* 143 */     if (paramAbstractInsnNode instanceof FieldInsnNode && (((FieldInsnNode)paramAbstractInsnNode).getOpcode() == this.opcode || this.opcode == -1)) {
/* 144 */       if (this.arrOpcode == 0) {
/* 145 */         return true;
/*     */       }
/*     */       
/* 148 */       if (paramAbstractInsnNode.getOpcode() != 178 && paramAbstractInsnNode.getOpcode() != 180) {
/* 149 */         return false;
/*     */       }
/*     */       
/* 152 */       return (Type.getType(((FieldInsnNode)paramAbstractInsnNode).desc).getSort() == 9);
/*     */     } 
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean addInsn(InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection, AbstractInsnNode paramAbstractInsnNode) {
/* 160 */     if (this.arrOpcode > 0) {
/* 161 */       FieldInsnNode fieldInsnNode = (FieldInsnNode)paramAbstractInsnNode;
/* 162 */       int i = getArrayOpcode(fieldInsnNode.desc);
/* 163 */       log("{} > > > > searching for array access opcode {} fuzz={}", new Object[] { this.className, Bytecode.getOpcodeName(i), Integer.valueOf(this.fuzzFactor) });
/*     */       
/* 165 */       if (findArrayNode(paramInsnList, fieldInsnNode, i, this.fuzzFactor) == null) {
/* 166 */         log("{} > > > > > failed to locate matching insn", new Object[] { this.className });
/* 167 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     log("{} > > > > > adding matching insn", new Object[] { this.className });
/*     */     
/* 173 */     return super.addInsn(paramInsnList, paramCollection, paramAbstractInsnNode);
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
/*     */   public static AbstractInsnNode findArrayNode(InsnList paramInsnList, FieldInsnNode paramFieldInsnNode, int paramInt1, int paramInt2) {
/* 191 */     byte b = 0;
/* 192 */     for (ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator(paramInsnList.indexOf((AbstractInsnNode)paramFieldInsnNode) + 1); listIterator.hasNext(); ) {
/* 193 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/* 194 */       if (abstractInsnNode.getOpcode() == paramInt1)
/* 195 */         return abstractInsnNode; 
/* 196 */       if (abstractInsnNode.getOpcode() == 190 && !b)
/* 197 */         return null; 
/* 198 */       if (abstractInsnNode instanceof FieldInsnNode) {
/* 199 */         FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
/* 200 */         if (fieldInsnNode.desc.equals(paramFieldInsnNode.desc) && fieldInsnNode.name.equals(paramFieldInsnNode.name) && fieldInsnNode.owner.equals(paramFieldInsnNode.owner)) {
/* 201 */           return null;
/*     */         }
/*     */       } 
/* 204 */       if (b++ > paramInt2) {
/* 205 */         return null;
/*     */       }
/*     */     } 
/* 208 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeFieldAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */