/*    */ package org.spongepowered.asm.bridge;
/*    */ 
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.objectweb.asm.commons.Remapper;
/*    */ import org.spongepowered.asm.mixin.extensibility.IRemapper;
/*    */ import org.spongepowered.asm.util.ObfuscationUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RemapperAdapter
/*    */   implements IRemapper, ObfuscationUtil.IClassRemapper
/*    */ {
/* 38 */   protected final Logger logger = LogManager.getLogger("mixin");
/*    */   protected final Remapper remapper;
/*    */   
/*    */   public RemapperAdapter(Remapper paramRemapper) {
/* 42 */     this.remapper = paramRemapper;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return getClass().getSimpleName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapMethodName(String paramString1, String paramString2, String paramString3) {
/* 52 */     this.logger.debug("{} is remapping method {}{} for {}", new Object[] { this, paramString2, paramString3, paramString1 });
/* 53 */     String str1 = this.remapper.mapMethodName(paramString1, paramString2, paramString3);
/* 54 */     if (!str1.equals(paramString2)) {
/* 55 */       return str1;
/*    */     }
/* 57 */     String str2 = unmap(paramString1);
/* 58 */     String str3 = unmapDesc(paramString3);
/* 59 */     this.logger.debug("{} is remapping obfuscated method {}{} for {}", new Object[] { this, paramString2, str3, str2 });
/* 60 */     return this.remapper.mapMethodName(str2, paramString2, str3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapFieldName(String paramString1, String paramString2, String paramString3) {
/* 65 */     this.logger.debug("{} is remapping field {}{} for {}", new Object[] { this, paramString2, paramString3, paramString1 });
/* 66 */     String str1 = this.remapper.mapFieldName(paramString1, paramString2, paramString3);
/* 67 */     if (!str1.equals(paramString2)) {
/* 68 */       return str1;
/*    */     }
/* 70 */     String str2 = unmap(paramString1);
/* 71 */     String str3 = unmapDesc(paramString3);
/* 72 */     this.logger.debug("{} is remapping obfuscated field {}{} for {}", new Object[] { this, paramString2, str3, str2 });
/* 73 */     return this.remapper.mapFieldName(str2, paramString2, str3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String map(String paramString) {
/* 78 */     this.logger.debug("{} is remapping class {}", new Object[] { this, paramString });
/* 79 */     return this.remapper.map(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String unmap(String paramString) {
/* 84 */     return paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String mapDesc(String paramString) {
/* 89 */     return this.remapper.mapDesc(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String unmapDesc(String paramString) {
/* 94 */     return ObfuscationUtil.unmapDescriptor(paramString, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\bridge\RemapperAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */