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
            STAND_BLOCKED = 78;
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
      /*  if () {
            setSkillIcon(context, x, y, 1, StandIcons.MANUAL_SHOOTING_ON, PowerIndex.SKILL_1);
        }
        else*/
            setSkillIcon(context, x, y, 1, StandIcons.MANUAL_SHOOTING_OFF, PowerIndex.SKILL_1);

        if (isPiloting())
            setSkillIcon(context, x, y, 2, StandIcons.CONTROL_MODE_OFF, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.CONTROL_MODE_ON, PowerIndex.SKILL_2);

        if (switchWindVisionToggle()) {
            setSkillIcon(context, x, y, 4, StandIcons.WIND_VISION_ON, PowerIndex.SKILL_4);
        }
        else
            setSkillIcon(context, x, y, 4, StandIcons.WIND_VISION_OFF, PowerIndex.SKILL_4);

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

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
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {

            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                toggleControlModeClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                if(!isPiloting()) {
                    dash();
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
            case PowerIndex.POWER_4 -> {
                return switchVision();
            }
        }
            return super.setPowerOther(move, lastMove);
    }
    public void switchVisionClient(){
        this.tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
        if (isClient() && switchWindVisionToggle()) {
            this.self.playSound(ModSounds.MANHATTAN_VISION_EVENT, 200F, 1.0F);
        }
    }
    public boolean switchVision(){
        if (isClient() && this.self instanceof Player PE) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!switchWindVisionToggle());
            if (switchWindVisionToggle()) {
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision").withStyle(ChatFormatting.DARK_GREEN), true);
            }
            else{
                PE.displayClientMessage(Component.translatable("text.roundabout.manhattan_transfer.wind_vision_off").withStyle(ChatFormatting.DARK_AQUA), true);
            }
        }
        return true;
    }
    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if(switchWindVisionToggle()) {
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
        return false;
    }
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        return 12379456;
    }
    public boolean switchWindVisionToggle(){
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
   // StandUser User = getUserData(this.self);
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
            BRAZIL_SKIN = 4,
            RADIOACTIVE_SKIN = 5,
            POLLINATION_SKIN = 6;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                AERO_TRANSFER_SKIN,
                BRAZIL_SKIN,
                RADIOACTIVE_SKIN,
                POLLINATION_SKIN
        );
    }
    public boolean returnFakeStandForHud(){
        return !hasStandActive(self);
    }
    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = getNewStandEntity();
        }
        if (displayStand != null) {
            if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
                displayStand = getNewStandEntity();
            }
        }
        if (displayStand != null) {
            displayStand.setSkin(((StandUser) self).roundabout$getStandSkin());
            displayStand.setAnimation(((StandUser) self).roundabout$getStandAnimation());
            displayStand.setIdleAnimation(((StandUser) self).roundabout$getIdlePos());
            displayStand.tickCount = self.tickCount;
            displayStand.setUser(self);
            displayStand.setupAnimationStates();
        }
        return displayStand;
    }
    public StandEntity displayStand = null;
    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        int $$1 = 0;
        int $$13 = 0;

        if (entity instanceof ManhattanTransferEntity ME) {
            LivingEntity ent = getPilotingStand();
            IEntityAndData entityAndData = ((IEntityAndData) ent);
            entity.xxa = kpi.leftImpulse;
            entity.zza = kpi.forwardImpulse;
            Vec3 vec32 = new Vec3(entity.xxa * walkingSpeed, 0, entity.zza * walkingSpeed);
            Vec3 delta = entity.getDeltaMovement();
            if (kpi.shiftKeyDown) {
                $$13--;
            }
            if (kpi.jumping) {
                $$13++;
            }
            if (ent != null) {
                Entity TE = MainUtil.getTargetEntity(ent, 300, 10);
                if (TE != null && !(TE instanceof StandEntity && !TE.isAttackable()) && !TE.isInvisible() && entityAndData.roundabout$getTrueInvisibilityManhattan() > 0) {
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
        }else if (skinId == ManhattanTransferEntity.AERO_TRANSFER_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.manhattan_aerosmith");
        } else if (skinId == ManhattanTransferEntity.BRAZIL_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.brazilian_transfer");
        } else if (skinId == ManhattanTransferEntity.RADIOACTIVE_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.radioactive_transfer");
        } else if (skinId == ManhattanTransferEntity.POLLINATION_SKIN){
            return Component.translatable(  "skins.roundabout.manhattan_transfer.pollination_transfer");
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