package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class VisageData {

    public final LivingEntity self;
    public VisageData(LivingEntity self) {
        this.self = self;
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new VisageData(entity);
    }
    public JojoNPC getModelNPC(LivingEntity pl){
        return null;
    }
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }
    public Vector3f scale(){
        return new Vector3f(0.9375F, 0.9375F, 0.9375F);
    }
    public Vector3f scaleHead(){
        return new Vector3f(1F, 1F, 1F);
    }
    public float getNametagHeight(){
        return 0.5f;
    }
    public boolean isCharacterVisage(){
        return true;
    }
    public boolean hasVoices(){
        return false;
    }
    public boolean isJojovein(){
        return false;
    }

    public VoiceData voiceData(LivingEntity realSelf){
        return new VoiceData(realSelf);
    }
    public Vec3i getHairColor(){
        return new Vec3i(1,1,1);
    }

    public String skinPathName;

    public String getSkinPath(){
        return null;
    }
    public boolean isSlim(){
        return false;
    }

    public boolean rendersSpikeyHair(){
        return false;
    }
    public boolean rendersBreast(){
        return false;
    }
    public boolean rendersPonytail(){
        return false;
    }
    public boolean rendersBigHair(){
        return false;
    }
    public boolean rendersKakyoinHair(){
        return false;
    }
    public boolean rendersJosukeDecals(){
        return false;
    }
    public boolean rendersTasselHat(){
        return false;
    }
    public boolean rendersLegCloakPart(){
        return false;
    }
    public boolean rendersAvdolHairPart(){
        return false;
    }
    public boolean rendersPlayerBreastPart(){
        return false;
    }
    public boolean rendersPlayerSmallBreastPart(){
        return false;
    }
    public boolean rendersDiegoHat(){
        return false;
    }
    public boolean rendersSpeedwagonFoundationHat(){
        return false;
    }
    public boolean rendersBasicHat(){
        return false;
    }
    public boolean rendersSmallBreast(){
        return false;
    }
    public boolean rendersPlayerBreast(){
        return false;
    }
    public boolean rendersArmor(){
        return false;
    }

}
