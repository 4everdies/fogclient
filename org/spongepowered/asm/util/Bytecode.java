/*      */ package org.spongepowered.asm.util;
/*      */ 
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.spongepowered.asm.lib.ClassReader;
/*      */ import org.spongepowered.asm.lib.ClassVisitor;
/*      */ import org.spongepowered.asm.lib.ClassWriter;
/*      */ import org.spongepowered.asm.lib.MethodVisitor;
/*      */ import org.spongepowered.asm.lib.Opcodes;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldNode;
/*      */ import org.spongepowered.asm.lib.tree.FrameNode;
/*      */ import org.spongepowered.asm.lib.tree.InsnList;
/*      */ import org.spongepowered.asm.lib.tree.IntInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.JumpInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.LabelNode;
/*      */ import org.spongepowered.asm.lib.tree.LdcInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.LineNumberNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.lib.tree.TypeInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.VarInsnNode;
/*      */ import org.spongepowered.asm.lib.util.CheckClassAdapter;
/*      */ import org.spongepowered.asm.lib.util.TraceClassVisitor;
/*      */ import org.spongepowered.asm.mixin.Debug;
/*      */ import org.spongepowered.asm.mixin.Final;
/*      */ import org.spongepowered.asm.mixin.Intrinsic;
/*      */ import org.spongepowered.asm.mixin.Overwrite;
/*      */ import org.spongepowered.asm.util.throwables.SyntheticBridgeException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Bytecode
/*      */ {
/*      */   public enum Visibility
/*      */   {
/*   76 */     PRIVATE(2),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   81 */     PROTECTED(4),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   86 */     PACKAGE(0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   91 */     PUBLIC(1);
/*      */     
/*      */     static final int MASK = 7;
/*      */     
/*      */     final int access;
/*      */     
/*      */     Visibility(int param1Int1) {
/*   98 */       this.access = param1Int1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final int[] CONSTANTS_INT = new int[] { 2, 3, 4, 5, 6, 7, 8 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   public static final int[] CONSTANTS_FLOAT = new int[] { 11, 12, 13 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   public static final int[] CONSTANTS_DOUBLE = new int[] { 14, 15 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   public static final int[] CONSTANTS_LONG = new int[] { 9, 10 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  134 */   public static final int[] CONSTANTS_ALL = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   private static final Object[] CONSTANTS_VALUES = new Object[] { null, 
/*      */       
/*  148 */       Integer.valueOf(-1), 
/*  149 */       Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), 
/*  150 */       Long.valueOf(0L), Long.valueOf(1L), 
/*  151 */       Float.valueOf(0.0F), Float.valueOf(1.0F), Float.valueOf(2.0F), 
/*  152 */       Double.valueOf(0.0D), Double.valueOf(1.0D) };
/*      */ 
/*      */   
/*  155 */   private static final String[] CONSTANTS_TYPES = new String[] { null, "I", "I", "I", "I", "I", "I", "I", "J", "J", "F", "F", "F", "D", "D", "I", "I" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   private static final String[] BOXING_TYPES = new String[] { null, "java/lang/Boolean", "java/lang/Character", "java/lang/Byte", "java/lang/Short", "java/lang/Integer", "java/lang/Float", "java/lang/Long", "java/lang/Double", null, null, null };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  187 */   private static final String[] UNBOXING_METHODS = new String[] { null, "booleanValue", "charValue", "byteValue", "shortValue", "intValue", "floatValue", "longValue", "doubleValue", null, null, null };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  205 */   private static final Class<?>[] MERGEABLE_MIXIN_ANNOTATIONS = new Class[] { Overwrite.class, Intrinsic.class, Final.class, Debug.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  212 */   private static Pattern mergeableAnnotationPattern = getMergeableAnnotationPattern();
/*      */   
/*  214 */   private static final Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MethodNode findMethod(ClassNode paramClassNode, String paramString1, String paramString2) {
/*  229 */     for (MethodNode methodNode : paramClassNode.methods) {
/*  230 */       if (methodNode.name.equals(paramString1) && methodNode.desc.equals(paramString2)) {
/*  231 */         return methodNode;
/*      */       }
/*      */     } 
/*  234 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractInsnNode findInsn(MethodNode paramMethodNode, int paramInt) {
/*  245 */     ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator();
/*  246 */     while (listIterator.hasNext()) {
/*  247 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  248 */       if (abstractInsnNode.getOpcode() == paramInt) {
/*  249 */         return abstractInsnNode;
/*      */       }
/*      */     } 
/*  252 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MethodInsnNode findSuperInit(MethodNode paramMethodNode, String paramString) {
/*  265 */     if (!"<init>".equals(paramMethodNode.name)) {
/*  266 */       return null;
/*      */     }
/*      */     
/*  269 */     byte b = 0;
/*  270 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  271 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  272 */       if (abstractInsnNode instanceof TypeInsnNode && abstractInsnNode.getOpcode() == 187) {
/*  273 */         b++; continue;
/*  274 */       }  if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() == 183) {
/*  275 */         MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
/*  276 */         if ("<init>".equals(methodInsnNode.name)) {
/*  277 */           if (b > 0) {
/*  278 */             b--; continue;
/*  279 */           }  if (methodInsnNode.owner.equals(paramString)) {
/*  280 */             return methodInsnNode;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  285 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void textify(ClassNode paramClassNode, OutputStream paramOutputStream) {
/*  296 */     paramClassNode.accept((ClassVisitor)new TraceClassVisitor(new PrintWriter(paramOutputStream)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void textify(MethodNode paramMethodNode, OutputStream paramOutputStream) {
/*  307 */     TraceClassVisitor traceClassVisitor = new TraceClassVisitor(new PrintWriter(paramOutputStream));
/*  308 */     MethodVisitor methodVisitor = traceClassVisitor.visitMethod(paramMethodNode.access, paramMethodNode.name, paramMethodNode.desc, paramMethodNode.signature, (String[])paramMethodNode.exceptions
/*  309 */         .toArray((Object[])new String[0]));
/*  310 */     paramMethodNode.accept(methodVisitor);
/*  311 */     traceClassVisitor.visitEnd();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void dumpClass(ClassNode paramClassNode) {
/*  320 */     ClassWriter classWriter = new ClassWriter(3);
/*  321 */     paramClassNode.accept((ClassVisitor)classWriter);
/*  322 */     dumpClass(classWriter.toByteArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void dumpClass(byte[] paramArrayOfbyte) {
/*  331 */     ClassReader classReader = new ClassReader(paramArrayOfbyte);
/*  332 */     CheckClassAdapter.verify(classReader, true, new PrintWriter(System.out));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printMethodWithOpcodeIndices(MethodNode paramMethodNode) {
/*  341 */     System.err.printf("%s%s\n", new Object[] { paramMethodNode.name, paramMethodNode.desc });
/*  342 */     byte b = 0;
/*  343 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext();) {
/*  344 */       System.err.printf("[%4d] %s\n", new Object[] { Integer.valueOf(b++), describeNode(listIterator.next()) });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printMethod(MethodNode paramMethodNode) {
/*  354 */     System.err.printf("%s%s\n", new Object[] { paramMethodNode.name, paramMethodNode.desc });
/*  355 */     for (ListIterator<AbstractInsnNode> listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  356 */       System.err.print("  ");
/*  357 */       printNode(listIterator.next());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printNode(AbstractInsnNode paramAbstractInsnNode) {
/*  367 */     System.err.printf("%s\n", new Object[] { describeNode(paramAbstractInsnNode) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String describeNode(AbstractInsnNode paramAbstractInsnNode) {
/*  377 */     if (paramAbstractInsnNode == null) {
/*  378 */       return String.format("   %-14s ", new Object[] { "null" });
/*      */     }
/*      */     
/*  381 */     if (paramAbstractInsnNode instanceof LabelNode) {
/*  382 */       return String.format("[%s]", new Object[] { ((LabelNode)paramAbstractInsnNode).getLabel() });
/*      */     }
/*      */     
/*  385 */     String str = String.format("   %-14s ", new Object[] { paramAbstractInsnNode.getClass().getSimpleName().replace("Node", "") });
/*  386 */     if (paramAbstractInsnNode instanceof JumpInsnNode) {
/*  387 */       str = str + String.format("[%s] [%s]", new Object[] { getOpcodeName(paramAbstractInsnNode), ((JumpInsnNode)paramAbstractInsnNode).label.getLabel() });
/*  388 */     } else if (paramAbstractInsnNode instanceof VarInsnNode) {
/*  389 */       str = str + String.format("[%s] %d", new Object[] { getOpcodeName(paramAbstractInsnNode), Integer.valueOf(((VarInsnNode)paramAbstractInsnNode).var) });
/*  390 */     } else if (paramAbstractInsnNode instanceof MethodInsnNode) {
/*  391 */       MethodInsnNode methodInsnNode = (MethodInsnNode)paramAbstractInsnNode;
/*  392 */       str = str + String.format("[%s] %s %s %s", new Object[] { getOpcodeName(paramAbstractInsnNode), methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc });
/*  393 */     } else if (paramAbstractInsnNode instanceof FieldInsnNode) {
/*  394 */       FieldInsnNode fieldInsnNode = (FieldInsnNode)paramAbstractInsnNode;
/*  395 */       str = str + String.format("[%s] %s %s %s", new Object[] { getOpcodeName(paramAbstractInsnNode), fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc });
/*  396 */     } else if (paramAbstractInsnNode instanceof LineNumberNode) {
/*  397 */       LineNumberNode lineNumberNode = (LineNumberNode)paramAbstractInsnNode;
/*  398 */       str = str + String.format("LINE=[%d] LABEL=[%s]", new Object[] { Integer.valueOf(lineNumberNode.line), lineNumberNode.start.getLabel() });
/*  399 */     } else if (paramAbstractInsnNode instanceof LdcInsnNode) {
/*  400 */       str = str + ((LdcInsnNode)paramAbstractInsnNode).cst;
/*  401 */     } else if (paramAbstractInsnNode instanceof IntInsnNode) {
/*  402 */       str = str + ((IntInsnNode)paramAbstractInsnNode).operand;
/*  403 */     } else if (paramAbstractInsnNode instanceof FrameNode) {
/*  404 */       str = str + String.format("[%s] ", new Object[] { getOpcodeName(((FrameNode)paramAbstractInsnNode).type, "H_INVOKEINTERFACE", -1) });
/*      */     } else {
/*  406 */       str = str + String.format("[%s] ", new Object[] { getOpcodeName(paramAbstractInsnNode) });
/*      */     } 
/*  408 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getOpcodeName(AbstractInsnNode paramAbstractInsnNode) {
/*  420 */     return (paramAbstractInsnNode != null) ? getOpcodeName(paramAbstractInsnNode.getOpcode()) : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getOpcodeName(int paramInt) {
/*  432 */     return getOpcodeName(paramInt, "UNINITIALIZED_THIS", 1);
/*      */   }
/*      */   
/*      */   private static String getOpcodeName(int paramInt1, String paramString, int paramInt2) {
/*  436 */     if (paramInt1 >= paramInt2) {
/*  437 */       boolean bool = false;
/*      */       
/*      */       try {
/*  440 */         for (Field field : Opcodes.class.getDeclaredFields()) {
/*  441 */           if (bool || field.getName().equals(paramString)) {
/*      */ 
/*      */             
/*  444 */             bool = true;
/*  445 */             if (field.getType() == int.class && field.getInt(null) == paramInt1)
/*  446 */               return field.getName(); 
/*      */           } 
/*      */         } 
/*  449 */       } catch (Exception exception) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  454 */     return (paramInt1 >= 0) ? String.valueOf(paramInt1) : "UNKNOWN";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean methodHasLineNumbers(MethodNode paramMethodNode) {
/*  464 */     for (ListIterator listIterator = paramMethodNode.instructions.iterator(); listIterator.hasNext();) {
/*  465 */       if (listIterator.next() instanceof LineNumberNode) {
/*  466 */         return true;
/*      */       }
/*      */     } 
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean methodIsStatic(MethodNode paramMethodNode) {
/*  479 */     return ((paramMethodNode.access & 0x8) == 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean fieldIsStatic(FieldNode paramFieldNode) {
/*  489 */     return ((paramFieldNode.access & 0x8) == 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getFirstNonArgLocalIndex(MethodNode paramMethodNode) {
/*  503 */     return getFirstNonArgLocalIndex(Type.getArgumentTypes(paramMethodNode.desc), ((paramMethodNode.access & 0x8) == 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getFirstNonArgLocalIndex(Type[] paramArrayOfType, boolean paramBoolean) {
/*  519 */     return getArgsSize(paramArrayOfType) + (paramBoolean ? 1 : 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getArgsSize(Type[] paramArrayOfType) {
/*  530 */     int i = 0;
/*      */     
/*  532 */     for (Type type : paramArrayOfType) {
/*  533 */       i += type.getSize();
/*      */     }
/*      */     
/*  536 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadArgs(Type[] paramArrayOfType, InsnList paramInsnList, int paramInt) {
/*  548 */     loadArgs(paramArrayOfType, paramInsnList, paramInt, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadArgs(Type[] paramArrayOfType, InsnList paramInsnList, int paramInt1, int paramInt2) {
/*  561 */     loadArgs(paramArrayOfType, paramInsnList, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadArgs(Type[] paramArrayOfType1, InsnList paramInsnList, int paramInt1, int paramInt2, Type[] paramArrayOfType2) {
/*  575 */     int i = paramInt1; byte b = 0;
/*      */     
/*  577 */     for (Type type : paramArrayOfType1) {
/*  578 */       paramInsnList.add((AbstractInsnNode)new VarInsnNode(type.getOpcode(21), i));
/*  579 */       if (paramArrayOfType2 != null && b < paramArrayOfType2.length && paramArrayOfType2[b] != null) {
/*  580 */         paramInsnList.add((AbstractInsnNode)new TypeInsnNode(192, paramArrayOfType2[b].getInternalName()));
/*      */       }
/*  582 */       i += type.getSize();
/*  583 */       if (paramInt2 >= paramInt1 && i >= paramInt2) {
/*      */         return;
/*      */       }
/*  586 */       b++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<LabelNode, LabelNode> cloneLabels(InsnList paramInsnList) {
/*  599 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/*      */     
/*  601 */     for (ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator(); listIterator.hasNext(); ) {
/*  602 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*  603 */       if (abstractInsnNode instanceof LabelNode) {
/*  604 */         hashMap.put(abstractInsnNode, new LabelNode(((LabelNode)abstractInsnNode).getLabel()));
/*      */       }
/*      */     } 
/*      */     
/*  608 */     return (Map)hashMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String generateDescriptor(Object paramObject, Object... paramVarArgs) {
/*  621 */     StringBuilder stringBuilder = (new StringBuilder()).append('(');
/*      */     
/*  623 */     for (Object object : paramVarArgs) {
/*  624 */       stringBuilder.append(toDescriptor(object));
/*      */     }
/*      */     
/*  627 */     return stringBuilder.append(')').append((paramObject != null) ? toDescriptor(paramObject) : "V").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String toDescriptor(Object paramObject) {
/*  637 */     if (paramObject instanceof String)
/*  638 */       return (String)paramObject; 
/*  639 */     if (paramObject instanceof Type)
/*  640 */       return paramObject.toString(); 
/*  641 */     if (paramObject instanceof Class) {
/*  642 */       return Type.getDescriptor((Class)paramObject);
/*      */     }
/*  644 */     return (paramObject == null) ? "" : paramObject.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getDescriptor(Type[] paramArrayOfType) {
/*  655 */     return "(" + Joiner.on("").join((Object[])paramArrayOfType) + ")";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getDescriptor(Type[] paramArrayOfType, Type paramType) {
/*  666 */     return getDescriptor(paramArrayOfType) + paramType.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String changeDescriptorReturnType(String paramString1, String paramString2) {
/*  677 */     if (paramString1 == null)
/*  678 */       return null; 
/*  679 */     if (paramString2 == null) {
/*  680 */       return paramString1;
/*      */     }
/*  682 */     return paramString1.substring(0, paramString1.lastIndexOf(')') + 1) + paramString2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Class<? extends Annotation> paramClass) {
/*  693 */     return paramClass.getSimpleName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(AnnotationNode paramAnnotationNode) {
/*  704 */     return getSimpleName(paramAnnotationNode.desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(String paramString) {
/*  714 */     int i = Math.max(paramString.lastIndexOf('/'), 0);
/*  715 */     return paramString.substring(i + 1).replace(";", "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isConstant(AbstractInsnNode paramAbstractInsnNode) {
/*  726 */     if (paramAbstractInsnNode == null) {
/*  727 */       return false;
/*      */     }
/*  729 */     return Ints.contains(CONSTANTS_ALL, paramAbstractInsnNode.getOpcode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getConstant(AbstractInsnNode paramAbstractInsnNode) {
/*  741 */     if (paramAbstractInsnNode == null)
/*  742 */       return null; 
/*  743 */     if (paramAbstractInsnNode instanceof LdcInsnNode)
/*  744 */       return ((LdcInsnNode)paramAbstractInsnNode).cst; 
/*  745 */     if (paramAbstractInsnNode instanceof IntInsnNode) {
/*  746 */       int j = ((IntInsnNode)paramAbstractInsnNode).operand;
/*  747 */       if (paramAbstractInsnNode.getOpcode() == 16 || paramAbstractInsnNode.getOpcode() == 17) {
/*  748 */         return Integer.valueOf(j);
/*      */       }
/*  750 */       throw new IllegalArgumentException("IntInsnNode with invalid opcode " + paramAbstractInsnNode.getOpcode() + " in getConstant");
/*      */     } 
/*      */     
/*  753 */     int i = Ints.indexOf(CONSTANTS_ALL, paramAbstractInsnNode.getOpcode());
/*  754 */     return (i < 0) ? null : CONSTANTS_VALUES[i];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type getConstantType(AbstractInsnNode paramAbstractInsnNode) {
/*  765 */     if (paramAbstractInsnNode == null)
/*  766 */       return null; 
/*  767 */     if (paramAbstractInsnNode instanceof LdcInsnNode) {
/*  768 */       Object object = ((LdcInsnNode)paramAbstractInsnNode).cst;
/*  769 */       if (object instanceof Integer)
/*  770 */         return Type.getType("I"); 
/*  771 */       if (object instanceof Float)
/*  772 */         return Type.getType("F"); 
/*  773 */       if (object instanceof Long)
/*  774 */         return Type.getType("J"); 
/*  775 */       if (object instanceof Double)
/*  776 */         return Type.getType("D"); 
/*  777 */       if (object instanceof String)
/*  778 */         return Type.getType("Ljava/lang/String;"); 
/*  779 */       if (object instanceof Type) {
/*  780 */         return Type.getType("Ljava/lang/Class;");
/*      */       }
/*  782 */       throw new IllegalArgumentException("LdcInsnNode with invalid payload type " + object.getClass() + " in getConstant");
/*      */     } 
/*      */     
/*  785 */     int i = Ints.indexOf(CONSTANTS_ALL, paramAbstractInsnNode.getOpcode());
/*  786 */     return (i < 0) ? null : Type.getType(CONSTANTS_TYPES[i]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasFlag(ClassNode paramClassNode, int paramInt) {
/*  797 */     return ((paramClassNode.access & paramInt) == paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasFlag(MethodNode paramMethodNode, int paramInt) {
/*  808 */     return ((paramMethodNode.access & paramInt) == paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasFlag(FieldNode paramFieldNode, int paramInt) {
/*  819 */     return ((paramFieldNode.access & paramInt) == paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean compareFlags(MethodNode paramMethodNode1, MethodNode paramMethodNode2, int paramInt) {
/*  832 */     return (hasFlag(paramMethodNode1, paramInt) == hasFlag(paramMethodNode2, paramInt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean compareFlags(FieldNode paramFieldNode1, FieldNode paramFieldNode2, int paramInt) {
/*  845 */     return (hasFlag(paramFieldNode1, paramInt) == hasFlag(paramFieldNode2, paramInt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Visibility getVisibility(MethodNode paramMethodNode) {
/*  863 */     return getVisibility(paramMethodNode.access & 0x7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Visibility getVisibility(FieldNode paramFieldNode) {
/*  881 */     return getVisibility(paramFieldNode.access & 0x7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Visibility getVisibility(int paramInt) {
/*  899 */     if ((paramInt & 0x4) != 0)
/*  900 */       return Visibility.PROTECTED; 
/*  901 */     if ((paramInt & 0x2) != 0)
/*  902 */       return Visibility.PRIVATE; 
/*  903 */     if ((paramInt & 0x1) != 0) {
/*  904 */       return Visibility.PUBLIC;
/*      */     }
/*  906 */     return Visibility.PACKAGE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setVisibility(MethodNode paramMethodNode, Visibility paramVisibility) {
/*  917 */     paramMethodNode.access = setVisibility(paramMethodNode.access, paramVisibility.access);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setVisibility(FieldNode paramFieldNode, Visibility paramVisibility) {
/*  928 */     paramFieldNode.access = setVisibility(paramFieldNode.access, paramVisibility.access);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setVisibility(MethodNode paramMethodNode, int paramInt) {
/*  939 */     paramMethodNode.access = setVisibility(paramMethodNode.access, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setVisibility(FieldNode paramFieldNode, int paramInt) {
/*  950 */     paramFieldNode.access = setVisibility(paramFieldNode.access, paramInt);
/*      */   }
/*      */   
/*      */   private static int setVisibility(int paramInt1, int paramInt2) {
/*  954 */     return paramInt1 & 0xFFFFFFF8 | paramInt2 & 0x7;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getMaxLineNumber(ClassNode paramClassNode, int paramInt1, int paramInt2) {
/*  966 */     int i = 0;
/*  967 */     for (MethodNode methodNode : paramClassNode.methods) {
/*  968 */       for (ListIterator<AbstractInsnNode> listIterator = methodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  969 */         AbstractInsnNode abstractInsnNode = listIterator.next();
/*  970 */         if (abstractInsnNode instanceof LineNumberNode) {
/*  971 */           i = Math.max(i, ((LineNumberNode)abstractInsnNode).line);
/*      */         }
/*      */       } 
/*      */     } 
/*  975 */     return Math.max(paramInt1, i + paramInt2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getBoxingType(Type paramType) {
/*  986 */     return (paramType == null) ? null : BOXING_TYPES[paramType.getSort()];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getUnboxingMethod(Type paramType) {
/*  999 */     return (paramType == null) ? null : UNBOXING_METHODS[paramType.getSort()];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeAnnotations(ClassNode paramClassNode1, ClassNode paramClassNode2) {
/* 1014 */     paramClassNode2.visibleAnnotations = mergeAnnotations(paramClassNode1.visibleAnnotations, paramClassNode2.visibleAnnotations, "class", paramClassNode1.name);
/* 1015 */     paramClassNode2.invisibleAnnotations = mergeAnnotations(paramClassNode1.invisibleAnnotations, paramClassNode2.invisibleAnnotations, "class", paramClassNode1.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeAnnotations(MethodNode paramMethodNode1, MethodNode paramMethodNode2) {
/* 1030 */     paramMethodNode2.visibleAnnotations = mergeAnnotations(paramMethodNode1.visibleAnnotations, paramMethodNode2.visibleAnnotations, "method", paramMethodNode1.name);
/* 1031 */     paramMethodNode2.invisibleAnnotations = mergeAnnotations(paramMethodNode1.invisibleAnnotations, paramMethodNode2.invisibleAnnotations, "method", paramMethodNode1.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeAnnotations(FieldNode paramFieldNode1, FieldNode paramFieldNode2) {
/* 1046 */     paramFieldNode2.visibleAnnotations = mergeAnnotations(paramFieldNode1.visibleAnnotations, paramFieldNode2.visibleAnnotations, "field", paramFieldNode1.name);
/* 1047 */     paramFieldNode2.invisibleAnnotations = mergeAnnotations(paramFieldNode1.invisibleAnnotations, paramFieldNode2.invisibleAnnotations, "field", paramFieldNode1.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<AnnotationNode> mergeAnnotations(List<AnnotationNode> paramList1, List<AnnotationNode> paramList2, String paramString1, String paramString2) {
/*      */     try {
/* 1062 */       if (paramList1 == null) {
/* 1063 */         return paramList2;
/*      */       }
/*      */       
/* 1066 */       if (paramList2 == null) {
/* 1067 */         paramList2 = new ArrayList<AnnotationNode>();
/*      */       }
/*      */       
/* 1070 */       for (AnnotationNode annotationNode : paramList1) {
/* 1071 */         if (!isMergeableAnnotation(annotationNode)) {
/*      */           continue;
/*      */         }
/*      */         
/* 1075 */         for (Iterator<AnnotationNode> iterator = paramList2.iterator(); iterator.hasNext();) {
/* 1076 */           if (((AnnotationNode)iterator.next()).desc.equals(annotationNode.desc)) {
/* 1077 */             iterator.remove();
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 1082 */         paramList2.add(annotationNode);
/*      */       } 
/* 1084 */     } catch (Exception exception) {
/* 1085 */       logger.warn("Exception encountered whilst merging annotations for {} {}", new Object[] { paramString1, paramString2 });
/*      */     } 
/*      */     
/* 1088 */     return paramList2;
/*      */   }
/*      */   
/*      */   private static boolean isMergeableAnnotation(AnnotationNode paramAnnotationNode) {
/* 1092 */     if (paramAnnotationNode.desc.startsWith("L" + Constants.MIXIN_PACKAGE_REF)) {
/* 1093 */       return mergeableAnnotationPattern.matcher(paramAnnotationNode.desc).matches();
/*      */     }
/* 1095 */     return true;
/*      */   }
/*      */   
/*      */   private static Pattern getMergeableAnnotationPattern() {
/* 1099 */     StringBuilder stringBuilder = new StringBuilder("^L(");
/* 1100 */     for (byte b = 0; b < MERGEABLE_MIXIN_ANNOTATIONS.length; b++) {
/* 1101 */       if (b > 0) {
/* 1102 */         stringBuilder.append('|');
/*      */       }
/* 1104 */       stringBuilder.append(MERGEABLE_MIXIN_ANNOTATIONS[b].getName().replace('.', '/'));
/*      */     } 
/* 1106 */     return Pattern.compile(stringBuilder.append(");$").toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void compareBridgeMethods(MethodNode paramMethodNode1, MethodNode paramMethodNode2) {
/* 1117 */     ListIterator<AbstractInsnNode> listIterator1 = paramMethodNode1.instructions.iterator();
/* 1118 */     ListIterator<AbstractInsnNode> listIterator2 = paramMethodNode2.instructions.iterator();
/*      */     
/* 1120 */     byte b = 0;
/* 1121 */     for (; listIterator1.hasNext() && listIterator2.hasNext(); b++) {
/* 1122 */       AbstractInsnNode abstractInsnNode1 = listIterator1.next();
/* 1123 */       AbstractInsnNode abstractInsnNode2 = listIterator2.next();
/* 1124 */       if (!(abstractInsnNode1 instanceof LabelNode))
/*      */       {
/*      */ 
/*      */         
/* 1128 */         if (abstractInsnNode1 instanceof MethodInsnNode) {
/* 1129 */           MethodInsnNode methodInsnNode1 = (MethodInsnNode)abstractInsnNode1;
/* 1130 */           MethodInsnNode methodInsnNode2 = (MethodInsnNode)abstractInsnNode2;
/* 1131 */           if (!methodInsnNode1.name.equals(methodInsnNode2.name))
/* 1132 */             throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_NAME, paramMethodNode1.name, paramMethodNode1.desc, b, abstractInsnNode1, abstractInsnNode2); 
/* 1133 */           if (!methodInsnNode1.desc.equals(methodInsnNode2.desc))
/* 1134 */             throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_DESC, paramMethodNode1.name, paramMethodNode1.desc, b, abstractInsnNode1, abstractInsnNode2); 
/*      */         } else {
/* 1136 */           if (abstractInsnNode1.getOpcode() != abstractInsnNode2.getOpcode())
/* 1137 */             throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INSN, paramMethodNode1.name, paramMethodNode1.desc, b, abstractInsnNode1, abstractInsnNode2); 
/* 1138 */           if (abstractInsnNode1 instanceof VarInsnNode) {
/* 1139 */             VarInsnNode varInsnNode1 = (VarInsnNode)abstractInsnNode1;
/* 1140 */             VarInsnNode varInsnNode2 = (VarInsnNode)abstractInsnNode2;
/* 1141 */             if (varInsnNode1.var != varInsnNode2.var) {
/* 1142 */               throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LOAD, paramMethodNode1.name, paramMethodNode1.desc, b, abstractInsnNode1, abstractInsnNode2);
/*      */             }
/* 1144 */           } else if (abstractInsnNode1 instanceof TypeInsnNode) {
/* 1145 */             TypeInsnNode typeInsnNode1 = (TypeInsnNode)abstractInsnNode1;
/* 1146 */             TypeInsnNode typeInsnNode2 = (TypeInsnNode)abstractInsnNode2;
/* 1147 */             if (typeInsnNode1.getOpcode() == 192 && !typeInsnNode1.desc.equals(typeInsnNode2.desc))
/* 1148 */               throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_CAST, paramMethodNode1.name, paramMethodNode1.desc, b, abstractInsnNode1, abstractInsnNode2); 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1153 */     if (listIterator1.hasNext() || listIterator2.hasNext())
/* 1154 */       throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LENGTH, paramMethodNode1.name, paramMethodNode1.desc, b, null, null); 
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\Bytecode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */