package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MaskItem extends Item {
    public final VisageData visageData;
    public MaskItem(Properties $$0, VisageData visageData) {
        super($$0);
        this.visageData = visageData;
    }

    public MutableComponent getDisplayName2() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        super.appendHoverText($$0,$$1,$$2,$$3);
        $$2.add(this.getDisplayName2());
        if (visageData.hasVoices() || $$0.is(ModItems.BLANK_MASK)){
            $$2.add(
                    Component.translatable("roundabout.cinderella.mod_visage.voice_available").withStyle(ChatFormatting.AQUA)
            );
        }
    }
}
