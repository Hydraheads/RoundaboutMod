package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.mobs.Butterfly;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GEButterflyEntity extends Butterfly implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;
    private Vec3 targetPos = null;

    public GEButterflyEntity(EntityType<? extends Butterfly> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 60, false);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    @Override
    public void setCreator(@Nullable LivingEntity creator) {
        this.creator = creator;
        if (creator != null && !this.level().isClientSide()) {
            // 100 block raycast
            Vec3 eyePos = creator.getEyePosition(1.0F);
            Vec3 lookVec = creator.getLookAngle();
            Vec3 reachVec = eyePos.add(lookVec.x * 100.0D, lookVec.y * 100.0D, lookVec.z * 100.0D);

            BlockHitResult result = this.level().clip(new ClipContext(
                    eyePos, reachVec,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    creator
            ));

            if (result.getType() != HitResult.Type.MISS) {
                this.targetPos = result.getLocation();
            } else {
                this.targetPos = reachVec;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                gotoTarget();
            } else {
                revertToItem();
            }
        }
    }

    private void gotoTarget() {
        if (this.horizontalCollision || this.verticalCollision) {
            revertToItem();
            return;
        }

        if (this.targetPos != null) {
            // pathfinds to the targeted cursor location
            if (this.tickCount % 3 == 0) {
                this.getNavigation().moveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 1.0D);
            }

            Path path = this.getNavigation().getPath();
            if (path != null && !path.isDone()) {
                //normal bee flight was too slow, so i'm hijacking the pathfinding list and moving the bee really fast manually
                Vec3 nextWaypoint = path.getNextEntityPos(this);
                Vec3 flightDir = nextWaypoint.subtract(this.position()).normalize();
                this.setDeltaMovement(flightDir.scale(0.45D));
            } else {
                Vec3 flightDir = this.targetPos.subtract(this.position()).normalize();
                this.setDeltaMovement(flightDir.scale(0.45D));
            }

            this.lookAt(EntityAnchorArgument.Anchor.EYES, this.targetPos);

            // reverts into an item once it reaches the cursor target position
            if (this.position().distanceToSqr(this.targetPos) <= 1.44D) {
                revertToItem();
            }
        } else {
            // lobotomizes the bee if there's no target
            if (this.tickCount % 10 == 0) {
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    private void revertToItem() {
        if (!this.level().isClientSide()) {
            if (!this.sourceItem.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(
                        this.level(),
                        this.getX(), this.getY(), this.getZ(),
                        this.sourceItem.copy()
                );
                this.level().addFreshEntity(itemEntity);
            }
            this.discard();
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // lobotomize the bee so that it won't try to fight the target pathfinding
        this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    @Nullable
    public LivingEntity getCreator() { return creator; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public ItemStack getSourceItem() { return sourceItem; }
    @Override
    public void setSourceItem(ItemStack stack) { this.sourceItem = stack; }
}