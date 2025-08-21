package dev.isxander.splitscreen.client.remote.ipc;

import com.google.common.base.Suppliers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.engine.SplitscreenEngine;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import dev.isxander.splitscreen.client.ipc.ConnectionUtils;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import dev.isxander.splitscreen.client.ipc.SplitscreenConnection;
import dev.isxander.splitscreen.client.ipc.packets.HandshakeProtocols;
import dev.isxander.splitscreen.client.ipc.packets.PlayProtocols;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.handshake.ControllerboundHandshakePacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundHelloPacket;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDomainSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.net.InetAddress;
import java.net.UnixDomainSocketAddress;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_2535;
import net.minecraft.class_2598;
import net.minecraft.class_310;
import net.minecraft.class_7648;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

public class PawnConnectionListener {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Supplier<EpollEventLoopGroup> NETWORK_EPOLL_WORKER_GROUP = Suppliers.memoize(() -> {
      return new EpollEventLoopGroup(2, (new ThreadFactoryBuilder()).setNameFormat("Controlify Netty Epoll Client IO #%d").setDaemon(true).build());
   });
   private static final Supplier<NioEventLoopGroup> NETWORK_WORKER_GROUP = Suppliers.memoize(() -> {
      return new NioEventLoopGroup(2, (new ThreadFactoryBuilder()).setNameFormat("Controlify Netty Client IO #%d").setDaemon(true).build());
   });
   private final class_2535 controllerConnection;
   private final RemotePawnMain remotePawnMain;

   public PawnConnectionListener(class_310 minecraft, IPCMethod connectionMethod, RemotePawnMain remotePawnMain) {
      class_2535 var15;
      label33: {
         super();
         this.remotePawnMain = remotePawnMain;
         Objects.requireNonNull(connectionMethod);
         byte var5 = 0;
         boolean var13;
         Throwable var10000;
         switch(connectionMethod.typeSwitch<invokedynamic>(connectionMethod, var5)) {
         case 0:
            IPCMethod.TCP var6 = (IPCMethod.TCP)connectionMethod;
            IPCMethod.TCP var16 = var6;

            int var17;
            try {
               var17 = var16.port();
            } catch (Throwable var12) {
               var10000 = var12;
               var13 = false;
               break;
            }

            int var18 = var17;
            var15 = this.connectToTcp(var18, minecraft);
            break label33;
         case 1:
            IPCMethod.Unix var8 = (IPCMethod.Unix)connectionMethod;
            IPCMethod.Unix var10001 = var8;

            String var14;
            try {
               var14 = var10001.path();
            } catch (Throwable var11) {
               var10000 = var11;
               var13 = false;
               break;
            }

            String var10 = var14;
            var15 = this.connectToUnixSocket(var10, minecraft);
            break label33;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         Throwable var4 = var10000;
         throw new MatchException(var4.toString(), var4);
      }

      this.controllerConnection = var15;
   }

   public class_2535 getControllerConnection() {
      return this.controllerConnection;
   }

   private class_2535 connectToUnixSocket(String socketPath, class_310 minecraft) {
      LOGGER.info("Connecting to controller unix socket at {}", socketPath);
      return this.connect(minecraft, ((Bootstrap)(new Bootstrap()).channel(Epoll.isAvailable() ? EpollDomainSocketChannel.class : NioDomainSocketChannel.class)).remoteAddress(UnixDomainSocketAddress.of(socketPath)));
   }

   private class_2535 connectToTcp(int port, class_310 minecraft) {
      LOGGER.info("Connecting to controller tcp port {}", port);
      return this.connect(minecraft, ((Bootstrap)(new Bootstrap()).channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)).remoteAddress(InetAddress.getLoopbackAddress(), port));
   }

   private class_2535 connect(class_310 minecraft, Bootstrap bootstrap) {
      Validate.isTrue(this.controllerConnection == null, "Already connected to a controller", new Object[0]);
      final class_2535 connection = new SplitscreenConnection(class_2598.field_11942);
      ((Bootstrap)((Bootstrap)bootstrap.group(Epoll.isAvailable() ? (EventLoopGroup)NETWORK_EPOLL_WORKER_GROUP.get() : (EventLoopGroup)NETWORK_WORKER_GROUP.get())).handler(new ChannelInitializer<Channel>(this) {
         protected void initChannel(Channel ch) {
            try {
               ch.config().setOption(ChannelOption.TCP_NODELAY, true);
            } catch (ChannelException var3) {
            }

            ChannelPipeline pipeline = ch.pipeline().addLast("timeout", new ReadTimeoutHandler(5));
            ConnectionUtils.configureSerialization(pipeline, class_2598.field_11942, false, HandshakeProtocols.CONTROLLERBOUND);
            connection.method_53859(pipeline);
            PawnConnectionListener.LOGGER.info("Established connection with controller");
         }
      })).connect().syncUninterruptibly();
      connection.method_52905((c) -> {
         SplitscreenEngine splitscreenEngine = this.remotePawnMain.getSplitscreenEngine();
         c.method_56330(PlayProtocols.pawnbound(splitscreenEngine.getPawnboundCustomPayloadCodec()), new PawnPlayPacketListener(c, this.remotePawnMain, minecraft));
         c.method_52906(new ControllerboundHandshakePacket(1), (class_7648)null, true);
         c.method_56329(PlayProtocols.controllerbound(splitscreenEngine.getControllerboundCustomPayloadCodec()));
      });
      connection.method_10743(new ControllerboundHelloPacket((ControllerUID)RelaunchArguments.CONTROLLER.get().orElse((Object)null)));
      ClientTickEvents.START_CLIENT_TICK.register((client) -> {
         connection.method_10754();
      });
      return connection;
   }
}
