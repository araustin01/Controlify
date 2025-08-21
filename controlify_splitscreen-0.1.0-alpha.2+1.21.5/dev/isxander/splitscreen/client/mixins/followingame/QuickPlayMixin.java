package dev.isxander.splitscreen.client.mixins.followingame;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchQuickPlayFormat;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_442;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_8496;
import net.minecraft.class_9112;
import net.minecraft.class_642.class_8678;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_8496.class})
public class QuickPlayMixin {
   @WrapMethod(
      method = {"method_51263(Lnet/minecraft/class_310;Ljava/lang/String;)V"}
   )
   private static void doRelaunchQuickplay(class_310 minecraft, String string, Operation<Void> original) {
      RelaunchQuickPlayFormat.parse(string).ifLeft((quickPlayFormat) -> {
         joinRelaunchedServer(minecraft, quickPlayFormat);
      }).ifRight((ip) -> {
         original.call(new Object[]{minecraft, ip});
      });
   }

   @Unique
   private static void joinRelaunchedServer(class_310 minecraft, RelaunchQuickPlayFormat format) {
      RemotePawnMain remotePawn = (RemotePawnMain)SplitscreenBootstrapper.getPawn().orElseThrow(() -> {
         return new IllegalStateException("Parsed relaunched quickplay format but this is not a pawn.");
      });
      format.nonce().ifPresent((nonce) -> {
         try {
            byte[] nonceBytes = Hex.decodeHex(nonce);
            remotePawn.getPawn().setLastLoginNonce(nonceBytes);
         } catch (DecoderException var3) {
            throw new IllegalStateException("Could not parse login nonce provided", var3);
         }
      });
      class_642 serverData = new class_642("Relaunched Initial World", format.ip(), class_8678.field_45611);
      class_639 serverAddress = class_639.method_2950(format.ip());
      class_412.method_36877(new class_442(), minecraft, serverAddress, serverData, true, (class_9112)null);
   }
}
