/*    */ package org.spongepowered.asm.bridge;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import org.objectweb.asm.commons.Remapper;
/*    */ import org.spongepowered.asm.mixin.extensibility.IRemapper;
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
/*    */ public final class RemapperAdapterFML
/*    */   extends RemapperAdapter
/*    */ {
/*    */   private static final String DEOBFUSCATING_REMAPPER_CLASS = "fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
/*    */   private static final String DEOBFUSCATING_REMAPPER_CLASS_FORGE = "net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
/*    */   private static final String DEOBFUSCATING_REMAPPER_CLASS_LEGACY = "cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
/*    */   private static final String INSTANCE_FIELD = "INSTANCE";
/*    */   private static final String UNMAP_METHOD = "unmap";
/*    */   private final Method mdUnmap;
/*    */   
/*    */   private RemapperAdapterFML(Remapper paramRemapper, Method paramMethod) {
/* 46 */     super(paramRemapper);
/* 47 */     this.logger.info("Initialised Mixin FML Remapper Adapter with {}", new Object[] { paramRemapper });
/* 48 */     this.mdUnmap = paramMethod;
/*    */   }
/*    */ 
/*    */   
/*    */   public String unmap(String paramString) {
/*    */     try {
/* 54 */       return this.mdUnmap.invoke(this.remapper, new Object[] { paramString }).toString();
/* 55 */     } catch (Exception exception) {
/* 56 */       return paramString;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IRemapper create() {
/*    */     try {
/* 65 */       Class<?> clazz = getFMLDeobfuscatingRemapper();
/* 66 */       Field field = clazz.getDeclaredField("INSTANCE");
/* 67 */       Method method = clazz.getDeclaredMethod("unmap", new Class[] { String.class });
/* 68 */       Remapper remapper = (Remapper)field.get(null);
/* 69 */       return new RemapperAdapterFML(remapper, method);
/* 70 */     } catch (Exception exception) {
/* 71 */       exception.printStackTrace();
/* 72 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Class<?> getFMLDeobfuscatingRemapper() throws ClassNotFoundException {
/*    */     try {
/* 82 */       return Class.forName("net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
/* 83 */     } catch (ClassNotFoundException classNotFoundException) {
/* 84 */       return Class.forName("cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\bridge\RemapperAdapterFML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */