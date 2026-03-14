/*      */ package org.spongepowered.asm.util;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Deque;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.spongepowered.asm.lib.signature.SignatureReader;
/*      */ import org.spongepowered.asm.lib.signature.SignatureVisitor;
/*      */ import org.spongepowered.asm.lib.signature.SignatureWriter;
/*      */ import org.spongepowered.asm.lib.tree.ClassNode;
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
/*      */ public class ClassSignature
/*      */ {
/*      */   protected static final String OBJECT = "java/lang/Object";
/*      */   
/*      */   static class Lazy
/*      */     extends ClassSignature
/*      */   {
/*      */     private final String sig;
/*      */     private ClassSignature generated;
/*      */     
/*      */     Lazy(String param1String) {
/*   64 */       this.sig = param1String;
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassSignature wake() {
/*   69 */       if (this.generated == null) {
/*   70 */         this.generated = ClassSignature.of(this.sig);
/*      */       }
/*   72 */       return this.generated;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TypeVar
/*      */     implements Comparable<TypeVar>
/*      */   {
/*      */     private final String originalName;
/*      */ 
/*      */ 
/*      */     
/*      */     private String currentName;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TypeVar(String param1String) {
/*   93 */       this.currentName = this.originalName = param1String;
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(TypeVar param1TypeVar) {
/*   98 */       return this.currentName.compareTo(param1TypeVar.currentName);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  103 */       return this.currentName;
/*      */     }
/*      */     
/*      */     String getOriginalName() {
/*  107 */       return this.originalName;
/*      */     }
/*      */     
/*      */     void rename(String param1String) {
/*  111 */       this.currentName = param1String;
/*      */     }
/*      */     
/*      */     public boolean matches(String param1String) {
/*  115 */       return this.originalName.equals(param1String);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*  120 */       return this.currentName.equals(param1Object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  125 */       return this.currentName.hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface IToken
/*      */   {
/*      */     public static final String WILDCARDS = "+-";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String asType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String asBound();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ClassSignature.Token asToken();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     IToken setArray(boolean param1Boolean);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     IToken setWildcard(char param1Char);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Token
/*      */     implements IToken
/*      */   {
/*      */     static final String SYMBOLS = "+-*";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean inner;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean array;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  202 */     private char symbol = Character.MIN_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String type;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private List<Token> classBound;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private List<Token> ifaceBound;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private List<ClassSignature.IToken> signature;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private List<ClassSignature.IToken> suffix;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Token tail;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Token() {
/*  238 */       this(false);
/*      */     }
/*      */     
/*      */     Token(String param1String) {
/*  242 */       this(param1String, false);
/*      */     }
/*      */     
/*      */     Token(char param1Char) {
/*  246 */       this();
/*  247 */       this.symbol = param1Char;
/*      */     }
/*      */     
/*      */     Token(boolean param1Boolean) {
/*  251 */       this(null, param1Boolean);
/*      */     }
/*      */     
/*      */     Token(String param1String, boolean param1Boolean) {
/*  255 */       this.inner = param1Boolean;
/*  256 */       this.type = param1String;
/*      */     }
/*      */     
/*      */     Token setSymbol(char param1Char) {
/*  260 */       if (this.symbol == '\000' && "+-*".indexOf(param1Char) > -1) {
/*  261 */         this.symbol = param1Char;
/*      */       }
/*  263 */       return this;
/*      */     }
/*      */     
/*      */     Token setType(String param1String) {
/*  267 */       if (this.type == null) {
/*  268 */         this.type = param1String;
/*      */       }
/*  270 */       return this;
/*      */     }
/*      */     
/*      */     boolean hasClassBound() {
/*  274 */       return (this.classBound != null);
/*      */     }
/*      */     
/*      */     boolean hasInterfaceBound() {
/*  278 */       return (this.ifaceBound != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassSignature.IToken setArray(boolean param1Boolean) {
/*  283 */       this.array |= param1Boolean;
/*  284 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public ClassSignature.IToken setWildcard(char param1Char) {
/*  289 */       if ("+-".indexOf(param1Char) == -1) {
/*  290 */         return this;
/*      */       }
/*  292 */       return setSymbol(param1Char);
/*      */     }
/*      */     
/*      */     private List<Token> getClassBound() {
/*  296 */       if (this.classBound == null) {
/*  297 */         this.classBound = new ArrayList<Token>();
/*      */       }
/*  299 */       return this.classBound;
/*      */     }
/*      */     
/*      */     private List<Token> getIfaceBound() {
/*  303 */       if (this.ifaceBound == null) {
/*  304 */         this.ifaceBound = new ArrayList<Token>();
/*      */       }
/*  306 */       return this.ifaceBound;
/*      */     }
/*      */     
/*      */     private List<ClassSignature.IToken> getSignature() {
/*  310 */       if (this.signature == null) {
/*  311 */         this.signature = new ArrayList<ClassSignature.IToken>();
/*      */       }
/*  313 */       return this.signature;
/*      */     }
/*      */     
/*      */     private List<ClassSignature.IToken> getSuffix() {
/*  317 */       if (this.suffix == null) {
/*  318 */         this.suffix = new ArrayList<ClassSignature.IToken>();
/*      */       }
/*  320 */       return this.suffix;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ClassSignature.IToken addTypeArgument(char param1Char) {
/*  330 */       if (this.tail != null) {
/*  331 */         return this.tail.addTypeArgument(param1Char);
/*      */       }
/*      */       
/*  334 */       Token token = new Token(param1Char);
/*  335 */       getSignature().add(token);
/*  336 */       return token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ClassSignature.IToken addTypeArgument(String param1String) {
/*  346 */       if (this.tail != null) {
/*  347 */         return this.tail.addTypeArgument(param1String);
/*      */       }
/*      */       
/*  350 */       Token token = new Token(param1String);
/*  351 */       getSignature().add(token);
/*  352 */       return token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ClassSignature.IToken addTypeArgument(Token param1Token) {
/*  362 */       if (this.tail != null) {
/*  363 */         return this.tail.addTypeArgument(param1Token);
/*      */       }
/*      */       
/*  366 */       getSignature().add(param1Token);
/*  367 */       return param1Token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ClassSignature.IToken addTypeArgument(ClassSignature.TokenHandle param1TokenHandle) {
/*  377 */       if (this.tail != null) {
/*  378 */         return this.tail.addTypeArgument(param1TokenHandle);
/*      */       }
/*      */       
/*  381 */       ClassSignature.TokenHandle tokenHandle = param1TokenHandle.clone();
/*  382 */       getSignature().add(tokenHandle);
/*  383 */       return tokenHandle;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Token addBound(String param1String, boolean param1Boolean) {
/*  395 */       if (param1Boolean) {
/*  396 */         return addClassBound(param1String);
/*      */       }
/*      */       
/*  399 */       return addInterfaceBound(param1String);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Token addClassBound(String param1String) {
/*  409 */       Token token = new Token(param1String);
/*  410 */       getClassBound().add(token);
/*  411 */       return token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Token addInterfaceBound(String param1String) {
/*  421 */       Token token = new Token(param1String);
/*  422 */       getIfaceBound().add(token);
/*  423 */       return token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Token addInnerClass(String param1String) {
/*  433 */       this.tail = new Token(param1String, true);
/*  434 */       getSuffix().add(this.tail);
/*  435 */       return this.tail;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  443 */       return asType();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String asBound() {
/*  451 */       StringBuilder stringBuilder = new StringBuilder();
/*      */       
/*  453 */       if (this.type != null) {
/*  454 */         stringBuilder.append(this.type);
/*      */       }
/*      */       
/*  457 */       if (this.classBound != null) {
/*  458 */         for (Token token : this.classBound) {
/*  459 */           stringBuilder.append(token.asType());
/*      */         }
/*      */       }
/*      */       
/*  463 */       if (this.ifaceBound != null) {
/*  464 */         for (Token token : this.ifaceBound) {
/*  465 */           stringBuilder.append(':').append(token.asType());
/*      */         }
/*      */       }
/*      */       
/*  469 */       return stringBuilder.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String asType() {
/*  477 */       return asType(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String asType(boolean param1Boolean) {
/*  488 */       StringBuilder stringBuilder = new StringBuilder();
/*      */       
/*  490 */       if (this.array) {
/*  491 */         stringBuilder.append('[');
/*      */       }
/*      */       
/*  494 */       if (this.symbol != '\000') {
/*  495 */         stringBuilder.append(this.symbol);
/*      */       }
/*      */       
/*  498 */       if (this.type == null) {
/*  499 */         return stringBuilder.toString();
/*      */       }
/*      */       
/*  502 */       if (!this.inner) {
/*  503 */         stringBuilder.append('L');
/*      */       }
/*      */       
/*  506 */       stringBuilder.append(this.type);
/*      */       
/*  508 */       if (!param1Boolean) {
/*  509 */         if (this.signature != null) {
/*  510 */           stringBuilder.append('<');
/*  511 */           for (ClassSignature.IToken iToken : this.signature) {
/*  512 */             stringBuilder.append(iToken.asType());
/*      */           }
/*  514 */           stringBuilder.append('>');
/*      */         } 
/*      */         
/*  517 */         if (this.suffix != null) {
/*  518 */           for (ClassSignature.IToken iToken : this.suffix) {
/*  519 */             stringBuilder.append('.').append(iToken.asType());
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  524 */       if (!this.inner) {
/*  525 */         stringBuilder.append(';');
/*      */       }
/*      */       
/*  528 */       return stringBuilder.toString();
/*      */     }
/*      */     
/*      */     boolean isRaw() {
/*  532 */       return (this.signature == null);
/*      */     }
/*      */     
/*      */     String getClassType() {
/*  536 */       return (this.type != null) ? this.type : "java/lang/Object";
/*      */     }
/*      */ 
/*      */     
/*      */     public Token asToken() {
/*  541 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class TokenHandle
/*      */     implements IToken
/*      */   {
/*      */     final ClassSignature.Token token;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean array;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     char wildcard;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TokenHandle() {
/*  570 */       this(new ClassSignature.Token());
/*      */     }
/*      */     
/*      */     TokenHandle(ClassSignature.Token param1Token) {
/*  574 */       this.token = param1Token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassSignature.IToken setArray(boolean param1Boolean) {
/*  583 */       this.array |= param1Boolean;
/*  584 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassSignature.IToken setWildcard(char param1Char) {
/*  593 */       if ("+-".indexOf(param1Char) > -1) {
/*  594 */         this.wildcard = param1Char;
/*      */       }
/*  596 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String asBound() {
/*  604 */       return this.token.asBound();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String asType() {
/*  612 */       StringBuilder stringBuilder = new StringBuilder();
/*      */       
/*  614 */       if (this.wildcard > '\000') {
/*  615 */         stringBuilder.append(this.wildcard);
/*      */       }
/*      */       
/*  618 */       if (this.array) {
/*  619 */         stringBuilder.append('[');
/*      */       }
/*      */       
/*  622 */       return stringBuilder.append(ClassSignature.this.getTypeVar(this)).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassSignature.Token asToken() {
/*  630 */       return this.token;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  638 */       return this.token.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TokenHandle clone() {
/*  646 */       return new TokenHandle(this.token);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class SignatureParser
/*      */     extends SignatureVisitor
/*      */   {
/*      */     private FormalParamElement param;
/*      */ 
/*      */ 
/*      */     
/*      */     abstract class SignatureElement
/*      */       extends SignatureVisitor
/*      */     {
/*      */       public SignatureElement() {
/*  664 */         super(327680);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract class TokenElement
/*      */       extends SignatureElement
/*      */     {
/*      */       protected ClassSignature.Token token;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       private boolean array;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public ClassSignature.Token getToken() {
/*  686 */         if (this.token == null) {
/*  687 */           this.token = new ClassSignature.Token();
/*      */         }
/*  689 */         return this.token;
/*      */       }
/*      */       
/*      */       protected void setArray() {
/*  693 */         this.array = true;
/*      */       }
/*      */       
/*      */       private boolean getArray() {
/*  697 */         boolean bool = this.array;
/*  698 */         this.array = false;
/*  699 */         return bool;
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitClassType(String param2String) {
/*  704 */         getToken().setType(param2String);
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitClassBound() {
/*  709 */         getToken();
/*  710 */         return new ClassSignature.SignatureParser.BoundElement(this, true);
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitInterfaceBound() {
/*  715 */         getToken();
/*  716 */         return new ClassSignature.SignatureParser.BoundElement(this, false);
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitInnerClassType(String param2String) {
/*  721 */         this.token.addInnerClass(param2String);
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitArrayType() {
/*  726 */         setArray();
/*  727 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitTypeArgument(char param2Char) {
/*  732 */         return new ClassSignature.SignatureParser.TypeArgElement(this, param2Char);
/*      */       }
/*      */       
/*      */       ClassSignature.Token addTypeArgument() {
/*  736 */         return this.token.addTypeArgument('*').asToken();
/*      */       }
/*      */       
/*      */       ClassSignature.IToken addTypeArgument(char param2Char) {
/*  740 */         return this.token.addTypeArgument(param2Char).setArray(getArray());
/*      */       }
/*      */       
/*      */       ClassSignature.IToken addTypeArgument(String param2String) {
/*  744 */         return this.token.addTypeArgument(param2String).setArray(getArray());
/*      */       }
/*      */       
/*      */       ClassSignature.IToken addTypeArgument(ClassSignature.Token param2Token) {
/*  748 */         return this.token.addTypeArgument(param2Token).setArray(getArray());
/*      */       }
/*      */       
/*      */       ClassSignature.IToken addTypeArgument(ClassSignature.TokenHandle param2TokenHandle) {
/*  752 */         return this.token.addTypeArgument(param2TokenHandle).setArray(getArray());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class FormalParamElement
/*      */       extends TokenElement
/*      */     {
/*      */       private final ClassSignature.TokenHandle handle;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       FormalParamElement(String param2String) {
/*  768 */         this.handle = ClassSignature.this.getType(param2String);
/*  769 */         this.token = this.handle.asToken();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class TypeArgElement
/*      */       extends TokenElement
/*      */     {
/*      */       private final ClassSignature.SignatureParser.TokenElement type;
/*      */ 
/*      */ 
/*      */       
/*      */       private final char wildcard;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       TypeArgElement(ClassSignature.SignatureParser.TokenElement param2TokenElement, char param2Char) {
/*  790 */         this.type = param2TokenElement;
/*  791 */         this.wildcard = param2Char;
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitArrayType() {
/*  796 */         this.type.setArray();
/*  797 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitBaseType(char param2Char) {
/*  802 */         this.token = this.type.addTypeArgument(param2Char).asToken();
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitTypeVariable(String param2String) {
/*  807 */         ClassSignature.TokenHandle tokenHandle = ClassSignature.this.getType(param2String);
/*  808 */         this.token = this.type.addTypeArgument(tokenHandle).setWildcard(this.wildcard).asToken();
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitClassType(String param2String) {
/*  813 */         this.token = this.type.addTypeArgument(param2String).setWildcard(this.wildcard).asToken();
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitTypeArgument() {
/*  818 */         this.token.addTypeArgument('*');
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitTypeArgument(char param2Char) {
/*  823 */         return new TypeArgElement(this, param2Char);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void visitEnd() {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class BoundElement
/*      */       extends TokenElement
/*      */     {
/*      */       private final ClassSignature.SignatureParser.TokenElement type;
/*      */ 
/*      */ 
/*      */       
/*      */       private final boolean classBound;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       BoundElement(ClassSignature.SignatureParser.TokenElement param2TokenElement, boolean param2Boolean) {
/*  848 */         this.type = param2TokenElement;
/*  849 */         this.classBound = param2Boolean;
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitClassType(String param2String) {
/*  854 */         this.token = this.type.token.addBound(param2String, this.classBound);
/*      */       }
/*      */ 
/*      */       
/*      */       public void visitTypeArgument() {
/*  859 */         this.token.addTypeArgument('*');
/*      */       }
/*      */ 
/*      */       
/*      */       public SignatureVisitor visitTypeArgument(char param2Char) {
/*  864 */         return new ClassSignature.SignatureParser.TypeArgElement(this, param2Char);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class SuperClassElement
/*      */       extends TokenElement
/*      */     {
/*      */       public void visitEnd() {
/*  876 */         ClassSignature.this.setSuperClass(this.token);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class InterfaceElement
/*      */       extends TokenElement
/*      */     {
/*      */       public void visitEnd() {
/*  888 */         ClassSignature.this.addInterface(this.token);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SignatureParser() {
/*  901 */       super(327680);
/*      */     }
/*      */ 
/*      */     
/*      */     public void visitFormalTypeParameter(String param1String) {
/*  906 */       this.param = new FormalParamElement(param1String);
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureVisitor visitClassBound() {
/*  911 */       return this.param.visitClassBound();
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureVisitor visitInterfaceBound() {
/*  916 */       return this.param.visitInterfaceBound();
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureVisitor visitSuperclass() {
/*  921 */       return new SuperClassElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureVisitor visitInterface() {
/*  926 */       return new InterfaceElement();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class SignatureRemapper
/*      */     extends SignatureWriter
/*      */   {
/*  936 */     private final Set<String> localTypeVars = new HashSet<String>();
/*      */ 
/*      */     
/*      */     public void visitFormalTypeParameter(String param1String) {
/*  940 */       this.localTypeVars.add(param1String);
/*  941 */       super.visitFormalTypeParameter(param1String);
/*      */     }
/*      */ 
/*      */     
/*      */     public void visitTypeVariable(String param1String) {
/*  946 */       if (!this.localTypeVars.contains(param1String)) {
/*  947 */         ClassSignature.TypeVar typeVar = ClassSignature.this.getTypeVar(param1String);
/*  948 */         if (typeVar != null) {
/*  949 */           super.visitTypeVariable(typeVar.toString());
/*      */           return;
/*      */         } 
/*      */       } 
/*  953 */       super.visitTypeVariable(param1String);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  962 */   private final Map<TypeVar, TokenHandle> types = new LinkedHashMap<TypeVar, TokenHandle>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  967 */   private Token superClass = new Token("java/lang/Object");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  972 */   private final List<Token> interfaces = new ArrayList<Token>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  977 */   private final Deque<String> rawInterfaces = new LinkedList<String>();
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
/*      */   private ClassSignature read(String paramString) {
/*  990 */     if (paramString != null) {
/*      */       try {
/*  992 */         (new SignatureReader(paramString)).accept(new SignatureParser());
/*  993 */       } catch (Exception exception) {
/*  994 */         exception.printStackTrace();
/*      */       } 
/*      */     }
/*  997 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeVar getTypeVar(String paramString) {
/* 1007 */     for (TypeVar typeVar : this.types.keySet()) {
/* 1008 */       if (typeVar.matches(paramString)) {
/* 1009 */         return typeVar;
/*      */       }
/*      */     } 
/* 1012 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TokenHandle getType(String paramString) {
/* 1022 */     for (TypeVar typeVar : this.types.keySet()) {
/* 1023 */       if (typeVar.matches(paramString)) {
/* 1024 */         return this.types.get(typeVar);
/*      */       }
/*      */     } 
/*      */     
/* 1028 */     TokenHandle tokenHandle = new TokenHandle();
/* 1029 */     this.types.put(new TypeVar(paramString), tokenHandle);
/* 1030 */     return tokenHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getTypeVar(TokenHandle paramTokenHandle) {
/* 1041 */     for (Map.Entry<TypeVar, TokenHandle> entry : this.types.entrySet()) {
/* 1042 */       TypeVar typeVar = (TypeVar)entry.getKey();
/* 1043 */       TokenHandle tokenHandle = (TokenHandle)entry.getValue();
/* 1044 */       if (paramTokenHandle == tokenHandle || paramTokenHandle.asToken() == tokenHandle.asToken()) {
/* 1045 */         return "T" + typeVar + ";";
/*      */       }
/*      */     } 
/* 1048 */     return paramTokenHandle.token.asType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addTypeVar(TypeVar paramTypeVar, TokenHandle paramTokenHandle) throws IllegalArgumentException {
/* 1059 */     if (this.types.containsKey(paramTypeVar)) {
/* 1060 */       throw new IllegalArgumentException("TypeVar " + paramTypeVar + " is already present on " + this);
/*      */     }
/*      */     
/* 1063 */     this.types.put(paramTypeVar, paramTokenHandle);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setSuperClass(Token paramToken) {
/* 1072 */     this.superClass = paramToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSuperClass() {
/* 1081 */     return this.superClass.asType(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addInterface(Token paramToken) {
/* 1090 */     if (!paramToken.isRaw()) {
/* 1091 */       String str = paramToken.asType(true);
/* 1092 */       for (ListIterator<Token> listIterator = this.interfaces.listIterator(); listIterator.hasNext(); ) {
/* 1093 */         Token token = listIterator.next();
/* 1094 */         if (token.isRaw() && token.asType(true).equals(str)) {
/* 1095 */           listIterator.set(paramToken);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1101 */     this.interfaces.add(paramToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInterface(String paramString) {
/* 1110 */     this.rawInterfaces.add(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addRawInterface(String paramString) {
/* 1119 */     Token token = new Token(paramString);
/* 1120 */     String str = token.asType(true);
/* 1121 */     for (Token token1 : this.interfaces) {
/* 1122 */       if (token1.asType(true).equals(str)) {
/*      */         return;
/*      */       }
/*      */     } 
/* 1126 */     this.interfaces.add(token);
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
/*      */   public void merge(ClassSignature paramClassSignature) {
/*      */     try {
/* 1140 */       HashSet<String> hashSet = new HashSet();
/* 1141 */       for (TypeVar typeVar : this.types.keySet()) {
/* 1142 */         hashSet.add(typeVar.toString());
/*      */       }
/*      */       
/* 1145 */       paramClassSignature.conform(hashSet);
/* 1146 */     } catch (IllegalStateException illegalStateException) {
/*      */       
/* 1148 */       illegalStateException.printStackTrace();
/*      */       
/*      */       return;
/*      */     } 
/* 1152 */     for (Map.Entry<TypeVar, TokenHandle> entry : paramClassSignature.types.entrySet()) {
/* 1153 */       addTypeVar((TypeVar)entry.getKey(), (TokenHandle)entry.getValue());
/*      */     }
/*      */     
/* 1156 */     for (Token token : paramClassSignature.interfaces) {
/* 1157 */       addInterface(token);
/*      */     }
/*      */   }
/*      */   
/*      */   private void conform(Set<String> paramSet) {
/* 1162 */     for (TypeVar typeVar : this.types.keySet()) {
/* 1163 */       String str = findUniqueName(typeVar.getOriginalName(), paramSet);
/* 1164 */       typeVar.rename(str);
/* 1165 */       paramSet.add(str);
/*      */     } 
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
/*      */   private String findUniqueName(String paramString, Set<String> paramSet) {
/* 1178 */     if (!paramSet.contains(paramString)) {
/* 1179 */       return paramString;
/*      */     }
/*      */     
/* 1182 */     if (paramString.length() == 1) {
/* 1183 */       String str1 = findOffsetName(paramString.charAt(0), paramSet);
/* 1184 */       if (str1 != null) {
/* 1185 */         return str1;
/*      */       }
/*      */     } 
/*      */     
/* 1189 */     String str = findOffsetName('T', paramSet, "", paramString);
/* 1190 */     if (str != null) {
/* 1191 */       return str;
/*      */     }
/*      */     
/* 1194 */     str = findOffsetName('T', paramSet, paramString, "");
/* 1195 */     if (str != null) {
/* 1196 */       return str;
/*      */     }
/*      */     
/* 1199 */     str = findOffsetName('T', paramSet, "T", paramString);
/* 1200 */     if (str != null) {
/* 1201 */       return str;
/*      */     }
/*      */     
/* 1204 */     str = findOffsetName('T', paramSet, "", paramString + "Type");
/* 1205 */     if (str != null) {
/* 1206 */       return str;
/*      */     }
/*      */     
/* 1209 */     throw new IllegalStateException("Failed to conform type var: " + paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String findOffsetName(char paramChar, Set<String> paramSet) {
/* 1220 */     return findOffsetName(paramChar, paramSet, "", "");
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
/*      */   private String findOffsetName(char paramChar, Set<String> paramSet, String paramString1, String paramString2) {
/* 1233 */     String str = String.format("%s%s%s", new Object[] { paramString1, Character.valueOf(paramChar), paramString2 });
/* 1234 */     if (!paramSet.contains(str)) {
/* 1235 */       return str;
/*      */     }
/*      */     
/* 1238 */     if (paramChar > '@' && paramChar < '[') {
/* 1239 */       int i; for (i = paramChar - 64; i + 65 != paramChar; i = ++i % 26) {
/* 1240 */         str = String.format("%s%s%s", new Object[] { paramString1, Character.valueOf((char)(i + 65)), paramString2 });
/* 1241 */         if (!paramSet.contains(str)) {
/* 1242 */           return str;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1247 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SignatureVisitor getRemapper() {
/* 1256 */     return (SignatureVisitor)new SignatureRemapper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1267 */     while (this.rawInterfaces.size() > 0) {
/* 1268 */       addRawInterface(this.rawInterfaces.remove());
/*      */     }
/*      */     
/* 1271 */     StringBuilder stringBuilder = new StringBuilder();
/*      */     
/* 1273 */     if (this.types.size() > 0) {
/* 1274 */       boolean bool = false;
/* 1275 */       StringBuilder stringBuilder1 = new StringBuilder();
/* 1276 */       for (Map.Entry<TypeVar, TokenHandle> entry : this.types.entrySet()) {
/* 1277 */         String str = ((TokenHandle)entry.getValue()).asBound();
/* 1278 */         if (!str.isEmpty()) {
/* 1279 */           stringBuilder1.append(entry.getKey()).append(':').append(str);
/* 1280 */           bool = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1284 */       if (bool) {
/* 1285 */         stringBuilder.append('<').append(stringBuilder1).append('>');
/*      */       }
/*      */     } 
/*      */     
/* 1289 */     stringBuilder.append(this.superClass.asType());
/*      */     
/* 1291 */     for (Token token : this.interfaces) {
/* 1292 */       stringBuilder.append(token.asType());
/*      */     }
/*      */     
/* 1295 */     return stringBuilder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassSignature wake() {
/* 1302 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassSignature of(String paramString) {
/* 1312 */     return (new ClassSignature()).read(paramString);
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
/*      */   public static ClassSignature of(ClassNode paramClassNode) {
/* 1324 */     if (paramClassNode.signature != null) {
/* 1325 */       return of(paramClassNode.signature);
/*      */     }
/*      */     
/* 1328 */     return generate(paramClassNode);
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
/*      */   public static ClassSignature ofLazy(ClassNode paramClassNode) {
/* 1341 */     if (paramClassNode.signature != null) {
/* 1342 */       return new Lazy(paramClassNode.signature);
/*      */     }
/*      */     
/* 1345 */     return generate(paramClassNode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassSignature generate(ClassNode paramClassNode) {
/* 1355 */     ClassSignature classSignature = new ClassSignature();
/* 1356 */     classSignature.setSuperClass(new Token((paramClassNode.superName != null) ? paramClassNode.superName : "java/lang/Object"));
/* 1357 */     for (String str : paramClassNode.interfaces) {
/* 1358 */       classSignature.addInterface(new Token(str));
/*      */     }
/* 1360 */     return classSignature;
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\ClassSignature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */