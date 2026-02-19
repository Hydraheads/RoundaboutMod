package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.entity.goals.CorpseMeleeAttackGoal;
import net.hydra.jojomod.entity.goals.SwellGoalFallen;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Collection;

public class FallenCreeper extends FallenMob implements PowerableMob {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(FallenCreeper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(FallenCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(FallenCreeper.class, EntityDataSerializers.BOOLEAN);
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;
    private int droppedSkulls;
    public FallenCreeper(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public String getData(){
        return "creeper";
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SwellGoalFallen(this));
        this.goalSelector.addGoal(3, new CorpseMeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.addBehaviourGoals();
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);

    }

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(DATA_SWELL_DIR)) {
            super.defineSynchedData();
            this.entityData.define(DATA_SWELL_DIR, -1);
            this.entityData.define(DATA_IS_POWERED, false);
            this.entityData.define(DATA_IS_IGNITED, false);
        }
    }


    @Override
    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    public float getSwelling(float $$0) {
        return Mth.lerp($$0, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int $$0) {
        this.entityData.set(DATA_SWELL_DIR, $$0);
    }

    @Override
    public void thunderHit(ServerLevel $$0, LightningBolt $$1) {
        super.thunderHit($$0, $$1);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    @Override
    protected InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$2.is(ItemTags.CREEPER_IGNITERS) && getActivated()) {
            SoundEvent $$3 = $$2.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.level().playSound($$0, this.getX(), this.getY(), this.getZ(), $$3, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level().isClientSide) {
                this.ignite();
                if (!$$2.isDamageableItem()) {
                    $$2.shrink(1);
                } else {
                    $$2.hurtAndBreak(1, $$0, $$1x -> $$1x.broadcastBreakEvent($$1));
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract($$0, $$1);
        }
    }
    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float $$0 = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * $$0, Level.ExplosionInteraction.MOB);
            this.discard();
            this.spawnLingeringCloud();
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> $$0 = this.getActiveEffects();
        if (!$$0.isEmpty()) {
            AreaEffectCloud $$1 = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            $$1.setRadius(2.5F);
            $$1.setRadiusOnUse(-0.5F);
            $$1.setWaitTime(10);
            $$1.setDuration($$1.getDuration() / 2);
            $$1.setRadiusPerTick(-$$1.getRadius() / (float)$$1.getDuration());

            for (MobEffectInstance $$2 : $$0) {
                $$1.addEffect(new MobEffectInstance($$2));
            }

            this.level().addFreshEntity($$1);
        }
    }

    @Override
    public void tick() {


        if (((StandUser)this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            if (this.isAlive()) {
                oldSwell = swell;
            }

            this.swell -= 1;
            if (this.swell < 0) {
                this.swell = 0;
            }
            super.tick();
            return;
        }


        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int $$0 = this.getSwellDir();
            if ($$0 > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += $$0;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }

        super.tick();
    }

    public boolean doHurtTarget(Entity p_32281_) {
        return true;
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (this.entityData.get(DATA_IS_POWERED)) {
            $$0.putBoolean("powered", true);
        }

        $$0.putShort("Fuse", (short)this.maxSwell);
        $$0.putByte("ExplosionRadius", (byte)this.explosionRadius);
        $$0.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        this.entityData.set(DATA_IS_POWERED, $$0.getBoolean("powered"));
        if ($$0.contains("Fuse", 99)) {
            this.maxSwell = $$0.getShort("Fuse");
        }

        if ($$0.contains("ExplosionRadius", 99)) {
            this.explosionRadius = $$0.getByte("ExplosionRadius");
        }

        if ($$0.getBoolean("ignited")) {
            this.ignite();
        }
    }
    @Override
    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    @Override
    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        boolean $$3 = super.causeFallDamage($$0, $$1, $$2);
        this.swell += (int)($$0 * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return $$3;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()){
            return SoundEvents.CREEPER_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()){
            return SoundEvents.CREEPER_DEATH;
        } else {
            return super.getDeathSound();
        }
    }
    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }
}