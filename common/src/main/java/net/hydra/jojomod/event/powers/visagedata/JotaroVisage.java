package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DIOVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.JotaroVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class JotaroVisage extends VisageData {
    public JotaroVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new JotaroVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOTARO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.09F,1.09F,1.09F);
    }

    public Vector3f scaleHead(){
        return new Vector3f(0.95F, 0.95F, 0.95F);
    }
    @Override
    public float getNametagHeight(){
        return 0.54f;
    }

    public boolean hasVoices(){
        return true;
    }
    public boolean rendersBasicHat(){
        return true;
    }
    public String getSkinPath(){
        return "jotaro";
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.975F, 0.975F, 0.975F);
    }
    @Override
    public VoiceData voiceData(LivingEntity slef){
        return new JotaroVoice(slef);
    }
}
