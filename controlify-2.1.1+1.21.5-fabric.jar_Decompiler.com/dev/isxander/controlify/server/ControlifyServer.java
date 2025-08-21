package dev.isxander.controlify.server;

import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.platform.network.SidedNetworkApi;
import dev.isxander.controlify.server.packets.EntityVibrationPacket;
import dev.isxander.controlify.server.packets.OriginVibrationPacket;
import dev.isxander.controlify.server.packets.ServerPolicyPacket;
import dev.isxander.controlify.server.packets.VibrationPacket;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_3222;

public class ControlifyServer {
   private static ControlifyServer INSTANCE;

   public static ControlifyServer getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ControlifyServer();
      }

      return INSTANCE;
   }

   public void onInitialize() {
      ControlifyHandshake.setupOnServer();
      SidedNetworkApi.S2C().registerPacket(VibrationPacket.CHANNEL, VibrationPacket.CODEC);
      SidedNetworkApi.S2C().registerPacket(OriginVibrationPacket.CHANNEL, OriginVibrationPacket.CODEC);
      SidedNetworkApi.S2C().registerPacket(EntityVibrationPacket.CHANNEL, EntityVibrationPacket.CODEC);
      SidedNetworkApi.S2C().registerPacket(ServerPolicyPacket.CHANNEL, ServerPolicyPacket.CODEC);
      PlatformMainUtil.registerCommandRegistrationCallback((dispatcher, registry, env) -> {
         VibrateCommand.register(dispatcher);
      });
   }

   public void onInitializeServer() {
      ControlifyServerConfig.HANDLER.load();
      ControlifyServerConfig.HANDLER.save();
      CUtil.LOGGER.log("Reach-around policy: {}", ((ControlifyServerConfig)ControlifyServerConfig.HANDLER.instance()).reachAroundPolicy);
      CUtil.LOGGER.log("No-fly drift policy: {}", ((ControlifyServerConfig)ControlifyServerConfig.HANDLER.instance()).noFlyDriftPolicy);
      CUtil.LOGGER.log("Enforce keyboard-like movement: {}", ((ControlifyServerConfig)ControlifyServerConfig.HANDLER.instance()).enforceKeyboardLikeMovement);
      ControlifyServerConfig config = (ControlifyServerConfig)ControlifyServerConfig.HANDLER.instance();
      PlatformMainUtil.registerPlayerJoinedEvent((player) -> {
         this.setServerPolicy(ServerPolicies.REACH_AROUND, player, config.reachAroundPolicy);
         this.setServerPolicy(ServerPolicies.DISABLE_FLY_DRIFTING, player, config.noFlyDriftPolicy);
         this.setServerPolicy(ServerPolicies.KEYBOARD_LIKE_MOVEMENT, player, config.enforceKeyboardLikeMovement);
      });
   }

   private void setServerPolicy(ServerPolicies policy, class_3222 player, boolean option) {
      if (option != policy.getUnsetValue()) {
         SidedNetworkApi.S2C().sendPacket(player, ServerPolicyPacket.CHANNEL, new ServerPolicyPacket(policy.getId(), option));
      }
   }
}
