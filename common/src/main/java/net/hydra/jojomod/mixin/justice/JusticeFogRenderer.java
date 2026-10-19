package net.hydra.jojomod.mixin.justice;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IClientLevel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.DiverDownControlsClient;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class JusticeFogRenderer {

    @Shadow
    private static float fogRed;

    @Shadow
    private static float fogBlue;

    @Shadow
    private static float fogGreen;

    /**This is the main part of where the fog rendering magic, the other part happens in the ClientLevel.
     * Vanilla fog rendering is relatively complex, so it may take a bit of examing to understand the code.
     * Star Platinum's scoping and Justice users can see through the fog better.*/

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$setupFog(Camera $$0, FogRenderer.FogMode $$1, float $$2, boolean $$3, float $$4, CallbackInfo ci) {
        //i needed an anti-xray for diver down and justice fog was literally perfect for this because it uses fog rendering
        //thanks hydra!
        if (DiverDownControlsClient.isDiving()) {
            ClientLevel clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null) {
                Vec3 pos = $$0.getPosition();
                //make a bounding box around the camera, so that no clipping shenanigans occur
                AABB camBox = new AABB(
                        pos.x - 0.1, pos.y - 0.1, pos.z - 0.1,
                        pos.x + 0.1, pos.y + 0.1, pos.z + 0.1
                );
                boolean isSolid = false;
                for (BlockPos p : BlockPos.betweenClosed(
                        Mth.floor(camBox.minX), Mth.floor(camBox.minY), Mth.floor(camBox.minZ),
                        Mth.floor(camBox.maxX), Mth.floor(camBox.maxY), Mth.floor(camBox.maxZ))) {
                    BlockState state = clientLevel.getBlockState(p);
                    // have to check if the block is solid or not because things like stairs don't trigger the anti xray, and that messes up the thing
                    if (!state.isAir()) {
                        if (state.isSolidRender(clientLevel, p)) {
                            isSolid = true;
                            break;
                        }

                        VoxelShape shape = state.getShape(clientLevel, p);

                        if (!shape.isEmpty()) {
                            AABB localCamBox = camBox.move(-p.getX(), -p.getY(), -p.getZ());
                            if (Shapes.joinIsNotEmpty(
                                    shape,
                                    Shapes.create(localCamBox),
                                    BooleanOp.AND)) {
                                isSolid = true;
                                break;
                            }
                        }
                    }
                }

                if (isSolid) {
                    RenderSystem.setShaderFogStart(0.0F);
                    RenderSystem.setShaderFogEnd(0.0F);
                    RenderSystem.setShaderFogShape(FogShape.SPHERE);
                    ci.cancel();
                    return;
                }
            }
        }
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
                            float joever = mi.isInfiniteDuration() ? 5.0F : Mth.lerp(Math.min(1.0F, (float) mi.getDuration() / ClientUtil.fadeTime), $$2, 5.0F);
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

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void roundabout$timeEraseFog(
            Camera camera,
            FogRenderer.FogMode mode,
            float renderDistance,
            boolean thickFog,
            float partialTick,
            CallbackInfo ci) {

        if (!ClientUtil.isUsingTimeErase)
            return;
        FogType $$5 = camera.getFluidInCamera();
        if ($$5 != FogType.NONE)
            return;
        float progress;

        float s = 23F;
        float e = 45.0F;

        float pt = partialTick;
        if (!ClientUtil.isUsingTimeErase){
            pt*=-1;
        }

        progress = Math.max(((ClientUtil.renderTimeEraseTime() + pt) / ClientUtil.fadeTime),0);
        progress = Math.min(progress,1);

        // smoothstep
        progress = progress * progress * (3.0F - 2.0F * progress);

        float start = Mth.lerp(progress, renderDistance * 0.75F, s);
        float end = Mth.lerp(progress, renderDistance, e);

        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
    }
    @Inject(method = "setupColor", at = @At("RETURN"))
    private static void roundabout$timeEraseFogColor(
            Camera camera,
            float partialTick,
            ClientLevel level,
            int renderDistance,
            float darkenWorldAmount,
            CallbackInfo ci) {

        if (ClientUtil.TimeErase < 1 || !ClientUtil.isUsingTimeErase)
            return;

        FogType $$5 = camera.getFluidInCamera();
        if ($$5 != FogType.NONE)
            return;

        float pt = partialTick;
        if (!ClientUtil.isUsingTimeErase){
            pt*=-1;
        }
        float progress = Mth.clamp(
                (ClientUtil.renderTimeEraseTime() + pt) / ClientUtil.fadeTime,
                0F,
                1F
        );

        progress = progress * progress * (3F - 2F * progress);

        float r = 0.07F;
        float g = 0.00F;
        float b = 0.05F;
        RenderSystem.clearColor(r,
                g,
                b,
                1.0F);
        fogRed = r;
        fogGreen = g;
        fogBlue =b;
    }

    //basically the same checks as above, but this time for color
    @Inject(method = "setupColor", at = @At("HEAD"), cancellable = true)
    private static void roundabout$diverDownXrayColorBlock(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, CallbackInfo ci) {
        if (DiverDownControlsClient.isDiving() && level != null) {
            Vec3 pos = camera.getPosition();
            AABB camBox = new AABB(
                    pos.x - 0.1, pos.y - 0.1, pos.z - 0.1,
                    pos.x + 0.1, pos.y + 0.1, pos.z + 0.1
            );
            boolean isSolid = false;

            for (BlockPos p : BlockPos.betweenClosed(
                    Mth.floor(camBox.minX), Mth.floor(camBox.minY), Mth.floor(camBox.minZ),
                    Mth.floor(camBox.maxX), Mth.floor(camBox.maxY), Mth.floor(camBox.maxZ))) {

                BlockState state = level.getBlockState(p);

                if (!state.isAir()) {
                    if (state.isSolidRender(level, p)) {
                        isSolid = true;
                        break;
                    }

                    VoxelShape shape = state.getShape(level, p);

                    if (!shape.isEmpty()) {
                        AABB localCamBox = camBox.move(-p.getX(), -p.getY(), -p.getZ());

                        if (Shapes.joinIsNotEmpty(
                                shape,
                                Shapes.create(localCamBox),
                                BooleanOp.AND)) {
                            isSolid = true;
                            break;
                        }
                    }
                }
            }

            if (isSolid) {
                // black fog, completely block everything
                RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 1.0F);
                fogRed = 0.0F;
                fogGreen = 0.0F;
                fogBlue = 0.0F;
                ci.cancel();
            }
        }
    }
}