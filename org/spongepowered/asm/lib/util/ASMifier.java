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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ASMifier
/*      */   extends Printer
/*      */ {
/*      */   protected final String name;
/*      */   protected final int id;
/*      */   protected Map<Label, String> labelNames;
/*      */   private static final int ACCESS_CLASS = 262144;
/*      */   private static final int ACCESS_FIELD = 524288;
/*      */   private static final int ACCESS_INNER = 1048576;
/*      */   
/*      */   public ASMifier() {
/*   92 */     this(327680, "cw", 0);
/*   93 */     if (getClass() != ASMifier.class) {
/*   94 */       throw new IllegalStateException();
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
/*      */   protected ASMifier(int paramInt1, String paramString, int paramInt2) {
/*  111 */     super(paramInt1);
/*  112 */     this.name = paramString;
/*  113 */     this.id = paramInt2;
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
/*      */   public static void main(String[] paramArrayOfString) throws Exception {
/*      */     ClassReader classReader;
/*  129 */     boolean bool1 = false;
/*  130 */     byte b = 2;
/*      */     
/*  132 */     boolean bool2 = true;
/*  133 */     if (paramArrayOfString.length < 1 || paramArrayOfString.length > 2) {
/*  134 */       bool2 = false;
/*      */     }
/*  136 */     if (bool2 && "-debug".equals(paramArrayOfString[0])) {
/*  137 */       bool1 = true;
/*  138 */       b = 0;
/*  139 */       if (paramArrayOfString.length != 2) {
/*  140 */         bool2 = false;
/*      */       }
/*      */     } 
/*  143 */     if (!bool2) {
/*  144 */       System.err
/*  145 */         .println("Prints the ASM code to generate the given class.");
/*  146 */       System.err.println("Usage: ASMifier [-debug] <fully qualified class name or class file name>");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  151 */     if (paramArrayOfString[bool1].endsWith(".class") || paramArrayOfString[bool1].indexOf('\\') > -1 || paramArrayOfString[bool1]
/*  152 */       .indexOf('/') > -1) {
/*  153 */       classReader = new ClassReader(new FileInputStream(paramArrayOfString[bool1]));
/*      */     } else {
/*  155 */       classReader = new ClassReader(paramArrayOfString[bool1]);
/*      */     } 
/*  157 */     classReader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), b);
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
/*      */   public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*      */     String str;
/*  170 */     int i = paramString1.lastIndexOf('/');
/*  171 */     if (i == -1) {
/*  172 */       str = paramString1;
/*      */     } else {
/*  174 */       this.text.add("package asm." + paramString1.substring(0, i).replace('/', '.') + ";\n");
/*      */       
/*  176 */       str = paramString1.substring(i + 1);
/*      */     } 
/*  178 */     this.text.add("import java.util.*;\n");
/*  179 */     this.text.add("import org.objectweb.asm.*;\n");
/*  180 */     this.text.add("public class " + str + "Dump implements Opcodes {\n\n");
/*  181 */     this.text.add("public static byte[] dump () throws Exception {\n\n");
/*  182 */     this.text.add("ClassWriter cw = new ClassWriter(0);\n");
/*  183 */     this.text.add("FieldVisitor fv;\n");
/*  184 */     this.text.add("MethodVisitor mv;\n");
/*  185 */     this.text.add("AnnotationVisitor av0;\n\n");
/*      */     
/*  187 */     this.buf.setLength(0);
/*  188 */     this.buf.append("cw.visit(");
/*  189 */     switch (paramInt1) {
/*      */       case 196653:
/*  191 */         this.buf.append("V1_1");
/*      */         break;
/*      */       case 46:
/*  194 */         this.buf.append("V1_2");
/*      */         break;
/*      */       case 47:
/*  197 */         this.buf.append("V1_3");
/*      */         break;
/*      */       case 48:
/*  200 */         this.buf.append("V1_4");
/*      */         break;
/*      */       case 49:
/*  203 */         this.buf.append("V1_5");
/*      */         break;
/*      */       case 50:
/*  206 */         this.buf.append("V1_6");
/*      */         break;
/*      */       case 51:
/*  209 */         this.buf.append("V1_7");
/*      */         break;
/*      */       default:
/*  212 */         this.buf.append(paramInt1);
/*      */         break;
/*      */     } 
/*  215 */     this.buf.append(", ");
/*  216 */     appendAccess(paramInt2 | 0x40000);
/*  217 */     this.buf.append(", ");
/*  218 */     appendConstant(paramString1);
/*  219 */     this.buf.append(", ");
/*  220 */     appendConstant(paramString2);
/*  221 */     this.buf.append(", ");
/*  222 */     appendConstant(paramString3);
/*  223 */     this.buf.append(", ");
/*  224 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/*  225 */       this.buf.append("new String[] {");
/*  226 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  227 */         this.buf.append((b == 0) ? " " : ", ");
/*  228 */         appendConstant(paramArrayOfString[b]);
/*      */       } 
/*  230 */       this.buf.append(" }");
/*      */     } else {
/*  232 */       this.buf.append("null");
/*      */     } 
/*  234 */     this.buf.append(");\n\n");
/*  235 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitSource(String paramString1, String paramString2) {
/*  240 */     this.buf.setLength(0);
/*  241 */     this.buf.append("cw.visitSource(");
/*  242 */     appendConstant(paramString1);
/*  243 */     this.buf.append(", ");
/*  244 */     appendConstant(paramString2);
/*  245 */     this.buf.append(");\n\n");
/*  246 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/*  252 */     this.buf.setLength(0);
/*  253 */     this.buf.append("cw.visitOuterClass(");
/*  254 */     appendConstant(paramString1);
/*  255 */     this.buf.append(", ");
/*  256 */     appendConstant(paramString2);
/*  257 */     this.buf.append(", ");
/*  258 */     appendConstant(paramString3);
/*  259 */     this.buf.append(");\n\n");
/*  260 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitClassAnnotation(String paramString, boolean paramBoolean) {
/*  266 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitClassTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  272 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitClassAttribute(Attribute paramAttribute) {
/*  277 */     visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/*  283 */     this.buf.setLength(0);
/*  284 */     this.buf.append("cw.visitInnerClass(");
/*  285 */     appendConstant(paramString1);
/*  286 */     this.buf.append(", ");
/*  287 */     appendConstant(paramString2);
/*  288 */     this.buf.append(", ");
/*  289 */     appendConstant(paramString3);
/*  290 */     this.buf.append(", ");
/*  291 */     appendAccess(paramInt | 0x100000);
/*  292 */     this.buf.append(");\n\n");
/*  293 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/*  299 */     this.buf.setLength(0);
/*  300 */     this.buf.append("{\n");
/*  301 */     this.buf.append("fv = cw.visitField(");
/*  302 */     appendAccess(paramInt | 0x80000);
/*  303 */     this.buf.append(", ");
/*  304 */     appendConstant(paramString1);
/*  305 */     this.buf.append(", ");
/*  306 */     appendConstant(paramString2);
/*  307 */     this.buf.append(", ");
/*  308 */     appendConstant(paramString3);
/*  309 */     this.buf.append(", ");
/*  310 */     appendConstant(paramObject);
/*  311 */     this.buf.append(");\n");
/*  312 */     this.text.add(this.buf.toString());
/*  313 */     ASMifier aSMifier = createASMifier("fv", 0);
/*  314 */     this.text.add(aSMifier.getText());
/*  315 */     this.text.add("}\n");
/*  316 */     return aSMifier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  322 */     this.buf.setLength(0);
/*  323 */     this.buf.append("{\n");
/*  324 */     this.buf.append("mv = cw.visitMethod(");
/*  325 */     appendAccess(paramInt);
/*  326 */     this.buf.append(", ");
/*  327 */     appendConstant(paramString1);
/*  328 */     this.buf.append(", ");
/*  329 */     appendConstant(paramString2);
/*  330 */     this.buf.append(", ");
/*  331 */     appendConstant(paramString3);
/*  332 */     this.buf.append(", ");
/*  333 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/*  334 */       this.buf.append("new String[] {");
/*  335 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  336 */         this.buf.append((b == 0) ? " " : ", ");
/*  337 */         appendConstant(paramArrayOfString[b]);
/*      */       } 
/*  339 */       this.buf.append(" }");
/*      */     } else {
/*  341 */       this.buf.append("null");
/*      */     } 
/*  343 */     this.buf.append(");\n");
/*  344 */     this.text.add(this.buf.toString());
/*  345 */     ASMifier aSMifier = createASMifier("mv", 0);
/*  346 */     this.text.add(aSMifier.getText());
/*  347 */     this.text.add("}\n");
/*  348 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitClassEnd() {
/*  353 */     this.text.add("cw.visitEnd();\n\n");
/*  354 */     this.text.add("return cw.toByteArray();\n");
/*  355 */     this.text.add("}\n");
/*  356 */     this.text.add("}\n");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visit(String paramString, Object paramObject) {
/*  365 */     this.buf.setLength(0);
/*  366 */     this.buf.append("av").append(this.id).append(".visit(");
/*  367 */     appendConstant(this.buf, paramString);
/*  368 */     this.buf.append(", ");
/*  369 */     appendConstant(this.buf, paramObject);
/*  370 */     this.buf.append(");\n");
/*  371 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitEnum(String paramString1, String paramString2, String paramString3) {
/*  377 */     this.buf.setLength(0);
/*  378 */     this.buf.append("av").append(this.id).append(".visitEnum(");
/*  379 */     appendConstant(this.buf, paramString1);
/*  380 */     this.buf.append(", ");
/*  381 */     appendConstant(this.buf, paramString2);
/*  382 */     this.buf.append(", ");
/*  383 */     appendConstant(this.buf, paramString3);
/*  384 */     this.buf.append(");\n");
/*  385 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public ASMifier visitAnnotation(String paramString1, String paramString2) {
/*  390 */     this.buf.setLength(0);
/*  391 */     this.buf.append("{\n");
/*  392 */     this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
/*  393 */     this.buf.append(this.id).append(".visitAnnotation(");
/*  394 */     appendConstant(this.buf, paramString1);
/*  395 */     this.buf.append(", ");
/*  396 */     appendConstant(this.buf, paramString2);
/*  397 */     this.buf.append(");\n");
/*  398 */     this.text.add(this.buf.toString());
/*  399 */     ASMifier aSMifier = createASMifier("av", this.id + 1);
/*  400 */     this.text.add(aSMifier.getText());
/*  401 */     this.text.add("}\n");
/*  402 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public ASMifier visitArray(String paramString) {
/*  407 */     this.buf.setLength(0);
/*  408 */     this.buf.append("{\n");
/*  409 */     this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
/*  410 */     this.buf.append(this.id).append(".visitArray(");
/*  411 */     appendConstant(this.buf, paramString);
/*  412 */     this.buf.append(");\n");
/*  413 */     this.text.add(this.buf.toString());
/*  414 */     ASMifier aSMifier = createASMifier("av", this.id + 1);
/*  415 */     this.text.add(aSMifier.getText());
/*  416 */     this.text.add("}\n");
/*  417 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitAnnotationEnd() {
/*  422 */     this.buf.setLength(0);
/*  423 */     this.buf.append("av").append(this.id).append(".visitEnd();\n");
/*  424 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitFieldAnnotation(String paramString, boolean paramBoolean) {
/*  434 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitFieldTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  440 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitFieldAttribute(Attribute paramAttribute) {
/*  445 */     visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitFieldEnd() {
/*  450 */     this.buf.setLength(0);
/*  451 */     this.buf.append(this.name).append(".visitEnd();\n");
/*  452 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitParameter(String paramString, int paramInt) {
/*  461 */     this.buf.setLength(0);
/*  462 */     this.buf.append(this.name).append(".visitParameter(");
/*  463 */     appendString(this.buf, paramString);
/*  464 */     this.buf.append(", ");
/*  465 */     appendAccess(paramInt);
/*  466 */     this.text.add(this.buf.append(");\n").toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public ASMifier visitAnnotationDefault() {
/*  471 */     this.buf.setLength(0);
/*  472 */     this.buf.append("{\n").append("av0 = ").append(this.name)
/*  473 */       .append(".visitAnnotationDefault();\n");
/*  474 */     this.text.add(this.buf.toString());
/*  475 */     ASMifier aSMifier = createASMifier("av", 0);
/*  476 */     this.text.add(aSMifier.getText());
/*  477 */     this.text.add("}\n");
/*  478 */     return aSMifier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitMethodAnnotation(String paramString, boolean paramBoolean) {
/*  484 */     return visitAnnotation(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitMethodTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  490 */     return visitTypeAnnotation(paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean) {
/*  496 */     this.buf.setLength(0);
/*  497 */     this.buf.append("{\n").append("av0 = ").append(this.name)
/*  498 */       .append(".visitParameterAnnotation(").append(paramInt)
/*  499 */       .append(", ");
/*  500 */     appendConstant(paramString);
/*  501 */     this.buf.append(", ").append(paramBoolean).append(");\n");
/*  502 */     this.text.add(this.buf.toString());
/*  503 */     ASMifier aSMifier = createASMifier("av", 0);
/*  504 */     this.text.add(aSMifier.getText());
/*  505 */     this.text.add("}\n");
/*  506 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMethodAttribute(Attribute paramAttribute) {
/*  511 */     visitAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitCode() {
/*  516 */     this.text.add(this.name + ".visitCode();\n");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/*  522 */     this.buf.setLength(0);
/*  523 */     switch (paramInt1) {
/*      */       case -1:
/*      */       case 0:
/*  526 */         declareFrameTypes(paramInt2, paramArrayOfObject1);
/*  527 */         declareFrameTypes(paramInt3, paramArrayOfObject2);
/*  528 */         if (paramInt1 == -1) {
/*  529 */           this.buf.append(this.name).append(".visitFrame(Opcodes.F_NEW, ");
/*      */         } else {
/*  531 */           this.buf.append(this.name).append(".visitFrame(Opcodes.F_FULL, ");
/*      */         } 
/*  533 */         this.buf.append(paramInt2).append(", new Object[] {");
/*  534 */         appendFrameTypes(paramInt2, paramArrayOfObject1);
/*  535 */         this.buf.append("}, ").append(paramInt3).append(", new Object[] {");
/*  536 */         appendFrameTypes(paramInt3, paramArrayOfObject2);
/*  537 */         this.buf.append('}');
/*      */         break;
/*      */       case 1:
/*  540 */         declareFrameTypes(paramInt2, paramArrayOfObject1);
/*  541 */         this.buf.append(this.name).append(".visitFrame(Opcodes.F_APPEND,")
/*  542 */           .append(paramInt2).append(", new Object[] {");
/*  543 */         appendFrameTypes(paramInt2, paramArrayOfObject1);
/*  544 */         this.buf.append("}, 0, null");
/*      */         break;
/*      */       case 2:
/*  547 */         this.buf.append(this.name).append(".visitFrame(Opcodes.F_CHOP,")
/*  548 */           .append(paramInt2).append(", null, 0, null");
/*      */         break;
/*      */       case 3:
/*  551 */         this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME, 0, null, 0, null");
/*      */         break;
/*      */       
/*      */       case 4:
/*  555 */         declareFrameTypes(1, paramArrayOfObject2);
/*  556 */         this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {");
/*      */         
/*  558 */         appendFrameTypes(1, paramArrayOfObject2);
/*  559 */         this.buf.append('}');
/*      */         break;
/*      */     } 
/*  562 */     this.buf.append(");\n");
/*  563 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitInsn(int paramInt) {
/*  568 */     this.buf.setLength(0);
/*  569 */     this.buf.append(this.name).append(".visitInsn(").append(OPCODES[paramInt])
/*  570 */       .append(");\n");
/*  571 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIntInsn(int paramInt1, int paramInt2) {
/*  576 */     this.buf.setLength(0);
/*  577 */     this.buf.append(this.name)
/*  578 */       .append(".visitIntInsn(")
/*  579 */       .append(OPCODES[paramInt1])
/*  580 */       .append(", ")
/*  581 */       .append((paramInt1 == 188) ? TYPES[paramInt2] : 
/*  582 */         Integer.toString(paramInt2)).append(");\n");
/*  583 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitVarInsn(int paramInt1, int paramInt2) {
/*  588 */     this.buf.setLength(0);
/*  589 */     this.buf.append(this.name).append(".visitVarInsn(").append(OPCODES[paramInt1])
/*  590 */       .append(", ").append(paramInt2).append(");\n");
/*  591 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeInsn(int paramInt, String paramString) {
/*  596 */     this.buf.setLength(0);
/*  597 */     this.buf.append(this.name).append(".visitTypeInsn(").append(OPCODES[paramInt])
/*  598 */       .append(", ");
/*  599 */     appendConstant(paramString);
/*  600 */     this.buf.append(");\n");
/*  601 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  607 */     this.buf.setLength(0);
/*  608 */     this.buf.append(this.name).append(".visitFieldInsn(")
/*  609 */       .append(OPCODES[paramInt]).append(", ");
/*  610 */     appendConstant(paramString1);
/*  611 */     this.buf.append(", ");
/*  612 */     appendConstant(paramString2);
/*  613 */     this.buf.append(", ");
/*  614 */     appendConstant(paramString3);
/*  615 */     this.buf.append(");\n");
/*  616 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3) {
/*  623 */     if (this.api >= 327680) {
/*  624 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
/*      */       return;
/*      */     } 
/*  627 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, (paramInt == 185));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/*  634 */     if (this.api < 327680) {
/*  635 */       super.visitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*      */       return;
/*      */     } 
/*  638 */     doVisitMethodInsn(paramInt, paramString1, paramString2, paramString3, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   private void doVisitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/*  643 */     this.buf.setLength(0);
/*  644 */     this.buf.append(this.name).append(".visitMethodInsn(")
/*  645 */       .append(OPCODES[paramInt]).append(", ");
/*  646 */     appendConstant(paramString1);
/*  647 */     this.buf.append(", ");
/*  648 */     appendConstant(paramString2);
/*  649 */     this.buf.append(", ");
/*  650 */     appendConstant(paramString3);
/*  651 */     this.buf.append(", ");
/*  652 */     this.buf.append(paramBoolean ? "true" : "false");
/*  653 */     this.buf.append(");\n");
/*  654 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInvokeDynamicInsn(String paramString1, String paramString2, Handle paramHandle, Object... paramVarArgs) {
/*  660 */     this.buf.setLength(0);
/*  661 */     this.buf.append(this.name).append(".visitInvokeDynamicInsn(");
/*  662 */     appendConstant(paramString1);
/*  663 */     this.buf.append(", ");
/*  664 */     appendConstant(paramString2);
/*  665 */     this.buf.append(", ");
/*  666 */     appendConstant(paramHandle);
/*  667 */     this.buf.append(", new Object[]{");
/*  668 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/*  669 */       appendConstant(paramVarArgs[b]);
/*  670 */       if (b != paramVarArgs.length - 1) {
/*  671 */         this.buf.append(", ");
/*      */       }
/*      */     } 
/*  674 */     this.buf.append("});\n");
/*  675 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int paramInt, Label paramLabel) {
/*  680 */     this.buf.setLength(0);
/*  681 */     declareLabel(paramLabel);
/*  682 */     this.buf.append(this.name).append(".visitJumpInsn(").append(OPCODES[paramInt])
/*  683 */       .append(", ");
/*  684 */     appendLabel(paramLabel);
/*  685 */     this.buf.append(");\n");
/*  686 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLabel(Label paramLabel) {
/*  691 */     this.buf.setLength(0);
/*  692 */     declareLabel(paramLabel);
/*  693 */     this.buf.append(this.name).append(".visitLabel(");
/*  694 */     appendLabel(paramLabel);
/*  695 */     this.buf.append(");\n");
/*  696 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLdcInsn(Object paramObject) {
/*  701 */     this.buf.setLength(0);
/*  702 */     this.buf.append(this.name).append(".visitLdcInsn(");
/*  703 */     appendConstant(paramObject);
/*  704 */     this.buf.append(");\n");
/*  705 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIincInsn(int paramInt1, int paramInt2) {
/*  710 */     this.buf.setLength(0);
/*  711 */     this.buf.append(this.name).append(".visitIincInsn(").append(paramInt1).append(", ")
/*  712 */       .append(paramInt2).append(");\n");
/*  713 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label... paramVarArgs) {
/*  719 */     this.buf.setLength(0); byte b;
/*  720 */     for (b = 0; b < paramVarArgs.length; b++) {
/*  721 */       declareLabel(paramVarArgs[b]);
/*      */     }
/*  723 */     declareLabel(paramLabel);
/*      */     
/*  725 */     this.buf.append(this.name).append(".visitTableSwitchInsn(").append(paramInt1)
/*  726 */       .append(", ").append(paramInt2).append(", ");
/*  727 */     appendLabel(paramLabel);
/*  728 */     this.buf.append(", new Label[] {");
/*  729 */     for (b = 0; b < paramVarArgs.length; b++) {
/*  730 */       this.buf.append((b == 0) ? " " : ", ");
/*  731 */       appendLabel(paramVarArgs[b]);
/*      */     } 
/*  733 */     this.buf.append(" });\n");
/*  734 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel) {
/*  740 */     this.buf.setLength(0); byte b;
/*  741 */     for (b = 0; b < paramArrayOfLabel.length; b++) {
/*  742 */       declareLabel(paramArrayOfLabel[b]);
/*      */     }
/*  744 */     declareLabel(paramLabel);
/*      */     
/*  746 */     this.buf.append(this.name).append(".visitLookupSwitchInsn(");
/*  747 */     appendLabel(paramLabel);
/*  748 */     this.buf.append(", new int[] {");
/*  749 */     for (b = 0; b < paramArrayOfint.length; b++) {
/*  750 */       this.buf.append((b == 0) ? " " : ", ").append(paramArrayOfint[b]);
/*      */     }
/*  752 */     this.buf.append(" }, new Label[] {");
/*  753 */     for (b = 0; b < paramArrayOfLabel.length; b++) {
/*  754 */       this.buf.append((b == 0) ? " " : ", ");
/*  755 */       appendLabel(paramArrayOfLabel[b]);
/*      */     } 
/*  757 */     this.buf.append(" });\n");
/*  758 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMultiANewArrayInsn(String paramString, int paramInt) {
/*  763 */     this.buf.setLength(0);
/*  764 */     this.buf.append(this.name).append(".visitMultiANewArrayInsn(");
/*  765 */     appendConstant(paramString);
/*  766 */     this.buf.append(", ").append(paramInt).append(");\n");
/*  767 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitInsnAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  773 */     return visitTypeAnnotation("visitInsnAnnotation", paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString) {
/*  780 */     this.buf.setLength(0);
/*  781 */     declareLabel(paramLabel1);
/*  782 */     declareLabel(paramLabel2);
/*  783 */     declareLabel(paramLabel3);
/*  784 */     this.buf.append(this.name).append(".visitTryCatchBlock(");
/*  785 */     appendLabel(paramLabel1);
/*  786 */     this.buf.append(", ");
/*  787 */     appendLabel(paramLabel2);
/*  788 */     this.buf.append(", ");
/*  789 */     appendLabel(paramLabel3);
/*  790 */     this.buf.append(", ");
/*  791 */     appendConstant(paramString);
/*  792 */     this.buf.append(");\n");
/*  793 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitTryCatchAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  799 */     return visitTypeAnnotation("visitTryCatchAnnotation", paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt) {
/*  807 */     this.buf.setLength(0);
/*  808 */     this.buf.append(this.name).append(".visitLocalVariable(");
/*  809 */     appendConstant(paramString1);
/*  810 */     this.buf.append(", ");
/*  811 */     appendConstant(paramString2);
/*  812 */     this.buf.append(", ");
/*  813 */     appendConstant(paramString3);
/*  814 */     this.buf.append(", ");
/*  815 */     appendLabel(paramLabel1);
/*  816 */     this.buf.append(", ");
/*  817 */     appendLabel(paramLabel2);
/*  818 */     this.buf.append(", ").append(paramInt).append(");\n");
/*  819 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Printer visitLocalVariableAnnotation(int paramInt, TypePath paramTypePath, Label[] paramArrayOfLabel1, Label[] paramArrayOfLabel2, int[] paramArrayOfint, String paramString, boolean paramBoolean) {
/*  826 */     this.buf.setLength(0);
/*  827 */     this.buf.append("{\n").append("av0 = ").append(this.name)
/*  828 */       .append(".visitLocalVariableAnnotation(");
/*  829 */     this.buf.append(paramInt);
/*  830 */     if (paramTypePath == null) {
/*  831 */       this.buf.append(", null, ");
/*      */     } else {
/*  833 */       this.buf.append(", TypePath.fromString(\"").append(paramTypePath).append("\"), ");
/*      */     } 
/*  835 */     this.buf.append("new Label[] {"); byte b;
/*  836 */     for (b = 0; b < paramArrayOfLabel1.length; b++) {
/*  837 */       this.buf.append((b == 0) ? " " : ", ");
/*  838 */       appendLabel(paramArrayOfLabel1[b]);
/*      */     } 
/*  840 */     this.buf.append(" }, new Label[] {");
/*  841 */     for (b = 0; b < paramArrayOfLabel2.length; b++) {
/*  842 */       this.buf.append((b == 0) ? " " : ", ");
/*  843 */       appendLabel(paramArrayOfLabel2[b]);
/*      */     } 
/*  845 */     this.buf.append(" }, new int[] {");
/*  846 */     for (b = 0; b < paramArrayOfint.length; b++) {
/*  847 */       this.buf.append((b == 0) ? " " : ", ").append(paramArrayOfint[b]);
/*      */     }
/*  849 */     this.buf.append(" }, ");
/*  850 */     appendConstant(paramString);
/*  851 */     this.buf.append(", ").append(paramBoolean).append(");\n");
/*  852 */     this.text.add(this.buf.toString());
/*  853 */     ASMifier aSMifier = createASMifier("av", 0);
/*  854 */     this.text.add(aSMifier.getText());
/*  855 */     this.text.add("}\n");
/*  856 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int paramInt, Label paramLabel) {
/*  861 */     this.buf.setLength(0);
/*  862 */     this.buf.append(this.name).append(".visitLineNumber(").append(paramInt).append(", ");
/*  863 */     appendLabel(paramLabel);
/*  864 */     this.buf.append(");\n");
/*  865 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMaxs(int paramInt1, int paramInt2) {
/*  870 */     this.buf.setLength(0);
/*  871 */     this.buf.append(this.name).append(".visitMaxs(").append(paramInt1).append(", ")
/*  872 */       .append(paramInt2).append(");\n");
/*  873 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMethodEnd() {
/*  878 */     this.buf.setLength(0);
/*  879 */     this.buf.append(this.name).append(".visitEnd();\n");
/*  880 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitAnnotation(String paramString, boolean paramBoolean) {
/*  888 */     this.buf.setLength(0);
/*  889 */     this.buf.append("{\n").append("av0 = ").append(this.name)
/*  890 */       .append(".visitAnnotation(");
/*  891 */     appendConstant(paramString);
/*  892 */     this.buf.append(", ").append(paramBoolean).append(");\n");
/*  893 */     this.text.add(this.buf.toString());
/*  894 */     ASMifier aSMifier = createASMifier("av", 0);
/*  895 */     this.text.add(aSMifier.getText());
/*  896 */     this.text.add("}\n");
/*  897 */     return aSMifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public ASMifier visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/*  902 */     return visitTypeAnnotation("visitTypeAnnotation", paramInt, paramTypePath, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASMifier visitTypeAnnotation(String paramString1, int paramInt, TypePath paramTypePath, String paramString2, boolean paramBoolean) {
/*  908 */     this.buf.setLength(0);
/*  909 */     this.buf.append("{\n").append("av0 = ").append(this.name).append(".")
/*  910 */       .append(paramString1).append("(");
/*  911 */     this.buf.append(paramInt);
/*  912 */     if (paramTypePath == null) {
/*  913 */       this.buf.append(", null, ");
/*      */     } else {
/*  915 */       this.buf.append(", TypePath.fromString(\"").append(paramTypePath).append("\"), ");
/*      */     } 
/*  917 */     appendConstant(paramString2);
/*  918 */     this.buf.append(", ").append(paramBoolean).append(");\n");
/*  919 */     this.text.add(this.buf.toString());
/*  920 */     ASMifier aSMifier = createASMifier("av", 0);
/*  921 */     this.text.add(aSMifier.getText());
/*  922 */     this.text.add("}\n");
/*  923 */     return aSMifier;
/*      */   }
/*      */   
/*      */   public void visitAttribute(Attribute paramAttribute) {
/*  927 */     this.buf.setLength(0);
/*  928 */     this.buf.append("// ATTRIBUTE ").append(paramAttribute.type).append('\n');
/*  929 */     if (paramAttribute instanceof ASMifiable) {
/*  930 */       if (this.labelNames == null) {
/*  931 */         this.labelNames = new HashMap<Label, String>();
/*      */       }
/*  933 */       this.buf.append("{\n");
/*  934 */       ((ASMifiable)paramAttribute).asmify(this.buf, "attr", this.labelNames);
/*  935 */       this.buf.append(this.name).append(".visitAttribute(attr);\n");
/*  936 */       this.buf.append("}\n");
/*      */     } 
/*  938 */     this.text.add(this.buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ASMifier createASMifier(String paramString, int paramInt) {
/*  946 */     return new ASMifier(327680, paramString, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void appendAccess(int paramInt) {
/*  957 */     boolean bool = true;
/*  958 */     if ((paramInt & 0x1) != 0) {
/*  959 */       this.buf.append("ACC_PUBLIC");
/*  960 */       bool = false;
/*      */     } 
/*  962 */     if ((paramInt & 0x2) != 0) {
/*  963 */       this.buf.append("ACC_PRIVATE");
/*  964 */       bool = false;
/*      */     } 
/*  966 */     if ((paramInt & 0x4) != 0) {
/*  967 */       this.buf.append("ACC_PROTECTED");
/*  968 */       bool = false;
/*      */     } 
/*  970 */     if ((paramInt & 0x10) != 0) {
/*  971 */       if (!bool) {
/*  972 */         this.buf.append(" + ");
/*      */       }
/*  974 */       this.buf.append("ACC_FINAL");
/*  975 */       bool = false;
/*      */     } 
/*  977 */     if ((paramInt & 0x8) != 0) {
/*  978 */       if (!bool) {
/*  979 */         this.buf.append(" + ");
/*      */       }
/*  981 */       this.buf.append("ACC_STATIC");
/*  982 */       bool = false;
/*      */     } 
/*  984 */     if ((paramInt & 0x20) != 0) {
/*  985 */       if (!bool) {
/*  986 */         this.buf.append(" + ");
/*      */       }
/*  988 */       if ((paramInt & 0x40000) == 0) {
/*  989 */         this.buf.append("ACC_SYNCHRONIZED");
/*      */       } else {
/*  991 */         this.buf.append("ACC_SUPER");
/*      */       } 
/*  993 */       bool = false;
/*      */     } 
/*  995 */     if ((paramInt & 0x40) != 0 && (paramInt & 0x80000) != 0) {
/*      */       
/*  997 */       if (!bool) {
/*  998 */         this.buf.append(" + ");
/*      */       }
/* 1000 */       this.buf.append("ACC_VOLATILE");
/* 1001 */       bool = false;
/*      */     } 
/* 1003 */     if ((paramInt & 0x40) != 0 && (paramInt & 0x40000) == 0 && (paramInt & 0x80000) == 0) {
/*      */       
/* 1005 */       if (!bool) {
/* 1006 */         this.buf.append(" + ");
/*      */       }
/* 1008 */       this.buf.append("ACC_BRIDGE");
/* 1009 */       bool = false;
/*      */     } 
/* 1011 */     if ((paramInt & 0x80) != 0 && (paramInt & 0x40000) == 0 && (paramInt & 0x80000) == 0) {
/*      */       
/* 1013 */       if (!bool) {
/* 1014 */         this.buf.append(" + ");
/*      */       }
/* 1016 */       this.buf.append("ACC_VARARGS");
/* 1017 */       bool = false;
/*      */     } 
/* 1019 */     if ((paramInt & 0x80) != 0 && (paramInt & 0x80000) != 0) {
/*      */       
/* 1021 */       if (!bool) {
/* 1022 */         this.buf.append(" + ");
/*      */       }
/* 1024 */       this.buf.append("ACC_TRANSIENT");
/* 1025 */       bool = false;
/*      */     } 
/* 1027 */     if ((paramInt & 0x100) != 0 && (paramInt & 0x40000) == 0 && (paramInt & 0x80000) == 0) {
/*      */       
/* 1029 */       if (!bool) {
/* 1030 */         this.buf.append(" + ");
/*      */       }
/* 1032 */       this.buf.append("ACC_NATIVE");
/* 1033 */       bool = false;
/*      */     } 
/* 1035 */     if ((paramInt & 0x4000) != 0 && ((paramInt & 0x40000) != 0 || (paramInt & 0x80000) != 0 || (paramInt & 0x100000) != 0)) {
/*      */ 
/*      */       
/* 1038 */       if (!bool) {
/* 1039 */         this.buf.append(" + ");
/*      */       }
/* 1041 */       this.buf.append("ACC_ENUM");
/* 1042 */       bool = false;
/*      */     } 
/* 1044 */     if ((paramInt & 0x2000) != 0 && ((paramInt & 0x40000) != 0 || (paramInt & 0x100000) != 0)) {
/*      */       
/* 1046 */       if (!bool) {
/* 1047 */         this.buf.append(" + ");
/*      */       }
/* 1049 */       this.buf.append("ACC_ANNOTATION");
/* 1050 */       bool = false;
/*      */     } 
/* 1052 */     if ((paramInt & 0x400) != 0) {
/* 1053 */       if (!bool) {
/* 1054 */         this.buf.append(" + ");
/*      */       }
/* 1056 */       this.buf.append("ACC_ABSTRACT");
/* 1057 */       bool = false;
/*      */     } 
/* 1059 */     if ((paramInt & 0x200) != 0) {
/* 1060 */       if (!bool) {
/* 1061 */         this.buf.append(" + ");
/*      */       }
/* 1063 */       this.buf.append("ACC_INTERFACE");
/* 1064 */       bool = false;
/*      */     } 
/* 1066 */     if ((paramInt & 0x800) != 0) {
/* 1067 */       if (!bool) {
/* 1068 */         this.buf.append(" + ");
/*      */       }
/* 1070 */       this.buf.append("ACC_STRICT");
/* 1071 */       bool = false;
/*      */     } 
/* 1073 */     if ((paramInt & 0x1000) != 0) {
/* 1074 */       if (!bool) {
/* 1075 */         this.buf.append(" + ");
/*      */       }
/* 1077 */       this.buf.append("ACC_SYNTHETIC");
/* 1078 */       bool = false;
/*      */     } 
/* 1080 */     if ((paramInt & 0x20000) != 0) {
/* 1081 */       if (!bool) {
/* 1082 */         this.buf.append(" + ");
/*      */       }
/* 1084 */       this.buf.append("ACC_DEPRECATED");
/* 1085 */       bool = false;
/*      */     } 
/* 1087 */     if ((paramInt & 0x8000) != 0) {
/* 1088 */       if (!bool) {
/* 1089 */         this.buf.append(" + ");
/*      */       }
/* 1091 */       this.buf.append("ACC_MANDATED");
/* 1092 */       bool = false;
/*      */     } 
/* 1094 */     if (bool) {
/* 1095 */       this.buf.append('0');
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
/*      */   protected void appendConstant(Object paramObject) {
/* 1108 */     appendConstant(this.buf, paramObject);
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
/*      */   static void appendConstant(StringBuffer paramStringBuffer, Object paramObject) {
/* 1122 */     if (paramObject == null) {
/* 1123 */       paramStringBuffer.append("null");
/* 1124 */     } else if (paramObject instanceof String) {
/* 1125 */       appendString(paramStringBuffer, (String)paramObject);
/* 1126 */     } else if (paramObject instanceof Type) {
/* 1127 */       paramStringBuffer.append("Type.getType(\"");
/* 1128 */       paramStringBuffer.append(((Type)paramObject).getDescriptor());
/* 1129 */       paramStringBuffer.append("\")");
/* 1130 */     } else if (paramObject instanceof Handle) {
/* 1131 */       paramStringBuffer.append("new Handle(");
/* 1132 */       Handle handle = (Handle)paramObject;
/* 1133 */       paramStringBuffer.append("Opcodes.").append(HANDLE_TAG[handle.getTag()])
/* 1134 */         .append(", \"");
/* 1135 */       paramStringBuffer.append(handle.getOwner()).append("\", \"");
/* 1136 */       paramStringBuffer.append(handle.getName()).append("\", \"");
/* 1137 */       paramStringBuffer.append(handle.getDesc()).append("\")");
/* 1138 */     } else if (paramObject instanceof Byte) {
/* 1139 */       paramStringBuffer.append("new Byte((byte)").append(paramObject).append(')');
/* 1140 */     } else if (paramObject instanceof Boolean) {
/* 1141 */       paramStringBuffer.append(((Boolean)paramObject).booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
/*      */     }
/* 1143 */     else if (paramObject instanceof Short) {
/* 1144 */       paramStringBuffer.append("new Short((short)").append(paramObject).append(')');
/* 1145 */     } else if (paramObject instanceof Character) {
/* 1146 */       char c = ((Character)paramObject).charValue();
/* 1147 */       paramStringBuffer.append("new Character((char)").append(c).append(')');
/* 1148 */     } else if (paramObject instanceof Integer) {
/* 1149 */       paramStringBuffer.append("new Integer(").append(paramObject).append(')');
/* 1150 */     } else if (paramObject instanceof Float) {
/* 1151 */       paramStringBuffer.append("new Float(\"").append(paramObject).append("\")");
/* 1152 */     } else if (paramObject instanceof Long) {
/* 1153 */       paramStringBuffer.append("new Long(").append(paramObject).append("L)");
/* 1154 */     } else if (paramObject instanceof Double) {
/* 1155 */       paramStringBuffer.append("new Double(\"").append(paramObject).append("\")");
/* 1156 */     } else if (paramObject instanceof byte[]) {
/* 1157 */       byte[] arrayOfByte = (byte[])paramObject;
/* 1158 */       paramStringBuffer.append("new byte[] {");
/* 1159 */       for (byte b = 0; b < arrayOfByte.length; b++) {
/* 1160 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfByte[b]);
/*      */       }
/* 1162 */       paramStringBuffer.append('}');
/* 1163 */     } else if (paramObject instanceof boolean[]) {
/* 1164 */       boolean[] arrayOfBoolean = (boolean[])paramObject;
/* 1165 */       paramStringBuffer.append("new boolean[] {");
/* 1166 */       for (byte b = 0; b < arrayOfBoolean.length; b++) {
/* 1167 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfBoolean[b]);
/*      */       }
/* 1169 */       paramStringBuffer.append('}');
/* 1170 */     } else if (paramObject instanceof short[]) {
/* 1171 */       short[] arrayOfShort = (short[])paramObject;
/* 1172 */       paramStringBuffer.append("new short[] {");
/* 1173 */       for (byte b = 0; b < arrayOfShort.length; b++) {
/* 1174 */         paramStringBuffer.append((b == 0) ? "" : ",").append("(short)").append(arrayOfShort[b]);
/*      */       }
/* 1176 */       paramStringBuffer.append('}');
/* 1177 */     } else if (paramObject instanceof char[]) {
/* 1178 */       char[] arrayOfChar = (char[])paramObject;
/* 1179 */       paramStringBuffer.append("new char[] {");
/* 1180 */       for (byte b = 0; b < arrayOfChar.length; b++) {
/* 1181 */         paramStringBuffer.append((b == 0) ? "" : ",").append("(char)")
/* 1182 */           .append(arrayOfChar[b]);
/*      */       }
/* 1184 */       paramStringBuffer.append('}');
/* 1185 */     } else if (paramObject instanceof int[]) {
/* 1186 */       int[] arrayOfInt = (int[])paramObject;
/* 1187 */       paramStringBuffer.append("new int[] {");
/* 1188 */       for (byte b = 0; b < arrayOfInt.length; b++) {
/* 1189 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfInt[b]);
/*      */       }
/* 1191 */       paramStringBuffer.append('}');
/* 1192 */     } else if (paramObject instanceof long[]) {
/* 1193 */       long[] arrayOfLong = (long[])paramObject;
/* 1194 */       paramStringBuffer.append("new long[] {");
/* 1195 */       for (byte b = 0; b < arrayOfLong.length; b++) {
/* 1196 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfLong[b]).append('L');
/*      */       }
/* 1198 */       paramStringBuffer.append('}');
/* 1199 */     } else if (paramObject instanceof float[]) {
/* 1200 */       float[] arrayOfFloat = (float[])paramObject;
/* 1201 */       paramStringBuffer.append("new float[] {");
/* 1202 */       for (byte b = 0; b < arrayOfFloat.length; b++) {
/* 1203 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfFloat[b]).append('f');
/*      */       }
/* 1205 */       paramStringBuffer.append('}');
/* 1206 */     } else if (paramObject instanceof double[]) {
/* 1207 */       double[] arrayOfDouble = (double[])paramObject;
/* 1208 */       paramStringBuffer.append("new double[] {");
/* 1209 */       for (byte b = 0; b < arrayOfDouble.length; b++) {
/* 1210 */         paramStringBuffer.append((b == 0) ? "" : ",").append(arrayOfDouble[b]).append('d');
/*      */       }
/* 1212 */       paramStringBuffer.append('}');
/*      */     } 
/*      */   }
/*      */   
/*      */   private void declareFrameTypes(int paramInt, Object[] paramArrayOfObject) {
/* 1217 */     for (byte b = 0; b < paramInt; b++) {
/* 1218 */       if (paramArrayOfObject[b] instanceof Label) {
/* 1219 */         declareLabel((Label)paramArrayOfObject[b]);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void appendFrameTypes(int paramInt, Object[] paramArrayOfObject) {
/* 1225 */     for (byte b = 0; b < paramInt; b++) {
/* 1226 */       if (b > 0) {
/* 1227 */         this.buf.append(", ");
/*      */       }
/* 1229 */       if (paramArrayOfObject[b] instanceof String) {
/* 1230 */         appendConstant(paramArrayOfObject[b]);
/* 1231 */       } else if (paramArrayOfObject[b] instanceof Integer) {
/* 1232 */         switch (((Integer)paramArrayOfObject[b]).intValue()) {
/*      */           case 0:
/* 1234 */             this.buf.append("Opcodes.TOP");
/*      */             break;
/*      */           case 1:
/* 1237 */             this.buf.append("Opcodes.INTEGER");
/*      */             break;
/*      */           case 2:
/* 1240 */             this.buf.append("Opcodes.FLOAT");
/*      */             break;
/*      */           case 3:
/* 1243 */             this.buf.append("Opcodes.DOUBLE");
/*      */             break;
/*      */           case 4:
/* 1246 */             this.buf.append("Opcodes.LONG");
/*      */             break;
/*      */           case 5:
/* 1249 */             this.buf.append("Opcodes.NULL");
/*      */             break;
/*      */           case 6:
/* 1252 */             this.buf.append("Opcodes.UNINITIALIZED_THIS");
/*      */             break;
/*      */         } 
/*      */       } else {
/* 1256 */         appendLabel((Label)paramArrayOfObject[b]);
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
/*      */   protected void declareLabel(Label paramLabel) {
/* 1270 */     if (this.labelNames == null) {
/* 1271 */       this.labelNames = new HashMap<Label, String>();
/*      */     }
/* 1273 */     String str = this.labelNames.get(paramLabel);
/* 1274 */     if (str == null) {
/* 1275 */       str = "l" + this.labelNames.size();
/* 1276 */       this.labelNames.put(paramLabel, str);
/* 1277 */       this.buf.append("Label ").append(str).append(" = new Label();\n");
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
/*      */   protected void appendLabel(Label paramLabel) {
/* 1290 */     this.buf.append(this.labelNames.get(paramLabel));
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\li\\util\ASMifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */