package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Unique;

public class AnubisSlipstreamEntity extends Entity {
    public AnubisSlipstreamEntity(EntityType<?> $$0, Level $$1, int lifetime) {
        super($$0, $$1);
        setLifetime(lifetime);
    }
    public AnubisSlipstreamEntity(EntityType<?> $$0, Level $$1) {
        this($$0, $$1,60);
    }


    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(AnubisSlipstreamEntity.class,
            EntityDataSerializers.INT);
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(LIFETIME)) {
            this.entityData.define(LIFETIME,-1);
        }
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            int lifetime = this.getLifetime();
            if (lifetime > 0) {
                lifetime--;
                this.setLifetime(lifetime);
                if (lifetime == 0) {
                    this.discard();
                }
            }
            if (this.tickCount%4 == 0) {
                ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                        this.getX()+(Math.random()*3-1.5),
                        this.getY()+(Math.random()*3-1.5) + 0.5F,
                        this.getZ()+(Math.random()*3-1.5),
                        0, 0, 0, 0, 0);
            }
        }
    }

    public int getLifetime() {return this.getEntityData().get(LIFETIME);}
    public void setLifetime(int i) {this.getEntityData().set(LIFETIME,i);}

    @Override
    public void playerTouch(Player $$0) {
        StandUser SU = (StandUser) $$0;
        if (!level().isClientSide()) {
            // TODO: POTENTIALLY CHANGE THIS TO NOT BE AN EFFECT?
            if (!(SU.roundabout$getStandPowers() instanceof PowersAnubis)) {
                $$0.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1,10));
            }
        }
    }

    @Override
    public boolean isAttackable() {return false;}
    @Override
    public boolean isNoGravity() {return true;}
    @Override
    public boolean canBeHitByProjectile() {return false;}
    @Override
    public boolean isAlive() {return false;}
    @Override
    public boolean isPickable() {return false;}
    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    public boolean isInvulnerable() {
        return true;
    }
    @Override
    public boolean hurt(DamageSource $$0, float $$1) {return false;}

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {}
}
