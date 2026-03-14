/*     */ package org.spongepowered.tools.obfuscation.struct;
/*     */ 
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.tools.Diagnostic;
/*     */ import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Message
/*     */ {
/*     */   private Diagnostic.Kind kind;
/*     */   private CharSequence msg;
/*     */   private final Element element;
/*     */   private final AnnotationMirror annotation;
/*     */   private final AnnotationValue value;
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence) {
/*  48 */     this(paramKind, paramCharSequence, (Element)null, (AnnotationMirror)null, (AnnotationValue)null);
/*     */   }
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement) {
/*  52 */     this(paramKind, paramCharSequence, paramElement, (AnnotationMirror)null, (AnnotationValue)null);
/*     */   }
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationHandle paramAnnotationHandle) {
/*  56 */     this(paramKind, paramCharSequence, paramElement, paramAnnotationHandle.asMirror(), (AnnotationValue)null);
/*     */   }
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror) {
/*  60 */     this(paramKind, paramCharSequence, paramElement, paramAnnotationMirror, (AnnotationValue)null);
/*     */   }
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationHandle paramAnnotationHandle, AnnotationValue paramAnnotationValue) {
/*  64 */     this(paramKind, paramCharSequence, paramElement, paramAnnotationHandle.asMirror(), paramAnnotationValue);
/*     */   }
/*     */   
/*     */   public Message(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/*  68 */     this.kind = paramKind;
/*  69 */     this.msg = paramCharSequence;
/*  70 */     this.element = paramElement;
/*  71 */     this.annotation = paramAnnotationMirror;
/*  72 */     this.value = paramAnnotationValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message sendTo(Messager paramMessager) {
/*  82 */     if (this.value != null) {
/*  83 */       paramMessager.printMessage(this.kind, this.msg, this.element, this.annotation, this.value);
/*  84 */     } else if (this.annotation != null) {
/*  85 */       paramMessager.printMessage(this.kind, this.msg, this.element, this.annotation);
/*  86 */     } else if (this.element != null) {
/*  87 */       paramMessager.printMessage(this.kind, this.msg, this.element);
/*     */     } else {
/*  89 */       paramMessager.printMessage(this.kind, this.msg);
/*     */     } 
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Diagnostic.Kind getKind() {
/*  98 */     return this.kind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message setKind(Diagnostic.Kind paramKind) {
/* 108 */     this.kind = paramKind;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence getMsg() {
/* 118 */     return this.msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message setMsg(CharSequence paramCharSequence) {
/* 128 */     this.msg = paramCharSequence;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element getElement() {
/* 136 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationMirror getAnnotation() {
/* 143 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationValue getValue() {
/* 150 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\struct\Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */