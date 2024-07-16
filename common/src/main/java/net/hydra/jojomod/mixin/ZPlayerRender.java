package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.client.LocacacaBeamLayer;
import net.hydra.jojomod.entity.client.StoneLayer;
import net.hydra.jojomod.entity.projectile.KnifeLayer;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class ZPlayerRender extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public ZPlayerRender(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method="<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At(value = "RETURN"))
    private void roundaboutRenderKnives(EntityRendererProvider.Context $$0, boolean $$1, CallbackInfo ci) {
        this.addLayer(new KnifeLayer<>($$0, this));
        this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>($$0, this));
    }

    private static AbstractClientPlayer ACP;
    private static InteractionHand IH;


    /**Stone Arms with locacaca first person*/
    @Inject(method = "renderRightHand", at = @At(value = "TAIL"))
    public void renderRightHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        byte curse = ((StandUser) $$3).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.MAIN_HAND) {
            this.model.rightSleeve.xScale += 0.04F;
            this.model.rightSleeve.zScale += 0.04F;
            this.model.rightSleeve.render($$0, $$1.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_RIGHT_ARM)), $$2, OverlayTexture.NO_OVERLAY);
            this.model.rightSleeve.xScale -= 0.04F;
            this.model.rightSleeve.zScale -= 0.04F;
        }
    }

    @Inject(method = "renderLeftHand", at = @At(value = "TAIL"))
    public void renderLeftHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        byte curse = ((StandUser) $$3).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.OFF_HAND) {
            this.model.leftSleeve.xScale += 0.04F;
            this.model.leftSleeve.zScale += 0.04F;
            this.model.leftSleeve.render($$0, $$1.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_LEFT_ARM)), $$2, OverlayTexture.NO_OVERLAY);
            this.model.leftSleeve.xScale -= 0.04F;
            this.model.leftSleeve.zScale -= 0.04F;
        }
    }


    @Shadow
    private void setModelProperties(AbstractClientPlayer $$0) {
    }


    @Inject(method = "getArmPose", at = @At(value = "HEAD"))
    private static void roundaboutGetArmPose(AbstractClientPlayer $$0, InteractionHand $$1, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
        ACP = $$0;
        IH = $$1;
    }
    @ModifyVariable(method = "getArmPose", at = @At(value = "STORE"),ordinal = 0)
    private static ItemStack roundaboutGetArmPose2(ItemStack $$0) {
        if (IH == InteractionHand.MAIN_HAND && ((IEntityAndData)ACP).getRoundaboutRenderMainHand() != null){
            $$0 = ((IEntityAndData)ACP).getRoundaboutRenderMainHand();
        } if (IH == InteractionHand.OFF_HAND && ((IEntityAndData)ACP).getRoundaboutRenderOffHand() != null){
            $$0 = ((IEntityAndData)ACP).getRoundaboutRenderOffHand();
        }
        return $$0;
    }

    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer var1) {
        return null;
    }
}
