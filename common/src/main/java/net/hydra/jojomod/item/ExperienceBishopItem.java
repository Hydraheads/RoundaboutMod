package net.hydra.jojomod.item;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExperienceBishopItem extends Item implements Vanishable {
    public ExperienceBishopItem(Properties $$0) {
        super($$0.defaultDurability(3));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }
    /**Default 72000*/
    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }


    public static void attackThePerson(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack != null && !(stack.getItem() instanceof ExperienceBishopItem)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }
        player.swing(InteractionHand.MAIN_HAND, true);

        int levels = player.isShiftKeyDown() ? 10 : 1;

        int removedXP;

        if (player.experienceLevel < levels) {
            // Player doesn't have enough levels, take everything.
            removedXP = getPlayerTotalExperience(player);
        } else {
            // Player has enough levels, remove exactly N levels.
            removedXP = getXpToRemove(player, levels);
        }

        player.giveExperiencePoints(-removedXP);

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("StoredXP", tag.getInt("StoredXP") + removedXP);


        player.displayClientMessage(
                Component.translatable(
                        "text.roundabout.memory.15"
                ).withStyle(ChatFormatting.AQUA),
                true
        );

        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.DING_EVENT,
                SoundSource.NEUTRAL,
                1F,
                1.4F
        );
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }

        int usedTime = this.getUseDuration(stack) - timeLeft;
        int itemTime = 7;

        if (usedTime < itemTime) {
            return;
        }

        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }


        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return;
        }

        int storedXP = tag.getInt("StoredXP");

        int levels = player.isShiftKeyDown() ? 10 : 1;

        int wantedXP = getXpToAdd(player, levels);

// Can't withdraw more than is stored.
        int givenXP = Math.min(wantedXP, storedXP);

        player.giveExperiencePoints(givenXP);

        tag.putInt("StoredXP", storedXP - givenXP);



        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.DING_EVENT,
                SoundSource.NEUTRAL,
                1F,
                1F
        );

        player.displayClientMessage(
                Component.translatable(
                        "text.roundabout.memory.10"
                ).withStyle(ChatFormatting.AQUA),
                true
        );

    }
    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {

        int stored = stack.getOrCreateTag().getInt("StoredXP");

        $$2.add(Component.literal("Stored XP: " + stored)
                .withStyle(ChatFormatting.GREEN));
    }

    private static int getExperienceForLevels(Player player, int levels) {
        int xp = 0;

        for (int lvl = player.experienceLevel;
             lvl < player.experienceLevel + levels;
             lvl++) {
            xp += getXpNeededForNextLevel(lvl);
        }

        return xp;
    }
    public static int getXpNeededForNextLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }
    private static int getPlayerExperience(Player player) {
        int xp = 0;

        // XP from completed levels
        for (int i = 0; i < player.experienceLevel; i++) {
            xp += getXpNeededForNextLevel(i);
        }

        // XP inside the current level
        xp += Math.round(player.experienceProgress * player.getXpNeededForNextLevel());

        return xp;
    }
    public static int getTotalExperienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int)(2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int)(4.5 * level * level - 162.5 * level + 2220);
        }
    }
    public static int getPlayerTotalExperience(Player player) {
        return getTotalExperienceForLevel(player.experienceLevel)
                + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }
    public static int getXpToRemove(Player player, int levelsToRemove) {
        int currentTotal = getPlayerTotalExperience(player);

        int targetLevel = Math.max(0, player.experienceLevel - levelsToRemove);

        int targetTotal = getTotalExperienceForLevel(targetLevel)
                + Math.round(player.experienceProgress * getXpNeededForNextLevel(targetLevel));

        return Math.max(0, currentTotal - targetTotal);
    }

    public static int getXpToAdd(Player player, int levelsToAdd) {
        int currentTotal = getPlayerTotalExperience(player);

        int targetLevel = player.experienceLevel + levelsToAdd;

        int targetTotal = getTotalExperienceForLevel(targetLevel)
                + Math.round(player.experienceProgress * getXpNeededForNextLevel(targetLevel));

        return targetTotal - currentTotal;
    }
}
