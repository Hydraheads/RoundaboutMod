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
    public static ResourceLocation uno = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin.png");
    public static ResourceLocation dos = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin_2.png");
    public static ResourceLocation tres = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin_3.png");
    public static ResourceLocation cuatro = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin_4.png");
    public static ResourceLocation cinco = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin_5.png");
    public static ResourceLocation seis = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_spin_6.png");
    public ResourceLocation getTextureLocation(Entity context){
        int state = context.tickCount%12 / 2;
        if (context instanceof LivingEntity LE) {
            StandUser SU = (StandUser) LE;
            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
                if (PT.isFastSpin()) {
                    state = context.tickCount % 6;
                }
            }
        }
        return switch (state) {
            case 0 ->  uno;
            case 1-> dos;
            case 2 -> tres;
            case 3 -> cuatro;
            case 4 -> cinco;
            case 5 -> seis;
            default -> uno;
        };
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
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float alpha) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            StandUser user = ((StandUser) LE);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}