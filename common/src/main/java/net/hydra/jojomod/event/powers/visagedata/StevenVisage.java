package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class StevenVisage extends VisageData {
    // 190
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (190.0F / 185.0F) = 0.962F
    public StevenVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(240,218,118);
    }

    public VisageData generateVisageData(LivingEntity entity){
        return new StevenVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.STEVEN_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }

    @Override
    public boolean isSlim(){
        return false;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.04F,1.04F,1.04F);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.962F, 0.962F, 0.962F);
    }
    public Vector3f scaleHead(){
        return new Vector3f(0.975F, 0.975F, 0.975F);
    }
    @Override
    public float getNametagHeight(){
        return 0.53f;
    }
    public String getSkinPath(){
        return "steven";
    }
}
