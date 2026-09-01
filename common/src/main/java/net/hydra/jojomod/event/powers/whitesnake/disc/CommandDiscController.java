package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.item.CommandDiscItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class CommandDiscController {
    private static final int ATTACK_COMMAND_DURATION = 100;
    private static final int EXPLOSIVE_COMMAND_DURATION = 200;
    private static final double EXPLOSIVE_COMMAND_DISTANCE_SQR = 100.0D;
    private static final Map<LivingEntity, AttackCommand> ATTACK_COMMANDS = new WeakHashMap<>();
    private static final Map<LivingEntity, ExplosiveCommand> EXPLOSIVE_COMMANDS = new WeakHashMap<>();

    private CommandDiscController() {
    }

    public static void commandAttack(LivingEntity commanded, LivingEntity target) {
        ATTACK_COMMANDS.put(commanded,
                new AttackCommand(target.getUUID(), commanded.level().getGameTime() + ATTACK_COMMAND_DURATION));
    }

    public static void clearAttackCommand(LivingEntity commanded) {
        ATTACK_COMMANDS.remove(commanded);
    }

    public static void commandExplosion(LivingEntity commanded, LivingEntity user) {
        EXPLOSIVE_COMMANDS.put(commanded, new ExplosiveCommand(commanded.position(), user,
                commanded.level().getGameTime() + EXPLOSIVE_COMMAND_DURATION));
    }

    public static void tickExplosion(LivingEntity commanded) {
        if (commanded.level().isClientSide()) return;
        ExplosiveCommand command = EXPLOSIVE_COMMANDS.get(commanded);
        if (command == null) return;
        if (!commanded.isAlive() || commanded.level().getGameTime() >= command.expiresAt()) {
            EXPLOSIVE_COMMANDS.remove(commanded);
            return;
        }
        if (commanded.position().distanceToSqr(command.origin()) >= EXPLOSIVE_COMMAND_DISTANCE_SQR) {
            EXPLOSIVE_COMMANDS.remove(commanded);
            CommandDiscItem.explode(commanded, command.user());
        }
    }

    public static void tick(LivingEntity commanded) {
        if (commanded.level().isClientSide() || !commanded.isAlive()) return;
        AttackCommand command = ATTACK_COMMANDS.get(commanded);
        if (command == null || !(commanded.level() instanceof ServerLevel level)) return;
        if (level.getGameTime() >= command.expiresAt()) {
            clearAttackCommand(commanded);
            if (commanded instanceof Mob mob) mob.setTarget(null);
            return;
        }
        Entity found = level.getEntity(command.targetId());
        if (!(found instanceof LivingEntity target) || !target.isAlive() || target == commanded) {
            clearAttackCommand(commanded);
            if (commanded instanceof Mob mob) mob.setTarget(null);
            return;
        }

        if (commanded instanceof Mob mob) {
            if (mob.isNoAi()) mob.setNoAi(false);
            mob.setTarget(target);
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            mob.getNavigation().moveTo(target, 1.15D);
            double reach = mob.getBbWidth() + target.getBbWidth() + 1.0D;
            if (mob.distanceToSqr(target) <= reach * reach && mob.tickCount % 20 == 0) {
                mob.swing(InteractionHand.MAIN_HAND);
                target.hurt(mob.damageSources().mobAttack(mob), 3.0F);
            }
        } else if (commanded instanceof ServerPlayer player && player.distanceToSqr(target) <= 9.0D
                && player.tickCount % 20 == 0) {
            MemoryAiController.forcePlayerAttack(player, target);
        }
    }

    private record AttackCommand(UUID targetId, long expiresAt) {
    }

    private record ExplosiveCommand(Vec3 origin, LivingEntity user, long expiresAt) {
    }
}
