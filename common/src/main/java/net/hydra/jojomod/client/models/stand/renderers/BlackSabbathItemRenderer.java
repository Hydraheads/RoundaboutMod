package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.models.stand.BlackSabbathModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.item.RoundaboutArrowItem;
import net.hydra.jojomod.item.StandArrowItem;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.*;

public class BlackSabbathItemRenderer <T extends BlackSabbathEntity, M extends StandModel<T>> extends RenderLayer<T, M> {

    private final ItemInHandRenderer itemInHandRenderer;

     public BlackSabbathItemRenderer(RenderLayerParent<T, M> p_234838_, ItemInHandRenderer p_234839_) {
            super(p_234838_);
            this.itemInHandRenderer = p_234839_;
     }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, BlackSabbathEntity blackSabbathEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        boolean flag1 = this.getParentModel().young;
        ItemStack it = blackSabbathEntity.getHeldItemSabbath();
         if(!it.isEmpty() && !(it.getItem() instanceof BlockItem && !(it.is(Items.HOPPER))) && blackSabbathEntity.getGrabbing() != 0){
             boolean flag2 = it.getItem() instanceof RoundaboutArrowItem || it.getItem() instanceof SwordItem || it.getItem() instanceof GlaiveItem || it.getItem() instanceof HoeItem || it.getItem() instanceof PickaxeItem || it.getItem() instanceof AxeItem || it.getItem() instanceof ShovelItem || it.getItem() instanceof ShearsItem || it.getItem() instanceof BrushItem;
             boolean flag3 = it.getItem() instanceof PotionItem || it.getItem() instanceof ArmorItem || it.getItem() instanceof BucketItem || it.is(Items.HOPPER);
            poseStack.pushPose();
            if(!flag1 && !flag2) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            } else {
                poseStack.scale(0.75F, 0.75F, 0.75F);
            }
             if (flag1) {
                 float f = 0.75F;
                 poseStack.scale(0.34F, 0.34F, 0.34F);
             }
             if(flag2) {
                 poseStack.translate(((this.getParentModel()).head.x / 16.0F) - 0.1F, ((this.getParentModel()).head.y / 19.0F) + 1.12F, ((this.getParentModel()).head.z / 16.0F) - 0.3F);
             } else if (flag3){
                 poseStack.translate(((this.getParentModel()).head.x / 16.0F), ((this.getParentModel()).head.y / 19.0F) + 1.12F, ((this.getParentModel()).head.z / 16.0F) - 0.3F);
             }else {
                 poseStack.translate(((this.getParentModel()).head.x / 16.0F) - 0F, ((this.getParentModel()).head.y / 19.0F) + 1.15F, ((this.getParentModel()).head.z / 16.0F) - 0.55F);
             }
                 poseStack.mulPose(Axis.YP.rotationDegrees(v4));
                 poseStack.mulPose(Axis.XP.rotationDegrees(v5));
                 poseStack.mulPose(Axis.XP.rotationDegrees(102.0F));
                 if (flag2) {
                     poseStack.mulPose(Axis.ZP.rotation(179.85F));
                 } else if (flag3) {
                     poseStack.mulPose(Axis.ZP.rotation(110F));
                 }

             this.itemInHandRenderer.renderItem(blackSabbathEntity, it, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i);

            poseStack.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int p_117191_) {

    }
}
