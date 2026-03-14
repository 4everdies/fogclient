/*    */ package com.fogclient.util;
/*    */ 
/*    */ import com.fogclient.module.render.EscolheNickModule;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class CustomFontRenderer extends FontRenderer {
/*    */   public CustomFontRenderer(GameSettings paramGameSettings, ResourceLocation paramResourceLocation, TextureManager paramTextureManager, boolean paramBoolean) {
/* 13 */     super(paramGameSettings, paramResourceLocation, paramTextureManager, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_175065_a(String paramString, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean) {
/* 18 */     return super.func_175065_a(replaceNick(paramString), paramFloat1, paramFloat2, paramInt, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_78256_a(String paramString) {
/* 23 */     return super.func_78256_a(replaceNick(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_78279_b(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 28 */     super.func_78279_b(replaceNick(paramString), paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_78271_c(String paramString, int paramInt) {
/* 33 */     return super.func_78271_c(replaceNick(paramString), paramInt);
/*    */   }
/*    */   
/*    */   private String replaceNick(String paramString) {
/* 37 */     EscolheNickModule escolheNickModule = EscolheNickModule.getInstance();
/* 38 */     if (escolheNickModule == null || !escolheNickModule.isEnabled() || EscolheNickModule.nickMap.isEmpty()) return paramString;
/*    */     
/* 40 */     if (paramString == null) return null;
/*    */     
/* 42 */     for (Map.Entry entry : EscolheNickModule.nickMap.entrySet()) {
/* 43 */       if (paramString.contains((CharSequence)entry.getKey())) {
/* 44 */         paramString = paramString.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
/*    */       }
/*    */     } 
/* 47 */     return paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\util\CustomFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */