package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class SilverChariotItemLayer<T extends SilverChariotEntity, M extends StandModel<T>> extends RenderLayer<T, M> {
    private ItemStack itemStack;

    public SilverChariotItemLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            T silverChariot,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        LivingEntity user = silverChariot.getUser();
        if (user == null
                || silverChariot.getSilverChariotOffhandItem().isEmpty()
                || silverChariot.getSilverChariotOffhandItem() == null
                || !(((StandUser) user).roundabout$getStandPowers() instanceof PowersSilverChariot PSC
                && (PSC.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE || PSC.getActivePower() == PowerIndex.SNEAK_ATTACK))
        ) {
            return;
        }
        poseStack.pushPose();
        M model = this.getParentModel();
        model.root().translateAndRotate(poseStack);
        model.root().getChild("stand2").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").getChild("body2").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").getChild("body2")
                .getChild("torso").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest")
                .getChild("left_arm").translateAndRotate(poseStack);
        model.root().getChild("stand2")
                .getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest")
                .getChild("left_arm").getChild("lower_left_arm").translateAndRotate(poseStack);
        // model.body.translateAndRotate(poseStack);
        // model.leftHand.translateAndRotate(poseStack);
        // poseStack.translate(0.0D, 0.5D, 0.0D);
        // poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F / 16F, 2.0F / 16F, -8.0F / 16F);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                user.getMainHandItem(),
                ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                multiBufferSource,
                silverChariot.level(),
                silverChariot.getId()
        );
        poseStack.popPose();
    }
}
