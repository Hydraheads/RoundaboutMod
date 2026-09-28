package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.entity.pathfinding.CommandDiscPossession;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.CommandDiscItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.EnumMap;
import java.util.UUID;
import java.util.WeakHashMap;

public final class CommandDiscController {
    private static final int ATTACK_COMMAND_DURATION = 100;
    private static final int EXPLOSIVE_COMMAND_DURATION = 400;
    private static final double EXPLOSIVE_COMMAND_DISTANCE_SQR = 100.0D;
    private static final Map<Mob, AttackCommand> ATTACK_COMMANDS = new WeakHashMap<>();
    private static final Map<LivingEntity, ExplosiveCommand> EXPLOSIVE_COMMANDS = new WeakHashMap<>();
    private static final Map<LivingEntity, Map<CommandDiscItem.Command, ItemStack>> COMMAND_DISCS = new WeakHashMap<>();

    private CommandDiscController() {
    }

    public static void storeCommandDisc(LivingEntity commanded, CommandDiscItem.Command command, ItemStack stack) {
        if (commanded.level().isClientSide()) return;
        ejectCommandDisc(commanded, command);
        if (!stack.isEmpty()) {
            COMMAND_DISCS.computeIfAbsent(commanded, target -> new EnumMap<>(CommandDiscItem.Command.class))
                    .put(command, stack);
        }
        if (command == CommandDiscItem.Command.JUMP_BACK || command == CommandDiscItem.Command.FORGET) {
            ejectCommandDisc(commanded, command);
        }
    }

    private static void ejectCommandDisc(LivingEntity commanded, CommandDiscItem.Command command) {
        Map<CommandDiscItem.Command, ItemStack> discs = COMMAND_DISCS.get(commanded);
        if (discs == null) return;
        ItemStack stack = discs.remove(command);
        if (discs.isEmpty()) COMMAND_DISCS.remove(commanded);
        WhitesnakeDiscUtil.ejectCommandDisc(commanded, stack);
    }

    public static void ejectCommandDisc(LivingEntity commanded) {
        if (commanded.level().isClientSide()) return;
        ATTACK_COMMANDS.remove(commanded);
        EXPLOSIVE_COMMANDS.remove(commanded);
        Map<CommandDiscItem.Command, ItemStack> discs = COMMAND_DISCS.remove(commanded);
        if (discs != null) {
            for (ItemStack stack : discs.values()) WhitesnakeDiscUtil.ejectCommandDisc(commanded, stack);
        }
    }

    public static void save(LivingEntity commanded, CompoundTag tag) {
        tag.remove("roundabout.CommandDiscs");
        if (!COMMAND_DISCS.containsKey(commanded)) return;
        CompoundTag discs = new CompoundTag();
        COMMAND_DISCS.get(commanded).forEach((command, stack) ->
                discs.put(command.name(), stack.save(new CompoundTag())));
        tag.put("roundabout.CommandDiscs", discs);
    }

    public static void load(LivingEntity commanded, CompoundTag tag) {
        ATTACK_COMMANDS.remove(commanded);
        EXPLOSIVE_COMMANDS.remove(commanded);
        COMMAND_DISCS.remove(commanded);
        if (!tag.contains("roundabout.CommandDiscs", 10)) return;
        CompoundTag discs = tag.getCompound("roundabout.CommandDiscs");
        for (CommandDiscItem.Command command : CommandDiscItem.Command.values()) {
            if (!discs.contains(command.name(), 10)) continue;
            ItemStack stack = ItemStack.of(discs.getCompound(command.name()));
            if (stack.getItem() instanceof CommandDiscItem disc && disc.getCommand() == command
                    && stack.getDamageValue() < stack.getMaxDamage()) {
                stack.setCount(1);
                COMMAND_DISCS.computeIfAbsent(commanded, target -> new EnumMap<>(CommandDiscItem.Command.class))
                        .put(command, stack);
            }
        }
    }

    public static void commandAttack(Mob commanded, LivingEntity target) {
        ATTACK_COMMANDS.put(commanded,
                new AttackCommand(target.getUUID(), commanded.level().getGameTime() + ATTACK_COMMAND_DURATION));
    }

    public static boolean commandAttack(ServerPlayer commanded, LivingEntity target) {
        StandUser standUser = (StandUser) commanded;
        if (standUser.roundabout$isPossessed()) return false;

        CommandDiscPossession possession = new CommandDiscPossession(commanded.level(), commanded, target);
        possession.setPos(commanded.position());
        if (!commanded.level().addFreshEntity(possession)) return false;
        if (!commanded.startRiding(possession, true)) {
            possession.discard();
            return false;
        }
        commanded.stopUsingItem();
        standUser.roundabout$setPossessor(possession);
        standUser.roundabout$setActive(false);
        return true;
    }

    private static void clearAttackCommand(Mob commanded) {
        ATTACK_COMMANDS.remove(commanded);
        ejectCommandDisc(commanded, CommandDiscItem.Command.ATTACK);
    }

    public static void commandExplosion(LivingEntity commanded, LivingEntity user) {
        EXPLOSIVE_COMMANDS.put(commanded, new ExplosiveCommand(commanded.position(), user,
                commanded.level().getGameTime() + EXPLOSIVE_COMMAND_DURATION));
    }

    public static void tickExplosion(LivingEntity commanded) {
        if (commanded.level().isClientSide()) return;
        ExplosiveCommand command = EXPLOSIVE_COMMANDS.get(commanded);
        if (command == null) {
            ejectCommandDisc(commanded, CommandDiscItem.Command.EXPLOSIVE);
            return;
        }
        if (!commanded.isAlive() || commanded.level().getGameTime() >= command.expiresAt()) {
            EXPLOSIVE_COMMANDS.remove(commanded);
            ejectCommandDisc(commanded, CommandDiscItem.Command.EXPLOSIVE);
            return;
        }
        if (commanded.position().distanceToSqr(command.origin()) >= EXPLOSIVE_COMMAND_DISTANCE_SQR) {
            EXPLOSIVE_COMMANDS.remove(commanded);
            Map<CommandDiscItem.Command, ItemStack> discs = COMMAND_DISCS.get(commanded);
            ItemStack stack = discs == null ? ItemStack.EMPTY : discs.remove(CommandDiscItem.Command.EXPLOSIVE);
            if (discs != null && discs.isEmpty()) COMMAND_DISCS.remove(commanded);
            CommandDiscItem.explode(commanded, command.user());
            WhitesnakeDiscUtil.ejectCommandDisc(commanded, stack);
        }
    }

    public static void tick(LivingEntity commanded) {
        if (commanded.level().isClientSide()) return;
        ejectCommandDisc(commanded, CommandDiscItem.Command.JUMP_BACK);
        ejectCommandDisc(commanded, CommandDiscItem.Command.FORGET);
        if (!commanded.isAlive()) {
            ejectCommandDisc(commanded);
            return;
        }
        if (!(commanded instanceof Mob mob)) {
            if (!(((StandUser) commanded).roundabout$getPossessor() instanceof CommandDiscPossession possession)
                    || possession.isRemoved()) {
                ejectCommandDisc(commanded, CommandDiscItem.Command.ATTACK);
            }
            return;
        }
        AttackCommand command = ATTACK_COMMANDS.get(mob);
        if (command == null) {
            ejectCommandDisc(mob, CommandDiscItem.Command.ATTACK);
            return;
        }
        if (!(mob.level() instanceof ServerLevel level)) return;
        if (level.getGameTime() >= command.expiresAt() || !((DiscBearer) mob).roundabout$hasMemoryDisc()) {
            clearAttackCommand(mob);
            mob.setTarget(null);
            return;
        }
        Entity found = level.getEntity(command.targetId());
        if (!(found instanceof LivingEntity target) || !target.isAlive() || target == mob) {
            clearAttackCommand(mob);
            mob.setTarget(null);
            return;
        }

        if (mob.isNoAi()) mob.setNoAi(false);
        mob.setTarget(target);
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getNavigation().moveTo(target, 1.15D);
        double reach = mob.getBbWidth() + target.getBbWidth() + 1.0D;
        if (mob.distanceToSqr(target) <= reach * reach && mob.tickCount % 20 == 0) {
            mob.swing(InteractionHand.MAIN_HAND);
            target.hurt(mob.damageSources().mobAttack(mob), 3.0F);
        }
    }

    private record AttackCommand(UUID targetId, long expiresAt) {
    }

    private record ExplosiveCommand(Vec3 origin, LivingEntity user, long expiresAt) {
    }
}
