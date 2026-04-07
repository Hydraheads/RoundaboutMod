package net.hydra.jojomod.entity.zombie_minion;

import net.hydra.jojomod.entity.Zombiefish;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.level.Level;

public class VillagerMinion extends BaseMinion{
    public VillagerMinion(EntityType<? extends VillagerMinion> $$0, Level $$1) {
        super($$0, $$1);
    }
    public VillagerMinion.IllagerArmPose getArmPose() {
        if (this.isAggressive()) {
            return VillagerMinion.IllagerArmPose.ATTACKING;
        } else {
            return VillagerMinion.IllagerArmPose.CROSSED;
        }
    }
    public static enum IllagerArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL;
    }
}
