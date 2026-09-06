package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class HayatoVisage extends VisageData {
    public HayatoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new HayatoVisage(entity);
    }
    /*@Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.ENYA.create(pl.level());
    }*/
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(195,126,92);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.695F, 0.695F, 0.695F);
    }
    public boolean isSlim(){
        return true;
    }

    public boolean rendersSchoolHat(){
        return true;
    }

    public boolean rendersBackpack() {
        return true;
    }

    @Override
    public float getNametagHeight(){
        return -0.2F;
    }

    public String getSkinPath(){
        return "hayato_kawajiri";
    }
}
