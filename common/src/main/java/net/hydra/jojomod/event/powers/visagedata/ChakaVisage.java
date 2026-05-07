package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ChakaVisage extends VisageData {
    public ChakaVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new ChakaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.CHAKA.create(pl.level());
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(1,11,7);
    }


    @Override
    public float getNametagHeight(){
        return 0.54f;
    }
    public String getSkinPath(){
        return "chaka";
    }
}
