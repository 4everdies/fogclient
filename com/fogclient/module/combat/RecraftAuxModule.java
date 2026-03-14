/*     */ package com.fogclient.module.combat;
/*     */ 
/*     */ import com.fogclient.module.Category;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import com.fogclient.util.crafting.CraftAction;
/*     */ import com.fogclient.util.crafting.CraftActionType;
/*     */ import com.fogclient.util.crafting.RecipeType;
/*     */ import com.fogclient.util.crafting.RecraftRecipe;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ContainerPlayer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class RecraftAuxModule
/*     */   extends Module
/*     */ {
/*     */   private NumberSetting speedSetting;
/*     */   private boolean isCrafting = false;
/*  30 */   private int tickCounter = 0;
/*  31 */   private List<CraftAction> actionQueue = new ArrayList<>();
/*  32 */   private int currentActionIndex = 0;
/*     */ 
/*     */   
/*  35 */   private static final Item BOWL = Items.field_151054_z;
/*  36 */   private static final Item RED_MUSHROOM = Item.func_150898_a((Block)Blocks.field_150337_Q);
/*  37 */   private static final Item BROWN_MUSHROOM = Item.func_150898_a((Block)Blocks.field_150338_P);
/*  38 */   private static final Item COCOA_BEANS = Items.field_151100_aR;
/*  39 */   private static final Item CACTUS = Item.func_150898_a((Block)Blocks.field_150434_aF);
/*  40 */   private static final Item MUSHROOM_SOUP = Items.field_151009_A;
/*     */   
/*     */   private static final int CRAFT_SLOT_1 = 1;
/*     */   private static final int CRAFT_SLOT_2 = 2;
/*     */   private static final int CRAFT_SLOT_3 = 3;
/*     */   private static final int CRAFT_SLOT_4 = 4;
/*     */   private static final int RESULT_SLOT = 0;
/*     */   
/*     */   public RecraftAuxModule() {
/*  49 */     super("AjudanteDeRecraft", "Auxilia no recraft de sopas.", Category.COMBAT);
/*  50 */     this.keybind = 0;
/*  51 */     this.speedSetting = new NumberSetting("Speed", this, 100.0D, 1.0D, 100.0D, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  66 */     this.isCrafting = false;
/*  67 */     this.actionQueue.clear();
/*  68 */     this.currentActionIndex = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onKeyPressed() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAction() {
/*  79 */     if (isEnabled() && 
/*  80 */       !this.isCrafting) {
/*  81 */       handleAutoRecraft();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpeed() {
/*  87 */     return (int)this.speedSetting.getValue();
/*     */   }
/*     */   
/*     */   public void onTick() {
/*     */     int k;
/*  92 */     if (!this.isCrafting) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     Minecraft minecraft = Minecraft.func_71410_x();
/*  97 */     if (minecraft.field_71462_r == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/*  98 */       this.isCrafting = false;
/*  99 */       this.actionQueue.clear();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 105 */     int i = getSpeed();
/*     */     
/* 107 */     int j = 0;
/*     */ 
/*     */     
/* 110 */     if (i >= 90) {
/* 111 */       int m = (i - 90) / 2;
/* 112 */       j = 3 + m;
/* 113 */       k = 0;
/* 114 */     } else if (i >= 50) {
/* 115 */       j = 1 + (i - 50) / 40;
/* 116 */       k = 0;
/*     */     } else {
/* 118 */       j = 1;
/* 119 */       k = 10 - (int)(i / 50.0D * 10.0D);
/*     */     } 
/*     */     
/* 122 */     if (this.tickCounter < k) {
/* 123 */       this.tickCounter++;
/*     */       return;
/*     */     } 
/* 126 */     this.tickCounter = 0;
/*     */     
/* 128 */     byte b = 0;
/*     */     
/* 130 */     while (this.currentActionIndex < this.actionQueue.size() && b < j) {
/* 131 */       CraftAction craftAction = this.actionQueue.get(this.currentActionIndex);
/*     */       
/* 133 */       if (this.currentActionIndex == 0 && craftAction.type == CraftActionType.WAIT && this.actionQueue.size() == 1) {
/* 134 */         if (executeAction(craftAction)) {
/* 135 */           this.currentActionIndex++;
/* 136 */           prepareCraftActions();
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 141 */       if (i > 30 && craftAction.type == CraftActionType.PICKUP && this.currentActionIndex + 1 < this.actionQueue.size()) {
/* 142 */         CraftAction craftAction1 = this.actionQueue.get(this.currentActionIndex + 1);
/* 143 */         if (craftAction1.type == CraftActionType.PLACE) {
/* 144 */           if (executeAction(craftAction)) {
/* 145 */             this.currentActionIndex++;
/* 146 */             b++;
/*     */             
/* 148 */             if ((b < j || i > 60) && 
/* 149 */               executeAction(craftAction1)) {
/* 150 */               this.currentActionIndex++;
/* 151 */               b++;
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 158 */         if (executeAction(craftAction)) {
/* 159 */           this.currentActionIndex++;
/* 160 */           b++;
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 166 */       if (executeAction(craftAction)) {
/* 167 */         this.currentActionIndex++;
/* 168 */         b++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     if (this.currentActionIndex >= this.actionQueue.size()) {
/* 176 */       this.isCrafting = false;
/* 177 */       this.actionQueue.clear();
/* 178 */       this.currentActionIndex = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleAutoRecraft() {
/* 184 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 185 */     EntityPlayerSP entityPlayerSP = minecraft.field_71439_g;
/*     */     
/* 187 */     if (entityPlayerSP == null)
/*     */       return; 
/* 189 */     if (minecraft.field_71462_r != null && !(minecraft.field_71462_r instanceof GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/* 193 */     if (minecraft.field_71462_r instanceof GuiInventory) {
/* 194 */       this.isCrafting = true;
/* 195 */       this.tickCounter = 0;
/* 196 */       this.currentActionIndex = 0;
/* 197 */       this.actionQueue.clear();
/* 198 */       prepareCraftActions();
/*     */       
/*     */       return;
/*     */     } 
/* 202 */     minecraft.func_147108_a((GuiScreen)new GuiInventory((EntityPlayer)entityPlayerSP));
/* 203 */     this.isCrafting = true;
/* 204 */     this.tickCounter = 0;
/* 205 */     this.currentActionIndex = 0;
/* 206 */     this.actionQueue.clear();
/* 207 */     this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
/*     */   }
/*     */   
/*     */   private void prepareCraftActions() {
/* 211 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 212 */     EntityPlayerSP entityPlayerSP = minecraft.field_71439_g;
/*     */     
/* 214 */     if (entityPlayerSP == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     ContainerPlayer containerPlayer = (ContainerPlayer)((GuiInventory)minecraft.field_71462_r).field_147002_h;
/*     */     
/* 220 */     RecraftRecipe recraftRecipe = findRecraftRecipe(containerPlayer);
/* 221 */     if (recraftRecipe == null) {
/* 222 */       this.isCrafting = false;
/* 223 */       setEnabled(false);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 228 */     if (recraftRecipe.recipeType == RecipeType.MUSHROOMS) {
/* 229 */       byte b1 = 3;
/* 230 */       byte b2 = 4;
/* 231 */       byte b3 = 2;
/*     */       
/* 233 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.bowlSlot, -1));
/* 234 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, b1));
/* 235 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient1Slot, -1));
/* 236 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, b2));
/* 237 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient2Slot, -1));
/* 238 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, b3));
/* 239 */     } else if (recraftRecipe.recipeType == RecipeType.COCOA || recraftRecipe.recipeType == RecipeType.CACTUS) {
/* 240 */       byte b1 = 3;
/* 241 */       byte b2 = 4;
/*     */       
/* 243 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.bowlSlot, -1));
/* 244 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, b1));
/* 245 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient1Slot, -1));
/* 246 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, b2));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RecraftRecipe findRecraftRecipe(ContainerPlayer paramContainerPlayer) {
/* 253 */     byte b1 = -1;
/* 254 */     byte b2 = -1;
/* 255 */     byte b3 = -1;
/*     */     byte b4;
/* 257 */     for (b4 = 9; b4 < 45; b4++) {
/* 258 */       Slot slot = paramContainerPlayer.func_75139_a(b4);
/* 259 */       if (slot.func_75216_d()) {
/* 260 */         ItemStack itemStack = slot.func_75211_c();
/* 261 */         if (itemStack.func_77973_b() == BOWL && b1 == -1) {
/* 262 */           b1 = b4;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     if (b1 == -1) return null;
/*     */     
/* 269 */     b4 = -1;
/* 270 */     byte b5 = -1; byte b;
/* 271 */     for (b = 9; b < 45; b++) {
/* 272 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 273 */       if (slot.func_75216_d()) {
/* 274 */         ItemStack itemStack = slot.func_75211_c();
/* 275 */         if (itemStack.func_77973_b() == RED_MUSHROOM && b4 == -1) {
/* 276 */           b4 = b;
/* 277 */         } else if (itemStack.func_77973_b() == BROWN_MUSHROOM && b5 == -1) {
/* 278 */           b5 = b;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 283 */     if (b4 != -1 && b5 != -1) {
/* 284 */       return new RecraftRecipe(RecipeType.MUSHROOMS, b1, b4, b5);
/*     */     }
/*     */     
/* 287 */     for (b = 9; b < 45; b++) {
/* 288 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 289 */       if (slot.func_75216_d()) {
/* 290 */         ItemStack itemStack = slot.func_75211_c();
/* 291 */         if (itemStack.func_77973_b() == COCOA_BEANS && itemStack.func_77960_j() == 3) {
/* 292 */           b2 = b;
/* 293 */           return new RecraftRecipe(RecipeType.COCOA, b1, b2, -1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 298 */     for (b = 9; b < 45; b++) {
/* 299 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 300 */       if (slot.func_75216_d()) {
/* 301 */         ItemStack itemStack = slot.func_75211_c();
/* 302 */         if (itemStack.func_77973_b() == CACTUS) {
/* 303 */           b2 = b;
/* 304 */           return new RecraftRecipe(RecipeType.CACTUS, b1, b2, -1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     return null;
/*     */   }
/*     */   
/*     */   private boolean executeAction(CraftAction paramCraftAction) {
/* 313 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 314 */     if (minecraft.field_71462_r == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/* 315 */       return false;
/*     */     }
/*     */     
/* 318 */     ContainerPlayer containerPlayer = (ContainerPlayer)((GuiInventory)minecraft.field_71462_r).field_147002_h;
/*     */     
/* 320 */     if (paramCraftAction.type == CraftActionType.WAIT) {
/* 321 */       return true;
/*     */     }
/*     */     
/* 324 */     if (paramCraftAction.type == CraftActionType.PICKUP) {
/* 325 */       Slot slot = containerPlayer.func_75139_a(paramCraftAction.fromSlot);
/* 326 */       if (!slot.func_75216_d()) return true;
/*     */       
/* 328 */       ItemStack itemStack = minecraft.field_71439_g.field_71071_by.func_70445_o();
/* 329 */       if (itemStack != null) return false;
/*     */       
/* 331 */       minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, paramCraftAction.fromSlot, 1, 0, (EntityPlayer)minecraft.field_71439_g);
/* 332 */       return true;
/*     */     } 
/*     */     
/* 335 */     if (paramCraftAction.type == CraftActionType.PLACE) {
/* 336 */       ItemStack itemStack = minecraft.field_71439_g.field_71071_by.func_70445_o();
/* 337 */       if (itemStack == null) return false;
/*     */       
/* 339 */       minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, paramCraftAction.toSlot, 0, 0, (EntityPlayer)minecraft.field_71439_g);
/* 340 */       return true;
/*     */     } 
/*     */     
/* 343 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\RecraftAuxModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */