package net.hydra.jojomod.client.models.minions;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.zombie_minion.ChickenMinion;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;

public class ChickenMinionModel<T extends Entity> extends AgeableListModel<T> {
    public static final String RED_THING = "red_thing";
    public final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    public final ModelPart beak;
    public final ModelPart redThing;

    public ChickenMinionModel(ModelPart $$0) {
        this.head = $$0.getChild("head");
        this.beak = $$0.getChild("beak");
        this.redThing = $$0.getChild("red_thing");
        this.body = $$0.getChild("body");
        this.rightLeg = $$0.getChild("right_leg");
        this.leftLeg = $$0.getChild("left_leg");
        this.rightWing = $$0.getChild("right_wing");
        this.leftWing = $$0.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        int $$2 = 16;
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        $$1.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        $$1.addOrReplaceChild("red_thing", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
        $$1.addOrReplaceChild("right_leg", $$3, PartPose.offset(-2.0F, 19.0F, 1.0F));
        $$1.addOrReplaceChild("left_leg", $$3, PartPose.offset(1.0F, 19.0F, 1.0F));
        $$1.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
        $$1.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));
        return LayerDefinition.create($$0, 64, 32);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.beak, this.redThing);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        this.head.xRot = $$5 * ((float)Math.PI / 180F);
        this.head.yRot = $$4 * ((float)Math.PI / 180F);
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;
        this.rightLeg.xRot = Mth.cos($$1 * 0.6662F) * 1.4F * $$2;
        this.leftLeg.xRot = Mth.cos($$1 * 0.6662F + (float)Math.PI) * 1.4F * $$2;
        this.rightWing.zRot = $$3;
        this.leftWing.zRot = -$$3;
    }
}
