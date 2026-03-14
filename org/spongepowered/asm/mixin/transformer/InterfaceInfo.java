/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.Unique;
/*     */ import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
/*     */ import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InterfaceInfo
/*     */ {
/*     */   private final MixinInfo mixin;
/*     */   private final String prefix;
/*     */   private final Type iface;
/*     */   private final boolean unique;
/*     */   private Set<String> methods;
/*     */   
/*     */   private InterfaceInfo(MixinInfo paramMixinInfo, String paramString, Type paramType, boolean paramBoolean) {
/*  82 */     if (paramString == null || paramString.length() < 2 || !paramString.endsWith("$")) {
/*  83 */       throw new InvalidMixinException(paramMixinInfo, String.format("Prefix %s for iface %s is not valid", new Object[] { paramString, paramType.toString() }));
/*     */     }
/*     */     
/*  86 */     this.mixin = paramMixinInfo;
/*  87 */     this.prefix = paramString;
/*  88 */     this.iface = paramType;
/*  89 */     this.unique = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initMethods() {
/*  96 */     this.methods = new HashSet<String>();
/*  97 */     readInterface(this.iface.getInternalName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readInterface(String paramString) {
/* 107 */     ClassInfo classInfo = ClassInfo.forName(paramString);
/*     */     
/* 109 */     for (ClassInfo.Method method : classInfo.getMethods()) {
/* 110 */       this.methods.add(method.toString());
/*     */     }
/*     */     
/* 113 */     for (String str : classInfo.getInterfaces()) {
/* 114 */       readInterface(str);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 124 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getIface() {
/* 133 */     return this.iface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 142 */     return this.iface.getClassName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInternalName() {
/* 151 */     return this.iface.getInternalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnique() {
/* 160 */     return this.unique;
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
/*     */   public boolean renameMethod(MethodNode paramMethodNode) {
/* 173 */     if (this.methods == null) {
/* 174 */       initMethods();
/*     */     }
/*     */     
/* 177 */     if (!paramMethodNode.name.startsWith(this.prefix)) {
/* 178 */       if (this.methods.contains(paramMethodNode.name + paramMethodNode.desc)) {
/* 179 */         decorateUniqueMethod(paramMethodNode);
/*     */       }
/* 181 */       return false;
/*     */     } 
/*     */     
/* 184 */     String str1 = paramMethodNode.name.substring(this.prefix.length());
/* 185 */     String str2 = str1 + paramMethodNode.desc;
/*     */     
/* 187 */     if (!this.methods.contains(str2)) {
/* 188 */       throw new InvalidMixinException(this.mixin, String.format("%s does not exist in target interface %s", new Object[] { str1, getName() }));
/*     */     }
/*     */     
/* 191 */     if ((paramMethodNode.access & 0x1) == 0) {
/* 192 */       throw new InvalidMixinException(this.mixin, String.format("%s cannot implement %s because it is not visible", new Object[] { str1, getName() }));
/*     */     }
/*     */     
/* 195 */     Annotations.setVisible(paramMethodNode, MixinRenamed.class, new Object[] { "originalName", paramMethodNode.name, "isInterfaceMember", Boolean.valueOf(true) });
/* 196 */     decorateUniqueMethod(paramMethodNode);
/* 197 */     paramMethodNode.name = str1;
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decorateUniqueMethod(MethodNode paramMethodNode) {
/* 208 */     if (!this.unique) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     if (Annotations.getVisible(paramMethodNode, Unique.class) == null) {
/* 213 */       Annotations.setVisible(paramMethodNode, Unique.class, new Object[0]);
/* 214 */       this.mixin.getClassInfo().findMethod(paramMethodNode).setUnique(true);
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
/*     */   static InterfaceInfo fromAnnotation(MixinInfo paramMixinInfo, AnnotationNode paramAnnotationNode) {
/* 227 */     String str = (String)Annotations.getValue(paramAnnotationNode, "prefix");
/* 228 */     Type type = (Type)Annotations.getValue(paramAnnotationNode, "iface");
/* 229 */     Boolean bool = (Boolean)Annotations.getValue(paramAnnotationNode, "unique");
/*     */     
/* 231 */     if (str == null || type == null) {
/* 232 */       throw new InvalidMixinException(paramMixinInfo, String.format("@Interface annotation on %s is missing a required parameter", new Object[] { paramMixinInfo }));
/*     */     }
/*     */     
/* 235 */     return new InterfaceInfo(paramMixinInfo, str, type, (bool != null && bool.booleanValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 240 */     if (this == paramObject) {
/* 241 */       return true;
/*     */     }
/* 243 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 244 */       return false;
/*     */     }
/*     */     
/* 247 */     InterfaceInfo interfaceInfo = (InterfaceInfo)paramObject;
/*     */     
/* 249 */     return (this.mixin.equals(interfaceInfo.mixin) && this.prefix.equals(interfaceInfo.prefix) && this.iface.equals(interfaceInfo.iface));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 254 */     int i = this.mixin.hashCode();
/* 255 */     i = 31 * i + this.prefix.hashCode();
/* 256 */     i = 31 * i + this.iface.hashCode();
/* 257 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\InterfaceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */