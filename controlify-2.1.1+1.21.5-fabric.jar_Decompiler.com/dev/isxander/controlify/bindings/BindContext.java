package dev.isxander.controlify.bindings;

import com.mojang.serialization.Lifecycle;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.gui.screen.RadialMenuScreen;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import java.util.function.Function;
import net.minecraft.class_2370;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_465;
import net.minecraft.class_5321;

public record BindContext(class_2960 id, Function<class_310, Boolean> isApplicable) {
   public static final class_2378<BindContext> REGISTRY = new class_2370(class_5321.method_29180(CUtil.rl("bind_context")), Lifecycle.stable());
   public static final BindContext UNKNOWN = register("unknown", (mc) -> {
      return true;
   });
   public static final BindContext IN_GAME = register("in_game", (mc) -> {
      return mc.field_1755 == null && mc.field_1687 != null && mc.field_1724 != null;
   });
   public static final BindContext ANY_SCREEN = register("screen", (mc) -> {
      return mc.field_1755 != null;
   });
   public static final BindContext REGULAR_SCREEN = register("regular_screen", (mc) -> {
      return mc.field_1755 != null && !Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled();
   });
   public static final BindContext CONTAINER = register("container", (mc) -> {
      return mc.field_1755 instanceof class_465;
   });
   public static final BindContext V_MOUSE_CURSOR = register("vmouse_cursor", (mc) -> {
      return mc.field_1755 != null && ScreenProcessorProvider.provide(mc.field_1755).virtualMouseBehaviour().hasCursor() && Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled();
   });
   public static final BindContext V_MOUSE_COMPAT = register("vmouse_compat", (mc) -> {
      return mc.field_1755 != null && ScreenProcessorProvider.provide(mc.field_1755).virtualMouseBehaviour() == VirtualMouseBehaviour.ENABLED && Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled();
   });
   public static final BindContext RADIAL_MENU = register("radial_menu", (mc) -> {
      return mc.field_1755 instanceof RadialMenuScreen;
   });

   public BindContext(class_2960 id, Function<class_310, Boolean> isApplicable) {
      this.id = id;
      this.isApplicable = isApplicable;
   }

   private static BindContext register(String path, Function<class_310, Boolean> predicate) {
      BindContext context = new BindContext(CUtil.rl(path), predicate);
      class_2378.method_10230(REGISTRY, context.id(), context);
      return context;
   }

   public class_2960 id() {
      return this.id;
   }

   public Function<class_310, Boolean> isApplicable() {
      return this.isApplicable;
   }
}
