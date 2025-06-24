package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DiegoVisage extends VisageData {
    public DiegoVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new DiegoVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.PARALLEL_DIEGO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.98F,0.98F,0.98F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.915F, 0.915F, 0.915F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "diego";
    }
}
