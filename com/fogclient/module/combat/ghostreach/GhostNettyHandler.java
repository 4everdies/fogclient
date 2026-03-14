/*     */ package com.fogclient.module.combat.ghostreach;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.network.play.server.S14PacketEntity;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class GhostNettyHandler
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*     */   private final GhostReachCore core;
/*  20 */   private final Map<Integer, ServerPos> realServerPositions = new HashMap<>();
/*  21 */   private final Map<Integer, ServerPos> accumulatedDesync = new HashMap<>();
/*     */   private static Field s14_entityId;
/*     */   private static Field s14_posX;
/*     */   private static Field s14_posY;
/*     */   private static Field s14_posZ;
/*     */   
/*     */   static {
/*     */     try {
/*  29 */       s14_entityId = getField(S14PacketEntity.class, new String[] { "entityId", "field_149074_a" });
/*  30 */       s14_posX = getField(S14PacketEntity.class, new String[] { "posX", "field_149072_b" });
/*  31 */       s14_posY = getField(S14PacketEntity.class, new String[] { "posY", "field_149073_c" });
/*  32 */       s14_posZ = getField(S14PacketEntity.class, new String[] { "posZ", "field_149070_d" });
/*     */ 
/*     */       
/*  35 */       s18_entityId = getField(S18PacketEntityTeleport.class, new String[] { "entityId", "field_149458_a" });
/*  36 */       s18_posX = getField(S18PacketEntityTeleport.class, new String[] { "posX", "field_149456_b" });
/*  37 */       s18_posY = getField(S18PacketEntityTeleport.class, new String[] { "posY", "field_149457_c" });
/*  38 */       s18_posZ = getField(S18PacketEntityTeleport.class, new String[] { "posZ", "field_149454_d" });
/*  39 */     } catch (Exception exception) {
/*  40 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */   private static Field s18_entityId; private static Field s18_posX; private static Field s18_posY; private static Field s18_posZ;
/*     */   private static Field getField(Class<?> paramClass, String... paramVarArgs) throws NoSuchFieldException {
/*  45 */     for (String str : paramVarArgs) {
/*     */       try {
/*  47 */         Field field = paramClass.getDeclaredField(str);
/*  48 */         field.setAccessible(true);
/*  49 */         return field;
/*  50 */       } catch (NoSuchFieldException noSuchFieldException) {}
/*     */     } 
/*  52 */     throw new NoSuchFieldException("Could not find field for " + paramClass.getSimpleName());
/*     */   }
/*     */   
/*     */   public GhostNettyHandler(GhostReachCore paramGhostReachCore) {
/*  56 */     this.core = paramGhostReachCore;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception {
/*  61 */     if (paramObject instanceof net.minecraft.network.play.server.S03PacketTimeUpdate) {
/*  62 */       this.core.updateTPS();
/*     */     }
/*  64 */     else if (paramObject instanceof S14PacketEntity || paramObject instanceof S18PacketEntityTeleport) {
/*  65 */       handleEntityPacket(paramObject);
/*     */     } 
/*  67 */     super.channelRead(paramChannelHandlerContext, paramObject);
/*     */   }
/*     */   
/*     */   private void handleEntityPacket(Object paramObject) {
/*     */     try {
/*     */       int i;
/*  73 */       boolean bool1 = paramObject instanceof S18PacketEntityTeleport;
/*  74 */       boolean bool2 = paramObject instanceof S14PacketEntity;
/*     */       
/*  76 */       if (bool2) {
/*  77 */         i = s14_entityId.getInt(paramObject);
/*     */       } else {
/*  79 */         i = s18_entityId.getInt(paramObject);
/*     */       } 
/*     */       
/*  82 */       ServerPos serverPos1 = this.realServerPositions.get(Integer.valueOf(i));
/*  83 */       if (serverPos1 == null) {
/*  84 */         if (bool1) {
/*  85 */           serverPos1 = new ServerPos(0, 0, 0);
/*  86 */           this.realServerPositions.put(Integer.valueOf(i), serverPos1);
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       }
/*     */       
/*  92 */       if (bool1) {
/*  93 */         serverPos1.x = s18_posX.getInt(paramObject);
/*  94 */         serverPos1.y = s18_posY.getInt(paramObject);
/*  95 */         serverPos1.z = s18_posZ.getInt(paramObject);
/*  96 */         this.accumulatedDesync.remove(Integer.valueOf(i));
/*     */       } else {
/*  98 */         serverPos1.x += s14_posX.getByte(paramObject);
/*  99 */         serverPos1.y += s14_posY.getByte(paramObject);
/* 100 */         serverPos1.z += s14_posZ.getByte(paramObject);
/*     */       } 
/*     */       
/* 103 */       boolean bool3 = this.core.isTarget(i);
/* 104 */       ServerPos serverPos2 = this.accumulatedDesync.get(Integer.valueOf(i));
/* 105 */       Minecraft minecraft = Minecraft.func_71410_x();
/*     */       
/* 107 */       if (bool3) {
/* 108 */         if (minecraft.field_71439_g != null) {
/* 109 */           Vec3 vec3 = minecraft.field_71439_g.func_174824_e(1.0F);
/*     */           
/* 111 */           double d1 = serverPos1.x / 32.0D;
/* 112 */           double d2 = serverPos1.y / 32.0D;
/* 113 */           double d3 = serverPos1.z / 32.0D;
/*     */           
/* 115 */           double d4 = d1 - vec3.field_72450_a;
/* 116 */           double d5 = d2 - vec3.field_72448_b;
/* 117 */           double d6 = d3 - vec3.field_72449_c;
/* 118 */           double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
/*     */           
/* 120 */           if (d7 > 0.1D) {
/* 121 */             d4 /= d7; d5 /= d7; d6 /= d7;
/*     */             
/* 123 */             double d8 = 2.85D;
/* 124 */             double d9 = vec3.field_72450_a + d4 * d8;
/* 125 */             double d10 = vec3.field_72448_b + d5 * d8;
/* 126 */             double d11 = vec3.field_72449_c + d6 * d8;
/*     */             
/* 128 */             int j = MathHelper.func_76128_c(d9 * 32.0D);
/* 129 */             int k = MathHelper.func_76128_c(d10 * 32.0D);
/* 130 */             int m = MathHelper.func_76128_c(d11 * 32.0D);
/*     */             
/* 132 */             if (bool1) {
/* 133 */               s18_posX.setInt(paramObject, j);
/* 134 */               s18_posY.setInt(paramObject, k);
/* 135 */               s18_posZ.setInt(paramObject, m);
/* 136 */               this.accumulatedDesync.put(Integer.valueOf(i), new ServerPos(j - serverPos1.x, k - serverPos1.y, m - serverPos1.z));
/*     */             } else {
/* 138 */               byte b1 = s14_posX.getByte(paramObject);
/* 139 */               byte b2 = s14_posY.getByte(paramObject);
/* 140 */               byte b3 = s14_posZ.getByte(paramObject);
/*     */               
/* 142 */               byte b4 = (serverPos2 != null) ? serverPos2.x : 0;
/* 143 */               byte b5 = (serverPos2 != null) ? serverPos2.y : 0;
/* 144 */               byte b6 = (serverPos2 != null) ? serverPos2.z : 0;
/*     */               
/* 146 */               int n = j - serverPos1.x - b1 + b4;
/* 147 */               int i1 = k - serverPos1.y - b2 + b5;
/* 148 */               int i2 = m - serverPos1.z - b3 + b6;
/*     */               
/* 150 */               byte b7 = clampToByte(n);
/* 151 */               byte b8 = clampToByte(i1);
/* 152 */               byte b9 = clampToByte(i2);
/*     */               
/* 154 */               s14_posX.setByte(paramObject, b7);
/* 155 */               s14_posY.setByte(paramObject, b8);
/* 156 */               s14_posZ.setByte(paramObject, b9);
/*     */               
/* 158 */               int i3 = b7 + b4 - b1;
/* 159 */               int i4 = b8 + b5 - b2;
/* 160 */               int i5 = b9 + b6 - b3;
/*     */               
/* 162 */               this.accumulatedDesync.put(Integer.valueOf(i), new ServerPos(i3, i4, i5));
/*     */             } 
/*     */             
/* 165 */             if (GhostReachCore.debug) {
/* 166 */               minecraft.func_152344_a(() -> paramMinecraft.field_71456_v.func_146158_b().func_146227_a((IChatComponent)new ChatComponentText("Â§7[GhostReach] Â§aRelocating entity " + paramInt)));
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 173 */       else if (serverPos2 != null && 
/* 174 */         !bool1) {
/* 175 */         byte b1 = s14_posX.getByte(paramObject);
/* 176 */         byte b2 = s14_posY.getByte(paramObject);
/* 177 */         byte b3 = s14_posZ.getByte(paramObject);
/*     */         
/* 179 */         int j = b1 - serverPos2.x;
/* 180 */         int k = b2 - serverPos2.y;
/* 181 */         int m = b3 - serverPos2.z;
/*     */         
/* 183 */         byte b4 = clampToByte(j);
/* 184 */         byte b5 = clampToByte(k);
/* 185 */         byte b6 = clampToByte(m);
/*     */         
/* 187 */         s14_posX.setByte(paramObject, b4);
/* 188 */         s14_posY.setByte(paramObject, b5);
/* 189 */         s14_posZ.setByte(paramObject, b6);
/*     */         
/* 191 */         int n = serverPos2.x + b4 - b1;
/* 192 */         int i1 = serverPos2.y + b5 - b2;
/* 193 */         int i2 = serverPos2.z + b6 - b3;
/*     */         
/* 195 */         if (n == 0 && i1 == 0 && i2 == 0) {
/* 196 */           this.accumulatedDesync.remove(Integer.valueOf(i));
/*     */         } else {
/* 198 */           this.accumulatedDesync.put(Integer.valueOf(i), new ServerPos(n, i1, i2));
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 203 */     } catch (Exception exception) {
/* 204 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte clampToByte(int paramInt) {
/* 209 */     if (paramInt > 127) return Byte.MAX_VALUE; 
/* 210 */     if (paramInt < -128) return Byte.MIN_VALUE; 
/* 211 */     return (byte)paramInt;
/*     */   }
/*     */   private static class ServerPos { int x; int y;
/*     */     int z;
/*     */     
/*     */     ServerPos(int param1Int1, int param1Int2, int param1Int3) {
/* 217 */       this.x = param1Int1; this.y = param1Int2; this.z = param1Int3;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\module\combat\ghostreach\GhostNettyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */