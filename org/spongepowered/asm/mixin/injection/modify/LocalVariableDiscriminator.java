/*     */ package org.spongepowered.asm.mixin.injection.modify;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.Target;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.Locals;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVariableDiscriminator
/*     */ {
/*     */   private final boolean argsOnly;
/*     */   private final int ordinal;
/*     */   private final int index;
/*     */   private final Set<String> names;
/*     */   private final boolean print;
/*     */   
/*     */   public static class Context
/*     */     implements PrettyPrinter.IPrettyPrintable
/*     */   {
/*     */     final Target target;
/*     */     final Type returnType;
/*     */     final AbstractInsnNode node;
/*     */     final int baseArgIndex;
/*     */     final Local[] locals;
/*     */     private final boolean isStatic;
/*     */     
/*     */     public class Local
/*     */     {
/*  68 */       int ord = 0;
/*     */ 
/*     */ 
/*     */       
/*     */       String name;
/*     */ 
/*     */ 
/*     */       
/*     */       Type type;
/*     */ 
/*     */ 
/*     */       
/*     */       public Local(String param2String, Type param2Type) {
/*  81 */         this.name = param2String;
/*  82 */         this.type = param2Type;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  87 */         return String.format("Local[ordinal=%d, name=%s, type=%s]", new Object[] { Integer.valueOf(this.ord), this.name, this.type });
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Context(Type param1Type, boolean param1Boolean, Target param1Target, AbstractInsnNode param1AbstractInsnNode) {
/* 125 */       this.isStatic = Bytecode.methodIsStatic(param1Target.method);
/* 126 */       this.returnType = param1Type;
/* 127 */       this.target = param1Target;
/* 128 */       this.node = param1AbstractInsnNode;
/* 129 */       this.baseArgIndex = this.isStatic ? 0 : 1;
/* 130 */       this.locals = initLocals(param1Target, param1Boolean, param1AbstractInsnNode);
/* 131 */       initOrdinals();
/*     */     }
/*     */     
/*     */     private Local[] initLocals(Target param1Target, boolean param1Boolean, AbstractInsnNode param1AbstractInsnNode) {
/* 135 */       if (!param1Boolean) {
/* 136 */         LocalVariableNode[] arrayOfLocalVariableNode = Locals.getLocalsAt(param1Target.classNode, param1Target.method, param1AbstractInsnNode);
/* 137 */         if (arrayOfLocalVariableNode != null) {
/* 138 */           Local[] arrayOfLocal1 = new Local[arrayOfLocalVariableNode.length];
/* 139 */           for (byte b = 0; b < arrayOfLocalVariableNode.length; b++) {
/* 140 */             if (arrayOfLocalVariableNode[b] != null) {
/* 141 */               arrayOfLocal1[b] = new Local((arrayOfLocalVariableNode[b]).name, Type.getType((arrayOfLocalVariableNode[b]).desc));
/*     */             }
/*     */           } 
/* 144 */           return arrayOfLocal1;
/*     */         } 
/*     */       } 
/*     */       
/* 148 */       Local[] arrayOfLocal = new Local[this.baseArgIndex + param1Target.arguments.length];
/* 149 */       if (!this.isStatic) {
/* 150 */         arrayOfLocal[0] = new Local("this", Type.getType(param1Target.classNode.name));
/*     */       }
/* 152 */       for (int i = this.baseArgIndex; i < arrayOfLocal.length; i++) {
/* 153 */         Type type = param1Target.arguments[i - this.baseArgIndex];
/* 154 */         arrayOfLocal[i] = new Local("arg" + i, type);
/*     */       } 
/* 156 */       return arrayOfLocal;
/*     */     }
/*     */     
/*     */     private void initOrdinals() {
/* 160 */       HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 161 */       for (byte b = 0; b < this.locals.length; b++) {
/* 162 */         Integer integer = Integer.valueOf(0);
/* 163 */         if (this.locals[b] != null) {
/* 164 */           integer = (Integer)hashMap.get((this.locals[b]).type);
/* 165 */           hashMap.put((this.locals[b]).type, integer = Integer.valueOf((integer == null) ? 0 : (integer.intValue() + 1)));
/* 166 */           (this.locals[b]).ord = integer.intValue();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void print(PrettyPrinter param1PrettyPrinter) {
/* 173 */       param1PrettyPrinter.add("%5s  %7s  %30s  %-50s  %s", new Object[] { "INDEX", "ORDINAL", "TYPE", "NAME", "CANDIDATE" });
/* 174 */       for (int i = this.baseArgIndex; i < this.locals.length; i++) {
/* 175 */         Local local = this.locals[i];
/* 176 */         if (local != null) {
/* 177 */           Type type = local.type;
/* 178 */           String str1 = local.name;
/* 179 */           int j = local.ord;
/* 180 */           String str2 = this.returnType.equals(type) ? "YES" : "-";
/* 181 */           param1PrettyPrinter.add("[%3d]    [%3d]  %30s  %-50s  %s", new Object[] { Integer.valueOf(i), Integer.valueOf(j), SignaturePrinter.getTypeName(type, false), str1, str2 });
/* 182 */         } else if (i > 0) {
/* 183 */           Local local1 = this.locals[i - 1];
/* 184 */           boolean bool = (local1 != null && local1.type != null && local1.type.getSize() > 1) ? true : false;
/* 185 */           param1PrettyPrinter.add("[%3d]           %30s", new Object[] { Integer.valueOf(i), bool ? "<top>" : "-" });
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Local
/*     */   {
/*     */     int ord = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     String name;
/*     */ 
/*     */ 
/*     */     
/*     */     Type type;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Local(String param1String, Type param1Type) {
/*     */       this.name = param1String;
/*     */       this.type = param1Type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*     */       return String.format("Local[ordinal=%d, name=%s, type=%s]", new Object[] { Integer.valueOf(this.ord), this.name, this.type });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalVariableDiscriminator(boolean paramBoolean1, int paramInt1, int paramInt2, Set<String> paramSet, boolean paramBoolean2) {
/* 226 */     this.argsOnly = paramBoolean1;
/* 227 */     this.ordinal = paramInt1;
/* 228 */     this.index = paramInt2;
/* 229 */     this.names = Collections.unmodifiableSet(paramSet);
/* 230 */     this.print = paramBoolean2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArgsOnly() {
/* 238 */     return this.argsOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrdinal() {
/* 245 */     return this.ordinal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 252 */     return this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getNames() {
/* 259 */     return this.names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNames() {
/* 266 */     return !this.names.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean printLVT() {
/* 273 */     return this.print;
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
/*     */   protected boolean isImplicit(Context paramContext) {
/* 286 */     return (this.ordinal < 0 && this.index < paramContext.baseArgIndex && this.names.isEmpty());
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
/*     */   public int findLocal(Type paramType, boolean paramBoolean, Target paramTarget, AbstractInsnNode paramAbstractInsnNode) {
/*     */     try {
/* 300 */       return findLocal(new Context(paramType, paramBoolean, paramTarget, paramAbstractInsnNode));
/* 301 */     } catch (InvalidImplicitDiscriminatorException invalidImplicitDiscriminatorException) {
/* 302 */       return -2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int findLocal(Context paramContext) {
/* 313 */     if (isImplicit(paramContext)) {
/* 314 */       return findImplicitLocal(paramContext);
/*     */     }
/* 316 */     return findExplicitLocal(paramContext);
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
/*     */   private int findImplicitLocal(Context paramContext) {
/* 328 */     int i = 0;
/* 329 */     byte b = 0;
/* 330 */     for (int j = paramContext.baseArgIndex; j < paramContext.locals.length; j++) {
/* 331 */       Context.Local local = paramContext.locals[j];
/* 332 */       if (local != null && local.type.equals(paramContext.returnType)) {
/*     */ 
/*     */         
/* 335 */         b++;
/* 336 */         i = j;
/*     */       } 
/*     */     } 
/* 339 */     if (b == 1) {
/* 340 */       return i;
/*     */     }
/*     */     
/* 343 */     throw new InvalidImplicitDiscriminatorException("Found " + b + " candidate variables but exactly 1 is required.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findExplicitLocal(Context paramContext) {
/* 354 */     for (int i = paramContext.baseArgIndex; i < paramContext.locals.length; i++) {
/* 355 */       Context.Local local = paramContext.locals[i];
/* 356 */       if (local != null && local.type.equals(paramContext.returnType))
/*     */       {
/*     */         
/* 359 */         if (this.ordinal > -1) {
/* 360 */           if (this.ordinal == local.ord) {
/* 361 */             return i;
/*     */           
/*     */           }
/*     */         }
/* 365 */         else if (this.index >= paramContext.baseArgIndex) {
/* 366 */           if (this.index == i) {
/* 367 */             return i;
/*     */           
/*     */           }
/*     */         }
/* 371 */         else if (this.names.contains(local.name)) {
/* 372 */           return i;
/*     */         } 
/*     */       }
/*     */     } 
/* 376 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalVariableDiscriminator parse(AnnotationNode paramAnnotationNode) {
/* 386 */     boolean bool1 = ((Boolean)Annotations.getValue(paramAnnotationNode, "argsOnly", Boolean.FALSE)).booleanValue();
/* 387 */     int i = ((Integer)Annotations.getValue(paramAnnotationNode, "ordinal", Integer.valueOf(-1))).intValue();
/* 388 */     int j = ((Integer)Annotations.getValue(paramAnnotationNode, "index", Integer.valueOf(-1))).intValue();
/* 389 */     boolean bool2 = ((Boolean)Annotations.getValue(paramAnnotationNode, "print", Boolean.FALSE)).booleanValue();
/*     */     
/* 391 */     HashSet<String> hashSet = new HashSet();
/* 392 */     List list = (List)Annotations.getValue(paramAnnotationNode, "name", null);
/* 393 */     if (list != null) {
/* 394 */       hashSet.addAll(list);
/*     */     }
/*     */     
/* 397 */     return new LocalVariableDiscriminator(bool1, i, j, hashSet, bool2);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\modify\LocalVariableDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */