package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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


public class PollinationTransferModel <T extends ManhattanTransferEntity> extends StandModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "pollination_transfer"), "main");
    private final ModelPart core;
    private final ModelPart stem;
    private final ModelPart greenrod;
    private final ModelPart leaf1;
    private final ModelPart leaf2;
    private final ModelPart wing;
    private final ModelPart wing2;
    private final ModelPart wing3;
    private final ModelPart wing4;
    private final ModelPart base;
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

        this.core = stand.getChild("core");

        this.stem = stand.getChild("stem");

        this.greenrod = stand.getChild("stem").getChild("greenrod");
        this.leaf1 = stand.getChild("stem").getChild("leaf1");
        this.leaf2 = stand.getChild("stem").getChild("leaf2");



        this.wing = stand.getChild("wing");

        this.wing2 = stand.getChild("wing2");

        this.wing3 = stand.getChild("wing3");

        this.wing4 = stand.getChild("wing4");

        this.base = stand.getChild("base");

        this.petals = stand.getChild("base").getChild("petals");

        this.bone = stand.getChild("base").getChild("petals").getChild("bone");
        this.bone2 = stand.getChild("base").getChild("petals").getChild("bone2");
        this.bone3 = stand.getChild("base").getChild("petals").getChild("bone3");
        this.bone4 = stand.getChild("base").getChild("petals").getChild("bone4");
        this.bone5 = stand.getChild("base").getChild("petals").getChild("bone5");
        this.bone6 = stand.getChild("base").getChild("petals").getChild("bone6");
        this.bone7 = stand.getChild("base").getChild("petals").getChild("bone7");
        this.bone8 = stand.getChild("base").getChild("petals").getChild("bone8");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition core = stand.addOrReplaceChild("core", CubeListBuilder.create().texOffs(1, 28).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition stem = stand.addOrReplaceChild("stem", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition greenrod = stem.addOrReplaceChild("greenrod", CubeListBuilder.create().texOffs(34, 21).addBox(2.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 2.0F, -2.5F));

        PartDefinition leaf1 = stem.addOrReplaceChild("leaf1", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition cube_r1 = leaf1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 38).addBox(0.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition leaf2 = stem.addOrReplaceChild("leaf2", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition cube_r2 = leaf2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 38).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition wing = stand.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = wing.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 13).addBox(-3.0209F, -1.9976F, 0.1684F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6F, 5.2F, -0.9076F, 0.0F, -0.0349F));

        PartDefinition cube_r4 = wing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 24).addBox(-3.1F, -2.0F, -1.0F, 6.2F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, -0.2094F, 0.0F, -0.0349F));

        PartDefinition cube_r5 = wing.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9F, 3.0F, -0.2155F, -0.0025F, -0.034F));

        PartDefinition wing2 = stand.addOrReplaceChild("wing2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r6 = wing2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(28, 11).addBox(-3.0F, -1.9576F, 0.0392F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4F, -2.5F, 0.0F, 0.0F, -1.5708F, 0.9163F));

        PartDefinition cube_r7 = wing2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 24).addBox(-3.1F, -2.0F, -2.0F, 6.2F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.2182F));

        PartDefinition cube_r8 = wing2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.9F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition wing3 = stand.addOrReplaceChild("wing3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = wing3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 9).addBox(-6.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -0.9F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition cube_r10 = wing3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(28, 11).addBox(-3.0F, -1.9576F, -0.0392F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4F, -2.5F, 0.0F, 0.0F, -1.5708F, -0.9163F));

        PartDefinition cube_r11 = wing3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(24, 24).addBox(-3.1F, -2.0F, -1.0F, 6.2F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.0F, -1.5708F, -0.2182F));

        PartDefinition wing4 = stand.addOrReplaceChild("wing4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r12 = wing4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 13).addBox(-3.0F, -1.9576F, 0.0392F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -5.4F, 0.9163F, 0.0F, 0.0F));

        PartDefinition cube_r13 = wing4.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(24, 24).addBox(-3.1F, -2.0F, -2.0F, 6.2F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r14 = wing4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -6.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9F, -3.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition base = stand.addOrReplaceChild("base", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, -1.1F, 0.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.9F, -3.0F));

        PartDefinition petals = base.addOrReplaceChild("petals", CubeListBuilder.create(), PartPose.offset(0.0F, 0.9F, 3.0F));

        PartDefinition bone = petals.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.8604F, -1.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.6109F, 0.7854F, 0.0F));

        PartDefinition bone2 = petals.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r16 = bone2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.8604F, -1.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -2.5307F, 0.7854F, 3.1416F));

        PartDefinition bone3 = petals.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r17 = bone3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.8604F, 0.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.6109F, 0.7854F, 0.0F));

        PartDefinition bone4 = petals.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r18 = bone4.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(28, 5).addBox(-2.0F, -5.8604F, 0.7287F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 2.5307F, 0.7854F, 3.1416F));

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

   /* @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }*/

    @Override
    public ModelPart root() {
        return stand;
    }
}
