package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.TuskAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class TuskDrillNailModel extends PsuedoHierarchicalModel {
    public static ResourceLocation buh = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/drill_nail.png");
    public ResourceLocation getTextureLocation(Entity context){
        return buh;
    }

    private final ModelPart Root;
    private final ModelPart nail1;
    private final ModelPart nail2;

    public TuskDrillNailModel() {
        super(RenderType::entityTranslucent);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition nail1 = partdefinition.addOrReplaceChild("nail1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition nail2 = partdefinition.addOrReplaceChild("nail2", CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        this.Root = LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();

        this.nail1 = Root.getChild("nail1");
        this.nail2 = Root.getChild("nail2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        return LayerDefinition.create(meshdefinition, 2, 4);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Root.render(poseStack,vertexConsumer,packedLight,packedOverlay,red,green,blue,alpha);
      //  nail1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
     //   nail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int i) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            StandUser user = ((StandUser) LE);
            user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount+i*3);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));

            nail1.yRot = partialTicks * 100;
            nail2.yRot = (partialTicks-50) * -100;

            poseStack.scale(0.5F,0.5F,0.5F);
            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,-90),0,0,0);
            poseStack.translate(0,-1.3,0);//BACK, DOWN

            float scale = 0.05F;
            float r = 0;
            float g = 206/255.0F;
            float b = 228/255.0F;
            if (context instanceof Player P) {
                IPlayerEntity IPE = (IPlayerEntity) P;
                r = IPE.rdbt$getHairColorX();
                g = IPE.rdbt$getHairColorY();
                b = IPE.rdbt$getHairColorZ();
            }
            r *= (1-scale) + (float)Math.sin(partialTicks+i) *  scale;
            g *= (1-scale) +  (float)Math.sin(partialTicks+i*2) * scale;
            b *= (1-scale) +  (float)Math.sin(partialTicks+i*3) * scale;
            root().render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, r, g, b, 1);
        }
    }

    @Override
    public ModelPart root() {return Root;}
    @Override
    public void setupAnim(Entity var1, float ageInTicks) {}
}


