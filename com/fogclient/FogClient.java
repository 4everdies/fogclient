/*    */ package com.fogclient;
/*    */ 
/*    */ import com.fogclient.command.CommandKillBot;
/*    */ import com.fogclient.command.CommandSpawnBot;
/*    */ import com.fogclient.config.ConfigManager;
/*    */ import com.fogclient.entity.EntityPvPBot;
/*    */ import com.fogclient.event.EventDispatcher;
/*    */ import com.fogclient.module.ModuleManager;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.Mod;
/*    */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*    */ import net.minecraftforge.fml.common.Mod.Instance;
/*    */ import net.minecraftforge.fml.common.SidedProxy;
/*    */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
/*    */ import net.minecraftforge.fml.common.registry.EntityRegistry;
/*    */ 
/*    */ @Mod(modid = "fogclient", version = "1.0", name = "FogClient", acceptedMinecraftVersions = "[1.8.9]")
/*    */ public class FogClient {
/*    */   public static final String MODID = "fogclient";
/*    */   public static final String VERSION = "1.0";
/*    */   public static final String NAME = "FogClient";
/*    */   @Instance
/*    */   public static FogClient instance;
/*    */   @SidedProxy(clientSide = "com.fogclient.ClientProxy", serverSide = "com.fogclient.CommonProxy")
/*    */   public static CommonProxy proxy;
/*    */   
/*    */   @EventHandler
/*    */   public void preInit(FMLPreInitializationEvent paramFMLPreInitializationEvent) {
/* 33 */     EntityRegistry.registerModEntity(EntityPvPBot.class, "TreinoBot", 202, this, 64, 1, true);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void init(FMLInitializationEvent paramFMLInitializationEvent) {
/* 39 */     proxy.registerRenderers();
/*    */ 
/*    */     
/* 42 */     MinecraftForge.EVENT_BUS.register(new EventDispatcher());
/*    */ 
/*    */     
/* 45 */     ModuleManager.getInstance();
/*    */ 
/*    */     
/* 48 */     ConfigManager.loadConfig();
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void serverLoad(FMLServerStartingEvent paramFMLServerStartingEvent) {
/* 53 */     paramFMLServerStartingEvent.registerServerCommand((ICommand)new CommandSpawnBot());
/* 54 */     paramFMLServerStartingEvent.registerServerCommand((ICommand)new CommandKillBot());
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void serverStopped(FMLServerStoppedEvent paramFMLServerStoppedEvent) {
/* 60 */     ConfigManager.saveConfig();
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\FogClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */