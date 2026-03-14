/*     */ package org.spongepowered.asm.mixin.struct;
/*     */ 
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
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
/*     */ public abstract class MemberRef
/*     */ {
/*     */   public static final class Method
/*     */     extends MemberRef
/*     */   {
/*     */     private static final int OPCODES = 191;
/*     */     public final MethodInsnNode insn;
/*     */     
/*     */     public Method(MethodInsnNode param1MethodInsnNode) {
/*  59 */       this.insn = param1MethodInsnNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isField() {
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOpcode() {
/*  69 */       return this.insn.getOpcode();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOpcode(int param1Int) {
/*  74 */       if ((param1Int & 0xBF) == 0) {
/*  75 */         throw new IllegalArgumentException("Invalid opcode for method instruction: 0x" + Integer.toHexString(param1Int));
/*     */       }
/*     */       
/*  78 */       this.insn.setOpcode(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOwner() {
/*  83 */       return this.insn.owner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(String param1String) {
/*  88 */       this.insn.owner = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/*  93 */       return this.insn.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setName(String param1String) {
/*  98 */       this.insn.name = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDesc() {
/* 103 */       return this.insn.desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDesc(String param1String) {
/* 108 */       this.insn.desc = param1String;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Field
/*     */     extends MemberRef
/*     */   {
/*     */     private static final int OPCODES = 183;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final FieldInsnNode insn;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Field(FieldInsnNode param1FieldInsnNode) {
/* 130 */       this.insn = param1FieldInsnNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isField() {
/* 135 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOpcode() {
/* 140 */       return this.insn.getOpcode();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOpcode(int param1Int) {
/* 145 */       if ((param1Int & 0xB7) == 0) {
/* 146 */         throw new IllegalArgumentException("Invalid opcode for field instruction: 0x" + Integer.toHexString(param1Int));
/*     */       }
/*     */       
/* 149 */       this.insn.setOpcode(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOwner() {
/* 154 */       return this.insn.owner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(String param1String) {
/* 159 */       this.insn.owner = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 164 */       return this.insn.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setName(String param1String) {
/* 169 */       this.insn.name = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDesc() {
/* 174 */       return this.insn.desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDesc(String param1String) {
/* 179 */       this.insn.desc = param1String;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Handle
/*     */     extends MemberRef
/*     */   {
/*     */     private org.spongepowered.asm.lib.Handle handle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Handle(org.spongepowered.asm.lib.Handle param1Handle) {
/* 198 */       this.handle = param1Handle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public org.spongepowered.asm.lib.Handle getMethodHandle() {
/* 207 */       return this.handle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isField() {
/* 212 */       switch (this.handle.getTag()) {
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/* 218 */           return false;
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/* 223 */           return true;
/*     */       } 
/* 225 */       throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getOpcode() {
/* 231 */       int i = MemberRef.opcodeFromTag(this.handle.getTag());
/* 232 */       if (i == 0) {
/* 233 */         throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
/*     */       }
/* 235 */       return i;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOpcode(int param1Int) {
/* 240 */       int i = MemberRef.tagFromOpcode(param1Int);
/* 241 */       if (i == 0) {
/* 242 */         throw new MixinTransformerError("Invalid opcode " + Bytecode.getOpcodeName(param1Int) + " for method handle " + this.handle + ".");
/*     */       }
/* 244 */       boolean bool = (i == 9) ? true : false;
/* 245 */       this.handle = new org.spongepowered.asm.lib.Handle(i, this.handle.getOwner(), this.handle.getName(), this.handle.getDesc(), bool);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOwner() {
/* 250 */       return this.handle.getOwner();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(String param1String) {
/* 255 */       boolean bool = (this.handle.getTag() == 9) ? true : false;
/* 256 */       this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), param1String, this.handle.getName(), this.handle.getDesc(), bool);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 261 */       return this.handle.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setName(String param1String) {
/* 266 */       boolean bool = (this.handle.getTag() == 9) ? true : false;
/* 267 */       this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), param1String, this.handle.getDesc(), bool);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDesc() {
/* 272 */       return this.handle.getDesc();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDesc(String param1String) {
/* 277 */       boolean bool = (this.handle.getTag() == 9) ? true : false;
/* 278 */       this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), this.handle.getName(), param1String, bool);
/*     */     }
/*     */   }
/*     */   
/* 282 */   private static final int[] H_OPCODES = new int[] { 0, 180, 178, 181, 179, 182, 184, 183, 183, 185 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getOpcode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setOpcode(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getOwner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setOwner(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setName(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getDesc();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setDesc(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 360 */     String str = Bytecode.getOpcodeName(getOpcode());
/* 361 */     return String.format("%s for %s.%s%s%s", new Object[] { str, getOwner(), getName(), isField() ? ":" : "", getDesc() });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 366 */     if (!(paramObject instanceof MemberRef)) {
/* 367 */       return false;
/*     */     }
/*     */     
/* 370 */     MemberRef memberRef = (MemberRef)paramObject;
/* 371 */     return (getOpcode() == memberRef.getOpcode() && 
/* 372 */       getOwner().equals(memberRef.getOwner()) && 
/* 373 */       getName().equals(memberRef.getName()) && 
/* 374 */       getDesc().equals(memberRef.getDesc()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 379 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   static int opcodeFromTag(int paramInt) {
/* 383 */     return (paramInt >= 0 && paramInt < H_OPCODES.length) ? H_OPCODES[paramInt] : 0;
/*     */   }
/*     */   
/*     */   static int tagFromOpcode(int paramInt) {
/* 387 */     for (byte b = 1; b < H_OPCODES.length; b++) {
/* 388 */       if (H_OPCODES[b] == paramInt) {
/* 389 */         return b;
/*     */       }
/*     */     } 
/* 392 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\struct\MemberRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */