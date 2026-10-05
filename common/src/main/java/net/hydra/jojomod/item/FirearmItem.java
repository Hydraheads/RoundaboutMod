package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FirearmItem extends Item {

    public FirearmItem(Properties $$0) {
        super($$0);
    }
    public static boolean cycleReload = false;
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    public boolean interceptAttack(ItemStack itemStack, Player player) {
        if (player != null && player.getUseItem() != null) {
            if (player.getUseItem() == itemStack) {
                return true;
            }
        }
        return false;
    }
    public static final String AMMO_COUNT_TAG = "AmmoCount";
    public static final String RELOADING_TAG = "IsReloading";

    public int getAmmo(ItemStack stack) {
        return stack.getOrCreateTag().getInt(AMMO_COUNT_TAG);
    }

    public void setAmmo(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(AMMO_COUNT_TAG, count);
    }

    public boolean getReloading(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(RELOADING_TAG);
    }

    public void setReloading(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(RELOADING_TAG, value);
    }
    public boolean isCrouchingOrSomething(Player player, ItemStack stack){
        return (player.isCrouching() && !(player.getUseItem() == stack)) || cycleReload;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    public void fireBullet(Level level, Player player, InteractionHand hand) {
        LivingEntity livingEntity = player;
        RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(level, livingEntity);
        $$7.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity($$7);
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
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }
    public boolean isReloading(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(RELOADING_TAG);
    }

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
    @Override
    public void releaseUsing(ItemStack stack, Level dimension, LivingEntity livingEntity, int timeLeft) {
        if (!dimension.isClientSide && livingEntity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (!(player.getUseItem() == itemStack)) {
                player.stopUsingItem();
            }
        }
    }

    public int getMaxAmmo(){
        return 1;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int ammo = getAmmo(stack);
        tooltip.add(
                Component.literal("Ammo: " + ammo + " / " + getMaxAmmo())
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}