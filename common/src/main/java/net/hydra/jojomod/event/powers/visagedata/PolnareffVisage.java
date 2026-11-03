package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DIOVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PolnareffVisage extends VisageData {
    public PolnareffVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new PolnareffVisage(entity);
    }

    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.POLNAREFF_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }

    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.09F,1.09F,1.09F);
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(222,249,255);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.975F, 0.975F, 0.975F);
    }
    @Override
    public float getNametagHeight(){
        return 0.54f;
    }

    public Vector3f scaleHead(){
        return new Vector3f(0.95F, 0.95F, 0.95F);
    }

    public String getSkinPath(){
        return "polnareff";
    }

    public boolean rendersExtraHair() {return true;}
    public boolean rendersExtraHairDetails() {return true;}
    public boolean rendersPolnareffDecals() { return true; }
}
