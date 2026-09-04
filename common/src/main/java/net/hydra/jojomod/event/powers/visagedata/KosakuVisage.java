package net.hydra.jojomod.event.powers.visagedata;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class KosakuVisage extends VisageData {
    public KosakuVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new KosakuVisage(entity);
    }
    /*@Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }*/
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(23, 24, 30);
    }

    @Override
    public boolean rendersKosakuHair(){
        return true;
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.937F, 0.937F, 0.937F);
    }

    public String getSkinPath(){
        return "kosaku";
    }

}
