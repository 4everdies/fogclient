/*      */ package org.spongepowered.asm.mixin.transformer;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.FieldNode;
/*      */ import org.spongepowered.asm.lib.tree.FrameNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodInsnNode;
/*      */ import org.spongepowered.asm.lib.tree.MethodNode;
/*      */ import org.spongepowered.asm.mixin.Final;
/*      */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*      */ import org.spongepowered.asm.mixin.Mutable;
/*      */ import org.spongepowered.asm.mixin.Shadow;
/*      */ import org.spongepowered.asm.mixin.Unique;
/*      */ import org.spongepowered.asm.mixin.gen.Accessor;
/*      */ import org.spongepowered.asm.mixin.gen.Invoker;
/*      */ import org.spongepowered.asm.service.MixinService;
/*      */ import org.spongepowered.asm.util.Annotations;
/*      */ import org.spongepowered.asm.util.ClassSignature;
/*      */ import org.spongepowered.asm.util.perf.Profiler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ClassInfo
/*      */ {
/*      */   public static final int INCLUDE_PRIVATE = 2;
/*      */   public static final int INCLUDE_STATIC = 8;
/*      */   public static final int INCLUDE_ALL = 10;
/*      */   
/*      */   public enum SearchType
/*      */   {
/*   85 */     ALL_CLASSES,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   90 */     SUPER_CLASSES_ONLY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Traversal
/*      */   {
/*  114 */     NONE(null, false, (Traversal)ClassInfo.SearchType.SUPER_CLASSES_ONLY),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  119 */     ALL(null, true, (Traversal)ClassInfo.SearchType.ALL_CLASSES),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  124 */     IMMEDIATE((String)NONE, true, (Traversal)ClassInfo.SearchType.SUPER_CLASSES_ONLY),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  130 */     SUPER((String)ALL, false, (Traversal)ClassInfo.SearchType.SUPER_CLASSES_ONLY);
/*      */     
/*      */     private final Traversal next;
/*      */     
/*      */     private final boolean traverse;
/*      */     
/*      */     private final ClassInfo.SearchType searchType;
/*      */     
/*      */     Traversal(Traversal param1Traversal, boolean param1Boolean, ClassInfo.SearchType param1SearchType) {
/*  139 */       this.next = (param1Traversal != null) ? param1Traversal : this;
/*  140 */       this.traverse = param1Boolean;
/*  141 */       this.searchType = param1SearchType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Traversal next() {
/*  148 */       return this.next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canTraverse() {
/*  155 */       return this.traverse;
/*      */     }
/*      */     
/*      */     public ClassInfo.SearchType getSearchType() {
/*  159 */       return this.searchType;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class FrameData
/*      */   {
/*  169 */     private static final String[] FRAMETYPES = new String[] { "NEW", "FULL", "APPEND", "CHOP", "SAME", "SAME1" };
/*      */ 
/*      */ 
/*      */     
/*      */     public final int index;
/*      */ 
/*      */ 
/*      */     
/*      */     public final int type;
/*      */ 
/*      */ 
/*      */     
/*      */     public final int locals;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     FrameData(int param1Int1, int param1Int2, int param1Int3) {
/*  187 */       this.index = param1Int1;
/*  188 */       this.type = param1Int2;
/*  189 */       this.locals = param1Int3;
/*      */     }
/*      */     
/*      */     FrameData(int param1Int, FrameNode param1FrameNode) {
/*  193 */       this.index = param1Int;
/*  194 */       this.type = param1FrameNode.type;
/*  195 */       this.locals = (param1FrameNode.local != null) ? param1FrameNode.local.size() : 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  203 */       return String.format("FrameData[index=%d, type=%s, locals=%d]", new Object[] { Integer.valueOf(this.index), FRAMETYPES[this.type + 1], Integer.valueOf(this.locals) });
/*      */     } }
/*      */   static abstract class Member { private final Type type;
/*      */     private final String memberName;
/*      */     private final String memberDesc;
/*      */     private final boolean isInjected;
/*      */     private final int modifiers;
/*      */     private String currentName;
/*      */     private String currentDesc;
/*      */     private boolean decoratedFinal;
/*      */     private boolean decoratedMutable;
/*      */     private boolean unique;
/*      */     
/*  216 */     enum Type { METHOD,
/*  217 */       FIELD; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Member(Member param1Member) {
/*  274 */       this(param1Member.type, param1Member.memberName, param1Member.memberDesc, param1Member.modifiers, param1Member.isInjected);
/*  275 */       this.currentName = param1Member.currentName;
/*  276 */       this.currentDesc = param1Member.currentDesc;
/*  277 */       this.unique = param1Member.unique;
/*      */     }
/*      */     
/*      */     protected Member(Type param1Type, String param1String1, String param1String2, int param1Int) {
/*  281 */       this(param1Type, param1String1, param1String2, param1Int, false);
/*      */     }
/*      */     
/*      */     protected Member(Type param1Type, String param1String1, String param1String2, int param1Int, boolean param1Boolean) {
/*  285 */       this.type = param1Type;
/*  286 */       this.memberName = param1String1;
/*  287 */       this.memberDesc = param1String2;
/*  288 */       this.isInjected = param1Boolean;
/*  289 */       this.currentName = param1String1;
/*  290 */       this.currentDesc = param1String2;
/*  291 */       this.modifiers = param1Int;
/*      */     }
/*      */     
/*      */     public String getOriginalName() {
/*  295 */       return this.memberName;
/*      */     }
/*      */     
/*      */     public String getName() {
/*  299 */       return this.currentName;
/*      */     }
/*      */     
/*      */     public String getOriginalDesc() {
/*  303 */       return this.memberDesc;
/*      */     }
/*      */     
/*      */     public String getDesc() {
/*  307 */       return this.currentDesc;
/*      */     }
/*      */     
/*      */     public boolean isInjected() {
/*  311 */       return this.isInjected;
/*      */     }
/*      */     
/*      */     public boolean isRenamed() {
/*  315 */       return !this.currentName.equals(this.memberName);
/*      */     }
/*      */     
/*      */     public boolean isRemapped() {
/*  319 */       return !this.currentDesc.equals(this.memberDesc);
/*      */     }
/*      */     
/*      */     public boolean isPrivate() {
/*  323 */       return ((this.modifiers & 0x2) != 0);
/*      */     }
/*      */     
/*      */     public boolean isStatic() {
/*  327 */       return ((this.modifiers & 0x8) != 0);
/*      */     }
/*      */     
/*      */     public boolean isAbstract() {
/*  331 */       return ((this.modifiers & 0x400) != 0);
/*      */     }
/*      */     
/*      */     public boolean isFinal() {
/*  335 */       return ((this.modifiers & 0x10) != 0);
/*      */     }
/*      */     
/*      */     public boolean isSynthetic() {
/*  339 */       return ((this.modifiers & 0x1000) != 0);
/*      */     }
/*      */     
/*      */     public boolean isUnique() {
/*  343 */       return this.unique;
/*      */     }
/*      */     
/*      */     public void setUnique(boolean param1Boolean) {
/*  347 */       this.unique = param1Boolean;
/*      */     }
/*      */     
/*      */     public boolean isDecoratedFinal() {
/*  351 */       return this.decoratedFinal;
/*      */     }
/*      */     
/*      */     public boolean isDecoratedMutable() {
/*  355 */       return this.decoratedMutable;
/*      */     }
/*      */     
/*      */     public void setDecoratedFinal(boolean param1Boolean1, boolean param1Boolean2) {
/*  359 */       this.decoratedFinal = param1Boolean1;
/*  360 */       this.decoratedMutable = param1Boolean2;
/*      */     }
/*      */     
/*      */     public boolean matchesFlags(int param1Int) {
/*  364 */       return (((this.modifiers ^ 0xFFFFFFFF | param1Int & 0x2) & 0x2) != 0 && ((this.modifiers ^ 0xFFFFFFFF | param1Int & 0x8) & 0x8) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract ClassInfo getOwner();
/*      */ 
/*      */     
/*      */     public ClassInfo getImplementor() {
/*  372 */       return getOwner();
/*      */     }
/*      */     
/*      */     public int getAccess() {
/*  376 */       return this.modifiers;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String renameTo(String param1String) {
/*  384 */       this.currentName = param1String;
/*  385 */       return param1String;
/*      */     }
/*      */     
/*      */     public String remapTo(String param1String) {
/*  389 */       this.currentDesc = param1String;
/*  390 */       return param1String;
/*      */     }
/*      */     
/*      */     public boolean equals(String param1String1, String param1String2) {
/*  394 */       return ((this.memberName.equals(param1String1) || this.currentName.equals(param1String1)) && (this.memberDesc
/*  395 */         .equals(param1String2) || this.currentDesc.equals(param1String2)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*  400 */       if (!(param1Object instanceof Member)) {
/*  401 */         return false;
/*      */       }
/*      */       
/*  404 */       Member member = (Member)param1Object;
/*  405 */       return ((member.memberName.equals(this.memberName) || member.currentName.equals(this.currentName)) && (member.memberDesc
/*  406 */         .equals(this.memberDesc) || member.currentDesc.equals(this.currentDesc)));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  411 */       return toString().hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  416 */       return String.format(getDisplayFormat(), new Object[] { this.memberName, this.memberDesc });
/*      */     }
/*      */     
/*      */     protected String getDisplayFormat() {
/*  420 */       return "%s%s";
/*      */     } }
/*      */ 
/*      */   
/*      */   enum Type
/*      */   {
/*      */     METHOD, FIELD;
/*      */   }
/*      */   
/*      */   public class Method
/*      */     extends Member {
/*      */     private final List<ClassInfo.FrameData> frames;
/*      */     private boolean isAccessor;
/*      */     
/*      */     public Method(ClassInfo.Member param1Member) {
/*  435 */       super(param1Member);
/*  436 */       this.frames = (param1Member instanceof Method) ? ((Method)param1Member).frames : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Method(MethodNode param1MethodNode) {
/*  441 */       this(param1MethodNode, false);
/*  442 */       setUnique((Annotations.getVisible(param1MethodNode, Unique.class) != null));
/*  443 */       this.isAccessor = (Annotations.getSingleVisible(param1MethodNode, new Class[] { Accessor.class, Invoker.class }) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public Method(MethodNode param1MethodNode, boolean param1Boolean) {
/*  448 */       super(ClassInfo.Member.Type.METHOD, param1MethodNode.name, param1MethodNode.desc, param1MethodNode.access, param1Boolean);
/*  449 */       this.frames = gatherFrames(param1MethodNode);
/*  450 */       setUnique((Annotations.getVisible(param1MethodNode, Unique.class) != null));
/*  451 */       this.isAccessor = (Annotations.getSingleVisible(param1MethodNode, new Class[] { Accessor.class, Invoker.class }) != null);
/*      */     }
/*      */     
/*      */     public Method(String param1String1, String param1String2) {
/*  455 */       super(ClassInfo.Member.Type.METHOD, param1String1, param1String2, 1, false);
/*  456 */       this.frames = null;
/*      */     }
/*      */     
/*      */     public Method(String param1String1, String param1String2, int param1Int) {
/*  460 */       super(ClassInfo.Member.Type.METHOD, param1String1, param1String2, param1Int, false);
/*  461 */       this.frames = null;
/*      */     }
/*      */     
/*      */     public Method(String param1String1, String param1String2, int param1Int, boolean param1Boolean) {
/*  465 */       super(ClassInfo.Member.Type.METHOD, param1String1, param1String2, param1Int, param1Boolean);
/*  466 */       this.frames = null;
/*      */     }
/*      */     
/*      */     private List<ClassInfo.FrameData> gatherFrames(MethodNode param1MethodNode) {
/*  470 */       ArrayList<ClassInfo.FrameData> arrayList = new ArrayList();
/*  471 */       for (ListIterator<AbstractInsnNode> listIterator = param1MethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/*  472 */         AbstractInsnNode abstractInsnNode = listIterator.next();
/*  473 */         if (abstractInsnNode instanceof FrameNode) {
/*  474 */           arrayList.add(new ClassInfo.FrameData(param1MethodNode.instructions.indexOf(abstractInsnNode), (FrameNode)abstractInsnNode));
/*      */         }
/*      */       } 
/*  477 */       return arrayList;
/*      */     }
/*      */     
/*      */     public List<ClassInfo.FrameData> getFrames() {
/*  481 */       return this.frames;
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassInfo getOwner() {
/*  486 */       return ClassInfo.this;
/*      */     }
/*      */     
/*      */     public boolean isAccessor() {
/*  490 */       return this.isAccessor;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*  495 */       if (!(param1Object instanceof Method)) {
/*  496 */         return false;
/*      */       }
/*      */       
/*  499 */       return super.equals(param1Object);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class InterfaceMethod
/*      */     extends Method
/*      */   {
/*      */     private final ClassInfo owner;
/*      */ 
/*      */ 
/*      */     
/*      */     public InterfaceMethod(ClassInfo.Member param1Member) {
/*  513 */       super(param1Member);
/*  514 */       this.owner = param1Member.getOwner();
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassInfo getOwner() {
/*  519 */       return this.owner;
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassInfo getImplementor() {
/*  524 */       return ClassInfo.this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class Field
/*      */     extends Member
/*      */   {
/*      */     public Field(ClassInfo.Member param1Member) {
/*  535 */       super(param1Member);
/*      */     }
/*      */     
/*      */     public Field(FieldNode param1FieldNode) {
/*  539 */       this(param1FieldNode, false);
/*      */     }
/*      */     
/*      */     public Field(FieldNode param1FieldNode, boolean param1Boolean) {
/*  543 */       super(ClassInfo.Member.Type.FIELD, param1FieldNode.name, param1FieldNode.desc, param1FieldNode.access, param1Boolean);
/*      */       
/*  545 */       setUnique((Annotations.getVisible(param1FieldNode, Unique.class) != null));
/*      */       
/*  547 */       if (Annotations.getVisible(param1FieldNode, Shadow.class) != null) {
/*  548 */         boolean bool1 = (Annotations.getVisible(param1FieldNode, Final.class) != null) ? true : false;
/*  549 */         boolean bool2 = (Annotations.getVisible(param1FieldNode, Mutable.class) != null) ? true : false;
/*  550 */         setDecoratedFinal(bool1, bool2);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Field(String param1String1, String param1String2, int param1Int) {
/*  555 */       super(ClassInfo.Member.Type.FIELD, param1String1, param1String2, param1Int, false);
/*      */     }
/*      */     
/*      */     public Field(String param1String1, String param1String2, int param1Int, boolean param1Boolean) {
/*  559 */       super(ClassInfo.Member.Type.FIELD, param1String1, param1String2, param1Int, param1Boolean);
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassInfo getOwner() {
/*  564 */       return ClassInfo.this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*  569 */       if (!(param1Object instanceof Field)) {
/*  570 */         return false;
/*      */       }
/*      */       
/*  573 */       return super.equals(param1Object);
/*      */     }
/*      */ 
/*      */     
/*      */     protected String getDisplayFormat() {
/*  578 */       return "%s:%s";
/*      */     }
/*      */   }
/*      */   
/*  582 */   private static final Logger logger = LogManager.getLogger("mixin");
/*      */   
/*  584 */   private static final Profiler profiler = MixinEnvironment.getProfiler();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String JAVA_LANG_OBJECT = "java/lang/Object";
/*      */ 
/*      */ 
/*      */   
/*  592 */   private static final Map<String, ClassInfo> cache = new HashMap<String, ClassInfo>();
/*      */   
/*  594 */   private static final ClassInfo OBJECT = new ClassInfo(); private final String name; private final String superName; private final String outerName;
/*      */   
/*      */   static {
/*  597 */     cache.put("java/lang/Object", OBJECT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isProbablyStatic;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Set<String> interfaces;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Set<Method> methods;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Set<Field> fields;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  639 */   private final Set<MixinInfo> mixins = new HashSet<MixinInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  645 */   private final Map<ClassInfo, ClassInfo> correspondingTypes = new HashMap<ClassInfo, ClassInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final MixinInfo mixin;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final MethodMapper methodMapper;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isMixin;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isInterface;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int access;
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo superClass;
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo outerClass;
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassSignature signature;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo() {
/*  688 */     this.name = "java/lang/Object";
/*  689 */     this.superName = null;
/*  690 */     this.outerName = null;
/*  691 */     this.isProbablyStatic = true;
/*  692 */     this.methods = (Set<Method>)ImmutableSet.of(new Method("getClass", "()Ljava/lang/Class;"), new Method("hashCode", "()I"), new Method("equals", "(Ljava/lang/Object;)Z"), new Method("clone", "()Ljava/lang/Object;"), new Method("toString", "()Ljava/lang/String;"), new Method("notify", "()V"), (Object[])new Method[] { new Method("notifyAll", "()V"), new Method("wait", "(J)V"), new Method("wait", "(JI)V"), new Method("wait", "()V"), new Method("finalize", "()V") });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     this.fields = Collections.emptySet();
/*  706 */     this.isInterface = false;
/*  707 */     this.interfaces = Collections.emptySet();
/*  708 */     this.access = 1;
/*  709 */     this.isMixin = false;
/*  710 */     this.mixin = null;
/*  711 */     this.methodMapper = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo(ClassNode paramClassNode) {
/*  720 */     Profiler.Section section = profiler.begin(1, "class.meta");
/*      */     try {
/*  722 */       this.name = paramClassNode.name;
/*  723 */       this.superName = (paramClassNode.superName != null) ? paramClassNode.superName : "java/lang/Object";
/*  724 */       this.methods = new HashSet<Method>();
/*  725 */       this.fields = new HashSet<Field>();
/*  726 */       this.isInterface = ((paramClassNode.access & 0x200) != 0);
/*  727 */       this.interfaces = new HashSet<String>();
/*  728 */       this.access = paramClassNode.access;
/*  729 */       this.isMixin = paramClassNode instanceof MixinInfo.MixinClassNode;
/*  730 */       this.mixin = this.isMixin ? ((MixinInfo.MixinClassNode)paramClassNode).getMixin() : null;
/*      */       
/*  732 */       this.interfaces.addAll(paramClassNode.interfaces);
/*      */       
/*  734 */       for (MethodNode methodNode : paramClassNode.methods) {
/*  735 */         addMethod(methodNode, this.isMixin);
/*      */       }
/*      */       
/*  738 */       boolean bool = true;
/*  739 */       String str = paramClassNode.outerClass;
/*  740 */       for (FieldNode fieldNode : paramClassNode.fields) {
/*  741 */         if ((fieldNode.access & 0x1000) != 0 && 
/*  742 */           fieldNode.name.startsWith("this$")) {
/*  743 */           bool = false;
/*  744 */           if (str == null) {
/*  745 */             str = fieldNode.desc;
/*  746 */             if (str != null && str.startsWith("L")) {
/*  747 */               str = str.substring(1, str.length() - 1);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  753 */         this.fields.add(new Field(fieldNode, this.isMixin));
/*      */       } 
/*      */       
/*  756 */       this.isProbablyStatic = bool;
/*  757 */       this.outerName = str;
/*  758 */       this.methodMapper = new MethodMapper(MixinEnvironment.getCurrentEnvironment(), this);
/*  759 */       this.signature = ClassSignature.ofLazy(paramClassNode);
/*      */     } finally {
/*  761 */       section.end();
/*      */     } 
/*      */   }
/*      */   
/*      */   void addInterface(String paramString) {
/*  766 */     this.interfaces.add(paramString);
/*  767 */     getSignature().addInterface(paramString);
/*      */   }
/*      */   
/*      */   void addMethod(MethodNode paramMethodNode) {
/*  771 */     addMethod(paramMethodNode, true);
/*      */   }
/*      */   
/*      */   private void addMethod(MethodNode paramMethodNode, boolean paramBoolean) {
/*  775 */     if (!paramMethodNode.name.startsWith("<")) {
/*  776 */       this.methods.add(new Method(paramMethodNode, paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addMixin(MixinInfo paramMixinInfo) {
/*  784 */     if (this.isMixin) {
/*  785 */       throw new IllegalArgumentException("Cannot add target " + this.name + " for " + paramMixinInfo.getClassName() + " because the target is a mixin");
/*      */     }
/*  787 */     this.mixins.add(paramMixinInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<MixinInfo> getMixins() {
/*  794 */     return Collections.unmodifiableSet(this.mixins);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMixin() {
/*  801 */     return this.isMixin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPublic() {
/*  808 */     return ((this.access & 0x1) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbstract() {
/*  815 */     return ((this.access & 0x400) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSynthetic() {
/*  822 */     return ((this.access & 0x1000) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isProbablyStatic() {
/*  829 */     return this.isProbablyStatic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInner() {
/*  836 */     return (this.outerName != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInterface() {
/*  843 */     return this.isInterface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getInterfaces() {
/*  850 */     return Collections.unmodifiableSet(this.interfaces);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  855 */     return this.name;
/*      */   }
/*      */   
/*      */   public MethodMapper getMethodMapper() {
/*  859 */     return this.methodMapper;
/*      */   }
/*      */   
/*      */   public int getAccess() {
/*  863 */     return this.access;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  870 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  877 */     return this.name.replace('/', '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSuperName() {
/*  884 */     return this.superName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo getSuperClass() {
/*  892 */     if (this.superClass == null && this.superName != null) {
/*  893 */       this.superClass = forName(this.superName);
/*      */     }
/*      */     
/*  896 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOuterName() {
/*  903 */     return this.outerName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo getOuterClass() {
/*  911 */     if (this.outerClass == null && this.outerName != null) {
/*  912 */       this.outerClass = forName(this.outerName);
/*      */     }
/*      */     
/*  915 */     return this.outerClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassSignature getSignature() {
/*  924 */     return this.signature.wake();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<ClassInfo> getTargets() {
/*  931 */     if (this.mixin != null) {
/*  932 */       ArrayList<ClassInfo> arrayList = new ArrayList();
/*  933 */       arrayList.add(this);
/*  934 */       arrayList.addAll(this.mixin.getTargets());
/*  935 */       return arrayList;
/*      */     } 
/*      */     
/*  938 */     return (List<ClassInfo>)ImmutableList.of(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Method> getMethods() {
/*  947 */     return Collections.unmodifiableSet(this.methods);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Method> getInterfaceMethods(boolean paramBoolean) {
/*  961 */     HashSet<Method> hashSet = new HashSet();
/*      */     
/*  963 */     ClassInfo classInfo = addMethodsRecursive(hashSet, paramBoolean);
/*  964 */     if (!this.isInterface) {
/*  965 */       while (classInfo != null && classInfo != OBJECT) {
/*  966 */         classInfo = classInfo.addMethodsRecursive(hashSet, paramBoolean);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  971 */     for (Iterator<Method> iterator = hashSet.iterator(); iterator.hasNext();) {
/*  972 */       if (!((Method)iterator.next()).isAbstract()) {
/*  973 */         iterator.remove();
/*      */       }
/*      */     } 
/*      */     
/*  977 */     return Collections.unmodifiableSet(hashSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo addMethodsRecursive(Set<Method> paramSet, boolean paramBoolean) {
/*  990 */     if (this.isInterface) {
/*  991 */       for (Method method : this.methods) {
/*      */         
/*  993 */         if (!method.isAbstract())
/*      */         {
/*  995 */           paramSet.remove(method);
/*      */         }
/*  997 */         paramSet.add(method);
/*      */       } 
/*  999 */     } else if (!this.isMixin && paramBoolean) {
/* 1000 */       for (MixinInfo mixinInfo : this.mixins) {
/* 1001 */         mixinInfo.getClassInfo().addMethodsRecursive(paramSet, paramBoolean);
/*      */       }
/*      */     } 
/*      */     
/* 1005 */     for (String str : this.interfaces) {
/* 1006 */       forName(str).addMethodsRecursive(paramSet, paramBoolean);
/*      */     }
/*      */     
/* 1009 */     return getSuperClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSuperClass(String paramString) {
/* 1020 */     return hasSuperClass(paramString, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSuperClass(String paramString, Traversal paramTraversal) {
/* 1032 */     if ("java/lang/Object".equals(paramString)) {
/* 1033 */       return true;
/*      */     }
/*      */     
/* 1036 */     return (findSuperClass(paramString, paramTraversal) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSuperClass(ClassInfo paramClassInfo) {
/* 1047 */     return hasSuperClass(paramClassInfo, Traversal.NONE, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSuperClass(ClassInfo paramClassInfo, Traversal paramTraversal) {
/* 1059 */     return hasSuperClass(paramClassInfo, paramTraversal, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSuperClass(ClassInfo paramClassInfo, Traversal paramTraversal, boolean paramBoolean) {
/* 1072 */     if (OBJECT == paramClassInfo) {
/* 1073 */       return true;
/*      */     }
/*      */     
/* 1076 */     return (findSuperClass(paramClassInfo.name, paramTraversal, paramBoolean) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo findSuperClass(String paramString) {
/* 1087 */     return findSuperClass(paramString, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo findSuperClass(String paramString, Traversal paramTraversal) {
/* 1099 */     return findSuperClass(paramString, paramTraversal, false, new HashSet<String>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassInfo findSuperClass(String paramString, Traversal paramTraversal, boolean paramBoolean) {
/* 1112 */     if (OBJECT.name.equals(paramString)) {
/* 1113 */       return null;
/*      */     }
/*      */     
/* 1116 */     return findSuperClass(paramString, paramTraversal, paramBoolean, new HashSet<String>());
/*      */   }
/*      */   
/*      */   private ClassInfo findSuperClass(String paramString, Traversal paramTraversal, boolean paramBoolean, Set<String> paramSet) {
/* 1120 */     ClassInfo classInfo = getSuperClass();
/* 1121 */     if (classInfo != null) {
/* 1122 */       for (ClassInfo classInfo1 : classInfo.getTargets()) {
/* 1123 */         if (paramString.equals(classInfo1.getName())) {
/* 1124 */           return classInfo;
/*      */         }
/*      */         
/* 1127 */         ClassInfo classInfo2 = classInfo1.findSuperClass(paramString, paramTraversal.next(), paramBoolean, paramSet);
/* 1128 */         if (classInfo2 != null) {
/* 1129 */           return classInfo2;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1134 */     if (paramBoolean) {
/* 1135 */       ClassInfo classInfo1 = findInterface(paramString);
/* 1136 */       if (classInfo1 != null) {
/* 1137 */         return classInfo1;
/*      */       }
/*      */     } 
/*      */     
/* 1141 */     if (paramTraversal.canTraverse()) {
/* 1142 */       for (MixinInfo mixinInfo : this.mixins) {
/* 1143 */         String str = mixinInfo.getClassName();
/* 1144 */         if (paramSet.contains(str)) {
/*      */           continue;
/*      */         }
/* 1147 */         paramSet.add(str);
/* 1148 */         ClassInfo classInfo1 = mixinInfo.getClassInfo();
/* 1149 */         if (paramString.equals(classInfo1.getName())) {
/* 1150 */           return classInfo1;
/*      */         }
/* 1152 */         ClassInfo classInfo2 = classInfo1.findSuperClass(paramString, Traversal.ALL, paramBoolean, paramSet);
/* 1153 */         if (classInfo2 != null) {
/* 1154 */           return classInfo2;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1159 */     return null;
/*      */   }
/*      */   
/*      */   private ClassInfo findInterface(String paramString) {
/* 1163 */     for (String str : getInterfaces()) {
/* 1164 */       ClassInfo classInfo1 = forName(str);
/* 1165 */       if (paramString.equals(str)) {
/* 1166 */         return classInfo1;
/*      */       }
/* 1168 */       ClassInfo classInfo2 = classInfo1.findInterface(paramString);
/* 1169 */       if (classInfo2 != null) {
/* 1170 */         return classInfo2;
/*      */       }
/*      */     } 
/* 1173 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ClassInfo findCorrespondingType(ClassInfo paramClassInfo) {
/* 1187 */     if (paramClassInfo == null || !paramClassInfo.isMixin || this.isMixin) {
/* 1188 */       return null;
/*      */     }
/*      */     
/* 1191 */     ClassInfo classInfo = this.correspondingTypes.get(paramClassInfo);
/* 1192 */     if (classInfo == null) {
/* 1193 */       classInfo = findSuperTypeForMixin(paramClassInfo);
/* 1194 */       this.correspondingTypes.put(paramClassInfo, classInfo);
/*      */     } 
/* 1196 */     return classInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassInfo findSuperTypeForMixin(ClassInfo paramClassInfo) {
/* 1204 */     ClassInfo classInfo = this;
/*      */     
/* 1206 */     while (classInfo != null && classInfo != OBJECT) {
/* 1207 */       for (MixinInfo mixinInfo : classInfo.mixins) {
/* 1208 */         if (mixinInfo.getClassInfo().equals(paramClassInfo)) {
/* 1209 */           return classInfo;
/*      */         }
/*      */       } 
/*      */       
/* 1213 */       classInfo = classInfo.getSuperClass();
/*      */     } 
/*      */     
/* 1216 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMixinInHierarchy() {
/* 1227 */     if (!this.isMixin) {
/* 1228 */       return false;
/*      */     }
/*      */     
/* 1231 */     ClassInfo classInfo = getSuperClass();
/*      */     
/* 1233 */     while (classInfo != null && classInfo != OBJECT) {
/* 1234 */       if (classInfo.isMixin) {
/* 1235 */         return true;
/*      */       }
/* 1237 */       classInfo = classInfo.getSuperClass();
/*      */     } 
/*      */     
/* 1240 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMixinTargetInHierarchy() {
/* 1252 */     if (this.isMixin) {
/* 1253 */       return false;
/*      */     }
/*      */     
/* 1256 */     ClassInfo classInfo = getSuperClass();
/*      */     
/* 1258 */     while (classInfo != null && classInfo != OBJECT) {
/* 1259 */       if (classInfo.mixins.size() > 0) {
/* 1260 */         return true;
/*      */       }
/* 1262 */       classInfo = classInfo.getSuperClass();
/*      */     } 
/*      */     
/* 1265 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(MethodNode paramMethodNode, SearchType paramSearchType) {
/* 1276 */     return findMethodInHierarchy(paramMethodNode.name, paramMethodNode.desc, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(MethodNode paramMethodNode, SearchType paramSearchType, int paramInt) {
/* 1288 */     return findMethodInHierarchy(paramMethodNode.name, paramMethodNode.desc, paramSearchType, Traversal.NONE, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(MethodInsnNode paramMethodInsnNode, SearchType paramSearchType) {
/* 1299 */     return findMethodInHierarchy(paramMethodInsnNode.name, paramMethodInsnNode.desc, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(MethodInsnNode paramMethodInsnNode, SearchType paramSearchType, int paramInt) {
/* 1311 */     return findMethodInHierarchy(paramMethodInsnNode.name, paramMethodInsnNode.desc, paramSearchType, Traversal.NONE, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(String paramString1, String paramString2, SearchType paramSearchType) {
/* 1323 */     return findMethodInHierarchy(paramString1, paramString2, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(String paramString1, String paramString2, SearchType paramSearchType, Traversal paramTraversal) {
/* 1336 */     return findMethodInHierarchy(paramString1, paramString2, paramSearchType, paramTraversal, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethodInHierarchy(String paramString1, String paramString2, SearchType paramSearchType, Traversal paramTraversal, int paramInt) {
/* 1350 */     return findInHierarchy(paramString1, paramString2, paramSearchType, paramTraversal, paramInt, Member.Type.METHOD);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(FieldNode paramFieldNode, SearchType paramSearchType) {
/* 1361 */     return findFieldInHierarchy(paramFieldNode.name, paramFieldNode.desc, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(FieldNode paramFieldNode, SearchType paramSearchType, int paramInt) {
/* 1373 */     return findFieldInHierarchy(paramFieldNode.name, paramFieldNode.desc, paramSearchType, Traversal.NONE, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(FieldInsnNode paramFieldInsnNode, SearchType paramSearchType) {
/* 1384 */     return findFieldInHierarchy(paramFieldInsnNode.name, paramFieldInsnNode.desc, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(FieldInsnNode paramFieldInsnNode, SearchType paramSearchType, int paramInt) {
/* 1396 */     return findFieldInHierarchy(paramFieldInsnNode.name, paramFieldInsnNode.desc, paramSearchType, Traversal.NONE, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(String paramString1, String paramString2, SearchType paramSearchType) {
/* 1408 */     return findFieldInHierarchy(paramString1, paramString2, paramSearchType, Traversal.NONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(String paramString1, String paramString2, SearchType paramSearchType, Traversal paramTraversal) {
/* 1421 */     return findFieldInHierarchy(paramString1, paramString2, paramSearchType, paramTraversal, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findFieldInHierarchy(String paramString1, String paramString2, SearchType paramSearchType, Traversal paramTraversal, int paramInt) {
/* 1435 */     return findInHierarchy(paramString1, paramString2, paramSearchType, paramTraversal, paramInt, Member.Type.FIELD);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <M extends Member> M findInHierarchy(String paramString1, String paramString2, SearchType paramSearchType, Traversal paramTraversal, int paramInt, Member.Type paramType) {
/* 1452 */     if (paramSearchType == SearchType.ALL_CLASSES) {
/* 1453 */       M m = (M)findMember(paramString1, paramString2, paramInt, paramType);
/* 1454 */       if (m != null) {
/* 1455 */         return m;
/*      */       }
/*      */       
/* 1458 */       if (paramTraversal.canTraverse()) {
/* 1459 */         for (MixinInfo mixinInfo : this.mixins) {
/* 1460 */           M m1 = (M)mixinInfo.getClassInfo().findMember(paramString1, paramString2, paramInt, paramType);
/* 1461 */           if (m1 != null) {
/* 1462 */             return cloneMember(m1);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1468 */     ClassInfo classInfo = getSuperClass();
/* 1469 */     if (classInfo != null) {
/* 1470 */       for (ClassInfo classInfo1 : classInfo.getTargets()) {
/* 1471 */         M m = (M)classInfo1.findInHierarchy(paramString1, paramString2, SearchType.ALL_CLASSES, paramTraversal.next(), paramInt & 0xFFFFFFFD, paramType);
/*      */         
/* 1473 */         if (m != null) {
/* 1474 */           return m;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1479 */     if (paramType == Member.Type.METHOD && (this.isInterface || MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces())) {
/* 1480 */       for (String str : this.interfaces) {
/* 1481 */         ClassInfo classInfo1 = forName(str);
/* 1482 */         if (classInfo1 == null) {
/* 1483 */           logger.debug("Failed to resolve declared interface {} on {}", new Object[] { str, this.name });
/*      */           
/*      */           continue;
/*      */         } 
/* 1487 */         M m = (M)classInfo1.findInHierarchy(paramString1, paramString2, SearchType.ALL_CLASSES, paramTraversal.next(), paramInt & 0xFFFFFFFD, paramType);
/* 1488 */         if (m != null) {
/* 1489 */           return this.isInterface ? m : (M)new InterfaceMethod((Member)m);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1494 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <M extends Member> M cloneMember(M paramM) {
/* 1508 */     if (paramM instanceof Method) {
/* 1509 */       return (M)new Method((Member)paramM);
/*      */     }
/*      */     
/* 1512 */     return (M)new Field((Member)paramM);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethod(MethodNode paramMethodNode) {
/* 1522 */     return findMethod(paramMethodNode.name, paramMethodNode.desc, paramMethodNode.access);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethod(MethodNode paramMethodNode, int paramInt) {
/* 1533 */     return findMethod(paramMethodNode.name, paramMethodNode.desc, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethod(MethodInsnNode paramMethodInsnNode) {
/* 1543 */     return findMethod(paramMethodInsnNode.name, paramMethodInsnNode.desc, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethod(MethodInsnNode paramMethodInsnNode, int paramInt) {
/* 1554 */     return findMethod(paramMethodInsnNode.name, paramMethodInsnNode.desc, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method findMethod(String paramString1, String paramString2, int paramInt) {
/* 1566 */     return findMember(paramString1, paramString2, paramInt, Member.Type.METHOD);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findField(FieldNode paramFieldNode) {
/* 1576 */     return findField(paramFieldNode.name, paramFieldNode.desc, paramFieldNode.access);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findField(FieldInsnNode paramFieldInsnNode, int paramInt) {
/* 1587 */     return findField(paramFieldInsnNode.name, paramFieldInsnNode.desc, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field findField(String paramString1, String paramString2, int paramInt) {
/* 1599 */     return findMember(paramString1, paramString2, paramInt, Member.Type.FIELD);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <M extends Member> M findMember(String paramString1, String paramString2, int paramInt, Member.Type paramType) {
/* 1613 */     Set set = (Set)((paramType == Member.Type.METHOD) ? this.methods : this.fields);
/*      */     
/* 1615 */     for (Member member : set) {
/* 1616 */       if (member.equals(paramString1, paramString2) && member.matchesFlags(paramInt)) {
/* 1617 */         return (M)member;
/*      */       }
/*      */     } 
/*      */     
/* 1621 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/* 1629 */     if (!(paramObject instanceof ClassInfo)) {
/* 1630 */       return false;
/*      */     }
/* 1632 */     return ((ClassInfo)paramObject).name.equals(this.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1640 */     return this.name.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ClassInfo fromClassNode(ClassNode paramClassNode) {
/* 1653 */     ClassInfo classInfo = cache.get(paramClassNode.name);
/* 1654 */     if (classInfo == null) {
/* 1655 */       classInfo = new ClassInfo(paramClassNode);
/* 1656 */       cache.put(paramClassNode.name, classInfo);
/*      */     } 
/*      */     
/* 1659 */     return classInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo forName(String paramString) {
/* 1671 */     paramString = paramString.replace('.', '/');
/*      */     
/* 1673 */     ClassInfo classInfo = cache.get(paramString);
/* 1674 */     if (classInfo == null) {
/*      */       try {
/* 1676 */         ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(paramString);
/* 1677 */         classInfo = new ClassInfo(classNode);
/* 1678 */       } catch (Exception exception) {
/* 1679 */         logger.catching(Level.TRACE, exception);
/* 1680 */         logger.warn("Error loading class: {} ({}: {})", new Object[] { paramString, exception.getClass().getName(), exception.getMessage() });
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1685 */       cache.put(paramString, classInfo);
/* 1686 */       logger.trace("Added class metadata for {} to metadata cache", new Object[] { paramString });
/*      */     } 
/*      */     
/* 1689 */     return classInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo forType(org.spongepowered.asm.lib.Type paramType) {
/* 1701 */     if (paramType.getSort() == 9)
/* 1702 */       return forType(paramType.getElementType()); 
/* 1703 */     if (paramType.getSort() < 9) {
/* 1704 */       return null;
/*      */     }
/* 1706 */     return forName(paramType.getClassName().replace('.', '/'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo getCommonSuperClass(String paramString1, String paramString2) {
/* 1718 */     if (paramString1 == null || paramString2 == null) {
/* 1719 */       return OBJECT;
/*      */     }
/* 1721 */     return getCommonSuperClass(forName(paramString1), forName(paramString2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo getCommonSuperClass(org.spongepowered.asm.lib.Type paramType1, org.spongepowered.asm.lib.Type paramType2) {
/* 1733 */     if (paramType1 == null || paramType2 == null || paramType1
/* 1734 */       .getSort() != 10 || paramType2.getSort() != 10) {
/* 1735 */       return OBJECT;
/*      */     }
/* 1737 */     return getCommonSuperClass(forType(paramType1), forType(paramType2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassInfo getCommonSuperClass(ClassInfo paramClassInfo1, ClassInfo paramClassInfo2) {
/* 1749 */     return getCommonSuperClass(paramClassInfo1, paramClassInfo2, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo getCommonSuperClassOrInterface(String paramString1, String paramString2) {
/* 1761 */     if (paramString1 == null || paramString2 == null) {
/* 1762 */       return OBJECT;
/*      */     }
/* 1764 */     return getCommonSuperClassOrInterface(forName(paramString1), forName(paramString2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo getCommonSuperClassOrInterface(org.spongepowered.asm.lib.Type paramType1, org.spongepowered.asm.lib.Type paramType2) {
/* 1776 */     if (paramType1 == null || paramType2 == null || paramType1
/* 1777 */       .getSort() != 10 || paramType2.getSort() != 10) {
/* 1778 */       return OBJECT;
/*      */     }
/* 1780 */     return getCommonSuperClassOrInterface(forType(paramType1), forType(paramType2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassInfo getCommonSuperClassOrInterface(ClassInfo paramClassInfo1, ClassInfo paramClassInfo2) {
/* 1792 */     return getCommonSuperClass(paramClassInfo1, paramClassInfo2, true);
/*      */   }
/*      */   
/*      */   private static ClassInfo getCommonSuperClass(ClassInfo paramClassInfo1, ClassInfo paramClassInfo2, boolean paramBoolean) {
/* 1796 */     if (paramClassInfo1.hasSuperClass(paramClassInfo2, Traversal.NONE, paramBoolean))
/* 1797 */       return paramClassInfo2; 
/* 1798 */     if (paramClassInfo2.hasSuperClass(paramClassInfo1, Traversal.NONE, paramBoolean))
/* 1799 */       return paramClassInfo1; 
/* 1800 */     if (paramClassInfo1.isInterface() || paramClassInfo2.isInterface()) {
/* 1801 */       return OBJECT;
/*      */     }
/*      */     
/*      */     do {
/* 1805 */       paramClassInfo1 = paramClassInfo1.getSuperClass();
/* 1806 */       if (paramClassInfo1 == null) {
/* 1807 */         return OBJECT;
/*      */       }
/* 1809 */     } while (!paramClassInfo2.hasSuperClass(paramClassInfo1, Traversal.NONE, paramBoolean));
/*      */     
/* 1811 */     return paramClassInfo1;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\ClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */