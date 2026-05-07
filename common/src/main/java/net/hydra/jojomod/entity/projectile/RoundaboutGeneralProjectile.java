package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.UnburnableProjectile;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class RoundaboutGeneralProjectile extends AbstractHurtingProjectile implements UnburnableProjectile {
    public RoundaboutGeneralProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(RoundaboutGeneralProjectile.class, EntityDataSerializers.INT);

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


    public int lifespan = 0;
    public int getLifeSpan(){
        return lifespan;
    }
    public int getMaxLifeSpan(){
        return 200;
    }
    public boolean tickLifeSpan(){
        lifespan++;
        if (lifespan >= getMaxLifeSpan()){
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    @Override
    protected float getInertia() {
        return 1F;
    }
    @Override
    public boolean isControlledByLocalInstance() {
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean isPickable() {
        return false;
    }


    public void setOldPosAndRot2() {
        if (storeVec != null) {
            double $$0 = storeVec.x();
            double $$1 = storeVec.y();
            double $$2 = storeVec.z();
            this.xo = $$0;
            this.yo = $$1;
            this.zo = $$2;
            this.xOld = $$0;
            this.yOld = $$1;
            this.zOld = $$2;
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    public double renderRotation = 0;
    public double lastRenderRotation = 0;

    public void setRenderRotation(double rotation){
        lastRenderRotation = renderRotation;
        renderRotation = rotation;
    }
    public Vec3 storeVec;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public boolean initialized = false;


    public boolean isBundle = false;
    public int saneAgeTicking;
    public int inWaterTicks=0;
    public void tickWater(){
        inWaterTicks++;
        if (inWaterTicks > 40){
            this.discard();
        }
    }
    public void tick() {
        boolean client = this.level().isClientSide();
        if (!client){
            if (tickLifeSpan()){
                return;
            }

            if (isEffectivelyInWater()){
                tickWater();
            }
            if (this.getStandUser() != null){
                if (MainUtil.cheapDistanceTo2(this.getX(),this.getZ(),this.standUser.getX(),this.standUser.getZ()) > 80
                        || !this.getStandUser().isAlive() || this.getStandUser().isRemoved()){
                    this.discard();
                }
            } else {
                this.discard();
            }
        }

        if (forcedDeltaMovement != null){
            setDeltaMovement(forcedDeltaMovement);
        }
        super.tick();
        if (!client){
            if (isEffectivelyInWater()){
                tickWater();
            }
            if (this.getDeltaMovement().equals(Vec3.ZERO)){
                this.discard();
            }
        }
    }


    public double getRandomY(double $$0) {
        return this.getY((2.0 * this.random.nextDouble() - 1.0) * $$0);
    }


    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
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
        Vec3 force = new Vec3($$6, $$7, $$8)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.0172275 * (double)$$4),
                        this.random.triangle(0.0, 0.0172275 * (double)$$4),
                        this.random.triangle(0.0, 0.0172275 * (double)$$4)
                )
                .scale((double)$$3);
        forcedDeltaMovement = force;
        Vec3 $$9 = $$0.getDeltaMovement();
    }
    public Vec3 forcedDeltaMovement = null;


    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }



    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(USER_ID)) {
            super.defineSynchedData();
            this.entityData.define(USER_ID, -1);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
        if (standUser != null) {
            $$0.putUUID("standUser", standUser.getUUID());
            $$0.putInt("lifeSpan", lifespan);
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("standUser")) {
            standUserUUID = $$0.getUUID("standUser");
            lifespan = $$0.getInt("lifeSpan");
            if (!this.level().isClientSide()) {
                Entity ett = ((ServerLevel) this.level()).getEntity(standUserUUID);
                if (ett instanceof LivingEntity lett) {
                    standUser = lett;
                    this.setUserID(lett.getId());
                }
            }
        }
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
    protected boolean shouldBurn() {
        return false;
    }
}

