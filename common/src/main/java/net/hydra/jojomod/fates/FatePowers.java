package net.hydra.jojomod.fates;

import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.world.entity.LivingEntity;

public class FatePowers {
    /**Note that self refers to the entity with the power*/
    public final LivingEntity self;

    public FatePowers(LivingEntity self) {
        this.self = self;
    }
    public FatePowers() {
        this.self = null;
    }

    public void tick(){
    }


    /**This is imporant, on every fate class, override this and do something like
     *     return new VampireFate(entity); */
    public FatePowers generateFatePowers(LivingEntity entity){
        return new FatePowers(entity);
    }
}
