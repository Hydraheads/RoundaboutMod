package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IHumanoidModelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.visages.mobs.JosukePartEightNPC;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class StoneLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    /**Code for humanoid models turned to stone*/
    private final EntityRenderDispatcher dispatcher;

    private final LivingEntityRenderer<T, M> livingEntityRenderer;

    private M transformedModel;

    public StoneLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
        this.livingEntityRenderer = livingEntityRenderer;
        this.transformedModel = livingEntityRenderer.getModel();
    }


    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, T var4,
                       float var5, float var6, float var7, float var8, float var9, float var10) {
        if (((IEntityAndData)var4).roundabout$getTrueInvisibility() > - 1)
            return;

        transformedModel = livingEntityRenderer.getModel();
        byte curse = ((StandUser)var4).roundabout$getLocacacaCurse();
        if (curse > -1) {
            ResourceLocation rl = null;
            if (curse == LocacacaCurseIndex.LEFT_LEG){
                rl = StandIcons.STONE_LEFT_LEG;
            } else if (curse == LocacacaCurseIndex.RIGHT_LEG){
                rl = StandIcons.STONE_RIGHT_LEG;
            } else if (curse == LocacacaCurseIndex.LEFT_HAND){
                rl = StandIcons.STONE_LEFT_ARM;
            } else if (curse == LocacacaCurseIndex.RIGHT_HAND){
                rl = StandIcons.STONE_RIGHT_ARM;
            } else if (curse == LocacacaCurseIndex.CHEST){
                rl = StandIcons.STONE_CHEST;
            } else if (curse == LocacacaCurseIndex.HEAD){
                if (var4 instanceof Player PL){
                    ItemStack stack =((IPlayerEntity)PL).roundabout$getMaskSlot();
                    if (stack !=null && stack.is(ModItems.JOSUKE_PART_EIGHT_MASK)){
                        rl = StandIcons.STONE_HEAD_JOSUKE;
                    } else {
                        rl = StandIcons.STONE_HEAD;
                    }
                } else if (var4 instanceof JosukePartEightNPC){
                    rl = StandIcons.STONE_HEAD_JOSUKE;
                } else {
                    rl = StandIcons.STONE_HEAD;
                }
            } else if (curse == LocacacaCurseIndex.HEART){
                rl = StandIcons.STONE_HEART;
            }
            renderPart(poseStack,multiBufferSource,integ,this.transformedModel,1,1,1,null,
                    rl);
        }
    }

    private void renderPart(
           PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, HumanoidModel<T> $$4,float $$6, float $$7, float $$8, @Nullable String $$9,
           ResourceLocation RL
    ) {
        if (RL != null) {
            VertexConsumer $$10 = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(RL));

            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.xScale += 0.04F);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.zScale += 0.04F);
            $$4.renderToBuffer(poseStack, $$10, integ, OverlayTexture.NO_OVERLAY, $$6, $$7, $$8, 1.0F);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.xScale -= 0.04F);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.zScale -= 0.04F);
        }
    }
}
