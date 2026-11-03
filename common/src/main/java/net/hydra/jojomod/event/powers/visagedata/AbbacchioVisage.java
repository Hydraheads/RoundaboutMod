package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class AbbacchioVisage extends VisageData {
    // ~188
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (188.0F / 185.0F) = 1.016F

    @Override
    public Vector3f scale(){
        return new Vector3f(1.016F, 1.016F, 1.016F);
    }
    @Override
    public float getNametagHeight(){
        return 0.55f;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.9375,0.9375,0.9375);
    }

    public AbbacchioVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new AbbacchioVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.ABBACCHIO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(255,255,255);
    }

    public String getSkinPath(){
        return "abbacchio";
    }
}
