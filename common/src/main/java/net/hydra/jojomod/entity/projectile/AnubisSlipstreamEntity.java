package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

public class AnubisSlipstreamEntity extends Entity {

    AnubisSlipstreamEntity lastSlipstream;
    public AnubisSlipstreamEntity(EntityType<?> $$0, Level $$1, int lifetime,AnubisSlipstreamEntity lastSlipstream) {
        super($$0, $$1);
        this.lastSlipstream = lastSlipstream;
        setLifetime(lifetime);
    }
    public AnubisSlipstreamEntity(EntityType<?> $$0, Level $$1) {
        this($$0, $$1,90,null);
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


            if (this.lastSlipstream != null) {
                final double iterations = 4;
                for (int i=0;i<iterations;i++) {
                    Vec3 vec3 = this.getPosition(0F).lerp(this.lastSlipstream.getPosition(0F),i/iterations);
                    spawnParticles(vec3.x,vec3.y,vec3.z);
                }
            }

           /* if (this.tickCount%4 == 0) {
                ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                        this.getX()+(Math.random()*3-1.5),
                        this.getY()+(Math.random()*3-1.5) + 0.5F,
                        this.getZ()+(Math.random()*3-1.5),
                        0, 0, 0, 0, 0);
            } */
        }
    }

    private void spawnParticles(double x, double y, double z) {
        BlockPos blockPos = new BlockPos((int)x,(int)y,(int)z);
        for(int i=0;i<2;i++) {
            if (this.level().getBlockState(blockPos).isAir()) {
                blockPos = blockPos.below();
            } else {
                break;
            }
        }
        if (this.tickCount % 2 == 1) {
            BlockState blockState = this.level().getBlockState(blockPos);
            if (!blockState.isAir()) {
                ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, blockPos.getY() + 0.8, z,
                        0, 0, 0, 0, 0);
            }
        }
    }

    public int getLifetime() {return this.getEntityData().get(LIFETIME);}
    public void setLifetime(int i) {this.getEntityData().set(LIFETIME,i);}

    @Override
    public void playerTouch(Player $$0) {
        StandUser SU = (StandUser) $$0;
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
