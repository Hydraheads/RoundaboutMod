package net.hydra.jojomod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SnubnoseAmmoItem extends AmmoItem {
    public SnubnoseAmmoItem(Properties $$0) {
        super($$0);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("text.roundabout.bullets.revolver").withStyle(ChatFormatting.YELLOW));
    }
}