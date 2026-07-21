package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GhiaccioVisage extends VisageData {
    // 171 canon
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (171.0F / 185.0F) = 0.8665541F
    public GhiaccioVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new GhiaccioVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.GHIACCIO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.9375,0.9375,0.9375);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(123,196,220);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.866F, 0.866F, 0.866F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "ghiaccio";
    }

}
