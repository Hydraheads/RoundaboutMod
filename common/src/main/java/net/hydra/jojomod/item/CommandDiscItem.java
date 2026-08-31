package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.whitesnake.disc.CommandDiscController;
import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryAiController;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ExplosionUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public final class CommandDiscItem extends Item {
    public enum Command {
        JUMP_BACK,
        ATTACK,
        FORGET,
        EXPLOSIVE
    }

    private final Command command;

    public CommandDiscItem(Properties properties, Command command) {
        super(properties);
        this.command = command;
    }

    public boolean applyCommand(Entity target, LivingEntity whitesnakeUser) {
        if (!(target instanceof LivingEntity living) || WhitesnakeDiscUtil.isDiscBlacklisted(living)) return false;
        boolean applied = switch (command) {
            case JUMP_BACK -> applyJumpBack(living, whitesnakeUser);
            case ATTACK -> applyAttack(living, whitesnakeUser);
            case FORGET -> applyForget(living, whitesnakeUser);
            case EXPLOSIVE -> applyExplosionCommand(living, whitesnakeUser);
        };
        if (applied && !target.level().isClientSide()) {
            target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_INSERT_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (applied && target instanceof ServerPlayer player) {
            String message = switch (command) {
                case JUMP_BACK -> "message.roundabout.command.jump_back";
                case ATTACK -> "message.roundabout.command.attack";
                case FORGET -> "message.roundabout.command.forget";
                case EXPLOSIVE -> "message.roundabout.command.explosive";
            };
            player.displayClientMessage(Component.translatable(message), true);
        }
        return applied;
    }

    private static boolean applyExplosionCommand(LivingEntity target, LivingEntity user) {
        if (!target.level().isClientSide()) CommandDiscController.commandExplosion(target, user);
        return true;
    }

    public static void explode(LivingEntity target, LivingEntity user) {
        if (target.level().isClientSide()) return;
        Level level = target.level();
        Vec3 position = target.position();
        ExplosionUtil.explosionHurtSneakyWithMulti(position,
                ModDamageTypes.of(level, ModDamageTypes.EXPLOSIVE_STAND, user), level,
                ClientNetworking.getAppropriateConfig().whitesnakeSettings.explosiveDiscDamage,
                0.4F, 1.5F, 1.0F, 1.0F);
        ExplosionUtil.explodeEffects(position, level, ModParticles.KILLER_QUEEN_EXPLOSION, 0.55F);
        level.playSound(null, target.blockPosition(), ModSounds.KILLER_QUEEN_EXPLOSION_EVENT,
                SoundSource.PLAYERS, 0.3F, 1.0F);
    }

    private static boolean applyJumpBack(LivingEntity target, LivingEntity user) {
        Vec3 away = target.position().subtract(user.position()).multiply(1.0D, 0.0D, 1.0D);
        if (away.lengthSqr() < 0.0001D) {
            away = user.getLookAngle().multiply(-1.0D, 0.0D, -1.0D);
        }
        away = away.normalize();
        target.setDeltaMovement(away.x * 1.15D, Math.max(0.65D, target.getDeltaMovement().y), away.z * 1.15D);
        target.hurtMarked = true;
        target.fallDistance = 0.0F;
        return true;
    }

    private static boolean applyAttack(LivingEntity target, LivingEntity user) {
        LivingEntity commandedTarget = user.getLastHurtMob();
        if (commandedTarget == null || !commandedTarget.isAlive() || commandedTarget == target) return false;
        if (target instanceof Mob mob) {
            if (mob.isNoAi()) mob.setNoAi(false);
            mob.setTarget(commandedTarget);
            mob.getLookControl().setLookAt(commandedTarget, 30.0F, 30.0F);
            mob.getNavigation().moveTo(commandedTarget, 1.15D);
            CommandDiscController.commandAttack(target, commandedTarget);
            return true;
        }
        if (target instanceof ServerPlayer player) {
            player.lookAt(EntityAnchorArgument.Anchor.EYES,
                    commandedTarget.getEyePosition());
            CommandDiscController.commandAttack(target, commandedTarget);
            MemoryAiController.forcePlayerAttack(player, commandedTarget);
            return true;
        }
        return false;
    }

    private static boolean applyForget(LivingEntity target, LivingEntity user) {
        if (target == user) {
            target.setLastHurtByMob(null);
            target.setLastHurtByPlayer(null);
            target.setLastHurtMob(null);
            return true;
        }
        if (!(target instanceof Mob mob) || !(target instanceof NeutralMob neutral)
                || !neutral.isAngryAt(user)) return false;
        neutral.stopBeingAngry();
        neutral.setLastHurtByMob(null);
        neutral.setLastHurtByPlayer(null);
        mob.setTarget(null);
        mob.getNavigation().stop();
        ((StandUser) target).roundabout$deeplyRemoveAttackTarget();
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player,
                                                   InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) return InteractionResultHolder.pass(stack);
        if (!level.isClientSide() && applyCommand(player, player) && !player.isCreative()) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        DiscItemData.addOwnerTooltip(stack, lines, false);
        lines.add(Component.translatable(getDescriptionId() + ".desc"));
    }
}
