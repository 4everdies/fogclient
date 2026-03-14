/*     */ package org.spongepowered.asm.mixin.transformer.ext.extensions;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.io.Files;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.transformer.ClassInfo;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.IExtension;
/*     */ import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
/*     */ import org.spongepowered.asm.util.Constants;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
/*     */ import org.spongepowered.asm.util.SignaturePrinter;
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
/*     */ public class ExtensionCheckInterfaces
/*     */   implements IExtension
/*     */ {
/*     */   private static final String AUDIT_DIR = "audit";
/*     */   private static final String IMPL_REPORT_FILENAME = "mixin_implementation_report";
/*     */   private static final String IMPL_REPORT_CSV_FILENAME = "mixin_implementation_report.csv";
/*     */   private static final String IMPL_REPORT_TXT_FILENAME = "mixin_implementation_report.txt";
/*  67 */   private static final Logger logger = LogManager.getLogger("mixin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final File csv;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final File report;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private final Multimap<ClassInfo, ClassInfo.Method> interfaceMethods = (Multimap<ClassInfo, ClassInfo.Method>)HashMultimap.create();
/*     */ 
/*     */   
/*     */   private boolean strict;
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionCheckInterfaces() {
/*  91 */     File file = new File(Constants.DEBUG_OUTPUT_DIR, "audit");
/*  92 */     file.mkdirs();
/*  93 */     this.csv = new File(file, "mixin_implementation_report.csv");
/*  94 */     this.report = new File(file, "mixin_implementation_report.txt");
/*     */     
/*     */     try {
/*  97 */       Files.write("Class,Method,Signature,Interface\n", this.csv, Charsets.ISO_8859_1);
/*  98 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 103 */       String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
/* 104 */       Files.write("Mixin Implementation Report generated on " + str + "\n", this.report, Charsets.ISO_8859_1);
/* 105 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkActive(MixinEnvironment paramMixinEnvironment) {
/* 116 */     this.strict = paramMixinEnvironment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS_STRICT);
/* 117 */     return paramMixinEnvironment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preApply(ITargetClassContext paramITargetClassContext) {
/* 126 */     ClassInfo classInfo = paramITargetClassContext.getClassInfo();
/* 127 */     for (ClassInfo.Method method : classInfo.getInterfaceMethods(false)) {
/* 128 */       this.interfaceMethods.put(classInfo, method);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postApply(ITargetClassContext paramITargetClassContext) {
/* 138 */     ClassInfo classInfo = paramITargetClassContext.getClassInfo();
/*     */ 
/*     */     
/* 141 */     if (classInfo.isAbstract() && !this.strict) {
/* 142 */       logger.info("{} is skipping abstract target {}", new Object[] { getClass().getSimpleName(), paramITargetClassContext });
/*     */       
/*     */       return;
/*     */     } 
/* 146 */     String str = classInfo.getName().replace('/', '.');
/* 147 */     byte b = 0;
/* 148 */     PrettyPrinter prettyPrinter = new PrettyPrinter();
/*     */     
/* 150 */     prettyPrinter.add("Class: %s", new Object[] { str }).hr();
/* 151 */     prettyPrinter.add("%-32s %-47s  %s", new Object[] { "Return Type", "Missing Method", "From Interface" }).hr();
/*     */     
/* 153 */     Set set = classInfo.getInterfaceMethods(true);
/* 154 */     HashSet hashSet = new HashSet(classInfo.getSuperClass().getInterfaceMethods(true));
/* 155 */     hashSet.addAll(this.interfaceMethods.removeAll(classInfo));
/*     */     
/* 157 */     for (ClassInfo.Method method1 : set) {
/* 158 */       ClassInfo.Method method2 = classInfo.findMethodInHierarchy(method1.getName(), method1.getDesc(), ClassInfo.SearchType.ALL_CLASSES, ClassInfo.Traversal.ALL);
/*     */ 
/*     */       
/* 161 */       if (method2 != null && !method2.isAbstract()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 166 */       if (hashSet.contains(method1)) {
/*     */         continue;
/*     */       }
/*     */       
/* 170 */       if (b) {
/* 171 */         prettyPrinter.add();
/*     */       }
/*     */       
/* 174 */       SignaturePrinter signaturePrinter = (new SignaturePrinter(method1.getName(), method1.getDesc())).setModifiers("");
/* 175 */       String str1 = method1.getOwner().getName().replace('/', '.');
/* 176 */       b++;
/* 177 */       prettyPrinter.add("%-32s%s", new Object[] { signaturePrinter.getReturnType(), signaturePrinter });
/* 178 */       prettyPrinter.add("%-80s  %s", new Object[] { "", str1 });
/*     */       
/* 180 */       appendToCSVReport(str, method1, str1);
/*     */     } 
/*     */     
/* 183 */     if (b > 0) {
/* 184 */       prettyPrinter.hr().add("%82s%s: %d", new Object[] { "", "Total unimplemented", Integer.valueOf(b) });
/* 185 */       prettyPrinter.print(System.err);
/* 186 */       appendToTextReport(prettyPrinter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void export(MixinEnvironment paramMixinEnvironment, String paramString, boolean paramBoolean, byte[] paramArrayOfbyte) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendToCSVReport(String paramString1, ClassInfo.Method paramMethod, String paramString2) {
/*     */     try {
/* 201 */       Files.append(String.format("%s,%s,%s,%s\n", new Object[] { paramString1, paramMethod.getName(), paramMethod.getDesc(), paramString2 }), this.csv, Charsets.ISO_8859_1);
/* 202 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendToTextReport(PrettyPrinter paramPrettyPrinter) {
/* 208 */     FileOutputStream fileOutputStream = null;
/*     */     
/*     */     try {
/* 211 */       fileOutputStream = new FileOutputStream(this.report, true);
/* 212 */       PrintStream printStream = new PrintStream(fileOutputStream);
/* 213 */       printStream.print("\n");
/* 214 */       paramPrettyPrinter.print(printStream);
/* 215 */     } catch (Exception exception) {
/*     */     
/*     */     } finally {
/* 218 */       IOUtils.closeQuietly(fileOutputStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\mixin\transformer\ext\extensions\ExtensionCheckInterfaces.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */