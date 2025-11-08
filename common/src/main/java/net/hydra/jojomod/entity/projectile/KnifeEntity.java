package net.hydra.jojomod.entity.projectile;

import com.google.common.collect.Sets;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class KnifeEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(KnifeEntity.class, EntityDataSerializers.BOOLEAN);
    private final Set<MobEffectInstance> effects = Sets.newHashSet();

    private ItemStack knifeItem = new ItemStack(ModItems.KNIFE);

    public KnifeEntity(EntityType<? extends KnifeEntity> entity,  Level world) {
        super(entity, world);
    }
    public KnifeEntity(Level world, LivingEntity player, ItemStack itemStack) {
        super(ModEntities.THROWN_KNIFE, player, world);
        this.knifeItem = itemStack.copy();
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }
    public KnifeEntity(Level world, LivingEntity player, ItemStack itemStack, Vec3 pos) {
        super(ModEntities.THROWN_KNIFE, player, world);
        this.knifeItem = itemStack.copy();
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
        this.setPos(pos);
    }

    public KnifeEntity(Level world, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.THROWN_KNIFE, p_36862_, p_36863_, p_36864_, world);
    }

    public KnifeEntity(Level world, LivingEntity entity) {
        super(ModEntities.THROWN_KNIFE, entity, world);
    }


    public void tick() {
        super.tick();
    }
    protected ItemStack getPickupItem() {
         return new ItemStack(ModItems.KNIFE);
    }


    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(ID_FOIL)) {
            super.defineSynchedData();
            this.entityData.define(ID_FOIL, false);
        }
    }
    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
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
    protected void onHitBlock(BlockHitResult $$0) {
        ((IAbstractArrowAccess)this).roundabout$setLastState(this.level().getBlockState($$0.getBlockPos()));
        BlockState BSS = this.level().getBlockState($$0.getBlockPos());
        BSS.onProjectileHit(this.level(), BSS, $$0, this);
        Vec3 $$1 = $$0.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement($$1);
        Vec3 $$2 = $$1.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - $$2.x, this.getY() - $$2.y, this.getZ() - $$2.z);
        this.playSound(ModSounds.KNIFE_IMPACT_GROUND_EVENT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);
        ((IAbstractArrowAccess)this).roundabout$resetPiercedEntities();
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        if ($$1 instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$getStandPowers().dealWithProjectile(this,$$0)){
                this.discard();
                return;
            }
        }
        float $$2;

        if ($$1 instanceof Player) {
            $$2 = (float) (2.29F * (ClientNetworking.getAppropriateConfig().itemSettings.knifeDamageOnPlayers *0.01));
        } else {
            $$2 = (float) (4.0F * (ClientNetworking.getAppropriateConfig().itemSettings.knifeDamageOnMobs *0.01));;
        }

        if ($$1 instanceof LivingEntity $$3) {
            int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, $$3);
            $$2 = (float) ($$2 * (1-(f*0.03)));


            $$2 += EnchantmentHelper.getDamageBonus(this.knifeItem, $$3.getMobType());
        }

        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.KNIFE, $$4);
        SoundEvent $$6 = ModSounds.KNIFE_IMPACT_EVENT;
        Vec3 DM = $$1.getDeltaMovement();
        if ($$1.hurt($$5, $$2)) {

            if ($$4 instanceof LivingEntity LE) {
                LE.setLastHurtMob($$1);
            }
                if (MainUtil.getMobBleed($$1)){
                    ((StandUser)$$1).roundabout$setBleedLevel(0);
                    ((LivingEntity)$$1).addEffect(new MobEffectInstance(ModEffects.BLEED, 400, 0), this);
                }
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if ($$1 instanceof LivingEntity $$7) {
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
                if ($$4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, $$4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                }

                this.doPostHurtEffects($$7);
            }
            this.playSound($$6, 1.0F, (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        }

    }

}
