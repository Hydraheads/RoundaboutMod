package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FirstPersonJackalRifleModel<T extends Entity> extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "jackal_rifle"), "main");
    private final ModelPart JackalRifle;
    private final ModelPart structure;
    private final ModelPart barrel;
    private final ModelPart muzzle;
    private final ModelPart handle;
    private final ModelPart Root;

    public FirstPersonJackalRifleModel() {

        this.Root = createBodyLayer().bakeRoot();
        this.JackalRifle = Root.getChild("JackalRifle");
        this.structure = this.JackalRifle.getChild("structure");
        this.barrel = this.structure.getChild("barrel");
        this.muzzle = this.barrel.getChild("muzzle");
        this.handle = this.JackalRifle.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition JackalRifle = partdefinition.addOrReplaceChild("JackalRifle", CubeListBuilder.create(), PartPose.offset(0.0F, 19.4F, 5.765F));

        PartDefinition structure = JackalRifle.addOrReplaceChild("structure", CubeListBuilder.create().texOffs(2, 22).addBox(-0.5F, -1.0F, -2.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(15, 2).addBox(-0.5F, -1.5F, 2.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(0, 11).addBox(-1.0F, 0.0F, -4.75F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, 1).addBox(-1.0F, -1.0F, -4.75F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(15, 22).addBox(0.0F, 0.0F, 2.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 13).addBox(-0.5F, 0.0F, 2.25F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, -2.25F));

        PartDefinition barrel = structure.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.49F, -3.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.75F));

        PartDefinition muzzle = barrel.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(22, 5).addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.25F))
                .texOffs(25, 7).addBox(0.0F, -1.0F, 3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 10).addBox(-0.5F, -0.5F, -1.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(22, 13).addBox(-0.5F, 0.25F, -0.65F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.5F));

        PartDefinition handle = JackalRifle.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(20, 21).addBox(-1.0F, -3.95F, 3.51F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(22, 0).addBox(-1.0F, -0.95F, 0.49F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(22, 9).addBox(-1.0F, -0.45F, -0.51F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.015F)), PartPose.offset(0.0F, 0.05F, 1.47F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {

    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        JackalRifle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static ResourceLocation getTextureLocation(Entity entity){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/item/jackal_rifle.png");
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light) {
        if (context instanceof LivingEntity LE) {
            IPlayerEntity ipe = ((IPlayerEntity) LE);
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            boolean mainHandRight = true;
            if (LE instanceof Player player) {
                mainHandRight = player.getMainArm() == HumanoidArm.RIGHT;
                if (mainHandRight) {
                    this.animate(ipe.roundabout$getJackalModelAim(), Poses.JACKAL_MODEL_AIM.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getJackalModelRecoil(), Poses.JACKAL_MODEL_RECOIL.ad, partialTicks, 1f);
                } else if (!mainHandRight) {
//                    this.animate(ipe.roundabout$getJackalModelAimLeft(), Poses.JACKAL_MODEL_AIM_LEFT.ad, partialTicks, 1f);
//                    this.animate(ipe.roundabout$getJackalModelRecoilLeft(), Poses.JACKAL_MODEL_RECOIL_LEFT.ad, partialTicks, 1f);
                }
            }
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        }
    }
}