package net.hydra.jojomod.event.powers.disc;

import net.hydra.jojomod.access.DiscBearer;
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
        return target instanceof Mob && target.hasEffect(ModEffects.DREAMING)
                && !((DiscBearer) target).roundabout$hasTemporaryMemoryDisc();
    }

    public static boolean isDreamingWithoutMemory(Mob mob) {
        return mob.hasEffect(ModEffects.DREAMING)
                && !((DiscBearer) mob).roundabout$hasTemporaryMemoryDisc();
    }

    public static void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.roundabout$setMemoryBeforeDreaming(snapshot(bearer));
        bearer.roundabout$setTemporaryMemoryDisc(stack.copyWithCount(1));
        MemoryDiscItem.applyMemory(stack, target);
    }

    public static void tick(LivingEntity entity) {
        if (!(entity instanceof Mob) || entity.level().isClientSide()) return;
        DiscBearer bearer = (DiscBearer) entity;
        if (!bearer.roundabout$hasTemporaryMemoryDisc()
                || entity.hasEffect(ModEffects.DREAMING)) return;

        ItemStack temporaryDisc = bearer.roundabout$getTemporaryMemoryDisc();
        restore(bearer, bearer.roundabout$getMemoryBeforeDreaming());
        bearer.roundabout$setTemporaryMemoryDisc(ItemStack.EMPTY);
        bearer.roundabout$setMemoryBeforeDreaming(new CompoundTag());
        if (!temporaryDisc.isEmpty()) entity.spawnAtLocation(temporaryDisc, 0.35F);
    }

    private static CompoundTag snapshot(DiscBearer bearer) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(HAS_MEMORY, bearer.roundabout$ownsMemoryDisc());
        tag.putString(OWNER_ID, bearer.roundabout$getMemoryDiscOwnerId());
        tag.putString(OWNER_NAME, bearer.roundabout$getMemoryDiscOwnerName());
        tag.putString(TAME_OWNER_ID, bearer.roundabout$getMemoryTameOwnerId());
        tag.putString(TAME_OWNER_NAME, bearer.roundabout$getMemoryTameOwnerName());
        tag.putByte(PERSONALITY, bearer.roundabout$getMemoryPersonality());
        tag.put(MEMORY_READING, bearer.roundabout$getMemoryReading());
        tag.putInt(SEAL_TICKS, bearer.roundabout$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY));
        tag.putInt(SEAL_MAX_TICKS, bearer.roundabout$getDiscSealMaxTicks(WhitesnakeDiscUtil.MEMORY));
        return tag;
    }

    private static void restore(DiscBearer bearer, CompoundTag tag) {
        bearer.roundabout$setMemoryDiscOwnerId(tag.getString(OWNER_ID));
        bearer.roundabout$setMemoryDiscOwnerName(tag.getString(OWNER_NAME));
        bearer.roundabout$setMemoryTameOwnerId(tag.getString(TAME_OWNER_ID));
        bearer.roundabout$setMemoryTameOwnerName(tag.getString(TAME_OWNER_NAME));
        bearer.roundabout$setMemoryPersonality(tag.getByte(PERSONALITY));
        bearer.roundabout$setMemoryReading(tag.contains(MEMORY_READING, CompoundTag.TAG_COMPOUND)
                ? tag.getCompound(MEMORY_READING) : new CompoundTag());
        bearer.roundabout$setHasMemoryDisc(tag.getBoolean(HAS_MEMORY));
        bearer.roundabout$setDiscSeal(WhitesnakeDiscUtil.MEMORY, tag.getInt(SEAL_TICKS),
                tag.getInt(SEAL_MAX_TICKS));
    }
}
