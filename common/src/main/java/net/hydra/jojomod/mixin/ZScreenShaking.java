package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGameRenderer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public class ZScreenShaking implements IGameRenderer {

    @Shadow
    @Final
    Minecraft minecraft;


    @Shadow
    void loadEffect(ResourceLocation $$0){

    }
    @Shadow
    public void renderItemInHand(PoseStack $$0, Camera $$1, float $$2) {}


    @Override
    public void roundabout$loadEffect(ResourceLocation $$0){
        this.loadEffect($$0);
    }

    @Inject(method = "shouldRenderBlockOutline()Z", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$shouldOutline(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser sus = ((StandUser) player);
            StandPowers powers = sus.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                cir.setReturnValue(false);
            }
        }
    }


    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$tick(CallbackInfo ci) {
        if (ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
            boolean changed = false;
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                if (roundabout$tsShaderStatus == 0) {
                    ClientConfig clientConfig = ConfigManager.getClientConfig();
                    if (!(clientConfig != null && clientConfig.timeStopSettings != null && ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen && !((TimeStop) minecraft.player.level()).isTimeStoppingEntity(minecraft.player))) {
                        changed = true;
                        roundabout$tsShaderStatus = 1;
                        this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
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


    @Inject(method = "checkEntityPostEffect(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$checkEntityPostEffect(Entity $$0, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta

        if (ConfigManager.getClientConfig().timeStopSettings.simpleTimeStopShader) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).inTimeStopRange(minecraft.player)) {
                this.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
            } else {
                this.postEffect = null;
            }
        }
    }
    @Inject(method = "tickFov()V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tickfov(CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            StandPowers SP = ((StandUser)player).roundabout$getStandPowers();
            if (SP.scopeLevel > 0) {
                ci.cancel();

                float f = 1.0F;
                this.oldFov = this.fov;

                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.minecraft.getCameraEntity();
                if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson() && abstractclientplayer.isScoping()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.05F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.0225F;
                    } else {
                        f = 0.01F;
                    }
                } else if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.3F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.125F;
                    } else {
                        f = 0.05F;
                    }
                }

                this.fov += (f - this.fov) * 0.5F;
                if (this.fov > 1.5F) {
                    this.fov = 1.5F;
                }

                if (this.fov < 0.01F) {
                    this.fov = 0.01F;
                }
            }
        }
    }


    @Inject(method = "renderItemInHand", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$RenderHandsWithItems(PoseStack $$0, Camera $$1, float $$2, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() != null) {
                    Entity Ent = this.minecraft.getCameraEntity();
                    $$2 = ((IEntityAndData) Ent).roundabout$getPreTSTick();
                    cleared = true;
                    this.renderItemInHand($$0, $$1, $$2);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }

    @Unique
    public int roundabout$tsShaderStatus = 0;

    public boolean cleared = false;
    /**Minor code to prevent nauseating barrage shaking effect when getting barraged.*/

    /**The amount of screen shakes has to be negligable for a 20 hits per sec move*/
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tiltViewWhenHurt(PoseStack $$0, float $$1, CallbackInfo ci) {
        //$$0 is matrcices, $$1 is tickdelta
            if (minecraft.player != null && (((StandUser) minecraft.player).roundabout$isDazed() ||
                    (((IPlayerEntity) minecraft.player).roundabout$getCameraHits() > -1))) {
                assert minecraft.level != null;
                if (minecraft.level.getGameTime() % 5 != 0) {
                    LivingEntity livingEntity = (LivingEntity) minecraft.getCameraEntity();
                    if (livingEntity != null) {
                        float f = (float) livingEntity.hurtTime - $$1;
                        if (f < 0.0f) {
                            return;
                        }
                        f /= ((float) livingEntity.hurtDuration * 2);
                        f = Mth.sin(f * f * f * f * (float) Math.PI);
                        float g = livingEntity.getHurtDir();
                        $$0.mulPose(Axis.YP.rotationDegrees(-g));
                        float h = (float) ((double) (-f) * 14.0 * minecraft.options.damageTiltStrength().get());
                        $$0.mulPose(Axis.ZP.rotationDegrees(h));
                        $$0.mulPose(Axis.YP.rotationDegrees(g));
                    }
                }
                ci.cancel();
            }

    }


    @Shadow
    private void bobHurt(PoseStack $$0, float $$1){}
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tiltViewWhenHurt2(PoseStack $$0, float $$1, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).roundabout$getPreTSTick();
                    cleared = true;
                    this.bobHurt($$0, $$1);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }

    @Shadow
    private void bobView(PoseStack $$0, float $$1){}
    @Shadow
    @Final
    private RenderBuffers renderBuffers;
    @Shadow private float fov;
    @Shadow private float oldFov;

    @Shadow @Nullable private PostChain postEffect;

    @Inject(method = "renderLevel", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderLevel(float $$0, long $$1, PoseStack $$2, CallbackInfo ci) {
        /*
        if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player != null){
            $$2.pushPose();
            ModFirstPersonLayers.render($$2,this.renderBuffers.bufferSource(),this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, $$0),this.minecraft.player,$$0);
            $$2.popPose();
        }
         */
    }


    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutBobView(PoseStack $$0, float $$1, CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandPowers SP = ((StandUser) player).roundabout$getStandPowers();
            if (SP.scopeLevel > 0) {
                ci.cancel();
            }
        }

        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() instanceof Player) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).roundabout$getPreTSTick();
                    cleared = true;
                    this.bobView($$0, $$1);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }
}
