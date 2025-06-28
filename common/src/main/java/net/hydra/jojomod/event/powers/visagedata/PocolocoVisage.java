package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.LivingEntity;

public class PocolocoVisage extends VisageData {
    public PocolocoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new PocolocoVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.POCOLOCO.create(pl.level());
    }

    public String getSkinPath(){
        return "pocoloco";
    }
}
