package net.hydra.jojomod.access;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface DiscBearer {
    boolean sba$hasSightDisc();
    boolean sba$ownsSightDisc();
    void sba$setHasSightDisc(boolean value);

    boolean sba$hasMemoryDisc();
    boolean sba$ownsMemoryDisc();
    void sba$setHasMemoryDisc(boolean value);

    boolean sba$hasHearingDisc();
    boolean sba$ownsHearingDisc();
    void sba$setHasHearingDisc(boolean value);

    int sba$getDiscSealTicks(byte type);
    int sba$getDiscSealMaxTicks(byte type);
    void sba$setDiscSeal(byte type, int ticks, int maxTicks);

    String sba$getSightDiscOwnerId();
    void sba$setSightDiscOwnerId(String value);

    String sba$getSightDiscOwnerName();
    void sba$setSightDiscOwnerName(String value);

    String sba$getMemoryDiscOwnerId();
    void sba$setMemoryDiscOwnerId(String value);

    String sba$getMemoryDiscOwnerName();
    void sba$setMemoryDiscOwnerName(String value);

    String sba$getMemoryTameOwnerId();
    void sba$setMemoryTameOwnerId(String value);

    String sba$getMemoryTameOwnerName();
    void sba$setMemoryTameOwnerName(String value);

    String sba$getHearingDiscOwnerId();
    void sba$setHearingDiscOwnerId(String value);

    String sba$getHearingDiscOwnerName();
    void sba$setHearingDiscOwnerName(String value);

    byte sba$getMemoryPersonality();
    void sba$setMemoryPersonality(byte value);

    CompoundTag sba$getMemoryReading();
    void sba$setMemoryReading(CompoundTag value);

    boolean sba$hasTemporaryMemoryDisc();
    ItemStack sba$getTemporaryMemoryDisc();
    void sba$setTemporaryMemoryDisc(ItemStack value);
    CompoundTag sba$getMemoryBeforeDreaming();
    void sba$setMemoryBeforeDreaming(CompoundTag value);

    ItemStack sba$getMusicDisc();
    void sba$setMusicDisc(ItemStack value);
}
