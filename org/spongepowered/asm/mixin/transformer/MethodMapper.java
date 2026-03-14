/*     */ package org.spongepowered.asm.mixin.transformer;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
/*     */ import org.spongepowered.asm.util.Counter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodMapper
/*     */ {
/*  51 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */   
/*  53 */   private static final List<String> classes = new ArrayList<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final Map<String, Counter> methods = new HashMap<String, Counter>();
/*     */   
/*     */   private final ClassInfo info;
/*     */   
/*     */   public MethodMapper(MixinEnvironment paramMixinEnvironment, ClassInfo paramClassInfo) {
/*  64 */     this.info = paramClassInfo;
/*     */   }
/*     */   
/*     */   public ClassInfo getClassInfo() {
/*  68 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remapHandlerMethod(MixinInfo paramMixinInfo, MethodNode paramMethodNode, ClassInfo.Method paramMethod) {
/*  79 */     if (!(paramMethodNode instanceof MixinInfo.MixinMethodNode) || !((MixinInfo.MixinMethodNode)paramMethodNode).isInjector()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     if (paramMethod.isUnique()) {
/*  84 */       logger.warn("Redundant @Unique on injector method {} in {}. Injectors are implicitly unique", new Object[] { paramMethod, paramMixinInfo });
/*     */     }
/*     */     
/*  87 */     if (paramMethod.isRenamed()) {
/*  88 */       paramMethodNode.name = paramMethod.getName();
/*     */       
/*     */       return;
/*     */     } 
/*  92 */     String str = getHandlerName((MixinInfo.MixinMethodNode)paramMethodNode);
/*  93 */     paramMethodNode.name = paramMethod.renameTo(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHandlerName(MixinInfo.MixinMethodNode paramMixinMethodNode) {
/* 103 */     String str1 = InjectionInfo.getInjectorPrefix(paramMixinMethodNode.getInjectorAnnotation());
/* 104 */     String str2 = getClassUID(paramMixinMethodNode.getOwner().getClassRef());
/* 105 */     String str3 = getMethodUID(paramMixinMethodNode.name, paramMixinMethodNode.desc, !paramMixinMethodNode.isSurrogate());
/* 106 */     return String.format("%s$%s$%s%s", new Object[] { str1, paramMixinMethodNode.name, str2, str3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getClassUID(String paramString) {
/* 116 */     int i = classes.indexOf(paramString);
/* 117 */     if (i < 0) {
/* 118 */       i = classes.size();
/* 119 */       classes.add(paramString);
/*     */     } 
/* 121 */     return finagle(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getMethodUID(String paramString1, String paramString2, boolean paramBoolean) {
/* 133 */     String str = String.format("%s%s", new Object[] { paramString1, paramString2 });
/* 134 */     Counter counter = methods.get(str);
/* 135 */     if (counter == null) {
/* 136 */       counter = new Counter();
/* 137 */       methods.put(str, counter);
/* 138 */     } else if (paramBoolean) {
/* 139 */       counter.value++;
/*     */     } 
/* 141 */     return String.format("%03x", new Object[] { Integer.valueOf(counter.value) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String finagle(int paramInt) {
/* 151 */     String str = Integer.toHexString(paramInt);
/* 152 */     StringBuilder stringBuilder = new StringBuilder();
/* 153 */     for (byte b = 0; b < str.length(); b++) {
/* 154 */       char c = str.charAt(b);
/* 155 */       stringBuilder.append(c = (char)(c + ((c < ':') ? 49 : 10)));
/*     */     } 
/* 157 */     return Strings.padStart(stringBuilder.toString(), 3, 'z');
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\MethodMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */