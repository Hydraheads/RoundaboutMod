package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RattDartEntity extends AbstractArrow {

    private static final EntityDataAccessor<Integer> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BOUNCES = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TYPE = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$PARTICLES = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$BREAKS_BLOCKS = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BOOLEAN);

    public byte getShotType() {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            return this.getEntityData().get(ROUNDABOUT$TYPE);
        }
        return BASIC;
    }
    public void setShotType(byte b) {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            this.getEntityData().set(ROUNDABOUT$TYPE,b);
        }
    }


    public int getBounces() {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            return this.getEntityData().get(ROUNDABOUT$BOUNCES);
        }
        return 0;
    }
    public void setBounces(int i) {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().set(ROUNDABOUT$BOUNCES,i);
        }
    }

    public int getSuperthrowTicks() {
        if (this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            return this.getEntityData().get(ROUNDABOUT$SUPER_THROWN);
        }
        return 0;
    }
    public void setSuperthrowTicks(int i) {
        if (this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            this.getEntityData().set(ROUNDABOUT$SUPER_THROWN,i);
        }
    }

    public boolean shouldParticle() {
        if (this.getEntityData().hasItem(ROUNDABOUT$PARTICLES)) {
            return this.getEntityData().get(ROUNDABOUT$PARTICLES);
        }
        return false;
    }
    public void setParticle(boolean b) {
        if (this.getEntityData().hasItem(ROUNDABOUT$PARTICLES)) {
            this.getEntityData().set(ROUNDABOUT$PARTICLES,b);
        }
    }

    public boolean getBlockBreak() {
        if (this.getEntityData().hasItem(ROUNDABOUT$BREAKS_BLOCKS)) {
            return this.getEntityData().get(ROUNDABOUT$BREAKS_BLOCKS);
        }
        return false;
    }
    public void setBlockBreak(boolean b) {
        if (this.getEntityData().hasItem(ROUNDABOUT$BREAKS_BLOCKS)) {
            this.getEntityData().set(ROUNDABOUT$BREAKS_BLOCKS,b);
        }
    }





    public static byte
                BASIC = (byte) 1,
                BURST = (byte) 2,
                BURST_CHARGED = (byte) 3,
                CHARGED = (byte) 4;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            this.getEntityData().define(ROUNDABOUT$SUPER_THROWN, 0);
            this.getEntityData().define(ROUNDABOUT$BOUNCES, 0);
            this.getEntityData().define(ROUNDABOUT$TYPE, RattDartEntity.BASIC);
            this.getEntityData().define(ROUNDABOUT$PARTICLES, true);
            this.getEntityData().define(ROUNDABOUT$BREAKS_BLOCKS, false);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("superthrow_ticks")) {
            this.entityData.set(ROUNDABOUT$SUPER_THROWN,$$0.getInt("superthrow_ticks"));
        }
        if ($$0.contains("bounces")) {
            this.entityData.set(ROUNDABOUT$BOUNCES,$$0.getInt("bounces"));
        }
        if ($$0.contains("type")) {
            this.entityData.set(ROUNDABOUT$TYPE,$$0.getByte("type"));
        }
        if ($$0.contains("particles")) {
            this.entityData.set(ROUNDABOUT$PARTICLES,$$0.getBoolean("particles"));
        }
        if ($$0.contains("breaks")) {
            this.entityData.set(ROUNDABOUT$BREAKS_BLOCKS,$$0.getBoolean("breaks"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            $$0.putInt("superthrow_ticks",this.getEntityData().get(ROUNDABOUT$SUPER_THROWN));
        }
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            $$0.putInt("bounces",this.getEntityData().get(ROUNDABOUT$BOUNCES));
        }
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            $$0.putByte("type",this.getEntityData().get(ROUNDABOUT$TYPE));
        }
        if (this.getEntityData().hasItem(ROUNDABOUT$PARTICLES)) {
            $$0.putBoolean("particles",this.getEntityData().get(ROUNDABOUT$PARTICLES));
        }
        if (this.getEntityData().hasItem(ROUNDABOUT$BREAKS_BLOCKS)) {
            $$0.putBoolean("breaks",this.getEntityData().get(ROUNDABOUT$BREAKS_BLOCKS));
        }
    }

    double ding() {
        double ding = 0.2;
        return Math.random()*ding-ding/2;
    }

    public void alignDart(LivingEntity player) {
        if ( ((StandUser) player).roundabout$getStandPowers() instanceof PowersRatt PR) {
            if (PR.getStandEntity(player) != null ) {
                if (PR.getStandEntity(player) instanceof RattEntity RE) {
                    Vec3 rots = PR.getRotations(PR.getShootTarget());
                    Vec2 v = new Vec2((float) (-1 * Math.cos(rots.y)),
                            (float) (-1 * Math.sin(rots.y)));
                    this.setPos(RE.getEyeP(0).add(new Vec3(ding(), Mth.clamp(ding(), 0, 3), ding())));
                }
            }
        }
    }

    private int life = 0;
    public RattDartEntity(Level world, LivingEntity player,byte type) {
        super(ModEntities.RATT_DART, player, world);
        this.setShotType(type);
        if (type != BASIC) {
            this.setBounces(1);
        }
    }

    public RattDartEntity(Level world, LivingEntity player) {
        super(ModEntities.RATT_DART, player, world);
        alignDart(player);
    }

    public RattDartEntity(EntityType<? extends RattDartEntity> entity, Level world) {
        super(entity, world);
    }


    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {
            if (getBlockBreak() && getOwner() instanceof Player pl && MainUtil.getIsGamemodeApproriateForGrief(pl) && MainUtil.canPlaceOnClaim(pl,$$0)) {
                BlockState BSS = this.level().getBlockState($$0.getBlockPos());
                if (MainUtil.isDestructible(level(),$$0.getBlockPos(), BSS) && !MainUtil.isBlockDestructionBlacklisted(BSS)){
                    level().destroyBlock($$0.getBlockPos(), false, pl);
                }
                discard();
                return;
            } else if (getBounces() > 0) {
                this.setBounces(getBounces()-1);

                // yoinked from BladedBowlerHatEntity
                Vec3 velocity = this.getDeltaMovement();
                Direction hitDir = $$0.getDirection();
                Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

                // Makes it bounce
                Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

                // Slowly stops it bouncing
                reflected = reflected.scale(0.5); // less bounce / more bounce :)

                this.setDeltaMovement(reflected);

                Vec3 hitLoc = $$0.getLocation();
                Vec3 pushOut = normal.scale(0.2);
                this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);

            } else {
                onHitBlock2($$0);
                this.setParticle(false);
            }
            this.setSuperthrowTicks(0);

        }
    }

    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected void onHitBlock2(BlockHitResult $$0) {
        ((IAbstractArrowAccess)this).roundabout$setLastState(this.level().getBlockState($$0.getBlockPos()));
        BlockState BSS = this.level().getBlockState($$0.getBlockPos());
        BSS.onProjectileHit(this.level(), BSS, $$0, this);
        Vec3 $$1 = $$0.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement($$1);
        Vec3 $$2 = $$1.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - $$2.x, this.getY() - $$2.y, this.getZ() - $$2.z);
        this.playSound(ModSounds.RATT_DART_THUNK_EVENT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);
        ((IAbstractArrowAccess)this).roundabout$resetPiercedEntities();
    }
    public void applyEffect(LivingEntity $$1) {
        if (MainUtil.isBossMob($$1) || $$1 instanceof RoadRollerEntity) {
            DamageSource DS = ModDamageTypes.of($$1.level(), ModDamageTypes.MELTING, this.getOwner());
            $$1.hurt(DS,ClientNetworking.getAppropriateConfig().rattSettings.rattAttackBonusOnBosses);
            return;
        }

        if (this.getOwner() != null) {
            if (this.getOwner() instanceof Creeper) {
                $$1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,30,4));
            }
        }

        MobEffectInstance effect = $$1.getEffect(ModEffects.MELTING);

        int stack = effect != null ? effect.getAmplifier() : -1;
        stack += this.getShotType() == CHARGED ? 2 : 1;


        if (stack != -1) {
            int duration =(int)  (600 * (this.getShotType() == CHARGED ? 1.5 : 1));
            int originalDuration = effect != null ? effect.getDuration() : 0;
            $$1.addEffect(new MobEffectInstance(ModEffects.MELTING, Math.max(duration,originalDuration) , stack), this);
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();

        if ($$1.equals(this.getOwner())) {return;}

        if (!level().isClientSide && $$0.getEntity() instanceof EnderMan em) {

            for (int i = 0; i < 64; i++) {
                if (((IEnderMan) em).roundabout$teleport()) {
                    return;
                }
            }
        }

        float degrees = MainUtil.getLookAtEntityYaw(this, $$1);
        float force = 0.8F;
        if (this.getShotType() == CHARGED) {
            force = 1;
        }


        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(),ModDamageTypes.STAND);
        if (this.getOwner() != null) {
            $$5 = ModDamageTypes.of($$1.level(),ModDamageTypes.STAND,this,this.getOwner());
        }


        float damage =  1;
        damage += (($$1 instanceof Mob) ? ClientNetworking.getAppropriateConfig().rattSettings.rattAttackBonusOnMobs : 0);
        damage *= this.getShotType() == CHARGED ? 3 : 1;

        if ($$1.hurt($$5,damage)) {


            if (this.getShotType() == BASIC && $$4 != null) {
                if ( ((StandUser)$$4).roundabout$getStandPowers() instanceof PowersRatt PR ) {
                    if ($$4 instanceof Player P) {
                        S2CPacketUtil.sendIntPowerDataPacket(P, PowersRatt.UPDATE_CHARGE, Math.min(PR.getChargeTime()+ClientNetworking.getAppropriateConfig().rattSettings.rattChargePerHit,100));
                    }
                }
            }

            if ($$4 instanceof LivingEntity LE) {
                if ( ((StandUser)$$4).roundabout$getStandPowers() instanceof PowersRatt PR ) {
                    if ($$1 instanceof LivingEntity l) {
                        PR.addEXP(2 + ((!PR.isPlaced() && PR.isAuto()) ? 2 : 0) ,l);
                    }
                }
                LE.setLastHurtMob($$1);
            }

            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if ($$1 instanceof LivingEntity || ($$1 instanceof EnderDragonPart)) {
                LivingEntity $$7;
                if ($$1 instanceof LivingEntity L) {
                    $$7 = L;
                } else {
                    $$7 = ((EnderDragonPart)$$1).parentMob;
                }
                applyEffect($$7);
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
                if ($$4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, $$4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                }

                this.doPostHurtEffects($$7);
            }
            this.playSound(ModSounds.RATT_DART_IMPACT_EVENT, 1.0F, (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            force *= 0.5F;
            if ($$1 instanceof Player P) {
                if (P.isBlocking()) {
                    if (this.getShotType() == CHARGED || getShotType() == BURST_CHARGED) {
                        MainUtil.knockShieldPlusStand(P,50);
                    }
                }
            }

        }

        if ($$1 instanceof Mob) {
            force /= 2;
        } else if ($$1 instanceof Player P) {
            if (P.isCreative()) {
                force = 0;
            }
        }
        MainUtil.takeUnresistableKnockbackWithY($$1, force,
                Mth.sin(degrees * ((float) Math.PI / 180)),
                Mth.sin(-23 * ((float) Math.PI / 180)),
                -Mth.cos(degrees * ((float) Math.PI / 180)));

        this.discard();

    }

    @Override
    public void tick(){
        Vec3 delta = this.getDeltaMovement();
        if (inGroundTime >= 160) {
            this.remove(RemovalReason.DISCARDED);
        }
        if(this.isInWater()) {
            this.setSuperthrowTicks(0);
        }
        super.tick();
        this.life += 1;
        if (life > 200) {
            this.discard();
        }
        if (this.getSuperthrowTicks() > 0) {
            this.setDeltaMovement(delta);
        }
        if (!this.level().isClientSide()) {
            if (this.shouldParticle()) {
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.86F, 0.28F, 0.48F
                        ), 1f),
                        this.getX(), this.getY(), this.getZ(),
                        0, 0, 0, 0, 0);
            }
        }
    }


}
