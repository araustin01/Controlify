package dev.isxander.splitscreen.client.ipc;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.flow.FlowControlHandler;
import net.minecraft.class_10551;
import net.minecraft.class_10552;
import net.minecraft.class_2543;
import net.minecraft.class_2545;
import net.minecraft.class_2550;
import net.minecraft.class_2552;
import net.minecraft.class_2598;
import net.minecraft.class_8762;
import net.minecraft.class_9127;
import net.minecraft.class_9130.class_9131;
import net.minecraft.class_9130.class_9133;

public class ConnectionUtils {
   public static void configureSerialization(ChannelPipeline pipeline, class_2598 inbound, boolean memoryOnly, class_9127<?> initialProtocol) {
      class_2598 outbound = inbound.method_36146();
      boolean decoder = inbound == class_2598.field_11941;
      boolean encoder = outbound == class_2598.field_11941;
      pipeline.addLast("splitter", createFrameDecoder(memoryOnly)).addLast(new ChannelHandler[]{new FlowControlHandler()}).addLast(inboundHandlerName(decoder), (ChannelHandler)(decoder ? new class_2543(initialProtocol) : new class_9131())).addLast("prepender", createFrameEncoder(memoryOnly)).addLast(outboundHandlerName(encoder), (ChannelHandler)(encoder ? new class_2545(initialProtocol) : new class_9133()));
   }

   public static ChannelOutboundHandler createFrameEncoder(boolean memoryOnly) {
      return (ChannelOutboundHandler)(memoryOnly ? new class_10552() : new class_2552());
   }

   public static ChannelInboundHandler createFrameDecoder(boolean memoryOnly) {
      return (ChannelInboundHandler)(memoryOnly ? new class_10551() : new class_2550((class_8762)null));
   }

   public static String inboundHandlerName(boolean serverbound) {
      return serverbound ? "decoder" : "inbound_config";
   }

   public static String outboundHandlerName(boolean clientbound) {
      return clientbound ? "encoder" : "outbound_config";
   }
}
