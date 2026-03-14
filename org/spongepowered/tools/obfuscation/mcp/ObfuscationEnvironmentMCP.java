/*    */ package org.spongepowered.tools.obfuscation.mcp;
/*    */ 
/*    */ import javax.annotation.processing.Filer;
/*    */ import javax.annotation.processing.Messager;
/*    */ import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;
/*    */ import org.spongepowered.tools.obfuscation.ObfuscationType;
/*    */ import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
/*    */ import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
/*    */ import org.spongepowered.tools.obfuscation.mapping.mcp.MappingProviderSrg;
/*    */ import org.spongepowered.tools.obfuscation.mapping.mcp.MappingWriterSrg;
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
/*    */ public class ObfuscationEnvironmentMCP
/*    */   extends ObfuscationEnvironment
/*    */ {
/*    */   protected ObfuscationEnvironmentMCP(ObfuscationType paramObfuscationType) {
/* 43 */     super(paramObfuscationType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IMappingProvider getMappingProvider(Messager paramMessager, Filer paramFiler) {
/* 48 */     return (IMappingProvider)new MappingProviderSrg(paramMessager, paramFiler);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IMappingWriter getMappingWriter(Messager paramMessager, Filer paramFiler) {
/* 53 */     return (IMappingWriter)new MappingWriterSrg(paramMessager, paramFiler);
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\tools\obfuscation\mcp\ObfuscationEnvironmentMCP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */