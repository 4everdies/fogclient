/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.AnnotationVisitor;
/*     */ import org.spongepowered.asm.lib.Attribute;
/*     */ import org.spongepowered.asm.lib.ClassVisitor;
/*     */ import org.spongepowered.asm.lib.FieldVisitor;
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
/*     */ public class FieldNode
/*     */   extends FieldVisitor
/*     */ {
/*     */   public int access;
/*     */   public String name;
/*     */   public String desc;
/*     */   public String signature;
/*     */   public Object value;
/*     */   public List<AnnotationNode> visibleAnnotations;
/*     */   public List<AnnotationNode> invisibleAnnotations;
/*     */   public List<TypeAnnotationNode> visibleTypeAnnotations;
/*     */   public List<TypeAnnotationNode> invisibleTypeAnnotations;
/*     */   public List<Attribute> attrs;
/*     */   
/*     */   public FieldNode(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 147 */     this(327680, paramInt, paramString1, paramString2, paramString3, paramObject);
/* 148 */     if (getClass() != FieldNode.class) {
/* 149 */       throw new IllegalStateException();
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
/*     */   public FieldNode(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 179 */     super(paramInt1);
/* 180 */     this.access = paramInt2;
/* 181 */     this.name = paramString1;
/* 182 */     this.desc = paramString2;
/* 183 */     this.signature = paramString3;
/* 184 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
/* 194 */     AnnotationNode annotationNode = new AnnotationNode(paramString);
/* 195 */     if (paramBoolean) {
/* 196 */       if (this.visibleAnnotations == null) {
/* 197 */         this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 199 */       this.visibleAnnotations.add(annotationNode);
/*     */     } else {
/* 201 */       if (this.invisibleAnnotations == null) {
/* 202 */         this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
/*     */       }
/* 204 */       this.invisibleAnnotations.add(annotationNode);
/*     */     } 
/* 206 */     return annotationNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int paramInt, TypePath paramTypePath, String paramString, boolean paramBoolean) {
/* 212 */     TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(paramInt, paramTypePath, paramString);
/* 213 */     if (paramBoolean) {
/* 214 */       if (this.visibleTypeAnnotations == null) {
/* 215 */         this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 217 */       this.visibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } else {
/* 219 */       if (this.invisibleTypeAnnotations == null) {
/* 220 */         this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
/*     */       }
/* 222 */       this.invisibleTypeAnnotations.add(typeAnnotationNode);
/*     */     } 
/* 224 */     return typeAnnotationNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute paramAttribute) {
/* 229 */     if (this.attrs == null) {
/* 230 */       this.attrs = new ArrayList<Attribute>(1);
/*     */     }
/* 232 */     this.attrs.add(paramAttribute);
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
/* 254 */     if (paramInt == 262144) {
/* 255 */       if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations
/* 256 */         .size() > 0) {
/* 257 */         throw new RuntimeException();
/*     */       }
/* 259 */       if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations
/* 260 */         .size() > 0) {
/* 261 */         throw new RuntimeException();
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
/*     */   public void accept(ClassVisitor paramClassVisitor) {
/* 273 */     FieldVisitor fieldVisitor = paramClassVisitor.visitField(this.access, this.name, this.desc, this.signature, this.value);
/* 274 */     if (fieldVisitor == null) {
/*     */       return;
/*     */     }
/*     */     
/* 278 */     byte b1 = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size(); byte b2;
/* 279 */     for (b2 = 0; b2 < b1; b2++) {
/* 280 */       AnnotationNode annotationNode = this.visibleAnnotations.get(b2);
/* 281 */       annotationNode.accept(fieldVisitor.visitAnnotation(annotationNode.desc, true));
/*     */     } 
/* 283 */     b1 = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size();
/* 284 */     for (b2 = 0; b2 < b1; b2++) {
/* 285 */       AnnotationNode annotationNode = this.invisibleAnnotations.get(b2);
/* 286 */       annotationNode.accept(fieldVisitor.visitAnnotation(annotationNode.desc, false));
/*     */     } 
/* 288 */     b1 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size();
/* 289 */     for (b2 = 0; b2 < b1; b2++) {
/* 290 */       TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(b2);
/* 291 */       typeAnnotationNode.accept(fieldVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
/*     */     } 
/*     */ 
/*     */     
/* 295 */     b1 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size();
/* 296 */     for (b2 = 0; b2 < b1; b2++) {
/* 297 */       TypeAnnotationNode typeAnnotationNode = this.invisibleTypeAnnotations.get(b2);
/* 298 */       typeAnnotationNode.accept(fieldVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, false));
/*     */     } 
/*     */     
/* 301 */     b1 = (this.attrs == null) ? 0 : this.attrs.size();
/* 302 */     for (b2 = 0; b2 < b1; b2++) {
/* 303 */       fieldVisitor.visitAttribute(this.attrs.get(b2));
/*     */     }
/* 305 */     fieldVisitor.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\FieldNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */