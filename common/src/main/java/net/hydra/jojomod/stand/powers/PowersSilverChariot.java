package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PowersSilverChariot extends NewPunchingStand {
	public PowersSilverChariot(LivingEntity self) {
        super(self);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.SILVER_CHARIOT.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSilverChariot(entity);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));

        return $$1;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                // TODO: Implement rapier spin ability
                // rapierSpinClient();
            }

            case SKILL_1_CROUCH -> {
                // TODO: Implement rapier slash ability
                // rapierSlashClient();
            }

            case SKILL_1_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_1_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_2_NORMAL -> {
                // TODO: Implement control mode ability
                toggleControlModeClient(0);
            }
            case SKILL_2_CROUCH -> {
                // Might implement another ability here
            }
            case SKILL_2_GUARD -> {
                // TODO: Implement armor shed ability
            }
            case SKILL_2_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                // TODO: Implement carry self ability
                // toggleControlModeClient((short) 1);
            }
            case SKILL_3_GUARD -> {
                // TODO: Implement slab cutting ability
                // tryCutBlockIntoSlabsClient();
            }
            case SKILL_3_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_4_NORMAL -> {
                // TODO: Implement rapier shot ability
                // shootRapierClient();
            }
            case SKILL_4_CROUCH -> {
                // TODO: Implement platform rapier shot ability
                // shootRapierPlatformClient()
            }
            case SKILL_4_GUARD -> {
                // TODO: Implement statue cutting ability
                // tryToCreateStatue();
            }
            case SKILL_4_CROUCH_GUARD -> {

            }
        }
    }

    @Override
    public void tickPower() {
        // super.tickPower();
        if (this.getStandEntity(this.getSelf()) instanceof SilverChariotEntity SCE) {
            if (!isPiloting()) {
                SCE.setDeltaMovement(SCE.getDeltaMovement().x, 0, SCE.getDeltaMovement().z);
            }
        }

        if (this.self instanceof Player PL){
            int getPilotInt = ((IPlayerEntity) PL).roundabout$getControlling();
            Entity getPilotEntity = this.self.level().getEntity(getPilotInt);
            if (this.self.level().isClientSide() && isPacketPlayer()) {
                if (getPilotEntity instanceof LivingEntity le) {
                    if (le.isRemoved() || !le.isAlive() ||
                            MainUtil.cheapDistanceTo2(le.getX(), le.getZ(), PL.getX(), PL.getZ())
                                    > getMaxPilotRange()) {
                        IPlayerEntity ipe = ((IPlayerEntity) PL);
                        ipe.roundabout$setIsControlling(0);
                        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
                        ClientUtil.setCameraEntity(null);
                    } else {
                        StandEntity SE = getStandEntity(this.self);
                        if (SE != null && le.is(SE)) {
                            ClientUtil.setCameraEntity(le);
                        }
                    }
                } else {
                    ClientUtil.setCameraEntity(null);
                }
            }
        }

        super.tickPower();

        if(this.getStandEntity(this.getSelf()) != null && this.getSelf() != null) {
            if (MainUtil.cheapDistanceTo2(this.getStandEntity(self).getX(), this.getStandEntity(self).getZ(), this.self.getX(), this.self.getZ()) > getMaxPilotRange()) {
                ((StandUser)this.self).roundabout$setActive(false);
            }
        }

        if (this.getStandEntity(this.getSelf()) != null) {
            if (this.getStandEntity(this.getSelf()).forceDespawnSet) {
                setPowerNone();
            }
        }



        if(this.self != null && this.self.isUsingItem() && isPiloting()){
            this.self.stopUsingItem();
        }

        // super.tickPower();
    }

    public void tryToDashClient(){
        // if (!doVault()){
        //    dash();
        // }
        dash();
    }


    // Control mode

    public boolean controlModeZero = false;
    public boolean controlModeOne = false;
    public boolean controlModeTwo = false;

    public void toggleControlModeClient(int controlModeType) {
        switch (controlModeType) {
            case 0 -> {
                controlModeZero();
            }
            case 1 -> {
                // controlMode1();
            }
            case 2 -> {
                // controlMode2();
            }
        }
    }

    public void controlModeZero() {
        if (isPiloting()) {
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = (IPlayerEntity) PE;
                ipe.roundabout$setIsControlling(0);
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        } else {
            StandEntity entity = this.getStandEntity(this.self);
            int L = 0;
            if (entity != null) {
                L = entity.getId();
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, L);
        }
    }

    @Override
    public void setPiloting(int ID) {
        if (this.self instanceof Player PE) {
            IPlayerEntity ipe = (IPlayerEntity) PE;
            Entity ent = this.self.level().getEntity(ID);
            if (ent != null && ent.is(this.getPilotingStand())) {
                // poseStand(OffsetIndex.LOOSE);
                ipe.roundabout$setIsControlling(ID);
            } else {
                ipe.roundabout$setIsControlling(ID);
                // poseStand(OffsetIndex.FLOAT);
            }
        }
    }

    @Override
    public boolean isPiloting() {
        if (this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            int zint = ipe.roundabout$getControlling();
            StandEntity sde = ((StandUser) PE).roundabout$getStand();
            if (sde != null && zint == sde.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        int $$13 = 0;
        if (entity instanceof SilverChariotEntity SCE) {
            LivingEntity ent = getPilotingStand();
            IEntityAndData entityAndData = ((IEntityAndData) ent);
            if (this.isClient()) {
                entity.xxa = kpi.leftImpulse;
                entity.zza = kpi.forwardImpulse;
                Vec3 delta = entity.getDeltaMovement();

                if (kpi.shiftKeyDown) {
                    $$13--;
                }
                if (kpi.jumping) {
                    $$13++;
                }
                if (ent != null) {
                    if ($$13 != 0) {
                        entity.setDeltaMovement(delta.x, $$13 * 5.0F, delta.z);
                    } else {
                        entity.setDeltaMovement(delta.x, 0, delta.z);
                    }
                }
            } else {
                entity.setDeltaMovement(Vec3.ZERO);
                // entity.xxa = 0;
                // entity.zza = 0;
            }
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if (!(ent instanceof SilverChariotEntity)) {
            if (this.getStandEntity(this.getSelf()) instanceof SilverChariotEntity SCE) {
                if (isPiloting()) {
                    if (this.getStandEntity(this.getSelf()) != null && ent != null && ent instanceof LivingEntity) {
                        if (this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (ent instanceof SilverChariotEntity SCE) {
            if (this.getSelf() == SCE.getUser()) {
                if (this.isHoldingSneak() && !isPiloting()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean pilotInputInteract() {
        return super.pilotInputInteract();
    }

    @Override
    public void synchToCamera(){
        if (isPiloting()) {
            LivingEntity ent = getPilotingStand();
            if (ent != null) {
                ClientUtil.synchToCamera(ent);
            }
        }
    }
}