package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GyroVisage extends VisageData {
    // ~173
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (173.0F / 185.0F) = 0.876F
    public GyroVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new GyroVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.GYRO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.9375,0.9375,0.9375);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(255,222,143);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.876F, 0.876F, 0.876F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "gyro";
    }
    public boolean isSlim(){
        return false;
    }
    @Override
    public boolean rendersGyroHat(){
        return true;
    }
    @Override
    public boolean rendersSteelBalls(){
        return true;
    }
    @Override
    public boolean rendersCape(){
        return true;
    }
}
