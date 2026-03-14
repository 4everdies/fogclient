/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.injection.InjectionPoint;
/*     */ import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionPointException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
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
/*     */ 
/*     */ public class InjectionPointData
/*     */ {
/*  55 */   private static final Pattern AT_PATTERN = createPattern();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private final Map<String, String> args = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMixinContext context;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MethodNode method;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotationNode parent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String at;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String type;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InjectionPoint.Selector selector;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String target;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String slice;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int ordinal;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int opcode;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String id;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPointData(IMixinContext paramIMixinContext, MethodNode paramMethodNode, AnnotationNode paramAnnotationNode, String paramString1, List<String> paramList, String paramString2, String paramString3, int paramInt1, int paramInt2, String paramString4) {
/* 119 */     this.context = paramIMixinContext;
/* 120 */     this.method = paramMethodNode;
/* 121 */     this.parent = paramAnnotationNode;
/* 122 */     this.at = paramString1;
/* 123 */     this.target = paramString2;
/* 124 */     this.slice = Strings.nullToEmpty(paramString3);
/* 125 */     this.ordinal = Math.max(-1, paramInt1);
/* 126 */     this.opcode = paramInt2;
/* 127 */     this.id = paramString4;
/*     */     
/* 129 */     parseArgs(paramList);
/*     */     
/* 131 */     this.args.put("target", paramString2);
/* 132 */     this.args.put("ordinal", String.valueOf(paramInt1));
/* 133 */     this.args.put("opcode", String.valueOf(paramInt2));
/*     */     
/* 135 */     Matcher matcher = AT_PATTERN.matcher(paramString1);
/* 136 */     this.type = parseType(matcher, paramString1);
/* 137 */     this.selector = parseSelector(matcher);
/*     */   }
/*     */   
/*     */   private void parseArgs(List<String> paramList) {
/* 141 */     if (paramList == null) {
/*     */       return;
/*     */     }
/* 144 */     for (String str : paramList) {
/* 145 */       if (str != null) {
/* 146 */         int i = str.indexOf('=');
/* 147 */         if (i > -1) {
/* 148 */           this.args.put(str.substring(0, i), str.substring(i + 1)); continue;
/*     */         } 
/* 150 */         this.args.put(str, "");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAt() {
/* 160 */     return this.at;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 167 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPoint.Selector getSelector() {
/* 174 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMixinContext getContext() {
/* 181 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodNode getMethod() {
/* 188 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getMethodReturnType() {
/* 195 */     return Type.getReturnType(this.method.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationNode getParent() {
/* 202 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSlice() {
/* 209 */     return this.slice;
/*     */   }
/*     */   
/*     */   public LocalVariableDiscriminator getLocalVariableDiscriminator() {
/* 213 */     return LocalVariableDiscriminator.parse(this.parent);
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
/*     */   public String get(String paramString1, String paramString2) {
/* 225 */     String str = this.args.get(paramString1);
/* 226 */     return (str != null) ? str : paramString2;
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
/*     */   public int get(String paramString, int paramInt) {
/* 238 */     return parseInt(get(paramString, String.valueOf(paramInt)), paramInt);
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
/*     */   public boolean get(String paramString, boolean paramBoolean) {
/* 250 */     return parseBoolean(get(paramString, String.valueOf(paramBoolean)), paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo get(String paramString) {
/*     */     try {
/* 262 */       return MemberInfo.parseAndValidate(get(paramString, ""), this.context);
/* 263 */     } catch (InvalidMemberDescriptorException invalidMemberDescriptorException) {
/* 264 */       throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\").%s descriptor \"%s\" on %s", new Object[] { this.at, paramString, this.target, 
/* 265 */             InjectionInfo.describeInjector(this.context, this.parent, this.method) });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberInfo getTarget() {
/*     */     try {
/* 274 */       return MemberInfo.parseAndValidate(this.target, this.context);
/* 275 */     } catch (InvalidMemberDescriptorException invalidMemberDescriptorException) {
/* 276 */       throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\") descriptor \"%s\" on %s", new Object[] { this.at, this.target, 
/* 277 */             InjectionInfo.describeInjector(this.context, this.parent, this.method) });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrdinal() {
/* 285 */     return this.ordinal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpcode() {
/* 292 */     return this.opcode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpcode(int paramInt) {
/* 303 */     return (this.opcode > 0) ? this.opcode : paramInt;
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
/*     */   
/*     */   public int getOpcode(int paramInt, int... paramVarArgs) {
/* 316 */     for (int i : paramVarArgs) {
/* 317 */       if (this.opcode == i) {
/* 318 */         return this.opcode;
/*     */       }
/*     */     } 
/* 321 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 328 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 333 */     return this.type;
/*     */   }
/*     */   
/*     */   private static Pattern createPattern() {
/* 337 */     return Pattern.compile(String.format("^([^:]+):?(%s)?$", new Object[] { Joiner.on('|').join((Object[])InjectionPoint.Selector.values()) }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String parseType(String paramString) {
/* 347 */     Matcher matcher = AT_PATTERN.matcher(paramString);
/* 348 */     return parseType(matcher, paramString);
/*     */   }
/*     */   
/*     */   private static String parseType(Matcher paramMatcher, String paramString) {
/* 352 */     return paramMatcher.matches() ? paramMatcher.group(1) : paramString;
/*     */   }
/*     */   
/*     */   private static InjectionPoint.Selector parseSelector(Matcher paramMatcher) {
/* 356 */     return (paramMatcher.matches() && paramMatcher.group(2) != null) ? InjectionPoint.Selector.valueOf(paramMatcher.group(2)) : InjectionPoint.Selector.DEFAULT;
/*     */   }
/*     */   
/*     */   private static int parseInt(String paramString, int paramInt) {
/*     */     try {
/* 361 */       return Integer.parseInt(paramString);
/* 362 */     } catch (Exception exception) {
/* 363 */       return paramInt;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean parseBoolean(String paramString, boolean paramBoolean) {
/*     */     try {
/* 369 */       return Boolean.parseBoolean(paramString);
/* 370 */     } catch (Exception exception) {
/* 371 */       return paramBoolean;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\InjectionPointData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */