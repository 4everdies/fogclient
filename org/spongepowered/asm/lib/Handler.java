/*     */ package org.spongepowered.asm.lib;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Handler
/*     */ {
/*     */   Label start;
/*     */   Label end;
/*     */   Label handler;
/*     */   String desc;
/*     */   int type;
/*     */   Handler next;
/*     */   
/*     */   static Handler remove(Handler paramHandler, Label paramLabel1, Label paramLabel2) {
/*  84 */     if (paramHandler == null) {
/*  85 */       return null;
/*     */     }
/*  87 */     paramHandler.next = remove(paramHandler.next, paramLabel1, paramLabel2);
/*     */     
/*  89 */     int i = paramHandler.start.position;
/*  90 */     int j = paramHandler.end.position;
/*  91 */     int k = paramLabel1.position;
/*  92 */     int m = (paramLabel2 == null) ? Integer.MAX_VALUE : paramLabel2.position;
/*     */     
/*  94 */     if (k < j && m > i) {
/*  95 */       if (k <= i) {
/*  96 */         if (m >= j) {
/*     */           
/*  98 */           paramHandler = paramHandler.next;
/*     */         } else {
/*     */           
/* 101 */           paramHandler.start = paramLabel2;
/*     */         } 
/* 103 */       } else if (m >= j) {
/*     */         
/* 105 */         paramHandler.end = paramLabel1;
/*     */       } else {
/*     */         
/* 108 */         Handler handler = new Handler();
/* 109 */         handler.start = paramLabel2;
/* 110 */         handler.end = paramHandler.end;
/* 111 */         handler.handler = paramHandler.handler;
/* 112 */         handler.desc = paramHandler.desc;
/* 113 */         handler.type = paramHandler.type;
/* 114 */         handler.next = paramHandler.next;
/* 115 */         paramHandler.end = paramLabel1;
/* 116 */         paramHandler.next = handler;
/*     */       } 
/*     */     }
/* 119 */     return paramHandler;
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\lib\Handler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */