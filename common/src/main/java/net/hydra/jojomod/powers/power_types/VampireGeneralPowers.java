package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class VampireGeneralPowers extends PunchingGeneralPowers {
    public VampireGeneralPowers(LivingEntity self) {
        super(self);
    }
    public VampireGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new VampireGeneralPowers(entity);
    }

    /**The text name of the fate*/
    public Component getPowerName(){
        return Component.translatable("text.roundabout.powers.vampire");
    }
    public Component getPowerTagName(){
        return Component.translatable("text.roundabout.powers.vampire_select");
    }
    public int getMaxGuardPoints(){
        return 13;
    }
}
