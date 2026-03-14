/*     */ package org.spongepowered.tools.obfuscation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*     */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*     */ import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Mappings
/*     */   implements IMappingConsumer
/*     */ {
/*     */   public static class MappingConflictException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final IMapping<?> oldMapping;
/*     */     private final IMapping<?> newMapping;
/*     */     
/*     */     public MappingConflictException(IMapping<?> param1IMapping1, IMapping<?> param1IMapping2) {
/*  51 */       this.oldMapping = param1IMapping1;
/*  52 */       this.newMapping = param1IMapping2;
/*     */     }
/*     */     
/*     */     public IMapping<?> getOld() {
/*  56 */       return this.oldMapping;
/*     */     }
/*     */     
/*     */     public IMapping<?> getNew() {
/*  60 */       return this.newMapping;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class UniqueMappings
/*     */     implements IMappingConsumer
/*     */   {
/*     */     private final IMappingConsumer mappings;
/*     */ 
/*     */     
/*  73 */     private final Map<ObfuscationType, Map<MappingField, MappingField>> fields = new HashMap<ObfuscationType, Map<MappingField, MappingField>>();
/*     */     
/*  75 */     private final Map<ObfuscationType, Map<MappingMethod, MappingMethod>> methods = new HashMap<ObfuscationType, Map<MappingMethod, MappingMethod>>();
/*     */ 
/*     */     
/*     */     public UniqueMappings(IMappingConsumer param1IMappingConsumer) {
/*  79 */       this.mappings = param1IMappingConsumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/*  84 */       clearMaps();
/*  85 */       this.mappings.clear();
/*     */     }
/*     */     
/*     */     protected void clearMaps() {
/*  89 */       this.fields.clear();
/*  90 */       this.methods.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void addFieldMapping(ObfuscationType param1ObfuscationType, MappingField param1MappingField1, MappingField param1MappingField2) {
/*  95 */       if (!checkForExistingMapping(param1ObfuscationType, param1MappingField1, param1MappingField2, this.fields)) {
/*  96 */         this.mappings.addFieldMapping(param1ObfuscationType, param1MappingField1, param1MappingField2);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addMethodMapping(ObfuscationType param1ObfuscationType, MappingMethod param1MappingMethod1, MappingMethod param1MappingMethod2) {
/* 102 */       if (!checkForExistingMapping(param1ObfuscationType, param1MappingMethod1, param1MappingMethod2, this.methods)) {
/* 103 */         this.mappings.addMethodMapping(param1ObfuscationType, param1MappingMethod1, param1MappingMethod2);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private <TMapping extends IMapping<TMapping>> boolean checkForExistingMapping(ObfuscationType param1ObfuscationType, TMapping param1TMapping1, TMapping param1TMapping2, Map<ObfuscationType, Map<TMapping, TMapping>> param1Map) throws Mappings.MappingConflictException {
/* 109 */       Map<Object, Object> map = (Map)param1Map.get(param1ObfuscationType);
/* 110 */       if (map == null) {
/* 111 */         map = new HashMap<Object, Object>();
/* 112 */         param1Map.put(param1ObfuscationType, map);
/*     */       } 
/* 114 */       IMapping iMapping = (IMapping)map.get(param1TMapping1);
/* 115 */       if (iMapping != null) {
/* 116 */         if (iMapping.equals(param1TMapping2)) {
/* 117 */           return true;
/*     */         }
/* 119 */         throw new Mappings.MappingConflictException(iMapping, param1TMapping2);
/*     */       } 
/* 121 */       map.put(param1TMapping1, param1TMapping2);
/* 122 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public IMappingConsumer.MappingSet<MappingField> getFieldMappings(ObfuscationType param1ObfuscationType) {
/* 127 */       return this.mappings.getFieldMappings(param1ObfuscationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public IMappingConsumer.MappingSet<MappingMethod> getMethodMappings(ObfuscationType param1ObfuscationType) {
/* 132 */       return this.mappings.getMethodMappings(param1ObfuscationType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private final Map<ObfuscationType, IMappingConsumer.MappingSet<MappingField>> fieldMappings = new HashMap<ObfuscationType, IMappingConsumer.MappingSet<MappingField>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   private final Map<ObfuscationType, IMappingConsumer.MappingSet<MappingMethod>> methodMappings = new HashMap<ObfuscationType, IMappingConsumer.MappingSet<MappingMethod>>();
/*     */   
/*     */   private UniqueMappings unique;
/*     */ 
/*     */   
/*     */   public Mappings() {
/* 152 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/* 156 */     for (ObfuscationType obfuscationType : ObfuscationType.types()) {
/* 157 */       this.fieldMappings.put(obfuscationType, new IMappingConsumer.MappingSet());
/* 158 */       this.methodMappings.put(obfuscationType, new IMappingConsumer.MappingSet());
/*     */     } 
/*     */   }
/*     */   
/*     */   public IMappingConsumer asUnique() {
/* 163 */     if (this.unique == null) {
/* 164 */       this.unique = new UniqueMappings(this);
/*     */     }
/* 166 */     return this.unique;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMappingConsumer.MappingSet<MappingField> getFieldMappings(ObfuscationType paramObfuscationType) {
/* 174 */     IMappingConsumer.MappingSet<MappingField> mappingSet = this.fieldMappings.get(paramObfuscationType);
/* 175 */     return (mappingSet != null) ? mappingSet : new IMappingConsumer.MappingSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMappingConsumer.MappingSet<MappingMethod> getMethodMappings(ObfuscationType paramObfuscationType) {
/* 183 */     IMappingConsumer.MappingSet<MappingMethod> mappingSet = this.methodMappings.get(paramObfuscationType);
/* 184 */     return (mappingSet != null) ? mappingSet : new IMappingConsumer.MappingSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 192 */     this.fieldMappings.clear();
/* 193 */     this.methodMappings.clear();
/* 194 */     if (this.unique != null) {
/* 195 */       this.unique.clearMaps();
/*     */     }
/* 197 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFieldMapping(ObfuscationType paramObfuscationType, MappingField paramMappingField1, MappingField paramMappingField2) {
/* 202 */     IMappingConsumer.MappingSet<MappingField> mappingSet = this.fieldMappings.get(paramObfuscationType);
/* 203 */     if (mappingSet == null) {
/* 204 */       mappingSet = new IMappingConsumer.MappingSet();
/* 205 */       this.fieldMappings.put(paramObfuscationType, mappingSet);
/*     */     } 
/* 207 */     mappingSet.add(new IMappingConsumer.MappingSet.Pair((IMapping)paramMappingField1, (IMapping)paramMappingField2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMethodMapping(ObfuscationType paramObfuscationType, MappingMethod paramMappingMethod1, MappingMethod paramMappingMethod2) {
/* 212 */     IMappingConsumer.MappingSet<MappingMethod> mappingSet = this.methodMappings.get(paramObfuscationType);
/* 213 */     if (mappingSet == null) {
/* 214 */       mappingSet = new IMappingConsumer.MappingSet();
/* 215 */       this.methodMappings.put(paramObfuscationType, mappingSet);
/*     */     } 
/* 217 */     mappingSet.add(new IMappingConsumer.MappingSet.Pair((IMapping)paramMappingMethod1, (IMapping)paramMappingMethod2));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\Mappings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */