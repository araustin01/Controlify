package dev.isxander.splitscreen.client.host.ipc;

import com.google.common.base.Suppliers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.ipc.ConnectionUtils;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import dev.isxander.splitscreen.client.ipc.SplitscreenConnection;
import dev.isxander.splitscreen.client.ipc.packets.HandshakeProtocols;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerDomainSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.class_2535;
import net.minecraft.class_2561;
import net.minecraft.class_2598;
import net.minecraft.class_310;
import net.minecraft.class_7648;
import org.slf4j.Logger;

public class ControllerConnectionListener {
   public static final Logger LOGGER = LogUtils.getLogger();
   public static final Supplier<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP = Suppliers.memoize(() -> {
      return new EpollEventLoopGroup(2, (new ThreadFactoryBuilder()).setNameFormat("Controlify netty epoll server IO #%d").setDaemon(true).build());
   });
   public static final Supplier<NioEventLoopGroup> SERVER_EVENT_GROUP = Suppliers.memoize(() -> {
      return new NioEventLoopGroup(2, (new ThreadFactoryBuilder()).setNameFormat("Controlify netty epoll server IO #%d").setDaemon(true).build());
   });
   private final List<ChannelFuture> channels = Collections.synchronizedList(new ArrayList());
   private final List<class_2535> connections = Collections.synchronizedList(new ArrayList());
   private final class_310 minecraft;
   private volatile boolean running = true;

   public ControllerConnectionListener(IPCMethod ipcMethod, SplitscreenController controller, class_310 minecraft) {
      this.minecraft = minecraft;
      Objects.requireNonNull(ipcMethod);
      byte var5 = 0;
      Throwable var13;
      boolean var10001;
      switch(ipcMethod.typeSwitch<invokedynamic>(ipcMethod, var5)) {
      case 0:
         IPCMethod.TCP var6 = (IPCMethod.TCP)ipcMethod;
         IPCMethod.TCP var15 = var6;

         int var16;
         try {
            var16 = var15.port();
         } catch (Throwable var12) {
            var13 = var12;
            var10001 = false;
            break;
         }

         int var17 = var16;
         this.startTcpListener(var17, controller);
         return;
      case 1:
         IPCMethod.Unix var8 = (IPCMethod.Unix)ipcMethod;
         IPCMethod.Unix var10000 = var8;

         String var14;
         try {
            var14 = var10000.path();
         } catch (Throwable var11) {
            var13 = var11;
            var10001 = false;
            break;
         }

         String var10 = var14;
         this.startUnixListener(var10, controller);
         return;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      Throwable var4 = var13;
      throw new MatchException(var4.toString(), var4);
   }

   private void startUnixListener(String socketPath, SplitscreenController controller) {
      LOGGER.info("Starting unix socket listener on {}", socketPath);
      Path socketPathFile = Path.of(socketPath, new String[0]);

      try {
         Files.createDirectories(socketPathFile.getParent());
         Files.deleteIfExists(socketPathFile);
      } catch (IOException var5) {
         LOGGER.error("Failed to cleanup socket path", var5);
      }

      this.startListener(controller, (ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(Epoll.isAvailable() ? EpollServerDomainSocketChannel.class : NioServerDomainSocketChannel.class)).localAddress((SocketAddress)(Epoll.isAvailable() ? new DomainSocketAddress(socketPath) : UnixDomainSocketAddress.of(socketPathFile))));
   }

   private void startTcpListener(int port, SplitscreenController controller) {
      LOGGER.info("Starting tcp listener on port {}", port);
      this.startListener(controller, (ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)).localAddress(InetAddress.getLoopbackAddress(), port));
   }

   private void startListener(SplitscreenController controller, ServerBootstrap boostrap) {
      synchronized(this.channels) {
         boostrap.group(Epoll.isAvailable() ? (EventLoopGroup)SERVER_EPOLL_EVENT_GROUP.get() : (EventLoopGroup)SERVER_EVENT_GROUP.get()).childHandler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel ch) {
               try {
                  ch.config().setOption(ChannelOption.TCP_NODELAY, true);
               } catch (ChannelException var4) {
               }

               ChannelPipeline pipeline = ch.pipeline().addLast("timeout", new ReadTimeoutHandler(5));
               ConnectionUtils.configureSerialization(pipeline, class_2598.field_11941, false, HandshakeProtocols.CONTROLLERBOUND);
               class_2535 connection = new SplitscreenConnection(class_2598.field_11941);
               ControllerConnectionListener.this.connections.add(connection);
               connection.method_53859(pipeline);
               connection.method_52912(new ControllerHandshakePacketListener(controller, connection, ControllerConnectionListener.this.minecraft));
               ControllerConnectionListener.LOGGER.info("Established connection with {}", ch.remoteAddress());
            }
         });
         this.channels.add(boostrap.bind().syncUninterruptibly());
      }
   }

   public void stop() {
      this.running = false;
      Iterator var1 = this.channels.iterator();

      while(var1.hasNext()) {
         ChannelFuture channel = (ChannelFuture)var1.next();

         try {
            channel.channel().close().sync();
         } catch (InterruptedException var4) {
            LOGGER.error("Failed to close channel", var4);
         }
      }

   }

   public void tick() {
      synchronized(this.connections) {
         Iterator it = this.connections.iterator();

         while(it.hasNext()) {
            class_2535 connection = (class_2535)it.next();
            if (!connection.method_10772()) {
               if (connection.method_10758()) {
                  try {
                     connection.method_10754();
                  } catch (Throwable var7) {
                     LOGGER.error("Failed to handle packet for {}", connection.method_52909(false), var7);
                     class_2561 component = class_2561.method_43470("Internal server error");
                     connection.method_10752(new PawnboundDisconnectPacket(component), class_7648.method_45084(() -> {
                        connection.method_10747(component);
                     }));
                     connection.method_10757();
                  }
               } else {
                  LOGGER.info("Disconnected {}", connection.method_52909(false));
                  it.remove();
                  connection.method_10768();
               }
            }
         }

      }
   }

   public List<class_2535> getConnections() {
      return Collections.unmodifiableList(this.connections);
   }
}
