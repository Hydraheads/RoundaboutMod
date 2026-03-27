package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ManhattanTransferModel<T extends ManhattanTransferEntity> extends StandModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "manhattan_transfer"), "main");
    private final ModelPart core;
    private final ModelPart mangacores;
    private final ModelPart key_string1;
    private final ModelPart key;
    private final ModelPart key_string2;
    private final ModelPart key2;
    private final ModelPart front_wing;
    private final ModelPart front_flap;
    private final ModelPart front_petals;
    private final ModelPart front_flower;
    private final ModelPart right_wing;
    private final ModelPart right_flap;
    private final ModelPart right_flower;
    private final ModelPart right_petals;
    private final ModelPart left_wing;
    private final ModelPart left_flap;
    private final ModelPart left_petals;
    private final ModelPart left_flower;
    private final ModelPart back_wing;
    private final ModelPart back_flap;
    private final ModelPart back_petals;
    private final ModelPart back_flower;

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

    public ManhattanTransferModel(ModelPart root) {
        this.stand = root.getChild("stand");

        this.core = stand.getChild("core");

        this.mangacores = stand.getChild("core").getChild("mangacores");

        this.key_string1 = stand.getChild("key_string1");
        this.key = stand.getChild("key_string1").getChild("key");

        this.key_string2 = stand.getChild("key_string2");
        this.key2 = stand.getChild("key_string2").getChild("key2");

        this.front_wing = stand.getChild("front_wing");

        this.front_flap = stand.getChild("front_wing").getChild("front_flap");
        this.front_petals = stand.getChild("front_wing").getChild("front_petals");
        this.front_flower = stand.getChild("front_wing").getChild("front_flower");

        this.right_wing = stand.getChild("right_wing");

        this.right_flap = this.right_wing.getChild("right_flap");
        this.right_flower = this.right_wing.getChild("right_flower");
        this.right_petals = this.right_flower.getChild("right_petals");

        this.left_wing = stand.getChild("left_wing");

        this.left_flap = stand.getChild("left_wing").getChild("left_flap");
        this.left_petals = stand.getChild("left_wing").getChild("left_petals");
        this.left_flower = stand.getChild("left_wing").getChild("left_flower");

        this.back_wing = stand.getChild("back_wing");

        this.back_flap = stand.getChild("back_wing").getChild("back_flap");
        this.back_petals = stand.getChild("back_wing").getChild("back_petals");
        this.back_flower = stand.getChild("back_wing").getChild("back_flower");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition core = stand.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(-0.25F))
                .texOffs(2, 20).addBox(-2.0F, 1.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mangacores = core.addOrReplaceChild("mangacores", CubeListBuilder.create().texOffs(25, 3).addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(25, 9).addBox(-1.5F, -4.5F, -3.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-5, 0).addBox(-2.5F, 1.0F, -2.5F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_core_r1 = mangacores.addOrReplaceChild("upper_core_r1", CubeListBuilder.create().texOffs(25, 3).mirror().addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition upper_core_r2 = mangacores.addOrReplaceChild("upper_core_r2", CubeListBuilder.create().texOffs(25, 3).addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition key_string1 = stand.addOrReplaceChild("key_string1", CubeListBuilder.create().texOffs(61, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 2.5F, -1.5F));

        PartDefinition key = key_string1.addOrReplaceChild("key", CubeListBuilder.create().texOffs(60, 5).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 6).addBox(0.0F, 0.5F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition key_string2 = stand.addOrReplaceChild("key_string2", CubeListBuilder.create().texOffs(61, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 2.5F, 1.5F));

        PartDefinition key2 = key_string2.addOrReplaceChild("key2", CubeListBuilder.create().texOffs(60, 5).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 6).addBox(0.0F, 0.5F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition front_wing = stand.addOrReplaceChild("front_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition connectors_r1 = front_wing.addOrReplaceChild("connectors_r1", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition front_flap = front_wing.addOrReplaceChild("front_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition front_wing_r1 = front_flap.addOrReplaceChild("front_wing_r1", CubeListBuilder.create().texOffs(36, 28).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition front_petals = front_wing.addOrReplaceChild("front_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition manga_petal_r1 = front_petals.addOrReplaceChild("manga_petal_r1", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition front_petals_r1 = front_petals.addOrReplaceChild("front_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition front_flower = front_wing.addOrReplaceChild("front_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition front_flower_r1 = front_flower.addOrReplaceChild("front_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_wing = stand.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0F, 1.5708F, -0.0873F));

        PartDefinition connectors_r2 = right_wing.addOrReplaceChild("connectors_r2", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_flap = right_wing.addOrReplaceChild("right_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -0.3075F));

        PartDefinition right_wing_r1 = right_flap.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(36, 37).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition right_flower = right_wing.addOrReplaceChild("right_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition right_flower_r1 = right_flower.addOrReplaceChild("right_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_petals = right_flower.addOrReplaceChild("right_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -5.6413F, -2.2749F));

        PartDefinition manga_petal_r2 = right_petals.addOrReplaceChild("manga_petal_r2", CubeListBuilder.create().texOffs(43, 12).mirror().addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition right_petals_r1 = right_petals.addOrReplaceChild("right_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition left_wing = stand.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0F, -1.5708F, 0.0873F));

        PartDefinition connectors_r3 = left_wing.addOrReplaceChild("connectors_r3", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition left_flap = left_wing.addOrReplaceChild("left_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition left_wing_r1 = left_flap.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(36, 55).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition left_petals = left_wing.addOrReplaceChild("left_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition manga_petal_r3 = left_petals.addOrReplaceChild("manga_petal_r3", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition left_petals_r1 = left_petals.addOrReplaceChild("left_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition left_flower = left_wing.addOrReplaceChild("left_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition left_flower_r1 = left_flower.addOrReplaceChild("left_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_wing = stand.addOrReplaceChild("back_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, -3.0543F, 0.0F, 3.1416F));

        PartDefinition connectors_r4 = back_wing.addOrReplaceChild("connectors_r4", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_flap = back_wing.addOrReplaceChild("back_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition back_wing_r1 = back_flap.addOrReplaceChild("back_wing_r1", CubeListBuilder.create().texOffs(36, 46).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition back_petals = back_wing.addOrReplaceChild("back_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition manga_petal_r4 = back_petals.addOrReplaceChild("manga_petal_r4", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5043F, -0.0653F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, 0.0436F, 0.0F, 0.0F));

        PartDefinition back_petals_r1 = back_petals.addOrReplaceChild("back_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_flower = back_wing.addOrReplaceChild("back_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition back_flower_r1 = back_flower.addOrReplaceChild("back_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    StandPowers Power = new PowersStarPlatinum(null);

   /* @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
*/


    @Override
    public ModelPart root() {
        return stand;
    }
}

