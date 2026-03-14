/*     */ package org.spongepowered.asm.util.perf;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.TreeMap;
/*     */ import org.spongepowered.asm.util.PrettyPrinter;
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
/*     */ public final class Profiler
/*     */ {
/*     */   public static final int ROOT = 1;
/*     */   public static final int FINE = 2;
/*     */   
/*     */   public class Section
/*     */   {
/*     */     static final String SEPARATOR_ROOT = " -> ";
/*     */     static final String SEPARATOR_CHILD = ".";
/*     */     private final String name;
/*     */     private boolean root;
/*     */     private boolean fine;
/*     */     protected boolean invalidated;
/*     */     private String info;
/*     */     
/*     */     Section(String param1String) {
/*  89 */       this.name = param1String;
/*  90 */       this.info = param1String;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Section getDelegate() {
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     Section invalidate() {
/* 101 */       this.invalidated = true;
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Section setRoot(boolean param1Boolean) {
/* 111 */       this.root = param1Boolean;
/* 112 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRoot() {
/* 119 */       return this.root;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Section setFine(boolean param1Boolean) {
/* 128 */       this.fine = param1Boolean;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFine() {
/* 136 */       return this.fine;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 143 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getBaseName() {
/* 151 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setInfo(String param1String) {
/* 160 */       this.info = param1String;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getInfo() {
/* 167 */       return this.info;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Section start() {
/* 176 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Section stop() {
/* 185 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Section end() {
/* 194 */       if (!this.invalidated) {
/* 195 */         Profiler.this.end(this);
/*     */       }
/* 197 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Section next(String param1String) {
/* 207 */       end();
/* 208 */       return Profiler.this.begin(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void mark() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTime() {
/* 224 */       return 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTotalTime() {
/* 231 */       return 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getSeconds() {
/* 238 */       return 0.0D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getTotalSeconds() {
/* 245 */       return 0.0D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long[] getTimes() {
/* 253 */       return new long[1];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 260 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTotalCount() {
/* 267 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getAverageTime() {
/* 275 */       return 0.0D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getTotalAverageTime() {
/* 283 */       return 0.0D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String toString() {
/* 291 */       return this.name;
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
/*     */   class LiveSection
/*     */     extends Section
/*     */   {
/* 306 */     private int cursor = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     private long[] times = new long[0];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     private long start = 0L;
/*     */ 
/*     */     
/*     */     private long time;
/*     */     
/*     */     private long markedTime;
/*     */     
/*     */     private int count;
/*     */     
/*     */     private int markedCount;
/*     */ 
/*     */     
/*     */     LiveSection(String param1String, int param1Int) {
/* 329 */       super(param1String);
/* 330 */       this.cursor = param1Int;
/*     */     }
/*     */ 
/*     */     
/*     */     Profiler.Section start() {
/* 335 */       this.start = System.currentTimeMillis();
/* 336 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Profiler.Section stop() {
/* 341 */       if (this.start > 0L) {
/* 342 */         this.time += System.currentTimeMillis() - this.start;
/*     */       }
/* 344 */       this.start = 0L;
/* 345 */       this.count++;
/* 346 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Profiler.Section end() {
/* 351 */       stop();
/* 352 */       if (!this.invalidated) {
/* 353 */         Profiler.this.end(this);
/*     */       }
/* 355 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     void mark() {
/* 360 */       if (this.cursor >= this.times.length) {
/* 361 */         this.times = Arrays.copyOf(this.times, this.cursor + 4);
/*     */       }
/* 363 */       this.times[this.cursor] = this.time;
/* 364 */       this.markedTime += this.time;
/* 365 */       this.markedCount += this.count;
/* 366 */       this.time = 0L;
/* 367 */       this.count = 0;
/* 368 */       this.cursor++;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTime() {
/* 373 */       return this.time;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTotalTime() {
/* 378 */       return this.time + this.markedTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getSeconds() {
/* 383 */       return this.time * 0.001D;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getTotalSeconds() {
/* 388 */       return (this.time + this.markedTime) * 0.001D;
/*     */     }
/*     */ 
/*     */     
/*     */     public long[] getTimes() {
/* 393 */       long[] arrayOfLong = new long[this.cursor + 1];
/* 394 */       System.arraycopy(this.times, 0, arrayOfLong, 0, Math.min(this.times.length, this.cursor));
/* 395 */       arrayOfLong[this.cursor] = this.time;
/* 396 */       return arrayOfLong;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 401 */       return this.count;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getTotalCount() {
/* 406 */       return this.count + this.markedCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getAverageTime() {
/* 411 */       return (this.count > 0) ? (this.time / this.count) : 0.0D;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getTotalAverageTime() {
/* 416 */       return (this.count > 0) ? ((this.time + this.markedTime) / (this.count + this.markedCount)) : 0.0D;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class SubSection
/*     */     extends LiveSection
/*     */   {
/*     */     private final String baseName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Profiler.Section root;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SubSection(String param1String1, int param1Int, String param1String2, Profiler.Section param1Section) {
/* 439 */       super(param1String1, param1Int);
/* 440 */       this.baseName = param1String2;
/* 441 */       this.root = param1Section;
/*     */     }
/*     */ 
/*     */     
/*     */     Profiler.Section invalidate() {
/* 446 */       this.root.invalidate();
/* 447 */       return super.invalidate();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getBaseName() {
/* 452 */       return this.baseName;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setInfo(String param1String) {
/* 457 */       this.root.setInfo(param1String);
/* 458 */       super.setInfo(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     Profiler.Section getDelegate() {
/* 463 */       return this.root;
/*     */     }
/*     */ 
/*     */     
/*     */     Profiler.Section start() {
/* 468 */       this.root.start();
/* 469 */       return super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public Profiler.Section end() {
/* 474 */       this.root.stop();
/* 475 */       return super.end();
/*     */     }
/*     */ 
/*     */     
/*     */     public Profiler.Section next(String param1String) {
/* 480 */       stop();
/* 481 */       return this.root.next(param1String);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 489 */   private final Map<String, Section> sections = new TreeMap<String, Section>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 494 */   private final List<String> phases = new ArrayList<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 499 */   private final Deque<Section> stack = new LinkedList<Section>();
/*     */ 
/*     */   
/*     */   private boolean active;
/*     */ 
/*     */ 
/*     */   
/*     */   public Profiler() {
/* 507 */     this.phases.add("Initial");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActive(boolean paramBoolean) {
/* 517 */     if ((!this.active && paramBoolean) || !paramBoolean) {
/* 518 */       reset();
/*     */     }
/* 520 */     this.active = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 527 */     for (Section section : this.sections.values()) {
/* 528 */       section.invalidate();
/*     */     }
/*     */     
/* 531 */     this.sections.clear();
/* 532 */     this.phases.clear();
/* 533 */     this.phases.add("Initial");
/* 534 */     this.stack.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section get(String paramString) {
/* 544 */     Section section = this.sections.get(paramString);
/* 545 */     if (section == null) {
/* 546 */       section = this.active ? new LiveSection(paramString, this.phases.size() - 1) : new Section(paramString);
/* 547 */       this.sections.put(paramString, section);
/*     */     } 
/*     */     
/* 550 */     return section;
/*     */   }
/*     */   
/*     */   private Section getSubSection(String paramString1, String paramString2, Section paramSection) {
/* 554 */     Section section = this.sections.get(paramString1);
/* 555 */     if (section == null) {
/* 556 */       section = new SubSection(paramString1, this.phases.size() - 1, paramString2, paramSection);
/* 557 */       this.sections.put(paramString1, section);
/*     */     } 
/*     */     
/* 560 */     return section;
/*     */   }
/*     */   
/*     */   boolean isHead(Section paramSection) {
/* 564 */     return (this.stack.peek() == paramSection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section begin(String... paramVarArgs) {
/* 574 */     return begin(0, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section begin(int paramInt, String... paramVarArgs) {
/* 585 */     return begin(paramInt, Joiner.on('.').join((Object[])paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section begin(String paramString) {
/* 595 */     return begin(0, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section begin(int paramInt, String paramString) {
/* 606 */     boolean bool1 = ((paramInt & 0x1) != 0) ? true : false;
/* 607 */     boolean bool2 = ((paramInt & 0x2) != 0) ? true : false;
/*     */     
/* 609 */     String str = paramString;
/* 610 */     Section section1 = this.stack.peek();
/* 611 */     if (section1 != null) {
/* 612 */       str = section1.getName() + (bool1 ? " -> " : ".") + str;
/* 613 */       if (section1.isRoot() && !bool1) {
/* 614 */         int i = section1.getName().lastIndexOf(" -> ");
/* 615 */         paramString = ((i > -1) ? section1.getName().substring(i + 4) : section1.getName()) + "." + paramString;
/* 616 */         bool1 = true;
/*     */       } 
/*     */     } 
/*     */     
/* 620 */     Section section2 = get(bool1 ? paramString : str);
/* 621 */     if (bool1 && section1 != null && this.active) {
/* 622 */       section2 = getSubSection(str, section1.getName(), section2);
/*     */     }
/*     */     
/* 625 */     section2.setFine(bool2).setRoot(bool1);
/* 626 */     this.stack.push(section2);
/*     */     
/* 628 */     return section2.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void end(Section paramSection) {
/*     */     try {
/* 639 */       for (Section section1 = this.stack.pop(), section2 = section1; section2 != paramSection; section2 = this.stack.pop()) {
/* 640 */         if (section2 == null && this.active) {
/* 641 */           if (section1 == null) {
/* 642 */             throw new IllegalStateException("Attempted to pop " + paramSection + " but the stack is empty");
/*     */           }
/* 644 */           throw new IllegalStateException("Attempted to pop " + paramSection + " which was not in the stack, head was " + section1);
/*     */         } 
/*     */       } 
/* 647 */     } catch (NoSuchElementException noSuchElementException) {
/* 648 */       if (this.active) {
/* 649 */         throw new IllegalStateException("Attempted to pop " + paramSection + " but the stack is empty");
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
/*     */   public void mark(String paramString) {
/* 662 */     long l = 0L;
/* 663 */     for (Section section : this.sections.values()) {
/* 664 */       l += section.getTime();
/*     */     }
/*     */ 
/*     */     
/* 668 */     if (l == 0L) {
/* 669 */       int i = this.phases.size();
/* 670 */       this.phases.set(i - 1, paramString);
/*     */       
/*     */       return;
/*     */     } 
/* 674 */     this.phases.add(paramString);
/* 675 */     for (Section section : this.sections.values()) {
/* 676 */       section.mark();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Section> getSections() {
/* 684 */     return Collections.unmodifiableCollection(this.sections.values());
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
/*     */   public PrettyPrinter printer(boolean paramBoolean1, boolean paramBoolean2) {
/* 696 */     PrettyPrinter prettyPrinter = new PrettyPrinter();
/*     */ 
/*     */     
/* 699 */     int i = this.phases.size() + 4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 704 */     int[] arrayOfInt = { 0, 1, 2, i - 2, i - 1 };
/*     */     
/* 706 */     Object[] arrayOfObject = new Object[i * 2]; int j;
/* 707 */     for (byte b = 0; b < i; j = ++b * 2) {
/* 708 */       arrayOfObject[j + 1] = PrettyPrinter.Alignment.RIGHT;
/* 709 */       if (b == arrayOfInt[0]) {
/* 710 */         arrayOfObject[j] = (paramBoolean2 ? "" : "  ") + "Section";
/* 711 */         arrayOfObject[j + 1] = PrettyPrinter.Alignment.LEFT;
/* 712 */       } else if (b == arrayOfInt[1]) {
/* 713 */         arrayOfObject[j] = "    TOTAL";
/* 714 */       } else if (b == arrayOfInt[3]) {
/* 715 */         arrayOfObject[j] = "    Count";
/* 716 */       } else if (b == arrayOfInt[4]) {
/* 717 */         arrayOfObject[j] = "Avg. ";
/* 718 */       } else if (b - arrayOfInt[2] < this.phases.size()) {
/* 719 */         arrayOfObject[j] = this.phases.get(b - arrayOfInt[2]);
/*     */       } else {
/* 721 */         arrayOfObject[j] = "";
/*     */       } 
/*     */     } 
/*     */     
/* 725 */     prettyPrinter.table(arrayOfObject).th().hr().add();
/*     */     
/* 727 */     for (Section section : this.sections.values()) {
/* 728 */       if ((section.isFine() && !paramBoolean1) || (paramBoolean2 && section.getDelegate() != section)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 733 */       printSectionRow(prettyPrinter, i, arrayOfInt, section, paramBoolean2);
/*     */ 
/*     */       
/* 736 */       if (paramBoolean2) {
/* 737 */         for (Section section1 : this.sections.values()) {
/* 738 */           Section section2 = section1.getDelegate();
/* 739 */           if ((section1.isFine() && !paramBoolean1) || section2 != section || section2 == section1) {
/*     */             continue;
/*     */           }
/*     */           
/* 743 */           printSectionRow(prettyPrinter, i, arrayOfInt, section1, paramBoolean2);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 748 */     return prettyPrinter.add();
/*     */   }
/*     */   
/*     */   private void printSectionRow(PrettyPrinter paramPrettyPrinter, int paramInt, int[] paramArrayOfint, Section paramSection, boolean paramBoolean) {
/* 752 */     boolean bool = (paramSection.getDelegate() != paramSection) ? true : false;
/* 753 */     Object[] arrayOfObject = new Object[paramInt];
/* 754 */     byte b1 = 1;
/* 755 */     if (paramBoolean) {
/* 756 */       arrayOfObject[0] = bool ? ("  > " + paramSection.getBaseName()) : paramSection.getName();
/*     */     } else {
/* 758 */       arrayOfObject[0] = (bool ? "+ " : "  ") + paramSection.getName();
/*     */     } 
/*     */     
/* 761 */     long[] arrayOfLong = paramSection.getTimes();
/* 762 */     for (long l : arrayOfLong) {
/* 763 */       if (b1 == paramArrayOfint[1]) {
/* 764 */         arrayOfObject[b1++] = paramSection.getTotalTime() + " ms";
/*     */       }
/* 766 */       if (b1 >= paramArrayOfint[2] && b1 < arrayOfObject.length) {
/* 767 */         arrayOfObject[b1++] = l + " ms";
/*     */       }
/*     */     } 
/*     */     
/* 771 */     arrayOfObject[paramArrayOfint[3]] = Integer.valueOf(paramSection.getTotalCount());
/* 772 */     arrayOfObject[paramArrayOfint[4]] = (new DecimalFormat("   ###0.000 ms")).format(paramSection.getTotalAverageTime());
/*     */     
/* 774 */     for (byte b2 = 0; b2 < arrayOfObject.length; b2++) {
/* 775 */       if (arrayOfObject[b2] == null) {
/* 776 */         arrayOfObject[b2] = "-";
/*     */       }
/*     */     } 
/*     */     
/* 780 */     paramPrettyPrinter.tr(arrayOfObject);
/*     */   }
/*     */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\as\\util\perf\Profiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */