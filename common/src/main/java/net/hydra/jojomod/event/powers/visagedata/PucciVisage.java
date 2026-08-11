package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.event.powers.visagedata.voicedata.PucciVoice;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.world.entity.LivingEntity;

public final class PucciVisage extends VisageData {
    public PucciVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public VisageData generateVisageData(LivingEntity entity) {
        return new PucciVisage(entity);
    }

    @Override
    public JojoNPC getModelNPC(LivingEntity entity) {
        return ModEntities.PUCCI.create(entity.level());
    }

    @Override
    public boolean hasVoices() {
        return true;
    }

    @Override
    public VoiceData voiceData(LivingEntity entity) {
        return new PucciVoice(entity);
    }

    @Override
    public String getSkinPath() {
        return "pucci";
    }
}
