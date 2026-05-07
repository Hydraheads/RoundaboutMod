package net.hydra.jojomod.entity.projectile;

import com.mojang.authlib.yggdrasil.response.User;
import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersEmperor;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.UUID;

public class EmperorBulletEntity extends AbstractArrow {
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean isPickable() {
        return false;
    }

    public EmperorBulletEntity(EntityType<? extends EmperorBulletEntity> type, Level level) {
        super(type, level);
    }

    public EmperorBulletEntity(Level level, LivingEntity livingEntity) {
        super(ModEntities.ROUNDABOUT_BULLET_ENTITY, livingEntity, level);
    }
    public EmperorBulletEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.ROUNDABOUT_BULLET_ENTITY, p_36862_, p_36863_, p_36864_, $$0);
    }

    public LivingEntity standUser;

    private float getBulletDamage() {
        LivingEntity owner = (LivingEntity) this.getOwner();

        if (owner instanceof StandUser su) {
            StandPowers powers = su.roundabout$getStandPowers();
            if (powers instanceof PowersEmperor emperor) {
                return emperor.getEmperorBulletStrength(owner);
            }
        }

        return 3.0F;
    }

    @Override
    public Entity getOwner() {
        return super.getOwner();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 200) {
            this.discard();
            return;
        }

        this.setNoGravity(true);

        this.setDeltaMovement(this.getDeltaMovement().scale(0.995));

        if (!level().isClientSide && !this.inGround) {
            boolean isFlying = getDeltaMovement().lengthSqr() > 1;

            if (isFlying) {
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.2F, 0.2F, 0.2F), 1f), this.getX(), this.getY(), this.getZ(), 0, 0, 0, 0, 0);
            }
        }
    }



    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (!level().isClientSide && entity instanceof EnderMan em) {
            if (((IEnderMan) em).roundabout$teleport()) {
                return;
            }
        }

        if (entity instanceof LivingEntity $$3) {
            StandPowers entityPowers = ((StandUser) $$3).roundabout$getStandPowers();
            if (entityPowers != null ) {
                if (entityPowers.dealWithProjectile(this, result)) {
                    return;
                }
            }
        }



        if (entity instanceof LivingEntity livingEntity) {

            boolean hadIFrames = livingEntity.invulnerableTime > 0 || livingEntity.hurtTime > 0;

            if (livingEntity.isInvulnerable()) {
                return;
            }

            float damage = getBulletDamage();


            boolean didDamage = livingEntity.hurt(ModDamageTypes.of(level(), ModDamageTypes.BULLET, this, this.getOwner()), damage);

            if (didDamage) {
                applyEffect(livingEntity);
            }
        }



        Entity $$2 = this.getOwner();
        DamageSource damageSource = ModDamageTypes.of(level(), ModDamageTypes.BULLET, this, $$2);

        if ($$2 instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(entity);
        }

        this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.BULLET_PENTRATION_EVENT, this.getSoundSource(), 1.0F, 1.0F);
        this.discard();
    }

    public void applyEffect(LivingEntity target) {
        if (!MainUtil.isBossMob(target) && !(target instanceof RoadRollerEntity)) {
            if (MainUtil.getMobBleed(target)) {
                ((StandUser) target).roundabout$setBleedLevel(1);
                target.addEffect(new MobEffectInstance(ModEffects.BLEED, 400, 0), this);
            }
        }
    }
}