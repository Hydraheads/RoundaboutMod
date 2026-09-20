package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BoneProjectileEntity extends AbstractArrow {

    private Vec3 startPos;
    private boolean initialized = false;
    private static final double GRAVITY_DISTANCE_SQR = 25.0 * 25.0;

    public BoneProjectileEntity(EntityType<? extends BoneProjectileEntity> type, Level level) {
        super(type, level);
    }

    public BoneProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BONE_PROJECTILE, shooter, level);
    }

    public BoneProjectileEntity(Level level, double x, double y, double z) {
        super(ModEntities.BONE_PROJECTILE, x, y, z, level);
    }

    @Override
    public void tick() {
        if (!this.initialized) {
            this.startPos = this.position();
            this.initialized = true;
        }

        if (this.isNoGravity() && this.startPos != null && this.position().distanceToSqr(this.startPos) >= GRAVITY_DISTANCE_SQR) {
            this.setNoGravity(false);
        }

        // failsafe just to make sure it's deleted after 10 seconds
        if (!this.level().isClientSide() && this.tickCount >= 100) {
            this.discard();
            return;
        }

        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Shatters upon hitting a block without sticking into it
        shatter(result.getLocation());
    }

    private void shatter(Vec3 pos) {
        if (!this.level().isClientSide()) {
            // remove the entity
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity target) {
            // Stand damage
            DamageHandler.StandDamageEntity(target, 1.5F, this.getOwner() != null ? this.getOwner() : this);
            this.playSound(SoundEvents.SKELETON_HURT, 1.0F, 1.2F);
            this.discard(); // Bone shatters on impact
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Initialized", this.initialized);
        if (this.startPos != null) {
            tag.putDouble("StartX", this.startPos.x);
            tag.putDouble("StartY", this.startPos.y);
            tag.putDouble("StartZ", this.startPos.z);
        }
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.initialized = tag.getBoolean("Initialized");
        if (tag.contains("StartX")) {
            this.startPos = new Vec3(tag.getDouble("StartX"), tag.getDouble("StartY"), tag.getDouble("StartZ"));
        }
    }
}