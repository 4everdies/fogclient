/*     */ package org.spongepowered.asm.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.Opcodes;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.FrameNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.LabelNode;
/*     */ import org.spongepowered.asm.lib.tree.LocalVariableNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Analyzer;
/*     */ import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
/*     */ import org.spongepowered.asm.lib.tree.analysis.BasicValue;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Frame;
/*     */ import org.spongepowered.asm.lib.tree.analysis.Interpreter;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
/*     */ import org.spongepowered.asm.util.asm.MixinVerifier;
/*     */ import org.spongepowered.asm.util.throwables.LVTGeneratorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Locals
/*     */ {
/*  63 */   private static final Map<String, List<LocalVariableNode>> calculatedLocalVariables = new HashMap<String, List<LocalVariableNode>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadLocals(Type[] paramArrayOfType, InsnList paramInsnList, int paramInt1, int paramInt2) {
/*  80 */     for (; paramInt1 < paramArrayOfType.length && paramInt2 > 0; paramInt1++) {
/*  81 */       if (paramArrayOfType[paramInt1] != null) {
/*  82 */         paramInsnList.add((AbstractInsnNode)new VarInsnNode(paramArrayOfType[paramInt1].getOpcode(21), paramInt1));
/*  83 */         paramInt2--;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalVariableNode[] getLocalsAt(ClassNode paramClassNode, MethodNode paramMethodNode, AbstractInsnNode paramAbstractInsnNode) {
/* 131 */     for (byte b1 = 0; b1 < 3 && (paramAbstractInsnNode instanceof LabelNode || paramAbstractInsnNode instanceof org.spongepowered.asm.lib.tree.LineNumberNode); b1++) {
/* 132 */       paramAbstractInsnNode = nextNode(paramMethodNode.instructions, paramAbstractInsnNode);
/*     */     }
/*     */     
/* 135 */     ClassInfo classInfo = ClassInfo.forName(paramClassNode.name);
/* 136 */     if (classInfo == null) {
/* 137 */       throw new LVTGeneratorException("Could not load class metadata for " + paramClassNode.name + " generating LVT for " + paramMethodNode.name);
/*     */     }
/* 139 */     ClassInfo.Method method = classInfo.findMethod(paramMethodNode);
/* 140 */     if (method == null) {
/* 141 */       throw new LVTGeneratorException("Could not locate method metadata for " + paramMethodNode.name + " generating LVT in " + paramClassNode.name);
/*     */     }
/* 143 */     List<ClassInfo.FrameData> list = method.getFrames();
/*     */     
/* 145 */     LocalVariableNode[] arrayOfLocalVariableNode = new LocalVariableNode[paramMethodNode.maxLocals];
/* 146 */     int i = 0; byte b2 = 0;
/*     */ 
/*     */     
/* 149 */     if ((paramMethodNode.access & 0x8) == 0) {
/* 150 */       arrayOfLocalVariableNode[i++] = new LocalVariableNode("this", paramClassNode.name, null, null, null, 0);
/*     */     }
/*     */ 
/*     */     
/* 154 */     for (Type type : Type.getArgumentTypes(paramMethodNode.desc)) {
/* 155 */       arrayOfLocalVariableNode[i] = new LocalVariableNode("arg" + b2++, type.toString(), null, null, null, i);
/* 156 */       i += type.getSize();
/*     */     } 
/*     */     
/* 159 */     int j = i;
/* 160 */     byte b = -1; int k = 0;
/*     */     
/* 162 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/* 163 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/* 164 */       if (abstractInsnNode instanceof FrameNode) {
/* 165 */         b++;
/* 166 */         FrameNode frameNode = (FrameNode)abstractInsnNode;
/* 167 */         ClassInfo.FrameData frameData = (b < list.size()) ? list.get(b) : null;
/*     */         
/* 169 */         k = (frameData != null && frameData.type == 0) ? Math.min(k, frameData.locals) : frameNode.local.size();
/*     */ 
/*     */         
/* 172 */         for (byte b4 = 0, b5 = 0; b5 < arrayOfLocalVariableNode.length; b5++, b4++) {
/*     */           
/* 174 */           E e = (b4 < frameNode.local.size()) ? frameNode.local.get(b4) : null;
/*     */           
/* 176 */           if (e instanceof String) {
/* 177 */             arrayOfLocalVariableNode[b5] = getLocalVariableAt(paramClassNode, paramMethodNode, paramAbstractInsnNode, b5);
/* 178 */           } else if (e instanceof Integer) {
/* 179 */             boolean bool1 = (e == Opcodes.UNINITIALIZED_THIS || e == Opcodes.NULL) ? true : false;
/* 180 */             boolean bool2 = (e == Opcodes.INTEGER || e == Opcodes.FLOAT) ? true : false;
/* 181 */             boolean bool3 = (e == Opcodes.DOUBLE || e == Opcodes.LONG) ? true : false;
/* 182 */             if (e != Opcodes.TOP)
/*     */             {
/* 184 */               if (bool1) {
/* 185 */                 arrayOfLocalVariableNode[b5] = null;
/* 186 */               } else if (bool2 || bool3) {
/* 187 */                 arrayOfLocalVariableNode[b5] = getLocalVariableAt(paramClassNode, paramMethodNode, paramAbstractInsnNode, b5);
/*     */                 
/* 189 */                 if (bool3) {
/* 190 */                   b5++;
/* 191 */                   arrayOfLocalVariableNode[b5] = null;
/*     */                 } 
/*     */               } else {
/* 194 */                 throw new LVTGeneratorException("Unrecognised locals opcode " + e + " in locals array at position " + b4 + " in " + paramClassNode.name + "." + paramMethodNode.name + paramMethodNode.desc);
/*     */               } 
/*     */             }
/* 197 */           } else if (e == null) {
/* 198 */             if (b5 >= j && b5 >= k && k > 0) {
/* 199 */               arrayOfLocalVariableNode[b5] = null;
/*     */             }
/*     */           } else {
/* 202 */             throw new LVTGeneratorException("Invalid value " + e + " in locals array at position " + b4 + " in " + paramClassNode.name + "." + paramMethodNode.name + paramMethodNode.desc);
/*     */           }
/*     */         
/*     */         } 
/* 206 */       } else if (abstractInsnNode instanceof VarInsnNode) {
/* 207 */         VarInsnNode varInsnNode = (VarInsnNode)abstractInsnNode;
/* 208 */         arrayOfLocalVariableNode[varInsnNode.var] = getLocalVariableAt(paramClassNode, paramMethodNode, paramAbstractInsnNode, varInsnNode.var);
/*     */       } 
/*     */       
/* 211 */       if (abstractInsnNode == paramAbstractInsnNode) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 217 */     for (byte b3 = 0; b3 < arrayOfLocalVariableNode.length; b3++) {
/* 218 */       if (arrayOfLocalVariableNode[b3] != null && (arrayOfLocalVariableNode[b3]).desc == null) {
/* 219 */         arrayOfLocalVariableNode[b3] = null;
/*     */       }
/*     */     } 
/*     */     
/* 223 */     return arrayOfLocalVariableNode;
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
/*     */   public static LocalVariableNode getLocalVariableAt(ClassNode paramClassNode, MethodNode paramMethodNode, AbstractInsnNode paramAbstractInsnNode, int paramInt) {
/* 239 */     return getLocalVariableAt(paramClassNode, paramMethodNode, paramMethodNode.instructions.indexOf(paramAbstractInsnNode), paramInt);
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
/*     */   private static LocalVariableNode getLocalVariableAt(ClassNode paramClassNode, MethodNode paramMethodNode, int paramInt1, int paramInt2) {
/* 254 */     LocalVariableNode localVariableNode1 = null;
/* 255 */     LocalVariableNode localVariableNode2 = null;
/*     */     
/* 257 */     for (LocalVariableNode localVariableNode : getLocalVariableTable(paramClassNode, paramMethodNode)) {
/* 258 */       if (localVariableNode.index != paramInt2) {
/*     */         continue;
/*     */       }
/* 261 */       if (isOpcodeInRange(paramMethodNode.instructions, localVariableNode, paramInt1)) {
/* 262 */         localVariableNode1 = localVariableNode; continue;
/* 263 */       }  if (localVariableNode1 == null) {
/* 264 */         localVariableNode2 = localVariableNode;
/*     */       }
/*     */     } 
/*     */     
/* 268 */     if (localVariableNode1 == null && !paramMethodNode.localVariables.isEmpty()) {
/* 269 */       for (LocalVariableNode localVariableNode : getGeneratedLocalVariableTable(paramClassNode, paramMethodNode)) {
/* 270 */         if (localVariableNode.index == paramInt2 && isOpcodeInRange(paramMethodNode.instructions, localVariableNode, paramInt1)) {
/* 271 */           localVariableNode1 = localVariableNode;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 276 */     return (localVariableNode1 != null) ? localVariableNode1 : localVariableNode2;
/*     */   }
/*     */   
/*     */   private static boolean isOpcodeInRange(InsnList paramInsnList, LocalVariableNode paramLocalVariableNode, int paramInt) {
/* 280 */     return (paramInsnList.indexOf((AbstractInsnNode)paramLocalVariableNode.start) < paramInt && paramInsnList.indexOf((AbstractInsnNode)paramLocalVariableNode.end) > paramInt);
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
/*     */   public static List<LocalVariableNode> getLocalVariableTable(ClassNode paramClassNode, MethodNode paramMethodNode) {
/* 295 */     if (paramMethodNode.localVariables.isEmpty()) {
/* 296 */       return getGeneratedLocalVariableTable(paramClassNode, paramMethodNode);
/*     */     }
/* 298 */     return paramMethodNode.localVariables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<LocalVariableNode> getGeneratedLocalVariableTable(ClassNode paramClassNode, MethodNode paramMethodNode) {
/* 309 */     String str = String.format("%s.%s%s", new Object[] { paramClassNode.name, paramMethodNode.name, paramMethodNode.desc });
/* 310 */     List<LocalVariableNode> list = calculatedLocalVariables.get(str);
/* 311 */     if (list != null) {
/* 312 */       return list;
/*     */     }
/*     */     
/* 315 */     list = generateLocalVariableTable(paramClassNode, paramMethodNode);
/* 316 */     calculatedLocalVariables.put(str, list);
/* 317 */     return list;
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
/*     */   public static List<LocalVariableNode> generateLocalVariableTable(ClassNode paramClassNode, MethodNode paramMethodNode) {
/* 329 */     ArrayList<Type> arrayList = null;
/* 330 */     if (paramClassNode.interfaces != null) {
/* 331 */       arrayList = new ArrayList();
/* 332 */       for (String str : paramClassNode.interfaces) {
/* 333 */         arrayList.add(Type.getObjectType(str));
/*     */       }
/*     */     } 
/*     */     
/* 337 */     Type type = null;
/* 338 */     if (paramClassNode.superName != null) {
/* 339 */       type = Type.getObjectType(paramClassNode.superName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 344 */     Analyzer analyzer = new Analyzer((Interpreter)new MixinVerifier(Type.getObjectType(paramClassNode.name), type, arrayList, false));
/*     */     try {
/* 346 */       analyzer.analyze(paramClassNode.name, paramMethodNode);
/* 347 */     } catch (AnalyzerException analyzerException) {
/* 348 */       analyzerException.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 352 */     Frame[] arrayOfFrame = analyzer.getFrames();
/*     */ 
/*     */     
/* 355 */     int i = paramMethodNode.instructions.size();
/*     */ 
/*     */     
/* 358 */     ArrayList<LocalVariableNode> arrayList1 = new ArrayList();
/*     */     
/* 360 */     LocalVariableNode[] arrayOfLocalVariableNode = new LocalVariableNode[paramMethodNode.maxLocals];
/* 361 */     BasicValue[] arrayOfBasicValue = new BasicValue[paramMethodNode.maxLocals];
/* 362 */     LabelNode[] arrayOfLabelNode = new LabelNode[i];
/* 363 */     String[] arrayOfString = new String[paramMethodNode.maxLocals];
/*     */ 
/*     */     
/* 366 */     for (byte b = 0; b < i; b++) {
/* 367 */       Frame frame = arrayOfFrame[b];
/* 368 */       if (frame != null) {
/*     */ 
/*     */         
/* 371 */         LabelNode labelNode1 = null;
/*     */         
/* 373 */         for (byte b1 = 0; b1 < frame.getLocals(); b1++) {
/* 374 */           BasicValue basicValue = (BasicValue)frame.getLocal(b1);
/* 375 */           if (basicValue != null || arrayOfBasicValue[b1] != null)
/*     */           {
/*     */             
/* 378 */             if (basicValue == null || !basicValue.equals(arrayOfBasicValue[b1])) {
/*     */ 
/*     */ 
/*     */               
/* 382 */               if (labelNode1 == null) {
/* 383 */                 AbstractInsnNode abstractInsnNode = paramMethodNode.instructions.get(b);
/* 384 */                 if (abstractInsnNode instanceof LabelNode) {
/* 385 */                   labelNode1 = (LabelNode)abstractInsnNode;
/*     */                 } else {
/* 387 */                   arrayOfLabelNode[b] = labelNode1 = new LabelNode();
/*     */                 } 
/*     */               } 
/*     */               
/* 391 */               if (basicValue == null && arrayOfBasicValue[b1] != null) {
/* 392 */                 arrayList1.add(arrayOfLocalVariableNode[b1]);
/* 393 */                 (arrayOfLocalVariableNode[b1]).end = labelNode1;
/* 394 */                 arrayOfLocalVariableNode[b1] = null;
/* 395 */               } else if (basicValue != null) {
/* 396 */                 if (arrayOfBasicValue[b1] != null) {
/* 397 */                   arrayList1.add(arrayOfLocalVariableNode[b1]);
/* 398 */                   (arrayOfLocalVariableNode[b1]).end = labelNode1;
/* 399 */                   arrayOfLocalVariableNode[b1] = null;
/*     */                 } 
/*     */                 
/* 402 */                 String str = (basicValue.getType() != null) ? basicValue.getType().getDescriptor() : arrayOfString[b1];
/* 403 */                 arrayOfLocalVariableNode[b1] = new LocalVariableNode("var" + b1, str, null, labelNode1, null, b1);
/* 404 */                 if (str != null) {
/* 405 */                   arrayOfString[b1] = str;
/*     */                 }
/*     */               } 
/*     */               
/* 409 */               arrayOfBasicValue[b1] = basicValue;
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 414 */     LabelNode labelNode = null; int j;
/* 415 */     for (j = 0; j < arrayOfLocalVariableNode.length; j++) {
/* 416 */       if (arrayOfLocalVariableNode[j] != null) {
/* 417 */         if (labelNode == null) {
/* 418 */           labelNode = new LabelNode();
/* 419 */           paramMethodNode.instructions.add((AbstractInsnNode)labelNode);
/*     */         } 
/*     */         
/* 422 */         (arrayOfLocalVariableNode[j]).end = labelNode;
/* 423 */         arrayList1.add(arrayOfLocalVariableNode[j]);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 428 */     for (j = i - 1; j >= 0; j--) {
/* 429 */       if (arrayOfLabelNode[j] != null) {
/* 430 */         paramMethodNode.instructions.insert(paramMethodNode.instructions.get(j), (AbstractInsnNode)arrayOfLabelNode[j]);
/*     */       }
/*     */     } 
/*     */     
/* 434 */     return arrayList1;
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
/*     */   private static AbstractInsnNode nextNode(InsnList paramInsnList, AbstractInsnNode paramAbstractInsnNode) {
/* 446 */     int i = paramInsnList.indexOf(paramAbstractInsnNode) + 1;
/* 447 */     if (i > 0 && i < paramInsnList.size()) {
/* 448 */       return paramInsnList.get(i);
/*     */     }
/* 450 */     return paramAbstractInsnNode;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\Locals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */