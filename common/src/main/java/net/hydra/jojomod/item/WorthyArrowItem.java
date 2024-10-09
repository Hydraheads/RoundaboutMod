package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class WorthyArrowItem extends RoundaboutArrowItem {
    public WorthyArrowItem(Properties $$0) {
        super($$0);
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
            $$2.add(
                    Component.translatable("item.roundabout.worthy_arrow.info").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.ITALIC)
            );
            $$2.add(
                    Component.translatable("item.roundabout.worthy_arrow.info_2").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.ITALIC)
            );
    }
}