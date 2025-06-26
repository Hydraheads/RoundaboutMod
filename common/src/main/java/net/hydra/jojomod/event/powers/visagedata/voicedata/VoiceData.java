package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.event.powers.VoiceLine;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoiceData {

    public final LivingEntity self;

    public int talkingTicks = -1;
    public int idleCooldown = -1;
    public int killCooldown = -1;
    public int hurtCooldown = -1;
    public int attackCooldown = -1;
    public int summonCooldown = -1;

    public int challengeCooldown = -1;
    public int challengeNumber = -1;
    public List<VoiceLine> tickLines = new ArrayList<>();
    public List<VoiceLine> hurtLines = new ArrayList<>();
    public List<VoiceLine> deathLines = new ArrayList<>();
    public List<VoiceLine> killLines = new ArrayList<>();
    public List<VoiceLine> attackLines = new ArrayList<>();
    public List<VoiceLine> summonLines = new ArrayList<>();
    public VoiceData(LivingEntity self) {
        this.self = self;
    }

    public boolean inTheMiddleOfTalking(){
        return this.talkingTicks > -1;
    }

    public void safeInit(){
        if (tickLines == null) {tickLines = new ArrayList<>();}
        if (hurtLines == null) {hurtLines = new ArrayList<>();}
        if (deathLines == null) {deathLines = new ArrayList<>();}
        if (killLines == null) {killLines = new ArrayList<>();}
        if (attackLines == null) {attackLines = new ArrayList<>();}
        if (summonLines == null) {summonLines = new ArrayList<>();}
    }
    public void addVoiceLine(VoiceLine vl){
        safeInit();
        switch (vl.soundCategory){
            case IDLE -> tickLines.add(vl);
            case HURT -> hurtLines.add(vl);
            case DEATH -> deathLines.add(vl);
            case KILL -> killLines.add(vl);
            case ATTACK -> attackLines.add(vl);
            case SUMMON -> summonLines.add(vl);
        }
    }
    public void forceTalkingTicks(int ticksLasting){
        talkingTicks = ticksLasting;

    }

    public void playSoundIfPossible(SoundEvent se, int ticksLasting, float pitch, float volume){
        if (!inTheMiddleOfTalking()) {
            talkingTicks = ticksLasting;
            this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), volume, pitch);
        }
    }
    public void playSound(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        idleCooldown = ticksLasting+6000;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSound2(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        this.self.level().playSound(null, this.self.getX(),this.self.getY(),this.self.getZ(), se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSoundKill(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        killCooldown = ticksLasting+600;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSoundSummon(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        summonCooldown = ticksLasting+200;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSoundAttack(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        attackCooldown = ticksLasting+600;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSoundHurt(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        hurtCooldown = ticksLasting+100;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }
    public void playSoundChallenge(SoundEvent se, int ticksLasting){
        talkingTicks = ticksLasting;
        challengeCooldown = ticksLasting+2400;
        this.self.level().playSound(null, this.self, se, this.self.getSoundSource(), 2F, 1F);
    }

    public void challengeId(int tickCount, int challengeID){
        challengeNumber = challengeID;
        talkingTicks = tickCount;
    }
    public void respondToChallenge(){

    }
    public void playOnTick(){
        if (inTheMiddleOfTalking()){
            talkingTicks--;
            if (talkingTicks <= -1 && challengeNumber != -1){
                respondToChallenge();
                challengeNumber = -1;
            }
        }
        if (idleCooldown > -1){
            idleCooldown --;
        }
        if (killCooldown > -1){
            killCooldown --;
        }
        if (hurtCooldown > -1){
            hurtCooldown --;
        }
        if (attackCooldown > -1){
            attackCooldown --;
        }
        if (summonCooldown > -1){
            summonCooldown --;
        }
        if (challengeCooldown > -1){
            challengeCooldown --;
        }

        if (!inTheMiddleOfTalking() && !self.isCrouching()){
            safeInit();
            if (challengeCooldown <= -1){
                challenge();
            }

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
            overrideHurt($$0);
            if (!hurtLines.isEmpty() && hurtCooldown <= -1) {
                VoiceLine vl = getRandomElement(hurtLines);
                if (vl != null){
                    playSoundHurt(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideHurt(DamageSource $$0){
    }
    public void playIfDying(DamageSource $$0){
        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideDying($$0);
            if (!deathLines.isEmpty()) {
                VoiceLine vl = getRandomElement(deathLines);
                if (vl != null){
                    playSound2(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideDying(DamageSource $$0){
    }
    public void playIfKilled(LivingEntity victim){
        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideKilling(victim);
            if (!killLines.isEmpty() && killCooldown <= -1) {
                VoiceLine vl = getRandomElement(killLines);
                if (vl != null){
                    playSoundKill(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideKilling(LivingEntity victim){
    }
    public void playIfAttacking(Entity victim){
        if (!inTheMiddleOfTalking()){
            safeInit();
            overrideAttacking(victim);
            if (!attackLines.isEmpty() && attackCooldown <= -1) {
                VoiceLine vl = getRandomElement(attackLines);
                if (vl != null){
                    playSoundAttack(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void overrideAttacking(Entity victim){
    }

    public void playSummon(){
        if (!inTheMiddleOfTalking()){
            safeInit();
            playSummonOverride();
            if (!summonLines.isEmpty() && summonCooldown <= -1) {
                VoiceLine vl = getRandomElement(summonLines);
                if (vl != null){
                    playSoundSummon(vl.soundEvent,vl.lengthInTicks);
                }
            }
        }
    }
    public void playSummonOverride(){
    }

    public void challenge(){

    }
    public static final TargetingConditions roundabout$attackTargeting = TargetingConditions.forNonCombat().range(64.0);
}
