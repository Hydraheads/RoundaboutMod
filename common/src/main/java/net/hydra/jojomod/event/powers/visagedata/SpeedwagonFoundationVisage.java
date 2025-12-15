package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SpeedwagonFoundationVisage extends VisageData {
    public SpeedwagonFoundationVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new SpeedwagonFoundationVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.SPEEDWAGON_FOUNDATION_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public boolean rendersSpeedwagonFoundationHat(){
        return true;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(66,48,36);
    }
    public String getSkinPath(){
        return "speedwagon_foundation";
    }
}
