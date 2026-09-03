package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;

public final class WhitesnakeDiscUtil {
    public static final byte STAND = 0;
    public static final byte SIGHT = 1;
    public static final byte MEMORY = 2;
    public static final byte HEARING = 3;

    private WhitesnakeDiscUtil() {
    }

    public static boolean ejectDisc(LivingEntity target, byte type) {
        if (isDiscBlacklisted(target)) return false;
        if (!isDiscStealEnabled(type)) return false;
        Config.WhitesnakeSettings config = ClientNetworking.getAppropriateConfig().whitesnakeSettings;
        if (target instanceof ServerPlayer && !Boolean.TRUE.equals(config.stealPlayerDiscs)) {
            return DiscSealController.seal(target, type);
        }
        boolean lowHealthSteal = config.stealDiscWhenLowHealth && target.getHealth() < 2.0F;
        if (!lowHealthSteal && hallucinationLevel(target) < config.hallucinationAllowsDiscSteal) {
            return DiscSealController.seal(target, type);
        }
        return switch (type) {
            case SIGHT -> ejectSight(target);
            case MEMORY -> ejectMemory(target);
            case HEARING -> ejectHearing(target);
            default -> ejectStand(target);
        };
    }

    public static boolean canCarrySightDisc(LivingEntity target) {
        return isSightDiscEnabled() && !isDiscBlacklisted(target)
                && !(target instanceof Sniffer || target instanceof Bat
                || target instanceof Dolphin || target instanceof Warden);
    }

    public static boolean isDiscBlacklisted(LivingEntity target) {
        return MainUtil.isDiscEntityBlacklisted(target);
    }

    public static boolean isSightDiscEnabled() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.sightDiscStealEnabled;
    }

    public static boolean isHearingDiscEnabled() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.hearingDiscStealEnabled;
    }

    public static boolean isBodyDiscEnabled(byte type) {
        return switch (type) {
            case SIGHT -> isSightDiscEnabled();
            case HEARING -> isHearingDiscEnabled();
            default -> true;
        };
    }

    private static boolean ejectSight(LivingEntity target) {
        return dropExtracted(target, extractSight(target, true));
    }

    private static boolean ejectHearing(LivingEntity target) {
        return dropExtracted(target, extractHearing(target, true));
    }

    private static boolean ejectMemory(LivingEntity target) {
        return dropExtracted(target, extractMemory(target, true));
    }

    private static boolean ejectStand(LivingEntity target) {
        return dropExtracted(target, extractStand(target, true));
    }

    public static ItemStack extractDiscStack(LivingEntity target, byte type) {
        if (isDiscBlacklisted(target)) return ItemStack.EMPTY;
        return switch (type) {
            case SIGHT -> extractSight(target, true);
            case MEMORY -> extractMemory(target, true);
            case HEARING -> extractHearing(target, true);
            case STAND -> extractStand(target, false);
            default -> ItemStack.EMPTY;
        };
    }

    public static void ejectMobMemoryFromPlayer(ServerPlayer player) {
        DiscBearer bearer = (DiscBearer) player;
        if (!bearer.roundabout$ownsMemoryDisc()
                || bearer.roundabout$getMemoryPersonality() == MemoryPersonality.PLAYER) return;
        bearer.roundabout$setDiscSeal(MEMORY, 0, 0);
        ItemStack disc = extractMemory(player, true);
        bearer.roundabout$setMemoryDiscOwnerId("");
        bearer.roundabout$setMemoryDiscOwnerName("");
        bearer.roundabout$setMemoryTameOwnerId("");
        bearer.roundabout$setMemoryTameOwnerName("");
        bearer.roundabout$setMemoryPersonality(MemoryPersonality.PLAYER);
        if (!disc.isEmpty() && !player.getInventory().add(disc)) player.drop(disc, false);
    }

    private static ItemStack extractSight(LivingEntity target, boolean storeOwner) {
        DiscBearer bearer = (DiscBearer) target;
        if (!canCarrySightDisc(target) || !bearer.roundabout$hasSightDisc()) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(ModItems.SIGHT_DISC);
        setStoredOwnerOrTarget(stack, target, bearer.roundabout$getSightDiscOwnerId(),
                bearer.roundabout$getSightDiscOwnerName(), storeOwner);
        bearer.roundabout$setHasSightDisc(false);
        ((StandUser) target).roundabout$deeplyRemoveAttackTarget();
        return stack;
    }

    private static ItemStack extractHearing(LivingEntity target, boolean storeOwner) {
        if (!isHearingDiscEnabled()) return ItemStack.EMPTY;
        DiscBearer bearer = (DiscBearer) target;
        if (!bearer.roundabout$hasHearingDisc()) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(ModItems.HEARING_DISC);
        setStoredOwnerOrTarget(stack, target, bearer.roundabout$getHearingDiscOwnerId(),
                bearer.roundabout$getHearingDiscOwnerName(), storeOwner);
        bearer.roundabout$setHasHearingDisc(false);
        return stack;
    }

    private static ItemStack extractMemory(LivingEntity target, boolean storeOwner) {
        DiscBearer bearer = (DiscBearer) target;
        if (!bearer.roundabout$hasMemoryDisc()) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(ModItems.MEMORY_DISC);
        setStoredOwnerOrTarget(stack, target, bearer.roundabout$getMemoryDiscOwnerId(),
                bearer.roundabout$getMemoryDiscOwnerName(), storeOwner);
        DiscItemData.setPersonality(stack, bearer.roundabout$getMemoryPersonality());
        DiscItemData.copyTameOwner(stack, target);
        String memoryOwner = bearer.roundabout$getMemoryDiscOwnerId();
        if (memoryOwner == null || memoryOwner.isEmpty()
                || memoryOwner.equals(target.getUUID().toString())) {
            DiscItemData.captureMemoryReading(stack, target);
        } else {
            DiscItemData.setMemoryReading(stack, bearer.roundabout$getMemoryReading());
        }
        bearer.roundabout$setHasMemoryDisc(false);
        bearer.roundabout$setMemoryReading(new CompoundTag());
        ((StandUser) target).roundabout$deeplyRemoveAttackTarget();
        if (target instanceof Mob mob) {
            mob.setTarget(null);
            mob.getNavigation().stop();
            if (mob.isNoAi()) mob.setNoAi(false);
        }
        return stack;
    }

    private static ItemStack extractStand(LivingEntity target, boolean storeOwner) {
        StandUser standUser = (StandUser) target;
        ItemStack current = standUser.roundabout$getStandDisc();
        if (current.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = MainUtil.saveToDiscData(target, current.copy());
        if (storeOwner) DiscItemData.setOwnerIfMissing(stack, target);
        standUser.roundabout$getStandPowers().onStandSwitch();
        standUser.roundabout$setStand(null);
        standUser.roundabout$setActive(false);
        standUser.roundabout$setStandDisc(ItemStack.EMPTY);
        standUser.roundabout$setStandPowers(null);
        return stack;
    }

    private static void setStoredOwnerOrTarget(ItemStack stack, LivingEntity target, String id, String name,
                                               boolean useTargetIfMissing) {
        if (id != null && !id.isEmpty() && name != null && !name.isEmpty()) {
            DiscItemData.setOwner(stack, id, name);
        } else if (useTargetIfMissing) {
            DiscItemData.setOwner(stack, target);
        }
    }

    private static void drop(LivingEntity target, ItemStack stack) {
        if (!target.level().isClientSide()) {
            target.spawnAtLocation(stack, 0.35F);
            target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_EJECT_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static boolean dropExtracted(LivingEntity target, ItemStack stack) {
        if (stack.isEmpty()) return false;
        drop(target, stack);
        return true;
    }

    public static boolean hasForeignMemory(LivingEntity entity) {
        DiscBearer bearer = (DiscBearer) entity;
        String ownerId = bearer.roundabout$getMemoryDiscOwnerId();
        return bearer.roundabout$hasMemoryDisc() && !ownerId.isEmpty()
                && !ownerId.equals(entity.getUUID().toString());
    }

    public static byte effectivePersonality(LivingEntity entity) {
        DiscBearer bearer = (DiscBearer) entity;
        return bearer.roundabout$hasMemoryDisc() ? bearer.roundabout$getMemoryPersonality()
                : MemoryPersonality.PASSIVE;
    }

    public static boolean hasUsableStandDisc(LivingEntity entity) {
        StandUser standUser = (StandUser) entity;
        return !standUser.roundabout$getStandDisc().isEmpty() && standUser.roundabout$getSealedTicks() <= 0;
    }

    public static boolean isDiscStealEnabled(byte type) {
        Config.WhitesnakeSettings config = ClientNetworking.getAppropriateConfig().whitesnakeSettings;
        return switch (type) {
            case STAND -> config.standDiscStealEnabled;
            case SIGHT -> config.sightDiscStealEnabled;
            case MEMORY -> config.memoryDiscStealEnabled;
            case HEARING -> config.hearingDiscStealEnabled;
            default -> false;
        };
    }

    public static byte firstEnabledDisc() {
        for (byte type = STAND; type <= HEARING; type++) {
            if (isDiscStealEnabled(type)) return type;
        }
        return -1;
    }

    public static byte randomEnabledDisc(RandomSource random) {
        int enabled = 0;
        for (byte type = STAND; type <= HEARING; type++) {
            if (isDiscStealEnabled(type)) enabled++;
        }
        if (enabled == 0) return -1;
        int selected = random.nextInt(enabled);
        for (byte type = STAND; type <= HEARING; type++) {
            if (isDiscStealEnabled(type) && selected-- == 0) return type;
        }
        return -1;
    }

    public static int hallucinationLevel(LivingEntity target) {
        MobEffectInstance hallucination = target.getEffect(ModEffects.HALLUCINATION);
        return hallucination == null ? 0 : Math.min(
                HallucinationEffect.MAX_LEVEL, hallucination.getAmplifier() + 1);
    }
}
