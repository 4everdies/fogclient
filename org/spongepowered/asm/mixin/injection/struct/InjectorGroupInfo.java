/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.injection.Group;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
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
/*     */ public class InjectorGroupInfo
/*     */ {
/*     */   private final String name;
/*     */   
/*     */   public static final class Map
/*     */     extends HashMap<String, InjectorGroupInfo>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*  52 */     private static final InjectorGroupInfo NO_GROUP = new InjectorGroupInfo("NONE", true);
/*     */ 
/*     */     
/*     */     public InjectorGroupInfo get(Object param1Object) {
/*  56 */       return forName(param1Object.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectorGroupInfo forName(String param1String) {
/*  67 */       InjectorGroupInfo injectorGroupInfo = super.get(param1String);
/*  68 */       if (injectorGroupInfo == null) {
/*  69 */         injectorGroupInfo = new InjectorGroupInfo(param1String);
/*  70 */         put(param1String, injectorGroupInfo);
/*     */       } 
/*  72 */       return injectorGroupInfo;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectorGroupInfo parseGroup(MethodNode param1MethodNode, String param1String) {
/*  84 */       return parseGroup(Annotations.getInvisible(param1MethodNode, Group.class), param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectorGroupInfo parseGroup(AnnotationNode param1AnnotationNode, String param1String) {
/*  96 */       if (param1AnnotationNode == null) {
/*  97 */         return NO_GROUP;
/*     */       }
/*     */       
/* 100 */       String str = (String)Annotations.getValue(param1AnnotationNode, "name");
/* 101 */       if (str == null || str.isEmpty()) {
/* 102 */         str = param1String;
/*     */       }
/* 104 */       InjectorGroupInfo injectorGroupInfo = forName(str);
/*     */       
/* 106 */       Integer integer1 = (Integer)Annotations.getValue(param1AnnotationNode, "min");
/* 107 */       if (integer1 != null && integer1.intValue() != -1) {
/* 108 */         injectorGroupInfo.setMinRequired(integer1.intValue());
/*     */       }
/*     */       
/* 111 */       Integer integer2 = (Integer)Annotations.getValue(param1AnnotationNode, "max");
/* 112 */       if (integer2 != null && integer2.intValue() != -1) {
/* 113 */         injectorGroupInfo.setMaxAllowed(integer2.intValue());
/*     */       }
/*     */       
/* 116 */       return injectorGroupInfo;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void validateAll() throws InjectionValidationException {
/* 125 */       for (InjectorGroupInfo injectorGroupInfo : values()) {
/* 126 */         injectorGroupInfo.validate();
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
/*     */ 
/*     */   
/* 140 */   private final List<InjectionInfo> members = new ArrayList<InjectionInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean isDefault;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   private int minCallbackCount = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   private int maxCallbackCount = Integer.MAX_VALUE;
/*     */   
/*     */   public InjectorGroupInfo(String paramString) {
/* 158 */     this(paramString, false);
/*     */   }
/*     */   
/*     */   InjectorGroupInfo(String paramString, boolean paramBoolean) {
/* 162 */     this.name = paramString;
/* 163 */     this.isDefault = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return String.format("@Group(name=%s, min=%d, max=%d)", new Object[] { getName(), Integer.valueOf(getMinRequired()), Integer.valueOf(getMaxAllowed()) });
/*     */   }
/*     */   
/*     */   public boolean isDefault() {
/* 172 */     return this.isDefault;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 176 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getMinRequired() {
/* 180 */     return Math.max(this.minCallbackCount, 1);
/*     */   }
/*     */   
/*     */   public int getMaxAllowed() {
/* 184 */     return Math.min(this.maxCallbackCount, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<InjectionInfo> getMembers() {
/* 193 */     return Collections.unmodifiableCollection(this.members);
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
/*     */   public void setMinRequired(int paramInt) {
/* 205 */     if (paramInt < 1) {
/* 206 */       throw new IllegalArgumentException("Cannot set zero or negative value for injector group min count. Attempted to set min=" + paramInt + " on " + this);
/*     */     }
/*     */     
/* 209 */     if (this.minCallbackCount > 0 && this.minCallbackCount != paramInt) {
/* 210 */       LogManager.getLogger("mixin").warn("Conflicting min value '{}' on @Group({}), previously specified {}", new Object[] { Integer.valueOf(paramInt), this.name, 
/* 211 */             Integer.valueOf(this.minCallbackCount) });
/*     */     }
/* 213 */     this.minCallbackCount = Math.max(this.minCallbackCount, paramInt);
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
/*     */   public void setMaxAllowed(int paramInt) {
/* 225 */     if (paramInt < 1) {
/* 226 */       throw new IllegalArgumentException("Cannot set zero or negative value for injector group max count. Attempted to set max=" + paramInt + " on " + this);
/*     */     }
/*     */     
/* 229 */     if (this.maxCallbackCount < Integer.MAX_VALUE && this.maxCallbackCount != paramInt) {
/* 230 */       LogManager.getLogger("mixin").warn("Conflicting max value '{}' on @Group({}), previously specified {}", new Object[] { Integer.valueOf(paramInt), this.name, 
/* 231 */             Integer.valueOf(this.maxCallbackCount) });
/*     */     }
/* 233 */     this.maxCallbackCount = Math.min(this.maxCallbackCount, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectorGroupInfo add(InjectionInfo paramInjectionInfo) {
/* 243 */     this.members.add(paramInjectionInfo);
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectorGroupInfo validate() throws InjectionValidationException {
/* 254 */     if (this.members.size() == 0)
/*     */     {
/* 256 */       return this;
/*     */     }
/*     */     
/* 259 */     int i = 0;
/* 260 */     for (InjectionInfo injectionInfo : this.members) {
/* 261 */       i += injectionInfo.getInjectedCallbackCount();
/*     */     }
/*     */     
/* 264 */     int j = getMinRequired();
/* 265 */     int k = getMaxAllowed();
/* 266 */     if (i < j)
/* 267 */       throw new InjectionValidationException(this, String.format("expected %d invocation(s) but only %d succeeded", new Object[] { Integer.valueOf(j), Integer.valueOf(i) })); 
/* 268 */     if (i > k) {
/* 269 */       throw new InjectionValidationException(this, String.format("maximum of %d invocation(s) allowed but %d succeeded", new Object[] { Integer.valueOf(k), Integer.valueOf(i) }));
/*     */     }
/*     */     
/* 272 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\InjectorGroupInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */