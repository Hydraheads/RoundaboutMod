package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.item.Item;

public class MaskItem extends Item {
    public final VisageData visageData;
    public MaskItem(Properties $$0, VisageData visageData) {
        super($$0);
        this.visageData = visageData;
    }
}
