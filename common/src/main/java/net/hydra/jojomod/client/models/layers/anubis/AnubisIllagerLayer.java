package net.hydra.jojomod.client.models.layers.anubis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.access.AccessIllagerModel;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.*;
import org.joml.Quaternionf;

public class AnubisIllagerLayer<T extends AbstractIllager, M extends IllagerModel<T>> extends RenderLayer {


    public AnubisIllagerLayer(EntityRendererProvider.Context context, IllagerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Entity entity, float v, float v1, float v2, float partialTicks, float v4, float v5) {

        if ( entity instanceof AbstractIllager AI ) {
            if (AI.getArmPose() == AbstractIllager.IllagerArmPose.ATTACKING) {
                StandUser SU = (StandUser) AI;
                if (SU.roundabout$getStandPowers() instanceof PowersAnubis && SU.roundabout$hasAStand()) {
                    ((AccessIllagerModel) this.getParentModel()).roundabout$getRightArm().translateAndRotate(poseStack);

                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,-90),0,0,0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,90),0,0,0);
                    poseStack.translate(0.8,0.5,0.1);

                    byte skin = PowersAnubis.ANIME;
                    if (AI instanceof Illusioner || AI instanceof Evoker) {
                        skin = PowersAnubis.ILLUSORY;
                    } else if (AI instanceof Pillager || AI instanceof Vindicator) {
                        skin = PowersAnubis.CLEAVER;
                    }

                    ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                            1, 1, 1, 1.0F, skin);
                }
            }
        }

    }
}
