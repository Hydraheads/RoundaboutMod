package net.hydra.jojomod.client.models.layers.anubis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.mobs.AnubisGuardianModel;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.access.models.*;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.joml.Quaternionf;

public class AnubisMobLayer<T extends LivingEntity, M extends HierarchicalModel<T>> extends RenderLayer {


    public AnubisMobLayer(MobRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Entity entity, float v, float v1, float v2, float partialTicks, float v4, float v5) {

        StandUser SU = (StandUser) entity;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis) {
            if (entity instanceof AbstractIllager AI) {
                renderIllager(poseStack,bufferSource,packedLight,AI,v,v1,v2,partialTicks,v4,v5);
            } else if (entity instanceof Wolf W) {
                renderWolf(poseStack,bufferSource,packedLight,W,v,v1,v2,partialTicks,v4,v5);
            } else if (entity instanceof Fox F) {
                renderFox(poseStack,bufferSource,packedLight,F,v,v1,v2,partialTicks,v4,v5);
            } else if (entity instanceof Horse H) {
                renderHorse(poseStack,bufferSource,packedLight,H,v,v1,v2,partialTicks,v4,v5);
            } else if (entity instanceof AbstractVillager AV) {
                renderAbstractVillager(poseStack,bufferSource,packedLight,AV,v,v1,v2,partialTicks,v4,v5);
            } else if (entity instanceof LivingEntity LE && getParentModel() instanceof QuadrupedModel<?>) {
                renderQuadModel(poseStack,bufferSource,packedLight,LE,v,v1,v2,partialTicks,v4,v5);
            }

        }
    }

    private void renderIllager(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractIllager AI, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        if (AI.getArmPose() == AbstractIllager.IllagerArmPose.ATTACKING || (PowerTypes.isUsingStand(AI)) ) {
            if (AI instanceof AnubisGuardian AG) {
                if (!AG.hasTotem() && AG.getArmPose().equals(AbstractIllager.IllagerArmPose.ATTACKING)){
                    ((AnubisGuardianModel) this.getParentModel()).getArm(HumanoidArm.RIGHT).translateAndRotate(poseStack);
                } else {
                    return;
                }
            } else {
                ((AccessIllagerModel) this.getParentModel()).roundabout$getRightArm().translateAndRotate(poseStack);
            }

            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, -90), 0, 0, 0);
            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 90), 0, 0, 0);
            poseStack.translate(0.8, 0.5, 0.1);

            byte skin = PowersAnubis.ANIME;
            if (AI instanceof Illusioner || AI instanceof Evoker) {
                skin = PowersAnubis.ILLUSORY;
            } else if (AI instanceof Pillager || AI instanceof Vindicator) {
                skin = PowersAnubis.CLEAVER;
            }

            ModStrayModels.ANUBIS.render(AI, partialTicks, poseStack, bufferSource, packedLight,
                    1, 1, 1, 1.0F, skin);
        }
    }

    private void renderWolf(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Wolf W, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        StandUser SU = (StandUser) W;
        ClientUtil.pushPoseAndCooperate(poseStack, 50);

        ((AccessWolfModel)this.getParentModel()).roundabout$getHead().translateAndRotate(poseStack);

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,90),0,0,0);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,-90),0,0,0);

        poseStack.translate(0.55,0,-0.06);

        boolean hasHeyYaOut = (PowerTypes.hasStandActive(W) && SU.roundabout$getStandPowers() instanceof PowersAnubis);
        int heyTicks = SU.roundabout$getAnubisVanishTicks();
        float heyFull = 0;
        float fixedPartial = partialTicks - (int) partialTicks;
        if (((TimeStop)W.level()).CanTimeStopEntity(W)){
            fixedPartial = 0;
        }
        if (hasHeyYaOut){
            heyFull = heyTicks+fixedPartial;
            heyFull = Math.min(heyFull/10,1f);
        } else {
            heyFull = heyTicks-fixedPartial;
            heyFull = Math.max(heyFull/10,0);
        }


        ModStrayModels.ANUBIS.render(W, partialTicks, poseStack, bufferSource, packedLight,
                1, 1, 1, heyFull, SU.roundabout$getStandSkin());

        ClientUtil.popPoseAndCooperate(poseStack,50);
    }

    private void renderFox(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Fox F, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        StandUser SU = (StandUser) F;

        ClientUtil.pushPoseAndCooperate(poseStack, 50);

        ((AccessFoxModel)this.getParentModel()).roundabout$getHead().translateAndRotate(poseStack);

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,90),0,0,0);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,-90),0,0,0);

        poseStack.translate(0.75,0,-0.1);


        boolean hasHeyYaOut = (PowerTypes.hasStandActive(F) && SU.roundabout$getStandPowers() instanceof PowersAnubis);
        int heyTicks = SU.roundabout$getAnubisVanishTicks();
        float heyFull = 0;
        float fixedPartial = partialTicks - (int) partialTicks;
        if (((TimeStop)F.level()).CanTimeStopEntity(F)){
            fixedPartial = 0;
        }
        if (hasHeyYaOut){
            heyFull = heyTicks+fixedPartial;
            heyFull = Math.min(heyFull/10,1f);
        } else {
            heyFull = heyTicks-fixedPartial;
            heyFull = Math.max(heyFull/10,0);
        }

        ModStrayModels.ANUBIS.render(F, partialTicks, poseStack, bufferSource, packedLight,
                1, 1, 1, heyFull, SU.roundabout$getStandSkin());

        ClientUtil.popPoseAndCooperate(poseStack,50);
    }

    private void renderHorse(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Horse H, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        StandUser SU = (StandUser) H;
        ClientUtil.pushPoseAndCooperate(poseStack, 50);

        ((AccessHorseModel) this.getParentModel()).roundabout$getBody().translateAndRotate(poseStack);

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, 90), 0, 0, 0);
        poseStack.translate(0.6, 0, -0.3);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 55), 0, 0, 0);

        poseStack.scale(0.8F, 0.8F, 0.8F);

        ModStrayModels.ANUBIS.render(H, partialTicks, poseStack, bufferSource, packedLight,
                1, 1, 1, 1.0F, SU.roundabout$getStandSkin());

        ClientUtil.popPoseAndCooperate(poseStack, 50);

    }

    private void renderAbstractVillager(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractVillager AV, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        StandUser SU = (StandUser) AV;
        ClientUtil.pushPoseAndCooperate(poseStack, 50);

        ((AccessVillagerModel) this.getParentModel()).roundabout$getRoot().translateAndRotate(poseStack);

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,90),0,0,0);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,60),0,0,0);

        poseStack.translate(0.8,0.35,0.35);

        poseStack.scale(0.8F, 0.8F, 0.8F);

        ModStrayModels.ANUBIS.render(AV, partialTicks, poseStack, bufferSource, packedLight,
                1, 1, 1, 1.0F, SU.roundabout$getStandSkin());

        ClientUtil.popPoseAndCooperate(poseStack, 50);

    }

    private void renderQuadModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float v, float v1, float v2, float partialTicks, float v4, float v5) {
        StandUser SU = (StandUser) entity;
        ClientUtil.pushPoseAndCooperate(poseStack, 50);

        ((AccessQuadrupedModel) this.getParentModel()).roundabout$getBody().translateAndRotate(poseStack);

        poseStack.scale(0.8F,0.8F,0.8F);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,60),0,0,0);
        poseStack.translate(0.6,-0.35,0);

        ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                1, 1, 1, 1.0F, SU.roundabout$getStandSkin());

        ClientUtil.popPoseAndCooperate(poseStack, 50);

    }

}
