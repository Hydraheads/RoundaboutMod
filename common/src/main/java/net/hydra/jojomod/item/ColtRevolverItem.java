package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ColtRevolverItem extends FirearmItem implements Vanishable {

    public ColtRevolverItem(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    private static final String AMMO_COUNT_TAG = "AmmoCount";
    private static final String RELOADING_TAG = "IsReloading";

    private int getAmmo(ItemStack stack) {
        return stack.getOrCreateTag().getInt(AMMO_COUNT_TAG);
    }

    private void setAmmo(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(AMMO_COUNT_TAG, count);
    }

    private boolean getReloading(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(RELOADING_TAG);
    }

    private void setReloading(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(RELOADING_TAG, value);
    }

    int maxAmmo = 6;

    private boolean isReloading(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(RELOADING_TAG);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    private boolean hasSnubnoseAmmo(Player player) {
        Inventory inv = player.getInventory();

        for (ItemStack stack : inv.items) {
            if (stack.getItem() instanceof SnubnoseAmmoItem && stack.getCount() > 0) {
                return true;
            }
        }
        for (ItemStack stack : inv.offhand) {
            if (stack.getItem() instanceof SnubnoseAmmoItem && stack.getCount() > 0) {
                return true;
            }
        }

        if (player.isCreative()) {
            return true;
        }

        return false;
    }

    private int consumeSnubnoseAmmo(Player player, int amount) {
        Inventory inv = player.getInventory();
        int consumed = 0;

        for (int i = 0; i < inv.items.size() && amount > 0; i++) {
            ItemStack stack = inv.items.get(i);
            if (stack.getItem() instanceof SnubnoseAmmoItem && !player.isCreative()) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;

                if (amount <= 0) break;
            }
        }

        for (int i = 0; i < inv.offhand.size() && amount > 0; i++) {
            ItemStack stack = inv.offhand.get(i);
            if (stack.getItem() instanceof SnubnoseAmmoItem && !player.isCreative()) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;

                if (amount <= 0) break;
            }
        }

        if (player.isCreative()) {
            return maxAmmo;
        }

        return consumed;
    }

    public void cancelReload(ItemStack stack, Player player) {
        if (isReloading(stack)) {
            ((StandUser) player).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.ITEM_GROUP, 10, false);
            setReloading(stack, false);
            player.getCooldowns().removeCooldown(this);
            player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }



    @Override
    public void fireBullet(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.getCooldowns().isOnCooldown(this)) {
            return;
        }
        ItemStack itemStack = player.getItemInHand(hand);
        if (getAmmo(itemStack) > 0) {
            player.getCooldowns().addCooldown(this, 10);
            if (player.isCreative()) {
            } else {
                setAmmo(itemStack, getAmmo(itemStack) - 1);
            }
            LivingEntity livingEntity = player;
            RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(level, livingEntity);
            $$7.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.0F, 1.3F);
            $$7.setAmmoType(RoundaboutBulletEntity.COLT);
            level.addFreshEntity($$7);
            level.playSound(null, player, ModSounds.SNUBNOSE_FIRE_EVENT, SoundSource.PLAYERS, 100.0F, 1.0F);
            if (level instanceof ServerLevel serverLevel) {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 up = new Vec3(0, 1, 0);
                Vec3 right = look.cross(up).normalize();

                double forwardOffset = 1.0;
                double sideOffset = 0.24;
                double verticalOffset = -0.15;

                if ((player.getMainArm() == HumanoidArm.LEFT && player.getMainHandItem().getItem() instanceof ColtRevolverItem) || (player.getMainArm() == HumanoidArm.RIGHT && player.getOffhandItem().getItem() instanceof ColtRevolverItem)) {
                    sideOffset -= sideOffset * 2;
                }

                Vec3 pos = player.getEyePosition().add(look.scale(forwardOffset)).add(right.scale(sideOffset)).add(0, verticalOffset, 0);
                serverLevel.sendParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            if (livingEntity != null && ((StandUser) livingEntity).roundabout$isBubbleEncased()) {
                StandUser SE = ((StandUser) livingEntity);
                if (!level.isClientSide()) {
                    SE.roundabout$setBubbleEncased((byte) 0);
                    level.playSound(null, livingEntity.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                    ((ServerLevel) level).sendParticles(ModParticles.BUBBLE_POP,
                            livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(),
                            5, 0.25, 0.25, 0.25, 0.025);
                }
            }
        } else {
            level.playSound(null, player, ModSounds.SNUBNOSE_DRY_FIRE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (player instanceof ServerPlayer SP) {
                SP.displayClientMessage(Component.translatable("text.roundabout.out_of_bullets").withStyle(ChatFormatting.LIGHT_PURPLE), true);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(itemStack.getItem() instanceof ColtRevolverItem)) {
            return InteractionResultHolder.fail(itemStack);
        }
        if (!(player.getUseItem() == itemStack)) {
            if ((player.isCrouching() && hasSnubnoseAmmo(player) && getAmmo(itemStack) != maxAmmo) || (player.isCrouching() && player.isCreative())) {
                if (!isReloading(itemStack)) {
                    setReloading(itemStack, true);
                    player.getCooldowns().addCooldown(this, 60);
                    ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.REVOLVER_RELOAD, 10, false);
                }

                return InteractionResultHolder.consume(itemStack);
            } else {
                if (player.isCrouching() && getAmmo(itemStack) == maxAmmo) {
                    if (player instanceof ServerPlayer SP) {
                        SP.displayClientMessage(Component.translatable("text.roundabout.already_reloaded").withStyle(ChatFormatting.GRAY), true);
                    }
                } else if (player.isCrouching() && getAmmo(itemStack) != maxAmmo && !hasSnubnoseAmmo(player)) {
                    if (player instanceof ServerPlayer SP) {
                        SP.displayClientMessage(Component.translatable("text.roundabout.no_more_usable_ammo").withStyle(ChatFormatting.GRAY), true);
                    }
                } else {
                    player.startUsingItem(hand);
                }
            }
        }
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if (!(entity instanceof Player player)) return;
        if (level.isClientSide) return;

        if (isReloading(stack)) {
            boolean justTookDamage = player.hurtTime == player.hurtDuration - 1;

            if (justTookDamage) {
                DamageSource last = player.getLastDamageSource();
                if (last != null && last.getEntity() instanceof Entity) {
                    cancelReload(stack, player);
                    return;
                }
            }
        }

        if ((isReloading(stack) && player.getMainHandItem() != stack) && (isReloading(stack) && player.getOffhandItem() != stack)) {
            cancelReload(stack, player);
            return;
        }

        if (isReloading(stack) && !player.getCooldowns().isOnCooldown(this) && player.getMainHandItem() == stack
                || isReloading(stack) && !player.getCooldowns().isOnCooldown(this) && (player.getOffhandItem() == stack)) {
            int currentAmmo = getAmmo(stack);
            int ammoNeeded = maxAmmo - currentAmmo;

            int ammoLoaded = consumeSnubnoseAmmo(player, ammoNeeded);

            if (ammoLoaded > 0) {
                if (player.isCreative()) {
                    setAmmo(stack, maxAmmo);
                } else {
                    setAmmo(stack, currentAmmo + ammoLoaded);
                }
            }

            setReloading(stack, false);
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int ammo = getAmmo(stack);
        tooltip.add(
                Component.literal("Ammo: " + ammo + " / " + maxAmmo)
                        .withStyle(ChatFormatting.GRAY)
        );

        tooltip.add(Component.translatable("leveling.roundabout.disc_wip").withStyle(ChatFormatting.RED));
    }
}