package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CreativeStandDiscItem extends MaxStandDiscItem {
    public CreativeStandDiscItem(Properties properties, StandPowers standPowers) {
        super(properties, standPowers);
    }

    public CreativeStandDiscItem(Properties properties, StandPowers standPowers, StandDiscItem baseDisc) {
        super(properties, standPowers, baseDisc);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(
                Component.translatable("item.roundabout.hairspray.info")
                        .withStyle(ChatFormatting.AQUA)
                        .withStyle(ChatFormatting.ITALIC)
        );
    }

    @Override
    public MutableComponent getDisplayName2() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
}