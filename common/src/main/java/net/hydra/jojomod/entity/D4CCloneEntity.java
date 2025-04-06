package net.hydra.jojomod.entity;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.UUID;

public class D4CCloneEntity extends CloneEntity implements NeutralMob {
    public D4CCloneEntity(EntityType<? extends PathfinderMob> entity, Level world) {
        super(entity, world);
    }

    private static final EntityDataAccessor<Boolean> SELECTED =
            SynchedEntityData.defineId(D4CCloneEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public void tick() {
        /* weirdly specific bug fix */
        if (this.player == null)
        {
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        if (!this.level().isClientSide()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem());
            this.setItemInHand(InteractionHand.OFF_HAND, player.getOffhandItem());
        }
        else {
            ItemStack mainHand = getMainHandItem();
            if (mainHand.is(ItemTags.PICKAXES))
            {

            } else if (mainHand.is(ItemTags.SWORDS)) {

            }

            Minecraft client = Minecraft.getInstance();

            if (client.player == null)
                return;

            StandUser localPlayer = ((StandUser)client.player);
            if (localPlayer.roundabout$getStand() instanceof D4CEntity)
            {
                if (this.player == client.player)
                {
                    PowersD4C powers = (PowersD4C) localPlayer.roundabout$getStandPowers();
                    this.setSelected(powers.targetingClone == this);
                }
            }
        }

        super.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, this::shouldAngerAt));
    }

    private boolean shouldAngerAt(LivingEntity entity) {
        if (entity instanceof Enemy)
            return true;

        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        ItemStack mainHand = this.getMainHandItem();
        float hurtAmount = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (!mainHand.isEmpty())
        {
            hurtAmount += mainHand.getDamageValue();
            Roundabout.LOGGER.info("hurtAmount: {}", hurtAmount);
        }

        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

        boolean hurt = target.hurt(this.damageSources().mobAttack(this), hurtAmount);

        if (hurt && knockback > 0.0F && target instanceof LivingEntity) {
            ((LivingEntity)target)
                    .knockback(
                            (double)(knockback * 0.5F),
                            (double) Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
                            (double)(-Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)))
                    );
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
        }

        return hurt;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SELECTED, false);
    }

    private Vector3f sizeOffset = new Vector3f(0.f, 0.f, 0.f);
    public void setSizeOffset(Vector3f value) { this.sizeOffset = value; }
    public Vector3f getSizeOffset() { return this.sizeOffset; }

    public void setSelected(boolean value) { this.entityData.set(SELECTED, value); }
    public boolean isSelected() { return this.getEntityData().get(SELECTED); }

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int $$0) {
        this.remainingPersistentAngerTime = $$0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID $$0) {
        this.persistentAngerTarget = $$0;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
}