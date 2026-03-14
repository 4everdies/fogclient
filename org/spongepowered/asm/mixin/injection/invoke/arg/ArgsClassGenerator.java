/*     */ package org.spongepowered.asm.mixin.injection.invoke.arg;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.ClassWriter;
/*     */ import org.spongepowered.asm.lib.Label;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.util.CheckClassAdapter;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
/*     */ import org.spongepowered.asm.util.asm.MethodVisitorEx;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ArgsClassGenerator
/*     */   implements IClassGenerator
/*     */ {
/*  57 */   public static final String ARGS_NAME = Args.class.getName();
/*  58 */   public static final String ARGS_REF = ARGS_NAME.replace('.', '/');
/*     */   
/*     */   public static final String GETTER_PREFIX = "$";
/*     */   
/*     */   private static final String CLASS_NAME_BASE = "org.spongepowered.asm.synthetic.args.Args$";
/*     */   
/*     */   private static final String OBJECT = "java/lang/Object";
/*     */   
/*     */   private static final String OBJECT_ARRAY = "[Ljava/lang/Object;";
/*     */   
/*     */   private static final String VALUES_FIELD = "values";
/*     */   
/*     */   private static final String CTOR_DESC = "([Ljava/lang/Object;)V";
/*     */   
/*     */   private static final String SET = "set";
/*     */   
/*     */   private static final String SET_DESC = "(ILjava/lang/Object;)V";
/*     */   
/*     */   private static final String SETALL = "setAll";
/*     */   
/*     */   private static final String SETALL_DESC = "([Ljava/lang/Object;)V";
/*     */   
/*     */   private static final String NPE = "java/lang/NullPointerException";
/*     */   
/*     */   private static final String NPE_CTOR_DESC = "(Ljava/lang/String;)V";
/*     */   
/*     */   private static final String AIOOBE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException";
/*     */   
/*     */   private static final String AIOOBE_CTOR_DESC = "(I)V";
/*     */   
/*     */   private static final String ACE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException";
/*     */   private static final String ACE_CTOR_DESC = "(IILjava/lang/String;)V";
/*  90 */   private int nextIndex = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private final BiMap<String, String> classNames = (BiMap<String, String>)HashBiMap.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   private final Map<String, byte[]> classBytes = (Map)new HashMap<String, byte>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName(String paramString) {
/* 115 */     String str1 = Bytecode.changeDescriptorReturnType(paramString, "V");
/* 116 */     String str2 = (String)this.classNames.get(str1);
/* 117 */     if (str2 == null) {
/* 118 */       str2 = String.format("%s%d", new Object[] { "org.spongepowered.asm.synthetic.args.Args$", Integer.valueOf(this.nextIndex++) });
/* 119 */       this.classNames.put(str1, str2);
/*     */     } 
/* 121 */     return str2;
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
/*     */   public String getClassRef(String paramString) {
/* 134 */     return getClassName(paramString).replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String paramString) {
/* 143 */     return getBytes(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes(String paramString) {
/* 154 */     byte[] arrayOfByte = this.classBytes.get(paramString);
/* 155 */     if (arrayOfByte == null) {
/* 156 */       String str = (String)this.classNames.inverse().get(paramString);
/* 157 */       if (str == null) {
/* 158 */         return null;
/*     */       }
/* 160 */       arrayOfByte = generateClass(paramString, str);
/* 161 */       this.classBytes.put(paramString, arrayOfByte);
/*     */     } 
/* 163 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] generateClass(String paramString1, String paramString2) {
/*     */     CheckClassAdapter checkClassAdapter;
/* 174 */     String str = paramString1.replace('.', '/');
/* 175 */     Type[] arrayOfType = Type.getArgumentTypes(paramString2);
/*     */     
/* 177 */     ClassWriter classWriter1 = new ClassWriter(2);
/* 178 */     ClassWriter classWriter2 = classWriter1;
/* 179 */     if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
/* 180 */       checkClassAdapter = new CheckClassAdapter((ClassVisitor)classWriter1);
/*     */     }
/*     */     
/* 183 */     checkClassAdapter.visit(50, 4129, str, null, ARGS_REF, null);
/* 184 */     checkClassAdapter.visitSource(paramString1.substring(paramString1.lastIndexOf('.') + 1) + ".java", null);
/*     */     
/* 186 */     generateCtor(str, paramString2, arrayOfType, (ClassVisitor)checkClassAdapter);
/* 187 */     generateToString(str, paramString2, arrayOfType, (ClassVisitor)checkClassAdapter);
/* 188 */     generateFactory(str, paramString2, arrayOfType, (ClassVisitor)checkClassAdapter);
/* 189 */     generateSetters(str, paramString2, arrayOfType, (ClassVisitor)checkClassAdapter);
/* 190 */     generateGetters(str, paramString2, arrayOfType, (ClassVisitor)checkClassAdapter);
/*     */     
/* 192 */     checkClassAdapter.visitEnd();
/*     */     
/* 194 */     return classWriter1.toByteArray();
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
/*     */   private void generateCtor(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 207 */     MethodVisitor methodVisitor = paramClassVisitor.visitMethod(2, "<init>", "([Ljava/lang/Object;)V", null, null);
/* 208 */     methodVisitor.visitCode();
/* 209 */     methodVisitor.visitVarInsn(25, 0);
/* 210 */     methodVisitor.visitVarInsn(25, 1);
/* 211 */     methodVisitor.visitMethodInsn(183, ARGS_REF, "<init>", "([Ljava/lang/Object;)V", false);
/* 212 */     methodVisitor.visitInsn(177);
/* 213 */     methodVisitor.visitMaxs(2, 2);
/* 214 */     methodVisitor.visitEnd();
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
/*     */   private void generateToString(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 226 */     MethodVisitor methodVisitor = paramClassVisitor.visitMethod(1, "toString", "()Ljava/lang/String;", null, null);
/* 227 */     methodVisitor.visitCode();
/* 228 */     methodVisitor.visitLdcInsn("Args" + getSignature(paramArrayOfType));
/* 229 */     methodVisitor.visitInsn(176);
/* 230 */     methodVisitor.visitMaxs(1, 1);
/* 231 */     methodVisitor.visitEnd();
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
/*     */   private void generateFactory(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 246 */     String str = Bytecode.changeDescriptorReturnType(paramString2, "L" + paramString1 + ";");
/* 247 */     MethodVisitorEx methodVisitorEx = new MethodVisitorEx(paramClassVisitor.visitMethod(9, "of", str, null, null));
/* 248 */     methodVisitorEx.visitCode();
/*     */ 
/*     */     
/* 251 */     methodVisitorEx.visitTypeInsn(187, paramString1);
/* 252 */     methodVisitorEx.visitInsn(89);
/*     */ 
/*     */     
/* 255 */     methodVisitorEx.visitConstant((byte)paramArrayOfType.length);
/* 256 */     methodVisitorEx.visitTypeInsn(189, "java/lang/Object");
/*     */ 
/*     */     
/* 259 */     byte b = 0;
/* 260 */     for (Type type : paramArrayOfType) {
/* 261 */       methodVisitorEx.visitInsn(89);
/* 262 */       methodVisitorEx.visitConstant(b);
/* 263 */       methodVisitorEx.visitVarInsn(type.getOpcode(21), b);
/* 264 */       box((MethodVisitor)methodVisitorEx, type);
/* 265 */       methodVisitorEx.visitInsn(83);
/* 266 */       b = (byte)(b + type.getSize());
/*     */     } 
/*     */ 
/*     */     
/* 270 */     methodVisitorEx.visitMethodInsn(183, paramString1, "<init>", "([Ljava/lang/Object;)V", false);
/*     */ 
/*     */     
/* 273 */     methodVisitorEx.visitInsn(176);
/*     */     
/* 275 */     methodVisitorEx.visitMaxs(6, Bytecode.getArgsSize(paramArrayOfType));
/* 276 */     methodVisitorEx.visitEnd();
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
/*     */   private void generateGetters(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 291 */     byte b = 0;
/* 292 */     for (Type type : paramArrayOfType) {
/* 293 */       String str1 = "$" + b;
/* 294 */       String str2 = "()" + type.getDescriptor();
/* 295 */       MethodVisitorEx methodVisitorEx = new MethodVisitorEx(paramClassVisitor.visitMethod(1, str1, str2, null, null));
/* 296 */       methodVisitorEx.visitCode();
/*     */ 
/*     */       
/* 299 */       methodVisitorEx.visitVarInsn(25, 0);
/* 300 */       methodVisitorEx.visitFieldInsn(180, paramString1, "values", "[Ljava/lang/Object;");
/* 301 */       methodVisitorEx.visitConstant(b);
/* 302 */       methodVisitorEx.visitInsn(50);
/*     */ 
/*     */       
/* 305 */       unbox((MethodVisitor)methodVisitorEx, type);
/*     */ 
/*     */       
/* 308 */       methodVisitorEx.visitInsn(type.getOpcode(172));
/*     */       
/* 310 */       methodVisitorEx.visitMaxs(2, 1);
/* 311 */       methodVisitorEx.visitEnd();
/* 312 */       b = (byte)(b + 1);
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
/*     */   private void generateSetters(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 326 */     generateIndexedSetter(paramString1, paramString2, paramArrayOfType, paramClassVisitor);
/* 327 */     generateMultiSetter(paramString1, paramString2, paramArrayOfType, paramClassVisitor);
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
/*     */   private void generateIndexedSetter(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 342 */     MethodVisitorEx methodVisitorEx = new MethodVisitorEx(paramClassVisitor.visitMethod(1, "set", "(ILjava/lang/Object;)V", null, null));
/*     */     
/* 344 */     methodVisitorEx.visitCode();
/*     */     
/* 346 */     Label label1 = new Label(), label2 = new Label();
/* 347 */     Label[] arrayOfLabel = new Label[paramArrayOfType.length]; byte b;
/* 348 */     for (b = 0; b < arrayOfLabel.length; b++) {
/* 349 */       arrayOfLabel[b] = new Label();
/*     */     }
/*     */ 
/*     */     
/* 353 */     methodVisitorEx.visitVarInsn(25, 0);
/* 354 */     methodVisitorEx.visitFieldInsn(180, paramString1, "values", "[Ljava/lang/Object;");
/*     */ 
/*     */     
/* 357 */     for (b = 0; b < paramArrayOfType.length; b = (byte)(b + 1)) {
/* 358 */       methodVisitorEx.visitVarInsn(21, 1);
/* 359 */       methodVisitorEx.visitConstant(b);
/* 360 */       methodVisitorEx.visitJumpInsn(159, arrayOfLabel[b]);
/*     */     } 
/*     */ 
/*     */     
/* 364 */     throwAIOOBE(methodVisitorEx, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     for (b = 0; b < paramArrayOfType.length; b++) {
/* 370 */       String str = Bytecode.getBoxingType(paramArrayOfType[b]);
/* 371 */       methodVisitorEx.visitLabel(arrayOfLabel[b]);
/* 372 */       methodVisitorEx.visitVarInsn(21, 1);
/* 373 */       methodVisitorEx.visitVarInsn(25, 2);
/* 374 */       methodVisitorEx.visitTypeInsn(192, (str != null) ? str : paramArrayOfType[b].getInternalName());
/* 375 */       methodVisitorEx.visitJumpInsn(167, (str != null) ? label2 : label1);
/*     */     } 
/*     */ 
/*     */     
/* 379 */     methodVisitorEx.visitLabel(label2);
/* 380 */     methodVisitorEx.visitInsn(89);
/* 381 */     methodVisitorEx.visitJumpInsn(199, label1);
/*     */ 
/*     */     
/* 384 */     throwNPE(methodVisitorEx, "Argument with primitive type cannot be set to NULL");
/*     */ 
/*     */     
/* 387 */     methodVisitorEx.visitLabel(label1);
/* 388 */     methodVisitorEx.visitInsn(83);
/* 389 */     methodVisitorEx.visitInsn(177);
/* 390 */     methodVisitorEx.visitMaxs(6, 3);
/* 391 */     methodVisitorEx.visitEnd();
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
/*     */   private void generateMultiSetter(String paramString1, String paramString2, Type[] paramArrayOfType, ClassVisitor paramClassVisitor) {
/* 405 */     MethodVisitorEx methodVisitorEx = new MethodVisitorEx(paramClassVisitor.visitMethod(1, "setAll", "([Ljava/lang/Object;)V", null, null));
/*     */     
/* 407 */     methodVisitorEx.visitCode();
/*     */     
/* 409 */     Label label1 = new Label(), label2 = new Label();
/* 410 */     byte b = 6;
/*     */ 
/*     */     
/* 413 */     methodVisitorEx.visitVarInsn(25, 1);
/* 414 */     methodVisitorEx.visitInsn(190);
/* 415 */     methodVisitorEx.visitInsn(89);
/* 416 */     methodVisitorEx.visitConstant((byte)paramArrayOfType.length);
/*     */ 
/*     */     
/* 419 */     methodVisitorEx.visitJumpInsn(159, label1);
/*     */     
/* 421 */     methodVisitorEx.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException");
/* 422 */     methodVisitorEx.visitInsn(89);
/* 423 */     methodVisitorEx.visitInsn(93);
/* 424 */     methodVisitorEx.visitInsn(88);
/* 425 */     methodVisitorEx.visitConstant((byte)paramArrayOfType.length);
/* 426 */     methodVisitorEx.visitLdcInsn(getSignature(paramArrayOfType));
/*     */     
/* 428 */     methodVisitorEx.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException", "<init>", "(IILjava/lang/String;)V", false);
/* 429 */     methodVisitorEx.visitInsn(191);
/*     */     
/* 431 */     methodVisitorEx.visitLabel(label1);
/* 432 */     methodVisitorEx.visitInsn(87);
/*     */ 
/*     */     
/* 435 */     methodVisitorEx.visitVarInsn(25, 0);
/* 436 */     methodVisitorEx.visitFieldInsn(180, paramString1, "values", "[Ljava/lang/Object;");
/*     */     byte b1;
/* 438 */     for (b1 = 0; b1 < paramArrayOfType.length; b1 = (byte)(b1 + 1)) {
/*     */       
/* 440 */       methodVisitorEx.visitInsn(89);
/* 441 */       methodVisitorEx.visitConstant(b1);
/*     */ 
/*     */       
/* 444 */       methodVisitorEx.visitVarInsn(25, 1);
/* 445 */       methodVisitorEx.visitConstant(b1);
/* 446 */       methodVisitorEx.visitInsn(50);
/*     */ 
/*     */       
/* 449 */       String str = Bytecode.getBoxingType(paramArrayOfType[b1]);
/* 450 */       methodVisitorEx.visitTypeInsn(192, (str != null) ? str : paramArrayOfType[b1].getInternalName());
/*     */ 
/*     */       
/* 453 */       if (str != null) {
/* 454 */         methodVisitorEx.visitInsn(89);
/* 455 */         methodVisitorEx.visitJumpInsn(198, label2);
/* 456 */         b = 7;
/*     */       } 
/*     */ 
/*     */       
/* 460 */       methodVisitorEx.visitInsn(83);
/*     */     } 
/*     */     
/* 463 */     methodVisitorEx.visitInsn(177);
/*     */     
/* 465 */     methodVisitorEx.visitLabel(label2);
/* 466 */     throwNPE(methodVisitorEx, "Argument with primitive type cannot be set to NULL");
/* 467 */     methodVisitorEx.visitInsn(177);
/*     */     
/* 469 */     methodVisitorEx.visitMaxs(b, 2);
/* 470 */     methodVisitorEx.visitEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwNPE(MethodVisitorEx paramMethodVisitorEx, String paramString) {
/* 477 */     paramMethodVisitorEx.visitTypeInsn(187, "java/lang/NullPointerException");
/* 478 */     paramMethodVisitorEx.visitInsn(89);
/* 479 */     paramMethodVisitorEx.visitLdcInsn(paramString);
/* 480 */     paramMethodVisitorEx.visitMethodInsn(183, "java/lang/NullPointerException", "<init>", "(Ljava/lang/String;)V", false);
/* 481 */     paramMethodVisitorEx.visitInsn(191);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwAIOOBE(MethodVisitorEx paramMethodVisitorEx, int paramInt) {
/* 489 */     paramMethodVisitorEx.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException");
/* 490 */     paramMethodVisitorEx.visitInsn(89);
/* 491 */     paramMethodVisitorEx.visitVarInsn(21, paramInt);
/* 492 */     paramMethodVisitorEx.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException", "<init>", "(I)V", false);
/* 493 */     paramMethodVisitorEx.visitInsn(191);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void box(MethodVisitor paramMethodVisitor, Type paramType) {
/* 504 */     String str = Bytecode.getBoxingType(paramType);
/* 505 */     if (str != null) {
/* 506 */       String str1 = String.format("(%s)L%s;", new Object[] { paramType.getDescriptor(), str });
/* 507 */       paramMethodVisitor.visitMethodInsn(184, str, "valueOf", str1, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void unbox(MethodVisitor paramMethodVisitor, Type paramType) {
/* 518 */     String str = Bytecode.getBoxingType(paramType);
/* 519 */     if (str != null) {
/* 520 */       String str1 = Bytecode.getUnboxingMethod(paramType);
/* 521 */       String str2 = "()" + paramType.getDescriptor();
/* 522 */       paramMethodVisitor.visitTypeInsn(192, str);
/* 523 */       paramMethodVisitor.visitMethodInsn(182, str, str1, str2, false);
/*     */     } else {
/* 525 */       paramMethodVisitor.visitTypeInsn(192, paramType.getInternalName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getSignature(Type[] paramArrayOfType) {
/* 530 */     return (new SignaturePrinter("", null, paramArrayOfType)).setFullyQualified(true).getFormattedArgs();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\invoke\arg\ArgsClassGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */