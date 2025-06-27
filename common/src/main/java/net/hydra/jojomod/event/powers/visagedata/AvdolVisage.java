package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class AvdolVisage extends VisageData {
    public AvdolVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new AvdolVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.AVDOL.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0675F,1.0675F,1.0675F);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.969F, 0.96F, 0.96F);
    }

    public Vector3f scaleHead(){
        return new Vector3f(0.95F, 0.95F, 0.95F);
    }

    public boolean rendersLegCloakPart(){
        return true;
    }
    public boolean rendersAvdolHairPart(){
        return true;
    }
    public boolean rendersPonytail(){
        return true;
    }

    @Override
    public float getNametagHeight(){
        return 0.54f;
    }
    public String getSkinPath(){
        return "avdol";
    }
}
