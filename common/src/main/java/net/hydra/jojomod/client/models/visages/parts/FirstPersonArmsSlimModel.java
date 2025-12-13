package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FirstPersonArmsSlimModel<T extends Entity> extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "actualplayerarms"), "main");
    private final ModelPart transform;
    private final ModelPart rform;
    private final ModelPart right_arm;
    private final ModelPart lform;
    private final ModelPart left_arm;
    private final ModelPart Root;

    public FirstPersonArmsSlimModel() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.transform = Root.getChild("transform");
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

        PartDefinition right_arm = rform.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition lform = transform.addOrReplaceChild("lform", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -2.1226F, -1.0385F, 2.2938F));

        PartDefinition left_arm = lform.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(49, 48).addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return Root;
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
                       int light) {
        if (context instanceof LivingEntity LE) {
            IPlayerEntity ipe = ((IPlayerEntity) LE);
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            boolean mainHandRight = true;
            if (LE instanceof Player player) {
                mainHandRight = player.getMainArm() == HumanoidArm.RIGHT;
            }
            if (mainHandRight) {
                this.animate(ipe.roundabout$getSnubnoseAim(), Poses.SNUBNOSE_AIM.ad, partialTicks, 1f);
                this.animate(ipe.roundabout$getSnubnoseRecoil(), Poses.SNUBNOSE_RECOIL.ad, partialTicks, 1f);
            } else {
                this.animate(ipe.roundabout$getSnubnoseAimLeft(), Poses.SNUBNOSE_AIM_LEFT.ad, partialTicks, 1f);
                this.animate(ipe.roundabout$getSnubnoseRecoilLeft(), Poses.SNUBNOSE_RECOIL_LEFT.ad, partialTicks, 1f);
            }
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
//            Roundabout.LOGGER.info("Is Aiming:"+ipe.roundabout$getSnubnoseAim().isStarted());
//            Roundabout.LOGGER.info("is Recoiling"+ipe.roundabout$getSnubnoseRecoil().isStarted());
        }
    }
}