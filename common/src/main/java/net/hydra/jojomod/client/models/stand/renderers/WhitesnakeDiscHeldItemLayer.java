package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.hydra.jojomod.item.FirearmItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class WhitesnakeDiscHeldItemLayer extends StandHeldItemLayer<WhitesnakeEntity, StandModel<WhitesnakeEntity>> {
    public WhitesnakeDiscHeldItemLayer(RenderLayerParent<WhitesnakeEntity, StandModel<WhitesnakeEntity>> parent,
                                       ItemInHandRenderer itemRenderer) {
        super(parent, itemRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, WhitesnakeEntity entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        LivingEntity user = entity.getUser();
        if (user == null || !entity.getHeldItem().isEmpty()
                || (!entity.isRemoteControlled() && entity.getAnimation() != WhitesnakeEntity.ITEM_THROW)) return;
        ItemStack disc = user.getMainHandItem();
        if (!WhitesnakeControlInventory.isHeldItem(disc)) return;
        boolean firearm = disc.getItem() instanceof FirearmItem;
        poseStack.pushPose();
        renderArmWithItem(entity, disc,
                firearm ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                firearm ? HumanoidArm.RIGHT : HumanoidArm.LEFT,
                poseStack, buffer, light);
        poseStack.popPose();
    }
}
