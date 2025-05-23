package net.hydra.jojomod.event.powers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class VoiceLine {

    /**
     * Approximately how long the voiceline is in ticks, overshooting is better than undershooting.
     */

    public int lengthInTicks;

    /**
     * The corresponding sound event.
     */
    public SoundEvent soundEvent;


    /**
     * Category makes registration easier.
     */
    public SOUND_CATEGORIES soundCategory;

    public enum SOUND_CATEGORIES {
        IDLE,
        ATTACK,
        ABILITY,
        HURT,
        DEATH,
        KILL,
        SUMMON,
        INTERACTION,
        COMMUNICATION
    }

    public VoiceLine(int lengthInTicks, SoundEvent soundEvent, SOUND_CATEGORIES soundCategory) {
        this.lengthInTicks = lengthInTicks;
        this.soundEvent = soundEvent;
        this.soundCategory = soundCategory;
    }
}