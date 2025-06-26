package net.hydra.jojomod.event.powers.visagedata;

import net.minecraft.world.entity.LivingEntity;

public class NonCharacterVisage extends VisageData{
    @Override
    public VisageData generateVisageData(LivingEntity entity){
        return new NonCharacterVisage(entity);
    }
    public NonCharacterVisage(LivingEntity self) {
        super(self);
    }

    public boolean isCharacterVisage(){
        return false;
    }
}
