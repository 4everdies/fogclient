/*     */ package org.spongepowered.asm.lib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Item
/*     */ {
/*     */   int index;
/*     */   int type;
/*     */   int intVal;
/*     */   long longVal;
/*     */   String strVal1;
/*     */   String strVal2;
/*     */   String strVal3;
/*     */   int hashCode;
/*     */   Item next;
/*     */   
/*     */   Item() {}
/*     */   
/*     */   Item(int paramInt) {
/* 122 */     this.index = paramInt;
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
/*     */   Item(int paramInt, Item paramItem) {
/* 134 */     this.index = paramInt;
/* 135 */     this.type = paramItem.type;
/* 136 */     this.intVal = paramItem.intVal;
/* 137 */     this.longVal = paramItem.longVal;
/* 138 */     this.strVal1 = paramItem.strVal1;
/* 139 */     this.strVal2 = paramItem.strVal2;
/* 140 */     this.strVal3 = paramItem.strVal3;
/* 141 */     this.hashCode = paramItem.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(int paramInt) {
/* 151 */     this.type = 3;
/* 152 */     this.intVal = paramInt;
/* 153 */     this.hashCode = Integer.MAX_VALUE & this.type + paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(long paramLong) {
/* 163 */     this.type = 5;
/* 164 */     this.longVal = paramLong;
/* 165 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(float paramFloat) {
/* 175 */     this.type = 4;
/* 176 */     this.intVal = Float.floatToRawIntBits(paramFloat);
/* 177 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)paramFloat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(double paramDouble) {
/* 187 */     this.type = 6;
/* 188 */     this.longVal = Double.doubleToRawLongBits(paramDouble);
/* 189 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)paramDouble;
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
/*     */ 
/*     */   
/*     */   void set(int paramInt, String paramString1, String paramString2, String paramString3) {
/* 207 */     this.type = paramInt;
/* 208 */     this.strVal1 = paramString1;
/* 209 */     this.strVal2 = paramString2;
/* 210 */     this.strVal3 = paramString3;
/* 211 */     switch (paramInt) {
/*     */       case 7:
/* 213 */         this.intVal = 0;
/*     */       case 1:
/*     */       case 8:
/*     */       case 16:
/*     */       case 30:
/* 218 */         this.hashCode = Integer.MAX_VALUE & paramInt + paramString1.hashCode();
/*     */         return;
/*     */       case 12:
/* 221 */         this
/* 222 */           .hashCode = Integer.MAX_VALUE & paramInt + paramString1.hashCode() * paramString2.hashCode();
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     this
/* 231 */       .hashCode = Integer.MAX_VALUE & paramInt + paramString1.hashCode() * paramString2.hashCode() * paramString3.hashCode();
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
/*     */   void set(String paramString1, String paramString2, int paramInt) {
/* 246 */     this.type = 18;
/* 247 */     this.longVal = paramInt;
/* 248 */     this.strVal1 = paramString1;
/* 249 */     this.strVal2 = paramString2;
/* 250 */     this
/* 251 */       .hashCode = Integer.MAX_VALUE & 18 + paramInt * this.strVal1.hashCode() * this.strVal2.hashCode();
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
/*     */   void set(int paramInt1, int paramInt2) {
/* 265 */     this.type = 33;
/* 266 */     this.intVal = paramInt1;
/* 267 */     this.hashCode = paramInt2;
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
/*     */   boolean isEqualTo(Item paramItem) {
/* 281 */     switch (this.type) {
/*     */       case 1:
/*     */       case 7:
/*     */       case 8:
/*     */       case 16:
/*     */       case 30:
/* 287 */         return paramItem.strVal1.equals(this.strVal1);
/*     */       case 5:
/*     */       case 6:
/*     */       case 32:
/* 291 */         return (paramItem.longVal == this.longVal);
/*     */       case 3:
/*     */       case 4:
/* 294 */         return (paramItem.intVal == this.intVal);
/*     */       case 31:
/* 296 */         return (paramItem.intVal == this.intVal && paramItem.strVal1.equals(this.strVal1));
/*     */       case 12:
/* 298 */         return (paramItem.strVal1.equals(this.strVal1) && paramItem.strVal2.equals(this.strVal2));
/*     */       case 18:
/* 300 */         return (paramItem.longVal == this.longVal && paramItem.strVal1.equals(this.strVal1) && paramItem.strVal2
/* 301 */           .equals(this.strVal2));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     return (paramItem.strVal1.equals(this.strVal1) && paramItem.strVal2.equals(this.strVal2) && paramItem.strVal3
/* 309 */       .equals(this.strVal3));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\Item.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */