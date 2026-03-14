/*    */ package com.fogclient.command;
/*    */ import com.fogclient.entity.DificuldadeBot;
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.entity.SharedMonsterAttributes;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class CommandSpawnBot extends CommandBase {
/*    */   public String func_71517_b() {
/* 13 */     return "spawnbot";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender paramICommandSender) {
/* 17 */     return "/spawnbot <dano_em_coracoes> <dificuldade (facil|intermediario|dificil|cheater)>\nExemplo: /spawnbot 4 facil";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender paramICommandSender, String[] paramArrayOfString) {
/* 22 */     if (!(paramICommandSender instanceof EntityPlayer)) {
/* 23 */       paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Apenas jogadores podem usar este comando."));
/*    */       return;
/*    */     } 
/* 26 */     if (paramArrayOfString.length == 0) {
/* 27 */       paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText(func_71518_a(paramICommandSender)));
/*    */       
/*    */       return;
/*    */     } 
/* 31 */     EntityPlayer entityPlayer = (EntityPlayer)paramICommandSender;
/* 32 */     World world = entityPlayer.field_70170_p;
/* 33 */     String str1 = entityPlayer.func_70005_c_();
/*    */     
/* 35 */     float f = 8.0F;
/*    */     try {
/* 37 */       if (paramArrayOfString.length >= 1) {
/* 38 */         f = Float.parseFloat(paramArrayOfString[0]) * 2.0F;
/*    */       }
/* 40 */     } catch (NumberFormatException numberFormatException) {
/* 41 */       paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Dano invÃ¡lido, usando padrÃ£o de 4 coraÃ§Ãµes."));
/*    */     } 
/*    */     
/* 44 */     String str2 = (paramArrayOfString.length >= 2) ? paramArrayOfString[1].toLowerCase() : "facil";
/* 45 */     DificuldadeBot dificuldadeBot = DificuldadeBot.FACIL;
/*    */     
/* 47 */     if (str2.startsWith("fac") || str2.startsWith("eas")) { dificuldadeBot = DificuldadeBot.FACIL; }
/* 48 */     else if (str2.startsWith("int") || str2.startsWith("med")) { dificuldadeBot = DificuldadeBot.INTERMEDIARIO; }
/* 49 */     else if (str2.startsWith("dif") || str2.startsWith("har")) { dificuldadeBot = DificuldadeBot.DIFICIL; }
/* 50 */     else if (str2.startsWith("che")) { dificuldadeBot = DificuldadeBot.CHEATER; }
/*    */     else
/* 52 */     { paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Dificuldade invÃ¡lida. Use: facil, intermediario, dificil ou cheater."));
/*    */       
/*    */       return; }
/*    */     
/* 56 */     EntityPvPBot entityPvPBot = new EntityPvPBot(world, dificuldadeBot);
/* 57 */     entityPvPBot.func_70107_b(entityPlayer.field_70165_t, entityPlayer.field_70163_u, entityPlayer.field_70161_v);
/* 58 */     entityPvPBot.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(f);
/* 59 */     entityPvPBot.setNickExibido(str1);
/*    */     
/* 61 */     if (!world.field_72995_K) {
/* 62 */       world.func_72838_d((Entity)entityPvPBot);
/* 63 */       paramICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Bot de PvP invocado com dano de " + (f / 2.0F) + " coraÃ§Ãµes! Dificuldade: " + str2));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 69 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\command\CommandSpawnBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */