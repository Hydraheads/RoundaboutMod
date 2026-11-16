package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.mobs.Butterfly;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GEButterflyEntity extends Butterfly implements GELifeform {
    private boolean isReflectingDamage = false;
    @Nullable
    private UUID creatorUUID;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;
    private Vec3 targetPos = null;

    public GEButterflyEntity(EntityType<? extends Butterfly> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 60, false);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    @Override
    @Nullable
    public UUID getCreatorUUID() {
        return this.creatorUUID;
    }

    @Override
    public void setCreatorUUID(@Nullable UUID uuid) {
        this.creatorUUID = uuid;
    }

    @Override
    @Nullable
    public LivingEntity getCreator() {
        if (this.creatorUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.creatorUUID);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    @Override
    public void setCreator(@Nullable LivingEntity creator) {
        this.creatorUUID = (creator != null) ? creator.getUUID() : null;
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
                this.die(this.damageSources().generic());
            }
        }
    }

    private void gotoTarget() {
        if (this.horizontalCollision || this.verticalCollision) {
            this.die(this.damageSources().generic());
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
                this.die(this.damageSources().generic());
            }
        } else {
            // lobotomizes the bee if there's no target
            if (this.tickCount % 10 == 0) {
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // lobotomize the bee so that it won't try to fight the target pathfinding
        this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.creatorUUID != null) {
            tag.putUUID("CreatorUUID", this.creatorUUID);
        }
        tag.putInt("GELifespan", this.lifespan);
        if (!this.sourceItem.isEmpty()) {
            tag.put("GESourceItem", this.sourceItem.save(new CompoundTag()));
        }
        if (this.targetPos != null) {
            tag.putDouble("TargetX", this.targetPos.x);
            tag.putDouble("TargetY", this.targetPos.y);
            tag.putDouble("TargetZ", this.targetPos.z);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("CreatorUUID")) {
            this.creatorUUID = tag.getUUID("CreatorUUID");
        }
        if (tag.contains("GELifespan")) {
            this.lifespan = tag.getInt("GELifespan");
        }
        if (tag.contains("GESourceItem")) {
            this.sourceItem = ItemStack.of(tag.getCompound("GESourceItem"));
        }
        if (tag.contains("TargetX") && tag.contains("TargetY") && tag.contains("TargetZ")) {
            this.targetPos = new Vec3(tag.getDouble("TargetX"), tag.getDouble("TargetY"), tag.getDouble("TargetZ"));
        }
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public ItemStack getSourceItem() { return sourceItem; }
    @Override
    public void setSourceItem(ItemStack stack) { this.sourceItem = stack; }
}