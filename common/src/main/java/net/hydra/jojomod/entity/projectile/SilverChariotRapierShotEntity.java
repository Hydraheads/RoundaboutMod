package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import java.util.UUID;

public class SilverChariotRapierShotEntity extends AbstractHurtingProjectile implements UnburnableProjectile {
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BOUNCES = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TYPE = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.INT);

    public SilverChariotRapierShotEntity(EntityType<? extends SilverChariotRapierShotEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected SilverChariotRapierShotEntity(EntityType<? extends SilverChariotRapierShotEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    public SilverChariotRapierShotEntity(LivingEntity $$0, double $$1, double $$2, double $$3, Level $$4, byte type) {
        this(ModEntities.SILVER_CHARIOT_RAPIER, $$1, $$2, $$3, $$4);
        this.setOwner($$0);
        this.setRapierShotType(type);
    }

    public final float speed = 3.0F;

    private float damage;

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public static void hurtEntity() {

    }

    public void alignRapier(LivingEntity livingEntity) {

    }

    public LivingEntity standUser;
    public UUID standUserUUID;

    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }

    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            this.standUser = LE;
            if (!this.level().isClientSide()){
                standUserUUID = LE.getUUID();
            }
        }
    }

    public void setUser(LivingEntity user) {
        standUser = user;
        this.getEntityData().set(USER_ID, user.getId());
        if (!this.level().isClientSide()){
            standUserUUID = user.getUUID();
        }
    }

    public int getBounces() {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().get(ROUNDABOUT$BOUNCES);
        }
        return 0;
    }

    public void setBounces(int bounces) {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().set(ROUNDABOUT$BOUNCES, bounces);
        }
    }

    public byte getRapierShotType() {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            return this.getEntityData().get(ROUNDABOUT$TYPE);
        }
        return BASE;
    }

    public void setRapierShotType(byte type) {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            this.getEntityData().set(ROUNDABOUT$TYPE, type);
        }
    }

    public byte getSkin() {
        return this.getEntityData().get(SKIN);
    }
    public void setSkin(byte skin) {
        this.getEntityData().set(SKIN, skin);
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        // super.onHitBlock($$0);
        if (!this.level().isClientSide()) {
            if (this.getRapierShotType() == PLATFORM) {
                this.createPlatform($$0.getLocation());
                this.discard();
            } else if (this.getRapierShotType() == BASE && this.getBounces() > 0) {
                this.setBounces(this.getBounces() - 1);

                Vec3 velocity = this.getDeltaMovement();
                Direction hitDir = $$0.getDirection();
                Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

                Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));
                // reflected = reflected.scale(1.0);

                this.setDeltaMovement(reflected);

                Vec3 hitLoc = $$0.getLocation();
                Vec3 pushOut = normal.scale(0.2);
                this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);
            }
        }
    }

    private void createPlatform(Vec3 position) {
        SilverChariotRapierPlatformEntity platform = new SilverChariotRapierPlatformEntity(
                ModEntities.SILVER_CHARIOT_RAPIER_PLATFORM,
                this.level()
        );

        if (platform == null) {
            return;
        }
    }

    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y; $$2 = vecMagic.x;
        }
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();

        Vec3 force = new Vec3($$6, $$7, $$8)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.0172275 * (double)$$4),
                        this.random.triangle(0.0, 0.0172275 * (double)$$4),
                        this.random.triangle(0.0, 0.0172275 * (double)$$4)
                )
                .scale((double)$$3);
        forcedDeltaMovement = force;
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        // super.onHitEntity($$0);

        Entity $$1 = $$0.getEntity();
        if (!MainUtil.isMobOrItsMounts($$1, getUser())) {
            if ($$1 instanceof LivingEntity LE) {
                this.rapierHit(LE);
            }
            this.discard();
        }

        /*

        Entity $$1 = $$0.getEntity();

        if ($$1.equals(this.getOwner())) {
            return;
        }

        if (this.getOwner() instanceof TamableAnimal TT && TT.getOwner() != null) {
            if ($$1 instanceof TamableAnimal TA && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())) {
                return;
            }
            if ($$1.is(TT.getOwner())) {
                return;
            }
        }

        float degrees = MainUtil.getLookAtEntityYaw(this, $$1);
        float force = 2.0F;

        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.STAND);
        if (this.getOwner() != null) {
            $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.STAND, this, this.getOwner());
        }

        float damage = 1.0f;

         */
    }

    public void rapierHit(LivingEntity livingEntity) {
        if (!this.level().isClientSide()) {
            LivingEntity user = this.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersSilverChariot PSC) {
                PSC.addEXP(2);
                this.level().playSound(null, this.blockPosition(), ModSounds.RATT_DART_IMPACT_EVENT,
                        SoundSource.PLAYERS, 2F, 1F);
                if (livingEntity != null) {
                    this.getEntity(livingEntity, PSC);
                }
            }
        }
    }

    public static void damageEntity(Entity gotten, Entity proj, LivingEntity user, PowersSilverChariot PSC) {
        if (PowerTypes.isInADifferentExistence(gotten,proj)){
            return;
        }
        if (!(user instanceof Player) && !(user instanceof Monster)){
            if (!(gotten instanceof Monster)){
                if (!(user instanceof Mob mb && mb.getTarget() !=null && mb.getTarget().is(gotten))){
                    return;
                }
            }
        }
        if (gotten instanceof TamableAnimal TA){
            if (user instanceof TamableAnimal TT && TT.getOwner() != null
                    && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                return;
            }
        }
        float dmg = 1;
        float strength = 0.85F;
        // strength*=multi;

        dmg = PSC.getRapierShotDamage(gotten);
        strength *= 2F;

        if (gotten.hurt(ModDamageTypes.of(gotten.level(), ModDamageTypes.STAND, user), dmg)) {
            if (gotten instanceof LivingEntity le) {
                PSC.addEXP(2, le);
            }
        } else if (gotten instanceof LivingEntity le && le.isBlocking()) {
            int breakShield = 160;
            MainUtil.knockShield(le, breakShield);
        }

        float degrees = MainUtil.getLookAtEntityYaw(proj, gotten);
        MainUtil.takeKnockbackWithY(gotten, strength,
                Mth.sin(degrees * ((float) Math.PI / 180)),
                Mth.sin(-17 * ((float) Math.PI / 180)),
                -Mth.cos(degrees * ((float) Math.PI / 180)));
    }

    public void getEntity(Entity gotten, PowersSilverChariot PSC) {
        if (gotten != null && getUser() != null && !MainUtil.isMobOrItsMounts(gotten, getUser())) {
            damageEntity(gotten, this, this.standUser, PSC);
        }
    }

    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }

    public static final byte
                BASE = (byte) 1,
                PLATFORM = (byte) 2;

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(USER_ID)) {
            super.defineSynchedData();
            this.entityData.define(USER_ID, -1);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    private int life = 0;

    public Vec3 forcedDeltaMovement;

    @Override
    public void tick() {
        /*
        boolean client = this.level().isClientSide();
        if (!client) {
            if (this.getStandUser() != null){
                if (MainUtil.cheapDistanceTo2(this.getX(),this.getZ(),this.standUser.getX(),this.standUser.getZ()) > 80
                        || !this.getStandUser().isAlive() || this.getStandUser().isRemoved()){
                    this.discard();
                }
            } else {
                this.discard();
            }
        }

        LivingEntity le = this.getStandUser();
        if (le != null) {
            if (((StandUser) this.getStandUser()).roundabout$getStandPowers() instanceof PowersSilverChariot PSC) {
                this.setDeltaMovement(Vec3.ZERO);
            } else {
                if (!client) {
                    this.discard();
                    return;
                }
            }
        }
        */

        // this.tickRotateFromVelocity();

        super.tick();

        this.tickRotateFromVelocity();

        if (this.getRapierShotType() == BASE) {
            if (this.tickCount > 600) {
                this.discard();
            }
        } else if (this.getRapierShotType() == PLATFORM) {
            if (this.tickCount > 1200) {
                this.discard();
            }
        }
    }

    public void initRotateFromVelocity(float velMagnitude) {
        // "origin" will be used instead of the user for when Silver Chariot shoots
        // it's rapier towards an enemy while in control mode.
        LivingEntity origin = this.standUser;

        Vec3 dir = origin.getLookAngle();

        Vec3 vel = dir.normalize().scale(velMagnitude);

        this.setDeltaMovement(vel);

        float newYaw   = (float) (Mth.atan2(vel.x, vel.z) * (180F / Math.PI));
        float newPitch = -(float) (Mth.atan2(vel.y, Math.sqrt(vel.x * vel.x + vel.z * vel.z)) * (180F / Math.PI));

        this.setYRot(newYaw);
        this.setXRot(newPitch);

        this.yRotO = newYaw;
        this.xRotO = newPitch;
    }

    private void tickRotateFromVelocity() {
        Vec3 vel = this.getDeltaMovement();

        if (vel.lengthSqr() == 1.0E-7D) {
            return;
        }

        float newYaw   = (float) (Mth.atan2(vel.x, vel.z) * (180F / Math.PI));
        float newPitch = -(float) (Mth.atan2(vel.y, Math.sqrt(vel.x * vel.x + vel.z * vel.z)) * (180F / Math.PI));

        this.setYRot(newYaw);
        this.setXRot(newPitch);

        this.yRotO = newYaw;
        this.xRotO = newPitch;
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
