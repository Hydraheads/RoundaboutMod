package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class PowersKhnum extends NewDashPreset {
    private static final byte NEUTRAL = 0;
    private static final byte TALL_LEGS = 1;
    private static final byte WIDE = 2;
    private static final byte SMALL = 3;

    public PowersKhnum(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKhnum(entity);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().khnumSettings.enableKhnum;
    }

    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return false;
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> ClientUtil.openKhnumDisguiseScreen();
            case SKILL_1_CROUCH -> ClientUtil.openKhnumVisageScreen();
            case SKILL_2_NORMAL -> setFormClient(TALL_LEGS);
            case SKILL_2_CROUCH -> setFormClient(WIDE);
            case SKILL_3_NORMAL -> dash();
            case SKILL_3_CROUCH -> setFormClient(SMALL);
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> resetClient();
        }
    }

    private void setFormClient(byte form) {
        byte move = form == WIDE ? PowerIndex.POWER_2_SNEAK : PowerIndex.POWER_2;
        ((StandUser) self).roundabout$tryPower(move, true);
        tryPowerPacket(move);
    }

    private void resetClient() {
        ((StandUser) self).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }

    private void resetKhnum() {
        StandUser user = (StandUser) self;
        user.roundabout$setKhnumForm(NEUTRAL);
        user.roundabout$setKhnumVisage(237, 135, 135);
        user.roundabout$clearDisguise();
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_2_SNEAK) {
            ((StandUser) self).roundabout$setKhnumForm(move == PowerIndex.POWER_2_SNEAK ? WIDE : TALL_LEGS);
            return true;
        }
        if (move == PowerIndex.POWER_4) {
            resetKhnum();
            return true;
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (self instanceof Mob && !self.level().isClientSide) {
            byte desiredForm = attackTarget == null ? SMALL : WIDE;
            if (((StandUser) self).roundabout$getKhnumForm() != desiredForm) {
                ((StandUser) self).roundabout$setKhnumForm(desiredForm);
            }
        }
        super.tickMobAI(attackTarget);
    }
}
