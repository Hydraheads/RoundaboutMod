package net.hydra.jojomod.client.models.visages;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;

public class ZombieVisageBasisModel<T extends Mob> extends PlayerModel<T> {
    public ZombieVisageBasisModel(ModelPart $$0, boolean tf) {
        super($$0, tf);
    }
    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        super.setupAnim($$0, $$1, $$2, $$3, $$4, $$5);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive($$0), this.attackTime, $$3);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
    }

    public boolean isAggressive(T $$0) {
        return $$0.isAggressive();
    }
}

