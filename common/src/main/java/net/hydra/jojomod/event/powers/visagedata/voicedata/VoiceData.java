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
    public int idleCooldown = -1;
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
        idleCooldown = ticksLasting+600;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSound2(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        this.self.level().playSound(null, this.self.getX(),this.self.getY(),this.self.getZ(), se, this.self.getSoundSource(), 2F, 1F);
    }

    public void playOnTick(){
        if (inTheMiddleOfTalking()){
            talkingTicks--;
        }
        if (idleCooldown > -1){
            idleCooldown --;
        }

        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideTick();
            if (!tickLines.isEmpty() && idleCooldown <= -1) {
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
            safeInit();
            overrideHurt();
            if (!hurtLines.isEmpty()) {
                VoiceLine vl = getRandomElement(hurtLines);
                if (vl != null){
                    playSound(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideHurt(){
    }
    public void playIfDying(DamageSource $$0){
        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideDying();
            if (!deathLines.isEmpty()) {
                VoiceLine vl = getRandomElement(deathLines);
                if (vl != null){
                    playSound2(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideDying(){
    }
}
