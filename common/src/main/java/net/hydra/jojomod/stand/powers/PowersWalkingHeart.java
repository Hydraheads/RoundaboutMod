package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.projectile.CinderellaVisageDisplayEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.LuckyLipstickItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersWalkingHeart extends NewDashPreset {

    public PowersWalkingHeart(LivingEntity self) {
        super(self);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.WALKING_HEART.create(this.getSelf().level());
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersWalkingHeart(entity);
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        $$1.add((byte) 3);
        $$1.add((byte) 4);
        return $$1;
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                spikeAttackModeToggleClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH-> {
                extendHeels();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dashOrWallLatch();
            }
        }
    }


    public void extendHeels(){
        if (!this.onCooldown(PowerIndex.SKILL_3) || hasExtendedHeelsForWalking()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }


    public void dashOrWallLatch(){
        if (canLatchOntoWall())
            doWallLatchClient();
        else if (!hasExtendedHeelsForWalking())
            dash();
    }

    public void doWallLatchClient(){
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            tryPowerPacket(PowerIndex.POWER_3);
        }
    }



    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == CinderellaEntity.MANGA_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.manga");
        } if (skinId == CinderellaEntity.ZOMBIE_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.zombie");
        } if (skinId == CinderellaEntity.JACK_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.jack_in_the_box");
        } if (skinId == CinderellaEntity.BUSINESS_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.business");
        }
        return Component.translatable("skins.roundabout.cinderella.base");
    }

    public Direction heelDirection = Direction.DOWN;

    public Direction getHeelDirection(){
        return heelDirection;
    }
    public void setHeelDirection(Direction dir){
        heelDirection = dir;
    }

    public boolean hasExtendedHeelsForWalking(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canLatchOntoWall(){
        if ((this.self.onGround() && !hasExtendedHeelsForWalking()) || (!this.self.onGround() && hasExtendedHeelsForWalking()))
            return false;
        BlockPos pos1 = this.self.getOnPos();
        Direction gravdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();

        if (this.self.level().getBlockState(pos1).isSolid()){
            pos1 = pos1.relative(gravdir.getOpposite());
        }
        Direction rd = RotationUtil.getRealFacingDirection(this.self);
        if (rd == gravdir)
            return false;
        pos1 = pos1.relative(RotationUtil.getRealFacingDirection(this.self));
        if (this.self.level().getBlockState(pos1).isSolid()){
            return true;
        }
        return false;
    }

    public void regularExtendHeels(){
        boolean isAnchored = hasExtendedHeelsForWalking();
            if (isAnchored){
                if (!this.self.level().isClientSide()) {
                    toggleSpikes(false);
                }
            } else {
                if (self.onGround()){
                    this.setCooldown(PowerIndex.SKILL_3, 6);
                    if (!this.self.level().isClientSide()) {
                        setHeelDirection(Direction.DOWN);
                        toggleSpikes(true);
                    }
                }
            }
    }

    public void spikeAttackModeToggleClient(){
        if (!hasExtendedHeelsForWalking()) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);

            getStandUserSelf().roundabout$getStandPowers().tryPower(PowerIndex.NONE, true);
            tryPowerPacket(PowerIndex.NONE);
            ClientUtil.stopDestroyingBlock();
        }
    }

    public void wallLatch(){
        if (canLatchOntoWall()){
            this.setCooldown(PowerIndex.SKILL_3, 6);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WALL_LATCH_EVENT, SoundSource.PLAYERS, 1F, 1f);
                toggleSpikes(true);
                Direction gd = RotationUtil.getRealFacingDirection(this.self);
                setHeelDirection(gd);
                ((IGravityEntity) this.self).roundabout$setGravityDirection(gd);
                justFlippedTicks = 7;
            }
        }
    }

    public void grantFallImmunity(){
        if (ClientNetworking.getAppropriateConfig().walkingHeartSettings.fallProtectionOnRelease) {
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
        }
    }

    public void toggleSpikes(boolean toggle){
        if (!this.self.level().isClientSide()) {
            boolean getTog = getStandUserSelf().roundabout$getUniqueStandModeToggle();
            if (toggle != getTog) {
                if (toggle) {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, 1f);
                } else {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, 1.5F);
                }
            }
        }
        getStandUserSelf().roundabout$setUniqueStandModeToggle(toggle);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.SPIKE_ATTACK_MODE, PowerIndex.SKILL_1);

        if (hasExtendedHeelsForWalking())
            setSkillIcon(context, x, y, 2, StandIcons.GROUND_IMPLANT, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.GROUND_IMPLANT_OUT, PowerIndex.SKILL_2);
        if (canLatchOntoWall() || hasExtendedHeelsForWalking())
            setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK, PowerIndex.SKILL_3);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 3 && hasExtendedHeelsForWalking() && !canLatchOntoWall())
            return true;
        if (slot == 1 && hasExtendedHeelsForWalking())
            return true;
        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_WALKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (inCombatMode()) {
            return 0;
        }
        return super.inputSpeedModifiers(basis);
    }
    public boolean inCombatMode(){
        return getStandUserSelf().roundabout$getCombatMode();
    }

    public boolean switchModes(){
        if (getStandUserSelf().roundabout$getCombatMode()){
            getStandUserSelf().roundabout$setCombatMode(false);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.HEEL_STOMP_EVENT, 1F, 1.0F);
            }
        } else {
            getStandUserSelf().roundabout$setCombatMode(true);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.HEEL_RAISE_EVENT, 1F, 1.0F);
            }
        }

        return true;
    }

    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                regularExtendHeels();
            }
            case PowerIndex.POWER_3 -> {
                wallLatch();
            }
            case PowerIndex.POWER_4 -> {
                switchModes();
            }
        }
        return super.tryPower(move,forced);
    }

    public void tickPower() {
        if (this.self.level().isClientSide()) {
            if (hasExtendedHeelsForWalking() && !getStandUserSelf().rdbt$getJumping()){
                if (!self.onGround()) {
                    if (this.self.getDeltaMovement().y < 0){
                        this.self.setDeltaMovement(this.self.getDeltaMovement().add(0,-0.14,0));
                    }
                }
            }
        } else {
            if (hasExtendedHeelsForWalking()){
                if (justFlippedTicks > 0){
                    justFlippedTicks--;
                } else {
                    if (self.onGround()){
                        mercyTicks = 5;
                    } else {
                        Vec3 newVec = new Vec3(0,-0.2,0);
                        Vec3 newVec2 = new Vec3(0,-1.0,0);
                        Vec3 newVec4 = new Vec3(0,-0.5,0);
                        Vec3 newVec5 = new Vec3(0,-1.1,0);
                        newVec = RotationUtil.vecPlayerToWorld(newVec,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                        newVec2 = RotationUtil.vecPlayerToWorld(newVec2,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos2 = BlockPos.containing(self.getPosition(1).add(newVec2));
                        newVec4 = RotationUtil.vecPlayerToWorld(newVec4,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                        newVec5 = RotationUtil.vecPlayerToWorld(newVec5,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos5 = BlockPos.containing(self.getPosition(1).add(newVec5));
                        if (
                                (
                                        self.level().getBlockState(pos).isSolid()
                                                || self.level().getBlockState(pos2).isSolid()
                                                || self.level().getBlockState(pos4).isSolid()
                                                || self.level().getBlockState(pos5).isSolid()
                                )){
                            mercyTicks--;
                        } else {
                            mercyTicks = 0;
                        }
                    }
                    if (self.isSleeping() || (!self.onGround() && mercyTicks <= 0) || self.getRootVehicle() != this.self) {
                        heelDirection = Direction.DOWN;
                        if (((IGravityEntity) this.self).roundabout$getGravityDirection() != heelDirection){
                            grantFallImmunity();
                        }
                        toggleSpikes(false);
                        ((IGravityEntity) this.self).roundabout$setGravityDirection(heelDirection);
                        setHeelDirection(heelDirection);
                    }
                }

            } else {
                setHeelDirection(Direction.DOWN);
            }
        }
        super.tickPower();
    }

    public int justFlippedTicks = 0;
    public int mercyTicks = 0;

    @Override
    public void updateUniqueMoves() {

        super.updateUniqueMoves();
    }

    public static final byte VISAGE_NOISE = 104;
    public static final byte IMPALE_NOISE = 105;
    public boolean deface(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            playStandUserOnlySoundsIfNearby(IMPALE_NOISE, 27, false,false);
            this.animateStand(CinderellaEntity.DEFACE);
            this.poseStand(OffsetIndex.ATTACK);

            return true;
        }
        return false;
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.visage_creation",
                "instruction.roundabout.press_skill", StandIcons.CINDERELLA_MASK, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.face_removal",
                "instruction.roundabout.press_skill", StandIcons.CINDERELLA_SCALP,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.visages",
                "instruction.roundabout.passive", StandIcons.CINDERELLA_VISAGES,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.lucky_lipstick",
                "instruction.roundabout.passive", StandIcons.CINDERELLA_LIPSTICK,0,level,bypass));
        return $$1;
    }
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CinderellaEntity.PART_4_SKIN);
        $$1.add(CinderellaEntity.MANGA_SKIN);
        $$1.add(CinderellaEntity.ZOMBIE_SKIN);
        $$1.add(CinderellaEntity.JACK_SKIN);
        $$1.add(CinderellaEntity.BUSINESS_SKIN);
        return $$1;
    }


    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {;
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_2) {
            Entity TE = this.getTargetEntity(playerEntity, 5F);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        }
    }
}
