package net.hydra.jojomod.item;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.DreamingMemoryController;
import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MemoryDiscItem extends AbstractBodyDiscItem {
    public MemoryDiscItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canImplant(ItemStack stack, LivingEntity target) {
        if (target instanceof Player && DiscItemData.getPersonality(stack) != MemoryPersonality.PLAYER) {
            return false;
        }
        if (DreamingMemoryController.canTemporarilyImplant(target)) return true;
        return !((DiscBearer) target).roundabout$ownsMemoryDisc();
    }

    @Override
    protected void implant(ItemStack stack, LivingEntity target) {
        if (DreamingMemoryController.canTemporarilyImplant(target)) {
            DreamingMemoryController.implant(stack, target);
            return;
        }
        applyMemory(stack, target);
    }

    public static void applyMemory(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.roundabout$setMemoryDiscOwnerId(DiscItemData.getOwnerId(stack));
        bearer.roundabout$setMemoryDiscOwnerName(DiscItemData.getOwnerName(stack));
        bearer.roundabout$setMemoryPersonality(DiscItemData.getPersonality(stack));
        bearer.roundabout$setMemoryTameOwnerId(DiscItemData.getTameOwnerId(stack));
        bearer.roundabout$setMemoryTameOwnerName(DiscItemData.getTameOwnerName(stack));
        bearer.roundabout$setMemoryReading(DiscItemData.getMemoryReading(stack));
        bearer.roundabout$setHasMemoryDisc(true);
        if (target instanceof Mob mob) {
            mob.setNoAi(false);
            mob.setTarget(null);
        }
    }

    @Override
    protected boolean showPersonality() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            if (level.isClientSide()) ClientUtil.openMemoryReadingScreen(stack.copy(), hand);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, hand);
    }
}
