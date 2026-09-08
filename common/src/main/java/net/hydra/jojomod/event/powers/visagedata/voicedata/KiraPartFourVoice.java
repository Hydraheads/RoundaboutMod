package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.LivingEntity;

public class KiraPartFourVoice extends VoiceData{
    public KiraPartFourVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(26, ModSounds.DIEGO_HO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(18, ModSounds.DIEGO_HO_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(40, ModSounds.DIEGO_LAUGH_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(70, ModSounds.DIEGO_KONO_DIEGO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(16, ModSounds.DIEGO_INTERESTING_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));

        addVoiceLine(new VoiceLine(35, ModSounds.KIRA4_DEATH_1_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(33, ModSounds.KIRA4_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(23, ModSounds.KIRA4_DEATH_3_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));

        addVoiceLine(new VoiceLine(80, ModSounds.KIRA4_I_BEAT_THEM_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(51, ModSounds.KIRA4_LIVE_HAPPY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(16, ModSounds.DIEGO_KUREI_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(4, ModSounds.DIEGO_ATTACK_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(7, ModSounds.DIEGO_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIEGO_WRY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(13, ModSounds.KIRA4_DAMAGE_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(39, ModSounds.KIRA4_DAMAGE_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_DAMAGE_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_5_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(6, ModSounds.KIRA4_DAMAGE_6_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(36, ModSounds.KIRA4_DAMAGE_7_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));

        addVoiceLine(new VoiceLine(20, ModSounds.DIEGO_THE_WORLD_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(29, ModSounds.DIEGO_THE_WORLD_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(23, ModSounds.DIEGO_THE_WORLD_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(27, ModSounds.DIEGO_THE_WORLD_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }


    public void challenge(){
    }
}

