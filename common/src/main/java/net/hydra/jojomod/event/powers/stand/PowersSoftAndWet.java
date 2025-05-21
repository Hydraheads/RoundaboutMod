package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersSoftAndWet extends PunchingStand {
    public PowersSoftAndWet(LivingEntity self) {
        super(self);
    }

    public List<SoftAndWetBubbleEntity> bubbleList = new ArrayList<>();
    @Override
    public StandEntity getNewStandEntity(){
        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (sk == SoftAndWetEntity.KING_SKIN){
            return ModEntities.SOFT_AND_WET_KING.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DROWNED_SKIN
        || sk == SoftAndWetEntity.DROWNED_SKIN_2){
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



    public void bubbleListInit(){
        if (bubbleList == null) {
            bubbleList = new ArrayList<>();
        }
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
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 2 && !canDoBubblRedirect() && isGuarding()) {
            return true;
        }
        if (slot == 2 && !canDoBubblePop() && isHoldingSneak() && !isGuarding()) {
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_SELECTION, PowerIndex.NO_CD);
        }


        if (isGuarding()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_POP, PowerIndex.SKILL_2_SNEAK);
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


    @Override
    public boolean canGuard(){
        return super.canGuard();
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
        bubble.lifeSpan = 400;
        return bubble;
    }
    public boolean bubbleShot(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null){
            this.setCooldown(PowerIndex.SKILL_2, 10);

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_2);
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(true);
            shootBubbleSpeed(bubble,getBubbleSpeed());
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }
        return true;
    }
    public boolean canDoBubblePop(){
        bubbleListInit();
        return !bubbleList.isEmpty();
    }
    public boolean canDoBubblRedirect(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType()==PlunderTypes.SIGHT.id ||
                        PBE.getPlunderType()==PlunderTypes.FRICTION.id))) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean bubblePop() {
        bubbleListInit();
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 40);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder){
                            if (!plunder.getFinished()){
                               plunder.popBubble();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean bubbleRedirect(){
        bubbleListInit();
        if (!bubbleList.isEmpty()){

            if (canDoBubblRedirect()) {
                this.setCooldown(PowerIndex.SKILL_EXTRA_2, 3);

                Vec3 vec3d = this.self.getEyePosition(0);
                Vec3 vec3d2 = this.self.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                BlockHitResult blockHit = this.self.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));

                if (!this.self.level().isClientSide()) {
                    List<SoftAndWetBubbleEntity> bubbleList3 = new ArrayList<>(bubbleList) {
                    };
                    if (!bubbleList3.isEmpty()) {
                        int totalnumber = bubbleList3.size();
                        for (SoftAndWetBubbleEntity value : bubbleList3) {
                            if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType()==PlunderTypes.SIGHT.id ||
                                    PBE.getPlunderType()==PlunderTypes.FRICTION.id))) {
                                Vec3 vector = new Vec3((blockHit.getLocation().x() - value.getX()),
                                        (blockHit.getLocation().y() - value.getY()),
                                        (blockHit.getLocation().z() - value.getZ())).normalize().scale(value.getSped());
                                value.setDeltaMovement(vector);
                                value.hurtMarked = true;
                                value.hasImpulse = true;
                                if (!value.getLaunched()){
                                    value.setLaunched(true);
                                }
                            }
                        }
                    }
                } else {
                    this.self.playSound(ModSounds.BUBBLE_HOVERED_OVER_EVENT, 0.2F, (float) (0.95F+Math.random()*0.1F));
                    this.self.level()
                            .addParticle(
                                    ModParticles.POINTER_SOFT,
                                    blockHit.getLocation().x(),
                                    blockHit.getLocation().y() + 0.5,
                                    blockHit.getLocation().z(),
                                    0,
                                    0,
                                    0
                            );
                }
            }
            /**
            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
             **/
        }
        return true;
    }

    public float getBubbleSpeed(){
        if (bubbleType == PlunderTypes.OXYGEN.id){
            return 0.6F;
        } else if (bubbleType == PlunderTypes.ITEM.id){
            return 0.5F;
        } else if (bubbleType == PlunderTypes.POTION_EFFECTS.id){
            return 0.25F;
        } else if (bubbleType == PlunderTypes.SOUND.id){
            return 0.3F;
        }else if (bubbleType == PlunderTypes.MOISTURE.id){
            return 0.3F;
        }
        return 0.18F;
    }

    public void shootBubble(SoftAndWetBubbleEntity ankh){
        shootBubbleSpeed(ankh, 1.01F);
    }
    public void shootBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.62), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.bubbleShot();
        } else if (move == PowerIndex.POWER_2_EXTRA) {
            return this.bubbleRedirect();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.bubblePop();
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

    public void unloadBubbles(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.isRemoved() || !value.isAlive()) {
                    bubbleList.remove(value);
                }
            }
        }
    }
    @Override
    public void tickPowerEnd(){
        unloadBubbles();
        super.tickPowerEnd();
    }
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)){
                            hold2 = true;

                            int bubbleType = 1;
                            ClientConfig clientConfig = ConfigManager.getClientConfig();
                            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                            }

                            this.tryChargedPower(PowerIndex.POWER_2_EXTRA, true, bubbleType);
                            ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2_EXTRA, bubbleType);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }
            } else if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)){
                            hold2 = true;

                            int bubbleType = 1;
                            ClientConfig clientConfig = ConfigManager.getClientConfig();
                            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                            }

                            this.tryChargedPower(PowerIndex.POWER_2_SNEAK, true, bubbleType);
                            ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2_SNEAK, bubbleType);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
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

