package net.hydra.jojomod.entity.projectile;


import com.google.common.collect.Sets;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Set;

public class GasolineCanEntity extends ThrowableItemProjectile {

    public float spinningCanX = 0;
    public float spinningCanXo = 0;
    public boolean done;
    public float bounces = 3;
    public GasolineCanEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GasolineCanEntity(LivingEntity living, Level $$1) {
        super(ModEntities.GASOLINE_CAN, living, $$1);
    }

    @Override
    public void tick(){
        spinningCanX = Mth.wrapDegrees(spinningCanX-15);
        super.tick();
    }
    @Override
    protected Item getDefaultItem() {
        return ModItems.GASOLINE_CAN;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        bounces--;
        if (bounces < 0 || !$$0.getDirection().equals(Direction.UP)) {
            super.onHitBlock($$0);

            if (!done){
                SoundEvent $$6 = ModSounds.CAN_BOUNCE_END_EVENT;
                this.playSound($$6, 1F, 1F);

            }
            BlockState state = this.level().getBlockState($$0.getBlockPos());
            Block block = state.getBlock();


            if (((IFireBlock) Blocks.FIRE).roundabout$canBurn(state)) {
                if (block instanceof TntBlock) {
                    this.level().removeBlock($$0.getBlockPos(), false);
                    TntBlock.explode(this.level(), $$0.getBlockPos());
                }
            } else {
                SoundEvent $$6 = SoundEvents.WOOD_STEP;
                this.playSound($$6, 0.5F, 2F);
            }
            this.discard();
        } else {
            SoundEvent $$6 = ModSounds.CAN_BOUNCE_EVENT;
            float volume = 1F;
            float pitch = 1F;
            if (bounces < 1){
                done=true;
                $$6 = ModSounds.CAN_BOUNCE_END_EVENT;
            } else {
                if (bounces == 1){
                    volume = 0.9F;
                    pitch = 0.9F;
                }
            }
            this.playSound($$6, volume, pitch);
            this.setDeltaMovement(this.getDeltaMovement().x, 0.18+(0.04*bounces), this.getDeltaMovement().z);

        }
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
