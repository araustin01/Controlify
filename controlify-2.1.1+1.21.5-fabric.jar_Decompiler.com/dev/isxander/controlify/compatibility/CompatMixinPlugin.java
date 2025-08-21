package dev.isxander.controlify.compatibility;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public abstract class CompatMixinPlugin implements IMixinConfigPlugin {
   private final boolean compatEnabled = FabricLoader.getInstance().isModLoaded(this.getModId());

   protected CompatMixinPlugin() {
   }

   public abstract String getModId();

   public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
      return this.compatEnabled;
   }

   public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public void onLoad(String mixinPackage) {
   }

   public String getRefMapperConfig() {
      return null;
   }
}
