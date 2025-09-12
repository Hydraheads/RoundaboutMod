package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.FireProjectile;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
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

public class StandFireballEntity extends AbstractHurtingProjectile implements UnburnableProjectile, FireProjectile {
    public StandFireballEntity(EntityType<? extends StandFireballEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected StandFireballEntity(EntityType<? extends StandFireballEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StandFireballEntity.class, EntityDataSerializers.INT);

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
        /**
         if (this.getStandUser() != null && ((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
         if (this.getCrossNumber() > 0) {
         return true;
         }
         }
         **/
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

    protected StandFireballEntity(EntityType<StandFireballEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public StandFireballEntity(LivingEntity $$1, Level $$2) {
        this(ModEntities.STAND_FIREBALL, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

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
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
            //this is where ankh go boom boom
            radialExplosion(null);

            if (!this.level().isClientSide()) {
                LivingEntity user = this.getStandUser();
                if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                    BlockPos pos = $$0.getBlockPos().relative($$0.getDirection());
                    PMR.createStandFire2(pos);
                }
            }
            this.discard();
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
    protected void onHitEntity(EntityHitResult $$0) {
            Entity $$1 = $$0.getEntity();
            if (getUserID() != $$1.getId() && !($$1 instanceof MagiciansRedEntity) && !($$1 instanceof Projectile)) {
                    radialExplosion($$1);
                this.discard();
            }
    }
    public void radialExplosion(Entity mainTarget){
        if (!this.level().isClientSide()){
            LivingEntity user = this.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                this.level().playSound(null, this.blockPosition(), ModSounds.FIREBALL_HIT_EVENT, SoundSource.PLAYERS, 2.5F, (float)(0.99F + Math.random()*0.02));
                ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                        this.getY(), this.getZ(),
                        100,
                        0.005, 0.01, 0.005,
                        0.02);
                if (mainTarget != null) {
                    getEntity(mainTarget, PMR, user);
                }
                List<Entity> entityList = DamageHandler.genHitbox(user, this.getX(), this.getY(),
                        this.getZ(), 2, 2, 2);
                if (!entityList.isEmpty()){
                    for (Entity value : entityList) {
                        if (!(mainTarget != null && value.is(mainTarget)) && value.isPickable()){
                            getEntity(value, PMR, user);
                        }
                    }
                }
            }
        }
    }

    public void getEntity(Entity gotten, PowersMagiciansRed PMR, LivingEntity user){
        if (gotten !=null && gotten.getId() != getUserID()) {
            float dmg = PMR.getFireballDamage(gotten);
            float strength = 0.85F;
            if (!(user instanceof Player) && !(user instanceof Monster)){
                if (!(gotten instanceof Monster)){
                    if (!(user instanceof Mob mb && mb.getTarget() !=null && mb.getTarget().is(gotten))){
                        return;
                    }
                }
            }

            if (gotten.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.CROSSFIRE, this.standUser),
                    dmg)) {
                float degrees = MainUtil.getLookAtEntityYaw(this, gotten);
                MainUtil.takeUnresistableKnockbackWithY(gotten, strength,
                        Mth.sin(degrees * ((float) Math.PI / 180)),
                        Mth.sin(-17 * ((float) Math.PI / 180)),
                        -Mth.cos(degrees * ((float) Math.PI / 180)));
                if (gotten instanceof LivingEntity LE) {
                    PMR.addEXP(3,LE);
                    StandUser userLE = ((StandUser) LE);
                    int ticks = 20;
                    if (userLE.roundabout$getRemainingFireTicks() > -1){
                        ticks+=userLE.roundabout$getRemainingFireTicks();
                    } else {
                        ticks += 80;
                    }
                    userLE.roundabout$setOnStandFire(PMR.getFireColor(), standUser);
                    userLE.roundabout$setRemainingStandFireTicks(ticks);
                }
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    protected ParticleOptions getTrailParticle() {
        LivingEntity user = this.getStandUser();
        if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            return PMR.getFlameParticle();
        }
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USER_ID, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
        if (standUser != null) {
            $$0.putUUID("standUser", standUser.getUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("standUser")) {
            standUserUUID = $$0.getUUID("standUser");
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

