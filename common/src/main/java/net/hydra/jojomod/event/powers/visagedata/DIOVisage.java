package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DIOVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DIOVisage extends VisageData {
    public DIOVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new DIOVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.DIO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.09F,1.09F,1.09F);
    }

    public boolean rendersSpikeyHair(){
        return true;
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
    public boolean hasVoices(){
        return true;
    }
    @Override
    public VoiceData voiceData(LivingEntity slef){
        return new DIOVoice(slef);
    }

    public String getSkinPath(){
        return "dio";
    }
}
