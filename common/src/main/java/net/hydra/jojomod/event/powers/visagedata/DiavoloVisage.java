package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.visagedata.voicedata.JotaroVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DiavoloVisage extends VisageData {
    // 191
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (191.0F / 185.0F) = 0.967F
    public DiavoloVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(22,22,22);
    }

    public VisageData generateVisageData(LivingEntity entity){
        return new DiavoloVisage(entity);
    }
    public boolean swapName(){
        return true;
    }
    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.DIAVOLO_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }

    @Override
    public boolean isSlim(){
        if (isDisguised()){
            return true;
        }
        return false;
    }
    public boolean isDisguised(){
        if (PowerTypes.hasHandsActive(self)){
            return true;
        }
        return false;
    }
    @Override
    public Vec3 sizeModifier(){
        if (isDisguised()){
            return new Vec3(1.0F,1.0F,1.0F);
        }
        return new Vec3(1.04F,1.04F,1.04F);
    }
    @Override
    public Vector3f scale(){
        if (isDisguised()){
            return new Vector3f(0.861F, 0.861F, 0.861F);
        }
        return new Vector3f(0.967F, 0.967F, 0.967F);
    }
    public Vector3f scaleHead(){
        if (isDisguised()){
            return new Vector3f(1F, 1F, 1F);
        }
        return new Vector3f(0.96F, 0.96F, 0.96F);
    }
    @Override
    public float getNametagHeight(){
        if (isDisguised()){
            return 0.49F;
        }
        return 0.53f;
    }
    public String getSkinPath(){
        if (isDisguised()){
            return "doppio";
        }
        return "diavolo";
    }
}
