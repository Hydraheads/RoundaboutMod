package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DotHanVisage extends VisageData {
    public DotHanVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new DotHanVisage(entity);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(12,12,12);
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.91F,0.91F,0.91F);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.DOT_HAN_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public boolean rendersDotHanHair(){
        return true;
    }
    public String getSkinPath(){
        return "dot_han";
    }
}
