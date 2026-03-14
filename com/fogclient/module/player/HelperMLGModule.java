/*    */ package com.fogclient.module.player;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import java.lang.reflect.Method;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HelperMLGModule
/*    */   extends Module
/*    */ {
/*    */   private Method rightClickMethod;
/*    */   private boolean executing = false;
/*    */   
/*    */   public HelperMLGModule() {
/* 22 */     super("HelperMLG", "Place water automatically when falling.", Category.PLAYER);
/*    */     
/*    */     try {
/* 25 */       this.rightClickMethod = ReflectionHelper.findMethod(Minecraft.class, mc, new String[] { "func_147121_ag", "rightClickMouse" }, new Class[0]);
/* 26 */     } catch (Exception exception) {
/* 27 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 34 */     this.executing = false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 44 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/* 45 */       return;  if (!isEnabled()) {
/*    */       return;
/*    */     }
/* 48 */     if (mc.field_71439_g.func_70694_bm() != null && mc.field_71439_g
/* 49 */       .func_70694_bm().func_77973_b() == Items.field_151131_as && mc.field_71439_g.field_70143_R > 1.5F) {
/*    */ 
/*    */       
/* 52 */       MovingObjectPosition movingObjectPosition = mc.field_71476_x;
/*    */       
/* 54 */       if (movingObjectPosition != null && movingObjectPosition.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
/* 55 */         double d = mc.field_71439_g.field_70163_u - (movingObjectPosition.func_178782_a().func_177956_o() + 1);
/*    */ 
/*    */         
/* 58 */         if (d > 0.0D && d < 2.0D && 
/* 59 */           !this.executing) {
/* 60 */           forceClick();
/* 61 */           this.executing = true;
/*    */         }
/*    */       
/*    */       } 
/* 65 */     } else if (mc.field_71439_g.field_70122_E) {
/* 66 */       this.executing = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void forceClick() {
/*    */     try {
/* 72 */       if (this.rightClickMethod != null) {
/* 73 */         this.rightClickMethod.invoke(mc, new Object[0]);
/*    */       } else {
/*    */         
/* 76 */         int i = mc.field_71474_y.field_74313_G.func_151463_i();
/* 77 */         KeyBinding.func_74507_a(i);
/*    */       } 
/* 79 */     } catch (Exception exception) {
/* 80 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\player\HelperMLGModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */