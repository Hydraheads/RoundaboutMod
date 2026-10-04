package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Iterator;
import java.util.List;

public final class AnasuiVoice extends VoiceData {
    public AnasuiVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(28, ModSounds.ANASUI_STAND_SUMMON_1_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(28, ModSounds.ANASUI_STAND_SUMMON_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(24, ModSounds.ANASUI_IDLE_1_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(171, ModSounds.ANASUI_IDLE_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(107, ModSounds.ANASUI_IDLE_3_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(47, ModSounds.ANASUI_IDLE_4_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(71, ModSounds.ANASUI_IDLE_5_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(157, ModSounds.ANASUI_IDLE_6_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(54, ModSounds.ANASUI_IDLE_7_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(17, ModSounds.ANASUI_HURT_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(17, ModSounds.ANASUI_HURT_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(17, ModSounds.ANASUI_HURT_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(36, ModSounds.ANASUI_DEATH_1_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(50, ModSounds.ANASUI_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(59, ModSounds.ANASUI_KILL_1_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(77, ModSounds.ANASUI_KILL_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(47, ModSounds.ANASUI_KILL_3_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(47, ModSounds.ANASUI_KILL_4_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(61, ModSounds.ANASUI_KILL_5_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(49, ModSounds.ANASUI_KILL_6_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        playEquipVoice();
    }

    private void playEquipVoice() {
        playSound(ModSounds.ANASUI_VISAGE_EQUIP_EVENT, 108);
    }

    public void playPhasePunch() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        playSoundAttack(ModSounds.ANASUI_PHASE_PUNCH_EVENT, 17);
    }

    public void playDiverZip() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        double db = Math.random();
        if (db < 0.33) {
            playSoundAttack(ModSounds.ANASUI_ZIP_1_EVENT, 42);
        }else if (db < 0.66) {
            playSoundAttack(ModSounds.ANASUI_ZIP_2_EVENT, 28);
        }else {
            playSoundAttack(ModSounds.ANASUI_ZIP_3_EVENT, 48);
        }
    }

    public void playSubmerge() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        playSoundAttack(ModSounds.ANASUI_SUBMERGE_EVENT, 50);
    }

    public void playTrap() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        playSoundAttack(ModSounds.ANASUI_TRAP_EVENT, 21);
    }

    public void playTrapTrigger() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        double db = Math.random();
        if (db <= 0.50) {
            playSoundAttack(ModSounds.ANASUI_TRAP_TRIGGER_1_EVENT, 34);
        }else {
            playSoundAttack(ModSounds.ANASUI_TRAP_TRIGGER_2_EVENT, 32);
        }
    }

    public void playGroundDive() {
        if (attackCooldown > -1 || inTheMiddleOfTalking()) return;
        double db = Math.random();
        if (db <= 0.50) {
            playSoundAttack(ModSounds.ANASUI_GROUND_DIVE_1_EVENT, 25);
        }else {
            playSoundAttack(ModSounds.ANASUI_GROUND_DIVE_2_EVENT, 21);
        }
    }

    @Override
    public void challenge(){
        if (this.self.tickCount % 11 == 0) {
            AABB aab = this.self.getBoundingBox().inflate(10.0, 8.0, 10.0);
            List<? extends LivingEntity> le = this.self.level().getNearbyEntities(LivingEntity.class,
                    roundabout$attackTargeting, self, aab);
            Iterator var4 = le.iterator();
            while (var4.hasNext()) {
                LivingEntity nle = (LivingEntity) var4.next();
                VoiceData vd = null;
                if (nle instanceof Player pl) {
                    IPlayerEntity ipe = ((IPlayerEntity) pl);
                    vd = ipe.roundabout$getVoiceData();
                }
                if (vd instanceof JotaroVoice jv) {
                    if (!jv.inTheMiddleOfTalking() && !nle.isCrouching() && jv.challengeCooldown <= -1) {
                        double db = Math.random();
                        if (db >= 0.5F) {
                            playSoundChallenge(ModSounds.ANASUI_JOTARO_1_EVENT,90);
                            jv.challengeId(100,4);
                        } else {
                            playSoundChallenge(ModSounds.ANASUI_JOTARO_2_EVENT,99);
                            jv.challengeId(109,5);
                        }
                    }
                }
                if (vd instanceof PucciVoice pv) {
                    if (!pv.inTheMiddleOfTalking() && !nle.isCrouching() && pv.challengeCooldown <= -1) {
                        double db = Math.random();
                        if (db >= 0.5F) {
                            playSoundChallenge(ModSounds.ANASUI_PUCCI_1_EVENT,156);
                            pv.challengeId(166,1);
                        } else {
                            playSoundChallenge(ModSounds.ANASUI_PUCCI_2_EVENT,187);
                            pv.challengeId(197,2);
                        }
                    }
                }
            }
        }
    }
}
