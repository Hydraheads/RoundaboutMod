package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.models.projectile.PWMeteorModel;
import net.hydra.jojomod.entity.FireProjectile;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.stand.powers.PowersPlanetWaves;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.UUID;

public class PWBigMeteorEntity extends AbstractHurtingProjectile implements UnburnableProjectile, FireProjectile {
    public PWBigMeteorEntity(EntityType<? extends net.hydra.jojomod.entity.projectile.PWBigMeteorEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected PWBigMeteorEntity(EntityType<? extends net.hydra.jojomod.entity.projectile.PWBigMeteorEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(PWBigMeteorEntity.class, EntityDataSerializers.INT);

    public LivingEntity standUser;
    public UUID standUserUUID;

    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }

    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE) {
            this.standUser = LE;
            if (!this.level().isClientSide()) {
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

    public void setRenderRotation(double rotation) {
        lastRenderRotation = renderRotation;
        renderRotation = rotation;
    }

    public Vec3 storeVec;

    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()) {
            standUserUUID = User.getUUID();
        }
    }

    public boolean initialized = false;

    protected PWBigMeteorEntity(EntityType<net.hydra.jojomod.entity.projectile.PWBigMeteorEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    private boolean isProtectedBlock(BlockState state) {

        if (state.is(BlockTags.COAL_ORES)) return true;
        if (state.is(BlockTags.IRON_ORES)) return true;
        if (state.is(BlockTags.GOLD_ORES)) return true;
        if (state.is(BlockTags.REDSTONE_ORES)) return true;
        if (state.is(BlockTags.LAPIS_ORES)) return true;
        if (state.is(BlockTags.DIAMOND_ORES)) return true;
        if (state.is(BlockTags.EMERALD_ORES)) return true;
        if (state.is(BlockTags.COPPER_ORES)) return true;

        if (state.is(Blocks.NETHER_QUARTZ_ORE)) return true;
        if (state.is(Blocks.NETHER_GOLD_ORE)) return true;
        if (state.is(Blocks.ANCIENT_DEBRIS)) return true;

        return false;
    }
    public PWBigMeteorEntity(LivingEntity $$1, Level $$2) {
        this(ModEntities.PW_BIG_METEOR, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    private static final int MAX_BLOCKS_DISINTEGRATED = 150;
    private int blocksDisintegrated = 0;
    private boolean missedPlayer = false;
    private boolean disintegrationSoundPlayed = false;
    private boolean slowing = false;
    private int slowTicks = 0;
    private static final int MAX_SLOW_TICKS = 60;
    private boolean stopParticles = false;
    public boolean isBundle = false;
    public int saneAgeTicking;
    public int inWaterTicks = 0;

    public void tickWater() {
        inWaterTicks++;
        if (inWaterTicks > 40) {
            this.discard();
        }
    }

    private static final EntityDataAccessor<Float> BIG_METEOR_SCALE =
            SynchedEntityData.defineId(PWBigMeteorEntity.class, EntityDataSerializers.FLOAT);

    public float getBigMeteorScale() {
        return this.entityData.get(BIG_METEOR_SCALE);
    }

    public void setBigMeteorScale(float scale) {
        this.entityData.set(BIG_METEOR_SCALE, scale);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        LivingEntity user = this.standUser;
        if (user == null || !user.isAlive()) {
            this.discard();
            return;
        }

        Vec3 velocity = this.getDeltaMovement();

        if (velocity.lengthSqr() > 0.0001) {

            Vec3 toUser = user.position()
                    .add(0, user.getBbHeight() * 0.5, 0)
                    .subtract(this.position())
                    .normalize();

            Vec3 movementDir = velocity.normalize();

            // 1 = directly toward user
            // 0 = sideways
            // -1 = away
            double approachDot = movementDir.dot(toUser);

            double distance = this.distanceTo(user);

            //distance from user
            if (!slowing
                    && distance <= 7.2    //osea que tanto te perdona
                    && approachDot > 0.9) {//0.9 = casi golpe directo
                //0.5 = more forgiving cone

                slowing = true;
                disintegrationSoundPlayed = false;
            }
        }


        if (!slowing) {
            if (forcedDeltaMovement != null) {
                this.setDeltaMovement(forcedDeltaMovement);
            }
            return;
        }

        double distance = this.distanceTo(user);


        float dissolveStrength = (float) Mth.clamp(
                1.0 - (distance / 10.0),
                0.0,
                1.0
        );

// scales from 0.03 to 0.12
        float shrinkRate = 0.03f + dissolveStrength * 0.09f;

        double velocityScale = 0.82 - (dissolveStrength * 0.18);

        this.setDeltaMovement(
                this.getDeltaMovement().scale(velocityScale)
        );

        float scale = getBigMeteorScale();
        scale = Math.max(0f, scale - shrinkRate);

        setBigMeteorScale(scale);

        if (!disintegrationSoundPlayed) {
            disintegrationSoundPlayed = true;

            this.level().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    ModSounds.PLANET_WAVES_DISINTEGRATION_EVENT,
                    SoundSource.PLAYERS,
                    1.5F,
                    1.0F
            );
        }

        if (scale <= 0.02F) {
            this.discard();
        }
    }


    public double getRandomY(double $$0) {
        return this.getY((2.0 * this.random.nextDouble() - 1.0) * $$0);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(15.0F, 15.0F);
    }

    private boolean exploded = false;
    private boolean processingExplosion = false;

    private void explodeAndIgnite() {

        if (this.level().isClientSide()) return;


        radialExplosion(null);

        BlockPos center = this.blockPosition();

        int explosionRadius = 4;


        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-explosionRadius, -explosionRadius, -explosionRadius),
                center.offset(explosionRadius, explosionRadius, explosionRadius))) {

            double dist = pos.distSqr(center);

            if (dist > explosionRadius * explosionRadius) continue;

            BlockState state = this.level().getBlockState(pos);

            if (state.isAir()) continue;

            if (state.is(Blocks.BEDROCK)) continue;

            if (isProtectedBlock(state)) continue;

            this.level().destroyBlock(pos, false);
        }

        // fire inside crater
        int fireRadius = 5;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-fireRadius, -fireRadius, -fireRadius),
                center.offset(fireRadius, 1, fireRadius))) {

            // only random spots
            if (this.random.nextFloat() > 0.22F) {
                continue;
            }

            // must be air
            if (!this.level().isEmptyBlock(pos)) {
                continue;
            }

            BlockPos below = pos.below();
            BlockState belowState = this.level().getBlockState(below);

            // fire needs support
            if (!belowState.isFaceSturdy(this.level(), below, Direction.UP)) {
                continue;
            }

            BlockState fire = Blocks.FIRE.defaultBlockState();

            if (fire.canSurvive(this.level(), pos)) {
                this.level().setBlock(pos, fire, 3);
            }
        }
        LivingEntity user = this.getStandUser();

        if (user instanceof StandUser SU &&
                SU.roundabout$getStandPowers() instanceof PowersPlanetWaves PPW) {

            PPW.addEXP(10);
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {

        if (this.level().isClientSide()) return;

        LivingEntity user = this.getStandUser();

        if (user == null) {
            explodeAndIgnite();
            return;
        }

        Vec3 meteorPos = this.position();

        Vec3 toUser = user.position()
                .add(0, user.getBbHeight() * 0.5, 0)
                .subtract(meteorPos)
                .normalize();

        Vec3 movementDir = this.getDeltaMovement().normalize();

        double approachDot = movementDir.dot(toUser);

        if (approachDot <= 0.0D) {
            missedPlayer = true;
        }

        if (missedPlayer) {
            explodeAndIgnite();
            return;
        }

        AABB box = this.getBoundingBox();

        BlockPos min = BlockPos.containing(
                Math.floor(box.minX),
                Math.floor(box.minY),
                Math.floor(box.minZ)
        );

        BlockPos max = BlockPos.containing(
                Math.floor(box.maxX),
                Math.floor(box.maxY),
                Math.floor(box.maxZ)
        );

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {

            BlockState state = this.level().getBlockState(pos);

            if (state.isAir()) continue;


            if (state.is(Blocks.BEDROCK)) {
                explodeAndIgnite();
                return;
            }

            // preserve ores
            if (isProtectedBlock(state)) {
                continue;
            }

            this.level().destroyBlock(pos, false);

            blocksDisintegrated++;

            /*
             once block limit is reached,
             trigger missed-player explosion
             */
            if (blocksDisintegrated >= MAX_BLOCKS_DISINTEGRATED) {

                missedPlayer = true;

                explodeAndIgnite();
                return;
            }
        }
    }

    public LivingEntity getUser() {
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE) {
            return LE;
        }
        return null;
    }

    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y;
            $$2 = vecMagic.x;
        }
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double) $$6, (double) $$7, (double) $$8, $$4, $$5);
        Vec3 force = new Vec3($$6, $$7, $$8)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.0172275 * (double) $$4),
                        this.random.triangle(0.0, 0.0172275 * (double) $$4),
                        this.random.triangle(0.0, 0.0172275 * (double) $$4)
                )
                .scale((double) $$3);
        forcedDeltaMovement = force;
        Vec3 $$9 = $$0.getDeltaMovement();
    }

    public Vec3 forcedDeltaMovement = null;
    private Vec3 targetPos;
    private boolean fixedTarget = false;

    public void setTargetPos(Vec3 pos) {
        this.targetPos = pos;
        this.fixedTarget = pos != null;
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity target = hit.getEntity();
        LivingEntity user = this.standUser;

        if (target == user) return;

        if (target instanceof LivingEntity living && user != null) {
            living.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.CROSSFIRE, user), 8F);
        }
    }

    public void radialExplosion(Entity mainTarget) {

        if (processingExplosion) return;
        processingExplosion = true;

        if (!this.level().isClientSide()) {

            LivingEntity user = this.getStandUser();

            if (user != null &&
                    ((StandUser) user).roundabout$getStandPowers()
                            instanceof PowersPlanetWaves PPW) {

                this.level().playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        ModSounds.FIREBALL_HIT_EVENT,
                        SoundSource.PLAYERS,
                        2.5F,
                        1.0F
                );

                if (!slowing) {
                    ((ServerLevel) this.level()).sendParticles(
                            PPW.getFlameParticle(),
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            100,
                            0.005,
                            0.01,
                            0.005,
                            0.02
                    );
                }


                if (mainTarget != null) {


                    if (mainTarget != user) {
                        getEntity(mainTarget, PPW, user);
                    }
                }

                List<Entity> entityList = DamageHandler.genHitbox(
                        user,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        2,
                        2,
                        2
                );

                for (Entity value : entityList) {


                    if (value == user) continue;


                    if (value == this) continue;


                    if (mainTarget != null && value.is(mainTarget)) continue;


                    if (value.isPassengerOfSameVehicle(user)) continue;

                    if (value.isPickable()) {
                        getEntity(value, PPW, user);
                    }
                }
            }
        }

        processingExplosion = false;
    }
    public void shootTowardLookTarget(LivingEntity user, float speed) {

        HitResult hit = user.pick(120.0D, 0.0F, false);

        Vec3 targetPos = hit.getLocation();

        Vec3 dir = targetPos.subtract(this.position()).normalize();

        this.shoot(dir.x, dir.y, dir.z, speed, 0.0F);

        this.forcedDeltaMovement = dir.scale(speed);

        this.setYRot((float)(Mth.atan2(dir.x, dir.z) * (180F / Math.PI)));

        this.setXRot((float)(Mth.atan2(
                dir.y,
                Math.sqrt(dir.x * dir.x + dir.z * dir.z)
        ) * (180F / Math.PI)));
    }
    public void getEntity(Entity gotten, PowersPlanetWaves PPW, LivingEntity user) {
        if (gotten == standUser || gotten == this.getOwner()) {
            return;
        }
        if (gotten != null && gotten.getId() != getUserID()) {
            float dmg = PPW.getFireballDamage(gotten);
            float strength = 0.85F;
            if (!(user instanceof Player) && !(user instanceof Monster)) {
                if (!(gotten instanceof Monster)) {
                    if (!(user instanceof Mob mb && mb.getTarget() != null && mb.getTarget().is(gotten))) {
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
                    PPW.addEXP(25, LE);
                    MainUtil.makeBleed(LE, 2, 200, gotten);
                    StandUser userLE = ((StandUser) LE);
                    int ticks = 20;
                    if (userLE.roundabout$getRemainingFireTicks() > -1) {
                        ticks += userLE.roundabout$getRemainingFireTicks();
                    } else {
                        ticks += 80;
                    }
                    userLE.roundabout$setOnStandFire(PPW.getFireColor(), standUser);
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
        if (stopParticles) {
            return ParticleTypes.EXPLOSION;
        }

        LivingEntity user = this.getStandUser();
        if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersPlanetWaves PPW) {
            ParticleOptions particle = PPW.getFlameParticle();
            return particle != null ? particle : ParticleTypes.SMOKE;
        }

        return ParticleTypes.SMOKE;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(USER_ID, -1);
        this.entityData.define(BIG_METEOR_SCALE, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (standUser != null) {
            $$0.putUUID("standUser", standUser.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
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

    public LivingEntity getStandUser() {
        if (standUser != null) {
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()) {
            Entity ett = ((ServerLevel) this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett) {
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE) {
            standUser = LE;
        }
        return standUser;
    }
}
