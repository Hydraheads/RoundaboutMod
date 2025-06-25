package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EnyaVisage extends VisageData {
    public EnyaVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new EnyaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.ENYA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.6975F, 0.6975F, 0.6975F);
    }
    public boolean isSlim(){
        return true;
    }
    public boolean rendersBigHair(){
        return true;
    }
    public boolean rendersSmallBreast(){
        return true;
    }
    @Override
    public float getNametagHeight(){
        return -0.2F;
    }

    public String getSkinPath(){
        return "enya";
    }
}
