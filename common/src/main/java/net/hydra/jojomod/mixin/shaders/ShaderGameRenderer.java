package net.hydra.jojomod.mixin.shaders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IShaderGameRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public class ShaderGameRenderer implements IShaderGameRenderer {

    /**Enables handling of shader logic and access to shader related function,
     * such as time stop desaturation*/

    @Unique
    public int roundabout$tsShaderStatus = 0;
    @Override
    public void roundabout$loadEffect(ResourceLocation $$0){
        this.loadEffect($$0);
    }

    @Override
    public boolean roundabout$tsShaderStatus() {
        return roundabout$tsShaderStatus == 1;
    }

    @Unique
    private int roundabout$frameCount = 0;
    @Override
    public float roundabout$getFrameCount() {
        return (float)roundabout$frameCount;
    }

    @Inject(method = "renderLevel", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderLevel(float $$0, long $$1, PoseStack $$2, CallbackInfo ci) {
        roundabout$frameCount++;
    }

    @Inject(method = "checkEntityPostEffect(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$checkEntityPostEffect(Entity $$0, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null && ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                if (!(ClientUtil.getScreenFreeze() && !((TimeStop) minecraft.player.level()).isTimeStoppingEntity(minecraft.player))) {
                    //this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
                }
            } else {
                this.postEffect = null;
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$tick(CallbackInfo ci) {
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null && ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
            boolean changed = false;
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                if (roundabout$tsShaderStatus == 0) {
                    if (!(ClientUtil.getScreenFreeze() && !((TimeStop) minecraft.player.level()).isTimeStoppingEntity(minecraft.player))) {
                        changed = true;
                        roundabout$tsShaderStatus = 1;
                        //this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
                    }
                }
            } else {
                if (roundabout$tsShaderStatus == 1) {
                    changed = true;
                    roundabout$tsShaderStatus = 0;
                    this.postEffect = null;
                }
            }

            if (changed) {
                Minecraft mc = Minecraft.getInstance();
                CameraType $$0 = mc.options.getCameraType();
                if ($$0.isFirstPerson() != mc.options.getCameraType().isFirstPerson()) {
                    mc.gameRenderer.checkEntityPostEffect(mc.options.getCameraType().isFirstPerson() ? mc.getCameraEntity() : null);
                }

                mc.levelRenderer.needsUpdate();
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    void loadEffect(ResourceLocation $$0){

    }

    @Shadow @Nullable
    private PostChain postEffect;

    @Shadow
    @Final
    Minecraft minecraft;


}
