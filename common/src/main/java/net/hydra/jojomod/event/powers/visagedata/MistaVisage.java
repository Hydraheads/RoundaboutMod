package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;

public class MistaVisage extends VisageData {
    public MistaVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new MistaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.MISTA_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(34,33,32);
    }

    public String getSkinPath(){
        return "mista";
    }
}
