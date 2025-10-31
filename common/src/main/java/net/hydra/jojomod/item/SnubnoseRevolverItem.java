package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.BladedBowlerHatEntity;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

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

    private static final String FIRING_MODE = "FiringMode";

    private boolean getFiringMode(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(FIRING_MODE);
    }

    private void setFiringMode(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(FIRING_MODE, value);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    int maxAmmo = 6;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (!(itemStack.getItem() instanceof SnubnoseRevolverItem)) {
            return InteractionResultHolder.fail(itemStack);
        }
        if (!getFiringMode(itemStack)) {
            setFiringMode(itemStack, true);
            Roundabout.LOGGER.info(""+getFiringMode(itemStack));
        } else if (player.isCrouching()) {
            setAmmo(itemStack, maxAmmo);
            Roundabout.LOGGER.info("Reloaded");
        }
        super.use(level, player, hand);

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level dimension, LivingEntity livingEntity, int timeLeft) {
        if (!dimension.isClientSide && livingEntity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (getFiringMode(itemStack)) {
                setFiringMode(itemStack, false);
                Roundabout.LOGGER.info(""+getFiringMode(itemStack));
            }
        }
    }
}