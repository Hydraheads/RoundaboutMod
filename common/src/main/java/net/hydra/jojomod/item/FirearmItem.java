package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.access.IGEProjectile;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public abstract class FirearmItem extends Item {

    public static final String AMMO_COUNT_TAG = "AmmoCount";
    public static final String RELOADING_TAG = "IsReloading";
    public static final String TREE_IMBUED_SHOTS_TAG = "TreeImbuedShots";
    public static final String TREE_IMBUED_OWNER_TAG = "TreeImbuedOwner";
    public static boolean cycleReload = false;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public FirearmItem(Item.Properties $$3,float speed) {
        super($$3);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", speed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    public FirearmItem(Item.Properties $$3) {
        super($$3);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -1.3, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();

    }
    // checks if there's ammo in inventory
    protected boolean hasAmmoInInventory(Player player) {
        if (canWhitesnakeUse() && WhitesnakeControlInventory.isActive(player)) {
            return WhitesnakeControlInventory.hasAmmo(player);
        }
        Inventory inv = player.getInventory();
        for (ItemStack stack : inv.items) {
            if (stack.getItem() == getAmmoItem() && stack.getCount() > 0) return true;
        }
        for (ItemStack stack : inv.offhand) {
            if (stack.getItem() == getAmmoItem() && stack.getCount() > 0) return true;
        }
        return player.isCreative();
    }

    // ammo consumption
    protected int consumeAmmoFromInventory(Player player, ItemStack gunStack, int amount) {
        if (canWhitesnakeUse() && WhitesnakeControlInventory.isActive(player)) {
            return player.isCreative() ? getMaxAmmo() : WhitesnakeControlInventory.consumeAmmo(player, amount);
        }
        Inventory inv = player.getInventory();
        int consumed = 0;

        // prioritizes imbue shots first
        if (!gunStack.isEmpty()) {
            for (int i = 0; i < inv.offhand.size() && amount > 0; i++) {
                ItemStack stack = inv.offhand.get(i);
                if (stack.getItem() == getAmmoItem() && PowersGoldExperience.isTreeImbuedProjectileItem(stack)) {
                    int remove = Math.min(stack.getCount(), amount);
                    int currentImbued = gunStack.getOrCreateTag().getInt(TREE_IMBUED_SHOTS_TAG);
                    gunStack.getOrCreateTag().putInt(TREE_IMBUED_SHOTS_TAG, currentImbued + remove);
                    CompoundTag imbue = PowersGoldExperience.getImbueData(stack);
                    if (imbue != null && imbue.hasUUID("Owner")) {
                        gunStack.getOrCreateTag().putUUID(TREE_IMBUED_OWNER_TAG, imbue.getUUID("Owner"));
                    }
                    PowersGoldExperience.clearImbueFromConsumedStack(player.level(), stack);
                    if (!player.isCreative()) {
                        stack.shrink(remove);
                    }
                    consumed += remove;
                    amount -= remove;
                }
            }
            for (int i = 0; i < inv.items.size() && amount > 0; i++) {
                ItemStack stack = inv.items.get(i);
                if (stack.getItem() == getAmmoItem() && PowersGoldExperience.isTreeImbuedProjectileItem(stack)) {
                    int remove = Math.min(stack.getCount(), amount);
                    int currentImbued = gunStack.getOrCreateTag().getInt(TREE_IMBUED_SHOTS_TAG);
                    gunStack.getOrCreateTag().putInt(TREE_IMBUED_SHOTS_TAG, currentImbued + remove);
                    CompoundTag imbue = PowersGoldExperience.getImbueData(stack);
                    if (imbue != null && imbue.hasUUID("Owner")) {
                        gunStack.getOrCreateTag().putUUID(TREE_IMBUED_OWNER_TAG, imbue.getUUID("Owner"));
                    }
                    PowersGoldExperience.clearImbueFromConsumedStack(player.level(), stack);
                    if (!player.isCreative()) {
                        stack.shrink(remove);
                    }
                    consumed += remove;
                    amount -= remove;
                }
            }
        }
        for (int i = 0; i < inv.items.size() && amount > 0; i++) {
            ItemStack stack = inv.items.get(i);
            if (stack.getItem() == getAmmoItem() && !player.isCreative()) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;
                if (amount <= 0) break;
            }
        }
        for (int i = 0; i < inv.offhand.size() && amount > 0; i++) {
            ItemStack stack = inv.offhand.get(i);
            if (stack.getItem() == getAmmoItem() && !player.isCreative()) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;
                if (amount <= 0) break;
            }
        }
        return player.isCreative() ? getMaxAmmo() : consumed;
    }

    // reload start logic
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(itemStack.getItem() instanceof FirearmItem)) return InteractionResultHolder.fail(itemStack);


        if ((isCrouchingOrSomething(player, itemStack) && hasAmmoInInventory(player) && getAmmo(itemStack) != getMaxAmmo()) || (isCrouchingOrSomething(player, itemStack) && player.isCreative())) {
            if (!isReloading(itemStack)) {
                setReloading(itemStack, true);
                player.getCooldowns().addCooldown(this, getReloadCooldown());
                player.stopUsingItem();
                ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(getReloadSound(), 10, false);
            }
            return InteractionResultHolder.consume(itemStack);
        } else {
            if (isCrouchingOrSomething(player, itemStack) && getAmmo(itemStack) == getMaxAmmo()) {
                if (player instanceof ServerPlayer SP) SP.displayClientMessage(Component.translatable("text.roundabout.already_reloaded").withStyle(ChatFormatting.GRAY), true);
            } else if (isCrouchingOrSomething(player, itemStack) && getAmmo(itemStack) != getMaxAmmo() && !hasAmmoInInventory(player)) {
                if (player instanceof ServerPlayer SP) SP.displayClientMessage(Component.translatable("text.roundabout.no_more_usable_ammo").withStyle(ChatFormatting.GRAY), true);
            } else {
                player.startUsingItem(hand);
            }
        }
        return InteractionResultHolder.consume(itemStack);
    }

    // reload tick logic
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!(entity instanceof Player player) || level.isClientSide) return;

        if (isReloading(stack)) {
            if (player.hurtTime == player.hurtDuration - 1) {
                DamageSource last = player.getLastDamageSource();
                if (last != null && last.getEntity() != null && !(last.getDirectEntity() instanceof Projectile)) {
                    cancelReload(stack, player);
                    return;
                }
            }
        }

        if (isReloading(stack) && player.getMainHandItem() != stack && player.getOffhandItem() != stack) {
            cancelReload(stack, player);
            return;
        }

        if (isReloading(stack) && !player.getCooldowns().isOnCooldown(this) && (player.getMainHandItem() == stack || player.getOffhandItem() == stack)) {
            int ammoNeeded = getMaxAmmo() - getAmmo(stack);
            int ammoLoaded = consumeAmmoFromInventory(player, stack, ammoNeeded);

            if (ammoLoaded > 0) {
                setAmmo(stack, player.isCreative() ? getMaxAmmo() : getAmmo(stack) + ammoLoaded);
            }
            setReloading(stack, false);
        }
    }

    // firing logic
    public void fireBullet(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.getCooldowns().isOnCooldown(this)) return;
        if (ClientNetworking.getAppropriateConfig().timeStopSettings.timestopCancelsGuns &&
                ((TimeStop) player.level()).inTimeStopRange(player)
        ){
            return;
        }

        ItemStack itemStack = player.getItemInHand(hand);
        if (getAmmo(itemStack) > 0) {
            player.getCooldowns().addCooldown(this, getFireCooldown());
            itemStack.hurtAndBreak(1, player, p -> {});
            if (!player.isCreative()) setAmmo(itemStack, getAmmo(itemStack) - 1);

            LivingEntity shotSource = player;
            RoundaboutBulletEntity bullet = new RoundaboutBulletEntity(level, shotSource);

            // for whitesnake
            if (canWhitesnakeUse() && WhitesnakeControlInventory.isActive(player)
                    && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                    && powers.getPilotingStand() != null) {
                shotSource = powers.getPilotingStand();
                bullet.setPos(shotSource.getX(), shotSource.getEyeY() - 0.15D, shotSource.getZ());
            }

            int imbuedShots = itemStack.getOrCreateTag().getInt(TREE_IMBUED_SHOTS_TAG);
            configureBulletTrajectory(bullet, shotSource);
            bullet.setAmmoType(getBulletType());
            if (imbuedShots > 0) {
                itemStack.getOrCreateTag().putInt(TREE_IMBUED_SHOTS_TAG, imbuedShots - 1);
                ((IGEProjectile) bullet).roundabout$setGETreeImbue(true);
                if (itemStack.getOrCreateTag().hasUUID(TREE_IMBUED_OWNER_TAG)) {
                    ((IGEProjectile) bullet).roundabout$setGETreeImbueOwner(itemStack.getOrCreateTag().getUUID(TREE_IMBUED_OWNER_TAG));
                    if (imbuedShots - 1 <= 0) {
                        itemStack.getOrCreateTag().remove(TREE_IMBUED_OWNER_TAG);
                    }
                }
            }
            level.addFreshEntity(bullet);
            applyRecoil(player);

            level.playSound(null, player.blockPosition(), getFireSound(), SoundSource.PLAYERS, getFireSoundVolume(), getFireSoundPitch());

            if (level instanceof ServerLevel serverLevel) {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 offsets = getMuzzleOffset();
                double sideOffset = offsets.y;

                if ((player.getMainArm() == HumanoidArm.LEFT && player.getMainHandItem().getItem() == this) || (player.getMainArm() == HumanoidArm.RIGHT && player.getOffhandItem().getItem() == this)) {
                    sideOffset -= sideOffset * 2;
                }
                Vec3 pos = player.getEyePosition().add(look.scale(offsets.x)).add(0, offsets.z, 0); // x=forward, z=vertical
                spawnMuzzleFlash(serverLevel, pos);
            }

            if (((StandUser) player).roundabout$isBubbleEncased()) {
                if (!level.isClientSide()) {
                    ((StandUser) player).roundabout$setBubbleEncased((byte) 0);
                    level.playSound(null, player.blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                    ((ServerLevel) level).sendParticles(ModParticles.BUBBLE_POP, player.getX(), player.getY() + player.getBbHeight() * 0.5, player.getZ(), 5, 0.25, 0.25, 0.25, 0.025);
                }
            }
        } else {
            level.playSound(null, player, ModSounds.SNUBNOSE_DRY_FIRE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (player instanceof ServerPlayer SP) SP.displayClientMessage(Component.translatable("text.roundabout.out_of_bullets").withStyle(ChatFormatting.LIGHT_PURPLE), true);
        }
    }

    // cancels reloads
    public void cancelReload(ItemStack stack, Player player) {
        if (isReloading(stack)) {
            setReloading(stack, false);
            if (player != null) {
                ((StandUser) player).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.ITEM_GROUP, 10, false);
                player.getCooldowns().removeCooldown(stack.getItem());
                player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    // replaces punching and stuff with shooting the gun
    public boolean interceptAttack(ItemStack itemStack, Player player) {
        if (player != null && player.getUseItem() != null) {
            if (player.getUseItem() == itemStack) {
                return true;
            }
        }
        return false;
    }

    // cancels using item on swap
    @Override
    public void releaseUsing(ItemStack stack, Level dimension, LivingEntity livingEntity, int timeLeft) {
        if (!dimension.isClientSide && livingEntity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (!(player.getUseItem() == itemStack)) {
                player.stopUsingItem();
            }
        }
    }

    // muzzle flash for all guns
    protected void spawnMuzzleFlash(ServerLevel level, Vec3 position) { level.sendParticles(ModParticles.FLASH, position.x, position.y, position.z, 0, 255, 210, 17, 1.0); }

    // getters and setters overrides
    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }
    @Override
    public boolean isEnchantable(ItemStack p_41456_) { return false; }
    @Override
    public UseAnim getUseAnimation(ItemStack $$0) { return UseAnim.BOW; }

    // for ammo count
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Ammo: " + getAmmo(stack) + " / " + getMaxAmmo()).withStyle(ChatFormatting.GRAY));
    }

    // abstract getters and setters
    // you NEED to override these, even if you don't need to use them
    public abstract int getMaxAmmo();
    public abstract Item getAmmoItem();
    public abstract int getReloadCooldown();
    public abstract byte getReloadSound();

    public abstract int getFireCooldown();
    public abstract byte getBulletType();
    public abstract SoundEvent getFireSound();
    public abstract float getFireSoundVolume();
    public abstract float getFireSoundPitch();
    public abstract void applyRecoil(Player player);
    public abstract void configureBulletTrajectory(RoundaboutBulletEntity bullet, LivingEntity shooter);
    public abstract Vec3 getMuzzleOffset();
    // you're welcome Olive
    public abstract boolean canWhitesnakeUse();

    // getters and setters
    public int getAmmo(ItemStack stack) { return stack.getOrCreateTag().getInt(AMMO_COUNT_TAG); }
    public void setAmmo(ItemStack stack, int count) { stack.getOrCreateTag().putInt(AMMO_COUNT_TAG, count); }
    public boolean getReloading(ItemStack stack) { return stack.getOrCreateTag().getBoolean(RELOADING_TAG); }
    public void setReloading(ItemStack stack, boolean value) { stack.getOrCreateTag().putBoolean(RELOADING_TAG, value); }
    public boolean isReloading(ItemStack stack) { return stack.getOrCreateTag().getBoolean(RELOADING_TAG); }
    public boolean isCrouchingOrSomething(Player player, ItemStack stack){ return (player.isCrouching() && player.getUseItem() != stack) || cycleReload; }
}