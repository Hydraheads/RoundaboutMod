package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SheriffVisage extends VisageData {
    public SheriffVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new SheriffVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.SHERIFF_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.98F,0.98F,0.98F);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(182,184,162);
    }
    @Override
    public Vector3f scaleHead(){
        return new Vector3f(0.85F, 0.94F, 0.85F);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(1F, 0.9F, 1F);
    }
    @Override
    public float getNametagHeight(){
        return 0.5f;
    }
    public String getSkinPath(){
        return "sheriff";
    }
    @Override
    public boolean rendersSheriffHat(){
        return true;
    }

}
