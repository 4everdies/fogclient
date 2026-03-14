/*     */ package com.fogclient.module.render;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.ActionSetting;
/*     */ import com.fogclient.setting.StringSetting;
/*     */ import com.fogclient.util.CustomFontRenderer;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.resources.IReloadableResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.ClientCommandHandler;
/*     */ import net.minecraftforge.event.entity.player.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ public class EscolheNickModule extends Module {
/*  26 */   public static Map<String, String> nickMap = new HashMap<>();
/*     */   private static EscolheNickModule instance; private boolean fontRendererInjected = false; private StringSetting realNick; private StringSetting newNick;
/*     */   private ActionSetting applyButton;
/*     */   private ActionSetting resetButton;
/*     */   private boolean commandsRegistered;
/*     */   
/*     */   public static EscolheNickModule getInstance() {
/*     */     return instance;
/*     */   }
/*     */   
/*  36 */   public EscolheNickModule() { super("EscolheNick", "Change players' nicknames locally", Category.RENDER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.commandsRegistered = false; instance = this; this.realNick = new StringSetting("Nick Real", this, ""); this.newNick = new StringSetting("Novo Nick", this, ""); this.applyButton = new ActionSetting("Aplicar", this, new Runnable() { public void run() { if (EscolheNickModule.this.realNick.getValue().isEmpty() || EscolheNickModule.this.newNick.getValue().isEmpty()) { if (EscolheNickModule.mc.field_71439_g != null) EscolheNickModule.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§cPreencha os campos de nick!"));  return; }  EscolheNickModule.nickMap.put(EscolheNickModule.this.realNick.getValue(), EscolheNickModule.this.newNick.getValue()); if (EscolheNickModule.mc.field_71439_g != null) { EscolheNickModule.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§aNick alterado: " + EscolheNickModule.this.realNick.getValue() + " -> " + EscolheNickModule.this.newNick.getValue())); EscolheNickModule.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§7Relogue no servidor para aplicar completamente.")); }  EscolheNickModule.this.applyTabNames(); } }
/*     */       ); this.resetButton = new ActionSetting("Resetar", this, new Runnable() {
/*     */           public void run() { EscolheNickModule.nickMap.clear(); if (EscolheNickModule.mc.field_71439_g != null) EscolheNickModule.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§aNicks resetados."));  EscolheNickModule.this.applyTabNames(); }
/* 112 */         }); } public void onEnable() { injectFontRenderer(); registerCommands(); applyTabNames(); } private void registerCommands() { if (this.commandsRegistered)
/*     */       return;  
/* 114 */     try { ClientCommandHandler.instance.func_71560_a((ICommand)new CommandNickEscolher());
/* 115 */       ClientCommandHandler.instance.func_71560_a((ICommand)new CommandNickReset());
/* 116 */       this.commandsRegistered = true; }
/* 117 */     catch (Exception exception)
/* 118 */     { exception.printStackTrace(); }  } public void onDisable() { applyTabNames(); }
/*     */   private void injectFontRenderer() { if (this.fontRendererInjected)
/*     */       return;  if (!(mc.field_71466_p instanceof CustomFontRenderer)) { CustomFontRenderer customFontRenderer = new CustomFontRenderer(mc.field_71474_y, new ResourceLocation("textures/font/ascii.png"), mc.field_71446_o, false); if (mc.field_71474_y.field_74363_ab != null) { customFontRenderer.func_78264_a(mc.func_152349_b()); customFontRenderer.func_78275_b(mc.func_135016_M().func_135044_b()); }
/*     */        mc.field_71466_p = (FontRenderer)customFontRenderer; if (mc.func_110442_L() instanceof IReloadableResourceManager)
/*     */         ((IReloadableResourceManager)mc.func_110442_L()).func_110542_a((IResourceManagerReloadListener)mc.field_71466_p);  this.fontRendererInjected = true; }
/*     */      }
/* 124 */   public void onEvent(Event paramEvent) { if (paramEvent instanceof PlayerEvent.NameFormat) {
/* 125 */       PlayerEvent.NameFormat nameFormat = (PlayerEvent.NameFormat)paramEvent;
/* 126 */       if (!isEnabled() || nickMap.isEmpty())
/*     */         return; 
/* 128 */       String str = nameFormat.username;
/* 129 */       for (Map.Entry<String, String> entry : nickMap.entrySet()) {
/* 130 */         if (str.equals(entry.getKey())) {
/* 131 */           nameFormat.displayname = (String)entry.getValue();
/*     */         }
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 139 */     applyTabNames();
/*     */   }
/*     */   
/*     */   private void applyTabNames() {
/* 143 */     if (mc.field_71441_e == null || mc.func_147114_u() == null)
/*     */       return; 
/* 145 */     if (!isEnabled() || nickMap.isEmpty()) {
/* 146 */       for (NetworkPlayerInfo networkPlayerInfo : mc.func_147114_u().func_175106_d()) {
/*     */ 
/*     */         
/* 149 */         if (networkPlayerInfo.func_178854_k() != null) {
/* 150 */           networkPlayerInfo.func_178859_a(null);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     for (NetworkPlayerInfo networkPlayerInfo : mc.func_147114_u().func_175106_d()) {
/* 157 */       GameProfile gameProfile = networkPlayerInfo.func_178845_a();
/* 158 */       String str = gameProfile.getName();
/* 159 */       if (nickMap.containsKey(str)) {
/* 160 */         networkPlayerInfo.func_178859_a((IChatComponent)new ChatComponentText(nickMap.get(str)));
/*     */         continue;
/*     */       } 
/* 163 */       if (networkPlayerInfo.func_178854_k() != null && !networkPlayerInfo.func_178854_k().func_150254_d().equals(str))
/* 164 */         networkPlayerInfo.func_178859_a(null); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class CommandNickEscolher
/*     */     extends CommandBase
/*     */   {
/*     */     public String func_71517_b() {
/* 172 */       return "nickescolher";
/*     */     } public String func_71518_a(ICommandSender param1ICommandSender) {
/* 174 */       return "/nickescolher <real> <novo>";
/*     */     }
/*     */     public void func_71515_b(ICommandSender param1ICommandSender, String[] param1ArrayOfString) throws CommandException {
/* 177 */       if (param1ArrayOfString.length < 2)
/* 178 */         return;  EscolheNickModule.nickMap.put(param1ArrayOfString[0], param1ArrayOfString[1]);
/* 179 */       param1ICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Nick set: " + param1ArrayOfString[0] + " -> " + param1ArrayOfString[1]));
/* 180 */       param1ICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Relogue no servidor para a alteraÃ§Ã£o de nick ser aplicada corretamente"));
/* 181 */       if (EscolheNickModule.instance != null) EscolheNickModule.instance.applyTabNames(); 
/*     */     }
/*     */     public int func_82362_a() {
/* 184 */       return 0;
/*     */     } }
/*     */   
/*     */   public static class CommandNickReset extends CommandBase {
/*     */     public String func_71517_b() {
/* 189 */       return "nickreset";
/*     */     } public String func_71518_a(ICommandSender param1ICommandSender) {
/* 191 */       return "/nickreset";
/*     */     }
/*     */     public void func_71515_b(ICommandSender param1ICommandSender, String[] param1ArrayOfString) throws CommandException {
/* 194 */       EscolheNickModule.nickMap.clear();
/* 195 */       param1ICommandSender.func_145747_a((IChatComponent)new ChatComponentText("Â§aNicks resetados com sucesso. Â§7(" + EscolheNickModule.nickMap.size() + " ativos)"));
/* 196 */       if (EscolheNickModule.instance != null) EscolheNickModule.instance.applyTabNames(); 
/*     */     }
/*     */     public int func_82362_a() {
/* 199 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\EscolheNickModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */