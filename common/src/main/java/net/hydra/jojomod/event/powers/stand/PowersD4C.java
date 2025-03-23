package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ClientConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class PowersD4C extends PunchingStand {
    public PowersD4C(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.D4C.create(this.getSelf().level());
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.NO_CD);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);


        if (!isBetweenTwoThings(this.getSelf()))
            setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4, true);
        else
            setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4);
    }

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {

    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {

    }

    private boolean held3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown && !held3)
        {
            held3 = true;
            super.buttonInput3(keyIsDown, options);
        }
        else if (!keyIsDown)
        {
            held3 = false;
            inputDash = false;
        }
    }

    private boolean held4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (keyIsDown && !held4 && !(this.onCooldown(PowerIndex.SKILL_4)))
        {
            held4 = true;

            if (isBetweenTwoThings(this.getSelf()))
            {
                this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().cooldownsInTicks.d4cDimensionHopToNewDimension);
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4);
            }
        }
        else if (!keyIsDown)
            held4 = false;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (this.activePower == PowerIndex.POWER_4)
            Roundabout.LOGGER.info("AAA");

        return super.tryPower(move, forced);
    }

    @Override
    public void tickPower() {
        super.tickPower();
    }

    /** Is the entity inbetween two tings? */
    @SuppressWarnings("deprecation") // isSolid()
    boolean isBetweenTwoThings(LivingEntity entity)
    {
        // TODO: replace this with a more advanced predicate
        BlockPos pos = entity.blockPosition();
        BlockState bottom = entity.level().getBlockState(pos.subtract(new Vec3i(0, 1, 0)));
        BlockState top = entity.level().getBlockState(pos.subtract(new Vec3i(0, -2, 0)));

        return (!bottom.isAir() && !top.isAir()) && (bottom.isSolid() && bottom.isSolid());
    };


    @Override
    public boolean isWip(){
        return true;
    }
}