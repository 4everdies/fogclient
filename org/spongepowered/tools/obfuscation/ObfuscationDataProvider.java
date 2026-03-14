/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
/*     */ import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
/*     */ import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObfuscationDataProvider
/*     */   implements IObfuscationDataProvider
/*     */ {
/*     */   private final IMixinAnnotationProcessor ap;
/*     */   private final List<ObfuscationEnvironment> environments;
/*     */   
/*     */   public ObfuscationDataProvider(IMixinAnnotationProcessor paramIMixinAnnotationProcessor, List<ObfuscationEnvironment> paramList) {
/*  55 */     this.ap = paramIMixinAnnotationProcessor;
/*  56 */     this.environments = paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ObfuscationData<T> getObfEntryRecursive(MemberInfo paramMemberInfo) {
/*  66 */     MemberInfo memberInfo = paramMemberInfo;
/*  67 */     ObfuscationData<String> obfuscationData = getObfClass(memberInfo.owner);
/*  68 */     ObfuscationData<?> obfuscationData1 = getObfEntry(memberInfo);
/*     */     try {
/*  70 */       while (obfuscationData1.isEmpty()) {
/*  71 */         TypeHandle typeHandle1 = this.ap.getTypeProvider().getTypeHandle(memberInfo.owner);
/*  72 */         if (typeHandle1 == null) {
/*  73 */           return (ObfuscationData)obfuscationData1;
/*     */         }
/*     */         
/*  76 */         TypeHandle typeHandle2 = typeHandle1.getSuperclass();
/*  77 */         obfuscationData1 = getObfEntryUsing(memberInfo, typeHandle2);
/*  78 */         if (!obfuscationData1.isEmpty()) {
/*  79 */           return applyParents(obfuscationData, (ObfuscationData)obfuscationData1);
/*     */         }
/*     */         
/*  82 */         for (TypeHandle typeHandle : typeHandle1.getInterfaces()) {
/*  83 */           obfuscationData1 = getObfEntryUsing(memberInfo, typeHandle);
/*  84 */           if (!obfuscationData1.isEmpty()) {
/*  85 */             return applyParents(obfuscationData, (ObfuscationData)obfuscationData1);
/*     */           }
/*     */         } 
/*     */         
/*  89 */         if (typeHandle2 == null) {
/*     */           break;
/*     */         }
/*     */         
/*  93 */         memberInfo = memberInfo.move(typeHandle2.getName());
/*     */       } 
/*  95 */     } catch (Exception exception) {
/*  96 */       exception.printStackTrace();
/*  97 */       return getObfEntry(paramMemberInfo);
/*     */     } 
/*  99 */     return (ObfuscationData)obfuscationData1;
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
/*     */   private <T> ObfuscationData<T> getObfEntryUsing(MemberInfo paramMemberInfo, TypeHandle paramTypeHandle) {
/* 113 */     return (paramTypeHandle == null) ? new ObfuscationData<T>() : getObfEntry(paramMemberInfo.move(paramTypeHandle.getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ObfuscationData<T> getObfEntry(MemberInfo paramMemberInfo) {
/* 124 */     if (paramMemberInfo.isField()) {
/* 125 */       return (ObfuscationData)getObfField(paramMemberInfo);
/*     */     }
/* 127 */     return (ObfuscationData)getObfMethod(paramMemberInfo.asMethodMapping());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ObfuscationData<T> getObfEntry(IMapping<T> paramIMapping) {
/* 133 */     if (paramIMapping != null) {
/* 134 */       if (paramIMapping.getType() == IMapping.Type.FIELD)
/* 135 */         return (ObfuscationData)getObfField((MappingField)paramIMapping); 
/* 136 */       if (paramIMapping.getType() == IMapping.Type.METHOD) {
/* 137 */         return (ObfuscationData)getObfMethod((MappingMethod)paramIMapping);
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return new ObfuscationData<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> getObfMethodRecursive(MemberInfo paramMemberInfo) {
/* 151 */     return getObfEntryRecursive(paramMemberInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> getObfMethod(MemberInfo paramMemberInfo) {
/* 161 */     return getRemappedMethod(paramMemberInfo, paramMemberInfo.isConstructor());
/*     */   }
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> getRemappedMethod(MemberInfo paramMemberInfo) {
/* 166 */     return getRemappedMethod(paramMemberInfo, true);
/*     */   }
/*     */   
/*     */   private ObfuscationData<MappingMethod> getRemappedMethod(MemberInfo paramMemberInfo, boolean paramBoolean) {
/* 170 */     ObfuscationData<MappingMethod> obfuscationData = new ObfuscationData();
/*     */     
/* 172 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 173 */       MappingMethod mappingMethod = obfuscationEnvironment.getObfMethod(paramMemberInfo);
/* 174 */       if (mappingMethod != null) {
/* 175 */         obfuscationData.put(obfuscationEnvironment.getType(), mappingMethod);
/*     */       }
/*     */     } 
/*     */     
/* 179 */     if (!obfuscationData.isEmpty() || !paramBoolean) {
/* 180 */       return obfuscationData;
/*     */     }
/*     */     
/* 183 */     return remapDescriptor(obfuscationData, paramMemberInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> getObfMethod(MappingMethod paramMappingMethod) {
/* 193 */     return getRemappedMethod(paramMappingMethod, paramMappingMethod.isConstructor());
/*     */   }
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> getRemappedMethod(MappingMethod paramMappingMethod) {
/* 198 */     return getRemappedMethod(paramMappingMethod, true);
/*     */   }
/*     */   
/*     */   private ObfuscationData<MappingMethod> getRemappedMethod(MappingMethod paramMappingMethod, boolean paramBoolean) {
/* 202 */     ObfuscationData<MappingMethod> obfuscationData = new ObfuscationData();
/*     */     
/* 204 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 205 */       MappingMethod mappingMethod = obfuscationEnvironment.getObfMethod(paramMappingMethod);
/* 206 */       if (mappingMethod != null) {
/* 207 */         obfuscationData.put(obfuscationEnvironment.getType(), mappingMethod);
/*     */       }
/*     */     } 
/*     */     
/* 211 */     if (!obfuscationData.isEmpty() || !paramBoolean) {
/* 212 */       return obfuscationData;
/*     */     }
/*     */     
/* 215 */     return remapDescriptor(obfuscationData, new MemberInfo((IMapping)paramMappingMethod));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingMethod> remapDescriptor(ObfuscationData<MappingMethod> paramObfuscationData, MemberInfo paramMemberInfo) {
/* 226 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 227 */       MemberInfo memberInfo = obfuscationEnvironment.remapDescriptor(paramMemberInfo);
/* 228 */       if (memberInfo != null) {
/* 229 */         paramObfuscationData.put(obfuscationEnvironment.getType(), memberInfo.asMethodMapping());
/*     */       }
/*     */     } 
/*     */     
/* 233 */     return paramObfuscationData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingField> getObfFieldRecursive(MemberInfo paramMemberInfo) {
/* 243 */     return getObfEntryRecursive(paramMemberInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingField> getObfField(MemberInfo paramMemberInfo) {
/* 252 */     return getObfField(paramMemberInfo.asFieldMapping());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<MappingField> getObfField(MappingField paramMappingField) {
/* 261 */     ObfuscationData<MappingField> obfuscationData = new ObfuscationData();
/*     */     
/* 263 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 264 */       MappingField mappingField = obfuscationEnvironment.getObfField(paramMappingField);
/* 265 */       if (mappingField != null) {
/* 266 */         if (mappingField.getDesc() == null && paramMappingField.getDesc() != null) {
/* 267 */           mappingField = mappingField.transform(obfuscationEnvironment.remapDescriptor(paramMappingField.getDesc()));
/*     */         }
/* 269 */         obfuscationData.put(obfuscationEnvironment.getType(), mappingField);
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     return obfuscationData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<String> getObfClass(TypeHandle paramTypeHandle) {
/* 282 */     return getObfClass(paramTypeHandle.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationData<String> getObfClass(String paramString) {
/* 291 */     ObfuscationData<String> obfuscationData = new ObfuscationData<String>(paramString);
/*     */     
/* 293 */     for (ObfuscationEnvironment obfuscationEnvironment : this.environments) {
/* 294 */       String str = obfuscationEnvironment.getObfClass(paramString);
/* 295 */       if (str != null) {
/* 296 */         obfuscationData.put(obfuscationEnvironment.getType(), str);
/*     */       }
/*     */     } 
/*     */     
/* 300 */     return obfuscationData;
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
/*     */   private static <T> ObfuscationData<T> applyParents(ObfuscationData<String> paramObfuscationData, ObfuscationData<T> paramObfuscationData1) {
/* 312 */     for (ObfuscationType obfuscationType : paramObfuscationData1) {
/* 313 */       String str = paramObfuscationData.get(obfuscationType);
/* 314 */       T t = paramObfuscationData1.get(obfuscationType);
/* 315 */       paramObfuscationData1.put(obfuscationType, (T)MemberInfo.fromMapping((IMapping)t).move(str).asMapping());
/*     */     } 
/* 317 */     return paramObfuscationData1;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\ObfuscationDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */