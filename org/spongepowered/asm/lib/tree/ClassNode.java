/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.FieldVisitor;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
/*     */ import org.spongepowered.asm.lib.TypePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassNode
/*     */   extends ClassVisitor
/*     */ {
/*     */   public int version;
/*     */   public int access;
/*     */   public String name;
/*     */   public String signature;
/*     */   public String superName;
/*     */   public List<String> interfaces;
/*     */   public String sourceFile;
/*     */   public String sourceDebug;
/*     */   public String outerClass;
/*     */   public String outerMethod;
/*     */   public String outerMethodDesc;
/*     */   public List<AnnotationNode> visibleAnnotations;
/*     */   public List<AnnotationNode> invisibleAnnotations;
/*     */   public List<TypeAnnotationNode> visibleTypeAnnotations;
/*     */   public List<TypeAnnotationNode> invisibleTypeAnnotations;
/*     */   public List<Attribute> attrs;
/*     */   public List<InnerClassNode> innerClasses;
/*     */   public List<FieldNode> fields;
/*     */   public List<MethodNode> methods;
/*     */   
/*     */   public ClassNode() {
/* 195 */     this(327680);
/* 196 */     if (getClass() != ClassNode.class) {
/* 197 */       throw new IllegalStateException();
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
/*     */   public ClassNode(int paramInt) {
/* 209 */     super(paramInt);
/* 210 */     this.interfaces = new ArrayList<String>();
/* 211 */     this.innerClasses = new ArrayList<InnerClassNode>();
/* 212 */     this.fields = new ArrayList<FieldNode>();
/* 213 */     this.methods = new ArrayList<MethodNode>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 224 */     this.version = paramInt1;
/* 225 */     this.access = paramInt2;
/* 226 */     this.name = paramString1;
/* 227 */     this.signature = paramString2;
/* 228 */     this.superName = paramString3;
/* 229 */     if (paramArrayOfString != null) {
/* 230 */       this.interfaces.addAll(Arrays.asList(paramArrayOfString));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitSource(String paramString1, String paramString2) {
/* 236 */     this.sourceFile = paramString1;
/* 237 */     this.sourceDebug = paramString2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
/* 243 */     this.outerClass = paramString1;
/* 244 */     this.outerMethod = paramString2;
/* 245 */     this.outerMethodDesc = paramString3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 251 */     AnnotationNode annotationNode = new AnnotationNode(paramString);
/* 252 */     if (paramBoolean) {
/* 253 */       if (this.visibleAnnotations == null) {
/* 254 */         this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 256 */       this.visibleAnnotations.add(annotationNode);
/*     */     } else {
/* 258 */       if (this.invisibleAnnotations == null) {
/* 259 */         this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 261 */       this.invisibleAnnotations.add(annotationNode);
/*     */     } 
/* 263 */     return annotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 269 */     TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(paramInt, paramTypePath, paramString);
/* 270 */     if (paramBoolean) {
/* 271 */       if (this.visibleTypeAnnotations == null) {
/* 272 */         this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 274 */       this.visibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } else {
/* 276 */       if (this.invisibleTypeAnnotations == null) {
/* 277 */         this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 279 */       this.invisibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } 
/* 281 */     return typeAnnotationNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 286 */     if (this.attrs == null) {
/* 287 */       this.attrs = new ArrayList<Attribute>(1);
/*     */     }
/* 289 */     this.attrs.add(paramAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 295 */     InnerClassNode innerClassNode = new InnerClassNode(paramString1, paramString2, paramString3, paramInt);
/*     */     
/* 297 */     this.innerClasses.add(innerClassNode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 303 */     FieldNode fieldNode = new FieldNode(paramInt, paramString1, paramString2, paramString3, paramObject);
/* 304 */     this.fields.add(fieldNode);
/* 305 */     return fieldNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/* 311 */     MethodNode methodNode = new MethodNode(paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
/*     */     
/* 313 */     this.methods.add(methodNode);
/* 314 */     return methodNode;
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
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(int paramInt) {
/* 336 */     if (paramInt == 262144) {
/* 337 */       if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations
/* 338 */         .size() > 0) {
/* 339 */         throw new RuntimeException();
/*     */       }
/* 341 */       if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations
/* 342 */         .size() > 0) {
/* 343 */         throw new RuntimeException();
/*     */       }
/* 345 */       for (FieldNode fieldNode : this.fields) {
/* 346 */         fieldNode.check(paramInt);
/*     */       }
/* 348 */       for (MethodNode methodNode : this.methods) {
/* 349 */         methodNode.check(paramInt);
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
/*     */   public void accept(ClassVisitor paramClassVisitor) {
/* 362 */     String[] arrayOfString = new String[this.interfaces.size()];
/* 363 */     this.interfaces.toArray(arrayOfString);
/* 364 */     paramClassVisitor.visit(this.version, this.access, this.name, this.signature, this.superName, arrayOfString);
/*     */     
/* 366 */     if (this.sourceFile != null || this.sourceDebug != null) {
/* 367 */       paramClassVisitor.visitSource(this.sourceFile, this.sourceDebug);
/*     */     }
/*     */     
/* 370 */     if (this.outerClass != null) {
/* 371 */       paramClassVisitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
/*     */     }
/*     */ 
/*     */     
/* 375 */     byte b1 = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size(); byte b2;
/* 376 */     for (b2 = 0; b2 < b1; b2++) {
/* 377 */       AnnotationNode annotationNode = this.visibleAnnotations.get(b2);
/* 378 */       annotationNode.accept(paramClassVisitor.visitAnnotation(annotationNode.desc, true));
/*     */     } 
/* 380 */     b1 = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size();
/* 381 */     for (b2 = 0; b2 < b1; b2++) {
/* 382 */       AnnotationNode annotationNode = this.invisibleAnnotations.get(b2);
/* 383 */       annotationNode.accept(paramClassVisitor.visitAnnotation(annotationNode.desc, false));
/*     */     } 
/* 385 */     b1 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size();
/* 386 */     for (b2 = 0; b2 < b1; b2++) {
/* 387 */       TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(b2);
/* 388 */       typeAnnotationNode.accept(paramClassVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
/*     */     } 
/*     */ 
/*     */     
/* 392 */     b1 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size();
/* 393 */     for (b2 = 0; b2 < b1; b2++) {
/* 394 */       TypeAnnotationNode typeAnnotationNode = this.invisibleTypeAnnotations.get(b2);
/* 395 */       typeAnnotationNode.accept(paramClassVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, false));
/*     */     } 
/*     */     
/* 398 */     b1 = (this.attrs == null) ? 0 : this.attrs.size();
/* 399 */     for (b2 = 0; b2 < b1; b2++) {
/* 400 */       paramClassVisitor.visitAttribute(this.attrs.get(b2));
/*     */     }
/*     */     
/* 403 */     for (b2 = 0; b2 < this.innerClasses.size(); b2++) {
/* 404 */       ((InnerClassNode)this.innerClasses.get(b2)).accept(paramClassVisitor);
/*     */     }
/*     */     
/* 407 */     for (b2 = 0; b2 < this.fields.size(); b2++) {
/* 408 */       ((FieldNode)this.fields.get(b2)).accept(paramClassVisitor);
/*     */     }
/*     */     
/* 411 */     for (b2 = 0; b2 < this.methods.size(); b2++) {
/* 412 */       ((MethodNode)this.methods.get(b2)).accept(paramClassVisitor);
/*     */     }
/*     */     
/* 415 */     paramClassVisitor.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\ClassNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */