package net.hydra.jojomod.entity.goals;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AvoidCatHeadGoal<T extends LivingEntity> extends Goal {
    protected final PathfinderMob mob;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    @Nullable
    protected T toAvoid;
    protected final float maxDist;
    @Nullable
    protected Path path;
    protected final PathNavigation pathNav;
    protected final Class<T> avoidClass;
    protected final Predicate<LivingEntity> avoidPredicate;
    protected final Predicate<LivingEntity> predicateOnAvoidEntity;
    private final TargetingConditions avoidEntityTargeting;

    public AvoidCatHeadGoal(PathfinderMob pathfinderMob, Class<T> class_, float f, double d, double e) {
        this(pathfinderMob, class_, livingEntity -> true, f, d, e, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
    }

    public AvoidCatHeadGoal(PathfinderMob pathfinderMob, Class<T> class_, Predicate<LivingEntity> predicate, float f, double d, double e, Predicate<LivingEntity> predicate2) {
        this.mob = pathfinderMob;
        this.avoidClass = class_;
        this.avoidPredicate = predicate;
        this.maxDist = f;
        this.walkSpeedModifier = d;
        this.sprintSpeedModifier = e;
        this.predicateOnAvoidEntity = predicate2;
        this.pathNav = pathfinderMob.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.avoidEntityTargeting = TargetingConditions.forCombat().range(f).selector(predicate2.and(predicate));
    }

    public AvoidCatHeadGoal(PathfinderMob pathfinderMob, Class<T> class_, float f, double d, double e, Predicate<LivingEntity> predicate) {
        this(pathfinderMob, class_, livingEntity -> true, f, d, e, predicate);
    }

    public boolean canUse() {
        T LE = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate((double)this.maxDist, (double)3.0F, (double)this.maxDist), ($$0x) -> true), this.avoidEntityTargeting, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        if (LE instanceof BaseMinion bm && bm.getHeadItem() != null && bm.getHeadItem().is(ModItems.CAT_REMAINS)) {
            this.toAvoid = LE;
            Vec3 $$0 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
            if ($$0 == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr($$0.x, $$0.y, $$0.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath($$0.x, $$0.y, $$0.z, 0);
                return this.path != null;
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    public void start() {
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
    }

    public void stop() {
        this.toAvoid = null;
    }

    public void tick() {
        if (this.mob.distanceToSqr(this.toAvoid) < (double)49.0F) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }

    }
}
