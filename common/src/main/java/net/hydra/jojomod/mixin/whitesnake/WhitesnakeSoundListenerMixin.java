package net.hydra.jojomod.mixin.whitesnake;

import com.mojang.blaze3d.audio.Listener;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.access.DiscBearer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEngineExecutor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class WhitesnakeSoundListenerMixin {
    @Shadow private boolean loaded;
    @Shadow @Final private Listener listener;
    @Shadow @Final private SoundEngineExecutor executor;

    @Inject(method = "updateSource", at = @At("TAIL"))
    private void roundaboutWhitesnake$listenerPosition(Camera camera, CallbackInfo ci) {
        if (!loaded) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null
                && ((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting()) {
            LivingEntity stand = powers.getPilotingStand();
            if (stand != null && stand.isAlive() && !stand.isRemoved()) {
                Vec3 position = stand.getEyePosition(minecraft.getFrameTime());
                executor.execute(() -> listener.setListenerPosition(position));
            }
        }
        float volume = minecraft.player == null
                || ((DiscBearer) minecraft.player).roundabout$hasHearingDisc()
                ? minecraft.options.getSoundSourceVolume(SoundSource.MASTER) : 0.0F;
        executor.execute(() -> listener.setGain(volume));
    }
}
