package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.BubbleScaffoldBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetExplosiveBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
        if (sk == SoftAndWetEntity.KING_SKIN) {
            return ModEntities.SOFT_AND_WET_KING.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DEBUT){
                return ModEntities.SOFT_AND_WET_DEBUT.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DROWNED_SKIN
        || sk == SoftAndWetEntity.DROWNED_SKIN_2){
            return ModEntities.SOFT_AND_WET_DROWNED.create(this.getSelf().level());
        }
        return ModEntities.SOFT_AND_WET.create(this.getSelf().level());
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                miningSettings.speedMultiplierSoftAndWet*0.01));
    }


    @Override
    public float getPickMiningSpeed() {
        return 12F;
    }
    @Override
    public float getAxeMiningSpeed() {
        return 8F;
    }
    @Override
    public float getSwordMiningSpeed() {
        return 8F;
    }
    @Override
    public float getShovelMiningSpeed() {
        return 8F;
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().miningSettings.getMiningTierSoftAndWet;
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
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice >= BARRAGE_NOISE && soundChoice <= BARRAGE_NOISE_2) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            playStandUserOnlySoundsIfNearby(BARRAGE_NOISE, 27, false,true);
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_SOFT_AND_WET_EVENT;
        } else if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.SOFT_AND_WET_BARRAGE_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2) {
            return ModSounds.SOFT_AND_WET_BARRAGE_2_EVENT;
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
        $$1.add(SoftAndWetEntity.DEBUT);
        $$1.add(SoftAndWetEntity.STRIPED);
        $$1.add(SoftAndWetEntity.FIGURE_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN_2);
        $$1.add(SoftAndWetEntity.KING_SKIN);
        $$1.add(SoftAndWetEntity.BETA_SKIN);
        return $$1;
    }

    public boolean goBeyondCharged(){
        return false;
    }
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 2 && ((inShootingMode() && !goBeyondCharged()) || (!canDoBubbleRedirect() && isGuarding()))) {
            return true;
        }
        if (slot == 1 && (inShootingMode() || (!canDoBubbleClusterRedirect() && isGuarding()))) {
            return true;
        }
        if (slot == 3 && (!canVault() && !canFallBrace() && !isGuarding() && isHoldingSneak()) && !canBridge()){
            return true;
        }
        if (slot == 3 && (!canVault() && !canFallBrace() && isGuarding() && !canBigBubble())){
            return false;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            if (canDoBubbleClusterPop()){
                setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_POP, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL, PowerIndex.SKILL_1_SNEAK);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_SELECTION, PowerIndex.NO_CD);
        }


        if (inShootingMode()) {
            setSkillIcon(context, x, y, 2, StandIcons.GO_BEYOND, PowerIndex.SKILL_EXTRA_2);
        } else if (isGuarding()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_POP, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE, PowerIndex.SKILL_2);
        }

        if (canVault()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_VAULT, PowerIndex.SKILL_3_SNEAK);
        } else if (canFallBrace()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_FALL_CATCH, PowerIndex.NONE);
        } else if (isGuarding()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_BUBBLE_ENCASEMENT, PowerIndex.SKILL_EXTRA);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_BUBBLE_SCAFFOLD, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

        if (isGuarding()){
            setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
        } else {
            if (inShootingMode()) {
                setSkillIcon(context, x, y, 4, StandIcons.SOFT_SHOOTING_MODE_EXIT, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.SOFT_SHOOTING_MODE, PowerIndex.SKILL_4);
            }
        }

    }

    /**For mob ai, change the bubbleType before trypower to set what kind of plunder it has*/
    public byte bubbleType = PlunderTypes.ITEM.id;


    @Override
    public boolean canGuard(){
        return super.canGuard();
    }

    public boolean hold1 = false;
    public boolean holdDownClick = false;

    public int getUseTicks(){
        return 2499;
    }
    public int getGoBeyondUseTicks(){
        return 800;

}
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (keyIsDown) {

                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryChargedPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (inShootingMode()){
                        if (!holdDownClick){
                            if (!this.onCooldown(PowerIndex.SKILL_4)) {
                                if (getInExplosiveSpinMode() || confirmShot(getUseTicks())) {
                                    this.tryPower(PowerIndex.POWER_4_EXTRA, true);
                                    if (getInExplosiveSpinMode()){
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4_BONUS);
                                    } else {
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4_EXTRA);
                                    }
                                }
                            }
                        }
                    } else {
                        Minecraft mc = Minecraft.getInstance();

                        if (!isHoldingSneak()) {
                            super.buttonInputAttack(keyIsDown, options);
                        } else {
                            if (this.canAttack()) {
                                this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                holdDownClick = true;
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                            } else {
                                super.buttonInputAttack(keyIsDown, options);
                            }
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (!inShootingMode()) {
                super.buttonInputBarrage(keyIsDown, options);
            }
        }
    }

    public static int maxSuperHitTime = 25;
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {


        StandUser standUser = ((StandUser) playerEntity);
        StandPowers powers = standUser.roundabout$getStandPowers();
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            int ClashTime = Math.min(15, Math.round(((float) attackTimeDuring / maxSuperHitTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !inShootingMode()) {
            if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                            hold1 = true;

                            this.tryPower(PowerIndex.POWER_1_BONUS, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1_BONUS);
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                            if (this.activePower != PowerIndex.POWER_1_SNEAK && !canDoBubbleClusterPop()) {
                                hold1 = true;

                                int bubbleType = 1;
                                ClientConfig clientConfig = ConfigManager.getClientConfig();
                                if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                    bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                                }

                                this.tryChargedPower(PowerIndex.POWER_1_SNEAK, true, bubbleType);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_1_SNEAK, bubbleType);
                            } else {
                                if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                    hold1 = true;
                                    this.tryPower(PowerIndex.EXTRA_2, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_2);
                                }
                            }
                        } else if (this.activePower == PowerIndex.POWER_1_SNEAK || this.canDoBubbleClusterRedirect()) {
                            if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                hold1 = true;
                                this.tryPower(PowerIndex.EXTRA_2, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_2);
                            }
                        }
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
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.primaryPlunderBubbleLifespanInTicks;
        return bubble;
    }
    public SoftAndWetExplosiveBubbleEntity getExplosiveBubble(){
        SoftAndWetExplosiveBubbleEntity bubble = new SoftAndWetExplosiveBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.explosiveBubbleLifespanInTicks;
        return bubble;
    }

    public boolean canShootExplosive(int useTicks){
        if ((shootTicks+useTicks) <= getMaxShootTicks()){
            return true;
        }
        return false;
    }

    public int pauseTicks(){
        return 20;
    }
    public int getPauseGrowthTicks(){
        return pauseGrowthTicks;
    }
    public boolean confirmShot(int useTicks){
        if (canShootExplosive(useTicks)){
            pauseGrowthTicks = pauseTicks();
            setGoBeyondChargeTicks(goBeyondChargeTicks+getGoBeyondUseTicks());
            setShootTicks((shootTicks+useTicks));
            return true;
        }
        return false;
    }

    public int shootTicks = 0;
    public int getShootTicks(){
        return shootTicks;
    }
    public void setShootTicks(int shootTicks){
        this.shootTicks = Mth.clamp(shootTicks,0,getMaxShootTicks());

    }
    public int goBeyondChargeTicks = 0;
    public boolean inExplosiveSpinMode = false;
    public boolean getInExplosiveSpinMode(){
        return inExplosiveSpinMode;
    }
    public void setInExplosiveSpinMode(boolean mode){
        this.inExplosiveSpinMode = mode;
    }
    public int getGoBeyondCharge(){
        return goBeyondChargeTicks;
    }
    public void setGoBeyondChargeTicks(int goBeyondChargeTicks){
        this.goBeyondChargeTicks = Mth.clamp(goBeyondChargeTicks,0,getMaxGoBeyondChargeTicks());

        if (getInExplosiveSpinMode() && getGoBeyondCharge() == 0){
            setInExplosiveSpinMode(false);
        }
        if (!getInExplosiveSpinMode() && getGoBeyondCharge() >= getMaxGoBeyondChargeTicks()){
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_SPIN_MODE_EVENT, 1F, 1.0F);
            }
            setInExplosiveSpinMode(true);
        }
    }
    public int getMaxShootTicks(){
        return 10000;
    }
    public int getMaxGoBeyondChargeTicks(){
        return 10000;
    }

    public float getExplosiveSpeed(){
        if (getInExplosiveSpinMode()){
            return 0.8F;
        }
        return 0.54F;
    }

    public boolean inShootingMode(){
        return getStandUserSelf().roundabout$getCombatMode();
    }
    public boolean shootExplosiveBubble(){
        this.setCooldown(PowerIndex.SKILL_4, 3);
        SoftAndWetExplosiveBubbleEntity bubble = getExplosiveBubble();

        if (bubble != null){

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_2);
            shootExplosiveBubbleSpeed(bubble,getExplosiveSpeed());
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));

        }
        return true;
    }
    public boolean switchModes(){
        if (getStandUserSelf().roundabout$getCombatMode()){
            getStandUserSelf().roundabout$setCombatMode(false);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF_EVENT, 1F, 1.0F);
            }
        } else {
            getStandUserSelf().roundabout$setCombatMode(true);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_EVENT, 1F, 1.0F);
            }
        }

        return true;
    }
    public boolean bubbleShot(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null){
            this.setCooldown(PowerIndex.SKILL_2, 20);

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

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (!(value instanceof SoftAndWetPlunderBubbleEntity PBE && PBE.isPopPlunderBubbble())) {
                    return true;
                }
            }
        }
        return !bubbleList.isEmpty();
    }
    public boolean canDoBubbleClusterRedirect(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleClusterPop(){
        bubbleListInit();

        if (this.activePower == PowerIndex.POWER_1_SNEAK){
            return true;
        }

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE && !PBE.isPopPlunderBubbble()) {
                    if (!PBE.getSingular() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleRedirect(){

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

    public boolean clusterBubblePop() {
        bubbleListInit();
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 10);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished() && !plunder.getSingular()){
                                if (plunder.getPlunderType() == PlunderTypes.ITEM.id){
                                    plunder.forceDropItem();
                                }
                                plunder.popBubble();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean bubblePop() {
        bubbleListInit();
        if (!this.self.level().isClientSide()) {
            bubbleNumber++;
        }
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 10);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished()){
                                if (plunder.getPlunderType() == PlunderTypes.ITEM.id){
                                    plunder.forceDropItem();
                                }
                               plunder.popBubble();
                            }
                        } else if (value instanceof SoftAndWetExplosiveBubbleEntity SBE){
                            SBE.popWithForce();
                        }
                    }
                }
            }
        }
        return false;
    }

    /**If there are any cluster bubbles that have not yet entered the plunder state, redirect them*/
    public boolean bubbleClusterRedirect(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        shootBubbleSpeed2(PBE,PBE.getSped()*0.65F);
                        PBE.setLaunched(true);
                    }
                }
            }
        }
        return false;
    }

    /**Redirects all bubbles that have entered the plunder state and are compatible*/
    public boolean bubbleRedirect(){
        bubbleListInit();
        if (!bubbleList.isEmpty()){

            if (canDoBubbleRedirect()) {
                this.setCooldown(PowerIndex.SKILL_EXTRA_2, 3);

                Vec3 vec3d = this.self.getEyePosition(0);
                Vec3 vec3d2 = this.self.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 30, vec3d2.y * 30, vec3d2.z * 30);
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
                                if (totalnumber > 1){
                                    vector = new Vec3(
                                            vector.x()+(((Math.random()-0.5)*totalnumber)*value.getSped()*0.03),
                                            vector.y(),
                                            vector.z()+(((Math.random()-0.5)*totalnumber)*value.getSped()*0.03)
                                    ).normalize().scale(value.getSped());
                                }
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

    @Override
    public boolean hasShootingModeVisually(){
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
        return 0.17F;
    }

    public void shootBubble(SoftAndWetBubbleEntity ankh){
        shootBubbleSpeed(ankh, 1.01F);
    }
    public void shootExplosiveBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.72F), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.62), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootBubbleSpeed2(SoftAndWetBubbleEntity ankh, float speed){
        ankh.shootFromRotationDeltaAgnostic3(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed);
    }
    public void shootBubbleRandomly(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.2), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(),-1*(float)(Math.random()*50), (float)(Math.random()*360), 1.0F, 0.25F, 0);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.bubbleShot();
        } else if (move == PowerIndex.POWER_2_EXTRA) {
            return this.bubbleRedirect();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.bubblePop();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_4){
            return this.switchModes();
        } else if (move == PowerIndex.POWER_4_EXTRA){
            if (!this.self.level().isClientSide()) {
                this.setInExplosiveSpinMode(false);
            }
            return this.shootExplosiveBubble();
        } else if (move == PowerIndex.POWER_4_BONUS){
            if (!this.self.level().isClientSide()) {
                this.setInExplosiveSpinMode(true);
            }
            return this.shootExplosiveBubble();
        } else if (move == PowerIndex.POWER_3){
            return this.bubbleLadder();
        } else if (move == PowerIndex.POWER_3_EXTRA){
            return this.bubbleLadderPlace();
        } else if (move == PowerIndex.POWER_3_BONUS){
            return this.bigEncasementBubbleCreate();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.bubbleClusterStart();
        } else if (move == PowerIndex.POWER_1) {
            return this.spawnRandomBubble();
        } else if (move == PowerIndex.POWER_1_BONUS) {
            return this.bubbleClusterRedirect();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerKickAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.EXTRA_2) {
            return this.clusterBubblePop();
        }
        return super.setPowerOther(move,lastMove);
    }
    public int chargedFinal;
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,maxSuperHitTime);
        if (chargedFinal >= maxSuperHitTime){
            this.animateStand((byte) 32);
        } else {
            this.animateStand((byte) 30);
        }
        //playBarrageCrySound();
        return true;
    }
    public boolean setPowerKickAttack() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.animateStand((byte) 31);
        this.poseStand(OffsetIndex.GUARD);
        return true;
    }

    public static final byte BARRAGE_NOISE = 20;
    public static final byte BARRAGE_NOISE_2 = BARRAGE_NOISE+1;
    public static final byte BARRAGE_NOISE_3 = BARRAGE_NOISE+2;
    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
       if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_1_SNEAK) {
            bubbleType = (byte)chargeTime;
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
        return super.tryChargedPower(move, forced, chargeTime);
    }
    @Override
    public boolean cancelSprintJump(){
       if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            return true;
        }
        return super.cancelSprintJump();
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.POWER_1_SNEAK){
            basis *= 0.2f;
        } else if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE){
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.6f;
        }
        return super.inputSpeedModifiers(basis);
    }

    public boolean bubbleClusterStart(){
        if (!this.self.level().isClientSide()) {
            clusterBubblePop();
        }
        setActivePower(PowerIndex.POWER_1_SNEAK);
        this.poseStand(OffsetIndex.FOLLOW);
        this.attackTimeDuring = 0;
        animateStand((byte) 0);
        return true;
    }
    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().guardPoints.softAndWetDefend;
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos) {

        if (move == PowerIndex.POWER_3_EXTRA) {
            if (blockPos.getX() > 35){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.below();
            } else if (blockPos.getX() < -35){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.above();
            } else {
                int yomi= blockPos.getY();
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.relative(Direction.fromYRot(this.self.getYHeadRot()));
            }
        }
        return tryPower(move, forced);
    }
    public int bubbleNumber = 0;

    public boolean bigEncasementBubbleCreate() {
        this.setCooldown(PowerIndex.SKILL_EXTRA, 80);
        if (!this.self.level().isClientSide()) {
            EncasementBubbleEntity encasement = ModEntities.ENCASEMENT_BUBBLE.create(this.getSelf().level());
            if (encasement != null){

                encasement.bubbleNo = bubbleNumber;
                Vec3 movevec = this.self.getPosition(0).add(0,(this.self.getEyeHeight()*0.65F),0).add(this.self.getForward().normalize().scale(0.72));
                encasement.absMoveTo(movevec.x(), movevec.y(), movevec.z());
                encasement.setUser(this.self);
                encasement.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.encasementBubbleFloatingLifespanInTicks;
                this.getSelf().level().addFreshEntity(encasement);

                //((StandUser)this.self).roundabout$setAdjustedGravity(30);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BIG_BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }
        return false;
    }

    /**Bubble Scaffolding, build a ladder overtime somewhat like the Builder blocks in Twilight*/
    public boolean bubbleLadderPlace(){
        if (!this.self.level().isClientSide()){
            if (MainUtil.tryPlaceBlock(this.self,buildingBubbleScaffoldPos,false)){
                this.self.level().setBlockAndUpdate(buildingBubbleScaffoldPos, ModBlocks.BUBBLE_SCAFFOLD.defaultBlockState());
                if (this.self.level().getBlockEntity(buildingBubbleScaffoldPos) instanceof BubbleScaffoldBlockEntity SBE) {
                    SBE.standuser = this.self;
                    SBE.bubbleNo = bubbleNumber;
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                }
            }
        }
        return false;
    }
    public boolean bubbleLadder(){
        setActivePower(PowerIndex.POWER_3);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.attackTimeDuring = 0;
        bubbleScaffoldCount = 0;
        animateStand((byte) 1);
        buildingBubbleScaffoldPos = this.self.blockPosition();
        return true;
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.updateBubbleCluster();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateKickAttackCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateKickAttack();
        } else if (this.getActivePower() == PowerIndex.POWER_3){
            this.updateBubbleScaffold();
        }
        super.updateUniqueMoves();
    }

    public void updateKickAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.encasementKick();
            }
        }
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.65* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 4.7* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnMobs*0.01)));
        }
    }

    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 2.35* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 5.7* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnMobs*0.01)));
        }
    }


    public float getExplosiveBubbleStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.1* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.softAndWetAttacksOnMobs*0.01)));
        }
    }

    public float getKickAttackKnockback(){
        return (((float)this.chargedFinal/(float)maxSuperHitTime)*2.2F);
    }
    public float getKickAttackStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*1.8F+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+1;
        }
    }

    public void kickAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getKickAttackStrength(entity);
            knockbackStrength = getKickAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (chargedFinal >= maxSuperHitTime) {
                        addEXP(5, LE);
                    }
                }
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= maxSuperHitTime) {
                    knockShield2(entity, getKickAttackKnockShieldTime());

                }
            }

            if (entity instanceof LivingEntity LE) {
                if (chargedFinal >= maxSuperHitTime) {
                    StandUser SE = ((StandUser) LE);
                    if (!SE.roundabout$isLaunchBubbleEncased()) {
                        float xRot = this.self.getXRot();
                        SE.roundabout$setStoredVelocity(
                                new Vec3(
                                        Mth.sin(this.self.getYRot() * ((float) Math.PI / 180)),
                                        Mth.sin((xRot - 20) * ((float) Math.PI / 180)),
                                        -Mth.cos(this.self.getYRot() * ((float) Math.PI / 180))
                                )
                        );
                        if (LE.isAlive()) {
                            SE.roundabout$setBubbleLaunchEncased();
                        }

                        if (!this.self.level().isClientSide()) {
                            Vec3 $$2 = LE.getDeltaMovement();
                            float $$4 = (float) Mth.floor(LE.getY());
                            for (int $$8 = 0; (float) $$8 < 1.0F + LE.getBbWidth() * 20.0F; $$8++) {
                                double $$9 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();
                                double $$10 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();

                                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SPLASH,
                                        LE.getX() + $$9, (double) ($$4 + 1.0F), LE.getZ() + $$10,
                                        30, $$2.x, $$2.y, $$2.z, 0.4);
                            }
                        }

                        if (SE instanceof Player && !this.self.level().isClientSide) {
                            ((ServerPlayer) SE).displayClientMessage(Component.translatable("text.roundabout.launch_bubble_encased"), true);
                        }
                        Vec3 storedVec = SE.roundabout$getStoredVelocity();
                        MainUtil.takeUnresistableKnockbackWithY(LE, 0.13f, storedVec.x, storedVec.y, storedVec.z);
                    }
                }
            }

            int fireCount = 50;
            float firespeed =0.05F;
            if (chargedFinal >= maxSuperHitTime){
                fireCount = 100;
                firespeed =0.1F;
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getKickAttackSound();
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            if (chargedFinal >= maxSuperHitTime && entity instanceof LivingEntity) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WATER_ENCASE_EVENT, SoundSource.PLAYERS, 1F, pitch);
            }
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            kickAttackImpact(target);
        }
    }
    public SoundEvent getKickAttackSound(){
        return ModSounds.SOFT_AND_WET_KICK_EVENT;
    }
    public int getKickAttackKnockShieldTime(){
        return 40;
    }
    public void encasementKick(){

        if (chargedFinal >= maxSuperHitTime) {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianKickMinimum + chargedFinal * 1.5));
        } else {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianKickMinimum + chargedFinal));
        }
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(getTargetEntityId(), PacketDataIndex.INT_STAND_ATTACK);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            kickAttackImpact(targetEntity);
        }
    }
    public void updateKickAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= maxSuperHitTime &&
                    (!(this.getSelf() instanceof Player) || (this.self.level().isClientSide() && isPacketPlayer()))){
                int atd = this.getAttackTimeDuring();
                ((StandUser) this.getSelf()).roundabout$tryChargedPower(PowerIndex.SNEAK_ATTACK, true,maxSuperHitTime);
                if (this.self.level().isClientSide()){
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                }
            }
        }
    }
    int bubbleScaffoldCount = 0;

    public int pauseGrowthTicks = 0;
    public BlockPos buildingBubbleScaffoldPos = BlockPos.ZERO;
    public void updateBubbleScaffold(){
        if (this.self instanceof Player PE && this.self.level().isClientSide()) {
            if (isPacketPlayer()) {
                if (this.attackTimeDuring % 6 == 2) {

                    /**The client is the only one with accurate rotation and timings so it should be deciding how to build the ladder*/
                    ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_3_EXTRA,
                            new BlockPos(
                                    (int)this.self.getXRot(),
                                    (int)this.self.getYRot(),
                                    0
                                    ));
                    bubbleScaffoldCount++;
                    this.setCooldown(PowerIndex.SKILL_3, 240);
                    if (bubbleScaffoldCount >= 10){
                        this.tryPower(PowerIndex.NONE, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    }
                }
            }
        }
    }
    public void updateBubbleCluster(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                bubbleCheck(true);
            }
        } else {
            bubbleCheck(false);
        }
    }
    public void bubbleCheck(boolean packetPlayer){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 37){
                this.tryPower(PowerIndex.NONE, true);
                if (packetPlayer) {
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring % 3 == 0){
                if (packetPlayer){
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                } else {
                    spawnRandomBubble();
                }
            }
        }
    }

    public boolean spawnRandomBubble(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null) {
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(false);
            shootBubbleRandomly(bubble, getBubbleSpeed()); //0.025F
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }

        return true;
    }

    public void unloadBubbles(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.isRemoved() || !value.isAlive() || (this.self.level().isClientSide() && this.self.level().getEntity(value.getId()) == null)) {
                    bubbleList.remove(value);
                } else {
                }
            }
        }
    }

    public int getLowerTicks(){
        return 50;
    }
    public int getLowerGoBeyondTicks(){
        return 5;
    }
    public int getLowerExplosiveSpinTicks(){
        return 100;
    }
    @Override
    public void tickPower(){
        unloadBubbles();
        /**Burn through ticks*/
        if (this.self instanceof Player PE && PE.isCreative()) {
            setShootTicks(0);
        } else if (getPauseGrowthTicks() > 0){
            pauseGrowthTicks-=1;
        } else {
            if (getShootTicks() > 0) {
               setShootTicks(getShootTicks() - getLowerTicks());
            }
        }

        if (this.self instanceof Player PE && PE.isCreative()) {
            if (getGoBeyondCharge()>0) {
                setGoBeyondChargeTicks(0);
            }
        } else {
            if (getGoBeyondCharge()>0){
                if (getInExplosiveSpinMode()){
                    setGoBeyondChargeTicks(getGoBeyondCharge()-getLowerExplosiveSpinTicks());
                } else {
                    setGoBeyondChargeTicks(getGoBeyondCharge()-getLowerGoBeyondTicks());
                }
            }
        }

        super.tickPower();
    }
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (inShootingMode()) {
            } else if (isGuarding()) {

                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)){
                            hold2 = true;


                            this.tryPower(PowerIndex.POWER_2_EXTRA, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_EXTRA);
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


                            this.tryPower(PowerIndex.POWER_2_SNEAK, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_SNEAK);
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

    @Override
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }

    @Override
    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.SUMMON_SOFT_AND_WET_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }
    @Override
    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
    }
    public boolean hold3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!(keyIsDown && !hold3 && doVault())) {
                if (canFallBrace()) {
                    if (keyIsDown) {
                        if (!hold3){
                            hold3 = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isGuarding()) {
                    if (keyIsDown) {
                        if (!hold3){
                            hold3 = true;
                            if (!this.onCooldown(PowerIndex.SKILL_EXTRA) && canBigBubble()) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BONUS, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3_BONUS);
                            }
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isHoldingSneak()) {
                    if (keyIsDown) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)) {
                            if (!hold3) {
                                if (canBridge()) {
                                    hold3 = true;
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);
                                }
                            }
                        }
                    } else {
                        hold3 = false;
                    }
                } else {
                    if (!keyIsDown) {
                        hold3 = false;
                    }
                    super.buttonInput3(keyIsDown, options);
                }
            }
        }
    }

    public boolean hold4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold4){
                        hold4 = true;
                    }
                } else {
                    hold4 = false;
                }
            } else if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!this.onCooldown(PowerIndex.SKILL_3)) {
                        if (!hold4) {
                           hold4 = true;
                        }
                    }
                } else {
                    hold4 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold4) {
                        hold4 = true;

                        this.tryPower(PowerIndex.POWER_4, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4);

                        getStandUserSelf().roundabout$getStandPowers().tryPower(PowerIndex.NONE, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                        ClientUtil.stopDestroyingBlock();
                    }
                } else {
                    hold4 = false;
                }
            }
        }
    }

    public boolean canBridge(){
        return ((this.self.onGround() && !this.self.isInWater()) || this.self.onClimbable() || (this.self instanceof Player PE && PE.isCreative()));
    }


    public boolean canBigBubble(){
        return ((this.self.onGround() || this.self.isInWater()) || this.self.onClimbable() || (this.self instanceof Player PE && PE.isCreative()));
    }
}

