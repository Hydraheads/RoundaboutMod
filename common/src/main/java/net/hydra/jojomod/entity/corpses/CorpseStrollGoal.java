package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CorpseStrollGoal extends RandomStrollGoal {
    public static final float PROBABILITY = 0.001F;
    protected final float probability;

    public CorpseStrollGoal(PathfinderMob $$0, double $$1) {
        this($$0, $$1, 0.001F);
    }

    public CorpseStrollGoal(PathfinderMob $$0, double $$1, float $$2) {
        super($$0, $$1);
        this.probability = $$2;
    }
    @Override
    public boolean canUse() {
        if (this.mob instanceof FallenMob FM && FM.getMovementTactic() == Tactics.ROAM.id) {
            return super.canUse();
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 $$0 = LandRandomPos.getPos(this.mob, 15, 7);
            return $$0 == null ? super.getPosition() : $$0;
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 10, 7) : super.getPosition();
        }
    }
}
