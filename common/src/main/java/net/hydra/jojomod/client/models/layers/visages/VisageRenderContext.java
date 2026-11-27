package net.hydra.jojomod.client.models.layers.visages;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.hydra.jojomod.stand.powers.PowersOasis;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class VisageRenderContext {
    public boolean hideExtraPartsWithSuit = false;
    public boolean isHoldingBowlerHat = false;
    public boolean isUsingStoneMask = false;
    public boolean isBodyFrozen = false;
    public boolean isLegsFrozen = false;

    public VisageRenderContext(LivingEntity entity, float partialTicks) {
        StandUser user = ((StandUser) entity);

        float heyFull = 0;

        boolean hasWhiteAlbumOut =user.sba$getStandPowers() instanceof PowersWhiteAlbum pw && pw.renderHelmet();
        //int whiteAlbumTicks = user.sba$getWhiteAlbumVanishTicks();
        int whiteAlbumTicks = MainUtil.getWornStandVanishticksSafe(user, PowersWhiteAlbum.class);
        byte skin = user.sba$getStandSkin();
        if (hasWhiteAlbumOut || whiteAlbumTicks > 0){
            if (user.sba$getStandPowers() instanceof PowersWhiteAlbum) {
                if (user.sba$getLastStandSkin() != skin){
                    user.sba$setLastStandSkin(skin);
                    whiteAlbumTicks = 0;
                    user.sba$setCurrentStandVanishTicks(0);
                }
            }

            float partialTicks2 = partialTicks % 1;
            if (hasWhiteAlbumOut){
                heyFull = whiteAlbumTicks+partialTicks2;
                heyFull = Math.min(heyFull/10,1f);
            } else {
                heyFull = whiteAlbumTicks-partialTicks2;
                heyFull = Math.max(heyFull/10,0);
            }

            if (heyFull > 0){
                hideExtraPartsWithSuit = true;
            }
        }

        boolean hasOasisOut = user.sba$getStandPowers() instanceof PowersOasis po && po.renderSuit();
        int oasisTicks = user.sba$getOasisVanishTicks();
        float fadeAmt = 0;
        byte oasisSkin = user.sba$getStandSkin();
        if (hasOasisOut || oasisTicks > 0){
            if (user.sba$getLastStandSkin() != oasisSkin){
                user.sba$setLastStandSkin(oasisSkin);
                oasisTicks = 0;
                user.sba$setOasisVanishTicks(0);
            }

            float partialTicks2 = partialTicks % 1;
            if (hasOasisOut){
                fadeAmt = oasisTicks+partialTicks2;
                fadeAmt = Math.min(fadeAmt/10,1f);
            } else {
                fadeAmt = oasisTicks-partialTicks2;
                fadeAmt = Math.max(fadeAmt/10,0);
            }

            if (fadeAmt > 0){
                hideExtraPartsWithSuit = true;
            }
        }


        if (user.sba$getStandPowers() instanceof Powers20thCenturyBoy && PowerTypes.hasStandActive(entity) && user.sba$getIdlePos() == 0){hideExtraPartsWithSuit = true;}

        ItemStack hand = entity.getMainHandItem();
        ItemStack offHand = entity.getOffhandItem();

        isHoldingBowlerHat = (hand.getItem() instanceof BowlerHatItem) || (offHand.getItem() instanceof BowlerHatItem);
        isUsingStoneMask = MainUtil.isWearingEitherStoneMask(entity);
        isBodyFrozen = HeatUtil.isBodyFrozen(entity);
        isLegsFrozen = HeatUtil.isLegsFrozen(entity);
    }

}
