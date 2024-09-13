package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class StandDiscItem extends Item {

    public final StandPowers standPowers;
    public StandDiscItem(Properties $$0, StandPowers standPowers) {
        super($$0);
        this.standPowers = standPowers;
    }

    public void generateStandPowers(LivingEntity entity){
        ((StandUser)entity).setStandPowers(standPowers.generateStandPowers(entity));
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(this.getDisplayName().withStyle(ChatFormatting.AQUA));
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
}
