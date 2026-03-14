/*     */ package org.spongepowered.asm.mixin.struct;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.ClassNode;
/*     */ import org.spongepowered.asm.lib.tree.LineNumberNode;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
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
/*     */ public class SourceMap
/*     */ {
/*     */   private static final String DEFAULT_STRATUM = "Mixin";
/*     */   private static final String NEWLINE = "\n";
/*     */   private final String sourceFile;
/*     */   
/*     */   public static class File
/*     */   {
/*     */     public final int id;
/*     */     public final int lineOffset;
/*     */     public final int size;
/*     */     public final String sourceFileName;
/*     */     public final String sourceFilePath;
/*     */     
/*     */     public File(int param1Int1, int param1Int2, int param1Int3, String param1String) {
/*  84 */       this(param1Int1, param1Int2, param1Int3, param1String, null);
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
/*     */     public File(int param1Int1, int param1Int2, int param1Int3, String param1String1, String param1String2) {
/*  96 */       this.id = param1Int1;
/*  97 */       this.lineOffset = param1Int2;
/*  98 */       this.size = param1Int3;
/*  99 */       this.sourceFileName = param1String1;
/* 100 */       this.sourceFilePath = param1String2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void applyOffset(ClassNode param1ClassNode) {
/* 110 */       for (MethodNode methodNode : param1ClassNode.methods) {
/* 111 */         applyOffset(methodNode);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void applyOffset(MethodNode param1MethodNode) {
/* 122 */       for (ListIterator<AbstractInsnNode> listIterator = param1MethodNode.instructions.iterator(); listIterator.hasNext(); ) {
/* 123 */         AbstractInsnNode abstractInsnNode = listIterator.next();
/* 124 */         if (abstractInsnNode instanceof LineNumberNode) {
/* 125 */           ((LineNumberNode)abstractInsnNode).line += this.lineOffset - 1;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void appendFile(StringBuilder param1StringBuilder) {
/* 131 */       if (this.sourceFilePath != null) {
/* 132 */         param1StringBuilder.append("+ ").append(this.id).append(" ").append(this.sourceFileName).append("\n");
/* 133 */         param1StringBuilder.append(this.sourceFilePath).append("\n");
/*     */       } else {
/* 135 */         param1StringBuilder.append(this.id).append(" ").append(this.sourceFileName).append("\n");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void appendLines(StringBuilder param1StringBuilder) {
/* 145 */       param1StringBuilder.append("1#").append(this.id)
/* 146 */         .append(",").append(this.size)
/* 147 */         .append(":").append(this.lineOffset)
/* 148 */         .append("\n");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Stratum
/*     */   {
/*     */     private static final String STRATUM_MARK = "*S";
/*     */ 
/*     */     
/*     */     private static final String FILE_MARK = "*F";
/*     */ 
/*     */     
/*     */     private static final String LINES_MARK = "*L";
/*     */ 
/*     */     
/*     */     public final String name;
/*     */     
/* 167 */     private final Map<String, SourceMap.File> files = new LinkedHashMap<String, SourceMap.File>();
/*     */     
/*     */     public Stratum(String param1String) {
/* 170 */       this.name = param1String;
/*     */     }
/*     */     
/*     */     public SourceMap.File addFile(int param1Int1, int param1Int2, String param1String1, String param1String2) {
/* 174 */       SourceMap.File file = this.files.get(param1String2);
/* 175 */       if (file == null) {
/* 176 */         file = new SourceMap.File(this.files.size() + 1, param1Int1, param1Int2, param1String1, param1String2);
/* 177 */         this.files.put(param1String2, file);
/*     */       } 
/* 179 */       return file;
/*     */     }
/*     */     
/*     */     void appendTo(StringBuilder param1StringBuilder) {
/* 183 */       param1StringBuilder.append("*S").append(" ").append(this.name).append("\n");
/*     */       
/* 185 */       param1StringBuilder.append("*F").append("\n");
/* 186 */       for (SourceMap.File file : this.files.values()) {
/* 187 */         file.appendFile(param1StringBuilder);
/*     */       }
/*     */       
/* 190 */       param1StringBuilder.append("*L").append("\n");
/* 191 */       for (SourceMap.File file : this.files.values()) {
/* 192 */         file.appendLines(param1StringBuilder);
/*     */       }
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
/*     */ 
/*     */   
/* 206 */   private final Map<String, Stratum> strata = new LinkedHashMap<String, Stratum>();
/*     */   
/* 208 */   private int nextLineOffset = 1;
/*     */   
/* 210 */   private String defaultStratum = "Mixin";
/*     */   
/*     */   public SourceMap(String paramString) {
/* 213 */     this.sourceFile = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceFile() {
/* 220 */     return this.sourceFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPseudoGeneratedSourceFile() {
/* 227 */     return this.sourceFile.replace(".java", "$mixin.java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File addFile(ClassNode paramClassNode) {
/* 237 */     return addFile(this.defaultStratum, paramClassNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File addFile(String paramString, ClassNode paramClassNode) {
/* 248 */     return addFile(paramString, paramClassNode.sourceFile, paramClassNode.name + ".java", Bytecode.getMaxLineNumber(paramClassNode, 500, 50));
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
/*     */   public File addFile(String paramString1, String paramString2, int paramInt) {
/* 260 */     return addFile(this.defaultStratum, paramString1, paramString2, paramInt);
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
/*     */   public File addFile(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 273 */     Stratum stratum = this.strata.get(paramString1);
/* 274 */     if (stratum == null) {
/* 275 */       stratum = new Stratum(paramString1);
/* 276 */       this.strata.put(paramString1, stratum);
/*     */     } 
/*     */     
/* 279 */     File file = stratum.addFile(this.nextLineOffset, paramInt, paramString2, paramString3);
/* 280 */     this.nextLineOffset += paramInt;
/* 281 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 286 */     StringBuilder stringBuilder = new StringBuilder();
/* 287 */     appendTo(stringBuilder);
/* 288 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendTo(StringBuilder paramStringBuilder) {
/* 293 */     paramStringBuilder.append("SMAP").append("\n");
/*     */ 
/*     */     
/* 296 */     paramStringBuilder.append(getSourceFile()).append("\n");
/*     */ 
/*     */     
/* 299 */     paramStringBuilder.append(this.defaultStratum).append("\n");
/* 300 */     for (Stratum stratum : this.strata.values()) {
/* 301 */       stratum.appendTo(paramStringBuilder);
/*     */     }
/*     */ 
/*     */     
/* 305 */     paramStringBuilder.append("*E").append("\n");
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\struct\SourceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */