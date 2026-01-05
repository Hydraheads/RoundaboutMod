package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PunchingGeneralPowers extends GeneralPowers {
    public PunchingGeneralPowers(LivingEntity self) {
        super(self);
    }
    public boolean isBrawling(){
        return true;
    }
    public PunchingGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new PunchingGeneralPowers(entity);
    }

    @Override
    public boolean isMining() {
        return true;
    }

    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) { if (this.canAttack()) {
            this.tryPower(PowerIndex.ATTACK);
            tryPowerPacket(PowerIndex.ATTACK);
        }}
    }
    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        StandUser standUser = ((StandUser) this.getSelf());
        if (isGuarding() && this.getSelf().getVehicle() == null) {
            basis*=0.2f;
        }
        return basis;
    }

    public boolean cancelSprintJump(){
        return this.isGuarding();
    }

    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf()) && !this.isGuarding()) {
            buttonInputAttack(keyIsDown, options);
        }
    }
    public void preCheckButtonInputUse(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputUse(keyIsDown, options);
        }
    }
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputBarrage(keyIsDown, options);
        }
    }
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            return buttonInputGuard(keyIsDown, options);
        }
        return false;
    }

    public boolean canGuard(){
        return !this.isBarraging();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            ((StandUser)this.getSelf()).roundabout$tryPowerP(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    @Override
    public void tickPower(){
        if (!self.level().isClientSide()) {
            if (getActivePower() != PowerIndex.GUARD && getPlayerPos2() == PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
        }
        super.tickPower();
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!self.level().isClientSide()) {
            if (move != PowerIndex.GUARD && getPlayerPos2() == PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public boolean setPowerGuard(){
        if (!self.level().isClientSide()) {
            if (getPlayerPos2() != PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.GUARD);
            }
        }
        return super.setPowerGuard();
    }


    @Override
    public boolean interceptAttack(){
        return true;
    }

    @Override
    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return true;
    }

    public float getPunchAngle(){
        return ClientNetworking.getAppropriateConfig().generalStandSettings.basePunchAngle;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean powerOn = PowerTypes.hasPowerActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (powerOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (powerOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {
                    if (TE != null) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (powerOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }
}
