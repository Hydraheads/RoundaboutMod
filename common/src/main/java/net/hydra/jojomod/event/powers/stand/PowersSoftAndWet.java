package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PowersSoftAndWet extends PunchingStand {
    public PowersSoftAndWet(LivingEntity self) {
        super(self);
    }

    @Override
    public StandEntity getNewStandEntity(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == SoftAndWetEntity.KING_SKIN){
            return ModEntities.SOFT_AND_WET_KING.create(this.getSelf().level());
        } else if (((StandUser)this.getSelf()).roundabout$getStandSkin() == SoftAndWetEntity.DROWNED_SKIN){
            return ModEntities.SOFT_AND_WET_DROWNED.create(this.getSelf().level());
        }
        return ModEntities.SOFT_AND_WET.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSoftAndWet(entity);
    }


    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }


    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_SOFT_AND_WET_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(SoftAndWetEntity.LIGHT_SKIN);
        $$1.add(SoftAndWetEntity.MANGA_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN);
        $$1.add(SoftAndWetEntity.KING_SKIN);
        $$1.add(SoftAndWetEntity.BETA_SKIN);
        return $$1;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_SELECTION, PowerIndex.NO_CD);

        }

        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2);

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);
    }

    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold1) {
                        hold1 = true;
                    }
                } else {
                    hold1 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)){
                            hold1 = true;
                           ClientUtil.openPlunderScreen();
                        }
                    }
                } else {

                    hold1 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }
}

