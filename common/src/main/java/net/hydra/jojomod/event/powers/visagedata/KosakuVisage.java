package net.hydra.jojomod.event.powers.visagedata;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.layers.visages.VisageRenderContext;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class KosakuVisage extends VisageData {
    public KosakuVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new KosakuVisage(entity);
    }
    /*@Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }*/
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(23, 24, 30);
    }

    @Override
    public boolean rendersKosakuHair(){
        return true;
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.937F, 0.937F, 0.937F);
    }

    public String getSkinPath(){
        return "kosaku";
    }

    @Override
    public void render(VisageRenderContext renderContext, HumanoidModel<LivingEntity> model, PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, String path, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                       float r, float g, float b) {
        if (!renderContext.isBodyFrozen && !renderContext.hideExtraPartsWithSuit && !renderContext.isHoldingBowlerHat) {
            renderKosakuHair(model, poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                    r, g, b);
        }
    }

    public void renderKosakuHair(HumanoidModel<LivingEntity> model, PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        model.head.translateAndRotate(poseStack);
        ModStrayModels.kosakuHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }

}
