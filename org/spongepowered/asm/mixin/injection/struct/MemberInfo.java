/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Strings;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
/*     */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MemberInfo
/*     */ {
/*     */   public final String owner;
/*     */   public final String name;
/*     */   public final String desc;
/*     */   public final boolean matchAll;
/*     */   private final boolean forceField;
/*     */   private final String unparsed;
/*     */   
/*     */   public MemberInfo(String paramString, boolean paramBoolean) {
/* 123 */     this(paramString, null, null, paramBoolean);
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
/*     */   public MemberInfo(String paramString1, String paramString2, boolean paramBoolean) {
/* 136 */     this(paramString1, paramString2, null, paramBoolean);
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
/*     */   public MemberInfo(String paramString1, String paramString2, String paramString3) {
/* 148 */     this(paramString1, paramString2, paramString3, false);
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
/*     */   public MemberInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 161 */     this(paramString1, paramString2, paramString3, paramBoolean, null);
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
/*     */   public MemberInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4) {
/* 174 */     if (paramString2 != null && paramString2.contains(".")) {
/* 175 */       throw new IllegalArgumentException("Attempt to instance a MemberInfo with an invalid owner format");
/*     */     }
/*     */     
/* 178 */     this.owner = paramString2;
/* 179 */     this.name = paramString1;
/* 180 */     this.desc = paramString3;
/* 181 */     this.matchAll = paramBoolean;
/* 182 */     this.forceField = false;
/* 183 */     this.unparsed = paramString4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo(AbstractInsnNode paramAbstractInsnNode) {
/* 193 */     this.matchAll = false;
/* 194 */     this.forceField = false;
/* 195 */     this.unparsed = null;
/*     */     
/* 197 */     if (paramAbstractInsnNode instanceof MethodInsnNode) {
/* 198 */       MethodInsnNode methodInsnNode = (MethodInsnNode)paramAbstractInsnNode;
/* 199 */       this.owner = methodInsnNode.owner;
/* 200 */       this.name = methodInsnNode.name;
/* 201 */       this.desc = methodInsnNode.desc;
/* 202 */     } else if (paramAbstractInsnNode instanceof FieldInsnNode) {
/* 203 */       FieldInsnNode fieldInsnNode = (FieldInsnNode)paramAbstractInsnNode;
/* 204 */       this.owner = fieldInsnNode.owner;
/* 205 */       this.name = fieldInsnNode.name;
/* 206 */       this.desc = fieldInsnNode.desc;
/*     */     } else {
/* 208 */       throw new IllegalArgumentException("insn must be an instance of MethodInsnNode or FieldInsnNode");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo(IMapping<?> paramIMapping) {
/* 218 */     this.owner = paramIMapping.getOwner();
/* 219 */     this.name = paramIMapping.getSimpleName();
/* 220 */     this.desc = paramIMapping.getDesc();
/* 221 */     this.matchAll = false;
/* 222 */     this.forceField = (paramIMapping.getType() == IMapping.Type.FIELD);
/* 223 */     this.unparsed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberInfo(MemberInfo paramMemberInfo, MappingMethod paramMappingMethod, boolean paramBoolean) {
/* 232 */     this.owner = paramBoolean ? paramMappingMethod.getOwner() : paramMemberInfo.owner;
/* 233 */     this.name = paramMappingMethod.getSimpleName();
/* 234 */     this.desc = paramMappingMethod.getDesc();
/* 235 */     this.matchAll = paramMemberInfo.matchAll;
/* 236 */     this.forceField = false;
/* 237 */     this.unparsed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberInfo(MemberInfo paramMemberInfo, String paramString) {
/* 247 */     this.owner = paramString;
/* 248 */     this.name = paramMemberInfo.name;
/* 249 */     this.desc = paramMemberInfo.desc;
/* 250 */     this.matchAll = paramMemberInfo.matchAll;
/* 251 */     this.forceField = paramMemberInfo.forceField;
/* 252 */     this.unparsed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 260 */     String str1 = (this.owner != null) ? ("L" + this.owner + ";") : "";
/* 261 */     String str2 = (this.name != null) ? this.name : "";
/* 262 */     String str3 = this.matchAll ? "*" : "";
/* 263 */     String str4 = (this.desc != null) ? this.desc : "";
/* 264 */     String str5 = str4.startsWith("(") ? "" : ((this.desc != null) ? ":" : "");
/* 265 */     return str1 + str2 + str3 + str5 + str4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String toSrg() {
/* 276 */     if (!isFullyQualified()) {
/* 277 */       throw new MixinException("Cannot convert unqualified reference to SRG mapping");
/*     */     }
/*     */     
/* 280 */     if (this.desc.startsWith("(")) {
/* 281 */       return this.owner + "/" + this.name + " " + this.desc;
/*     */     }
/*     */     
/* 284 */     return this.owner + "/" + this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toDescriptor() {
/* 291 */     if (this.desc == null) {
/* 292 */       return "";
/*     */     }
/*     */     
/* 295 */     return (new SignaturePrinter(this)).setFullyQualified(true).toDescriptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toCtorType() {
/* 302 */     if (this.unparsed == null) {
/* 303 */       return null;
/*     */     }
/*     */     
/* 306 */     String str = getReturnType();
/* 307 */     if (str != null) {
/* 308 */       return str;
/*     */     }
/*     */     
/* 311 */     if (this.owner != null) {
/* 312 */       return this.owner;
/*     */     }
/*     */     
/* 315 */     if (this.name != null && this.desc == null) {
/* 316 */       return this.name;
/*     */     }
/*     */     
/* 319 */     return (this.desc != null) ? this.desc : this.unparsed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toCtorDesc() {
/* 327 */     if (this.desc != null && this.desc.startsWith("(") && this.desc.indexOf(')') > -1) {
/* 328 */       return this.desc.substring(0, this.desc.indexOf(')') + 1) + "V";
/*     */     }
/*     */     
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReturnType() {
/* 340 */     if (this.desc == null || this.desc.indexOf(')') == -1 || this.desc.indexOf('(') != 0) {
/* 341 */       return null;
/*     */     }
/*     */     
/* 344 */     String str = this.desc.substring(this.desc.indexOf(')') + 1);
/* 345 */     if (str.startsWith("L") && str.endsWith(";")) {
/* 346 */       return str.substring(1, str.length() - 1);
/*     */     }
/* 348 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMapping<?> asMapping() {
/* 356 */     return isField() ? (IMapping<?>)asFieldMapping() : (IMapping<?>)asMethodMapping();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod asMethodMapping() {
/* 363 */     if (!isFullyQualified()) {
/* 364 */       throw new MixinException("Cannot convert unqualified reference " + this + " to MethodMapping");
/*     */     }
/*     */     
/* 367 */     if (isField()) {
/* 368 */       throw new MixinException("Cannot convert a non-method reference " + this + " to MethodMapping");
/*     */     }
/*     */     
/* 371 */     return new MappingMethod(this.owner, this.name, this.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingField asFieldMapping() {
/* 378 */     if (!isField()) {
/* 379 */       throw new MixinException("Cannot convert non-field reference " + this + " to FieldMapping");
/*     */     }
/*     */     
/* 382 */     return new MappingField(this.owner, this.name, this.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullyQualified() {
/* 391 */     return (this.owner != null && this.name != null && this.desc != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isField() {
/* 401 */     return (this.forceField || (this.desc != null && !this.desc.startsWith("(")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 410 */     return "<init>".equals(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassInitialiser() {
/* 419 */     return "<clinit>".equals(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInitialiser() {
/* 429 */     return (isConstructor() || isClassInitialiser());
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
/*     */   public MemberInfo validate() throws InvalidMemberDescriptorException {
/* 442 */     if (this.owner != null) {
/* 443 */       if (!this.owner.matches("(?i)^[\\w\\p{Sc}/]+$")) {
/* 444 */         throw new InvalidMemberDescriptorException("Invalid owner: " + this.owner);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 449 */       if (this.unparsed != null && this.unparsed.lastIndexOf('.') > 0 && this.owner.startsWith("L")) {
/* 450 */         throw new InvalidMemberDescriptorException("Malformed owner: " + this.owner + " If you are seeing this message unexpectedly and the owner appears to be correct, replace the owner descriptor with formal type L" + this.owner + "; to suppress this error");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 456 */     if (this.name != null && !this.name.matches("(?i)^<?[\\w\\p{Sc}]+>?$")) {
/* 457 */       throw new InvalidMemberDescriptorException("Invalid name: " + this.name);
/*     */     }
/*     */     
/* 460 */     if (this.desc != null) {
/* 461 */       if (!this.desc.matches("^(\\([\\w\\p{Sc}\\[/;]*\\))?\\[*[\\w\\p{Sc}/;]+$")) {
/* 462 */         throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
/*     */       }
/* 464 */       if (isField()) {
/* 465 */         if (!this.desc.equals(Type.getType(this.desc).getDescriptor())) {
/* 466 */           throw new InvalidMemberDescriptorException("Invalid field type in descriptor: " + this.desc);
/*     */         }
/*     */       } else {
/*     */         try {
/* 470 */           Type.getArgumentTypes(this.desc);
/* 471 */         } catch (Exception exception) {
/* 472 */           throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
/*     */         } 
/*     */         
/* 475 */         String str = this.desc.substring(this.desc.indexOf(')') + 1);
/*     */         try {
/* 477 */           Type type = Type.getType(str);
/* 478 */           if (!str.equals(type.getDescriptor())) {
/* 479 */             throw new InvalidMemberDescriptorException("Invalid return type \"" + str + "\" in descriptor: " + this.desc);
/*     */           }
/* 481 */         } catch (Exception exception) {
/* 482 */           throw new InvalidMemberDescriptorException("Invalid return type \"" + str + "\" in descriptor: " + this.desc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 487 */     return this;
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
/*     */   public boolean matches(String paramString1, String paramString2, String paramString3) {
/* 501 */     return matches(paramString1, paramString2, paramString3, 0);
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
/*     */   public boolean matches(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 517 */     if (this.desc != null && paramString3 != null && !this.desc.equals(paramString3)) {
/* 518 */       return false;
/*     */     }
/* 520 */     if (this.name != null && paramString2 != null && !this.name.equals(paramString2)) {
/* 521 */       return false;
/*     */     }
/* 523 */     if (this.owner != null && paramString1 != null && !this.owner.equals(paramString1)) {
/* 524 */       return false;
/*     */     }
/* 526 */     return (paramInt == 0 || this.matchAll);
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
/*     */   public boolean matches(String paramString1, String paramString2) {
/* 539 */     return matches(paramString1, paramString2, 0);
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
/*     */   public boolean matches(String paramString1, String paramString2, int paramInt) {
/* 554 */     return ((this.name == null || this.name.equals(paramString1)) && (this.desc == null || (paramString2 != null && paramString2
/* 555 */       .equals(this.desc))) && (paramInt == 0 || this.matchAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 564 */     if (paramObject == null || paramObject.getClass() != MemberInfo.class) {
/* 565 */       return false;
/*     */     }
/*     */     
/* 568 */     MemberInfo memberInfo = (MemberInfo)paramObject;
/* 569 */     return (this.matchAll == memberInfo.matchAll && this.forceField == memberInfo.forceField && 
/* 570 */       Objects.equal(this.owner, memberInfo.owner) && 
/* 571 */       Objects.equal(this.name, memberInfo.name) && 
/* 572 */       Objects.equal(this.desc, memberInfo.desc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 580 */     return Objects.hashCode(new Object[] { Boolean.valueOf(this.matchAll), this.owner, this.name, this.desc });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo move(String paramString) {
/* 589 */     if ((paramString == null && this.owner == null) || (paramString != null && paramString.equals(this.owner))) {
/* 590 */       return this;
/*     */     }
/* 592 */     return new MemberInfo(this, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo transform(String paramString) {
/* 601 */     if ((paramString == null && this.desc == null) || (paramString != null && paramString.equals(this.desc))) {
/* 602 */       return this;
/*     */     }
/* 604 */     return new MemberInfo(this.name, this.owner, paramString, this.matchAll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo remapUsing(MappingMethod paramMappingMethod, boolean paramBoolean) {
/* 615 */     return new MemberInfo(this, paramMappingMethod, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemberInfo parseAndValidate(String paramString) throws InvalidMemberDescriptorException {
/* 625 */     return parse(paramString, null, null).validate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemberInfo parseAndValidate(String paramString, IMixinContext paramIMixinContext) throws InvalidMemberDescriptorException {
/* 636 */     return parse(paramString, paramIMixinContext.getReferenceMapper(), paramIMixinContext.getClassRef()).validate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemberInfo parse(String paramString) {
/* 646 */     return parse(paramString, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemberInfo parse(String paramString, IMixinContext paramIMixinContext) {
/* 657 */     return parse(paramString, paramIMixinContext.getReferenceMapper(), paramIMixinContext.getClassRef());
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
/*     */   private static MemberInfo parse(String paramString1, IReferenceMapper paramIReferenceMapper, String paramString2) {
/* 669 */     String str1 = null;
/* 670 */     String str2 = null;
/* 671 */     String str3 = Strings.nullToEmpty(paramString1).replaceAll("\\s", "");
/*     */     
/* 673 */     if (paramIReferenceMapper != null) {
/* 674 */       str3 = paramIReferenceMapper.remap(paramString2, str3);
/*     */     }
/*     */     
/* 677 */     int i = str3.lastIndexOf('.');
/* 678 */     int j = str3.indexOf(';');
/* 679 */     if (i > -1) {
/* 680 */       str2 = str3.substring(0, i).replace('.', '/');
/* 681 */       str3 = str3.substring(i + 1);
/* 682 */     } else if (j > -1 && str3.startsWith("L")) {
/* 683 */       str2 = str3.substring(1, j).replace('.', '/');
/* 684 */       str3 = str3.substring(j + 1);
/*     */     } 
/*     */     
/* 687 */     int k = str3.indexOf('(');
/* 688 */     int m = str3.indexOf(':');
/* 689 */     if (k > -1) {
/* 690 */       str1 = str3.substring(k);
/* 691 */       str3 = str3.substring(0, k);
/* 692 */     } else if (m > -1) {
/* 693 */       str1 = str3.substring(m + 1);
/* 694 */       str3 = str3.substring(0, m);
/*     */     } 
/*     */     
/* 697 */     if ((str3.indexOf('/') > -1 || str3.indexOf('.') > -1) && str2 == null) {
/* 698 */       str2 = str3;
/* 699 */       str3 = "";
/*     */     } 
/*     */     
/* 702 */     boolean bool = str3.endsWith("*");
/* 703 */     if (bool) {
/* 704 */       str3 = str3.substring(0, str3.length() - 1);
/*     */     }
/*     */     
/* 707 */     if (str3.isEmpty()) {
/* 708 */       str3 = null;
/*     */     }
/*     */     
/* 711 */     return new MemberInfo(str3, str2, str1, bool, paramString1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemberInfo fromMapping(IMapping<?> paramIMapping) {
/* 721 */     return new MemberInfo(paramIMapping);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\MemberInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */