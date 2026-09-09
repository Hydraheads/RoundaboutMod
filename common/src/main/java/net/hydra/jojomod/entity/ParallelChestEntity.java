package net.hydra.jojomod.entity;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class ParallelChestEntity extends Entity {

    public ParallelChestEntity(EntityType<? extends ParallelChestEntity>  $$0, Level $$1) {
        super($$0, $$1);
        this.fallDamageMax = 40;
        this.fallDamagePerDistance = 0.2F;
    }

    public static final float dimensions = 1F;

    public int tickDestroy = 0;
    public void tick(){
        super.tick();
        if (getOpened()) {
            tickDestroy++;
            if (!level().isClientSide()) {
                if (tickDestroy > 21){
                    discard();
                }
            } else {
            }
        }
    }
    public boolean isPickable() {
        return !this.isRemoved();
    }
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    private boolean hurtEntities;
    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        if (!this.hurtEntities) {
            return false;
        } else {
            int $$3 = Mth.ceil($$0 - 1.0F);
            if ($$3 < 0) {
                return false;
            } else {
                Predicate<Entity> $$4 = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);

                DamageSource var10000;
                var10000 = this.damageSources().fallingBlock(this);

                DamageSource $$6 = var10000;
                float $$7 = (float)Math.min(Mth.floor((float)$$3 * this.fallDamagePerDistance), this.fallDamageMax);
                this.level().getEntities(this, this.getBoundingBox(), $$4).forEach(($$2x) -> $$2x.hurt($$6, $$7));


                return false;
            }
        }
    }
    private int fallDamageMax;
    private float fallDamagePerDistance;
    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

    public void refillAGun(Player player){
        if (player != null){
            for(int $$5 = 0; $$5 < player.getInventory().getContainerSize(); ++$$5) {
                ItemStack $$6 = player.getInventory().getItem($$5);
                if ($$6.getItem() instanceof FirearmItem fi){
                    if (fi.getAmmo($$6) < fi.getMaxAmmo()){
                        fi.setAmmo($$6, fi.getMaxAmmo());
                        return;
                    }
                }
            }
        }
    }

    public static boolean needsARefill(Entity entity){
        if (entity instanceof Player player){
            for(int $$5 = 0; $$5 < player.getInventory().getContainerSize(); ++$$5) {
                ItemStack $$6 = player.getInventory().getItem($$5);
                if ($$6.getItem() instanceof FirearmItem fi){
                    if (fi.getAmmo($$6) < fi.getMaxAmmo()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 location, InteractionHand intHand) {
        if (!player.level().isClientSide()) {
            this.playSound(ModSounds.SPECIAL_CHEST_EVENT);
            this.playSound(SoundEvents.CHEST_OPEN);
            setOpened(true);
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(OPENED)) {
            this.entityData.define(OPENED, false);
            this.entityData.define(AMMO, false);
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("openedChest", getOpened());
        $$0.putBoolean("ammoChest", getAmmo());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.setOpened($$0.getBoolean("openedChest"));
        this.setAmmo($$0.getBoolean("ammoChest"));
    }

    public boolean getOpened() {
        return this.getEntityData().get(OPENED);
    }
    public void setOpened(boolean bool){
        this.entityData.set(OPENED, bool);
    }
    public boolean getAmmo() {
        return this.getEntityData().get(AMMO);
    }
    public void setAmmo(boolean bool){
        this.entityData.set(AMMO, bool);
    }
    private static final EntityDataAccessor<Boolean> OPENED =
            SynchedEntityData.defineId(ParallelChestEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> AMMO =
            SynchedEntityData.defineId(ParallelChestEntity.class, EntityDataSerializers.BOOLEAN);
}
