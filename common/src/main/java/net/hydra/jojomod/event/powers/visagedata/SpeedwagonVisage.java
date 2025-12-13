package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SpeedwagonVisage extends VisageData {
    public SpeedwagonVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new SpeedwagonVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.SPEEDWAGON_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(191,190,109);
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.962F,0.962F,0.962F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.9F, 0.9F, 0.9F);
    }
    @Override
    public float getNametagHeight(){
        return 0.2f;
    }
    public String getSkinPath(){
        return "speedwagon";
    }
}
