package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.LayerAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public class TuskDrillModel extends PsuedoHierarchicalModel {
    public static ResourceLocation[] drills = new ResourceLocation[12];
    public ResourceLocation getTextureLocation(Entity context){
        if (drills[0] == null) {
            for(int i=0;i<drills.length-1;i++) {
                drills[i] = new ResourceLocation(Roundabout.MOD_ID,
                        "textures/stand/tusk/projectiles/drill_spin_" + (i+1) + ".png");
            }
        }
        int state = context.tickCount%11;
        if (context instanceof LivingEntity LE) {
            StandUser SU = (StandUser) LE;
            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
                if (PT.isFastSpin()) {
               //     state = context.tickCount % 6;
                }
            }
        }
        return drills[state];
    }

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "tusk_drill_spin"), "main");
    private final ModelPart armAddon;
    private final ModelPart Root;


    public TuskDrillModel() {
        super(RenderType::entityTranslucent);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        this.Root = LayerDefinition.create(meshdefinition, 32, 32).bakeRoot();
        this.armAddon = Root.getChild("bb_main");
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        armAddon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {}

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, float alpha) {
        if (context instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers() instanceof PowersTusk PT) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            StandUser user = ((StandUser) LE);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //The number at the end is inversely proportional so 2 is half speed
            float r = PT.getNailColor().x;
            float g = PT.getNailColor().y;
            float b = PT.getNailColor().z;
            root().render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}