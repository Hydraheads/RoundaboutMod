package net.hydra.jojomod.event.powers.visagedata.aesthetician;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.AyaVisage;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.world.entity.LivingEntity;

public class AestheticianVisage1 extends AyaVisage {
    public AestheticianVisage1(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new AestheticianVisage1(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.AESTHETICIAN.create(pl.level());
    }
    public String getSkinPath(){
        return "aesthetician";
    }
}
