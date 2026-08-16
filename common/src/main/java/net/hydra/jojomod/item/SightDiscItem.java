package net.hydra.jojomod.item;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SightDiscItem extends AbstractBodyDiscItem {
    public SightDiscItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canImplant(LivingEntity target) {
        return WhitesnakeDiscUtil.isSightDiscEnabled()
                && WhitesnakeDiscUtil.canCarrySightDisc(target)
                && !((DiscBearer) target).roundabout$ownsSightDisc();
    }

    @Override
    protected void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.roundabout$setSightDiscOwnerId(DiscItemData.getOwnerId(stack));
        bearer.roundabout$setSightDiscOwnerName(DiscItemData.getOwnerName(stack));
        bearer.roundabout$setHasSightDisc(true);
    }

    @Override
    protected boolean showPersonality() {
        return false;
    }
}
