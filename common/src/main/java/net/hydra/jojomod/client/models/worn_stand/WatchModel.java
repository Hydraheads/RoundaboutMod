package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.MandomAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.hydra.jojomod.stand.powers.PowersMandom;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class WatchModel extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart watch;
    private final ModelPart Root;

    public WatchModel() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.watch = Root.getChild("watch");
    }


    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition watch = partdefinition.addOrReplaceChild("watch", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition scaleDown = watch.addOrReplaceChild("scaleDown", CubeListBuilder.create().texOffs(0, 0).addBox(-5.175F, -0.9F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-6.025F, -2.425F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offset(1.5F, 5.5F, 0.0F));

        PartDefinition hand = scaleDown.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(26, 0).addBox(-0.375F, -1.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offset(-5.525F, 0.075F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        watch.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }
    public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/watches/base.png");
    public static ResourceLocation rolex = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/watches/rolex.png");
    public ResourceLocation getTextureLocation(byte skin){
        switch (skin)
        {
            case PowersMandom.ROLEX -> {return rolex;}
            default -> {return base;}
        }
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation((byte)0)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof Player PL) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            StandUser user = ((StandUser) PL);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(skin)));
            user.roundabout$getHeyYaAnimation2().startIfStopped(context.tickCount);
            this.animate(user.roundabout$getHeyYaAnimation2(), MandomAnimations.tick, partialTicks, 1f);
            //this.animate(user.roundabout$getHeyYaAnimation(), HeyYaAnimations.idle_normal, partialTicks, 1f);
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

