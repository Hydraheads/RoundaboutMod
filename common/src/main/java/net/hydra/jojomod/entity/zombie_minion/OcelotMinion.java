package net.hydra.jojomod.entity.zombie_minion;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class OcelotMinion extends BaseMinion{
    public OcelotMinion(EntityType<? extends OcelotMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.36).add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ATTACK_DAMAGE, 7).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
