package net.hydra.jojomod.item;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SightDiscItem extends AbstractBodyDiscItem {
    public SightDiscItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canImplant(ItemStack stack, LivingEntity target) {
        return WhitesnakeDiscUtil.isSightDiscEnabled()
                && WhitesnakeDiscUtil.canCarrySightDisc(target)
                && !((DiscBearer) target).sba$ownsSightDisc();
    }

    @Override
    protected void implant(ItemStack stack, LivingEntity target) {
        DiscBearer bearer = (DiscBearer) target;
        bearer.sba$setSightDiscOwnerId(DiscItemData.getOwnerId(stack));
        bearer.sba$setSightDiscOwnerName(DiscItemData.getOwnerName(stack));
        bearer.sba$setHasSightDisc(true);
    }

    @Override
    protected boolean showPersonality() {
        return false;
    }
}
