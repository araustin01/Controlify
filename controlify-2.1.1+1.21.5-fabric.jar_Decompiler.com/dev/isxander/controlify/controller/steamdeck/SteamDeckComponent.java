package dev.isxander.controlify.controller.steamdeck;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class SteamDeckComponent implements ECSComponent {
   public static final class_2960 ID = CUtil.rl("steam_deck");

   public class_2960 id() {
      return ID;
   }
}
