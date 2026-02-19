package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.ZombieFate;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
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

public class HairVeinPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart head_spikes;
    private final ModelPart Root;
    private final ModelPart spike_1;
    private final ModelPart spike_2;
    private final ModelPart spike_3;
    private final ModelPart spike_4;
    private final ModelPart spike_5;
    private final ModelPart spike_6;

    public HairVeinPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.head_spikes = Root.getChild("head_spikes");
        this.spike_1 = this.head_spikes.getChild("spike_1");
        this.spike_2 = this.head_spikes.getChild("spike_2");
        this.spike_3 = this.head_spikes.getChild("spike_3");
        this.spike_4 = this.head_spikes.getChild("spike_4");
        this.spike_5 = this.head_spikes.getChild("spike_5");
        this.spike_6 = this.head_spikes.getChild("spike_6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head_spikes = partdefinition.addOrReplaceChild("head_spikes", CubeListBuilder.create(), PartPose.offset(11.0F, 24.0F, 0.0F));

        PartDefinition spike_1 = head_spikes.addOrReplaceChild("spike_1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7F, -28.0F, 2.0F, 0.0F, -0.0436F, 0.0F));

        PartDefinition spike_2 = head_spikes.addOrReplaceChild("spike_2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0564F, -1.0F, -1.999F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3F, -28.0F, 2.0F, 0.0F, 0.0436F, 0.0F));

        PartDefinition spike_3 = head_spikes.addOrReplaceChild("spike_3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7F, -31.0F, 0.0F, 0.0451F, -0.0909F, -0.0171F));

        PartDefinition spike_4 = head_spikes.addOrReplaceChild("spike_4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3F, -31.0F, 0.0F, 0.0451F, 0.0909F, 0.0171F));

        PartDefinition spike_5 = head_spikes.addOrReplaceChild("spike_5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -32.7F, 0.0F, 0.0356F, 0.0247F, 0.0231F));

        PartDefinition spike_6 = head_spikes.addOrReplaceChild("spike_6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -32.7F, 0.0F, 0.0356F, -0.0247F, -0.014F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head_spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/hair/zombie_hair/zombie_vein.png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation()));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
            //The number at the end is inversely proportional so 2 is half speed

            if (context instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof
                    ZombieFate gp){
                boolean tier2 = gp.extended && gp.getPlayerPos2() == PlayerPosIndex.HAIR_SPIKE_2;
                float partial = partialTicks % 1;
                if (tier2){
                    if (gp.retract){
                        partial*= -((float) ZombieFate.maxSpike2 /3);
                        partial = Math.max(gp.spikeTimeDuring+partial,0);
                    } else {
                        partial*= ((float) ZombieFate.maxSpike2 /3);
                        partial = Math.min(gp.spikeTimeDuring+partial,ZombieFate.maxSpike2);
                    }
                } else {
                    partial = Math.min(((gp.spikeTimeDuring+partial)*6),ZombieFate.maxSpike);
                }

                modifySpike(spike_1,partial,tier2);
                modifySpike(spike_2,partial,tier2);
                modifySpike(spike_3,partial,tier2);
                modifySpike(spike_4,partial,tier2);
                modifySpike(spike_5,partial,tier2);
                modifySpike(spike_6,partial,tier2);
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);

                modifySpike2(spike_1,partial,tier2);
                modifySpike2(spike_2,partial,tier2);
                modifySpike2(spike_3,partial,tier2);
                modifySpike2(spike_4,partial,tier2);
                modifySpike2(spike_5,partial,tier2);
                modifySpike2(spike_6,partial,tier2);
                //root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
            }

        }
    }
    public static float maxSize2 = 48;

    public void modifySpike(ModelPart spike, float partial, boolean tier2){
        if (spike != null){
            spike.xScale = 0.5F;
            spike.yScale = 0.5F;
            if (!tier2){
                spike.zScale = (partial)/3;
            } else {
                spike.zScale = partial;
            }
        }
    }
    public void modifySpike2(ModelPart spike, float partial, boolean tier2){
        if (spike != null){
            spike.xScale = 0.5F;
            spike.yScale = 0.01F;
            if (!tier2){
                spike.zScale = (partial)/3;
            } else {
                spike.zScale = partial;
            }
        }
    }

}

