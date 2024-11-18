package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GasolineSplatterEntity extends ThrowableItemProjectile {
    public GasolineSplatterEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GasolineSplatterEntity(LivingEntity living, Level $$1) {
        super(ModEntities.GASOLINE_SPLATTER, living, $$1);
    }

    public GasolineSplatterEntity(Level level, double d0, double d1, double d2) {
        super(ModEntities.GASOLINE_SPLATTER, d0, d1,d2,level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModBlocks.GASOLINE_SPLATTER.asItem();
    }

    public boolean isBundle = false;

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!this.level().isClientSide) {

            BlockPos bPos = new BlockPos($$0.getBlockPos().getX(), $$0.getBlockPos().getY() + 1, $$0.getBlockPos().getZ());
            if (canPlaceGoo(bPos)) {
                this.level().setBlockAndUpdate(bPos, ModBlocks.GASOLINE_SPLATTER.defaultBlockState().setValue(ModBlocks.GAS_CAN_LEVEL, Integer.valueOf(2)));
            }

            if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof CampfireBlock ||
                    this.level().getBlockState(bPos).getBlock() instanceof FireBlock ||
                    this.level().getBlockState(bPos).getFluidState().is(Fluids.LAVA) ||
                    this.level().getBlockState(bPos).getFluidState().is(Fluids.FLOWING_LAVA)
            ){
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                        40, 0.0, 0.2, 0.0, 0.2);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                        1, 0.5, 0.5, 0.5, 0.2);
                MainUtil.gasExplode(null, (ServerLevel) this.level(), this.getOnPos(), 0, 2, 4, MainUtil.gasDamageMultiplier()*14);
            } else {
                ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                        15, 0.4, 0.4, 0.25, 0.4);
                SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
                this.playSound($$6, 1F, 1.5F);

                List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox(this.level(), $$0.getBlockPos().getX(), $$0.getBlockPos().getY(),
                        $$0.getBlockPos().getZ(), 2, 2, 2));
                if (!entities.isEmpty()) {
                    for (Entity value : entities) {
                        if (value instanceof LivingEntity){
                            ((StandUser) value).roundabout$setGasolineTime(((StandUser) value).roundabout$getMaxGasolineTime());
                        }
                    }
                }
            }

            this.discard();
        }
    }

    public boolean canPlaceGoo(BlockPos pos){
        BlockPos blk =  new BlockPos(pos.getX(), pos.getY(), pos.getZ());

        if (this.level().isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            if (this.level().getBlockState($$8).isFaceSturdy(this.level(), $$8, Direction.UP)) {
                return true;
            }
        }

        return false;
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

            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            if ($$0.getEntity() instanceof LivingEntity) {
                ((StandUser) $$0.getEntity()).roundabout$setGasolineTime(((StandUser) $$0.getEntity()).roundabout$getMaxBucketGasolineTime());
            }
            List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox(this.level(), $$0.getEntity().getX(),  $$0.getEntity().getY(),
                    $$0.getEntity().getZ(), 2, 2, 2));
            if (!entities.isEmpty()) {
                for (Entity value : entities) {
                    if (value instanceof LivingEntity){
                        ((StandUser) value).roundabout$setGasolineTime(((StandUser) value).roundabout$getMaxBucketGasolineTime());
                    }
                }
            }
            this.discard();
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
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}
