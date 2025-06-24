package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class AyaVisage extends VisageData {
    public AyaVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new AyaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.AYA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.95F,0.95F,0.95F);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.8F, 0.839F, 0.8F);
    }
    @Override
    public float getNametagHeight(){
        return 0.25f;
    }
    public String getSkinPath(){
        return "aya";
    }
    public boolean isSlim(){
        return true;
    }
    public boolean rendersBreast(){
        return true;
    }
}
