/*    */ package org.spongepowered.asm.launch.platform;
/*    */ 
/*    */ import java.net.URI;
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
/*    */ public class MixinPlatformAgentDefault
/*    */   extends MixinPlatformAgentAbstract
/*    */ {
/*    */   public MixinPlatformAgentDefault(MixinPlatformManager paramMixinPlatformManager, URI paramURI) {
/* 42 */     super(paramMixinPlatformManager, paramURI);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void prepare() {
/* 48 */     String str1 = this.attributes.get("MixinCompatibilityLevel");
/* 49 */     if (str1 != null) {
/* 50 */       this.manager.setCompatibilityLevel(str1);
/*    */     }
/*    */     
/* 53 */     String str2 = this.attributes.get("MixinConfigs");
/* 54 */     if (str2 != null) {
/* 55 */       for (String str : str2.split(",")) {
/* 56 */         this.manager.addConfig(str.trim());
/*    */       }
/*    */     }
/*    */     
/* 60 */     String str3 = this.attributes.get("MixinTokenProviders");
/* 61 */     if (str3 != null) {
/* 62 */       for (String str : str3.split(",")) {
/* 63 */         this.manager.addTokenProvider(str.trim());
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void initPrimaryContainer() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void inject() {}
/*    */ 
/*    */   
/*    */   public String getLaunchTarget() {
/* 78 */     return this.attributes.get("Main-Class");
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\launch\platform\MixinPlatformAgentDefault.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */