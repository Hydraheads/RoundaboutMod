package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.event.powers.stand.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PowersStarPlatinum extends TWAndSPSharedPowers {
    public PowersStarPlatinum(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersStarPlatinum(entity);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.STAR_SUMMON_SOUND_EVENT;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.STAR_PLATINUM.create(this.getSelf().level());
    }
    @Override
    public SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y){
        if (this.getSelf().isCrouching()){

            setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_ITEM, PowerIndex.SKILL_2);

            boolean done = false;
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1){

                if (!this.getSelf().onGround() && canStandRebound()) {
                    done=true;
                    setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
                }

            } else {

                if (!this.getSelf().onGround()){
                    if (canVault()){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                    } else if (this.getSelf().fallDistance > 3){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                    }
                }
            }
            if (!done){
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
            }
        } else {


            //setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(),2.1F);
            if (targetEntity != null && canGrab(targetEntity)) {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_MOB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_BLOCK, PowerIndex.SKILL_2);
            }


            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
            } else {
                if (!(((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3){
                    setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
                }
            }
        }

        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (this.getSelf().isCrouching()){
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP, PowerIndex.SKILL_4);
        }
    }


    @Override
    public boolean setPowerOther(int move, int lastMove) {
        return super.setPowerOther(move,lastMove);
    }
    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= 100){
            this.maxChargeTSTime = 100;
            this.setChargedTSTicks(100);
        } else if (chargedTSSeconds == 20) {
            this.maxChargeTSTime = 20;
        } else {
            this.maxChargeTSTime = 100;
        }
        return 0;
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
}
