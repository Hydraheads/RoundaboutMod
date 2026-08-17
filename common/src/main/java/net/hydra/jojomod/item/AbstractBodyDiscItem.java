package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractBodyDiscItem extends Item {
    protected AbstractBodyDiscItem(Properties properties) {
        super(properties);
    }

    protected abstract boolean canImplant(ItemStack stack, LivingEntity target);
    protected abstract void implant(ItemStack stack, LivingEntity target);
    protected abstract boolean showPersonality();

    private boolean implantAndConsume(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (!canImplant(stack, target)) {
            return false;
        }
        if (!target.level().isClientSide()) {
            implant(stack, target);
            target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_INSERT_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!(user instanceof Player player) || !player.isCreative()) {
                stack.shrink(1);
            }
        }
        return true;
    }

    public boolean implantFromThrow(ItemStack stack, LivingEntity target, LivingEntity thrower) {
        return implantAndConsume(stack, target, thrower);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean implanted = implantAndConsume(stack, player, player);
        return implanted ? InteractionResultHolder.sidedSuccess(stack, level.isClientSide())
                : InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        implantAndConsume(stack, target, attacker);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        DiscItemData.addOwnerTooltip(stack, lines, showPersonality());
    }
}
