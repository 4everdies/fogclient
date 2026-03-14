/*    */ package com.fogclient.module.misc;
/*    */ 
/*    */ import com.fogclient.module.Category;
/*    */ import com.fogclient.module.Module;
/*    */ import com.fogclient.setting.NumberSetting;
/*    */ import com.fogclient.setting.StringSetting;
/*    */ import java.time.LocalTime;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class LembreteModule
/*    */   extends Module {
/* 13 */   private final NumberSetting hourSetting = new NumberSetting("Hora (0-23)", this, 12.0D, 0.0D, 23.0D, 1.0D);
/* 14 */   private final NumberSetting minuteSetting = new NumberSetting("Minuto (0-59)", this, 0.0D, 0.0D, 59.0D, 1.0D);
/* 15 */   private final StringSetting messageSetting = new StringSetting("Mensagem", this, "Hora do Lembrete!");
/*    */   
/*    */   private boolean alarming = false;
/* 18 */   private long alarmStartTime = 0L;
/* 19 */   private long lastMessageTime = 0L;
/*    */   
/*    */   public LembreteModule() {
/* 22 */     super("Despertador", "Cria um lembrete com hora marcada.", Category.MISC);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 27 */     this.alarming = false;
/* 28 */     this.alarmStartTime = 0L;
/* 29 */     this.lastMessageTime = 0L;
/* 30 */     if (mc.field_71439_g != null) {
/* 31 */       mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§a[Lembrete] Â§fMÃ³dulo ativado. O lembrete tocarÃ¡ Ã s " + 
/* 32 */             String.format("%02d:%02d", new Object[] { Integer.valueOf((int)this.hourSetting.getValue()), Integer.valueOf((int)this.minuteSetting.getValue()) })));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 38 */     this.alarming = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 43 */     if (!isEnabled())
/* 44 */       return;  if (mc.field_71439_g == null || mc.field_71441_e == null)
/*    */       return; 
/* 46 */     LocalTime localTime = LocalTime.now();
/* 47 */     int i = localTime.getHour();
/* 48 */     int j = localTime.getMinute();
/* 49 */     int k = (int)this.hourSetting.getValue();
/* 50 */     int m = (int)this.minuteSetting.getValue();
/*    */ 
/*    */     
/* 53 */     if (!this.alarming && 
/* 54 */       i == k && j == m) {
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
/* 65 */       this.alarming = true;
/* 66 */       this.alarmStartTime = System.currentTimeMillis();
/* 67 */       this.lastMessageTime = 0L;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 72 */     if (this.alarming) {
/* 73 */       long l = System.currentTimeMillis();
/*    */ 
/*    */       
/* 76 */       if (l - this.alarmStartTime > 60000L) {
/* 77 */         this.alarming = false;
/*    */ 
/*    */ 
/*    */         
/*    */         return;
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 86 */       if (l - this.lastMessageTime >= 5000L) {
/* 87 */         mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Â§e[LEMBRETE] Â§f" + this.messageSetting.getValue()));
/* 88 */         mc.field_71439_g.func_85030_a("random.orb", 1.0F, 1.0F);
/* 89 */         this.lastMessageTime = l;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\misc\LembreteModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */