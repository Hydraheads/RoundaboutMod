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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.*;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.entity.stand.StandEntity;
import java.util.Arrays;
import java.util.List;

public class PowersManhattanTransfer extends NewDashPreset {
    public PowersManhattanTransfer(LivingEntity self) {
        super(self);
    }
    public static final byte
            STAND_BLOCKED = 78,


            MANHATTAN_VISION = 82,
            MANHATTAN_DODGE = 83;
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().manhattanTransferSettings.enableManhattanTransfer;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersManhattanTransfer(entity);
    }
   @Override
    public StandEntity getNewStandEntity() {
        byte skin = ((StandUser) this.getSelf()).roundabout$getStandSkin();
            if (((StandUser) this.getSelf()).roundabout$getStandSkin() == ManhattanTransferEntity.POLLINATION_SKIN) {
                return ModEntities.POLLINATION_TRANSFER.create(this.getSelf().level());
            }
            return ModEntities.MANHATTAN_TRANSFER.create(this.getSelf().level());
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        ClientUtil.fx.roundabout$onGUI(context);
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
            setSkillIcon(context, x, y, 3, StandIcons.MANHATTAN_DODGE, PowerIndex.GLOBAL_DASH);
        }
        else{
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        super.renderIcons(context, x, y);
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
                    manhattanDodge();
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
        this.tryPower(PowerIndex.POWER_1, true);
        tryPowerPacket(PowerIndex.POWER_1);
    }

    public boolean switchShootingOther(){
        if (!isClient() && this.self instanceof Player PE) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!switchShootingMode());
            if (!switchShootingMode()) {
                visionModeClient = true;
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision").withStyle(ChatFormatting.DARK_GREEN), true);
            }
            else{
                visionModeClient = false;
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision_off").withStyle(ChatFormatting.DARK_AQUA), true);
            }
        }
        return true;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {

            case PowersManhattanTransfer.MANHATTAN_DODGE -> {
              /*  this.setCooldown(PowersManhattanTransfer.MANHATTAN_DODGE,ClientNetworking.getAppropriateConfig().rattSettings.rattLeapCooldown);
                //Vec3 dir = this.getStandEntity(this.getSelf()).getViewVector(1);
                if (this.getStandEntity(this.getSelf()) != null) {
                    Vec3 dir = this.getStandEntity(this.getSelf()).getViewVector(1);
                    dir = dir.scale(3);
                    Vec3 vec3 = new Vec3(dir.x, dir.y, dir.z);
                    this.getStandEntity(this.getSelf()).setDeltaMovement(vec3);
                    //entity.setDeltaMovement(entity.getForward());
                }*/
              //  this.getStandEntity(this.getSelf()).level().playSound(null,this.getSelf().blockPosition(),ModSounds.VAMPIRE_DASH_EVENT, SoundSource.PLAYERS, 1F,1.2F);
            }
        }
        return super.tryPower(move, forced);
    }

    public void manhattanDodge() {
        if (!onCooldown(PowersManhattanTransfer.MANHATTAN_DODGE) && !isAttackIneptVisually(PowersManhattanTransfer.MANHATTAN_DODGE,4)) {
            tryPower(PowersManhattanTransfer.MANHATTAN_DODGE);
            tryPowerPacket(PowersManhattanTransfer.MANHATTAN_DODGE);
            if (isClient()) {
                this.self.playSound(ModSounds.VAMPIRE_DASH_EVENT, 200F, 1.0F);
            }
        }
    }

    public void switchVisionClient(){
        this.tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
        if (isClient() && visionModeClient) {
            this.self.playSound(ModSounds.MANHATTAN_VISION_EVENT, 200F, 1.0F);
        }
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
    public boolean highlightsEntity(Entity ent,Player player){
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if(visionModeClient) {
            if (this.getStandEntity(this.getSelf()) != null && ent != null && !(ent instanceof RoadRollerEntity) && ent instanceof LivingEntity && entityAndData.roundabout$getTrueInvisibilityManhattan() > 0) {
                if (this.getStandEntity(this.getSelf()).hasLineOfSight(ent) && !player.hasLineOfSight(ent)) {
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
        return false;
    }
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (ent instanceof ManhattanTransferEntity ME) {
            if (this.getSelf() == ME.getUser()) {
                if (this.isHoldingSneak() && !isPiloting()) {
                    return 12379556;
                }
            }
        }
        return 12379456;
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
    @Override
    public void tickPower() {
        super.tickPower();
        if (this.getStandEntity(this.getSelf()) != null) {
            Vec3 vec3 = new Vec3(walkingSpeed, 0, walkingSpeed);
            if (!isPiloting()) {
                if(this.getStandEntity(this.getSelf()).isInWaterOrRain()){
                    this.getStandEntity(this.getSelf()).setDeltaMovement(this.getStandEntity(this.getSelf()).getForward().scale(0.010 * configSpeed()));
                }
                else{
                this.getStandEntity(this.getSelf()).setDeltaMovement(this.getStandEntity(this.getSelf()).getForward().scale(0.022 * configSpeed()));
            }}
            if (isActive()) {
                DimensionType t = this.getStandEntity(this.getSelf()).level().dimensionType();
                DimensionType T = this.getSelf().level().dimensionType();

                if (this.getSelf().distanceTo(this.getStandEntity(this.getSelf())) > this.getMaxPilotRange() ) {
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

            }
        }
        /*forceDespawnSet*/
        if (this.getStandEntity(this.getSelf()) != null) {
            if (this.getStandEntity(this.getSelf()).forceDespawnSet) {
                setPowerNone();
            }
        }
        StandEntity SE = this.getStandEntity(this.getSelf());
    }
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
                                entity.setDeltaMovement(entity.getForward().scale(0.04 * configSpeed()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.6, $$13 * flyingSpeed * 2.5F, delta.z / 1.6);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.6, 0, delta.z / 1.6);
                                }
                            }
                        } else {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.06 * configSpeed()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.1, $$13 * flyingSpeed * 3F, delta.z / 1.1);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.1, 0, delta.z / 1.1);
                                }
                            }
                        }
                }
                else {
                        if (!ME.isInRain()) {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.022 * configSpeed()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 1.6, $$13 * flyingSpeed * 2.7F, delta.z / 1.6);
                                } else {
                                    entity.setDeltaMovement(delta.x / 1.6, 0, delta.z / 1.6);
                                }
                            }
                        } else {
                            if (kpi.leftImpulse == 0 && kpi.forwardImpulse == 0) {
                                entity.setDeltaMovement(entity.getForward());
                                entity.setDeltaMovement(entity.getForward().scale(0.012 * configSpeed()));
                            } else {
                                if ($$13 != 0) {
                                    entity.setDeltaMovement(delta.x / 2.2, $$13 * flyingSpeed * 2F, delta.z / 2.2);
                                } else {
                                    entity.setDeltaMovement(delta.x / 2.2, 0, delta.z / 2.2);
                                }
                            }
                        }
                    }
            }
        }
    }

     /*   @Override
    public boolean dealWithProjectile(Entity ent, HitResult res){
        if (!ent.level().isClientSide()) {
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand) && stand instanceof ManhattanTransferEntity SE && this.self instanceof ServerPlayer PE) {
                if (SE.getScoping() && !onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                    if (!hasBlock() && !hasEntity() &&
                            ((StandUser) this.getSelf()).roundabout$getActivePower() == PowerIndex.GUARD) {
                        boolean success = false;
                        if (ent instanceof AbstractArrow AA) {
                            ItemStack ii = ((IAbstractArrowAccess)ent).roundabout$GetPickupItem();
                            if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            } else if (ent instanceof RattDartEntity RD) {
                                success = true;
                                RD.applyEffect(this.getSelf());
                            } else if (ent instanceof RoundaboutBulletEntity BE) {
                                success = true;
                                SE.canAcquireHeldItem = true;
                                ItemStack bulletItem = BE.getBulletItemStack();
                                SE.setHeldItem(bulletItem);
                            }
                        } else if (ent instanceof ThrownAnubisEntity TAE) {
                            success = true;
                            SE.canAcquireHeldItem = true;
                            ItemStack anubisItem = TAE.getItem();
                            SE.setHeldItem(anubisItem);
                        } else if (ent instanceof ThrownObjectEntity TO) {
                            ItemStack ii = TO.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (TO.places) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            }
                        } else if (ent instanceof ThrownPotion TP) {
                            ItemStack ii = TP.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (TP.getOwner() == null || TP.getOwner() instanceof Player) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            }
                        }

                        if (success){
                            int cdr = ClientNetworking.getAppropriateConfig().starPlatinumSettings.guardianCooldown;
                            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                                    PowerIndex.SKILL_EXTRA_2, cdr);
                            ((StarPlatinumEntity) stand).setScoping(false);
                            if (ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumScopeUsesPotionEffectForNightVision) {
                                MobEffectInstance ME = this.getSelf().getEffect(MobEffects.NIGHT_VISION);
                                if (ME != null && ME.getDuration() >= 100000 && ME.getAmplifier() > 20) {
                                    this.getSelf().removeEffect(MobEffects.NIGHT_VISION);
                                }
                            }
                            this.setCooldown(PowerIndex.SKILL_EXTRA_2, cdr);
                            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ITEM_CATCH_EVENT, SoundSource.PLAYERS, 1.7F, 1.2F);
                            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 1.7F, 0.5F);
                            poseStand(OffsetIndex.FOLLOW_NOLEAN);
                            if (MainUtil.isThrownBlockItem(SE.getHeldItem().getItem())) {
                                animateStand(StandEntity.BLOCK_GRAB);
                            } else {
                                animateStand(StandEntity.ITEM_GRAB);
                            }

                            ((ServerLevel) this.self.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    ent.getX(), ent.getY(), ent.getZ(),
                                    0, 0, 0, 0, 0);
                            return true;
                        }
                    }
                }
            }
        }
        return super.dealWithProjectile(ent,res);
    }*/

    /**Ignore this, it's for later :)    */

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
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.MANHATTAN_SUMMON_EVENT;
            }
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
        return $$1;
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
    //COMMAND TO QUICKLY PUT MANHATTAN TRANSFER INTO ALL MOBS: /roundaboutSetStand @e manhattan_transfer 1 "from 1 to 5" 0 false

}