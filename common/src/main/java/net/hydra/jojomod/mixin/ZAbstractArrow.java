package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.TimeMovingProjectile;
import net.hydra.jojomod.entity.projectile.HarpoonEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractArrow.class)
public abstract class ZAbstractArrow extends Entity implements IAbstractArrowAccess {
    /**Makes the arrows, knives, and tridents thrown in timestop */
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN =
            SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private int roundabout$superThrowTicks = -1;

    @Unique
    @Override
    public int roundabout$getSuperThrowTicks() {
        return roundabout$superThrowTicks;
    }
    @Unique
    @Override
    public boolean roundabout$getSuperThrow() {
        return this.getEntityData().get(ROUNDABOUT$SUPER_THROWN);
    }
    @Unique
    @Override
    public void roundabout$starThrowInit(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        roundabout$superThrowTicks = 50;
    }
    @Unique
    @Override
    public void roundabout$starThrowInit2(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        roundabout$superThrowTicks = 20;
    }

    @Shadow
    @Nullable
    private BlockState lastState;

    @Shadow
    private void resetPiercedEntities() {
    }

    @Override
    public void roundabout$resetPiercedEntities(){
        resetPiercedEntities();
    }
    @Override
    public void roundabout$setLastState(BlockState last){
        lastState = last;
    }

    @Unique
    @Override
    public void roundabout$cancelSuperThrow(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
    }
    @Unique
    @Override
    public void roundabout$setSuperThrowTicks(int sTHrow){
        roundabout$superThrowTicks = sTHrow;
    }
    @Shadow
    public boolean isNoPhysics(){
        return false;
    }
    @Shadow
    protected boolean canHitEntity(Entity $$0x){
        return false;
    }

    @Shadow
    protected boolean tryPickup(Player $$0){return false;}

    public ZAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    protected boolean inGround;
    @Shadow
    public int shakeTime;

    @Override
    public ItemStack roundabout$GetPickupItem(){
        return this.getPickupItem();
    }
    @Override
    public boolean roundaboutGetInGround(){
        return this.inGround;
    }

    @Override
    public void roundaboutSetInGround(boolean inGround){
        this.inGround = inGround;
    }

    public byte roundaboutGetPierceLevel(){
        return this.getPierceLevel();
    }
    @Shadow
    public byte getPierceLevel(){
        return 0;
    }
    @Override
    @Nullable
    public EntityHitResult roundabout$FindHitEntity(Vec3 $$0, Vec3 $$1){
        return this.findHitEntity($$0,$$1);
    }
    @Shadow
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1){
        return null;
    }

    @Shadow protected abstract ItemStack getPickupItem();

    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(ROUNDABOUT$SUPER_THROWN, false);
    }

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        Entity $$1 = $$0.getEntity();
        if ($$1 instanceof LivingEntity LE){
            StandPowers entityPowers = ((StandUser)LE).roundabout$getStandPowers();
            if (!this.level().isClientSide && entityPowers instanceof PowersD4C d4cPowers)
            {
                if (d4cPowers.meltDodgeTicks >= 0)
                {
                    d4cPowers.meltDodge((AbstractArrow)(Object)this);
                    ci.cancel();
                }
            }

            if (entityPowers.dealWithProjectile(this)){
                ci.cancel();
                this.discard();
            }
        }
    }
    @Unique
    private Vec3 roundabout$delta = Vec3.ZERO;
    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$SetPosForTS(CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this)) && ((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            super.tick();
            TimeMovingProjectile.tick((AbstractArrow) (Object) this);
            this.checkInsideBlocks();
            ci.cancel();
        } else {
            roundabout$delta = this.getDeltaMovement();

        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"),cancellable = true)
    private void roundabout$SuperThrow(CallbackInfo ci) {
        if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {
            if (!this.isNoPhysics() && !this.inGround) {
                this.setDeltaMovement(roundabout$delta);
            } else if (this.inGround){
                this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
            }
        }
        if (!this.level().isClientSide) {
            if (!this.isNoPhysics()) {
                if (roundabout$superThrowTicks > -1) {
                    roundabout$superThrowTicks--;
                    if (roundabout$superThrowTicks <= -1) {
                        this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
                    } else {
                        if (this.tickCount % 4 == 0) {
                            ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    this.getX(), this.getY(), this.getZ(),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }
            } else {
                if (roundabout$superThrowTicks > -1) {
                    this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
                }
            }
        }
    }
    @Inject(method = "playerTouch", at = @At(value = "HEAD"),cancellable = true)
    private void roundaboutS$hakeTime(Player $$0, CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this))) {
            if (!this.level().isClientSide && (this.inGround || this.isNoPhysics())) {
                if (this.tryPickup($$0)) {
                    $$0.take(this, 1);
                    this.discard();
                }
            }
            ci.cancel();
        }
    }
}
