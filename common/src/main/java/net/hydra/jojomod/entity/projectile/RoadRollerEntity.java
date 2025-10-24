package net.hydra.jojomod.entity.projectile;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IAreaOfEffectCloud;
import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class RoadRollerEntity extends LivingEntity implements PlayerRideable {
    private ItemStack roadRollerItem = new ItemStack(ModItems.ROAD_ROLLER);
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputDown;
    private float outOfControlTicks;
    private static final Ingredient CONCRETE;
    private int explosionParticleDelay = 0;

    public static final byte
            WHEELS = 1,
            EXPLOSION = 2;


    public final AnimationState wheels = new AnimationState();
    public final AnimationState explode = new AnimationState();

    public byte getAnimation() {
        if (this.getInputUp()) {
            return WHEELS;
        }
        if (this.getExploded()) {
            // Roundabout.LOGGER.info("wow!");
            return EXPLOSION;
        }
        return 0;
    }

    public void setupAnimationStates() {
        if (!this.level().isClientSide) {
            return;
        }

        byte animation = getAnimation();

        if (animation == WHEELS) {
            this.wheels.startIfStopped(this.tickCount);
        } else {
            this.wheels.stop();
        }

        if (animation == EXPLOSION) {
            this.explode.startIfStopped(this.tickCount);
        } else {
            this.explode.stop();
        }
    }

    public RoadRollerEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public RoadRollerEntity(Level level, LivingEntity owner, ItemStack stack,
                            double x, double y, double z) {
        super(ModEntities.ROAD_ROLLER_ENTITY, level);
        this.roadRollerItem = stack.copy();
        this.setPos(x, y, z);
    }

    public LivingEntity thrower = null;

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return RoadRollerEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.01D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    private boolean wasOnGround = true;
    private boolean groundState = false;
    private double highestY = Double.NEGATIVE_INFINITY;
    public boolean isThrown;

    @Override
    public boolean canCollideWith(Entity other) {
        if ((this.getVehicle() instanceof StandEntity stand) || getExploded()) {
            return false;
        }

        if (other instanceof LivingEntity le && ((StandUser) le).roundabout$getStandPowers().cancelCollision(this)) {
            return false;
        }

        return super.canCollideWith(other);
    }

    @Override
    public boolean canBeCollidedWith() {
        Entity vehicle = this.getVehicle();
        if ((vehicle instanceof StandEntity stand) || getExploded()) {
            return false;
        }
        return super.canBeCollidedWith();
    }

    @Override
    public boolean isPushable() {
        if (getExploded()) {
            return false;
        }
        return !(this.getVehicle() instanceof StandEntity) && super.isPushable();
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 0.65F;
    }

    private float lastYaw = 0f;

    public void setLastYaw(float yaw) {
        this.lastYaw = yaw;
    }

    private Entity previousVehicle = null;

    public boolean hasBeenBaraged = false;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.markHurt();
        return super.hurt(source, amount);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void setSecondsOnFire(int seconds) {
    }

    @Override
    public void die(DamageSource $$0) {
        if (hasBeenBaraged && isThrown && thrower != null) {
            setExploded(true);
            Roundabout.LOGGER.info("Explode!");
            this.deathTime = 0;

        }
        super.die($$0);
    }

    @Override
    protected void tickDeath() {
        if (!getExploded()) {
            explosionParticleDelay = 0;
        }

        if (getExploded()) {
            if (this.deathTime == 0) {
                this.explode.startIfStopped(this.tickCount);
            }

            if (getExploded() && hitGroundBeforeExploding && !this.level().isClientSide) {
                explosionParticleDelay++;

                if (explosionParticleDelay == 10) {
                    BlockPos center = this.blockPosition();
                    RandomSource random = this.level().getRandom();
                    int radius = 8;
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            double distSq = x * x + z * z;
                            if (distSq <= radius * radius && random.nextFloat() < 0.5f) {
                                BlockPos firePos = center.offset(x, 0, z);
                                BlockState below = level().getBlockState(firePos.below());
                                if (below.isSolid() && level().isEmptyBlock(firePos)) {
                                    level().setBlock(firePos, Blocks.FIRE.defaultBlockState(), 11);
                                }
                            }
                        }
                    }
                }
            }

            if (this.deathTime < 300) {
                this.deathTime++;
                return;
            }

            super.die(this.damageSources().generic());
            this.remove(RemovalReason.KILLED);
        } else {
            super.tickDeath();
        }
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {
            if (hitResult.getEntity() instanceof EnderMan em) {
                ((IEnderMan) em).roundabout$teleport();
                return;
            }

            if (this.noPhysics) {
                return;
            }

            final double speed = this.getDeltaMovement().length();
            if (speed < 1D) return;

            float damage = 8.0f;
            float aboveDamage = 13.0f;

            double fallDistance = this.highestY - this.getY();

            if (fallDistance > 2.0D && this.getDeltaMovement().y < -1D) {
                damage = aboveDamage;
            }

            if (thrower != null) {
                DamageSource src = ModDamageTypes.of(this.level(), ModDamageTypes.ROAD_ROLLER, this, this.thrower);
                hitResult.getEntity().hurt(src, damage);
            }
        }
    }

    public boolean mixingSoundStarted = false;
    public boolean explosionSoundStarted = false;

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.IN_WALL)) return true;
        return super.isInvulnerableTo(source);
    }

    private boolean scrapParticlesSpawned = false;
    private boolean smokeParticlesSpawned = false;
    private boolean explosionParticlesSpawned = false;

    private int explosionExpandTicks = 0;
    private boolean explosionExpanded = false;
    private boolean shouldExpandHitbox = false;
    private boolean hitGroundBeforeExploding = false;

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (shouldExpandHitbox) {
            return super.getDimensions(pose).scale(5.0F);
        }
        return super.getDimensions(pose);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getControllingPassenger() == null) {
            this.setYRot(this.yRotO);
            this.yBodyRot = this.yRotO;
            this.yHeadRot = this.yRotO;
        }

        if (!this.level().isClientSide()) {
            sendOutMessage();
        }

        if (level().isClientSide && this.tickCount % 60 == 1) {
            if (!((TimeStop) level()).inTimeStopRange(this)) {
                ClientUtil.handleRoadRollerAmbientSound(this);
            }
        }

        if (!this.level().isClientSide) {
            if (isThrown) {
                if (this.tickCount % 180 == 0) {
                    this.thrower = null;
                }
            }
        }

        if (!level().isClientSide) {
            if (this.onGround()) {
                hitGroundBeforeExploding = true;
            }
        }

        if (level().isClientSide) {
            if (getExploded() && !explosionSoundStarted) {
                if (explosionParticleDelay >= 10) {
                    if (!((TimeStop) level()).inTimeStopRange(this)) {
                        explosionSoundStarted = true;
                        ClientUtil.handleRoadRollerExplosionSound(this);
                    }
                }
            }
        }

        if (!level().isClientSide) {
            if (getExploded()) {
                explosionExpandTicks++;

                if (!explosionExpanded && explosionExpandTicks == 5) {
                    explosionExpanded = true;
                    shouldExpandHitbox = true;
                    this.refreshDimensions();

                    AABB area = this.getBoundingBox().inflate(2.0D);
                    for (Entity e : level().getEntities(this, area, entity -> entity instanceof LivingEntity && entity != this)) {
                        e.hurt(ModDamageTypes.of(level(), ModDamageTypes.ROAD_ROLLER, this, thrower), 8.0F);
                    }
                }

                if (explosionExpanded && explosionExpandTicks >= 25) {
                    shouldExpandHitbox = false;
                    explosionExpandTicks = 0;
                    this.refreshDimensions();
                }
            }
        }

        if (level().isClientSide) {
            if (getExploded()) {
                explosionParticleDelay++;

                if (explosionParticleDelay >= 10) {

                    if (!scrapParticlesSpawned) {
                        scrapParticlesSpawned = true;
                        for (int i = 0; i < 15; i++) {
                            double vx = (random.nextDouble() - 0.5D) * 1.2D;
                            double vy = random.nextDouble() * 0.6D;
                            double vz = (random.nextDouble() - 0.5D) * 1.2D;
                            level().addParticle(ModParticles.ROAD_ROLLER_SCRAP,
                                    this.getX(), this.getY() + 1.0D, this.getZ(),
                                    vx, vy, vz);
                        }
                    }

                    if (!smokeParticlesSpawned) {
                        smokeParticlesSpawned = true;
                        for (int i = 0; i < 40; i++) {
                            double vx = (random.nextDouble() - 0.5D) * 1.2D;
                            double vy = random.nextDouble() * 0.6D;
                            double vz = (random.nextDouble() - 0.5D) * 1.2D;
                            level().addParticle(ModParticles.ROAD_ROLLER_SMOKE,
                                    this.getX(), this.getY() + 1.0D, this.getZ(),
                                    vx, vy, vz);
                        }
                    }

                    if (!explosionParticlesSpawned) {
                        explosionParticlesSpawned = true;
                        for (int i = 0; i < 40; i++) {
                            double vx = (random.nextDouble() - 0.5D) * 1.2D;
                            double vy = random.nextDouble() * 0.6D;
                            double vz = (random.nextDouble() - 0.5D) * 1.2D;
                            level().addParticle(ModParticles.ROAD_ROLLER_EXPLOSION,
                                    this.getX(), this.getY() + 1.0D, this.getZ(),
                                    vx, vy, vz);
                        }
                    }
                }
            } else {
                explosionParticleDelay = 0;
            }
        }

        if (level().isClientSide) {
            setupAnimationStates();
        }

        if (!level().isClientSide) {
            float fraction = this.getHealth() / this.getMaxHealth();
            this.setCrackiness(Crackiness.byHealthFraction(fraction));
        }

        if (!level().isClientSide) {
            BlockPos pos = this.blockPosition();
            AABB box = this.getBoundingBox();

            boolean insideABlock = false;

            for (int x = Mth.floor(box.minX); x <= Mth.floor(box.maxX); x++) {
                for (int y = Mth.floor(box.minY); y <= Mth.floor(box.maxY); y++) {
                    for (int z = Mth.floor(box.minZ); z <= Mth.floor(box.maxZ); z++) {
                        BlockPos bp = new BlockPos(x, y, z);
                        BlockState state = level().getBlockState(bp);

                        if (!state.isAir() && !state.getCollisionShape(level(), bp).isEmpty()) {
                            insideABlock = true;
                            break;
                        }
                    }
                    if (insideABlock) break;
                }
                if (insideABlock) break;
            }

            if (this.isInWall() || insideABlock) {
                int tries = 0;
                while ((this.isInWall() || insideABlock) && tries < 40) {
                    this.teleportTo(this.getX(), this.getY() + 0.05D, this.getZ());
                    this.refreshDimensions();
                    insideABlock = !this.level().noCollision(this);
                    tries++;
                }

                if (this.isInWall() || insideABlock) {
                    this.teleportTo(this.getX(), this.getY() + 0.1D, this.getZ());
                    this.refreshDimensions();
                }
            }
        }

        if (level().isClientSide && getPavingBoolean()) {
            RandomSource random = level().getRandom();

            float yaw = this.getViewYRot(1.0F);
            if (yaw == 0f && lastYaw != 0f) yaw = lastYaw;
            else lastYaw = yaw;

            float yawRad = (float) Math.toRadians(yaw);
            float cos = Mth.cos(yawRad);
            float sin = Mth.sin(yawRad);

            double rightX = cos, rightZ = sin;
            double fwdX = -sin, fwdZ = cos;

            double forwardOffset = 0.12D;

            for (int i = 0; i < 4; i++) {
                double localX = (random.nextDouble() - 0.5) * 1.8;
                double localZ = (random.nextDouble() - 1.75) * 1.8;

                localX = -localX;

                double offsetX = localX * rightX + localZ * fwdX;
                double offsetZ = localX * rightZ + localZ * fwdZ;

                offsetX += fwdX * forwardOffset;
                offsetZ += fwdZ * forwardOffset;

                double x = getX() + offsetX;
                double y = getY() + 2.4D + random.nextDouble() * 0.3;
                double z = getZ() + offsetZ;
                double vy = 0.05D + random.nextDouble() * 0.02;

                level().addParticle(ParticleTypes.SMOKE, x, y, z, 0, vy, 0);
            }
        }


        Entity currentVehicle = this.getVehicle();
        if (!(previousVehicle instanceof StandEntity) && currentVehicle instanceof StandEntity) {
            this.setLastYaw(this.getYRot());
        }
        previousVehicle = currentVehicle;

        Vec3 currentPos = this.position();
        Vec3 nextPos = currentPos.add(this.getDeltaMovement());

        AABB sweptBox = this.getBoundingBox()
                .expandTowards(this.getDeltaMovement())
                .inflate(this.getBbWidth() * 1 + 0.1);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this.level(), this, currentPos, nextPos, sweptBox,
                this::canHitEntity
        );

        if (entityHitResult != null) {
            this.onHitEntity(entityHitResult);
        }

        AABB box = this.getBoundingBox().inflate(0.2);
        for (Entity e : level().getEntities(this, box, this::canHitEntity)) {
            this.onHitEntity(new EntityHitResult(e));
        }

        final boolean onGroundNow = this.onGround();
        final double highestYValue = this.getY();

        if (wasOnGround && !onGroundNow) {
            isThrown = true;
            highestY = highestYValue;
        }

        if (isThrown) {
            if (highestYValue > highestY) {
                highestY = highestYValue;
            }
        }

        wasOnGround = onGroundNow;

        if (getControllingPassenger() == null) {
            setInputUp(false);
            inputDown = false;
            inputLeft = false;
            inputRight = false;
        }

        if (!getInputUp()) {
            return;
        }

        Vec3 velocity = this.getDeltaMovement();
        if (velocity.lengthSqr() > 0.0025) {
            float targetYaw = (float) Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 90.0f;
            lastYaw = targetYaw;
        }
        this.setYRot(lastYaw);
        this.yRotO = lastYaw;
        this.yBodyRot = lastYaw;
        this.yHeadRot = lastYaw;

        if (!this.level().isClientSide) {
            boolean willRaise = getPavingBoolean();
            if (willRaise) {
                outer:
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos sigmaPos = BlockPos.containing(this.position().add(0, -0.5, 0));
                        BlockPos bp = sigmaPos.offset(x, 0, z);
                        BlockState s = this.level().getBlockState(bp);
                        if (s.is(Blocks.DIRT_PATH)) {
                            willRaise = true;
                            break outer;
                        }
                    }
                }
            }

            if (willRaise) {
                double dy = 0.0626D;
                this.teleportTo(this.getX(), this.getY() + dy, this.getZ());
            }

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {

                    BlockPos sigmaPos = BlockPos.containing(this.position().add(0, -0.5, 0));
                    BlockPos blockPos = sigmaPos.offset(x, 0, z);

                    BlockState state = this.level().getBlockState(blockPos);
                    if (!(state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.DIRT_PATH))) {
                        continue;
                    }

                    if (!getPavingBoolean()) {
                        this.level().setBlock(blockPos, Blocks.DIRT_PATH.defaultBlockState(), 3);
                    } else {
                        ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(getConcreteColour().getItem());
                        if (itemID == null) continue;

                        String fixedString = itemID.getPath().replace("_powder", "");

                        ResourceLocation blockID = new ResourceLocation(itemID.getNamespace(), fixedString);

                        Block actualConcrete = BuiltInRegistries.BLOCK.getOptional(blockID).orElse(Blocks.BLACK_CONCRETE);

                        this.level().setBlock(blockPos, actualConcrete.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    protected boolean canHitEntity(Entity $$0x) {
        if ($$0x == thrower) {
            return false;
        }

        return true;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    @Override
    public void doPush(Entity $$0) {
        if ((this.getVehicle() instanceof StandEntity stand) || getExploded()) {
            return;
        }
        super.doPush($$0);
    }

    int rideLengthTicks = 0;
    public void sendOutMessage(){
        Entity cpas = this.getControllingPassenger();
        if (cpas instanceof ServerPlayer SP){
            rideLengthTicks++;
            if (rideLengthTicks == 40){
                SP.displayClientMessage(Component.translatable("text.roundabout.riding_road_roller").withStyle(ChatFormatting.LIGHT_PURPLE), true);
            }
        } else {
            rideLengthTicks = 0;
        }
    }

    @Override
    public void push(Entity $$0) {
        if (this.getVehicle() instanceof StandEntity stand) {
            return;
        }

        super.push($$0);

        if (this.level().isClientSide) {
            return;
        }

        if (!this.isThrown) {
            return;
        }

        if ($$0 == this.getControllingPassenger()) {
            return;
        }

        if ($$0 == this.getVehicle() || this.isPassengerOfSameVehicle($$0)) {
            return;
        }

        if (this.noPhysics || $$0.noPhysics) {
            return;
        }

        Vec3 dir = this.getDeltaMovement();
        if (dir.lengthSqr() > 1.0E-4D) {
            Vec3 n = dir.normalize();
            $$0.push(n.x * 0.1D, 0.2D, n.z * 0.1D);
        }
    }

    @Override
    protected float getRiddenSpeed(Player $$0) {
        return (float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0);
    }

    @Override
    protected void tickRidden(Player $$0, Vec3 $$1) {
        super.tickRidden($$0, $$1);
        Vec2 $$2 = this.getRiddenRotation($$0);
        this.setRot($$2.y, $$2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        Vec3 $$4 = this.getDeltaMovement();
        if (!getInputUp()) {
            this.setDeltaMovement($$4.multiply(0.85, 1.0, 0.85));
            return;
        }
        if (this.onGround()) {
            float sin = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
            float cos = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));

            this.setDeltaMovement($$4.add((-0.06F * sin), 0.0, (0.06F * cos)));
        } else {
            this.setDeltaMovement($$4.multiply(0.98, 1.0, 0.98));
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) (this.getBbHeight() * 0.7F);
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction move) {
        double backOffset = 0.7;
        float yawRad = (float) Math.toRadians(this.getYRot());
        double x = this.getX() + Mth.sin(yawRad) * backOffset;
        double y = this.getY() + 0.95;
        double z = this.getZ() - Mth.cos(yawRad) * backOffset;
        passenger.setPos(x, y, z);
    }

    @Override
    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        if ($$0 > 1.0F) {
        }

        int $$3 = this.calculateFallDamage($$0, $$1);
        if ($$3 <= 0) {
            return false;
        } else {
            this.hurt($$2, (float) $$3);

            this.playBlockFallSound();
            return true;
        }
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (!this.getPassengers().isEmpty()) {
            Entity entity = this.getPassengers().get(0);
            if (entity instanceof LivingEntity) {
                return (LivingEntity) entity;
            }
        }

        return null;
    }

    public float maxUpStep() {
        float f = super.maxUpStep();
        return this.getControllingPassenger() instanceof Player ? Math.max(f, 1.5F) : f;
    }

    @Override
    public void makeStuckInBlock(BlockState $$0, Vec3 $$1) {
        if (!$$0.is(Blocks.COBWEB)) {
            super.makeStuckInBlock($$0, $$1);
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity $$0) {
        return new Vec2($$0.getXRot() * 0.5F, $$0.getYRot());
    }

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        float forward = $$0.zza;
        boolean pressed = forward > 0.0F;

        if (!level().isClientSide) {
            setInputUp(pressed);
        }

        inputDown = forward < 0.0F;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        ;
        return Vec3.ZERO;
    }

    protected void doPlayerRide(Player $$0) {
        if (!this.level().isClientSide) {
            $$0.setYRot(this.getYRot());
            $$0.setXRot(this.getXRot());
            $$0.startRiding(this);
        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 $$0, LivingEntity $$1) {
        double $$2 = this.getX() + $$0.x;
        double $$3 = this.getBoundingBox().minY;
        double $$4 = this.getZ() + $$0.z;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();

        for (Pose $$6 : $$1.getDismountPoses()) {
            $$5.set($$2, $$3, $$4);
            double $$7 = this.getBoundingBox().maxY + 0.75;

            do {
                double $$8 = this.level().getBlockFloorHeight($$5);
                if ((double) $$5.getY() + $$8 > $$7) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid($$8)) {
                    AABB $$9 = $$1.getLocalBoundsForPose($$6);
                    Vec3 $$10 = new Vec3($$2, (double) $$5.getY() + $$8, $$4);
                    if (DismountHelper.canDismountTo(this.level(), $$1, $$9.move($$10))) {
                        $$1.setPose($$6);
                        return $$10;
                    }
                }

                $$5.move(Direction.UP);
            } while (!((double) $$5.getY() < $$7));
        }

        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity $$0) {
        Vec3 $$1 = getCollisionHorizontalEscapeVector(
                (double) this.getBbWidth(), (double) $$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 $$2 = this.getDismountLocationInDirection($$1, $$0);
        if ($$2 != null) {
            return $$2;
        } else {
            Vec3 $$3 = getCollisionHorizontalEscapeVector(
                    (double) this.getBbWidth(), (double) $$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            Vec3 $$4 = this.getDismountLocationInDirection($$3, $$0);
            return $$4 != null ? $$4 : this.position();
        }
    }

    protected static final EntityDataAccessor<Integer> PAVING_TIMER = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> PAVING_BOOLEAN = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<ItemStack> CONCRETE_COLOUR = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Boolean> INPUT_UP = SynchedEntityData.defineId(RoadRollerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EXPLODED = SynchedEntityData.defineId(RoadRollerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Byte> CRACKINESS = SynchedEntityData.defineId(RoadRollerEntity.class, EntityDataSerializers.BYTE);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PAVING_TIMER, 0);
        this.entityData.define(PAVING_BOOLEAN, false);
        this.entityData.define(CONCRETE_COLOUR, ItemStack.EMPTY);
        this.entityData.define(INPUT_UP, false);
        this.entityData.define(EXPLODED, false);
        this.entityData.define(CRACKINESS, Crackiness.NONE);
    }

    public final void setPavingTimer(int PavingTimer) {
        this.entityData.set(PAVING_TIMER, PavingTimer);
    }
    public int getPavingTimer() {
        return this.entityData.get(PAVING_TIMER);
    }

    public void setPavingBoolean(boolean value) {
        boolean wasPaving = this.getPavingBoolean();
        this.entityData.set(PAVING_BOOLEAN, value);

        if (level().isClientSide) {
            if (value && !wasPaving && !((TimeStop) level()).inTimeStopRange(this)) {
                mixingSoundStarted = true;
                ClientUtil.handleRoadRollerMixingSound(this);
            }
            if (!value && wasPaving) {
                mixingSoundStarted = false;
            }
        }
    }
    public boolean getPavingBoolean() {
        return this.entityData.get(PAVING_BOOLEAN);
    }

    public ItemStack getConcreteColour() {
        return this.entityData.get(CONCRETE_COLOUR);
    }

    public void setConcreteColour(ItemStack stack) {
        this.entityData.set(CONCRETE_COLOUR, stack.copy());
    }

    public void setInputUp(boolean value) {
        this.entityData.set(INPUT_UP, value);
    }

    public boolean getInputUp() {
        return this.entityData.get(INPUT_UP);
    }

    public void setExploded(boolean value) {
        this.entityData.set(EXPLODED, value);
    }

    public boolean getExploded() {
        return this.entityData.get(EXPLODED);
    }

    public byte getCrackiness() {
        return this.entityData.get(CRACKINESS);
    }

    public void setCrackiness(byte level) {
        this.entityData.set(CRACKINESS, level);
    }

    public InteractionResult interact(Player $$0, InteractionHand $$1) {

        if (getExploded()) {
            return InteractionResult.FAIL;
        }

        boolean concreteEnabled = false;
        ItemStack item = $$0.getItemInHand($$1);
        if (this.isConcrete(item)) {
            concreteEnabled = true;
            if (getPavingBoolean()) {
                ItemStack current = getConcreteColour();
                if (!ItemStack.isSameItemSameTags(current, item)) {
                    setConcreteColour(item);
                }
                setPavingTimer(0);
            } else {
                setConcreteColour(item);
                setPavingBoolean(true);
                setPavingTimer(0);
            }
            if (!$$0.getAbilities().instabuild && !level().isClientSide) {
                item.shrink(1);
            }

            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if ($$0.isPassengerOfSameVehicle(this)) {
            return InteractionResult.PASS;
        }
        if ($$0.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (this.outOfControlTicks < 60.0F) {
            if (!this.level().isClientSide) {
                if (!concreteEnabled) {
                    return $$0.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
                } else if (concreteEnabled) {
                    setPavingBoolean(true);
                }
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    public boolean isConcrete(ItemStack itemStack) {
        return CONCRETE.test(itemStack);
    }

    static {
        CONCRETE = Ingredient.of(new ItemLike[]{Items.BLACK_CONCRETE_POWDER, Items.RED_CONCRETE_POWDER, Items.BLUE_CONCRETE_POWDER, Items.YELLOW_CONCRETE_POWDER, Items.PURPLE_CONCRETE_POWDER, Items.MAGENTA_CONCRETE_POWDER, Items.ORANGE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE_POWDER, Items.GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIME_CONCRETE_POWDER, Items.BROWN_CONCRETE_POWDER, Items.CYAN_CONCRETE_POWDER, Items.GREEN_CONCRETE_POWDER, Items.PINK_CONCRETE_POWDER, Items.WHITE_CONCRETE_POWDER});
    }

    public static class Crackiness {
        public static final byte NONE = 0;
        public static final byte MEDIUM = 1;
        public static final byte HIGH = 2;

        public static byte byHealthFraction(float fraction) {
            if (fraction < 0.25F) return HIGH;
            if (fraction < 0.5F) return MEDIUM;
            return NONE;
        }
    }
}
