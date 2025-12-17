package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class KakyoinVisage extends VisageData {
    public KakyoinVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new KakyoinVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.VALENTINE.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.956F,0.956F,0.956F);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(187,115,95);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.898F, 0.898F, 0.898F);
    }

    @Override
    public boolean rendersKakyoinHair(){
        return true;
    }

    public String getSkinPath(){
        return "kakyoin";
    }
}
