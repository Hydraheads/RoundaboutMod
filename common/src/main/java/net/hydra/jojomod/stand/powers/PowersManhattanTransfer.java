package net.hydra.jojomod.stand.powers;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.*;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.entity.stand.StandEntity;
import java.util.Arrays;
import java.util.List;
import static net.hydra.jojomod.event.index.SoundIndex.MANHATTAN_RAIN;

public class PowersManhattanTransfer extends NewDashPreset {
    public PowersManhattanTransfer(LivingEntity self) {
        super(self);
    }
    public static final byte
            MANHATTAN_DODGE = 82,
            DEFLECT_PROJECTILE = 83,

         UNLOADED_HATTAN =84,
         LOADED_HATTAN =85,
         LOAD_CHECK = 86;

    private byte currentHattanStatus = UNLOADED_HATTAN;

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowersManhattanTransfer.LOAD_CHECK-> {
                this.currentHattanStatus = (byte)data;
                if (data == LOADED_HATTAN) {
                    //this.setPowerNone();
                }
            }

        }
        super.updatePowerInt(activePower,data);
    }

    public void syncHattanStatus(byte status) {
        this.currentHattanStatus = status;
        this.updatePowerInt(PowersManhattanTransfer.LOAD_CHECK, status);
        if(this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), PowersManhattanTransfer.LOAD_CHECK, status);
        }
    }

    public boolean isLoaded(){
        if(!this.isClient()) {
            this.currentHattanStatus = LOADED_HATTAN;

            this.syncHattanStatus(LOADED_HATTAN);
        }
        return  true;
    }

    public boolean isNotLoaded(){
        if(!this.isClient()) {
            this.currentHattanStatus = UNLOADED_HATTAN;
            this.syncHattanStatus(UNLOADED_HATTAN);
        }
        return  true;
    }

    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().manhattanTransferSettings.enableManhattanTransfer;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersManhattanTransfer(entity);
    }
    @Override
    public void onStandSummon(boolean desummon) {
        if(desummon){
            if(this.getStandEntity(this.getSelf()) instanceof ManhattanTransferEntity ME){
                ME.isDesummoning = true;
            }
        }
        super.onStandSummon(desummon);
    }
   @Override
    public StandEntity getNewStandEntity() {
        byte skin = ((StandUser) this.getSelf()).roundabout$getStandSkin();
            if (((StandUser) this.getSelf()).roundabout$getStandSkin() == ManhattanTransferEntity.POLLINATION_SKIN) {
                return ModEntities.POLLINATION_TRANSFER.create(this.getSelf().level());
            }
            return ModEntities.MANHATTAN_TRANSFER.create(this.getSelf().level());
    }
    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }
    @Override
    public int getMaxPilotRange() {
        return ClientNetworking.getAppropriateConfig().manhattanTransferSettings.manhattanTransferMaxRange;
    }
    public int configSpeed(){
        return ClientNetworking.getAppropriateConfig().manhattanTransferSettings.getAutoSpeed;
    }
    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                    switchShooting();
            }
                case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                toggleControlModeClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                if(!isPiloting()) {
                    dash();
                }
                else{
                    if (this.currentHattanStatus != LOADED_HATTAN){
                        manhattanDodge();
                    }
                }
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                switchVisionClient();
            }
        }
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                return switchShootingOther();
            }
            case PowerIndex.POWER_4 -> {
                switchVision();
            }
        }
            return super.setPowerOther(move, lastMove);
    }

    public void switchShooting(){
        if(!isPiloting()) {
            this.tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    public boolean switchShootingOther(){
        if (!isClient() && this.self instanceof Player PE) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!switchShootingMode());
        }
        return true;
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1 && isPiloting()){
            return true;
        }
        if(slot == 3 && this.currentHattanStatus == LOADED_HATTAN && isPiloting()){
            return  true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowersManhattanTransfer.MANHATTAN_DODGE -> {
                    this.setCooldown(PowersManhattanTransfer.MANHATTAN_DODGE, ClientNetworking.getAppropriateConfig().manhattanTransferSettings.manhattanDashCooldown);
                    this.setXtraSpdTick(10);
                    this.getStandEntity(this.getSelf()).level().playSound(null, this.getSelf().blockPosition(), ModSounds.VAMPIRE_DASH_EVENT, SoundSource.PLAYERS, 0.8F, 2F);

                    if (this.getStandEntity(this.getSelf()) != null && this.getStandEntity(this.getSelf()) instanceof ManhattanTransferEntity ME) {
                        if (!ME.level().isClientSide()) {
                            ((ServerLevel) ME.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    ME.getX(), ME.getY(), ME.getZ(),
                                    0, 0, 0, 0, 0);
                        }
                    }
            }
            case PowersManhattanTransfer.DEFLECT_PROJECTILE -> {
                if(this.getStandEntity(this.getSelf()) != null && this.getStandEntity(this.getSelf()) instanceof  ManhattanTransferEntity ME){
                    if(this.currentHattanStatus == LOADED_HATTAN) {
                        this.soundThree();
                    }
                    ME.shootHattan();
                    ME.setHeldItemManhattan(ItemStack.EMPTY);
                    ME.hasItem = false;
                }
            }
        }
        return super.tryPower(move, forced);
    }

    public void manhattanDodge() {
        if (!onCooldown(PowersManhattanTransfer.MANHATTAN_DODGE) && !isAttackIneptVisually(PowersManhattanTransfer.MANHATTAN_DODGE,3)) {
            tryPower(PowersManhattanTransfer.MANHATTAN_DODGE);
            tryPowerPacket(PowersManhattanTransfer.MANHATTAN_DODGE);
            if (isClient()) {
                this.self.playSound(ModSounds.VAMPIRE_DASH_EVENT, 100F, 1.2F);
            }
        }
    }

    public void soundThree() {
            if (isClient()) {
                if(this.self.distanceTo(this.getStandEntity(this.getSelf())) > 16) {
                    this.self.playSound(ModSounds.BULLET_RICOCHET_EVENT, 100F, (this.getStandEntity(this.getSelf()).getRandom().nextFloat() * 0.2F + 0.7F));
                }
            }
    }
    public void switchVisionClient(){
        this.tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
        if (isClient() && visionModeClient) {
            this.self.playSound(ModSounds.MANHATTAN_VISION_EVENT, 150F, 0.9F);
        }
    }

    @Override
    public void updateIntMove(int in) {
        super.updateIntMove(in);
    }

    public boolean visionModeClient = false;

    public void switchVision(){
        if (isClient() && this.self instanceof Player PE) {

            if (!visionModeClient) {
                visionModeClient = true;
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision").withStyle(ChatFormatting.DARK_GREEN), true);
            }
            else{
                visionModeClient = false;
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision_off").withStyle(ChatFormatting.DARK_AQUA), true);
            }
        }
    }
    @Override
    public void pilotInputAttack(){
        LivingEntity ent = getPilotingStand();
        if (ent != null && !switchShootingMode()) {
            tryPower(PowersManhattanTransfer.DEFLECT_PROJECTILE, true);
            tryPowerPacket(PowersManhattanTransfer.DEFLECT_PROJECTILE);
            Entity TE = MainUtil.getTargetEntity(ent, 300, 10);
            IEntityAndData entityAndData = ((IEntityAndData) TE);
            //If Target is detected
            if (TE != null && entityAndData.roundabout$getTrueInvisibilityManhattan() > 1 && !(TE instanceof StandEntity && !TE.isAttackable())) {
                Vec3 vec3d = ent.getEyePosition(0);
                Vec3 vec3d2 = ent.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                BlockHitResult blockHit = ent.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ent));
                if ((blockHit.distanceTo(ent) - 1) < ent.distanceToSqr(TE)) {
                }
            }
        }
    }
    public boolean switchShootingMode(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    @Override
    public void setPiloting(int ID) {
        if (this.self instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
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
    private float flyingSpeed = 0.055F;
    private float walkingSpeed = 0.005F;
    public void toggleControlModeClient() {
        if (isPiloting()) {
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$setIsControlling(0);
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        } else {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
            StandEntity entity = this.getStandEntity(this.self);
            int L = 0;
            if (entity != null) {
                L = entity.getId();
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, L);
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
    public void sealStand(boolean forced) {
        int sealTime = 0;
        StandUser user = ((StandUser) this.self);
        if (this.self instanceof Player PE && PE.isCreative() && sealTime > 0) {
            sealTime = 0;
        }
        if (this.self.level().isClientSide()) {
            user.roundabout$setMaxSealedTicks(sealTime);
            user.roundabout$setSealedTicks(sealTime);
        }
        if (!this.self.level().isClientSide() && user instanceof Player PE) {
            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) PE),
                    PacketDataIndex.S2C_INT_SEAL, sealTime);
        }
        user.roundabout$setActive(false);
    }
    public boolean isActive() {
        return this.getStandEntity(this.getSelf()) != null;
    }

    public int XtraSpdTick = 0;

    public void setXtraSpdTick(int speedy){XtraSpdTick = speedy;}

    public double extraSpeedEmergencyHattan(){
        if(XtraSpdTick > 7){
            return  3.5F;
        }else if(XtraSpdTick > 4){
            return  3F;
        }
        else if(XtraSpdTick > 1){
            return  2.5F;
        } else{
            return  1F;
        }
    }

    public boolean isSoundRainInterrupted = false;


    @Override
    public void tickPower() {
        super.tickPower();
        if (XtraSpdTick > 1) {
            XtraSpdTick--;
        }
        if (this.getStandEntity(this.getSelf()) instanceof ManhattanTransferEntity ME) {
            if (ME.isInRain()) {
                if (isSoundRainInterrupted) {
                    isSoundRainInterrupted = false;
                }
                if (ME.DodgeRainTicks >= 1) {
                    ME.DodgeRainTicks--;
                } else if (ME.DodgeRainTicks < 1) {
                    ME.setDodgeRainTicks(440);
                    ((StandUser) ME).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.MANHATTAN_RAIN, 12, false);
                }
            }
            if (!ME.isInRain()) {
                ME.setDodgeRainTicks(0);
                ((StandUser) ME).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.ITEM_GROUP, 100, false);
            }

            if (!hasStandActive(this.getSelf())) {
                ME.hasItem = false;
                ME.hasItemTwo = false;
            } else {
                ME.isDesummoning = false;
            }

            if (this.isClient() || !this.isClient()) {
                if (!isPiloting()) {
                    if (this.currentHattanStatus == UNLOADED_HATTAN) {
                        if (this.getStandEntity(this.getSelf()).isInWaterOrRain()) {
                            this.getStandEntity(this.getSelf()).setDeltaMovement(this.getStandEntity(this.getSelf()).getForward().scale(0.010 * configSpeed() * extraSpeedEmergencyHattan()));
                        } else {
                            this.getStandEntity(this.getSelf()).setDeltaMovement(this.getStandEntity(this.getSelf()).getForward().scale(0.022 * configSpeed() * extraSpeedEmergencyHattan()));
                        }
                    } else {
                        this.getStandEntity(this.getSelf()).setDeltaMovement(Vec3.ZERO);
                    }
                }
                if (this.currentHattanStatus == LOADED_HATTAN) {
                    ME.stopsManhattanAnimationsWhenHeldItem = true;
                } else {
                    ME.stopsManhattanAnimationsWhenHeldItem = false;
                }
            }

            Vec3 vec3 = new Vec3(walkingSpeed, 0, walkingSpeed);

            if (isActive()) {
                DimensionType t = this.getStandEntity(this.getSelf()).level().dimensionType();
                DimensionType T = this.getSelf().level().dimensionType();

                if (this.getSelf().distanceTo(this.getStandEntity(this.getSelf())) > this.getMaxPilotRange()) {
                    sealStand(true);
                }
            }

        }
        if (this.self instanceof Player PL) {

            if (this.getStandEntity(this.getSelf()) != null) {
                if (this.getStandEntity(this.getSelf()).forceDespawnSet) {
                    setPowerNone();
                }
            }
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
                // System.out.println("is Up:  " + isPressingW);
                // System.out.println("is Down:  " + isPressingS);
            }
        }
        /*forceDespawnSet*/
        if (this.getStandEntity(this.getSelf()) != null) {
            if (this.getStandEntity(this.getSelf()).forceDespawnSet) {
                setPowerNone();
            }
        }
        if (this.getStandEntity(this.getSelf()) instanceof ManhattanTransferEntity ME) {
            if (ME.getHattanTarget() != 0 && switchShootingMode()){
                if(securityTicks < 1 && this.targetHattan != null && ME.hasLineOfSight(this.targetHattan)) {
                    tryPower(PowersManhattanTransfer.DEFLECT_PROJECTILE, true);
                    tryPowerPacket(PowersManhattanTransfer.DEFLECT_PROJECTILE);
                } else {
                    securityTicks--;
                }
            } else if (ME.getHattanTarget() == 0){
                setSecurityTicks(2);
            }
            StandEntity SE = this.getStandEntity(this.getSelf());
        }

        if(this.self != null && this.self.isUsingItem() && isPiloting()){
            this.self.stopUsingItem();
        }
        super.tickPower();
    }

    int securityTicks = 0;
    void setSecurityTicks(int st){securityTicks = st;}

    public void synchToCamera(){
        if (isPiloting()) {
            LivingEntity ent = getPilotingStand();
            if (ent != null) {
                ClientUtil.synchToCamera(ent);
            }
        }
    }
    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }
    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            AERO_TRANSFER_SKIN = 3,
            JOLLY_SKIN = 4,
            BRAZIL_SKIN = 5,
            RADIOACTIVE_SKIN = 6,
            POLLINATION_SKIN = 7,
            UFO_TRANSFER_SKIN = 8;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                AERO_TRANSFER_SKIN,
                JOLLY_SKIN,
                BRAZIL_SKIN,
                RADIOACTIVE_SKIN,
                POLLINATION_SKIN,
                UFO_TRANSFER_SKIN
        );
    }
    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        int $$1 = 0;
        int $$13 = 0;

        if (entity instanceof ManhattanTransferEntity ME) {
            LivingEntity ent = getPilotingStand();
            IEntityAndData entityAndData = ((IEntityAndData) ent);
            if(this.isClient() && this.currentHattanStatus == UNLOADED_HATTAN){
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
                    Entity TE = MainUtil.getTargetEntity(ent, 300, 10);
                    if (TE != null && !(TE instanceof StandEntity && !TE.isAttackable()) && !TE.isInvisible()) {
                        if (ME.isInRain()) {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.04 * configSpeed() * extraSpeedEmergencyHattan()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.4, $$13 * flyingSpeed * 2.5F * extraSpeedEmergencyHattan(), delta.z / 1.4);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.4, 0, delta.z / 1.4);
                                }
                            }
                        } else {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.06 * configSpeed() * extraSpeedEmergencyHattan()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.1, $$13 * flyingSpeed * 3F * extraSpeedEmergencyHattan(), delta.z / 1.1);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.1, 0, delta.z / 1.1);
                                }
                            }
                        }
                    } else {
                        if (!ME.isInRain()) {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.022 * configSpeed() * extraSpeedEmergencyHattan()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.6, $$13 * flyingSpeed * 2.7F * extraSpeedEmergencyHattan(), delta.z / 1.6);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.6, 0, delta.z / 1.6);
                                }
                            }
                        } else {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.012 * configSpeed() * extraSpeedEmergencyHattan()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 2.2, $$13 * flyingSpeed * 2F * extraSpeedEmergencyHattan(), delta.z / 2.2);
                                } else {
                                    entity.setDeltaMovement(delta.x / 2.2, 0, delta.z / 2.2);
                                }
                            }
                        }
                    }
                }else {
                    entity.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
        keyInputForManhattan();
    }

    public void keyInputForManhattan() {
        Options options = Minecraft.getInstance().options;
        if (isPiloting()) {
            if (options.keyUp.isDown()) {
                isPressingW = true;
                isPressingS = false;
            }
            if (options.keyDown.isDown()) {
                isPressingS = true;
                isPressingW = false;
            }
            if (options.keyLeft.isDown()) {
                isPressingA = true;
                isPressingD = false;
            }
            if (options.keyRight.isDown()) {
                isPressingD = true;
                isPressingA = false;
            }

        }
    }

    public boolean isPressingW = true;
    public boolean isPressingA = false;
    public boolean isPressingS = false;
    public boolean isPressingD = false;

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if(visionModeClient) {
            if (this.getStandEntity(this.getSelf()) != null && ent != null && !(ent instanceof RoadRollerEntity) && ent instanceof LivingEntity && entityAndData.roundabout$getTrueInvisibilityManhattan() > 0) {
                if (this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                    return true;
                }
            }
        }
        if(isPiloting()){
            if (this.getStandEntity(this.getSelf()) != null && ent != null && !(ent instanceof RoadRollerEntity) & ent instanceof LivingEntity && entityAndData.roundabout$getTrueInvisibilityManhattan() > 0) {
                if (this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                    return true;
                }
            }
        }
        if (ent instanceof ManhattanTransferEntity ME) {
            if (this.getSelf() == ME.getUser()) {
                if (this.isHoldingSneak()) {
                    return true;
                }
            }
        }
        if(this.switchShootingMode()) {
            if (targetHattan != null && ent == targetHattan) {
                if (this.isActive() && this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public LivingEntity targetHattan = null;

    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (ent instanceof ManhattanTransferEntity ME) {
            if (this.getSelf() == ME.getUser()) {
                if (this.isHoldingSneak()/* && !isPiloting()*/) {
                    return 65425;
                }
            }
        }
        if(this.switchShootingMode()) {
            if (targetHattan != null && ent == targetHattan) {
                if (this.isActive() && this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                    return 3407755;
                }
            }
        }
        return 29960;
    }

    @Override
    public int getDisplayPowerInventoryScale() {
        return 45;
    }
    @Override
    public int getDisplayPowerInventoryYOffset() {
        return 22;
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == ManhattanTransferEntity.ANIME_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.manhattan_part_6");
        } else if (skinId == ManhattanTransferEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.manhattan_manga_part_6");
        }else if (skinId == ManhattanTransferEntity.AERO_TRANSFER_SKIN) {
            return Component.translatable("skins.roundabout.manhattan_transfer.manhattan_aerosmith");
        }else if (skinId == ManhattanTransferEntity.JOLLY_SKIN){
                return Component.translatable(  "skins.roundabout.manhattan_transfer.manhattan_jolly");
        } else if (skinId == ManhattanTransferEntity.BRAZIL_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.brazilian_transfer");
        } else if (skinId == ManhattanTransferEntity.RADIOACTIVE_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.radioactive_transfer");
        } else if (skinId == ManhattanTransferEntity.POLLINATION_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.pollination_transfer");
        }else if (skinId == ManhattanTransferEntity.UFO_TRANSFER_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.ufotransfer");
        }


        return Component.translatable(  "skins.roundabout.manhattan_transfer.manhattan_part_6");
    }
    @Override
    public boolean isSecondaryStand() {
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.MANHATTAN_SUMMON_EVENT;
        } else if (soundChoice == MANHATTAN_RAIN) {
            return ModSounds.MANHATTAN_DODGING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.manual_shooting",
                "instruction.roundabout.press_skill", StandIcons.MANUAL_SHOOTING_OFF, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.control_mode",
                "instruction.roundabout.press_skill", StandIcons.CONTROL_MODE_ON, 2, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.wind_vision",
                "instruction.roundabout.press_skill", StandIcons.WIND_VISION_ON, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.wind_reading",
                "instruction.roundabout.passive_manhattan",  StandIcons.WIND_READING, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.bonus_damage",
                "instruction.roundabout.passive",  StandIcons.MANHATTAN_DAMAGE_BOOST, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0, "ability.roundabout.manhattan_dodge",
                "instruction.roundabout.press_skill",  StandIcons.MANHATTAN_DODGE, 3, level, bypass));
        return $$1;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        if (switchShootingMode()) {
            setSkillIcon(context, x, y, 1, StandIcons.MANUAL_SHOOTING_ON, PowerIndex.SKILL_1);
        }
        else
            setSkillIcon(context, x, y, 1, StandIcons.MANUAL_SHOOTING_OFF, PowerIndex.SKILL_1);

        if (isPiloting())
            setSkillIcon(context, x, y, 2, StandIcons.CONTROL_MODE_OFF, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.CONTROL_MODE_ON, PowerIndex.SKILL_2);

        if (visionModeClient) {
            setSkillIcon(context, x, y, 4, StandIcons.WIND_VISION_ON, PowerIndex.SKILL_4);
        }
        else
            setSkillIcon(context, x, y, 4, StandIcons.WIND_VISION_OFF, PowerIndex.SKILL_4);

        if(isPiloting()){
            setSkillIcon(context, x, y, 3, StandIcons.MANHATTAN_DODGE, PowersManhattanTransfer.MANHATTAN_DODGE);
        }
        else{
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        super.renderIcons(context, x, y);
    }
    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {
        if ($$0.is(ModDamageTypes.STAND) || $$0.is(ModDamageTypes.STAND_RUSH) || $$0.is(ModDamageTypes.PENETRATING_STAND)) {
            if ($$0.getEntity().getPosition(1).distanceTo(this.getSelf().getPosition(1)) < 6.0) {
                if (this.getSelf() instanceof Player P) {
                    if (this.getStandUserSelf().roundabout$getCombatMode()) {
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);
                    }
                }
            }
        }
    }
    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.DARK_PURPLE);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "14Kacper").withStyle(ChatFormatting.BLUE);
    }
    //COMMAND TO QUICKLY PUT MANHATTAN TRANSFER INTO ALL MOBS: /roundaboutSetStand @e manhattan_transfer 1 1 0 false

    /**Ignore*/
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        boolean isRangedAttackMob = this.getSelf() instanceof RangedAttackMob || this.getSelf() instanceof Blaze || this.getSelf() instanceof Ghast;
        if(isRangedAttackMob ){
            this.getSelf().playSound(SoundEvents.GHAST_HURT);
            this.getSelf().kill();
        } else {
        }
    }
}