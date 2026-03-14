/*    */ package org.spongepowered.tools.obfuscation.mapping.mcp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import javax.annotation.processing.Filer;
/*    */ import javax.annotation.processing.Messager;
/*    */ import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
/*    */ import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
/*    */ import org.spongepowered.tools.obfuscation.ObfuscationType;
/*    */ import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
/*    */ import org.spongepowered.tools.obfuscation.mapping.common.MappingWriter;
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
/*    */ public class MappingWriterSrg
/*    */   extends MappingWriter
/*    */ {
/*    */   public MappingWriterSrg(Messager paramMessager, Filer paramFiler) {
/* 46 */     super(paramMessager, paramFiler);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(String paramString, ObfuscationType paramObfuscationType, IMappingConsumer.MappingSet<MappingField> paramMappingSet, IMappingConsumer.MappingSet<MappingMethod> paramMappingSet1) {
/* 51 */     if (paramString == null) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     PrintWriter printWriter = null;
/*    */     
/*    */     try {
/* 58 */       printWriter = openFileWriter(paramString, paramObfuscationType + " output SRGs");
/* 59 */       writeFieldMappings(printWriter, paramMappingSet);
/* 60 */       writeMethodMappings(printWriter, paramMappingSet1);
/* 61 */     } catch (IOException iOException) {
/* 62 */       iOException.printStackTrace();
/*    */     } finally {
/* 64 */       if (printWriter != null) {
/*    */         try {
/* 66 */           printWriter.close();
/* 67 */         } catch (Exception exception) {}
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeFieldMappings(PrintWriter paramPrintWriter, IMappingConsumer.MappingSet<MappingField> paramMappingSet) {
/* 75 */     for (IMappingConsumer.MappingSet.Pair<MappingField> pair : paramMappingSet) {
/* 76 */       paramPrintWriter.println(formatFieldMapping(pair));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void writeMethodMappings(PrintWriter paramPrintWriter, IMappingConsumer.MappingSet<MappingMethod> paramMappingSet) {
/* 81 */     for (IMappingConsumer.MappingSet.Pair<MappingMethod> pair : paramMappingSet) {
/* 82 */       paramPrintWriter.println(formatMethodMapping(pair));
/*    */     }
/*    */   }
/*    */   
/*    */   protected String formatFieldMapping(IMappingConsumer.MappingSet.Pair<MappingField> paramPair) {
/* 87 */     return String.format("FD: %s/%s %s/%s", new Object[] { ((MappingField)paramPair.from).getOwner(), ((MappingField)paramPair.from).getName(), ((MappingField)paramPair.to).getOwner(), ((MappingField)paramPair.to).getName() });
/*    */   }
/*    */   
/*    */   protected String formatMethodMapping(IMappingConsumer.MappingSet.Pair<MappingMethod> paramPair) {
/* 91 */     return String.format("MD: %s %s %s %s", new Object[] { ((MappingMethod)paramPair.from).getName(), ((MappingMethod)paramPair.from).getDesc(), ((MappingMethod)paramPair.to).getName(), ((MappingMethod)paramPair.to).getDesc() });
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mapping\mcp\MappingWriterSrg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */