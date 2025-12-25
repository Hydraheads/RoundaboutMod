package net.hydra.jojomod.mixin.shaders;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersTheWorld;
import net.hydra.jojomod.stand.powers.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.client.shader.TimestopShaderManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class ShaderLevelRenderer {
    @Shadow @Nullable private ClientLevel level;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FDDD)V", shift=At.Shift.AFTER))
    private void renderShaders(PoseStack $$0, float partialTick, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci){
        if (Minecraft.getInstance().player == null)
            return;

        if (TimestopShaderManager.TIMESTOP_DEPTH_BUFFER != null) {
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.clear(Minecraft.ON_OSX);
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }

        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null) {
            if (ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader) {
                ImmutableList<TimeStopInstance> listTs = ImmutableList.copyOf(((TimeStop) Minecraft.getInstance().player.level()).rdbt$getTimeStoppingEntitiesClient());
                if (!listTs.isEmpty()) {
                    for (int i = listTs.size() - 1; i >= 0; --i) {
                        TimeStopInstance tinstance = listTs.get(i);
                        if (tinstance != null) {

                            Vec3 color = Vec3.ZERO;
                            //Determine the bubble's radius so it grows but only on full size time stops
                            float radius = ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite;
                            if (radius < 0){radius = 100000;}
                            float radius2 = radius;
                            float maxRadius = radius;
                            boolean full = false;
                            boolean full2 = false;
                            boolean subBubble = false;
                            boolean colorless = true;
                            boolean leg2 = false;
                            int maxDuration = ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum;


                            if (level != null) {
                                Entity ent = level.getEntity(tinstance.id);
                                if (ent instanceof LivingEntity LE) {
                                    if (((StandUser)LE).roundabout$getStandPowers() instanceof PowersTheWorld){
                                        maxDuration = ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld;
                                    }
                                }
                            }

                            if (tinstance.firstDuration >= maxDuration){
                                subBubble = true;
                                radius = Math.min(((tinstance.durationInterpolation) + partialTick)*(maxRadius/16.66f), maxRadius);
                                radius2 = Math.min(((tinstance.durationInterpolation) + partialTick)*(maxRadius/16.66f), maxRadius*2);
                                if (radius >= 24){
                                    full = true;
                                }
                                if (radius2 > maxRadius){
                                    radius2 = maxRadius - (radius2-maxRadius);
                                    leg2 = true;
                                }
                                if (radius2 >= 24){
                                    full2 = true;
                                }
                                colorless = false;
                            } else {
                                full = true;
                            }

                            // Determine the position of bubble precisely with interpolation
                            Vec3 locationVec = new Vec3(tinstance.x, tinstance.y, tinstance.z);
                            if (level != null){
                                Entity ent = level.getEntity(tinstance.id);
                                if (ent != null){
                                    Vec3 pos = ent.getEyePosition(partialTick).subtract(ent.getPosition(partialTick)).scale(0.5);
                                    pos = pos.add(ent.getPosition(partialTick));
                                    locationVec = new Vec3(pos.x(),pos.y(),pos.z());

                                    if (ent instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers()
                                            instanceof TWAndSPSharedPowers tp){
                                        color = tp.getTSColor();
                                        if (color.equals(new Vec3(1,1,1))){
                                            double t = radius2 / maxRadius;
                                            t = Mth.clamp(t, 0.0, 1.0);// smoothstep easing
                                            t = t * t * (3.0 - 2.0 * t);
                                            if (leg2){
                                                color = new Vec3(1.5f-t,1.5f,0.5f);
                                            } else {
                                                color = new Vec3(0.5f,0.5f+t,1.5f-t);
                                            }
                                        }
                                        if (color.equals(Vec3.ZERO)){
                                            colorless = true;
                                        }
                                    }
                                }
                            }


                            if (radius2 > 0 && !colorless && subBubble) {
                                TimestopShaderManager.renderBubble(new TimestopShaderManager.Bubble(
                                        new Vec3(locationVec.x, locationVec.y, locationVec.z),
                                        radius2,
                                        maxRadius,
                                        color,
                                        (full2),
                                        0.8f
                                ));
                            }
                            float gop = 0;
                            if (colorless && radius < maxRadius)
                                gop = 0.8f;
                            TimestopShaderManager.renderBubble(new TimestopShaderManager.Bubble(
                                    new Vec3(locationVec.x, locationVec.y, locationVec.z),
                                    radius,
                                    maxRadius,
                                    new Vec3(1., 1., 1.),
                                    full,
                                    gop
                            ));

                        }
                    }
                }
            }
        }
    }
}
