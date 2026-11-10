package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class CioccolataVisage extends VisageData {
    // 171 canon
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (171.0F / 185.0F) = 0.8665541F
    public CioccolataVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new CioccolataVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.CIOCCOLATA_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.05,1.05,1.05);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(111,240,111);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(1.02F, 1.02F, 1.02F);
    }
    @Override
    public float getNametagHeight(){
        return 0.5f;
    }
    public String getSkinPath(){
        return "cioccolata";
    }
    @Override
    public boolean rendersCioccolataHair(){
        return true;
    }

}
