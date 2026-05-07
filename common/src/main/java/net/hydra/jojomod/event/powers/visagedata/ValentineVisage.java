package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ValentineVisage extends VisageData {
    public ValentineVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new ValentineVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.VALENTINE.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(247,238,106);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.939F, 0.939F, 0.939F);
    }


    public boolean rendersLegCloakPart(){
        return true;
    }
    public String getSkinPath(){
        return "valentine";
    }
}
