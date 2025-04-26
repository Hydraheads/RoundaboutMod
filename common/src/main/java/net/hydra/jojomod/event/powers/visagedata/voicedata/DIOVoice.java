package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.player.Player;

public class DIOVoice extends VoiceData{
    public DIOVoice(Player self) {
        super(self);
        addVoiceLine(new VoiceLine(26, ModSounds.DIO_HOHO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
    }
}
