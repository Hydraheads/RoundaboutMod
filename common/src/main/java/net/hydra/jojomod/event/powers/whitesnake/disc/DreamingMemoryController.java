package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public final class DreamingMemoryController {
    private static final String HAS_MEMORY = "HasMemory";
    private static final String OWNER_ID = "OwnerId";
    private static final String OWNER_NAME = "OwnerName";
    private static final String TAME_OWNER_ID = "TameOwnerId";
    private static final String TAME_OWNER_NAME = "TameOwnerName";
    private static final String PERSONALITY = "Personality";
    private static final String MEMORY_READING = "MemoryReading";
    private static final String SEAL_TICKS = "SealTicks";
    private static final String SEAL_MAX_TICKS = "SealMaxTicks";

    private DreamingMemoryController() {
    }

    public static boolean canTemporarilyImplant(LivingEntity target) {
        if (!(target instanceof Mob)) return false;
        DiscBearer bearer = (DiscBearer) target;
        return bearer.sba$ownsMemoryDisc()
                && bearer.sba$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY) > 0
                && !bearer.sba$hasTemporaryMemoryDisc();
    }

    public static boolean isDreamingWithoutMemory(Mob mob) {
        return mob.hasEffect(ModEffects.SLEEPING)
                && !((DiscBearer) mob).sba$hasTemporaryMemoryDisc();
    }

    public static void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.sba$setMemoryBeforeDreaming(snapshot(bearer));
        bearer.sba$setTemporaryMemoryDisc(stack.copyWithCount(1));
        MemoryDiscItem.applyMemory(stack, target);
    }

    public static void tick(LivingEntity entity) {
        if (!(entity instanceof Mob) || entity.level().isClientSide()) return;
        DiscBearer bearer = (DiscBearer) entity;
        if (!bearer.sba$hasTemporaryMemoryDisc()) return;

        CompoundTag previousMemory = bearer.sba$getMemoryBeforeDreaming();
        int remaining = previousMemory.getInt(SEAL_TICKS);
        int next = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing
                ? Math.max(0, remaining - 1) : 0;
        previousMemory.putInt(SEAL_TICKS, next);
        bearer.sba$setMemoryBeforeDreaming(previousMemory);
        if (next > 0) return;

        ItemStack temporaryDisc = bearer.sba$getTemporaryMemoryDisc();
        restore(bearer, previousMemory);
        bearer.sba$setTemporaryMemoryDisc(ItemStack.EMPTY);
        bearer.sba$setMemoryBeforeDreaming(new CompoundTag());
        if (!temporaryDisc.isEmpty()) entity.spawnAtLocation(temporaryDisc, 0.35F);
    }

    private static CompoundTag snapshot(DiscBearer bearer) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(HAS_MEMORY, bearer.sba$ownsMemoryDisc());
        tag.putString(OWNER_ID, bearer.sba$getMemoryDiscOwnerId());
        tag.putString(OWNER_NAME, bearer.sba$getMemoryDiscOwnerName());
        tag.putString(TAME_OWNER_ID, bearer.sba$getMemoryTameOwnerId());
        tag.putString(TAME_OWNER_NAME, bearer.sba$getMemoryTameOwnerName());
        tag.putByte(PERSONALITY, bearer.sba$getMemoryPersonality());
        tag.put(MEMORY_READING, bearer.sba$getMemoryReading());
        tag.putInt(SEAL_TICKS, bearer.sba$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY));
        tag.putInt(SEAL_MAX_TICKS, bearer.sba$getDiscSealMaxTicks(WhitesnakeDiscUtil.MEMORY));
        return tag;
    }

    private static void restore(DiscBearer bearer, CompoundTag tag) {
        bearer.sba$setMemoryDiscOwnerId(tag.getString(OWNER_ID));
        bearer.sba$setMemoryDiscOwnerName(tag.getString(OWNER_NAME));
        bearer.sba$setMemoryTameOwnerId(tag.getString(TAME_OWNER_ID));
        bearer.sba$setMemoryTameOwnerName(tag.getString(TAME_OWNER_NAME));
        bearer.sba$setMemoryPersonality(tag.getByte(PERSONALITY));
        bearer.sba$setMemoryReading(tag.contains(MEMORY_READING, CompoundTag.TAG_COMPOUND)
                ? tag.getCompound(MEMORY_READING) : new CompoundTag());
        bearer.sba$setHasMemoryDisc(tag.getBoolean(HAS_MEMORY));
        bearer.sba$setDiscSeal(WhitesnakeDiscUtil.MEMORY, tag.getInt(SEAL_TICKS),
                tag.getInt(SEAL_MAX_TICKS));
    }
}
