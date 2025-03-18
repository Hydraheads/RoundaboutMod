package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MaxStandDiscItem extends StandDiscItem{
    public StandDiscItem baseDisc;
    public MaxStandDiscItem(Properties $$0, StandPowers standPowers) {
        super($$0, standPowers);
    }

    @Override

    public MutableComponent getDisplayName2() {
        return Component.translatable(baseDisc.getDescriptionId() + ".desc");
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(this.getDisplayName2().withStyle(ChatFormatting.AQUA));
        $$2.add(Component.translatable("leveling.roundabout.disc_maxed").withStyle(ChatFormatting.GRAY));
        CompoundTag $$4 = $$0.getTagElement("Memory");
        if ($$4 == null || $$1 == null || !$$4.contains("Skin")) return;
        byte skin = ($$4.getByte("Skin"));
        $$2.add(Component.literal(standPowers.getSkinName(skin).getString()).withStyle(ChatFormatting.BLUE));
    }
}
