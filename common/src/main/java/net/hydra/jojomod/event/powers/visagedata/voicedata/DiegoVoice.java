package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.LivingEntity;

public class DiegoVoice  extends VoiceData{
    public DiegoVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(26, ModSounds.DIEGO_HO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(18, ModSounds.DIEGO_HO_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(40, ModSounds.DIEGO_LAUGH_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(70, ModSounds.DIEGO_KONO_DIEGO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(16, ModSounds.DIEGO_INTERESTING_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(76, ModSounds.DIEGO_DEATH_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(40, ModSounds.DIEGO_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(18, ModSounds.DIEGO_NO_WAY_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(16, ModSounds.DIEGO_KUREI_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIEGO_CHECKMATE_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(4, ModSounds.DIEGO_ATTACK_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(7, ModSounds.DIEGO_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIEGO_WRY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(28, ModSounds.DIEGO_TAUNT_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.DIEGO_HURT_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(7, ModSounds.DIEGO_HURT_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(8, ModSounds.DIEGO_HURT_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(11, ModSounds.DIEGO_HURT_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(10, ModSounds.DIEGO_NANI_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(20, ModSounds.DIEGO_THE_WORLD_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(29, ModSounds.DIEGO_THE_WORLD_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(23, ModSounds.DIEGO_THE_WORLD_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(27, ModSounds.DIEGO_THE_WORLD_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }


    public void challenge(){
    }
}

