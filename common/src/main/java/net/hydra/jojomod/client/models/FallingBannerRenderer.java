package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.IceTwisterModel;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.objects.FallingBannerEntity;
import net.hydra.jojomod.entity.objects.IceTwisterEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FallingBannerRenderer extends EntityRenderer<FallingBannerEntity> {

    public FallingBannerRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }


    public void render(FallingBannerEntity entity, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {

        if (((TimeStop) entity.level()).inTimeStopRange(entity)) {
            $$2 = 0;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            if (ClientUtil.getThrowFadePercent(entity, $$2) <= 0) {
                return;
            }
            boolean $$18 = !entity.isInvisible();
            boolean $$19 = !$$18 && !entity.isInvisibleTo(minecraft.player);
            boolean $$20 = minecraft.shouldEntityAppearGlowing(entity);
            boolean $$21 = this.getRenderT($$18, $$19, $$20);
            if ($$21) {
                renderBannerFlag($$3,$$4,$$5,entity,$$2,entity.getBanner());
            }
        }

    }
    protected boolean getRenderT(boolean $$1, boolean $$2, boolean $$3) {
        if ($$2 || $$1) {
            return true;
        } else {
            return $$3 ? true : false;
        }
    }
    public void renderBannerFlag(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FallingBannerEntity entity, float partialTicks,
                                 ItemStack stack) {
        ClientUtil.pushPoseAndCooperate(poseStack,46);
        // Move the banner attachment point upward

        // Move it forward relative to the player's facing direction
        poseStack.translate(0.0D, -0.2D, 0.0D);
        poseStack.translate(0.0D, 0.0D, -1.3D);


        // Tilt it forward
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        float scale = 1;

        poseStack.translate(0.0D, 0.75D, 0.2D);
        ModStrayModels.bannerFlag.roundabout$renderInSpot(stack,
                entity.position(),entity.level(),partialTicks, poseStack, bufferSource, packedLight,OverlayTexture.NO_OVERLAY,scale);
        ClientUtil.popPoseAndCooperate(poseStack,46);
    }

    @Override
    public ResourceLocation getTextureLocation(FallingBannerEntity var1) {
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/stand/white_album/projectiles/ice_twister.png");
    }

}
