package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ShizukaVisage extends VisageData {
    public ShizukaVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new ShizukaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.SHIZUKA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.96F,0.96F,0.96F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.89F, 0.89F, 0.89F);
    }
    @Override
    public float getNametagHeight(){
        return 0.2f;
    }
    public String getSkinPath(){
        return "shizuka";
    }
    public boolean isSlim(){
        return true;
    }
    public boolean isJojovein(){
        return true;
    }
    public boolean rendersSmallBreast(){
        return true;
    }
}
