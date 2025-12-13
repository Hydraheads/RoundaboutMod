package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;

public class PocolocoVisage extends VisageData {
    public PocolocoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new PocolocoVisage(entity);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(12,12,12);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.POCOLOCO.create(pl.level());
    }

    public String getSkinPath(){
        return "pocoloco";
    }
}
