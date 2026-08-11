package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public final class PucciVoice extends VoiceData {
    public PucciVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(28, ModSounds.PUCCI_STAND_SUMMON_1_EVENT,
                VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(28, ModSounds.PUCCI_STAND_SUMMON_2_EVENT,
                VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(18, ModSounds.PUCCI_HURT_1_EVENT,
                VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(18, ModSounds.PUCCI_HURT_2_EVENT,
                VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(18, ModSounds.PUCCI_HURT_3_EVENT,
                VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(18, ModSounds.PUCCI_HURT_4_EVENT,
                VoiceLine.SOUND_CATEGORIES.HURT));
        playEquipVoice();
    }

    private void playEquipVoice() {
        SoundEvent sound = switch (self.getRandom().nextInt(3)) {
            case 1 -> ModSounds.PUCCI_VISAGE_EQUIP_2_EVENT;
            case 2 -> ModSounds.PUCCI_VISAGE_EQUIP_3_EVENT;
            default -> ModSounds.PUCCI_VISAGE_EQUIP_1_EVENT;
        };
        playSound(sound, 30);
    }
}
