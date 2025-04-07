package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LifeTrackerEntity extends LivingEntity {
    public LifeTrackerEntity(EntityType<LifeTrackerEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if (isEffectivelyInWater()) {
                this.discard();
                return;
            }
            if (user != null) {
                if (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), user.getX(), user.getZ()) > 80
                        || !user.isAlive() || user.isRemoved() || !(((StandUser)user).roundabout$getStandPowers()
                instanceof PowersMagiciansRed)) {
                    this.discard();
                    return;
                }

                if (((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                    if (this.tickCount %4 == 0) {
                        ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                                this.getY() + (this.getBbHeight() * 0.5), this.getZ(),
                                1,
                                0.3, 0.3, 0.3,
                                0.005);
                    }
                }
            } else {
                this.discard();
                return;
            }
        } else {
            if (user != null) {
                AABB $$0 = this.getBoundingBox().inflate(15, 15, 15);
                List<? extends LivingEntity> $$1 = this.level().getNearbyEntities(LivingEntity.class, MainUtil.plsWorkTargetting, this, $$0);
                for (LivingEntity $$3 : $$1) {
                    if (!$$3.is(user) && !($$3 instanceof StandEntity) && !($$3 instanceof ArmorStand) && !($$3 instanceof FallenMob)) {
                        ((StandUser) $$3).roundabout$setDetectTicks(2);
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public void doPush(Entity $$0) {
            if (!$$0.isPassengerOfSameVehicle(this)) {
                if (!$$0.noPhysics && !this.noPhysics) {
                    double $$1 = this.getX() - $$0.getX();
                    double $$2 = this.getZ() - $$0.getZ();
                    double $$3 = Mth.absMax($$1, $$2);
                    if ($$3 >= 0.009999999776482582) {
                        $$3 = Math.sqrt($$3);
                        $$1 /= $$3;
                        $$2 /= $$3;
                        double $$4 = 1.0 / $$3;
                        if ($$4 > 1.0) {
                            $$4 = 1.0;
                        }

                        $$1 *= $$4;
                        $$2 *= $$4;
                        $$1 *= 0.25000000074505806;
                        $$2 *= 0.25000000074505806;
                        if (!this.isVehicle() && this.isPushable()) {
                            this.push($$1, 0.0, $$2);
                        }
                    }

                }
            }
    }
    @Override
    public void push(Entity $$0) {
    }
    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
    }
    public void shoot(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = (new Vec3($$0, $$1, $$2)).normalize().add(this.random.triangle(0.0, 0.0172275 * (double)$$4), this.random.triangle(0.0, 0.0172275 * (double)$$4), this.random.triangle(0.0, 0.0172275 * (double)$$4)).scale((double)$$3);
        this.setDeltaMovement($$5);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }


    /**Like UserID and FollowingID, but for the actual entity data.*/
    @Nullable
    private LivingEntity User;

    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(LifeTrackerEntity.class,
            EntityDataSerializers.INT);

    /**
     * Sets stand User, the mob who "owns" the stand
     */
    public LivingEntity getUser() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(USER_ID));
        } else {
            return this.User;
        }
    }
    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        return false;
    }
    public void setUser(LivingEntity StandSet){
        this.User = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(USER_ID, standSetId);
    }


    @Override
    public boolean isPickable() {
        return false;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USER_ID, -1);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot $$0) {
       return ItemStack.EMPTY;
    }
    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }


}
