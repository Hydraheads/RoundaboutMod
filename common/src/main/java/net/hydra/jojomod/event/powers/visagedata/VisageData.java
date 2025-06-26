package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class VisageData {

    public final Player self;
    public VisageData(Player self) {
        this.self = self;
    }
    public VisageData generateVisageData(Player entity){
        return new VisageData(entity);
    }
    public JojoNPC getModelNPC(Player pl){
        return null;
    }
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }
    public Vector3f scale(){
        return new Vector3f(0.9375F, 0.9375F, 0.9375F);
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

    public VoiceData voiceData(Player realSelf){
        return new VoiceData(realSelf);
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
    public boolean rendersDiegoHat(){
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

}
