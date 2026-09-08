package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.LivingEntity;

public class KiraPartFourVoice extends VoiceData{
    public KiraPartFourVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(142, ModSounds.KIRA4_IDLE_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(96, ModSounds.KIRA4_THREAT_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));

        addVoiceLine(new VoiceLine(35, ModSounds.KIRA4_DEATH_1_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(33, ModSounds.KIRA4_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(23, ModSounds.KIRA4_DEATH_3_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));

        addVoiceLine(new VoiceLine(80, ModSounds.KIRA4_I_BEAT_THEM_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(51, ModSounds.KIRA4_LIVE_HAPPY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(13, ModSounds.KIRA4_SHIBO_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(6, ModSounds.KIRA4_ATTACK_1_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(12, ModSounds.KIRA4_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(5, ModSounds.KIRA4_ATTACK_3_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.KIRA4_ATTACK_4_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(13, ModSounds.KIRA4_DAMAGE_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(39, ModSounds.KIRA4_DAMAGE_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_DAMAGE_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_5_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(6, ModSounds.KIRA4_DAMAGE_6_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(36, ModSounds.KIRA4_DAMAGE_7_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));

        addVoiceLine(new VoiceLine(24, ModSounds.KIRA4_KILLER_QUEEN_1_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(15, ModSounds.KIRA4_KILLER_QUEEN_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_KILLER_QUEEN_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_KILLER_QUEEN_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }


    public void challenge(){

    }
}

