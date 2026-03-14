/*    */ package org.spongepowered.tools.obfuscation.mapping.common;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import javax.annotation.processing.Filer;
/*    */ import javax.annotation.processing.Messager;
/*    */ import javax.tools.Diagnostic;
/*    */ import javax.tools.FileObject;
/*    */ import javax.tools.StandardLocation;
/*    */ import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
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
/*    */ public abstract class MappingWriter
/*    */   implements IMappingWriter
/*    */ {
/*    */   private final Messager messager;
/*    */   private final Filer filer;
/*    */   
/*    */   public MappingWriter(Messager paramMessager, Filer paramFiler) {
/* 49 */     this.messager = paramMessager;
/* 50 */     this.filer = paramFiler;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PrintWriter openFileWriter(String paramString1, String paramString2) throws IOException {
/* 62 */     if (paramString1.matches("^.*[\\\\/:].*$")) {
/* 63 */       File file = new File(paramString1);
/* 64 */       file.getParentFile().mkdirs();
/* 65 */       this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + paramString2 + " to " + file.getAbsolutePath());
/* 66 */       return new PrintWriter(file);
/*    */     } 
/*    */     
/* 69 */     FileObject fileObject = this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", paramString1, new javax.lang.model.element.Element[0]);
/* 70 */     this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + paramString2 + " to " + (new File(fileObject.toUri())).getAbsolutePath());
/* 71 */     return new PrintWriter(fileObject.openWriter());
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mapping\common\MappingWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */