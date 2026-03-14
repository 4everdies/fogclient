/*     */ package com.fogclient.module.player;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import java.lang.reflect.Field;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class JuntaPotesModule extends Module {
/*     */   private static Field guiLeftField;
/*     */   
/*     */   static {
/*     */     try {
/*  22 */       guiLeftField = ReflectionHelper.findField(GuiContainer.class, new String[] { "guiLeft", "field_147003_i" });
/*  23 */       guiLeftField.setAccessible(true);
/*  24 */       guiTopField = ReflectionHelper.findField(GuiContainer.class, new String[] { "guiTop", "field_147009_r" });
/*  25 */       guiTopField.setAccessible(true);
/*  26 */     } catch (Exception exception) {
/*  27 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */   private static Field guiTopField;
/*     */   public JuntaPotesModule() {
/*  32 */     super("JuntaPotes", "Groups bowls in inventory with middle click (Button 5)", Category.PLAYER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */   
/*     */   public void onDisable() {}
/*     */ 
/*     */   
/*     */   public void onEvent(Event paramEvent) {
/*  43 */     if (paramEvent instanceof GuiScreenEvent.MouseInputEvent.Pre) {
/*  44 */       GuiScreenEvent.MouseInputEvent.Pre pre = (GuiScreenEvent.MouseInputEvent.Pre)paramEvent;
/*  45 */       if (!Mouse.getEventButtonState())
/*     */         return; 
/*  47 */       int i = Mouse.getEventButton();
/*     */       
/*  49 */       if (i == 4 && 
/*  50 */         pre.gui instanceof GuiContainer) {
/*  51 */         GuiContainer guiContainer = (GuiContainer)pre.gui;
/*  52 */         Slot slot = getSlotAtMousePosition(guiContainer);
/*     */         
/*  54 */         if (slot != null && slot.func_75216_d() && isValidItem(slot.func_75211_c())) {
/*     */           try {
/*  56 */             handleItemClick(guiContainer, slot);
/*  57 */             pre.setCanceled(true);
/*  58 */           } catch (Exception exception) {
/*  59 */             exception.printStackTrace();
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Slot getSlotAtMousePosition(GuiContainer paramGuiContainer) {
/*  68 */     if (guiLeftField == null || guiTopField == null) return null;
/*     */     
/*     */     try {
/*  71 */       int i = guiLeftField.getInt(paramGuiContainer);
/*  72 */       int j = guiTopField.getInt(paramGuiContainer);
/*     */       
/*  74 */       int k = Mouse.getEventX() * paramGuiContainer.field_146294_l / mc.field_71443_c;
/*  75 */       int m = paramGuiContainer.field_146295_m - Mouse.getEventY() * paramGuiContainer.field_146295_m / mc.field_71440_d - 1;
/*     */       
/*  77 */       for (Slot slot : paramGuiContainer.field_147002_h.field_75151_b) {
/*  78 */         if (isMouseOverSlot(slot, k, m, i, j)) {
/*  79 */           return slot;
/*     */         }
/*     */       } 
/*  82 */     } catch (Exception exception) {
/*  83 */       exception.printStackTrace();
/*     */     } 
/*  85 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isMouseOverSlot(Slot paramSlot, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  89 */     int i = paramInt3 + paramSlot.field_75223_e;
/*  90 */     int j = paramInt4 + paramSlot.field_75221_f;
/*  91 */     return (paramInt1 >= i - 1 && paramInt1 < i + 16 + 1 && paramInt2 >= j - 1 && paramInt2 < j + 16 + 1);
/*     */   }
/*     */   
/*     */   private boolean isValidItem(ItemStack paramItemStack) {
/*  95 */     if (paramItemStack == null) return false; 
/*  96 */     Item item = paramItemStack.func_77973_b();
/*  97 */     return (item == Items.field_151054_z);
/*     */   }
/*     */   
/*     */   private void handleItemClick(GuiContainer paramGuiContainer, Slot paramSlot) {
/* 101 */     ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70445_o();
/*     */     
/* 103 */     if (itemStack == null) {
/* 104 */       mc.field_71442_b.func_78753_a(paramGuiContainer.field_147002_h.field_75152_c, paramSlot.field_75222_d, 0, 0, (EntityPlayer)mc.field_71439_g);
/*     */     }
/*     */     
/* 107 */     mc.field_71442_b.func_78753_a(paramGuiContainer.field_147002_h.field_75152_c, paramSlot.field_75222_d, 0, 6, (EntityPlayer)mc.field_71439_g);
/* 108 */     mc.field_71442_b.func_78753_a(paramGuiContainer.field_147002_h.field_75152_c, paramSlot.field_75222_d, 0, 0, (EntityPlayer)mc.field_71439_g);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\player\JuntaPotesModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */