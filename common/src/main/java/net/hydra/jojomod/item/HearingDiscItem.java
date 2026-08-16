package net.hydra.jojomod.item;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HearingDiscItem extends AbstractBodyDiscItem {
    public HearingDiscItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canImplant(LivingEntity target) {
        return WhitesnakeDiscUtil.isHearingDiscEnabled()
                && !((DiscBearer) target).roundabout$ownsHearingDisc();
    }

    @Override
    protected void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.roundabout$setHearingDiscOwnerId(DiscItemData.getOwnerId(stack));
        bearer.roundabout$setHearingDiscOwnerName(DiscItemData.getOwnerName(stack));
        bearer.roundabout$setHasHearingDisc(true);
    }

    @Override
    protected boolean showPersonality() {
        return false;
    }
}
