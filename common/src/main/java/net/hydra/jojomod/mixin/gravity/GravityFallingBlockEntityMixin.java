package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFFloatingBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class GravityFallingBlockEntityMixin extends Entity implements JJFFloatingBlocks.SyncedFloatingBlock {
    @Unique
    private static final EntityDataAccessor<Boolean> JJF_FLOATING =
            SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void jjf$defineFloating(CallbackInfo ci) {
        ((FallingBlockEntity) (Object) this).getEntityData().define(JJF_FLOATING, false);
    }

    @Override
    public boolean jjf$isFloating() {
        return ((FallingBlockEntity) (Object) this).getEntityData().get(JJF_FLOATING);
    }

    protected GravityFallingBlockEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean canBeCollidedWith() {
        return JJFFloatingBlocks.isFloating((FallingBlockEntity) (Object) this);
    }

    @Unique
    private static final int JJF_FLOAT_DURATION = 600 ;

    @Unique
    private java.util.UUID jjf$floatOwner;

    @Override
    public java.util.UUID jjf$getFloatOwner() {
        return jjf$floatOwner;
    }

    @Override
    public void jjf$setFloatOwner(java.util.UUID owner) {
        jjf$floatOwner = owner;
    }

    @Unique
    private int jjf$floatTicks;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void jjf$saveFloatAge(CompoundTag tag, CallbackInfo ci) {
        if (jjf$floatOwner != null) {
            tag.putUUID("JJFFloatOwner", jjf$floatOwner);
        }
        if (jjf$floatTicks > 0) {
            tag.putInt("JJFFloatTicks", jjf$floatTicks);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void jjf$loadFloatAge(CompoundTag tag, CallbackInfo ci) {
        jjf$floatOwner = tag.hasUUID("JJFFloatOwner") ? tag.getUUID("JJFFloatOwner") : null;
        jjf$floatTicks = tag.getInt("JJFFloatTicks");
    }

    @Unique
    private int jjf$projectileCooldown;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void jjf$floatBlock(CallbackInfo ci) {
        FallingBlockEntity block = (FallingBlockEntity) (Object) this;
        boolean real = block.getTags().contains(JJFFloatingBlocks.REAL_TAG);
        boolean fake = block.getTags().contains(JJFFloatingBlocks.FAKE_TAG);
        if (!block.level().isClientSide()) {
            block.getEntityData().set(JJF_FLOATING, real || fake);
        }
        if (block.level().isClientSide()) {
            if (jjf$isFloating()) {
                jjf$movePlatform(block);
                ci.cancel();
            }
            return;
        }
        if (!real && !fake) {
            return;
        }
        jjf$floatTicks++;
        if (jjf$floatTicks >= JJF_FLOAT_DURATION) {
            if (fake) {
                block.discard();
            } else {
                block.removeTag(JJFFloatingBlocks.REAL_TAG);
                block.getEntityData().set(JJF_FLOATING, false);
                block.setNoGravity(false);
            }
            ci.cancel();
            return;
        }

        if (jjf$projectileCooldown > 0) {
            jjf$projectileCooldown--;
        } else {
            for (Projectile projectile : block.level().getEntitiesOfClass(Projectile.class,
                    block.getBoundingBox().inflate(0.35D),
                    p -> p.isAlive() && p.getDeltaMovement().lengthSqr() > 0.0025D)) {
                Vec3 incoming = projectile.getDeltaMovement();
                if (JJFFloatingBlocks.knockBack(block, incoming, 0.35D + Math.min(0.8D, incoming.length() * 0.55D))) {
                    jjf$projectileCooldown = 5;
                    break;
                }
            }
        }
        block.setNoGravity(true);
        jjf$movePlatform(block);
        block.hasImpulse = true;
        block.hurtMarked = true;
        ci.cancel();
    }

    @Unique
    private void jjf$movePlatform(FallingBlockEntity block) {
        AABB before = block.getBoundingBox();
        Vec3 start = block.position();
        Vec3 velocity = block.getDeltaMovement();
        Vec3 next = new Vec3(velocity.x * 0.992D,
                Math.min(0.20D, velocity.y * 0.992D + 0.001D),
                velocity.z * 0.992D);
        block.setDeltaMovement(next);
        block.move(MoverType.SELF, next);
        JJFFloatingBlocks.carryRiders(block, before, block.position().subtract(start));
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        ((IEntityAndData)this).roundabout$tickQVec();
    }
}
