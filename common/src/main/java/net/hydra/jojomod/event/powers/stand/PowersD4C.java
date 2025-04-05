package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
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
        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.D4C_BETWEEN_VISION, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.D4C_DIMENSION_KIDNAP, PowerIndex.SKILL_1_SNEAK);

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SUMMON, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SWAP, PowerIndex.SKILL_2_SNEAK);

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3);
        else
            setSkillIcon(context, x, y, 3, StandIcons.D4C_MELT_DODGE, PowerIndex.SKILL_3_SNEAK);

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
        else
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4_SNEAK);
    }

    private boolean held1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {

    }

    private boolean held2 = false;
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (keyIsDown && !held2)
        {
            held3 = true;

            if (!isHoldingSneak() && !(this.onCooldown(PowerIndex.SKILL_2)))
            {
                if (isBetweenTwoThings(this.getSelf()))
                {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2);
                    this.setCooldown(PowerIndex.SKILL_2, 80);
                }
            }
            else
            {
                if (!(this.onCooldown(PowerIndex.SKILL_2_SNEAK)))
                {
                    Entity TE = MainUtil.getTargetEntity(this.getSelf(), 100, 10);
                    if (TE instanceof D4CCloneEntity clone)
                    {
                        if (clone.player != this.getSelf())
                            return;

                        // there isnt a data index for what i want and im too lazy to add one so here, update move, it probably fits
                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(clone.getId(), PacketDataIndex.INT_UPDATE_MOVE);

                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_SNEAK);

                        this.setCooldown(PowerIndex.SKILL_2_SNEAK, 40);
                    }
                }
            }
        }
        else if (!keyIsDown)
        {
            held3 = false;
        }
    }

    private boolean spawnClone()
    {
        if (!(this.getSelf() instanceof Player PE))
            return false;

        D4CCloneEntity entity = ModEntities.D4C_CLONE.create(this.getSelf().level());
        if (entity == null) {Roundabout.LOGGER.error("Error in PowersD4C: clone entity was null after attempting to create it!"); return false; }

        entity.absMoveTo(
                this.getSelf().getX(),
                this.getSelf().getY(),
                this.getSelf().getZ()
        );

        entity.setPlayer(PE);
        entity.setYRot(this.getSelf().getYRot());
        entity.setXRot(this.getSelf().getXRot());

        this.getSelf().level().addFreshEntity(entity);

        return true;
    }

    private void swapClone(D4CCloneEntity clone)
    {
        if (!(this.getSelf() instanceof Player PE))
            return;

        if (clone.player != PE)
            return;

        Vec3 clonePos = clone.position();
        float cloneXRot = clone.getXRot();
        float cloneYRot = clone.getYRot();

        clone.teleportTo(
                this.getSelf().getX(),
                this.getSelf().getY(),
                this.getSelf().getZ()
        );

        clone.setYRot(this.getSelf().getYRot());
        clone.setXRot(this.getSelf().getXRot());

        PE.teleportTo(clonePos.x, clonePos.y, clonePos.z);
        PE.moveTo(clonePos.x, clonePos.y, clonePos.z, cloneXRot, cloneYRot);
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

                //ModPacketHandler.PACKET_ACCESS.registerNewWorld();
            }
        }
        else if (!keyIsDown)
            held4 = false;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP)
        {
            case PowerIndex.SKILL_4, PowerIndex.SKILL_4_SNEAK, PowerIndex.SKILL_2 -> {
                return !(isBetweenTwoThings(this.getSelf()));
            }
        }

        return super.isAttackIneptVisually(activeP, slot);
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

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                D4CEntity.MANGA_SKIN,
                D4CEntity.WONDER_FESTIVAL
        );
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                return this.spawnClone();
            }
        }

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {
        Entity targeted = this.getSelf().level().getEntity(in);
        if (targeted instanceof D4CCloneEntity clone)
        {
            this.swapClone(clone);
        }

        super.updateIntMove(in);
    }
}