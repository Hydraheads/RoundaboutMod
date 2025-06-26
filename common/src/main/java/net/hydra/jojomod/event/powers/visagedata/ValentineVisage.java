package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ValentineVisage extends VisageData {
    public ValentineVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new ValentineVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.VALENTINE.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
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
