/*     */ package org.spongepowered.asm.lib.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.MethodVisitor;
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
/*     */ public class FrameNode
/*     */   extends AbstractInsnNode
/*     */ {
/*     */   public int type;
/*     */   public List<Object> local;
/*     */   public List<Object> stack;
/*     */   
/*     */   private FrameNode() {
/*  81 */     super(-1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FrameNode(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2) {
/* 110 */     super(-1);
/* 111 */     this.type = paramInt1;
/* 112 */     switch (paramInt1) {
/*     */       case -1:
/*     */       case 0:
/* 115 */         this.local = asList(paramInt2, paramArrayOfObject1);
/* 116 */         this.stack = asList(paramInt3, paramArrayOfObject2);
/*     */         break;
/*     */       case 1:
/* 119 */         this.local = asList(paramInt2, paramArrayOfObject1);
/*     */         break;
/*     */       case 2:
/* 122 */         this.local = Arrays.asList(new Object[paramInt2]);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 127 */         this.stack = asList(1, paramArrayOfObject2);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/* 134 */     return 14;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MethodVisitor paramMethodVisitor) {
/* 145 */     switch (this.type) {
/*     */       case -1:
/*     */       case 0:
/* 148 */         paramMethodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), 
/* 149 */             asArray(this.stack));
/*     */         break;
/*     */       case 1:
/* 152 */         paramMethodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), 0, null);
/*     */         break;
/*     */       case 2:
/* 155 */         paramMethodVisitor.visitFrame(this.type, this.local.size(), null, 0, null);
/*     */         break;
/*     */       case 3:
/* 158 */         paramMethodVisitor.visitFrame(this.type, 0, null, 0, null);
/*     */         break;
/*     */       case 4:
/* 161 */         paramMethodVisitor.visitFrame(this.type, 0, null, 1, asArray(this.stack));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractInsnNode clone(Map<LabelNode, LabelNode> paramMap) {
/* 168 */     FrameNode frameNode = new FrameNode();
/* 169 */     frameNode.type = this.type;
/* 170 */     if (this.local != null) {
/* 171 */       frameNode.local = new ArrayList();
/* 172 */       for (byte b = 0; b < this.local.size(); b++) {
/* 173 */         Object object = this.local.get(b);
/* 174 */         if (object instanceof LabelNode) {
/* 175 */           object = paramMap.get(object);
/*     */         }
/* 177 */         frameNode.local.add(object);
/*     */       } 
/*     */     } 
/* 180 */     if (this.stack != null) {
/* 181 */       frameNode.stack = new ArrayList();
/* 182 */       for (byte b = 0; b < this.stack.size(); b++) {
/* 183 */         Object object = this.stack.get(b);
/* 184 */         if (object instanceof LabelNode) {
/* 185 */           object = paramMap.get(object);
/*     */         }
/* 187 */         frameNode.stack.add(object);
/*     */       } 
/*     */     } 
/* 190 */     return frameNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Object> asList(int paramInt, Object[] paramArrayOfObject) {
/* 196 */     return Arrays.<Object>asList(paramArrayOfObject).subList(0, paramInt);
/*     */   }
/*     */   
/*     */   private static Object[] asArray(List<Object> paramList) {
/* 200 */     Object[] arrayOfObject = new Object[paramList.size()];
/* 201 */     for (byte b = 0; b < arrayOfObject.length; b++) {
/* 202 */       Object object = paramList.get(b);
/* 203 */       if (object instanceof LabelNode) {
/* 204 */         object = ((LabelNode)object).getLabel();
/*     */       }
/* 206 */       arrayOfObject[b] = object;
/*     */     } 
/* 208 */     return arrayOfObject;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\tree\FrameNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */