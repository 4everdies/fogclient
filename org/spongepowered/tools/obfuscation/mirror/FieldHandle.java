/*     */ package org.spongepowered.tools.obfuscation.mirror;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldHandle
/*     */   extends MemberHandle<MappingField>
/*     */ {
/*     */   private final VariableElement element;
/*     */   private final boolean rawType;
/*     */   
/*     */   public FieldHandle(TypeElement paramTypeElement, VariableElement paramVariableElement) {
/*  51 */     this(TypeUtils.getInternalName(paramTypeElement), paramVariableElement);
/*     */   }
/*     */   
/*     */   public FieldHandle(String paramString, VariableElement paramVariableElement) {
/*  55 */     this(paramString, paramVariableElement, false);
/*     */   }
/*     */   
/*     */   public FieldHandle(TypeElement paramTypeElement, VariableElement paramVariableElement, boolean paramBoolean) {
/*  59 */     this(TypeUtils.getInternalName(paramTypeElement), paramVariableElement, paramBoolean);
/*     */   }
/*     */   
/*     */   public FieldHandle(String paramString, VariableElement paramVariableElement, boolean paramBoolean) {
/*  63 */     this(paramString, paramVariableElement, paramBoolean, TypeUtils.getName(paramVariableElement), TypeUtils.getInternalName(paramVariableElement));
/*     */   }
/*     */   
/*     */   public FieldHandle(String paramString1, String paramString2, String paramString3) {
/*  67 */     this(paramString1, (VariableElement)null, false, paramString2, paramString3);
/*     */   }
/*     */   
/*     */   private FieldHandle(String paramString1, VariableElement paramVariableElement, boolean paramBoolean, String paramString2, String paramString3) {
/*  71 */     super(paramString1, paramString2, paramString3);
/*  72 */     this.element = paramVariableElement;
/*  73 */     this.rawType = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImaginary() {
/*  80 */     return (this.element == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VariableElement getElement() {
/*  87 */     return this.element;
/*     */   }
/*     */ 
/*     */   
/*     */   public Visibility getVisibility() {
/*  92 */     return TypeUtils.getVisibility(this.element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRawType() {
/* 100 */     return this.rawType;
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField asMapping(boolean paramBoolean) {
/* 105 */     return new MappingField(paramBoolean ? getOwner() : null, getName(), getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     String str1 = (getOwner() != null) ? ("L" + getOwner() + ";") : "";
/* 111 */     String str2 = Strings.nullToEmpty(getName());
/* 112 */     String str3 = Strings.nullToEmpty(getDesc());
/* 113 */     return String.format("%s%s:%s", new Object[] { str1, str2, str3 });
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\FieldHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */