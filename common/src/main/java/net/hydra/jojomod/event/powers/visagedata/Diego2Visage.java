package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DiegoVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class Diego2Visage extends VisageData {
    public Diego2Visage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new Diego2Visage(entity);
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.PARALLEL_DIEGO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.DIEGO_2_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }
    @Override
    public boolean rendersDiegoLegs(){
        return true;
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.98F,0.98F,0.98F);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(255,165,91);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.915F, 0.915F, 0.915F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "diego_2";
    }

    public boolean rendersDiego2Hat(){
        return true;
    }
    public boolean hasVoices(){
        return true;
    }

    @Override
    public VoiceData voiceData(LivingEntity slef){
        return new DiegoVoice(slef);
    }
}
