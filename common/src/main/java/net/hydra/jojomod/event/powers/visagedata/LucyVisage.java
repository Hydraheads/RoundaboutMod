package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LucyVisage extends VisageData {
    // 180 -> 160
    // 0.9375F ->
    public LucyVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new LucyVisage(entity);
    }    @Override
    public Vec3i getHairColor(){
        return new Vec3i(255,247,198);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.LUCY_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.96F,0.96F,0.96F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.8F, 0.833F, 0.8F);
    }

    @Override
    public String getSkinPath(){
        return "lucy";
    }
    @Override
    public boolean isSlim(){
        return true;
    }
    @Override
    public boolean rendersBreast(){
        return true;
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    @Override
    public boolean rendersLucyHair(){
        return true;
    }
}
