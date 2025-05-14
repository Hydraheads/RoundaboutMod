package net.hydra.jojomod.mixin;


import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IClientLevel;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FogRenderer.class)
public class ZFogRenderer {

    @Final
    @Shadow
    private static int WATER_FOG_DISTANCE = 96;
    @Final
    @Shadow
    public static float BIOME_FOG_TRANSITION_TIME = 5000.0F;
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;
    @Shadow
    private static int targetBiomeFog;
    @Shadow
    private static int previousBiomeFog;
    @Shadow
    private static long biomeChangedTime;

    @Unique
    private static boolean roundabout$tempBlind = false;

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "TAIL"))
    private static void roundabout$setupFogTail(Camera $$0, FogRenderer.FogMode $$1, float $$2, boolean $$3, float $$4, CallbackInfo ci) {
        if (roundabout$tempBlind){
            roundabout$tempBlind = false;
            if ($$0.getEntity() instanceof Player PE){
                PE.removeEffect(MobEffects.BLINDNESS);
            }
        }
    }
    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$setupFog(Camera $$0, FogRenderer.FogMode $$1, float $$2, boolean $$3, float $$4, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null){
            Level lvl = Minecraft.getInstance().player.level();
            IClientLevel icl = ((IClientLevel) lvl);
            float skyLerp = icl.roundabout$getSkyLerp();
            float maxSkyLerp = icl.roundabout$getMaxSkyLerp();
            Entity $$6 = $$0.getEntity();
            if (skyLerp > 0){

                FogType $$5 = $$0.getFluidInCamera();
                float fogCutRange = 60f;
                if (((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers().canSeeThroughFog()){
                    fogCutRange = 140;
                }

                float start = 0;
                float end = 0;
                FogShape shape = FogShape.SPHERE;
                boolean isBlind = false;
                boolean isDark = false;
                if ($$6 instanceof LivingEntity LE) {
                    isBlind = LE.hasEffect(MobEffects.BLINDNESS);
                    isDark = LE.hasEffect(MobEffects.DARKNESS);
                }
                if ($$5 == FogType.LAVA) {
                    if ($$6.isSpectator()) {
                        start = -8.0F;
                        end = $$2 * 0.5F;
                    } else if ($$6 instanceof LivingEntity && ((LivingEntity)$$6).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                        start = 0.0F;
                        end = 3.0F;
                    } else {
                        start = 0.25F;
                        end = 1.0F;
                    }
                } else if ($$5 == FogType.POWDER_SNOW) {
                    if ($$6.isSpectator()) {
                        start = -8.0F;
                        end = $$2 * 0.5F;
                    } else {
                        start = 0.0F;
                        end = 2.0F;
                    }
                } else if (isBlind || isDark) {
                    LivingEntity $$9 = (LivingEntity)$$6;
                    if (isBlind){
                        MobEffectInstance mi = $$9.getEffect(MobEffects.BLINDNESS);
                        if (mi != null) {
                            float joever = mi.isInfiniteDuration() ? 5.0F : Mth.lerp(Math.min(1.0F, (float) mi.getDuration() / 20.0F), $$2, 5.0F);
                            if ($$1 == FogRenderer.FogMode.FOG_SKY) {
                                start = 0.0F;
                                end = joever * 0.8F;
                            } else {
                                start = joever * 0.25F;
                                end = joever;
                            }
                        }
                    } else if (isDark){
                        MobEffectInstance mi = $$9.getEffect(MobEffects.DARKNESS);
                        if (mi != null) {
                            if (!mi.getFactorData().isEmpty()) {
                                float joever = Mth.lerp(mi.getFactorData().get().getFactor($$9, $$4), $$2, 15.0F);
                                start = $$1 == FogRenderer.FogMode.FOG_SKY ? 0.0F : joever * 0.75F;
                                end = joever;
                            }
                        }
                    }
                } else if ($$5 == FogType.WATER) {
                    start = -8.0F;
                    end = 96.0F;
                    if ($$6 instanceof LocalPlayer $$11) {
                        end = end * Math.max(0.25F, $$11.getWaterVision());
                        Holder<Biome> $$12 = $$11.level().getBiome($$11.blockPosition());
                        if ($$12.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                            end *= 0.85F;
                        }
                    }

                    if (end > $$2) {
                        end = $$2;
                        shape = FogShape.CYLINDER;
                    }
                } else if ($$3) {
                    start = $$2 * 0.05F;
                    end = Math.min($$2, 192.0F) * 0.5F;
                } else if ($$1 == FogRenderer.FogMode.FOG_SKY) {
                    float joever = Math.min(fogCutRange+((((maxSkyLerp-skyLerp)/maxSkyLerp))*$$2),$$2);
                    start = 0;
                    end = joever;
                } else {
                    float joever = Math.min(fogCutRange+((((maxSkyLerp-skyLerp)/maxSkyLerp))*$$2),$$2);
                    start = 0;
                    end = joever;
                }

                RenderSystem.setShaderFogStart(start);
                RenderSystem.setShaderFogEnd(end);
                RenderSystem.setShaderFogShape(shape);
                ci.cancel();
            }
        }
    }

}
