package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;

public class PowersGER extends PowersGoldExperience{
    public PowersGER(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersGER(entity);}

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GOLD_EXPERIENCE_REQUIEM.create(this.getSelf().level());
    }

    public boolean canUseRequiem() { return false; }
    public boolean isRequiemStand() {return true;}

    public boolean hasNormalGEDisc() { return false;}
}