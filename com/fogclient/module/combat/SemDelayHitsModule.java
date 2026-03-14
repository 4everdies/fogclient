/*    */ package com.fogclient.module.combat;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import java.lang.reflect.Field;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SemDelayHitsModule
/*    */   extends Module
/*    */ {
/*    */   private Field leftClickCounterField;
/*    */   
/*    */   public SemDelayHitsModule() {
/* 18 */     super("HitInfinito", "Remove o delay de hit (1.8).", Category.COMBAT);
/*    */     try {
/* 20 */       this.leftClickCounterField = ReflectionHelper.findField(Minecraft.class, new String[] { "field_71429_W", "leftClickCounter" });
/* 21 */       this.leftClickCounterField.setAccessible(true);
/* 22 */     } catch (Exception exception) {
/* 23 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 39 */     if (!isEnabled())
/*    */       return; 
/* 41 */     if (mc.field_71439_g == null)
/*    */       return; 
/*    */     try {
/* 44 */       if (this.leftClickCounterField != null) {
/* 45 */         this.leftClickCounterField.setInt(mc, 0);
/*    */       }
/* 47 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\SemDelayHitsModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */