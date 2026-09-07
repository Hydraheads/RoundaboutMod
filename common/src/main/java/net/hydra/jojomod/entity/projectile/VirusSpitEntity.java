package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.block.BloodyStoneMaskBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StoneMaskBlock;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.objects.GentlyWeepsEntity;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.stand.powers.PowersPurpleHaze;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VirusSpitEntity extends ThrowableProjectile {
    public VirusSpitEntity(EntityType<? extends ThrowableProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public VirusSpitEntity(LivingEntity living, Level $$1) {
        super(ModEntities.VIRUS_SPIT, living, $$1);
    }

    public VirusSpitEntity(Level level, double d0, double d1, double d2) {
        super(ModEntities.VIRUS_SPIT, d0, d1,d2,level);
    }

    public int healthAmt = 0;


    private static final EntityDataAccessor<Byte> SPLATTER_TYPE = SynchedEntityData.defineId(
            VirusSpitEntity.class, EntityDataSerializers.BYTE
    );
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(SPLATTER_TYPE, (byte)0);
    }

    //0 = default, healing vampire blood
    //1 = king crimson splash, damaging and blinding

    public byte getSplatterType(){
        return this.getEntityData().get(SPLATTER_TYPE);
    }
    public void setSplatterType(byte type){
        this.getEntityData().set(SPLATTER_TYPE,type);
    }

    public boolean isBundle = false;

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!this.level().isClientSide) {
            if (isRemoved())
                return;
            if (getSplatterType() != 2) {
                ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.BLOOD_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                        15, 0.4, 0.4, 0.25, 0.4);
                SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
                this.playSound($$6, 1F, 1.5F);

            }
            this.discard();
        }
    }

    @Override
    public void tick(){
        if (!this.level().isClientSide){
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.BLOOD_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide) {
            if (isRemoved() || $$0.getEntity() == null)
                return;
            if (ownedBy($$0.getEntity()))
                return;
            if ($$0.getEntity() instanceof EnderMan em) {
                ((IEnderMan) em).roundabout$teleport();
                return;
            }
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.BLOOD_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);

            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            if ($$0.getEntity() instanceof LivingEntity LE ) {
                LE.addEffect(new MobEffectInstance(ModEffects.HAZE_VIRUS, 200,0), getOwner());
            }
            if (getOwner() instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers()
                    instanceof PowersPurpleHaze pph){

                pph.addEXP(4);
            }

            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);

        $$0.putInt("healthAmt", healthAmt);
        $$0.putByte("bloodType", getSplatterType());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);

        healthAmt = $$0.getInt("healthAmt");
        if ($$0.contains("bloodType")) {
            setSplatterType($$0.getByte("bloodType"));
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
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y; $$2 = vecMagic.x;
        }
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
    @Override
    protected float getGravity() {
        if (getSplatterType() == 1){
            return 0.04F;
        }
        return 0.06F;
    }
}
