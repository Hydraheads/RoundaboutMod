package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.model.HierarchicalModel;
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

public class FirstPersonColtRevolverModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "colt_revolver"), "main");
    private final ModelPart ColtSAA;
    private final ModelPart Root;

    public FirstPersonColtRevolverModel() {

        this.Root = createBodyLayer().bakeRoot();
        this.ColtSAA = Root.getChild("ColtSAA");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ColtSAA = partdefinition.addOrReplaceChild("ColtSAA", CubeListBuilder.create().texOffs(10, 6).addBox(-0.5F, -4.875F, -4.725F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.001F))
                .texOffs(10, 12).addBox(0.0F, -2.375F, -3.725F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(7, 16).addBox(0.0F, -4.875F, -0.725F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-0.5F, -4.625F, -8.725F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 15).addBox(0.0F, -5.125F, -8.225F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 12).addBox(-1.0F, -4.25F, -1.725F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -4.9F, -4.375F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.35F)), PartPose.offset(0.0F, 23.75F, 0.0F));

        PartDefinition handle_r1 = ColtSAA.addOrReplaceChild("handle_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -1.2F, -0.5F, 0.4363F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ColtSAA.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public ResourceLocation getTextureLocation(Entity context){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/item/colt_revolver.png");
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(context)));
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
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.COLT_MODEL_AIM.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.COLT_MODEL_RECOIL.ad, partialTicks, 1f);
                } else if (!mainHandRight) {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.COLT_MODEL_AIM_LEFT.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.COLT_MODEL_RECOIL_LEFT.ad, partialTicks, 1f);
                }
            }
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        }
    }
}