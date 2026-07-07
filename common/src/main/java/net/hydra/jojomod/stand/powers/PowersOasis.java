package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.world.entity.LivingEntity;

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



}
