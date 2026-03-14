/*     */ package com.fogclient.config;
/*     */ import com.fogclient.module.Module;
/*     */ import com.fogclient.module.ModuleManager;
/*     */ import com.fogclient.setting.BooleanSetting;
/*     */ import com.fogclient.setting.KeySetting;
/*     */ import com.fogclient.setting.ModeSetting;
/*     */ import com.fogclient.setting.NumberSetting;
/*     */ import com.fogclient.setting.Setting;
/*     */ import com.fogclient.setting.StringSetting;
/*     */ import com.fogclient.ui.component.Panel;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ConfigManager {
/*  21 */   private static final File CONFIG_DIR = new File(System.getProperty("user.home"), ".fogclient");
/*  22 */   private static final File CONFIG_FILE = new File(CONFIG_DIR, "config.json");
/*  23 */   private static final File GUI_CONFIG_FILE = new File(CONFIG_DIR, "gui.json");
/*  24 */   private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
/*     */ 
/*     */   
/*     */   static {
/*  28 */     if (!CONFIG_DIR.exists()) {
/*  29 */       CONFIG_DIR.mkdirs();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveConfig() {
/*     */     try {
/*  38 */       JsonObject jsonObject1 = new JsonObject();
/*     */ 
/*     */       
/*  41 */       JsonObject jsonObject2 = new JsonObject();
/*  42 */       for (Module module : ModuleManager.getInstance().getModules()) {
/*  43 */         JsonObject jsonObject3 = new JsonObject();
/*     */ 
/*     */         
/*  46 */         jsonObject3.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
/*     */ 
/*     */         
/*  49 */         jsonObject3.addProperty("keybind", Integer.valueOf(module.getKeybind()));
/*  50 */         jsonObject3.addProperty("actionKeybind", Integer.valueOf(module.getActionKeybind()));
/*     */ 
/*     */         
/*  53 */         JsonObject jsonObject4 = new JsonObject();
/*  54 */         for (Setting setting : module.getSettings()) {
/*  55 */           if (setting instanceof BooleanSetting) {
/*  56 */             jsonObject4.addProperty(setting.getName(), Boolean.valueOf(((BooleanSetting)setting).getValue())); continue;
/*  57 */           }  if (setting instanceof NumberSetting) {
/*  58 */             jsonObject4.addProperty(setting.getName(), Double.valueOf(((NumberSetting)setting).getValue())); continue;
/*  59 */           }  if (setting instanceof ModeSetting) {
/*  60 */             jsonObject4.addProperty(setting.getName(), ((ModeSetting)setting).getValue()); continue;
/*  61 */           }  if (setting instanceof StringSetting) {
/*  62 */             jsonObject4.addProperty(setting.getName(), ((StringSetting)setting).getValue()); continue;
/*  63 */           }  if (setting instanceof KeySetting) {
/*  64 */             jsonObject4.addProperty(setting.getName(), Integer.valueOf(((KeySetting)setting).getKeyCode()));
/*     */           }
/*     */         } 
/*  67 */         jsonObject3.add("settings", (JsonElement)jsonObject4);
/*     */         
/*  69 */         jsonObject2.add(module.getName(), (JsonElement)jsonObject3);
/*     */       } 
/*     */       
/*  72 */       jsonObject1.add("modules", (JsonElement)jsonObject2);
/*     */ 
/*     */       
/*  75 */       FileWriter fileWriter = new FileWriter(CONFIG_FILE);
/*  76 */       gson.toJson((JsonElement)jsonObject1, fileWriter);
/*  77 */       fileWriter.close();
/*  78 */     } catch (IOException iOException) {
/*  79 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadConfig() {
/*  87 */     if (!CONFIG_FILE.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  92 */       JsonParser jsonParser = new JsonParser();
/*  93 */       JsonObject jsonObject1 = jsonParser.parse(new FileReader(CONFIG_FILE)).getAsJsonObject();
/*     */       
/*  95 */       if (!jsonObject1.has("modules")) {
/*     */         return;
/*     */       }
/*     */       
/*  99 */       JsonObject jsonObject2 = jsonObject1.getAsJsonObject("modules");
/*     */       
/* 101 */       for (Module module : ModuleManager.getInstance().getModules()) {
/* 102 */         if (!jsonObject2.has(module.getName())) {
/*     */           continue;
/*     */         }
/*     */         
/* 106 */         JsonObject jsonObject = jsonObject2.getAsJsonObject(module.getName());
/*     */ 
/*     */         
/* 109 */         if (jsonObject.has("enabled")) {
/* 110 */           boolean bool = jsonObject.get("enabled").getAsBoolean();
/* 111 */           if (bool != module.isEnabled()) {
/* 112 */             module.setEnabled(bool);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 117 */         if (jsonObject.has("keybind")) {
/* 118 */           module.setKeybind(jsonObject.get("keybind").getAsInt());
/*     */         }
/* 120 */         if (jsonObject.has("actionKeybind")) {
/* 121 */           module.setActionKeybind(jsonObject.get("actionKeybind").getAsInt());
/*     */         }
/*     */ 
/*     */         
/* 125 */         if (jsonObject.has("settings")) {
/* 126 */           JsonObject jsonObject3 = jsonObject.getAsJsonObject("settings");
/* 127 */           for (Setting setting : module.getSettings()) {
/* 128 */             if (!jsonObject3.has(setting.getName())) {
/*     */               continue;
/*     */             }
/*     */             
/* 132 */             if (setting instanceof BooleanSetting) {
/* 133 */               ((BooleanSetting)setting).setEnabled(jsonObject3.get(setting.getName()).getAsBoolean()); continue;
/* 134 */             }  if (setting instanceof NumberSetting) {
/* 135 */               ((NumberSetting)setting).setValue(jsonObject3.get(setting.getName()).getAsDouble()); continue;
/* 136 */             }  if (setting instanceof ModeSetting) {
/* 137 */               ((ModeSetting)setting).setMode(jsonObject3.get(setting.getName()).getAsString()); continue;
/* 138 */             }  if (setting instanceof StringSetting) {
/* 139 */               ((StringSetting)setting).setValue(jsonObject3.get(setting.getName()).getAsString()); continue;
/* 140 */             }  if (setting instanceof KeySetting) {
/* 141 */               ((KeySetting)setting).setKeyCode(jsonObject3.get(setting.getName()).getAsInt());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 146 */     } catch (Exception exception) {
/* 147 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveGUIConfig(Map<String, Panel> paramMap) {
/*     */     try {
/* 156 */       JsonObject jsonObject = new JsonObject();
/*     */       
/* 158 */       for (Map.Entry<String, Panel> entry : paramMap.entrySet()) {
/* 159 */         JsonObject jsonObject1 = new JsonObject();
/* 160 */         jsonObject1.addProperty("x", Integer.valueOf(((Panel)entry.getValue()).getX()));
/* 161 */         jsonObject1.addProperty("y", Integer.valueOf(((Panel)entry.getValue()).getY()));
/* 162 */         jsonObject1.addProperty("open", Boolean.valueOf(((Panel)entry.getValue()).isOpen()));
/* 163 */         jsonObject.add((String)entry.getKey(), (JsonElement)jsonObject1);
/*     */       } 
/*     */       
/* 166 */       FileWriter fileWriter = new FileWriter(GUI_CONFIG_FILE);
/* 167 */       gson.toJson((JsonElement)jsonObject, fileWriter);
/* 168 */       fileWriter.close();
/* 169 */     } catch (IOException iOException) {
/* 170 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, PanelConfig> loadGUIConfig() {
/* 178 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/* 180 */     if (!GUI_CONFIG_FILE.exists()) {
/* 181 */       return (Map)hashMap;
/*     */     }
/*     */     
/*     */     try {
/* 185 */       JsonParser jsonParser = new JsonParser();
/* 186 */       JsonObject jsonObject = jsonParser.parse(new FileReader(GUI_CONFIG_FILE)).getAsJsonObject();
/*     */       
/* 188 */       for (Map.Entry entry : jsonObject.entrySet()) {
/* 189 */         JsonObject jsonObject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
/* 190 */         PanelConfig panelConfig = new PanelConfig();
/* 191 */         panelConfig.x = jsonObject1.get("x").getAsInt();
/* 192 */         panelConfig.y = jsonObject1.get("y").getAsInt();
/* 193 */         panelConfig.open = jsonObject1.get("open").getAsBoolean();
/* 194 */         hashMap.put(entry.getKey(), panelConfig);
/*     */       } 
/* 196 */     } catch (Exception exception) {
/* 197 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 200 */     return (Map)hashMap;
/*     */   }
/*     */   
/*     */   public static class PanelConfig {
/*     */     public int x;
/*     */     public int y;
/*     */     public boolean open;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\com\fogclient\config\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */