package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoiceData {

    public final Player self;

    public int talkingTicks = -1;
    public int timeSinceLastIdle = -1;
    public List<VoiceLine> tickLines = new ArrayList<>();
    public List<VoiceLine> hurtLines = new ArrayList<>();
    public List<VoiceLine> deathLines = new ArrayList<>();
    public VoiceData(Player self) {
        this.self = self;
    }

    public boolean inTheMiddleOfTalking(){
        return this.talkingTicks > -1;
    }

    public void safeInit(){
        if (tickLines == null) {tickLines = new ArrayList<>();}
        if (hurtLines == null) {hurtLines = new ArrayList<>();}
        if (deathLines == null) {deathLines = new ArrayList<>();}
    }
    public void addVoiceLine(VoiceLine vl){
        safeInit();
        switch (vl.soundCategory){
            case IDLE -> tickLines.add(vl);
            case HURT -> hurtLines.add(vl);
            case DEATH -> deathLines.add(vl);
        }
    }

    public void playSound(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }

    public void playOnTick(){
        if (inTheMiddleOfTalking()){
            talkingTicks--;
        }

        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideTick();
            if (!tickLines.isEmpty()) {
                VoiceLine vl = getRandomElement(tickLines);
                if (vl != null){
                    playSound(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null; // Return null for empty or null list
        }
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
    public void overrideTick(){
    }
    public void playIfHurt(DamageSource $$0){
        if (!inTheMiddleOfTalking()){
            overrideHurt();
        }
    }
    public void overrideHurt(){
    }
    public void playIfDying(DamageSource $$0){
        if (!inTheMiddleOfTalking()){
            overrideDying();
        }
    }
    public void overrideDying(){
    }
}
