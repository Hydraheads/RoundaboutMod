package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class KiraPartFourVisage extends VisageData {
    public KiraPartFourVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new KiraPartFourVisage(entity);
    }
    /*@Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }*/
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(227, 208, 143);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.937F, 0.937F, 0.937F);
    }

    public String getSkinPath(){
        return "kira_part_four";
    }

}
