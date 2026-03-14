/*    */ package org.spongepowered.asm.util.throwables;
/*    */ 
/*    */ import org.spongepowered.asm.util.ConstraintParser;
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
/*    */ public class ConstraintViolationException
/*    */   extends Exception
/*    */ {
/*    */   private static final String MISSING_VALUE = "UNRESOLVED";
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final ConstraintParser.Constraint constraint;
/*    */   private final String badValue;
/*    */   
/*    */   public ConstraintViolationException(ConstraintParser.Constraint paramConstraint) {
/* 43 */     this.constraint = paramConstraint;
/* 44 */     this.badValue = "UNRESOLVED";
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(ConstraintParser.Constraint paramConstraint, int paramInt) {
/* 48 */     this.constraint = paramConstraint;
/* 49 */     this.badValue = String.valueOf(paramInt);
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(String paramString, ConstraintParser.Constraint paramConstraint) {
/* 53 */     super(paramString);
/* 54 */     this.constraint = paramConstraint;
/* 55 */     this.badValue = "UNRESOLVED";
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(String paramString, ConstraintParser.Constraint paramConstraint, int paramInt) {
/* 59 */     super(paramString);
/* 60 */     this.constraint = paramConstraint;
/* 61 */     this.badValue = String.valueOf(paramInt);
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(Throwable paramThrowable, ConstraintParser.Constraint paramConstraint) {
/* 65 */     super(paramThrowable);
/* 66 */     this.constraint = paramConstraint;
/* 67 */     this.badValue = "UNRESOLVED";
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(Throwable paramThrowable, ConstraintParser.Constraint paramConstraint, int paramInt) {
/* 71 */     super(paramThrowable);
/* 72 */     this.constraint = paramConstraint;
/* 73 */     this.badValue = String.valueOf(paramInt);
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(String paramString, Throwable paramThrowable, ConstraintParser.Constraint paramConstraint) {
/* 77 */     super(paramString, paramThrowable);
/* 78 */     this.constraint = paramConstraint;
/* 79 */     this.badValue = "UNRESOLVED";
/*    */   }
/*    */   
/*    */   public ConstraintViolationException(String paramString, Throwable paramThrowable, ConstraintParser.Constraint paramConstraint, int paramInt) {
/* 83 */     super(paramString, paramThrowable);
/* 84 */     this.constraint = paramConstraint;
/* 85 */     this.badValue = String.valueOf(paramInt);
/*    */   }
/*    */   
/*    */   public ConstraintParser.Constraint getConstraint() {
/* 89 */     return this.constraint;
/*    */   }
/*    */   
/*    */   public String getBadValue() {
/* 93 */     return this.badValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\throwables\ConstraintViolationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */