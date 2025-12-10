package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FirstPersonArmsLayer<T extends Entity> extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "actualplayerarms"), "main");
    private final ModelPart transform;
    private final ModelPart rform;
    private final ModelPart right_arm;
    private final ModelPart lform;
    private final ModelPart left_arm;
    private final ModelPart root;

    public FirstPersonArmsLayer() {
        this.root = createBodyLayer().bakeRoot();
        this.transform = root.getChild("transform");
        this.rform = this.transform.getChild("rform");
        this.right_arm = this.rform.getChild("right_arm");
        this.lform = this.transform.getChild("lform");
        this.left_arm = this.lform.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition transform = partdefinition.addOrReplaceChild("transform", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -1.6144F, 0.0F, 0.0F));

        PartDefinition rform = transform.addOrReplaceChild("rform", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, -2.1226F, 1.0385F, -2.2938F));

        PartDefinition right_arm = rform.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition lform = transform.addOrReplaceChild("lform", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -2.1226F, -1.0385F, 2.2938F));

        PartDefinition left_arm = lform.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        transform.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static Player player;

    public static ResourceLocation getTextureLocation(Entity entity) {
        if (entity instanceof Player player) {
            return ((AbstractClientPlayer) player).getSkinTextureLocation();
        }
        return DefaultPlayerSkin.getDefaultSkin();
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //r = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }
}