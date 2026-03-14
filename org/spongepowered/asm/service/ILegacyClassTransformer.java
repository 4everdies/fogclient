package org.spongepowered.asm.service;

public interface ILegacyClassTransformer extends ITransformer {
  String getName();
  
  boolean isDelegationExcluded();
  
  byte[] transformClassBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\homy\AppData\Roaming\.minecraft\mods\FogClientV1_cracked.jar!\org\spongepowered\asm\service\ILegacyClassTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */