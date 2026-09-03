package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class MemoryDiscConversionService {
    private MemoryDiscConversionService() {
    }

    public static void convert(ServerPlayer player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        ItemStack result;
        String message;
        if (DiscItemData.isCreeperMemory(held)) {
            result = new ItemStack(ModItems.EXPLOSIVE_COMMAND_DISC);
            message = "message.roundabout.memory_conversion_explosive";
        } else if (DiscItemData.isSlimeMemory(held)) {
            result = new ItemStack(ModItems.JUMP_BACK_COMMAND_DISC);
            message = "message.roundabout.memory_conversion_jump_back";
        } else {
            player.displayClientMessage(Component.translatable(
                    "message.roundabout.memory_conversion_invalid"), true);
            return;
        }

        if (held.getCount() == 1) {
            player.setItemInHand(hand, result);
        } else {
            held.shrink(1);
            if (!player.getInventory().add(result)) player.drop(result, false);
        }
        player.level().playSound(null, player.blockPosition(), ModSounds.WHITESNAKE_COMMAND_DISC_CREATE_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        player.displayClientMessage(Component.translatable(message), true);
    }
}
