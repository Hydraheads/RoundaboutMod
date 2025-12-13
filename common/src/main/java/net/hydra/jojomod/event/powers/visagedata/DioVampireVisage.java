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

public class DioVampireVisage extends VisageData {
    public DioVampireVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new DioVampireVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.DIO_VAMPIRE_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public Vec3i getHairColor(){
        return new Vec3i(245,225,97);
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1,1,1);
    }

    public boolean rendersSpikeyHair(){
        return true;
    }
    public boolean hasVoices(){
        return true;
    }
    @Override
    public VoiceData voiceData(LivingEntity slef){
        return new DIOVoice(slef);
    }

    public String getSkinPath(){
        return "dio_vampire";
    }
}
