package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.CinderellaAnimations;
import net.hydra.jojomod.client.models.stand.animations.ManhattanTransferAnimations;
import net.hydra.jojomod.client.models.stand.animations.RattAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
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
    private final ModelPart stand2;
    private final ModelPart core;
    private final ModelPart mangacores;
    private final ModelPart key_string1;
    private final ModelPart key;
    private final ModelPart key_string2;
    private final ModelPart key2;
    private final ModelPart front_wing;
    private final ModelPart front_flap;
    private final ModelPart front_petals;
    private final ModelPart front_anime;
    private final ModelPart front_manga;
    private final ModelPart front_flower;
    private final ModelPart right_wing;
    private final ModelPart right_flap;
    private final ModelPart right_flower;
    private final ModelPart right_petals;
    private final ModelPart right_anime;
    private final ModelPart right_manga;
    private final ModelPart left_wing;
    private final ModelPart left_flap;
    private final ModelPart left_petals;
    private final ModelPart left_anime;
    private final ModelPart left_manga;
    private final ModelPart left_flower;
    private final ModelPart back_wing;
    private final ModelPart back_flap;
    private final ModelPart back_petals;
    private final ModelPart back_anime;
    private final ModelPart back_manga;
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

        this.stand2 = stand.getChild("stand2");

        this.core = stand.getChild("stand2").getChild("core");

        this.mangacores = stand.getChild("stand2").getChild("core").getChild("mangacores");

        this.key_string1 = stand.getChild("stand2").getChild("key_string1");
        this.key = stand.getChild("stand2").getChild("key_string1").getChild("key");

        this.key_string2 = stand.getChild("stand2").getChild("key_string2");
        this.key2 = stand.getChild("stand2").getChild("key_string2").getChild("key2");

        this.front_wing = stand.getChild("stand2").getChild("front_wing");
        this.front_flap = stand.getChild("stand2").getChild("front_wing").getChild("front_flap");
        this.front_petals = stand.getChild("stand2").getChild("front_wing").getChild("front_petals");
        this.front_anime = stand.getChild("stand2").getChild("front_wing").getChild("front_petals").getChild("front_anime");
        this.front_manga = stand.getChild("stand2").getChild("front_wing").getChild("front_petals").getChild("front_manga");
        this.front_flower = stand.getChild("stand2").getChild("front_wing").getChild("front_flower");

        this.right_wing = stand.getChild("stand2").getChild("right_wing");
        this.right_flap = stand.getChild("stand2").getChild("right_wing").getChild("right_flap");
        this.right_flower = stand.getChild("stand2").getChild("right_wing").getChild("right_flower");
        this.right_anime = stand.getChild("stand2").getChild("right_wing").getChild("right_flower").getChild("right_petals").getChild("right_anime");
        this.right_manga = stand.getChild("stand2").getChild("right_wing").getChild("right_flower").getChild("right_petals").getChild("right_manga");
        this.right_petals = stand.getChild("stand2").getChild("right_wing").getChild("right_flower").getChild("right_petals");

        this.left_wing = stand.getChild("stand2").getChild("left_wing");
        this.left_flap = stand.getChild("stand2").getChild("left_wing").getChild("left_flap");
        this.left_petals = stand.getChild("stand2").getChild("left_wing").getChild("left_petals");
        this.left_anime = stand.getChild("stand2").getChild("left_wing").getChild("left_petals").getChild("left_anime");
        this.left_manga = stand.getChild("stand2").getChild("left_wing").getChild("left_petals").getChild("left_manga");
        this.left_flower = stand.getChild("stand2").getChild("left_wing").getChild("left_flower");

        this.back_wing = stand.getChild("stand2").getChild("back_wing");
        this.back_flap = stand.getChild("stand2").getChild("back_wing").getChild("back_flap");
        this.back_petals = stand.getChild("stand2").getChild("back_wing").getChild("back_petals");
        this.back_anime = stand.getChild("stand2").getChild("back_wing").getChild("back_petals").getChild("back_anime");
        this.back_manga = stand.getChild("stand2").getChild("back_wing").getChild("back_petals").getChild("back_manga");
        this.back_flower = stand.getChild("stand2").getChild("back_wing").getChild("back_flower");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition core = stand2.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(-0.25F))
                .texOffs(2, 20).addBox(-2.0F, 1.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mangacores = core.addOrReplaceChild("mangacores", CubeListBuilder.create().texOffs(25, 3).addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(25, 9).addBox(-1.5F, -4.5F, -3.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-5, 0).addBox(-2.5F, 1.0F, -2.5F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_core_r1 = mangacores.addOrReplaceChild("upper_core_r1", CubeListBuilder.create().texOffs(25, 3).mirror().addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition upper_core_r2 = mangacores.addOrReplaceChild("upper_core_r2", CubeListBuilder.create().texOffs(25, 3).addBox(-1.5F, -4.5F, 1.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition key_string1 = stand2.addOrReplaceChild("key_string1", CubeListBuilder.create().texOffs(61, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 2.5F, -1.5F));

        PartDefinition key = key_string1.addOrReplaceChild("key", CubeListBuilder.create().texOffs(60, 5).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 6).addBox(0.0F, 0.5F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition key_string2 = stand2.addOrReplaceChild("key_string2", CubeListBuilder.create().texOffs(61, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 2.5F, 1.5F));

        PartDefinition key2 = key_string2.addOrReplaceChild("key2", CubeListBuilder.create().texOffs(60, 5).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 6).addBox(0.0F, 0.5F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition front_wing = stand2.addOrReplaceChild("front_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition connectors_r1 = front_wing.addOrReplaceChild("connectors_r1", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition front_flap = front_wing.addOrReplaceChild("front_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition front_wing_r1 = front_flap.addOrReplaceChild("front_wing_r1", CubeListBuilder.create().texOffs(36, 28).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition front_petals = front_wing.addOrReplaceChild("front_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition front_anime = front_petals.addOrReplaceChild("front_anime", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition front_petals_r1 = front_anime.addOrReplaceChild("front_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition front_manga = front_petals.addOrReplaceChild("front_manga", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition manga_petal_r1 = front_manga.addOrReplaceChild("manga_petal_r1", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition front_flower = front_wing.addOrReplaceChild("front_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition front_flower_r1 = front_flower.addOrReplaceChild("front_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_wing = stand2.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0F, 1.5708F, -0.0873F));

        PartDefinition connectors_r2 = right_wing.addOrReplaceChild("connectors_r2", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_flap = right_wing.addOrReplaceChild("right_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -0.3075F));

        PartDefinition right_wing_r1 = right_flap.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(36, 37).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition right_flower = right_wing.addOrReplaceChild("right_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition right_flower_r1 = right_flower.addOrReplaceChild("right_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition right_petals = right_flower.addOrReplaceChild("right_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -5.6413F, -2.2749F));

        PartDefinition right_anime = right_petals.addOrReplaceChild("right_anime", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

       PartDefinition right_petals_r1 = right_anime.addOrReplaceChild("right_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

       PartDefinition right_manga = right_petals.addOrReplaceChild("right_manga", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition manga_petal_r2 = right_manga.addOrReplaceChild("manga_petal_r2", CubeListBuilder.create().texOffs(43, 12).mirror().addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition left_wing = stand2.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, 0.0F, -1.5708F, 0.0873F));

        PartDefinition connectors_r3 = left_wing.addOrReplaceChild("connectors_r3", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition left_flap = left_wing.addOrReplaceChild("left_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition left_wing_r1 = left_flap.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(36, 55).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition left_petals = left_wing.addOrReplaceChild("left_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition left_anime = left_petals.addOrReplaceChild("left_anime", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_petals_r1 = left_anime.addOrReplaceChild("left_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition left_manga = left_petals.addOrReplaceChild("left_manga", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition manga_petal_r3 = left_manga.addOrReplaceChild("manga_petal_r3", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5005F, -0.0218F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, -0.0436F, 0.0F, 0.0F));

        PartDefinition left_flower = left_wing.addOrReplaceChild("left_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition left_flower_r1 = left_flower.addOrReplaceChild("left_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_wing = stand2.addOrReplaceChild("back_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.625F, 0.0F, -3.0543F, 0.0F, 3.1416F));

        PartDefinition connectors_r4 = back_wing.addOrReplaceChild("connectors_r4", CubeListBuilder.create().texOffs(39, 0).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.625F, -3.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_flap = back_wing.addOrReplaceChild("back_flap", CubeListBuilder.create(), PartPose.offset(0.0F, 1.385F, -2.3075F));

        PartDefinition back_wing_r1 = back_flap.addOrReplaceChild("back_wing_r1", CubeListBuilder.create().texOffs(36, 46).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition back_petals = back_wing.addOrReplaceChild("back_petals", CubeListBuilder.create(), PartPose.offset(0.0F, -4.2565F, -4.5822F));

        PartDefinition back_anime = back_petals.addOrReplaceChild("back_anime", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition back_petals_r1 = back_anime.addOrReplaceChild("back_petals_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition back_manga = back_petals.addOrReplaceChild("back_manga", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition manga_petal_r4 = back_manga.addOrReplaceChild("manga_petal_r4", CubeListBuilder.create().texOffs(43, 12).addBox(-3.5F, -2.5043F, -0.0653F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1082F, 0.4882F, 0.0436F, 0.0F, 0.0F));

        PartDefinition back_flower = back_wing.addOrReplaceChild("back_flower", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.8595F, -2.184F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3849F, -2.3073F));

        PartDefinition back_flower_r1 = back_flower.addOrReplaceChild("back_flower_r1", CubeListBuilder.create().texOffs(43, 20).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
        this.animate(pEntity.rain_dodging_manhattan, ManhattanTransferAnimations.Rain_Dodge, pAgeInTicks, 1f);
    }



    @Override
    public ModelPart root() {
        return stand;
    }
}

