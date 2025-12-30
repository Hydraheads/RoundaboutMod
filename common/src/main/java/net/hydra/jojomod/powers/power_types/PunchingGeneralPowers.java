package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.world.entity.LivingEntity;

public class PunchingGeneralPowers extends GeneralPowers {
    public PunchingGeneralPowers(LivingEntity self) {
        super(self);
    }
    public PunchingGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new PunchingGeneralPowers(entity);
    }
}
