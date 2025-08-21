package dev.isxander.splitscreen.server.login;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.ControllerBridge;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.config.SplitscreenConfig;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.host.gui.SplitscreenDisconnectedScreen;
import dev.isxander.splitscreen.client.host.util.LANUtil;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import dev.isxander.splitscreen.server.login.packets.ClientboundIdentifyPacket;
import dev.isxander.splitscreen.server.login.packets.ClientboundNoncePacket;
import dev.isxander.splitscreen.server.login.packets.ServerboundIdentifyPacket;
import dev.isxander.splitscreen.server.login.packets.ServerboundNonceAckPacket;
import dev.isxander.splitscreen.server.mixins.login.ClientHandshakePacketListenerImplAccessor;
import dev.isxander.splitscreen.server.mixins.login.DisconnectedScreenAccessor;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.class_639;
import org.slf4j.Logger;

public class SplitscreenLoginFlowClient {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void init() {
      SplitscreenBootstrapper.getSide().ifPresent((side) -> {
         Optional<SplitscreenController> controllerOpt = SplitscreenBootstrapper.getController();
         Optional<RemotePawnMain> pawnOpt = SplitscreenBootstrapper.getPawn();
         ClientLoginNetworking.registerGlobalReceiver(SplitscreenLoginFlowServer.CHANNEL_IDENTIFY, (client, listener, buf, sender) -> {
            LOGGER.info("Received splitscreen identify packet. This server supports Splitscreen!");
            ClientboundIdentifyPacket packet = (ClientboundIdentifyPacket)ClientboundIdentifyPacket.STREAM_CODEC.decode(buf);
            int requestedProtocolVersion = packet.protocolVersion();
            int supportedProtocolVersion = 1;
            if (requestedProtocolVersion != supportedProtocolVersion) {
               LOGGER.error("Requested splitscreen protocol version {} does not match supported version {}", requestedProtocolVersion, Integer.valueOf(supportedProtocolVersion));
               return CompletableFuture.completedFuture((Object)null);
            } else {
               Object var10000;
               switch(side) {
               case CONTROLLER:
                  SplitscreenController controller = (SplitscreenController)controllerOpt.orElseThrow();
                  int subPlayerCount = controller.getPawnCount(false);
                  LOGGER.info("Identifying as controller with {} sub-players", subPlayerCount);
                  var10000 = new ClientIdentification.Controller(subPlayerCount, SplitscreenConfig.INSTANCE.createSharedConfig());
                  break;
               case PAWN:
                  RemotePawnMain pawn = (RemotePawnMain)pawnOpt.orElseThrow();
                  UUID controllerUuid = (UUID)RelaunchArguments.HOST_UUID.get().orElseThrow();
                  int subPlayerIndex = (Integer)RelaunchArguments.PAWN_INDEX.get().orElseThrow() - 1;
                  byte[] nonce = pawn.getPawn().getLastLoginNonce();
                  byte[] hmac = SplitscreenLoginFlowServer.generateHmac(nonce, controllerUuid, subPlayerIndex);
                  LOGGER.info("Identifying as pawn with controller UUID {} and sub-player index {}", controllerUuid, subPlayerIndex);
                  var10000 = new ClientIdentification.Pawn(controllerUuid, hmac, subPlayerIndex);
                  break;
               default:
                  throw new MatchException((String)null, (Throwable)null);
               }

               ClientIdentification identification = var10000;
               return CompletableFuture.completedFuture((new ServerboundIdentifyPacket((ClientIdentification)identification)).encode());
            }
         });
         controllerOpt.ifPresent((controller) -> {
            ClientLoginNetworking.registerGlobalReceiver(SplitscreenLoginFlowServer.CHANNEL_CONTROLLER, (client, listener, buf, sender) -> {
               LOGGER.info("Received nonce packet.");
               ClientboundNoncePacket packet = (ClientboundNoncePacket)ClientboundNoncePacket.STREAM_CODEC.decode(buf);
               byte[] nonce = packet.nonce();
               ((SplitscreenController)controllerOpt.get()).getLocalPawn().setLastLoginNonce(nonce);
               class_639 address;
               if (!client.method_1496()) {
                  address = class_639.method_2950(((ClientHandshakePacketListenerImplAccessor)listener).getServerData().field_3761);
               } else {
                  address = LANUtil.getOrPublishLANServer(client.method_1576());
               }

               controller.forEachPawn((pawn) -> {
                  pawn.joinServer(address.method_2952(), address.method_2954(), controller.getLocalPawn().getLastLoginNonce());
               });
               return CompletableFuture.completedFuture((new ServerboundNonceAckPacket()).encode());
            });
         });
         ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof DisconnectedScreenAccessor) {
               DisconnectedScreenAccessor accessor = (DisconnectedScreenAccessor)screen;
               if (!(screen instanceof SplitscreenDisconnectedScreen)) {
                  ((ControllerBridge)SplitscreenBootstrapper.getControllerBridge().orElseThrow()).serverDisconnected(accessor.getDetails().comp_2853());
               }
            }

         });
      });
   }
}
