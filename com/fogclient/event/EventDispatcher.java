/*     */ package com.fogclient.event;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.module.ModuleManager;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.event.entity.living.LivingEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class EventDispatcher {
/*  20 */   private final Minecraft mc = Minecraft.func_71410_x();
/*     */ 
/*     */   
/*     */   private int saveTimer;
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent paramKeyInputEvent) {
/*  29 */     if (this.mc.field_71462_r != null)
/*     */       return; 
/*  31 */     int i = Keyboard.getEventKey();
/*  32 */     if (i == 0)
/*     */       return; 
/*  34 */     if (Keyboard.getEventKeyState()) {
/*  35 */       ModuleManager.getInstance().onKeyPressed(i);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMouseInput(InputEvent.MouseInputEvent paramMouseInputEvent) {
/*  41 */     if (this.mc.field_71462_r != null)
/*     */       return; 
/*  43 */     int i = Mouse.getEventButton();
/*  44 */     if (i < 0) {
/*     */       return;
/*     */     }
/*     */     
/*  48 */     int j = -100 + i;
/*     */     
/*  50 */     if (Mouse.getEventButtonState())
/*  51 */       ModuleManager.getInstance().onKeyPressed(j); 
/*     */   }
/*     */   
/*     */   public EventDispatcher() {
/*  55 */     this.saveTimer = 0;
/*     */     MinecraftForge.EVENT_BUS.register(this);
/*     */     FMLCommonHandler.instance().bus().register(this); } @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent paramClientTickEvent) {
/*  59 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*     */       return; 
/*  61 */     for (Module module : ModuleManager.getInstance().getModules()) {
/*  62 */       if (module.isEnabled()) {
/*  63 */         if (paramClientTickEvent.phase == TickEvent.Phase.START) {
/*  64 */           module.onTickStart(); continue;
/*     */         } 
/*  66 */         module.onTick();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (paramClientTickEvent.phase == TickEvent.Phase.END) {
/*  73 */       this.saveTimer++;
/*  74 */       if (this.saveTimer >= 100) {
/*  75 */         this.saveTimer = 0;
/*  76 */         ConfigManager.saveConfig();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onLivingUpdate(LivingEvent.LivingUpdateEvent paramLivingUpdateEvent) {
/*  83 */     if (paramLivingUpdateEvent.entity != this.mc.field_71439_g)
/*     */       return; 
/*  85 */     for (Module module : ModuleManager.getInstance().getModules()) {
/*  86 */       if (module.isEnabled()) {
/*  87 */         module.onLivingUpdate();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderWorldLast(RenderWorldLastEvent paramRenderWorldLastEvent) {
/*  94 */     for (Module module : ModuleManager.getInstance().getModules()) {
/*  95 */       if (module.isEnabled()) {
/*  96 */         module.onRenderTick();
/*  97 */         module.onEvent((Event)paramRenderWorldLastEvent);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttack(AttackEntityEvent paramAttackEntityEvent) {
/* 104 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 105 */       if (module.isEnabled()) {
/* 106 */         module.onEvent((Event)paramAttackEntityEvent);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onGuiOpen(GuiOpenEvent paramGuiOpenEvent) {
/* 113 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 114 */       if (module.isEnabled()) {
/* 115 */         module.onEvent((Event)paramGuiOpenEvent);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre paramPre) {
/* 122 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 123 */       if (module.isEnabled()) {
/* 124 */         module.onEvent((Event)paramPre);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent paramPlayerLoggedInEvent) {
/* 131 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 132 */       if (module.isEnabled()) {
/* 133 */         module.onEvent((Event)paramPlayerLoggedInEvent);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onNameFormat(PlayerEvent.NameFormat paramNameFormat) {
/* 140 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 141 */       if (module.isEnabled()) {
/* 142 */         module.onEvent((Event)paramNameFormat);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldTick(TickEvent.WorldTickEvent paramWorldTickEvent) {
/* 149 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 150 */       if (module.isEnabled()) {
/* 151 */         module.onEvent((Event)paramWorldTickEvent);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onNeighborNotify(BlockEvent.NeighborNotifyEvent paramNeighborNotifyEvent) {
/* 158 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 159 */       if (module.isEnabled()) {
/* 160 */         module.onEvent((Event)paramNeighborNotifyEvent);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlayerInteract(PlayerInteractEvent paramPlayerInteractEvent) {
/* 167 */     for (Module module : ModuleManager.getInstance().getModules()) {
/* 168 */       if (module.isEnabled())
/* 169 */         module.onEvent((Event)paramPlayerInteractEvent); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\event\EventDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */