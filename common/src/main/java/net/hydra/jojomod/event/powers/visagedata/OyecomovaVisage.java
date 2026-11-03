package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;

public class OyecomovaVisage extends VisageData {
    public OyecomovaVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new OyecomovaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.OYECOMOVA_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(78,49,39);
    }

    public String getSkinPath(){
        return "oyecomova";
    }
    public boolean rendersOyecomovaHat() {return true;}
}
