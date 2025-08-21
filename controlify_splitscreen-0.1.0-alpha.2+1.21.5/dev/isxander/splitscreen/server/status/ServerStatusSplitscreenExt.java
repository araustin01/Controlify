package dev.isxander.splitscreen.server.status;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.splitscreen.util.CSUtil;
import dev.isxander.splitscreen.util.CodecExtensionHelper;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import net.minecraft.class_2926;
import net.minecraft.class_2960;
import net.minecraft.class_642;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ServerStatusSplitscreenExt(int[] supportedProtocols, int maxSubPlayers) {
   public static final class_2960 SPLITSCREEN_SUPPORTED_SPRITE = CSUtil.rl("splitscreen_supported");
   public static final class_2960 SPLITSCREEN_UNSUPPORTED_SPRITE = CSUtil.rl("splitscreen_unsupported");
   public static final Codec<ServerStatusSplitscreenExt> CODEC = RecordCodecBuilder.create((instance) -> {
      return instance.group(Codec.INT_STREAM.fieldOf("supported_protocols").xmap(IntStream::toArray, IntStream::of).forGetter(ServerStatusSplitscreenExt::supportedProtocols), Codec.INT.optionalFieldOf("max_sub_players", -1).forGetter(ServerStatusSplitscreenExt::maxSubPlayers)).apply(instance, ServerStatusSplitscreenExt::new);
   });
   public static final Codec<Optional<ServerStatusSplitscreenExt>> CODEC_FIELD_OPT;
   public static final ThreadLocal<ServerStatusSplitscreenExt> inProgressParam;

   public ServerStatusSplitscreenExt(int[] supportedProtocols, int maxSubPlayers) {
      this.supportedProtocols = supportedProtocols;
      this.maxSubPlayers = maxSubPlayers;
   }

   public static Codec<class_2926> wrapCodec(Codec<class_2926> codec) {
      return CodecExtensionHelper.buildExtensionCodec(codec, CODEC_FIELD_OPT, ServerStatusSplitscreenExt::getExt, inProgressParam);
   }

   public static class_2926 copyWithExt(class_2926 vanilla, ServerStatusSplitscreenExt ext) {
      return (class_2926)construct(() -> {
         return new class_2926(vanilla.comp_1273(), vanilla.comp_1274(), vanilla.comp_1275(), vanilla.comp_1276(), vanilla.comp_1277());
      }, ext);
   }

   public static <T> T construct(Supplier<T> ctor, ServerStatusSplitscreenExt ext) {
      Object var2;
      try {
         inProgressParam.set(ext);
         var2 = ctor.get();
      } finally {
         inProgressParam.remove();
      }

      return var2;
   }

   public static Optional<ServerStatusSplitscreenExt> getExt(@NotNull class_2926 vanilla) {
      return getExt0(vanilla);
   }

   public static Optional<ServerStatusSplitscreenExt> getExt(@NotNull class_642 vanilla) {
      return getExt0(vanilla);
   }

   public static void setExt(@NotNull class_642 vanilla, @Nullable ServerStatusSplitscreenExt ext) {
      setExt0(vanilla, ext);
   }

   private static Optional<ServerStatusSplitscreenExt> getExt0(@NotNull Object vanilla) {
      ServerStatusSplitscreenExt.Duck duck = (ServerStatusSplitscreenExt.Duck)vanilla;
      return Optional.ofNullable(duck.splitscreen$getExt());
   }

   private static void setExt0(@NotNull Object vanilla, @Nullable ServerStatusSplitscreenExt ext) {
      ServerStatusSplitscreenExt.Duck duck = (ServerStatusSplitscreenExt.Duck)vanilla;
      duck.splitscreen$setExt(ext);
   }

   public int[] supportedProtocols() {
      return this.supportedProtocols;
   }

   public int maxSubPlayers() {
      return this.maxSubPlayers;
   }

   static {
      CODEC_FIELD_OPT = CODEC.lenientOptionalFieldOf("isxander_splitscreen").codec();
      inProgressParam = new ThreadLocal();
   }

   public interface Duck {
      ServerStatusSplitscreenExt splitscreen$getExt();

      void splitscreen$setExt(ServerStatusSplitscreenExt var1);
   }
}
