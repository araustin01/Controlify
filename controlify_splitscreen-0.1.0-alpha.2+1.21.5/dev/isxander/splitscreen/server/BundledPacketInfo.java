package dev.isxander.splitscreen.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.class_2540;
import net.minecraft.class_3222;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record BundledPacketInfo(boolean includeController, Collection<Integer> pawnIndexes) {
   public static final class_9139<class_2540, BundledPacketInfo> STREAM_CODEC;

   public BundledPacketInfo(boolean includeController, Collection<Integer> pawnIndexes) {
      this.includeController = includeController;
      this.pawnIndexes = pawnIndexes;
   }

   public static BundledPacketInfo create(SplitscreenPlayerInfo.Controller controller, List<class_3222> players) {
      boolean includeController = players.contains(controller.player());
      List<Integer> pawnIndexes = controller.subPlayerInfos().stream().map(SplitscreenPlayerInfo.SubPlayer::pawnIndex).toList();
      return new BundledPacketInfo(includeController, pawnIndexes);
   }

   public boolean includeController() {
      return this.includeController;
   }

   public Collection<Integer> pawnIndexes() {
      return this.pawnIndexes;
   }

   static {
      STREAM_CODEC = class_9139.method_56435(class_9135.field_48547, BundledPacketInfo::includeController, class_9135.method_56376(ArrayList::new, class_9135.field_48550), BundledPacketInfo::pawnIndexes, BundledPacketInfo::new);
   }
}
