package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class JosukePartEightVisage extends VisageData {
    public JosukePartEightVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new JosukePartEightVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(130,74,130);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.937F, 0.937F, 0.937F);
    }
    public boolean isSlim(){
        return true;
    }
    public String getSkinPath(){
        return "josuke_part_eight";
    }

    public boolean rendersJosukeDecals(){
        return true;
    }
    public boolean rendersTasselHat(){
        return true;
    }
}
