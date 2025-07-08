package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ThrownWaterBottleEntity extends ThrowableItemProjectile implements ItemSupplier {
    public static final double SPLASH_RANGE = 4.0;
    private static final double SPLASH_RANGE_SQ = 16.0;
    public static final Predicate<LivingEntity> WATER_SENSITIVE_OR_ON_FIRE = $$0 -> $$0.isSensitiveToWater() || $$0.isOnFire();

    public ThrownWaterBottleEntity(EntityType<? extends ThrownWaterBottleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ThrownWaterBottleEntity(Level $$0, LivingEntity $$1) {
        super(ModEntities.THROWN_WATER_BOTTLE, $$1, $$0);
    }

    public ThrownWaterBottleEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(ModEntities.THROWN_WATER_BOTTLE, $$1, $$2, $$3, $$0);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.POTION;
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
    }

    public void splashWater(){
        AABB $$0 = this.getBoundingBox().inflate(4.0, 2.0, 4.0);

        for (LivingEntity $$2 : this.level().getEntitiesOfClass(LivingEntity.class, $$0, WATER_SENSITIVE_OR_ON_FIRE)) {
            double $$3 = this.distanceToSqr($$2);
            if ($$3 < 16.0) {
                if ($$2.isSensitiveToWater()) {
                    $$2.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 1.0F);
                }

                if ($$2.isOnFire() && $$2.isAlive()) {
                    $$2.extinguishFire();
                }
            }
        }

        for (Axolotl $$5 : this.level().getEntitiesOfClass(Axolotl.class, $$0)) {
            $$5.rehydrate();
        }
        for (SurvivorEntity $$5 : this.level().getEntitiesOfClass(SurvivorEntity.class, $$0)) {
            $$5.setActivated(true);
        }
    }

    @Override
    protected void onHit(HitResult $$0) {
        super.onHit($$0);
        if (!this.level().isClientSide) {
            ItemStack $$1 = this.getItem();
            Potion $$2 = PotionUtils.getPotion($$1);
            List<MobEffectInstance> $$3 = PotionUtils.getMobEffects($$1);
            splashWater();

            int $$5 = $$2.hasInstantEffects() ? 2007 : 2002;
            this.level().levelEvent($$5, this.blockPosition(), PotionUtils.getColor($$1));
            this.discard();
        }
    }


    private void makeAreaOfEffectCloud(ItemStack $$0, Potion $$1) {
        AreaEffectCloud $$2 = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        Entity $$3 = this.getOwner();
        if ($$3 instanceof LivingEntity) {
            $$2.setOwner((LivingEntity)$$3);
        }

        $$2.setRadius(3.0F);
        $$2.setRadiusOnUse(-0.5F);
        $$2.setWaitTime(10);
        $$2.setRadiusPerTick(-$$2.getRadius() / (float)$$2.getDuration());
        $$2.setPotion($$1);

        for (MobEffectInstance $$4 : PotionUtils.getCustomEffects($$0)) {
            $$2.addEffect(new MobEffectInstance($$4));
        }

        CompoundTag $$5 = $$0.getTag();
        if ($$5 != null && $$5.contains("CustomPotionColor", 99)) {
            $$2.setFixedColor($$5.getInt("CustomPotionColor"));
        }

        this.level().addFreshEntity($$2);
    }

    private boolean isLingering() {
        return this.getItem().is(Items.LINGERING_POTION);
    }

    private void dowseFire(BlockPos $$0) {
        BlockState $$1 = this.level().getBlockState($$0);
        if ($$1.is(BlockTags.FIRE)) {
            this.level().removeBlock($$0, false);
        } else if (AbstractCandleBlock.isLit($$1)) {
            AbstractCandleBlock.extinguish(null, $$1, this.level(), $$0);
        } else if (CampfireBlock.isLitCampfire($$1)) {
            this.level().levelEvent(null, 1009, $$0, 0);
            CampfireBlock.dowse(this.getOwner(), this.level(), $$0, $$1);
            this.level().setBlockAndUpdate($$0, $$1.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }
    }
}