package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.client.ClientNetworking;
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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.UUID;

public class PWMeteorEntity extends AbstractHurtingProjectile implements UnburnableProjectile, FireProjectile {
    public PWMeteorEntity(EntityType<? extends PWMeteorEntity> $$0, Level $$1) {
        super($$0, $$1);
    }


    protected PWMeteorEntity(EntityType<? extends PWMeteorEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }


    public boolean isWip(){return true;}

    public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);}

    public Component ifWipListDev(){ return Component.literal("Lloyd10").withStyle(ChatFormatting.YELLOW);}


    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(PWMeteorEntity.class, EntityDataSerializers.INT);

    public LivingEntity standUser;
    public UUID standUserUUID;

    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }

    private static final EntityDataAccessor<Float> METEOR_SCALE =
            SynchedEntityData.defineId(PWMeteorEntity.class, EntityDataSerializers.FLOAT);

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

    public float getMeteorScale() {
        return this.entityData.get(METEOR_SCALE);
    }

    public void setMeteorScale(float scale) {
        this.entityData.set(METEOR_SCALE, scale);
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

    protected PWMeteorEntity(EntityType<PWMeteorEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public PWMeteorEntity(LivingEntity $$1, Level $$2) {
        this(ModEntities.PW_METEOR, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public boolean isBundle = false;
    public int saneAgeTicking;
    public int inWaterTicks = 0;

    public void tickWater() {
        inWaterTicks++;

        if (inWaterTicks >= 5 && !slowing) {
            slowing = true;
            disintegrationSoundPlayed = false;
            stopParticles = true;
        }
    }
    private boolean playedDisintegrationSound = false;
    public void setTargetPos(Vec3 pos) {
        this.targetPos = pos;
        this.fixedTarget = pos != null;
    }
    private int chainIndex = 0;
    private boolean isChainStarter = false;
    private int chainSpawnDelay = 0;
    private boolean spawnedChildren = false;
    private int chainDelay = 0;

    public void setChain(int index, boolean starter) {
        this.chainIndex = index;
        this.isChainStarter = starter;

        //  tick spacing
        this.chainSpawnDelay = index * 6;
    }
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;
        if (this.isEffectivelyInWater()) {
            tickWater();
        } else {
            inWaterTicks = 0;
        }
        LivingEntity user = this.standUser;
        if (user == null || !user.isAlive()) {
            this.discard();
            return;
        }
        if (trackingUser && user != null && !slowing) {

            Vec3 velocity = this.getDeltaMovement();

            if (velocity.lengthSqr() > 0.0001) {

                Vec3 offset = this.position().subtract(
                        user.position().add(0, user.getBbHeight() * 0.5, 0)
                );

                double distance = offset.length();

                Vec3 towardUser = offset.scale(-1).normalize();

                Vec3 tangent = new Vec3(
                        -offset.z,
                        0,
                        offset.x
                ).normalize();

                Vec3 force;

                if (distance > 10.0) {
                    force = towardUser.scale(0.35);
                } else {
                    force = towardUser.scale((double) (ClientNetworking.getAppropriateConfig()
                                    .PlanetWavesSettings.meteorTrackingPower) /2)// mientras mas grande mas targeting fuerte
                            .add(tangent.scale(0.04)); // mientras mas grande orbiting mas fuerte

                    if (velocity.y < 0) {
                        force = force.add(0, 0.015, 0);
                    }
                }

                Vec3 newVelocity = velocity.add(force);

                double speed = velocity.length();

                this.setDeltaMovement(
                        newVelocity.normalize().scale(speed)
                );
            }
        }

        if (isChainStarter && !spawnedChildren) {


            if (chainSpawnDelay > 0) {
                chainSpawnDelay--;
                return;
            }

            spawnedChildren = true;

            Vec3 baseDir = this.getDeltaMovement().lengthSqr() > 0.0001
                    ? this.getDeltaMovement().normalize()
                    : new Vec3(0, -1, 0);


            Vec3 right = new Vec3(-baseDir.z, 0, baseDir.x);

            for (int i = 1; i < 3; i++) {

                PWMeteorEntity extra = new PWMeteorEntity(this.standUser, this.level());

                extra.setUser(this.standUser);
                extra.setOwner(this.standUser);
                extra.setTrackingUser(this.trackingUser);
                extra.setChain(i, false);


                double angle = this.random.nextDouble() * Math.PI * 2.0;
                double radius = 1.2 + this.random.nextDouble() * 1.5; // 1.2–2.7 block spread

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                double offsetY = (this.random.nextDouble() - 0.5) * 0.8;

                Vec3 spawnOffset = this.position().add(offsetX, offsetY, offsetZ);

                extra.absMoveTo(spawnOffset.x, spawnOffset.y, spawnOffset.z);

                Vec3 dir = this.getDeltaMovement().lengthSqr() > 0.0001
                        ? this.getDeltaMovement().normalize()
                        : new Vec3(0, -1, 0);

                extra.shoot(dir.x, dir.y, dir.z, 1.8F, 0.0F);

                this.level().addFreshEntity(extra);
            }
        }


        Vec3 velocity = this.getDeltaMovement();

        if (velocity.lengthSqr() > 0.0001) {

            Vec3 toUser = user.position()
                    .add(0, user.getBbHeight() * 0.5, 0)
                    .subtract(this.position())
                    .normalize();

            Vec3 movementDir = velocity.normalize();

            double approachDot = movementDir.dot(toUser);
            double distance = this.distanceTo(user);

            if (!slowing && distance <= 7.2 && approachDot > 0.9) {
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

        float shrinkRate = 0.03f + dissolveStrength * 0.09f;

        double velocityScale = 0.82 - (dissolveStrength * 0.18);

        this.setDeltaMovement(this.getDeltaMovement().scale(velocityScale));

        float scale = getMeteorScale();
        scale = Math.max(0f, scale - shrinkRate);
        setMeteorScale(scale);

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

    private boolean processingExplosion = false;
    @Override
    protected void onHitBlock(BlockHitResult hit) {

        if (this.level().isClientSide()) return;

        LivingEntity user = this.getStandUser();

        if (user == null) {
            radialExplosion(null);
            this.discard();
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


        if (!missedPlayer) {

            BlockPos pos = hit.getBlockPos();
            BlockState state = this.level().getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof net.minecraft.world.level.block.AbstractGlassBlock) {
                this.level().destroyBlock(pos, true);
            }

            return;
        }

        radialExplosion(null);

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

        this.discard();
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
    private boolean slowing = false;
    private boolean disintegrationSoundPlayed = false;
    private int slowTicks = 0;
    private static final int MAX_SLOW_TICKS = 40;
    public boolean stopParticles = false;
    private int disintegrationSoundTick = 0;

    @Override
    protected void onHitEntity(EntityHitResult hit) {

        if (this.level().isClientSide()) return;

        Entity target = hit.getEntity();
        LivingEntity user = this.standUser;

        if (target == user) return;

        if (user != null
                && target instanceof LivingEntity living
                && ((StandUser) user).roundabout$getStandPowers() instanceof PowersPlanetWaves PPW) {

            getEntity(living, PPW, user);

            radialExplosion(living);

            this.discard();
        }
    }

    public void radialExplosion(Entity mainTarget) {
        if (processingExplosion) return;
        processingExplosion = true;

        if (!this.level().isClientSide()) {

            LivingEntity user = this.getStandUser();

            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersPlanetWaves PPW) {


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
                            this.getX(), this.getY(), this.getZ(),
                            100, 0.005, 0.01, 0.005, 0.02
                    );
                }

                if (mainTarget != null) {
                    getEntity(mainTarget, PPW, user);
                }

                List<Entity> entityList = DamageHandler.genHitbox(user, this.getX(), this.getY(),
                        this.getZ(), 2, 2, 2);

                for (Entity value : entityList) {
                    if (value == this || value == user || (mainTarget != null && value.is(mainTarget))) continue;
                    if (value.isPickable()) {
                        getEntity(value, PPW, user);
                    }
                }
            }
        }

        processingExplosion = false;
    }
    public void shootTowardLookTarget(LivingEntity user, float speed) {

        Vec3 targetPos;

        if (user.isShiftKeyDown()) {

            targetPos = new Vec3(
                    user.getX(),
                    user.getY() - 2.0D,
                    user.getZ()
            );
        } else {

            HitResult hit = user.pick(120.0D, 0.0F, false);
            targetPos = hit.getLocation();
        }

        Vec3 dir = targetPos.subtract(this.position()).normalize();

        this.shoot(dir.x, dir.y, dir.z, speed, 0.0F);

        this.forcedDeltaMovement = dir.scale(speed);

        this.setYRot((float)(Mth.atan2(dir.x, dir.z) * (180F / Math.PI)));

        this.setXRot((float)(Mth.atan2(
                dir.y,
                Math.sqrt(dir.x * dir.x + dir.z * dir.z)
        ) * (180F / Math.PI)));
    }
    private boolean trackingUser = false;

    public void setTrackingUser(boolean trackingUser) {
        this.trackingUser = trackingUser;
    }

    public boolean isTrackingUser() {
        return trackingUser;
    }
    public void getEntity(Entity gotten, PowersPlanetWaves PPW, LivingEntity user) {
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
                    PPW.addEXP(5, LE);
                    MainUtil.makeBleed(LE, 1, 100, gotten);

                    LE.setSecondsOnFire(5);
                }
            }
        }
    }
    private boolean missedPlayer = false;

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
        this.entityData.define(METEOR_SCALE, 1.0F);
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

