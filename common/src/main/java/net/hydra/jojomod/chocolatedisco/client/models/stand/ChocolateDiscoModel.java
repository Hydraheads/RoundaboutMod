package net.hydra.jojomod.chocolatedisco.client.models.stand;

import net.hydra.jojomod.client.models.stand.StandModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;

public class ChocolateDiscoModel
        extends StandModel<ChocolateDiscoEntity> {

    private final ModelPart upper;
    private final ModelPart lower;

    public ChocolateDiscoModel(ModelPart root) {

        this.stand = root.getChild("stand");

        ModelPart stand2 =
                this.stand.getChild("stand2");

        this.upper =
                stand2.getChild("upper");

        this.lower =
                stand2.getChild("lower");

        this.head = this.stand;
        this.body = this.stand;
        this.leftHand = this.stand;
        this.rightHand = this.stand;
    }

    public static LayerDefinition getTexturedModelData() {

        MeshDefinition meshdefinition =
                new MeshDefinition();

        PartDefinition partdefinition =
                meshdefinition.getRoot();

        PartDefinition stand =
                partdefinition.addOrReplaceChild(
                        "stand",
                        CubeListBuilder.create(),
                        PartPose.offsetAndRotation(
                                -1.5F,
                                -0.5F,
                                -2.3F,
                                0.0F,
                                0.0F,
                                0.0F
                        )
                );

        PartDefinition stand2 =
                stand.addOrReplaceChild(
                        "stand2",
                        CubeListBuilder.create(),
                        PartPose.offset(
                                0.0F,
                                0.0F,
                                0.0F
                        )
                );

        stand2.addOrReplaceChild(
                "bracelet",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -3.0F,
                                -8.5F,
                                -3.0F,
                                6.0F,
                                9.0F,
                                6.0F,
                                new CubeDeformation(0.05F)
                        ),
                PartPose.offset(
                        0.0F,
                        -0.1F,
                        0.0F
                )
        );

        stand2.addOrReplaceChild(
                "upper",
                CubeListBuilder.create()
                        .texOffs(4, 16)
                        .addBox(
                                -2.0F,
                                -3.5F,
                                0.0F,
                                2.0F,
                                7.0F,
                                0.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        -3.1F,
                        -3.15F,
                        -3.0F
                )
        );

        stand2.addOrReplaceChild(
                "lower",
                CubeListBuilder.create()
                        .texOffs(12, 16)
                        .addBox(
                                0.0F,
                                -3.5F,
                                0.0F,
                                0.0F,
                                7.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        3.0F,
                        -3.12F,
                        3.1F
                )
        );

        return LayerDefinition.create(
                meshdefinition,
                32,
                32
        );
    }

    public void setOpenProgress(float progress) {

        progress =
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                progress
                        )
                );

        final float START_UPPER_ROTATION = 90.0F;
        final float OPEN_UPPER_ROTATION = -90.0F;

        final float START_LOWER_ROTATION = -90.0F;
        final float OPEN_LOWER_ROTATION = 90.0F;

        float upperRotation =
                (float) Math.toRadians(START_UPPER_ROTATION)
                        * (1.0F - progress)
                        + (float) Math.toRadians(OPEN_UPPER_ROTATION)
                        * progress;

        float lowerRotation =
                (float) Math.toRadians(START_LOWER_ROTATION)
                        * (1.0F - progress)
                        + (float) Math.toRadians(OPEN_LOWER_ROTATION)
                        * progress;

        this.upper.yRot =
                upperRotation;

        this.lower.yRot =
                lowerRotation;
    }
}