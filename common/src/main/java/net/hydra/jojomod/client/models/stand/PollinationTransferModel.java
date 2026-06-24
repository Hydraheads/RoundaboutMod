package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.ManhattanTransferAnimations;
import net.hydra.jojomod.client.models.stand.animations.PollinationTransferAnimations;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.PollinationTransferEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;


public class PollinationTransferModel <T extends PollinationTransferEntity> extends StandModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "pollination_transfer"), "main");

    private final ModelPart stand2;
    private final ModelPart core;
    private final ModelPart stem;
    private final ModelPart greenrod;
    private final ModelPart leaf1;
    private final ModelPart leaf2;
    private final ModelPart wing;
    private final ModelPart spikes;
    private final ModelPart wing2;
    private final ModelPart spikes2;
    private final ModelPart wing3;
    private final ModelPart spikes3;
    private final ModelPart wing4;
    private final ModelPart spikes4;
    private final ModelPart petals;
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart bone5;
    private final ModelPart bone6;
    private final ModelPart bone7;
    private final ModelPart bone8;



    @Override
    public void defaultModifiers(T entity) {
        Minecraft mc = Minecraft.getInstance();
        if (entity.getUser() != null) {
            LivingEntity User = entity.getUser();
            if (!mc.isPaused() && !(((TimeStop) entity.level()).CanTimeStopEntity(User))) {
                float tickDelta = mc.getDeltaFrameTime();
            }
        }
        super.defaultModifiers(entity);
    }

    public PollinationTransferModel(ModelPart root) {
        this.stand = root.getChild("stand");

        this.stand2 = stand.getChild("stand2");

        this.core = stand.getChild("stand2").getChild("core");

        this.stem = stand.getChild("stand2").getChild("stem");

        this.greenrod = stand.getChild("stand2").getChild("stem").getChild("greenrod");
        this.leaf1 = stand.getChild("stand2").getChild("stem").getChild("leaf1");
        this.leaf2 = stand.getChild("stand2").getChild("stem").getChild("leaf2");

        this.spikes = stand.getChild("stand2").getChild("wing").getChild("spikes");
        this.spikes2 = stand.getChild("stand2").getChild("wing2").getChild("spikes2");
        this.spikes3 = stand.getChild("stand2").getChild("wing3").getChild("spikes3");
        this.spikes4 = stand.getChild("stand2").getChild("wing4").getChild("spikes4");

        this.wing = stand.getChild("stand2").getChild("wing");

        this.wing2 = stand.getChild("stand2").getChild("wing2");

        this.wing3 = stand.getChild("stand2").getChild("wing3");

        this.wing4 = stand.getChild("stand2").getChild("wing4");

        this.petals = stand.getChild("stand2").getChild("petals");

        this.bone = stand.getChild("stand2").getChild("petals").getChild("bone");
        this.bone2 = stand.getChild("stand2").getChild("petals").getChild("bone2");
        this.bone3 = stand.getChild("stand2").getChild("petals").getChild("bone3");
        this.bone4 = stand.getChild("stand2").getChild("petals").getChild("bone4");
        this.bone5 = stand.getChild("stand2").getChild("petals").getChild("bone5");
        this.bone6 = stand.getChild("stand2").getChild("petals").getChild("bone6");
        this.bone7 = stand.getChild("stand2").getChild("petals").getChild("bone7");
        this.bone8 = stand.getChild("stand2").getChild("petals").getChild("bone8");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition core = stand2.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition stem = stand2.addOrReplaceChild("stem", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition greenrod = stem.addOrReplaceChild("greenrod", CubeListBuilder.create().texOffs(34, 21).addBox(2.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 2.0F, -2.5F));

        PartDefinition leaf1 = stem.addOrReplaceChild("leaf1", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition cube_r1 = leaf1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 3).addBox(0.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition leaf2 = stem.addOrReplaceChild("leaf2", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition cube_r2 = leaf2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition wing = stand2.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = wing.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 24).addBox(-2.9F, -1.95F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(-0.1F, -1.0F, 3.0F, -0.2094F, 0.0F, 0.0F));

        PartDefinition cube_r4 = wing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9F, 3.0F, -0.2155F, 0.0F, 0.0F));

        PartDefinition spikes = wing.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 5.0F));

        PartDefinition cube_r5 = spikes.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(44, 13).addBox(-3.0209F, -1.84F, 0.0452F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.4F, -0.9076F, 0.0F, 0.0F));

        PartDefinition wing2 = stand2.addOrReplaceChild("wing2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r6 = wing2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 24).addBox(-2.9F, -2.05F, -2.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(3.0F, -0.8F, -0.1F, 0.0F, -1.5708F, 0.2182F));

        PartDefinition cube_r7 = wing2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.8F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition spikes2 = wing2.addOrReplaceChild("spikes2", CubeListBuilder.create(), PartPose.offset(5.1359F, -1.914F, 0.0F));

        PartDefinition cube_r8 = spikes2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(44, 11).addBox(-3.0F, -1.9576F, 0.0392F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2641F, -0.386F, 0.0F, 0.0F, -1.5708F, 0.9163F));

        PartDefinition wing3 = stand2.addOrReplaceChild("wing3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = wing3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 9).addBox(-6.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -0.8F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition cube_r10 = wing3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(24, 24).addBox(-2.9F, -2.0F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(-3.0F, -0.9F, -0.1F, 0.0F, -1.5708F, -0.2182F));

        PartDefinition spikes3 = wing3.addOrReplaceChild("spikes3", CubeListBuilder.create(), PartPose.offset(-5.0F, -2.0F, 0.0F));

        PartDefinition cube_r11 = spikes3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(44, 11).addBox(-3.0F, -1.9999F, 0.011F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3359F, -0.414F, 0.0F, 0.0F, -1.5708F, -0.9163F));

        PartDefinition wing4 = stand2.addOrReplaceChild("wing4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r12 = wing4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 24).addBox(-2.9F, -1.95F, -2.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(-0.1F, -0.9F, -3.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r13 = wing4.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -6.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, -3.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition spikes4 = wing4.addOrReplaceChild("spikes4", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, -5.4F));

        PartDefinition cube_r14 = spikes4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(44, 13).addBox(-3.0F, -1.9576F, 0.0392F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1F, 0.0F, 0.9163F, 0.0F, 0.0F));

        PartDefinition petals = stand2.addOrReplaceChild("petals", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone = petals.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.87F, -1.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -2.0F, 0.2F, 0.6109F, 0.7854F, 0.0F));

        PartDefinition bone2 = petals.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r16 = bone2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.87F, -1.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -2.0F, -0.2F, -2.5307F, 0.7854F, 3.1416F));

        PartDefinition bone3 = petals.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r17 = bone3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.87F, 0.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -2.0F, -0.2F, -0.6109F, 0.7854F, 0.0F));

        PartDefinition bone4 = petals.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r18 = bone4.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.87F, 0.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -2.0F, 0.2F, 2.5307F, 0.7854F, 3.1416F));

        PartDefinition bone5 = petals.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r19 = bone5.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2.0F, 0.0F, 0.0F, 1.5708F, 0.6109F));

        PartDefinition bone6 = petals.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r20 = bone6.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -2.5F, 0.6109F, 0.0F, 0.0F));

        PartDefinition bone7 = petals.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r21 = bone7.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -2.0F, 0.0F, 0.0F, 1.5708F, -0.6109F));

        PartDefinition bone8 = petals.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r22 = bone8.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.5F, -0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);

    }
    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
        this.animate(pEntity.flowerhattan_is_loaded, PollinationTransferAnimations.flower_loaded, pAgeInTicks, 1f);
        this.animate(pEntity.rain_dodging_flowerhattan, PollinationTransferAnimations.rain_dodge_flowerhattan, pAgeInTicks, 1f);
        this.animate(pEntity.slow_flowerhattan, PollinationTransferAnimations.f_slow, pAgeInTicks, 1f);
        this.animate(pEntity.slow_flowerhattan_back, PollinationTransferAnimations.b_slow, pAgeInTicks, 1f);
        this.animate(pEntity.slow_flowerhattan_left, PollinationTransferAnimations.l_slow, pAgeInTicks, 1f);
        this.animate(pEntity.slow_flowerhattan_right, PollinationTransferAnimations.r_slow, pAgeInTicks, 1f);
        this.animate(pEntity.forward_flowerhattan_incipit, PollinationTransferAnimations.f_beg, pAgeInTicks, 1f);
        this.animate(pEntity.back_flowerhattan_incipit, PollinationTransferAnimations.b_beg, pAgeInTicks, 1f);
        this.animate(pEntity.left_flowerhattan_incipit, PollinationTransferAnimations.l_beg, pAgeInTicks, 1f);
        this.animate(pEntity.right_flowerhattan_incipit, PollinationTransferAnimations.r_beg, pAgeInTicks, 1f);
        this.animate(pEntity.forward_flowerhattan_stop, PollinationTransferAnimations.f_stop, pAgeInTicks, 1f);
        this.animate(pEntity.back_flowerhattan_stop, PollinationTransferAnimations.b_stop, pAgeInTicks, 1f);
        this.animate(pEntity.left_flowerhattan_stop, PollinationTransferAnimations.l_stop, pAgeInTicks, 1f);
        this.animate(pEntity.right_flowerhattan_stop, PollinationTransferAnimations.r_stop, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
