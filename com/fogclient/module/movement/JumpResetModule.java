/*    */ package com.fogclient.module.movement;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import java.lang.reflect.Method;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JumpResetModule
/*    */   extends Module
/*    */ {
/*    */   private Method jumpMethod;
/*    */   private boolean hasJumped = false;
/*    */   
/*    */   public JumpResetModule() {
/* 20 */     super("PuloDoGato0KB", "Reduces knockback by jumping.", Category.MOVEMENT);
/*    */     
/*    */     try {
/* 23 */       this.jumpMethod = ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[] { "func_70664_aZ", "jump" }, new Class[0]);
/* 24 */     } catch (Exception exception) {
/* 25 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 31 */     this.hasJumped = false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 41 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*    */       return; 
/* 43 */     if (mc.field_71439_g.field_70737_aN == 0) {
/* 44 */       this.hasJumped = false;
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     if (this.hasJumped)
/*    */       return; 
/* 50 */     if (mc.field_71439_g.field_70737_aN > 0 && 
/* 51 */       mc.field_71439_g.field_70122_E)
/*    */     {
/* 53 */       if (mc.field_71474_y.field_74312_F.func_151470_d())
/*    */         try {
/* 55 */           if (this.jumpMethod != null) {
/* 56 */             this.jumpMethod.invoke(mc.field_71439_g, new Object[0]);
/* 57 */             this.hasJumped = true;
/*    */           }
/*    */         
/* 60 */         } catch (Exception exception) {
/* 61 */           exception.printStackTrace();
/*    */         }  
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\movement\JumpResetModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */