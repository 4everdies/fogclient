/*    */ package org.spongepowered.asm.mixin;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ import net.minecraft.launchwrapper.ITweaker;
/*    */ import net.minecraft.launchwrapper.LaunchClassLoader;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
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
/*    */ public class EnvironmentStateTweaker
/*    */   implements ITweaker
/*    */ {
/*    */   public void acceptOptions(List<String> paramList, File paramFile1, File paramFile2, String paramString) {}
/*    */   
/*    */   public void injectIntoClassLoader(LaunchClassLoader paramLaunchClassLoader) {
/* 48 */     MixinBootstrap.getPlatform().inject();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLaunchTarget() {
/* 53 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getLaunchArguments() {
/* 58 */     MixinEnvironment.gotoPhase(MixinEnvironment.Phase.DEFAULT);
/* 59 */     return new String[0];
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\EnvironmentStateTweaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */