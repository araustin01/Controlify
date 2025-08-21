package dev.isxander.controlify.controller.id;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public record ControllerType(@Nullable String friendlyName, String mappingId, class_2960 namespace, boolean forceJoystick, boolean dontLoad) {
   public static final ControllerType DEFAULT = new ControllerType((String)null, "default", CUtil.rl("default"), false, false);
   public static final MapCodec<ControllerType> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(Codec.STRING.optionalFieldOf("name", (Object)null).forGetter(ControllerType::friendlyName), Codec.STRING.optionalFieldOf("mapping", DEFAULT.mappingId()).forGetter(ControllerType::mappingId), class_2960.field_25139.optionalFieldOf("namespace", DEFAULT.namespace()).forGetter(ControllerType::namespace), Codec.BOOL.optionalFieldOf("force_joystick", false).forGetter(ControllerType::forceJoystick), Codec.BOOL.optionalFieldOf("dont_load", false).forGetter(ControllerType::dontLoad)).apply(instance, ControllerType::new);
   });

   public ControllerType(@Nullable String friendlyName, String mappingId, class_2960 namespace, boolean forceJoystick, boolean dontLoad) {
      this.friendlyName = friendlyName;
      this.mappingId = mappingId;
      this.namespace = namespace;
      this.forceJoystick = forceJoystick;
      this.dontLoad = dontLoad;
   }

   public class_2960 getIconSprite() {
      return this.namespace.method_45138("controllers/");
   }

   public boolean isSteamDeck() {
      return this.namespace.equals(SteamDeckUtil.STEAM_DECK_NAMESPACE);
   }

   @Nullable
   public String friendlyName() {
      return this.friendlyName;
   }

   public String mappingId() {
      return this.mappingId;
   }

   public class_2960 namespace() {
      return this.namespace;
   }

   public boolean forceJoystick() {
      return this.forceJoystick;
   }

   public boolean dontLoad() {
      return this.dontLoad;
   }
}
