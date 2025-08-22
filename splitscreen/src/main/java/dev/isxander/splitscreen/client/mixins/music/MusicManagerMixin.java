package dev.isxander.splitscreen.client.mixins.music;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
    /**
     * On pawn clients, forward music requests to the controller to avoid multiple tracks.
     * On the controller (host) or when splitscreen is inactive, use vanilla behavior.
     */
    @WrapMethod(method = "startPlaying")
    private void preventMusicIfPawn(MusicInfo music, Operation<Void> original) {
        if (SplitscreenBootstrapper.getPawn().isPresent()) {
            SplitscreenBootstrapper.getControllerBridge().ifPresent(bridge -> bridge.requestPlayMusic(music.music(), music.volume()));
        } else {
            original.call(music);
        }
    }
}
