package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DoppioVisage extends VisageData {
    // 170
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (170.0F / 185.0F) = 0.861F
    public DoppioVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(22,22,22);
    }

    public VisageData generateVisageData(LivingEntity entity){
        return new DoppioVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.DOPPIO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.861F, 0.861F, 0.861F);
    }
    public Vector3f scaleHead(){
        return new Vector3f(1F, 1F, 1F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "doppio";
    }
}
