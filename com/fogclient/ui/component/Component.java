/*    */ package com.fogclient.ui.component;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public abstract class Component
/*    */ {
/*  7 */   protected static final Minecraft mc = Minecraft.func_71410_x();
/*    */ 
/*    */   
/*    */   public abstract void render(int paramInt1, int paramInt2, float paramFloat);
/*    */   
/*    */   public abstract void mouseClicked(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public boolean isHovered(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 15 */     return (paramInt1 >= paramInt3 && paramInt1 <= paramInt3 + paramInt5 && paramInt2 >= paramInt4 && paramInt2 <= paramInt4 + paramInt6);
/*    */   }
/*    */   
/*    */   public abstract void mouseReleased(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public abstract void keyTyped(char paramChar, int paramInt);
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclien\\ui\component\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */