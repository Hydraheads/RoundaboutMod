package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.projectile.ColdBlastProjectile;
import net.hydra.jojomod.entity.projectile.GentlyWeepsEntity;
import net.hydra.jojomod.entity.projectile.IceTwisterEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PowersWhiteAlbum extends NewDashPreset {
    public PowersWhiteAlbum(LivingEntity self) {
        super(self);
    }
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.enableWhiteAlbum;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersWhiteAlbum(entity);
    }

    public boolean freezeImmune(){
        return hasStandActive(self) || super.freezeImmune();
    }

    public static final byte ICE_CHARGE = 80;

    /**returns if you are using stand guard*/
    public boolean isGuardInput(){
        return isChargingCold();
    }
    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }

    public boolean skatesActive = false;
    public boolean hasSkatesActivated(){
        return skatesActive && PowerTypes.hasStandActive(self);
    }


    @Override
    public boolean isBrawling(){
        return fistsOut;
    }
    @Override
    public boolean interceptAttack(){
        return fistsOut;
    }

    @Override
    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return fistsOut;
    }

    @Override
    public void onLandingAnimatedJump(){
        if (hasSkatesActivated()){
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATING_LAND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
    }

    @Override
    public float guardSpecialties(DamageSource sauce, float damage){
        if (sauce.is(DamageTypes.PLAYER_ATTACK)){
            if (self instanceof Player){
                damage*=ClientNetworking.getAppropriateConfig().whiteAlbumSettings.playerGuardDamageMultiplierv2;
            } else {
                damage*=ClientNetworking.getAppropriateConfig().whiteAlbumSettings.playerGuardDamageMultiplierMobs;
            }
        }
        if (sauce.is(DamageTypes.MOB_ATTACK)){
            damage*=ClientNetworking.getAppropriateConfig().whiteAlbumSettings.mobGuardDamageMultiplierv2;
        }

        if (MainUtil.isStandDamage(sauce)){
            damage*=2F;
        }
        if (sauce.is(ModDamageTypes.BULLET) || sauce.getDirectEntity() instanceof Projectile ||
                sauce.getEntity() instanceof Blaze){
            damage*=0.5F;
        }
        if (sauce.is(DamageTypes.ARROW) && sauce.getEntity() instanceof Player){
            damage*=0.4F;
        }
        return super.guardSpecialties(sauce,damage);
    }

    public void onChangedFrozenWater(BlockPos blockPos, int j){
        BlockState blockState = ModBlocks.WHITE_ALBUM_ICE_BLOCK.defaultBlockState();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, -1, -j), blockPos.offset(j, -1, j))) {
            BlockState blockState3;
            if (!blockPos2.closerToCenterThan(self.position(), j)) continue;
            mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = self.level().getBlockState(mutableBlockPos);
            if (!blockState2.isAir() || (blockState3 = self.level().getBlockState(blockPos2)) != FrostedIceBlock.meltsInto()
                    || !blockState.canSurvive(self.level(), blockPos2) ||
                    !self.level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
            self.level().setBlockAndUpdate(blockPos2, blockState);
            self.level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_BLOCK, Mth.nextInt(self.getRandom(), 110, 130));
        }
    }
    @Override
    public void onChangedBlock(BlockPos blockPos){
        if (self instanceof Animal ah && ah.getPassengers() != null && !ah.getPassengers().isEmpty() &&
                ah.getPassengers().get(0) instanceof Player) {
            onChangedFrozenWater(blockPos,3);
        } else if (hasSkatesActivated() && acceleration > 0 && !self.isSwimming() &&
        !self.isFallFlying()) {
            boolean canFreezeGrass = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezesGrassv2;
            boolean canFreezeSnow = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezesSnow;
            if (!self.onGround()) {
                return;
            }
            onChangedFrozenWater(blockPos,3);

            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int j = 2;
            BlockState blockState = ModBlocks.WHITE_ALBUM_ICE_SLAB.defaultBlockState();
            for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, 0, -j), blockPos.offset(j, 0, j))) {

                mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = self.level().getBlockState(mutableBlockPos);
                BlockState blockState3 =self.level().getBlockState(mutableBlockPos.below());
                if (!blockState.canSurvive(self.level(), blockPos2) ||
                        !self.level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
                    if (blockState3.isAir() ||
                        (MainUtil.getIsGamemodeApproriateForGrief(self) &&
                                (canFreezeGrass || (blockState3.is(Blocks.SNOW) && canFreezeSnow))
                                && blockState3.canBeReplaced() &&
                                !(blockState3.getBlock() instanceof LiquidBlockContainer)&&
                                !(blockState3.getBlock() instanceof FireBlock)&&
                                !(blockState3.getBlock() instanceof StickyIceCoatingBlock)&&
                                !(blockState3.getBlock() instanceof ColdAirBlock)&&
                                !(blockState3.getBlock() instanceof StandFireBlock)
                                &&
                        !blockState3.liquid() &&
                        !(blockState3.hasProperty(BlockStateProperties.WATERLOGGED) &&
                                blockState3.getValue(BlockStateProperties.WATERLOGGED)
                                )
                        )
                ) {
                        self.level().setBlockAndUpdate(blockPos2, blockState);
                        self.level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_SLAB, Mth.nextInt(self.getRandom(), 110, 130));
                }
            }
        }
    }

    @Override
    public boolean surpassesFire(){
        if (((StandUser)self).roundabout$getStandPowers() instanceof
                PowersWhiteAlbum PWA &&
                !(((StandUser)self).roundabout$getGuardBroken())
                && hasStandActive(self)
        ){
            return true;
        }
        return false;
    }

    public boolean isChargingCold(){
        return (activePower == PowerIndex.EXTRA);
    }
    public int stallTicks = 0;
    /**When you take damage, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptIncomingHarm(DamageSource $$0, float $$1){
        if (!self.level().isClientSide() && hasStandActive(self)) {

            //Even if you block the attack, your ice blast should be canceled bc it's insane
            if (isChargingCold()){
                if ($$0.getEntity() instanceof LivingEntity LE && self instanceof Player player){
                    S2CPacketUtil.sendSimpleByteToClientPacket(player,PacketDataIndex.STALL);
                    ((StandUser)self).roundabout$tryPower(PowerIndex.NONE,true);
                }
            }

            if (self instanceof Player pl) {
                if ($$0.is(DamageTypes.FALL) ||
                        ($$0.getEntity() != null &&
                                ($$0.is(DamageTypes.MOB_ATTACK) || $$0.is(DamageTypes.PLAYER_ATTACK)))
                ) {
                    if (!self.level().isClientSide) {
                        S2CPacketUtil.sendGenericIntToClientPacket(pl,
                                PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION, 0);
                    }
                    acceleration = 0;
                }
            }

            StandUser user = getStandUserSelf();
            if (!user.roundabout$getGuardBroken()) {
                if ($$0.is(DamageTypes.FALL)) {
                    if ($$1 > ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints) {
                        user.roundabout$breakGuard();
                        this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                SoundSource.PLAYERS, 1F, 1.5F);
                        if (self instanceof Player player){
                            player.getCooldowns().addCooldown(Items.SHIELD, 100);
                        }
                    } else {
                        user.roundabout$damageGuard($$1);
                        if (!user.roundabout$getGuardBroken()) {
                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_GUARD_SOUND_EVENT,
                                    SoundSource.PLAYERS, 0.8F, 1.6F + self.level().random.nextFloat() * 0.3f);
                        } else {
                            this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                    SoundSource.PLAYERS, 1F, 1.5F);
                            if (self instanceof Player player){
                                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            }
                        }

                        return true;
                    }
                } else {
                    if ($$0.is(DamageTypes.CACTUS) ||
                            $$0.is(DamageTypes.STALAGMITE) ||
                            $$0.is(DamageTypes.SWEET_BERRY_BUSH) ||
                            $$0.is(DamageTypes.LAVA) ||
                            $$0.is(DamageTypes.IN_FIRE)||
                            $$0.is(DamageTypes.ON_FIRE)
                    ){
                        $$1*=0.05F;
                        if ($$1 > ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints) {
                            user.roundabout$breakGuard();
                            this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                    SoundSource.PLAYERS, 1F, 1.5F);
                            if (self instanceof Player player){
                                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            }
                        } else {
                            user.roundabout$damageGuard($$1);
                            if (user.roundabout$getGuardBroken()) {
                                this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                        SoundSource.PLAYERS, 1F, 1.5F);
                                if (self instanceof Player player){
                                    player.getCooldowns().addCooldown(Items.SHIELD, 100);
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void fixThis(){
        //Roundabout.LOGGER.info("2");
        if (!self.level().isClientSide()) {
            //Roundabout.LOGGER.info("3");
            if (hasSkatesActivated()) {
                //Roundabout.LOGGER.info("4");
                if (acceleration >= getMaxAccelerationTicks()) {
                    //Roundabout.LOGGER.info("5");
                    setPlayerPos(PlayerPosIndex.SKATE_TWIRL);
                    twirlTicks = 20;
                } else {
                    //Roundabout.LOGGER.info("6");
                    if (getPlayerPos() != PlayerPosIndex.SKATE_TWIRL) {
                        //Roundabout.LOGGER.info("7");
                        setPlayerPos(PlayerPosIndex.SKATE_JUMP);
                    }
                }
            }
        }
    }
    @Override
    public void onJump(){
        fixThis();
    }

    @Override
    public boolean cancelSprintJump(){
        if (hasSkatesActivated()){
            if (acceleration < getMaxAccelerationTicks()){
                return true;
            }
        } else {
            if (isChargingCold()){
                return true;
            }
        }
        return super.cancelSprintJump();
    }

    public int acceleration = 0;
    public float inputSpeedModifiers(float basis){
        if (hasSkatesActivated()){
            basis *= 1.3f+(acceleration*ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumAccelerationAmountv3);
        } if (isChargingCold()){
            basis *= 0.9f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 80) {
                HeatUtil.addHeat(self,-99);
            }
        }
    }

    public boolean canGuard(){
        return !this.isBarraging() && isBrawling() && stallTicks <= 0;
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard() && !isChargingCold()) {
            ((StandUser)this.getSelf()).roundabout$tryPowerP(PowerIndex.EXTRA,true);
            tryPowerPacket(PowerIndex.EXTRA);
            return true;
        }
        return false;
    }

    public boolean isBlockingTraditionally() {
        if (this.self.isUsingItem() && !this.self.getUseItem().isEmpty()) {
            Item $$0 = this.self.getUseItem().getItem();
            if ($$0.getUseAnimation(this.self.getUseItem()) != UseAnim.BLOCK) {
                return false;
            } else {
                return $$0.getUseDuration(this.self.getUseItem()) - this.self.getUseItemRemainingTicks() >= 5;
            }
        } else {
            return false;
        }
    }

    /**for stands that subvert guard mechanics like white album*/
    @Override
    public boolean isSpecialGuarding(){
        return !isBlockingTraditionally() &&
                ((self instanceof Player || MainUtil.isHumanoid2(self)) && hasStandActive(self))
                && !isBarraging();
    }

    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints;
    }

    @Override
    public float regenBrokenGuard(){
        return getStandUserSelf().roundabout$getMaxGuardPoints() / 200;
    }
    @Override
    public float regenGuard(){
        return getStandUserSelf().roundabout$getMaxGuardPoints() / 440;
    }

    @Override
    public boolean forceCrit(){
        return acceleration >= getMaxAccelerationTicks() || super.forceCrit();
    }

    private static final int MAX_ICE_SNAP_RADIUS = 30;
    int lastAcceleration = 0;
    double lastY = 0;
    @Override
    public void tickPower() {
        if (stallTicks > 0){
            stallTicks--;
        }
        if (!self.level().isClientSide()) {
            if (hasSkatesActivated() && self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vf &&
                    vf.isPlantedInWall()){
                toggleSkates();
            }

            ticklIceEntities();
            boolean broken = !getStandUserSelf().roundabout$shieldNotDisabled() || getStandUserSelf().roundabout$getGuardBroken();
            if (cracked){
                if (!broken) {
                    cracked = false;
                    saveDiscAndSync();
                }
            } else {
                if (broken) {
                    cracked = true;
                    saveDiscAndSync();
                }

                if (PowerTypes.hasStandActive(self)){
                if (self.level() instanceof ServerLevel sl) {
                    if ((self instanceof Mob mb && !MainUtil.isHumanoid2(mb)) ||  getStandUserSelf().roundabout$getStandSkin() == YUKI){
                        if (self.tickCount % 10 == 0) {
                            sl.sendParticles(
                                    ParticleTypes.SNOWFLAKE,
                                    self.getEyePosition().x,
                                    self.getEyePosition().y,
                                    self.getEyePosition().z,
                                    1,     // count
                                    0.5,    // x spread
                                    0.2,    // y spread
                                    0.5,    // z spread
                                    0.01    // speed
                            );
                        }
                    }
                }
                }
            }
            if (lastY < self.getY() && !self.onGround() && !self.isInWater() && !self.isSwimming()) {
                fixThis();
            }

            if (startIceSnapRing > 0 && self.tickCount % 3 == 0) {
                if (hasSkatesActivated()){
                    startIceSnapRing = 0;
                } else {

                    BlockPos center = iceCenter;
                    int r = startIceSnapRing;

                    // Check the square perimeter at radius r
                    processRing(center, r);

                    startIceSnapRing++;

                    if (startIceSnapRing > MAX_ICE_SNAP_RADIUS) {
                        startIceSnapRing = 0;
                    }
                }
            }
        }
        if (isPacketPlayer()){
            playDing();
            lastAcceleration = acceleration;
            if (hasSkatesActivated()){
                if (self.isInWater() || self.hurtTime > 0 || self.isUsingItem()
                || !self.isSprinting() || self.isSwimming()) {
                    acceleration = 0;
                } else if (!self.onGround()) {
                    if (lastY < self.getY()){
                        acceleration = 0;
                    } else {
                        if (acceleration > 10){
                            if (self.getDeltaMovement().y > -0.3F) {
                                if (self.level().getBlockState(self.getOnPos().below()).isSolid()) {
                                    self.setDeltaMovement(self.getDeltaMovement().add(0, -0.3F, 0));
                                    acceleration = Math.min(getMaxAccelerationTicks(), acceleration + 5);
                                }
                            }
                            acceleration = Math.min(getMaxAccelerationTicks(),acceleration+5);
                        }
                    }
                } else {
                    if (!self.horizontalCollision){
                        if (lastY < self.getY()){
                            acceleration = Math.max(0,acceleration-25);
                        } else {
                            acceleration = Math.min(getMaxAccelerationTicks(),acceleration+1);
                        }
                    } else {
                        acceleration = Math.max(0,acceleration-15);
                    }
                }

            } else {
                acceleration = 0;
            }
            if (acceleration != lastAcceleration){
                C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION,acceleration);
            }
        }
        if (self.onGround()){
            lastY = self.getY();
        }
        super.tickPower();
    }

    public void setAcceleration(int num){
        byte pos = getPlayerPos();
        acceleration = num;
        if (num > 0){
            if (pos != PlayerPosIndex.SKATE_JUMP &&
                    pos != PlayerPosIndex.SKATE_TWIRL){
                setPlayerPos(PlayerPosIndex.SKATE_GENERAL);
            }
        } else {
            if (pos == PlayerPosIndex.SKATE_GENERAL){
                setPlayerPos(PlayerPosIndex.NONE);
            }
        }
    }
    private void processRing(BlockPos center, int r) {

        Level level = self.level();

        for (int y = -4; y <= 4; y++) {

            for (int x = -r; x <= r; x++) {
                removeIce(level, center.offset(x, y, -r));
                removeIce(level, center.offset(x, y, r));
            }

            for (int z = -r + 1; z <= r - 1; z++) {
                removeIce(level, center.offset(-r, y, z));
                removeIce(level, center.offset(r, y, z));
            }
        }
    }
    private void removeIce(Level level, BlockPos pos) {

        BlockState state = level.getBlockState(pos);

        if (state.is(ModBlocks.WHITE_ALBUM_ICE_SLAB) || state.is(ModBlocks.STICKY_ICE)
                || state.is(ModBlocks.COLD_AIR)) {

            level.destroyBlock(pos, false);
            // or:
            // level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else if (state.is(ModBlocks.WHITE_ALBUM_ICE_BLOCK)) {
            WhiteAlbumIceBlock.melt2(state,level,pos);
            // or:
            // level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public static int getMaxAccelerationTicks(){
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumMaxAcceleration;
    }
    /**Add Knockback to attacks when appropriate. This replaces the knockback enchant if it is
     * higher than the knockback enchant*/
    public int bufferAttackKnockbackLevel(){
        if (hasSkatesActivated()) {
            if (acceleration >= getMaxAccelerationTicks()) {
                return 3;
            } else if (acceleration > getMaxAccelerationTicks() * 0.75) {
                return 2;
            } else if (acceleration > getMaxAccelerationTicks() * 0.5) {
                return 1;
            }
        }
        return 0;
    }
    @Override
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (self instanceof Player pl) {
            if (!self.level().isClientSide) {
                S2CPacketUtil.sendGenericIntToClientPacket(pl,
                        PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION, 0);
            }
            acceleration = 0;
        }

        return false;
    }

    public ResourceLocation getIconYes(int slot){
        if (slot == 4 && acceleration >= getMaxAccelerationTicks())
            return StandIcons.SQUARE_GOLD;
        return super.getIconYes(slot);
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons

        if (!isHoldingSneak()){
            if (fistsOut){
                setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT_2, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.SKILL_4);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.FREEZE_CANCEL, PowerIndex.SKILL_1_SNEAK);
        }

        if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 2, StandIcons.TWISTER, PowerIndex.SKILL_2, getTwisterLevel());
        } else {
            LockedOrNot(context, x, y, 2, StandIcons.GENTLY_WEEPS, PowerIndex.SKILL_2_SNEAK,getGentlyWeepsLevel());
        }


        if (!isHoldingSneak()){
            if (hasSkatesActivated()){
                LockedOrNot(context, x, y, 3, StandIcons.ICE_WALL_BEHIND, PowerIndex.SKILL_3, getIceWallLevel());
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        } else {
            LockedOrNot(context, x, y, 3, StandIcons.ICE_WALL, PowerIndex.SKILL_3,getIceWallLevel());
        }

        if (!isHoldingSneak()){
            if (hasSkatesActivated()){
                LockedOrNot(context, x, y, 4, StandIcons.SKATE_ACTIVE, PowerIndex.SKILL_1, getSkatesLevel());
            } else {
                LockedOrNot(context, x, y, 4, StandIcons.SKATE_INACTIVE, PowerIndex.SKILL_1, getSkatesLevel());
            }
        } else {
            LockedOrNot(context, x, y, 4, StandIcons.FREEZE_BLOCKS, PowerIndex.SKILL_4_SNEAK, getBlockFreezeLevel());
        }

        super.renderIcons(context, x, y);
    }


    public void toggleBlockFreezeClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && !self.isInWater()
        && canExecuteMoveWithLevel(getBlockFreezeLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
    }


    public boolean renderHelmet(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }

    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }

    @Override

    public void tickPowerEnd() {
        //Roundabout.LOGGER.info("skates: "+skatesActive);
    }

    public boolean cracked = false;
    public boolean fistsOut = false;
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("skatesActive",skatesActive);
        $$0.putBoolean("cracked",cracked);
        $$0.putBoolean("fistsOut",fistsOut);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("skatesActive")) {
            skatesActive = $$0.getBoolean("skatesActive");
        } if ($$0.contains("cracked")) {
            cracked = $$0.getBoolean("cracked");
        } if ($$0.contains("fistsOut")) {
            fistsOut = $$0.getBoolean("fistsOut");
        }
    }


    public void iceWallClient(){
        if (!this.onCooldown(PowerIndex.SKILL_3) && canExecuteMoveWithLevel(getIceWallLevel())) {
            if (canUseIceWall()) {
                tryPowerPacket(PowerIndex.POWER_3);
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            }
        }
    }


    public void toggleSkatesClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1) && canExecuteMoveWithLevel(getSkatesLevel())) {
           ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
           tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL-> {
                toggleFistsClient();
            }
            case SKILL_1_CROUCH-> {
                iceCancelClient();
            }
            case SKILL_2_NORMAL-> {
                iceTwisterClient();
            }
            case SKILL_2_CROUCH-> {
                gentlyWeepsClient();
            }
            case SKILL_3_NORMAL -> {
                dashOrWall();
            }
            case SKILL_3_CROUCH -> {
                iceWallClient();
            }
            case SKILL_4_NORMAL -> {
                toggleSkatesClient();
            }
            case SKILL_4_CROUCH -> {
                toggleBlockFreezeClient();
            }
        }
    }

    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_2 || num == PowerIndex.SKILL_2_SNEAK) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void iceTwisterClient(){
        if (!onCooldown(PowerIndex.SKILL_2) && !isChargingCold() && canExecuteMoveWithLevel(getTwisterLevel())){
            BlockHitResult hit = (BlockHitResult) self.pick(
                    5.0D, // reach distance
                    0.0F,
                    false
            );

            tryBlockPosPowerPacket(PowerIndex.POWER_2,hit.getBlockPos());
        }
    }

    public void gentlyWeepsClient(){
        if (!onCooldown(PowerIndex.SKILL_2_SNEAK) && !isChargingCold()
                && canExecuteMoveWithLevel(getGentlyWeepsLevel())){
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }
    @Override
    public float getBonusPassiveMiningSpeed(){
        return 1f + ClientNetworking.getAppropriateConfig().whiteAlbumSettings.miningSpeedBuffWhite;
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                whiteAlbumSettings.miningSpeedMultiplierWhiteAlbum *0.01));
    }


    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.getMiningTierWhiteAlbum;
    }

    @Override
    public float getPickMiningSpeed() {
        return 12F;
    }

    @Override
    public float getDamageAdd(DamageSource source, float amt, Entity target){
        if (PowerTypes.hasStandActive(self)) {
            if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)) {
                if (target instanceof Player pl) {
                    return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.bonusPlayerDMGWhite;
                } else {
                    return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.bonusMobDMGWhite;
                }
            }
        }
        return super.getDamageAdd(source,amt,target);
    }



    public void dashOrWall(){
        if (hasSkatesActivated()){
            if (!this.onCooldown(PowerIndex.SKILL_3) && canExecuteMoveWithLevel(getIceWallLevel())) {
                if (canUseIceWall()) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                    tryPowerPacket(PowerIndex.POWER_3_BLOCK);
                }
            }
        } else {
            dash();
        }
    }

    public void iceTwister(){
        BlockPos pos = twisterPos;
        if (pos == null) {
            return;
        }

        if (!onCooldown(PowerIndex.SKILL_2)) {

            if (self instanceof Mob){
                this.setCooldown(PowerIndex.SKILL_2, 320);
            } else {
                this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().whiteAlbumSettings.twisterCooldownv2);
            }
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 40);
            Level level = self.level();

            BlockPos checkPos = pos;

            while (checkPos.getY() > level.getMinBuildHeight()) {
                BlockState state = level.getBlockState(checkPos);

                boolean replaceable = state.canBeReplaced();
                boolean liquid = !state.getFluidState().isEmpty();

                if (!replaceable && !liquid || checkPos.getCenter().distanceTo(self.position()) > 7) {
                    // Found ground
                    BlockPos twisterPos = checkPos.above();

                    // Spawn twister here
                    addEXP(1);
                    IceTwisterEntity twister = new IceTwisterEntity(
                            this.self.level(), twisterPos.getCenter().subtract(0, 0.5F, 0));
                    addIceEntity(twister);
                    this.getSelf().level().addFreshEntity(twister);
                    twister.lifeSpan = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.twisterLifespan;
                    break;
                }

                checkPos = checkPos.below();
            }
        }
    }


    public void gentlyWeeps(){
        if (!onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            BlockPos pos = self.getOnPos();
            if (self.level().getBlockState(pos).is(ModBlocks.WHITE_ALBUM_ICE_SLAB)){
                pos = pos.below();
            }
            this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().whiteAlbumSettings.gentlyWeepsCooldown);
            this.setCooldown(PowerIndex.SKILL_2, 40);

            Level level = self.level();
            addEXP(3);
            GentlyWeepsEntity twister = new GentlyWeepsEntity(
                    level, pos.getCenter().add(0, 0.5F, 0));
            addIceEntity(twister);
            level.addFreshEntity(twister);
            twister.lifeSpan = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.gentlyWeepsLifespan;
        }
    }

    public void toggleFistsClient(){
        if (!onCooldown(PowerIndex.SKILL_4) && !isChargingCold()){
            this.setCooldown(PowerIndex.SKILL_4, 9);
            tryPowerPacket(PowerIndex.POWER_1_BONUS);
        }
    }

    public void iceCancelClient(){
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)){
            this.setCooldown(PowerIndex.SKILL_1, 40);
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 40);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }


    public int startIceSnapRing = 0;
    public BlockPos iceCenter = BlockPos.ZERO;
    public void iceCancelServer(){
        this.setCooldown(PowerIndex.SKILL_1, 40);
        if (!this.self.level().isClientSide()){
            if (hasSkatesActivated()) {
                toggleSkates();
            }
            clearAllIceEntities();
            iceCenter = self.blockPosition();
            startIceSnapRing = 1;

            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SNAP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
    }



    public HitResult pick(double $$0, float $$1, boolean $$2) {
        Vec3 $$3 = this.self.getEyePosition($$1);
        Vec3 $$4 = this.self.getViewVector($$1);
        Vec3 $$5 = $$3.add($$4.x * $$0, $$4.y * $$0, $$4.z * $$0);
        return this.self.level().clip(new ClipContext($$3, $$5, ClipContext.Block.COLLIDER, $$2 ? net.minecraft.world.level.ClipContext.Fluid.ANY : net.minecraft.world.level.ClipContext.Fluid.NONE, self));
    }

    public boolean canUseIceWall(){

        HitResult hit = pick(2, 0.0F, false);

        BlockPos centerPos;

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;

            // Spawn on top of the block you are looking at
            centerPos = blockHit.getBlockPos().relative(blockHit.getDirection());
        } else {
            // Fallback: 2 blocks in front of player
            centerPos = self.blockPosition().relative(self.getDirection(), 2);
        }

        for (var i = 0; i < 5; i++) {
            if (self.level().getBlockState(centerPos.below(i)).isSolid()){
                return true;
            }
        }
        return false;
    }


    public int placePhase = 0;
    BlockPos storeCenter = BlockPos.ZERO;
    Direction sideX = Direction.UP;
    public void iceWallServer(boolean special){
        int cooldown = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.iceWallCooldown;
        this.setCooldown(PowerIndex.SKILL_3, cooldown);
        if (!this.self.level().isClientSide()){

            BlockPos centerPos;
            Direction facing = self.getDirection();

            if (special){
                centerPos = self.getOnPos().relative(facing.getOpposite()).relative(facing.getOpposite());
            } else {

                HitResult hit = pick(2, 0.0F, false);
                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHit = (BlockHitResult) hit;

                    // Spawn on top of the block you are looking at
                    centerPos = blockHit.getBlockPos().relative(blockHit.getDirection());
                } else {
                    // Fallback: 2 blocks in front of player
                    centerPos = self.blockPosition().relative(self.getDirection(), 2);
                }
            }

            boolean isSafe = false;
            for (var i = 0; i < 5; i++) {
                if (self.level().getBlockState(centerPos.below(i)).isSolid()){
                    centerPos = centerPos.below(i);
                    isSafe = true;
                    i = 10;
                }
            }

            if (isSafe) {
                centerPos = new BlockPos(centerPos.getX(), Math.min(centerPos.getY(), self.blockPosition().getY()),
                        centerPos.getZ());

// Left/right axis
                Direction side = facing.getClockWise();
                sideX = side;
                storeCenter = centerPos;
                for (int width = -1; width <= 1; width++) {
                    for (int height = 0; height < 2; height++) {

                        BlockPos spawnPos = centerPos
                                .relative(side, width)
                                .above(height);

                        Vector3f newVec = new Vector3f((float) (spawnPos.getX()+0.5),
                                (float) (spawnPos.getY()),
                                (float) (spawnPos.getZ() + 0.5)).add(0, -1, 0);

                        BlockWallEntity wall =
                                // slightly off to not z-fight
                                new BlockWallEntity(
                                        self.level(),
                                        newVec.x,
                                        newVec.y,
                                        newVec.z,
                                        Blocks.PACKED_ICE.defaultBlockState()
                                );
                        wall.setDataFinalPos(newVec.add(0, 2, 0));
                        wall.timing = 200;
                        wall.tsmove = true;
                        wall.canGrief = MainUtil.getIsGamemodeApproriateForGrief(self);
                        addIceEntity(wall);
                        self.level().addFreshEntity(wall);
                    }
                }
                addEXP(1);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.ICE_RISES_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
        }
    }

    public void toggleFists(){
        int cooldown = 9;
        this.setCooldown(PowerIndex.SKILL_4, cooldown);
        if (!this.self.level().isClientSide()){
            fistsOut = !fistsOut;
            if (fistsOut){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 0.9F, (float) (1.02 + (Math.random() * 0.06)));
            } else {
                //this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
            saveDiscAndSync();
        }
    }

    @Override
    public void onReleaseGuard(){
        boolean shoot = isChargingCold() && hasColdCharged();
        StandUser standComp = ((StandUser) self);
        standComp.roundabout$tryPower(PowerIndex.NONE,true);
        stallTicks = 3;
        if (standComp.roundabout$getActivePowerPhase() > 0 ) {
            standComp.roundabout$setInterruptCD(3);
        }
        if (shoot){
            tryPowerPacket(PowerIndex.EXTRA_2);
        } else {
            C2SPacketUtil.guardCancelPacket();
        }
    }


    public boolean toggleSkates(){
        int cooldown = 25;
        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL){
            if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vf &&
                    vf.isPlantedInWall()) {
                if (skatesActive) {
                    skatesActive = false;
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_EQUIP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                    saveDiscAndSync();
                }
            } else {
                skatesActive = !skatesActive;
                if (skatesActive) {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_EQUIP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                } else {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_RETRACT_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                }
                saveDiscAndSync();
            }
        }
        return true;
    }

    public static float getWhiteAlbumAmt(Entity entity,float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasWhiteAlbumOut = user.roundabout$getStandPowers() instanceof PowersWhiteAlbum pw && pw.renderHelmet();
            int whiteAlbumTicks = user.roundabout$getWhiteAlbumVanishTicks();
            if (hasWhiteAlbumOut || whiteAlbumTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    whiteAlbumTicks = 0;
                    user.roundabout$setWhiteAlbumVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasWhiteAlbumOut) {
                    heyFull = whiteAlbumTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = whiteAlbumTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }

    @Override
    public boolean tryTripleIntPower(int move, boolean forced, int chargeTime, int move2, int move3){

        return tryPower(move, forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return toggleSkates();
            }
            case PowerIndex.POWER_1_BONUS -> {
                toggleFists();
            }
            case PowerIndex.POWER_2 -> {
                iceTwister();
            }
            case PowerIndex.POWER_2_SNEAK -> {
                gentlyWeeps();
            }
            case PowerIndex.POWER_3 -> {
                iceWallServer(false);
            }
            case PowerIndex.EXTRA -> {
                setPowerColdBlast();
            }
            case PowerIndex.EXTRA_2 -> {
                setPowerColdBlastShot();
            }
            case PowerIndex.POWER_3_BLOCK -> {
                iceWallServer(true);
            }
            case PowerIndex.POWER_1_SNEAK -> {
                iceCancelServer();
            }
            case PowerIndex.POWER_4_SNEAK -> {
                freezeBlocksServer();
            }
        }
        return super.setPowerOther(move,lastMove);
    }


    public void setPowerColdBlast() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.EXTRA);
        if (!self.level().isClientSide()) {
            if (getPlayerPos2() != PlayerPosIndex.CHARGE_SHOT) {
                setPlayerPos2(PlayerPosIndex.CHARGE_SHOT);
                playSoundsIfNearby(ICE_CHARGE, 32, false, true);
            }
        }
    }

    public void setPowerColdBlastShot() {

        if (getActivePower() == PowerIndex.EXTRA && self instanceof Player pl){
            if (getPlayerPos2() == PlayerPosIndex.CHARGE_SHOT) {

                self.level().playSound((Player)null, self.getX(), self.getY(), self.getZ(), ModSounds.COLD_SHOT_EVENT,
                        SoundSource.NEUTRAL, 1F, (float)(1F+Math.random()*0.08f));
                if (!self.level().isClientSide) {
                    ColdBlastProjectile bubble = new ColdBlastProjectile(self,self.level());
                    bubble.absMoveTo(self.getX(), self.getY(), self.getZ());
                    bubble.setUser(self);
                    bubble.setOwner(self);
                    bubble.shootThis(pl);
                    self.level().addFreshEntity(bubble);
                }
            }
        }

        if (((StandUser) self).roundabout$isGuardInput()) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }


    public void freezeBlocksServer(){
        int cooldown = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezeBlocksCooldown;
        this.setCooldown(PowerIndex.SKILL_4_SNEAK, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL && !PL.isInWater()){
            if (MainUtil.getIsGamemodeApproriateForGrief(PL)){
                int radius = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.blockFreezeRadius;
                BlockPos center = self.blockPosition();
                boolean canFreezeWater = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezesSurfaceWater;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {

                            // Compare squared distances (faster than sqrt)
                            if (x * x + y * y + z * z > radius * radius) {
                                continue;
                            }

                            BlockPos pos = center.offset(x, y, z);

                            BlockState state = self.level().getBlockState(pos);

                            if (canFreezeWater && state.is(Blocks.WATER) &&
                                    self.level().getBlockState(pos.above()).isAir()){
                                if (self.level().isUnobstructed(Blocks.ICE.defaultBlockState(), pos, CollisionContext.empty())) {
                                    if (self.level().isUnobstructed(Blocks.ICE.defaultBlockState(),
                                            pos.above(), CollisionContext.empty())) {
                                            if (!(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, pos))) {
                                                self.level().setBlock(
                                                        pos,
                                                        Blocks.ICE.defaultBlockState(),
                                                        Block.UPDATE_ALL
                                                );
                                            }

                                    }
                                }
                            } else {
                                Block replacement = MainUtil.FREEZABLE_BLOCKS.get(state.getBlock());

                                if (replacement != null) {
                                    if (!(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, pos))) {
                                        self.level().setBlock(
                                                pos,
                                                replacement.defaultBlockState(),
                                                Block.UPDATE_ALL
                                        );
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                ItemStack stack = self.getItemBySlot(EquipmentSlot.MAINHAND);

                if (stack.getItem() instanceof BlockItem blockItem
                        && MainUtil.FREEZABLE_BLOCK_ITEMS.containsKey(blockItem.getBlock())) {

                    Block frozen = MainUtil.FREEZABLE_BLOCK_ITEMS.get(blockItem.getBlock());

                    self.setItemSlot(
                            EquipmentSlot.MAINHAND,
                            new ItemStack(frozen)
                    );
                }
            }

            if (self.level() instanceof ServerLevel sl) {
                sl.sendParticles(ModParticles.COLD_CRACKLE,
                        self.getEyePosition().x,
                        self.getEyePosition().y,
                        self.getEyePosition().z,
                        0, 0, 0, 0, 0);

            }
            addEXP(1);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BLOCK_FREEZE_EVENT,
                    SoundSource.PLAYERS, 1F, 1F);
        }
    }

    int graceticks = 0;
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            boolean upAiNow = upAi(attackTarget);
            double dist = attackTarget.distanceTo(this.getSelf());
            boolean isCreeper = this.getSelf() instanceof Creeper;
            if (graceticks <= 0) {
                if (isCreeper) {
                    if (dist <= 6) {
                        if (!onCooldown(PowerIndex.SKILL_3)){
                            iceWallMob(attackTarget,self.getDirection());
                            iceWallMob(attackTarget,self.getDirection().getClockWise());
                            iceWallMob(attackTarget,self.getDirection().getCounterClockWise());
                        }
                    }
                } else {
                    if (dist <= 5) {
                        if (upAiNow && !onCooldown(PowerIndex.SKILL_2_SNEAK) && ((self.getHealth() < (self.getMaxHealth()/2))
                        || (!attackTarget.getUseItem().isEmpty() && (attackTarget.getUseItem().is(Items.BOW)
                                || attackTarget.getUseItem().is(Items.CROSSBOW)
                                || attackTarget.getUseItem().getItem() instanceof FirearmItem
                            ))
                        )){
                            tryPower(PowerIndex.POWER_2_SNEAK,true);
                            graceticks = 20;
                        } else if (!onCooldown(PowerIndex.SKILL_2)) {
                            tryBlockPosPower(PowerIndex.POWER_2,true,attackTarget.getOnPos());
                            graceticks = 120;
                        } else if (upAiNow && !onCooldown(PowerIndex.SKILL_3)){
                            iceWallMob(attackTarget,self.getDirection());
                            graceticks = 20;
                        }
                    }
                }
            } else {
                graceticks--;
            }
        }
        //tryBlockPosPowerPacket(PowerIndex.POWER_2,hit.getBlockPos());
    }


    public void iceWallMob(Entity relativeEntity, Direction direction){
        int cooldown = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.iceWallCooldown;
        this.setCooldown(PowerIndex.SKILL_3, cooldown);
        if (!this.self.level().isClientSide()){

            Direction facing = direction;
            BlockPos centerPos = relativeEntity.getOnPos().relative(facing).relative(facing);

            boolean isSafe = false;
            for (var i = 0; i < 5; i++) {
                if (self.level().getBlockState(centerPos.below(i)).isSolid()){
                    centerPos = centerPos.below(i);
                    isSafe = true;
                    i = 10;
                }
            }

            if (isSafe) {
                centerPos = new BlockPos(centerPos.getX(), Math.min(centerPos.getY(), self.blockPosition().getY()),
                        centerPos.getZ());

// Left/right axis
                Direction side = facing.getClockWise();
                sideX = side;
                storeCenter = centerPos;
                for (int width = -1; width <= 1; width++) {
                    for (int height = 0; height < 2; height++) {

                        BlockPos spawnPos = centerPos
                                .relative(side, width)
                                .above(height);

                        Vector3f newVec = new Vector3f((float) (spawnPos.getX()+0.5),
                                (float) (spawnPos.getY()),
                                (float) (spawnPos.getZ() + 0.5)).add(0, -1, 0);

                        BlockWallEntity wall =
                                // slightly off to not z-fight
                                new BlockWallEntity(
                                        self.level(),
                                        newVec.x,
                                        newVec.y,
                                        newVec.z,
                                        Blocks.PACKED_ICE.defaultBlockState()
                                );
                        wall.setDataFinalPos(newVec.add(0, 2, 0));
                        wall.timing = 200;
                        wall.canGrief = MainUtil.getIsGamemodeApproriateForGrief(self);
                        addIceEntity(wall);
                        self.level().addFreshEntity(wall);
                    }
                }
                addEXP(1);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.ICE_RISES_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
        }
    }

    public BlockPos twisterPos = BlockPos.ZERO;
    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos pos) {
        twisterPos = pos;
        return super.tryBlockPosPower(move, forced,pos);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        return tryPower(move, forced);
    }


    @Override
    public boolean tryPower(int move, boolean forced) {

        if (isChargingCold()) {
            stopSoundsIfNearby(ICE_CHARGE, 100, false);
        }
        return super.tryPower(move, forced);
    }



    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }

    public static final byte
            BASE = 1,
            BETA =2,
            FIERCE =3,
            BLACK =4,
            SHADE =5,
            KNIGHT =6,
            ICEOLOGER =7,
            STRAY =8,
            FRIGID =9,
            MANGA =10,
            YUKI =11;


    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(BASE);
        $$1.add(MANGA);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(FIERCE);
                $$1.add(KNIGHT);
            } if (Level > 2 || bypass){
                $$1.add(SHADE);
                $$1.add(BLACK);
            } if (Level > 3 || bypass){
                $$1.add(ICEOLOGER);
                $$1.add(STRAY);
            } if (Level > 4 || bypass){
                $$1.add(FRIGID);
                $$1.add(BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(YUKI);
            }
        }
        return $$1;
    }


    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.white_album."+getSkinString(skinId));
    }
    public static Component getSkinNameStatic(byte skinId) {
        return Component.translatable("skins.roundabout.white_album."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            case BETA -> "beta";
            case BLACK -> "black";
            case FIERCE -> "dramatic";
            case KNIGHT -> "knight";
            case STRAY -> "stray";
            case FRIGID -> "frigid";
            case ICEOLOGER -> "iceologer";
            case SHADE -> "shade";
            case MANGA -> "manga";
            case YUKI -> "yuki";
            default -> "base";
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.WHITE_ALBUM_SUMMON_EVENT;
            }
            case ICE_CHARGE -> {
                return ModSounds.ICE_BLAST_CHARGE_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }


    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 4 && isHoldingSneak() && self.isInWater()){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            PLACE = 61,
            RETRACT = 62,
            SHOCK = 63;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.toggle_brawl",
                "instruction.roundabout.press_skill", StandIcons.SUIT_COMBAT_2,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+99,0, "ability.roundabout.cold_punch",
                "instruction.roundabout.press_attack", StandIcons.WHITE_PUNCH,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.cold_blast",
                "instruction.roundabout.hold_block", StandIcons.COLD_BLAST,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.cold_guard",
                "instruction.roundabout.passive", StandIcons.COLD_GUARD,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.freeze_cancel",
                "instruction.roundabout.press_skill_crouch", StandIcons.FREEZE_CANCEL,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,getTwisterLevel(), "ability.roundabout.ice_twister",
                "instruction.roundabout.press_skill", StandIcons.TWISTER,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,getGentlyWeepsLevel(), "ability.roundabout.gently_weeps",
                "instruction.roundabout.press_skill_crouch", StandIcons.GENTLY_WEEPS,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,getIceWallLevel(), "ability.roundabout.ice_wall",
                "instruction.roundabout.press_skill_crouch", StandIcons.ICE_WALL,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,getIceWallLevel(), "ability.roundabout.ice_wall_behind",
                "instruction.roundabout.press_skill", StandIcons.ICE_WALL_BEHIND,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,getSkatesLevel(), "ability.roundabout.skate_activate",
                "instruction.roundabout.press_skill", StandIcons.SKATE_ACTIVE,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,getBlockFreezeLevel(), "ability.roundabout.block_freeze",
                "instruction.roundabout.press_skill_crouch", StandIcons.FREEZE_BLOCKS,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.suit_power",
                "instruction.roundabout.passive", StandIcons.SUIT_POWER,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.full_accel",
                "instruction.roundabout.passive", StandIcons.FULL_ACCEL,0,level,bypass));

        return $$1;
    }



    public List<Entity> iceEntities = new ArrayList<>();

    public void addIceEntity(Entity che){
        iceEntityInit();
        iceEntities.add(che);
    }
    public void iceEntityInit(){
        if (iceEntities == null) {
            iceEntities = new ArrayList<>();
        }
    }
    public void clearAllIceEntities(){
        iceEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(iceEntities) {
        };
        if (!iceEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                iceEntities.remove(value);
                if (value instanceof BlockWallEntity bwe){
                    bwe.breakAndDiscard();
                } else {
                    value.discard();
                }
            }
        }
    }

    public void ticklIceEntities(){
        iceEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(iceEntities) {
        };
        if (!iceEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                if (value.isRemoved() || !(value.isAlive())){
                    iceEntities.remove(value);
                }
            }
        }
    }

    @Override
    public boolean setPowerAttack(){
        setAttack();
        return false;
    }
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (activePowerPhase == 0){
                if (isBrawling()) {
                    if (!isChargingCold() &&
                            !isBarraging()) {
                        this.tryPower(PowerIndex.ATTACK);
                    }
                }
            }
        }
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (activePowerPhase == 0){
                if (isBrawling()) {

                }
            }
        }
    }

    public int getWhiteAlbumChargeLength(){
        return 35;
    }

    public boolean hasColdCharged(){
        return attackTimeDuring > getWhiteAlbumChargeLength();
    }

    public void playDing(){
        if (isChargingCold() && attackTimeDuring == getWhiteAlbumChargeLength()){
            this.self.playSound(ModSounds.DING_EVENT, 1F, (float) (1.1F+Math.random()*0.03F));
        }
    }

    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean powerOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        if (powerOn && isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (isChargingCold()) {
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            if (hasColdCharged()){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, 15, 6);
            } else {
                int ClashTime =  Math.round(attackTimeDuring / Math.max(getWhiteAlbumChargeLength(),attackTimeDuring) * 15);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 104, ClashTime, 6);
            }
        } else if (powerOn && isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null && isBrawling()) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);

                }
            }
            if (powerOn && isBrawling()) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }

    @Override
    public float getBrawlPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            if (!MainUtil.canFreeze(entity)){
                return 0.93F;
            }
            return 0.8F;
        } else {
            if (!MainUtil.canFreeze(entity)){
                return 2.5F;
            }
            return 2.2F;
        }
    }

    @Override
    public void playSummonEffects(boolean forced){
        if (!forced) {
            Level lv = this.getSelf().level();
            Holder<Biome> theBiome = lv.getBiome(this.getSelf().getOnPos());
            if ((this.getSelf()) instanceof Player PE
                    &&
                    (theBiome.is(Biomes.ICE_SPIKES) ||
                    theBiome.is(Biomes.SNOWY_PLAINS) ||
                    theBiome.is(Biomes.SNOWY_BEACH) ||
                            theBiome.is(Biomes.SNOWY_TAIGA) ||
                            theBiome.is(Biomes.SNOWY_SLOPES)
                    )
            ){
                StandUser user = ((StandUser)PE);
                ItemStack stack = user.roundabout$getStandDisc();
                if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_WHITE_ALBUM)){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    if (!ipe.roundabout$getUnlockedBonusSkin()){
                        if (!lv.isClientSide()) {
                            ipe.roundabout$setUnlockedBonusSkin(true);
                            lv.playSound(null, PE.getX(), PE.getY(),
                                    PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                            ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                    PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.2);
                            user.roundabout$setStandSkin(YUKI);
                            ((ServerPlayer) ipe).displayClientMessage(
                                    Component.translatable("unlock_skin.roundabout.white_album.yuki"), true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addToCombo(Entity targ){
        if (targ instanceof LivingEntity LV){
            addEXP(1,LV);
        }
        if (targ instanceof Player PL){
            int heat = HeatUtil.getHeat(PL);
            HeatUtil.addHeat(PL, -3);
        } else if (targ instanceof LivingEntity LE){
            HeatUtil.addHeat(LE,-13);
        }
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active");
    }

    //2- Ice Wall
    //3- Twister
    //4- Block Freeze
    //5- Gently Weeps
    @Override
    public byte getMaxLevel(){
        return 5;
    }

    public int getSkatesLevel(){return 1;}
    public int getIceWallLevel(){return 2;}
    public int getTwisterLevel(){return 3;}
    public int getBlockFreezeLevel(){return 4;}
    public int getGentlyWeepsLevel(){return 5;}

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1) {
            amt = 50;
        } else {
            amt = (100+((currentLevel-1)*150));
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }
    @Override
    public void gainExpFromStandardMining(BlockState $$1, BlockPos $$2) {
        if (!($$1.getBlock() instanceof IceBlock) && !$$1.is(Blocks.PACKED_ICE) &&
                !($$1.getDestroySpeed(this.self.level(),$$2) < 0.5) && MainUtil.isBlockExpAble($$1)) {
            if (Math.random() > 0.62) {
                addEXP(1);
            }
        }
    }
    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 5){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    @Override
    public boolean interceptSuccessfulDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if ((hasStandActive(this.getSelf()) && $$0.is(DamageTypes.PLAYER_ATTACK))){
            addEXP(2,target);
        }
        return false;
    }
}