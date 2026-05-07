package net.hydra.jojomod.stand.powers.presets;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.minecraft.client.Options;
import net.minecraft.world.entity.LivingEntity;

public class StandPowerRewrite extends StandPowers {
    public StandPowerRewrite(LivingEntity self) {
        super(self);
    }
    /**If you are not currently supposed to be able to activate your stand, override for sealing reasons*/
    @Override
    public boolean canSummonStand(){
        return true;
    }

}
