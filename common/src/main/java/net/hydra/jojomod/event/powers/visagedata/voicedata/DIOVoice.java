package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.player.Player;

public class DIOVoice extends VoiceData{
    public DIOVoice(Player self) {
        super(self);
        addVoiceLine(new VoiceLine(26, ModSounds.DIO_HOHO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(18, ModSounds.DIO_HOHO_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(40, ModSounds.DIO_LAUGH_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(28, ModSounds.DIO_SUBERASHI_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(76, ModSounds.DIO_DEATH_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(40, ModSounds.DIO_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(18, ModSounds.DIO_NO_WAY_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(16, ModSounds.DIO_KUREI_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIO_CHECKMATE_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(4, ModSounds.DIO_ATTACK_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(7, ModSounds.DIO_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIO_WRY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.DIO_HURT_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(7, ModSounds.DIO_HURT_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(8, ModSounds.DIO_HURT_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(11, ModSounds.DIO_HURT_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(10, ModSounds.DIO_NANI_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
    }
}
