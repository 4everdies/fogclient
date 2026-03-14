/*     */ package org.spongepowered.asm.lib.tree.analysis;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicVerifier
/*     */   extends BasicInterpreter
/*     */ {
/*     */   public BasicVerifier() {
/*  50 */     super(327680);
/*     */   }
/*     */   
/*     */   protected BasicVerifier(int paramInt) {
/*  54 */     super(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue copyOperation(AbstractInsnNode paramAbstractInsnNode, BasicValue paramBasicValue) throws AnalyzerException {
/*     */     BasicValue basicValue;
/*  61 */     switch (paramAbstractInsnNode.getOpcode()) {
/*     */       case 21:
/*     */       case 54:
/*  64 */         basicValue = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 23:
/*     */       case 56:
/*  68 */         basicValue = BasicValue.FLOAT_VALUE;
/*     */         break;
/*     */       case 22:
/*     */       case 55:
/*  72 */         basicValue = BasicValue.LONG_VALUE;
/*     */         break;
/*     */       case 24:
/*     */       case 57:
/*  76 */         basicValue = BasicValue.DOUBLE_VALUE;
/*     */         break;
/*     */       case 25:
/*  79 */         if (!paramBasicValue.isReference()) {
/*  80 */           throw new AnalyzerException(paramAbstractInsnNode, null, "an object reference", paramBasicValue);
/*     */         }
/*     */         
/*  83 */         return paramBasicValue;
/*     */       case 58:
/*  85 */         if (!paramBasicValue.isReference() && 
/*  86 */           !BasicValue.RETURNADDRESS_VALUE.equals(paramBasicValue)) {
/*  87 */           throw new AnalyzerException(paramAbstractInsnNode, null, "an object reference or a return address", paramBasicValue);
/*     */         }
/*     */         
/*  90 */         return paramBasicValue;
/*     */       default:
/*  92 */         return paramBasicValue;
/*     */     } 
/*  94 */     if (!basicValue.equals(paramBasicValue)) {
/*  95 */       throw new AnalyzerException(paramAbstractInsnNode, null, basicValue, paramBasicValue);
/*     */     }
/*  97 */     return paramBasicValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue unaryOperation(AbstractInsnNode paramAbstractInsnNode, BasicValue paramBasicValue) throws AnalyzerException {
/*     */     BasicValue basicValue;
/* 104 */     switch (paramAbstractInsnNode.getOpcode()) {
/*     */       case 116:
/*     */       case 132:
/*     */       case 133:
/*     */       case 134:
/*     */       case 135:
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 170:
/*     */       case 171:
/*     */       case 172:
/*     */       case 188:
/*     */       case 189:
/* 124 */         basicValue = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 118:
/*     */       case 139:
/*     */       case 140:
/*     */       case 141:
/*     */       case 174:
/* 131 */         basicValue = BasicValue.FLOAT_VALUE;
/*     */         break;
/*     */       case 117:
/*     */       case 136:
/*     */       case 137:
/*     */       case 138:
/*     */       case 173:
/* 138 */         basicValue = BasicValue.LONG_VALUE;
/*     */         break;
/*     */       case 119:
/*     */       case 142:
/*     */       case 143:
/*     */       case 144:
/*     */       case 175:
/* 145 */         basicValue = BasicValue.DOUBLE_VALUE;
/*     */         break;
/*     */       case 180:
/* 148 */         basicValue = newValue(
/* 149 */             Type.getObjectType(((FieldInsnNode)paramAbstractInsnNode).owner));
/*     */         break;
/*     */       case 192:
/* 152 */         if (!paramBasicValue.isReference()) {
/* 153 */           throw new AnalyzerException(paramAbstractInsnNode, null, "an object reference", paramBasicValue);
/*     */         }
/*     */         
/* 156 */         return super.unaryOperation(paramAbstractInsnNode, paramBasicValue);
/*     */       case 190:
/* 158 */         if (!isArrayValue(paramBasicValue)) {
/* 159 */           throw new AnalyzerException(paramAbstractInsnNode, null, "an array reference", paramBasicValue);
/*     */         }
/*     */         
/* 162 */         return super.unaryOperation(paramAbstractInsnNode, paramBasicValue);
/*     */       case 176:
/*     */       case 191:
/*     */       case 193:
/*     */       case 194:
/*     */       case 195:
/*     */       case 198:
/*     */       case 199:
/* 170 */         if (!paramBasicValue.isReference()) {
/* 171 */           throw new AnalyzerException(paramAbstractInsnNode, null, "an object reference", paramBasicValue);
/*     */         }
/*     */         
/* 174 */         return super.unaryOperation(paramAbstractInsnNode, paramBasicValue);
/*     */       case 179:
/* 176 */         basicValue = newValue(Type.getType(((FieldInsnNode)paramAbstractInsnNode).desc));
/*     */         break;
/*     */       default:
/* 179 */         throw new Error("Internal error.");
/*     */     } 
/* 181 */     if (!isSubTypeOf(paramBasicValue, basicValue)) {
/* 182 */       throw new AnalyzerException(paramAbstractInsnNode, null, basicValue, paramBasicValue);
/*     */     }
/* 184 */     return super.unaryOperation(paramAbstractInsnNode, paramBasicValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue binaryOperation(AbstractInsnNode paramAbstractInsnNode, BasicValue paramBasicValue1, BasicValue paramBasicValue2) throws AnalyzerException {
/*     */     BasicValue basicValue1;
/*     */     BasicValue basicValue2;
/*     */     FieldInsnNode fieldInsnNode;
/* 193 */     switch (paramAbstractInsnNode.getOpcode()) {
/*     */       case 46:
/* 195 */         basicValue1 = newValue(Type.getType("[I"));
/* 196 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 51:
/* 199 */         if (isSubTypeOf(paramBasicValue1, newValue(Type.getType("[Z")))) {
/* 200 */           basicValue1 = newValue(Type.getType("[Z"));
/*     */         } else {
/* 202 */           basicValue1 = newValue(Type.getType("[B"));
/*     */         } 
/* 204 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 52:
/* 207 */         basicValue1 = newValue(Type.getType("[C"));
/* 208 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 53:
/* 211 */         basicValue1 = newValue(Type.getType("[S"));
/* 212 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 47:
/* 215 */         basicValue1 = newValue(Type.getType("[J"));
/* 216 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 48:
/* 219 */         basicValue1 = newValue(Type.getType("[F"));
/* 220 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 49:
/* 223 */         basicValue1 = newValue(Type.getType("[D"));
/* 224 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 50:
/* 227 */         basicValue1 = newValue(Type.getType("[Ljava/lang/Object;"));
/* 228 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 96:
/*     */       case 100:
/*     */       case 104:
/*     */       case 108:
/*     */       case 112:
/*     */       case 120:
/*     */       case 122:
/*     */       case 124:
/*     */       case 126:
/*     */       case 128:
/*     */       case 130:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/* 247 */         basicValue1 = BasicValue.INT_VALUE;
/* 248 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 98:
/*     */       case 102:
/*     */       case 106:
/*     */       case 110:
/*     */       case 114:
/*     */       case 149:
/*     */       case 150:
/* 257 */         basicValue1 = BasicValue.FLOAT_VALUE;
/* 258 */         basicValue2 = BasicValue.FLOAT_VALUE;
/*     */         break;
/*     */       case 97:
/*     */       case 101:
/*     */       case 105:
/*     */       case 109:
/*     */       case 113:
/*     */       case 127:
/*     */       case 129:
/*     */       case 131:
/*     */       case 148:
/* 269 */         basicValue1 = BasicValue.LONG_VALUE;
/* 270 */         basicValue2 = BasicValue.LONG_VALUE;
/*     */         break;
/*     */       case 121:
/*     */       case 123:
/*     */       case 125:
/* 275 */         basicValue1 = BasicValue.LONG_VALUE;
/* 276 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 99:
/*     */       case 103:
/*     */       case 107:
/*     */       case 111:
/*     */       case 115:
/*     */       case 151:
/*     */       case 152:
/* 285 */         basicValue1 = BasicValue.DOUBLE_VALUE;
/* 286 */         basicValue2 = BasicValue.DOUBLE_VALUE;
/*     */         break;
/*     */       case 165:
/*     */       case 166:
/* 290 */         basicValue1 = BasicValue.REFERENCE_VALUE;
/* 291 */         basicValue2 = BasicValue.REFERENCE_VALUE;
/*     */         break;
/*     */       case 181:
/* 294 */         fieldInsnNode = (FieldInsnNode)paramAbstractInsnNode;
/* 295 */         basicValue1 = newValue(Type.getObjectType(fieldInsnNode.owner));
/* 296 */         basicValue2 = newValue(Type.getType(fieldInsnNode.desc));
/*     */         break;
/*     */       default:
/* 299 */         throw new Error("Internal error.");
/*     */     } 
/* 301 */     if (!isSubTypeOf(paramBasicValue1, basicValue1)) {
/* 302 */       throw new AnalyzerException(paramAbstractInsnNode, "First argument", basicValue1, paramBasicValue1);
/*     */     }
/* 304 */     if (!isSubTypeOf(paramBasicValue2, basicValue2)) {
/* 305 */       throw new AnalyzerException(paramAbstractInsnNode, "Second argument", basicValue2, paramBasicValue2);
/*     */     }
/*     */     
/* 308 */     if (paramAbstractInsnNode.getOpcode() == 50) {
/* 309 */       return getElementValue(paramBasicValue1);
/*     */     }
/* 311 */     return super.binaryOperation(paramAbstractInsnNode, paramBasicValue1, paramBasicValue2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue ternaryOperation(AbstractInsnNode paramAbstractInsnNode, BasicValue paramBasicValue1, BasicValue paramBasicValue2, BasicValue paramBasicValue3) throws AnalyzerException {
/*     */     BasicValue basicValue1;
/*     */     BasicValue basicValue2;
/* 321 */     switch (paramAbstractInsnNode.getOpcode()) {
/*     */       case 79:
/* 323 */         basicValue1 = newValue(Type.getType("[I"));
/* 324 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 84:
/* 327 */         if (isSubTypeOf(paramBasicValue1, newValue(Type.getType("[Z")))) {
/* 328 */           basicValue1 = newValue(Type.getType("[Z"));
/*     */         } else {
/* 330 */           basicValue1 = newValue(Type.getType("[B"));
/*     */         } 
/* 332 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 85:
/* 335 */         basicValue1 = newValue(Type.getType("[C"));
/* 336 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 86:
/* 339 */         basicValue1 = newValue(Type.getType("[S"));
/* 340 */         basicValue2 = BasicValue.INT_VALUE;
/*     */         break;
/*     */       case 80:
/* 343 */         basicValue1 = newValue(Type.getType("[J"));
/* 344 */         basicValue2 = BasicValue.LONG_VALUE;
/*     */         break;
/*     */       case 81:
/* 347 */         basicValue1 = newValue(Type.getType("[F"));
/* 348 */         basicValue2 = BasicValue.FLOAT_VALUE;
/*     */         break;
/*     */       case 82:
/* 351 */         basicValue1 = newValue(Type.getType("[D"));
/* 352 */         basicValue2 = BasicValue.DOUBLE_VALUE;
/*     */         break;
/*     */       case 83:
/* 355 */         basicValue1 = paramBasicValue1;
/* 356 */         basicValue2 = BasicValue.REFERENCE_VALUE;
/*     */         break;
/*     */       default:
/* 359 */         throw new Error("Internal error.");
/*     */     } 
/* 361 */     if (!isSubTypeOf(paramBasicValue1, basicValue1)) {
/* 362 */       throw new AnalyzerException(paramAbstractInsnNode, "First argument", "a " + basicValue1 + " array reference", paramBasicValue1);
/*     */     }
/* 364 */     if (!BasicValue.INT_VALUE.equals(paramBasicValue2)) {
/* 365 */       throw new AnalyzerException(paramAbstractInsnNode, "Second argument", BasicValue.INT_VALUE, paramBasicValue2);
/*     */     }
/* 367 */     if (!isSubTypeOf(paramBasicValue3, basicValue2)) {
/* 368 */       throw new AnalyzerException(paramAbstractInsnNode, "Third argument", basicValue2, paramBasicValue3);
/*     */     }
/*     */     
/* 371 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicValue naryOperation(AbstractInsnNode paramAbstractInsnNode, List<? extends BasicValue> paramList) throws AnalyzerException {
/* 377 */     int i = paramAbstractInsnNode.getOpcode();
/* 378 */     if (i == 197) {
/* 379 */       for (byte b = 0; b < paramList.size(); b++) {
/* 380 */         if (!BasicValue.INT_VALUE.equals(paramList.get(b))) {
/* 381 */           throw new AnalyzerException(paramAbstractInsnNode, null, BasicValue.INT_VALUE, (Value)paramList
/* 382 */               .get(b));
/*     */         }
/*     */       } 
/*     */     } else {
/* 386 */       byte b1 = 0;
/* 387 */       byte b2 = 0;
/* 388 */       if (i != 184 && i != 186) {
/* 389 */         Type type = Type.getObjectType(((MethodInsnNode)paramAbstractInsnNode).owner);
/* 390 */         if (!isSubTypeOf(paramList.get(b1++), newValue(type))) {
/* 391 */           throw new AnalyzerException(paramAbstractInsnNode, "Method owner", 
/* 392 */               newValue(type), (Value)paramList.get(0));
/*     */         }
/*     */       } 
/* 395 */       String str = (i == 186) ? ((InvokeDynamicInsnNode)paramAbstractInsnNode).desc : ((MethodInsnNode)paramAbstractInsnNode).desc;
/*     */       
/* 397 */       Type[] arrayOfType = Type.getArgumentTypes(str);
/* 398 */       while (b1 < paramList.size()) {
/* 399 */         BasicValue basicValue1 = newValue(arrayOfType[b2++]);
/* 400 */         BasicValue basicValue2 = paramList.get(b1++);
/* 401 */         if (!isSubTypeOf(basicValue2, basicValue1)) {
/* 402 */           throw new AnalyzerException(paramAbstractInsnNode, "Argument " + b2, basicValue1, basicValue2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 407 */     return super.naryOperation(paramAbstractInsnNode, paramList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void returnOperation(AbstractInsnNode paramAbstractInsnNode, BasicValue paramBasicValue1, BasicValue paramBasicValue2) throws AnalyzerException {
/* 414 */     if (!isSubTypeOf(paramBasicValue1, paramBasicValue2)) {
/* 415 */       throw new AnalyzerException(paramAbstractInsnNode, "Incompatible return type", paramBasicValue2, paramBasicValue1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isArrayValue(BasicValue paramBasicValue) {
/* 421 */     return paramBasicValue.isReference();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicValue getElementValue(BasicValue paramBasicValue) throws AnalyzerException {
/* 426 */     return BasicValue.REFERENCE_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSubTypeOf(BasicValue paramBasicValue1, BasicValue paramBasicValue2) {
/* 431 */     return paramBasicValue1.equals(paramBasicValue2);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\analysis\BasicVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */