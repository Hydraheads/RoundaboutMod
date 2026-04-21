package net.hydra.jojomod.client.models.minions;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.zombie_minion.OcelotMinion;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;

public class OcelotMinionModel<T extends Entity> extends AgeableListModel<T> {
    private static final int CROUCH_STATE = 0;
    private static final int WALK_STATE = 1;
    private static final int SPRINT_STATE = 2;
    protected static final int SITTING_STATE = 3;
    private static final float XO = 0.0F;
    private static final float YO = 16.0F;
    private static final float ZO = -9.0F;
    private static final float HEAD_WALK_Y = 15.0F;
    private static final float HEAD_WALK_Z = -9.0F;
    private static final float BODY_WALK_Y = 12.0F;
    private static final float BODY_WALK_Z = -10.0F;
    private static final float TAIL_1_WALK_Y = 15.0F;
    private static final float TAIL_1_WALK_Z = 8.0F;
    private static final float TAIL_2_WALK_Y = 20.0F;
    private static final float TAIL_2_WALK_Z = 14.0F;
    protected static final float BACK_LEG_Y = 18.0F;
    protected static final float BACK_LEG_Z = 5.0F;
    protected static final float FRONT_LEG_Y = 14.1F;
    private static final float FRONT_LEG_Z = -5.0F;
    private static final String TAIL_1 = "tail1";
    private static final String TAIL_2 = "tail2";
    protected final ModelPart leftHindLeg;
    protected final ModelPart rightHindLeg;
    protected final ModelPart leftFrontLeg;
    protected final ModelPart rightFrontLeg;
    protected final ModelPart tail1;
    protected final ModelPart tail2;
    public final ModelPart head;
    protected final ModelPart body;
    protected int state = 1;

    public OcelotMinionModel(ModelPart $$0) {
        super(true, 10.0F, 4.0F);
        this.head = $$0.getChild("head");
        this.body = $$0.getChild("body");
        this.tail1 = $$0.getChild("tail1");
        this.tail2 = $$0.getChild("tail2");
        this.leftHindLeg = $$0.getChild("left_hind_leg");
        this.rightHindLeg = $$0.getChild("right_hind_leg");
        this.leftFrontLeg = $$0.getChild("left_front_leg");
        this.rightFrontLeg = $$0.getChild("right_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation $$0 = CubeDeformation.NONE;
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        CubeDeformation $$3 = new CubeDeformation(-0.02F);
        $$2.addOrReplaceChild("head", CubeListBuilder.create().addBox("main", -2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, $$0).addBox("nose", -1.5F, -0.001F, -4.0F, 3, 2, 2, $$0, 0, 24).addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2, $$0, 0, 10).addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2, $$0, 6, 10), PartPose.offset(0.0F, 15.0F, -9.0F));
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, 3.0F, -8.0F, 4.0F, 16.0F, 6.0F, $$0), PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        $$2.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, $$0), PartPose.offsetAndRotation(0.0F, 15.0F, 8.0F, 0.9F, 0.0F, 0.0F));
        $$2.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, $$3), PartPose.offset(0.0F, 20.0F, 14.0F));
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, $$0);
        $$2.addOrReplaceChild("left_hind_leg", $$4, PartPose.offset(1.1F, 18.0F, 5.0F));
        $$2.addOrReplaceChild("right_hind_leg", $$4, PartPose.offset(-1.1F, 18.0F, 5.0F));
        CubeListBuilder $$5 = CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, $$0);
        $$2.addOrReplaceChild("left_front_leg", $$5, PartPose.offset(1.2F, 14.1F, -5.0F));
        $$2.addOrReplaceChild("right_front_leg", $$5, PartPose.offset(-1.2F, 14.1F, -5.0F));
        return LayerDefinition.create($$1, 64, 32);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg, this.tail1, this.tail2);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        this.head.xRot = $$5 * ((float)Math.PI / 180F);
        this.head.yRot = $$4 * ((float)Math.PI / 180F);
        if (this.state != 3) {
            this.body.xRot = ((float)Math.PI / 2F);
            if (this.state == 2) {
                this.leftHindLeg.xRot = Mth.cos($$1 * 0.6662F) * $$2;
                this.rightHindLeg.xRot = Mth.cos($$1 * 0.6662F + 0.3F) * $$2;
                this.leftFrontLeg.xRot = Mth.cos($$1 * 0.6662F + (float)Math.PI + 0.3F) * $$2;
                this.rightFrontLeg.xRot = Mth.cos($$1 * 0.6662F + (float)Math.PI) * $$2;
                this.tail2.xRot = 1.7278761F + ((float)Math.PI / 10F) * Mth.cos($$1) * $$2;
            } else {
                this.leftHindLeg.xRot = Mth.cos($$1 * 0.6662F) * $$2;
                this.rightHindLeg.xRot = Mth.cos($$1 * 0.6662F + (float)Math.PI) * $$2;
                this.leftFrontLeg.xRot = Mth.cos($$1 * 0.6662F + (float)Math.PI) * $$2;
                this.rightFrontLeg.xRot = Mth.cos($$1 * 0.6662F) * $$2;
                if (this.state == 1) {
                    this.tail2.xRot = 1.7278761F + ((float)Math.PI / 4F) * Mth.cos($$1) * $$2;
                } else {
                    this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos($$1) * $$2;
                }
            }
        }

    }

    public void prepareMobModel(T $$0, float $$1, float $$2, float $$3) {
        this.body.y = 12.0F;
        this.body.z = -10.0F;
        this.head.y = 15.0F;
        this.head.z = -9.0F;
        this.tail1.y = 15.0F;
        this.tail1.z = 8.0F;
        this.tail2.y = 20.0F;
        this.tail2.z = 14.0F;
        this.leftFrontLeg.y = 14.1F;
        this.leftFrontLeg.z = -5.0F;
        this.rightFrontLeg.y = 14.1F;
        this.rightFrontLeg.z = -5.0F;
        this.leftHindLeg.y = 18.0F;
        this.leftHindLeg.z = 5.0F;
        this.rightHindLeg.y = 18.0F;
        this.rightHindLeg.z = 5.0F;
        this.tail1.xRot = 0.9F;
        if ($$0.isCrouching()) {
            ++this.body.y;
            ModelPart var10000 = this.head;
            var10000.y += 2.0F;
            ++this.tail1.y;
            var10000 = this.tail2;
            var10000.y += -4.0F;
            var10000 = this.tail2;
            var10000.z += 2.0F;
            this.tail1.xRot = ((float) Math.PI / 2F);
            this.tail2.xRot = ((float) Math.PI / 2F);
            this.state = 0;
        } else if ($$0.isSprinting()) {
            this.tail2.y = this.tail1.y;
            ModelPart var7 = this.tail2;
            var7.z += 2.0F;
            this.tail1.xRot = ((float) Math.PI / 2F);
            this.tail2.xRot = ((float) Math.PI / 2F);
            this.state = 2;
        } else {
            this.state = 1;
        }
    }

}