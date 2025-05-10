package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
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
        } else if (((StandUser)this.getSelf()).roundabout$getStandSkin() == SoftAndWetEntity.DROWNED_SKIN
        || ((StandUser)this.getSelf()).roundabout$getStandSkin() == SoftAndWetEntity.DROWNED_SKIN_2){
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
        $$1.add(SoftAndWetEntity.FIGURE_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN_2);
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

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_FILL, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE, PowerIndex.SKILL_2);
        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);
    }

    /**For mob ai, change the bubbleType before trypower to set what kind of plunder it has*/
    public byte bubbleType = PlunderTypes.ITEM.id;

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
                        hold1 = true;
                       ClientUtil.openPlunderScreen();
                    }
                } else {

                    hold1 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }
    public boolean hold2 = false;

    public SoftAndWetPlunderBubbleEntity getPlunderBubble(){
        SoftAndWetPlunderBubbleEntity bubble = new SoftAndWetPlunderBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        return bubble;
    }
    public boolean bubbleShot(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null){
            shootBubbleSpeed(bubble,0.15F);
            this.getSelf().level().addFreshEntity(bubble);
        }
        return true;
    }
    public void shootBubble(SoftAndWetBubbleEntity ankh){
        shootBubbleSpeed(ankh, 1.01F);
    }
    public void shootBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.7), this.self.getZ());
        ankh.setXRot(this.getSelf().getXRot()%360);
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.bubbleShot();
        }
        return super.setPowerOther(move,lastMove);
    }
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_2) {
            bubbleType = (byte)chargeTime;
        }
        return super.tryChargedPower(move, forced, chargeTime);
    }
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold2) {
                        hold2 = true;
                    }
                } else {
                    hold2 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_2)){
                            hold2 = true;

                            int bubbleType = 1;
                            ClientConfig clientConfig = ConfigManager.getClientConfig();
                            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                            }
                            this.tryChargedPower(PowerIndex.POWER_2, true, bubbleType);
                            ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2, bubbleType);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {

                    hold2 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }
}

