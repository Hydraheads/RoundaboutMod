package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.event.powers.visagedata.voicedata.KiraPartFourVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class KirasakuVisage extends VisageData {
    public KirasakuVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new KirasakuVisage(entity);
    }
    /*@Override
    public JojoNPC getModelNPC(LivingEntity pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }*/

    public boolean hasVoices(){
        return true;
    }
    @Override
    public VoiceData voiceData(LivingEntity self){
        return new KiraPartFourVoice(self);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(23, 24, 30);
    }

    @Override
    public Vector3f scale(){
        return new Vector3f(0.937F, 0.937F, 0.937F);
    }

    public String getSkinPath(){
        return "kirasaku";
    }

}
