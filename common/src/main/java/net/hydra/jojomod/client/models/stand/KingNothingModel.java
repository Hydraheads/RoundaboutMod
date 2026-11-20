package net.hydra.jojomod.client.models.stand;// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.hydra.jojomod.client.models.stand.animations.KingNothingAnimations;
import net.hydra.jojomod.entity.stand.KingNothingEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class KingNothingModel<T extends KingNothingEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");

    public KingNothingModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horn_r = head2.addOrReplaceChild("horn_r", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.6F, -6.0F, 0.5F, 0.0F, 0.0F, 0.6981F));

        PartDefinition horn_r1 = horn_r.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(56, 14).addBox(-2.5F, -3.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition ram_r = horn_r.addOrReplaceChild("ram_r", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horn_r2 = ram_r.addOrReplaceChild("horn_r2", CubeListBuilder.create().texOffs(77, 16).addBox(-2.5F, -3.0F, -3.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition horn_l = head2.addOrReplaceChild("horn_l", CubeListBuilder.create(), PartPose.offsetAndRotation(3.6F, -6.0F, 0.5F, 0.0F, 0.0F, -0.6981F));

        PartDefinition horn_r3 = horn_l.addOrReplaceChild("horn_r3", CubeListBuilder.create().texOffs(56, 36).addBox(-2.5F, -3.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition ram_l = horn_l.addOrReplaceChild("ram_l", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horn_r4 = ram_l.addOrReplaceChild("horn_r4", CubeListBuilder.create().texOffs(77, 16).mirror().addBox(0.5F, -3.0F, -3.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition layer = upper_chest_only.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.23F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition jigsaw = upper_chest_only.addOrReplaceChild("jigsaw", CubeListBuilder.create().texOffs(16, 53).addBox(0.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition jigsaw2 = upper_chest_only.addOrReplaceChild("jigsaw2", CubeListBuilder.create().texOffs(56, 7).addBox(0.0F, -6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.06F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition jigsaw3 = upper_chest_only.addOrReplaceChild("jigsaw3", CubeListBuilder.create().texOffs(56, 0).addBox(-4.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition jigsaw4 = upper_chest_only.addOrReplaceChild("jigsaw4", CubeListBuilder.create().texOffs(56, 29).addBox(-4.0F, -6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition rlayer = upper_right_arm.addOrReplaceChild("rlayer", CubeListBuilder.create().texOffs(40, 31).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rjigsaw = upper_right_arm.addOrReplaceChild("rjigsaw", CubeListBuilder.create().texOffs(0, 61).addBox(0.02F, -2.99F, -2.0F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw2 = upper_right_arm.addOrReplaceChild("rjigsaw2", CubeListBuilder.create().texOffs(64, 47).addBox(0.02F, -2.99F, 0.01F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw3 = upper_right_arm.addOrReplaceChild("rjigsaw3", CubeListBuilder.create().texOffs(64, 20).addBox(0.02F, -6.0F, -2.0F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw4 = upper_right_arm.addOrReplaceChild("rjigsaw4", CubeListBuilder.create().texOffs(64, 42).addBox(0.02F, -6.0F, 0.01F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw5 = upper_right_arm.addOrReplaceChild("rjigsaw5", CubeListBuilder.create().texOffs(32, 43).addBox(-2.0F, -2.99F, -2.0F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw6 = upper_right_arm.addOrReplaceChild("rjigsaw6", CubeListBuilder.create().texOffs(24, 60).addBox(-2.0F, -2.99F, 0.01F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw7 = upper_right_arm.addOrReplaceChild("rjigsaw7", CubeListBuilder.create().texOffs(56, 42).addBox(-2.0F, -6.0F, -2.0F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition rjigsaw8 = upper_right_arm.addOrReplaceChild("rjigsaw8", CubeListBuilder.create().texOffs(16, 60).addBox(-2.0F, -6.0F, 0.01F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 5.15F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create(), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition rjigsaw9 = lower_right_arm.addOrReplaceChild("rjigsaw9", CubeListBuilder.create().texOffs(64, 67).addBox(0.044F, 3.022F, -2.0F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw10 = lower_right_arm.addOrReplaceChild("rjigsaw10", CubeListBuilder.create().texOffs(0, 71).addBox(0.044F, 3.022F, 0.022F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw11 = lower_right_arm.addOrReplaceChild("rjigsaw11", CubeListBuilder.create().texOffs(48, 70).addBox(0.044F, 0.0F, -2.0F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw12 = lower_right_arm.addOrReplaceChild("rjigsaw12", CubeListBuilder.create().texOffs(56, 70).addBox(0.044F, 0.0F, 0.022F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw13 = lower_right_arm.addOrReplaceChild("rjigsaw13", CubeListBuilder.create().texOffs(48, 65).addBox(-2.0F, 3.022F, -2.0F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw14 = lower_right_arm.addOrReplaceChild("rjigsaw14", CubeListBuilder.create().texOffs(8, 66).addBox(-2.0F, 3.022F, 0.022F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw15 = lower_right_arm.addOrReplaceChild("rjigsaw15", CubeListBuilder.create().texOffs(56, 65).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition rjigsaw16 = lower_right_arm.addOrReplaceChild("rjigsaw16", CubeListBuilder.create().texOffs(0, 66).addBox(-2.0F, 0.0F, 0.022F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition llayer = upper_left_arm.addOrReplaceChild("llayer", CubeListBuilder.create().texOffs(16, 43).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ljigsaw = upper_left_arm.addOrReplaceChild("ljigsaw", CubeListBuilder.create().texOffs(8, 61).addBox(0.02F, -2.99F, -2.0F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw2 = upper_left_arm.addOrReplaceChild("ljigsaw2", CubeListBuilder.create().texOffs(64, 57).addBox(0.02F, -6.0F, -2.0F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw3 = upper_left_arm.addOrReplaceChild("ljigsaw3", CubeListBuilder.create().texOffs(32, 60).addBox(-2.0F, -2.99F, -2.0F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw4 = upper_left_arm.addOrReplaceChild("ljigsaw4", CubeListBuilder.create().texOffs(48, 60).addBox(-2.0F, -6.0F, -2.0F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw5 = upper_left_arm.addOrReplaceChild("ljigsaw5", CubeListBuilder.create().texOffs(64, 52).addBox(0.02F, -2.99F, 0.01F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw6 = upper_left_arm.addOrReplaceChild("ljigsaw6", CubeListBuilder.create().texOffs(64, 62).addBox(0.02F, -6.0F, 0.01F, 1.98F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw7 = upper_left_arm.addOrReplaceChild("ljigsaw7", CubeListBuilder.create().texOffs(40, 60).addBox(-2.0F, -2.99F, 0.01F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition ljigsaw8 = upper_left_arm.addOrReplaceChild("ljigsaw8", CubeListBuilder.create().texOffs(56, 60).addBox(-2.0F, -6.0F, 0.01F, 2.0F, 2.99F, 1.99F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 5.25F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create(), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition ljigsaw9 = lower_left_arm.addOrReplaceChild("ljigsaw9", CubeListBuilder.create().texOffs(16, 70).addBox(0.044F, 3.022F, -2.0F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw10 = lower_left_arm.addOrReplaceChild("ljigsaw10", CubeListBuilder.create().texOffs(40, 70).addBox(0.044F, 0.0F, -2.0F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw11 = lower_left_arm.addOrReplaceChild("ljigsaw11", CubeListBuilder.create().texOffs(16, 65).addBox(-2.0F, 3.022F, -2.0F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw12 = lower_left_arm.addOrReplaceChild("ljigsaw12", CubeListBuilder.create().texOffs(40, 65).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw13 = lower_left_arm.addOrReplaceChild("ljigsaw13", CubeListBuilder.create().texOffs(24, 70).addBox(0.044F, 3.022F, 0.022F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw14 = lower_left_arm.addOrReplaceChild("ljigsaw14", CubeListBuilder.create().texOffs(32, 70).addBox(0.044F, 0.0F, 0.022F, 1.956F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw15 = lower_left_arm.addOrReplaceChild("ljigsaw15", CubeListBuilder.create().texOffs(24, 65).addBox(-2.0F, 0.0F, 0.022F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition ljigsaw16 = lower_left_arm.addOrReplaceChild("ljigsaw16", CubeListBuilder.create().texOffs(32, 65).addBox(-2.0F, 3.022F, 0.022F, 2.0F, 2.978F, 1.978F, new CubeDeformation(0.022F)), PartPose.offset(0.0F, -0.25F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.03F))
                .texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(48, 20).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(48, 51).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(32, 20).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.02F))
                .texOffs(40, 41).addBox(-2.0F, -0.0001F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.251F))
                .texOffs(32, 51).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(24, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(0, 42).addBox(-2.0F, 0.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersKingNothing(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw,
                          float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() / 20));
        this.animate(pEntity.disintegrate, KingNothingAnimations.DISINTEGRATE, pAgeInTicks, 1f);
        this.animate(pEntity.reach, KingNothingAnimations.REACH, pAgeInTicks, 2f);
        this.animate(pEntity.returning, KingNothingAnimations.RETURN, pAgeInTicks, 1f);
        this.animate(pEntity.hideHorns, KingNothingAnimations.HIDE_HORNS, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}