package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
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

public class BodySpikePart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart body_spikes;
    private final ModelPart Root;
    private final ModelPart spike1;
    private final ModelPart spike2;
    private final ModelPart spike3;
    private final ModelPart spike4;
    private final ModelPart spike5;
    private final ModelPart spike6;
    private final ModelPart spike7;
    private final ModelPart spike8;
    private final ModelPart spike9;
    private final ModelPart spike10;

    public BodySpikePart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.body_spikes = Root.getChild("body_spikes");
        this.spike1 = this.body_spikes.getChild("spike1");
        this.spike2 = this.body_spikes.getChild("spike2");
        this.spike3 = this.body_spikes.getChild("spike3");
        this.spike4 = this.body_spikes.getChild("spike4");
        this.spike5 = this.body_spikes.getChild("spike5");
        this.spike6 = this.body_spikes.getChild("spike6");
        this.spike7 = this.body_spikes.getChild("spike7");
        this.spike8 = this.body_spikes.getChild("spike8");
        this.spike9 = this.body_spikes.getChild("spike9");
        this.spike10 = this.body_spikes.getChild("spike10");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body_spikes = partdefinition.addOrReplaceChild("body_spikes", CubeListBuilder.create(), PartPose.offset(2.5922F, 3.8399F, 0.2773F));

        PartDefinition spike1 = body_spikes.addOrReplaceChild("spike1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.996F, -0.9218F, -1.9897F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2172F, 6.8564F, -1.728F, 0.3289F, 0.3736F, 0.1239F));

        PartDefinition spike2 = body_spikes.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0074F, -0.977F, -1.9978F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4292F, 6.964F, -1.8855F, 0.3289F, -0.3736F, -0.1239F));

        PartDefinition spike3 = body_spikes.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0532F, -1.0071F, -2.0045F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.159F, 5.1574F, -0.2089F, -3.0803F, -0.9083F, 3.0453F));

        PartDefinition spike4 = body_spikes.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0533F, -0.9885F, -1.991F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4882F, 5.1916F, -0.4954F, -3.0803F, 0.9083F, -3.0453F));

        PartDefinition spike5 = body_spikes.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0999F, -1.9866F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0922F, 0.668F, -0.96F, 2.2631F, 0.0F, -3.1416F));

        PartDefinition spike6 = body_spikes.addOrReplaceChild("spike6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.993F, -1.1116F, -1.9912F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.125F, -0.075F, 0.15F, 1.0749F, -1.2921F, -1.5112F));

        PartDefinition spike7 = body_spikes.addOrReplaceChild("spike7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1516F, -1.0647F, -1.9942F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3008F, -0.3496F, 0.9614F, 1.0749F, 1.2921F, 1.5112F));

        PartDefinition spike8 = body_spikes.addOrReplaceChild("spike8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0427F, -1.0012F, -1.9953F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6513F, 2.3889F, -2.0127F, -0.2795F, 0.0792F, 0.4052F));

        PartDefinition spike9 = body_spikes.addOrReplaceChild("spike9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.972F, -0.9713F, -2.0005F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7352F, 2.1688F, 0.6415F, -2.8591F, -0.1631F, -2.7607F));

        PartDefinition spike10 = body_spikes.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0427F, -1.0012F, -1.9953F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6513F, 4.3889F, -2.0127F, 0.4186F, 0.0792F, 0.4052F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/hair/vampire_body/vampire_hair_white.png");
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

            if (context instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers() instanceof VampireGeneralPowers gp){
                boolean tier2 = gp.extended && gp.getPlayerPos2() == PlayerPosIndex.HAIR_SPIKE_2;
                float partial = partialTicks % 1;
                if (tier2){
                    if (gp.retract){
                        partial*= -((float) VampireGeneralPowers.maxSpike2 /3);
                        partial = Math.max(gp.spikeTimeDuring+partial,0);
                    } else {
                        partial*= ((float) VampireGeneralPowers.maxSpike2 /3);
                        partial = Math.min(gp.spikeTimeDuring+partial,VampireGeneralPowers.maxSpike2);
                    }
                } else {
                    partial = Math.min(gp.spikeTimeDuring+partial,VampireGeneralPowers.maxSpike);
                }

                modifySpike(spike1,partial,tier2);
                modifySpike(spike2,partial,tier2);
                modifySpike(spike3,partial,tier2);
                modifySpike(spike4,partial,tier2);
                modifySpike(spike5,partial,tier2);
                modifySpike(spike6,partial,tier2);
                modifySpike(spike7,partial,tier2);
                modifySpike(spike8,partial,tier2);
                modifySpike(spike9,partial,tier2);
                modifySpike(spike10,partial,tier2);
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);

                modifySpike2(spike1,partial,tier2);
                modifySpike2(spike2,partial,tier2);
                modifySpike2(spike3,partial,tier2);
                modifySpike2(spike4,partial,tier2);
                modifySpike2(spike5,partial,tier2);
                modifySpike2(spike6,partial,tier2);
                modifySpike2(spike7,partial,tier2);
                modifySpike2(spike8,partial,tier2);
                modifySpike2(spike9,partial,tier2);
                modifySpike2(spike10,partial,tier2);
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
            }

        }
    }
    public static float maxSize2 = 48;

    public void modifySpike(ModelPart spike, float partial, boolean tier2){
        if (spike != null){
            spike.xScale = 0.01F;
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

