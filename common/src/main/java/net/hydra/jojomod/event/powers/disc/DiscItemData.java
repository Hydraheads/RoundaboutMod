package net.hydra.jojomod.event.powers.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public final class DiscItemData {
    public static final String DATA_TAG = "RoundaboutDiscData";
    public static final String OWNER_ID = "OwnerId";
    public static final String OWNER_NAME = "OwnerName";
    public static final String OWNER_TYPE = "OwnerType";
    public static final String PERSONALITY = "Personality";
    public static final String TAME_OWNER_ID = "TameOwnerId";
    public static final String TAME_OWNER_NAME = "TameOwnerName";
    public static final String MEMORY_READING = "MemoryReading";
    public static final String READING_KIND = "Kind";
    public static final String READING_ENTITY_TYPE = "EntityType";
    public static final String READING_INVENTORY = "Inventory";
    public static final String READING_SLOT = "MemorySlot";
    public static final String READING_SPAWN_POS = "SpawnPos";
    public static final String READING_SPAWN_DIMENSION = "SpawnDimension";
    public static final String READING_JOB_POS = "JobPos";
    public static final String READING_JOB_DIMENSION = "JobDimension";
    public static final byte READING_NONE = 0;
    public static final byte READING_PLAYER = 1;
    public static final byte READING_VILLAGER = 2;

    private DiscItemData() {
    }

    public static CompoundTag data(ItemStack stack) {
        return stack.getOrCreateTagElement(DATA_TAG);
    }

    public static String ownerName(LivingEntity owner) {
        return owner instanceof Player ? owner.getName().getString() : owner.getType().getDescription().getString();
    }

    public static void setOwner(ItemStack stack, LivingEntity owner) {
        CompoundTag tag = data(stack);
        tag.putString(OWNER_ID, owner.getUUID().toString());
        tag.putString(OWNER_NAME, ownerName(owner));
        tag.putString(OWNER_TYPE, BuiltInRegistries.ENTITY_TYPE.getKey(owner.getType()).toString());
    }

    public static void setOwner(ItemStack stack, String id, String name) {
        CompoundTag tag = data(stack);
        tag.putString(OWNER_ID, id);
        tag.putString(OWNER_NAME, name);
    }

    public static void setOwnerIfMissing(ItemStack stack, LivingEntity owner) {
        if (!hasOwner(stack)) {
            setOwner(stack, owner);
        }
    }

    public static boolean hasOwner(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag != null && tag.contains(OWNER_NAME) && !tag.getString(OWNER_NAME).isEmpty();
    }

    public static String getOwnerId(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? "" : tag.getString(OWNER_ID);
    }

    public static String getOwnerName(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? "" : tag.getString(OWNER_NAME);
    }

    public static String getOwnerType(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? "" : tag.getString(OWNER_TYPE);
    }

    public static byte getPersonality(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? MemoryPersonality.PASSIVE : tag.getByte(PERSONALITY);
    }

    public static void setPersonality(ItemStack stack, byte personality) {
        data(stack).putByte(PERSONALITY, personality);
    }

    public static String getTameOwnerId(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? "" : tag.getString(TAME_OWNER_ID);
    }

    public static String getTameOwnerName(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag == null ? "" : tag.getString(TAME_OWNER_NAME);
    }

    public static void setTameOwner(ItemStack stack, String id, String name) {
        CompoundTag tag = data(stack);
        tag.putString(TAME_OWNER_ID, id == null ? "" : id);
        tag.putString(TAME_OWNER_NAME, name == null ? "" : name);
    }

    public static void copyTameOwner(ItemStack stack, LivingEntity source) {
        DiscBearer bearer = (DiscBearer) source;
        String id = bearer.roundabout$getMemoryTameOwnerId();
        String name = bearer.roundabout$getMemoryTameOwnerName();
        if ((id == null || id.isEmpty()) && source instanceof TamableAnimal tamable
                && tamable.isTame() && tamable.getOwnerUUID() != null) {
            id = tamable.getOwnerUUID().toString();
            LivingEntity owner = tamable.getOwner();
            name = owner == null ? "" : owner.getName().getString();
            if (name.isEmpty() && source.level() instanceof ServerLevel server) {
                name = server.getServer().getProfileCache().get(tamable.getOwnerUUID())
                        .map(profile -> profile.getName()).orElse(id);
            }
        }
        if ((id == null || id.isEmpty()) && source instanceof AbstractHorse horse
                && horse.isTamed() && horse.getOwnerUUID() != null) {
            id = horse.getOwnerUUID().toString();
            if (source.level() instanceof ServerLevel server) {
                name = server.getServer().getProfileCache().get(horse.getOwnerUUID())
                        .map(profile -> profile.getName()).orElse(id);
            }
        }
        setTameOwner(stack, id, name);
    }

    public static CompoundTag getMemoryReading(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(DATA_TAG);
        return tag != null && tag.contains(MEMORY_READING, Tag.TAG_COMPOUND)
                ? tag.getCompound(MEMORY_READING).copy() : new CompoundTag();
    }

    public static void setMemoryReading(ItemStack stack, CompoundTag reading) {
        CompoundTag tag = data(stack);
        if (reading == null || reading.isEmpty()) tag.remove(MEMORY_READING);
        else tag.put(MEMORY_READING, reading.copy());
    }

    public static void captureMemoryReading(ItemStack stack, LivingEntity source) {
        CompoundTag reading = new CompoundTag();
        reading.putString(READING_ENTITY_TYPE,
                BuiltInRegistries.ENTITY_TYPE.getKey(source.getType()).toString());
        if (source instanceof ServerPlayer player) {
            reading.putByte(READING_KIND, READING_PLAYER);
            reading.put(READING_INVENTORY, captureInventory(player.getInventory()));
            BlockPos spawn = player.getRespawnPosition();
            ResourceKey<Level> dimension = player.getRespawnDimension();
            if (spawn == null) {
                MinecraftServer server = player.getServer();
                ServerLevel overworld = server == null ? null : server.getLevel(Level.OVERWORLD);
                if (overworld != null) spawn = overworld.getSharedSpawnPos();
                dimension = Level.OVERWORLD;
            }
            if (spawn != null) {
                reading.putLong(READING_SPAWN_POS, spawn.asLong());
                reading.putString(READING_SPAWN_DIMENSION, dimension.location().toString());
            }
        } else if (source instanceof Villager villager) {
            reading.putByte(READING_KIND, READING_VILLAGER);
            Optional<GlobalPos> jobSite = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
            if (jobSite.isPresent()) {
                reading.putLong(READING_JOB_POS, jobSite.get().pos().asLong());
                reading.putString(READING_JOB_DIMENSION,
                        jobSite.get().dimension().location().toString());
            }
        }
        setMemoryReading(stack, reading);
    }

    public static String getMemoryEntityType(ItemStack stack) {
        CompoundTag reading = getMemoryReading(stack);
        String type = reading.getString(READING_ENTITY_TYPE);
        return type.isEmpty() ? getOwnerType(stack) : type;
    }

    public static boolean isCreeperMemory(ItemStack stack) {
        return stack.getItem() instanceof MemoryDiscItem
                && "minecraft:creeper".equals(getMemoryEntityType(stack));
    }

    private static ListTag captureInventory(Inventory inventory) {
        ListTag list = new ListTag();
        for (int slot = 0; slot < inventory.items.size(); slot++) {
            addSnapshotStack(list, slot, inventory.items.get(slot));
        }
        for (int slot = 0; slot < inventory.armor.size(); slot++) {
            addSnapshotStack(list, 36 + slot, inventory.armor.get(slot));
        }
        for (int slot = 0; slot < inventory.offhand.size(); slot++) {
            addSnapshotStack(list, 40 + slot, inventory.offhand.get(slot));
        }
        return list;
    }

    private static void addSnapshotStack(ListTag list, int slot, ItemStack source) {
        if (source.isEmpty()) return;
        ItemStack copy = source.copy();
        CompoundTag stackTag = copy.getTag();
        if (stackTag != null && stackTag.contains(DATA_TAG, Tag.TAG_COMPOUND)) {
            stackTag.getCompound(DATA_TAG).remove(MEMORY_READING);
        }
        CompoundTag saved = copy.save(new CompoundTag());
        saved.putInt(READING_SLOT, slot);
        list.add(saved);
    }

    public static void addOwnerTooltip(ItemStack stack, java.util.List<Component> lines, boolean showPersonality) {
        if (hasOwner(stack)) {
            lines.add(Component.literal("Owner: " + getOwnerName(stack)).withStyle(ChatFormatting.GRAY));
        }
        if (showPersonality && stack.getTagElement(DATA_TAG) != null) {
            lines.add(Component.literal("Memory: " + MemoryPersonality.name(getPersonality(stack)))
                    .withStyle(ChatFormatting.DARK_PURPLE));
            String tameOwner = getTameOwnerName(stack);
            if (!tameOwner.isEmpty()) {
                lines.add(Component.literal("Tamed by: " + tameOwner)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static boolean hasPlayerControl(LivingEntity entity) {
        return !isLobotomized(entity);
    }

    public static boolean canUseAbilities(LivingEntity entity) {
        DiscBearer bearer = (DiscBearer) entity;
        if (!bearer.roundabout$hasMemoryDisc()) return WhitesnakeDiscUtil.hasUsableStandDisc(entity);
        return true;
    }

    public static boolean isLobotomized(LivingEntity entity) {
        return !((DiscBearer) entity).roundabout$hasMemoryDisc()
                && !WhitesnakeDiscUtil.hasUsableStandDisc(entity);
    }

    public static boolean isMemoryDevelopmentLimited(LivingEntity entity) {
        return entity instanceof Player && !((DiscBearer) entity).roundabout$hasMemoryDisc()
                && WhitesnakeDiscUtil.hasUsableStandDisc(entity);
    }

    public static boolean isBlankMemoryMob(LivingEntity entity) {
        return entity instanceof Mob && !((DiscBearer) entity).roundabout$hasMemoryDisc()
                && WhitesnakeDiscUtil.hasUsableStandDisc(entity);
    }

    public static boolean canSelfImplantHeldMemory(Player player) {
        DiscBearer bearer = (DiscBearer) player;
        if (bearer.roundabout$ownsMemoryDisc()) return false;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offhand = player.getOffhandItem();
        return isPlayerMemory(mainHand) || isPlayerMemory(offhand);
    }

    private static boolean isPlayerMemory(ItemStack stack) {
        return stack.getItem() instanceof MemoryDiscItem
                && getPersonality(stack) == MemoryPersonality.PLAYER;
    }
}
