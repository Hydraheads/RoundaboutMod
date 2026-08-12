package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.visagedata.voicedata.PucciVoice;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.LivingEntity;

public final class PucciMangaVisage extends VisageData {
    public PucciMangaVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public VisageData generateVisageData(LivingEntity entity) {
        return new PucciMangaVisage(entity);
    }

    @Override
    public JojoNPC getModelNPC(LivingEntity entity) {
        JojoNPC npc = ModEntities.PUCCI.create(entity.level());
        if (npc != null) {
            npc.setTrueBasis(ModItems.PUCCI_MANGA_MASK.getDefaultInstance());
        }
        return npc;
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
        return "pucci_manga";
    }
}
