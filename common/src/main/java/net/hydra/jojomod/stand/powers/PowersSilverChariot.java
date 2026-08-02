package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.GoddessStatueBlock;
import net.hydra.jojomod.block.GoddessStatuePart;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
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
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PowersSilverChariot extends NewPunchingStand {

    public static final byte
            SILVER_CHARIOT_RAPIER_SLASH = 84,
            SILVER_CHARIOT_RAPIER_SPIN = 83,
            SILVER_CHARIOT_OFFHAND_WEAPON = 82,
            SILVER_CHARIOT_CONTROL_MODE_ONE = 85,
            SILVER_CHARIOT_ARMOR_SHED = 86,
            SILVER_CHARIOT_SELF_GRAB = 87,
            SILVER_CHARIOT_SLAB_CUTTING = 88,
            SILVER_CHARIOT_STATUE_CUTTING = 89,
            SILVER_CHARIOT_RAPIER_SHOT = 90,
            SILVER_CHARIOT_RAPIER_SHOT_PLATFORM = 91;


    // Configs
    public int getAttackMultOnPlayers() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnPlayers;
    }

    public int getAttackMultOnMobs() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnMobs;
    }

    public int getGuardPoints() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotGuardPoints;
    }

    public int getMiningSpeedMultiplierSilverChariot() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.miningSpeedMultiplierSilverChariot;
    }

    public int getMiningTier() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.getMiningTierSilverChariot;
    }

    // Levels

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    public int getRapierSlashLevel() {
        return 1;
    }

    public int getRapierSpinLevel() {
        return 1;
    }

    public int getOffhandWeaponLevel() {
        return 1;
    }

    public int getControlModeOneLevel() {
        return 1;
    }

    public int getArmorShedLevel() {
        return 1;
    }

    public int getSelfGrabLevel() {
        return 1;
    }

    public int getSlabCuttingLevel() {
        return 1;
    }

    public int getStatueCuttingLevel() {
        return 1;
    }

    public int getRapierShotLevel() {
        return 1;
    }

    public int getRapierShotPlatformLevel() {
        return 1;
    }

    // Cooldowns
    public int getCooldownRapierSpin() {
        return 1;
    }

    public int getCooldownRapierSlash() {
        return 1;
    }

    public int getCooldownOffhandWeapon() {
        return 1;
    }

    public int getCooldownControlModeOne() {
        return 1;
    }

    public int getCooldownArmorShed() {
        return 1;
    }

    public int getCooldownSelfGrab() {
        return 1;
    }

    public int getCooldownSlabCutting() {
        return 1;
    }

    public int getCooldownStatueCutting() {
        return 1;
    }

    public int getCooldownRapierShot() {
        return 1;
    }

    public int getCooldownRapierShotPlatform() {
        return 1;
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.enableSilverChariot;
    }

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

    // Client side
    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                // Look at PowersMagiciansRed code

                // TODO: Implement rapier spin ability
                // rapierSpinClient();

                // Might implement forward barrage with 3 block range
            }
            case SKILL_1_CROUCH -> {
                // TODO: Implement rapier slash ability
                rapierSlashClient();
            }
            case SKILL_1_GUARD -> {
                // TODO: Implement slab cutting ability
                slabCuttingClient();
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
                // TODO: Implement Silver Chariot arm render ability
                // armRenderClient();
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
                statueCuttingClient();
            }
            case SKILL_4_CROUCH_GUARD -> {

            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1_SNEAK -> {
                rapierSlashServer();
            }
            case PowerIndex.POWER_1_BLOCK -> {
                slabCuttingServer();
            }
            case PowerIndex.POWER_4_BLOCK -> {
                statueCuttingServer();
            }
            case PowerIndex.POWER_3_SNEAK_EXTRA -> {
                selfGrabClient();
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    public boolean setPowerAttack() {
        return super.setPowerAttack();
    }

    @Override
    public boolean setPowerNone() {
        return super.setPowerNone();
    }

    @Override
    public void tickPower() {
        // super.tickPower();

        // Control mode

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
                controlModeOne();
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
                poseStand(OffsetIndex.LOOSE);
                ipe.roundabout$setIsControlling(ID);
            } else {
                ipe.roundabout$setIsControlling(ID);
                poseStand(OffsetIndex.FLOAT);
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
                entity.xxa = 0;
                entity.zza = 0;
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

    public void rapierSpinClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1) && canExecuteMoveWithLevel(getRapierSpinLevel())) {

        }
    }

    public void rapierSpinServer() {
        setAttackTimeDuring(-10);
        if (!self.level().isClientSide()) {

        }
    }

    public void rapierSlashClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK) && canExecuteMoveWithLevel(getRapierSlashLevel())) {
            if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
        }
    }

    public void rapierSlashServer() {
        setAttackTimeDuring(-10);
        if (!self.level().isClientSide())
        {
            MainUtil.playPop(self);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, (float) (1.05f + Math.random() * 0.05f));
            List<Entity> hitbox = StandGrabHitbox(self,DamageHandler.genHitbox(self, self.getX(), self.getY(),
                    self.getZ(), 4, 4, 4), 4, 360,true);
            if (hitbox != null)
            {
                for (Entity e : hitbox)
                {
                    if (!e.isInvulnerable() && e.isAlive() && e.getUUID() != self.getUUID() && (MainUtil.isStandPickable(e) || e instanceof StandEntity))
                    {
                        if
                        (
                            !(e instanceof StandEntity SE1 && SE1.getUser() != null && SE1.getUser().is(self))
                        )
                        {
                            if
                            (
                                DamageHandler.StandDamageEntity(e, getRapierSlashStrength(e), this.self)
                            )
                            {
                                e.setDeltaMovement(0, 0, 0);

                                if (e instanceof LivingEntity LE){
                                    MainUtil.makeBleed(LE,1,300,this.self);
                                }

                                if (e instanceof Player pl){
                                    setDazed(pl,(byte) 16);
                                } else if (e instanceof LivingEntity livingEntity && !MainUtil.isBossMob(livingEntity)){
                                    setDazed(livingEntity,(byte) 16);
                                }
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.05f));
                            } else {
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.1f));
                            }
                        }
                    }
                }
            }
        }
    }

    public float getRapierSlashStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(
                    (float) ((float) 1.35F * (getAttackMultOnPlayers() * 0.01))
            );
        } else {
            return levelupDamageMod(
                    (float) ((float) 5F * (getAttackMultOnMobs() * 0.01))
            );
        }
    }



    // Statue cutting ability
    public void statueCuttingClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4_GUARD) && canExecuteMoveWithLevel(getRapierSlashLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_4_BLOCK);
        }
    }

    public void statueCuttingServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            if (MainUtil.getIsGamemodeApproriateForGrief(player)) {
                if (canCreateStatue()) {

                }
            }
        }
    }

    public boolean canCreateStatue() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockPos bp2 = bp.below();
            BlockPos bp3 = bp.above();

            BlockState bs = this.self.level().getBlockState(bp);
            BlockState bs2 = this.self.level().getBlockState(bp2);
            BlockState bs3 = this.self.level().getBlockState(bp3);

            if (bs.is(Blocks.STONE) && bs2.is(Blocks.STONE) && bs3.is(Blocks.STONE)) {
                BlockState bs4 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.BOTTOM);
                BlockState bs5 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.MIDDLE);
                BlockState bs6 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.TOP);

                this.self.level().setBlock(
                        bp2,
                        bs4,
                        GoddessStatueBlock.UPDATE_ALL
                );
                this.self.level().setBlock(
                        bp,
                        bs5,
                        GoddessStatueBlock.UPDATE_ALL
                );this.self.level().setBlock(
                        bp3,
                        bs6,
                        GoddessStatueBlock.UPDATE_ALL
                );

            }
        }
        return false;
    }



    // Slab cutting
    public void slabCuttingClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1_GUARD) && canExecuteMoveWithLevel(getSlabCuttingLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_1_BLOCK);
        }
    }

    public void slabCuttingServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            if (MainUtil.getIsGamemodeApproriateForGrief(player)) {
                if (canCreateSlab()) {

                }
            }
        }
    }

    public boolean canCreateSlab() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockState bs = this.self.level().getBlockState(bp);

            Block slab = MainUtil.SILVER_CHARIOT_BLOCK_TO_SLAB.get(bs.getBlock());
            if (slab != null && !(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, bp))) {
                self.level().setBlock(
                        bp,
                        slab.defaultBlockState(),
                        Block.UPDATE_ALL
                );
            }
        }
        return false;
    }



    // Self grab
    public void controlModeOne() {

    }

    public void selfGrabClient() {
        if (!this.onCooldown(PowerIndex.SKILL_3) && canExecuteMoveWithLevel(getSelfGrabLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
        }
    }

    public void selfGrabServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                Entity entity = this.getSelf();


                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
                this.setActivePower(PowerIndex.POWER_3_SNEAK_EXTRA);
                this.setAttackTimeDuring(0);
                poseStand(OffsetIndex.LOOSE);
            }
        }
    }
}