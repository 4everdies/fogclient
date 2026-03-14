/*    */ package com.fogclient.command;
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class CommandKillBot extends CommandBase {
/*    */   public String func_71517_b() {
/* 12 */     return "killbot";
/*    */   }
/*    */   public String func_71518_a(ICommandSender paramICommandSender) {
/* 15 */     return "/killbot";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender paramICommandSender, String[] paramArrayOfString) {
/* 19 */     if (!(paramICommandSender instanceof EntityPlayer)) {
/* 20 */       paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Apenas jogadores podem usar este comando."));
/*    */       return;
/*    */     } 
/* 23 */     EntityPlayer entityPlayer = (EntityPlayer)paramICommandSender;
/* 24 */     World world = entityPlayer.field_70170_p;
/* 25 */     byte b = 0;
/* 26 */     if (!world.field_72995_K) {
/* 27 */       for (EntityPvPBot entityPvPBot : world.field_72996_f) {
/* 28 */         if (entityPvPBot instanceof EntityPvPBot) {
/* 29 */           ((EntityPvPBot)entityPvPBot).func_70106_y();
/* 30 */           b++;
/*    */         } 
/*    */       } 
/* 33 */       if (b == 1) {
/* 34 */         paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Removido 1 bot do mundo."));
/*    */       } else {
/* 36 */         paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Removidos " + b + " bots do mundo."));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 43 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\command\CommandKillBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */