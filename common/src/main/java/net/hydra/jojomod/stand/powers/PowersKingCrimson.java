package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.world.entity.LivingEntity;

public class PowersKingCrimson extends NewPunchingStand {

    public PowersKingCrimson(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().theWorldSettings.enableTheWorld;
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingCrimson(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.KING_CRIMSON.create(this.getSelf().level());
    }
}