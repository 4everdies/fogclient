/*    */ package org.spongepowered.asm.mixin.transformer;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.launchwrapper.IClassTransformer;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.spongepowered.asm.service.ILegacyClassTransformer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Proxy
/*    */   implements IClassTransformer, ILegacyClassTransformer
/*    */ {
/* 47 */   private static List<Proxy> proxies = new ArrayList<Proxy>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   private static MixinTransformer transformer = new MixinTransformer();
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isActive = true;
/*    */ 
/*    */ 
/*    */   
/*    */   public Proxy() {
/* 61 */     for (Proxy proxy : proxies) {
/* 62 */       proxy.isActive = false;
/*    */     }
/*    */     
/* 65 */     proxies.add(this);
/* 66 */     LogManager.getLogger("mixin").debug("Adding new mixin transformer proxy #{}", new Object[] { Integer.valueOf(proxies.size()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] transform(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
/* 71 */     if (this.isActive) {
/* 72 */       return transformer.transformClassBytes(paramString1, paramString2, paramArrayOfbyte);
/*    */     }
/*    */     
/* 75 */     return paramArrayOfbyte;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 80 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDelegationExcluded() {
/* 85 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] transformClassBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
/* 90 */     if (this.isActive) {
/* 91 */       return transformer.transformClassBytes(paramString1, paramString2, paramArrayOfbyte);
/*    */     }
/*    */     
/* 94 */     return paramArrayOfbyte;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\Proxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */