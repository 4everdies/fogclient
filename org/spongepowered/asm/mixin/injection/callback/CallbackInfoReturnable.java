/*     */ package org.spongepowered.asm.mixin.injection.callback;
/*     */ 
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallbackInfoReturnable<R>
/*     */   extends CallbackInfo
/*     */ {
/*     */   private R returnValue;
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean) {
/*  42 */     super(paramString, paramBoolean);
/*  43 */     this.returnValue = null;
/*     */   }
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, R paramR) {
/*  47 */     super(paramString, paramBoolean);
/*  48 */     this.returnValue = paramR;
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, byte paramByte) {
/*  53 */     super(paramString, paramBoolean);
/*  54 */     this.returnValue = (R)Byte.valueOf(paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, char paramChar) {
/*  59 */     super(paramString, paramBoolean);
/*  60 */     this.returnValue = (R)Character.valueOf(paramChar);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, double paramDouble) {
/*  65 */     super(paramString, paramBoolean);
/*  66 */     this.returnValue = (R)Double.valueOf(paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, float paramFloat) {
/*  71 */     super(paramString, paramBoolean);
/*  72 */     this.returnValue = (R)Float.valueOf(paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, int paramInt) {
/*  77 */     super(paramString, paramBoolean);
/*  78 */     this.returnValue = (R)Integer.valueOf(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, long paramLong) {
/*  83 */     super(paramString, paramBoolean);
/*  84 */     this.returnValue = (R)Long.valueOf(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean, short paramShort) {
/*  89 */     super(paramString, paramBoolean);
/*  90 */     this.returnValue = (R)Short.valueOf(paramShort);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallbackInfoReturnable(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/*  95 */     super(paramString, paramBoolean1);
/*  96 */     this.returnValue = (R)Boolean.valueOf(paramBoolean2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturnValue(R paramR) throws CancellationException {
/* 106 */     cancel();
/*     */     
/* 108 */     this.returnValue = paramR;
/*     */   }
/*     */   
/*     */   public R getReturnValue() {
/* 112 */     return this.returnValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getReturnValueB() {
/* 117 */     return (this.returnValue == null) ? 0 : ((Byte)this.returnValue).byteValue(); }
/* 118 */   public char getReturnValueC() { return (this.returnValue == null) ? Character.MIN_VALUE : ((Character)this.returnValue).charValue(); }
/* 119 */   public double getReturnValueD() { return (this.returnValue == null) ? 0.0D : ((Double)this.returnValue).doubleValue(); }
/* 120 */   public float getReturnValueF() { return (this.returnValue == null) ? 0.0F : ((Float)this.returnValue).floatValue(); }
/* 121 */   public int getReturnValueI() { return (this.returnValue == null) ? 0 : ((Integer)this.returnValue).intValue(); }
/* 122 */   public long getReturnValueJ() { return (this.returnValue == null) ? 0L : ((Long)this.returnValue).longValue(); }
/* 123 */   public short getReturnValueS() { return (this.returnValue == null) ? 0 : ((Short)this.returnValue).shortValue(); } public boolean getReturnValueZ() {
/* 124 */     return (this.returnValue == null) ? false : ((Boolean)this.returnValue).booleanValue();
/*     */   }
/*     */   
/*     */   static String getReturnAccessor(Type paramType) {
/* 128 */     if (paramType.getSort() == 10 || paramType.getSort() == 9) {
/* 129 */       return "getReturnValue";
/*     */     }
/*     */     
/* 132 */     return String.format("getReturnValue%s", new Object[] { paramType.getDescriptor() });
/*     */   }
/*     */   
/*     */   static String getReturnDescriptor(Type paramType) {
/* 136 */     if (paramType.getSort() == 10 || paramType.getSort() == 9) {
/* 137 */       return String.format("()%s", new Object[] { "Ljava/lang/Object;" });
/*     */     }
/*     */     
/* 140 */     return String.format("()%s", new Object[] { paramType.getDescriptor() });
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\callback\CallbackInfoReturnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */