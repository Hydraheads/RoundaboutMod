package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
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

    public boolean renderHelmet(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }




}
