package net.hydra.jojomod.client.models.visages;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.world.entity.Mob;

public class VisageBasisModel<T extends Mob> extends PlayerModel<T> {
    private final PartPose bodyDefault = this.body.storePose();
    private final PartPose headDefault = this.head.storePose();
    private final PartPose leftArmDefault = this.leftArm.storePose();
    private final PartPose rightArmDefault = this.rightArm.storePose();

    public VisageBasisModel(ModelPart $$0) {
        super($$0, false);
    }
    public VisageBasisModel(ModelPart $$0, boolean tf) {
        super($$0, tf);
    }
}

