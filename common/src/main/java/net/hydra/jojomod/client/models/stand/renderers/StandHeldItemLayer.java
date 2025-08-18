package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class StandHeldItemLayer <T extends StandEntity, M extends StandModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public StandHeldItemLayer(RenderLayerParent<T, M> p_234846_, ItemInHandRenderer p_234847_) {
        super(p_234846_);
        this.itemInHandRenderer = p_234847_;
    }

    public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
        boolean flag = p_117207_.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack =  p_117207_.getHeldItem();
        if (!itemstack.isEmpty()) {
            p_117204_.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                p_117204_.translate(0.0F, 0.75F, 0.0F);
                p_117204_.scale(0.5F, 0.5F, 0.5F);
            }

            if (itemstack.getItem() instanceof BlockItem){
                //p_117204_.scale(3,3,3);
            }
            this.renderArmWithItem(p_117207_, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, p_117204_, p_117205_, p_117206_);
            p_117204_.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_) {
        if (!p_117186_.isEmpty()) {
            ClientUtil.pushPoseAndCooperate(p_117189_,25);
            float shiftZ = 1.3F;
            float shiftY = 1;
            float shiftX = 3F;
            if (MainUtil.isThrownBlockItem((p_117186_.getItem()))){
                if (((StandEntity)p_117185_).getUser() != null) {
                    shiftZ = 0 - Math.max(0,Math.min(((StandUser)((StandEntity) p_117185_).getUser()).roundabout$getAttackTimeDuring(),10F))*1.4F;
                    shiftY = -0.5F;
                    shiftX = 4;
                }
            } else if (p_117185_ instanceof StarPlatinumEntity SP){
                shiftZ = -1F;
            }
            this.getParentModel().translateToHand(p_117188_, p_117189_, shiftZ, shiftY, shiftX);
            if (MainUtil.isThrownBlockItem((p_117186_.getItem()))) {
                p_117189_.mulPose(Axis.XP.rotationDegrees(-90.0F));
            } else {
                p_117189_.mulPose(Axis.XP.rotationDegrees(-65.0F));
            }
            p_117189_.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean flag = p_117188_ == HumanoidArm.LEFT;
            p_117189_.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

            if (MainUtil.isThrownBlockItem(p_117186_.getItem())){
                p_117189_.scale(3,3,3);
            }
            this.itemInHandRenderer.renderItem(p_117185_, p_117186_, p_270970_, flag, p_117189_, p_117190_, p_117191_);
            ClientUtil.popPoseAndCooperate(p_117189_,25);
        }
    }
}
