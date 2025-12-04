package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class HairsprayItem extends Item {
    public HairsprayItem(Properties $$0) {
        super($$0);
    }
    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        if (ConfigManager.getClientConfig().showCreativeTextOnWorthinessArrow) {
            $$2.add(
                    Component.translatable("item.roundabout.hairspray.info").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.ITALIC)
            );
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$0.isClientSide) {
            ClientUtil.openHairspryUI();
        }
        return InteractionResultHolder.fail($$3);
    }
}
