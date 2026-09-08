package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.event.powers.visagedata.HayatoVisage;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Iterator;
import java.util.List;

public class KiraPartFourVoice extends VoiceData{
    public KiraPartFourVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(142, ModSounds.KIRA4_IDLE_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(96, ModSounds.KIRA4_THREAT_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));

        addVoiceLine(new VoiceLine(35, ModSounds.KIRA4_DEATH_1_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(33, ModSounds.KIRA4_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(23, ModSounds.KIRA4_DEATH_3_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));

        addVoiceLine(new VoiceLine(80, ModSounds.KIRA4_I_BEAT_THEM_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(51, ModSounds.KIRA4_LIVE_HAPPY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(13, ModSounds.KIRA4_SHIBO_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(6, ModSounds.KIRA4_ATTACK_1_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(12, ModSounds.KIRA4_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(5, ModSounds.KIRA4_ATTACK_3_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.KIRA4_ATTACK_4_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));

        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(13, ModSounds.KIRA4_DAMAGE_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(39, ModSounds.KIRA4_DAMAGE_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_DAMAGE_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(14, ModSounds.KIRA4_DAMAGE_5_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(6, ModSounds.KIRA4_DAMAGE_6_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(36, ModSounds.KIRA4_DAMAGE_7_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));

        addVoiceLine(new VoiceLine(24, ModSounds.KIRA4_KILLER_QUEEN_1_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(15, ModSounds.KIRA4_KILLER_QUEEN_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_KILLER_QUEEN_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(20, ModSounds.KIRA4_KILLER_QUEEN_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }

    @Override
    public void playSoundChallenge(SoundEvent se, int ticksLasting){
        super.playSoundChallenge(se, ticksLasting);
        lastTarget = -1;
        staringTicks = 0;
    }

    int staringTicks = 0;
    int lastTarget = -1;


    public void challenge(){
        if (this.self.tickCount % 11 == 0) {
            AABB aab = this.self.getBoundingBox().inflate(10.0, 8.0, 10.0);
            List<? extends LivingEntity> le = this.self.level().getNearbyEntities(LivingEntity.class,
                    roundabout$attackTargeting, self, aab);
            Iterator var4 = le.iterator();
            while (var4.hasNext()) {
                LivingEntity nle = (LivingEntity) var4.next();
                VisageData vd = null;
                if (nle instanceof Player pl) {
                    IPlayerEntity ipe = ((IPlayerEntity) pl);
                    if (ipe.roundabout$getMaskSlot().getItem() instanceof MaskItem MI) {
                        vd = MI.visageData.generateVisageData(pl);
                    }
                }
                if (vd instanceof HayatoVisage jv) {
                    playSoundChallenge(ModSounds.KIRA4_HAYATO_EVENT,40);
                }
            }
        }

        if (self.getDeltaMovement().lengthSqr() > 0.4f) {
            lastTarget = -1;
            staringTicks = 0;
        }else if (staringTicks >= 75) {
            playSoundChallenge(ModSounds.KIRA4_MONOLOGUE_EVENT,1484);
        }else {
            Entity target = MainUtil.getTargetEntity(this.self, 9);
            if (target instanceof LivingEntity) {
                if (target.getId() != lastTarget) {
                    lastTarget = target.getId();
                    staringTicks = 0;
                } else {
                    staringTicks++;
                }
            } else {
                lastTarget = -1;
                staringTicks = 0;
            }
        }
    }
}

