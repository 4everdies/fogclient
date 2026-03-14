/*      */ package org.spongepowered.asm.util;
/*      */ 
/*      */ import com.google.common.base.Strings;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PrettyPrinter
/*      */ {
/*      */   public static interface IPrettyPrintable
/*      */   {
/*      */     void print(PrettyPrinter param1PrettyPrinter);
/*      */   }
/*      */   
/*      */   static interface IVariableWidthEntry
/*      */   {
/*      */     int getWidth();
/*      */   }
/*      */   
/*      */   static interface ISpecialEntry {}
/*      */   
/*      */   class KeyValue
/*      */     implements IVariableWidthEntry
/*      */   {
/*      */     private final String key;
/*      */     private final Object value;
/*      */     
/*      */     public KeyValue(String param1String, Object param1Object) {
/*   86 */       this.key = param1String;
/*   87 */       this.value = param1Object;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*   92 */       return String.format(PrettyPrinter.this.kvFormat, new Object[] { this.key, this.value });
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWidth() {
/*   97 */       return toString().length();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class HorizontalRule
/*      */     implements ISpecialEntry
/*      */   {
/*      */     private final char[] hrChars;
/*      */ 
/*      */     
/*      */     public HorizontalRule(char... param1VarArgs) {
/*  110 */       this.hrChars = param1VarArgs;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  115 */       return Strings.repeat(new String(this.hrChars), PrettyPrinter.this.width + 2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class CentredText
/*      */   {
/*      */     private final Object centred;
/*      */ 
/*      */ 
/*      */     
/*      */     public CentredText(Object param1Object) {
/*  128 */       this.centred = param1Object;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  133 */       String str = this.centred.toString();
/*  134 */       return String.format("%" + ((PrettyPrinter.this.width - str.length()) / 2 + str.length()) + "s", new Object[] { str });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Alignment
/*      */   {
/*  143 */     LEFT,
/*  144 */     RIGHT;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class Table
/*      */     implements IVariableWidthEntry
/*      */   {
/*  152 */     final List<PrettyPrinter.Column> columns = new ArrayList<PrettyPrinter.Column>();
/*      */     
/*  154 */     final List<PrettyPrinter.Row> rows = new ArrayList<PrettyPrinter.Row>();
/*      */     
/*  156 */     String format = "%s";
/*      */     
/*  158 */     int colSpacing = 2;
/*      */     
/*      */     boolean addHeader = true;
/*      */     
/*      */     void headerAdded() {
/*  163 */       this.addHeader = false;
/*      */     }
/*      */     
/*      */     void setColSpacing(int param1Int) {
/*  167 */       this.colSpacing = Math.max(0, param1Int);
/*  168 */       updateFormat();
/*      */     }
/*      */     
/*      */     Table grow(int param1Int) {
/*  172 */       while (this.columns.size() < param1Int) {
/*  173 */         this.columns.add(new PrettyPrinter.Column(this));
/*      */       }
/*  175 */       updateFormat();
/*  176 */       return this;
/*      */     }
/*      */     
/*      */     PrettyPrinter.Column add(PrettyPrinter.Column param1Column) {
/*  180 */       this.columns.add(param1Column);
/*  181 */       return param1Column;
/*      */     }
/*      */     
/*      */     PrettyPrinter.Row add(PrettyPrinter.Row param1Row) {
/*  185 */       this.rows.add(param1Row);
/*  186 */       return param1Row;
/*      */     }
/*      */     
/*      */     PrettyPrinter.Column addColumn(String param1String) {
/*  190 */       return add(new PrettyPrinter.Column(this, param1String));
/*      */     }
/*      */     
/*      */     PrettyPrinter.Column addColumn(PrettyPrinter.Alignment param1Alignment, int param1Int, String param1String) {
/*  194 */       return add(new PrettyPrinter.Column(this, param1Alignment, param1Int, param1String));
/*      */     }
/*      */     
/*      */     PrettyPrinter.Row addRow(Object... param1VarArgs) {
/*  198 */       return add(new PrettyPrinter.Row(this, param1VarArgs));
/*      */     }
/*      */     
/*      */     void updateFormat() {
/*  202 */       String str = Strings.repeat(" ", this.colSpacing);
/*  203 */       StringBuilder stringBuilder = new StringBuilder();
/*  204 */       boolean bool = false;
/*  205 */       for (PrettyPrinter.Column column : this.columns) {
/*  206 */         if (bool) {
/*  207 */           stringBuilder.append(str);
/*      */         }
/*  209 */         bool = true;
/*  210 */         stringBuilder.append(column.getFormat());
/*      */       } 
/*  212 */       this.format = stringBuilder.toString();
/*      */     }
/*      */     
/*      */     String getFormat() {
/*  216 */       return this.format;
/*      */     }
/*      */     
/*      */     Object[] getTitles() {
/*  220 */       ArrayList<String> arrayList = new ArrayList();
/*  221 */       for (PrettyPrinter.Column column : this.columns) {
/*  222 */         arrayList.add(column.getTitle());
/*      */       }
/*  224 */       return arrayList.toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  229 */       int i = 0;
/*  230 */       String[] arrayOfString = new String[this.columns.size()];
/*  231 */       for (byte b = 0; b < this.columns.size(); b++) {
/*  232 */         arrayOfString[b] = ((PrettyPrinter.Column)this.columns.get(b)).toString();
/*  233 */         i |= !arrayOfString[b].isEmpty() ? 1 : 0;
/*      */       } 
/*  235 */       return (i != 0) ? String.format(this.format, (Object[])arrayOfString) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWidth() {
/*  240 */       String str = toString();
/*  241 */       return (str != null) ? str.length() : 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Column
/*      */   {
/*      */     private final PrettyPrinter.Table table;
/*      */ 
/*      */     
/*  253 */     private PrettyPrinter.Alignment align = PrettyPrinter.Alignment.LEFT;
/*      */     
/*  255 */     private int minWidth = 1;
/*      */     
/*  257 */     private int maxWidth = Integer.MAX_VALUE;
/*      */     
/*  259 */     private int size = 0;
/*      */     
/*  261 */     private String title = "";
/*      */     
/*  263 */     private String format = "%s";
/*      */     
/*      */     Column(PrettyPrinter.Table param1Table) {
/*  266 */       this.table = param1Table;
/*      */     }
/*      */     
/*      */     Column(PrettyPrinter.Table param1Table, String param1String) {
/*  270 */       this(param1Table);
/*  271 */       this.title = param1String;
/*  272 */       this.minWidth = param1String.length();
/*  273 */       updateFormat();
/*      */     }
/*      */     
/*      */     Column(PrettyPrinter.Table param1Table, PrettyPrinter.Alignment param1Alignment, int param1Int, String param1String) {
/*  277 */       this(param1Table, param1String);
/*  278 */       this.align = param1Alignment;
/*  279 */       this.size = param1Int;
/*      */     }
/*      */     
/*      */     void setAlignment(PrettyPrinter.Alignment param1Alignment) {
/*  283 */       this.align = param1Alignment;
/*  284 */       updateFormat();
/*      */     }
/*      */     
/*      */     void setWidth(int param1Int) {
/*  288 */       if (param1Int > this.size) {
/*  289 */         this.size = param1Int;
/*  290 */         updateFormat();
/*      */       } 
/*      */     }
/*      */     
/*      */     void setMinWidth(int param1Int) {
/*  295 */       if (param1Int > this.minWidth) {
/*  296 */         this.minWidth = param1Int;
/*  297 */         updateFormat();
/*      */       } 
/*      */     }
/*      */     
/*      */     void setMaxWidth(int param1Int) {
/*  302 */       this.size = Math.min(this.size, this.maxWidth);
/*  303 */       this.maxWidth = Math.max(1, param1Int);
/*  304 */       updateFormat();
/*      */     }
/*      */     
/*      */     void setTitle(String param1String) {
/*  308 */       this.title = param1String;
/*  309 */       setWidth(param1String.length());
/*      */     }
/*      */     
/*      */     private void updateFormat() {
/*  313 */       int i = Math.min(this.maxWidth, (this.size == 0) ? this.minWidth : this.size);
/*  314 */       this.format = "%" + ((this.align == PrettyPrinter.Alignment.RIGHT) ? "" : "-") + i + "s";
/*  315 */       this.table.updateFormat();
/*      */     }
/*      */     
/*      */     int getMaxWidth() {
/*  319 */       return this.maxWidth;
/*      */     }
/*      */     
/*      */     String getTitle() {
/*  323 */       return this.title;
/*      */     }
/*      */     
/*      */     String getFormat() {
/*  327 */       return this.format;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  332 */       if (this.title.length() > this.maxWidth) {
/*  333 */         return this.title.substring(0, this.maxWidth);
/*      */       }
/*      */       
/*  336 */       return this.title;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class Row
/*      */     implements IVariableWidthEntry
/*      */   {
/*      */     final PrettyPrinter.Table table;
/*      */     
/*      */     final String[] args;
/*      */ 
/*      */     
/*      */     public Row(PrettyPrinter.Table param1Table, Object... param1VarArgs) {
/*  351 */       this.table = param1Table.grow(param1VarArgs.length);
/*  352 */       this.args = new String[param1VarArgs.length];
/*  353 */       for (byte b = 0; b < param1VarArgs.length; b++) {
/*  354 */         this.args[b] = param1VarArgs[b].toString();
/*  355 */         ((PrettyPrinter.Column)this.table.columns.get(b)).setMinWidth(this.args[b].length());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  361 */       Object[] arrayOfObject = new Object[this.table.columns.size()];
/*  362 */       for (byte b = 0; b < arrayOfObject.length; b++) {
/*  363 */         PrettyPrinter.Column column = this.table.columns.get(b);
/*  364 */         if (b >= this.args.length) {
/*  365 */           arrayOfObject[b] = "";
/*      */         } else {
/*  367 */           arrayOfObject[b] = (this.args[b].length() > column.getMaxWidth()) ? this.args[b].substring(0, column.getMaxWidth()) : this.args[b];
/*      */         } 
/*      */       } 
/*      */       
/*  371 */       return String.format(this.table.format, arrayOfObject);
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWidth() {
/*  376 */       return toString().length();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  384 */   private final HorizontalRule horizontalRule = new HorizontalRule(new char[] { '*' });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  389 */   private final List<Object> lines = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Table table;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean recalcWidth = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  405 */   protected int width = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  410 */   protected int wrapWidth = 80;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  415 */   protected int kvKeyWidth = 10;
/*      */   
/*  417 */   protected String kvFormat = makeKvFormat(this.kvKeyWidth);
/*      */   
/*      */   public PrettyPrinter() {
/*  420 */     this(100);
/*      */   }
/*      */   
/*      */   public PrettyPrinter(int paramInt) {
/*  424 */     this.width = paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter wrapTo(int paramInt) {
/*  434 */     this.wrapWidth = paramInt;
/*  435 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int wrapTo() {
/*  444 */     return this.wrapWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter table() {
/*  453 */     this.table = new Table();
/*  454 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter table(String... paramVarArgs) {
/*  464 */     this.table = new Table();
/*  465 */     for (String str : paramVarArgs) {
/*  466 */       this.table.addColumn(str);
/*      */     }
/*  468 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter table(Object... paramVarArgs) {
/*  493 */     this.table = new Table();
/*  494 */     Column column = null;
/*  495 */     for (Object object : paramVarArgs) {
/*  496 */       if (object instanceof String) {
/*  497 */         column = this.table.addColumn((String)object);
/*  498 */       } else if (object instanceof Integer && column != null) {
/*  499 */         int i = ((Integer)object).intValue();
/*  500 */         if (i > 0) {
/*  501 */           column.setWidth(i);
/*  502 */         } else if (i < 0) {
/*  503 */           column.setMaxWidth(-i);
/*      */         } 
/*  505 */       } else if (object instanceof Alignment && column != null) {
/*  506 */         column.setAlignment((Alignment)object);
/*  507 */       } else if (object != null) {
/*  508 */         column = this.table.addColumn(object.toString());
/*      */       } 
/*      */     } 
/*  511 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter spacing(int paramInt) {
/*  521 */     if (this.table == null) {
/*  522 */       this.table = new Table();
/*      */     }
/*  524 */     this.table.setColSpacing(paramInt);
/*  525 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter th() {
/*  535 */     return th(false);
/*      */   }
/*      */   
/*      */   private PrettyPrinter th(boolean paramBoolean) {
/*  539 */     if (this.table == null) {
/*  540 */       this.table = new Table();
/*      */     }
/*  542 */     if (!paramBoolean || this.table.addHeader) {
/*  543 */       this.table.headerAdded();
/*  544 */       addLine(this.table);
/*      */     } 
/*  546 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter tr(Object... paramVarArgs) {
/*  558 */     th(true);
/*  559 */     addLine(this.table.addRow(paramVarArgs));
/*  560 */     this.recalcWidth = true;
/*  561 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add() {
/*  570 */     addLine("");
/*  571 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(String paramString) {
/*  581 */     addLine(paramString);
/*  582 */     this.width = Math.max(this.width, paramString.length());
/*  583 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(String paramString, Object... paramVarArgs) {
/*  595 */     String str = String.format(paramString, paramVarArgs);
/*  596 */     addLine(str);
/*  597 */     this.width = Math.max(this.width, str.length());
/*  598 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Object[] paramArrayOfObject) {
/*  608 */     return add(paramArrayOfObject, "%s");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Object[] paramArrayOfObject, String paramString) {
/*  619 */     for (Object object : paramArrayOfObject) {
/*  620 */       add(paramString, new Object[] { object });
/*      */     } 
/*      */     
/*  623 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter addIndexed(Object[] paramArrayOfObject) {
/*  633 */     int i = String.valueOf(paramArrayOfObject.length - 1).length();
/*  634 */     String str = "[%" + i + "d] %s";
/*  635 */     for (byte b = 0; b < paramArrayOfObject.length; b++) {
/*  636 */       add(str, new Object[] { Integer.valueOf(b), paramArrayOfObject[b] });
/*      */     } 
/*      */     
/*  639 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter addWithIndices(Collection<?> paramCollection) {
/*  649 */     return addIndexed(paramCollection.toArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(IPrettyPrintable paramIPrettyPrintable) {
/*  660 */     if (paramIPrettyPrintable != null) {
/*  661 */       paramIPrettyPrintable.print(this);
/*      */     }
/*  663 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Throwable paramThrowable) {
/*  674 */     return add(paramThrowable, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Throwable paramThrowable, int paramInt) {
/*  686 */     while (paramThrowable != null) {
/*  687 */       add("%s: %s", new Object[] { paramThrowable.getClass().getName(), paramThrowable.getMessage() });
/*  688 */       add(paramThrowable.getStackTrace(), paramInt);
/*  689 */       paramThrowable = paramThrowable.getCause();
/*      */     } 
/*  691 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(StackTraceElement[] paramArrayOfStackTraceElement, int paramInt) {
/*  703 */     String str = Strings.repeat(" ", paramInt);
/*  704 */     for (StackTraceElement stackTraceElement : paramArrayOfStackTraceElement) {
/*  705 */       add("%s%s", new Object[] { str, stackTraceElement });
/*      */     } 
/*  707 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Object paramObject) {
/*  717 */     return add(paramObject, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Object paramObject, int paramInt) {
/*  728 */     String str = Strings.repeat(" ", paramInt);
/*  729 */     return append(paramObject, paramInt, str);
/*      */   }
/*      */   
/*      */   private PrettyPrinter append(Object paramObject, int paramInt, String paramString) {
/*  733 */     if (paramObject instanceof String)
/*  734 */       return add("%s%s", new Object[] { paramString, paramObject }); 
/*  735 */     if (paramObject instanceof Iterable) {
/*  736 */       for (Object object : paramObject) {
/*  737 */         append(object, paramInt, paramString);
/*      */       }
/*  739 */       return this;
/*  740 */     }  if (paramObject instanceof Map) {
/*  741 */       kvWidth(paramInt);
/*  742 */       return add((Map<?, ?>)paramObject);
/*  743 */     }  if (paramObject instanceof IPrettyPrintable)
/*  744 */       return add((IPrettyPrintable)paramObject); 
/*  745 */     if (paramObject instanceof Throwable)
/*  746 */       return add((Throwable)paramObject, paramInt); 
/*  747 */     if (paramObject.getClass().isArray()) {
/*  748 */       return add((Object[])paramObject, paramInt + "%s");
/*      */     }
/*  750 */     return add("%s%s", new Object[] { paramString, paramObject });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter addWrapped(String paramString, Object... paramVarArgs) {
/*  763 */     return addWrapped(this.wrapWidth, paramString, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter addWrapped(int paramInt, String paramString, Object... paramVarArgs) {
/*  777 */     String str1 = "";
/*  778 */     String str2 = String.format(paramString, paramVarArgs).replace("\t", "    ");
/*  779 */     Matcher matcher = Pattern.compile("^(\\s+)(.*)$").matcher(str2);
/*  780 */     if (matcher.matches()) {
/*  781 */       str1 = matcher.group(1);
/*      */     }
/*      */     
/*      */     try {
/*  785 */       for (String str : getWrapped(paramInt, str2, str1)) {
/*  786 */         addLine(str);
/*      */       }
/*  788 */     } catch (Exception exception) {
/*  789 */       add(str2);
/*      */     } 
/*  791 */     return this;
/*      */   }
/*      */   
/*      */   private List<String> getWrapped(int paramInt, String paramString1, String paramString2) {
/*  795 */     ArrayList<String> arrayList = new ArrayList();
/*      */     
/*  797 */     while (paramString1.length() > paramInt) {
/*  798 */       int i = paramString1.lastIndexOf(' ', paramInt);
/*  799 */       if (i < 10) {
/*  800 */         i = paramInt;
/*      */       }
/*  802 */       String str = paramString1.substring(0, i);
/*  803 */       arrayList.add(str);
/*  804 */       paramString1 = paramString2 + paramString1.substring(i + 1);
/*      */     } 
/*      */     
/*  807 */     if (paramString1.length() > 0) {
/*  808 */       arrayList.add(paramString1);
/*      */     }
/*      */     
/*  811 */     return arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter kv(String paramString1, String paramString2, Object... paramVarArgs) {
/*  823 */     return kv(paramString1, String.format(paramString2, paramVarArgs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter kv(String paramString, Object paramObject) {
/*  834 */     addLine(new KeyValue(paramString, paramObject));
/*  835 */     return kvWidth(paramString.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter kvWidth(int paramInt) {
/*  845 */     if (paramInt > this.kvKeyWidth) {
/*  846 */       this.kvKeyWidth = paramInt;
/*  847 */       this.kvFormat = makeKvFormat(paramInt);
/*      */     } 
/*  849 */     this.recalcWidth = true;
/*  850 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter add(Map<?, ?> paramMap) {
/*  860 */     for (Map.Entry<?, ?> entry : paramMap.entrySet()) {
/*  861 */       String str = (entry.getKey() == null) ? "null" : entry.getKey().toString();
/*  862 */       kv(str, entry.getValue());
/*      */     } 
/*  864 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter hr() {
/*  873 */     return hr('*');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter hr(char paramChar) {
/*  884 */     addLine(new HorizontalRule(new char[] { paramChar }));
/*  885 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter centre() {
/*  894 */     if (!this.lines.isEmpty()) {
/*  895 */       Object object = this.lines.get(this.lines.size() - 1);
/*  896 */       if (object instanceof String) {
/*  897 */         addLine(new CentredText(this.lines.remove(this.lines.size() - 1)));
/*      */       }
/*      */     } 
/*  900 */     return this;
/*      */   }
/*      */   
/*      */   private void addLine(Object paramObject) {
/*  904 */     if (paramObject == null) {
/*      */       return;
/*      */     }
/*  907 */     this.lines.add(paramObject);
/*  908 */     this.recalcWidth |= paramObject instanceof IVariableWidthEntry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace() {
/*  918 */     return trace(getDefaultLoggerName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(Level paramLevel) {
/*  929 */     return trace(getDefaultLoggerName(), paramLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(String paramString) {
/*  940 */     return trace(System.err, LogManager.getLogger(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(String paramString, Level paramLevel) {
/*  952 */     return trace(System.err, LogManager.getLogger(paramString), paramLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(Logger paramLogger) {
/*  963 */     return trace(System.err, paramLogger);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(Logger paramLogger, Level paramLevel) {
/*  975 */     return trace(System.err, paramLogger, paramLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream) {
/*  986 */     return trace(paramPrintStream, getDefaultLoggerName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream, Level paramLevel) {
/*  998 */     return trace(paramPrintStream, getDefaultLoggerName(), paramLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream, String paramString) {
/* 1010 */     return trace(paramPrintStream, LogManager.getLogger(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream, String paramString, Level paramLevel) {
/* 1023 */     return trace(paramPrintStream, LogManager.getLogger(paramString), paramLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream, Logger paramLogger) {
/* 1035 */     return trace(paramPrintStream, paramLogger, Level.DEBUG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter trace(PrintStream paramPrintStream, Logger paramLogger, Level paramLevel) {
/* 1048 */     log(paramLogger, paramLevel);
/* 1049 */     print(paramPrintStream);
/* 1050 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter print() {
/* 1059 */     return print(System.err);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter print(PrintStream paramPrintStream) {
/* 1069 */     updateWidth();
/* 1070 */     printSpecial(paramPrintStream, this.horizontalRule);
/* 1071 */     for (ISpecialEntry iSpecialEntry : this.lines) {
/* 1072 */       if (iSpecialEntry instanceof ISpecialEntry) {
/* 1073 */         printSpecial(paramPrintStream, iSpecialEntry); continue;
/*      */       } 
/* 1075 */       printString(paramPrintStream, iSpecialEntry.toString());
/*      */     } 
/*      */     
/* 1078 */     printSpecial(paramPrintStream, this.horizontalRule);
/* 1079 */     return this;
/*      */   }
/*      */   
/*      */   private void printSpecial(PrintStream paramPrintStream, ISpecialEntry paramISpecialEntry) {
/* 1083 */     paramPrintStream.printf("/*%s*/\n", new Object[] { paramISpecialEntry.toString() });
/*      */   }
/*      */   
/*      */   private void printString(PrintStream paramPrintStream, String paramString) {
/* 1087 */     if (paramString != null) {
/* 1088 */       paramPrintStream.printf("/* %-" + this.width + "s */\n", new Object[] { paramString });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter log(Logger paramLogger) {
/* 1099 */     return log(paramLogger, Level.INFO);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PrettyPrinter log(Logger paramLogger, Level paramLevel) {
/* 1110 */     updateWidth();
/* 1111 */     logSpecial(paramLogger, paramLevel, this.horizontalRule);
/* 1112 */     for (ISpecialEntry iSpecialEntry : this.lines) {
/* 1113 */       if (iSpecialEntry instanceof ISpecialEntry) {
/* 1114 */         logSpecial(paramLogger, paramLevel, iSpecialEntry); continue;
/*      */       } 
/* 1116 */       logString(paramLogger, paramLevel, iSpecialEntry.toString());
/*      */     } 
/*      */     
/* 1119 */     logSpecial(paramLogger, paramLevel, this.horizontalRule);
/* 1120 */     return this;
/*      */   }
/*      */   
/*      */   private void logSpecial(Logger paramLogger, Level paramLevel, ISpecialEntry paramISpecialEntry) {
/* 1124 */     paramLogger.log(paramLevel, "/*{}*/", new Object[] { paramISpecialEntry.toString() });
/*      */   }
/*      */   
/*      */   private void logString(Logger paramLogger, Level paramLevel, String paramString) {
/* 1128 */     if (paramString != null) {
/* 1129 */       paramLogger.log(paramLevel, String.format("/* %-" + this.width + "s */", new Object[] { paramString }));
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateWidth() {
/* 1134 */     if (this.recalcWidth) {
/* 1135 */       this.recalcWidth = false;
/* 1136 */       for (IVariableWidthEntry iVariableWidthEntry : this.lines) {
/* 1137 */         if (iVariableWidthEntry instanceof IVariableWidthEntry) {
/* 1138 */           this.width = Math.min(4096, Math.max(this.width, ((IVariableWidthEntry)iVariableWidthEntry).getWidth()));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String makeKvFormat(int paramInt) {
/* 1145 */     return String.format("%%%ds : %%s", new Object[] { Integer.valueOf(paramInt) });
/*      */   }
/*      */   
/*      */   private static String getDefaultLoggerName() {
/* 1149 */     String str = (new Throwable()).getStackTrace()[2].getClassName();
/* 1150 */     int i = str.lastIndexOf('.');
/* 1151 */     return (i == -1) ? str : str.substring(i + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void dumpStack() {
/* 1159 */     (new PrettyPrinter()).add(new Exception("Stack trace")).print(System.err);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void print(Throwable paramThrowable) {
/* 1168 */     (new PrettyPrinter()).add(paramThrowable).print(System.err);
/*      */   }
/*      */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\PrettyPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */