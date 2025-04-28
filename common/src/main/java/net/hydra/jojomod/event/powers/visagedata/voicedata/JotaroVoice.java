package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.player.Player;

public class JotaroVoice extends VoiceData{
    public JotaroVoice(Player self) {
        super(self);
        addVoiceLine(new VoiceLine(16, ModSounds.JOTARO_YARE_YARE_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(18, ModSounds.JOTARO_YARE_YARE_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(24, ModSounds.JOTARO_YARE_YARE_3_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(16, ModSounds.JOTARO_YARE_YARE_4_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(30, ModSounds.JOTARO_YARE_YARE_5_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(17, ModSounds.JOTARO_OI_OI_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(6, ModSounds.JOTARO_GRUNT_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(37, ModSounds.JOTARO_PISSED_OFF_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(19, ModSounds.JOTARO_FINISHER_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(6, ModSounds.JOTARO_ATTACK_1_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.JOTARO_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(14, ModSounds.JOTARO_ATTACK_3_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(18, ModSounds.JOTARO_YARO_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(12, ModSounds.JOTARO_YAMERO_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(42, ModSounds.JOTARO_DEATH_1_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(15, ModSounds.JOTARO_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(4, ModSounds.JOTARO_HURT_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(10, ModSounds.JOTARO_HURT_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(9, ModSounds.JOTARO_HURT_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(5, ModSounds.JOTARO_HURT_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(11, ModSounds.JOTARO_HURT_5_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(18, ModSounds.JOTARO_STAR_PLATINUM_1_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(22, ModSounds.JOTARO_STAR_PLATINUM_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(22, ModSounds.JOTARO_STAR_PLATINUM_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(21, ModSounds.JOTARO_STAR_PLATINUM_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }

    @Override
    public void respondToChallenge(){
        if (challengeNumber == 1){
            playSoundChallenge(ModSounds.JOTARO_DIO_EVENT,10);
        } if (challengeNumber == 2){
            playSoundChallenge(ModSounds.JOTARO_GETING_CLOSER_EVENT,68);
        }
    }
}
