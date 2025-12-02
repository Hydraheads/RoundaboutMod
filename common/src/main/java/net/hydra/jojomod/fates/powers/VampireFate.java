package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;

public class VampireFate extends VampiricFate {


    public VampireFate() {
        super();
    }
    public VampireFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
        return new VampireFate(entity);
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                hypnosis();
            }
            case SKILL_1_CROUCH -> {
                hairExtendClient();
            }
            case SKILL_2_NORMAL -> {
                suckBlood();
            }
            case SKILL_2_CROUCH -> {
                regenClient();
            }
            case SKILL_3_CROUCH -> {
                bloodSpeedClient();
            }
            case SKILL_3_NORMAL -> {
                dashOrWallWalk();
            }
            case SKILL_4_NORMAL -> {
                setSuperHearingClient();
            }
            case SKILL_4_CROUCH -> {
                clientChangeVision();
            }
        }
    };
    public static final byte HYPNOSIS = 50;
    public static final byte HAIR_EXTENDED = 51;



    @Override
    public boolean tryPower(int move, boolean forced) {
        if (move != HAIR_EXTENDED && !self.level().isClientSide() &&
                getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION) {
            super.setPlayerPos2(PlayerPosIndex.NONE_2);
        }

        switch (move) {
            case WALL_WALK -> {
                wallLatch();
            }
            case HAIR_EXTENDED -> {
                hairExtendServer();
            }
        }
        return super.tryPower(move,forced);
    }
    public void hairExtendClient(){
        if (isHearing()){
            stopHearingClient();
        }
        tryPowerPacket(HAIR_EXTENDED);
    }
    public void hypnosis(){
        if (isHearing()){
            stopHearingClient();
        }
        tryPowerPacket(HYPNOSIS);
    }
    public int animationProgress = 0;
    public int getProgressIntoAnimation(){
        return animationProgress;
    }
    public boolean hasHairExtended(){
        return getActivePower() == HAIR_EXTENDED;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == HYPNOSIS) {
            hypnosisServer();
        }
        return super.setPowerOther(move,lastMove);
    }
    public void hypnosisServer(){
        if (isHypnotizing){
            isHypnotizing = false;
            hypnoTicks = 0;
        } else {
            isHypnotizing = true;
            hypnoTicks = 0;
        }
    }
    public void hairExtendServer() {
        if (getActivePower() != BLOOD_REGEN) {
            if (hasHairExtended()) {
                xTryPower(PowerIndex.NONE, true);
            } else {
                setActivePower(HAIR_EXTENDED);
                setPlayerPos2(PlayerPosIndex.HAIR_EXTENSION);
                this.attackTimeDuring = 0;
            }
        }
    }
    public static final int hairChargeLength = 20;
    public boolean isHypnotizing = false;
    public int hypnoTicks = 0;

    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (slot == 3 && isPlantedInWall() && !isHoldingSneak() && !canLatchOntoWall())
            return true;
        if (slot == 3 && isHoldingSneak() && !canUseBloodSpeed() && !canLatchOntoWall())
            return true;
        if (slot == 2 && isHoldingSneak() && !canUseRegen())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public float getJumpHeightAddon(){
        //if (self.isCrouching() || isFast()){
        //    return super.getJumpHeightAddon()+4;
        //} else {
        //    return super.getJumpHeightAddon();
        //}
        if (self.level().isClientSide() && !isVisionOn()){
            return super.getJumpHeightAddon();
        }
        return super.getJumpHeightAddon()+4;
    }


    @Override
    public void tickPower(){
        tickHypnosis();
        tickHair();
        super.tickPower();
    }

    public void tickHair(){
        if (self.level().isClientSide()){
            if (getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION){
                animationProgress++;
            } else {
                animationProgress = 0;
            }
        }

        if (activePower == HAIR_EXTENDED){

            if (attackTimeDuring >= getMaxAttackTimeDuringHair()) {
                xTryPower(PowerIndex.NONE, true);
            }
        }
    }
    public void tickHypnosis() {
        if (!self.level().isClientSide())
            if (isHypnotizing) {
                if (hypnoTicks % 9 == 0){

                    Vec3 lvec = self.getLookAngle();
                    Position pn = self.getEyePosition().add(lvec.scale(3));
                    Vec3 rev = lvec.reverse();
                    ((ServerLevel) this.self.level()).sendParticles(ModParticles.HYPNO_SWIRL, pn.x(),
                            pn.y(), pn.z(),
                            0,
                            rev.x,rev.y,rev.z,
                            0.08);
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.HYPNOSIS_EVENT, SoundSource.PLAYERS, 1F, 1F);

                    AABB aab = this.getSelf().getBoundingBox().inflate(10.0, 8.0, 10.0);
                    List<? extends LivingEntity> le = this.self.level().getNearbyEntities(Mob.class, hypnosisTargeting, ((LivingEntity)(Object)self), aab);
                    Iterator var4 = le.iterator();
                    while(var4.hasNext()) {
                        Mob nle = (Mob) var4.next();
                        if (!nle.isRemoved() && nle.isAlive() && !nle.isSleeping() &&
                                !(MainUtil.isHypnotismTargetBlacklisted(nle))){
                            if (nle.getTarget() == null || !nle.getTarget().isAlive()
                            || nle.getTarget().isRemoved()){
                                ((IMob) nle).roundabout$setHypnotizedBy(self);
                            }
                        }
                    }

                }
                hypnoTicks++;
            }
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (hasHairExtended())
            basis*=0.8F;
        return super.inputSpeedModifiers(basis);
    }

    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(7);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.FLESH_BUD, PowerIndex.FATE_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.HYPNOTISM, PowerIndex.FATE_1);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
        }

        if ((canLatchOntoWall() || (isPlantedInWall() && !isHoldingSneak())) && canWallWalkConfig()) {
            setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
        } else if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        if (!isHoldingSneak() || isHearing()) {
            setSkillIcon(context, x, y, 4, StandIcons.HEARING_MODE, PowerIndex.FATE_4_SNEAK);
        } else {
            if (isVisionOn()){
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_ON, PowerIndex.FATE_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_OFF, PowerIndex.FATE_4);
            }
        }
    }

    public int getMaxAttackTimeDuringHair(){
        return hairChargeLength;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {

        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (!standOn){
            Entity TE = getTargetEntity(playerEntity, 7, 15);

            if (getActivePower() == HAIR_EXTENDED){
                float finalATime = (float) attackTimeDuring / getMaxAttackTimeDuringHair();
                int barTexture = 0;
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)){
                    barTexture = 68;
                }
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                int finalATimeInt = Math.round(finalATime * 15);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);
                return;
            }
        }
        super.renderAttackHud(context,playerEntity,scaledWidth,scaledHeight,ticks,vehicleHeartCount,flashAlpha,otherFlashAlpha);
    }
}
