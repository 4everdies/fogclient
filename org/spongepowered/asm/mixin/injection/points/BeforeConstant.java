/*     */ package org.spongepowered.asm.mixin.injection.points;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.google.common.primitives.Floats;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.mixin.injection.Constant;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AtCode("CONSTANT")
/*     */ public class BeforeConstant
/*     */   extends InjectionPoint
/*     */ {
/* 126 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */   
/*     */   private final int ordinal;
/*     */   
/*     */   private final boolean nullValue;
/*     */   
/*     */   private final Integer intValue;
/*     */   
/*     */   private final Float floatValue;
/*     */   
/*     */   private final Long longValue;
/*     */   
/*     */   private final Double doubleValue;
/*     */   
/*     */   private final String stringValue;
/*     */   
/*     */   private final Type typeValue;
/*     */   private final int[] expandOpcodes;
/*     */   private final boolean expand;
/*     */   private final String matchByType;
/*     */   private final boolean log;
/*     */   
/*     */   public BeforeConstant(IMixinContext paramIMixinContext, AnnotationNode paramAnnotationNode, String paramString) {
/* 149 */     super((String)Annotations.getValue(paramAnnotationNode, "slice", ""), InjectionPoint.Selector.DEFAULT, null);
/*     */     
/* 151 */     Boolean bool = (Boolean)Annotations.getValue(paramAnnotationNode, "nullValue", null);
/* 152 */     this.ordinal = ((Integer)Annotations.getValue(paramAnnotationNode, "ordinal", Integer.valueOf(-1))).intValue();
/* 153 */     this.nullValue = (bool != null && bool.booleanValue());
/* 154 */     this.intValue = (Integer)Annotations.getValue(paramAnnotationNode, "intValue", null);
/* 155 */     this.floatValue = (Float)Annotations.getValue(paramAnnotationNode, "floatValue", null);
/* 156 */     this.longValue = (Long)Annotations.getValue(paramAnnotationNode, "longValue", null);
/* 157 */     this.doubleValue = (Double)Annotations.getValue(paramAnnotationNode, "doubleValue", null);
/* 158 */     this.stringValue = (String)Annotations.getValue(paramAnnotationNode, "stringValue", null);
/* 159 */     this.typeValue = (Type)Annotations.getValue(paramAnnotationNode, "classValue", null);
/*     */     
/* 161 */     this.matchByType = validateDiscriminator(paramIMixinContext, paramString, bool, "on @Constant annotation");
/* 162 */     this.expandOpcodes = parseExpandOpcodes(Annotations.getValue(paramAnnotationNode, "expandZeroConditions", true, Constant.Condition.class));
/* 163 */     this.expand = (this.expandOpcodes.length > 0);
/*     */     
/* 165 */     this.log = ((Boolean)Annotations.getValue(paramAnnotationNode, "log", Boolean.FALSE)).booleanValue();
/*     */   }
/*     */   
/*     */   public BeforeConstant(InjectionPointData paramInjectionPointData) {
/* 169 */     super(paramInjectionPointData);
/*     */     
/* 171 */     String str1 = paramInjectionPointData.get("nullValue", null);
/* 172 */     Boolean bool = (str1 != null) ? Boolean.valueOf(Boolean.parseBoolean(str1)) : null;
/*     */     
/* 174 */     this.ordinal = paramInjectionPointData.getOrdinal();
/* 175 */     this.nullValue = (bool != null && bool.booleanValue());
/* 176 */     this.intValue = Ints.tryParse(paramInjectionPointData.get("intValue", ""));
/* 177 */     this.floatValue = Floats.tryParse(paramInjectionPointData.get("floatValue", ""));
/* 178 */     this.longValue = Longs.tryParse(paramInjectionPointData.get("longValue", ""));
/* 179 */     this.doubleValue = Doubles.tryParse(paramInjectionPointData.get("doubleValue", ""));
/* 180 */     this.stringValue = paramInjectionPointData.get("stringValue", null);
/* 181 */     String str2 = paramInjectionPointData.get("classValue", null);
/* 182 */     this.typeValue = (str2 != null) ? Type.getObjectType(str2.replace('.', '/')) : null;
/*     */     
/* 184 */     this.matchByType = validateDiscriminator(paramInjectionPointData.getContext(), "V", bool, "in @At(\"CONSTANT\") args");
/* 185 */     if ("V".equals(this.matchByType)) {
/* 186 */       throw new InvalidInjectionException(paramInjectionPointData.getContext(), "No constant discriminator could be parsed in @At(\"CONSTANT\") args");
/*     */     }
/*     */     
/* 189 */     ArrayList<Constant.Condition> arrayList = new ArrayList();
/* 190 */     String str3 = paramInjectionPointData.get("expandZeroConditions", "").toLowerCase();
/* 191 */     for (Constant.Condition condition : Constant.Condition.values()) {
/* 192 */       if (str3.contains(condition.name().toLowerCase())) {
/* 193 */         arrayList.add(condition);
/*     */       }
/*     */     } 
/*     */     
/* 197 */     this.expandOpcodes = parseExpandOpcodes(arrayList);
/* 198 */     this.expand = (this.expandOpcodes.length > 0);
/*     */     
/* 200 */     this.log = paramInjectionPointData.get("log", false);
/*     */   }
/*     */   
/*     */   private String validateDiscriminator(IMixinContext paramIMixinContext, String paramString1, Boolean paramBoolean, String paramString2) {
/* 204 */     int i = count(new Object[] { paramBoolean, this.intValue, this.floatValue, this.longValue, this.doubleValue, this.stringValue, this.typeValue });
/* 205 */     if (i == 1) {
/* 206 */       paramString1 = null;
/* 207 */     } else if (i > 1) {
/* 208 */       throw new InvalidInjectionException(paramIMixinContext, "Conflicting constant discriminators specified " + paramString2 + " for " + paramIMixinContext);
/*     */     } 
/* 210 */     return paramString1;
/*     */   }
/*     */   
/*     */   private int[] parseExpandOpcodes(List<Constant.Condition> paramList) {
/* 214 */     HashSet<Integer> hashSet = new HashSet();
/* 215 */     for (Constant.Condition condition1 : paramList) {
/* 216 */       Constant.Condition condition2 = condition1.getEquivalentCondition();
/* 217 */       for (int i : condition2.getOpcodes()) {
/* 218 */         hashSet.add(Integer.valueOf(i));
/*     */       }
/*     */     } 
/* 221 */     return Ints.toArray(hashSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection) {
/* 226 */     boolean bool = false;
/*     */     
/* 228 */     log("BeforeConstant is searching for constants in method with descriptor {}", new Object[] { paramString });
/*     */     
/* 230 */     ListIterator<AbstractInsnNode> listIterator = paramInsnList.iterator(); int i;
/* 231 */     for (byte b = 0; listIterator.hasNext(); ) {
/* 232 */       AbstractInsnNode abstractInsnNode = listIterator.next();
/*     */       
/* 234 */       boolean bool1 = this.expand ? matchesConditionalInsn(i, abstractInsnNode) : matchesConstantInsn(abstractInsnNode);
/* 235 */       if (bool1) {
/* 236 */         log("    BeforeConstant found a matching constant{} at ordinal {}", new Object[] { (this.matchByType != null) ? " TYPE" : " value", Integer.valueOf(b) });
/* 237 */         if (this.ordinal == -1 || this.ordinal == b) {
/* 238 */           log("      BeforeConstant found {}", new Object[] { Bytecode.describeNode(abstractInsnNode).trim() });
/* 239 */           paramCollection.add(abstractInsnNode);
/* 240 */           bool = true;
/*     */         } 
/* 242 */         b++;
/*     */       } 
/*     */       
/* 245 */       if (!(abstractInsnNode instanceof org.spongepowered.asm.lib.tree.LabelNode) && !(abstractInsnNode instanceof org.spongepowered.asm.lib.tree.FrameNode)) {
/* 246 */         i = abstractInsnNode.getOpcode();
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return bool;
/*     */   }
/*     */   
/*     */   private boolean matchesConditionalInsn(int paramInt, AbstractInsnNode paramAbstractInsnNode) {
/* 254 */     for (int i : this.expandOpcodes) {
/* 255 */       int j = paramAbstractInsnNode.getOpcode();
/* 256 */       if (j == i) {
/* 257 */         if (paramInt == 148 || paramInt == 149 || paramInt == 150 || paramInt == 151 || paramInt == 152) {
/* 258 */           log("  BeforeConstant is ignoring {} following {}", new Object[] { Bytecode.getOpcodeName(j), Bytecode.getOpcodeName(paramInt) });
/* 259 */           return false;
/*     */         } 
/*     */         
/* 262 */         log("  BeforeConstant found {} instruction", new Object[] { Bytecode.getOpcodeName(j) });
/* 263 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     if (this.intValue != null && this.intValue.intValue() == 0 && Bytecode.isConstant(paramAbstractInsnNode)) {
/* 268 */       Object object = Bytecode.getConstant(paramAbstractInsnNode);
/* 269 */       log("  BeforeConstant found INTEGER constant: value = {}", new Object[] { object });
/* 270 */       return (object instanceof Integer && ((Integer)object).intValue() == 0);
/*     */     } 
/*     */     
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matchesConstantInsn(AbstractInsnNode paramAbstractInsnNode) {
/* 277 */     if (!Bytecode.isConstant(paramAbstractInsnNode)) {
/* 278 */       return false;
/*     */     }
/*     */     
/* 281 */     Object object = Bytecode.getConstant(paramAbstractInsnNode);
/* 282 */     if (object == null) {
/* 283 */       log("  BeforeConstant found NULL constant: nullValue = {}", new Object[] { Boolean.valueOf(this.nullValue) });
/* 284 */       return (this.nullValue || "Ljava/lang/Object;".equals(this.matchByType));
/* 285 */     }  if (object instanceof Integer) {
/* 286 */       log("  BeforeConstant found INTEGER constant: value = {}, intValue = {}", new Object[] { object, this.intValue });
/* 287 */       return (object.equals(this.intValue) || "I".equals(this.matchByType));
/* 288 */     }  if (object instanceof Float) {
/* 289 */       log("  BeforeConstant found FLOAT constant: value = {}, floatValue = {}", new Object[] { object, this.floatValue });
/* 290 */       return (object.equals(this.floatValue) || "F".equals(this.matchByType));
/* 291 */     }  if (object instanceof Long) {
/* 292 */       log("  BeforeConstant found LONG constant: value = {}, longValue = {}", new Object[] { object, this.longValue });
/* 293 */       return (object.equals(this.longValue) || "J".equals(this.matchByType));
/* 294 */     }  if (object instanceof Double) {
/* 295 */       log("  BeforeConstant found DOUBLE constant: value = {}, doubleValue = {}", new Object[] { object, this.doubleValue });
/* 296 */       return (object.equals(this.doubleValue) || "D".equals(this.matchByType));
/* 297 */     }  if (object instanceof String) {
/* 298 */       log("  BeforeConstant found STRING constant: value = {}, stringValue = {}", new Object[] { object, this.stringValue });
/* 299 */       return (object.equals(this.stringValue) || "Ljava/lang/String;".equals(this.matchByType));
/* 300 */     }  if (object instanceof Type) {
/* 301 */       log("  BeforeConstant found CLASS constant: value = {}, typeValue = {}", new Object[] { object, this.typeValue });
/* 302 */       return (object.equals(this.typeValue) || "Ljava/lang/Class;".equals(this.matchByType));
/*     */     } 
/*     */     
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   protected void log(String paramString, Object... paramVarArgs) {
/* 309 */     if (this.log) {
/* 310 */       logger.info(paramString, paramVarArgs);
/*     */     }
/*     */   }
/*     */   
/*     */   private static int count(Object... paramVarArgs) {
/* 315 */     byte b = 0;
/* 316 */     for (Object object : paramVarArgs) {
/* 317 */       if (object != null) {
/* 318 */         b++;
/*     */       }
/*     */     } 
/* 321 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\points\BeforeConstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */