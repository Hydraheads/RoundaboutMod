package net.hydra.jojomod.access;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface DiscBearer {
    boolean roundabout$hasSightDisc();
    boolean roundabout$ownsSightDisc();
    void roundabout$setHasSightDisc(boolean value);

    boolean roundabout$hasMemoryDisc();
    boolean roundabout$ownsMemoryDisc();
    void roundabout$setHasMemoryDisc(boolean value);

    boolean roundabout$hasHearingDisc();
    boolean roundabout$ownsHearingDisc();
    void roundabout$setHasHearingDisc(boolean value);

    int roundabout$getDiscSealTicks(byte type);
    int roundabout$getDiscSealMaxTicks(byte type);
    void roundabout$setDiscSeal(byte type, int ticks, int maxTicks);

    String roundabout$getSightDiscOwnerId();
    void roundabout$setSightDiscOwnerId(String value);

    String roundabout$getSightDiscOwnerName();
    void roundabout$setSightDiscOwnerName(String value);

    String roundabout$getMemoryDiscOwnerId();
    void roundabout$setMemoryDiscOwnerId(String value);

    String roundabout$getMemoryDiscOwnerName();
    void roundabout$setMemoryDiscOwnerName(String value);

    String roundabout$getMemoryTameOwnerId();
    void roundabout$setMemoryTameOwnerId(String value);

    String roundabout$getMemoryTameOwnerName();
    void roundabout$setMemoryTameOwnerName(String value);

    String roundabout$getHearingDiscOwnerId();
    void roundabout$setHearingDiscOwnerId(String value);

    String roundabout$getHearingDiscOwnerName();
    void roundabout$setHearingDiscOwnerName(String value);

    byte roundabout$getMemoryPersonality();
    void roundabout$setMemoryPersonality(byte value);

    CompoundTag roundabout$getMemoryReading();
    void roundabout$setMemoryReading(CompoundTag value);

    boolean roundabout$hasTemporaryMemoryDisc();
    ItemStack roundabout$getTemporaryMemoryDisc();
    void roundabout$setTemporaryMemoryDisc(ItemStack value);
    CompoundTag roundabout$getMemoryBeforeDreaming();
    void roundabout$setMemoryBeforeDreaming(CompoundTag value);

    ItemStack roundabout$getMusicDisc();
    void roundabout$setMusicDisc(ItemStack value);
}
