/*     */ package org.spongepowered.asm.obfuscation.mapping.common;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingMethod
/*     */   implements IMapping<MappingMethod>
/*     */ {
/*     */   private final String owner;
/*     */   private final String name;
/*     */   private final String desc;
/*     */   
/*     */   public MappingMethod(String paramString1, String paramString2) {
/*  42 */     this(getOwnerFromName(paramString1), getBaseName(paramString1), paramString2);
/*     */   }
/*     */   
/*     */   public MappingMethod(String paramString1, String paramString2, String paramString3) {
/*  46 */     this.owner = paramString1;
/*  47 */     this.name = paramString2;
/*  48 */     this.desc = paramString3;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMapping.Type getType() {
/*  53 */     return IMapping.Type.METHOD;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  58 */     if (this.name == null) {
/*  59 */       return null;
/*     */     }
/*  61 */     return ((this.owner != null) ? (this.owner + "/") : "") + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSimpleName() {
/*  66 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOwner() {
/*  71 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDesc() {
/*  76 */     return this.desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod getSuper() {
/*  81 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isConstructor() {
/*  85 */     return "<init>".equals(this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod move(String paramString) {
/*  90 */     return new MappingMethod(paramString, getSimpleName(), getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod remap(String paramString) {
/*  95 */     return new MappingMethod(getOwner(), paramString, getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod transform(String paramString) {
/* 100 */     return new MappingMethod(getOwner(), getSimpleName(), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingMethod copy() {
/* 105 */     return new MappingMethod(getOwner(), getSimpleName(), getDesc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMethod addPrefix(String paramString) {
/* 116 */     String str = getSimpleName();
/* 117 */     if (str == null || str.startsWith(paramString)) {
/* 118 */       return this;
/*     */     }
/* 120 */     return new MappingMethod(getOwner(), paramString + str, getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     return Objects.hashCode(new Object[] { getName(), getDesc() });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 130 */     if (this == paramObject) {
/* 131 */       return true;
/*     */     }
/* 133 */     if (paramObject instanceof MappingMethod) {
/* 134 */       return (Objects.equal(this.name, ((MappingMethod)paramObject).name) && Objects.equal(this.desc, ((MappingMethod)paramObject).desc));
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String serialise() {
/* 141 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     String str = getDesc();
/* 147 */     return String.format("%s%s%s", new Object[] { getName(), (str != null) ? " " : "", (str != null) ? str : "" });
/*     */   }
/*     */   
/*     */   private static String getBaseName(String paramString) {
/* 151 */     if (paramString == null) {
/* 152 */       return null;
/*     */     }
/* 154 */     int i = paramString.lastIndexOf('/');
/* 155 */     return (i > -1) ? paramString.substring(i + 1) : paramString;
/*     */   }
/*     */   
/*     */   private static String getOwnerFromName(String paramString) {
/* 159 */     if (paramString == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     int i = paramString.lastIndexOf('/');
/* 163 */     return (i > -1) ? paramString.substring(0, i) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\obfuscation\mapping\common\MappingMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */