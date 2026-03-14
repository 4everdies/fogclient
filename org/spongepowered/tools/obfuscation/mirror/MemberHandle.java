/*    */ package org.spongepowered.tools.obfuscation.mirror;
/*    */ 
/*    */ import org.spongepowered.asm.obfuscation.mapping.IMapping;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class MemberHandle<T extends IMapping<T>>
/*    */ {
/*    */   private final String owner;
/*    */   private final String name;
/*    */   private final String desc;
/*    */   
/*    */   protected MemberHandle(String paramString1, String paramString2, String paramString3) {
/* 43 */     this.owner = paramString1;
/* 44 */     this.name = paramString2;
/* 45 */     this.desc = paramString3;
/*    */   }
/*    */   
/*    */   public final String getOwner() {
/* 49 */     return this.owner;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 53 */     return this.name;
/*    */   }
/*    */   
/*    */   public final String getDesc() {
/* 57 */     return this.desc;
/*    */   }
/*    */   
/*    */   public abstract Visibility getVisibility();
/*    */   
/*    */   public abstract T asMapping(boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mirror\MemberHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */