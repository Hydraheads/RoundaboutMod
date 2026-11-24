package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PolpoVisage extends VisageData{
    public PolpoVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new PolpoVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.POLPO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1F,1F,1F);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(65,52,44);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(1.47F, 1.05F, 1.47F);
    }
    @Override
    public float getNametagHeight(){
        return 0.95f;
    }
    public boolean renderPolpoNose(){return true;}
    public String getSkinPath(){
        return "polpo";
    }
}
