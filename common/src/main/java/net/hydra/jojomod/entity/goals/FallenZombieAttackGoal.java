package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.corpses.FallenZombie;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Zombie;

public class FallenZombieAttackGoal extends MeleeAttackGoal {
    private final FallenZombie zombie;
    private int raiseArmTicks;

    public FallenZombieAttackGoal(FallenZombie $$0, double $$1, boolean $$2) {
        super($$0, $$1, $$2);
        this.zombie = $$0;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        } else {
            this.zombie.setAggressive(false);
        }
    }
}