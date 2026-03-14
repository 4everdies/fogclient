/*    */ package org.spongepowered.tools.obfuscation.mirror;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import javax.lang.model.element.ExecutableElement;
/*    */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*    */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*    */ import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodHandle
/*    */   extends MemberHandle<MappingMethod>
/*    */ {
/*    */   private final ExecutableElement element;
/*    */   private final TypeHandle ownerHandle;
/*    */   
/*    */   public MethodHandle(TypeHandle paramTypeHandle, ExecutableElement paramExecutableElement) {
/* 50 */     this(paramTypeHandle, paramExecutableElement, TypeUtils.getName(paramExecutableElement), TypeUtils.getDescriptor(paramExecutableElement));
/*    */   }
/*    */   
/*    */   public MethodHandle(TypeHandle paramTypeHandle, String paramString1, String paramString2) {
/* 54 */     this(paramTypeHandle, (ExecutableElement)null, paramString1, paramString2);
/*    */   }
/*    */   
/*    */   private MethodHandle(TypeHandle paramTypeHandle, ExecutableElement paramExecutableElement, String paramString1, String paramString2) {
/* 58 */     super((paramTypeHandle != null) ? paramTypeHandle.getName() : null, paramString1, paramString2);
/* 59 */     this.element = paramExecutableElement;
/* 60 */     this.ownerHandle = paramTypeHandle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isImaginary() {
/* 67 */     return (this.element == null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExecutableElement getElement() {
/* 74 */     return this.element;
/*    */   }
/*    */ 
/*    */   
/*    */   public Visibility getVisibility() {
/* 79 */     return TypeUtils.getVisibility(this.element);
/*    */   }
/*    */ 
/*    */   
/*    */   public MappingMethod asMapping(boolean paramBoolean) {
/* 84 */     if (paramBoolean) {
/* 85 */       if (this.ownerHandle != null) {
/* 86 */         return (MappingMethod)new ResolvableMappingMethod(this.ownerHandle, getName(), getDesc());
/*    */       }
/* 88 */       return new MappingMethod(getOwner(), getName(), getDesc());
/*    */     } 
/* 90 */     return new MappingMethod(null, getName(), getDesc());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     String str1 = (getOwner() != null) ? ("L" + getOwner() + ";") : "";
/* 96 */     String str2 = Strings.nullToEmpty(getName());
/* 97 */     String str3 = Strings.nullToEmpty(getDesc());
/* 98 */     return String.format("%s%s%s", new Object[] { str1, str2, str3 });
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\MethodHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */