package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GuccioVisage extends VisageData {
    public GuccioVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new GuccioVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.GUCCIO.create(pl.level());
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(38,23,17);
    }
    @Override
    public boolean isSlim(){
        return true;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.87F,0.87F,0.87F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.8091F, 0.8091F, 0.8091F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "guccio";
    }
}
