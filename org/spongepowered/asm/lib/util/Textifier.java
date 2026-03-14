/*      */ package org.spongepowered.asm.lib.util;
/*      */ 
/*      */ import java.io.FileInputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.spongepowered.asm.lib.Attribute;
/*      */ import org.spongepowered.asm.lib.ClassReader;
/*      */ import org.spongepowered.asm.lib.Handle;
/*      */ import org.spongepowered.asm.lib.Label;
/*      */ import org.spongepowered.asm.lib.Type;
/*      */ import org.spongepowered.asm.lib.TypePath;
/*      */ import org.spongepowered.asm.lib.TypeReference;
/*      */ import org.spongepowered.asm.lib.signature.SignatureReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Textifier
/*      */   extends Printer
/*      */ {
/*      */   public static final int INTERNAL_NAME = 0;
/*      */   public static final int FIELD_DESCRIPTOR = 1;
/*      */   public static final int FIELD_SIGNATURE = 2;
/*      */   public static final int METHOD_DESCRIPTOR = 3;
/*      */   public static final int METHOD_SIGNATURE = 4;
/*      */   public static final int CLASS_SIGNATURE = 5;
/*      */   public static final int TYPE_DECLARATION = 6;
/*      */   public static final int CLASS_DECLARATION = 7;
/*      */   public static final int PARAMETERS_DECLARATION = 8;
/*      */   public static final int HANDLE_DESCRIPTOR = 9;
/*  118 */   protected String tab = "  ";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  123 */   protected String tab2 = "    ";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  128 */   protected String tab3 = "      ";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  133 */   protected String ltab = "   ";
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<Label, String> labelNames;
/*      */ 
/*      */ 
/*      */   
/*      */   private int access;
/*      */ 
/*      */ 
/*      */   
/*  145 */   private int valueNumber = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier() {
/*  156 */     this(327680);
/*  157 */     if (getClass() != Textifier.class) {
/*  158 */       throw new IllegalStateException();
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
/*      */   protected Textifier(int paramInt) {
/*  170 */     super(paramInt);
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
/*      */   public static void main(String[] paramArrayOfString) throws Exception {
/*      */     ClassReader classReader;
/*  185 */     boolean bool1 = false;
/*  186 */     byte b = 2;
/*      */     
/*  188 */     boolean bool2 = true;
/*  189 */     if (paramArrayOfString.length < 1 || paramArrayOfString.length > 2) {
/*  190 */       bool2 = false;
/*      */     }
/*  192 */     if (bool2 && "-debug".equals(paramArrayOfString[0])) {
/*  193 */       bool1 = true;
/*  194 */       b = 0;
/*  195 */       if (paramArrayOfString.length != 2) {
/*  196 */         bool2 = false;
/*      */       }
/*      */     } 
/*  199 */     if (!bool2) {
/*  200 */       System.err
/*  201 */         .println("Prints a disassembled view of the given class.");
/*  202 */       System.err.println("Usage: Textifier [-debug] <fully qualified class name or class file name>");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  207 */     if (paramArrayOfString[bool1].endsWith(".class") || paramArrayOfString[bool1].indexOf('\\') > -1 || paramArrayOfString[bool1]
/*  208 */       .indexOf('/') > -1) {
/*  209 */       classReader = new ClassReader(new FileInputStream(paramArrayOfString[bool1]));
/*      */     } else {
/*  211 */       classReader = new ClassReader(paramArrayOfString[bool1]);
/*      */     } 
/*  213 */     classReader.accept(new TraceClassVisitor(new PrintWriter(System.out)), b);
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
/*  224 */     this.access = paramInt2;
/*  225 */     int i = paramInt1 & 0xFFFF;
/*  226 */     int j = paramInt1 >>> 16;
/*  227 */     this.buf.setLength(0);
/*  228 */     this.buf.append("// class version ").append(i).append('.').append(j)
/*  229 */       .append(" (").append(paramInt1).append(")\n");
/*  230 */     if ((paramInt2 & 0x20000) != 0) {
/*  231 */       this.buf.append("// DEPRECATED\n");
/*      */     }
/*  233 */     this.buf.append("// access flags 0x")
/*  234 */       .append(Integer.toHexString(paramInt2).toUpperCase()).append('\n');
/*      */     
/*  236 */     appendDescriptor(5, paramString2);
/*  237 */     if (paramString2 != null) {
/*  238 */       TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(paramInt2);
/*  239 */       SignatureReader signatureReader = new SignatureReader(paramString2);
/*  240 */       signatureReader.accept(traceSignatureVisitor);
/*  241 */       this.buf.append("// declaration: ").append(paramString1)
/*  242 */         .append(traceSignatureVisitor.getDeclaration()).append('\n');
/*      */     } 
/*      */     
/*  245 */     appendAccess(paramInt2 & 0xFFFFFFDF);
/*  246 */     if ((paramInt2 & 0x2000) != 0) {
/*  247 */       this.buf.append("@interface ");
/*  248 */     } else if ((paramInt2 & 0x200) != 0) {
/*  249 */       this.buf.append("interface ");
/*  250 */     } else if ((paramInt2 & 0x4000) == 0) {
/*  251 */       this.buf.append("class ");
/*      */     } 
/*  253 */     appendDescriptor(0, paramString1);
/*      */     
/*  255 */     if (paramString3 != null && !"java/lang/Object".equals(paramString3)) {
/*  256 */       this.buf.append(" extends ");
/*  257 */       appendDescriptor(0, paramString3);
/*  258 */       this.buf.append(' ');
/*      */     } 
/*  260 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/*  261 */       this.buf.append(" implements ");
/*  262 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  263 */         appendDescriptor(0, paramArrayOfString[b]);
/*  264 */         this.buf.append(' ');
/*      */       } 
/*      */     } 
/*  267 */     this.buf.append(" {\n\n");
/*      */     
/*  269 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitSource(String paramString1, String paramString2) {
/*  274 */     this.buf.setLength(0);
/*  275 */     if (paramString1 != null) {
/*  276 */       this.buf.append(this.tab).append("// compiled from: ").append(paramString1)
/*  277 */         .append('\n');
/*      */     }
/*  279 */     if (paramString2 != null) {
/*  280 */       this.buf.append(this.tab).append("// debug info: ").append(paramString2)
/*  281 */         .append('\n');
/*      */     }
/*  283 */     if (this.buf.length() > 0) {
/*  284 */       this.text.add(this.buf.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/*  291 */     this.buf.setLength(0);
/*  292 */     this.buf.append(this.tab).append("OUTERCLASS ");
/*  293 */     appendDescriptor(0, paramString1);
/*  294 */     this.buf.append(' ');
/*  295 */     if (paramString2 != null) {
/*  296 */       this.buf.append(paramString2).append(' ');
/*      */     }
/*  298 */     appendDescriptor(3, paramString3);
/*  299 */     this.buf.append('\n');
/*  300 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitClassAnnotation(String paramString, boolean paramBoolean) {
/*  306 */     this.text.add("\n");
/*  307 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitClassTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  313 */     this.text.add("\n");
/*  314 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitClassAttribute(Attribute paramAttribute) {
/*  319 */     this.text.add("\n");
/*  320 */     visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/*  326 */     this.buf.setLength(0);
/*  327 */     this.buf.append(this.tab).append("// access flags 0x");
/*  328 */     this.buf.append(
/*  329 */         Integer.toHexString(paramInt & 0xFFFFFFDF).toUpperCase())
/*  330 */       .append('\n');
/*  331 */     this.buf.append(this.tab);
/*  332 */     appendAccess(paramInt);
/*  333 */     this.buf.append("INNERCLASS ");
/*  334 */     appendDescriptor(0, paramString1);
/*  335 */     this.buf.append(' ');
/*  336 */     appendDescriptor(0, paramString2);
/*  337 */     this.buf.append(' ');
/*  338 */     appendDescriptor(0, paramString3);
/*  339 */     this.buf.append('\n');
/*  340 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/*  346 */     this.buf.setLength(0);
/*  347 */     this.buf.append('\n');
/*  348 */     if ((paramInt & 0x20000) != 0) {
/*  349 */       this.buf.append(this.tab).append("// DEPRECATED\n");
/*      */     }
/*  351 */     this.buf.append(this.tab).append("// access flags 0x")
/*  352 */       .append(Integer.toHexString(paramInt).toUpperCase()).append('\n');
/*  353 */     if (paramString3 != null) {
/*  354 */       this.buf.append(this.tab);
/*  355 */       appendDescriptor(2, paramString3);
/*      */       
/*  357 */       TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
/*  358 */       SignatureReader signatureReader = new SignatureReader(paramString3);
/*  359 */       signatureReader.acceptType(traceSignatureVisitor);
/*  360 */       this.buf.append(this.tab).append("// declaration: ")
/*  361 */         .append(traceSignatureVisitor.getDeclaration()).append('\n');
/*      */     } 
/*      */     
/*  364 */     this.buf.append(this.tab);
/*  365 */     appendAccess(paramInt);
/*      */     
/*  367 */     appendDescriptor(1, paramString2);
/*  368 */     this.buf.append(' ').append(paramString1);
/*  369 */     if (paramObject != null) {
/*  370 */       this.buf.append(" = ");
/*  371 */       if (paramObject instanceof String) {
/*  372 */         this.buf.append('"').append(paramObject).append('"');
/*      */       } else {
/*  374 */         this.buf.append(paramObject);
/*      */       } 
/*      */     } 
/*      */     
/*  378 */     this.buf.append('\n');
/*  379 */     this.text.add(this.buf.toString());
/*      */     
/*  381 */     Textifier textifier = createTextifier();
/*  382 */     this.text.add(textifier.getText());
/*  383 */     return textifier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  389 */     this.buf.setLength(0);
/*  390 */     this.buf.append('\n');
/*  391 */     if ((paramInt & 0x20000) != 0) {
/*  392 */       this.buf.append(this.tab).append("// DEPRECATED\n");
/*      */     }
/*  394 */     this.buf.append(this.tab).append("// access flags 0x")
/*  395 */       .append(Integer.toHexString(paramInt).toUpperCase()).append('\n');
/*      */     
/*  397 */     if (paramString3 != null) {
/*  398 */       this.buf.append(this.tab);
/*  399 */       appendDescriptor(4, paramString3);
/*      */       
/*  401 */       TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
/*  402 */       SignatureReader signatureReader = new SignatureReader(paramString3);
/*  403 */       signatureReader.accept(traceSignatureVisitor);
/*  404 */       String str1 = traceSignatureVisitor.getDeclaration();
/*  405 */       String str2 = traceSignatureVisitor.getReturnType();
/*  406 */       String str3 = traceSignatureVisitor.getExceptions();
/*      */       
/*  408 */       this.buf.append(this.tab).append("// declaration: ").append(str2)
/*  409 */         .append(' ').append(paramString1).append(str1);
/*  410 */       if (str3 != null) {
/*  411 */         this.buf.append(" throws ").append(str3);
/*      */       }
/*  413 */       this.buf.append('\n');
/*      */     } 
/*      */     
/*  416 */     this.buf.append(this.tab);
/*  417 */     appendAccess(paramInt & 0xFFFFFFBF);
/*  418 */     if ((paramInt & 0x100) != 0) {
/*  419 */       this.buf.append("native ");
/*      */     }
/*  421 */     if ((paramInt & 0x80) != 0) {
/*  422 */       this.buf.append("varargs ");
/*      */     }
/*  424 */     if ((paramInt & 0x40) != 0) {
/*  425 */       this.buf.append("bridge ");
/*      */     }
/*  427 */     if ((this.access & 0x200) != 0 && (paramInt & 0x400) == 0 && (paramInt & 0x8) == 0)
/*      */     {
/*      */       
/*  430 */       this.buf.append("default ");
/*      */     }
/*      */     
/*  433 */     this.buf.append(paramString1);
/*  434 */     appendDescriptor(3, paramString2);
/*  435 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/*  436 */       this.buf.append(" throws ");
/*  437 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  438 */         appendDescriptor(0, paramArrayOfString[b]);
/*  439 */         this.buf.append(' ');
/*      */       } 
/*      */     } 
/*      */     
/*  443 */     this.buf.append('\n');
/*  444 */     this.text.add(this.buf.toString());
/*      */     
/*  446 */     Textifier textifier = createTextifier();
/*  447 */     this.text.add(textifier.getText());
/*  448 */     return textifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitClassEnd() {
/*  453 */     this.text.add("}\n");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visit(String paramString, Object paramObject) {
/*  462 */     this.buf.setLength(0);
/*  463 */     appendComa(this.valueNumber++);
/*      */     
/*  465 */     if (paramString != null) {
/*  466 */       this.buf.append(paramString).append('=');
/*      */     }
/*      */     
/*  469 */     if (paramObject instanceof String) {
/*  470 */       visitString((String)paramObject);
/*  471 */     } else if (paramObject instanceof Type) {
/*  472 */       visitType((Type)paramObject);
/*  473 */     } else if (paramObject instanceof Byte) {
/*  474 */       visitByte(((Byte)paramObject).byteValue());
/*  475 */     } else if (paramObject instanceof Boolean) {
/*  476 */       visitBoolean(((Boolean)paramObject).booleanValue());
/*  477 */     } else if (paramObject instanceof Short) {
/*  478 */       visitShort(((Short)paramObject).shortValue());
/*  479 */     } else if (paramObject instanceof Character) {
/*  480 */       visitChar(((Character)paramObject).charValue());
/*  481 */     } else if (paramObject instanceof Integer) {
/*  482 */       visitInt(((Integer)paramObject).intValue());
/*  483 */     } else if (paramObject instanceof Float) {
/*  484 */       visitFloat(((Float)paramObject).floatValue());
/*  485 */     } else if (paramObject instanceof Long) {
/*  486 */       visitLong(((Long)paramObject).longValue());
/*  487 */     } else if (paramObject instanceof Double) {
/*  488 */       visitDouble(((Double)paramObject).doubleValue());
/*  489 */     } else if (paramObject.getClass().isArray()) {
/*  490 */       this.buf.append('{');
/*  491 */       if (paramObject instanceof byte[]) {
/*  492 */         byte[] arrayOfByte = (byte[])paramObject;
/*  493 */         for (byte b = 0; b < arrayOfByte.length; b++) {
/*  494 */           appendComa(b);
/*  495 */           visitByte(arrayOfByte[b]);
/*      */         } 
/*  497 */       } else if (paramObject instanceof boolean[]) {
/*  498 */         boolean[] arrayOfBoolean = (boolean[])paramObject;
/*  499 */         for (byte b = 0; b < arrayOfBoolean.length; b++) {
/*  500 */           appendComa(b);
/*  501 */           visitBoolean(arrayOfBoolean[b]);
/*      */         } 
/*  503 */       } else if (paramObject instanceof short[]) {
/*  504 */         short[] arrayOfShort = (short[])paramObject;
/*  505 */         for (byte b = 0; b < arrayOfShort.length; b++) {
/*  506 */           appendComa(b);
/*  507 */           visitShort(arrayOfShort[b]);
/*      */         } 
/*  509 */       } else if (paramObject instanceof char[]) {
/*  510 */         char[] arrayOfChar = (char[])paramObject;
/*  511 */         for (byte b = 0; b < arrayOfChar.length; b++) {
/*  512 */           appendComa(b);
/*  513 */           visitChar(arrayOfChar[b]);
/*      */         } 
/*  515 */       } else if (paramObject instanceof int[]) {
/*  516 */         int[] arrayOfInt = (int[])paramObject;
/*  517 */         for (byte b = 0; b < arrayOfInt.length; b++) {
/*  518 */           appendComa(b);
/*  519 */           visitInt(arrayOfInt[b]);
/*      */         } 
/*  521 */       } else if (paramObject instanceof long[]) {
/*  522 */         long[] arrayOfLong = (long[])paramObject;
/*  523 */         for (byte b = 0; b < arrayOfLong.length; b++) {
/*  524 */           appendComa(b);
/*  525 */           visitLong(arrayOfLong[b]);
/*      */         } 
/*  527 */       } else if (paramObject instanceof float[]) {
/*  528 */         float[] arrayOfFloat = (float[])paramObject;
/*  529 */         for (byte b = 0; b < arrayOfFloat.length; b++) {
/*  530 */           appendComa(b);
/*  531 */           visitFloat(arrayOfFloat[b]);
/*      */         } 
/*  533 */       } else if (paramObject instanceof double[]) {
/*  534 */         double[] arrayOfDouble = (double[])paramObject;
/*  535 */         for (byte b = 0; b < arrayOfDouble.length; b++) {
/*  536 */           appendComa(b);
/*  537 */           visitDouble(arrayOfDouble[b]);
/*      */         } 
/*      */       } 
/*  540 */       this.buf.append('}');
/*      */     } 
/*      */     
/*  543 */     this.text.add(this.buf.toString());
/*      */   }
/*      */   
/*      */   private void visitInt(int paramInt) {
/*  547 */     this.buf.append(paramInt);
/*      */   }
/*      */   
/*      */   private void visitLong(long paramLong) {
/*  551 */     this.buf.append(paramLong).append('L');
/*      */   }
/*      */   
/*      */   private void visitFloat(float paramFloat) {
/*  555 */     this.buf.append(paramFloat).append('F');
/*      */   }
/*      */   
/*      */   private void visitDouble(double paramDouble) {
/*  559 */     this.buf.append(paramDouble).append('D');
/*      */   }
/*      */   
/*      */   private void visitChar(char paramChar) {
/*  563 */     this.buf.append("(char)").append(paramChar);
/*      */   }
/*      */   
/*      */   private void visitShort(short paramShort) {
/*  567 */     this.buf.append("(short)").append(paramShort);
/*      */   }
/*      */   
/*      */   private void visitByte(byte paramByte) {
/*  571 */     this.buf.append("(byte)").append(paramByte);
/*      */   }
/*      */   
/*      */   private void visitBoolean(boolean paramBoolean) {
/*  575 */     this.buf.append(paramBoolean);
/*      */   }
/*      */   
/*      */   private void visitString(String paramString) {
/*  579 */     appendString(this.buf, paramString);
/*      */   }
/*      */   
/*      */   private void visitType(Type paramType) {
/*  583 */     this.buf.append(paramType.getClassName()).append(".class");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/*  589 */     this.buf.setLength(0);
/*  590 */     appendComa(this.valueNumber++);
/*  591 */     if (paramString1 != null) {
/*  592 */       this.buf.append(paramString1).append('=');
/*      */     }
/*  594 */     appendDescriptor(1, paramString2);
/*  595 */     this.buf.append('.').append(paramString3);
/*  596 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public Textifier visitAnnotation(String paramString1, String paramString2) {
/*  601 */     this.buf.setLength(0);
/*  602 */     appendComa(this.valueNumber++);
/*  603 */     if (paramString1 != null) {
/*  604 */       this.buf.append(paramString1).append('=');
/*      */     }
/*  606 */     this.buf.append('@');
/*  607 */     appendDescriptor(1, paramString2);
/*  608 */     this.buf.append('(');
/*  609 */     this.text.add(this.buf.toString());
/*  610 */     Textifier textifier = createTextifier();
/*  611 */     this.text.add(textifier.getText());
/*  612 */     this.text.add(")");
/*  613 */     return textifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public Textifier visitArray(String paramString) {
/*  618 */     this.buf.setLength(0);
/*  619 */     appendComa(this.valueNumber++);
/*  620 */     if (paramString != null) {
/*  621 */       this.buf.append(paramString).append('=');
/*      */     }
/*  623 */     this.buf.append('{');
/*  624 */     this.text.add(this.buf.toString());
/*  625 */     Textifier textifier = createTextifier();
/*  626 */     this.text.add(textifier.getText());
/*  627 */     this.text.add("}");
/*  628 */     return textifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAnnotationEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitFieldAnnotation(String paramString, boolean paramBoolean) {
/*  642 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitFieldTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  648 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitFieldAttribute(Attribute paramAttribute) {
/*  653 */     visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitParameter(String paramString, int paramInt) {
/*  666 */     this.buf.setLength(0);
/*  667 */     this.buf.append(this.tab2).append("// parameter ");
/*  668 */     appendAccess(paramInt);
/*  669 */     this.buf.append(' ').append((paramString == null) ? "<no name>" : paramString)
/*  670 */       .append('\n');
/*  671 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public Textifier visitAnnotationDefault() {
/*  676 */     this.text.add(this.tab2 + "default=");
/*  677 */     Textifier textifier = createTextifier();
/*  678 */     this.text.add(textifier.getText());
/*  679 */     this.text.add("\n");
/*  680 */     return textifier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitMethodAnnotation(String paramString, boolean paramBoolean) {
/*  686 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitMethodTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  692 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/*  698 */     this.buf.setLength(0);
/*  699 */     this.buf.append(this.tab2).append('@');
/*  700 */     appendDescriptor(1, paramString);
/*  701 */     this.buf.append('(');
/*  702 */     this.text.add(this.buf.toString());
/*  703 */     Textifier textifier = createTextifier();
/*  704 */     this.text.add(textifier.getText());
/*  705 */     this.text.add(paramBoolean ? ") // parameter " : ") // invisible, parameter ");
/*  706 */     this.text.add(Integer.valueOf(paramInt));
/*  707 */     this.text.add("\n");
/*  708 */     return textifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMethodAttribute(Attribute paramAttribute) {
/*  713 */     this.buf.setLength(0);
/*  714 */     this.buf.append(this.tab).append("ATTRIBUTE ");
/*  715 */     appendDescriptor(-1, paramAttribute.type);
/*      */     
/*  717 */     if (paramAttribute instanceof Textifiable) {
/*  718 */       ((Textifiable)paramAttribute).textify(this.buf, this.labelNames);
/*      */     } else {
/*  720 */       this.buf.append(" : unknown\n");
/*      */     } 
/*      */     
/*  723 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitCode() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/*  733 */     this.buf.setLength(0);
/*  734 */     this.buf.append(this.ltab);
/*  735 */     this.buf.append("FRAME ");
/*  736 */     switch (paramInt1) {
/*      */       case -1:
/*      */       case 0:
/*  739 */         this.buf.append("FULL [");
/*  740 */         appendFrameTypes(paramInt2, paramArrayOfObject1);
/*  741 */         this.buf.append("] [");
/*  742 */         appendFrameTypes(paramInt3, paramArrayOfObject2);
/*  743 */         this.buf.append(']');
/*      */         break;
/*      */       case 1:
/*  746 */         this.buf.append("APPEND [");
/*  747 */         appendFrameTypes(paramInt2, paramArrayOfObject1);
/*  748 */         this.buf.append(']');
/*      */         break;
/*      */       case 2:
/*  751 */         this.buf.append("CHOP ").append(paramInt2);
/*      */         break;
/*      */       case 3:
/*  754 */         this.buf.append("SAME");
/*      */         break;
/*      */       case 4:
/*  757 */         this.buf.append("SAME1 ");
/*  758 */         appendFrameTypes(1, paramArrayOfObject2);
/*      */         break;
/*      */     } 
/*  761 */     this.buf.append('\n');
/*  762 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitInsn(int paramInt) {
/*  767 */     this.buf.setLength(0);
/*  768 */     this.buf.append(this.tab2).append(OPCODES[paramInt]).append('\n');
/*  769 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIntInsn(int paramInt1, int paramInt2) {
/*  774 */     this.buf.setLength(0);
/*  775 */     this.buf.append(this.tab2)
/*  776 */       .append(OPCODES[paramInt1])
/*  777 */       .append(' ')
/*  778 */       .append((paramInt1 == 188) ? TYPES[paramInt2] : 
/*  779 */         Integer.toString(paramInt2)).append('\n');
/*  780 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitVarInsn(int paramInt1, int paramInt2) {
/*  785 */     this.buf.setLength(0);
/*  786 */     this.buf.append(this.tab2).append(OPCODES[paramInt1]).append(' ').append(paramInt2)
/*  787 */       .append('\n');
/*  788 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeInsn(int paramInt, String paramString) {
/*  793 */     this.buf.setLength(0);
/*  794 */     this.buf.append(this.tab2).append(OPCODES[paramInt]).append(' ');
/*  795 */     appendDescriptor(0, paramString);
/*  796 */     this.buf.append('\n');
/*  797 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  803 */     this.buf.setLength(0);
/*  804 */     this.buf.append(this.tab2).append(OPCODES[paramInt]).append(' ');
/*  805 */     appendDescriptor(0, paramString1);
/*  806 */     this.buf.append('.').append(paramString2).append(" : ");
/*  807 */     appendDescriptor(1, paramString3);
/*  808 */     this.buf.append('\n');
/*  809 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  816 */     if (this.api >= 327680) {
/*  817 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*      */       return;
/*      */     } 
/*  820 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, (paramInt == 185));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/*  827 */     if (this.api < 327680) {
/*  828 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*      */       return;
/*      */     } 
/*  831 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   private void doVisitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/*  836 */     this.buf.setLength(0);
/*  837 */     this.buf.append(this.tab2).append(OPCODES[paramInt]).append(' ');
/*  838 */     appendDescriptor(0, paramString1);
/*  839 */     this.buf.append('.').append(paramString2).append(' ');
/*  840 */     appendDescriptor(3, paramString3);
/*  841 */     this.buf.append('\n');
/*  842 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/*  848 */     this.buf.setLength(0);
/*  849 */     this.buf.append(this.tab2).append("INVOKEDYNAMIC").append(' ');
/*  850 */     this.buf.append(paramString1);
/*  851 */     appendDescriptor(3, paramString2);
/*  852 */     this.buf.append(" [");
/*  853 */     this.buf.append('\n');
/*  854 */     this.buf.append(this.tab3);
/*  855 */     appendHandle(paramHandle);
/*  856 */     this.buf.append('\n');
/*  857 */     this.buf.append(this.tab3).append("// arguments:");
/*  858 */     if (paramVarArgs.length == 0) {
/*  859 */       this.buf.append(" none");
/*      */     } else {
/*  861 */       this.buf.append('\n');
/*  862 */       for (byte b = 0; b < paramVarArgs.length; b++) {
/*  863 */         this.buf.append(this.tab3);
/*  864 */         Object object = paramVarArgs[b];
/*  865 */         if (object instanceof String) {
/*  866 */           Printer.appendString(this.buf, (String)object);
/*  867 */         } else if (object instanceof Type) {
/*  868 */           Type type = (Type)object;
/*  869 */           if (type.getSort() == 11) {
/*  870 */             appendDescriptor(3, type.getDescriptor());
/*      */           } else {
/*  872 */             this.buf.append(type.getDescriptor()).append(".class");
/*      */           } 
/*  874 */         } else if (object instanceof Handle) {
/*  875 */           appendHandle((Handle)object);
/*      */         } else {
/*  877 */           this.buf.append(object);
/*      */         } 
/*  879 */         this.buf.append(", \n");
/*      */       } 
/*  881 */       this.buf.setLength(this.buf.length() - 3);
/*      */     } 
/*  883 */     this.buf.append('\n');
/*  884 */     this.buf.append(this.tab2).append("]\n");
/*  885 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int paramInt, Label paramLabel) {
/*  890 */     this.buf.setLength(0);
/*  891 */     this.buf.append(this.tab2).append(OPCODES[paramInt]).append(' ');
/*  892 */     appendLabel(paramLabel);
/*  893 */     this.buf.append('\n');
/*  894 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLabel(Label paramLabel) {
/*  899 */     this.buf.setLength(0);
/*  900 */     this.buf.append(this.ltab);
/*  901 */     appendLabel(paramLabel);
/*  902 */     this.buf.append('\n');
/*  903 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLdcInsn(Object paramObject) {
/*  908 */     this.buf.setLength(0);
/*  909 */     this.buf.append(this.tab2).append("LDC ");
/*  910 */     if (paramObject instanceof String) {
/*  911 */       Printer.appendString(this.buf, (String)paramObject);
/*  912 */     } else if (paramObject instanceof Type) {
/*  913 */       this.buf.append(((Type)paramObject).getDescriptor()).append(".class");
/*      */     } else {
/*  915 */       this.buf.append(paramObject);
/*      */     } 
/*  917 */     this.buf.append('\n');
/*  918 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIincInsn(int paramInt1, int paramInt2) {
/*  923 */     this.buf.setLength(0);
/*  924 */     this.buf.append(this.tab2).append("IINC ").append(paramInt1).append(' ')
/*  925 */       .append(paramInt2).append('\n');
/*  926 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label... paramVarArgs) {
/*  932 */     this.buf.setLength(0);
/*  933 */     this.buf.append(this.tab2).append("TABLESWITCH\n");
/*  934 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/*  935 */       this.buf.append(this.tab3).append(paramInt1 + b).append(": ");
/*  936 */       appendLabel(paramVarArgs[b]);
/*  937 */       this.buf.append('\n');
/*      */     } 
/*  939 */     this.buf.append(this.tab3).append("default: ");
/*  940 */     appendLabel(paramLabel);
/*  941 */     this.buf.append('\n');
/*  942 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel) {
/*  948 */     this.buf.setLength(0);
/*  949 */     this.buf.append(this.tab2).append("LOOKUPSWITCH\n");
/*  950 */     for (byte b = 0; b < paramArrayOfLabel.length; b++) {
/*  951 */       this.buf.append(this.tab3).append(paramArrayOfint[b]).append(": ");
/*  952 */       appendLabel(paramArrayOfLabel[b]);
/*  953 */       this.buf.append('\n');
/*      */     } 
/*  955 */     this.buf.append(this.tab3).append("default: ");
/*  956 */     appendLabel(paramLabel);
/*  957 */     this.buf.append('\n');
/*  958 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/*  963 */     this.buf.setLength(0);
/*  964 */     this.buf.append(this.tab2).append("MULTIANEWARRAY ");
/*  965 */     appendDescriptor(1, paramString);
/*  966 */     this.buf.append(' ').append(paramInt).append('\n');
/*  967 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  973 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/*  979 */     this.buf.setLength(0);
/*  980 */     this.buf.append(this.tab2).append("TRYCATCHBLOCK ");
/*  981 */     appendLabel(paramLabel1);
/*  982 */     this.buf.append(' ');
/*  983 */     appendLabel(paramLabel2);
/*  984 */     this.buf.append(' ');
/*  985 */     appendLabel(paramLabel3);
/*  986 */     this.buf.append(' ');
/*  987 */     appendDescriptor(0, paramString);
/*  988 */     this.buf.append('\n');
/*  989 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  995 */     this.buf.setLength(0);
/*  996 */     this.buf.append(this.tab2).append("TRYCATCHBLOCK @");
/*  997 */     appendDescriptor(1, paramString);
/*  998 */     this.buf.append('(');
/*  999 */     this.text.add(this.buf.toString());
/* 1000 */     Textifier textifier = createTextifier();
/* 1001 */     this.text.add(textifier.getText());
/* 1002 */     this.buf.setLength(0);
/* 1003 */     this.buf.append(") : ");
/* 1004 */     appendTypeReference(paramInt);
/* 1005 */     this.buf.append(", ").append(paramTypePath);
/* 1006 */     this.buf.append(paramBoolean ? "\n" : " // invisible\n");
/* 1007 */     this.text.add(this.buf.toString());
/* 1008 */     return textifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/* 1015 */     this.buf.setLength(0);
/* 1016 */     this.buf.append(this.tab2).append("LOCALVARIABLE ").append(paramString1).append(' ');
/* 1017 */     appendDescriptor(1, paramString2);
/* 1018 */     this.buf.append(' ');
/* 1019 */     appendLabel(paramLabel1);
/* 1020 */     this.buf.append(' ');
/* 1021 */     appendLabel(paramLabel2);
/* 1022 */     this.buf.append(' ').append(paramInt).append('\n');
/*      */     
/* 1024 */     if (paramString3 != null) {
/* 1025 */       this.buf.append(this.tab2);
/* 1026 */       appendDescriptor(2, paramString3);
/*      */       
/* 1028 */       TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
/* 1029 */       SignatureReader signatureReader = new SignatureReader(paramString3);
/* 1030 */       signatureReader.acceptType(traceSignatureVisitor);
/* 1031 */       this.buf.append(this.tab2).append("// declaration: ")
/* 1032 */         .append(traceSignatureVisitor.getDeclaration()).append('\n');
/*      */     } 
/* 1034 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/* 1041 */     this.buf.setLength(0);
/* 1042 */     this.buf.append(this.tab2).append("LOCALVARIABLE @");
/* 1043 */     appendDescriptor(1, paramString);
/* 1044 */     this.buf.append('(');
/* 1045 */     this.text.add(this.buf.toString());
/* 1046 */     Textifier textifier = createTextifier();
/* 1047 */     this.text.add(textifier.getText());
/* 1048 */     this.buf.setLength(0);
/* 1049 */     this.buf.append(") : ");
/* 1050 */     appendTypeReference(paramInt);
/* 1051 */     this.buf.append(", ").append(paramTypePath);
/* 1052 */     for (byte b = 0; b < paramArrayOfLabel1.length; b++) {
/* 1053 */       this.buf.append(" [ ");
/* 1054 */       appendLabel(paramArrayOfLabel1[b]);
/* 1055 */       this.buf.append(" - ");
/* 1056 */       appendLabel(paramArrayOfLabel2[b]);
/* 1057 */       this.buf.append(" - ").append(paramArrayOfint[b]).append(" ]");
/*      */     } 
/* 1059 */     this.buf.append(paramBoolean ? "\n" : " // invisible\n");
/* 1060 */     this.text.add(this.buf.toString());
/* 1061 */     return textifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int paramInt, Label paramLabel) {
/* 1066 */     this.buf.setLength(0);
/* 1067 */     this.buf.append(this.tab2).append("LINENUMBER ").append(paramInt).append(' ');
/* 1068 */     appendLabel(paramLabel);
/* 1069 */     this.buf.append('\n');
/* 1070 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMaxs(int paramInt1, int paramInt2) {
/* 1075 */     this.buf.setLength(0);
/* 1076 */     this.buf.append(this.tab2).append("MAXSTACK = ").append(paramInt1).append('\n');
/* 1077 */     this.text.add(this.buf.toString());
/*      */     
/* 1079 */     this.buf.setLength(0);
/* 1080 */     this.buf.append(this.tab2).append("MAXLOCALS = ").append(paramInt2).append('\n');
/* 1081 */     this.text.add(this.buf.toString());
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
/*      */   public void visitMethodEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Textifier visitAnnotation(String paramString, boolean paramBoolean) {
/* 1102 */     this.buf.setLength(0);
/* 1103 */     this.buf.append(this.tab).append('@');
/* 1104 */     appendDescriptor(1, paramString);
/* 1105 */     this.buf.append('(');
/* 1106 */     this.text.add(this.buf.toString());
/* 1107 */     Textifier textifier = createTextifier();
/* 1108 */     this.text.add(textifier.getText());
/* 1109 */     this.text.add(paramBoolean ? ")\n" : ") // invisible\n");
/* 1110 */     return textifier;
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
/*      */   public Textifier visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 1130 */     this.buf.setLength(0);
/* 1131 */     this.buf.append(this.tab).append('@');
/* 1132 */     appendDescriptor(1, paramString);
/* 1133 */     this.buf.append('(');
/* 1134 */     this.text.add(this.buf.toString());
/* 1135 */     Textifier textifier = createTextifier();
/* 1136 */     this.text.add(textifier.getText());
/* 1137 */     this.buf.setLength(0);
/* 1138 */     this.buf.append(") : ");
/* 1139 */     appendTypeReference(paramInt);
/* 1140 */     this.buf.append(", ").append(paramTypePath);
/* 1141 */     this.buf.append(paramBoolean ? "\n" : " // invisible\n");
/* 1142 */     this.text.add(this.buf.toString());
/* 1143 */     return textifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAttribute(Attribute paramAttribute) {
/* 1153 */     this.buf.setLength(0);
/* 1154 */     this.buf.append(this.tab).append("ATTRIBUTE ");
/* 1155 */     appendDescriptor(-1, paramAttribute.type);
/*      */     
/* 1157 */     if (paramAttribute instanceof Textifiable) {
/* 1158 */       ((Textifiable)paramAttribute).textify(this.buf, null);
/*      */     } else {
/* 1160 */       this.buf.append(" : unknown\n");
/*      */     } 
/*      */     
/* 1163 */     this.text.add(this.buf.toString());
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
/*      */   protected Textifier createTextifier() {
/* 1176 */     return new Textifier();
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
/*      */   protected void appendDescriptor(int paramInt, String paramString) {
/* 1191 */     if (paramInt == 5 || paramInt == 2 || paramInt == 4) {
/*      */       
/* 1193 */       if (paramString != null) {
/* 1194 */         this.buf.append("// signature ").append(paramString).append('\n');
/*      */       }
/*      */     } else {
/* 1197 */       this.buf.append(paramString);
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
/*      */   protected void appendLabel(Label paramLabel) {
/* 1209 */     if (this.labelNames == null) {
/* 1210 */       this.labelNames = new HashMap<Label, String>();
/*      */     }
/* 1212 */     String str = this.labelNames.get(paramLabel);
/* 1213 */     if (str == null) {
/* 1214 */       str = "L" + this.labelNames.size();
/* 1215 */       this.labelNames.put(paramLabel, str);
/*      */     } 
/* 1217 */     this.buf.append(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendHandle(Handle paramHandle) {
/* 1227 */     int i = paramHandle.getTag();
/* 1228 */     this.buf.append("// handle kind 0x").append(Integer.toHexString(i))
/* 1229 */       .append(" : ");
/* 1230 */     boolean bool = false;
/* 1231 */     switch (i) {
/*      */       case 1:
/* 1233 */         this.buf.append("GETFIELD");
/*      */         break;
/*      */       case 2:
/* 1236 */         this.buf.append("GETSTATIC");
/*      */         break;
/*      */       case 3:
/* 1239 */         this.buf.append("PUTFIELD");
/*      */         break;
/*      */       case 4:
/* 1242 */         this.buf.append("PUTSTATIC");
/*      */         break;
/*      */       case 9:
/* 1245 */         this.buf.append("INVOKEINTERFACE");
/* 1246 */         bool = true;
/*      */         break;
/*      */       case 7:
/* 1249 */         this.buf.append("INVOKESPECIAL");
/* 1250 */         bool = true;
/*      */         break;
/*      */       case 6:
/* 1253 */         this.buf.append("INVOKESTATIC");
/* 1254 */         bool = true;
/*      */         break;
/*      */       case 5:
/* 1257 */         this.buf.append("INVOKEVIRTUAL");
/* 1258 */         bool = true;
/*      */         break;
/*      */       case 8:
/* 1261 */         this.buf.append("NEWINVOKESPECIAL");
/* 1262 */         bool = true;
/*      */         break;
/*      */     } 
/* 1265 */     this.buf.append('\n');
/* 1266 */     this.buf.append(this.tab3);
/* 1267 */     appendDescriptor(0, paramHandle.getOwner());
/* 1268 */     this.buf.append('.');
/* 1269 */     this.buf.append(paramHandle.getName());
/* 1270 */     if (!bool) {
/* 1271 */       this.buf.append('(');
/*      */     }
/* 1273 */     appendDescriptor(9, paramHandle.getDesc());
/* 1274 */     if (!bool) {
/* 1275 */       this.buf.append(')');
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
/*      */   private void appendAccess(int paramInt) {
/* 1287 */     if ((paramInt & 0x1) != 0) {
/* 1288 */       this.buf.append("public ");
/*      */     }
/* 1290 */     if ((paramInt & 0x2) != 0) {
/* 1291 */       this.buf.append("private ");
/*      */     }
/* 1293 */     if ((paramInt & 0x4) != 0) {
/* 1294 */       this.buf.append("protected ");
/*      */     }
/* 1296 */     if ((paramInt & 0x10) != 0) {
/* 1297 */       this.buf.append("final ");
/*      */     }
/* 1299 */     if ((paramInt & 0x8) != 0) {
/* 1300 */       this.buf.append("static ");
/*      */     }
/* 1302 */     if ((paramInt & 0x20) != 0) {
/* 1303 */       this.buf.append("synchronized ");
/*      */     }
/* 1305 */     if ((paramInt & 0x40) != 0) {
/* 1306 */       this.buf.append("volatile ");
/*      */     }
/* 1308 */     if ((paramInt & 0x80) != 0) {
/* 1309 */       this.buf.append("transient ");
/*      */     }
/* 1311 */     if ((paramInt & 0x400) != 0) {
/* 1312 */       this.buf.append("abstract ");
/*      */     }
/* 1314 */     if ((paramInt & 0x800) != 0) {
/* 1315 */       this.buf.append("strictfp ");
/*      */     }
/* 1317 */     if ((paramInt & 0x1000) != 0) {
/* 1318 */       this.buf.append("synthetic ");
/*      */     }
/* 1320 */     if ((paramInt & 0x8000) != 0) {
/* 1321 */       this.buf.append("mandated ");
/*      */     }
/* 1323 */     if ((paramInt & 0x4000) != 0) {
/* 1324 */       this.buf.append("enum ");
/*      */     }
/*      */   }
/*      */   
/*      */   private void appendComa(int paramInt) {
/* 1329 */     if (paramInt != 0) {
/* 1330 */       this.buf.append(", ");
/*      */     }
/*      */   }
/*      */   
/*      */   private void appendTypeReference(int paramInt) {
/* 1335 */     TypeReference typeReference = new TypeReference(paramInt);
/* 1336 */     switch (typeReference.getSort()) {
/*      */       case 0:
/* 1338 */         this.buf.append("CLASS_TYPE_PARAMETER ").append(typeReference
/* 1339 */             .getTypeParameterIndex());
/*      */         break;
/*      */       case 1:
/* 1342 */         this.buf.append("METHOD_TYPE_PARAMETER ").append(typeReference
/* 1343 */             .getTypeParameterIndex());
/*      */         break;
/*      */       case 16:
/* 1346 */         this.buf.append("CLASS_EXTENDS ").append(typeReference.getSuperTypeIndex());
/*      */         break;
/*      */       case 17:
/* 1349 */         this.buf.append("CLASS_TYPE_PARAMETER_BOUND ")
/* 1350 */           .append(typeReference.getTypeParameterIndex()).append(", ")
/* 1351 */           .append(typeReference.getTypeParameterBoundIndex());
/*      */         break;
/*      */       case 18:
/* 1354 */         this.buf.append("METHOD_TYPE_PARAMETER_BOUND ")
/* 1355 */           .append(typeReference.getTypeParameterIndex()).append(", ")
/* 1356 */           .append(typeReference.getTypeParameterBoundIndex());
/*      */         break;
/*      */       case 19:
/* 1359 */         this.buf.append("FIELD");
/*      */         break;
/*      */       case 20:
/* 1362 */         this.buf.append("METHOD_RETURN");
/*      */         break;
/*      */       case 21:
/* 1365 */         this.buf.append("METHOD_RECEIVER");
/*      */         break;
/*      */       case 22:
/* 1368 */         this.buf.append("METHOD_FORMAL_PARAMETER ").append(typeReference
/* 1369 */             .getFormalParameterIndex());
/*      */         break;
/*      */       case 23:
/* 1372 */         this.buf.append("THROWS ").append(typeReference.getExceptionIndex());
/*      */         break;
/*      */       case 64:
/* 1375 */         this.buf.append("LOCAL_VARIABLE");
/*      */         break;
/*      */       case 65:
/* 1378 */         this.buf.append("RESOURCE_VARIABLE");
/*      */         break;
/*      */       case 66:
/* 1381 */         this.buf.append("EXCEPTION_PARAMETER ").append(typeReference
/* 1382 */             .getTryCatchBlockIndex());
/*      */         break;
/*      */       case 67:
/* 1385 */         this.buf.append("INSTANCEOF");
/*      */         break;
/*      */       case 68:
/* 1388 */         this.buf.append("NEW");
/*      */         break;
/*      */       case 69:
/* 1391 */         this.buf.append("CONSTRUCTOR_REFERENCE");
/*      */         break;
/*      */       case 70:
/* 1394 */         this.buf.append("METHOD_REFERENCE");
/*      */         break;
/*      */       case 71:
/* 1397 */         this.buf.append("CAST ").append(typeReference.getTypeArgumentIndex());
/*      */         break;
/*      */       case 72:
/* 1400 */         this.buf.append("CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT ").append(typeReference
/* 1401 */             .getTypeArgumentIndex());
/*      */         break;
/*      */       case 73:
/* 1404 */         this.buf.append("METHOD_INVOCATION_TYPE_ARGUMENT ").append(typeReference
/* 1405 */             .getTypeArgumentIndex());
/*      */         break;
/*      */       case 74:
/* 1408 */         this.buf.append("CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT ").append(typeReference
/* 1409 */             .getTypeArgumentIndex());
/*      */         break;
/*      */       case 75:
/* 1412 */         this.buf.append("METHOD_REFERENCE_TYPE_ARGUMENT ").append(typeReference
/* 1413 */             .getTypeArgumentIndex());
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void appendFrameTypes(int paramInt, Object[] paramArrayOfObject) {
/* 1419 */     for (byte b = 0; b < paramInt; b++) {
/* 1420 */       if (b > 0) {
/* 1421 */         this.buf.append(' ');
/*      */       }
/* 1423 */       if (paramArrayOfObject[b] instanceof String) {
/* 1424 */         String str = (String)paramArrayOfObject[b];
/* 1425 */         if (str.startsWith("[")) {
/* 1426 */           appendDescriptor(1, str);
/*      */         } else {
/* 1428 */           appendDescriptor(0, str);
/*      */         } 
/* 1430 */       } else if (paramArrayOfObject[b] instanceof Integer) {
/* 1431 */         switch (((Integer)paramArrayOfObject[b]).intValue()) {
/*      */           case 0:
/* 1433 */             appendDescriptor(1, "T");
/*      */             break;
/*      */           case 1:
/* 1436 */             appendDescriptor(1, "I");
/*      */             break;
/*      */           case 2:
/* 1439 */             appendDescriptor(1, "F");
/*      */             break;
/*      */           case 3:
/* 1442 */             appendDescriptor(1, "D");
/*      */             break;
/*      */           case 4:
/* 1445 */             appendDescriptor(1, "J");
/*      */             break;
/*      */           case 5:
/* 1448 */             appendDescriptor(1, "N");
/*      */             break;
/*      */           case 6:
/* 1451 */             appendDescriptor(1, "U");
/*      */             break;
/*      */         } 
/*      */       } else {
/* 1455 */         appendLabel((Label)paramArrayOfObject[b]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\Textifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */