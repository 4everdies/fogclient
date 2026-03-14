/*     */ package org.spongepowered.asm.mixin.gen;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.spongepowered.asm.lib.Type;
/*     */ import org.spongepowered.asm.lib.tree.FieldNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
/*     */ import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
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
/*     */ public class AccessorInfo
/*     */   extends SpecialMethodInfo
/*     */ {
/*     */   public enum AccessorType
/*     */   {
/*  63 */     FIELD_GETTER((String)ImmutableSet.of("get", "is"))
/*     */     {
/*     */       AccessorGenerator getGenerator(AccessorInfo param2AccessorInfo) {
/*  66 */         return new AccessorGeneratorFieldGetter(param2AccessorInfo);
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     FIELD_SETTER((String)ImmutableSet.of("set"))
/*     */     {
/*     */       AccessorGenerator getGenerator(AccessorInfo param2AccessorInfo) {
/*  77 */         return new AccessorGeneratorFieldSetter(param2AccessorInfo);
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     METHOD_PROXY((String)ImmutableSet.of("call", "invoke"))
/*     */     {
/*     */       AccessorGenerator getGenerator(AccessorInfo param2AccessorInfo) {
/*  87 */         return new AccessorGeneratorMethodProxy(param2AccessorInfo);
/*     */       }
/*     */     };
/*     */     
/*     */     private final Set<String> expectedPrefixes;
/*     */     
/*     */     AccessorType(Set<String> param1Set) {
/*  94 */       this.expectedPrefixes = param1Set;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isExpectedPrefix(String param1String) {
/* 105 */       return this.expectedPrefixes.contains(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getExpectedPrefixes() {
/* 116 */       return this.expectedPrefixes.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract AccessorGenerator getGenerator(AccessorInfo param1AccessorInfo);
/*     */   }
/*     */ 
/*     */   
/* 125 */   protected static final Pattern PATTERN_ACCESSOR = Pattern.compile("^(get|set|is|invoke|call)(([A-Z])(.*?))(_\\$md.*)?$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Type[] argTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Type returnType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AccessorType type;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Type targetFieldType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MemberInfo target;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FieldNode targetField;
/*     */ 
/*     */ 
/*     */   
/*     */   protected MethodNode targetMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AccessorInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode) {
/* 165 */     this(paramMixinTargetContext, paramMethodNode, (Class)Accessor.class);
/*     */   }
/*     */   
/*     */   protected AccessorInfo(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, Class<? extends Annotation> paramClass) {
/* 169 */     super(paramMixinTargetContext, paramMethodNode, Annotations.getVisible(paramMethodNode, paramClass));
/* 170 */     this.argTypes = Type.getArgumentTypes(paramMethodNode.desc);
/* 171 */     this.returnType = Type.getReturnType(paramMethodNode.desc);
/* 172 */     this.type = initType();
/* 173 */     this.targetFieldType = initTargetFieldType();
/* 174 */     this.target = initTarget();
/*     */   }
/*     */   
/*     */   protected AccessorType initType() {
/* 178 */     if (this.returnType.equals(Type.VOID_TYPE)) {
/* 179 */       return AccessorType.FIELD_SETTER;
/*     */     }
/* 181 */     return AccessorType.FIELD_GETTER;
/*     */   }
/*     */   
/*     */   protected Type initTargetFieldType() {
/* 185 */     switch (this.type) {
/*     */       case FIELD_GETTER:
/* 187 */         if (this.argTypes.length > 0) {
/* 188 */           throw new InvalidAccessorException(this.mixin, this + " must take exactly 0 arguments, found " + this.argTypes.length);
/*     */         }
/* 190 */         return this.returnType;
/*     */       
/*     */       case FIELD_SETTER:
/* 193 */         if (this.argTypes.length != 1) {
/* 194 */           throw new InvalidAccessorException(this.mixin, this + " must take exactly 1 argument, found " + this.argTypes.length);
/*     */         }
/* 196 */         return this.argTypes[0];
/*     */     } 
/*     */     
/* 199 */     throw new InvalidAccessorException(this.mixin, "Computed unsupported accessor type " + this.type + " for " + this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MemberInfo initTarget() {
/* 204 */     MemberInfo memberInfo = new MemberInfo(getTargetName(), null, this.targetFieldType.getDescriptor());
/* 205 */     this.annotation.visit("target", memberInfo.toString());
/* 206 */     return memberInfo;
/*     */   }
/*     */   
/*     */   protected String getTargetName() {
/* 210 */     String str = (String)Annotations.getValue(this.annotation);
/* 211 */     if (Strings.isNullOrEmpty(str)) {
/* 212 */       String str1 = inflectTarget();
/* 213 */       if (str1 == null) {
/* 214 */         throw new InvalidAccessorException(this.mixin, "Failed to inflect target name for " + this + ", supported prefixes: [get, set, is]");
/*     */       }
/* 216 */       return str1;
/*     */     } 
/* 218 */     return (MemberInfo.parse(str, (IMixinContext)this.mixin)).name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String inflectTarget() {
/* 228 */     return inflectTarget(this.method.name, this.type, toString(), (IMixinContext)this.mixin, this.mixin
/* 229 */         .getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String inflectTarget(String paramString1, AccessorType paramAccessorType, String paramString2, IMixinContext paramIMixinContext, boolean paramBoolean) {
/* 251 */     Matcher matcher = PATTERN_ACCESSOR.matcher(paramString1);
/* 252 */     if (matcher.matches()) {
/* 253 */       String str1 = matcher.group(1);
/* 254 */       String str2 = matcher.group(3);
/* 255 */       String str3 = matcher.group(4);
/*     */       
/* 257 */       String str4 = String.format("%s%s", new Object[] { toLowerCase(str2, !isUpperCase(str3)), str3 });
/* 258 */       if (!paramAccessorType.isExpectedPrefix(str1) && paramBoolean) {
/* 259 */         LogManager.getLogger("mixin").warn("Unexpected prefix for {}, found [{}] expecting {}", new Object[] { paramString2, str1, paramAccessorType
/* 260 */               .getExpectedPrefixes() });
/*     */       }
/* 262 */       return (MemberInfo.parse(str4, paramIMixinContext)).name;
/*     */     } 
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberInfo getTarget() {
/* 271 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Type getTargetFieldType() {
/* 278 */     return this.targetFieldType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldNode getTargetField() {
/* 285 */     return this.targetField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MethodNode getTargetMethod() {
/* 292 */     return this.targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Type getReturnType() {
/* 299 */     return this.returnType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Type[] getArgTypes() {
/* 306 */     return this.argTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 311 */     return String.format("%s->@%s[%s]::%s%s", new Object[] { this.mixin.toString(), Bytecode.getSimpleName(this.annotation), this.type.toString(), this.method.name, this.method.desc });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void locate() {
/* 321 */     this.targetField = findTargetField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodNode generate() {
/* 332 */     MethodNode methodNode = this.type.getGenerator(this).generate();
/* 333 */     Bytecode.mergeAnnotations(this.method, methodNode);
/* 334 */     return methodNode;
/*     */   }
/*     */   
/*     */   private FieldNode findTargetField() {
/* 338 */     return findTarget(this.classNode.fields);
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
/*     */   protected <TNode> TNode findTarget(List<TNode> paramList) {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: new java/util/ArrayList
/*     */     //   5: dup
/*     */     //   6: invokespecial <init> : ()V
/*     */     //   9: astore_3
/*     */     //   10: aload_1
/*     */     //   11: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   16: astore #4
/*     */     //   18: aload #4
/*     */     //   20: invokeinterface hasNext : ()Z
/*     */     //   25: ifeq -> 124
/*     */     //   28: aload #4
/*     */     //   30: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   35: astore #5
/*     */     //   37: aload #5
/*     */     //   39: invokestatic getNodeDesc : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   42: astore #6
/*     */     //   44: aload #6
/*     */     //   46: ifnull -> 18
/*     */     //   49: aload #6
/*     */     //   51: aload_0
/*     */     //   52: getfield target : Lorg/spongepowered/asm/mixin/injection/struct/MemberInfo;
/*     */     //   55: getfield desc : Ljava/lang/String;
/*     */     //   58: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   61: ifne -> 67
/*     */     //   64: goto -> 18
/*     */     //   67: aload #5
/*     */     //   69: invokestatic getNodeName : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   72: astore #7
/*     */     //   74: aload #7
/*     */     //   76: ifnull -> 121
/*     */     //   79: aload #7
/*     */     //   81: aload_0
/*     */     //   82: getfield target : Lorg/spongepowered/asm/mixin/injection/struct/MemberInfo;
/*     */     //   85: getfield name : Ljava/lang/String;
/*     */     //   88: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   91: ifeq -> 97
/*     */     //   94: aload #5
/*     */     //   96: astore_2
/*     */     //   97: aload #7
/*     */     //   99: aload_0
/*     */     //   100: getfield target : Lorg/spongepowered/asm/mixin/injection/struct/MemberInfo;
/*     */     //   103: getfield name : Ljava/lang/String;
/*     */     //   106: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
/*     */     //   109: ifeq -> 121
/*     */     //   112: aload_3
/*     */     //   113: aload #5
/*     */     //   115: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   120: pop
/*     */     //   121: goto -> 18
/*     */     //   124: aload_2
/*     */     //   125: ifnull -> 167
/*     */     //   128: aload_3
/*     */     //   129: invokeinterface size : ()I
/*     */     //   134: iconst_1
/*     */     //   135: if_icmple -> 165
/*     */     //   138: ldc 'mixin'
/*     */     //   140: invokestatic getLogger : (Ljava/lang/String;)Lorg/apache/logging/log4j/Logger;
/*     */     //   143: ldc '{} found an exact match for {} but other candidates were found!'
/*     */     //   145: iconst_2
/*     */     //   146: anewarray java/lang/Object
/*     */     //   149: dup
/*     */     //   150: iconst_0
/*     */     //   151: aload_0
/*     */     //   152: aastore
/*     */     //   153: dup
/*     */     //   154: iconst_1
/*     */     //   155: aload_0
/*     */     //   156: getfield target : Lorg/spongepowered/asm/mixin/injection/struct/MemberInfo;
/*     */     //   159: aastore
/*     */     //   160: invokeinterface debug : (Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   165: aload_2
/*     */     //   166: areturn
/*     */     //   167: aload_3
/*     */     //   168: invokeinterface size : ()I
/*     */     //   173: iconst_1
/*     */     //   174: if_icmpne -> 185
/*     */     //   177: aload_3
/*     */     //   178: iconst_0
/*     */     //   179: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   184: areturn
/*     */     //   185: aload_3
/*     */     //   186: invokeinterface size : ()I
/*     */     //   191: ifne -> 199
/*     */     //   194: ldc 'No'
/*     */     //   196: goto -> 201
/*     */     //   199: ldc 'Multiple'
/*     */     //   201: astore #4
/*     */     //   203: new org/spongepowered/asm/mixin/gen/throwables/InvalidAccessorException
/*     */     //   206: dup
/*     */     //   207: aload_0
/*     */     //   208: new java/lang/StringBuilder
/*     */     //   211: dup
/*     */     //   212: invokespecial <init> : ()V
/*     */     //   215: aload #4
/*     */     //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   220: ldc ' candidates were found matching '
/*     */     //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   225: aload_0
/*     */     //   226: getfield target : Lorg/spongepowered/asm/mixin/injection/struct/MemberInfo;
/*     */     //   229: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   232: ldc ' in '
/*     */     //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   237: aload_0
/*     */     //   238: getfield classNode : Lorg/spongepowered/asm/lib/tree/ClassNode;
/*     */     //   241: getfield name : Ljava/lang/String;
/*     */     //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   247: ldc ' for '
/*     */     //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   252: aload_0
/*     */     //   253: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   256: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   259: invokespecial <init> : (Lorg/spongepowered/asm/mixin/gen/AccessorInfo;Ljava/lang/String;)V
/*     */     //   262: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #350	-> 0
/*     */     //   #351	-> 2
/*     */     //   #353	-> 10
/*     */     //   #354	-> 37
/*     */     //   #355	-> 44
/*     */     //   #356	-> 64
/*     */     //   #359	-> 67
/*     */     //   #360	-> 74
/*     */     //   #361	-> 79
/*     */     //   #362	-> 94
/*     */     //   #364	-> 97
/*     */     //   #365	-> 112
/*     */     //   #368	-> 121
/*     */     //   #370	-> 124
/*     */     //   #371	-> 128
/*     */     //   #372	-> 138
/*     */     //   #374	-> 165
/*     */     //   #377	-> 167
/*     */     //   #378	-> 177
/*     */     //   #381	-> 185
/*     */     //   #382	-> 203
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
/*     */   private static <TNode> String getNodeDesc(TNode paramTNode) {
/* 387 */     return (paramTNode instanceof MethodNode) ? ((MethodNode)paramTNode).desc : ((paramTNode instanceof FieldNode) ? ((FieldNode)paramTNode).desc : null);
/*     */   }
/*     */   
/*     */   private static <TNode> String getNodeName(TNode paramTNode) {
/* 391 */     return (paramTNode instanceof MethodNode) ? ((MethodNode)paramTNode).name : ((paramTNode instanceof FieldNode) ? ((FieldNode)paramTNode).name : null);
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
/*     */   public static AccessorInfo of(MixinTargetContext paramMixinTargetContext, MethodNode paramMethodNode, Class<? extends Annotation> paramClass) {
/* 404 */     if (paramClass == Accessor.class)
/* 405 */       return new AccessorInfo(paramMixinTargetContext, paramMethodNode); 
/* 406 */     if (paramClass == Invoker.class) {
/* 407 */       return new InvokerInfo(paramMixinTargetContext, paramMethodNode);
/*     */     }
/* 409 */     throw new InvalidAccessorException(paramMixinTargetContext, "Could not parse accessor for unknown type " + paramClass.getName());
/*     */   }
/*     */   
/*     */   private static String toLowerCase(String paramString, boolean paramBoolean) {
/* 413 */     return paramBoolean ? paramString.toLowerCase() : paramString;
/*     */   }
/*     */   
/*     */   private static boolean isUpperCase(String paramString) {
/* 417 */     return paramString.toUpperCase().equals(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\gen\AccessorInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */