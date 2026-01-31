package net.hydra.jojomod.client.models.layers.anubis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.access.AccessFoxModel;
import net.hydra.jojomod.mixin.access.AccessWolfModel;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import org.joml.Quaternionf;

public class AnubisWolfLayer<T extends Wolf, M extends WolfModel<T>> extends RenderLayer {
    public AnubisWolfLayer(RenderLayerParent $$0) {super($$0);}

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Entity entity, float v, float v1, float v2, float partialTicks, float v4, float v5) {

        if ( entity instanceof Wolf W ) {
            StandUser SU = (StandUser) W;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis && SU.roundabout$hasAStand() && PowerTypes.hasStandActive(entity) ) {
                ClientUtil.pushPoseAndCooperate(poseStack,50);

                ((AccessWolfModel)this.getParentModel()).roundabout$getHead().translateAndRotate(poseStack);

                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,90),0,0,0);
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,-90),0,0,0);

                poseStack.translate(0.55,0,-0.06);

                ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                        1, 1, 1, 1.0F, SU.roundabout$getStandSkin());

                ClientUtil.popPoseAndCooperate(poseStack,50);
            }
        }
    }
}
