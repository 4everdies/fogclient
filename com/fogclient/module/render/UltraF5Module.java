/*    */ package com.fogclient.module.render;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import java.lang.reflect.Field;
/*    */ import net.minecraft.client.renderer.EntityRenderer;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ public class UltraF5Module
/*    */   extends Module
/*    */ {
/* 13 */   private final NumberSetting distance = new NumberSetting("DistÃ¢ncia", this, 10.0D, 2.0D, 100.0D, 1.0D);
/*    */   
/*    */   private Field thirdPersonDistanceField;
/*    */   
/*    */   private Field thirdPersonDistanceTempField;
/* 18 */   private float defaultDistance = 4.0F;
/*    */   
/*    */   public UltraF5Module() {
/* 21 */     super("UltraF5", "Aumenta a distÃ¢ncia da cÃ¢mera em terceira pessoa (F5).", Category.RENDER);
/*    */     
/*    */     try {
/* 24 */       this.thirdPersonDistanceField = ReflectionHelper.findField(EntityRenderer.class, new String[] { "field_78490_B", "thirdPersonDistance" });
/* 25 */       this.thirdPersonDistanceField.setAccessible(true);
/*    */ 
/*    */       
/* 28 */       this.thirdPersonDistanceTempField = ReflectionHelper.findField(EntityRenderer.class, new String[] { "field_78491_C", "thirdPersonDistanceTemp" });
/* 29 */       this.thirdPersonDistanceTempField.setAccessible(true);
/* 30 */     } catch (Exception exception) {
/* 31 */       exception.printStackTrace();
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
/*    */   public void onDisable() {
/* 43 */     if (mc.field_71460_t != null) {
/*    */       try {
/* 45 */         if (this.thirdPersonDistanceField != null) {
/* 46 */           this.thirdPersonDistanceField.setFloat(mc.field_71460_t, this.defaultDistance);
/*    */         }
/* 48 */         if (this.thirdPersonDistanceTempField != null) {
/* 49 */           this.thirdPersonDistanceTempField.setFloat(mc.field_71460_t, this.defaultDistance);
/*    */         }
/* 51 */       } catch (Exception exception) {
/* 52 */         exception.printStackTrace();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 59 */     if (!isEnabled())
/* 60 */       return;  if (mc.field_71460_t == null)
/*    */       return; 
/* 62 */     float f = (float)this.distance.getValue();
/*    */     
/*    */     try {
/* 65 */       if (this.thirdPersonDistanceField != null) {
/* 66 */         this.thirdPersonDistanceField.setFloat(mc.field_71460_t, f);
/*    */       }
/* 68 */       if (this.thirdPersonDistanceTempField != null) {
/* 69 */         this.thirdPersonDistanceTempField.setFloat(mc.field_71460_t, f);
/*    */       }
/* 71 */     } catch (Exception exception) {
/* 72 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\render\UltraF5Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */