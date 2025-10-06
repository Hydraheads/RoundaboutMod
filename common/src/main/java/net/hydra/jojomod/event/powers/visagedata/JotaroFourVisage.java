package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.JotaroVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class JotaroFourVisage extends VisageData {
    public JotaroFourVisage(LivingEntity self) {
        super(self);
    }

    public boolean hasVoices(){
        return true;
    }
    @Override
    public VoiceData voiceData(LivingEntity slef){
        return new JotaroVoice(slef);
    }


    public VisageData generateVisageData(LivingEntity entity){
        return new JotaroFourVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.JOTARO_4_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.091F,1.091F,1.091F);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.975F, 0.975F, 0.975F);
    }
    public Vector3f scaleHead(){
        return new Vector3f(0.95F, 0.95F, 0.95F);
    }
    public boolean rendersBasicHat(){
        return true;
    }
    public boolean rendersLegCloakPart(){
        return true;
    }
    @Override
    public float getNametagHeight(){
        return 0.54f;
    }
    public String getSkinPath(){
        return "jotaro_4";
    }
}
