package net.hydra.jojomod.mixin.shaders;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.client.shader.TimestopShaderManager;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class ShaderLevelRenderer {
    @Inject(method = "renderLevel", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupLevel(Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void render(PoseStack $$0, float partialTick, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci)
    {
        if (Minecraft.getInstance().player == null)
            return;
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null) {
            if (ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader) {
                ImmutableList<TimeStopInstance> listTs = ImmutableList.copyOf(((TimeStop) Minecraft.getInstance().player.level()).rdbt$getTimeStoppingEntitiesClient());
                if (!listTs.isEmpty()) {
                    for (int i = listTs.size() - 1; i >= 0; --i) {
                        TimeStopInstance tinstance = listTs.get(i);
                        if (tinstance != null) {
                            Roundabout.LOGGER.info("we're in " + tinstance);
                            TimestopShaderManager.renderBubble(new TimestopShaderManager.Bubble(
                                    new Vec3(tinstance.x, tinstance.y, tinstance.z),
                                    11,
                                    new Vec3(1., 1., 1.))
                            );
                        }
                    }
                }



                TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
                TimestopShaderManager.renderAll(partialTick);
            }
        }
    }
}