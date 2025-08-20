package net.hydra.jojomod.mixin.gravity.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BiomeAmbientSoundsHandler.class)
public class GravityBiomeAmbientSoundsHandlerMixin {
    @Shadow private float moodiness;

    @Shadow @Final private SoundManager soundManager;

    @Shadow @Final private LocalPlayer player;

    @Shadow @Final private RandomSource random;

    @Shadow private Optional<AmbientAdditionsSettings> additionsSettings;

    @Shadow private Optional<AmbientMoodSettings> moodSettings;

    @Shadow @Final private BiomeManager biomeManager;

    @Shadow @Nullable private Biome previousBiome;

    @Shadow @Final private Object2ObjectArrayMap<Biome, BiomeAmbientSoundsHandler.LoopSoundInstance> loopSounds;

    @Inject(
            method = "tick",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$tick(
            CallbackInfo ci
    ) {

        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        this.loopSounds.values().removeIf(AbstractTickableSoundInstance::isStopped);
        Biome $$0 = this.biomeManager.getNoiseBiomeAtPosition(this.player.getEyePosition().x, this.player.getY(), this.player.getEyePosition().z).value();
        if ($$0 != this.previousBiome) {
            this.previousBiome = $$0;
            this.moodSettings = $$0.getAmbientMood();
            this.additionsSettings = $$0.getAmbientAdditions();
            this.loopSounds.values().forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::fadeOut);
            $$0.getAmbientLoop().ifPresent($$1 -> this.loopSounds.compute($$0, ($$1x, $$2) -> {
                if ($$2 == null) {
                    $$2 = new BiomeAmbientSoundsHandler.LoopSoundInstance((SoundEvent)$$1.value());
                    this.soundManager.play($$2);
                }

                $$2.fadeIn();
                return (BiomeAmbientSoundsHandler.LoopSoundInstance)$$2;
            }));
        }

        this.additionsSettings.ifPresent($$0x -> {
            if (this.random.nextDouble() < $$0x.getTickChance()) {
                this.soundManager.play(SimpleSoundInstance.forAmbientAddition($$0x.getSoundEvent().value()));
            }
        });
        this.moodSettings
                .ifPresent(
                        $$0x -> {
                            Level $$1 = this.player.level();
                            int $$2 = $$0x.getBlockSearchExtent() * 2 + 1;
                            BlockPos $$3 = BlockPos.containing(
                                    this.player.getEyePosition().x + (double)this.random.nextInt($$2) - (double)$$0x.getBlockSearchExtent(),
                                    this.player.getEyePosition().y + (double)this.random.nextInt($$2) - (double)$$0x.getBlockSearchExtent(),
                                    this.player.getEyePosition().z + (double)this.random.nextInt($$2) - (double)$$0x.getBlockSearchExtent()
                            );
                            int $$4 = $$1.getBrightness(LightLayer.SKY, $$3);
                            if ($$4 > 0) {
                                this.moodiness = this.moodiness - (float)$$4 / (float)$$1.getMaxLightLevel() * 0.001F;
                            } else {
                                this.moodiness = this.moodiness - (float)($$1.getBrightness(LightLayer.BLOCK, $$3) - 1) / (float)$$0x.getTickDelay();
                            }

                            if (this.moodiness >= 1.0F) {
                                double $$5 = (double)$$3.getX() + 0.5;
                                double $$6 = (double)$$3.getY() + 0.5;
                                double $$7 = (double)$$3.getZ() + 0.5;
                                double $$8 = $$5 - this.player.getEyePosition().x;
                                double $$9 = $$6 - this.player.getEyePosition().y;
                                double $$10 = $$7 - this.player.getEyePosition().z;
                                double $$11 = Math.sqrt($$8 * $$8 + $$9 * $$9 + $$10 * $$10);
                                double $$12 = $$11 + $$0x.getSoundPositionOffset();
                                SimpleSoundInstance $$13 = SimpleSoundInstance.forAmbientMood(
                                        $$0x.getSoundEvent().value(),
                                        this.random,
                                        this.player.getEyePosition().x + $$8 / $$11 * $$12,
                                        this.player.getEyePosition().y + $$9 / $$11 * $$12,
                                        this.player.getEyePosition().z + $$10 / $$11 * $$12
                                );
                                this.soundManager.play($$13);
                                this.moodiness = 0.0F;
                            } else {
                                this.moodiness = Math.max(this.moodiness, 0.0F);
                            }
                        }
                );
    }
}
