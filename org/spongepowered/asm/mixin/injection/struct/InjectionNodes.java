/*     */ package org.spongepowered.asm.mixin.injection.struct;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InjectionNodes
/*     */   extends ArrayList<InjectionNodes.InjectionNode>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static class InjectionNode
/*     */     implements Comparable<InjectionNode>
/*     */   {
/*  58 */     private static int nextId = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final AbstractInsnNode originalTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private AbstractInsnNode currentTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Map<String, Object> decorations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectionNode(AbstractInsnNode param1AbstractInsnNode) {
/*  88 */       this.currentTarget = this.originalTarget = param1AbstractInsnNode;
/*  89 */       this.id = nextId++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getId() {
/*  96 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode getOriginalTarget() {
/* 103 */       return this.originalTarget;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AbstractInsnNode getCurrentTarget() {
/* 111 */       return this.currentTarget;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectionNode replace(AbstractInsnNode param1AbstractInsnNode) {
/* 120 */       this.currentTarget = param1AbstractInsnNode;
/* 121 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InjectionNode remove() {
/* 128 */       this.currentTarget = null;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(AbstractInsnNode param1AbstractInsnNode) {
/* 141 */       return (this.originalTarget == param1AbstractInsnNode || this.currentTarget == param1AbstractInsnNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isReplaced() {
/* 148 */       return (this.originalTarget != this.currentTarget);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRemoved() {
/* 155 */       return (this.currentTarget == null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V> InjectionNode decorate(String param1String, V param1V) {
/* 166 */       if (this.decorations == null) {
/* 167 */         this.decorations = new HashMap<String, Object>();
/*     */       }
/* 169 */       this.decorations.put(param1String, param1V);
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasDecoration(String param1String) {
/* 180 */       return (this.decorations != null && this.decorations.get(param1String) != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V> V getDecoration(String param1String) {
/* 192 */       return (this.decorations == null) ? null : (V)this.decorations.get(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(InjectionNode param1InjectionNode) {
/* 200 */       return (param1InjectionNode == null) ? Integer.MAX_VALUE : (hashCode() - param1InjectionNode.hashCode());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 208 */       return String.format("InjectionNode[%s]", new Object[] { Bytecode.describeNode(this.currentTarget).replaceAll("\\s+", " ") });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionNode add(AbstractInsnNode paramAbstractInsnNode) {
/* 220 */     InjectionNode injectionNode = get(paramAbstractInsnNode);
/* 221 */     if (injectionNode == null) {
/* 222 */       injectionNode = new InjectionNode(paramAbstractInsnNode);
/* 223 */       add(injectionNode);
/*     */     } 
/* 225 */     return injectionNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionNode get(AbstractInsnNode paramAbstractInsnNode) {
/* 236 */     for (InjectionNode injectionNode : this) {
/* 237 */       if (injectionNode.matches(paramAbstractInsnNode)) {
/* 238 */         return injectionNode;
/*     */       }
/*     */     } 
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(AbstractInsnNode paramAbstractInsnNode) {
/* 251 */     return (get(paramAbstractInsnNode) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(AbstractInsnNode paramAbstractInsnNode1, AbstractInsnNode paramAbstractInsnNode2) {
/* 262 */     InjectionNode injectionNode = get(paramAbstractInsnNode1);
/* 263 */     if (injectionNode != null) {
/* 264 */       injectionNode.replace(paramAbstractInsnNode2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(AbstractInsnNode paramAbstractInsnNode) {
/* 275 */     InjectionNode injectionNode = get(paramAbstractInsnNode);
/* 276 */     if (injectionNode != null)
/* 277 */       injectionNode.remove(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\injection\struct\InjectionNodes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */