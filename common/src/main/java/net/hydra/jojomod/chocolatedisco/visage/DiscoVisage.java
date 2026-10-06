package net.hydra.jojomod.chocolatedisco.visage;

import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.entity.LivingEntity;

public class DiscoVisage extends VisageData {

    public DiscoVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public VisageData generateVisageData(LivingEntity entity) {
        return new DiscoVisage(entity);
    }

    @Override
    public String getSkinPath() {
        return "disco";
    }
}