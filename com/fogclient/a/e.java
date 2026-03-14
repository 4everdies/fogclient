/*    */ package com.fogclient.a;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class e {
/*    */   private static boolean planMessageShown = false;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 14 */     if (event.phase != TickEvent.Phase.END)
/*    */       return; 
/* 16 */     Minecraft mc = Minecraft.func_71410_x();
/* 17 */     if (!planMessageShown && mc != null && mc.field_71439_g != null) {
/* 18 */       planMessageShown = true;
/* 19 */       String msg = "§2[FogClient]§f Plano: §a" + f.getPlanType();
/* 20 */       mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(msg));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\a\e.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */