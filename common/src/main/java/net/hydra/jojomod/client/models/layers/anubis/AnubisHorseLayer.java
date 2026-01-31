package net.hydra.jojomod.client.models.layers.anubis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.access.AccessHorseModel;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.*;
import org.joml.Quaternionf;

public class AnubisHorseLayer<T extends AbstractHorse, M extends HorseModel<T>> extends RenderLayer {
    public AnubisHorseLayer(RenderLayerParent $$0) {super($$0);}

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Entity entity, float v, float v1, float v2, float partialTicks, float v4, float v5) {

        if ( entity instanceof AbstractHorse AH ) {
            StandUser SU = (StandUser) AH;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis && SU.roundabout$hasAStand()) {
                ClientUtil.pushPoseAndCooperate(poseStack,50);

                ((AccessHorseModel)this.getParentModel()).roundabout$getBody().translateAndRotate(poseStack);

                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,90),0,0,0);
                poseStack.translate(0.6,0,-0.3);
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,55),0,0,0);

                poseStack.scale(0.8F,0.8F,0.8F);

                ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                        1, 1, 1, 1.0F, SU.roundabout$getStandSkin());

                ClientUtil.popPoseAndCooperate(poseStack,50);
            }
        }
    }
}
