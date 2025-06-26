package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EnyaOVAVisage extends VisageData {
    public EnyaOVAVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new EnyaOVAVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.OVA_ENYA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.95F,0.95F,0.95F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.84F, 0.87F, 0.84F);
    }
    public String getSkinPath(){
        return "enya_ova";
    }
    public boolean isSlim(){
        return true;
    }
    public boolean rendersBreast(){
        return true;
    }
}
