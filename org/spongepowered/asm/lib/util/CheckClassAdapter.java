/*      */ package org.spongepowered.asm.lib.util;
/*      */ 
/*      */ import java.io.FileInputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*      */ import org.spongepowered.asm.lib.Attribute;
/*      */ import org.spongepowered.asm.lib.ClassReader;
/*      */ import org.spongepowered.asm.lib.ClassVisitor;
/*      */ import org.spongepowered.asm.lib.FieldVisitor;
/*      */ import org.spongepowered.asm.lib.Label;
/*      */ import org.spongepowered.asm.lib.MethodVisitor;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.TypePath;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.lib.tree.TryCatchBlockNode;
/*      */ import org.spongepowered.asm.lib.tree.analysis.Analyzer;
/*      */ import org.spongepowered.asm.lib.tree.analysis.BasicValue;
/*      */ import org.spongepowered.asm.lib.tree.analysis.Frame;
/*      */ import org.spongepowered.asm.lib.tree.analysis.Interpreter;
/*      */ import org.spongepowered.asm.lib.tree.analysis.SimpleVerifier;
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
/*      */ public class CheckClassAdapter
/*      */   extends ClassVisitor
/*      */ {
/*      */   private int version;
/*      */   private boolean start;
/*      */   private boolean source;
/*      */   private boolean outer;
/*      */   private boolean end;
/*      */   private Map<Label, Integer> labels;
/*      */   private boolean checkDataFlow;
/*      */   
/*      */   public static void main(String[] paramArrayOfString) throws Exception {
/*      */     ClassReader classReader;
/*  177 */     if (paramArrayOfString.length != 1) {
/*  178 */       System.err.println("Verifies the given class.");
/*  179 */       System.err.println("Usage: CheckClassAdapter <fully qualified class name or class file name>");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  184 */     if (paramArrayOfString[0].endsWith(".class")) {
/*  185 */       classReader = new ClassReader(new FileInputStream(paramArrayOfString[0]));
/*      */     } else {
/*  187 */       classReader = new ClassReader(paramArrayOfString[0]);
/*      */     } 
/*      */     
/*  190 */     verify(classReader, false, new PrintWriter(System.err));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void verify(ClassReader paramClassReader, ClassLoader paramClassLoader, boolean paramBoolean, PrintWriter paramPrintWriter) {
/*  211 */     ClassNode classNode = new ClassNode();
/*  212 */     paramClassReader.accept(new CheckClassAdapter((ClassVisitor)classNode, false), 2);
/*      */ 
/*      */     
/*  215 */     Type type = (classNode.superName == null) ? null : Type.getObjectType(classNode.superName);
/*  216 */     List<MethodNode> list = classNode.methods;
/*      */     
/*  218 */     ArrayList<Type> arrayList = new ArrayList();
/*  219 */     for (Iterator<String> iterator = classNode.interfaces.iterator(); iterator.hasNext();) {
/*  220 */       arrayList.add(Type.getObjectType(iterator.next()));
/*      */     }
/*      */     
/*  223 */     for (byte b = 0; b < list.size(); b++) {
/*  224 */       MethodNode methodNode = list.get(b);
/*      */       
/*  226 */       SimpleVerifier simpleVerifier = new SimpleVerifier(Type.getObjectType(classNode.name), type, arrayList, ((classNode.access & 0x200) != 0));
/*      */       
/*  228 */       Analyzer<BasicValue> analyzer = new Analyzer((Interpreter)simpleVerifier);
/*  229 */       if (paramClassLoader != null) {
/*  230 */         simpleVerifier.setClassLoader(paramClassLoader);
/*      */       }
/*      */       try {
/*  233 */         analyzer.analyze(classNode.name, methodNode);
/*  234 */         if (!paramBoolean) {
/*      */           continue;
/*      */         }
/*  237 */       } catch (Exception exception) {
/*  238 */         exception.printStackTrace(paramPrintWriter);
/*      */       } 
/*  240 */       printAnalyzerResult(methodNode, analyzer, paramPrintWriter); continue;
/*      */     } 
/*  242 */     paramPrintWriter.flush();
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
/*      */   public static void verify(ClassReader paramClassReader, boolean paramBoolean, PrintWriter paramPrintWriter) {
/*  259 */     verify(paramClassReader, null, paramBoolean, paramPrintWriter);
/*      */   }
/*      */ 
/*      */   
/*      */   static void printAnalyzerResult(MethodNode paramMethodNode, Analyzer<BasicValue> paramAnalyzer, PrintWriter paramPrintWriter) {
/*  264 */     Frame[] arrayOfFrame = paramAnalyzer.getFrames();
/*  265 */     Textifier textifier = new Textifier();
/*  266 */     TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(textifier);
/*      */     
/*  268 */     paramPrintWriter.println(paramMethodNode.name + paramMethodNode.desc); byte b;
/*  269 */     for (b = 0; b < paramMethodNode.instructions.size(); b++) {
/*  270 */       paramMethodNode.instructions.get(b).accept(traceMethodVisitor);
/*      */       
/*  272 */       StringBuilder stringBuilder = new StringBuilder();
/*  273 */       Frame frame = arrayOfFrame[b];
/*  274 */       if (frame == null) {
/*  275 */         stringBuilder.append('?');
/*      */       } else {
/*  277 */         byte b1; for (b1 = 0; b1 < frame.getLocals(); b1++) {
/*  278 */           stringBuilder.append(getShortName(((BasicValue)frame.getLocal(b1)).toString()))
/*  279 */             .append(' ');
/*      */         }
/*  281 */         stringBuilder.append(" : ");
/*  282 */         for (b1 = 0; b1 < frame.getStackSize(); b1++) {
/*  283 */           stringBuilder.append(getShortName(((BasicValue)frame.getStack(b1)).toString()))
/*  284 */             .append(' ');
/*      */         }
/*      */       } 
/*  287 */       while (stringBuilder.length() < paramMethodNode.maxStack + paramMethodNode.maxLocals + 1) {
/*  288 */         stringBuilder.append(' ');
/*      */       }
/*  290 */       paramPrintWriter.print(Integer.toString(b + 100000).substring(1));
/*  291 */       paramPrintWriter.print(" " + stringBuilder + " : " + textifier.text.get(textifier.text.size() - 1));
/*      */     } 
/*  293 */     for (b = 0; b < paramMethodNode.tryCatchBlocks.size(); b++) {
/*  294 */       ((TryCatchBlockNode)paramMethodNode.tryCatchBlocks.get(b)).accept(traceMethodVisitor);
/*  295 */       paramPrintWriter.print(" " + textifier.text.get(textifier.text.size() - 1));
/*      */     } 
/*  297 */     paramPrintWriter.println();
/*      */   }
/*      */   
/*      */   private static String getShortName(String paramString) {
/*  301 */     int i = paramString.lastIndexOf('/');
/*  302 */     int j = paramString.length();
/*  303 */     if (paramString.charAt(j - 1) == ';') {
/*  304 */       j--;
/*      */     }
/*  306 */     return (i == -1) ? paramString : paramString.substring(i + 1, j);
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
/*      */   public CheckClassAdapter(ClassVisitor paramClassVisitor) {
/*  318 */     this(paramClassVisitor, true);
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
/*      */   
/*      */   public CheckClassAdapter(ClassVisitor paramClassVisitor, boolean paramBoolean) {
/*  337 */     this(327680, paramClassVisitor, paramBoolean);
/*  338 */     if (getClass() != CheckClassAdapter.class) {
/*  339 */       throw new IllegalStateException();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CheckClassAdapter(int paramInt, ClassVisitor paramClassVisitor, boolean paramBoolean) {
/*  359 */     super(paramInt, paramClassVisitor);
/*  360 */     this.labels = new HashMap<Label, Integer>();
/*  361 */     this.checkDataFlow = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  372 */     if (this.start) {
/*  373 */       throw new IllegalStateException("visit must be called only once");
/*      */     }
/*  375 */     this.start = true;
/*  376 */     checkState();
/*  377 */     checkAccess(paramInt2, 423473);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  382 */     if (paramString1 == null || !paramString1.endsWith("package-info")) {
/*  383 */       CheckMethodAdapter.checkInternalName(paramString1, "class name");
/*      */     }
/*  385 */     if ("java/lang/Object".equals(paramString1)) {
/*  386 */       if (paramString3 != null) {
/*  387 */         throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
/*      */       }
/*      */     } else {
/*      */       
/*  391 */       CheckMethodAdapter.checkInternalName(paramString3, "super class name");
/*      */     } 
/*  393 */     if (paramString2 != null) {
/*  394 */       checkClassSignature(paramString2);
/*      */     }
/*  396 */     if ((paramInt2 & 0x200) != 0 && 
/*  397 */       !"java/lang/Object".equals(paramString3)) {
/*  398 */       throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
/*      */     }
/*      */ 
/*      */     
/*  402 */     if (paramArrayOfString != null) {
/*  403 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  404 */         CheckMethodAdapter.checkInternalName(paramArrayOfString[b], "interface name at index " + b);
/*      */       }
/*      */     }
/*      */     
/*  408 */     this.version = paramInt1;
/*  409 */     super.visit(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitSource(String paramString1, String paramString2) {
/*  414 */     checkState();
/*  415 */     if (this.source) {
/*  416 */       throw new IllegalStateException("visitSource can be called only once.");
/*      */     }
/*      */     
/*  419 */     this.source = true;
/*  420 */     super.visitSource(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/*  426 */     checkState();
/*  427 */     if (this.outer) {
/*  428 */       throw new IllegalStateException("visitOuterClass can be called only once.");
/*      */     }
/*      */     
/*  431 */     this.outer = true;
/*  432 */     if (paramString1 == null) {
/*  433 */       throw new IllegalArgumentException("Illegal outer class owner");
/*      */     }
/*  435 */     if (paramString3 != null) {
/*  436 */       CheckMethodAdapter.checkMethodDesc(paramString3);
/*      */     }
/*  438 */     super.visitOuterClass(paramString1, paramString2, paramString3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/*  444 */     checkState();
/*  445 */     CheckMethodAdapter.checkInternalName(paramString1, "class name");
/*  446 */     if (paramString2 != null) {
/*  447 */       CheckMethodAdapter.checkInternalName(paramString2, "outer class name");
/*      */     }
/*  449 */     if (paramString3 != null) {
/*  450 */       byte b = 0;
/*  451 */       while (b < paramString3.length() && 
/*  452 */         Character.isDigit(paramString3.charAt(b))) {
/*  453 */         b++;
/*      */       }
/*  455 */       if (b == 0 || b < paramString3.length()) {
/*  456 */         CheckMethodAdapter.checkIdentifier(paramString3, b, -1, "inner class name");
/*      */       }
/*      */     } 
/*      */     
/*  460 */     checkAccess(paramInt, 30239);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  465 */     super.visitInnerClass(paramString1, paramString2, paramString3, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/*  471 */     checkState();
/*  472 */     checkAccess(paramInt, 413919);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  477 */     CheckMethodAdapter.checkUnqualifiedName(this.version, paramString1, "field name");
/*  478 */     CheckMethodAdapter.checkDesc(paramString2, false);
/*  479 */     if (paramString3 != null) {
/*  480 */       checkFieldSignature(paramString3);
/*      */     }
/*  482 */     if (paramObject != null) {
/*  483 */       CheckMethodAdapter.checkConstant(paramObject);
/*      */     }
/*      */     
/*  486 */     FieldVisitor fieldVisitor = super.visitField(paramInt, paramString1, paramString2, paramString3, paramObject);
/*  487 */     return new CheckFieldAdapter(fieldVisitor);
/*      */   }
/*      */ 
/*      */   
/*      */   public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*      */     CheckMethodAdapter checkMethodAdapter;
/*  493 */     checkState();
/*  494 */     checkAccess(paramInt, 400895);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  500 */     if (!"<init>".equals(paramString1) && !"<clinit>".equals(paramString1)) {
/*  501 */       CheckMethodAdapter.checkMethodIdentifier(this.version, paramString1, "method name");
/*      */     }
/*      */     
/*  504 */     CheckMethodAdapter.checkMethodDesc(paramString2);
/*  505 */     if (paramString3 != null) {
/*  506 */       checkMethodSignature(paramString3);
/*      */     }
/*  508 */     if (paramArrayOfString != null) {
/*  509 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  510 */         CheckMethodAdapter.checkInternalName(paramArrayOfString[b], "exception name at index " + b);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  515 */     if (this.checkDataFlow) {
/*  516 */       checkMethodAdapter = new CheckMethodAdapter(paramInt, paramString1, paramString2, super.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString), this.labels);
/*      */     } else {
/*      */       
/*  519 */       checkMethodAdapter = new CheckMethodAdapter(super.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString), this.labels);
/*      */     } 
/*      */     
/*  522 */     checkMethodAdapter.version = this.version;
/*  523 */     return checkMethodAdapter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/*  529 */     checkState();
/*  530 */     CheckMethodAdapter.checkDesc(paramString, false);
/*  531 */     return new CheckAnnotationAdapter(super.visitAnnotation(paramString, paramBoolean));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  537 */     checkState();
/*  538 */     int i = paramInt >>> 24;
/*  539 */     if (i != 0 && i != 17 && i != 16)
/*      */     {
/*      */       
/*  542 */       throw new IllegalArgumentException("Invalid type reference sort 0x" + 
/*  543 */           Integer.toHexString(i));
/*      */     }
/*  545 */     checkTypeRefAndPath(paramInt, paramTypePath);
/*  546 */     CheckMethodAdapter.checkDesc(paramString, false);
/*  547 */     return new CheckAnnotationAdapter(super.visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAttribute(Attribute paramAttribute) {
/*  553 */     checkState();
/*  554 */     if (paramAttribute == null) {
/*  555 */       throw new IllegalArgumentException("Invalid attribute (must not be null)");
/*      */     }
/*      */     
/*  558 */     super.visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitEnd() {
/*  563 */     checkState();
/*  564 */     this.end = true;
/*  565 */     super.visitEnd();
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
/*      */   private void checkState() {
/*  577 */     if (!this.start) {
/*  578 */       throw new IllegalStateException("Cannot visit member before visit has been called.");
/*      */     }
/*      */     
/*  581 */     if (this.end) {
/*  582 */       throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
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
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkAccess(int paramInt1, int paramInt2) {
/*  598 */     if ((paramInt1 & (paramInt2 ^ 0xFFFFFFFF)) != 0) {
/*  599 */       throw new IllegalArgumentException("Invalid access flags: " + paramInt1);
/*      */     }
/*      */     
/*  602 */     byte b1 = ((paramInt1 & 0x1) == 0) ? 0 : 1;
/*  603 */     byte b2 = ((paramInt1 & 0x2) == 0) ? 0 : 1;
/*  604 */     byte b3 = ((paramInt1 & 0x4) == 0) ? 0 : 1;
/*  605 */     if (b1 + b2 + b3 > 1) {
/*  606 */       throw new IllegalArgumentException("public private and protected are mutually exclusive: " + paramInt1);
/*      */     }
/*      */ 
/*      */     
/*  610 */     byte b4 = ((paramInt1 & 0x10) == 0) ? 0 : 1;
/*  611 */     byte b5 = ((paramInt1 & 0x400) == 0) ? 0 : 1;
/*  612 */     if (b4 + b5 > 1) {
/*  613 */       throw new IllegalArgumentException("final and abstract are mutually exclusive: " + paramInt1);
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
/*      */ 
/*      */   
/*      */   public static void checkClassSignature(String paramString) {
/*  628 */     int i = 0;
/*  629 */     if (getChar(paramString, 0) == '<') {
/*  630 */       i = checkFormalTypeParameters(paramString, i);
/*      */     }
/*  632 */     i = checkClassTypeSignature(paramString, i);
/*  633 */     while (getChar(paramString, i) == 'L') {
/*  634 */       i = checkClassTypeSignature(paramString, i);
/*      */     }
/*  636 */     if (i != paramString.length()) {
/*  637 */       throw new IllegalArgumentException(paramString + ": error at index " + i);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkMethodSignature(String paramString) {
/*  653 */     int i = 0;
/*  654 */     if (getChar(paramString, 0) == '<') {
/*  655 */       i = checkFormalTypeParameters(paramString, i);
/*      */     }
/*  657 */     i = checkChar('(', paramString, i);
/*  658 */     while ("ZCBSIFJDL[T".indexOf(getChar(paramString, i)) != -1) {
/*  659 */       i = checkTypeSignature(paramString, i);
/*      */     }
/*  661 */     i = checkChar(')', paramString, i);
/*  662 */     if (getChar(paramString, i) == 'V') {
/*  663 */       i++;
/*      */     } else {
/*  665 */       i = checkTypeSignature(paramString, i);
/*      */     } 
/*  667 */     while (getChar(paramString, i) == '^') {
/*  668 */       i++;
/*  669 */       if (getChar(paramString, i) == 'L') {
/*  670 */         i = checkClassTypeSignature(paramString, i); continue;
/*      */       } 
/*  672 */       i = checkTypeVariableSignature(paramString, i);
/*      */     } 
/*      */     
/*  675 */     if (i != paramString.length()) {
/*  676 */       throw new IllegalArgumentException(paramString + ": error at index " + i);
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
/*      */   public static void checkFieldSignature(String paramString) {
/*  688 */     int i = checkFieldTypeSignature(paramString, 0);
/*  689 */     if (i != paramString.length()) {
/*  690 */       throw new IllegalArgumentException(paramString + ": error at index " + i);
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
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkTypeRefAndPath(int paramInt, TypePath paramTypePath) {
/*  706 */     int i = 0;
/*  707 */     switch (paramInt >>> 24) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 22:
/*  711 */         i = -65536;
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 64:
/*      */       case 65:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*  722 */         i = -16777216;
/*      */         break;
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 23:
/*      */       case 66:
/*  729 */         i = -256;
/*      */         break;
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*  736 */         i = -16776961;
/*      */         break;
/*      */       default:
/*  739 */         throw new IllegalArgumentException("Invalid type reference sort 0x" + 
/*  740 */             Integer.toHexString(paramInt >>> 24));
/*      */     } 
/*  742 */     if ((paramInt & (i ^ 0xFFFFFFFF)) != 0) {
/*  743 */       throw new IllegalArgumentException("Invalid type reference 0x" + 
/*  744 */           Integer.toHexString(paramInt));
/*      */     }
/*  746 */     if (paramTypePath != null) {
/*  747 */       for (byte b = 0; b < paramTypePath.getLength(); b++) {
/*  748 */         int j = paramTypePath.getStep(b);
/*  749 */         if (j != 0 && j != 1 && j != 3 && j != 2)
/*      */         {
/*      */ 
/*      */           
/*  753 */           throw new IllegalArgumentException("Invalid type path step " + b + " in " + paramTypePath);
/*      */         }
/*      */         
/*  756 */         if (j != 3 && paramTypePath
/*  757 */           .getStepArgument(b) != 0) {
/*  758 */           throw new IllegalArgumentException("Invalid type path step argument for step " + b + " in " + paramTypePath);
/*      */         }
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int checkFormalTypeParameters(String paramString, int paramInt) {
/*  779 */     paramInt = checkChar('<', paramString, paramInt);
/*  780 */     paramInt = checkFormalTypeParameter(paramString, paramInt);
/*  781 */     while (getChar(paramString, paramInt) != '>') {
/*  782 */       paramInt = checkFormalTypeParameter(paramString, paramInt);
/*      */     }
/*  784 */     return paramInt + 1;
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
/*      */   private static int checkFormalTypeParameter(String paramString, int paramInt) {
/*  800 */     paramInt = checkIdentifier(paramString, paramInt);
/*  801 */     paramInt = checkChar(':', paramString, paramInt);
/*  802 */     if ("L[T".indexOf(getChar(paramString, paramInt)) != -1) {
/*  803 */       paramInt = checkFieldTypeSignature(paramString, paramInt);
/*      */     }
/*  805 */     while (getChar(paramString, paramInt) == ':') {
/*  806 */       paramInt = checkFieldTypeSignature(paramString, paramInt + 1);
/*      */     }
/*  808 */     return paramInt;
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
/*      */   
/*      */   private static int checkFieldTypeSignature(String paramString, int paramInt) {
/*  827 */     switch (getChar(paramString, paramInt)) {
/*      */       case 'L':
/*  829 */         return checkClassTypeSignature(paramString, paramInt);
/*      */       case '[':
/*  831 */         return checkTypeSignature(paramString, paramInt + 1);
/*      */     } 
/*  833 */     return checkTypeVariableSignature(paramString, paramInt);
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
/*      */   private static int checkClassTypeSignature(String paramString, int paramInt) {
/*  851 */     paramInt = checkChar('L', paramString, paramInt);
/*  852 */     paramInt = checkIdentifier(paramString, paramInt);
/*  853 */     while (getChar(paramString, paramInt) == '/') {
/*  854 */       paramInt = checkIdentifier(paramString, paramInt + 1);
/*      */     }
/*  856 */     if (getChar(paramString, paramInt) == '<') {
/*  857 */       paramInt = checkTypeArguments(paramString, paramInt);
/*      */     }
/*  859 */     while (getChar(paramString, paramInt) == '.') {
/*  860 */       paramInt = checkIdentifier(paramString, paramInt + 1);
/*  861 */       if (getChar(paramString, paramInt) == '<') {
/*  862 */         paramInt = checkTypeArguments(paramString, paramInt);
/*      */       }
/*      */     } 
/*  865 */     return checkChar(';', paramString, paramInt);
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
/*      */   private static int checkTypeArguments(String paramString, int paramInt) {
/*  881 */     paramInt = checkChar('<', paramString, paramInt);
/*  882 */     paramInt = checkTypeArgument(paramString, paramInt);
/*  883 */     while (getChar(paramString, paramInt) != '>') {
/*  884 */       paramInt = checkTypeArgument(paramString, paramInt);
/*      */     }
/*  886 */     return paramInt + 1;
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
/*      */   private static int checkTypeArgument(String paramString, int paramInt) {
/*  902 */     char c = getChar(paramString, paramInt);
/*  903 */     if (c == '*')
/*  904 */       return paramInt + 1; 
/*  905 */     if (c == '+' || c == '-') {
/*  906 */       paramInt++;
/*      */     }
/*  908 */     return checkFieldTypeSignature(paramString, paramInt);
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
/*      */   private static int checkTypeVariableSignature(String paramString, int paramInt) {
/*  925 */     paramInt = checkChar('T', paramString, paramInt);
/*  926 */     paramInt = checkIdentifier(paramString, paramInt);
/*  927 */     return checkChar(';', paramString, paramInt);
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
/*      */   private static int checkTypeSignature(String paramString, int paramInt) {
/*  943 */     switch (getChar(paramString, paramInt)) {
/*      */       case 'B':
/*      */       case 'C':
/*      */       case 'D':
/*      */       case 'F':
/*      */       case 'I':
/*      */       case 'J':
/*      */       case 'S':
/*      */       case 'Z':
/*  952 */         return paramInt + 1;
/*      */     } 
/*  954 */     return checkFieldTypeSignature(paramString, paramInt);
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
/*      */   private static int checkIdentifier(String paramString, int paramInt) {
/*  968 */     if (!Character.isJavaIdentifierStart(getChar(paramString, paramInt))) {
/*  969 */       throw new IllegalArgumentException(paramString + ": identifier expected at index " + paramInt);
/*      */     }
/*      */     
/*  972 */     paramInt++;
/*  973 */     while (Character.isJavaIdentifierPart(getChar(paramString, paramInt))) {
/*  974 */       paramInt++;
/*      */     }
/*  976 */     return paramInt;
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
/*      */   private static int checkChar(char paramChar, String paramString, int paramInt) {
/*  989 */     if (getChar(paramString, paramInt) == paramChar) {
/*  990 */       return paramInt + 1;
/*      */     }
/*  992 */     throw new IllegalArgumentException(paramString + ": '" + paramChar + "' expected at index " + paramInt);
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
/*      */   private static char getChar(String paramString, int paramInt) {
/* 1007 */     return (paramInt < paramString.length()) ? paramString.charAt(paramInt) : Character.MIN_VALUE;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\CheckClassAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */