package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class HatoVisage extends VisageData {
    public HatoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new HatoVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.HATO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.98F,0.98F,0.98F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.914F, 0.914F, 0.914F);
    }

    public String getSkinPath(){
        return "hato";
    }
    public boolean isSlim(){
        return true;
    }
    public boolean rendersBreast(){
        return true;
    }
}
