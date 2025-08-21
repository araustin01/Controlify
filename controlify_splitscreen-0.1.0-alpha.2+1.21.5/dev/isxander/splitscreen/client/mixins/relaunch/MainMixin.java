package dev.isxander.splitscreen.client.mixins.relaunch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.properties.PropertyMap;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import java.util.Optional;
import net.minecraft.class_320;
import net.minecraft.class_4844;
import net.minecraft.class_320.class_321;
import net.minecraft.class_542.class_547;
import net.minecraft.class_542.class_8495;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Main.class})
public class MainMixin {
   @ModifyExpressionValue(
      method = {"main([Ljava/lang/String;)V"},
      at = {@At(
   value = "NEW",
   target = "Lnet/minecraft/class_542$class_547;"
)}
   )
   private static class_547 modifyUserData(class_547 originalData) {
      return (class_547)RelaunchArguments.USERNAME.get().map((username) -> {
         return new class_547(new class_320(username, class_4844.method_43344(username), "", Optional.empty(), Optional.empty(), class_321.field_1990), new PropertyMap(), new PropertyMap(), originalData.field_3296);
      }).orElse(originalData);
   }

   @ModifyExpressionValue(
      method = {"main([Ljava/lang/String;)V"},
      at = {@At(
   value = "NEW",
   target = "Lnet/minecraft/class_542$class_8495;"
)}
   )
   private static class_8495 modifyQuickPlayData(class_8495 originalData) {
      return (class_8495)RelaunchArguments.LAN_GAME.get().map((lanIp) -> {
         return new class_8495((String)null, (String)null, lanIp, (String)null);
      }).orElse(originalData);
   }
}
