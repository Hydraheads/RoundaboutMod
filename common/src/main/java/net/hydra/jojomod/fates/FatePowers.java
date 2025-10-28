package net.hydra.jojomod.fates;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.minecraft.world.entity.LivingEntity;

public class FatePowers extends AbilityScapeBasis {

    public FatePowers(LivingEntity self) {
        super(self);
    }
    public FatePowers() {
        super(null);
    }
    public void tick(){
    }


    /**This is imporant, on every fate class, override this and do something like
     *     return new VampireFate(entity); */
    public FatePowers generateFatePowers(LivingEntity entity){
        return new FatePowers(entity);
    }
}
