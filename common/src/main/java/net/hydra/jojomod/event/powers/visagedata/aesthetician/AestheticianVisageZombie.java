package net.hydra.jojomod.event.powers.visagedata.aesthetician;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.AyaVisage;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;

public class AestheticianVisageZombie extends AyaVisage {
    public AestheticianVisageZombie(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new AestheticianVisageZombie(entity);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(117,164,99);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.AESTHETICIAN.create(pl.level());
    }
    public String getSkinPath(){
        return "zombie_aesthetician";
    }
}
