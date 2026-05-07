package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BloodSplatterEntity extends ThrowableProjectile {
    public BloodSplatterEntity(EntityType<? extends ThrowableProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BloodSplatterEntity(LivingEntity living, Level $$1) {
        super(ModEntities.BLOOD_SPLATTER, living, $$1);
    }

    public BloodSplatterEntity(Level level, double d0, double d1, double d2) {
        super(ModEntities.BLOOD_SPLATTER, d0, d1,d2,level);
    }

    public int healthAmt = 0;


    private static final EntityDataAccessor<Byte> SPLATTER_TYPE = SynchedEntityData.defineId(
            BloodSplatterEntity.class, EntityDataSerializers.BYTE
    );
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(SPLATTER_TYPE, (byte)0);
    }

    public boolean isBundle = false;

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!this.level().isClientSide) {

                ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.BLOOD_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                        15, 0.4, 0.4, 0.25, 0.4);
                SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
                this.playSound($$6, 1F, 1.5F);
            this.discard();
        }
    }

    @Override
    public void tick(){
        if (this.isOnFire() && !this.level().isClientSide){
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                    40, 0.0, 0.2, 0.0, 0.2);
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                    1, 0.5, 0.5, 0.5, 0.2);
            MainUtil.gasExplode(null, (ServerLevel) this.level(), this.getOnPos(), 0, 2, 4, MainUtil.gasDamageMultiplier()*14);
            this.discard();
            return;
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide) {
            if ($$0.getEntity() != null && ownedBy($$0.getEntity()))
                return;
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.BLOOD_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            if ($$0.getEntity() instanceof LivingEntity LE && (MainUtil.getMobBleed(LE) ||
            LE instanceof Player pl)) {
                LE.setHealth(Math.min(LE.getMaxHealth(),LE.getHealth()+healthAmt));
                if ((LE instanceof Mob mb && !((IMob)mb).roundabout$isVampire() && MainUtil.canMobResurrectWithBlood(mb))
                || (LE instanceof Player pl && FateTypes.isHuman(pl))){
                    LE.addEffect(new MobEffectInstance(ModEffects.VAMPIRE_BLOOD, 12000, 0),getOwner());
                }
            }
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);

        $$0.putInt("healthAmt", healthAmt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);

        healthAmt = $$0.getInt("healthAmt");
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
        return 0.06F;
    }
}
