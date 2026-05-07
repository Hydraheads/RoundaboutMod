package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.client.ModMusic;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class FateMusicManager {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Nullable private SoundInstance currentMusic;

    @Shadow private int nextSongDelay;

    @Shadow @Final private RandomSource random;

    @Shadow public abstract void startPlaying(Music music);
    @Unique
    private static final ModMusic RDBT$SILENT_REVERIE = new ModMusic(ModSounds.VSONG_SILENT_REVERIES_EVENT, 12000, 24000, false);
    @Unique
    private static final ModMusic RDBT$TWISTED = new ModMusic(ModSounds.VSONG_TWISTED_EVENT, 12000, 24000, false);
    @Unique
    private static final ModMusic RDBT$GOTHIC_ORGAN = new ModMusic(ModSounds.VSONG_GOTHIC_ORGAN_EVENT, 12000, 24000, false);
    @Unique
    private static final ModMusic RDBT$DAMNABLE_CEREMONY = new ModMusic(ModSounds.VSONG_DAMNABLE_CEREMONY_EVENT, 12000, 24000, false);
    @Unique
    private static final ModMusic RDBT$BLOODCURDLING_MOMENTS = new ModMusic(ModSounds.VSONG_BLOODCURDLING_MOMENTS_EVENT, 12000, 24000, false);

    /*Creepy vampire music*/
    @Inject(method = "tick()V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        if (minecraft.player != null){
            if (ConfigManager.getClientConfig() != null && ConfigManager.getClientConfig().vampireOSTChange) {
                if (FateTypes.isEvil(minecraft.player)) {
                    ci.cancel();

                    ModMusic $$0 = rdbt$getModMusic();
                    if (this.currentMusic != null) {
                        if (!$$0.getEvent().getLocation().equals(this.currentMusic.getLocation()) && $$0.replaceCurrentMusic()) {
                            this.minecraft.getSoundManager().stop(this.currentMusic);
                            this.nextSongDelay = Mth.nextInt(this.random, 0, $$0.getMinDelay() / 2);
                        }

                        if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
                            this.currentMusic = null;
                            this.nextSongDelay = Math.min(this.nextSongDelay, Mth.nextInt(this.random, $$0.getMinDelay(), $$0.getMaxDelay()));
                        }
                    }

                    this.nextSongDelay = Math.min(this.nextSongDelay, $$0.getMaxDelay());
                    if (this.currentMusic == null && this.nextSongDelay-- <= 0) {
                        this.rdbt$startPlaying($$0);
                    }
                }
            }
        }
    }

    @Unique
    public void rdbt$startPlaying(ModMusic $$0) {
        this.currentMusic = SimpleSoundInstance.forMusic($$0.getEvent());
        if (this.currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
            this.minecraft.getSoundManager().play(this.currentMusic);
        }

        this.nextSongDelay = Integer.MAX_VALUE;
    }

    @Unique
    public ModMusic rdbt$getModMusic(){
        double rand = Math.random();
        if (rand < 0.2){
            return RDBT$GOTHIC_ORGAN;
        } else if (rand < 0.4){
            return RDBT$SILENT_REVERIE;
        } else if (rand < 0.6){
            return RDBT$DAMNABLE_CEREMONY;
        } else if (rand < 0.8){
            return RDBT$BLOODCURDLING_MOMENTS;
        }
        return RDBT$TWISTED;
    }
}
