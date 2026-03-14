/*    */ package org.spongepowered.asm.service.mojang;
/*    */ 
/*    */ import net.minecraft.launchwrapper.Launch;
/*    */ import org.spongepowered.asm.service.IGlobalPropertyService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Blackboard
/*    */   implements IGlobalPropertyService
/*    */ {
/*    */   public final <T> T getProperty(String paramString) {
/* 46 */     return (T)Launch.blackboard.get(paramString);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setProperty(String paramString, Object paramObject) {
/* 57 */     Launch.blackboard.put(paramString, paramObject);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final <T> T getProperty(String paramString, T paramT) {
/* 72 */     Object object = Launch.blackboard.get(paramString);
/* 73 */     return (object != null) ? (T)object : paramT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getPropertyString(String paramString1, String paramString2) {
/* 87 */     Object object = Launch.blackboard.get(paramString1);
/* 88 */     return (object != null) ? object.toString() : paramString2;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\service\mojang\Blackboard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */