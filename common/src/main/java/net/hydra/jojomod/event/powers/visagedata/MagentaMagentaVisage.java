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

public class MagentaMagentaVisage extends VisageData {
    // ~173
    // steve is 185
    // Vector3f(0.9375F, 0.9375F, 0.9375F);
    //0.9375F * (165 / 185.0F) = 0.836
    public MagentaMagentaVisage(LivingEntity self) {
        super(self);
    }
    public VisageData generateVisageData(LivingEntity entity){
        return new MagentaMagentaVisage(entity);
    }

    @Override
    public JojoNPC getModelNPC(LivingEntity pl){
        JojoNPC jojoNPC = ModEntities.JOTARO.create(pl.level());
        if (jojoNPC !=null){
            jojoNPC.setTrueBasis(ModItems.MAGENTA_MAGENTA_MASK.getDefaultInstance());
        }
        return jojoNPC;
    }

    @Override
    public Vec3i getHairColor(){
        return new Vec3i(30,1,14);
    }
    public Vec3 sizeModifier(){
        return new Vec3(0.9375,0.9375,0.9375);
    }
    @Override
    public Vector3f scale(){
        return new Vector3f(0.876F, 0.876F, 0.876F);
    }
    @Override
    public float getNametagHeight(){
        return 0.49f;
    }
    public String getSkinPath(){
        return "magenta_magenta";
    }

    public boolean rendersMagentaCloth() {return true;}
    public boolean rendersMagentaHat() {return true;}
}
