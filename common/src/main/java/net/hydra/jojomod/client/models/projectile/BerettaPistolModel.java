package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
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
import net.minecraft.world.entity.LivingEntity;

public class BerettaPistolModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "beretta_compact_pistol_converted"), "main");
    private final ModelPart Root;
    private final ModelPart Beretta;

    public BerettaPistolModel() {
        this.Root = createBodyLayer().bakeRoot();
        this.Beretta = this.Root.getChild("Beretta");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Beretta = partdefinition.addOrReplaceChild("Beretta", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -5.775F, -4.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.025F))
                .texOffs(10, 16).addBox(-2.0F, -4.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.0F, -5.75F, -4.75F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.001F))
                .texOffs(16, 16).addBox(-1.5F, -4.0F, -3.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 9).addBox(-2.0F, -5.0F, -4.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 25.75F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Beretta.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/item/beretta_pistol.png");
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(getTextureLocation()));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
            //r = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }
}