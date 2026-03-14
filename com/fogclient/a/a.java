/*     */ package com.fogclient.a;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class a
/*     */   extends GuiScreen
/*     */ {
/*     */   private final a reason;
/*     */   
/*     */   public enum a
/*     */   {
/*  17 */     ACESSO_NEGADO,
/*  18 */     FALHA_CONEXAO,
/*  19 */     HWID_DIFERENTE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public a(a parama) {
/*  25 */     this.reason = parama;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  30 */     this.field_146292_n.clear();
/*     */     
/*  32 */     int i = this.field_146294_l / 2;
/*  33 */     int j = this.field_146295_m / 2;
/*     */     
/*  35 */     this.field_146292_n.add(new GuiButton(0, i - 60, j + 70, 120, 20, "Fechar"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int paramInt1, int paramInt2, float paramFloat) {
/*  40 */     func_146276_q_();
/*     */     
/*  42 */     int i = this.field_146294_l / 2;
/*  43 */     int j = this.field_146295_m / 2;
/*     */ 
/*     */     
/*  46 */     func_73732_a(this.field_146289_q, "FogClient - Acesso Bloqueado", i, j - 90, 16729156);
/*     */ 
/*     */     
/*  49 */     switch (this.reason) {
/*     */       case ACESSO_NEGADO:
/*  51 */         func_73732_a(this.field_146289_q, "Acesso Negado.", i, j - 55, 16737894);
/*     */         
/*  53 */         func_73732_a(this.field_146289_q, "Seu e-mail nao foi encontrado ou ja esta vinculado", i, j - 35, 13421772);
/*     */ 
/*     */         
/*  56 */         func_73732_a(this.field_146289_q, "a outra maquina.", i, j - 20, 13421772);
/*     */         
/*  58 */         func_73732_a(this.field_146289_q, "Adquira o FogClient em: fogclient.xyz", i, j + 5, 3066993);
/*     */ 
/*     */         
/*  61 */         func_73732_a(this.field_146289_q, "Suporte: discord.gg/aX4hWcJUcw", i, j + 25, 7506394);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case FALHA_CONEXAO:
/*  67 */         func_73732_a(this.field_146289_q, "Falha de Conexao.", i, j - 55, 16737894);
/*     */         
/*  69 */         func_73732_a(this.field_146289_q, "Nao foi possivel conectar ao servidor de licencas.", i, j - 35, 13421772);
/*     */ 
/*     */         
/*  72 */         func_73732_a(this.field_146289_q, "Verifique sua internet e tente novamente.", i, j - 15, 13421772);
/*     */ 
/*     */         
/*  75 */         func_73732_a(this.field_146289_q, "Suporte: discord.gg/aX4hWcJUcw", i, j + 10, 7506394);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case HWID_DIFERENTE:
/*  81 */         func_73732_a(this.field_146289_q, "Maquina nao Autorizada.", i, j - 55, 16737894);
/*     */         
/*  83 */         func_73732_a(this.field_146289_q, "Este e-mail ja esta vinculado a outra maquina.", i, j - 35, 13421772);
/*     */ 
/*     */         
/*  86 */         func_73732_a(this.field_146289_q, "Para transferir a licenca, contate o suporte.", i, j - 15, 13421772);
/*     */ 
/*     */         
/*  89 */         func_73732_a(this.field_146289_q, "Suporte: discord.gg/aX4hWcJUcw", i, j + 10, 7506394);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  95 */     super.func_73863_a(paramInt1, paramInt2, paramFloat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char paramChar, int paramInt) throws IOException {
/* 101 */     if (paramInt == 1) {
/*     */       return;
/*     */     }
/* 104 */     super.func_73869_a(paramChar, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton paramGuiButton) throws IOException {
/* 109 */     if (paramGuiButton.field_146127_k == 0)
/*     */     {
/* 111 */       Runtime.getRuntime().halt(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_73868_f() {
/* 117 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\a\a.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */