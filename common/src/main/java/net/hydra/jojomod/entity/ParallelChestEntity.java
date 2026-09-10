package net.hydra.jojomod.entity;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
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

    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }
    public void addItemToPlayer(Entity ent, ItemStack stack){
        if (ent instanceof Player PE) {
            if (canAddItem(stack, PE.getInventory()) && PE.isAlive()) {
                PE.addItem(stack);
            } else {
                ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                        this.getY() + this.getEyeHeight(), this.getZ(),
                        stack);
                $$4.setPickUpDelay(40);
                $$4.setThrower(PE.getUUID());
                PE.level().addFreshEntity($$4);
            }
        } else {
            ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                    this.getY() + this.getEyeHeight(), this.getZ(),
                    stack);
            $$4.setPickUpDelay(40);
            this.level().addFreshEntity($$4);
        }
    }
    @Override
    public InteractionResult interactAt(Player player, Vec3 location, InteractionHand intHand) {
        if (!player.level().isClientSide() && player.level() instanceof ServerLevel sl
        && player instanceof ServerPlayer sp) {
            if (getOpened()) {
                return InteractionResult.CONSUME;
            }
            this.playSound(ModSounds.SPECIAL_CHEST_EVENT);
            this.playSound(SoundEvents.CHEST_OPEN);
            setOpened(true);
            for (int i = 0; i < 10; i++) {

                double angle = (Math.PI * 2.0D / 10.0D) * i;
                double radius = 0.5D;

                float circleX = (float)(Math.cos(angle) * radius);
                float circleZ = (float)(Math.sin(angle) * radius);

                MainUtil.sendParticlesIfPossible(
                        sp, sl, ParticleTypes.END_ROD,
                        this.getX(), this.getY(), this.getZ(),
                        0,
                        circleX, 2D, circleZ,
                        0.2D
                );
            }
            if (getAmmo()){
                refillAGun(sp);
                sp.displayClientMessage(Component.translatable("text.roundabout.parallel_chest.gun_restore"), true);
            } else {
                addItemToPlayer(player,MainUtil.getRandomD4CLoot(player.level()));
                sp.displayClientMessage(Component.translatable("text.roundabout.parallel_chest.goody"), true);
            }
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
