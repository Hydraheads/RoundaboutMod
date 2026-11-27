package net.hydra.jojomod.item;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HearingDiscItem extends AbstractBodyDiscItem {
    public HearingDiscItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canImplant(ItemStack stack, LivingEntity target) {
        return WhitesnakeDiscUtil.isHearingDiscEnabled()
                && !((DiscBearer) target).sba$ownsHearingDisc();
    }

    @Override
    protected void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.sba$setHearingDiscOwnerId(DiscItemData.getOwnerId(stack));
        bearer.sba$setHearingDiscOwnerName(DiscItemData.getOwnerName(stack));
        bearer.sba$setHasHearingDisc(true);
    }

    @Override
    protected boolean showPersonality() {
        return false;
    }
}
