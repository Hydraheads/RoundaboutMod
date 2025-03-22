package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

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
        setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NO_CD);

        if (!isBetweenTwoThings(this.getSelf()))
            setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD);
    }

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {

    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {

    }

    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {

    }

    private boolean held4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (keyIsDown && !held4)
        {
            held4 = true;

            if (isBetweenTwoThings(this.getSelf()))
            {
                if (this.getSelf().level().isClientSide)
                    Roundabout.LOGGER.info("CLIENTSIDE");
                else
                    Roundabout.LOGGER.info("SERVERSIDE");
            }
        }
        else if (!keyIsDown)
            held4 = false;
    }

    /** Is the entity inbetween two blocks? */
    @SuppressWarnings("deprecation") // isSolid()
    boolean isBetweenTwoThings(LivingEntity entity)
    {
        BlockPos pos = entity.blockPosition();
        BlockState bottom = entity.level().getBlockState(pos.subtract(new Vec3i(0, 1, 0)));
        BlockState top = entity.level().getBlockState(pos.subtract(new Vec3i(0, -2, 0)));

        return (!bottom.isAir() && !top.isAir()) && (bottom.isSolid() && bottom.isSolid());
    };
}