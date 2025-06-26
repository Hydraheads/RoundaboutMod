package net.hydra.jojomod.event.powers.visagedata.voicedata;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Iterator;
import java.util.List;

public class DIOVoice extends VoiceData{
    public DIOVoice(LivingEntity self) {
        super(self);
        addVoiceLine(new VoiceLine(26, ModSounds.DIO_HOHO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(18, ModSounds.DIO_HOHO_2_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(40, ModSounds.DIO_LAUGH_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(28, ModSounds.DIO_SUBERASHI_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(70, ModSounds.DIO_KONO_DIO_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(16, ModSounds.DIO_INTERESTING_EVENT, VoiceLine.SOUND_CATEGORIES.IDLE));
        addVoiceLine(new VoiceLine(76, ModSounds.DIO_DEATH_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(40, ModSounds.DIO_DEATH_2_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(18, ModSounds.DIO_NO_WAY_EVENT, VoiceLine.SOUND_CATEGORIES.DEATH));
        addVoiceLine(new VoiceLine(16, ModSounds.DIO_KUREI_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIO_CHECKMATE_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(4, ModSounds.DIO_ATTACK_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(7, ModSounds.DIO_ATTACK_2_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(24, ModSounds.DIO_WRY_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(28, ModSounds.DIO_TAUNT_EVENT, VoiceLine.SOUND_CATEGORIES.KILL));
        addVoiceLine(new VoiceLine(8, ModSounds.DIO_HURT_1_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(7, ModSounds.DIO_HURT_2_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(8, ModSounds.DIO_HURT_3_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(11, ModSounds.DIO_HURT_4_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(10, ModSounds.DIO_NANI_EVENT, VoiceLine.SOUND_CATEGORIES.HURT));
        addVoiceLine(new VoiceLine(20, ModSounds.DIO_THE_WORLD_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(29, ModSounds.DIO_THE_WORLD_2_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(23, ModSounds.DIO_THE_WORLD_3_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
        addVoiceLine(new VoiceLine(27, ModSounds.DIO_THE_WORLD_4_EVENT, VoiceLine.SOUND_CATEGORIES.SUMMON));
    }


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
                        if (db > 0.75F) {
                            playSoundChallenge(ModSounds.DIO_JOTARO_EVENT,18);
                            jv.challengeId(20,1);
                        } else if (db > 0.5) {
                            playSoundChallenge(ModSounds.DIO_JOTARO_2_EVENT,18);
                            jv.challengeId(20,1);
                        } else {
                            playSoundChallenge(ModSounds.DIO_APPROACHING_ME_EVENT,101);
                            jv.challengeId(101,2);
                        }
                    }
                }
            }
        }
    }
}
