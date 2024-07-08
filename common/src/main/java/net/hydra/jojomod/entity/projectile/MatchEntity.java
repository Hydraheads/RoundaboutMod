package net.hydra.jojomod.entity.projectile;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.mixin.ZFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MatchEntity extends ThrowableItemProjectile {
    public MatchEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public MatchEntity(LivingEntity living, Level $$1) {
        super(ModEntities.THROWN_MATCH, living, $$1);
    }
    @Override
    protected Item getDefaultItem() {
        return ModItems.MATCH;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);

        BlockState state = this.level().getBlockState($$0.getBlockPos());
        Block block = state.getBlock();

        if (block instanceof GasolineBlock) {
            if (!this.level().isClientSide) {
                ((GasolineBlock) block).prime(state, (ServerLevel) this.level(), $$0.getBlockPos(), 0);
            }
        } else if(((IFireBlock)Blocks.FIRE).roundabout$canBurn(state)){
            if (block instanceof TntBlock) {
                this.level().removeBlock($$0.getBlockPos(), false);
                TntBlock.explode(this.level(), $$0.getBlockPos());
            }
        } else {
            SoundEvent $$6 = SoundEvents.WOOD_STEP;
            this.playSound($$6, 0.5F, 2F);
        }
        this.discard();
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        float $$2 = 2.0f;

        Entity $$4 = this.getOwner();

        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.MATCH, $$4);

        SoundEvent $$6 = SoundEvents.FIRE_EXTINGUISH;
        Vec3 DM = $$1.getDeltaMovement();

        if ($$1.getType() == EntityType.TNT_MINECART) {
            DamageSource DS = $$1.damageSources().explosion($$1, $$0.getEntity());
            ((IMinecartTNT)$$1).roundabout$explode(DS, this.getDeltaMovement().lengthSqr());
            this.discard();
        } else if ($$1.hurt($$5, $$2)) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if ($$1 instanceof LivingEntity $$7) {
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
            }
            this.playSound($$6, 0.8F, 1.6F);
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
