package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class MoldLeftArmLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;

    public MoldLeftArmLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        if (entity != null && entity instanceof Player) {
            if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                return;
            StandUser user = ((StandUser) entity);
            boolean MoldedRightArm = false;
            boolean HasGreenDay = (user.roundabout$getStandPowers() instanceof PowersGreenDay);
            if (HasGreenDay){
                MoldedRightArm = !((PowersGreenDay)user.roundabout$getStandPowers()).HasOffHand  & (entity.getMainArm() == HumanoidArm.RIGHT);
                if(!MoldedRightArm) {
                    MoldedRightArm = !((PowersGreenDay) user.roundabout$getStandPowers()).HasMainArm & (entity.getMainArm() == HumanoidArm.LEFT);
                }
            }
            if (!entity.isInvisible()) {
                if (MoldedRightArm) {
                    Roundabout.LOGGER.info("rendering");

                    poseStack.pushPose();
                    getParentModel().body.translateAndRotate(poseStack);
                    ModStrayModels.MoldLeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                                10, 10, 10, 1F);
                    poseStack.popPose();
                }
            }
        }
    }
}

