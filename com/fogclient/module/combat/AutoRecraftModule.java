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
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ public class AutoRecraftModule
/*     */   extends Module
/*     */ {
/*     */   private NumberSetting speedSetting;
/*     */   private static final int MIN_DELAY_TICKS = 0;
/*     */   private static final int MAX_DELAY_TICKS = 2;
/*     */   private boolean isCrafting = false;
/*  34 */   private int tickCounter = 0;
/*  35 */   private int currentDelay = 0;
/*  36 */   private List<CraftAction> actionQueue = new ArrayList<>();
/*  37 */   private int currentActionIndex = 0;
/*  38 */   private static final Random random = new Random();
/*     */ 
/*     */   
/*  41 */   private static final Item BOWL = Items.field_151054_z;
/*  42 */   private static final Item RED_MUSHROOM = Item.func_150898_a((Block)Blocks.field_150337_Q);
/*  43 */   private static final Item BROWN_MUSHROOM = Item.func_150898_a((Block)Blocks.field_150338_P);
/*  44 */   private static final Item COCOA_BEANS = Items.field_151100_aR;
/*  45 */   private static final Item CACTUS = Item.func_150898_a((Block)Blocks.field_150434_aF);
/*  46 */   private static final Item MUSHROOM_SOUP = Items.field_151009_A;
/*     */   
/*     */   private static final int CRAFT_SLOT_1 = 1;
/*     */   private static final int CRAFT_SLOT_2 = 2;
/*     */   private static final int CRAFT_SLOT_3 = 3;
/*     */   private static final int CRAFT_SLOT_4 = 4;
/*     */   private static final int RESULT_SLOT = 0;
/*     */   
/*     */   public AutoRecraftModule() {
/*  55 */     super("RecraftPerfeito", "Recrafta sopas automaticamente.", Category.COMBAT);
/*  56 */     this.keybind = 0;
/*  57 */     this.actionKeybind = 19;
/*  58 */     this.speedSetting = new NumberSetting("Speed", this, 100.0D, 1.0D, 100.0D, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAction() {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  73 */     this.isCrafting = false;
/*  74 */     this.actionQueue.clear();
/*  75 */     this.currentActionIndex = 0;
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
/*  86 */     if (isEnabled() && 
/*  87 */       !this.isCrafting) {
/*  88 */       handleAutoRecraft();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpeed() {
/*  94 */     return (int)this.speedSetting.getValue();
/*     */   }
/*     */   
/*     */   public void onTick() {
/*     */     int k;
/*  99 */     if (!this.isCrafting) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 104 */     if (minecraft.field_71462_r == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/* 105 */       this.isCrafting = false;
/* 106 */       this.actionQueue.clear();
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     int i = getSpeed();
/* 111 */     if (i < 0) i = 0; 
/* 112 */     if (i > 100) i = 100;
/*     */     
/* 114 */     int j = 75 + i * 25 / 100;
/* 115 */     if (j < 0) j = 0; 
/* 116 */     if (j > 100) j = 100;
/*     */ 
/*     */     
/* 119 */     if (j >= 100) {
/* 120 */       k = 5;
/*     */     } else {
/* 122 */       k = 1 + j * 3 / 100;
/* 123 */       if (k < 1) k = 1; 
/* 124 */       if (k > 4) k = 4;
/*     */     
/*     */     } 
/* 127 */     byte b1 = 20;
/* 128 */     int m = b1 * (100 - j) / 100;
/* 129 */     if (m < 0) m = 0;
/*     */     
/* 131 */     if (m > 0) {
/* 132 */       this.tickCounter++;
/* 133 */       if (this.tickCounter <= m) {
/*     */         return;
/*     */       }
/* 136 */       this.tickCounter = 0;
/*     */     } 
/*     */     
/* 139 */     byte b2 = 0;
/*     */     
/* 141 */     while (this.currentActionIndex < this.actionQueue.size() && b2 < k) {
/* 142 */       CraftAction craftAction = this.actionQueue.get(this.currentActionIndex);
/*     */       
/* 144 */       if (this.currentActionIndex == 0 && craftAction.type == CraftActionType.WAIT && this.actionQueue.size() == 1) {
/* 145 */         if (executeAction(craftAction)) {
/* 146 */           this.currentActionIndex++;
/* 147 */           prepareCraftActions();
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 152 */       if (craftAction.type == CraftActionType.PICKUP && this.currentActionIndex + 1 < this.actionQueue.size()) {
/* 153 */         CraftAction craftAction1 = this.actionQueue.get(this.currentActionIndex + 1);
/* 154 */         if (craftAction1.type == CraftActionType.PLACE) {
/* 155 */           if (executeAction(craftAction)) {
/* 156 */             this.currentActionIndex++;
/* 157 */             b2++;
/* 158 */             if (executeAction(craftAction1)) {
/* 159 */               this.currentActionIndex++;
/* 160 */               b2++;
/*     */             } 
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 166 */         if (executeAction(craftAction)) {
/* 167 */           this.currentActionIndex++;
/* 168 */           b2++;
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 174 */       if (executeAction(craftAction)) {
/* 175 */         this.currentActionIndex++;
/* 176 */         b2++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (this.currentActionIndex >= this.actionQueue.size()) {
/* 184 */       this.isCrafting = false;
/* 185 */       this.actionQueue.clear();
/* 186 */       this.currentActionIndex = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleAutoRecraft() {
/* 192 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 193 */     EntityPlayerSP entityPlayerSP = minecraft.field_71439_g;
/*     */     
/* 195 */     if (entityPlayerSP == null)
/*     */       return; 
/* 197 */     if (minecraft.field_71462_r != null && !(minecraft.field_71462_r instanceof GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/* 201 */     if (minecraft.field_71462_r instanceof GuiInventory) {
/* 202 */       this.isCrafting = true;
/* 203 */       this.tickCounter = 0;
/* 204 */       this.currentActionIndex = 0;
/* 205 */       this.actionQueue.clear();
/* 206 */       prepareCraftActions();
/*     */       
/*     */       return;
/*     */     } 
/* 210 */     minecraft.func_147108_a((GuiScreen)new GuiInventory((EntityPlayer)entityPlayerSP));
/* 211 */     this.isCrafting = true;
/* 212 */     this.tickCounter = 0;
/* 213 */     this.currentActionIndex = 0;
/* 214 */     this.actionQueue.clear();
/* 215 */     this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
/*     */   }
/*     */   
/*     */   private void prepareCraftActions() {
/* 219 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 220 */     EntityPlayerSP entityPlayerSP = minecraft.field_71439_g;
/*     */     
/* 222 */     if (entityPlayerSP == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/* 226 */     ContainerPlayer containerPlayer = (ContainerPlayer)((GuiInventory)minecraft.field_71462_r).field_147002_h;
/*     */     
/* 228 */     RecraftRecipe recraftRecipe = findRecraftRecipe(containerPlayer);
/* 229 */     if (recraftRecipe == null) {
/* 230 */       this.isCrafting = false;
/* 231 */       setEnabled(false);
/*     */       
/*     */       return;
/*     */     } 
/* 235 */     ArrayList<Integer> arrayList = new ArrayList();
/* 236 */     arrayList.add(Integer.valueOf(1));
/* 237 */     arrayList.add(Integer.valueOf(2));
/* 238 */     arrayList.add(Integer.valueOf(3));
/* 239 */     arrayList.add(Integer.valueOf(4));
/* 240 */     Collections.shuffle(arrayList, random);
/*     */     
/* 242 */     if (recraftRecipe.recipeType == RecipeType.MUSHROOMS) {
/* 243 */       int i = ((Integer)arrayList.get(0)).intValue();
/* 244 */       int j = ((Integer)arrayList.get(1)).intValue();
/* 245 */       int k = ((Integer)arrayList.get(2)).intValue();
/*     */       
/* 247 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.bowlSlot, -1));
/* 248 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, i));
/* 249 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient1Slot, -1));
/* 250 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, j));
/* 251 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient2Slot, -1));
/* 252 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, k));
/* 253 */     } else if (recraftRecipe.recipeType == RecipeType.COCOA || recraftRecipe.recipeType == RecipeType.CACTUS) {
/* 254 */       int i = ((Integer)arrayList.get(0)).intValue();
/* 255 */       int j = ((Integer)arrayList.get(1)).intValue();
/*     */       
/* 257 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.bowlSlot, -1));
/* 258 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, i));
/* 259 */       this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, recraftRecipe.ingredient1Slot, -1));
/* 260 */       this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, j));
/*     */     } 
/*     */     
/* 263 */     this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
/* 264 */     this.actionQueue.add(new CraftAction(CraftActionType.SHIFT_CLICK_RESULT, 0, -1));
/* 265 */     this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
/* 266 */     this.actionQueue.add(new CraftAction(CraftActionType.PULL_REMAINING_ITEMS, -1, -1));
/* 267 */     this.actionQueue.add(new CraftAction(CraftActionType.CLOSE_INVENTORY, -1, -1));
/*     */   }
/*     */   
/*     */   private RecraftRecipe findRecraftRecipe(ContainerPlayer paramContainerPlayer) {
/* 271 */     byte b1 = -1;
/* 272 */     byte b2 = -1;
/* 273 */     byte b3 = -1;
/*     */     byte b4;
/* 275 */     for (b4 = 9; b4 < 45; b4++) {
/* 276 */       Slot slot = paramContainerPlayer.func_75139_a(b4);
/* 277 */       if (slot.func_75216_d()) {
/* 278 */         ItemStack itemStack = slot.func_75211_c();
/* 279 */         if (itemStack.func_77973_b() == BOWL && b1 == -1) {
/* 280 */           b1 = b4;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     if (b1 == -1) return null;
/*     */     
/* 287 */     b4 = -1;
/* 288 */     byte b5 = -1; byte b;
/* 289 */     for (b = 9; b < 45; b++) {
/* 290 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 291 */       if (slot.func_75216_d()) {
/* 292 */         ItemStack itemStack = slot.func_75211_c();
/* 293 */         if (itemStack.func_77973_b() == RED_MUSHROOM && b4 == -1) {
/* 294 */           b4 = b;
/* 295 */         } else if (itemStack.func_77973_b() == BROWN_MUSHROOM && b5 == -1) {
/* 296 */           b5 = b;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 301 */     if (b4 != -1 && b5 != -1) {
/* 302 */       return new RecraftRecipe(RecipeType.MUSHROOMS, b1, b4, b5);
/*     */     }
/*     */     
/* 305 */     for (b = 9; b < 45; b++) {
/* 306 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 307 */       if (slot.func_75216_d()) {
/* 308 */         ItemStack itemStack = slot.func_75211_c();
/* 309 */         if (itemStack.func_77973_b() == COCOA_BEANS && itemStack.func_77960_j() == 3) {
/* 310 */           b2 = b;
/* 311 */           return new RecraftRecipe(RecipeType.COCOA, b1, b2, -1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 316 */     for (b = 9; b < 45; b++) {
/* 317 */       Slot slot = paramContainerPlayer.func_75139_a(b);
/* 318 */       if (slot.func_75216_d()) {
/* 319 */         ItemStack itemStack = slot.func_75211_c();
/* 320 */         if (itemStack.func_77973_b() == CACTUS) {
/* 321 */           b2 = b;
/* 322 */           return new RecraftRecipe(RecipeType.CACTUS, b1, b2, -1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     return null;
/*     */   }
/*     */   
/*     */   private boolean executeAction(CraftAction paramCraftAction) {
/* 331 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 332 */     if (minecraft.field_71462_r == null || !(minecraft.field_71462_r instanceof GuiInventory)) {
/* 333 */       return false;
/*     */     }
/*     */     
/* 336 */     ContainerPlayer containerPlayer = (ContainerPlayer)((GuiInventory)minecraft.field_71462_r).field_147002_h;
/*     */     
/* 338 */     if (paramCraftAction.type == CraftActionType.WAIT) {
/* 339 */       return true;
/*     */     }
/*     */     
/* 342 */     if (paramCraftAction.type == CraftActionType.SHIFT_CLICK_RESULT) {
/* 343 */       Slot slot = containerPlayer.func_75139_a(0);
/* 344 */       if (slot.func_75216_d()) {
/* 345 */         ItemStack itemStack = slot.func_75211_c();
/* 346 */         if (itemStack.func_77973_b() == MUSHROOM_SOUP) {
/* 347 */           minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, 0, 0, 1, (EntityPlayer)minecraft.field_71439_g);
/* 348 */           return true;
/*     */         } 
/*     */       } 
/* 351 */       return false;
/*     */     } 
/*     */     
/* 354 */     if (paramCraftAction.type == CraftActionType.PULL_REMAINING_ITEMS) {
/* 355 */       boolean bool = false;
/* 356 */       for (byte b = 1; b <= 4; b++) {
/* 357 */         Slot slot = containerPlayer.func_75139_a(b);
/* 358 */         if (slot.func_75216_d()) {
/* 359 */           bool = true;
/* 360 */           minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, b, 0, 1, (EntityPlayer)minecraft.field_71439_g);
/*     */           break;
/*     */         } 
/*     */       } 
/* 364 */       return !bool;
/*     */     } 
/*     */     
/* 367 */     if (paramCraftAction.type == CraftActionType.CLOSE_INVENTORY) {
/* 368 */       minecraft.func_147108_a(null);
/* 369 */       this.isCrafting = false;
/* 370 */       return true;
/*     */     } 
/*     */     
/* 373 */     if (paramCraftAction.type == CraftActionType.PICKUP) {
/* 374 */       Slot slot = containerPlayer.func_75139_a(paramCraftAction.fromSlot);
/* 375 */       if (!slot.func_75216_d()) return true;
/*     */       
/* 377 */       ItemStack itemStack = minecraft.field_71439_g.field_71071_by.func_70445_o();
/* 378 */       if (itemStack != null) return false;
/*     */       
/* 380 */       minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, paramCraftAction.fromSlot, 1, 0, (EntityPlayer)minecraft.field_71439_g);
/* 381 */       return true;
/*     */     } 
/*     */     
/* 384 */     if (paramCraftAction.type == CraftActionType.PLACE) {
/* 385 */       ItemStack itemStack = minecraft.field_71439_g.field_71071_by.func_70445_o();
/* 386 */       if (itemStack == null) return false;
/*     */       
/* 388 */       minecraft.field_71442_b.func_78753_a(containerPlayer.field_75152_c, paramCraftAction.toSlot, 0, 0, (EntityPlayer)minecraft.field_71439_g);
/* 389 */       return true;
/*     */     } 
/*     */     
/* 392 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\AutoRecraftModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */