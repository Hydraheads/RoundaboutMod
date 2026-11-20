package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SilverChariotSwordModel extends PsuedoHierarchicalModel {

    private final ModelPart sword;
    private final ModelPart blade;
    private final ModelPart tip;

    public SilverChariotSwordModel() {
        super(RenderType::entityTranslucent);

        this.sword = createBodyLayer().bakeRoot();
        this.blade = this.sword.getChild("blade");
        this.tip = this.blade.getChild("tip");

        ModStrayModels.SILVER_CHARIOT_SWORD = this;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        /*

        PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(112, 100).addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(112, 92).addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 11.5F, 0.0667F, 0.0F, 0.0F, -1.5708F));

        PartDefinition blade = sword.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(92, 107).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0667F, 0.0F, 0.0F, 1.5708F));

        PartDefinition tip = blade.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(97, 110).addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -5.0F));

        */

        PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(112, 100).addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(112, 92).addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.5F, 5.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition blade = sword.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(92, 107).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0667F, 0.0F, 0.0F, 1.5708F));

        PartDefinition tip = blade.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(97, 110).addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -5.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float $$7) {
        sword.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    @Override
    public ModelPart root() {
        return sword;
    }

    private static final ResourceLocation PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/silver_chariot.png");
    private static final ResourceLocation PART_3_GREY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_silver_chariot.png");
    private static final ResourceLocation PART_3_MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/manga_silver_chariot.png");
    private static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/part_5_silver_chariot.png");
    private static final ResourceLocation AQUA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/aqua_silver_chariot.png");
    private static final ResourceLocation AZURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/azure_silver_chariot.png");
    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/blue_silver_chariot.png");
    private static final ResourceLocation CRYSTAL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/crystal_silver_chariot.png");
    private static final ResourceLocation END_OF_THE_WORLD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/end_of_the_world_silver_chariot.png");
    private static final ResourceLocation GENESIS_OF_THE_UNIVERSE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/genesis_of_the_universe_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_A = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_a_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_B = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_b_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_C = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_c_silver_chariot.png");
    private static final ResourceLocation NIGHTMARE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/nightmare_silver_chariot.png");
    private static final ResourceLocation ORANGE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/orange_silver_chariot.png");
    private static final ResourceLocation PASSIONE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/passione_silver_chariot.png");
    private static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/purple_silver_chariot.png");
    private static final ResourceLocation SANDY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/sandy_silver_chariot.png");
    private static final ResourceLocation TURQUOISE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/turquoise_silver_chariot.png");
    private static final ResourceLocation YELLOW = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/yellow_silver_chariot.png");
    private static final ResourceLocation PHARAOH = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/pharaoh_silver_chariot.png");


    public ResourceLocation getTextureLocation(Entity context, byte skin) {
        switch (skin) {
            case SilverChariotEntity.PART_3_GREY -> {
                return PART_3_GREY;
            }
            case SilverChariotEntity.PART_3_MANGA -> {
                return PART_3_MANGA;
            }
            case SilverChariotEntity.PART_5 -> {
                return PART_5;
            }
            case SilverChariotEntity.AQUA -> {
                return AQUA;
            }
            case SilverChariotEntity.AZURE -> {
                return AZURE;
            }
            case SilverChariotEntity.BLUE -> {
                return BLUE;
            }
            case SilverChariotEntity.CRYSTAL -> {
                return CRYSTAL;
            }
            case SilverChariotEntity.END_OF_THE_WORLD -> {
                return END_OF_THE_WORLD;
            }
            case SilverChariotEntity.GENESIS_OF_THE_UNIVERSE -> {
                return GENESIS_OF_THE_UNIVERSE;
            }
            case SilverChariotEntity.JOJONIUM_A -> {
                return JOJONIUM_A;
            }
            case SilverChariotEntity.JOJONIUM_B -> {
                return JOJONIUM_B;
            }
            case SilverChariotEntity.JOJONIUM_C -> {
                return JOJONIUM_C;
            }
            case SilverChariotEntity.NIGHTMARE -> {
                return NIGHTMARE;
            }
            case SilverChariotEntity.ORANGE -> {
                return ORANGE;
            }
            case SilverChariotEntity.PASSIONE -> {
                return PASSIONE;
            }
            case SilverChariotEntity.PURPLE -> {
                return PURPLE;
            }
            case SilverChariotEntity.SANDY -> {
                return SANDY;
            }
            case SilverChariotEntity.TURQUOISE -> {
                return TURQUOISE;
            }
            case SilverChariotEntity.YELLOW -> {
                return YELLOW;
            }
            case SilverChariotEntity.PHARAOH -> {
                return PHARAOH;
            }
            default -> {
                return PART_3;
            }
        }
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {

    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float rr, float gg, float bb, float v) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context, (byte)0)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            StandUser user = ((StandUser) LE);
        }

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
    }
}
