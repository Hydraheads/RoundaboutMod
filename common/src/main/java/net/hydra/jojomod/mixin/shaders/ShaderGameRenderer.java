package net.hydra.jojomod.mixin.shaders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.IShaderGameRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.client.shader.RCoreShader;
import net.zetalasis.client.shader.RPostShaderRegistry;
import net.zetalasis.client.shader.TimestopShaderManager;
import net.zetalasis.client.shader.callback.RenderCallbackRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Mixin(GameRenderer.class)
public abstract class ShaderGameRenderer implements IShaderGameRenderer {

    @Shadow private int tick;

    @Shadow protected abstract double getFov(Camera camera, float f, boolean bl);

    /**Enables handling of shader logic and access to shader related function,
     * such as time stop desaturation*/

    @Unique
    public int sba$tsShaderStatus = 0;
    @Override
    public void sba$loadEffect(ResourceLocation $$0){
        this.loadEffect($$0);
    }

    @Override
    public boolean sba$tsShaderStatus() {
        return sba$tsShaderStatus == 1;
    }

    @Unique
    private int sba$frameCount = 0;
    @Override
    public float sba$getFrameCount() {
        return (float)sba$frameCount;
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.BEFORE))
    private void sba$renderShaders(float tickDelta, long $$1, boolean renderLevel, CallbackInfo ci)
    {
        TimestopShaderManager.renderAll(tickDelta);

        RenderCallbackRegistry.sba$GAME_RENDERER_FINISH(tickDelta);


        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null){
            if (!ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader && ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
                if (RPostShaderRegistry.DESATURATE != null) {
                    if (((IShaderGameRenderer) Minecraft.getInstance().gameRenderer).sba$tsShaderStatus()) {
                        RPostShaderRegistry.DESATURATE.sba$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);
                        RPostShaderRegistry.DESATURATE.sba$process(tickDelta);
                    }
                }
            }
        }

        if(ClientUtil.checkIfClientCanSeeMobsForWindVision()){
            RPostShaderRegistry.WIND_VISION.sba$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);
            RPostShaderRegistry.WIND_VISION.sba$process(tickDelta);
        }
        if(ClientUtil.canEpitaphRenderShader()){
            RPostShaderRegistry.EPITAPH.sba$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);

            RPostShaderRegistry.EPITAPH.sba$setUniform("GameTime",(float) ClientUtil.getPlayer().tickCount);
            RPostShaderRegistry.EPITAPH.sba$setUniform("GameTimeStart",(float) ClientUtil.GameTimeStart);
            RPostShaderRegistry.EPITAPH.sba$setUniform("PartialTick",tickDelta%1);
            Vec3 vec = new Vec3(1.0,0,1.0);

            if (((StandUser)ClientUtil.getPlayer()).sba$getStandPowers() instanceof PowersKingCrimson pkc){
                vec = pkc.getEpitaphColors();

            }
            RPostShaderRegistry.EPITAPH.sba$setUniform("RedValue",(float)vec.x);
            RPostShaderRegistry.EPITAPH.sba$setUniform("GreenValue",(float)vec.y);
            RPostShaderRegistry.EPITAPH.sba$setUniform("BlueValue",(float)vec.z);

            RPostShaderRegistry.EPITAPH.sba$process(tickDelta);
        }
        if(ClientUtil.canRenderWorldMerge()){
            if (PowerTypes.isInD4CWorldWithRender(ClientUtil.getPlayer()) ||
                    ((StandUser)ClientUtil.getPlayer()).sba$getStandPowers() instanceof PowersD4C) {
                RPostShaderRegistry.D4C_DIM_MERGE.sba$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);

                RPostShaderRegistry.D4C_DIM_MERGE.sba$setUniform("GameTime", (float) ClientUtil.getPlayer().tickCount);
                RPostShaderRegistry.D4C_DIM_MERGE.sba$setUniform("GameTimeStart", (float) ClientUtil.D4CTickStart);
                RPostShaderRegistry.D4C_DIM_MERGE.sba$setUniform("PartialTick", tickDelta % 1);

                RPostShaderRegistry.D4C_DIM_MERGE.sba$process(tickDelta);
            }
        } else if(ClientUtil.leftADimension){

            RPostShaderRegistry.D4C_DIM_MERGE_LEAVE.sba$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);

            RPostShaderRegistry.D4C_DIM_MERGE_LEAVE.sba$setUniform("GameTime",(float) ClientUtil.getPlayer().tickCount);
            RPostShaderRegistry.D4C_DIM_MERGE_LEAVE.sba$setUniform("GameTimeStart",(float) ClientUtil.D4CTickStart2);
            RPostShaderRegistry.D4C_DIM_MERGE_LEAVE.sba$setUniform("PartialTick",tickDelta%1);

            RPostShaderRegistry.D4C_DIM_MERGE_LEAVE.sba$process(tickDelta);
        }
    }

    @Inject(method = "reloadShaders", at=@At("HEAD"))
    private void sba$reloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        RPostShaderRegistry.bootstrap();

        try {
            RCoreShader.sba$meltDodgeProgram = Objects.requireNonNull(sba$registerShader(provider, "meltdodge")).getProgram();
            RCoreShader.sba$loveTrainProgram = Objects.requireNonNull(sba$registerShader(provider, "lovetrainlines")).getProgram();
        } catch(Exception e) {
            //put shader debug stuff here
            SBA.LOGGER.info("Oops, something went wrong loading shaders");
        }
        SBA.LOGGER.info("Reloaded shaders!");
    }

    @Unique private RCoreShader sba$registerShader(ResourceProvider provider, String name)
    {
        try
        {
            RCoreShader shader = new RCoreShader(provider, name);

            if (shader.getProgram() != null)
            {
                shader.getProgram().setSampler("DiffuseSampler", this.minecraft.getMainRenderTarget().getColorTextureId());
            }
            else {
                throw new IOException("Shader was null!");
            }

            return shader;
        }
        catch (IOException e)
        {
            SBA.LOGGER.warn("sba${} failed to load!", name);
            return null;
        }
    }

    @Inject(method = "renderLevel", at = @At(value = "HEAD"), cancellable = true)
    private void sba$renderLevel(float $$0, long $$1, PoseStack $$2, CallbackInfo ci) {
        // TODO: copy the old depth buffer to a texture
        sba$frameCount++;
    }

    @Inject(method = "checkEntityPostEffect(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "TAIL"), cancellable = true)
    private void sba$checkEntityPostEffect(Entity $$0, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null) {
            if (ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader) {

            } else if (ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
                if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                    if (!(ClientUtil.getScreenFreeze() && !((TimeStop) minecraft.player.level()).isTimeStoppingEntity(minecraft.player))) {
                        //this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
                    }
                } else {
                    this.postEffect = null;
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void sba$tick(CallbackInfo ci) {
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null){
            if (ConfigManager.getClientConfig().timeStopSettings.advancedTimeStopShader) {

            } else if (ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
                boolean changed = false;
                if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                    if (sba$tsShaderStatus == 0) {
                        if (!(ClientUtil.getScreenFreeze() && !((TimeStop) minecraft.player.level()).isTimeStoppingEntity(minecraft.player))) {
                            changed = true;
                            sba$tsShaderStatus = 1;
                            //this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
                        }
                    }
                } else {
                    if (sba$tsShaderStatus == 1) {
                        changed = true;
                        sba$tsShaderStatus = 0;
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
    }

    @Override
    public float sba$getFov(Camera activeRenderInfo, float partialTicks, boolean useFOVSetting) {
        return (float) getFov(activeRenderInfo, partialTicks, useFOVSetting);
    }

    @Inject(method = "resize", at = @At("TAIL"))
    private void onResize(int width, int height, CallbackInfo ci)
    {
        if (TimestopShaderManager.TIMESTOP_DEPTH_BUFFER != null)
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.resize(width, height, true);
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

    @Shadow @Final private Map<String, ShaderInstance> shaders;

    @Shadow public abstract Minecraft getMinecraft();

}
