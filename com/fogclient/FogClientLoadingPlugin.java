/*    */ package com.fogclient;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ @MCVersion("1.8.9")
/*    */ public class FogClientLoadingPlugin
/*    */   implements IFMLLoadingPlugin {
/*    */   public FogClientLoadingPlugin() {
/* 13 */     MixinBootstrap.init();
/* 14 */     Mixins.addConfiguration("mixins.ghostreach.json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 19 */     return new String[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 24 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 29 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> paramMap) {}
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\FogClientLoadingPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */