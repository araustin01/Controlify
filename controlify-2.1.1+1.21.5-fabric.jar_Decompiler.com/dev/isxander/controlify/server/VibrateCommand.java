package dev.isxander.controlify.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.isxander.controlify.platform.network.SidedNetworkApi;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.server.packets.EntityVibrationPacket;
import dev.isxander.controlify.server.packets.OriginVibrationPacket;
import dev.isxander.controlify.server.packets.VibrationPacket;
import dev.isxander.controlify.utils.CUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.class_1297;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2172;
import net.minecraft.class_2186;
import net.minecraft.class_2277;
import net.minecraft.class_2321;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_3222;

public class VibrateCommand {
   private static final SuggestionProvider<class_2168> SOURCES_SUGGESTION = class_2321.method_10022(CUtil.rl("vibration_sources"), (context, builder) -> {
      return class_2172.method_9270(RumbleSource.values().stream().map(RumbleSource::id).toList(), builder);
   });

   public static void register(CommandDispatcher<class_2168> dispatcher) {
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("vibratecontroller").requires((source) -> {
         return source.method_9259(2);
      })).then(class_2170.method_9244("receivers", class_2186.method_9308()).then(class_2170.method_9244("low_freq_vibration", FloatArgumentType.floatArg(0.0F, 1.0F)).then(class_2170.method_9244("high_freq_vibration", FloatArgumentType.floatArg(0.0F, 1.0F)).then(((RequiredArgumentBuilder)class_2170.method_9244("duration", IntegerArgumentType.integer(1)).then(class_2170.method_9247("static").executes((context) -> {
         return vibrateStatic((class_2168)context.getSource(), class_2186.method_9312(context, "receivers"), FloatArgumentType.getFloat(context, "low_freq_vibration"), FloatArgumentType.getFloat(context, "high_freq_vibration"), IntegerArgumentType.getInteger(context, "duration"), RumbleSource.MASTER);
      }))).then(class_2170.method_9247("positioned").then(((RequiredArgumentBuilder)class_2170.method_9244("range", FloatArgumentType.floatArg(0.0F)).then(class_2170.method_9244("position", class_2277.method_9735(true)).executes((context) -> {
         return vibrateFromOrigin((class_2168)context.getSource(), class_2186.method_9312(context, "receivers"), class_2277.method_9736(context, "position"), FloatArgumentType.getFloat(context, "range"), IntegerArgumentType.getInteger(context, "duration"), FloatArgumentType.getFloat(context, "low_freq_vibration"), FloatArgumentType.getFloat(context, "high_freq_vibration"), RumbleSource.MASTER);
      }))).then(class_2170.method_9244("entity", class_2186.method_9309()).executes((context) -> {
         return vibrateFromEntity((class_2168)context.getSource(), class_2186.method_9312(context, "receivers"), class_2186.method_9313(context, "entity"), FloatArgumentType.getFloat(context, "range"), IntegerArgumentType.getInteger(context, "duration"), FloatArgumentType.getFloat(context, "low_freq_vibration"), FloatArgumentType.getFloat(context, "high_freq_vibration"), RumbleSource.MASTER);
      })))))))));
   }

   private static int vibrateStatic(class_2168 source, Collection<class_3222> targets, float lowFreqMagnitude, float highFreqMagnitude, int durationTicks, RumbleSource rumbleSource) {
      RumbleState[] frames = new RumbleState[durationTicks];
      Arrays.fill(frames, new RumbleState(lowFreqMagnitude, highFreqMagnitude));
      VibrationPacket packet = new VibrationPacket(rumbleSource, frames);
      Iterator var8 = targets.iterator();

      while(var8.hasNext()) {
         class_3222 player = (class_3222)var8.next();
         SidedNetworkApi.S2C().sendPacket(player, VibrationPacket.CHANNEL, packet);
      }

      source.method_9226(() -> {
         return targets.size() == 1 ? class_2561.method_43471("controlify.command.vibratecontroller.static.single") : class_2561.method_43469("controlify.command.vibratecontroller.static.multiple", new Object[]{targets.size()});
      }, true);
      return targets.size();
   }

   private static int vibrateFromOrigin(class_2168 source, Collection<class_3222> targets, class_243 origin, float effectRange, int duration, float lowFreqMagnitude, float highFreqMagnitude, RumbleSource rumbleSource) {
      RumbleState state = new RumbleState(lowFreqMagnitude, highFreqMagnitude);
      OriginVibrationPacket packet = new OriginVibrationPacket(origin.method_46409(), effectRange, duration, state, rumbleSource);
      Iterator var10 = targets.iterator();

      while(var10.hasNext()) {
         class_3222 player = (class_3222)var10.next();
         SidedNetworkApi.S2C().sendPacket(player, OriginVibrationPacket.CHANNEL, packet);
      }

      source.method_9226(() -> {
         return targets.size() == 1 ? class_2561.method_43469("controlify.command.vibratecontroller.pos.single", new Object[]{formatDouble(origin.field_1352), formatDouble(origin.field_1351), formatDouble(origin.field_1350)}) : class_2561.method_43469("controlify.command.vibratecontroller.pos.multiple", new Object[]{targets.size(), formatDouble(origin.field_1352), formatDouble(origin.field_1351), formatDouble(origin.field_1350)});
      }, true);
      return targets.size();
   }

   private static int vibrateFromEntity(class_2168 source, Collection<class_3222> targets, class_1297 origin, float effectRange, int duration, float lowFreqMagnitude, float highFreqMagnitude, RumbleSource rumbleSource) {
      RumbleState state = new RumbleState(lowFreqMagnitude, highFreqMagnitude);
      EntityVibrationPacket packet = new EntityVibrationPacket(origin.method_5628(), effectRange, duration, state, rumbleSource);
      Iterator var10 = targets.iterator();

      while(var10.hasNext()) {
         class_3222 player = (class_3222)var10.next();
         SidedNetworkApi.S2C().sendPacket(player, EntityVibrationPacket.CHANNEL, packet);
      }

      source.method_9226(() -> {
         return targets.size() == 1 ? class_2561.method_43469("controlify.command.vibratecontroller.entity.single", new Object[]{origin.method_5476()}) : class_2561.method_43469("controlify.command.vibratecontroller.entity.multiple", new Object[]{targets.size(), origin.method_5476()});
      }, true);
      return targets.size();
   }

   private static String formatDouble(double d) {
      return String.format(Locale.ROOT, "%f", d);
   }
}
