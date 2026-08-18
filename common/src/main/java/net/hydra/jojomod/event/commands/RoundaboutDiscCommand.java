package net.hydra.jojomod.event.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.hydra.jojomod.item.AbstractBodyDiscItem;
import net.hydra.jojomod.item.HearingDiscItem;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.SightDiscItem;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;

public final class RoundaboutDiscCommand {
    private static final List<String> MEMORY_TYPES = List.of(
            "Hostile", "Neutral", "Passive", "Player", "Zombie", "None", "Hand");
    private static final List<String> BODY_DISC_VALUES = List.of("true", "false", "Hand");
    private static final List<String> EXTRACT_TYPES = List.of("memory", "stand", "sight", "hearing");
    private static final SimpleCommandExceptionType INVALID_MEMORY_TYPE = new SimpleCommandExceptionType(
            Component.literal("Memory type must be Hostile, Neutral, Passive, Player, Zombie, None, or Hand."));
    private static final SimpleCommandExceptionType INVALID_BODY_DISC_VALUE = new SimpleCommandExceptionType(
            Component.literal("The final value must be true, false, or Hand."));
    private static final SimpleCommandExceptionType WRONG_HELD_DISC = new SimpleCommandExceptionType(
            Component.literal("Your held item does not match the requested disc type."));
    private static final SimpleCommandExceptionType TARGET_HAS_DISC = new SimpleCommandExceptionType(
            Component.literal("That target already has that type of disc inserted."));
    private static final SimpleCommandExceptionType TARGET_MISSING_DISC = new SimpleCommandExceptionType(
            Component.literal("That target does not have that type of disc to extract."));
    private static final SimpleCommandExceptionType TARGET_NOT_LIVING = new SimpleCommandExceptionType(
            Component.literal("The selected target must be a living entity."));
    private static final SimpleCommandExceptionType INVALID_EXTRACT_TYPE = new SimpleCommandExceptionType(
            Component.literal("Disc type must be memory, stand, sight, or hearing."));
    private static final SimpleCommandExceptionType INVALID_MEMORY_ENTITY = new SimpleCommandExceptionType(
            Component.literal("That ID is not a living mob that can own a memory disc."));
    private static final SimpleCommandExceptionType DISC_DISABLED = new SimpleCommandExceptionType(
            Component.literal("That disc type is disabled in the server config."));
    private static final SimpleCommandExceptionType MOB_MEMORY_PLAYER_TARGET = new SimpleCommandExceptionType(
            Component.literal("Mob memory discs cannot be inserted into players."));
    private static final SimpleCommandExceptionType DISC_TARGET_BLACKLISTED = new SimpleCommandExceptionType(
            Component.literal("That entity is blacklisted from having discs."));

    private RoundaboutDiscCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("roundaboutDisc")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("insertdisc")
                        .then(Commands.literal("memorydisc")
                                .then(Commands.argument("username", EntityArgument.entity())
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests((context, builder) ->
                                                        SharedSuggestionProvider.suggest(MEMORY_TYPES, builder))
                                                .executes(context -> insertMemory(
                                                        context.getSource(),
                                                        livingTarget(EntityArgument.getEntity(context, "username")),
                                                        StringArgumentType.getString(context, "value"))))))
                        .then(Commands.literal("sightdisc")
                                .then(Commands.argument("username", EntityArgument.entity())
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests((context, builder) ->
                                                        SharedSuggestionProvider.suggest(BODY_DISC_VALUES, builder))
                                                .executes(context -> insertBodyDisc(
                                                        context.getSource(),
                                                        livingTarget(EntityArgument.getEntity(context, "username")),
                                                        WhitesnakeDiscUtil.SIGHT,
                                                        StringArgumentType.getString(context, "value"))))))
                        .then(Commands.literal("hearingdisc")
                                .then(Commands.argument("username", EntityArgument.entity())
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests((context, builder) ->
                                                        SharedSuggestionProvider.suggest(BODY_DISC_VALUES, builder))
                                                .executes(context -> insertBodyDisc(
                                                        context.getSource(),
                                                        livingTarget(EntityArgument.getEntity(context, "username")),
                                                        WhitesnakeDiscUtil.HEARING,
                                                        StringArgumentType.getString(context, "value"))))))
                        .then(Commands.literal("standdisc")
                                .then(Commands.argument("username", EntityArgument.entity())
                                        .then(Commands.literal("Hand")
                                                .executes(context -> insertHeldDisc(
                                                        context.getSource(),
                                                        livingTarget(EntityArgument.getEntity(context, "username")),
                                                        WhitesnakeDiscUtil.STAND)))
                                        .then(Commands.literal("hand")
                                                .executes(context -> insertHeldDisc(
                                                        context.getSource(),
                                                        livingTarget(EntityArgument.getEntity(context, "username")),
                                                        WhitesnakeDiscUtil.STAND))))))
                .then(Commands.literal("extractdisc")
                        .then(Commands.argument("username", EntityArgument.entity())
                                .then(Commands.argument("disc", StringArgumentType.word())
                                        .suggests((context, builder) ->
                                                SharedSuggestionProvider.suggest(EXTRACT_TYPES, builder))
                                        .executes(context -> extractDisc(
                                                context.getSource(),
                                                livingTarget(EntityArgument.getEntity(context, "username")),
                                                StringArgumentType.getString(context, "disc"))))))
                .then(Commands.literal("summondisc")
                        .then(Commands.literal("memorydisc")
                                .then(Commands.argument("mob", ResourceLocationArgument.id())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(
                                                BuiltInRegistries.ENTITY_TYPE.keySet(), builder))
                                        .executes(context -> summonMemoryDisc(
                                                context.getSource(),
                                                ResourceLocationArgument.getId(context, "mob")))))
                        .then(Commands.literal("sightdisc")
                                .executes(context -> summonBodyDisc(
                                        context.getSource(), WhitesnakeDiscUtil.SIGHT)))
                        .then(Commands.literal("hearingdisc")
                                .executes(context -> summonBodyDisc(
                                        context.getSource(), WhitesnakeDiscUtil.HEARING)))));
    }

    private static int summonMemoryDisc(CommandSourceStack source, ResourceLocation mobId)
            throws CommandSyntaxException {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(mobId)
                .orElseThrow(INVALID_MEMORY_ENTITY::create);
        Entity created = type.create(source.getLevel());
        if (!(created instanceof LivingEntity mob)) throw INVALID_MEMORY_ENTITY.create();
        if (WhitesnakeDiscUtil.isDiscBlacklisted(mob)) {
            created.discard();
            throw DISC_TARGET_BLACKLISTED.create();
        }

        ItemStack disc = new ItemStack(ModItems.MEMORY_DISC);
        DiscItemData.setOwner(disc, mob);
        DiscItemData.setPersonality(disc, MemoryPersonality.classify(mob));
        DiscItemData.captureMemoryReading(disc, mob);
        created.discard();
        giveDisc(source, disc);
        source.sendSuccess(() -> Component.literal("Summoned a "
                + mob.getType().getDescription().getString() + " memory disc."), true);
        return 1;
    }

    private static int summonBodyDisc(CommandSourceStack source, byte discType)
            throws CommandSyntaxException {
        requireBodyDiscEnabled(discType);
        ServerPlayer player = source.getPlayerOrException();
        ItemStack disc = new ItemStack(discType == WhitesnakeDiscUtil.SIGHT
                ? ModItems.SIGHT_DISC : ModItems.HEARING_DISC);
        giveDisc(player, disc);
        source.sendSuccess(() -> Component.literal("Summoned your " + discName(discType) + " disc."), true);
        return 1;
    }

    private static void giveDisc(CommandSourceStack source, ItemStack disc) throws CommandSyntaxException {
        giveDisc(source.getPlayerOrException(), disc);
    }

    private static void giveDisc(ServerPlayer player, ItemStack disc) {
        if (!player.getInventory().add(disc) && !disc.isEmpty()) player.drop(disc, false);
    }

    private static int insertMemory(CommandSourceStack source, LivingEntity target, String value)
            throws CommandSyntaxException {
        requireDiscTarget(target);
        String type = value.toLowerCase(Locale.ROOT);
        if (type.equals("hand")) return insertHeldDisc(source, target, WhitesnakeDiscUtil.MEMORY);

        DiscBearer bearer = (DiscBearer) target;
        if (type.equals("none")) {
            bearer.roundabout$setHasMemoryDisc(false);
            bearer.roundabout$setMemoryPersonality(MemoryPersonality.PLAYER);
            bearer.roundabout$setMemoryDiscOwnerId("");
            bearer.roundabout$setMemoryDiscOwnerName("");
            bearer.roundabout$setMemoryTameOwnerId("");
            bearer.roundabout$setMemoryTameOwnerName("");
            bearer.roundabout$setMemoryReading(new CompoundTag());
            source.sendSuccess(() -> Component.literal("Removed " + target.getName().getString()
                    + "'s memory disc."), true);
            return 1;
        }

        byte personality = switch (type) {
            case "hostile" -> MemoryPersonality.HOSTILE;
            case "neutral" -> MemoryPersonality.NEUTRAL;
            case "passive" -> MemoryPersonality.PASSIVE;
            case "player" -> MemoryPersonality.PLAYER;
            case "zombie" -> MemoryPersonality.ZOMBIE;
            default -> throw INVALID_MEMORY_TYPE.create();
        };
        if (target instanceof Player && personality != MemoryPersonality.PLAYER) {
            throw MOB_MEMORY_PLAYER_TARGET.create();
        }
        bearer.roundabout$setMemoryPersonality(personality);
        bearer.roundabout$setHasMemoryDisc(true);
        bearer.roundabout$setMemoryDiscOwnerId("");
        bearer.roundabout$setMemoryDiscOwnerName("");
        source.sendSuccess(() -> Component.literal("Inserted " + MemoryPersonality.name(personality)
                + " memory into " + target.getName().getString() + "."), true);
        return 1;
    }

    private static int insertBodyDisc(CommandSourceStack source, LivingEntity target, byte discType, String value)
            throws CommandSyntaxException {
        requireDiscTarget(target);
        requireBodyDiscEnabled(discType);
        String normalized = value.toLowerCase(Locale.ROOT);
        if (normalized.equals("hand")) return insertHeldDisc(source, target, discType);
        if (!normalized.equals("true") && !normalized.equals("false")) {
            throw INVALID_BODY_DISC_VALUE.create();
        }
        boolean present = Boolean.parseBoolean(normalized);
        DiscBearer bearer = (DiscBearer) target;
        if (discType == WhitesnakeDiscUtil.SIGHT) {
            bearer.roundabout$setHasSightDisc(present);
            bearer.roundabout$setSightDiscOwnerId("");
            bearer.roundabout$setSightDiscOwnerName("");
        } else {
            bearer.roundabout$setHasHearingDisc(present);
            bearer.roundabout$setHearingDiscOwnerId("");
            bearer.roundabout$setHearingDiscOwnerName("");
        }
        String name = discType == WhitesnakeDiscUtil.SIGHT ? "sight" : "hearing";
        source.sendSuccess(() -> Component.literal("Set " + target.getName().getString()
                + "'s " + name + " disc to " + present + "."), true);
        return 1;
    }

    private static int insertHeldDisc(CommandSourceStack source, LivingEntity target, byte discType)
            throws CommandSyntaxException {
        requireDiscTarget(target);
        requireBodyDiscEnabled(discType);
        ServerPlayer actor = source.getPlayerOrException();
        ItemStack held = matchingHeldDisc(actor, discType);
        if (held.isEmpty()) throw WRONG_HELD_DISC.create();
        if (discType == WhitesnakeDiscUtil.MEMORY && target instanceof Player
                && DiscItemData.getPersonality(held) != MemoryPersonality.PLAYER) {
            throw MOB_MEMORY_PLAYER_TARGET.create();
        }

        boolean inserted;
        if (discType == WhitesnakeDiscUtil.STAND) {
            if (!((StandUser) target).roundabout$getStandDisc().isEmpty()) throw TARGET_HAS_DISC.create();
            ItemStack implanted = held.copy();
            implanted.setCount(1);
            inserted = StandArrowItem.grantStand(implanted, target);
            if (inserted && !actor.isCreative()) held.shrink(1);
            if (inserted) {
                target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_INSERT_EVENT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            inserted = ((AbstractBodyDiscItem) held.getItem()).implantFromThrow(held, target, actor);
        }
        if (!inserted) throw TARGET_HAS_DISC.create();
        source.sendSuccess(() -> Component.literal("Inserted the held " + discName(discType)
                + " disc into " + target.getName().getString() + "."), true);
        return 1;
    }

    private static ItemStack matchingHeldDisc(ServerPlayer player, byte discType) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (matchesDiscType(mainHand, discType)) return mainHand;
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        return matchesDiscType(offHand, discType) ? offHand : ItemStack.EMPTY;
    }

    private static boolean matchesDiscType(ItemStack stack, byte discType) {
        if (stack.isEmpty()) return false;
        return switch (discType) {
            case WhitesnakeDiscUtil.STAND -> stack.getItem() instanceof StandDiscItem;
            case WhitesnakeDiscUtil.SIGHT -> stack.getItem() instanceof SightDiscItem;
            case WhitesnakeDiscUtil.MEMORY -> stack.getItem() instanceof MemoryDiscItem;
            case WhitesnakeDiscUtil.HEARING -> stack.getItem() instanceof HearingDiscItem;
            default -> false;
        };
    }

    private static int extractDisc(CommandSourceStack source, LivingEntity target, String requestedType)
            throws CommandSyntaxException {
        requireDiscTarget(target);
        ServerPlayer actor = source.getPlayerOrException();
        byte discType = switch (requestedType.toLowerCase(Locale.ROOT)) {
            case "memory" -> WhitesnakeDiscUtil.MEMORY;
            case "stand" -> WhitesnakeDiscUtil.STAND;
            case "sight" -> WhitesnakeDiscUtil.SIGHT;
            case "hearing" -> WhitesnakeDiscUtil.HEARING;
            default -> throw INVALID_EXTRACT_TYPE.create();
        };
        requireBodyDiscEnabled(discType);
        ItemStack extracted = WhitesnakeDiscUtil.extractDiscStack(target, discType);
        if (extracted.isEmpty()) throw TARGET_MISSING_DISC.create();

        target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_EJECT_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        boolean stored = actor.getInventory().add(extracted);
        if (!stored && !extracted.isEmpty()) actor.drop(extracted, false);
        source.sendSuccess(() -> Component.literal("Extracted " + target.getName().getString()
                + "'s " + discName(discType) + " disc into your inventory."), true);
        return 1;
    }

    private static LivingEntity livingTarget(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity living) return living;
        throw TARGET_NOT_LIVING.create();
    }

    private static String discName(byte discType) {
        return switch (discType) {
            case WhitesnakeDiscUtil.STAND -> "stand";
            case WhitesnakeDiscUtil.SIGHT -> "sight";
            case WhitesnakeDiscUtil.MEMORY -> "memory";
            case WhitesnakeDiscUtil.HEARING -> "hearing";
            default -> "unknown";
        };
    }

    private static void requireBodyDiscEnabled(byte discType) throws CommandSyntaxException {
        if (!WhitesnakeDiscUtil.isBodyDiscEnabled(discType)) throw DISC_DISABLED.create();
    }

    private static void requireDiscTarget(LivingEntity target) throws CommandSyntaxException {
        if (WhitesnakeDiscUtil.isDiscBlacklisted(target)) throw DISC_TARGET_BLACKLISTED.create();
    }
}
