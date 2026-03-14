/*     */ package org.spongepowered.asm.util.throwables;
/*     */ 
/*     */ import java.util.ListIterator;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
/*     */ import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyntheticBridgeException
/*     */   extends MixinException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Problem problem;
/*     */   private final String name;
/*     */   private final String desc;
/*     */   private final int index;
/*     */   private final AbstractInsnNode a;
/*     */   private final AbstractInsnNode b;
/*     */   
/*     */   public enum Problem
/*     */   {
/*  58 */     BAD_INSN("Conflicting opcodes %4$s and %5$s at offset %3$d in synthetic bridge method %1$s%2$s"),
/*  59 */     BAD_LOAD("Conflicting variable access at offset %3$d in synthetic bridge method %1$s%2$s"),
/*  60 */     BAD_CAST("Conflicting type cast at offset %3$d in synthetic bridge method %1$s%2$s"),
/*  61 */     BAD_INVOKE_NAME("Conflicting synthetic bridge target method name in synthetic bridge method %1$s%2$s Existing:%6$s Incoming:%7$s"),
/*  62 */     BAD_INVOKE_DESC("Conflicting synthetic bridge target method descriptor in synthetic bridge method %1$s%2$s Existing:%8$s Incoming:%9$s"),
/*  63 */     BAD_LENGTH("Mismatched bridge method length for synthetic bridge method %1$s%2$s unexpected extra opcode at offset %3$d");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Problem(String param1String1) {
/*  68 */       this.message = param1String1;
/*     */     }
/*     */     
/*     */     String getMessage(String param1String1, String param1String2, int param1Int, AbstractInsnNode param1AbstractInsnNode1, AbstractInsnNode param1AbstractInsnNode2) {
/*  72 */       return String.format(this.message, new Object[] { param1String1, param1String2, Integer.valueOf(param1Int), Bytecode.getOpcodeName(param1AbstractInsnNode1), Bytecode.getOpcodeName(param1AbstractInsnNode1), 
/*  73 */             getInsnName(param1AbstractInsnNode1), getInsnName(param1AbstractInsnNode2), getInsnDesc(param1AbstractInsnNode1), getInsnDesc(param1AbstractInsnNode2) });
/*     */     }
/*     */     
/*     */     private static String getInsnName(AbstractInsnNode param1AbstractInsnNode) {
/*  77 */       return (param1AbstractInsnNode instanceof MethodInsnNode) ? ((MethodInsnNode)param1AbstractInsnNode).name : "";
/*     */     }
/*     */     
/*     */     private static String getInsnDesc(AbstractInsnNode param1AbstractInsnNode) {
/*  81 */       return (param1AbstractInsnNode instanceof MethodInsnNode) ? ((MethodInsnNode)param1AbstractInsnNode).desc : "";
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
/*     */   public SyntheticBridgeException(Problem paramProblem, String paramString1, String paramString2, int paramInt, AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 108 */     super(paramProblem.getMessage(paramString1, paramString2, paramInt, paramAbstractInsnNode1, paramAbstractInsnNode2));
/* 109 */     this.problem = paramProblem;
/* 110 */     this.name = paramString1;
/* 111 */     this.desc = paramString2;
/* 112 */     this.index = paramInt;
/* 113 */     this.a = paramAbstractInsnNode1;
/* 114 */     this.b = paramAbstractInsnNode2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printAnalysis(IMixinContext paramIMixinContext, MethodNode paramMethodNode1, MethodNode paramMethodNode2) {
/* 123 */     PrettyPrinter prettyPrinter = new PrettyPrinter();
/* 124 */     prettyPrinter.addWrapped(100, getMessage(), new Object[0]).hr();
/* 125 */     prettyPrinter.add().kv("Method", this.name + this.desc).kv("Problem Type", this.problem).add().hr();
/* 126 */     String str1 = (String)Annotations.getValue(Annotations.getVisible(paramMethodNode1, MixinMerged.class), "mixin");
/* 127 */     String str2 = (str1 != null) ? str1 : paramIMixinContext.getTargetClassRef().replace('/', '.');
/* 128 */     printMethod(prettyPrinter.add("Existing method").add().kv("Owner", str2).add(), paramMethodNode1).hr();
/* 129 */     printMethod(prettyPrinter.add("Incoming method").add().kv("Owner", paramIMixinContext.getClassRef().replace('/', '.')).add(), paramMethodNode2).hr();
/* 130 */     printProblem(prettyPrinter, paramIMixinContext, paramMethodNode1, paramMethodNode2).print(System.err);
/*     */   }
/*     */   
/*     */   private PrettyPrinter printMethod(PrettyPrinter paramPrettyPrinter, MethodNode paramMethodNode) {
/* 134 */     byte b = 0;
/* 135 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); b++) {
/* 136 */       paramPrettyPrinter.kv((b == this.index) ? ">>>>" : "", Bytecode.describeNode(listIterator.next()));
/*     */     }
/* 138 */     return paramPrettyPrinter.add(); } private PrettyPrinter printProblem(PrettyPrinter paramPrettyPrinter, IMixinContext paramIMixinContext, MethodNode paramMethodNode1, MethodNode paramMethodNode2) { ListIterator<AbstractInsnNode> listIterator1, listIterator2; Type[] arrayOfType1, arrayOfType2; byte b1; Type type2, type3;
/*     */     MethodInsnNode methodInsnNode1, methodInsnNode2;
/*     */     Type arrayOfType3[], arrayOfType4[], type4, type5;
/*     */     byte b2;
/* 142 */     Type type1 = Type.getObjectType(paramIMixinContext.getTargetClassRef());
/*     */     
/* 144 */     paramPrettyPrinter.add("Analysis").add();
/* 145 */     switch (this.problem) {
/*     */       case BAD_INSN:
/* 147 */         paramPrettyPrinter.add("The bridge methods are not compatible because they contain incompatible opcodes");
/* 148 */         paramPrettyPrinter.add("at index " + this.index + ":").add();
/* 149 */         paramPrettyPrinter.kv("Existing opcode: %s", Bytecode.getOpcodeName(this.a));
/* 150 */         paramPrettyPrinter.kv("Incoming opcode: %s", Bytecode.getOpcodeName(this.b)).add();
/* 151 */         paramPrettyPrinter.add("This implies that the bridge methods are from different interfaces. This problem");
/* 152 */         paramPrettyPrinter.add("may not be resolvable without changing the base interfaces.").add();
/*     */         break;
/*     */       
/*     */       case BAD_LOAD:
/* 156 */         paramPrettyPrinter.add("The bridge methods are not compatible because they contain different variables at");
/* 157 */         paramPrettyPrinter.add("opcode index " + this.index + ".").add();
/*     */         
/* 159 */         listIterator1 = paramMethodNode1.instructions.iterator();
/* 160 */         listIterator2 = paramMethodNode2.instructions.iterator();
/*     */         
/* 162 */         arrayOfType1 = Type.getArgumentTypes(paramMethodNode1.desc);
/* 163 */         arrayOfType2 = Type.getArgumentTypes(paramMethodNode2.desc);
/* 164 */         for (b1 = 0; listIterator1.hasNext() && listIterator2.hasNext(); b1++) {
/* 165 */           AbstractInsnNode abstractInsnNode1 = listIterator1.next();
/* 166 */           AbstractInsnNode abstractInsnNode2 = listIterator2.next();
/* 167 */           if (abstractInsnNode1 instanceof VarInsnNode && abstractInsnNode2 instanceof VarInsnNode) {
/* 168 */             VarInsnNode varInsnNode1 = (VarInsnNode)abstractInsnNode1;
/* 169 */             VarInsnNode varInsnNode2 = (VarInsnNode)abstractInsnNode2;
/*     */             
/* 171 */             Type type6 = (varInsnNode1.var > 0) ? arrayOfType1[varInsnNode1.var - 1] : type1;
/* 172 */             Type type7 = (varInsnNode2.var > 0) ? arrayOfType2[varInsnNode2.var - 1] : type1;
/* 173 */             paramPrettyPrinter.kv("Target " + b1, "%8s %-2d %s", new Object[] { Bytecode.getOpcodeName((AbstractInsnNode)varInsnNode1), Integer.valueOf(varInsnNode1.var), type6 });
/* 174 */             paramPrettyPrinter.kv("Incoming " + b1, "%8s %-2d %s", new Object[] { Bytecode.getOpcodeName((AbstractInsnNode)varInsnNode2), Integer.valueOf(varInsnNode2.var), type7 });
/*     */             
/* 176 */             if (type6.equals(type7)) {
/* 177 */               paramPrettyPrinter.kv("", "Types match: %s", new Object[] { type6 });
/* 178 */             } else if (type6.getSort() != type7.getSort()) {
/* 179 */               paramPrettyPrinter.kv("", "Types are incompatible");
/* 180 */             } else if (type6.getSort() == 10) {
/* 181 */               ClassInfo classInfo = ClassInfo.getCommonSuperClassOrInterface(type6, type7);
/* 182 */               paramPrettyPrinter.kv("", "Common supertype: %s", new Object[] { classInfo });
/*     */             } 
/*     */             
/* 185 */             paramPrettyPrinter.add();
/*     */           } 
/*     */         } 
/*     */         
/* 189 */         paramPrettyPrinter.add("Since this probably means that the methods come from different interfaces, you");
/* 190 */         paramPrettyPrinter.add("may have a \"multiple inheritance\" problem, it may not be possible to implement");
/* 191 */         paramPrettyPrinter.add("both root interfaces");
/*     */         break;
/*     */       
/*     */       case BAD_CAST:
/* 195 */         paramPrettyPrinter.add("Incompatible CHECKCAST encountered at opcode " + this.index + ", this could indicate that the bridge");
/* 196 */         paramPrettyPrinter.add("is casting down for contravariant generic types. It may be possible to coalesce the");
/* 197 */         paramPrettyPrinter.add("bridges by adjusting the types in the target method.").add();
/*     */         
/* 199 */         type2 = Type.getObjectType(((TypeInsnNode)this.a).desc);
/* 200 */         type3 = Type.getObjectType(((TypeInsnNode)this.b).desc);
/* 201 */         paramPrettyPrinter.kv("Target type", type2);
/* 202 */         paramPrettyPrinter.kv("Incoming type", type3);
/* 203 */         paramPrettyPrinter.kv("Common supertype", ClassInfo.getCommonSuperClassOrInterface(type2, type3)).add();
/*     */         break;
/*     */       
/*     */       case BAD_INVOKE_NAME:
/* 207 */         paramPrettyPrinter.add("Incompatible invocation targets in synthetic bridge. This is extremely unusual");
/* 208 */         paramPrettyPrinter.add("and implies that a remapping transformer has incorrectly remapped a method. This");
/* 209 */         paramPrettyPrinter.add("is an unrecoverable error.");
/*     */         break;
/*     */       
/*     */       case BAD_INVOKE_DESC:
/* 213 */         methodInsnNode1 = (MethodInsnNode)this.a;
/* 214 */         methodInsnNode2 = (MethodInsnNode)this.b;
/*     */         
/* 216 */         arrayOfType3 = Type.getArgumentTypes(methodInsnNode1.desc);
/* 217 */         arrayOfType4 = Type.getArgumentTypes(methodInsnNode2.desc);
/*     */         
/* 219 */         if (arrayOfType3.length != arrayOfType4.length) {
/* 220 */           int i = (Type.getArgumentTypes(paramMethodNode1.desc)).length;
/* 221 */           String str = (arrayOfType3.length == i) ? "The TARGET" : ((arrayOfType4.length == i) ? " The INCOMING" : "NEITHER");
/*     */           
/* 223 */           paramPrettyPrinter.add("Mismatched invocation descriptors in synthetic bridge implies that a remapping");
/* 224 */           paramPrettyPrinter.add("transformer has incorrectly coalesced a bridge method with a conflicting name.");
/* 225 */           paramPrettyPrinter.add("Overlapping bridge methods should always have the same number of arguments, yet");
/* 226 */           paramPrettyPrinter.add("the target method has %d arguments, the incoming method has %d. This is an", new Object[] { Integer.valueOf(arrayOfType3.length), Integer.valueOf(arrayOfType4.length) });
/* 227 */           paramPrettyPrinter.add("unrecoverable error. %s method has the expected arg count of %d", new Object[] { str, Integer.valueOf(i) });
/*     */           
/*     */           break;
/*     */         } 
/* 231 */         type4 = Type.getReturnType(methodInsnNode1.desc);
/* 232 */         type5 = Type.getReturnType(methodInsnNode2.desc);
/*     */         
/* 234 */         paramPrettyPrinter.add("Incompatible invocation descriptors in synthetic bridge implies that generified");
/* 235 */         paramPrettyPrinter.add("types are incompatible over one or more generic superclasses or interfaces. It may");
/* 236 */         paramPrettyPrinter.add("be possible to adjust the generic types on implemented members to rectify this");
/* 237 */         paramPrettyPrinter.add("problem by coalescing the appropriate generic types.").add();
/*     */         
/* 239 */         printTypeComparison(paramPrettyPrinter, "return type", type4, type5);
/* 240 */         for (b2 = 0; b2 < arrayOfType3.length; b2++) {
/* 241 */           printTypeComparison(paramPrettyPrinter, "arg " + b2, arrayOfType3[b2], arrayOfType4[b2]);
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case BAD_LENGTH:
/* 247 */         paramPrettyPrinter.add("Mismatched bridge method length implies the bridge methods are incompatible");
/* 248 */         paramPrettyPrinter.add("and may originate from different superinterfaces. This is an unrecoverable");
/* 249 */         paramPrettyPrinter.add("error.").add();
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     return paramPrettyPrinter; }
/*     */ 
/*     */   
/*     */   private PrettyPrinter printTypeComparison(PrettyPrinter paramPrettyPrinter, String paramString, Type paramType1, Type paramType2) {
/* 260 */     paramPrettyPrinter.kv("Target " + paramString, "%s", new Object[] { paramType1 });
/* 261 */     paramPrettyPrinter.kv("Incoming " + paramString, "%s", new Object[] { paramType2 });
/*     */     
/* 263 */     if (paramType1.equals(paramType2)) {
/* 264 */       paramPrettyPrinter.kv("Analysis", "Types match: %s", new Object[] { paramType1 });
/* 265 */     } else if (paramType1.getSort() != paramType2.getSort()) {
/* 266 */       paramPrettyPrinter.kv("Analysis", "Types are incompatible");
/* 267 */     } else if (paramType1.getSort() == 10) {
/* 268 */       ClassInfo classInfo = ClassInfo.getCommonSuperClassOrInterface(paramType1, paramType2);
/* 269 */       paramPrettyPrinter.kv("Analysis", "Common supertype: L%s;", new Object[] { classInfo });
/*     */     } 
/*     */     
/* 272 */     return paramPrettyPrinter.add();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\throwables\SyntheticBridgeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */