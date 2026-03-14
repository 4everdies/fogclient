/*     */ package org.spongepowered.asm.obfuscation.mapping.common;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Strings;
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
/*     */ 
/*     */ public class MappingField
/*     */   implements IMapping<MappingField>
/*     */ {
/*     */   private final String owner;
/*     */   private final String name;
/*     */   private final String desc;
/*     */   
/*     */   public MappingField(String paramString1, String paramString2) {
/*  44 */     this(paramString1, paramString2, null);
/*     */   }
/*     */   
/*     */   public MappingField(String paramString1, String paramString2, String paramString3) {
/*  48 */     this.owner = paramString1;
/*  49 */     this.name = paramString2;
/*  50 */     this.desc = paramString3;
/*     */   }
/*     */ 
/*     */   
/*     */   public IMapping.Type getType() {
/*  55 */     return IMapping.Type.FIELD;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  60 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getSimpleName() {
/*  65 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getOwner() {
/*  70 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDesc() {
/*  75 */     return this.desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField getSuper() {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField move(String paramString) {
/*  85 */     return new MappingField(paramString, getName(), getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField remap(String paramString) {
/*  90 */     return new MappingField(getOwner(), paramString, getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField transform(String paramString) {
/*  95 */     return new MappingField(getOwner(), getName(), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingField copy() {
/* 100 */     return new MappingField(getOwner(), getName(), getDesc());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     return Objects.hashCode(new Object[] { toString() });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 110 */     if (this == paramObject) {
/* 111 */       return true;
/*     */     }
/* 113 */     if (paramObject instanceof MappingField) {
/* 114 */       return Objects.equal(toString(), ((MappingField)paramObject).toString());
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String serialise() {
/* 121 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return String.format("L%s;%s:%s", new Object[] { getOwner(), getName(), Strings.nullToEmpty(getDesc()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\obfuscation\mapping\common\MappingField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */