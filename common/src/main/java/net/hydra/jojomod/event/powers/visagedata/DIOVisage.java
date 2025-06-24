package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DIOVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DIOVisage extends VisageData {
    public DIOVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new DIOVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.DIO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.09F,1.09F,1.09F);
    }
    @Override
    public float getNametagHeight(){
        return 0.54f;
    }
    public boolean hasVoices(){
        return true;
    }
    @Override
    public VoiceData voiceData(Player slef){
        return new DIOVoice(slef);
    }

    public String getSkinPath(){
        return "dio";
    }
}
