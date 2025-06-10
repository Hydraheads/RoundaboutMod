package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.index.PowerIndex;
import net.minecraft.resources.ResourceLocation;

public class GuiIcon {
    public ResourceLocation iconLocation;
    public byte index;

    public GuiIcon(byte index, ResourceLocation iconLocation)
    {
        this.iconLocation = iconLocation;
        this.index = index;
    }
}
