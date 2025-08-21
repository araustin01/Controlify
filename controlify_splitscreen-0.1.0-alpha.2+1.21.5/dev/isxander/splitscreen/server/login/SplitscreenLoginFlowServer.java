package dev.isxander.splitscreen.server.login;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.config.SplitscreenServerConfig;
import dev.isxander.splitscreen.config.SplitscreenServerSharedConfig;
import dev.isxander.splitscreen.server.login.packets.ClientboundIdentifyPacket;
import dev.isxander.splitscreen.server.login.packets.ClientboundNoncePacket;
import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import dev.isxander.splitscreen.util.CSUtil;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.LoginPacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.class_2535;
import net.minecraft.class_2915;
import net.minecraft.class_2960;
import net.minecraft.class_3248;
import net.minecraft.class_9812;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class SplitscreenLoginFlowServer {
   public static final int PROTOCOL_VERSION = 1;
   public static final class_2960 CHANNEL_IDENTIFY = CSUtil.rl("splitscreen_identify");
   public static final class_2960 CHANNEL_CONTROLLER = CSUtil.rl("splitscreen_controller");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Random RANDOM = new SecureRandom();
   private static final Map<UUID, SplitscreenLoginFlowServer.ControllerState> CONTROLLER_STATE = new ConcurrentHashMap();
   private static final Map<UUID, UUID> SUB_PLAYER_TO_CONTROLLER = new ConcurrentHashMap();

   public static void init() {
   }

   public static void startIdentifyFlow(class_3248 listener0, class_2535 connection, class_2915 helloPacket) {
      LoginPacketSender sender0 = ServerLoginNetworking.getSender(listener0);
      sender0.sendPacket(CHANNEL_IDENTIFY, (new ClientboundIdentifyPacket(1)).encode());
      ServerLoginNetworking.registerReceiver(listener0, CHANNEL_IDENTIFY, (param2, nullx, nullxx, nullxxx, nullxxxx, nullxxxxx) -> {
         // $FF: Couldn't be decompiled
      });
   }

   public static boolean onLoginComplete(class_3248 listener, GameProfile profile) {
      SplitscreenLoginFlowServer.ListenerState state = state(listener);
      ClientIdentification identification = state.identification;
      LoginPacketSender sender = ServerLoginNetworking.getSender(listener);
      LOGGER.info("Saved client identification {}", identification);
      if (identification instanceof ClientIdentification.Controller) {
         ClientIdentification.Controller var5 = (ClientIdentification.Controller)identification;
         ClientIdentification.Controller var10000 = var5;

         int subPlayerCount;
         SplitscreenServerSharedConfig var17;
         label32: {
            Throwable var15;
            label38: {
               int var16;
               boolean var10001;
               try {
                  var16 = var10000.subPlayerCount();
               } catch (Throwable var11) {
                  var15 = var11;
                  var10001 = false;
                  break label38;
               }

               int var8 = var16;
               subPlayerCount = var8;
               var10000 = var5;

               try {
                  var17 = var10000.config();
                  break label32;
               } catch (Throwable var10) {
                  var15 = var10;
                  var10001 = false;
               }
            }

            Throwable var12 = var15;
            throw new MatchException(var12.toString(), var12);
         }

         SplitscreenServerSharedConfig var13 = var17;
         SplitscreenServerSharedConfig config = var13;
         byte[] nonce = generateNonce();
         SplitscreenLoginFlowServer.ControllerState controllerState = new SplitscreenLoginFlowServer.ControllerState(profile, nonce, subPlayerCount, config);
         CONTROLLER_STATE.put(profile.getId(), controllerState);
         state.controllerState = controllerState;
         LOGGER.info("Sending nonce to controller {}", profile.getName());
         sender.sendPacket(CHANNEL_CONTROLLER, (new ClientboundNoncePacket(nonce)).encode());
         ServerLoginNetworking.registerReceiver(listener, CHANNEL_CONTROLLER, (server, listener1, understood, buf, synchronizer, sender1) -> {
            state.nonceAck |= understood;
            if (subPlayerCount == 0) {
               controllerState.allDone().complete((Object)null);
            }

         });
      }

      if (state.controllerState != null) {
         LOGGER.info("Delaying login for controller {}: waiting for sub-players", profile.getName());
         return true;
      } else {
         return false;
      }
   }

   public static void onClientDisconnect(GameProfile clientProfile, class_9812 disconnectionDetails) {
      SplitscreenLoginFlowServer.ControllerState state = state(clientProfile.getId());
      if (state != null) {
         Iterator var3 = state.subPlayerConnections.values().iterator();

         while(var3.hasNext()) {
            WeakReference<class_2535> connectionRef = (WeakReference)var3.next();
            class_2535 connection = (class_2535)connectionRef.get();
            if (connection != null) {
               connection.method_60924(disconnectionDetails);
            }
         }

         CONTROLLER_STATE.remove(clientProfile.getId());
      }

   }

   public static ServerStatusSplitscreenExt buildSplitscreenStatus() {
      int maxClients = (Integer)SplitscreenServerConfig.INSTANCE.maxClients.get();
      return new ServerStatusSplitscreenExt(new int[]{1}, maxClients);
   }

   public static SplitscreenLoginFlowServer.ListenerState state(class_3248 listener) {
      return ((LoginListenerStateHolder)listener).splitscreen$state();
   }

   @Nullable
   private static SplitscreenLoginFlowServer.ControllerState state(UUID uuid) {
      return (SplitscreenLoginFlowServer.ControllerState)CONTROLLER_STATE.get(uuid);
   }

   @Nullable
   public static SplitscreenLoginFlowServer.ControllerState getStateFromControllerOrSubplayer(UUID uuid) {
      SplitscreenLoginFlowServer.ControllerState controllerState = (SplitscreenLoginFlowServer.ControllerState)CONTROLLER_STATE.get(uuid);
      if (controllerState != null) {
         return controllerState;
      } else {
         UUID controllerUuid = (UUID)SUB_PLAYER_TO_CONTROLLER.get(uuid);
         return controllerUuid != null ? (SplitscreenLoginFlowServer.ControllerState)CONTROLLER_STATE.get(controllerUuid) : null;
      }
   }

   private static byte[] generateNonce() {
      byte[] nonce = new byte[16];
      RANDOM.nextBytes(nonce);
      return nonce;
   }

   static byte[] generateHmac(byte[] nonce, UUID controllerUuid, int subPlayerIndex) {
      ByteBuffer hmacBuf = ByteBuffer.allocate(20);
      hmacBuf.putLong(controllerUuid.getMostSignificantBits());
      hmacBuf.putLong(controllerUuid.getLeastSignificantBits());
      hmacBuf.putInt(subPlayerIndex);
      hmacBuf.flip();
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, nonce)).hmac(hmacBuf);
   }

   public static class ListenerState {
      private SplitscreenLoginFlowServer.ControllerState controllerState;
      @Nullable
      private ClientIdentification identification;
      private boolean passedSplitscreenAuth = false;
      private boolean nonceAck;

      public boolean passedSplitscreenAuth() {
         return this.passedSplitscreenAuth;
      }

      @Nullable
      public SplitscreenLoginFlowServer.ControllerState controllerState() {
         return this.controllerState;
      }

      public boolean hasAckedNonce() {
         return this.nonceAck;
      }

      public boolean canFinishLogin() {
         return this.controllerState.allDone().isDone() && (!(this.identification instanceof ClientIdentification.Controller) || this.nonceAck);
      }
   }

   public static class ControllerState {
      private final GameProfile hostProfile;
      private final byte[] nonce;
      private final int subPlayers;
      private final Set<Integer> subPlayerWaiting = ConcurrentHashMap.newKeySet();
      private final CompletableFuture<Void> allDoneFuture = new CompletableFuture();
      private final Map<UUID, WeakReference<class_2535>> subPlayerConnections = new ConcurrentHashMap();
      private final GameProfile[] subPlayerProfiles;
      private final SplitscreenServerSharedConfig sharedConfig;

      private ControllerState(GameProfile hostProfile, byte[] nonce, int subPlayers, SplitscreenServerSharedConfig sharedConfig) {
         this.hostProfile = hostProfile;
         this.nonce = nonce;
         this.subPlayers = subPlayers;

         for(int i = 0; i < subPlayers; ++i) {
            this.subPlayerWaiting.add(i);
         }

         this.subPlayerProfiles = new GameProfile[subPlayers];
         this.sharedConfig = sharedConfig;
      }

      public GameProfile hostProfile() {
         return this.hostProfile;
      }

      public GameProfile subPlayerProfile(int index) {
         String var10000 = this.hostProfile.getName();
         String username = var10000 + "." + (index + 1);
         UUID uuid = UUID.nameUUIDFromBytes((String.valueOf(this.hostProfile.getId()) + "splitscreen" + index).getBytes(StandardCharsets.UTF_8));
         return new GameProfile(uuid, username);
      }

      public CompletableFuture<Void> allDone() {
         return this.allDoneFuture;
      }

      public byte[] nonce() {
         return this.nonce;
      }

      public int subPlayers() {
         return this.subPlayers;
      }

      public boolean isValidSubPlayerIndex(int index) {
         return index >= 0 && index < this.subPlayers;
      }

      public boolean isNextSubPlayerIndex(int index) {
         return index == this.subPlayers;
      }

      public boolean isWaitingForSubPlayer(int index) {
         return this.subPlayerWaiting.contains(index);
      }

      public int getSubPlayerIndex(GameProfile profile) {
         for(int i = 0; i < this.subPlayerProfiles().length; ++i) {
            if (this.subPlayerProfiles()[i].equals(profile)) {
               return i;
            }
         }

         return -1;
      }

      public void signalSubPlayerFinished(int index, GameProfile profile, class_2535 connection) {
         if (this.subPlayerWaiting.remove(index)) {
            this.subPlayerConnections.put(profile.getId(), new WeakReference(connection));
            this.subPlayerProfiles[index] = profile;
            if (this.subPlayerWaiting.isEmpty()) {
               this.allDoneFuture.complete((Object)null);
            }
         } else if (!this.allDone().isDone()) {
            throw new IllegalStateException("Subplayer " + index + " was not waiting. Duplicate login?");
         }

      }

      public GameProfile[] subPlayerProfiles() {
         return this.subPlayerProfiles;
      }

      public class_2535 subPlayerConnection(UUID uuid) {
         WeakReference<class_2535> connection = (WeakReference)this.subPlayerConnections.get(uuid);
         return connection != null ? (class_2535)connection.get() : null;
      }

      public SplitscreenServerSharedConfig sharedConfig() {
         return this.sharedConfig;
      }
   }
}
