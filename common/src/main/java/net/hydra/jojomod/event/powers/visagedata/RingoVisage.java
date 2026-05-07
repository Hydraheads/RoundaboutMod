package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RingoVisage extends VisageData {
    public RingoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new RingoVisage(entity);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(222,220,217);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.RINGO.create(pl.level());
    }

    public String getSkinPath(){
        return "ringo";
    }
}
