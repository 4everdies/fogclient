/*     */ package org.spongepowered.asm.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class VersionNumber
/*     */   implements Serializable, Comparable<VersionNumber>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  44 */   public static final VersionNumber NONE = new VersionNumber();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final Pattern PATTERN = Pattern.compile("^(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5}))?)?)?(-[a-zA-Z0-9_\\-]+)?$");
/*     */ 
/*     */ 
/*     */   
/*     */   private final long value;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String suffix;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VersionNumber() {
/*  64 */     this.value = 0L;
/*  65 */     this.suffix = "";
/*     */   }
/*     */   
/*     */   private VersionNumber(short[] paramArrayOfshort) {
/*  69 */     this(paramArrayOfshort, null);
/*     */   }
/*     */   
/*     */   private VersionNumber(short[] paramArrayOfshort, String paramString) {
/*  73 */     this.value = pack(paramArrayOfshort);
/*  74 */     this.suffix = (paramString != null) ? paramString : "";
/*     */   }
/*     */   
/*     */   private VersionNumber(short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
/*  78 */     this(paramShort1, paramShort2, paramShort3, paramShort4, null);
/*     */   }
/*     */   
/*     */   private VersionNumber(short paramShort1, short paramShort2, short paramShort3, short paramShort4, String paramString) {
/*  82 */     this.value = pack(new short[] { paramShort1, paramShort2, paramShort3, paramShort4 });
/*  83 */     this.suffix = (paramString != null) ? paramString : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     short[] arrayOfShort = unpack(this.value);
/*     */     
/*  93 */     return String.format("%d.%d%3$s%4$s%5$s", new Object[] {
/*  94 */           Short.valueOf(arrayOfShort[0]), 
/*  95 */           Short.valueOf(arrayOfShort[1]), ((this.value & 0x7FFFFFFFL) > 0L) ? 
/*  96 */           String.format(".%d", new Object[] { Short.valueOf(arrayOfShort[2]) }) : "", ((this.value & 0x7FFFL) > 0L) ? 
/*  97 */           String.format(".%d", new Object[] { Short.valueOf(arrayOfShort[3]) }) : "", this.suffix
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(VersionNumber paramVersionNumber) {
/* 106 */     if (paramVersionNumber == null) {
/* 107 */       return 1;
/*     */     }
/* 109 */     long l = this.value - paramVersionNumber.value;
/* 110 */     return (l > 0L) ? 1 : ((l < 0L) ? -1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 118 */     if (!(paramObject instanceof VersionNumber)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     return (((VersionNumber)paramObject).value == this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     return (int)(this.value >> 32L) ^ (int)(this.value & 0xFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long pack(short... paramVarArgs) {
/* 140 */     return paramVarArgs[0] << 48L | paramVarArgs[1] << 32L | (paramVarArgs[2] << 16) | paramVarArgs[3];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static short[] unpack(long paramLong) {
/* 150 */     return new short[] { (short)(int)(paramLong >> 48L), (short)(int)(paramLong >> 32L & 0x7FFFL), (short)(int)(paramLong >> 16L & 0x7FFFL), (short)(int)(paramLong & 0x7FFFL) };
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
/*     */   public static VersionNumber parse(String paramString) {
/* 165 */     return parse(paramString, NONE);
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
/*     */   public static VersionNumber parse(String paramString1, String paramString2) {
/* 177 */     return parse(paramString1, parse(paramString2));
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
/*     */   private static VersionNumber parse(String paramString, VersionNumber paramVersionNumber) {
/* 189 */     if (paramString == null) {
/* 190 */       return paramVersionNumber;
/*     */     }
/*     */     
/* 193 */     Matcher matcher = PATTERN.matcher(paramString);
/* 194 */     if (!matcher.matches()) {
/* 195 */       return paramVersionNumber;
/*     */     }
/*     */     
/* 198 */     short[] arrayOfShort = new short[4];
/* 199 */     for (byte b = 0; b < 4; b++) {
/* 200 */       String str = matcher.group(b + 1);
/* 201 */       if (str != null) {
/* 202 */         int i = Integer.parseInt(str);
/* 203 */         if (i > 32767) {
/* 204 */           throw new IllegalArgumentException("Version parts cannot exceed 32767, found " + i);
/*     */         }
/* 206 */         arrayOfShort[b] = (short)i;
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     return new VersionNumber(arrayOfShort, matcher.group(5));
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\VersionNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */