package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.BladedBowlerHatEntity;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.io.Serial;

public class SnubnoseRevolverItem extends FirearmItem implements Vanishable {

    public SnubnoseRevolverItem(Properties $$0) {
        super($$0);
    }

    private static final String AMMO_COUNT_TAG = "AmmoCount";

    private int getAmmo(ItemStack stack) {
        return stack.getOrCreateTag().getInt(AMMO_COUNT_TAG);
    }

    private void setAmmo(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(AMMO_COUNT_TAG, count);
    }

    int maxAmmo = 6;

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

        return false;
    }

    private int consumeSnubnoseAmmo(Player player, int amount) {
        Inventory inv = player.getInventory();
        int consumed = 0;

        for (int i = 0; i < inv.items.size() && amount > 0; i++) {
            ItemStack stack = inv.items.get(i);
            if (stack.getItem() instanceof SnubnoseAmmoItem) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;

                if (amount <= 0) break;
            }
        }

        for (int i = 0; i < inv.offhand.size() && amount > 0; i++) {
            ItemStack stack = inv.offhand.get(i);
            if (stack.getItem() instanceof SnubnoseAmmoItem) {
                int remove = Math.min(stack.getCount(), amount);
                stack.shrink(remove);
                consumed += remove;
                amount -= remove;

                if (amount <= 0) break;
            }
        }

        return consumed;
    }

    @Override
    public void fireBullet(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (getAmmo(itemStack) > 0) {
            setAmmo(itemStack, getAmmo(itemStack) - 1);
            Roundabout.LOGGER.info("Ammo shot:"+getAmmo(itemStack));
            LivingEntity livingEntity = player;
            RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(level, livingEntity);
            $$7.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.0F, 0.0F);
            level.addFreshEntity($$7);
            level.playSound(null, player, ModSounds.SNUBNOSE_FIRE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
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
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(itemStack.getItem() instanceof SnubnoseRevolverItem)) {
            return InteractionResultHolder.fail(itemStack);
        }
        if (!(player.getUseItem() == itemStack)) {
            if (player.isCrouching() && hasSnubnoseAmmo(player) && getAmmo(itemStack) != maxAmmo) {
                int currentAmmo = getAmmo(itemStack);
                Roundabout.LOGGER.info("Ammo:"+getAmmo(itemStack));
                int ammoNeeded = maxAmmo - currentAmmo;
                Roundabout.LOGGER.info("Ammo needed:"+ammoNeeded);

                int ammoLoaded = consumeSnubnoseAmmo(player, ammoNeeded);
                Roundabout.LOGGER.info("Ammo loaded:"+ammoLoaded);

                if (ammoLoaded > 0) {
                    setAmmo(itemStack, currentAmmo + ammoLoaded);
                    Roundabout.LOGGER.info("Final ammo:"+(currentAmmo + ammoLoaded));
                    level.playSound(null, player, ModSounds.SNUBNOSE_RELOAD_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            } else {
                player.startUsingItem(hand);
            }
        }
        super.use(level, player, hand);

        return InteractionResultHolder.consume(itemStack);
    }
}