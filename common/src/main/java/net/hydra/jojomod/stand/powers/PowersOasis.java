package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PowersOasis extends NewDashPreset {
    public PowersOasis(LivingEntity self) {
        super(self);
    }


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersOasis(entity);
    }

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public boolean rendersPlayer(){
        return true;
    }



    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

    }

    public boolean renderSuit(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }

    // stand fading
    public static float getOasisAmt(Entity entity, float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasOasisOut = user.roundabout$getStandPowers() instanceof PowersOasis po && po.renderSuit();
            int oasisTicks = user.roundabout$getOasisVanishTicks();
            if (hasOasisOut || oasisTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    oasisTicks = 0;
                    user.roundabout$setOasisVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasOasisOut) {
                    heyFull = oasisTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = oasisTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }



    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            default -> "base";
        };
    }

}
