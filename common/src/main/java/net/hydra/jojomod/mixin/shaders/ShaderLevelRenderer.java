package net.hydra.jojomod.mixin.shaders;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
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

    @Inject(method = "renderLevel", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupLevel(Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void render(PoseStack $$0, float partialTick, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci)
    {
        if (Minecraft.getInstance().player == null)
            return;

        if (TimestopShaderManager.TIMESTOP_DEPTH_BUFFER != null)
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());

        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null) {
            if (ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader) {
                ImmutableList<TimeStopInstance> listTs = ImmutableList.copyOf(((TimeStop) Minecraft.getInstance().player.level()).rdbt$getTimeStoppingEntitiesClient());
                if (!listTs.isEmpty()) {
                    for (int i = listTs.size() - 1; i >= 0; --i) {
                        TimeStopInstance tinstance = listTs.get(i);
                        if (tinstance != null) {

                            // Determine the position of bubble precisely with interpolation
                            Vec3 locationVec = new Vec3(tinstance.x, tinstance.y, tinstance.z);
                            if (level != null){
                                Entity ent = level.getEntity(tinstance.id);
                                if (ent != null){
                                    Vec3 pos = ent.getEyePosition(partialTick).subtract(ent.getPosition(partialTick)).scale(0.5);
                                    pos = pos.add(ent.getPosition(partialTick));
                                    locationVec = new Vec3(pos.x(),pos.y(),pos.z());
                                }
                            }

                            //Determine the bubble's radius so it grows but only on full size time stops
                            float radius = ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite;
                            if (radius < 0){radius = 100000;}
                            float radius2 = radius;
                            float maxRadius = radius;
                            boolean full = false;
                            boolean full2 = false;
                            boolean subBubble = false;
                            boolean colorless = true;
                            if (tinstance.maxDuration >= 100){
                                radius = Math.min(((tinstance.maxDuration-tinstance.durationInterpolation) + partialTick)*6, maxRadius);
                                radius2 = Math.min(((tinstance.maxDuration-tinstance.durationInterpolation) + partialTick)*6, maxRadius*2);
                                if (radius >= 30){
                                    full = true;
                                }
                                if (radius2 > maxRadius){
                                    radius2 = maxRadius - (radius2-maxRadius);
                                }
                                if (radius2 >= 24){
                                    full2 = true;
                                }
                                colorless = false;
                            }

                            if (radius2 > 0 && !colorless) {
                                TimestopShaderManager.renderBubble(new TimestopShaderManager.Bubble(
                                        new Vec3(locationVec.x, locationVec.y, locationVec.z),
                                        radius2,
                                        maxRadius,
                                        new Vec3(1.5f, 0.5f, 0.5f),
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