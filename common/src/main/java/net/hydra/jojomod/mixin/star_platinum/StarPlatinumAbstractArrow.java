package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.access.ISuperThrownAbstractArrow;
import net.hydra.jojomod.entity.TimeMovingProjectile;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class StarPlatinumAbstractArrow extends Entity implements ISuperThrownAbstractArrow {

    /**Makes super thrown abstract arrows have super throw physics */
    private static final EntityDataAccessor<Boolean> SBA$SUPER_THROWN =
            SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private int sba$superThrowTicks = -1;

    @Unique
    @Override
    public int sba$getSuperThrowTicks() {
        return sba$superThrowTicks;
    }
    @Unique
    @Override
    public boolean sba$getSuperThrow() {
        return this.getEntityData().get(SBA$SUPER_THROWN);
    }


    @Unique
    @Override
    public void sba$cancelSuperThrow(){
        this.entityData.set(SBA$SUPER_THROWN, false);
    }
    @Unique
    @Override
    public void sba$setSuperThrowTicks(int sTHrow){
        sba$superThrowTicks = sTHrow;
    }
    @Unique
    @Override
    public void sba$starThrowInit(){
        this.entityData.set(SBA$SUPER_THROWN, true);
        sba$superThrowTicks = 50;
    }
    @Unique
    @Override
    public void sba$starThrowInit2(){
        this.entityData.set(SBA$SUPER_THROWN, true);
        sba$superThrowTicks = 20;
    }
    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"),cancellable = true)
    private void sba$defineSynchedData(CallbackInfo ci) {
        if (!((AbstractArrow)(Object)this).getEntityData().hasItem(SBA$SUPER_THROWN)) {
            this.entityData.define(SBA$SUPER_THROWN, false);
        }
    }
    @Inject(method = "tick", at = @At(value = "TAIL"),cancellable = true)
    private void sba$SuperThrow(CallbackInfo ci) {
        if (this.getEntityData().get(SBA$SUPER_THROWN)) {
            if (!this.isNoPhysics() && !this.inGround) {
                this.setDeltaMovement(sba$delta);
            } else if (this.inGround){
                this.entityData.set(SBA$SUPER_THROWN, false);
            }
        }
        if (!this.level().isClientSide) {
            if (!this.isNoPhysics()) {
                if (sba$superThrowTicks > -1) {
                    sba$superThrowTicks--;
                    if (sba$superThrowTicks <= -1) {
                        this.entityData.set(SBA$SUPER_THROWN, false);
                    } else {
                        if (this.tickCount % 4 == 0) {
                            ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    this.getX(), this.getY(), this.getZ(),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }
            } else {
                if (sba$superThrowTicks > -1) {
                    this.entityData.set(SBA$SUPER_THROWN, false);
                }
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void sba$SetPosForTS(CallbackInfo ci) {
        if (!(((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this)) && ((IProjectileAccess) this).sba$getSBAIsTimeStopCreated())) {
            sba$delta = this.getDeltaMovement();
        }
    }
    @Unique
    private Vec3 sba$delta = Vec3.ZERO;
    @Unique
    @Override
    public void sba$setDelta(Vec3 delta){
        sba$delta = delta;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public StarPlatinumAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public boolean isNoPhysics(){
        return false;
    }


    @Shadow
    protected boolean inGround;
}
